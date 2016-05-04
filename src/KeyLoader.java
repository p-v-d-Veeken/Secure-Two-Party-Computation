import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class KeyLoader
{
	private static final String keyFileFormat = "keys/public_key_";
	private static final String keyFileExtension = ".pem";

	static byte[] getKey(String nodeName)
	{
		String key = "";
		try
		{
			File    file    = new File(keyFileFormat + nodeName + keyFileExtension);
			Scanner scanner = new Scanner(file);

			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				key += !line.equals("-----BEGIN PUBLIC KEY-----") && !line.equals("-----END PUBLIC KEY-----")
				       ? line
			          : "";
			}
		}
		catch(IOException e) { e.printStackTrace(); }

		return Base64.decode(key);
	}
}
