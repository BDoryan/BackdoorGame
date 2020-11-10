package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.game.manager.GroupManager;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketGroupChangePrivate extends Packet {

	public PacketGroupChangePrivate() {
		super(GROUP_CHANGE_PRIVATE);
	}
	
	private boolean isPrivate = false;

	public PacketGroupChangePrivate(boolean isPrivate) {
		super(GROUP_CHANGE_PRIVATE, isPrivate);
	}

	@Override
	public Packet clone() {
		return new PacketGroupChangePrivate();
	}

	@Override
	public void read() {
		this.isPrivate = readBoolean();
	}

	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}
	
	@Override
	public void process(GatewayClient client) {
		GroupManager.privateParty = isPrivate();
		EventManager.handle(new GroupUpdateEvent(GroupManager.getGroup()));
	}
}
