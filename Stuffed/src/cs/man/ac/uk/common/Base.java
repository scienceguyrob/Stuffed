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
 * File name: 	Base.java
 * Package: cs.man.ac.uk.common
 * Created:	Apr 9, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.common;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

import cs.man.ac.uk.log.LogManager;

/**
 * The class Base is simply a base class in the object hierarchy.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 04/09/14
 */
public class Base
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The logger used to write out debug information.
	 */
	public LogManager log;

	/**
	 * The name of the class.
	 */
	public String name = "";
	
	/**
     * The unique identifier of this class.
     */
    public String ID = "";

	//*****************************************
	//*****************************************
	//             	 Base
	//*****************************************
	//*****************************************

	/**
	 * Default constructor that initialises this class with a logger
	 * set not to log in verbose mode.
	 * @param pth the log file path.
	 * @param l the boolean logging flag.
	 */
	public Base(String pth,boolean l)
	{ 
		log = new LogManager(pth,l);
		ID = UUID.randomUUID().toString();
		name = UUID.randomUUID().toString();
	}

	//*****************************************
	//*****************************************
	//               Methods
	//*****************************************
	//*****************************************
	
	/**
	 * Writes an object to an XML file.
	 * @param obj the object to write to XML.
	 * @param path the path to the file the object should be written to.
	 * @return true if the object is written to XML correctly, else false.
	 */
	public static boolean write(Object obj, String path)
	{
		try
		{
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream( new FileOutputStream(path)));
			encoder.writeObject(obj);
			encoder.close();
			return true;
		}
		catch(Exception e){System.out.println(e.toString());return false;}
	}

	/**
	 * Reads the state of an object in from XML.
	 * @param path the path to the XML containing the state of the object.
	 * @return the Object if successful, else null.
	 */
	public static Object read(String path) 
	{
		try
		{
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(path)));
			Object o = (Object)decoder.readObject();
			decoder.close();
			return o;
		}
		catch(Exception e){System.out.println(e.toString());return null;}
	}
}
