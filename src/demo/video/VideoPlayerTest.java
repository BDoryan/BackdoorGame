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
import isotopestudio.backdoor.utils.OggClip.InternalException;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class VideoPlayerTest extends OggClip {

	private File directory;
	private List<BufferedImage> images;

	public VideoPlayerTest(File directory) throws FileNotFoundException, IOException {
		super(new FileInputStream(new File(directory, "audio.ogg")));
		this.directory = directory;
		this.setGain(0.9f);
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
	}

	/**
	 * Play the clip once
	 */
	@Override
	public void play() {
		stop();

		try {
			bitStream.reset();
		} catch (IOException e) {
			// ignore if no mark
		}

		player = new Thread() {
			public void run() {
				try {
					playStream(Thread.currentThread());
				} catch (InternalException e) {
					e.printStackTrace();
				}

				try {
					bitStream.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};
		player.setDaemon(true);
		player.start();
	}

	private boolean running = false;
	private int current_frame = 0;

	/*
	 * Taken from the JOrbis Player
	 */
	@Override
	public void playStream(Thread me) throws InternalException {
		boolean chained = false;

		initJOrbis();

		this.running = true;

		long lastRenderTime = System.nanoTime();

		int frames = 0;

		long timer = System.currentTimeMillis();
		long starter = timer;

		double renderTime = 1000000000 / rate;

		while (true) {
			played.set(true);

			if (checkState()) {
				return;
			}

			int eos = 0;

			int index = oy.buffer(BUFSIZE);
			buffer = oy.data;
			try {
				bytes = bitStream.read(buffer, index, BUFSIZE);
			} catch (Exception e) {
				throw new InternalException(e);
			}
			oy.wrote(bytes);

			if (chained) {
				chained = false;
			} else {
				if (oy.pageout(og) != 1) {
					if (bytes < BUFSIZE)
						break;
					throw new InternalException("Input does not appear to be an Ogg bitstream.");
				}
			}
			os.init(og.serialno());
			os.reset();

			vi.init();
			vc.init();

			if (os.pagein(og) < 0) {
				// error; stream version mismatch perhaps
				throw new InternalException("Error reading first page of Ogg bitstream data.");
			}

			if (os.packetout(op) != 1) {
				// no page? must not be vorbis
				throw new InternalException("Error reading initial header packet.");
			}

			if (vi.synthesis_headerin(vc, op) < 0) {
				// error case; not a vorbis header
				throw new InternalException("This Ogg bitstream does not contain Vorbis audio data.");
			}

			int i = 0;

			while (i < 2) {
				while (i < 2) {
					if (checkState()) {
						return;
					}

					int result = oy.pageout(og);
					if (result == 0)
						break; // Need more data
					if (result == 1) {
						os.pagein(og);
						while (i < 2) {
							result = os.packetout(op);
							if (result == 0)
								break;
							if (result == -1) {
								throw new InternalException("Corrupt secondary header.  Exiting.");
							}
							vi.synthesis_headerin(vc, op);
							i++;
						}
					}
				}

				index = oy.buffer(BUFSIZE);
				buffer = oy.data;
				try {
					bytes = bitStream.read(buffer, index, BUFSIZE);
				} catch (Exception e) {
					throw new InternalException(e);
				}
				if (bytes == 0 && i < 2) {
					throw new InternalException("End of file before finding all Vorbis headers!");
				}
				oy.wrote(bytes);
			}

			convsize = BUFSIZE / vi.channels;

			vd.synthesis_init(vi);
			vb.init(vd);

			float[][][] _pcmf = new float[1][][];
			int[] _index = new int[vi.channels];

			getOutputLine(vi.channels, vi.rate);

			while (eos == 0) {
				while (eos == 0) {
					if (player != me) {
						return;
					}

					int result = oy.pageout(og);
					if (result == 0)
						break; // need more data
					if (result == -1) { // missing or corrupt data at this page
						// position
						// System.err.println("Corrupt or missing data in
						// bitstream;
						// continuing...");
					} else {
						os.pagein(og);

						if (og.granulepos() == 0) { //
							chained = true; //
							eos = 1; //
							break; //
						} //

						while (true) {
							if (checkState()) {
								return;
							}

							result = os.packetout(op);
							if (result == 0)
								break; // need more data
							if (result == -1) { // missing or corrupt data at
								// this page position
								// no reason to complain; already complained
								// above

								// System.err.println("no reason to complain;
								// already complained above");
							} else {
								// we have a packet. Decode it
								int samples;
								if (vb.synthesis(op) == 0) { // test for
									// success!
									vd.synthesis_blockin(vb);
								}
								while ((samples = vd.synthesis_pcmout(_pcmf, _index)) > 0) {
									if (checkState()) {
										return;
									}

									float[][] pcmf = _pcmf[0];
									int bout = (samples < convsize ? samples : convsize);

									// convert doubles to 16 bit signed ints
									// (host order) and
									// interleave
									for (i = 0; i < vi.channels; i++) {
										int ptr = i * 2;
										// int ptr=i;
										int mono = _index[i];
										for (int j = 0; j < bout; j++) {
											int val = (int) (pcmf[i][mono + j] * 32767.);
											if (val > 32767) {
												val = 32767;
											}
											if (val < -32768) {
												val = -32768;
											}
											if (val < 0)
												val = val | 0x8000;
											convbuffer[ptr] = (byte) (val);
											convbuffer[ptr + 1] = (byte) (val >>> 8);
											ptr += 2 * (vi.channels);
										}
									}

									if ((System.nanoTime() - lastRenderTime > renderTime)) {
										lastRenderTime += renderTime;
										if (current_frame >= images.size()) {
											running = false;
										} else {
											if (current_frame == 0) {
												starter = System.currentTimeMillis();
											}
											// render image
											current_frame++;
											renderer.run();
										}
										frames++;

										if (System.currentTimeMillis() - timer > 1000) {
											timer += 1000;
											System.out.println("fps: " + frames);
											frames = 0;
										}
									}
									outputLine.write(convbuffer, 0, 2 * vi.channels * bout);
									vd.synthesis_read(bout);
								}
							}
						}
						if (og.eos() != 0)
							eos = 1;
					}
				}

				if (eos == 0) {
					index = oy.buffer(BUFSIZE);
					buffer = oy.data;
					try {
						bytes = bitStream.read(buffer, index, BUFSIZE);
					} catch (Exception e) {
						throw new InternalException(e);
					}
					if (bytes == -1) {
						break;
					}
					oy.wrote(bytes);
					if (bytes == 0)
						eos = 1;
				}
			}

			os.clear();
			vb.clear();
			vd.clear();
			vi.clear();
		}

		played.set(false);
		oy.clear();
		System.out.println(new SimpleDateFormat("ss.SSSSSS").format(System.currentTimeMillis() - starter));
	}

	private int rate = 60;
	private Runnable renderer;

	public void play(int rate, Runnable renderer) {
		this.rate = rate;
		this.renderer = renderer;

		play();
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
