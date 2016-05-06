public class Main
{
	public static void main(String[] args)
	{
		MessageCommunicator msgCom = new MessageCommunicator();
		msgCom.sendMessage("testtesttest");
		System.out.println(msgCom.receiveMessage());
	}
}
