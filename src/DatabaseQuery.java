import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

class DatabaseQuery
{
	private Paillier paillier;
	private Database database;

	DatabaseQuery(Paillier paillier, Database database)
	{
		this.paillier = paillier;
		this.database = database;
	}
	List<DatabaseEntry> findGreaterThan(Config.column column, BigInteger value) throws Exception
	{
		if(column.equals(Config.column.NAME))
		{
			throw new Exception("Non-numeric column specified.");
		}
		return database.getEntriesStream()
			.filter(entry -> compare(entry.get(column), value))
			.collect(Collectors.toList());
	}
	private boolean compare(BigInteger a, BigInteger b)
	{
		try
		{
			SecureComparison comp = new SecureComparison(paillier);
			BigInteger       res  = paillier.decrypt(comp.compare(a, b, Config.maxBitLength));

			return res.equals(BigInteger.ONE);
		}
		catch(Exception e) { e.printStackTrace(); }

		return false;
	}
}