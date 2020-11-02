package isotopestudio.backdoor.game.game;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.map.MapData;
import isotopestudio.backdoor.core.team.Team;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.desktop.Desktop;
import isotopestudio.backdoor.engine.components.desktop.dialog.Dialog;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.DashboardApplication;
import isotopestudio.backdoor.game.applications.NetworkApplication;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerConnectToElement;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerDisconnectCurrentElement;

public class GameParty {

	private NetworkApplication networkApplication;

	public GameParty() {
	}

	public void start() {
		BackdoorGame.game_party = this;
		
		this.networkApplication = NetworkApplication.show(this);
		this.networkApplication.load();

		Desktop desktop = BackdoorGame.getDesktop();

		desktop.addWindow(this.networkApplication);

		this.networkApplication.centerLocation();
	}

	public void endParty(Team winner) {
		Dialog dialog = new Dialog(Lang.get("party_dialog_end_party_title"), 450, 100);
		dialog.setResizable(false);

		Label questionLabel = new Label(Lang.get("party_dialog_end_party_message", "%team%",
				Lang.get(winner.getPath())), 10, 10, 450, 20);
		questionLabel.load();
		dialog.load();

		dialog.getContainer().add(questionLabel);
		dialog.show(BackdoorGame.getFrame());
		dialog.centerLocation();
	}

	public NetworkApplication getMap() {
		return networkApplication;
	}

	public MapData getMapData() {
		return BackdoorGame.getGameClient().getMap();
	}

	public void stop() {
		Desktop desktop = BackdoorGame.getDesktop();

		desktop.removeWindow(this.networkApplication);
		
		if (DashboardApplication.main != null)
			desktop.removeWindow(DashboardApplication.main);
	}

	/**
	 * TODO: mettre à jour pour les serveurs (éléments) offline 
	 */
	public boolean connect(String adress) {
		GameClient game_client = BackdoorGame.getGameClient();
		game_client.sendPacket(new PacketPlayerConnectToElement(adress));
		return false;
	}

	public void disconnect() {
		GameClient game_client = BackdoorGame.getGameClient();
		if(game_client.isConnected())
		game_client.sendPacket(new PacketPlayerDisconnectCurrentElement());
	}
}
