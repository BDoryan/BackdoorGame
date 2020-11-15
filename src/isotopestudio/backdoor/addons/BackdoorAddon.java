package isotopestudio.backdoor.addons;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public interface BackdoorAddon {

	public abstract void start();
	public abstract void load();
	public abstract void stop();
	
	public abstract AddonData getPluginData();
}
