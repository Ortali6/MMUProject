package com.hit.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import com.hit.util.MMULogger;

public class CLI extends Observable implements View, Runnable
{
	public static final String LFU = "LFU";
	public static final String LRU = "LRU";
	public static final String SECOND_CHANCE = "SECOND_CHANCE";
	public static final String START = "START";
	public static final String STOP = "STOP";
	private String choosedAlgo = null;
	private int cacheSize = 1024;
	private Scanner input;
	private PrintStream output;
	private String choosenView = null;
	
	public CLI(InputStream in, OutputStream out) 
	{
		input = new Scanner(in);
		output = new PrintStream(out);
		start();
	}
	
	public String[] getConfiguration()
	{
		String[] algoAndCacheSize = {choosedAlgo, Integer.toString(cacheSize)};
		return  algoAndCacheSize;
	}
	
	public void run() 
	{		
		setChanged();
		notifyObservers(choosenView);
	}
	
	public void write(String string)
	{
		output.println(string);
	}
	
	
	@SuppressWarnings("finally")
	private boolean isAlgoSizeAndView()
	{
		String sizeOfAlgoInString = null;
		String[] algoNameAndSize = input.nextLine().split(" ");
		boolean isAlgoSizeAndView = false;
		if(algoNameAndSize.length == 3)
		{
			if(algoNameAndSize[0].equalsIgnoreCase(LFU) || algoNameAndSize[0].equalsIgnoreCase(LRU) || algoNameAndSize[0].equalsIgnoreCase(SECOND_CHANCE))
			{
				sizeOfAlgoInString = algoNameAndSize[1];
				try
				{
					Integer sizeOfAlgo = Integer.parseInt(sizeOfAlgoInString);
					if(sizeOfAlgo > 0)
					{
						cacheSize = sizeOfAlgo;
						choosedAlgo = algoNameAndSize[0];
						if(algoNameAndSize[2].equalsIgnoreCase("LOCAL") || algoNameAndSize[2].equalsIgnoreCase("REMOTE"))
						{
							choosenView = algoNameAndSize[2];
							isAlgoSizeAndView = true;
						}
					}
				}
				catch (NumberFormatException e)
				{
					MMULogger.getInstance().write("\n" + e.getMessage(),Level.SEVERE);
				}
				finally
				{
					return isAlgoSizeAndView;
				}
				
			}
		}
		return isAlgoSizeAndView;
	}
	
	@Override
	public void start() 
	{
		String currentInput = null;
		
		write("Please write START or STOP");
		currentInput = input.nextLine();
		
		while(!currentInput.equalsIgnoreCase(START))			
		{
			if(currentInput.equalsIgnoreCase(STOP))
			{
				write("Thank you");
				return;
			}
			
			write("Not a valid command,please try again (START or STOP):\n");
			currentInput = input.nextLine();
		}
		
		write("Please enter required algorithm, RAM capacity and 'Local' or 'Remote':\n");
		boolean isCorrect = false;
		do			
		{
			isCorrect = isAlgoSizeAndView();
			if(!isCorrect)
				write("Not LRU or LFU or SECOND_CHANCE + cache size + Local or Remote, please try again:\n");
		}while(!isCorrect);
	}
}
	
	


