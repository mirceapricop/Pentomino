package pentogame.views.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import pentogame.objects.Board;
import pentogame.objects.PentoObject;

public class BoardRenderer extends JPanel implements ObjectRenderer {

	private Board _board;
	
	@Override
	public void render(PentoObject object) {
		_board = (Board)object;
		setSize(_board.field_cols * _board.grid_size+1, _board.field_rows * _board.grid_size+70);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i=0;i<_board.field_rows;i++) {
			for(int j=0;j<_board.field_cols;j++) {
				if(_board.fields[i][j] == '0') {
					drawSquare(i, j, g);
				}
			}
		}
	}
	
	private void drawSquare(int row, int col, Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(col*_board.grid_size, row*_board.grid_size, _board.grid_size, _board.grid_size);
		
		g.setColor(Color.BLACK);
		g.drawRect(col*_board.grid_size, row*_board.grid_size, _board.grid_size, _board.grid_size);
	}

}
