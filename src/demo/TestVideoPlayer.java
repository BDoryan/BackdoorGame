package demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import demo.video.VideoPlayer;
import doryanbessiere.isotopestudio.commons.LocalDirectory;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class TestVideoPlayer extends JPanel {

	public static VideoPlayer player;

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.setTitle("Test video player");
		jframe.setSize(1280, 720);
		jframe.setLocationRelativeTo(null);
		jframe.setLayout(null);
		TestVideoPlayer testVideoPlayer;
		jframe.setContentPane((testVideoPlayer = new TestVideoPlayer()));
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					testVideoPlayer.repaint();
				}
			}
		}).start();

		player = new VideoPlayer(new File(LocalDirectory.toFile(TestVideoPlayer.class), "datas/intro"));
		player.load();
		player.play(59, new Runnable() {
			int current_frame = -1;

			@Override
			public void run() {
				if (player.getCurrentFrame() != current_frame) {
					BufferedImage image_drawed = player.image();
					testVideoPlayer.setImage(image_drawed);
					current_frame = player.getCurrentFrame();
				}
			}
		});
	}

	private BufferedImage image;

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (image != null)
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
