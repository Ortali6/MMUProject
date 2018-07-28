package com.hit.memoryunits;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LFUAlgoCacheImpl;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;

public class MemoryManagementUnit 
{
	private int myRamCapacity;
	private IAlgoCache<Long, Long> myAlgo;
	private HardDisk myHD;
	private RAM myRAM;

	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		if (ramCapacity > 0) {
			myRamCapacity = ramCapacity;
		} else {
			myRamCapacity = 1024;
		}
		if (algo != null) {
			if (algo instanceof LFUAlgoCacheImpl)
				myAlgo = new LFUAlgoCacheImpl<>(myRamCapacity);
			else {
				if (algo instanceof LRUAlgoCacheImpl)
					myAlgo = new LRUAlgoCacheImpl<>(myRamCapacity);
				else {
					if (algo instanceof SecondChanceAlgoCacheImpl)
						myAlgo = new SecondChanceAlgoCacheImpl<>(myRamCapacity);
					else {
						myAlgo = new LFUAlgoCacheImpl<>(myRamCapacity);
					}
				}
			}
		} else
			myAlgo = new LFUAlgoCacheImpl<>(myRamCapacity);

		myRAM = new RAM(myRamCapacity);	
	}

	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds) throws IOException, ClassNotFoundException {
		myHD = HardDisk.getInstance();
		Page<byte[]> pagesToReturn[] = new Page[pageIds.length];

		int i = 0;
		if (pageIds != null) 
		{
			for (Long currID : pageIds) 
			{
				if (currID != null) 
				{
					List<Long> currVal = myAlgo.getElement(Arrays.asList(currID)); 
					// sends only 1	ID - i will get list size 1 or 0(null)
					if (currVal != null) 
					{
						pagesToReturn[i] = myRAM.getPage(currVal.get(0));
					} else // not in Algo - not in RAM
					{
						pagesToReturn[i] = addToRAM(currID);
					}
					i++;
				}
			}
		}

		return pagesToReturn;
	}
	
	private Page<byte[]> addToRAM(Long currIDToAddToRAM)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		Page<byte[]> pageToReturn = null;
		if (myRAM.getCurrentMemorySize() < myRAM.getInitialCapacity()) // if RAM notfull - add to algo and RAM
		{
			pageToReturn = myHD.pageFault(currIDToAddToRAM);
			if (pageToReturn != null) 
			{
				myRAM.addPage(pageToReturn);
				myAlgo.putElement(Arrays.asList(currIDToAddToRAM), Arrays.asList(currIDToAddToRAM));
			}
		} 
		else // RAM is full - send to Algo - to get ID to remove.
		{
			List<Long> IDToRemove = myAlgo.putElement(Arrays.asList(currIDToAddToRAM), Arrays.asList(currIDToAddToRAM)); // removes from ALGO and return what been removed - only 1 page get removed.
			if (IDToRemove != null) {
				Long currIDToRemove = IDToRemove.get(0);
				if (currIDToRemove != null) {
					Page<byte[]> currPageToRemoveFromRAM = myRAM.getPage(currIDToRemove);
					myRAM.removePage(currPageToRemoveFromRAM);
					pageToReturn = myHD.pageReplacement(currPageToRemoveFromRAM, currIDToAddToRAM);
					if (pageToReturn != null)
						myRAM.addPage(pageToReturn);
					else // if its not in the ram its not in myAlgo.
						myAlgo.removeElement(Arrays.asList(currIDToAddToRAM));
				}
			}
		}
		return pageToReturn;
	}
	
	public void ShutDown() throws FileNotFoundException, ClassNotFoundException, IOException
	{	
		Map<Long,Page<byte[]>> pages = myRAM.getAllPagesInRAM();
		myHD.writeToHDFile(pages);
	}
}
