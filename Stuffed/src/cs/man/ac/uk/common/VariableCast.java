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
 * File name: 	VariableCast.java
 * Package: cs.man.ac.uk.common
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * The class VariableCast is used to convert between various object
 * types, i.e. array list to primitive array.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class VariableCast
{
	//*****************************************
	//*****************************************
	//        ArrayList Methods
	//*****************************************
	//*****************************************

	/**
	 * Method that converts an array list of Strings to a String array.
	 *
	 * @param attributes An ArrayList instance.
	 * @return the converted String array.
	 */
	public static String[] convertStringListToArray(Vector<String> attributes)
	{
		String[] ret = new String[attributes.size()];
		Iterator<String> iterator = attributes.iterator();

		for (int i = 0; i < ret.length; i++)
			ret[i] = iterator.next().toString();

		return ret;
	}

	/**
	 * Method that converts an array list of Integers to an int array.
	 *
	 * @param list An ArrayList instance.
	 * @return the converted int array.
	 */
	public static int[] convertArrayListToIntArray(ArrayList<Integer> list)
	{
		int[] ret = new int[list.size()];
		Iterator<Integer> iterator = list.iterator();

		for (int i = 0; i < ret.length; i++)
			ret[i] = iterator.next().intValue();

		return ret;
	}

	/**
	 * Method that converts an array list of strings to an integer array.
	 *
	 * @param list an ArrayList instance.
	 * @return the converted string array.
	 */
	public static int[] convertStringListToIntArray(List<String> list)
	{
		int[] ret = new int[list.size()];
		Iterator<String> iterator = list.iterator();

		for (int i = 0; i < ret.length; i++)
			ret[i] = Integer.parseInt(iterator.next());

		return ret;
	}

	/**
	 * Method that converts an array list of strings to a float array array.
	 *
	 * @param list an ArrayList instance.
	 * @return the converted string array.
	 */
	public static float[] convertStringListToFloatArray(List<String> list)
	{
		float[] ret = new float[list.size()];
		Iterator<String> iterator = list.iterator();

		for (int i = 0; i < ret.length; i++)
			ret[i] = Float.parseFloat(iterator.next());

		return ret;
	}

	/**
	 * Method that converts an array list of strings to a double array.
	 *
	 * @param list an ArrayList instance.
	 * @return the converted string array.
	 */
	public static double[] convertStringListToDoubleArray(List<String> list)
	{
		double[] ret = new double[list.size()];
		Iterator<String> iterator = list.iterator();

		for (int i = 0; i < ret.length; i++)
			ret[i] = Double.parseDouble(iterator.next());

		return ret;
	}

	/**
	 * Returns an Integer array list as a primitive integer array.
	 * @param list the array list to convert.
	 * @return the list as a primitive integer array.
	 */
	public static int[] toIntArray(ArrayList<Integer> list)  
	{
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)  
			ret[i++] = e.intValue();

		return ret;
	}

	/**
	 * Converts an array of type int[] to Integer[].
	 * @param array the array to convert.
	 * @return the Integer[] representation of the int[] array, or an empty array
	 *         if the int[] is equal to null or empty.
	 */
	public static Integer[] intToIntegerArray(int[] array)
	{
		Integer[] result = {};

		if(array != null)
		{
			if(array.length > 0)
			{
				result = new Integer[array.length];
				for(int i = 0; i < array.length;i++)
					result[i] = (Integer)array[i];

				return result;
			}else { return result; }
		}else { return result; }
	}

	/**
	 * Converts a string to an integer. Returns Integer.MIN_VALUE if an error occurs.
	 * @param s the string to convert.
	 * @return the integer stored in the string.
	 */
	public static int toInteger(String s)
	{	
		if(Strings.doesStringContainInt(s))
		{
			try{ int result = Integer.parseInt(s); return result; }
			catch(NumberFormatException nfe){ return Integer.MIN_VALUE; }
		}
		else
			return Integer.MIN_VALUE;	    
	}
}
