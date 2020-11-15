package isotopestudio.backdoor.game.applications.terminal.interfaces;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
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
