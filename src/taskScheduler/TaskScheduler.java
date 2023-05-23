package taskScheduler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import application.Main;
import tasks.IllegalTaskStateException;
import tasks.Resource;
import tasks.Task;
import tasks.TypeOfTask;

public class TaskScheduler {

	int maxNumOfTasks = 10;
	public static int numOfTasks;
	int numOfRealCores;
	int numberOfCores;
	public int getNumberOfCores() {
		return numberOfCores;
	}

	public void setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;
	}

	public ArrayList<Resource> resources = new ArrayList<>();
	public static ArrayList<Task> tasks=new ArrayList<>();

	public TaskScheduler(int numberOfCores,int numberOfTasks) throws IllegalTaskStateException {

		maxNumOfTasks=numberOfTasks;
		
		this.numOfRealCores = Runtime.getRuntime().availableProcessors();
		if(numberOfCores>numOfRealCores) {
			throw new IllegalTaskStateException();
		}
		else this.numberOfCores=numberOfCores;
	}

	public int getMaxNumOfTasks() {
		return maxNumOfTasks;
	}

	public void setMaxNumOfTasks(int maxNumOfTasks) {
		this.maxNumOfTasks = maxNumOfTasks;
	}

	public int getNumOfTasks() {
		return numOfTasks;
	}

	public void setNumOfTasks(int numOfTasks) {
		TaskScheduler.numOfTasks = numOfTasks;
	}

	public int getNumOfRealCores() {
		return numOfRealCores;
	}

	public void setNumOfRealCores(int numOfCores) {
		this.numOfRealCores = numOfCores;
	}

	public static ArrayList<Task> getTasks() {
		return tasks;
	}

	public static void setTasks(ArrayList<Task> tasks) {
		TaskScheduler.tasks = tasks;
	}


	public static void createTask(TypeOfTask taskType, int numberOfCores, ArrayList<Resource> resources, int taskPriority,LocalDate endingTime, long duration) {


		Task tsk = new Task(taskType, numberOfCores, resources, taskPriority, endingTime, duration);
		tasks.add(tsk);
		numOfTasks++;

	}

	public static void startAllTasks() {

		ArrayList<Task> sortedByPriority = new ArrayList<Task>();
		for (Task t : tasks) {
			sortedByPriority.add(t);
		}

		Collections.sort(sortedByPriority, Comparator.comparing(Task::getTaskPriority));
		int numberOfFinishedTasks = 0;
		for (int i = 0; i <Math.min(Main.Scheduler.maxNumOfTasks, sortedByPriority.size()) ; i++) {
			System.out.println("Group of tasks: " + Main.Scheduler.maxNumOfTasks);	
			Task tsk = sortedByPriority.get(i);
			System.out.println("Group of tasks: " + tsk);	
			numberOfFinishedTasks++;
			new Thread(tsk).start();
			
		}
		for (int i = 0; i <Math.min(Main.Scheduler.maxNumOfTasks, sortedByPriority.size()) ; i++) {
			
			try {
				sortedByPriority.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(tasks.size());
		for(int i=0;i<numberOfFinishedTasks;i++) {
			tasks.remove(0);
		}
		
		System.out.println("Pokrenuti svi zadaci!");

	}

	public static void deleteTask(Task task) {

		int n=-1;
		for(Task t : tasks) {
			if(t.getTaskName().equalsIgnoreCase(task.getTaskName()))
				n++;
		}
		TaskScheduler.tasks.remove(n);
		
	}
	public static void startTask(Task task) throws IllegalTaskStateException {

		if(!task.isAlive()) {
		int n=-1;
		for(Task t : tasks) {
			if(t.getTaskName().equalsIgnoreCase(task.getTaskName()))
				n++;
		}
		new Thread(TaskScheduler.tasks.get(n)).start();
		}else throw new IllegalTaskStateException();
	}

	public static void deleteAllTasks() {

	ArrayList<Task> temporary = new ArrayList<Task>();
	
	for(Task t: TaskScheduler.tasks) {
		
		temporary.add(t);
		
	}
	
	TaskScheduler.tasks.removeAll(temporary);
		
	}

}
