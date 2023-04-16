package SbyteX;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Logger;
import util.Time;

import javax.swing.text.html.HTMLDocument;
import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;


public class LevelEditorScene extends Scene {
    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            // position               // color                  //UV
              100f,    0f,  0.0f,    1.0f, 0.0f, 0.0f, 1.0f,   1, 1,  // Bottom right 0
                0f,   100f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,   0, 0,  // Top left     1
              100f,   100f, 0.0f,    1.0f, 0.0f, 1.0f, 1.0f,   1, 0,  // Top right    2
                0f,     0f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f,   0, 1   // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = false;
    public LevelEditorScene() {
        super();

    }

    @Override
    public void init() {
        Logger.log("Creating test obj", 0);
        this.testObj = new GameObject("Test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(), 0.0f, 100.0f);

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        this.testTexture = new Texture("assets/images/testImage.png");

        // generate VAO, VBO, EBO and send to gpu
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create vbo upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int floatSizeBytes= Float.BYTES;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);


        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
                (positionsSize + colorSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float delta) {

        int speed = 180;

        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) {
            camera.position.y -= speed * delta;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) {
            camera.position.y += speed * delta;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            camera.position.x -= speed * delta;
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            camera.position.x += speed * delta;
        }

        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.UploadFloat("uTime", Time.getTime());

        //bind vao
        glBindVertexArray(vaoID);
        //enable vertex attrib pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if (!firstTime) {
            Logger.log("Creating GameObject 2", 0);
            GameObject go = new GameObject("Game test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for (GameObject go : this.gameObjects) {
            go.update(delta);
        }
    }
}
