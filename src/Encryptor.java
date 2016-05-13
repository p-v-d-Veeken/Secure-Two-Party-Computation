import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class Encryptor
{
	private Map<String, byte[]> RSAKeys;
	private Map<String, byte[]> AESKeys;
	private Map<String, Cipher> RSACiphers;
	private Map<String, Cipher> AESCiphers;

	public Encryptor()
	{
		Security.addProvider(new BouncyCastleProvider());

		this.RSAKeys = Arrays
			.stream(Config.nodes)
			.collect(Collectors.toMap(
				node -> node,
				this::getRSAKey
			));
		this.AESKeys = this.RSAKeys.keySet()
			.stream()
			.collect(Collectors.toMap(
				entry -> entry,
				entry -> this.createAESKey()
			));
		this.RSACiphers = this.RSAKeys.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> createRSACipher(entry.getValue())
			));
		this.AESCiphers = this.AESKeys.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> createAESCipher(entry.getValue())
			));
	}
	byte[] encrypt(String message, String recipient, List<String> nodes)
	{
		String recipientStr  = recipient + StringUtils.repeat(" ", 8 - recipient.length());
		byte[] paddedMessage = ArrayUtils.addAll(recipientStr.getBytes(), message.getBytes());

		System.out.println("sending message: " + new String(paddedMessage, StandardCharsets.UTF_8));

		for(String node : nodes)
		{
			if(RSAKeys.containsKey(node))
			{  paddedMessage = encryptMessageForNode(paddedMessage, node); }
			else
			{  //This part is used to purposely trigger errors in the mixnet, by encrypting with erroneous keys
				byte[] randomAes = this.createAESKey();
				paddedMessage = encryptMessageForNode(paddedMessage, Triple.of(
					this.createAESCipher(randomAes), randomAes, this.createRSACipher(this.createRSAKey())));
			}
		}
		return formatMessage(paddedMessage);
	}
	private byte[] encryptMessageForNode(byte[] message, Triple<Cipher, byte[], Cipher> cipherData)
	{
		try
		{
			byte[] IV           = Config.AESIV.getBytes();
			byte[] AESKey       = cipherData.getMiddle();
			byte[] encryptedKey = cipherData.getRight().doFinal(ArrayUtils.addAll(AESKey, IV));
			byte[] encryptedMsg = cipherData.getLeft().doFinal(message);

			return ArrayUtils.addAll(encryptedKey, encryptedMsg);
		}
		catch(IllegalBlockSizeException | BadPaddingException e)
		{  e.printStackTrace(); }

		return new byte[]{};
	}
	private byte[] encryptMessageForNode(byte[] message, String toNode)
	{
		return this.encryptMessageForNode(
			message, Triple.of(AESCiphers.get(toNode), AESKeys.get(toNode), RSACiphers.get(toNode)));
	}
	private byte[] formatMessage(byte[] message) //Prepends the 4 bytes long length field to the message
	{
		ByteBuffer bbLen = ByteBuffer.allocate(4);
		bbLen.order(ByteOrder.BIG_ENDIAN);
		bbLen.putInt(message.length);

		return ArrayUtils.addAll(bbLen.array(), message);
	}
	private byte[] getRSAKey(String nodeName) //Reads the key for the specified node from the ./keys directory
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
		catch(IOException e)
		{  e.printStackTrace(); }

		return Base64.decode(key);
	}
	private byte[] createAESKey()
	{
		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
			keyGen.init(128);
			return keyGen.generateKey().getEncoded();
		}
		catch(NoSuchAlgorithmException | NoSuchProviderException e)
		{  e.printStackTrace(); }

		return new byte[]{};
	}
	private byte[] createRSAKey()
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
			keyGen.initialize(1024);
			KeyPair key = keyGen.generateKeyPair();
			return key.getPublic().getEncoded();
		}
		catch(Exception e)
		{  e.printStackTrace(); }

		return new byte[]{};
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
		catch(NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException |
			NoSuchProviderException e)
		{  e.printStackTrace(); }

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
		catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
			InvalidAlgorithmParameterException | NoSuchProviderException e)
		{  e.printStackTrace(); }

		return null;
	}
}