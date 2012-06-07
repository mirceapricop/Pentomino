package pentogame.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Spline;

import pentogame.models.BoardModel;
import pentogame.models.ObjectModel;
import pentogame.objects.Board;
import pentogame.objects.HandCursor;
import pentogame.objects.Piece;
import pentogame.objects.Target;
import pentogame.views.WorldView;

public class WorldController {
	
	private ArrayList<WorldView> _views;
	private ArrayList<ObjectModel> _models;
	private Piece _selectedPiece;
	private String _state;
	private ArrayList<Timeline> _animations;
	
	public WorldController() {
		_views = new ArrayList<WorldView>();
		_models = new ArrayList<ObjectModel>();
		_animations = new ArrayList<Timeline>();
	}
	
	public void addView(WorldView view) {
		_views.add(view);
	}
	
	public void addModel(ObjectModel model) {
		try {
			model.loadObjects();
		} catch(Exception e) {
			System.out.println("Could not load model: " + model);
			e.printStackTrace();
			System.exit(1);
		}
		_models.add(model);
	}

	public void run() throws IOException {
		for(WorldView view : _views) {
			view.init(this);
		}
	}
	
	public ObjectModel fetchModel(String type) {
		for(ObjectModel model : _models) {
			if(model.getType().equals(type)) {
				return model;
			}
		}
		return null;
	}
	
	public void updateViews() {
		for(WorldView v : _views) {
			v.update();
		}
	}
	
	// view API starts here
	// Toggles a piece selected state
	public void selectPiece(String id) {
		Piece piece = (Piece) fetchModel("pieces").getById(id);
		piece.selected = !piece.selected;
		updateViews();
	}
	
	// Moves cursor to piece and selects it
	public void takePiece(String id) {
		final HandCursor hand = (HandCursor) fetchModel("hand").getObject();
		final Piece piece = (Piece) fetchModel("pieces").getById(id);
		Board board = (Board) fetchModel("board").getObject();
		
		hand.state = "idle";
		if(_selectedPiece!=null) {
			_selectedPiece.selected = false;
			_selectedPiece = null;
		}
		final Timeline movement = new Timeline(hand);
		movement.addPropertyToInterpolate("left", hand.left, piece.left+piece.getTemplateCols()*board.grid_size/2-25);
		movement.addPropertyToInterpolate("top", hand.top, piece.top+piece.getTemplateRows()*board.grid_size/2-25);
		movement.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState arg0, TimelineState new_state, float arg2, float arg3) {
				if(new_state.equals(TimelineState.DONE)) {
					piece.selected = true;
					hand.state = "grabbing";
					_selectedPiece = piece;
					_animations.remove(movement);
					updateViews();
				}
				if(new_state.equals(TimelineState.CANCELLED)) {
					_animations.remove(movement);
				}
			}
			@Override
			public void onTimelinePulse(float arg0, float arg1) {
				updateViews();
			}
		});
		movement.setDuration(1000);
		movement.setEase(new Spline((float) 0.6));
		movement.play();
		_animations.add(movement);
	}
	
	public void abort() {
		for(Timeline t : _animations) {
			t.cancel();
		}
	}
	
	// Moves the currently selected piece to the target with the indicated label
	// Returns:
	// 	 1 on success
	//	 0 on no piece selected 
	//	-1 on invalid target for piece
	public int movePieceTo(String target_label) {
		if(_selectedPiece == null) {
			return 0;
		}
		
		Target goal = ((BoardModel)fetchModel("board")).getTargetByLabel(target_label);
		if(goal == null || _selectedPiece.goal != goal.id) {
			return -1;
		}
		
		// Start the animation and return 1
		final HandCursor hand = (HandCursor) fetchModel("hand").getObject();
		final Piece piece = _selectedPiece;
		final Board board = (Board) fetchModel("board").getObject();
		
		final Timeline movement = new Timeline(piece);
		movement.addPropertyToInterpolate("left", piece.left, goal.left);
		movement.addPropertyToInterpolate("top", piece.top, goal.top);
		movement.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState arg0, TimelineState new_state, float arg2, float arg3) {
				if(new_state.equals(TimelineState.DONE)) {
					piece.selected = false;
					hand.state = "idle";
					_selectedPiece = null;
					_animations.remove(movement);
					updateViews();
				}
				if(new_state.equals(TimelineState.CANCELLED)) {
					_animations.remove(movement);
				}
			}
			@Override
			public void onTimelinePulse(float arg0, float arg1) {
				hand.left = piece.left+piece.getTemplateCols()*board.grid_size/2-25;
				hand.top = piece.top+piece.getTemplateRows()*board.grid_size/2-25;
				updateViews();
			}
		});
		
		movement.setDuration(1000);
		movement.setEase(new Spline((float) 0.6));
		movement.play();
		_animations.add(movement);
		return 1;
	}
	
	// Deselects the current piece
	public void deselect() {
		if(_selectedPiece != null) {
			_selectedPiece.selected = false;
			_selectedPiece = null;
			((HandCursor)fetchModel("hand").getObject()).state = "idle";
		}
		updateViews();
	}

	public void shutDown() {
		for(WorldView v : _views) {
			v.shutDown();
		}
		System.exit(0);
	}

}
