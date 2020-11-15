package isotopestudio.backdoor.engine.video;

import java.nio.ByteBuffer;

import org.liquidengine.legui.image.BufferedImageRGBA;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class VideoPlayer {
	
	public BufferedImageRGBA img;
    public int textureWidth;
    public int textureHeight;
    public int color;
    
    public ByteBuffer byteBuffer;

    public ByteBuffer generateImage(ByteBuffer buffer) {
        // copy 4 bytes at once
        for (int i = 0; i < textureHeight; ++i) {
            for (int j = 0; j < textureWidth; ++j) {
                int c = (color / 10 + i * j) % 200 + (color % 1000) / 100;
                buffer.put((textureWidth * i + j) * 4 + 0, (byte) (c >>> 24));
                buffer.put((textureWidth * i + j) * 4 + 1, (byte) (c >>> 16));
                buffer.put((textureWidth * i + j) * 4 + 2, (byte) (c >>> 8));
                buffer.put((textureWidth * i + j) * 4 + 3, (byte) (c));
            }
        }
        color++;
        buffer.rewind();
        return buffer;
    }

    public ByteBuffer generateEmptyImage(ByteBuffer buffer) {
        // copy 4 bytes at once
        for (int i = 0; i < textureHeight; ++i) {
            for (int j = 0; j < textureWidth; ++j) {
                buffer.put((textureWidth * i + j) * 4 + 0, (byte) 0);
                buffer.put((textureWidth * i + j) * 4 + 1, (byte) 0);
                buffer.put((textureWidth * i + j) * 4 + 2, (byte) 0);
                buffer.put((textureWidth * i + j) * 4 + 3, (byte) 0);
            }
        }
        buffer.rewind();
        return buffer;
    }
}
