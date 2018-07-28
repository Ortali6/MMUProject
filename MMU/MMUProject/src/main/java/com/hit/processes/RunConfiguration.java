package com.hit.processes;

import java.util.List;

public class RunConfiguration 
{
	private List<ProcessCycles> processesCycles = null;
	
	public RunConfiguration(List<ProcessCycles> processesCycles)
	{
		this.processesCycles = processesCycles;
	}
	
	public List<ProcessCycles> getProcessesCycles() 
	{
		return processesCycles;
	}
	
	public void setProcessesCycles(List<ProcessCycles> processesCycles)
	{
		this.processesCycles = processesCycles;
	}
	
	@Override
	public String toString()
	{
		StringBuilder myString = new StringBuilder();
		myString.append("\n''processesCycles'': [ ");
		
		for(ProcessCycles currPCS : processesCycles)
		{
			myString.append("\n{\n''processCycles'': [");
			myString.append(currPCS.toString());
			myString.append("},");
		}
		myString.delete(myString.capacity()-2, myString.capacity());
		myString.append("\n]\n}");
		return myString.toString();
	}
}
