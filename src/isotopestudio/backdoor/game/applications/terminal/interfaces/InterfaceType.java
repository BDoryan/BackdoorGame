package isotopestudio.backdoor.game.applications.terminal.interfaces;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public enum InterfaceType {

	ATTACK(new AttackInterface()),
	INVENTORY(new InventoryInterface()),
	MAIN(new MainInterface());
	
	private Interface interface_;

	private InterfaceType(Interface interface_) {
		this.interface_ = interface_;
	}

	/**
	 * @return the interface_
	 */
	public Interface getInterface() {
		return interface_;
	}
}
