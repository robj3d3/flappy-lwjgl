package com.robhallam.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.robhallam.flappy.math.Matrix4f;
import com.robhallam.flappy.math.Vector3f;
import com.robhallam.flappy.utils.ShaderUtils;

public class Shader {
	
	// Attributes are similar to Uniforms except they're set every single vertex
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>(); // caches location - doesn't change, no need to keep checking
	
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		
		// Running multiple times per FRAME - big bottleneck is communication between GPU + CPU
		int result = glGetUniformLocation(ID, name);
		if (result == -1) {
			System.err.println("Could not find uniform variable '" + name + "' !");
		}
		else {
			locationCache.put(name, result);
		}
		
		return result;
	}
	
	public void setUniform1i(String name, int value) {
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y) {
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		// OpenGL wants column-major (our matrices are, thus transpose=false)
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	public void enable() {
		glUseProgram(ID);
	}
	
	public void disable() {
		glUseProgram(0);
	}

}
