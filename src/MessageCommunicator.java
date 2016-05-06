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
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	void sendMessage(String message)
	{
		try
		{
			byte[] encryptedMsg = formatMessage(encryptor.encrypt(message));

			out.write(encryptedMsg);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return !message.equals("")
		       ? message
		       : null;
	}
	private byte[] formatMessage(byte[] message)
	{
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt(message.length);

		return ArrayUtils.addAll(bb.array(), message);
	}
}
