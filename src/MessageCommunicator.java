import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

class MessageCommunicator
{
	private BufferedReader 	 in;
	private Encryptor        encryptor;
	private DataOutputStream out;
	private Socket 			 socket;
	private SecureRandom 	 random = new SecureRandom();

	MessageCommunicator()
	{
		try
		{
			this.socket = new Socket(Config.host, Config.port);

			this.encryptor = new Encryptor();
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(IOException e)	{
			System.out.println("Can not connect to mixnet");
			System.exit(1);
		}
	}
	void sendMessage(String recipient, String message)
	{
		try
		{
			byte[] encryptedMsg = encryptor.encrypt(message, recipient);
			out.write(encryptedMsg);
			out.flush();
		}
		catch(IOException e) { e.printStackTrace(); }
	}

	public String randomMessage(int index) {
		return String.valueOf(index) + "-" + new BigInteger(130, random).toString(32);
	}

	public void listen() {
		this.in.lines().forEach(System.out::println);
	}

	public void close() {
		try {
			this.out.flush();
			this.out.close();
			this.socket.close();
			System.out.println("Connection to mixnet closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}