package com.hit.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class MMUClient 
{
	private String logFile = null;
	private boolean succeed = true;
	public Socket myServer;
	
	public MMUClient()
	{
		try {
			this.myServer = new Socket("localhost", 12345);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Request(String userName, String password, String fileName)
	{
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				try
				{ 				
					ObjectOutputStream output = new ObjectOutputStream(myServer.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(myServer.getInputStream());
					output.writeObject(userName);
					output.writeObject(password);
					output.writeObject(fileName);
					
					boolean isReadable = (boolean)input.readObject();
					if(!isReadable)
					{
						succeed = false;
					}
					
					String fileOrError = (String)input.readObject();
					
					if(fileOrError.intern().equals("ERROR"))
					{
						succeed = false;
					}
					else
					{						
						String checkAgain = (String)input.readObject();
						
						if(checkAgain.intern().equals("ERROR") || checkAgain == null)
						{
							succeed = false;
						}
						else
						{
							logFile = checkAgain;
						}
					}
					
					output.flush();
					output.close(); 
					input.close();
					
				}
				catch (Exception e) 
				{
					System.out.println("Exception");
				}
			}
		}).start();
	}

	public boolean getSucceeded() 
	{
		return succeed;
	}
	
	public String getLogFile() 
	{
		return logFile;
	}
}
