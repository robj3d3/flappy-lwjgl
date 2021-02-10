package com.robhallam.flappy.level;

import org.lwjgl.glfw.GLFW;

import com.robhallam.flappy.graphics.Shader;
import com.robhallam.flappy.graphics.Texture;
import com.robhallam.flappy.graphics.VertexArray;
import com.robhallam.flappy.input.Input;
import com.robhallam.flappy.math.Matrix4f;
import com.robhallam.flappy.math.Vector3f;

public class Bird {
	
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rot; // Only going to be z-rotation so no need for matrix
	private float delta = 0.0f;
	
	public Bird() {
		float[] vertices = new float[] {
				-SIZE / 2.0f, -SIZE / 2.0f, 0.1f,
				-SIZE / 2.0f, SIZE / 2.0f, 0.1f,
				 SIZE / 2.0f, SIZE / 2.0f, 0.1f,
			  	SIZE / 2.0f, -SIZE / 2.0f, 0.1f,
		};
		
		byte[] indices = new byte[] { // Stop us from making redundant vertices by reusing same vertices
				0, 1, 2, // First triangle
				2, 3, 0 // Second triangle
		};
		
		float[] tcs = new float[] {
				0, 1,
				0, 0,
				1, 0,
				1, 1
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("res/bird.png");
	}
	
	public void update() {
		if (Input.keys[GLFW.GLFW_KEY_UP]) {
			position.y += 0.1f; 
		}
		if (Input.keys[GLFW.GLFW_KEY_DOWN]) {
			position.y -= 0.1f; 
		}
		if (Input.keys[GLFW.GLFW_KEY_LEFT]) {
			position.x -= 0.1f; 
		}
		if (Input.keys[GLFW.GLFW_KEY_RIGHT]) {
			position.x += 0.1f; 
		}
		
	}
	
	public void render() {
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}
	
}
