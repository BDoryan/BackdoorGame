package isotopestudio.backdoor.engine.datapack;

import org.liquidengine.legui.style.Style;

public interface IDataParameters {

	public void applyBackgroundColor(String key, Style style);
	public void applyBorderColor(String key, Style style);
	public void applyTextColor(String key, Style style);
	public void applyTextFont(String key, Style style);
	
	public float[] getColor(String key);
	public String getString(String key);
	public Integer getInteger(String key);
	public Double getDouble(String key);
	public Long getLong(String key);
	public Float getFloat(String key);
	public Boolean getBoolean(String key);
	public boolean has(String key);
	public boolean notNull(String key);
	public boolean isColor(String key);
}
