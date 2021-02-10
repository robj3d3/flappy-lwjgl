package com.robhallam.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.robhallam.flappy.graphics.Shader;
import com.robhallam.flappy.input.Input;
import com.robhallam.flappy.level.Level;
import com.robhallam.flappy.math.Matrix4f;

public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window; // "ids" instead of objects - no objects in C
	
	private Level level;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL); // creates context in GLFW
		
		if (window == NULL) { 
			return;
		}
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2); // positions in center - in OOP would look like window.setPos(xpos, ypos)
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		GL.createCapabilities(); // tell LWJGL that OpenGL context exists
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Shader.BG.enable();
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.disable();
		
		level = new Level();
	}
	
	public void run() {
		init(); // init and render need to be on same thread
		while (running) {
			update();
			render();
			
			if (glfwWindowShouldClose(window)) {
				running = false;
			}
		}
		
	}
	
	private void update() {
		glfwPollEvents(); // key events
//		if (Input.keys[GLFW_KEY_SPACE]) {
//			System.out.println("FLAP!");
//		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
//		int i = glGetError();
//		if (i != GL_NO_ERROR) {
//			System.out.println(i);
//		}
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		new Main().start();

	}

}
