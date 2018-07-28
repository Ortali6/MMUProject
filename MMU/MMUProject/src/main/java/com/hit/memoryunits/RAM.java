package com.hit.memoryunits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RAM 
{
	
	private int capacity;
	private Map<Long,Page<byte[]>> currentPagesInRam;
	
	public RAM(int initialCapacity) 
	{
		if(initialCapacity >= 0)
			capacity = initialCapacity;
		else
			capacity = 1024;
		
		currentPagesInRam = new HashMap<>(capacity);
	}
	
	public Map<Long,Page<byte[]>> getPages()
	{
		return currentPagesInRam;		
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds)
	{
		List<Page<byte[]>> pagesToReturn = new ArrayList<>();
		Page<byte[]> pagesToReturn1[];
		for(Long currIdKey  : pageIds)
		{
			Page<byte[]> currPageValue = getPage(currIdKey);
			if(currPageValue != null)
			{
				pagesToReturn.add(currPageValue);
			}
		}

		if(pagesToReturn.size() > 0)
		{
			pagesToReturn1 = new Page[pagesToReturn.size()];
			for(int i = 0; i < pagesToReturn.size(); i++)
			{
				pagesToReturn1[i] = pagesToReturn.get(i);
			}
			return pagesToReturn1;
		}
		else
		{
			return null;
		}	
	}
	
	public Page<byte[]> getPage(Long pageId)
	{
		return currentPagesInRam.get(pageId);		
	}
	
	public void setPages(Map<Long,Page<byte[]>> pages)
	{
		currentPagesInRam.clear();
		currentPagesInRam.putAll(pages);
	}
	
	public void addPages(Page<byte[]>[] addPages)
	{
		for(Page<byte[]> currPage : addPages)
		{
			addPage(currPage);
		}
	}
	
	public void addPage(Page<byte[]> addPage)
	{
		if(currentPagesInRam.size() < capacity)
		{
			currentPagesInRam.put(addPage.getPageId(), addPage);
		}
	}
	
	public void removePages(Page<byte[]>[] removePages)
	{
		for(Page<byte[]> currPage : removePages)
		{
			removePage(currPage);
		}
	}
	
	public void removePage(Page<byte[]> removePage)
	{
		Long currLongIDKey = removePage.getPageId();
		if(currentPagesInRam.containsKey(currLongIDKey))
		{
			currentPagesInRam.remove(currLongIDKey);
		}
	}
	
	public int getInitialCapacity()
	{
		return capacity;
	}
	
	public int getCurrentMemorySize()
	{
		return currentPagesInRam.size();
	}
	
	public void setInitialCapacity(int initialCapacity)
	{
		Map<Long, Page<byte[]>> temp;
		
		if(initialCapacity >= 0)
		{		
			capacity = initialCapacity;
			temp = getPages();
			currentPagesInRam = new HashMap<>(capacity);
			setPages(temp);
		}
	}
	
	public Map<Long,Page<byte[]>> getAllPagesInRAM()
	{
		return currentPagesInRam;
	}
}
