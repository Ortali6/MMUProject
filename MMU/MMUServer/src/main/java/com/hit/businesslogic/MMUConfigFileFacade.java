package com.hit.businesslogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.login.AuthenticationManager;

public class MMUConfigFileFacade implements Runnable
{
	private String userName;
	private String password;
	private String fileName;
	private Socket someClient;
	AuthenticationManager authenticManager;
	MMUConfigFileBrowsing browsingConfigFile;
	
	public MMUConfigFileFacade(Socket someClient)
	{
		this.someClient = someClient;
	}
	
	private AuthenticationManager readConfigurationFile() 
	{
		FileReader configToReturn = null;
		try 
		{
			configToReturn = new FileReader("src/main/resources/com/hit/login/Users.json");
		} 
		catch (FileNotFoundException e) 
		{
			
		}
		return new Gson().fromJson(new JsonReader(configToReturn), AuthenticationManager.class);
	}

	@Override
	public void run() 
	{		
		boolean succeed;
		ObjectOutputStream output;
		ObjectInputStream input;
		try
		{
			input = new ObjectInputStream(someClient.getInputStream()); 
			output = new ObjectOutputStream(someClient.getOutputStream());
			
			output.flush();
			
			userName = (String)input.readObject(); 
			password = (String)input.readObject(); 
			fileName = (String)input.readObject(); 
			authenticManager = readConfigurationFile();
			
			succeed = authenticManager.authenticate(userName, password);
			output.writeObject(succeed);
			
			if(succeed)
			{
				browsingConfigFile = new MMUConfigFileBrowsing(fileName);
				File file = browsingConfigFile.getFileToClient(fileName);
				if(!file.exists()) output.writeObject("ERROR");
				else output.writeObject("file exist");
				browsingConfigFile.sendFileToClient(output,file);
			}
			
			output.flush();
			output.close();
			input.close();  
			someClient.close(); 
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.exit(0);
		}
	}
	
}
