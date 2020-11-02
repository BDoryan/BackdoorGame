package demo.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import isotopestudio.backdoor.utils.OggClip;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class VideoPlayer {

	private File directory;
	private List<BufferedImage> images;

	public VideoPlayer(File directory) {
		this.directory = directory;
	}

	private OggClip audio;

	public void load() {
		this.images = new ArrayList<>();
		for (File file : directory.listFiles()) {
			if (file.getName().endsWith(".jpg")) {
				try {
					images.add(ImageIO.read(file));
					System.out.println(file.getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		File audio_file = new File(directory, "audio.ogg");
		if (audio_file.exists()) {
			try {
				audio = new OggClip(new FileInputStream(audio_file));
				audio.setGain(0.9f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean running = false;
	private boolean pause = false;
	private int current_frame = 0;

	public void play(int rate, Runnable renderer) {
		this.running = true;
		this.pause = false;

		long lastRenderTime = System.nanoTime();

		int frames = 0;

		long timer = System.currentTimeMillis();

		if (audio != null) {
			audio.play();
		}

		long starter = timer;
		
		while (running) {
			if (!pause) {
				double renderTime = 1000000000 / rate;

				if ((System.nanoTime() - lastRenderTime > renderTime)) {
					lastRenderTime += renderTime;
					if (current_frame >= images.size()) {
						running = false;
						break;
					} else {
						if(current_frame == 0){
							starter = System.currentTimeMillis();
						}
						// render image
						current_frame++;
						renderer.run();
					}
					frames++;
				}

				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					System.out.println("fps: "+frames);
					frames = 0;
				}
			}
		}
		System.out.println(new SimpleDateFormat("ss.SSSSSS").format(System.currentTimeMillis() - starter));
	}

	public BufferedImage image() {
		if (current_frame > images.size() - 1 || current_frame < 0) {
			running = false;
			return images.get(images.size() - 1);
		}
		BufferedImage value = images.get(current_frame);
		return value;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @return running value
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return
	 */
	public int getCurrentFrame() {
		return current_frame;
	}
}
