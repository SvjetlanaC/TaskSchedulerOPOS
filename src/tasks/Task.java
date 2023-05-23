package tasks;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


public class Task extends Thread implements Serializable {
	

	int id;
	TypeOfTask taskType;
	static int c=0;
	int numberOfCores;
	String taskName;
	int taskPriority;
	LocalDate endingTime;
	long duration;
	boolean TaskDone = false;
	boolean pause=false;
	public ArrayList<Resource> resources = new ArrayList<>();

	public Task() {

	}

	public Task(TypeOfTask taskType, int numberOfCores, ArrayList<Resource> resources, int taskPriority, LocalDate endingTime,long duration) {

		this.taskType = taskType;
		this.resources = resources;
		this.taskPriority = taskPriority;
		this.endingTime = endingTime;
		this.duration = duration;
		this.numberOfCores=numberOfCores;
		this.taskName="Task" + c++;
		this.id=c;
	}

	public int getTaskId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isTaskDone() {
		return TaskDone;
	}

	public void setTaskDone(boolean taskDone) {
		TaskDone = taskDone;
	}

	public TypeOfTask getTaskType() {
		return taskType;
	}

	public void setTaskType(TypeOfTask taskType) {
		this.taskType = taskType;
	}

	public ArrayList<Resource> getResources() {
		return resources;
	}

	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(int taskPriority) {
		this.taskPriority = taskPriority;
	}

	public LocalDate getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(LocalDate endingTime) {
		this.endingTime = endingTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}


	public String toString() {
		
		return taskName + " " + taskType;
	}
	
	public void serialize(String path){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))){
            out.writeObject(this);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
	
	public Task deserialize(String path){
		Task task=null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
            task=(Task) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return task;
    }
	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
