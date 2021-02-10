package com.robhallam.flappy.level;

import com.robhallam.flappy.graphics.Shader;
import com.robhallam.flappy.graphics.Texture;
import com.robhallam.flappy.graphics.VertexArray;
import com.robhallam.flappy.math.Matrix4f;
import com.robhallam.flappy.math.Vector3f;

public class Level {
	
	private VertexArray background;
	private Texture bgTexture;
	
	private int xScroll = 0; // Horizontal scroll amount
	private int map = 0;
	
	private Bird bird;
	
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
		
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("res/bg.jpeg");
		
		bird = new Bird();
	}
	
	public void update() {
		xScroll--; // -ve because moving map left
		if (-xScroll % 335 == 0) map++;
		
		bird.update();
	}
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		background.bind(); // Only want to bind once
		for (int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			background.draw(); // don't want to bind-draw-bind-draw-bind... so don't use render, just bind-draw-draw-dr...
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
		bird.render();
	}
}
