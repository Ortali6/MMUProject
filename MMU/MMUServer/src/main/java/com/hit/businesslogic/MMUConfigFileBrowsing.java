package com.hit.businesslogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MMUConfigFileBrowsing
{
	private static Object locker = new Object();
	private String fileName;
	private final static String DEFAULT_FILE_NAME = "src/main/resources/listOfConfigs/ListOfConfigNames.txt";
	private Map<String,String> listOfFileNames = null;
	
	public MMUConfigFileBrowsing(String ifileName) throws IOException
	{
		this.fileName = ifileName;
		listOfFileNames = new HashMap<>();
		List<String> fileNames = new ArrayList<>(); 
		fileNames = Files.readAllLines(Paths.get(DEFAULT_FILE_NAME));
		for(String fName : fileNames)
		{
			listOfFileNames.put(fName, "src/main/resources/com/hit/data/" + fName);

		}
		
	}
	
	public File getFileToClient(String fileName) 
	{
		File file = null;
		if(listOfFileNames.containsKey(fileName))
			file = new File(listOfFileNames.get(fileName));
		return file;
	}
	
	public void sendFileToClient(ObjectOutputStream output, File file) throws IOException, ClassNotFoundException
	{
	    InputStream in = new FileInputStream(file.getPath());

	    synchronized(locker)
	    {
	    	if(listOfFileNames.containsKey(fileName))
	    	{
		    JsonParser parser = new JsonParser();
		    Object obj = parser.parse(new FileReader(file.getPath()));
	        JsonObject jsonObject =  (JsonObject) obj;
	        String pathName = "./resources/newConfigFiles/" + fileName;
	        output.writeObject(pathName);

	    	}
	    	else
	    	{
		        output.writeObject("ERROR");
	    	}
	    }
	    in.close();	

	}
}
