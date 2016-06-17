import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

class Database
{
	private List<DatabaseEntry> entries;

	Database(int size)
	{
		entries = new ArrayList<>(size);

		for(int i = 0; i < size; i++)
		{
			DatabaseEntry entry = new DatabaseEntry();

			entries.add(entry);
		}
	}
	void encryptDatabase(Paillier paillier)
	{
		entries = entries.stream()
			.map(entry -> DatabaseEntry.encryptEntry(entry, paillier))
			.collect(Collectors.toList());
	}
	Vector[] sendD(BigInteger d, Paillier paillier) throws Exception
	{
		d = paillier.decrypt(d);
		System.out.println(d.bitLength());

		return new Vector[1];
	}
}