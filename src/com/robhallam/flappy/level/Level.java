package com.robhallam.flappy.level;

import java.util.Random;

import com.robhallam.flappy.graphics.Shader;
import com.robhallam.flappy.graphics.Texture;
import com.robhallam.flappy.graphics.VertexArray;
import com.robhallam.flappy.input.Input;
import com.robhallam.flappy.math.Matrix4f;
import com.robhallam.flappy.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Level {
	
	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0; // Horizontal scroll amount
	private int map = 0;
	
	private Bird bird;
	
	private Pipe[] pipes = new Pipe[5 * 2]; // 5 on the top, 5 on the bottom
	private int index = 0;
	private float OFFSET = 5.0f;
	private boolean control = true, reset = false;
	
	private Random random = new Random();

	private float time = 0.0f;
	
	public Level() {
		float[] vertices = new float[] {
				-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
				-10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
				0.0f, 10.0f * 9.0f / 16.0f, 0.0f,
				0.0f, -10.0f * 9.0f / 16.0f, 0.0f
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

		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("res/bg.jpeg");

		bird = new Bird();
		
		createPipes();
	}
	
	private void createPipes() { // Instantiates Pipe objects
		Pipe.create();
		for (int i = 0; i < 5 * 2; i+= 2) {
			pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			pipes[i+1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.0f); // getX so aligned horizontally
			index += 2;
		}
	}
	private void updatePipes() {
		pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
		pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.0f);
		index += 2; // Essentially telling us how far along we are
	}
	
	public void update() {
		if (control) {
			xScroll--; // -ve because moving map left
			if (-xScroll % 335 == 0) map++;
			if (-xScroll > 250 && -xScroll % 120 == 0) updatePipes();
		}
		
		bird.update();
		
		if (control && collision()) {
			bird.fall();
			control = false; // Player's lost control of the bird
		}
		
		if (!control && Input.isKeyDown(GLFW_KEY_SPACE)) {
			reset = true;
		}
		
		time += 0.01f;
	}
	
	private void renderPipes() {
		Shader.PIPE.enable();
		Shader.PIPE.setUniform2f("bird", 0, bird.getY());
		Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
		Pipe.getTexture().bind();
		Pipe.getMesh().bind(); // Not calling render(), instead calling bind() and draw() as with BG
		
		for (int i = 0; i < 5 * 2; i++) {
			Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
			Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			Pipe.getMesh().draw();
		}
		
		Pipe.getMesh().unbind();
		Pipe.getTexture().unbind();
	}
	
	private boolean collision() {
		for (int i = 0; i < 5 * 2; i++) {
			float bx = -xScroll * 0.05f;
			float by = bird.getY();
			float px = pipes[i].getX();
			float py = pipes[i].getY();
			
			float bx0 = bx - bird.getSize() / 2.0f;
			float bx1 = bx + bird.getSize() / 2.0f;
			float by0 = by - bird.getSize() / 2.0f;
			float by1 = by + bird.getSize() / 2.0f;
			
			float px0 = px;
			float px1 = px + Pipe.getWidth();
			float py0 = py;
			float py1 = py + Pipe.getHeight();
			
			if (bx1 > px0 && bx0 < px1) { // Inside pipe horizontally
				if (by1 > py0 && by0 < py1) { // Inside pipe vertically
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isGameOver() {
		return reset;
	}
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		Shader.BG.setUniform2f("bird", 0, bird.getY());
		background.bind(); // Only want to bind once
		for (int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			background.draw(); // don't want to bind-draw-bind-draw-bind... so don't use render, just bind-draw-draw-dr...
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}
}
