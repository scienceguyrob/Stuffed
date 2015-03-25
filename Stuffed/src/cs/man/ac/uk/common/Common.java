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
 * File name: 	Common.java
 * Package: cs.man.ac.uk.common
 * Created:	May 1, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains a number of utility methods for the application.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Common
{
	//*****************************************
	//*****************************************
	//            Check OS
	//*****************************************
	//*****************************************

	/**
	 * Tests to see if the application is running on windows.
	 * @return true if the application is running under windows, else false.
	 */
	public static boolean isWindows()
	{
		String os = System.getProperty("os.name").toLowerCase();
		//windows
		return (os.indexOf( "win" ) >= 0); 
	}

	/**
	 * Tests to see if the application is running on MacIntosh.
	 * @return true if the application is running under MacIntosh, else false.
	 */
	public static boolean isMac()
	{
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf( "mac" ) >= 0); 
	}

	/**
	 * Tests to see if the application is running on Linux/Unix.
	 * @return true if the application is running under Linux/Unix, else false.
	 */
	public static boolean isUnix()
	{
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
	}

	//*****************************************
	//*****************************************
	//           Time and Date
	//*****************************************
	//*****************************************

	/**
	 * Gets the current date in the form dd/mm/yyyy.
	 * @return the current date in the form dd/mm/yyyy.
	 */
	public static String getDate()
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String year = Integer.toString(cal.get(Calendar.YEAR));

		if(day.length() == 1)
			day = "0"+day;

		if(month.length() == 1)
			month = "0"+month;

		return day+"/"+month+"/"+year;
	}

	/**
	 * Gets the current date in the form dd[separatormm]mm[separator]yyyy.
	 * @param separator the string to use to separate the date components.
	 * @return the current date in the form dd[separatormm]mm[separator]yyyy.
	 */
	public static String getDateWithSeperator(String separator)
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String year = Integer.toString(cal.get(Calendar.YEAR));

		if(day.length() == 1)
			day = "0"+day;

		if(month.length() == 1)
			month = "0"+month;

		return day+separator+month+separator+year;
	}

	/**
	 * Gets the current time in hours, minutes and seconds, condensed
	 * into a single string.
	 * @return the time condense into a string with no spaces or separation characters.
	 */
	public static String getCondensedTime()
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String hour = Integer.toString(cal.get(Calendar.HOUR));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		return hour+minute+second;
	}

	/**
	 * Gets the current time in hours, minutes and seconds, condensed
	 * into a single string separated by a specified character.
	 * @param separator the separation character
	 * @return the time condense into a string with no spaces or separation characters.
	 */
	public static String getCondensedTime(String separator)
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String hour = Integer.toString(cal.get(Calendar.HOUR));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		return hour + separator + minute + separator +second;
	}

	/**
	 * Gets the current date in the form Day Month Year,
	 * as in 31 July 2011.
	 * @return the current date in the form Day Month Year as string text.
	 */
	public static String getFancyDate()
	{
		String[] monthName = {"January", "February",
				"March", "April", "May", "June", "July",
				"August", "September", "October", "November",
				"December"
		};

		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = monthName[cal.get(Calendar.MONTH)];
		System.out.println("Month name: " + month);

		if(day.length() == 1)
			day = "0"+day;

		if(month.length() == 1)
			month = "0"+month;

		return day+" "+month+" "+year;
	}

	/**
	 * Gets the current time in the form hh:mm:ss.ms.
	 * @return the current time in the form hh:mm:ss.ms.
	 */
	public static String getTime()
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		// Get the components of the time
		String hour24 = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));     // 0..23
		String min = Integer.toString(cal.get(Calendar.MINUTE));             // 0..59
		String sec = Integer.toString(cal.get(Calendar.SECOND));             // 0..59
		String ms = Integer.toString(cal.get(Calendar.MILLISECOND));         // 0..999

		return hour24+":"+min+":"+sec+"."+ms;
	}

	//*****************************************
	//*****************************************
	//        Simple Object Methods
	//*****************************************
	//*****************************************

	/**
	 * Tests weather an object is null, and if the object is an array,
	 * checks that the array is non-empty.
	 * @param obj the empty to check.
	 * @return true if the object is null and/or empty, else false.
	 */
	public static boolean isNullOrEmpty(Object obj)
	{
		if(obj == null){ return true; }
		else if(isArray(obj))
		{
			Object[] array = (Object[]) obj;
			if(array.length  < 1)
				return true;
		}

		return false; 
	}

	/**
	 * Check if the given object is an array (primitive or native).
	 *
	 * @param obj  Object to test.
	 * @return true of the object is an array, else false.
	 */
	public static boolean isArray(final Object obj) 
	{
		if (obj != null)
			return obj.getClass().isArray();
		else
			return false;
	}

	//*****************************************
	//*****************************************
	//            File Methods
	//*****************************************
	//*****************************************

	/**
	 * gets the Canonical path of a file.
	 * @param f the file to get the Canonical path from.
	 * @return the Canonical path of the file, else null.
	 */
	public static String getFileCanonicalPath(File f)
	{
		try {  return f.getCanonicalPath(); }
		catch (IOException e){return null;}
	}

	/**
	 * Checks that the file at the specified path exists.
	 * @param path the file path to check.
	 * @return true if the file exists.
	 */
	public static boolean fileExist(String path)
	{
		if(isPathValid(path))
		{
			File f = new File(path);

			try
			{
				if(!f.getCanonicalPath().equals(path))
					return false;

				if ( f.exists() && !dirExist(path)  ){return true;}
				else{return false;}
			}
			catch(Exception e){return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a file at the specified path location.
	 * @param path the location to create the file at.
	 * @return true if the file was created, else false.
	 */
	public static boolean fileCreate(String path)
	{
		if(isPathValid(path))
		{
			try 
			{
				File file = new File(path);

				if(!file.isDirectory())
				{
					// Create file if it does not exist
					boolean success = file.createNewFile();
					if (success) {return true;}
					else {return true;}//file already exists
				}else{ return false; }//It's a directory
			} 
			catch (IOException e) {return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a file, and creates the directory structure for the file
	 * if it does not already exist.
	 * @param path the path to the file to create.
	 * @return true if the file was created successfully, else false.
	 */
	public static boolean fileCreateRecursive(String path)
	{
		if(isPathValid(path))
		{
			if(fileExist(path))
			{
				String pDir = getCanonicalPathToParent(path);

				boolean dirCreated = dirCreateRecursive(pDir);

				if(dirCreated){ return fileCreate(path); }
				else{ return false; }
			}
			else{ return true; }
		}
		else{ return false; }
	}

	/**
	 * Extracts the path to the parent directory of the specified file.
	 * @param path the path to the file whose parent directory we are to extract.
	 * @return the path to the parent directory of the specified file.
	 */
	public static String getCanonicalPathToParent(String path)
	{
		if(!Common.isNullOrEmpty(path) && Common.isPathValid(path))
		{
			if(Common.fileExist(path))
			{
				File file = new File(path);
				String fileName = file.getName();

				if(!Common.isNullOrEmpty(fileName))
				{
					// Get the path to the parent directory.
					return path.replace(File.separatorChar+fileName, "");
				}else { return null; }
			}
			else
			{
				int indexOfLastSeperator = path.lastIndexOf(File.separatorChar);
				return path.substring(0,indexOfLastSeperator);
			}
		}
		else { return null; }
	}

	/**
	 * Deletes a file at the specified path location.
	 * @param path the location to delete the file at.
	 * @return true if the file was deleted, else false.
	 */
	public static boolean fileDelete(String path)
	{
		if(isPathValid(path))
		{
			File file = new File(path);


			if(fileExist(path))
			{
				// Create file if it does not exist
				return file.delete();
			}
			else { return true; }
		}
		else
			return false;
	}

	/**
	 * Checks that the object at the specified path is a file.
	 * @param path the path to check.
	 * @return true if the object is a file.
	 */
	public static boolean isFile(String path)
	{
		if(!Common.isNullOrEmpty(path))
		{
			if(isPathValid(path))
			{
				File file = new File(path);
				if ( file .isFile() ){return true;}
				else{return false;}
			}
			else
				return false;
		}
		else { return false;}
	}

	/**
	 * Checks that the object at the specified path is a file.
	 * @param file the file to check.
	 * @return true if the object is a file.
	 */
	public static boolean isExisitingFile(File file)
	{
		if ( file .isFile() ){return true;}
		else{return false;}
	}

	/**
	 * Tests whether a path is valid.
	 * @param path the path to check.
	 * @return true if the file name is valid, else false.
	 */
	public static boolean isPathValid(String path) 
	{
		File f;

		if(path != null)
			f = new File(path);
		else
			return false;
		try 
		{
			@SuppressWarnings("unused")
			String canonicalPath = f.getCanonicalPath();

			// Check for invalid characters
			if(isWindows()){return isValidWinPath(path);}
			else if(isMac()){return isValidUnixPath(path);}
			else if(isUnix()){return isValidUnixPath(path);}
			else return true;

		}
		catch (IOException e) { return false; }
	}

	/**
	 * Tests to see if a path is valid for the Windows OS.
	 * @param path the path to test.
	 * @return true if a valid windows path, else false.
	 */
	public static boolean isValidWinPath(String path)
	{
		// If user inputs only a drive letter.
		if (path.length() == 3){ return false; }
		else if (path.endsWith("\\")){ return false; }
		else
		{
			String regex="([a-z]:\\\\(?:[-\\w\\.\\d]+\\\\)*(?:[-\\w\\.\\d]+)?)";

			Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(path);

			// Invalid path characters to explicitly check for.
			String[] invalidChars = {"\"","/","*","?","<",">","|"};

			for(int i = 0; i < invalidChars.length;i++)
			{
				if(path.contains(invalidChars[i]))
					return false;
			}

			// Check the only colon appears at start of path, i.e. C:\
			if(path.indexOf(":")>1)
				return false;

			return (m.find());
		}
	}

	/**
	 * UNTESTED.
	 * Tests to see if a path is valid for the Unix/Linux OS.
	 * @param path the path to test.
	 * @return true if a valid unix path, else false.
	 */
	public static boolean isValidUnixPath(String path)
	{
		String regex="((?:\\/[\\w\\.\\-]+)+)";

		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(path);
		return m.find();
	}

	//*****************************************
	//*****************************************
	//          Directory Methods
	//*****************************************
	//*****************************************

	/**
	 * Checks that the object at the specified path is a directory.
	 * @param path the path to check.
	 * @return true if the object is a directory.
	 */
	public static boolean isDirectory(String path)
	{
		if(isPathValid(path))
		{
			File dir = new File(path);
			if ( dir.isDirectory() ){return true;}
			else{return false;}
		}
		else
			return false;
	}

	/**
	 * Checks that the object at the specified path is a directory.
	 * @param dir the directory to check.
	 * @return true if the object is a directory.
	 */
	public static boolean isDirectory(File dir)
	{
		if ( dir.isDirectory() ){return true;}
		else{return false;}
	}

	/**
	 * Tests if a directory is empty.
	 * @param path the path to the directory to test.
	 * @return true if the directory is empty, else false.
	 */
	public static boolean isDirEmpty(String path)
	{
		if(isPathValid(path))
		{
			if(isDirectory(path) & dirExist(path))
			{
				File dir = new File(path);

				// Get list of file in the directory. If the length is zero
				// then the folder is empty.

				String[] files = dir.list();

				if (files.length == 0) 
					return true;
				else
					return false;
			}
			else { return false; }
		}
		else
			return false;
	} 

	/**
	 * Tests if a directory is empty.
	 * @param dir the directory to check.
	 * @return true if the directory is empty, else false.
	 */
	public static boolean isDirEmptyReadFile(File dir)
	{
		String[] files = dir.list();

		if (files.length == 0) 
			return true;
		else
			return false;
	} 

	/**
	 * Checks that the directory at the specified path exists.
	 * @param path the path to check.
	 * @return true if the directory exists
	 */
	public static boolean dirExist(String path)
	{
		if(isPathValid(path))
		{
			File dir = new File(path);

			try
			{
				if(!dir.getCanonicalPath().equals(path))
					return false;

				if ( dir.exists() && dir.isDirectory() ){return true;}
				else{return false;}
			}
			catch (IOException e){return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a directory at the specified path location.
	 * @param path the location to create the directory at.
	 * @return true if the directory was created, else false.
	 */
	public static boolean dirCreate(String path)
	{
		if(!dirExist(path))
		{
			if(isPathValid(path))
			{
				File dir = new File(path);

				// Create the directory, all ancestor directories must exist.
				return  dir.mkdir();
			}
			else
				return false;
		}
		else{ return true; }
	}

	/**
	 * Creates a directory at the specified path location.
	 * @param path the location to create the directory at.
	 * @return true if the directory was created, else false.
	 */
	public static boolean dirCreateRecursive(String path)
	{
		if(!dirExist(path))
		{
			if(isPathValid(path))
			{
				File dir = new File(path);

				// Create the directory, all ancestor directories must exist.
				return  dir.mkdirs();
			}
			else
				return false;
		}
		else{ return true; }
	}

	/**
	 * Deletes a directory at the specified path location,
	 * and all the files contained within it.
	 * @param path the location of the directory to delete.
	 * @return true if the directory was deleted, else false.
	 */
	public static boolean dirDelete(String path)
	{
		if(isPathValid(path))
		{
			if(dirExist(path))
			{
				File dir = new File(path);

				// Deletes all files and sub directories under the directory.
				// Returns true if all deletions were successful.
				// If a deletion fails, the method stops attempting to delete and returns false.
				if (dir.isDirectory()) 
				{
					String[] children = dir.list();

					for (int i=0; i < children.length; i++) 
					{
						boolean success = deleteDir(new File(dir, children[i]));
						if (!success) { return false; }
					}
				}

				// The directory is now empty so delete it
				return dir.delete();
			}
			else{ return true; }
		}
		else
			return false;
	}

	/**
	 * Deletes all files and sub directories under a directory.
	 * Returns true if all deletions were successful. This method is
	 * only called within the dirDelete(String path) method, and
	 * should only be called from there.
	 * 
	 * If a deletion fails, the method stops attempting to delete and returns false.
	 * @param dir the directory to delete.
	 * @return true if the directory was deleted, else false.
	 */
	private static boolean deleteDir(File dir) 
	{
		if (dir.isDirectory()) 
		{
			String[] children = dir.list();

			for (int i=0; i < children.length; i++) 
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {return false;}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Returns the size of the specified file in bytes.
	 * @param path the path to the file.
	 * @return the size of the file in bytes.
	 */
	public static long getFileSize(String path)
	{
		try
		{
			File file = new File(path);
			long fileSize = file.length();
			return fileSize;
		}
		catch (Exception e)
		{
			System.out.println("Could not obtain file size:\n"+e.toString());
			return -1;
		}
	}
	//*****************************************
	//*****************************************
	//            Search Methods
	//*****************************************
	//*****************************************

	/**
	 * Gets an array of files from a specified directory.
	 * @param path the path to the directory in which to look for files.
	 * @return an array of files if the directory is not empty, else null.
	 */
	public static File[] getFiles(String path)
	{
		if(!isDirEmpty(path))
		{
			File dir = new File(path);
			File[] files = dir.listFiles();

			// This filter only returns the files present
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file) {
					return file.isFile();
				}
			};

			files = dir.listFiles(fileFilter);
			Arrays.sort(files);
			return files;
		}
		else
			return null; // Directory is empty.
	}

	/**
	 * Gets an array of files from a specified directory.
	 * @param path the path to the directory in which to look for files.
	 * @return an array of paths if the directory is not empty, else null.
	 */
	public static String[] getFilePaths(String path)
	{
		if(!isDirEmpty(path))
		{
			File dir = new File(path);
			File[] files = dir.listFiles();

			// This filter only returns the files present
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file) {
					return file.isFile();
				}
			};

			files = dir.listFiles(fileFilter);
			Arrays.sort(files);

			String[] paths = new String[files.length];

			for(int i = 0; i < files.length;i++)
			{
				try 
				{
					paths[i] = files[i].getCanonicalPath();
				}
				catch(IOException e){}
			}

			return paths;
		}
		else
			return null; // Directory is empty.
	}

	/**
	 * Gets an array of files from a specified directory.
	 * @param path the path to the directory in which to look for files.
	 * @param f the file filter.
	 * @return an array of paths if the directory is not empty, else null.
	 */
	public static String[] getFilePaths(String path, String f)
	{
		if(!isDirEmpty(path))
		{
			File dir = new File(path);
			File[] files = dir.listFiles();

			// This filter only returns the files present
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file) {
					return file.isFile();
				}
			};

			if(files == null)
				return null;

			files = dir.listFiles(fileFilter);
			Arrays.sort(files);

			Vector<String> paths = new Vector<String>();

			for(int i = 0; i < files.length;i++)
			{
				try 
				{
					String cpath = files[i].getCanonicalPath();

					if(cpath.endsWith(f))
						paths.add(cpath);
				}
				catch(IOException e){}
			}

			String[] values = paths.toArray(new String[paths.size()]);

			return values;
		}
		else
			return null; // Directory is empty.
	}

	/**
	 * Gets an array of directories from a specified directory.
	 * @param path the path to the directory in which to look for sub directories.
	 * @return an array of directory paths if the directory is not empty, else null.
	 */
	public static File[] getDirectories(String path)
	{
		if(!isDirEmpty(path))
		{
			File dir = new File(path);
			File[] dirs = dir.listFiles();

			// This filter only returns the files present
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File d) {
					return d.isDirectory();
				}
			};

			dirs = dir.listFiles(fileFilter);
			Arrays.sort(dirs);
			return dirs;
		}
		else
			return null; // Directory is empty.
	}

	/**
	 * Generates an array of file objects, containing all files
	 * present in a specified directory, and sub directories.
	 * 
	 * @param path the to the directory to look inside.
	 * @return an array of file objects containing all files present within a directory
	 *         and its sub directories, else null is returned.
	 */
	public static File[] getFilesRecursive(File path)
	{
		Collection<File> files = listFiles(path,true);

		if(files.size()>0)
		{
			File[] arr = new File[files.size()];
			return files.toArray(arr);
		}
		else
			return null;
	}

	/**
	 * Generates an array of file objects, containing all files
	 * present in a specified directory, and sub directories.
	 * 
	 * @param path the to the directory to look inside.
	 * @return an array of file objects containing all files present within a directory
	 *         and its sub directories, else null is returned.
	 */
	public static File[] getFilesAndDirectoriesRecursive(File path)
	{
		Collection<File> files = listFilesAndDirectories(path,true);


		if(files.size()>0)
		{
			File[] arr = new File[files.size()];
			return files.toArray(arr);
		}
		else
			return null;
	}

	/**
	 * Searches a directory for files, can search recursively.
	 * @param path the path to the directory to search.
	 * @param recurse flag that when set to true means sub directories will be searched.
	 * @return a collection of file objects.
	 */
	private static Collection<File> listFiles(File path,boolean recurse)
	{
		// List of files / directories
		Vector<File> files = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = path.listFiles();

		// Go over entries
		for (File entry : entries)
		{
			if (isExisitingFile(entry)){ files.add(entry); }

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()){files.addAll(listFiles(entry, recurse));}
		}

		// Return collection of files
		return files;		
	}

	/**
	 * Searches a directory for files, can search recursively.
	 * @param path the path to the directory to search.
	 * @param recurse flag that when set to true means sub directories will be searched.
	 * @return a collection of file objects.
	 */
	private static Collection<File> listFilesAndDirectories(File path,boolean recurse)
	{
		// List of files / directories
		Vector<File> files = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = path.listFiles();

		if(!Common.isNullOrEmpty(entries))
			for (File entry : entries)
			{
				if (isExisitingFile(entry)){ files.add(entry); }
				else if(isDirectory(entry)){ files.add(entry); }
				// If the file is a directory and the recurse flag
				// is set, recurse into the directory
				if (recurse && entry.isDirectory()){files.addAll(listFiles(entry, recurse));}
			}

		// Return collection of files
		return files;		
	}

	/**
	 * Searches a directory for other directories, can search recursively.
	 * @param path the path to the directory to search.
	 * @param recurse flag that when set to true means sub directories will be searched.
	 * @return a collection of file objects.
	 */
	public static Collection<File> listDirectories(File path,boolean recurse)
	{
		// List of files / directories
		Vector<File> dirs = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = path.listFiles();

		if(!Common.isNullOrEmpty(entries))
			for (File entry : entries)
			{
				if(isDirectory(entry)){ dirs.add(entry); }
				// If the file is a directory and the recurse flag
				// is set, recurse into the directory
				if (recurse && entry.isDirectory()){dirs.addAll(listDirectories(entry, recurse));}
			}

		// Return collection of files
		return dirs;		
	}

	//*****************************************
	//*****************************************
	//         File & Directory Copy
	//*****************************************
	//*****************************************

	/**
	 * Copies a source file to a specified file path.
	 * @param sourcePath the file to copy.
	 * @param targetPath the path to copy the file to.
	 * @return true if the file was copied successfully, else false.
	 */
	public static boolean fileCopy(String sourcePath, String targetPath)
	{
		if (fileExist(sourcePath))
		{
			try
			{
				File source = new File(sourcePath);
				File target = new File(targetPath);

				// overwrite existing file.
				if(fileExist(targetPath))
					fileDelete(targetPath);

				// First try to create the target file's parent directory
				String targetParentDir = getCanonicalPathToParent(targetPath);
				boolean targetParentDirCreated = dirCreateRecursive(targetParentDir);

				if(targetParentDirCreated)
				{
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);

					// Transfer bytes from in to out
					byte[] buf = new byte[1024];
					int len;

					while ((len = in.read(buf)) >= 0) { out.write(buf, 0, len); }

					in.close();       	
					out.flush();
					out.close();

					return fileExist(targetPath);
				}
				else{ return false;}
			}
			catch (Exception e){ return false; }
		}
		else{ return false;}
	}

	/**
	 * Copies a source directory to a target location.
	 * @param sourcePath the directory to copy.
	 * @param targetPath the location to copy the directory to.
	 * @return true if the directory was created.
	 */
	public static boolean dirCopy(String sourcePath,String targetPath)
	{
		File source = new File(sourcePath);
		File target = new File(targetPath);
		dirCopy(source,target);

		return dirExist(targetPath);
	}

	/**
	 * Copies a source directory to a target location.
	 * @param sourcePath the directory to copy.
	 * @param targetPath the location to copy the directory to.
	 */
	private static void dirCopy(File sourcePath , File targetPath)
	{
		try
		{
			if (sourcePath.isDirectory()) 
			{
				if (!targetPath.exists()) { targetPath.mkdir(); }

				String[] children = sourcePath.list();

				for (int i=0; i<children.length; i++) 
				{
					dirCopy(new File(sourcePath, children[i]),new File(targetPath, children[i]));
				}
			} 
			else 
			{
				InputStream in = new FileInputStream(sourcePath);
				OutputStream out = new FileOutputStream(targetPath);

				// Copy the bits from in stream to the out stream
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) >= 0) { out.write(buf, 0, len); }
				in.close();
				out.flush();
				out.close();
			}
		}
		catch (Exception e){ }
	}
}