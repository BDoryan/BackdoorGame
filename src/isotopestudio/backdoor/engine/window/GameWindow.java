package isotopestudio.backdoor.engine.window;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.liquidengine.legui.image.StbBackedLoadableImage;
import org.liquidengine.legui.system.context.Context;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import isotopestudio.backdoor.game.settings.VideoSettings;

public class GameWindow {

	protected long window_id;

	protected String title;

	protected int width;
	protected int height;

	public boolean running = false;
	public long[] monitors = null;
	public boolean toggleFullscreen = false;
	public boolean fullscreen = false;
	public Context context;

	public GameWindow(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	public void create() {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		PointerBuffer pointerBuffer = glfwGetMonitors();
		int remaining = pointerBuffer.remaining();
		monitors = new long[remaining];
		for (int i = 0; i < remaining; i++) {
			monitors[i] = pointerBuffer.get(i);
		}

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		this.window_id = glfwCreateWindow(width, height, title, 0, 0);
		if (window_id == 0) {
			throw new IllegalStateException("Failed to create window!");
		}

		GLFWVidMode video_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window_id, video_mode.width() / 2 - width / 2, video_mode.height() / 2 - height / 2);

		StbBackedLoadableImage icon = new StbBackedLoadableImage("game_icon.png");
		GLFWImage image = GLFWImage.malloc();
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
		image.set(icon.getWidth(), icon.getHeight(), icon.getImageData());
		imagebf.put(0, image);
		glfwSetWindowIcon(window_id, imagebf);

		glfwShowWindow(window_id);

		glfwMakeContextCurrent(window_id);
		GL.createCapabilities();
		glfwSwapInterval(0);
	}

	public void setVisible(boolean visible) {
		glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getWindowSize() {
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(getID(), w, h);
		int width = w.get(0);
		int height = h.get(0);
		
		return new int[] {width, height};
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window_id);
	}

	public long getID() {
		return window_id;
	}

	public void waitEvents() {
		glfwWaitEvents();
	}

	public void dispose() {
		glfwDestroyWindow(window_id);
		glfwTerminate();
	}

	public boolean isFullscreenMode() {
		return fullscreen;
	}

	public void toggleFullscreen() {
		if (fullscreen) {
			glfwSetWindowMonitor(getID(), NULL, 100, 100, getWidth(),
					getHeight(), GLFW_DONT_CARE);
		} else {
			GLFWVidMode glfwVidMode = glfwGetVideoMode(monitors[0]);
			glfwSetWindowMonitor(getID(), monitors[0], 0, 0, glfwVidMode.width(),
					glfwVidMode.height(), glfwVidMode.refreshRate());
		}
		VideoSettings video_settings = VideoSettings.getSettings();
		fullscreen = !fullscreen;
		video_settings.fullscreen = fullscreen;
		video_settings.save();
		toggleFullscreen = false;		
	}
}
