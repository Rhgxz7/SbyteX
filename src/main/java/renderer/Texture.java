package renderer;

import org.lwjgl.BufferUtils;
import util.Logger;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private String filepath;
    private int texID;
    public Texture(String filepath) {
        this.filepath = filepath;

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // texture params
        //repeat image
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); //x axis
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); //y axis

        //stretch pixelate instead of blur
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //shrink pixelate not blur
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                String errMsg = "Error Loading texture image, unknown channels: " + filepath;
                Logger.log(errMsg, 3);

                assert false : "Error Loading texture image, unknown channels: " + filepath;
            }
        }
        else {
            String errMsg = "Error Loading texture image, can't load: " + filepath;
            Logger.log(errMsg, 3);

            assert false : "Error Loading texture image, can't load: " + filepath;
        }

        stbi_image_free(image); //avoid memory leak
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
