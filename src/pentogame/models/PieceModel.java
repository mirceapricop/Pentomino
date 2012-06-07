package pentogame.models;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import pentogame.models.queries.PieceQuery;
import pentogame.objects.PentoObject;
import pentogame.objects.Piece;

public class PieceModel implements ObjectModel {

	public ArrayList<PentoObject> _pieces;
	
	public PieceModel() {
		_pieces = new ArrayList<PentoObject>();
	}
	
	@Override
	public void loadObjects() throws FileNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		Map data = (Map)new Yaml().load(new FileInputStream("pento_pieces.yml"));
		int index = 0;
		for(Map piece_description : (Iterable<Map>)data.get("pieces")) {
			Piece new_piece = new Piece();
			new_piece.id = String.valueOf(index++);
			new_piece.type = (String) piece_description.get("type");
			new_piece.left = (Integer) piece_description.get("left");
			new_piece.top = (Integer) piece_description.get("top");
			
			String template_string = (String) ((Map) data.get("types")).get((String) piece_description.get("type"));
			String[] template_rows = template_string.split("\n");
			new_piece.template_rows = template_rows.length;
			new_piece.template_cols = template_rows[0].length();
			new_piece.template = new char[new_piece.template_rows][new_piece.template_cols];
			for(int i=0;i<new_piece.template.length;i++) {
				String current_row = template_rows[i];
				for(int j=0;j<new_piece.template_cols;j++) {
					new_piece.template[i][j] = current_row.charAt(j);
				}
			}
			if (piece_description.containsKey("color")){
				new_piece.color = (Color) Color.class.getDeclaredField((String) piece_description.get("color")).get(null);
				new_piece.color_name = (String) piece_description.get("color");
			}
			if (piece_description.containsKey("label")){
				new_piece.label = (String) piece_description.get("label");
			} 
			if (piece_description.containsKey("rotation")) {
				new_piece.rotation = (Integer) piece_description.get("rotation");
			} 
			if (piece_description.containsKey("goal")) {
				new_piece.goal = (Integer)piece_description.get("goal");
			}
			if (piece_description.containsKey("mirrored")) {
				new_piece.mirrored = (Boolean) piece_description.get("mirrored");
			}
			if (piece_description.containsKey("solved")) {
				new_piece.solved = (Boolean)piece_description.get("solved");
			}
			
			_pieces.add(new_piece);
		}
	}

	@Override
	public String getType() {
		return "pieces";
	}

	@Override
	public PentoObject getObject() {
		return _pieces.get(0);
	}

	@Override
	public ArrayList<PentoObject> getObjects() {
		return _pieces;
	}

	@Override
	public PentoObject getById(String id) {
		for(PentoObject p : _pieces) {
			if(((Piece)p).id.equals(id)) return p;
		}
		return null;
	}

	public PieceQuery startQuery() {
		ArrayList<PentoObject> searchable = new ArrayList<PentoObject>();
		for(PentoObject p : _pieces) {
			if(!((Piece)p).solved) {
				searchable.add(p);
			}
		}
		return new PieceQuery(searchable, this);
	}
	
	public int get_piece_row(Piece p) throws FileNotFoundException {
		ArrayList<PentoObject> _search = (ArrayList<PentoObject>) _pieces.clone();
		BoardModel bm = new BoardModel();
		bm.loadObjects();
		int _grid_size = bm.board.grid_size;
				
		Collections.sort(_search, new Comparator<PentoObject>() {
			@Override
			public int compare(PentoObject o1, PentoObject o2) {
				if(((Piece)o1).top < ((Piece)o2).top) return -1;
				if(((Piece)o1).top == ((Piece)o2).top) return 0;
				return 1;
			}
		});
		int current_row = 0;
		Piece last_piece = null;
		for(PentoObject current : _search) {
			if(last_piece != null && ((Piece)current).top >= last_piece.top + last_piece.template_rows*_grid_size) {
				current_row++;
			}
			if(((Piece)current).id == p.id) return current_row;
			last_piece = (Piece)current;
		}
		return -1;
	}
	
	public int get_piece_col(Piece p) throws FileNotFoundException {
		ArrayList<PentoObject> _search = (ArrayList<PentoObject>) _pieces.clone();
		BoardModel bm = new BoardModel();
		bm.loadObjects();
		int _grid_size = bm.board.grid_size;
		
		Collections.sort(_search, new Comparator<PentoObject>() {
			@Override
			public int compare(PentoObject o1, PentoObject o2) {
				if(((Piece)o1).left < ((Piece)o2).left) return -1;
				if(((Piece)o1).left == ((Piece)o2).left) return 0;
				return 1;
			}
		});
		int current_col = 0;
		Piece last_piece = null;
		for(PentoObject current : _search) {
			if(last_piece != null && ((Piece)current).left >= last_piece.left + last_piece.template_cols*_grid_size) {
				current_col++;
			}
			if(((Piece)current).id == p.id) return current_col;
			last_piece = (Piece)current;
		}
		return -1;
	}

}
