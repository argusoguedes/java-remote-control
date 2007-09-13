package org.one.stone.soup.screen.recorder.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.one.stone.soup.file.FileHelper;
import org.one.stone.soup.screen.recorder.DesktopScreenRecorder;
import org.one.stone.soup.screen.recorder.ScreenRecorder;
import org.one.stone.soup.screen.recorder.ScreenRecorderListener;
import org.one.stone.soup.swing.JRootFrame;

public class JRecorder extends JRootFrame implements ScreenRecorderListener,ActionListener{

	private ScreenRecorder recorder;
	private File temp;
	
	private JButton control;
	private JLabel text;
	private int frameCount;
	
	public JRecorder()
	{
		super("Screen Player",new String[]{});
		
		control = new JButton("Start Recording");
		control.setActionCommand("start");
		control.addActionListener(this);
		this.getContentPane().add( control,BorderLayout.NORTH );
		
		text = new JLabel("Ready to record");
		this.getContentPane().add( text,BorderLayout.SOUTH );
		
		this.pack();
		this.show();
	}

	public void actionPerformed(ActionEvent ev)
	{
		if(ev.getActionCommand().equals("start") && recorder==null)
		{
			try{
				temp = File.createTempFile("temp","rec");
				FileOutputStream oStream = new FileOutputStream(temp);
			
				recorder = new DesktopScreenRecorder(oStream,this);
				recorder.startRecording();
				
				control.setActionCommand("stop");
				control.setText("Stop Recording");
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		else if(ev.getActionCommand().equals("stop") && recorder!=null)
		{
			text.setText("Stopping");
			recorder.stopRecording();
		}
	}

	public void frameRecorded(boolean fullFrame)
	{
		frameCount++;
		text.setText("Frame: "+frameCount);		
	}

	public void recordingStopped()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(this);
		File target = chooser.getSelectedFile();
		if(target!=null)
		{
			FileHelper.copy(temp,target);
		}
		recorder = null;
		control.setActionCommand("start");
		control.setText("Start Recording");
		
		text.setText("Ready to record");
	}

	/* (non-Javadoc)
	 * @see wet.wired.swing.JRootFrame#destroy()
	 */
	public boolean destroy(Object source) {
		return true;
	}

	public static void main(String[] args)
	{
		JRecorder recorder = new JRecorder();
	}
}