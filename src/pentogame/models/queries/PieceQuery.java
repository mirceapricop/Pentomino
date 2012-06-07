package pentogame.models.queries;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import pentogame.models.PieceModel;
import pentogame.objects.PentoObject;
import pentogame.objects.Piece;

public class PieceQuery implements Query {
	
	private ArrayList<PentoObject> _results;
	private PieceModel _model;
	
	public PieceQuery(ArrayList<PentoObject> pieces, PieceModel model) {
		_results = pieces;
		_model = model;
	}

	public PieceQuery where(String property, String value) throws FileNotFoundException {
		ArrayList<PentoObject> new_results = new ArrayList<PentoObject>();
		if(property.equals("position")) {
			int row, col;
			try { row = Integer.parseInt(value.split(",")[0]); } catch(Exception e) { row = -1; }
			try { col = Integer.parseInt(value.split(",")[1]); } catch(Exception e) { col = -1; }
			return new PieceQuery(filter_position(row, col), _model);
		}
		for(PentoObject p : _results) {
			if(p.getProperty(property).equals(value)) {
				new_results.add(p);
			}
		}
		return new PieceQuery(new_results, _model);
	}

	private ArrayList<PentoObject> filter_position(int row, int col) throws FileNotFoundException {
		ArrayList<PentoObject> result = new ArrayList<PentoObject>();
		for(PentoObject p : _results) {
			if((row == -1 || _model.get_piece_row((Piece)p) == row) && (col == -1 || _model.get_piece_col((Piece)p) == col)) {
				result.add(p);
			}
		}
		return result;
	}

	@Override
	public ArrayList<PentoObject> fetch() {
		return _results;
	}

	public boolean canBeSeparatedBy(String attribute) throws FileNotFoundException {
		if(attribute.equals("row")) {
			int first = _model.get_piece_row((Piece)_results.get(0));
			for(PentoObject p : _results) {
				if(_model.get_piece_row((Piece)p) != first) return true;
			}
		}
		
		if(attribute.equals("col")) {
			int first = _model.get_piece_col((Piece)_results.get(0));
			for(PentoObject p : _results) {
				if(_model.get_piece_col((Piece)p) != first) return true;
			}
		}
		
		if(attribute.equals("color")) {
			String first = ((Piece)_results.get(0)).color_name;
			for(PentoObject p : _results) {
				if(!((Piece)p).color_name.equals(first)) return true;
			}
		}
		
		if(attribute.equals("form")) {
			String first = ((Piece)_results.get(0)).type;
			for(PentoObject p : _results) {
				if(!((Piece)p).type.equals(first)) return true;
			}
		}
		
		return false;
	}

}
