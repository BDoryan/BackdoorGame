package isotopestudio.backdoor.gateway.packet.packets;

import org.liquidengine.legui.image.Image;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.desktop.notification.Notification;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketClientReceiveNotification extends Packet {
	
	private String image_path;
	private String title;
	private String message;
	private int duration;
	
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
		this.duration = readInt();
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
	
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	
	@Override
	public void process(GatewayClient client) {
		Image image = null;
		if(image_path.startsWith("http")) {
			image = BackdoorGame.loadImageURL(image_path);
		} else {
			image = BackdoorGame.getDatapack().getImage(image_path);
		}
		BackdoorGame.getDesktop().spawnNotification(new Notification(image, title, message, getDuration()));
	}
}
