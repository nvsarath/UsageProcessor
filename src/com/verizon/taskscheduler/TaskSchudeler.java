package com.verizon.taskscheduler;

import javax.xml.ws.Dispatch;

import org.springframework.scheduling.annotation.Scheduled;

import com.verizon.usage.clent.UsageFileProcessor;


public class TaskSchudeler {
	
	
	public void run(){
		System.out.println("Task get Schulded "+System.currentTimeMillis());
		UsageFileProcessor processObj = new UsageFileProcessor ();
		processObj.startProcessing();
		System.out.println("Task completed");
	}
	public static void main(){
		System.out.println("Task get Schulded "+System.currentTimeMillis());
		UsageFileProcessor processObj = new UsageFileProcessor ();
		processObj.startProcessing();
		System.out.println("Task completed");
	}
}
