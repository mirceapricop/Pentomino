package pentogame.models;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import pentogame.objects.HandCursor;
import pentogame.objects.PentoObject;

public class HandModel implements ObjectModel {
	
	private HandCursor _cursor;

	@Override
	public void loadObjects() throws FileNotFoundException,
			IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		_cursor = new HandCursor();
	}

	@Override
	public String getType() {
		return "hand";
	}

	@Override
	public PentoObject getObject() {
		return _cursor;
	}

	@Override
	public ArrayList<PentoObject> getObjects() {
		return null;
	}

	@Override
	public PentoObject getById(String id) {
		return getObject();
	}
	
	
}
