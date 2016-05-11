import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.TreeMap;

class CacheAndCommandLog
{
	private URL                     cacheUrl;
	private TreeMap<String, String> log;
	private int                     cachePtr;
	private int                     counter;

	CacheAndCommandLog()
	{
		try
		{
			cacheUrl = new URL("http://" + Config.host + ":" + Config.envPort + "/" + Config.cacheLog);
			log = new TreeMap<>(new Comparator<String>(){
				@Override
				public int compare(String o1, String o2)
				{
					Integer i1 = Integer.parseInt(o1.split("\\.")[0]);
					Integer i2 = Integer.parseInt(o2.split("\\.")[0]);

					return Integer.compare(i1, i2);
				}
			});
			cachePtr = 0;
			counter = 0;
		}
		catch(MalformedURLException e) { e.printStackTrace(); }
	}
	void addCommand(String command)
	{
		if(command.equals("log")) { return; }

		counter++;
		String content = getNewContent();
		log.put(counter + ". " + command, content);
	}
	private String getNewContent()
	{
		String content = "";
		try
		{
			BufferedReader br = new BufferedReader(
				new InputStreamReader(cacheUrl.openStream())
			);
			String line;
			int    i;

			for(i = 0; (line = br.readLine()) != null; i++)
			{
				content += i >= cachePtr
				           ? (i + 1) + ". " + line + "\n"
				           : "";
			}
			cachePtr = i;
			br.close();
		}
		catch(IOException e) { e.printStackTrace(); }
		return content;
	}
	void dumpLog()
	{
		log.entrySet()
			.forEach(e -> printEntry(e.getKey(), e.getValue()));
	}
	private void printEntry(String key, String value)
	{
		System.out.print(key);
		for(int i = 0; i < 10 - key.length(); i ++)
		{ System.out.print(" "); }
		System.out.print("-> ");

		String[] entries = value.split("\n");

		for(int i = 0; i < entries.length; i++)
		{
			if(i != 0) { System.out.print("             "); }
			System.out.println(entries[i]);
		}
	}
}