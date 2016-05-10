import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		Assignments assignments = new Assignments(msgCom);

		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		String commands = "Commands:\n\tsend <recipient> <message>:\tSend a message\n\tassignment <number>:\t\tCall assignment code\n\tstop:\t\t\t\t\t\tStop the application";
		System.out.println(commands);
		System.out.print("Command: ");
		Boolean running = true;
		while(running) {
			String input = cin.readLine();
			if (input != null) {
				String[] inputArg = input.split(" ");
				switch(inputArg[0]) {
					case "send":
						msgCom.sendMessage(inputArg[1], inputArg[2]);
						break;
					case "assignment":
						Assignments.class.getMethod("assignment" + Integer.parseInt(inputArg[1])).invoke(assignments);
						break;
					case "stop":
						running = false;
						break;
					default:
						System.out.println("command not recognized");
						System.out.println(commands);
				}
				System.out.print("Command: ");
			}
		}

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
