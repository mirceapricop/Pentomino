package pentogame.objects;

public class HandCursor implements PentoObject {

	public String state;
	public int left;
	public int top;
	
	public HandCursor() {
		state = "idle";
		left = 275;
		top = 275;
	}
	
	public void setLeft(int l) {
		left = l;
	}
	
	public void setTop(int t) {
		top = t;
	}

	@Override
	public String getProperty(String property) {
		return null;
	}
}
