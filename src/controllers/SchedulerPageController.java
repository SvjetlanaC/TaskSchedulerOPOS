package controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import application.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import taskScheduler.TaskScheduler;
import tasks.IllegalTaskStateException;
import tasks.Resource;
import tasks.SortingPixelsTask;
import tasks.Task;
import tasks.TypeOfTask;

public class SchedulerPageController implements Initializable{

	public static Stage stage;
	public static boolean restored;
	@FXML
    private DatePicker datePicker;

    @FXML
    private TextField hoursTextField;

    @FXML
    private TextField minutesTextField;

    @FXML
    private TextField parallelTextField;

    @FXML
    private TextField priorityTextField;

    @FXML
    private TableColumn<Map.Entry<Task, String>, String> progressTableColumn;

    @FXML
    private Button resourcesButton;

    @FXML
    private TextField secondsTextField;

    @FXML
    private TableColumn<Map.Entry<Task, String>, Task> taskTableColumn;

    @FXML
    private TableView<Map.Entry<Task, String>> tasksTableView;

    @FXML
    private TextField timeOfExecutionTextField;

    @FXML
    private ComboBox<TypeOfTask> typeOfTaskComboBox;

    private ArrayList<Resource> pickedResources = new ArrayList<>();
    private static final List<Integer> hours = IntStream.range(0, 24).boxed().collect(Collectors.toList());
    private static final List<Integer> minSec = IntStream.range(0, 60).boxed().collect(Collectors.toList());
    
