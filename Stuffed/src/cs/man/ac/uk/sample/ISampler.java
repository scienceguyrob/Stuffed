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
 * File name: 	ISampler.java
 * Package: cs.man.ac.uk.interfaces
 * Created:	May 30, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

/**
 * The class ISampler defines an interface for the sampling code used by external applications.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public interface ISampler
{
	//*****************************************
	//*****************************************
	//           Getters and Setters
	//*****************************************
	//*****************************************
	
	/**
	 * @param path the absolute path to the file to write log statements to.
	 */
	public void setLogPath(String path);
	
	/**
	 * @return number of rows of data in the file to be sampled.
	 */
	public int getRows();
	
	/**
	 * @return number of columns of data in the file to be sampled.
	 */
	public int getColumns();
	
	/**
	 * @return true if the file to be sampled has existing meta data, else false.
	 */
	public boolean doesMetaDataExist();
	
	/**
	 * @return the different class labels for the data in the file being sampled, i.e.
	 * for a two class data file this would return {0,1} or {"Negative","Positive"} for
	 * example.
	 */
	public String[] getClassLabels();

	/**
	 * @return if the data is an ARFF file or a CSV file with a header, then grab the
	 * names of the attributes - i.e. strings which describe the data in each column
	 * of the file.
	 */
	public String[] getAttributeLabels();

	/**
	 * @return the count of the number of data attributes in the file being sampled,
	 * essentially this value is the column count - 1 (this is minus 1, as one of the attributes
	 * is the class label).
	 */
	public int getAttributeCount();

	/**
	 * @return the class distribution as an integer array, i.e. if there are
	 * 100 negative examples and 10 positives, then the negatives are treated
	 * as class zero and the positives as class one. So then:
	 * 
	 *         Negative   Positive
	 *           |          |
	 *           V          V
	 * return { 100     ,  10  }
	 *           |          |
	 *           V          V
	 *        Index 0    Index 1
	 */
	public int[] getClassDistribution();
	
	//*****************************************
	//*****************************************
	//            Main Methods
	//*****************************************
	//*****************************************
	
	/**
	 * Loads a particular file which is to be sampled.
	 * @param path the path to the file to be sampled.
	 * @param classIndex the column index of the class in the file being sampled.
	 * @return true if the file is loaded correctly.
	 */
	public boolean load(String path,int classIndex);
	
	/**
	 * Samples the file and writes the sampled output to training and test
	 * set files in CSV format.
	 * 
	 * @param trainSetPath the training set file to create.
	 * @param testSetPath the test set file to create.
	 * @param negTrainSamples the total number of negative examples to include in the training set.
	 * @param posTrainSamples the total number of positive examples to include in the training set.
	 * @param trainSetBalance the desired class distribution for the training set, i.e. trainSetBalance = 0.1 
	 * 						  means that 10% of the training set will be positive, the remainder will be negative. 
	 * @param testSetBalance the desired class distribution for the test set, i.e. testSetBalance = 0.01 
	 * 						  means that 1% of the training set will be positive, the remainder will be negative. 
	 * @param labelling the proportion of instances in the test set which should be labeled, i.e. labeling = 0.001
	 * 						  means that 0.1% of the test set will be labeled.
	 * @return an array containing values which describe the outcome of the sampling. These variables include:
	 * 		   i)  a boolean value which when true means the sampling was successful, else false.
	 * 		   ii) a string message describing the outcome, which may contained detailed information 
	 *             if the sampling failed for some reason.
	 */
	public Object[] sampleToCSV(String trainSetPath,String testSetPath,int negTrainSamples,int posTrainSamples,double trainSetBalance,double testSetBalance,double labelling);
	
	/**
	 * Samples the file and writes the sampled output to training and test
	 * set files in ARFF format.
	 * 
	 * @param trainSetPath the training set file to create.
	 * @param testSetPath the test set file to create.
	 * @param negTrainSamples the total number of negative examples to include in the training set.
	 * @param posTrainSamples the total number of positive examples to include in the training set.
	 * @param trainSetBalance the desired class distribution for the training set, i.e. trainSetBalance = 0.1 
	 * 						  means that 10% of the training set will be positive, the remainder will be negative. 
	 * @param testSetBalance the desired class distribution for the test set, i.e. testSetBalance = 0.01 
	 * 						  means that 1% of the training set will be positive, the remainder will be negative. 
	 * @param labelling the proportion of instances in the test set which should be labeled, i.e. labeling = 0.001
	 * 						  means that 0.1% of the test set will be labeled.
	 * @return an array containing values which describe the outcome of the sampling. These variables include:
	 * 		   i)  a boolean value which when true means the sampling was successful, else false.
	 * 		   ii) a string message describing the outcome, which may contained detailed information 
	 *             if the sampling failed for some reason.
	 */
	public Object[] sampleToARFF(String trainSetPath,String testSetPath,int negTrainSamples,int posTrainSamples,double trainSetBalance,double testSetBalance,double labelling);
}
