package pentogame.models.queries;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import pentogame.objects.PentoObject;

public interface Query {
	public PieceQuery where(String property, String value) throws FileNotFoundException;
	public ArrayList<PentoObject> fetch();
}
