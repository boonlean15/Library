package test;

import java.io.IOException;

public class Test1 implements Runnable {
	Task task;

	public Test1(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		 while(task.i<50)
	        {
	            synchronized (task)/* 必须要用一把锁对象，这个对象是task*/ {
	                while(!task.flag)
	                {
	                    try 
	                    {
	                        task.wait();  //操作wait()函数的必须和锁是同一个
	                    } catch (Exception e) 
	                    {}
	                }   
	                
	                if(task.i/5%2==1 && task.i<50){
	                	task.i++;
	                	System.out.println(Thread.currentThread().getName()+task.i);
	                	try {
							task.fw.write(Thread.currentThread().getName()+task.i +"\r\n");
							task.fw.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
	                }else{
	                	 task.notifyAll();
		                    task.flag = false;
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
