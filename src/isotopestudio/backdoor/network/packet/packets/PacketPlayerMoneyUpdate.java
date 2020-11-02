package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.game.applications.MarketApplication;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;
import isotopestudio.backdoor.network.player.NetworkedPlayer;

public class PacketPlayerMoneyUpdate extends Packet {

	public PacketPlayerMoneyUpdate() {
		super(PLAYER_MONEY_UPDATE);
	}

	public PacketPlayerMoneyUpdate(NetworkedPlayer player) {
		super(PLAYER_MONEY_UPDATE, player.getMoney());
	}

	@Override
	public Packet clone() {
		return new PacketPlayerMoneyUpdate();
	}

	private double money;

	public double getMoney() {
		return money;
	}

	@Override
	public void read() {
		money = readDouble();
	}

	@Override
	public void process(GameClient client) {
		if (MarketApplication.main != null) {
			if (client.getMoney() < getMoney()) {
				MarketApplication.main.winMoney(getMoney() - client.getMoney());
			} else {
				MarketApplication.main.loseMoney(client.getMoney() - getMoney());
			}
			MarketApplication.main.setMoney(getMoney());
		}
		client.setMoney(getMoney());
	}
}
