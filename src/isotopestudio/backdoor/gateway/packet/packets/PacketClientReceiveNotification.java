package isotopestudio.backdoor.gateway.packet.packets;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.desktop.notification.Notification;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketClientReceiveNotification extends Packet {
	
	private String image_path;
	private String title;
	private String message;
	
	public PacketClientReceiveNotification() {
		super(CLIENT_RECEIVE_NOTIFICATION);
	}

	@Override
	public Packet clone() {
		return new PacketClientReceiveNotification();
	}
	
	@Override
	public void read() {
		this.image_path = readString();
		this.title = Lang.translate(readString());
		this.message = Lang.translate(readString()); 
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the image_path
	 */
	public String getImagePath() {
		return image_path;
	}
	
	@Override
	public void process(GatewayClient client) {
		BackdoorGame.getDesktop().spawnNotification(new Notification(BackdoorGame.getDatapack().getImage(image_path), title, message, 10));
	}
}
