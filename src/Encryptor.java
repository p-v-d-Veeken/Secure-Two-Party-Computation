import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class Encryptor
{
	private Map<String, byte[]> RSAKeys;
	private Map<String, byte[]> AESKeys;
	private Map<String, Cipher> RSACiphers;
	private Map<String, Cipher> AESCiphers;

	Encryptor()
	{
		Security.addProvider(new BouncyCastleProvider());

		RSAKeys = Arrays
			.stream(Config.nodes)
			.collect(Collectors.toMap(
				node -> node,
				this::getKey
			));
		AESKeys = generateAESKeys();
		RSACiphers = RSAKeys.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> createRSACipher(entry.getValue())
			));
		AESCiphers = AESKeys.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> createAESCipher(entry.getValue())
			));
	}
	byte[] encrypt(byte[] message, String toNode)
	{
		try
		{
			byte[] AESKey = AESKeys.get(toNode);
			byte[] IV     = Config.AESIV.getBytes();
			byte[] encryptedKey, encryptedMsg, encrypted;

			encryptedKey = RSACiphers.get(toNode).doFinal(ArrayUtils.addAll(AESKey, IV));
			encryptedMsg = AESCiphers.get(toNode).doFinal(message);
			encrypted = ArrayUtils.addAll(encryptedKey, encryptedMsg);

			return encrypted;
		}
		catch(IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }
		return new byte[]{};
	}
	byte[] encrypt(String message, String recipient)
	{
		ByteBuffer recipient_buffer = ByteBuffer.allocate(8);
		byte[] recipient_bytes = recipient.getBytes();
		for (int i = 0; i < 8; i++) {
			try {
				recipient_buffer.put(recipient_bytes[i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				recipient_buffer.put(" ".getBytes());
			}
		}
		System.out.println("sending message: " + new String(ArrayUtils.addAll(recipient_buffer.array(), message.getBytes()), StandardCharsets.UTF_8));
		byte[] messageCache = encrypt(ArrayUtils.addAll(recipient_buffer.array(), message.getBytes()), "Cache");
		byte[] messageC     = encrypt(messageCache, "C");
		byte[] messageB     = encrypt(messageC, "B");
		return formatMessage(encrypt(messageB, "A"));
	}
	private byte[] formatMessage(byte[] message)
	{
		ByteBuffer bbLen = ByteBuffer.allocate(4);
		bbLen.order(ByteOrder.BIG_ENDIAN);
		bbLen.putInt(message.length);

		return ArrayUtils.addAll( bbLen.array(), message );
	}
	private byte[] getKey(String nodeName)
	{
		String key = "";
		try
		{
			File    file    = new File(Config.keyFileFormat + nodeName + Config.keyFileExtension);
			Scanner scanner = new Scanner(file);

			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				key += !line.equals("-----BEGIN PUBLIC KEY-----") && !line.equals("-----END PUBLIC KEY-----")
				       ? line
				       : "";
			}
		}
		catch(IOException e) { e.printStackTrace(); }

		return Base64.decode(key);
	}
	private Map<String, byte[]> generateAESKeys()
	{
		Map<String, byte[]> AESKeys = new HashMap<>();

		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
			keyGen.init(128);
			AESKeys = RSAKeys.keySet()
				.stream()
				.collect(Collectors.toMap(
					entry -> entry,
					entry -> keyGen.generateKey().getEncoded()
				));
		}
		catch(NoSuchAlgorithmException | NoSuchProviderException e) {	e.printStackTrace(); }
		return AESKeys;
	}
	private Cipher createRSACipher(byte[] key)
	{
		try
		{
			KeyFactory         keyFactory = KeyFactory.getInstance("RSA", "BC");
			X509EncodedKeySpec KeySpec    = new X509EncodedKeySpec(key);
			RSAPublicKey       pubKey     = (RSAPublicKey) keyFactory.generatePublic(KeySpec);

			Cipher cipher = Cipher.getInstance(Config.RSAMode, "BC");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);

			return cipher;
		}
		catch(NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException | NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private Cipher createAESCipher(byte[] key)
	{
		try
		{
			Cipher        cipher = Cipher.getInstance(Config.AESMode, "BC");
			SecretKeySpec spec   = new SecretKeySpec(key, "AES");

			cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(Config.AESIV.getBytes()));

			return cipher;
		}
		catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}