import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database
{
	private List<DatabaseEntry> db;

	public Database(int size)
	{
		db = new ArrayList<>(size);

		for(int i = 0; i < size; i++)
		{
			DatabaseEntry entry = new DatabaseEntry();

			db.add(entry);
		}
		int a = 0;
	}
	public void encryptDatabase(Paillier paillier)
	{
		int  a = 0;
		db = db.stream()
			.map(entry -> DatabaseEntry.encryptEntry(entry, paillier))
			.collect(Collectors.toList());
	}
}
