package isotopestudio.backdoor.engine.datapack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.border.SimpleLineBorder;

import doryanbessiere.isotopestudio.commons.ColorConvertor;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.game.BackdoorGame;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class DataParameters implements IDataParameters {

	protected LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();

	public static DataParameters load(File file) throws FileNotFoundException {
		return load(new FileInputStream(file));
	}

	public static DataParameters load(InputStream stream) {
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		BufferedReader read = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
		try {
			String line = null;
			while ((line = read.readLine()) != null) {
				if (!line.startsWith("#")) {
					if (line.contains("=")) {
						String[] args = line.split("=");

						if (args.length == 2) {
							String key = args[0];
							String value = args[1];

							keys.put(key, value == "null" ? null : value);
						}
					}
				}
			}
			read.close();
			DataParameters graphicsFile = new DataParameters();
			graphicsFile.keys.putAll(keys);
			return graphicsFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean default_data = false;
	
	public DataParameters setDefaultData(boolean default_data) {
		this.default_data = default_data;
		return this;
	}
	
	public boolean isDefaultData() {
		return default_data;
	}
	
	@Override
	public void applyBackgroundColor(String key, Style style) {
		if (notNull(key) && isColor(key)) {
			float[] color = getColor(key);
			Background background = style.getBackground();
			if(background== null) {
				style.setBackground(new Background());
				background = style.getBackground();
			}
			background.setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		}
	}

	@Override
	public void applyBorderColor(String key, Style style) {
		if (notNull(key) && isColor(key)) {
			float[] color = getColor(key);
			style.setBorder(new SimpleLineBorder(new Vector4f(color[0], color[1], color[2], color[3]), 1f));
		}
	}

	@Override
	public void applyTextColor(String key, Style style) {
		if (notNull(key) && isColor(key)) {
			float[] color = getColor(key);
			style.setTextColor(new Vector4f(color[0], color[1], color[2], color[3]));
		}
	}

	@Override
	public void applyTextFont(String key, Style style) {
		if (notNull(key)) {
			style.setFont(getString(key));
		}
	}

	@Override
	public float[] getColor(String key) {
		String value = getString(key);
		if(value != null) {
			float[] color = ColorConvertor.get(value);
			return new float[] {color[0]/255, color[1]/255, color[2]/255, color.length <= 3 ? 1 : color[3]};
		}
		return null;
	}
	
	public static Vector4f convertColor(float[] color) {
		if(color.length == 4) {
			return new Vector4f(color[0], color[1], color[2], color[3]);	
		} else if(color.length == 3) {
			return new Vector4f(color[0], color[1], color[2], 1);	
		}
		return null;
	}

	@Override
	public String getString(String key) {
		return keys.get(key);
	}

	@Override
	public Integer getInteger(String key) {
		return Integer.parseInt(getString(key));
	}

	@Override
	public Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

	@Override
	public Long getLong(String key) {
		return Long.parseLong(getString(key));
	}

	@Override
	public Float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}

	@Override
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	@Override
	public boolean has(String key) {
		return keys.containsKey(key);
	}

	@Override
	public boolean notNull(String key) {
		if(!has(key))
			return true;
		return keys.get(key) != null && !keys.get(key).equals("null") ;
	}

	@Override
	public boolean isColor(String key) {
		if(!notNull(key))
			return false;
		return getString(key).startsWith("rgb") || getString(key).startsWith("rgba") || getString(key).startsWith("#");
	}

	public static void applyBackgroundColor(IComponent component, String key, Style style) {
		DataParameters data = component.getDataParameters((Component)component);
		if (!data.has(key) && !data.isDefaultData()) {
			BackdoorGame.getDatapack().getData(component.getComponentName()).applyBackgroundColor(key, style);
			return;
		}
		data.applyBackgroundColor(key, style);
	}

	public static void applyBorderColor(IComponent component, String key, Style style) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			BackdoorGame.getDatapack().getData(component.getComponentName()).applyBorderColor(key, style);
			return;
		}
		data.applyBorderColor(key, style);
	}

	public static void applyTextColor(IComponent component, String key, Style style) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			BackdoorGame.getDatapack().getData(component.getComponentName()).applyTextColor(key, style);
			return;
		}
		data.applyTextColor(key, style);
	}

	public static void applyTextFont(IComponent component, String key, Style style) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			BackdoorGame.getDatapack().getData(component.getComponentName()).applyTextFont(key, style);
			return;
		}
		data.applyTextFont(key, style);
	}

	public static float[] getColor(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getColor(key);
		}
		return data.getColor(key);
	}

	public static String getString(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getString(key);
		}
		return data.getString(key);
	}

	public static Integer getInteger(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getInteger(key);
		}
		return data.getInteger(key);
	}

	public static Double getDouble(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getDouble(key);
		}
		return data.getDouble(key);
	}

	public static Long getLong(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getLong(key);
		}
		return data.getLong(key);
	}

	public static Float getFloat(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getFloat(key);
		}
		return data.getFloat(key);
	}

	public static Boolean getBoolean(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).getBoolean(key);
		}
		return data.getBoolean(key);
	}

	public static boolean has(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).has(key);
		}
		return data.has(key);
	}

	public static boolean notNull(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component)component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).notNull(key);
		}
		return data.notNull(key);
	}

	public static boolean isColor(IComponent component, String key) {
		DataParameters data = component.getDataParameters((Component) component);
		if (!data.has(key) && !data.isDefaultData()) {
			return BackdoorGame.getDatapack().getData(component.getComponentName()).isColor(key);
		}
		return data.isColor(key);
	}
}
