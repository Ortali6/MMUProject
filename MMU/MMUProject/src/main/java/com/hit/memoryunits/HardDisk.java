package com.hit.memoryunits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import com.hit.util.MMULogger;

public class HardDisk 
{
	private static HardDisk instance = null;
	private static Object locker = new Object();
	private final int _HARDSIDK_SIZE = 2048;
	private String _DEFAULT_FILE_NAME = "./resources/hardDiskFile/HardDisk.txt";
	private Map<Long,Page<byte[]>> currentPagesInHD;
	private File myHDFile;
	
	private HardDisk() throws FileNotFoundException, IOException, ClassNotFoundException
	{	
		myHDFile = new File(_DEFAULT_FILE_NAME);
		if(myHDFile.exists())
		{
			readFromHDFile();
		}
		else
		{
			boolean isCreated = false;
			do
			{
				isCreated = myHDFile.createNewFile();
			}while(!isCreated);
			
			_DEFAULT_FILE_NAME = myHDFile.getAbsolutePath();
			
			try
			{
				currentPagesInHD = new LinkedHashMap<>(_HARDSIDK_SIZE);
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(_DEFAULT_FILE_NAME));
				for (int i=0;i<_HARDSIDK_SIZE;i++)
				{
					currentPagesInHD.put((long)i, new Page<byte[]>((long)i,(new byte[]{(byte) i})));
				}
				outputStream.writeObject(currentPagesInHD);
				outputStream.flush();
				outputStream.close();
			}
			catch(IOException e) 
			{
				MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
			}
		}
		
	}
	
	public static HardDisk getInstance() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		if(instance == null) 
		{
			synchronized(locker)
			{
				if(instance == null)
				{
					instance = new HardDisk();
				}
			}
		}
		return instance;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void readFromHDFile() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		if(currentPagesInHD != null)
			currentPagesInHD.clear();
		currentPagesInHD = new LinkedHashMap<>();
		ObjectInputStream readMyHDFile = null;
				
		try
		{
			readMyHDFile = new ObjectInputStream(new FileInputStream(_DEFAULT_FILE_NAME));	
			Map<Long,Page<byte[]>> currPage = (LinkedHashMap<Long,Page<byte[]>>)readMyHDFile.readObject();
			currentPagesInHD.putAll(currPage);				
		}
		catch(FileNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		catch(IOException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		catch(ClassNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		finally
		{
			if(readMyHDFile != null)
			{
				readMyHDFile.close();
			}
		}
		
	}
	
	public Page<byte[]>	pageFault(Long pageId) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		MMULogger.getInstance().write(String.format("\nPF: " + pageId.toString()),Level.INFO);
		try
		{
			readFromHDFile();
			return currentPagesInHD.get(pageId);
		}
		catch(FileNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
			throw e;
		}
		catch(IOException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
			throw e;
		}
		catch(ClassNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
			throw e;
		}
		
	}
	
	public void writeToHDFile(Map<Long,Page<byte[]>> pages) throws IOException 
	{
		ObjectOutputStream writeToMyHDFile = null;
		if(!myHDFile.exists())
		{
			while(myHDFile.createNewFile() == false){}
			
			_DEFAULT_FILE_NAME = myHDFile.getAbsolutePath();
		}
		try
		{
			writeToMyHDFile = new ObjectOutputStream(new FileOutputStream(_DEFAULT_FILE_NAME));	
			writeToMyHDFile.writeObject(pages);
		}
		catch(IOException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		finally
		{
			if(writeToMyHDFile != null)
			{
				writeToMyHDFile.flush();
				writeToMyHDFile.close();
			}
		}
	}

	public Page<byte[]>	pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		MMULogger.getInstance().write(String.format("\nPR: MTH " + moveToHdPage.getPageId() + "   " + "MTR " + moveToRamId.toString()), Level.INFO);
		Page<byte[]> pageToReturn = null;
		try
		{
			readFromHDFile();
			currentPagesInHD.put(moveToHdPage.getPageId(), moveToHdPage);
			writeToHDFile(currentPagesInHD);
			
			pageToReturn = currentPagesInHD.get(moveToRamId);
		}
		catch(FileNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		catch(IOException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		catch(ClassNotFoundException e)
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		return pageToReturn;		
	}
	
}
