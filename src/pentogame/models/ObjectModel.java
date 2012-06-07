package pentogame.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import pentogame.objects.PentoObject;

public interface ObjectModel {
	
	public void loadObjects() throws FileNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException;
	public String getType();
	public PentoObject getObject();
	public PentoObject getById(String id);
	public ArrayList<PentoObject> getObjects();
}
