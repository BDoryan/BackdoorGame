package isotopestudio.backdoor.game.server;

import isotopestudio.backdoor.network.server.GameServer;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class ServerMaker extends GameServer {

	/**
	 * @param port
	 */
	public ServerMaker(int port, String password) {
		super(port, password);
	}
}
