package com.robhallam.flappy;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window; // "ids" instead of objects - no objects in C
	
	public void start() {
		running = false;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		gl
	}
	
	public void run() {
		init(); //init and render need to be on same thread
		while (running) {
			update();
			render();
		}
	}
	
	private void update() {
		
	}
	
	private void render() {
		
	}
	
	public static void main(String[] args) {
		new Main().start();

	}

}
