package SbyteX;

import util.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    public Scene() {
        Logger.log("Scene Created", 0);
    }
    public void init() {}

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
        }
    }

    public abstract void update(float delta);
}
