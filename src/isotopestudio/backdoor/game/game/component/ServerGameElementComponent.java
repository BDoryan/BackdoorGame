package isotopestudio.backdoor.game.game.component;

import isotopestudio.backdoor.core.elements.GameElement;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.game.BackdoorGame;

public class ServerGameElementComponent extends GameElementComponent implements IComponent {

	public ServerGameElementComponent(int x, int y, int radius, int thickness, int margin, GameElement gameElement) {
		super(x, y, radius, thickness, margin, gameElement);
	}

	@Override
	public void update() {
	}

	@Override
	public void load() {
		setImage(BackdoorGame.getDatapack().getImage("icon_server"));
	}
}
