package com.robhallam.flappy.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {
	
	public static boolean[] keys = new boolean[65536];
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action == GLFW.GLFW_RELEASE ? false : true; // REPEAT (hold down) - fired on PRESS (RELEASE only event fired when we release)
		
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

}
