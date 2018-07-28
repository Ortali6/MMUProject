package com.hit.driver;

import java.io.IOException;

import com.hit.controller.MMUConfigFileController;

public class MMUServerDriver 
{
		public static void main(String[] args) throws IOException 
		{
			new MMUConfigFileController().start();
		}
}
