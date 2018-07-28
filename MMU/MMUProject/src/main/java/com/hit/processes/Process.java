package com.hit.processes;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Runnable
{
	private int processId;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles)
	{
		this.processId = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
	}
	
	public int getId()
	{
		return processId;
		
	}
	
	public void setId(int id)
	{
		this.processId = id;
	}
	
	@Override
	public void run() 
	{
		List<ProcessCycle> processes = processCycles.getProcessCycles();
		Page<byte[]>[] currPagesArray = null;
		
		
		for(ProcessCycle currProccess : processes)
		{
			
			Long[] IDArray = currProccess.getPages().toArray(new Long[currProccess.getPages().size()]);
			List<byte[]> DataArray = currProccess.getData();
			
			synchronized(mmu)
			{
				try
				{
					currPagesArray = mmu.getPages(IDArray);
				}
				catch (ClassNotFoundException | IOException e) 
				{
					MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
				}
			
				if(currPagesArray != null)
				{
					for(int i = 0; i < currPagesArray.length; i++)
					{
						currPagesArray[i].setContent(DataArray.get(i));
						StringBuilder writeToLogger = new StringBuilder();
						writeToLogger.append(String.format("\nGP: P" + getId() + " " + currPagesArray[i].getPageId() + " ["));
						
						int currI = 1;
						int amountOfByes = currPagesArray[i].getContent().length;
						
						for(byte currByte : currPagesArray[i].getContent())
						{							
							writeToLogger.append(currByte);
							if(currI < amountOfByes)							
								writeToLogger.append(", ");
							currI++;
						}
						writeToLogger.append("]");
						MMULogger.getInstance().write(writeToLogger.toString(), Level.INFO);
					}
				}				
			}
			try
			{
				Thread.sleep(currProccess.getSleepMs());
			}
            catch(InterruptedException e)
			{
            	MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
            }
		}
	}
}
		
