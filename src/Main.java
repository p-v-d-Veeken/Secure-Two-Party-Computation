import java.util.Scanner;

public class Main
{
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws Exception
	{
		System.out.println("Enter the desired database size:");

		int      size     = scanner.nextInt();
		Database db       = new Database(size);
		Paillier paillier = new Paillier(Config.modLength);
		long     tic      = System.nanoTime();

		db.encryptDatabase(paillier);

		long toc = System.nanoTime();

		System.out.println(String.format("%1$d entries encrypted in %2$d ns", size, toc - tic));
	}
}
