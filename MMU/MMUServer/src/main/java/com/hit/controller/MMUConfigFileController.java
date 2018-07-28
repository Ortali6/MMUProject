package com.hit.controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hit.businesslogic.MMUConfigFileFacade;

public class MMUConfigFileController 
{
	private boolean serverUp = true;

	public void start() 
	{	
		try 
		{ 
			ServerSocket server = new ServerSocket(12345); 
			ExecutorService Users = Executors.newFixedThreadPool(25);
		
			while(serverUp)
			{
				Socket someClient = server.accept();	
				MMUConfigFileFacade Facade = new MMUConfigFileFacade(someClient);
				Users.execute(Facade);
			}
			
			server.close();
		}
		catch(Exception e)
		{
			System.out.println("Server Error");
		}
	}	

	//(new MMUConfigFileFacade(someClient)).start();
}
