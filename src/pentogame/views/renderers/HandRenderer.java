package pentogame.views.renderers;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pentogame.objects.HandCursor;
import pentogame.objects.PentoObject;

public class HandRenderer extends JPanel implements ObjectRenderer {

	private HandCursor _cursor;
	private JLabel _imageLabel;
	
	public HandRenderer() {
		_imageLabel = new JLabel();
		add(_imageLabel);
		setOpaque(false);
		setSize(50, 50);
	}
	
	@Override
	public void render(PentoObject object) {
		_cursor = (HandCursor) object;
		setLocation(_cursor.left, _cursor.top);
		_imageLabel.setIcon(new ImageIcon(_cursor.state+".png"));
		repaint();
	}

}
