package pentogame.objects;

import java.awt.Color;
import java.awt.Point;
import java.util.UUID;

public class Piece implements PentoObject {

	public String type;
	public Color color;
	public String color_name;
	public String label;
	public int rotation;
	public int left;
	public int top;
	public int goal;
	public int template_rows;
	public int template_cols;
	public char[][] template;
	public boolean selected;
	public boolean mirrored;
	public boolean solved;
	public String id;
	
	public Piece() {
		id = UUID.randomUUID().toString();
		color = Color.gray;
		color_name = "gray";
		label = "";
		rotation = 0;
		goal = 0;
		mirrored = false;
		selected = false;
		solved = false;
	}
	
	public boolean flipped() {
		return ((rotation%360)/90)%2 == 0;
	}
	
	public int getTemplateRows() {
		if(flipped()) {
			return template_rows;
		}
		return template_cols;
	}
	
	public int getTemplateCols() {
		if(flipped()) {
			return template_cols;
		}
		return template_rows;
	}
	
	private char[][] rotate_right(char[][] source) {
		int w = source[0].length;
		int h = source.length;
		char[][] result = new char[w][h];
		for(int i=0;i<h;i++) {
			for(int j=0;j<w;j++) {
				result[j][h-1-i] = source[i][j];
			}
		}
		return result;
	}
	
	private char[][] mirror(char[][] source) {
		int w = source[0].length;
		int h = source.length;
		char[][] result = new char[h][w];
		for(int i=0;i<h;i++) {
			for(int j=0;j<w;j++) {
				result[i][w-1-j] = source[i][j];
			}
		}
		return result;
	}
	
	public char[][] getTemplate() {
		char[][] result = template;
		int rows = template_rows;
		int cols = template_cols;
		for(int i=0;i<(rotation % 360)/90;i++) {
			result = rotate_right(result);
			rows = cols ^ rows;
			cols = cols ^ rows;
			rows = cols ^ rows;
		}
		if(mirrored) {
			result = mirror(result);
		}
		return result;
	}
	
	public String toString() {
		return id;
	}
	
	@Override
	public String getProperty(String property) {
		if(property.equals("color")) return color_name;
		if(property.equals("form")) return type;
		return null;
	}
	
	public int getTop() {
		return top;
	}
	
	public void setTop(int t) {
		top = t;
	}
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int l) {
		left = l;
	}
}
