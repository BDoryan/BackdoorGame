package isotopestudio.backdoor.game.manager;

import doryanbessiere.isotopestudio.api.profile.Profile;
import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.group.GroupApplication;
import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.gateway.group.GroupObject;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupCancel;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupCreate;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupDelete;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupInvite;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupKick;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupLeave;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupReady;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupSet;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupStart;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class GroupManager {
	
	private static GroupObject group;

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

	public static void start() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupStart());		
	}

	public static void ready() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupReady(true));
	}

	public static void unready() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupReady(false));
	}

	/**
	 * @param checked
	 * @param fromString
	 * @param fromString2
	 * @return
	 */
	public static void set(boolean isPrivate, GameMode gameMode, Versus versus) {
		BackdoorGame.getGateway().sendPacket(new PacketGroupSet(gameMode, versus, isPrivate));
	}

	public static void unsearch() {
		BackdoorGame.getGateway().sendPacket(new PacketGroupCancel());
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
		boolean isNewGroup = GroupManager.group == null && group != null;

		EventManager.push(new GroupUpdateEvent(group));
		GroupManager.group = group;
		
		if(isNewGroup) {
			if(!GroupApplication.isOpen())
				GroupApplication.showApplication();
		}
	}
}
