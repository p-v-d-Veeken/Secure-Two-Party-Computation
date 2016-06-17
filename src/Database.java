import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Database
{
	private List<DatabaseEntry> db;

	Database(int size)
	{
		db = new ArrayList<>(size);

		for(int i = 0; i < size; i++)
		{
			DatabaseEntry entry = new DatabaseEntry();

			db.add(entry);
		}
	}
	void encryptDatabase(Paillier paillier)
	{
		db = db.stream()
			.map(entry -> DatabaseEntry.encryptEntry(entry, paillier))
			.collect(Collectors.toList());
	}
}