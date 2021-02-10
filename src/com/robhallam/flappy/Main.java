package com.robhallam.flappy;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.robhallam.flappy.input.Input;

public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window; // "ids" instead of objects - no objects in C
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
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
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		new Main().start();

	}

}
