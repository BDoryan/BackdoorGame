package isotopestudio.backdoor.gateway.packet.packets.group;

import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.EventListener;

import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.desktop.notification.Notification;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupInvite extends Packet {

	public PacketGroupInvite() {
		super(GROUP_INVITE);
	}
	
	public PacketGroupInvite(Profile player) {
		super(GROUP_INVITE, player.toJson());
		this.player = player;
	}

	@Override
	public Packet clone() {
		return new PacketGroupInvite();
	}
	
	private Profile player;

	@Override
	public void read() {
		this.player = Profile.fromJson(readString());
	}

	@Override
	public void process(GatewayClient client) {
		BackdoorGame.group_invitations.add(player.getUuidString());
		Notification notification = new Notification(BackdoorGame.getDatapack().getImage("group_invitation"), Lang.get("group_invitation_title"), Lang.get("group_invitation_message", "%username%", player.getUsername()));
		notification.getListenerMap().addListener(MouseClickEvent.class, new EventListener<MouseClickEvent>() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				client.sendPacket(new PacketGroupAccept(player.getUuidString()));
			}
		});
		BackdoorGame.getDesktop().spawnNotification(notification);
	}
}
