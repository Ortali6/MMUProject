package com.hit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LFUAlgoCacheImpl;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model
{
	private int numProcesses;
	private int ramCapacity;
	private String AlgoName;
	private boolean isLocal = false;
	private String remoteConfigPath = null;
	private RunConfiguration runConfig;
	
	public MMUModel() {}
	
	@Override
	public void start() 
	{	
		MMULogger.getInstance().write("RC: " + ramCapacity, Level.INFO);
		MemoryManagementUnit mmu = new MemoryManagementUnit(ramCapacity, returnAlgo());
		if(isLocal)
		{
			runConfig = readConfigurationFile();
		}
		else
		{
			runConfig = setJsonConfiguration();
		}
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		List<Process> processes = createProcesses(processCycles, mmu);
		numProcesses = processes.size();
		runProcesses(processes);
		MMULogger.getInstance().close();
		
		setChanged();
		notifyObservers();
	}
	
	public void setConfiguration(List<String> configuration)
	{
		ramCapacity = Integer.parseInt(configuration.get(1));
		AlgoName = configuration.get(0);
	}
	
	private static void runProcesses(List<Process> applications)
	{
		ExecutorService executor = Executors.newCachedThreadPool();
		if(applications != null)
		{
			for(Process currProcess : applications)
			{
				executor.execute(new Thread(currProcess));
			}
			
			executor.shutdown();
			
			try 
			{
				executor.awaitTermination(2, TimeUnit.MINUTES);
			}
			catch (InterruptedException e) 
			{
				MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
			}
		}
	}
	
	private static List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu)
	{	
		List<Process> processListToReturn = new ArrayList<>();
		int processId = 0;
		if(appliocationsScenarios != null)
		{

			for(ProcessCycles currPCS : appliocationsScenarios)
			{
				processListToReturn.add(new Process(processId ,mmu, currPCS));
				processId++;
			}
		}
		MMULogger.getInstance().write("\nPN: " + processId + 1 ,Level.INFO);
		return processListToReturn;
		
	}
		
	private static RunConfiguration readConfigurationFile() 
	{
		FileReader configToReturn = null;
		try 
		{
			configToReturn = new FileReader("./resources/Configuration.json");
		} 
		catch (FileNotFoundException e) 
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		return new Gson().fromJson(new JsonReader(configToReturn), RunConfiguration.class);
	}

	private IAlgoCache<Long, Long> returnAlgo()
	{
		switch(AlgoName.toLowerCase()) 
		{
			case "lru": 
			{
				return new LRUAlgoCacheImpl<Long, Long>(ramCapacity);			
			}
			case "lfu": 
			{
				return new LFUAlgoCacheImpl<Long, Long>(ramCapacity);			
			}
			case "SECOND_CHANCE":
			{
				return new SecondChanceAlgoCacheImpl<Long, Long>(ramCapacity);		
			}
			default:
				return null;
			}
		}
	
	public int getNumProcesses()
	{
		return numProcesses;
	}

	public List<String> getLog() 
	{
		List<String> log = null;
		try 
		{
			log	= Files.readAllLines(Paths.get(MMULogger.getInstance().getPathName()));
		} 
		catch (IOException e) 
		{
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			MMULogger.getInstance().close();
		}
		return log;
	}

	public int getRamCapacity() 
	{
		return ramCapacity;
	}
	
	public void setLocalOrRemote(String localOrRemote) 
	{
		if(localOrRemote.intern().equalsIgnoreCase("local"))
		{
			isLocal = true;
		}
		else
		{
			isLocal = false;
		}
			
		
	}

	
	public RunConfiguration setJsonConfiguration() 
	{
		FileReader configToReturn = null;
		try 
		{
			configToReturn = new FileReader(remoteConfigPath);
		} 
		catch (FileNotFoundException e) 
		{
			MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
		}
		return new Gson().fromJson(new JsonReader(configToReturn), RunConfiguration.class);
		
	}
	

	public void setRemoteConfigPath(String jsonFile) 
	{
		remoteConfigPath = jsonFile;
	}
}

