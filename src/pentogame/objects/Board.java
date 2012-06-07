package pentogame.objects;

public class Board implements PentoObject {

	public int width;
	public int height;
	public int grid_size;
	public char[][] fields;
	public int field_rows;
	public int field_cols;
	
	public String toString() {
		return "Pento board: " + width + "x" + height + " with grid of " + grid_size;
	}

	@Override
	public String getProperty(String property) {
		return null;
	}
}
