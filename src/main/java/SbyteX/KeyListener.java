package SbyteX;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean KeyPressed[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }
    public static void keyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().KeyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE) {
            get().KeyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().KeyPressed[keyCode];
    }
}
