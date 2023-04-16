package SbyteX;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private float minView;
    private float maxView;
    private Matrix4f projectionMatrix;
    private Matrix4f  viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position, float minView, float maxView) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.minView = minView;
        this.maxView = maxView;
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        //zNear Min view, zFar Ma view
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f ,32.0f * 21.0f, minView, maxView);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
