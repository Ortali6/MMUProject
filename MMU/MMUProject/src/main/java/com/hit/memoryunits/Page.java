package com.hit.memoryunits;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Page<T> implements Serializable
{
	private Long pageID;
	
	private T pageContent;

	public Page(Long id,T content)
	{
		pageID = id;
		pageContent = content;
	}
	
	public Long getPageId()
	{
		return pageID;
	}
	
	public void setPageId(Long pageId)
	{
		pageID = pageId;
	}
	
	public T getContent()
	{
		return pageContent;	
	}
	
	public void setContent(T content)
	{
		pageContent = content;
	}
	
	@Override
	public int hashCode()
	{
		return (13 * pageID.intValue()) % Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Page<?>)
		{
			Page<T> temp = (Page<T>)obj;
			return temp.hashCode() == hashCode();
		}
		else
			return false;
		
	}
	
	@Override
	public String toString()
	{
		return String.format("Page ID: {0}\nContent: {1}", pageID.toString(), Arrays.asList(pageContent).toString());	
	}
}
