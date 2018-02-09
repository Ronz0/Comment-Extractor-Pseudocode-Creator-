/*
Written by Ron McLay


This program takes all your comments and outputs them neatly as one paragraph.

Simply copy all your code (ctrl+A, ctrl+C) and then paste it into text area and click convert;

*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;


public class Main {

	public static void main(String[] args) throws IOException
	{
		
		//Sets up all the Swing components
		JFrame frame = new JFrame ("Code Comment Extractor");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		//sets up panels
		JPanel primary = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.LIGHT_GRAY);
		
		//labes and texts areas and crap
		JLabel l1 = new JLabel("Paste Code Here:");
		JTextArea ta1 = new JTextArea (40, 60);
		JScrollPane sp1 = new JScrollPane(ta1);
		sp1.setPreferredSize(new Dimension(700, 600));
		
		
		
		
		//Components
		JButton convertButton = new JButton("Convert");
		
		JCheckBox blockButton = new JCheckBox("Include Block Comments");
		blockButton.setSelected(false);
		
		JCheckBox indentButton = new JCheckBox("Keep Indentaion");
		indentButton.setSelected(false);
		
		String[] languages = 
		{
		         "Java/C++/C#/GML",
		         "Python"
		};
	
		JComboBox languageBox = new JComboBox(languages);
		
		
		
		//Sets fonts
		languageBox.setFont(l1.getFont().deriveFont(17f));
		blockButton.setFont(l1.getFont().deriveFont(17f));
		indentButton.setFont(l1.getFont().deriveFont(17f));
		l1.setFont(l1.getFont().deriveFont(20f));
		convertButton.setFont(l1.getFont().deriveFont(17f));
		ta1.setFont(l1.getFont().deriveFont(15f));
		
		//sets everything in the panels
		panel1.setLayout(new BorderLayout());
	    panel1.add(languageBox,BorderLayout.WEST);
	    panel1.add(blockButton,BorderLayout.CENTER);
	    blockButton.setHorizontalAlignment(JLabel.CENTER);
	    panel1.add(indentButton,BorderLayout.EAST);
	    
	    
	    panel2.setLayout(new BorderLayout());
	    panel2.add(l1,BorderLayout.NORTH);
	    l1.setHorizontalAlignment(JLabel.CENTER);
	    panel2.add(sp1,BorderLayout.CENTER);
	    panel2.add(convertButton,BorderLayout.SOUTH);
	    
	    //primary
	    primary.setLayout(new BorderLayout());
	    primary.add(panel1,BorderLayout.NORTH);
	    primary.add(panel2,BorderLayout.SOUTH);


	    //finishes up the frame
	    frame.getContentPane().add (primary);
	    frame.pack();
	    frame.setVisible(true);
		
	    
	  //An event that runs when the user hits the convert button
	    convertButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            
	       
				//checks which language is being converted
				switch (languageBox.getSelectedItem().toString())
				{
					case "Java/C++/C#/GML":
					try {
						//runs the java method and sends it the check box info
						ta1.setText(java(ta1.getText(),blockButton.isSelected(),indentButton.isSelected()));
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					break;
					
					case "Python":
						try {
							//runs the python method and sends it the check box info
							ta1.setText(python(ta1.getText(),blockButton.isSelected(),indentButton.isSelected()));
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
					break;
					
				}
				ta1.setCaretPosition(0);
	         }

	    });
	}




public static String python(String test, boolean block, boolean indent) throws IOException {
		
	//makes and array to store each comment later
		ArrayList<String> comments = new ArrayList<String>();
		
		
		 // splits up the test string at every return, stores in an array.
		String lines[]= test.split("\n");
		
		
		boolean start = true;
		boolean stop = false;
		
		//goes through every line of code and checks if its a comment
		for (int x=0; x<lines.length; x++)
	      {
			//trims the white space and makes sure there's text there
			if (lines[x].trim().length() > 0)
			{
				//checks is the line is marked as a comment
				if (lines[x].trim().charAt(0) == '#')
				{
					//adds it to the array and trims the indents if selected
					if (!indent)
						comments.add(lines[x].trim().substring(1));
					else
						comments.add(lines[x]);
				}
				//checks for block comments
				else if (block && lines[x].trim().charAt(0) == '"' && lines[x].trim().charAt(1) == '"' && lines[x].trim().charAt(2) == '"')
				{
					//makes sure its the start of the block
					if (start)
					{
						//adds a bracket to signify a block comment
						comments.add("\n{");
						//go through all lines and adds them to the array until the block ends
						for (int y=1; x+y<lines.length ; y++)
					      {
							if (lines[x+y].trim().length() > 0)
							{
								if ((lines[x+y].trim().charAt(0) == '"' && lines[x+y].trim().charAt(1) == '"' && lines[x+y].trim().charAt(2) == '"')||stop)
								{
									stop = false;
									break;
								}
								else
								{
									if (!indent)
									{
										comments.add("\t"+lines[x+y].trim());
									}
									else
									{
										comments.add("\t"+lines[x+y]);
									}
								}
								//checks for the end of the block and stops when it finds it
								for (int z=1; z<lines[x+y].trim().length()-2; z++)
								{
									if (lines[x+y].trim().charAt(z) == '"' && lines[x+y].trim().charAt(z+1) == '"' && lines[x+y].trim().charAt(z+2) == '"')
									{
										stop = true;
									}
								}
							}
					      }
						comments.add("}\n");
						start=false;
					}
					else
					{
						start=true;
					}
				}
			}
			
	      }
		//adds all the comments to the string and returns it
		test ="";
		for (int x = 0; x < comments.size(); x++)
		{
			test+=(comments.get(x))+"\n";
		}
		return test;
	}




public static String java(String test, boolean block, boolean indent) throws IOException{
	
	//makes and array to store each comment later
		ArrayList<String> comments = new ArrayList<String>();
		
		
		// splits up the test string at every return, stores in an array.
		String lines[]= test.split("\n"); 
		
		boolean stop = false;
		
		
		//goes through every line of code and checks if its a comment
		for (int x=0; x<lines.length; x++)
	      {
			//trims the white space and makes sure there's text there
			if (lines[x].trim().length() > 0)
			{
				//checks is the line is marked as a comment
				if (lines[x].trim().charAt(0) == '/'  && lines[x].trim().charAt(1) == '/')
				{
					//adds it to the array and trims the indents if selected
					if (!indent)
						comments.add(lines[x].trim().substring(2));
					else
						comments.add(lines[x]);
				}
				//checks for block comments
				else if (block && lines[x].trim().charAt(0) == '/' && lines[x].trim().charAt(1) == '*')
				{
					
					//adds a bracket to signify a block comment
						comments.add("\n{");
						//go through all lines and adds them to the array until the block ends
						for (int y=0; x+y<lines.length ; y++)
					      {
							if ((lines[x+y].trim().length() > 0) && !(lines[x].trim().charAt(0) == '/' && lines[x].trim().charAt(1) == '*'))
							{
								
								if ((lines[x+y].trim().charAt(0) == '*' && lines[x+y].trim().charAt(1) == '/') || stop)
								{
									stop = false;
									break;
								}
								else
								{
									if (!indent)
									{
										comments.add("\t"+lines[x+y].trim());
									}
									else
									{
										comments.add("\t"+lines[x+y]);
									}
								}
								//checks for the end of the block and stops when it finds it
								for (int z=1; z<lines[x+y].trim().length()-1; z++)
								{
									if (lines[x+y].trim().charAt(z) == '*' && lines[x+y].trim().charAt(z+1) == '/')
									{
										stop = true;
									}
								}
								
							}
							else if (lines[x+y].trim().length() > 2)
							{
								if (!indent)
								{
									comments.add("\t"+lines[x+y].trim());
								}
								else
								{
									comments.add("\t"+lines[x+y]);
								}
								for (int z=1; z<lines[x+y].trim().length()-1; z++)
								{
									if (lines[x+y].trim().charAt(z) == '*' && lines[x+y].trim().charAt(z+1) == '/')
									{
										stop = true;
									}
								}
								if (stop)
								{
									stop = false;
									break;
								}
							}
							
					      }
						comments.add("}\n");
					
					
					
				}
			}
			
	      }
		//adds all the comments to the string and returns it
		test ="";
		for (int x = 0; x < comments.size(); x++)
		{
			test+=(comments.get(x))+"\n";
		}
		return test;
}
}