    @FXML
    void chooseResources(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Izaberite resurse");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*." + "bmp"));
        fileChooser.setInitialDirectory(new File(Main.putanjaUlaznogFoldera));
        List<File> picked = fileChooser.showOpenMultipleDialog(stage);
        if(picked != null && !picked.isEmpty()){
            pickedResources.clear();
            picked.forEach(f -> pickedResources.add(new Resource(f)));
            try{
            	 System.out.println(pickedResources.get(0));
            } catch (Exception exc){
                exc.printStackTrace();
                pickedResources.clear();
            }
        }
        Main.Scheduler.resources=pickedResources;
    }


    @FXML
    void pokreni(ActionEvent event) {
    	TaskScheduler.startAllTasks();
    	 
    	 tasksTableView.setItems(tasks);
    }

    @FXML
    void potvrdi(ActionEvent event) {

    	try{
            int priority = Integer.parseInt(priorityTextField.getText());
            int parallelism = Integer.parseInt(parallelTextField.getText());
            int executionTime = Integer.parseInt(timeOfExecutionTextField.getText()); // u sekundama
            int hours = Integer.parseInt(hoursTextField.getText());
            int minutes = Integer.parseInt(minutesTextField.getText());
            int seconds = Integer.parseInt(secondsTextField.getText());
            int dateCmp = datePicker.getValue().compareTo(LocalDate.now());

            if(priority > 0 && parallelism > 0 && executionTime > 0 && SchedulerPageController.hours.contains(hours) && minSec.contains(minutes) && minSec.contains(seconds) && dateCmp >= 0 && pickedResources.size() > 0 && typeOfTaskComboBox.getSelectionModel().getSelectedItem() != null){
                LocalDateTime deadline = LocalDateTime.of(datePicker.getValue(), LocalTime.of(hours, minutes, seconds));
                if(priority > Main.MinTaskPriority)
                    priority = Main.MinTaskPriority;
                if(parallelism > Main.Scheduler.getNumberOfCores())
                    parallelism = Main.Scheduler.getNumberOfCores();

//                TaskScheduler.createTask(typeOfTaskComboBox.getSelectionModel().getSelectedItem(), MainController.numberOfCores, Main.Scheduler.resources, priority,datePicker.getValue() ,executionTime);
                SortingPixelsTask task=new SortingPixelsTask(typeOfTaskComboBox.getSelectionModel().getSelectedItem(), MainController.numberOfCores, Main.Scheduler.resources, priority,datePicker.getValue() ,executionTime);
                TaskScheduler.tasks.add(task);
                
                Platform.runLater(() -> {
                    tasksTableView.getItems().add(Map.entry(task, "Zadatak spreman"));
                });
            } else {
                throw new IllegalArgumentException("Arguments are not valid!");
            }
        } catch (Exception exc){
            System.err.println(exc.getMessage());
        }
    }
    
    @FXML
    void pokreniTask(ActionEvent event) throws IllegalTaskStateException {
    	Entry<Task, String> task = tasksTableView.getSelectionModel().getSelectedItem();
    	TaskScheduler.startTask(task.getKey());
    }

    @FXML
    void izbrisiTask(ActionEvent event) {
    	Entry<Task, String> task = tasksTableView.getSelectionModel().getSelectedItem();
    	TaskScheduler.deleteTask(task.getKey());
    	ArrayList<Map.Entry<Task, String>> tasks2=new ArrayList<>() ;
    	for (Entry<Task, String> data : SchedulerPageController.tasks) {
			
		    if (data.getKey().getTaskName().equals(task.getKey().getTaskName())) {
		    	continue;
		    }else
		    	 tasks2.add(data);
		}
	 SchedulerPageController.tasks.clear();
	 SchedulerPageController.tasks.addAll(tasks2);
    }

    @FXML
    void nastavi(ActionEvent event) {
    	Entry<Task, String> task = tasksTableView.getSelectionModel().getSelectedItem();
    	try{
    		task.getKey().setPause(false);
    		System.out.println("Nastavlja " + task.getKey() + task.getKey().isPause());
			synchronized(task.getKey()){
				task.getKey().notifyAll();
			}
			ArrayList<Map.Entry<Task, String>> tasks2=new ArrayList<>() ;
	    	for (Entry<Task, String> data : SchedulerPageController.tasks) {
				
			    if (data.getKey().getTaskName().equals(task.getKey().getTaskName())) {
			    	tasks2.add(Map.entry(data.getKey(),"ZADATAK NASTAVLJA SA RADOM..."));
			    }else
			    	 tasks2.add(data);
			}
		 SchedulerPageController.tasks.clear();
		 SchedulerPageController.tasks.addAll(tasks2);
			
		}catch(Exception e){
			e.printStackTrace();
		}
    }

    @FXML
    void pauziraj(ActionEvent event) {
    	Entry<Task, String> task = tasksTableView.getSelectionModel().getSelectedItem();
    	task.getKey().setPause(true);
    	System.out.println("Pauziran " + task.getKey() + task.getKey().isPause());
    	ArrayList<Map.Entry<Task, String>> tasks2=new ArrayList<>() ;
    	for (Entry<Task, String> data : SchedulerPageController.tasks) {
			
		    if (data.getKey().getTaskName().equals(task.getKey().getTaskName())) {
		    	tasks2.add(Map.entry(data.getKey(),"ZADATAK JE PAUZIRAN..."));
		    }else
		    	 tasks2.add(data);
		}
	 SchedulerPageController.tasks.clear();
	 SchedulerPageController.tasks.addAll(tasks2);
    }
    List<File> pickedSer = new ArrayList<>();
    @FXML
    void readTask(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Izaberite serijalizovan zadatak");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Binary FILES", "*." + "ser"));//SortingPixels.Extension
        fileChooser.setInitialDirectory(new File(Main.putanjaSerijalizovanihZadataka));
        List<File> picked = fileChooser.showOpenMultipleDialog(stage);
        
        if(picked != null && !picked.isEmpty()){
            picked.forEach(f -> pickedSer.add(f));
            try{
            	 System.out.println(pickedSer.get(0));
            } catch (Exception exc){
                exc.printStackTrace();
                pickedSer.clear();
            }
        }
        
    }

    @FXML
    void pregledRezultata(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Izaberite serijalizovan zadatak");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BMP pictures", "*." + "bmp"));
        fileChooser.setInitialDirectory(new File(Main.putanjaIzlaznogFoldera));
        List<File> picked = fileChooser.showOpenMultipleDialog(stage);
    }
    
    @FXML
    void izbrisi(ActionEvent event) {
    	TaskScheduler.deleteAllTasks();
    	tasksTableView.getItems().clear();
    }

   
    
    public static ObservableList<Map.Entry<Task, String>> tasks = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		 datePicker.setValue(LocalDate.now());
	        typeOfTaskComboBox.getItems().addAll(TypeOfTask.values());

	        taskTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Task, String>, Task>, ObservableValue<Task>>() {
	            @Override
	            public ObservableValue<Task> call(TableColumn.CellDataFeatures<Map.Entry<Task, String>, Task> column) {
	                return new SimpleObjectProperty<Task>(column.getValue().getKey());
	            }
	        });
	        progressTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Task, String>, String>, ObservableValue<String>>() {
	            @Override
	            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Task, String>, String> column) {
	                return new SimpleObjectProperty<String>(column.getValue().getValue());
	            }
	        });
	        
	        tasksTableView.setItems(tasks);
	}

}
