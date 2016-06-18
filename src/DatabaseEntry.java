import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

class DatabaseEntry
{
	private BigInteger name;
	private BigInteger age;
	private BigInteger income;

	DatabaseEntry()
	{
		this.name = Generator.randName();
		this.age = Generator.randAge();
		this.income = Generator.randIncome();
	}
	static DatabaseEntry encryptEntry(DatabaseEntry entry, Paillier paillier)
	{
		try
		{
			entry.setName(paillier.encrypt(entry.getName()));
			entry.setAge(paillier.encrypt(entry.getAge()));
			entry.setIncome(paillier.encrypt(entry.getIncome()));
		}
		catch(Exception e) { e.printStackTrace(); }

		return entry;
	}
	BigInteger get(Config.column column)
	{
		BigInteger value = BigInteger.valueOf(-1);

		try
		{
			String columnName = column.toString().toLowerCase();
			String getterName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

			value = ((BigInteger) this.getClass().getDeclaredMethod(getterName).invoke(this));
		}
		catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return value;
	}
	BigInteger getIncome()
	{
		return income;
	}
	void setIncome(BigInteger income)
	{
		this.income = income;
	}
	BigInteger getName()
	{
		return name;
	}
	void setName(BigInteger name)
	{
		this.name = name;
	}
	BigInteger getAge()
	{
		return age;
	}
	void setAge(BigInteger age)
	{
		this.age = age;
	}
}