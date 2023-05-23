package tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import application.Main;
import controllers.SchedulerPageController;
import javafx.application.Platform;
import taskScheduler.TaskScheduler;

public class SortingPixelsTask extends Task {
	

	public SortingPixelsTask(TypeOfTask taskType, int numberOfCores, ArrayList<Resource> resources, int taskPriority, LocalDate endingTime,long duration) {

		super(taskType,numberOfCores, resources,taskPriority, endingTime, duration);
		
	}
    
	public void run() {
		
		System.out.println(taskName + " Started..");
		ArrayList<Map.Entry<Task, String>> tasks2=new ArrayList<>() ;
		 Platform.runLater(() -> {
			 for (Entry<Task, String> data : SchedulerPageController.tasks) {
				
				    if (data.getKey().getTaskName().equals(taskName)) {
				    	tasks2.add(Map.entry(this,"ZADATAK POČEO SA RADOM..."));
				    }else
				    	 tasks2.add(data);
				}
			 SchedulerPageController.tasks.clear();
			 SchedulerPageController.tasks.addAll(tasks2);
         });
		long endTime=0;
		long startTime = System.currentTimeMillis();
		
		BufferedImage img = null;
	    File f = null;
	    
	    //read image
	   for(Resource resource: Main.Scheduler.resources) {
	    try{
	    	f=new File(resource.getPath());
	      img = ImageIO.read(f);
	      
	    }catch(IOException e){
	      System.out.println(e);
	    }
	    //get image width and height
	    int width = img.getWidth();
	    int height = img.getHeight();
	    int l;
	    for(int y = 0; y < height; y++){
	      for(int x = 0; x < width; x++){
	    	  if(pause) {
	    		  synchronized(this){
	  				try{
	  					serialize(Main.putanjaSerijalizovanihZadataka+File.separator+taskName+".ser");
	  					f = new File(Main.putanjaIzlaznogFoldera + File.separator + resource.getName().substring(0,resource.getName().length()-4) + "SORTED.bmp");
		  		        ImageIO.write(img, "bmp", f);
	  					wait();
	  				}catch(Exception e){e.printStackTrace();}
	  			}
	  			
	  		}
	        int p = img.getRGB(x,y);
	        int a = (p>>24)&0xff; 
	        int r = (p>>16)&0xff;
	        int g = (p>>8)&0xff;
	        int b = p&0xff;  
	        
	        //RGB values in bmp are stored backwards i.e. BGR
	        //luminance
	        double s=0.3*r + 0.59*g + 0.11*b;
	        
	        double s2=0;
	        int pp=0,rp=0,gp=0,bp=0; //,ap=0
	        if(x>0) {
	        	pp = img.getRGB(x-1,y);
	            //ap = (pp>>24)&0xff;
	            rp = (pp>>16)&0xff;
	            gp = (pp>>8)&0xff;
	            bp = pp&0xff;
	            s2=0.3*rp + 0.59*gp + 0.11*bp;
	        }
	       
	        
	        for(l=x; l>0 && s<s2 ;l-=1)
	        {
	        	
	        	pp = img.getRGB(l-1,y);
	            
	            img.setRGB(l, y, pp);
	           
	            try {
		            pp = img.getRGB(l-2,y);
		            //ap = (pp>>24)&0xff;
		            rp = (pp>>16)&0xff;
		            gp = (pp>>8)&0xff;
		            bp = pp&0xff;
		            s2=0.3*rp + 0.59*gp + 0.11*bp;
	            }catch(Exception e) {
	            	continue;
	            }
	        }
	        p = (a<<24) | (r<<16) | (g<<8) | b;
	        img.setRGB(l, y, p);
	      }
	    }
	    //write image
	    try{
	    	f = new File(Main.putanjaIzlaznogFoldera + File.separator + resource.getName().substring(0,resource.getName().length()-4) + "SORTED.bmp");
	      ImageIO.write(img, "bmp", f);
	    }catch(IOException e){
	      System.out.println(e);
	    }
	    serialize(Main.putanjaSerijalizovanihZadataka+File.separator+taskName+".ser");
	    endTime=System.currentTimeMillis();
	    double time=(endTime - startTime) / 1000;
		System.out.println("time: " + time+ " sec.");
		ArrayList<Map.Entry<Task, String>> tasks3=new ArrayList<>() ;
		 Platform.runLater(() -> {
			 for (Entry<Task, String> data : SchedulerPageController.tasks) {
				
				    if (data.getKey().getTaskName().equals(this.getTaskName())) {
				    	tasks3.add(Map.entry(this,"ZADATAK ZAVRŠEN...    vrijeme izvrsavanja: " +time));
				    	
				    }else
				    	 tasks3.add(data);
				}
			 SchedulerPageController.tasks.clear();
			 SchedulerPageController.tasks.addAll(tasks3);
        });
	   }
	}
}