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
 * File name: 	BaseClassifier.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import cs.man.ac.uk.common.Base;

/**
 * Class containing helper methods for a classifier. These are primarily
 * methods that make it easier to debug classifier output, or get its
 * performance statistics.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class BaseClassifier extends Base
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param pth the log file path.
	 * @param v boolean logging flag.
	 */
	public BaseClassifier(String pth,boolean v){ super(pth,v);}

	//*****************************************
	//*****************************************
	//             Methods
	//*****************************************
	//*****************************************

	/**
	 * <p>Obtains labelled input patterns from the file specified, along with their
	 * positions in the corresponding test data set. Each line of the file should have the
	 * following format:</p>
	 * 
	 * [index position],[unique identifier],[attribute 1],...,[attribute N],[label]
	 * 
	 * <p>Such that,</p>
	 * 
	 * <ul>
	 * <li>index position    = the position of the instance in the test data set. </li>
	 * <li>unique identifier = a string that unique identifies the instance if available.</li>
	 * <li>label             = the class label.</li>
	 * </ul>
	 * 
	 * <p>For example, given an unlabelled two-class test data set as follows:</p>
	 * 
	 * 1 , 2 , 3 , ?
	 * 4 , 5 , 6 , ?
	 * 7 , 8 , 9 , ?
	 * 1 , 5 , 9 , ?
	 * 
	 * <p>Then the positive labelled data files would appear as follows:</p>
	 * 
	 *  1 , Example 1 , 1 , 2 , 3 , +
	 *  3 , Example 3 , 7 , 8 , 9 , +
	 *  
	 * <p>Whilst the negative file would be:</p>
	 * 
	 * 2 , Example 2 , 4 , 4 , 6 , -
	 * 4 , Example 4 , 1 , 5 , 9 , -
	 * 
	 * <p>This information is read in by this method, and stored in a hash map for
	 * quick access. The hash map uses an integer key and string value. Each instance
	 * is stored in the hash map using its index position as its key, and its
	 * unique identifier plus attributes as the value. For example, the positive
	 * examples from the file discussed above:</p>
	 * 
	 * 1 , Example 1 , 1 , 2 , 3 , +
	 * 3 , Example 3 , 7 , 8 , 9 , +
	 * 
	 * <p>would be stored as</p>
	 * 
	 * KEY VALUE
	 * [1],[Example 1 , 1 , 2 , 3 , +]
	 * [3],[Example 3 , 7 , 8 , 9 , +]
	 * 
	 * <p>This means that if we know the index of an instance being classified,
	 * we can check for its presence in the hash map and evaluate the predicted
	 * class immediately.</p>
	 * 
	 * <p>The reason for this approach may not be clear. However if a data set is
	 * extremely large, and you need to keep track of the false negatives or false 
	 * positives, then this type of Meta information is needed. Otherwise it would
	 * not be possible to evaluate performance on unlabelled data.</p>
	 * 
	 * @param labelledDataPath the path to the file containing positives.
	 * @return a hash map containing the labelled patterns, such that the hash 
	 *         key of each instance, is the index of its position in the test
	 *         data set (please see above explanation).
	 */
	public TreeMap<Integer,String> getMetaData(String labelledDataPath)
	{
		// Stores the labelled patterns
		TreeMap<Integer,String> patterns = new TreeMap<Integer,String>();

		//Firstly try to create the file
		File file = new File(labelledDataPath);

		//if the file exists
		if(file.exists())
		{
			String line = ""; 
			BufferedReader in = null;

			try
			{
				in = new BufferedReader(new FileReader(file));

				try
				{   
					while ((line = in.readLine()) != null)
					{
						// Extract the key first.
						int key = Integer.parseInt(line.substring(0,line.indexOf(",")));
						int indexOfFirstComma = line.indexOf(",");
						patterns.put(key,line.substring(indexOfFirstComma+1,line.length()));
					}

					return patterns;
				}
				catch(IOException e){return patterns;}
				finally{in.close();}
			}
			catch (Exception e) { return patterns; }
		}
		else{ return patterns; }
	}

	/**
	 * <p>Obtains labelled input patterns from the file specified, along with their
	 * positions in the corresponding test data set. Each line of the file should have the
	 * following format:</p>
	 * 
	 * [index position],[unique identifier],[attribute 1],...,[attribute N],[label]
	 * 
	 * <p>Such that,</p>
	 * 
	 * <ul>
	 * <li>index position    = the position of the instance in the test data set. </li>
	 * <li>unique identifier = a string that unique identifies the instance if available.</li>
	 * <li>label             = the class label.</li>
	 * </ul>
	 * 
	 * <p>For example, given an unlabelled two-class test data set as follows:</p>
	 * 
	 * 1 , 2 , 3 , ?
	 * 4 , 5 , 6 , ?
	 * 7 , 8 , 9 , ?
	 * 1 , 5 , 9 , ?
	 * 
	 * <p>Then the positive labelled data files would appear as follows:</p>
	 * 
	 *  1 , Example 1 , 1 , 2 , 3 , +
	 *  3 , Example 3 , 7 , 8 , 9 , +
	 *  
	 * <p>Whilst the negative file would be:</p>
	 * 
	 * 2 , Example 2 , 4 , 4 , 6 , -
	 * 4 , Example 4 , 1 , 5 , 9 , -
	 * 
	 * <p>This information is read in by this method, and stored in a hash map for
	 * quick access. The hash map uses an integer key and string value. Each instance
	 * is stored in the hash map using its index position as its key, and its
	 * unique identifier plus attributes as the value. For example, the positive
	 * examples from the file discussed above:</p>
	 * 
	 * 1 , Example 1 , 1 , 2 , 3 , +
	 * 3 , Example 3 , 7 , 8 , 9 , +
	 * 
	 * <p>would be stored as</p>
	 * 
	 * KEY VALUE
	 * [1],[Example 1 , 1 , 2 , 3 , +]
	 * [3],[Example 3 , 7 , 8 , 9 , +]
	 * 
	 * <p>This means that if we know the index of an instance being classified,
	 * we can check for its presence in the hash map and evaluate the predicted
	 * class immediately.</p>
	 * 
	 * <p>The reason for this approach may not be clear. However if a data set is
	 * extremely large, and you need to keep track of the false negatives or false 
	 * positives, then this type of Meta information is needed. Otherwise it would
	 * not be possible to evaluate performance on unlabelled data.</p>
	 * 
	 * @param truePosDataPath the path to the file containing positives.
	 * @return a hash map containing the labelled patterns, such that the hash 
	 *         key of each instance, is the index of its position in the test
	 *         data set (please see above explanation).
	 */
	public TreeMap<Integer,String> getPositiveMetaData(String truePosDataPath)
	{
		// Stores the labelled patterns
		TreeMap<Integer,String> patterns = new TreeMap<Integer,String>();

		//Firstly try to create the file
		File file = new File(truePosDataPath);

		//if the file exists
		if(file.exists())
		{
			String line = ""; 
			BufferedReader in = null;
			int count =0;
			try
			{
				in = new BufferedReader(new FileReader(file));

				try
				{   
					while ((line = in.readLine()) != null)
					{
						count++;
						int key = Integer.parseInt(line.replace("\n", ""));
						patterns.put(key,Integer.toString(count));
					}
					return patterns;
				}
				catch(IOException e){return patterns;}
				finally{in.close();}
			}
			catch (Exception e) { return patterns; }
		}
		else{ return patterns; }
	}
}
