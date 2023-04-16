package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean inUse = false;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index); // windows, linux \n
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            }
            else if(firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            }
            else {
                throw new IOException("Unknown property: " + firstPattern +
                        " in: " + filepath);
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            }
            else if(secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            }
            else {
                throw new IOException("Unknown property: " + secondPattern +
                        " in: " + filepath);
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error opening file shader: " + filepath;
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //check for compile err
        int succes = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Shader Error: " + filepath + " \nCompilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //fragment

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //check for compile err
        succes = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Shader Error: " + filepath + " \nCompilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //link shader
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //check for link error
        succes = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (succes == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Shader Error: " + filepath + " \nLink failed");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use() {
        //bind shader
        if (!inUse) {
            glUseProgram(shaderProgramID);
            inUse = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void UploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z , vec.w);
    }

    public void UploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void UploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void UploadFloat(String varName, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, value);
    }

    public void UploadInt(String varName, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, value);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
