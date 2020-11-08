package isotopestudio.backdoor.game;

import static org.bytedeco.opencv.global.opencv_imgproc.CV_BGR2RGBA;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.Animator;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.image.BufferedImageRGBA;
import org.liquidengine.legui.image.StbBackedLoadableImage;
import org.liquidengine.legui.input.KeyCode;
import org.liquidengine.legui.input.Keyboard;
import org.liquidengine.legui.listener.EventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.Renderer;
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import com.google.gson.Gson;

import doryanbessiere.isotopestudio.api.IsotopeStudioAPI;
import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.api.profile.ProfileAPI;
import doryanbessiere.isotopestudio.api.user.User;
import doryanbessiere.isotopestudio.api.user.UserAPI;
import doryanbessiere.isotopestudio.api.web.Response;
import doryanbessiere.isotopestudio.commons.GsonInstance;
import doryanbessiere.isotopestudio.commons.RunnerUtils;
import doryanbessiere.isotopestudio.commons.Toolkit;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import doryanbessiere.isotopestudio.commons.logger.Logger;
import doryanbessiere.isotopestudio.commons.logger.file.LoggerFile;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.desktop.desktop.Desktop;
import isotopestudio.backdoor.engine.components.desktop.dialog.Dialog;
import isotopestudio.backdoor.engine.components.desktop.notification.Notification;
import isotopestudio.backdoor.engine.components.events.TextDynamicSizeChangeEvent;
import isotopestudio.backdoor.engine.components.render.RenderManager;
import isotopestudio.backdoor.engine.datapack.DatapackObject;
import isotopestudio.backdoor.engine.keyboard.KeyboardLoader;
import isotopestudio.backdoor.engine.video.VideoPlayer;
import isotopestudio.backdoor.engine.window.GameWindow;
import isotopestudio.backdoor.game.applications.DebugApplication;
import isotopestudio.backdoor.game.applications.JavaTerminalApplication;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.game.command.commands.GameServerCommand;
import isotopestudio.backdoor.game.datapack.DatapackLoader;
import isotopestudio.backdoor.game.game.GameParty;
import isotopestudio.backdoor.game.manager.PatchManager;
import isotopestudio.backdoor.game.settings.AudioSettings;
import isotopestudio.backdoor.game.settings.GameSettings;
import isotopestudio.backdoor.game.settings.SettingsManager;
import isotopestudio.backdoor.game.settings.VideoSettings;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;
import isotopestudio.backdoor.gateway.packet.packets.PacketClientDisconnected;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.utils.ConsoleWriter;

public class BackdoorGame {

	private static boolean ignoreintro = false;

	public static String LEGUI_VERSION;
	public static String GAME_VERSION;
	public static String JOML_VERSION;
	public static boolean DEV_MODE = false;

	public static int FPS;
	public static int TPS;
	public static long PING = -1;

	public static Logger logger;
	public static DatapackLoader datapack;
	public static GameWindow game_window;
	public static Frame frame;
	public static Desktop desktop;
	public static DebugApplication debugApplication = null;
	public static JavaTerminalApplication java_terminal;

	// GAME NETWORKING
	
	public static GameParty game_party;
	public static GameClient game_client;
	
	// GAME GATEWAY
	
	public static GatewayClient gateway;

	public static User user;
	public static Profile user_profile;

	public static String target_component_variable;

	public static OpenCVFrameConverter converter;

	public static OpenCVFrameConverter getConverter() {
		return converter;
	}

