import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

class MessageCommunicator
{
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
		}
		catch(IOException e)	{ e.printStackTrace(); }
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