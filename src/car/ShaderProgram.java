package car;

import com.jogamp.opengl.GL2;

public class ShaderProgram {
    private int programID;
    
    public ShaderProgram(GL2 gl, String vertexSource, String fragmentSource) {
        // Compile the vertex and fragment shaders.
        int vertexShader = compileShader(gl, GL2.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(gl, GL2.GL_FRAGMENT_SHADER, fragmentSource);
        programID = gl.glCreateProgram();
        gl.glAttachShader(programID, vertexShader);
        gl.glAttachShader(programID, fragmentShader);
        gl.glLinkProgram(programID);
        
        // Error checking omitted for brevity.
    }
    
    private int compileShader(GL2 gl, int type, String source) {
        int shader = gl.glCreateShader(type);
        gl.glShaderSource(shader, 1, new String[]{source}, null);
        gl.glCompileShader(shader);
        // Check compile status here.
        return shader;
    }
    
    public void use(GL2 gl) {
        gl.glUseProgram(programID);
    }
    
    public void stop(GL2 gl) {
        gl.glUseProgram(0);
    }
}