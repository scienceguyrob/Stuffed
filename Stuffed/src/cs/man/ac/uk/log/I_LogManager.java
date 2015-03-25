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
 * File name: 	I_LogManager.java
 * Package: cs.man.ac.uk.log
 * Created:	May 1, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.log;

public interface I_LogManager
{
	/**
	 * Enables verbose logging.
	 */
	public abstract void enableLogging();

	/**
	 * Disables verbose logging.
	 */
	public abstract void disableLogging();

	/**
	 * @return true if logging is enabled, else false.
	 */
	public abstract boolean isLoggingEnabled();

	
	public abstract void cout(String message);  // Console out.
	public abstract void coutf(String message); // COnsole out, formatted.
	
	public abstract void logf(String text); // log to a file, formatted.
	public abstract void loguf(String text);// log to a file, unformatted.
	
	public abstract void erroruf(String message,Object error);// process error message, no formatting.
	public abstract void errorf(String message,Object error); // process error message, with formatting.

}