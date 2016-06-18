import java.math.BigInteger;
import java.util.Scanner;

class Assignment
{
	static void assignment1(Scanner scanner) throws Exception
	{
		System.out.print("Enter the desired database size:\n> ");

		int      size     = scanner.nextInt();
		Database db       = new Database(size);
		Paillier paillier = new Paillier(Config.modLength);
		long     tick     = System.currentTimeMillis();

		db.encryptDatabase(paillier);

		long tock = System.currentTimeMillis();

		System.out.println(String.format("%1$d "
			+ (size == 1
			   ? "entry"
			   : "entries") +
			" encrypted in %2$d ms", size, tock - tick));
	}
	static void assignment2(Scanner scanner) throws Exception
	{
		Paillier         paillier = new Paillier(Config.modLength);
		SecureComparison comp     = new SecureComparison(paillier);
		BigInteger       a, b, res;
		long             tick, tock;
		int              l;
		String           msg;

		System.out.print("Enter an integer value for a:\n> ");

		a = BigInteger.valueOf(scanner.nextInt());

		System.out.print("Enter an integer value for b:\n> ");

		b = BigInteger.valueOf(scanner.nextInt());
		l = Integer.max(a.bitLength(), b.bitLength());
		tick = System.currentTimeMillis();
		res = comp.compare(paillier.encrypt(a), paillier.encrypt(b), l);
		tock = System.currentTimeMillis();
		res = paillier.decrypt(res);
		msg = res.equals(BigInteger.ONE)
		      ? "Computed that a (%1$d) > b (%2$d) in %3$d ms"
		      : "Computed that a (%1$d) <= b (%2$d) in %3$d ms";

		System.out.println(String.format(msg, a, b, tock - tick));
	}
}