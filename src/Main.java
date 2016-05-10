import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static BufferedReader cin;

	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		Assignments assignments = new Assignments(msgCom);

		cin = new BufferedReader(new InputStreamReader(System.in));
		String commands = "Commands:\n\tsend <recipient> <message>:\tSend a message\n\tassignment <number>:\t\tCall assignment code\n\tstop:\t\t\t\t\t\tStop the application";
		System.out.println(commands);
		System.out.print("Command: ");
		Boolean running = true;
		while(running) {
            if (!msgCom.isConnected()) {
                running = false;
            }
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

		public void assignment2() throws InterruptedException, IOException {
            Boolean running = true;
            System.out.println("Enter the name of the node up to which you want to use the correct key (A, B, C or Cache), or stop");
            Map<String, Integer> map = new HashMap<>();
            while(running) {
                String input = cin.readLine();
                if (map.containsKey(input)) {
                    map.put(input, map.get(input) + 1);
                } else {
                    map.put(input, 1);
                }
                switch(input) {
                    case "A":
                    case "a":
                        input = "A";
                        msgCom.sendMessage("TEST" + input, msgCom.randomMessage(map.get(input)), Arrays.asList("X", "X", "X", "A"));
                        break;
                    case "B":
                    case "b":
                        input = "B";
                        msgCom.sendMessage("TEST" + input, msgCom.randomMessage(map.get(input)), Arrays.asList("X", "X", "B", "A"));
                        break;
                    case "C":
                    case "c":
                        input = "C";
                        msgCom.sendMessage("TEST" + input, msgCom.randomMessage(map.get(input)), Arrays.asList("X", "C", "B", "A"));
                        break;
                    case "Cache":
                    case "cache":
                    case "CACHE":
                    case "cc":
                        input = "Cache";
                        msgCom.sendMessage("TEST" + input, msgCom.randomMessage(map.get(input)), Arrays.asList("Cache", "C", "B", "A"));
                        break;
                    case "STOP":
                        running = false;
                        break;
                    default:
                        System.out.println("command not recognized");
                }
            }
            for (String key : map.keySet()) {
                if (Arrays.asList("Cache", "C", "B", "A").contains(key)) {
                    System.out.println("Threshold for " + key + " is " + map.get(key));
                }
            }
		}

		public void assignment3() throws InterruptedException {
			//TODO
		}
	}


}
