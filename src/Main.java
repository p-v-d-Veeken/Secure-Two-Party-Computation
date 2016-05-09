import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		Assignments assignments = new Assignments(msgCom);

		int assignment = 2;

		Assignments.class.getMethod("assignment" + assignment).invoke(assignments);

		msgCom.close();
	}

	private static class Assignments {

		private MessageCommunicator msgCom;

		public Assignments(MessageCommunicator msgCom) {
			this.msgCom = msgCom;
		}

		public void assignment1() throws InterruptedException {
			msgCom.sendMessage("TIM", "4003047-4095812");

			for (int i = 1; i < 10; i++) {
				for (int j = 0; j < i; j++) {
					msgCom.sendMessage("TEST" + i, msgCom.randomMessage(j));
				}
				Thread.sleep(10000);
			}
		}

		public void assignment2() throws InterruptedException {
			String timestamp = new Date().toString();
			for (int i = 0; i < 7; i++) {
				msgCom.sendMessage("TEST" + i, timestamp);
			}
		}

		public void assignment3() throws InterruptedException {
			//TODO
		}
	}


}
