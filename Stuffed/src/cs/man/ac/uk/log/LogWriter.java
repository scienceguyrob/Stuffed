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
 * File name: 	LogWriter.java
 * Package: cs.man.ac.uk.log
 * Created:	May 1, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.log;

import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.io.Writer;

/**
 * This class is used to write out log files.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class LogWriter
{
	//*****************************************
	//*****************************************
	//           Logging Methods
	//*****************************************
	//*****************************************

	/**
	 * Writes unformatted text information to the terminal (console).
	 * @param text the text information to log.
	 * @param log the verbose logging flag.
	 */
	public synchronized static void consoleOut(String text, boolean log)
	{ 
		if(log)
			System.out.println(text); 
	}
	
	/**
	 * Writes unformatted text information to the terminal (console).
	 * @param text the text information to log.
	 * @param log the verbose logging flag.
	 */
	public synchronized static void consoleOutFormatted(String text, boolean log)
	{ 
		if(log)
			System.out.println(debugFormat(text));
	}
	
	/**
	 * Writes formatted text information to the log file.
	 * @param path the path to write the log file to.
	 * @param text the text information to log.
	 * @param log the verbose logging flag.
	 * @return true if the logging operation was successful, else false.
	 */
	public synchronized static boolean logFormatted(String path, String text, boolean log)
	{ 
		if(log)
			return Writer.append(path , debugFormat(text));
		else
			return true;
	}
	
	/**
	 * Writes unformatted text information to the log file.
	 * @param path the path to write the log file to.
	 * @param text the text information to log.
	 * @param log the verbose logging flag.
	 * @return true if the logging operation was successful, else false.
	 */
	public synchronized static boolean logUnformatted(String path, String text, boolean log)
	{ 
		if(log)
			return Writer.append(path , text);
		else
			return true;
	}
	
	/**
	 * Writes formatted error information to the log file.
	 * @param path the path to write the log file to.
	 * @param text the text information that describes the circumstances under which the error occurred.
	 * @param e the exception that occurred.
	 * @return true if the logging operation was successful, else false.
	 */
	public synchronized static boolean errorLogFormatted(String path,String text,Object e)
	{ 
		try
		{
			System.out.println(text); 
			Exception exception = (Exception)e;
			System.out.println(exception.toString());
			exception.printStackTrace(); 
			return Writer.append(path , errorFormat(text,exception));
		}
		catch(Exception ex)
		{
			return Writer.append(path , errorFormat(text,new Exception("Unknown Error")));
		}
	}
	
	/**
	 * Writes unformatted error information to the log file.
	 * @param path the path to write the log file to.
	 * @param text the text information that describes the circumstances under which the error occurred.
	 * @param e the exception that occurred.
	 * @return true if the logging operation was successful, else false.
	 */
	public synchronized static boolean errorLogUnformatted(String path,String text,Object e)
	{ 
		try
		{
			System.out.println(text);
			Exception exception = (Exception)e;
			System.out.println(exception.toString());
			exception.printStackTrace(); 
			return Writer.append(path , text);
		}
		catch(Exception ex)
		{
			return Writer.append(path , errorFormat(text,new Exception("Unknown Error")));
		}
	}

	//*****************************************
	//*****************************************
	//      Log message formatting
	//*****************************************
	//*****************************************

	/**
	 * Formats a debug message into a suitable log format.
	 * @param text the output type.
	 * @return the formatted string.
	 */
	private static String debugFormat(String text)
	{
		String DATE = Common.getDate();
		String TIME = Common.getTime();

		//Get Tick count
		long TICKS = System.currentTimeMillis();

		String MESSAGE =  "INFO,"+DATE + "," + TIME + "," + TICKS + "," + text + ",\n";

		//Return String Formatted
		return MESSAGE;
	}

	/**
	 * Formats an error message into a suitable error log format.
	 * @param text the output type.
	 * @param e the exception that.
	 * @return the formatted string describing how the error occurred and of course the error itself..
	 */
	private static String errorFormat(String text,Exception e)
	{
		String DATE = Common.getDate();
		String TIME = Common.getTime();

		//Get Tick count
		long TICKS = System.currentTimeMillis();

		//Form a string describing the exception
		String EXCEPTION = e.toString();

		String MESSAGE =  "Error," + DATE + "," + TIME + "," + TICKS + "," + text + ","+ EXCEPTION + ",\n";

		//Return String Formatted
		return MESSAGE;
	}
}
