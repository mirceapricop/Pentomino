package pentogame.views;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import pentogame.controllers.WorldController;
import pentogame.models.PieceModel;
import pentogame.models.queries.PieceQuery;

public class PentoCommand extends Thread implements WorldView {
	
	private WorldController _controller;
	
	@Override
	public void init(WorldController controller) {
		_controller = controller;
		start();
	}
	
	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.print("$> ");
			try {
				String command = br.readLine();
				String method = command.split(" ")[0];
				if(method.equals("select")) {
					_controller.selectPiece(command.split(" ")[1]);
				}
				if(method.equals("take")) {
					_controller.takePiece(command.split(" ")[1]);
				}
				if(method.equals("query")) {
					PieceQuery query = ((PieceModel)_controller.fetchModel("pieces")).startQuery();
					while(true) {
						System.out.print("query> ");
						String filter = br.readLine();
						if(filter.equals("fetch")) break;
						query = query.where(filter.split(" ")[0], filter.split(" ")[1]);
					}
					System.out.println(query.fetch());
				}
				if(method.equals("moveto")) {
					System.out.println(""+_controller.movePieceTo(command.split(" " )[1]));
				}
			} catch (Exception e) {
				System.out.println("What!?");
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
	}

	@Override
	public void shutDown() {
	}

}
