package isotopestudio.backdoor.addons;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class AddonData {
	
	private String[] author;
	private String name;
	private String version;
	private String description;
	
	public AddonData(String name, String description, String version, String... author) {
		super();
		this.author = author;
		this.name = name;
		this.version = version;
		this.description = description;
	}

	/**
	 * @return the author
	 */
	public String[] getAuthor() {
		return author;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}
}
