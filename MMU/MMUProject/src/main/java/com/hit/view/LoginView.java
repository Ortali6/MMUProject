package com.hit.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends Observable implements View, ActionListener
{
	private JFrame loginViewFrame;
	private JLabel user_NameLabel;
	private JLabel passwordLabel;
	private JLabel file_NameLabel;
	private JTextField userNameTextField;
	private JPasswordField passwordTextField;
	private JTextField file_NameTextField;
	private JButton loginButton;
	private List<String> authentication = null;
	private boolean isCorrect = true;
	
	@Override
	public void start() 
	{
		
		loginViewFrame = new JFrame("MMU Login");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		loginViewFrame.setBounds(100, 100, 200, 150);
		loginViewFrame.setLocation(dim.width/2-loginViewFrame.getSize().width/2, dim.height/2-loginViewFrame.getSize().height/2);		
		loginViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginViewFrame.getContentPane().setLayout(null);
		
		user_NameLabel = new JLabel("User name: ");
		user_NameLabel.setBounds(10, 16, 89, 20);
		loginViewFrame.getContentPane().add(user_NameLabel);  
			
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(10, 36, 89, 20);
		loginViewFrame.getContentPane().add(passwordLabel);
		
		file_NameLabel = new JLabel("File Name:");
		file_NameLabel.setBounds(10, 56, 89, 20);
		loginViewFrame.getContentPane().add(file_NameLabel); 
		
		userNameTextField = new JTextField("");
		userNameTextField.setBounds(80, 16, 89, 20);
		loginViewFrame.getContentPane().add(userNameTextField);  
		
		passwordTextField = new JPasswordField("");
		passwordTextField.setBounds(80, 36, 89, 20);
		loginViewFrame.getContentPane().add(passwordTextField);
		
		file_NameTextField = new JTextField("");
		file_NameTextField.setBounds(80, 56, 89, 20);
		loginViewFrame.getContentPane().add(file_NameTextField); 
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		loginButton.setBounds(80, 80, 89, 20);
		loginViewFrame.getContentPane().add(loginButton); 
		
		loginViewFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == loginButton)
		{
			if(AllTextFiledIsFilled())
			{
				checkIfAllCorrect();
				if(!isCorrect)
				{
					int dialogButton = JOptionPane.OK_OPTION;
					JOptionPane.showConfirmDialog(null, "Please try again", "The details is not correct", dialogButton);
				}
				else
				{
					loginViewFrame.setVisible(false);
				}
			}
			else
			{
				int dialogButton = JOptionPane.OK_OPTION;
				JOptionPane.showConfirmDialog(null, "Please try again", "Not all field is full", dialogButton);			
			}
		}
		
	}

	private boolean AllTextFiledIsFilled()
	{
		if(userNameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || file_NameTextField.getText().isEmpty())
			return false;
		return true;
		
	}
	
	@SuppressWarnings("deprecation")
	private void checkIfAllCorrect()
	{
		authentication = new ArrayList<String>();
		authentication.add(userNameTextField.getText());
		authentication.add(passwordTextField.getText());
		authentication.add(file_NameTextField.getText());
		loginViewFrame.setVisible(false);
		setChanged();
		notifyObservers(authentication);
	}
	
	public synchronized void setIsCorrect(boolean isCorrect)
	{
		this.isCorrect = isCorrect;
		if(!isCorrect)
			loginViewFrame.setVisible(true);
	}


}
