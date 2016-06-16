import java.math.BigInteger;
import java.util.Random;

public class Generator
{
	private final static Random random = new Random();

	public static BigInteger randName()
	{
		String name = "";

		for(int i = 0; i < Config.nameLength; i++)
		{
			char c = (char) (random.nextInt(26) + 'a');
			name += c;
		}
		return new BigInteger(name.getBytes());
	}
	public static BigInteger randAge()
	{
		int age = random.nextInt(Config.ageMax - Config.ageMin + 1) + Config.ageMin;

		return BigInteger.valueOf(age);
	}
	public static BigInteger randIncome()
	{
		int income = random.nextInt(Config.ageMax - Config.ageMin + 1) + Config.ageMin;

		return BigInteger.valueOf(income);
	}
}
