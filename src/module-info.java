module TaskSchedulerOPOS {
	
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.controls;
	exports controllers;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controllers to javafx.graphics, javafx.fxml;
}
