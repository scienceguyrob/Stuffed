/**
 *
 * This file is part of Stuffed.
 *
 * Stuffed is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stuffed is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Stuffed.  If not, see <http://www.gnu.org/licenses/>.
 *
 * File name: 	LogManager.java
 * Package: cs.man.ac.uk.log
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.log;

import java.util.UUID;

import cs.man.ac.uk.common.Common;

/**
 * This class is used to provide a wrapper for logging utilities. It is
 * used to toggle verbose logging statements on and off in this application,
 * through the use of a command line argument passed to this class's 
 * constructor. A single copy of this class is initialised in the applications
 * main method, and this instance is then used throughout the program.
 * 
 * This is used instead of Java's own logging classes for the sake of simplicity.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class LogManager implements I_LogManager
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The boolean flag used to toggle logging on and off. If true 
	 * verbose logging statements will be written to the
	 * log file. If false only error statements will be written to 
	 * the log file.
	 */
	private boolean log = true;
	
	/**
	 * Full path to the log file to write message to.
	 */
	private String path = "";

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Primary constructor that builds a new logger instance. If
	 * the boolean flag passed to this constructor is true,
	 * verbose logging will be enabled. If the flag is false, 
	 * verbose logging will be disabled, however error logging
	 * will continue.
	 * @param pth the path to the log file.
	 * @param enableLogging the verbose logging flag.
	 */
	public LogManager(String pth, boolean enableLogging) 
	{ 
		log = enableLogging;
		
		if(Common.isPathValid(pth))
			this.path = pth;
		else
		{
			// Generate unique file path automatically.
			this.path = UUID.randomUUID().toString()+"_LOG.txt";
		}
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/**
	 * Enables verbose logging.
	 */
	public void enableLogging(){ log = true;}

	/**
	 * Disables verbose logging.
	 */
	public void disableLogging(){ log = false; }

	/**
	 * @return true if logging is enabled, else false.
	 */
	public boolean isLoggingEnabled(){ return this.log;}
	
	/**
	 * Sets the log file used if its valid.
	 * @param pth the new log file path.
	 */
	public void setPath(String pth) 
	{ 
		if(Common.isPathValid(pth))
			this.path = pth;
	}
	
	/**
	 * Outputs an unformatted logging message to the terminal/console.
	 * @param message the message to write out.
	 */
	public void cout(String message)
	{
		LogWriter.consoleOut(message,this.log);
	}
	
	/**
	 * Outputs an unformatted logging message to the terminal/console.
	 * @param message the message to write out.
	 * @param indents the number of tab indents to use preceding the text.
	 */
	public void cout(String message, int indents)
	{
		String indent = "";
		
		for(int i=0; i< indents;i++)
			indent+= "\t";
		
		LogWriter.consoleOut(indent + message,this.log);
	}
	
	/**
	 * Outputs an unformatted logging message to the terminal/console, and the log file.
	 * @param message the message to write out.
	 * @param indents the number of tab indents to use preceding the text.
	 */
	public void dualOut(String message, int indents)
	{
		String indent = "";
		
		for(int i=0; i< indents;i++)
			indent+= "\t";
		
		LogWriter.consoleOut(indent + message,this.log);
		LogWriter.logUnformatted(this.path,indent + message,this.log);
	}
	
	/**
	 * Outputs an formatted logging message to the terminal/console.
	 * @param message the message to write out.
	 */
	public void coutf(String message)
	{
		LogWriter.consoleOut(message,this.log);
	}

	/**
	 * Writes formatted text information to the log file.
	 * @param text the text information to log.
	 */
	public void logf(String text)
	{ 
		LogWriter.logFormatted(this.path,text,this.log);
	}
	
	/**
	 * Writes unformatted text information to the log file.
	 * @param text the text information to log.
	 */
	public void loguf(String text)
	{ 
		LogWriter.logUnformatted(this.path,text,this.log);
	}
	
	/**
	 * Outputs an error message to the error log file.
	 * @param message a message describing where and how the error has occurred.
	 * @param error the exception object.
	 */
	public void errorf(String message,Object error) 
	{ 
		LogWriter.errorLogFormatted(this.path, message, error); 
	}
	
	/**
	 * Outputs an error message to the error log file.
	 * @param message a message describing where and how the error has occurred.
	 * @param error the exception object.
	 */
	public void erroruf(String message,Object error) 
	{ 
		LogWriter.errorLogUnformatted(this.path, message,error); 
	}
}
