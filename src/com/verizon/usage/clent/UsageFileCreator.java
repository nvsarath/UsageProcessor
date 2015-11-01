package com.verizon.usage.clent;
import java.io.*; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
class UsageFileCreator { 
	public static void main(String args[]) throws Exception { 
		File f=new File("C:\\Users\\Administrator\\workspace\\UsageProject\\input\\test_20151102_Part1.txt");

		int customerId =100;
		int usage = 0;
		Random randomGenerator = new Random();
		ArrayList<String> sitesList = new ArrayList<String>();
		sitesList.add("google.com");
		sitesList.add("facebook.com");
		sitesList.add("yahoo.com");
		sitesList.add("twitter.com");
		sitesList.add("cricinfo.com");
		sitesList.add("greatandhra.com");
		sitesList.add("timesofindia.com");
		sitesList.add("thehindu.com");
		sitesList.add("smartprix.com");
		sitesList.add("amazon.com");

		String fileDate = f.getName().split("_")[1];
		Calendar calObj = Calendar.getInstance();
		calObj.set(Integer.parseInt(fileDate.substring(0,3)), Integer.parseInt(fileDate.substring(4, 5)), Integer.parseInt(fileDate.substring(6, 7)));
		SimpleDateFormat format = new SimpleDateFormat("dd-mmm-yyyy");
		fileDate = format.format(calObj.getTime());
		//5,00,00,000

		FileWriter f0 = new FileWriter(f,true);
		for(int j=0;j<5000;j++) {
			//int i=customerId;
			for (int i=0; i < 10000; i += 1) { 
				usage=randomGenerator.nextInt(100);
				int siteInd = usage/10;
				String hour = (String.valueOf(randomGenerator.nextInt(100)/12).length()==1)?("0" + (randomGenerator.nextInt(100)/12)):(String.valueOf(randomGenerator.nextInt(100)/12));
				String minute = (String.valueOf(randomGenerator.nextInt(100)/60).length()==1)?("0" + (randomGenerator.nextInt(100)/60)):(String.valueOf(randomGenerator.nextInt(100)/60));
				String seconds = (String.valueOf(randomGenerator.nextInt(100)/60).length()==1)?("0" + (randomGenerator.nextInt(100)/60)):(String.valueOf(randomGenerator.nextInt(100)/60));

				f0.write(i + "," + usage + "," + sitesList.get(siteInd)); 
				f0.write("\n");
			} 
		}
		f0.close(); 
		System.out.println("File write done");
	} 
}