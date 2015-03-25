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
 * File name: 	MOAClassifier.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 3, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.moawrappers;

import cs.man.ac.uk.common.Common;
import moa.streams.ArffFileStream;

/**
 * The class MOAClassifier defines the basic variables and methods needed
 * to evaluate MOA based classifiers.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/03/13
 */
public class MOAClassifier extends WekaClassifier implements I_MOAUtilities
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The training data stream constructed from an ARFF file.
	 */
	protected ArffFileStream trainingStream;

	/**
	 * The test data stream constructed from an ARFF file.
	 */
	protected ArffFileStream testStream;

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
	public MOAClassifier(String pth,boolean v){ super(pth,v);}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/**
	 * @return true if the classifier is able to begin classifying the ARFF data streams, else false.
	 */
	@Override
	public boolean ready()
	{
		if(Common.fileExist(this.trainingSetFilePath) && Common.fileExist(this.testSetFilePath))
		{
			this.trainingStream = new ArffFileStream (this.trainingSetFilePath, -1);
			this.testStream = new ArffFileStream (this.testSetFilePath, -1);

			if(this.trainingStream != null && this.testStream != null)
			{
				this.trainingStream.prepareForUse();
				this.testStream.prepareForUse();
				return true;
			}
			else 
			{
				this.log.cout("ARRF data streams are null an error must have occured during initialisation");
				return false;
			}
		}
		else 
		{
			this.log.cout("ARRF data streams cannot be intialised training and or test data set paths invalid");
			return false;
		}
	}

	/**
	 * Reloads the test and training data stream used by the classifier.
	 * 
	 * @param testSetFilePath the path to the new test data set.
	 * @param trainSetFilePath the path to the new training data set.
	 * @return true if loaded successfully, else false.
	 */
	@Override
	public boolean reloadStreams(String testSetFilePath, String trainSetFilePath)
	{
		this.setTrainingSetFilePath(trainSetFilePath);
		this.setTestSetFilePath(testSetFilePath);	

		return ready();
	}

	/**
	 * Reloads the existing training and testing data streams used by the classifier.
	 * 
	 * @return true if loaded successfully, else false.
	 */
	@Override
	public boolean reloadTrainingStreams()
	{
		this.trainingStream = null;
		this.testStream = null;		

		return ready();	
	}
}