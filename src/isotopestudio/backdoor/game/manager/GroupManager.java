package isotopestudio.backdoor.game.manager;

import doryanbessiere.isotopestudio.api.profile.Profile;
import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.group.GroupApplication;
import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.gateway.group.GroupObject;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupCreate;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupDelete;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupInvite;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupKick;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupLeave;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupReady;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupStart;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class GroupManager {
	
	private static GroupObject group;
	public static boolean privateParty = false;

	public static void kick(String playerUUID) {
		BackdoorGame.getGateway().sendPacket(new PacketGroupKick(playerUUID));		
	}

	public static void create() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupCreate());		
	}

	public static void delete() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupDelete());		
	}

	public static void leave() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupLeave());		
	}

	/**
	 * @param profile
	 */
	public static void invite(Profile profile) {
		BackdoorGame.getGateway().sendPacket(new PacketGroupInvite(profile));		
	}

	public static void start(Versus versus, GameMode gameMode) {
		BackdoorGame.getGateway().sendPacket(new PacketGroupStart(versus, gameMode));		
	}

	public static void ready() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupReady(true));
	}

	public static void unready() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupReady(false));
	}

	public static void search() {
	}

	public static void unsearch() {
	}
	
	/**
	 * @return the group
	 */
	public static GroupObject getGroup() {
		return group;
	}
	
	/**
	 * @param group the group to set
	 */
	public static void setGroup(GroupObject group) {
		if(GroupManager.group != null && group == null) {
			privateParty = false;
		} 
		
		boolean isNewGroup = GroupManager.group == null && group != null;
		
		EventManager.handle(new GroupUpdateEvent(group));
		GroupManager.group = group;
		
		if(isNewGroup) {
			if(!GroupApplication.isOpen())
				GroupApplication.showApplication();
		}
	}
}
