package com.hit.processes;

import java.util.List;

public class ProcessCycles 
{
	private List<ProcessCycle> processCycles = null;
	
	public ProcessCycles(List<ProcessCycle> processCycles)
	{
		this.processCycles = processCycles;
		
	}
	
	public List<ProcessCycle> getProcessCycles()
	{
		return processCycles;
	}

	public void setProcessCycles(List<ProcessCycle> processCycles)
	{
		this.processCycles = processCycles;
	}
	
	@Override
	public String toString()
	{
		StringBuilder myString = new StringBuilder();
		for(ProcessCycle currPC : processCycles)
		{
			myString.append("\n{,");
			myString.append(currPC.toString());
			myString.append("\n},");
		}
		myString.delete(myString.capacity()-2, myString.capacity());
		
		return myString.toString();
	}
	
}
