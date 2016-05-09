public class Main
{
	public static void main(String[] args) throws InterruptedException {
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		msgCom.sendMessage("TIM", "4003047-4095812");

		for (int i = 1; i < 10; i++) {
			for (int j = 0; j < i; j++) {
				msgCom.sendMessage("TEST" + i, msgCom.randomMessage(j));
			}
			Thread.sleep(10000);
		}

		msgCom.close();
	}
}
