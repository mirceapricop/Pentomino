package pentogame.views.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import pentogame.objects.PentoObject;
import pentogame.objects.Piece;

public class PieceRenderer extends JPanel implements ObjectRenderer {

	private Piece _piece;
	private int _grid_size;
	
	@Override
	public void render(PentoObject object) {
		_piece = (Piece) object;
		setSize(_piece.getTemplateCols() * _grid_size+1, _piece.getTemplateRows()*_grid_size+1);
		setLocation(_piece.left, _piece.top);
		
		if(_piece.label != null && _piece.label != "") {
			JLabel label = new JLabel(_piece.label);
			label.setFont(new Font("Courier", Font.BOLD, 15));
			add(label);
		}
			
		setOpaque(false);
		repaint();
	}
	
	public void setGridSize(int size) {
		_grid_size = size;
	}
	
	@Override
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
	    for(int i=0;i<_piece.getTemplateRows();i++) {
			for(int j=0;j<_piece.getTemplateCols();j++) {
				if(_piece.getTemplate()[i][j] == '0') {
					drawSquare(i, j, g);
				}
			}
		}
	}
	
	private void drawSquare(int row, int col, Graphics2D g) {
		g.setColor(_piece.color);
		g.fillRect(col*_grid_size, row*_grid_size, _grid_size, _grid_size);
		
		g.setColor(Color.BLACK);
		Stroke original = g.getStroke();
		if(_piece.selected) {
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.orange);
		}
		g.drawRect(col*_grid_size, row*_grid_size, _grid_size, _grid_size);	
		g.setStroke(original);
	}

}
