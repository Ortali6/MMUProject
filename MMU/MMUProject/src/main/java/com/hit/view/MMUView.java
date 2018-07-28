package com.hit.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MMUView extends Observable implements View, ActionListener
{
	JButton playButton;
	JButton play_AllButton;
	JButton resetButton;
	JButton pauseButton;
	
	JLabel page_faultLabel;
	JLabel page_ReplacementLabel;
	JLabel commandsLabel;
	JLabel procces_idLabel;
	JLabel page_idLabel;
	JLabel page_dataLabel;
	
	private int culumn;
	JComboBox<?> speedComboBox;
	
	static int	AMOUT_OF_BYTES_IN_PAGE;
	static int	RAM_CAPACITY;
	static int	AMOUT_OF_PROCESSSES;
	private JLabel speedLabel;
	private JLabel proccessesLabel;
	private JFrame mmuViewFrame;
	private Integer mmuViewFrameWidth;
	public String match = null;
	
	private JScrollPane dataListScroller;	
	private JTable dataTable;
	private DefaultTableModel tableModel;
	private int currentRowInTable = 0;
	
	private JTextPane page_faultTextField;
	private JTextPane page_ReplacementTextField;

	private JList<String> processList;
	
	private JList<String> logCommandList;
	private JScrollPane logCommandListscrollPane;
	private int indexLogCommand = 0;
	
	
	private Timer timer = null;	
	private List<String> logInfo;

	public MMUView()
	{	
		logInfo = new ArrayList<>();
	}

	@Override
	public void start() 
	{	
		createAndShowGui();
		setChanged();
		notifyObservers();
	}
	
	@SuppressWarnings("unchecked")
	private void createAndShowGui()
	{		
		mmuViewFrame = new JFrame("MMU Simulator");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mmuViewFrameWidth = 704;
		mmuViewFrame.setBounds(100, 100, mmuViewFrameWidth, 466);
		mmuViewFrame.setLocation(dim.width/2-mmuViewFrame.getSize().width/2, dim.height/2-mmuViewFrame.getSize().height/2);		
		mmuViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mmuViewFrame.getContentPane().setLayout(null);
		
		procces_idLabel = new JLabel("Process ID:");	
		procces_idLabel.setBounds(10, 13, 89, 14);
		mmuViewFrame.getContentPane().add(procces_idLabel);  
		
		page_idLabel = new JLabel("Page ID:");
		page_idLabel.setBounds(10, 30, 89, 14);
		mmuViewFrame.getContentPane().add(page_idLabel);  
		
		page_dataLabel = new JLabel("Page Data:");
		page_dataLabel.setBounds(10, 44, 89, 14);
		mmuViewFrame.getContentPane().add(page_dataLabel);  
					
		page_faultLabel = new JLabel("Page Fault Amount:");          
		page_faultLabel.setBounds(434, 224, 162, 14);    
		mmuViewFrame.getContentPane().add(page_faultLabel);            
		                                                      
		page_ReplacementLabel = new JLabel("Page Replacement Amount:");
		page_ReplacementLabel.setBounds(434, 250, 162, 14);       
		mmuViewFrame.getContentPane().add(page_ReplacementLabel);      
		                                                                                               
		playButton = new JButton("Play");                        
		playButton.addActionListener(this);                      
		playButton.setBounds(10, 300, 89, 23);                   
		mmuViewFrame.getContentPane().add(playButton);                 
		                                                      
		play_AllButton = new JButton("Play All");  
		play_AllButton.addActionListener(this);      
		play_AllButton.setBounds(120, 300, 89, 23);               
		mmuViewFrame.getContentPane().add(play_AllButton);              
		                   
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		pauseButton.setBounds(230, 300, 89, 23);
		mmuViewFrame.getContentPane().add(pauseButton);
		
		resetButton = new JButton("Reset");   
		resetButton.addActionListener(this);      
		resetButton.setBounds(10, 334, 89, 23);                  
		mmuViewFrame.getContentPane().add(resetButton);
		
		speedLabel = new JLabel("Speed of Play All");
		speedLabel.setBounds(120, 334, 150, 23);
		mmuViewFrame.getContentPane().add(speedLabel);
		
		
		speedComboBox = new JComboBox<Object>(ePlayAllSpeed.values());
		speedComboBox.setBounds(120, 360, 89, 23);
		mmuViewFrame.getContentPane().add(speedComboBox);
		
		proccessesLabel = new JLabel("Processes:");
		proccessesLabel.setBounds(340, 255, 89, 23);
		mmuViewFrame.getContentPane().add(proccessesLabel);
			
		processList = new JList<String>();         
		processList.setToolTipText("Select multiple by holding 'ctrl'");
		processList.setBorder(new LineBorder(new Color(0, 0, 0)));
		processList.setBackground(Color.WHITE);
		processList.setBounds(340, 274, 76, 142);	
		mmuViewFrame.getContentPane().add(processList); 
		
		commandsLabel = new JLabel("Log Commands:");
		commandsLabel.setBounds(434, 301, 110, 14);
		mmuViewFrame.getContentPane().add(commandsLabel);
		
		
		logCommandList = new JList<String>();
		logCommandList.setForeground(Color.BLACK);
		logCommandList.setBackground(Color.WHITE);
		logCommandList.setAutoscrolls(true);
		logCommandList.setBorder(new LineBorder(new Color(0, 0, 0)));
		logCommandList.setBounds(434, 315, 244, 101);
		logCommandList.setCellRenderer(new MyListCellRenderer());
		logCommandListscrollPane = new JScrollPane(logCommandList);
		logCommandListscrollPane.setBounds(434, 315, 244, 101);
		logCommandListscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		logCommandListscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mmuViewFrame.getContentPane().add(logCommandListscrollPane);
					
		page_faultTextField = new JTextPane();
		page_faultTextField.setEditable(false);
		page_faultTextField.setBounds(600, 218, 20, 20);
		page_faultTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setContentCenter(page_faultTextField);
		mmuViewFrame.getContentPane().add(page_faultTextField);
		
		page_faultTextField.setText("0");				
		page_ReplacementTextField = new JTextPane();
		page_ReplacementTextField.setEditable(false);
		page_ReplacementTextField.setBounds(600, 244, 20, 20);
		page_ReplacementTextField.setText("0");
		setContentCenter(page_ReplacementTextField);
		page_ReplacementTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mmuViewFrame.getContentPane().add(page_ReplacementTextField);
			
		
		this.mmuViewFrame.invalidate();
		this.page_faultTextField.invalidate();
		mmuViewFrame.setVisible(true);
	}
	
	private void setContentCenter(JTextPane textPane) 
	{
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}
	
	public enum ePlayAllSpeed
	{
		Slow(2000),
		Normal(1000),
		Fast(500),
		Immediate(1);
		
	  private int value;    

	  private ePlayAllSpeed(int value) 
	  {
	    this.value = value;
	  }

	  public int getValue() 
	  {
	    return value;
	  }
	}
	

	private void buildTable()
	{
		
		tableModel = new DefaultTableModel(AMOUT_OF_BYTES_IN_PAGE + 1,RAM_CAPACITY);
		dataTable = new JTable(tableModel);
		dataTable.setBounds(80, 20, RAM_CAPACITY * 40, AMOUT_OF_BYTES_IN_PAGE * 18);
		dataTable.setCellSelectionEnabled(false);
		dataTable.setVisible(true);
		dataTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataListScroller = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Integer mixWidth = Math.min(RAM_CAPACITY * 56, 600);
		dataListScroller.setBounds(80, 10, mixWidth, AMOUT_OF_BYTES_IN_PAGE * 27);				
		mmuViewFrame.getContentPane().add(dataListScroller);
		
		buildTableData();
	}

	private void buildTableData() 
	{
		for(int i = 0; i < RAM_CAPACITY; i++)
		{
			TableColumnModel colModel = dataTable.getColumnModel();
	        TableColumn col = colModel.getColumn(i);
	        col.setPreferredWidth(55);
		}
		
		for(int j = 0; j < RAM_CAPACITY; j++)
		{
			dataTable.setValueAt("Page ID: ", 0, j);				 
			dataTable.repaint();
			dataTable.invalidate();
		}
		
		for(int i = 1; i < AMOUT_OF_BYTES_IN_PAGE + 1; i++)
		{
			for(int j = 0; j < RAM_CAPACITY; j++)
			{
				dataTable.setValueAt(0, i, j);
			}
		}
		
		for(int column = 0; column < RAM_CAPACITY; column++)
			dataTable.getColumnModel().getColumn(column).setHeaderValue("Proc ID");
		
		dataTable.getTableHeader().repaint();
		dataTable.getTableHeader().invalidate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource() == playButton)
		{
			oneStepOneLine();
		}
		else 
		{
			if(arg0.getSource() == play_AllButton)
			{	
				playButton.setEnabled(false);
				play_AllButton.setEnabled(false);
				pauseButton.setEnabled(true);			
				speedComboBox.setEnabled(false);
				executeAllCommands();
			}
			else 
			{
				if(arg0.getSource() == pauseButton)
				{
					timer.stop();
					playButton.setEnabled(true);
					play_AllButton.setEnabled(true);
					pauseButton.setEnabled(false);			
					speedComboBox.setEnabled(true);		
				}
				else
				{
					if(arg0.getSource() == resetButton)
					{						
						resetAll();
					}
				}
			}
		}
		
	}
	
	private void resetAll() 
	{
		if(timer != null && timer.isRunning())
			timer.stop();
		playButton.setEnabled(true);
		play_AllButton.setEnabled(true);
		pauseButton.setEnabled(true);			
		speedComboBox.setEnabled(true);	
		speedComboBox.getModel().setSelectedItem(speedComboBox.getModel().getElementAt(0));
		page_faultTextField.setText("0");
		page_ReplacementTextField.setText("0");
		fillLogCommandList();
		buildTableData();
		processList.clearSelection();

		mmuViewFrame.invalidate();
		processList.invalidate();
		match = null;
		logCommandList.repaint();
		logCommandList.invalidate();		
		dataTable.invalidate();
		currentRowInTable = 0;
		indexLogCommand = 0;
	}

	private void executeAllCommands() 
	{
		ePlayAllSpeed timerMs = (ePlayAllSpeed)this.speedComboBox.getSelectedItem();		
		timer = new Timer(timerMs.getValue(), new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (indexLogCommand < logInfo.size())
				{
					oneStepOneLine();
				}
				else
				{
					pauseButton.setEnabled(false);
					play_AllButton.setEnabled(false);									
					timer.stop();
				}
			}
		});
		
		timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
        
	}
	
	public void setNeededInfo(int numProcesses, List<String> log, int ramCapacity) 
	{
		logInfo = log;
		RAM_CAPACITY = ramCapacity;
		AMOUT_OF_PROCESSSES = numProcesses;
		getAmountOfBytes();
		buildTheRest();
	}
	
	private void getAmountOfBytes() 
	{
		Iterator<String> commandsIterator = this.logInfo.iterator();
		String command;
		String subCommand;
		command = commandsIterator.next();
		while (commandsIterator.hasNext()) 
		{
			command = commandsIterator.next();
			if(command.startsWith("GP:")) 
			{
				subCommand = command.substring(command.indexOf("["), command.indexOf("]"));
				AMOUT_OF_BYTES_IN_PAGE = (subCommand.split(" ")).length;
				break;
			}
		}		
		
	}
	
	private void buildTheRest()
	{
		fillProcessList();
		fillLogCommandList();
		buildTable();
		mmuViewFrame.invalidate();	
	}
	
	private void fillProcessList()
	{
		processList.setListData(createProcessList().toArray(new String[0]));
		processList.setVisible(true);
	}
	
	private List<String> createProcessList()
	{
		List<String> processList = new ArrayList<String>();
		String process = "Process ";
		for (int i = 0 ; i < AMOUT_OF_PROCESSSES ; i++)
		{
			processList.add(process + i);
		}		
		return processList;
	}
	
	private void fillLogCommandList()
	{
		logCommandList.setListData(logInfo.toArray(new String[0]));
		logCommandList.setVisible(true);
	}
	
	public void oneStepOneLine()
	{
		if(indexLogCommand != logInfo.size())
		{
			String nextCommand = logInfo.get(indexLogCommand);
			String commandBeginning = nextCommand.substring(0, 2);
			switch(commandBeginning)
			{
				case "GP":
					executeGPCommand(nextCommand);
					break;
				case "PF":
					executePFCommand(nextCommand);
					break;
				case "PR":
					executePRCommand(nextCommand);
					break;
				default:
					break;
			}
			MarkCurrentLine();
	        mmuViewFrame.invalidate();
            logCommandList.invalidate();
            logCommandListscrollPane.invalidate();
            dataTable.getTableHeader().repaint();
    		dataTable.getTableHeader().invalidate();
			indexLogCommand++;
		}	
		
		if(indexLogCommand == logInfo.size())
		{
			int dialogButton = JOptionPane.YES_NO_OPTION; 
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to try again?", "The simulation is over!!!", dialogButton);
			if(dialogResult == 0) 
			{
				resetAll();
			}
			else
			{
				playButton.setEnabled(false);
				play_AllButton.setEnabled(false);
				pauseButton.setEnabled(false);
			}
		}
		
	}
	
	private void executePRCommand(String nextCommand) 
	{
		page_ReplacementTextField.setText(Integer.toString(Integer.parseInt(page_ReplacementTextField.getText().intern()) + 1));		
	}

	private void executePFCommand(String nextCommand) 
	{
		page_faultTextField.setText(Integer.toString(Integer.parseInt(page_faultTextField.getText().intern()) + 1));		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeGPCommand(String nextCommand) 
	{		
		List<String> pageDATA = new ArrayList(AMOUT_OF_BYTES_IN_PAGE);
		String[] parts = logInfo.get(indexLogCommand).split(" ");
		String ProcessID = parts[1];
		String ProcessNum = ProcessID.substring(1,ProcessID.length());

		if(processIsSelected(ProcessNum))
		{			
			if(currentRowInTable == RAM_CAPACITY)
			{
				currentRowInTable = 0;
			}
			
			String PageID = parts[2];			
			int start =  logInfo.get(indexLogCommand).indexOf("[") + 1;
			int end = logInfo.get(indexLogCommand).indexOf("]");
			String arrayOfData = logInfo.get(indexLogCommand).substring(start, end);
			String[] items = arrayOfData.split(", ");
			
			
			for (int i = 0; i < items.length; i++) 
			{
				pageDATA.add(items[i]);
			}
			
			culumn = alreadyInTable(PageID);
			
			if(culumn > -1)
			{
				for(int i = 1; i <= AMOUT_OF_BYTES_IN_PAGE; i++)
				{
					dataTable.setValueAt(pageDATA.get(i-1), i, alreadyInTable(PageID));
					
				}
			}
			else
			{
				culumn = currentRowInTable;
				
				dataTable.setValueAt(PageID , 0, currentRowInTable);
				for(int i = 1; i <= AMOUT_OF_BYTES_IN_PAGE; i++)
				{
					dataTable.setValueAt(pageDATA.get(i-1), i, currentRowInTable);
				}
				currentRowInTable++;
			}
			
			dataTable.getColumnModel().getColumn(culumn).setHeaderValue(ProcessID);
			mmuViewFrame.invalidate();
			dataTable.invalidate();
		}
		
	}
	
	private int alreadyInTable(String pageID) 
	{
		for(int i = 0; i < RAM_CAPACITY; i++)
		{
			if(dataTable.getValueAt(0, i).toString().intern().equals(pageID))
					return i;
		}
		return -1;
	}

	private boolean processIsSelected(String processNum)
	{
		Iterator<String> commandsIterator = processList.getSelectedValuesList().iterator();
		if(commandsIterator.hasNext())
		{
			String command;
			String subCommand;
			do
			{
				command = commandsIterator.next();
				subCommand = command.substring(command.indexOf(" ")+1, command.length());
				if(subCommand.intern().equals(processNum)) 
				{
					return true;
				}
				
			}while (commandsIterator.hasNext());
		}
		return false;
	}
		
	private void MarkCurrentLine()
	{
		match = logInfo.get(indexLogCommand);
	}
		
	 @SuppressWarnings({ "serial", "rawtypes" })
	class MyListCellRenderer extends JLabel implements ListCellRenderer 
	 {
	        public MyListCellRenderer() 
	        {
	            setOpaque(true);
	        }
	        public Component getListCellRendererComponent(JList paramlist, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	        {
	            setText(value.toString());
	            if (value.toString().equals(match)) 
	            {
		        	setBackground(Color.BLUE);	        	     
		        	logCommandListscrollPane.repaint();
		        	logCommandListscrollPane.invalidate(); 
	            }
	            else
	            {
	            	setBackground(Color.WHITE);	        	 
		        	logCommandListscrollPane.repaint();
		        	logCommandListscrollPane.invalidate();
	            }
	            return this;
	        }
	    }
	
}

