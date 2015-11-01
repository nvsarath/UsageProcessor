package com.verizon.usage.clent;

import java.io.BufferedReader;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//import org.json.JSONObject;

public class MultiThread {
	
	BufferedReader bReader = null;
	String fileName = null;
	HashMap<String, HashMap> customerMap = new HashMap<String, HashMap>();
//	JSONObject custObj = new JSONObject();
//	JSONObject custDateUsageMap =null;
	
	MultiThread() {
	
	}
	
	void  ProcessFiles(BufferedReader bReader,String fname){
		System.out.println("in process files ");
		int  noOfThread=50;
		long timeout=1000L;
		BlockingQueue<Runnable> blockingQueu=new ArrayBlockingQueue<Runnable>(noOfThread);
		System.out.println("after clock queue ");
		RejectedExecutionHandler rejectedExecutionHandler
		=new ThreadPoolExecutor.CallerRunsPolicy();
		ExecutorService service = new ThreadPoolExecutor(noOfThread, noOfThread+5, timeout, 
				TimeUnit.MILLISECONDS,blockingQueu, rejectedExecutionHandler);
		
		System.out.println("after service ");
	 List<String> recordList= new ArrayList();
		String line;
		final String filename = fname;
		try {
			while ((line = bReader.readLine()) != null) {
				recordList.add(line);
			//	System.out.println("line "+ line);
				if(recordList.size()>10000){
					final MultiThreadedProcessor  mtp=new MultiThreadedProcessor();
					final ArrayList<String> toProcessList = new ArrayList();
					toProcessList.addAll(recordList);
					service.submit(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							mtp.runLine(toProcessList,filename);
						}
					});
					
					recordList.clear();
				}				
				
				
			}
			if (recordList != null && recordList.size() > 0 ){

				final MultiThreadedProcessor  mtp=new MultiThreadedProcessor();
				final ArrayList<String> toProcessList = new ArrayList();
				toProcessList.addAll(recordList);
				service.submit(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						mtp.runLine(toProcessList,filename);
					}
				});
				
				recordList.clear();
	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
				
		ShutdownAndAwaitTermination(service);
	}
	
	public List<String> getRecordList() {
		String line;
		ArrayList<String> recordList = new ArrayList ();
		
		return recordList;
	}
	
	private void ShutdownAndAwaitTermination(ExecutorService service) {
		service.shutdown();
		int timeOut=300;
		System.out.println("shutdown the service");
		try{
			
			if(!service.awaitTermination(timeOut, TimeUnit.SECONDS)){
				System.out.println("shutdown now is called");
				service.shutdownNow();
			}else
				System.out.println("seems all are closed");
			
		}catch(Exception e){
			service.shutdownNow();
		}
		
	}

	public static void main(String[] args) {
		
	}

}
