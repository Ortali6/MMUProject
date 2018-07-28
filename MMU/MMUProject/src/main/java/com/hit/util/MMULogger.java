package com.hit.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MMULogger 
{
	public final static String DEFAULT_FILE_NAME = "./logs/log.txt";
	private static FileHandler handler;
	private static MMULogger instance = null;
	private static Object lock = new Object();
	private Logger logger;
	
	// Private default constructor for creating a handler and setting the handler format.
	private MMULogger() 
	{
		logger = Logger.getLogger(DEFAULT_FILE_NAME);
		try
		{
			File logFile = new File(DEFAULT_FILE_NAME);
			if(logFile.exists()) 
			{
				logFile.delete();
			}
			
			handler = new FileHandler(DEFAULT_FILE_NAME);
		} 
		
		catch (SecurityException | IOException e) 
		{
			MMULogger.getInstance().write(e.toString()+ "\n", Level.SEVERE);
			e.printStackTrace();
		}
		
		handler.setFormatter(new OnlyMessageFormatter());
		logger.addHandler(handler);
	}
		
	public static MMULogger getInstance() 
	{		
		if(instance == null) 
		{
			synchronized(lock) 
			{
				if(instance == null) 
				{
					instance = new MMULogger();
				}
			}
		}
		return instance;
	}
	
	public synchronized void write(String command, Level level) 
	{
		logger.log(level, command);
	}
	
	public void close() 
	{
		if(handler != null) 
		{
			handler.close();
			handler = null;
		}
	}
	
	public class OnlyMessageFormatter extends Formatter 
	{
		public OnlyMessageFormatter() 
		{
			super();
		}
		
		@Override
		public String format(final LogRecord record) 
		{
			return record.getMessage();
		}
		
	}

	public String getPathName()
	{
		return DEFAULT_FILE_NAME;
	}
}
