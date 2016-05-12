import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

class MessageCommunicator
{
	private Encryptor        encryptor;
	private DataOutputStream out;
	private Socket           socket;
	private SecureRandom     random;

	MessageCommunicator()
	{
		random = new SecureRandom();

		try
		{
			socket = new Socket(Config.host, Config.port);
			encryptor = new Encryptor();
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			System.out.println("Can not connect to mixnet");
			System.exit(1);
		}
	}
	void sendMessage(String recipient, String message)
	{
		this.sendMessage(recipient, message, Arrays.asList("Cache", "C", "B", "A"));
	}
	//By specifying a node which is not in Config.nodes, a message can be send which will trigger an error in the mixnet
	void sendMessage(String recipient, String message, List<String> nodes)
	{
		try
		{
			byte[] encryptedMsg = encryptor.encrypt(message, recipient, nodes);
			out.write(encryptedMsg);
			out.flush();
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	String randomMessage(int index)
	{
		return String.valueOf(index) + "-" + new BigInteger(130, random).toString(32);
	}
	void close()
	{
		try
		{
			out.flush();
			out.close();
			socket.close();

			System.out.println("Connection to mixnet closed");
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	boolean isConnected()
	{
		return this.socket.isConnected();
	}
}