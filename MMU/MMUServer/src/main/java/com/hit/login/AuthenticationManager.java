package com.hit.login;

import java.util.List;

import com.hit.dm.User;


public class AuthenticationManager 
{
	private List<User> users = null;
	
	public AuthenticationManager(List<User> users)
	{
		this.users = users;
	}
	
	public List<User> getUsers() 
	{
		return users;
	}
	
	public void setProcessesCycles(List<User> users)
	{
		this.users = users;
	}
	
	public boolean authenticate(String user, String password)
	{
		for(int i = 0; i < users.size(); i++)
		{
			if(users.get(i).getUserName().intern().equals(user) && users.get(i).getPassword().intern().equals(password))
				return true;
		}
		
		return false;
	}
}
