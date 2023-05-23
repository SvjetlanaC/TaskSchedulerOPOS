package application;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import taskScheduler.TaskScheduler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
	public static TaskScheduler Scheduler;

    // putanje do foldera u kojima se nalaze/smještaju resursi
    public static String putanjaUlaznogFoldera;
    public static String IntermediateFolderPath;
    public static String putanjaIzlaznogFoldera;
    // putanja serijalizovanih zadataka
    public static String putanjaSerijalizovanihZadataka;

    // podrazumijevane vrijednosti raspoređivača
    public static Integer numberOfTasks;
    public static Integer numberOfCores;

    // podrazumijevane vrijednosti za zadatake
    public static Integer DefaultTaskPriority;
    public static Integer MinTaskPriority;
    public static Integer TaskCounter;

    public static void loadPropertiesFile(String propertiesFile) throws IOException {
        FileInputStream input = new FileInputStream(propertiesFile);
        Properties properties = new Properties();
        properties.load(input);

        putanjaUlaznogFoldera = properties.getProperty("input");
        putanjaIzlaznogFoldera = properties.getProperty("output");
        IntermediateFolderPath = properties.getProperty("intermediate");
        putanjaSerijalizovanihZadataka = properties.getProperty("tasks");
        numberOfTasks = Integer.parseInt(properties.getProperty("brojKonkurentnihZadatakaDefault"));

        numberOfCores = Integer.parseInt(properties.getProperty("brojJezgaraDefault"));

        DefaultTaskPriority = Integer.parseInt(properties.getProperty("default-priority"));
        MinTaskPriority = Integer.parseInt(properties.getProperty("min-priority"));
    }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader= new FXMLLoader(getClass().getResource("/Main.fxml"));
	  		
			Pane root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			loadPropertiesFile("C:\\Users\\Account\\eclipse-workspace\\TaskSchedulerOPOS\\src\\load-info.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		launch(args);
	}
}
