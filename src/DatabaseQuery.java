import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
		List<DatabaseEntry> result = new ArrayList<>();

		for(DatabaseEntry entry : database.getEntries())
		{
			SecureComparison comp  = new SecureComparison(paillier);
			BigInteger       dbVal = entry.get(column);
			BigInteger       res   = paillier.decrypt(comp.compare(dbVal, value, Config.maxBitLength));

			if(res.equals(BigInteger.ONE))
			{
				result.add(entry);
			}
		}
		return result;
	}
}
