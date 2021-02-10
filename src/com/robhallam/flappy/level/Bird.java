package com.robhallam.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

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
				-SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
				-SIZE / 2.0f, SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f, SIZE / 2.0f, 0.2f,
			  	SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
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
		position.y -= delta;
		if (Input.isKeyDown(GLFW_KEY_SPACE)) {
			delta = -0.15f;
		}
		else {
			delta += 0.01f;
		}
		
		rot = -delta * 90.0f;
	}
	
	private void fall() {
		delta = -0.15f;
	}
	
	public void render() {
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}
	
}
