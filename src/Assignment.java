import java.util.Scanner;

class Assignment
{
	static void assignment1(Scanner scanner) throws Exception
	{
		System.out.print("Enter the desired database size:\n> ");

		int      size     = scanner.nextInt();
		Database db       = new Database(size);
		Paillier paillier = new Paillier(Config.modLength);
		long     tic      = System.currentTimeMillis();

		db.encryptDatabase(paillier);

		long toc = System.currentTimeMillis();

		System.out.println(String.format("%1$d "
			+ (size == 1 ? "entry" : "entries") +
			" encrypted in %2$d ms", size, toc - tic));
	}
}
