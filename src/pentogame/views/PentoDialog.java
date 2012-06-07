package pentogame.views;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pentogame.controllers.WorldController;
import pentogame.models.PieceModel;
import pentogame.models.queries.PieceQuery;
import pentogame.objects.PentoObject;
import pentogame.objects.Piece;

import com.clt.script.exp.*;
import com.clt.script.exp.values.*;
import com.clt.dialog.client.*;

public class PentoDialog extends Client implements WorldView {
	
	WorldController _controller;
	HashMap<String, String> normalizations;
	
	public PentoDialog() {
		normalizations = new HashMap<String, String>();
		normalizations.put("rot", "red");
		normalizations.put("blau", "blue");
		normalizations.put("gruen", "green");
		normalizations.put("gelb", "yellow");
	}

	// this is the name, that DialogOS uses to find your client
	public String getName() {
		return "Pentomino";
	}
	
	private String normalizeString(String input) {
		if(normalizations.containsKey(input)) {
			return normalizations.get(input);
		}
		return input;
	}

	private boolean structValueContainsKey(StructValue s, String key) {
		if(!s.containsLabel(key)) return false;
		try {
			return !(((StringValue)(s.getValue(key))).getString().equals("null") );
		} catch(Exception e) {
			return true;
		}
	}
	
	public void query(StructValue q) throws IOException {
		PieceQuery query = ((PieceModel)_controller.fetchModel("pieces")).startQuery();
		
		ArrayList<PentoObject> colorResults = new ArrayList<PentoObject>();
		ArrayList<PentoObject> formResults = new ArrayList<PentoObject>();
		
		if(structValueContainsKey(q, "farbe")) {
			System.out.println("Filtering by color: " + normalizeString(((StringValue)q.getValue("farbe")).getString()));
			query = query.where("color", normalizeString(((StringValue)q.getValue("farbe")).getString()));
			colorResults = query.fetch();
		}
		if(structValueContainsKey(q, "form")) {
			StructValue form = (StructValue)q.getValue("form");
			if(structValueContainsKey(form, "Buchstabe")) {
				System.out.println("Filtering by letter: " + ((StringValue)form.getValue("Buchstabe")).getString());
				query = query.where("form", ((StringValue)form.getValue("Buchstabe")).getString());
				formResults = query.fetch();
			}
		}
		if(structValueContainsKey(q, "position")) {
			StructValue position = (StructValue)q.getValue("position");
			if(structValueContainsKey(position, "positionX")) {
				String positionString = ((StringValue)position.getValue("positionX")).getString();
				System.out.println("Filtering by column: " + positionString);
				query = query.where("position", "," + positionString.split(" ")[1]);
			}
			if(structValueContainsKey(position, "positionY")) {
				String positionString = ((StringValue)position.getValue("positionY")).getString();
				System.out.println("Filtering by row: " + positionString);
				query = query.where("position", positionString.split(" ")[1] + ",");
			}
		}
		ArrayList<PentoObject> results = query.fetch();
		System.out.println("Results are: " + results);
		
		if(results.size() == 0) results = formResults; // Try to fall back on ignoring position
		if(results.size() == 0) results = colorResults; // Fall back again if we still have no matches
		
		if(results.size() == 1) {
			_controller.takePiece(((Piece)results.get(0)).id);
		}
		
		Map<String, Value> result_map = new HashMap<String, Value>();
		if(results.size() == 0) result_map.put("result", new StringValue("none"));
		if(results.size() == 1) result_map.put("result", new StringValue("success"));
		if(results.size() > 1) {
			result_map.put("result", new StringValue("more"));
			String[] attributes = {"row", "col", "color", "form"};
			ArrayList<StringValue> separators = new ArrayList<StringValue>();
			
			for(String attribute : attributes) {
				if((new PieceQuery(results, (PieceModel)_controller.fetchModel("pieces"))).canBeSeparatedBy(attribute)) {
					separators.add(new StringValue(attribute));
				}
			}
			
			result_map.put("ask", new ListValue(separators));
		}
		StructValue response = new StructValue(result_map);
		send(response);
	}
	
	public void deselect(StructValue q) {
		_controller.deselect();
	}
	
	public void move(StructValue q) throws IOException {
		int result = _controller.movePieceTo(((StringValue)q.getValue("argument")).getString());
		Map<String, Value> result_map = new HashMap<String, Value>();
		if(result == 1) {
			result_map.put("result", new StringValue("success"));
		} else {
			result_map.put("result", new StringValue("none"));
		}
		StructValue response = new StructValue(result_map);
		send(response);
	}
	
	public void output(Value v) {
		System.out.println("Got new command " + v.toString());
		try {
			if(v instanceof StructValue) {
				StructValue q = (StructValue)v;
				
				if(structValueContainsKey(q, "command")) {
					String command = ((StringValue)q.getValue("command")).getString();
					if(command.equals("deselect")) {
						deselect(q);
					}
					else if(command.equals("move")) {
						move(q);
					}
				} else query(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sessionStarted() {
		System.out.println("Session started.");
	}

	public void reset() {
		System.out.println("Reset.");
	}

	public void stateChanged(ConnectionState state) {
		System.out.println("State changed: " + state);
	}

	public void error(Throwable t) {
		System.err.println("An internal error has occurred: " + t);
	}

	@Override
	public void init(WorldController controller) throws IOException {
		_controller = controller;
		open();
	}

	@Override
	public void update() {
	}

	@Override
	public void shutDown() {
		System.out.println("Closing client");
		close();	
	}
}