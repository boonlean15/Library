package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Test implements Runnable {
	Task task;

	public Test(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		while(task.i< 50)
        {   
            synchronized (task) {
                while(task.flag)
                {
                    try {
                    	task.wait();
                    } catch (Exception e) {
                    }
                }
               
                	if(task.i/5%2==0 && task.i<50){
                		task.i++;
                		System.out.println(Thread.currentThread().getName()+task.i );
                		
                		try {
							task.fw.write(Thread.currentThread().getName()+task.i+"\r\n");
							task.fw.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
                		
                	}else{
                		task.notifyAll();
                        task.flag = true;
                	}
                	if(task.i == 50){
                		try {
							task.fw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                    
                    
                    
                    
                
            }
        }
		
		
		
	}

	

}
