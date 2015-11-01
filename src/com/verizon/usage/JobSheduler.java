package com.verizon.usage;


public class JobSheduler {
	
	
	public void run(){
		System.out.println("Task get Schulded "+System.currentTimeMillis());
		DBPersister processObj = new DBPersister ();
		processObj.loadingDataIntoCasandra();
		System.out.println("Task completed");
	}

}
