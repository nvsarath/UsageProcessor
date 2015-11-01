package com.verizon.usage.clent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

//import org.json.JSONArray;
//import org.json.JSONObject;



public class UsageFileProcessor {
	
//	static JSONObject custObj = new JSONObject();
	
	public static void main(String args[]) {
		
		UsageFileProcessor obj = new UsageFileProcessor();
		obj.startProcessing();
	}


	private static ArrayList<File> getFilesList(ArrayList<File> filesList) {
		File fd=new File("C:\\Users\\Administrator\\workspace\\UsageProject\\input");
		if(fd.isDirectory()){
			System.out.println("its directory :");
			filesList = listFilesForFolder(fd,filesList);
		} else {
			System.out.println("its file  :");
			filesList.add(fd);
		}
		return filesList;
	}


	public static ArrayList<File> listFilesForFolder(File entity, ArrayList<File> filesList) {
		if(entity.isDirectory()) {
			for (File fileEntry : entity.listFiles()) {
				filesList.add(fileEntry);
				listFilesForFolder(fileEntry,filesList);
			}			
		}
		return filesList;
	}
	
	public void startProcessing (){
		try {
			long startTime = System.currentTimeMillis();
			System.out.println(startTime);
			BufferedReader reader = null;
					
				//readFileMetaData();
				ArrayList<File> filesList = new ArrayList<File>();
				
				filesList = getFilesList(filesList);
				System.out.println("filesList size :" + filesList.size());
				for(int i=0; i<filesList.size();i++) {
					System.out.println("in loop :");
					String fileName = filesList.get(i).getName();
					System.out.println("filename  :"+fileName);
					FileReader fr = new FileReader((File)filesList.get(i));
					System.out.println("after readfilename  :"+fileName);
					
					String str[] = fileName.split("_");
					reader = new BufferedReader(fr);
					for (int j = 0; j < 1; j++) {
						System.out.println("in inner loop :");
						MultiThread mtObj = new MultiThread ();
						mtObj.ProcessFiles(reader,str[1]);
					}
					long endTime = System.currentTimeMillis();
					System.out.println("Total time taken : " + (endTime-startTime));
					System.out.println(filesList.get(i));
					fr.close();
					reader.close();
					File oldFile = new File(filesList.get(i).getName());
					System.out.println("C:\\Users\\Administrator\\workspace\\UsageProject\\input\\" + oldFile.getName());
					
					if(oldFile.renameTo(new File("C:\\Users\\Administrator\\workspace\\UsageProject\\processed\\" + oldFile.getName())))
						System.out.println("success");
					else{
						System.out.println("faile not moved");
						oldFile.delete();
					}
					//   	public static Map getCustomerObj(String customerId) {

					System.out.println(HazelCastClientUtil.getCustomerObj("100"));
					
					//System.out.println(custDateUsageMap);
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void readFileMetaData() throws Exception {
		// TODO Auto-generated method stub
		Path file=Paths.get("C:\\Users\\Administrator\\workspace\\MyProj\\src\\test_Oct28_Part1.txt");
		BasicFileAttributes basicAttr = Files.readAttributes(file, BasicFileAttributes.class);
		FileTime creationTime = basicAttr.creationTime();
		System.out.println("creationTime : " + creationTime);
		FileTime lastAccessTime = basicAttr.lastAccessTime();
		System.out.println("lastAccessTime : " + lastAccessTime);
		FileTime lastModifiedTime = basicAttr.lastModifiedTime();
		System.out.println("lastModifiedTime : " +lastModifiedTime);

	}
}