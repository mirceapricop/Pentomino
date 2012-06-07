package pentogame.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import pentogame.objects.Board;
import pentogame.objects.PentoObject;
import pentogame.objects.Target;

public class BoardModel implements ObjectModel {

	public Board board;
	public ArrayList<Target> targets;
	
	@Override
	public void loadObjects() throws FileNotFoundException {
		Map data = (Map)new Yaml().load(new FileInputStream("board.yml"));
		board = new Board();
		board.width = (Integer) data.get("width");
		board.height = (Integer) data.get("height");
		board.grid_size = (Integer) data.get("grid_size");
		
		String[] field_rows = ((String) data.get("fields")).split("\n");
		
		board.field_rows = field_rows.length;
		board.field_cols = field_rows[0].length();
		
		board.fields = new char[board.field_rows][board.field_cols];
		for(int i=0;i<board.field_rows;i++) {
			String current_row = field_rows[i];
			for(int j=0;j<board.field_cols;j++) {
				board.fields[i][j] = current_row.charAt(j);
			}
		}
		
		targets = new ArrayList<Target>();
		for(Map target_description : (Iterable<Map>)data.get("targets")) {
			Target new_target = new Target();
			new_target.id = (Integer)target_description.get("id");
			new_target.label = (String)target_description.get("label");
			new_target.top = (Integer)target_description.get("top");
			new_target.left = (Integer)target_description.get("left");
			targets.add(new_target);
		}
	}
	
	public String toString() {
		return "Board Model";
	}

	@Override
	public String getType() {
		return "board";
	}

	@Override
	public PentoObject getObject() {
		return board;
	}

	@Override
	public ArrayList<PentoObject> getObjects() {
		return null;
	}

	@Override
	public PentoObject getById(String id) {
		return getObject();
	}
	
	public Target getTargetByLabel(String label) {
		for(Target t : targets) {
			if(t.label.toLowerCase().equals(label.toLowerCase())) {
				return t;
			}
		}
		return null;
	}

}
