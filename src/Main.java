import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Main
{

	private static BufferedReader cin;

	public static void main(String[] args)
		throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException,
		IOException
	{
		MessageCommunicator msgCom = new MessageCommunicator();
		System.out.println("Connected to mixnet");
		cin = new BufferedReader(new InputStreamReader(System.in));
		String commands = "Commands:\n\tsend <recipient> <message>:\tSend a message\n\tassignment <number>:\t\tCall " +
			"assignment code\n\tstop:\t\t\t\t\t\tStop the application\n\treload:\t\t\t\t\t\tReload the application";
		System.out.println(commands);
		System.out.print("Command: ");
		Assignments assignments = new Assignments(msgCom);
		Boolean     running     = true;
		while(running)
		{
			if(!msgCom.isConnected())
			{
				running = false;
			}
			String input = cin.readLine();
			if(input != null)
			{
				String[] inputArg = input.split(" ");
				switch(inputArg[0])
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
						assignments = new Assignments(msgCom);
						System.out.println("Reloaded and connected to mixnet");
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

	private static class Assignments
	{

		private MessageCommunicator msgCom;

		public Assignments(MessageCommunicator msgCom)
		{
			this.msgCom = msgCom;
		}

		public void assignment1() throws InterruptedException
		{
			msgCom.sendMessage("TIM", "4003047-4095812");

			for(int i = 1; i < 10; i++)
			{
				for(int j = 0; j < i; j++)
				{
					msgCom.sendMessage("TEST" + i, msgCom.randomMessage(j));
				}
				Thread.sleep(10000);
			}
		}

		public void assignment2() throws InterruptedException, IOException
		{
			Boolean running = true;
			System.out.println("Enter the name of the node up to which you want to use the correct key (A, B, C or Cache)" +
				", or stop");
			Map<String, Integer> map = new HashMap<>();
			CacheAndCommandLog   log = new CacheAndCommandLog();
			while(running)
			{
				String       input  = cin.readLine();
				String       target = null;
				List<String> nodes  = null;
				switch(input)
				{
					case "A":
					case "a":
						target = "A";
						nodes = Arrays.asList("X", "X", "X", "A");
						break;
					case "B":
					case "b":
						target = "B";
						nodes = Arrays.asList("X", "X", "B", "A");
						break;
					case "C":
					case "c":
						target = "C";
						nodes = Arrays.asList("X", "C", "B", "A");
						break;
					case "Cache":
					case "cache":
					case "CACHE":
					case "cc":
						target = "Cache";
						nodes = Arrays.asList("Cache", "C", "B", "A");
						break;
					case "STOP":
					case "stop":
					case "Stop":
						running = false;
						break;
					case "log":
						log.dumpLog();
						break;
					default:
						System.out.println("command not recognized");
				}
				if(map.containsKey(target))
				{
					map.put(target, map.get(target) + 1);
				}
				else
				{
					map.put(target, 1);
				}
				if(target != null)
				{
					this.msgCom.sendMessage("TEST" + target, this.msgCom.randomMessage(map.get(target)), nodes);
				}
				Thread.sleep(1000);
				log.addCommand(input);
			}
			map.keySet().forEach(key -> {
				if(Arrays.asList("Cache", "C", "B", "A").contains(key))
				{
					System.out.println("Threshold for " + key + " is " + map.get(key));
				}
			});
		}

		public void assignment3() throws InterruptedException
		{
			//TODO
		}
	}

}