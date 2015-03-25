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
 * File name: 	Reader.java
 * Package: cs.man.ac.uk.io
 * Created:	May 1, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cs.man.ac.uk.common.Common;

/**
 * This class is used to read files in different ways.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Reader
{
	//*****************************************
	//*****************************************
	//                Methods
	//*****************************************
	//*****************************************

	/**
	 * Gets the full string contents of a file and returns them.
	 * 
	 * Returns null if there is an IOException, or if the file is empty.
	 * 
	 * @param path The file to extract the contents from.
	 * @return The contents of the file as a string, or null if the file is empty.
	 */
	public static String getContents(String path)
	{
		//Firstly try to create the file
		File file = new File(path);

		//if the file exists
		if(file.exists())
		{
			String line = "";
			StringBuilder builder = new StringBuilder();

			int counter = 0;

			// Read the file and display it line by line. 
			BufferedReader in = null;

			try
			{
				//open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					while ((line = in.readLine()) != null)
					{
						if (counter != 0)//if we are not on the first line
						{
							builder.append("\r"+line);
						}
						else//we are on the first line
						{
							//no need for new line character
							builder.append(line);
							counter++;
						}
					}
				}
				catch(IOException e){return null;}
				finally{in.close();}

				if(counter == 0){ return null; }
				else{ return builder.toString();}
			}
			catch (FileNotFoundException e) { return null; }
			catch (IOException e) { return null; }
		}
		else{ return null; }
	}

	/**
	 * Tests to see if a file is empty.
	 * @param path the path to the file to check.
	 * @return true if file is empty, else false.
	 */
	public static boolean isEmpty(String path)
	{
		if(Common.isFile(path))
		{
			try
			{
				FileInputStream stream = new FileInputStream(new File(path));  
				int b = stream.read(); 

				if (b == -1)  
				{  
					stream.close();
					return true;
				}

				stream.close();
				return false;
			}
			catch(IOException e){return false;}
		}
		else{return true;}
	}
}