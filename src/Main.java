import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class Main
{
	private static final BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
	{
		MessageCommunicator msgCom      = new MessageCommunicator();
		Assignments         assignments = new Assignments(cin, msgCom);
		Boolean             running     = true;

		System.out.println("Connected to mixnet");
		System.out.println(Config.commands);
		System.out.print("Command: ");

		while(running)
		{
			if(!msgCom.isConnected())
			{   running = false; }

			String input = cin.readLine();

			if(input != null)
			{
				String[] inputArg = input.split(" ");
				switch(inputArg[0]) //These are all possible commands which can be entered into the console
				{
					case "send":
						msgCom.sendMessage(inputArg[1], inputArg[2]);
						break;
					case "assignment":
						switch(Integer.parseInt(inputArg[1]))
						{
							case 1:
								assignments.assignment1();
								break;
							case 2:
								assignments.assignment2();
								break;
							case 3:
								assignments.assignment3();
								break;
							default:
								System.out.println("command not recognized");
						}
						break;
					case "stop":
						running = false;
						break;
					case "reload":
						msgCom = new MessageCommunicator();
						assignments = new Assignments(cin, msgCom);
						System.out.println("Reloaded and connected to mixnet");
						break;
					default:
						System.out.println("command not recognized");
						System.out.println(Config.commands);
				}
				System.out.print("Command: ");
			}
		}
		msgCom.close();
	}
}