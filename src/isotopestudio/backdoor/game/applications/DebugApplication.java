package isotopestudio.backdoor.game.applications;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.label.UpdateLabelStyleWidthListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import doryanbessiere.isotopestudio.commons.ColorConvertor;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import doryanbessiere.isotopestudio.commons.system.CPUUtils;
import doryanbessiere.isotopestudio.commons.system.MemoryUtils;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.ProgressBar;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.settings.GameSettings;
import isotopestudio.backdoor.game.settings.VideoSettings;
import isotopestudio.backdoor.utils.ColorConvert;

public class DebugApplication extends Window implements IComponent {

	private LinkedHashMap<String, Label> labels = new LinkedHashMap<String, Label>();

	private Label ram_usage_label;
	private ProgressBar ramUsageProgress;

	public DebugApplication() {
		super("", 0, 0, 400, 350);

		labels.put("fps", new Label());
		labels.put("tps", new Label());
		labels.put("fps_limiter", new Label());
		labels.put("ping", new Label());

		labels.put("game_version", new Label());
		labels.put("lwjgl_version", new Label());
		labels.put("joml_version", new Label());
		labels.put("legui_version", new Label());
		labels.put("opengl_version", new Label());

		labels.put("ping", new Label());
		labels.put("fullscreen", new Label());
		labels.put("resolution", new Label());
		labels.put("cpu_cores", new Label());
		labels.put("resolution", new Label());
		labels.put("gateway_status", new Label());
		labels.put("component_target", new Label());

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		int top = 10;

		for (Entry<String, Label> entries : labels.entrySet()) {
			Label label = entries.getValue();
			
			label.getStyle().setFontSize(16F);
			label.getStyle().setFont("main");
			label.getStyle().setLeft(10f);
			label.getStyle().setRight(10f);
			label.setVariable(entries.getKey());
			label.getStyle().setTop(top);
			label.getListenerMap().addListener(LabelWidthChangeEvent.class, new UpdateLabelStyleWidthListener());

			top += label.getSize().y;

			getContainer().add(label);
		}

		ram_usage_label = new Label();
		ram_usage_label.getStyle().setFontSize(16F);
		ram_usage_label.getStyle().setFont("main");
		ram_usage_label.getStyle().setLeft(10f);
		ram_usage_label.getStyle().setRight(10f);
		ram_usage_label.getStyle().setBottom(40f);
		ram_usage_label.getListenerMap().addListener(LabelWidthChangeEvent.class,
				new UpdateLabelStyleWidthListener());

		this.ramUsageProgress = new ProgressBar();
		ramUsageProgress.getStyle().setBottom(10f);
		ramUsageProgress.getStyle().setHeight(20F);
		ramUsageProgress.getStyle().setLeft(10f);
		ramUsageProgress.getStyle().setRight(10f);

		getContainer().add(ram_usage_label);
		getContainer().add(ramUsageProgress);

		update();
		load();
	}

	private long time = System.currentTimeMillis();

	public void update() {
		if (!BackdoorGame.getDesktop().containsWindow(this)) {
			BackdoorGame.debugApplication = null;
			return;
		}

		labels.get("fps").getTextState().setText(Lang.get("debug_fps") + " " + BackdoorGame.FPS);
		labels.get("tps").getTextState().setText(Lang.get("debug_tps") + " " + BackdoorGame.TPS);
		labels.get("fps_limiter").getTextState()
				.setText(Lang.get("debug_fps_limiter") + " " + (VideoSettings.getSettings().frames_limiter
						? Lang.get("debug_fps_limited_to", "%fps%", VideoSettings.getSettings().frames_limit + "")
						: Lang.get("debug_any_fps_limit")));
		labels.get("ping").getTextState().setText(Lang.get("debug_ping", "%ping%", BackdoorGame.PING+""));

		labels.get("game_version").getTextState().setText("Game version: " + BackdoorGame.GAME_VERSION);
		labels.get("legui_version").getTextState().setText("Legui version: " + BackdoorGame.LEGUI_VERSION);
		labels.get("joml_version").getTextState().setText("JOML version: " + BackdoorGame.JOML_VERSION);
		labels.get("lwjgl_version").getTextState().setText("LWJGL version: " + Version.getVersion());
		labels.get("opengl_version").getTextState().setText("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		labels.get("fullscreen").getTextState()
				.setText(Lang.get("debug_fullscreen") + " " + BackdoorGame.getGameWindow().isFullscreenMode());
		Vector2i windowSize = BackdoorGame.getGameWindow().context.getFramebufferSize();
		labels.get("resolution").getTextState()
				.setText(Lang.get("debug_dimension") + " " + windowSize.x + "x" + windowSize.y);
		labels.get("cpu_cores").getTextState()
				.setText(Lang.get("debug_cpu_cores", "%cores%", CPUUtils.availableProcessorsCores() + ""));
		labels.get("gateway_status").getTextState().setText(Lang.get("debug_gateway_status_"+(BackdoorGame.getGateway().isConnected() ? "connected" : "disconnected")));
		labels.get("component_target").getTextState().setText(Lang.get("debug_component_target", "%component_targeted%",
				BackdoorGame.target_component_variable == null ? "null" : BackdoorGame.target_component_variable));

		Vector4f color = success;

		MemoryUtils memoryUtils = new MemoryUtils();

		float usage = memoryUtils.usedMemory() * 100 / memoryUtils.maxmimumMemory();
		if (usage > 50 && usage < 70) {
			color = info;
		}
		if (usage > 70 && usage < 85) {
			color = warning;
		}
		if (usage > 85) {
			color = danger;
		}
		if ((time - System.currentTimeMillis()) < 0) {
			int mb = 1048576;

			this.ram_usage_label.getTextState()
					.setText(Lang.get("debug_ram_usage", "%usage%", ((int) usage) + "", "%used_mb%",
							(memoryUtils.usedMemory() / mb) + "", "%total_mb%",
							(memoryUtils.maxmimumMemory() / mb) + ""));

			this.ramUsageProgress.setProgressColor(color);
			this.ramUsageProgress.setValue(usage);

			time = System.currentTimeMillis() + 200;
		}
	}

	private Vector4f danger = ColorConvert.convertToVector4f(ColorConvertor.get("#f44336"));
	private Vector4f success = ColorConvert.convertToVector4f(ColorConvertor.get("#4CAF50"));
	private Vector4f info = ColorConvert.convertToVector4f(ColorConvertor.get("#2196F3"));
	private Vector4f warning = ColorConvert.convertToVector4f(ColorConvertor.get("#ff9800"));

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_debug"));
		super.load();

		if (labels != null) {
			for (Label label : labels.values()) {
				label.load();
			}
		}

		ram_usage_label.load();
		ramUsageProgress.load();

		getTitle().getTextState().setText(Lang.get("debug_title"));
	}
}
