package com.hit.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.model.MMUClient;
import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.CLI;
import com.hit.view.LoginView;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller, Observer
{
	Model model;
	View view;
	View loginView;
	MMUClient client;
	String localOrRemote = null;
	
	public MMUController(Model model, View[] views) 
	{
		this.model = model;
		this.view = views[0];
		this.loginView = views[1];
		
	}

	@Override
	public void update(Observable o, Object arg) 
	{	
		if(o instanceof CLI)
		{
			localOrRemote = arg.toString();
			((MMUModel)model).setConfiguration(Arrays.asList(((CLI) o).getConfiguration()));
			((MMUModel)model).setLocalOrRemote(localOrRemote.intern());
						
			if(localOrRemote.intern().equalsIgnoreCase("local"))
			{
				model.start();
			}
			else
			{
				loginView.start();
			}								
		}
		
		if(o == view)
		{
			((MMUView)view).setNeededInfo(((MMUModel)model).getNumProcesses(),((MMUModel)model).getLog(), ((MMUModel)model).getRamCapacity());
		}
		
		if(o == model)
		{
			view.start();			
		}
		
		if(o == loginView)
		{
			client = new MMUClient();
			@SuppressWarnings("unchecked")
			List<String> authentication = (List<String>) arg;
	
			client.Request(authentication.get(0), authentication.get(1), authentication.get(2));
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

			if(client.getSucceeded() && client.getLogFile() != null)
			{
				((LoginView)loginView).setIsCorrect(true);
				((MMUModel)model).setRemoteConfigPath(client.getLogFile());
				model.start();
			}
			else
			{				
				((LoginView)loginView).setIsCorrect(false);
			}
			
		}

	}



}
