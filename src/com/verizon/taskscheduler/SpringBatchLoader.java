package com.verizon.taskscheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class SpringBatchLoader {

	
	
	@Scheduled(cron="*/5 * * * * ?")
	public static void loadDeamon() {

		
		String str="classpath*:/Spring-Config2.xml";
		System.out.println(str);
	    
	    		    new FileSystemXmlApplicationContext( str);

	   
		
	}


}
