package com.hit.memoryunits;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import com.hit.algorithm.LRUAlgoCacheImpl;

public class MMUTest 
{
	@Test
	public void testMemoryManagementUnit() throws FileNotFoundException, ClassNotFoundException, IOException
	{
		MemoryManagementUnit mmu = new MemoryManagementUnit(3, new LRUAlgoCacheImpl<Long, Long>(3));
		Page<byte[]>[] pagesFromRam;
		try 
		{
			System.out.println("MMUtest:");
			pagesFromRam = mmu.getPages(new Long[]{(long)1, (long)2, (long)3});
			for(int i = 0; i<3; i++) 
			{	
				Assert.assertEquals((long)pagesFromRam[i].getPageId(), (long)(i+1));
				System.out.println(pagesFromRam[i].getPageId());
			}
			System.out.println();
			
			pagesFromRam = mmu.getPages(new Long[]{(long)2, (long)3, (long)4});
			for(int i = 0; i<3; i++) 
			{	
				Assert.assertEquals((long)pagesFromRam[i].getPageId(), (long)(i+2));
				System.out.println(pagesFromRam[i].getPageId());
			}
			System.out.println();
			
			pagesFromRam = mmu.getPages(new Long[]{(long)3, (long)2, (long)1});
			for(int i = 0; i<3; i++) 
			{	
				Assert.assertEquals((long)pagesFromRam[i].getPageId(), (long)(3-i));
				System.out.println(pagesFromRam[i].getPageId());
			}
			System.out.println();
		} 
		catch (ClassNotFoundException | IOException e) 
		{
			e.printStackTrace();
		}
	}
}
