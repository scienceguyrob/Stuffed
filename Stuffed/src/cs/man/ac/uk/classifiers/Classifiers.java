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
 * File name: 	Classifiers.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 3, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifiers;

/**
 * Contains integer constants used to represent
 * the classifiers in this application.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/03/13
 */
public class Classifiers
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************
	
	public static final int J48 = 1;
	public static final int MLP = 2;
	public static final int NB = 3;
	public static final int SVM = 4;
	public static final int HTREE = 5;
	public static final int GHVFDT = 6;
	
	///*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************
	
	/**
	 * Returns the string name of the specified classifier.
	 * @param classifier the classifier whose name is to be returned.
	 * @return the string name of the classifier.
	 */
	public static String getClassifierName(int classifier)
	{
		switch(classifier)
		{
			case 1:
				return "J48";
			case 2:
				return "MLP";
			case 3:
				return "NaiveBayes";
			case 4:
				return "SVM";
			case 5:
				return "HTREE";
			case 6:
				return "GHVFDT";
			default:
				return "UNKNOWN";
		}
	}
}