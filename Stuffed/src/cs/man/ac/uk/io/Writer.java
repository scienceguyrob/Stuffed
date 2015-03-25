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
 * File name: 	Writer.java
 * Package: cs.man.ac.uk.io
 * Created:	May 1, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import cs.man.ac.uk.common.Common;

/**
 * This class contains methods used for writing to files.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Writer
{
	//*****************************************
	//*****************************************
	//              Write to file
	//*****************************************
	//*****************************************

	/**
	 * Writes a string literal to a specified file. The literal is
	 * appended to the end of the file in question.
	 * @param path the path to the file to which the string literal will be appended.
	 * @param text the string text to append to the file.
	 * @return true if the write operation was successful, else false.
	 */
	public static boolean write(String path, String text)
	{
		if(!Common.fileExist(path))//Check file exists
			Common.fileCreate(path);//if not create it

		if(Common.fileExist(path))//make sure the file we created exists
		{
			try 
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(path));//Non-append
				out.write(text);
				out.close();
				return true;
			} 
			catch (IOException e) {return false;}
		}
		else{ return false; }
	}

	/**
	 * Writes a string literal to a specified file. The literal is
	 * appended to the end of the file in question.
	 * @param path the path to the file to which the string literal will be appended.
	 * @param text the string text to append to the file.
	 * @return true if the write operation was successful, else false.
	 */
	public static boolean append(String path, String text)
	{
		if(!Common.fileExist(path))//Check file exists
			Common.fileCreate(path);//if not create it

		if(Common.fileExist(path))//make sure the file we created exists
		{
			try 
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(path, true));//append
				out.write(text);
				out.close();
				return true;
			} 
			catch (IOException e) {return false;}
		}
		else{ return false; }
	}
	
	//*****************************************
	//*****************************************
	//              Clear File
	//*****************************************
	//*****************************************

	/**
	 * Clears a file of data.
	 * @param path the path to the text file to clear.
	 * @return true if the file was cleared successfully.
	 */
	public static boolean clear(String path)
	{	
		if (Common.fileExist(path))
		{
			boolean deleted = Common.fileDelete(path);

			if(deleted)
				return Common.fileCreate(path);
			else 
				return false;
		}
		else{ return false; }
	}
}
