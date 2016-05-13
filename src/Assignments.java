import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Assignments
{
	private BufferedReader      cin;
	private MessageCommunicator msgCom;

	Assignments(BufferedReader cin, MessageCommunicator msgCom)
	{
		this.cin = cin;
		this.msgCom = msgCom;
	}
	void assignment1() throws InterruptedException
	{
		msgCom.sendMessage("TIM", "4003047-4095812");

		for(int i = 1; i < 10; i++) //Send messages with increasing numbers
		{
			for(int j = 0; j < i; j++)
			{
				msgCom.sendMessage("TEST" + i, msgCom.randomMessage(j));
			}
			Thread.sleep(10000);
		}
	}
	void assignment2() throws InterruptedException, IOException
	{
		Boolean running = true;
		System.out.println("Enter the name of the node up to which you want to use the correct key (A, B, C or Cache)" +
			", or stop");
		Map<String, Integer> map = new HashMap<>(); //Keeps track of how many messages have been sent to a node
		CacheAndCommandLog   log = new CacheAndCommandLog();

		while(running)
		{
			String       input  = cin.readLine();
			String       target = null;
			List<String> nodes  = null;

			switch(input.toLowerCase())
			{
				case "a":
					target = "A"; //See MessageCommunicator.sendMessage and Encryptor.encrypt (line 73)
					nodes = Arrays.asList("X", "X", "X", "A");
					break;
				case "b":
					target = "B";
					nodes = Arrays.asList("X", "X", "B", "A");
					break;
				case "c":
					target = "C";
					nodes = Arrays.asList("X", "C", "B", "A");
					break;
				case "cache":
				case "cc":
					target = "Cache";
					nodes = Arrays.asList("Cache", "C", "B", "A");
					break;
				case "stop":
					running = false;
					break;
				case "log":
					log.dumpLog();
					break;
				default:
					System.out.println("command not recognized");
			}
			if(map.containsKey(target))
			{  map.put(target, map.get(target) + 1); }
			else
			{  map.put(target, 1); }
			if(target != null)
			{  this.msgCom.sendMessage("TEST" + target, this.msgCom.randomMessage(map.get(target)), nodes); }

			Thread.sleep(1000); //To ensure the message has reached the mixnet and has been entered into the cache log
			log.addCommand(input); //This keeps track of the sent commands and the corresponding changes in the log.
		}
	}
	void assignment3() throws InterruptedException
	{
		for(int i = 1; i < 1000; i++) //Flood the nodes
		{
			msgCom.sendMessage("FAKE" + i, msgCom.randomMessage(i ));
			Thread.sleep(100);
		}
	}
}