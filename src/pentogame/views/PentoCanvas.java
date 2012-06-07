package pentogame.views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import pentogame.controllers.WorldController;
import pentogame.models.BoardModel;
import pentogame.objects.Board;
import pentogame.objects.HandCursor;
import pentogame.objects.PentoObject;
import pentogame.objects.Piece;
import pentogame.views.renderers.BoardRenderer;
import pentogame.views.renderers.HandRenderer;
import pentogame.views.renderers.PieceRenderer;


public class PentoCanvas extends JFrame implements WorldView {

	private WorldController _controller;
	private BoardRenderer _fieldsPanel;
	private HandRenderer _handPanel;
	private HashMap<Piece, PieceRenderer> _piecePanels;
	private JLayeredPane layers;
	
	public PentoCanvas() {
		layers = new JLayeredPane();
		add(layers);
		_fieldsPanel = new BoardRenderer();
		_handPanel = new HandRenderer();
		_piecePanels = new HashMap<Piece, PieceRenderer>();
		layers.add(_fieldsPanel, new Integer(0));
		layers.add(_handPanel, new Integer(2));
	}
	
	private void setupFrame() {
		Board board = (Board) ((BoardModel)_controller.fetchModel("board")).getObject();
		setSize(board.width, board.height);
		layers.setSize(board.width, board.height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				_controller.shutDown();
			}
		});
		
		setLocationRelativeTo(null);
		setLayout(null);
		setTitle("PentoWorld");
		setResizable(false);
		setVisible(true);
	}
	
	private void renderFields() {
		Board board = (Board) ((BoardModel)_controller.fetchModel("board")).getObject();
		_fieldsPanel.render(board);
		// Positions the fields centered horizontally at the bottom
		_fieldsPanel.setLocation(getWidth()/2 - _fieldsPanel.getWidth()/2, getHeight() - _fieldsPanel.getHeight());
	}
	
	private void renderPieces() {
		ArrayList<PentoObject> pieces = _controller.fetchModel("pieces").getObjects();
		Board board = (Board) ((BoardModel)_controller.fetchModel("board")).getObject();
		for(PentoObject piece : pieces) {
			if(_piecePanels.get(piece) == null) {
				PieceRenderer new_panel = new PieceRenderer();
				_piecePanels.put((Piece) piece, new_panel);
				layers.add(new_panel, new Integer(1));
			}
			PieceRenderer update_panel = _piecePanels.get((Piece) piece);
			update_panel.setGridSize(board.grid_size);
			update_panel.render(piece);
		}
	}
	
	private void renderHand() {
		HandCursor cursor = (HandCursor) _controller.fetchModel("hand").getObject();
		_handPanel.render(cursor);
	}
	
	public void update() {
		renderFields();
		renderPieces();
		renderHand();
	}
	
	@Override
	public void init(WorldController controller) {
		_controller = controller;
		setupFrame();
		update();
	}

	@Override
	public void shutDown() {
		setVisible(false);
	}

}
