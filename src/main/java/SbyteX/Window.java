package SbyteX;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;
    public float r, g, b, a;
    private boolean fadeToBlack = false;
    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Sbyte Window";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 -> {
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
            }
            case 1 -> {
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
            }
            default -> {
                assert false : "Unknown scene: " + newScene;
            }
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window =  new Window();
        }

        return  Window.window;
    }

    public void run() {

        init();
        loop();

        //free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
    }

    public void init() {
        //error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Error glfwInit");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);


        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Error creating window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);

        glfwMakeContextCurrent(glfwWindow); //context
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities(); //give glfw access to context

        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
