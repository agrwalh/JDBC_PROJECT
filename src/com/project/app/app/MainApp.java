package com.project.app.app;

import com.project.app.controller.AppController;

// Entry point of the application
// Only job: create AppController and call run()
// All menu logic, input reading, validation is in the controller package
public class MainApp {

	public static void main(String[] args) {
		AppController controller = new AppController();
		controller.run();
	}
}