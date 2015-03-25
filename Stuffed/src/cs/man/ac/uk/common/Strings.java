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
 * File name: 	Strings.java
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods used to manipulate strings.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Strings
{
	//*****************************************
	//*****************************************
	//               Methods
	//*****************************************
	//*****************************************
	/**
	 * Checks if multiple keywords occur in a string.
	 *
	 * @param source the string to look at for keywords.
	 * @param keywords the words to look for in the source string.
	 * @return true if all the keywords are in the string.
	 */
	public static boolean areStringsInSource(String source, String[] keywords)
	{
		if(!Common.isNullOrEmpty(source))
		{
			if(!Common.isNullOrEmpty(keywords))
			{
				for(String s : keywords)
				{
					String source_lower = source.toLowerCase();
					String s_lower = s.toLowerCase();

					if (!source_lower.contains(s_lower)){ return false; }
				}

				return true;
			}
			else{ return false; }
		}
		else{ return false; }
	}
	
	/**
	 * Method that separates a string into sub-strings, broken up where each
	 * specified character appears.
	 * @param source the string to break up.
	 * @param sepChar the character used to breakup the string.
	 * @return the broken up string as an array, otherwise null.
	 */
	public static String[] seperateString(String source, String sepChar)
	{
		if (!isNullOrEmptyString(source) && !source.equals("") && !sepChar.equals(""))
		{
			try
			{
				String[] subStrings = source.split(sepChar);

				if(subStrings.length < 1){ return null; }
				else if(subStrings.length == 1)
				{
					// if the array has only one element, it may not have been
					// possible to split the string, in which case the call to 
					// split will return an array containing the source string.
					// As we don't want the source string to be returned in
					// the array, we explicitly return null.
					if(subStrings[0].equals(source))
						return null;
				}		

				return subStrings;
			}
			catch (Exception e){ return null; }
		}
		else{ return null; }
	}

	/**
	 * Method that separates a string into sub-strings, broken up where each
	 * specified character appears. This strings are then parsed into double values
	 * and returned as a double array.
	 * @param source the string to break up.
	 * @param sepChar the character used to breakup the string.
	 * @return the broken up string as an array, otherwise null.
	 */
	public static double[] splitStringToDouble(String source, String sepChar)
	{
		String sep = sepChar;

		if(sepChar.equals("|"))
			sep = "\\|";

		if (!isNullOrEmptyString(source) && !source.equals("") && !sepChar.equals(""))
		{
			try
			{
				// If the source string contains no separators, try
				// to convert the string to a double.
				if(!source.contains(sepChar))
					return new double[]{Double.parseDouble(source)};

				// Else the string does contain separators, split it.
				String[] subStrings = source.split(sep);

				if(subStrings.length < 1){ return null; }
				else if(subStrings.length == 1)
				{
					// if the array has only one element, it may not have been
					// possible to split the string, in which case the call to 
					// split will return an array containing the source string.
					// As we don't want the source string to be returned in
					// the array, we explicitly return null.
					if(subStrings[0].equals(source))
						return null;
				}		

				double[] values = new double[subStrings.length];

				for(int i = 0; i < values.length;i++)
					values[i] = Double.parseDouble(subStrings[i]);

				return values;
			}
			catch (Exception e){ return null; }
		}
		else{ return null; }
	}

	/**
	 * Method that checks that a string actually contains a full integer.
	 * @param source the string to check.
	 * @return true if the string is representing a valid integer value.
	 */
	public static boolean doesStringContainInt(String source)
	{
		if(!isNullOrEmptyString(source))
		{
			String regex="([-+]?\\d+)";	// Integer Number 1

			Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(source);

			if (m.find())
			{
				try
				{
					Integer.parseInt(source);
					return true;
				}
				catch (NumberFormatException nfe){  return false; }
			}
			else {return false;}
		}
		else { return false; }
	}

	/**
	 * Method that checks if a string is a string representation of a double or a float.
	 * @param source the string to check.
	 * @return true if the string is representing a float or double value.
	 */
	public static boolean doesStringContainDoubleOrFloat(String source)
	{
		if(!isNullOrEmptyString(source))
		{
			String regex="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Integer Number 1

			Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(source);

			if (m.find())
			{
				try
				{
					Double.parseDouble(source);
					return true;
				}
				catch (NumberFormatException nfe){  return false; }
			}
			else {return false;}
		}
		else { return false; }
	}

	/**
	 * Extracts the string data between a specific character, i.e. ':extract this:'.
	 * @param source the string to try and extract from.
	 * @param separator the separator character used to split up the string.
	 * @return the first string occurring between the first and last index of
	 *         the separation character, else null.
	 */
	public static String extractBetweenSeperator(String source, String separator)
	{
		if(!isNullOrEmptyString(source) | !isNullOrEmptyString(separator))
		{
			@SuppressWarnings("unused")
			String result = null;

			try
			{
				int firstIndexOfSeparator = source.indexOf(separator) + 1;
				int secondIndexOfSeparator = source.lastIndexOf(separator);
				return result = source.substring(firstIndexOfSeparator, secondIndexOfSeparator);
			}
			catch (Exception e) { return null; }
		}else {return null;}
	}

	/**
	 * Gets the name of a file from a path.
	 * @param path the full path to the file.
	 * @return the name of the file with its extension.
	 */
	public static String getFileNameFromPath(String path)
	{
		if(!isNullOrEmptyString(path) && Common.isPathValid(path))
		{
			File file = new File(path);
			String fileName = file.getName();

			if(!isNullOrEmptyString(fileName))
			{
				// Get the path to the parent directory.
				return fileName;
			}else { return path; }
		}
		else { return path; }
	}

	/**
	 * Checks if a string contains a file extension, but only if the extension
	 * exists at the end of the file name, i.e. "file.txt" whereas "file.file.txt"
	 * would be deemed invalid.
	 * @param s the string to check.
	 * @return true if the string contains a single file extension.
	 */
	public static boolean containsFileExtension(String s)
	{
		if(!isNullOrEmptyString(s))
		{
			if(s.contains("."))
			{
				// dot symbol at the start of string.
				if(s.indexOf(".") == 0)
					return false;

				// count occurrences of dot character
				// if greater than one return false.
				int count = 0;
				for (int i=0; i < s.length(); i++)
				{
					if (s.charAt(i) == '.')
						count++;

					if(count>1)
						return false;
				}

				// dot symbol at the end of string.
				if(s.lastIndexOf(".") == s.length()-1)
					return false;

				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * Trims any unwanted text out of command line arguments.
	 * 
	 * @param arg The argument.
	 * @param excess the string to remove from the argument.
	 *@return The updated argument.
	 */
	public static String trimArgument(String arg, String excess)
	{
		return arg.replace(excess.toUpperCase(), "").replace(excess.toLowerCase(), "");
	}

	/**
	 * Checks if any of the supplied strings are empty
	 * @param strings the strings to check.
	 * @return true if at least one string is empty, else false.
	 */
	public static boolean isAStringsEmpty(String ...strings )
	{
		for (String s : strings ) 
		{
			if(isEmptyString(s))
				return true;
		}

		return false;
	}

	/**
	 * Matches a string against the ISO-8601 combined date and time format: "yyyy-MM-dd'T'HH:mm:ss".
	 * @param txt the string to match.
	 * @return true if the string matches, else false.
	 */
	public static boolean isStringISO8601Time(String txt)
	{
		String re1="((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])";	// YYYYMMDD 1
		String re2="(\\'.*?\\')";	// Single Quote String 1
		String re3="((?:(?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):(?:[0-5][0-9])(?::[0-5][0-9])?(?:\\s?(?:am|AM|pm|PM))?)";	// HourMinuteSec 1
		Pattern p = Pattern.compile(re1+re2+re3,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(txt);

		if (m.find())
			return true;
		else
			return false;
	}

	/**
	 * Checks a string for occurrences of letters in the alphabet.
	 * 
	 * @param source the string to check.
	 * @return true if the string contains letters.
	 */
	public static boolean doesStringContainLetters(String source)
	{
		String[] letters = { "a","b","c","d","e","f","g","h","i","j","k","l","m",
				"n","o","p","q","r","s","t","u","v","w","x","y","z"};

		String src = source.toLowerCase();

		for(String l : letters)
		{
			if (src.contains(l))
				return true;
		}

		return false;
	}

	/**
	 * Test if a string is null or empty.
	 * @param s the string to check.
	 * @return true if null or empty, else false.
	 */
	public static boolean isNullOrEmptyString(String s)
	{
		if(s == null)
			return true;
		else if(s.equals(""))
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if a string is empty.
	 * @param s the string to check.
	 * @return true if the string is empty, else false.
	 */
	public static boolean isEmptyString(String s)
	{
		if(s.equals(""))
			return true;
		else 
			return false;
	}
}
