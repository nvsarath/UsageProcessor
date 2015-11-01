package com.verizon.usage.clent;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

//import org.json.JSONArray;
//import org.json.JSONObject;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;


public class MultiThreadedProcessor implements Runnable {
	BufferedReader bReader = null;
	String fileName = null;
	HashMap<String, HashMap> customerMap = new HashMap<String, HashMap>();
//	JSONObject custObj = new JSONObject();
//	JSONObject custDateUsageMap =null;

	MultiThreadedProcessor(BufferedReader reader,String fileName) {
		this.bReader = reader;
		this.fileName=fileName;
	//	this.custObj = custObj2;
	}
  public static int count;
	/*public synchronized void run1() {
		String line;
		String customerId;
		String currentUsage;
		String existingUsage;
		int accumilatedUsage=0;
		String siteName;
		ArrayList<String> excludeSites = new ArrayList<String>();
		loadExcludeSites(excludeSites);

		try {
			while ((line = bReader.readLine()) != null) {

				try {
					//System.out.println(line);
					StringTokenizer st = new StringTokenizer(line,",");
					while(st.hasMoreElements()) {
						customerId = st.nextToken();
						currentUsage = st.nextToken();
						siteName = st.nextToken();
						if(custObj.has(customerId)) {
							JSONArray custArry = (JSONArray) custObj.get(customerId);
							custDateUsageMap = custArry.getJSONObject(0);
							if(!excludeSites.contains(siteName)) {
								if(custDateUsageMap.has(fileName)){
									JSONArray objArr = (JSONArray) custDateUsageMap.get(fileName);
									existingUsage = objArr.getString(0);
									accumilatedUsage = Integer.parseInt(existingUsage) + Integer.parseInt(currentUsage);
									if(accumilatedUsage > 200) {
										writeForAlert(customerId,accumilatedUsage);
									}
									custDateUsageMap.remove(fileName);
									custDateUsageMap.append(fileName, String.valueOf(accumilatedUsage));
								}else {
									custDateUsageMap.append(fileName, String.valueOf(currentUsage));
								}
								if("100".equals(customerId)){
									System.out.println(fileName + "   " + currentUsage + "   " + String.valueOf(accumilatedUsage));
								}
								custObj.remove(customerId);
								custObj.append(customerId, custDateUsageMap);
							}
						} else {
							if(!excludeSites.contains(siteName)) {
								custDateUsageMap = new JSONObject();
								custDateUsageMap.append(fileName, String.valueOf(currentUsage));
								custObj.append(customerId, custDateUsageMap);								
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/

	public MultiThreadedProcessor() {
		// TODO Auto-generated constructor stub
	}

	public synchronized void run() {
		String line;
		String customerId;
		String currentUsage;
		String existingUsage;
		int accumilatedUsage=0;
		String siteName;
		ArrayList<String> excludeSites = new ArrayList<String>();
		loadExcludeSites(excludeSites);
		Map custDateUsageMap = null;
		boolean isUpdated=false;
		System.out.println("filename in thread : "+fileName);
		try {
			while ((line = bReader.readLine()) != null) {
				isUpdated=false;
				try {
					System.out.println(line);
					String[]  splitteer=line.split(","); 
					
					if(splitteer.length == 3) {
						customerId = splitteer[0];
						currentUsage = splitteer[1];
						siteName = splitteer[2];
					
						Map custObj=HazelCastClientUtil.getCustomerObj(customerId);
						
						if(custObj !=null && !custObj.isEmpty()) {
							//JSONArray custArry = (JSONArray) custObj.get(customerId);
							custDateUsageMap =custObj;
							
							
							if(!excludeSites.contains(siteName)) {
								if(custDateUsageMap.containsKey(fileName)){
									//JSONArray objArr = (JSONArray) custDateUsageMap.get(fileName);
									//existingUsage = objArr.getString(0);
									existingUsage = (String) custDateUsageMap.get(fileName);
									accumilatedUsage = Integer.parseInt(existingUsage) + Integer.parseInt(currentUsage);
									if(accumilatedUsage > 200) {
										writeForAlert(customerId,accumilatedUsage);
									}
									custDateUsageMap.remove(fileName);
									custDateUsageMap.put(fileName, String.valueOf(accumilatedUsage));
								}else {
									custDateUsageMap.put(fileName, String.valueOf(currentUsage));
								}
								if("100".equals(customerId)){
									System.out.println(fileName + "   " + currentUsage + "   " + String.valueOf(accumilatedUsage));
								}
								//custObj.remove(customerId);
								//custObj.put(customerId, custDateUsageMap);
								isUpdated=true;
							}
						} else {
							if(!excludeSites.contains(siteName)) {
								//custDateUsageMap = new JSONObject();
								custDateUsageMap=new HashMap();
								custDateUsageMap.put(fileName, String.valueOf(currentUsage));
								//custObj.append(customerId, custDateUsageMap);
								isUpdated=true;
							}
						}
						
						if(isUpdated){
						HazelCastClientUtil.loadMap(customerId, custDateUsageMap);
						}
						
					}
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public  void runLine(List<String>  recordList,String fileName) {
		//String line;
		String customerId;
		String currentUsage;
		String existingUsage;
		int accumilatedUsage=0;
		String siteName;
		String startDateTime;
		String endDateTime;
		ArrayList<String> excludeSites = new ArrayList<String>();
		loadExcludeSites(excludeSites);
		ArrayList<String> excludeTimes = new ArrayList<String>();
		loadExcludeTimes(excludeTimes);
		Map custDateUsageMap = null;
		boolean isUpdated=false;
		HazelcastInstance client = HazelCastClientUtil.getClient();
		
		System.out.println("no of blocks : "+count++);
		
		try {
			for(String line:recordList) {
				isUpdated=false;
				try {
				//	System.out.println(line);
					String[]  splitteer=line.split(","); 
					
					if(splitteer.length == 3) {
						customerId = splitteer[0];
						currentUsage = splitteer[1];
						siteName = splitteer[2];

						IMap<Object, Object> imap = client.getMap("customers");
						Map custObj= (Map)imap.get(customerId);
					//	Map custObj=HazelCastClientUtil.getCustomerObj(customerId);
						
						if(custObj !=null && !custObj.isEmpty()) {
							//JSONArray custArry = (JSONArray) custObj.get(customerId);
							custDateUsageMap =custObj;
							
							
							if(!excludeSites.contains(siteName)) {
								if(custDateUsageMap.containsKey(fileName)){
									//JSONArray objArr = (JSONArray) custDateUsageMap.get(fileName);
									//existingUsage = objArr.getString(0);
									existingUsage = (String) custDateUsageMap.get(fileName);
									accumilatedUsage = Integer.parseInt(existingUsage) + Integer.parseInt(currentUsage);
									if(accumilatedUsage > 2000) {
										writeForAlert(customerId,accumilatedUsage);
									}
									custDateUsageMap.remove(fileName);
									custDateUsageMap.put(fileName, String.valueOf(accumilatedUsage));
								}else {
									custDateUsageMap.put(fileName, String.valueOf(currentUsage));
								}
						/*		if("100".equals(customerId)){
									System.out.println(fileName + "   " + currentUsage + "   " + String.valueOf(accumilatedUsage));
								}*/
								//custObj.remove(customerId);
								//custObj.put(customerId, custDateUsageMap);
								isUpdated=true;
							}
						} else {
							if(!excludeSites.contains(siteName)) {
								//custDateUsageMap = new JSONObject();
								custDateUsageMap=new HashMap();
								custDateUsageMap.put(fileName, String.valueOf(currentUsage));
								//custObj.append(customerId, custDateUsageMap);
								isUpdated=true;
							}
						}
						
						if(isUpdated){
							//HazelCastClientUtil.loadMap(customerId, custDateUsageMap);
							IMap<Object, Object> imapPut = client.getMap("customers");
							imapPut.put(customerId, custDateUsageMap);
						}
						
					}
				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		//	System.out.println("finished block : "+count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private boolean isUsageInExcludedTime(String startDateTime, String endDateTime,
			ArrayList<String> excludeTimes) {
		long startTimeInMillis = new Date(startDateTime).getTime();
		long endTimeInMillis = new Date(endDateTime).getTime();
		System.out.println(startTimeInMillis + "    " + endTimeInMillis);
		for(int i=0;i<excludeTimes.size();i++) {
			String[] splitter = excludeTimes.get(i).split(",");
			long excludeStartTime = Long.valueOf(splitter[0]).longValue();
			long excludeEndTime = Long.valueOf(splitter[1]).longValue();
			System.out.println(excludeStartTime + "  exclude  " + excludeEndTime);

			if(startTimeInMillis > excludeStartTime && endTimeInMillis < excludeEndTime){
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private void loadExcludeTimes(ArrayList<String> excludeTimes) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		@SuppressWarnings("deprecation")
		Date startTime = new Date("01-Nov-2015 12:05:00");
		Date endTime = new Date("01-Nov-2015 12:10:00");
		excludeTimes.add((startTime.getTime()+","+endTime.getTime()));
	}

	private void loadExcludeSites(ArrayList<String> excludeSites) {
		// TODO Auto-generated method stub
		excludeSites.add("facebook.com");

	}

	private void writeForAlert(String customerId, int accumilatedUsage) {
		SendSms.sendMessageToDataUsers(customerId,accumilatedUsage);

	}
}