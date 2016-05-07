import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class MessageCommunicator
{
	private Encryptor        encryptor;
	private DataOutputStream out;
	private BufferedReader   in;

	MessageCommunicator()
	{
		try
		{
			Socket socket = new Socket(Config.host, Config.port);
			System.out.println(socket.isConnected());

			encryptor = new Encryptor();
			out = new DataOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(IOException e)	{ e.printStackTrace(); }
	}
	void sendMessage(String recipient, String message)
	{
		try
		{
			byte[] encryptedMsg = formatMessage(recipient.getBytes(), encryptor.encrypt(message));

			out.write(encryptedMsg);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	String receiveMessage()
	{
		String message = "";
		String line;

		try
		{
			while((line = in.readLine()) != null)
			{
				message += line;
			}
		}
		catch(IOException e) { e.printStackTrace(); }
		return !message.equals("")
		       ? message
		       : null;
	}
	private byte[] formatMessage(byte[] recipient, byte[] message)
	{
		ByteBuffer bbLen = ByteBuffer.allocate(4);
		ByteBuffer bbRcp = ByteBuffer.allocate(8);
		bbRcp.put(recipient);
		bbLen.order(ByteOrder.BIG_ENDIAN);
		bbLen.putInt(message.length + 8);

		return ArrayUtils.addAll( //length|recipient|message
			bbLen.array(),
			ArrayUtils.addAll(bbRcp.array(), message)
		);
	}
}