	public static void main(String[] args) {
		RunnerUtils arguments = new RunnerUtils(args);
		arguments.read();

		if(arguments.contains("devmode"))
			DEV_MODE = arguments.getBoolean("devmode");
		
		avutil.av_log_set_level(avutil.AV_LOG_QUIET);
		converter = new OpenCVFrameConverter.ToMat();

		ignoreintro = arguments.contains("ignoreintro")
				&& (arguments.getBoolean("ignoreintro") + "").equalsIgnoreCase("true");

		for (Entry<String, String> entries : arguments.variables.entrySet()) {
			System.out.println(entries.getKey() + "=" + entries.getValue());
		}
		
		if (arguments.contains("email") && arguments.contains("token")) {
			UserAPI authClient = new UserAPI(IsotopeStudioAPI.API_URL);
			try {
				Response response = authClient.loginToken(arguments.getString("email"), arguments.getString("token"));
				if (response.getPath().equals("success")) {
					HashMap<String, Object> informations = response.getInformations();
					String uuid = informations.get("uuid").toString();
					String username = informations.get("username").toString();
					String token = informations.get("token").toString();
					String email = informations.get("email").toString();
					String permissionlevelString = informations.get("permission_level").toString();
					Double permissionlevel = Toolkit.convertToDouble(permissionlevelString);
					
					user = new User(uuid, username, email, token, permissionlevel);

					user_profile = new ProfileAPI().getProfile(user.getEmail(), user.getToken(), user.getUUIDString());
					if (user_profile == null) {
						System.err.println("It is impossible to retrieve the user's profile.!");
					}
				} else {
					System.err.println("Login failed!");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		if (user == null) {
			JOptionPane.showMessageDialog(null, "You are not logged", "You cannot launch the game",
					JOptionPane.ERROR_MESSAGE);
			stop();
			return;
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(GameServerCommand.server != null) {
					GameServerCommand.server.close();
				}
				
				gateway.disconnect("game_close");
				 
				try {
					new UserAPI().disconnect(getUser());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			java.io.InputStream is = BackdoorGame.class.getClass().getResourceAsStream("/maven.properties");
			java.util.Properties p = new Properties();
			p.load(is);

			GAME_VERSION = p.getProperty("GAME_VERSION");
			JOML_VERSION = p.getProperty("JOML_VERSION");
			LEGUI_VERSION = p.getProperty("LEGUI_VERSION");
		} catch (IOException e) {
			e.printStackTrace();
		}

		File game_ini = new File(localDirectory(), "game.ini");
		if (game_ini.exists()) {
			game_ini.delete();
		}
		try {
			game_ini.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(game_ini));
			properties.setProperty("version", GAME_VERSION);
			FileOutputStream out = new FileOutputStream(game_ini);
			properties.store(out, "");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		load();
		System.out.println("All game files are loaded!");
		
		System.out.println("Game launch!");
		ConsoleWriter writer = new ConsoleWriter();
		writer.addContainer(" _____         _     _ \r\n" + "| __  |___ ___| |_ _| |___ ___ ___  \r\n"
				+ "| __ -| .'|  _| '_| . | . | . |  _|    Version\r\n" + "|_____|__,|___|_,_|___|___|___|_|      "
				+ GAME_VERSION + "\r\n", new Vector4i(4, 1, 4, 2));
		writer.backToLine();
		writer.addLine("Game directory: " + localDirectory().getPath());
		writer.backToLine();
		writer.addList("Authors:", 4, "Arthur Forgeard (Designer)", "Doryan Bessiere (Developer)", "");
		writer.backToLine();
		writer.write();

		start();
	}

	public static boolean isOfflineMode() {
		return gateway.isConnected();
	}
	
	public static boolean checkUpdate() throws MalformedURLException, IOException {
		Properties online_properties = new Properties();
		online_properties.load(new URL(IsotopeStudioAPI.API_URL + "latest/game.ini").openStream());

		if (!online_properties.getProperty("version").equalsIgnoreCase(GAME_VERSION)) {
			return true;
		}
		return false;
	}

	public static void load() {
		logger = new Logger(null, new LoggerFile(new File(localDirectory(), "logs"))) {/*
			@Override
			public void exception(Exception exception) {
				String title = exception.getLocalizedMessage();
				String message = "";
				for (Throwable throwable : exception.getSuppressed()) {
					message += throwable_(throwable);
				}
				
				if(getDesktop() != null)
				getDesktop().dialog(title, message);
				
				super.exception(exception);
			}
				
			private String throwable_(Throwable throwable) {
				String message = "";
				for (StackTraceElement element : throwable.getStackTrace()) {
					message += ("    at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":"
							+ element.getLineNumber() + ") \n");
				}
	
				Throwable ourCause = throwable.getCause();
				if (ourCause != null)
					message += "Caused by: " + ourCause.getClass().getName() +" \n "+ throwable_(ourCause);
				return message;
			}*/
		};

		System.out.println("Clearing the cache directory");
		if (getCacheDirectory().exists())
			try {
				FileUtils.deleteDirectory(getCacheDirectory());
				System.out.println("  - Clear success!");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("  - Clear failed!");
			}

		getCacheDirectory().mkdirs();

		System.out.println("Loading game files...");
		File[] directorys = new File[] { getDatapackDirectory(), new File(localDirectory(), "langs/") };
		for (File directory : directorys) {
			if (!directory.exists()) {
				directory.mkdirs();
			} else if (!directory.isDirectory()) {
				if (directory.delete()) {
					directory.mkdirs();
				} else {
					JOptionPane.showMessageDialog(null,
							"The folder '" + directory.getPath()
									+ "' can't be created because its impostor file can't be deleted.",
							"Fatal error", JOptionPane.ERROR_MESSAGE);
					crash();
				}
			}
		}

		System.out.println("  - Loading settings...");
		try {
			SettingsManager.load();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error, your settings files has been currupted");
		}

		GameSettings game_settings = GameSettings.getSettings();
		VideoSettings video_settings = VideoSettings.getSettings();
		AudioSettings audios_settings = AudioSettings.getSettings();

		System.out.println("  - Loading default datapack...");
		DatapackLoader.DEFAULT = new DatapackLoader();
		DatapackLoader.DEFAULT.loadDirectory(new File(directorys[0], "default"));

		if (!game_settings.datapack.equals("default")) {
			System.out.println("  - Loading " + game_settings.datapack + " datapack...");
			if (!loadDatapack(game_settings.datapack)) {
				datapack = DatapackLoader.DEFAULT;
			}
		} else {
			datapack = DatapackLoader.DEFAULT;
		}

		try {
			System.out.println("  - Loading langage file...");
			Lang lang = new Lang("", new FileInputStream(new File(directorys[1], game_settings.lang + ".lang")));
			lang.read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "The language file cannot be found! ", "Fatal error",
					JOptionPane.ERROR_MESSAGE);
			crash();
		}

		System.out.println("  - Loading keyboard layout...");
		KeyboardLoader.load(game_settings.keyboard_layout);

		System.out.println("  - Initialize all patch function");
		PatchManager.init();

		System.out.println("  - Starting the game window...");
		game_window = new GameWindow("Backdoor", 1280, 720);
	}

	public static boolean loadLang(String target) {
		GameSettings game_settings = GameSettings.getSettings();
		File langs_directory = new File(localDirectory(), "langs/");
		try {
			Lang lang = new Lang("", new FileInputStream(new File(langs_directory, game_settings.lang + ".lang")));
			lang.read();

			if (BackdoorGame.getDesktop() != null)
				BackdoorGame.getDesktop().reload();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<String> getLangs() {
		ArrayList<String> langs = new ArrayList<>();

		File langs_directory = new File(localDirectory(), "langs/");
		for (File file : langs_directory.listFiles()) {
			if (file.getName().endsWith(".lang")) {
				langs.add(file.getName().substring(0, file.getName().lastIndexOf(".lang")));
			}
		}
		return langs;
	}

	public static LinkedHashMap<DatapackObject, File> getDatapacks() {
		LinkedHashMap<DatapackObject, File> datapacks = new LinkedHashMap<>();

		File datapacks_directory = new File(localDirectory(), "datapacks/");
		for (File datapack_directory : datapacks_directory.listFiles()) {
			File datapack_object_file = new File(datapack_directory, "datapack.json");
			if (datapack_object_file.exists()) {
				try {
					DatapackObject datapackObject = DatapackObject.fromJson(new FileReader(datapack_object_file));

					datapacks.put(datapackObject, datapack_directory);
				} catch (Exception e) {
				}
			}
		}
		return datapacks;
	}

	public static boolean loadDatapack(String target) {
		File datapack_directory = new File(localDirectory(), "datapacks/");
		System.out.println("Loading " + target + " datapack...");
		DatapackLoader datapack = new DatapackLoader();
		File datapack_destination = new File(datapack_directory, target);
		if (!datapack_destination.exists()) {
			return false;
		}
		try {
			datapack.loadDirectory(datapack_destination);
			BackdoorGame.datapack = datapack;
			if (BackdoorGame.getDesktop() != null)
				BackdoorGame.getDesktop().reload();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void update() {
		if (debugApplication != null) {
			debugApplication.update();
		}

		if (getDesktop() != null)
			getDesktop().update(getDesktop());

		if (getDesktop() != null)
			getDesktop().update();

		for (IUpdate update : updates) {
			update.update();
		}
	}

	public static ArrayList<IUpdate> updates = new ArrayList<BackdoorGame.IUpdate>();
	public static ArrayList<IRender> renders = new ArrayList<BackdoorGame.IRender>();

	private static void render() {
		for (IRender render : renders) {
			render.render();
		}
	}

	public static void debugWindow() {
		if (debugApplication != null && desktop.containsWindow(debugApplication)) {
			return;
		}
		debugApplication = new DebugApplication();
		desktop.addWindow(debugApplication);
		debugApplication.centerLocation();
	}

	private static void initDesktop(Frame frame, int w, int h) {
		desktop = new Desktop(w, h);
		desktop.setFocusable(false);
		desktop.getListenerMap().addListener(WindowSizeEvent.class,
				(WindowSizeEventListener) event -> desktop.setSize(event.getWidth(), event.getHeight()));
		frame.getContainer().setFocusable(false);
	}

	public static FrameGrabber introduction_video_framegrabber = null;

	public static void runIntro() {
		new Thread(() -> {
			ImageView image = new ImageView();
			image.getStyle().setTop(0f);
			image.getStyle().setLeft(0f);
			image.getStyle().setBottom(0f);
			image.getStyle().setRight(0f);

			image.getStyle().setBorder(null);
			image.getStyle().setBorderRadius(0f);
			image.getStyle().setShadow(null);
			image.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1));

			VideoPlayer videoPlayer = new VideoPlayer();
			FrameGrabber fg = introduction_video_framegrabber;
			try {
				fg.start();
				if (image.getImage() == null) {
					videoPlayer.textureWidth = fg.getImageWidth();
					videoPlayer.textureHeight = fg.getImageHeight();
					videoPlayer.byteBuffer = ByteBuffer
							.allocate(videoPlayer.textureWidth * videoPlayer.textureHeight * 4);

					videoPlayer.img = new BufferedImageRGBA(videoPlayer.textureWidth, videoPlayer.textureHeight);
					videoPlayer.img.updateImageData(videoPlayer.generateEmptyImage(videoPlayer.byteBuffer));
					image.setImage(videoPlayer.img);
				}
				fg.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}

			frame.getContainer().add(image);
			image.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> {
				image.setSize(event.getWidth(), event.getHeight());
			});

			try {
				fg.restart();
				org.bytedeco.javacv.Frame f;

				try {
					final javax.sound.sampled.AudioFormat audioFormat = new javax.sound.sampled.AudioFormat(
							introduction_video_framegrabber.getSampleRate(), 16,
							introduction_video_framegrabber.getAudioChannels(), true, true);

					final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
					SourceDataLine soundLine;
					soundLine = (SourceDataLine) AudioSystem.getLine(info);
					soundLine.open(audioFormat);
					soundLine.start();

					ExecutorService executor = Executors.newSingleThreadExecutor();

					long prev = 0;
					long diff = 0;
					while ((f = fg.grab()) != null) {
						try {
							long timestamp = f.timestamp;
							diff = timestamp - prev;
							prev = timestamp;

							if (f.image != null) {
								if (f.image.length > 0 && f.image[0] instanceof ByteBuffer) {
									Mat mat = converter.convertToMat(f);
									Mat returnMatrix = new Mat(mat.rows(), mat.cols(), mat.depth(), 4);
									cvtColor(mat, returnMatrix, CV_BGR2RGBA);
									org.bytedeco.javacv.Frame ff = converter.convert(returnMatrix);

									// TimeUnit.MICROSECONDS.sleep(diff);
									videoPlayer.img.updateImageData((ByteBuffer) ff.image[0]);
								}
							} else if (f.samples != null) {
								final ShortBuffer channelSamplesShortBuffer = (ShortBuffer) f.samples[0];

								try {
									executor.submit(new Runnable() {
										public void run() {
											channelSamplesShortBuffer.rewind();

											final ByteBuffer outBuffer = ByteBuffer
													.allocate(channelSamplesShortBuffer.capacity() * 2);

											for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
												short val = channelSamplesShortBuffer.get(i);
												outBuffer.putShort(val);
											}

											soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
											outBuffer.clear();
										}
									}).get();
								} catch (InterruptedException interruptedException) {
									Thread.currentThread().interrupt();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				fg.stop();
			} catch (FrameGrabber.Exception e) {
				e.printStackTrace();
			}
			frame.getContainer().remove(image);
			startDesktop();
		}).start();
	}

	public static void init() {
		gateway = new GatewayClient(user) {
			
			@Override
			public void reconnect() throws UnknownHostException, IOException {
				super.reconnect();
				getDesktop().spawnNotification(new Notification(getDatapack().getImage("success"), Lang.get("gateway_connection_restored_title"), Lang.get("gateway_connection_restored_message"), 10));
			}
			
			@Override
			public void processPacket(Packet packet) {
				if(packet.getId() == Packet.CLIENT_DISCONNECTED) {
					PacketClientDisconnected packetDisconnected = (PacketClientDisconnected) packet;
					if(packetDisconnected.getType() == PacketClientDisconnected.KICKED) {
						if(packetDisconnected.getReason().equals("user_already_connected")){
							frame.getContainer().remove(getDesktop());
							int width = 600;
							Dialog dialog = new Dialog(Lang.get("dialog_gateway_disconnected_title"), frame.getContainer().getSize().x < width ? frame.getContainer().getSize().x - 20 : width, 50);
							dialog.getStyle().setMinWidth(dialog.getSize().x);
							dialog.getStyle().setMinHeight(dialog.getSize().y);
							
							dialog.getContainer().getStyle().setDisplay(DisplayType.FLEX);

							Text questionLabel = new Text(Lang.get("dialog_gateway_disconnected_"+packetDisconnected.getReason()));
							questionLabel.getStyle().setLeft(10f);
							questionLabel.getStyle().setTop(10f);
							questionLabel.getStyle().setRight(10f);
							questionLabel.getListenerMap().addListener(TextDynamicSizeChangeEvent.class, new EventListener<TextDynamicSizeChangeEvent>() {
								@Override
								public void process(TextDynamicSizeChangeEvent event) {
									dialog.getSize().set(dialog.getSize().x, event.getHeight() + 50);
								}
							});

							Button okButton = new Button(Lang.get("message_ok"));
							okButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
								@Override
								public void process(ButtonWidthChangeEvent event) {
									okButton.getStyle().setWidth(event.getWidth() + 20);
								}
							});

							okButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) e -> {
								if (CLICK == e.getAction()) {
									dialog.close();
									BackdoorGame.stop();
								}
							});

							okButton.getStyle().setLeft(10f);
							okButton.getStyle().setBottom(10f);
							okButton.getStyle().setHeight(20);

							dialog.getContainer().add(questionLabel);
							dialog.getContainer().add(okButton);

							dialog.load();
							questionLabel.load();
							okButton.load();

							dialog.show(BackdoorGame.frame);
						} else {
							getDesktop().dialog(Lang.get("dialog_gateway_disconnected_title"), Lang.get("dialog_gateway_disconnected_"+packetDisconnected.getReason()));
						}
					}
					return;
				}
				super.processPacket(packet);
			}
		};
		
		boolean reconnect = true;
		try {
			System.out.println("Download gateway properties.");
			Properties gateway_properties = new Properties();
			gateway_properties.load(new URL(IsotopeStudioAPI.API_URL+"gateway.info").openStream());
			System.out.println("Connection to gateway in process...");
			Integer port =  Integer.valueOf(gateway_properties.getProperty("gateway.port."+(DEV_MODE ? "dev" : GAME_VERSION.endsWith("snapshot") ? "snapshot" : "release"))); 
			gateway.connect("isotope-studio.fr", port, reconnect);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.err.println("The connection to the gateway failed");
			getDesktop().spawnNotification(new Notification(getDatapack().getImage("error"), Lang.get("gateway_connection_failed_title"), Lang.get("gateway_connection_failed_message"), 10));
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("The connection to the gateway failed");
			getDesktop().spawnNotification(new Notification(getDatapack().getImage("error"), Lang.get("gateway_connection_failed_title"), Lang.get("gateway_connection_failed_message"), 10));
		}
		
	}

	public static void preinit() {
	}

	public static void start() {
		ICommand.listenJavaConsole().start();

		VideoSettings video_settings = VideoSettings.getSettings();
		GameSettings game_settings = GameSettings.getSettings();
		
		preinit();

		if (!ignoreintro) {
			System.out.println("Loading the introductory video...");
			introduction_video_framegrabber = new FFmpegFrameGrabber(new File(getResourcesDirectory(), "intro.mp4"));
		}
		
		System.out.println("Creating game window...");
		game_window.create();
		if (video_settings.fullscreen) {
			game_window.toggleFullscreen();
		}

		frame = new Frame(game_window.getWindowSize()[0], game_window.getWindowSize()[1]);
		frame.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		DefaultInitializer initializer = new DefaultInitializer(game_window.getID(), frame);

		GLFWKeyCallbackI glfwKeyCallbackI = (w1, key, code, action,
				mods) -> game_window.running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
		GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> game_window.running = false;

		initializer.getCallbackKeeper().getChainKeyCallback().add(new GLFWKeyCallbackI() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action != GLFW.GLFW_RELEASE)
					return;
				if (key == Keyboard.getNativeCode(KeyCode.KEY_F5)) {
					desktop.reload();
				}
				if (key == Keyboard.getNativeCode(KeyCode.KEY_F10)) {
					reloadFramework();
				}
				if (key == Keyboard.getNativeCode(KeyCode.KEY_F11)) {
					getGameWindow().toggleFullscreen = true;
				}
				if (key == Keyboard.getNativeCode(KeyCode.KEY_F1)) {
					if (java_terminal != null && getDesktop().containsWindow(java_terminal)) {
						getDesktop().removeWindow(java_terminal);
						return;
					}
					openJavaTerminal();
				}
			}
		});
		initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
		initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);

		Renderer renderer = new NvgRenderer(video_settings.antialiasing);
		Animator animator = AnimatorProvider.getAnimator();

		game_window.context = initializer.getContext();
		game_window.running = true;

		RenderManager.init();

		System.out.println("renderer initialization...");
		renderer.initialize();

		System.out.println("Checking if you using a account.");
		if (user == null) {
			getDesktop().dialog(Lang.get("you_are_not_looged_in_to_an_account"),
					Lang.get("you_were_not_authenticated_when_the_game_started"));
			System.exit(IsotopeStudioAPI.EXIT_CODE_EXIT);
		}

		if (!ignoreintro) {
			System.out.println("Starting the introductory video...");
			runIntro();
		} else {
			startDesktop();
		}
		
		long initialTime = System.nanoTime();
		double deltaU = 0, deltaF = 0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();
		
		init();

		while (!getGameWindow().shouldClose()) {
			final double timeU = 1000000000 / game_settings.refresh_rate;
			final double timeF = 1000000000 / video_settings.frames_limit;

			long currentTime = System.nanoTime();
			deltaU += (currentTime - initialTime) / timeU;
			deltaF += (currentTime - initialTime) / timeF;
			initialTime = currentTime;

			game_window.context.updateGlfwWindow();
			Vector2i windowSize = game_window.context.getFramebufferSize();

			boolean frame_limiter = video_settings.frames_limiter;

			if (deltaU >= 1) {
				initializer.getSystemEventProcessor().processEvents(frame, game_window.context);
				initializer.getGuiEventProcessor().processEvents();
				update();

				glfwPollEvents();

				ticks++;

				deltaU--;
			}

			if (!frame_limiter || (frame_limiter && deltaF >= 1)) {
				glClearColor(0, 0, 0, 1);
				glViewport(0, 0, windowSize.x, windowSize.y);
				// Clear screen
				glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

				renderer.render(frame, game_window.context);
				render();

				glfwPollEvents();
				glfwSwapBuffers(game_window.getID());
				animator.runAnimations();

				if (game_window.toggleFullscreen) {
					game_window.toggleFullscreen();
				}

				LayoutManager.getInstance().layout(frame);

				frames++;

				if (frame_limiter)
					deltaF--;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				FPS = frames;
				TPS = ticks;

				frames = 0;
				ticks = 0;
				timer += 1000;
			}

			Thread.yield();

			if (System.currentTimeMillis() - last_time_online > 5000) { // 5 seconds
				long currenttime = System.currentTimeMillis();
				last_time_online = currenttime;
				new Thread(new Runnable() {
					@Override
					public void run() {
						UserAPI userAPI = new UserAPI(IsotopeStudioAPI.API_URL + "/");
						Long time = null;
						try {
							long start = System.currentTimeMillis();
							if ((time = userAPI.online(getUser())) != null) {
								PING = System.currentTimeMillis() - start;
								return;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.err.println("A problem occurred when to define you online!");
					}
				}).start();
			}
		}

		game_window.dispose();
		System.exit(0);
	}

	private static long last_time_online = 0;

	private static void startDesktop() {
		int[] windowSize = BackdoorGame.getGameWindow().getWindowSize();

		System.out.println("Creating desktop...");
		initDesktop(frame, windowSize[0], windowSize[1]);

		System.out.println("Desktop initialization...");
		desktop.init();

		frame.getContainer().add(desktop);
		desktop.ready();
	}

	private static void reloadFramework() {
		frame.getContainer().remove(getDesktop());
		startDesktop();
	}

	public static void openJavaTerminal() {
		if (java_terminal != null && getDesktop().containsWindow(java_terminal)) {
			return;
		}
		java_terminal = new JavaTerminalApplication();
		desktop.addWindow(java_terminal);
		java_terminal.centerLocation();
	}

	public static void restart() {
		System.exit(IsotopeStudioAPI.EXIT_CODE_RESTART);
	}

	public static void crash() {
		System.exit(IsotopeStudioAPI.EXIT_CODE_CRASH);
	}

	public static void stop() {
		System.exit(IsotopeStudioAPI.EXIT_CODE_EXIT);
	}

	public static File localDirectory() {
		try {
			File file = new File(BackdoorGame.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getParentFile();
			return file;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int connect(String address, int port, String password) {
		System.out.println("Connecting to " + address);
		BackdoorGame.game_client = new GameClient(address, port);
		try {
			int result = BackdoorGame.game_client.connect(BackdoorGame.user, password);
			if (result == 0) {
				System.out.println("Connected on the server: " + address + ":" + port);
			} else if (result == -1) {
				System.err.println("Failure to connect the server!");
			} else {
				System.err.println("Failure to connect the server!");
				System.err.println("> " + result);
			}
			return result;
		} catch (UnknownHostException e) {
			System.err.println("Unable to connect to the server " + address + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failure to connect the server!");
		}
		return -1;
	}

	public static DatapackLoader getDatapack() {
		return datapack;
	}

	public static Frame getFrame() {
		return frame;
	}

	public static GameWindow getGameWindow() {
		return game_window;
	}

	public static Desktop getDesktop() {
		return desktop;
	}

	public static Gson getGson() {
		return GsonInstance.instance();
	}

	public static Logger getLogger() {
		return logger;
	}

	public static GameClient getGameClient() {
		return game_client;
	}

	public static GameParty getGameParty() {
		return game_party;
	}

	public static Profile getUserProfile() {
		return user_profile;
	}

	public static File getCacheDirectory() {
		return new File(localDirectory(), "cache");
	}

	public static File getResourcesDirectory() {
		return new File(localDirectory(), "resources");
	}

	public static User getUser() {
		return user;
	}

	public static abstract class IUpdate {
		public abstract void update();
	}

	public static abstract class IRender {
		public abstract void render();
	}

	public static StbBackedLoadableImage loadImageURL(String url_string) {
		StbBackedLoadableImage image = new StbBackedLoadableImage(url_string);
		return image;
	}

	public static File downloadImage(String url_string) throws IOException {
		URL url = new URL(url_string);
		File destination = new File(getCacheDirectory(), UUID.randomUUID() + ".png");

		if (!destination.getParentFile().exists()) {
			destination.getParentFile().mkdirs();
		}

		if (destination.exists())
			destination.delete();

		destination.createNewFile();

		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destination.getPath());

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();

		return destination;
	}
	
	/**
	 * @return the gateway
	 */
	public static GatewayClient getGateway() {
		return gateway;
	}

	/**
	 * @return the directory datapacks
	 */
	public static File getDatapackDirectory() {
		return new File(localDirectory(), "datapacks/");
	}
}
