package pentogame.views;

import java.io.IOException;

import pentogame.controllers.WorldController;

public interface WorldView {

	public void init(WorldController controller) throws IOException;
	public void update();
	public void shutDown();
}
