package com.hit.processes;

import java.util.List;



public class ProcessCycle 
{
	private List<Long> pages = null;
	private int sleepMs;
	private List<byte[]> data = null;
	
	public ProcessCycle(List<Long> pages, int sleepMs, List<byte[]> data)
	{
		this.pages = pages;
		if(this.sleepMs >= 0)
			this.sleepMs = sleepMs;
		else
			this.sleepMs = 3;
		this.data = data;
	}
	
	public List<Long> getPages()
	{
		return pages;
	}
	
	public void setPages(List<Long> pages)
	{
		if(pages != null)
		{
			this.pages = pages;
		}
	}
	
	public int getSleepMs()
	{
		return sleepMs;
	}
	
	public void setSleepMs(int sleepMs)
	{
		if(sleepMs >= 0)
			this.sleepMs = sleepMs;	
		else
			sleepMs = 3;
	}
	
	public List<byte[]> getData()
	{
		return data;
	}
	
	public void setData(List<byte[]> data)
	{
		if(data != null)
			this.data = data;
	}
	
	@Override
	public String toString()
	{
		StringBuilder myString = new StringBuilder();
		myString.append("\n''pages'': [");

		for(Long currID : pages)
		{
			myString.append("\n" + currID + ",");

		}
		myString.delete(myString.capacity()-2, myString.capacity());
		myString.append("\n],\n''sleepMs'': " + getSleepMs() + ",");
		myString.append("\''ndata'': [");
	
		for(byte[] currData : data)
		{	
			myString.append("\n[");
			for(byte currBtye : currData)
			{
				myString.append("\n" + currBtye + ",");
			}
			myString.delete(myString.capacity()-2, myString.capacity());		
			myString.append("\n],");													
		}
		myString.delete(myString.capacity()-2, myString.capacity());
		myString.append("\n]");	
		return myString.toString();
		
	}
}
