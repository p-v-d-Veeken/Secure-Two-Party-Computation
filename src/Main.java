public class Main
{
	public static void main(String[] args)
	{
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		msgCom.sendMessage("TIM", "4003047");

		for (int i = 0; i < 100; i++) {
			msgCom.sendMessage("TEST", msgCom.randomMessage(i));
		}

		msgCom.close();
	}
}
