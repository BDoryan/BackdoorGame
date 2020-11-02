package isotopestudio.backdoor.utils;

import org.joml.Vector4f;

public class ColorConvert {

	public static Vector4f convertToVector4f(float[] color) {
		return new Vector4f( color[0] / 255, color[1] / 255, color[2] / 255, color.length <= 3 ? 1 : color[3]);
	}
}
