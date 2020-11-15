package isotopestudio.backdoor.engine.datapack;

import java.io.Reader;

import doryanbessiere.isotopestudio.commons.GsonInstance;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class DatapackObject {
	
	private String name;
	private String author;
	private String version;
	
	public DatapackObject(String name, String author, String version) {
		super();
		this.name = name;
		this.author = author;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toJson() {
		return GsonInstance.instance().toJson(this);
	}
	
	public static DatapackObject fromJson(String json) {
		return GsonInstance.instance().fromJson(json, DatapackObject.class);
	}
	
	public static DatapackObject fromJson(Reader reader) {
		return GsonInstance.instance().fromJson(reader, DatapackObject.class);
	}
}
