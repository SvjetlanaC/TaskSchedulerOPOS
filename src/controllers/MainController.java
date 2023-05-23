package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import taskScheduler.TaskScheduler;
import tasks.IllegalTaskStateException;

public class MainController implements Initializable{

	
	 @FXML
	 private TextField numberOfCoresTextField;
	 @FXML
     private TextField numberOfTasksTextField;
	 
	 @FXML
	 private Button potvrdiButton;
	 
	 
	 boolean restored=false;
	 static int numberOfCores;

	 @FXML
	 void potvrdi(ActionEvent event) {
		 int cores = Runtime.getRuntime().availableProcessors();
		 System.out.println(cores);
		 
		 numberOfCores=Integer.parseInt(numberOfCoresTextField.getText());
		 int numberOfTasks=Integer.parseInt(numberOfTasksTextField.getText());
		 
		 if(numberOfCores>0 && numberOfTasks>0) {
			 try {
				Main.Scheduler = new TaskScheduler(numberOfCores, numberOfTasks);
				System.out.println("Restored: " + restored);
	             try{
	                 FXMLLoader loader = new FXMLLoader(SchedulerPageController.class.getResource("/SchedulerPage.fxml"));
	                 Scene scene = new Scene(loader.load());
	                 Stage stage = new Stage();
	                 stage.setTitle("Raspoređivač zadataka");
	                 SchedulerPageController.stage = stage;
	                 SchedulerPageController.restored = restored;
	                 stage.setScene(scene);
	                 if(SchedulerPageController.stage != null)
	                	 SchedulerPageController.stage.close();
	                 stage.show();
	             } catch (IOException e){
	                 e.printStackTrace();
	             }
			} catch (IllegalTaskStateException e1) {
				e1.printStackTrace();
			}
             
         } else { 
        	 throw new IllegalArgumentException("Arguments are not valid!");
         }
    }
	public static Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		numberOfCoresTextField.setText(Main.numberOfCores.toString());
		numberOfTasksTextField.setText(Main.numberOfTasks.toString());
		
	}
}
