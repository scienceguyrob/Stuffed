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
 * File name: 	IFile.java
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
 * The class IFile defines an interface for files that can be sampled.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public interface IFile
{
	//*****************************************
	//*****************************************
	//           Getter & Setters
	//*****************************************
	//*****************************************
	
	/**
	 * @return the file extension of the file.
	 */
	public String getExtension();
	
	/**
	 * @return the absolute path to the file object represented by this class.
	 */
	public String getPath();
	
	/**
	 * @return the absolute path to the file object used to write log statements to.
	 */
	public String getLogPath();

	/**
	 * @return the file name of this object, minus the path (extension included).
	 */
	public String getFileName();

	/**
	 * @return the number of lines of data in the file represented by this object.
	 */
	public int getLineCount();

	/**
	 * Sets the value of the variable representing the number of lines of data in
	 * the file represented by this object.
	 * @param l the total number of lines.
	 */
	public void setLineCount(int l);

	/**
	 * @return the actual number of rows of data  in the file represented by this object,
	 * minus any non-data lines in the file.
	 */
	public int getRows();

	/**
	 * Sets the value of the variable representing the number of rows of data in
	 * the file represented by this object.
	 * @param r the total number of rows of data.
	 */
	public void setRows(int r);

	/**
	 * @return the actual number of columns of data in the file represented by this object,
	 * minus any non-data columns in the file.
	 */
	public int getColumns();

	/**
	 * Sets the value of the variable representing the number of columns of data in
	 * the file represented by this object.
	 * @param c the total number of columns of data.
	 */
	public void setColumns(int c);
	
	/**
	 * @return the relation aka the name of the data set.
	 */
	public String getRelation();

	/**
	 * Sets the name of the data set contained within the file represented by this class.
	 * @param r the relation name to set.
	 */
	public void setRelation(String r);

	/**
	 * @return a string array containing all the class labels used in the file represented
	 * by this class. For instance if there are two class 1 and 0 (o=negative and 1=positive)
	 * then this would return a string array containing "0" and "1".
	 */
	public String[] getClassLabels();

	/**
	 * Sets the values stored in a variable which records the different classes found in the
	 * file represented by this object.
	 * @param l the array of strings representing the different classes found in the file.
	 */
	public void setClassLabels(String[] l);

	/**
	 * @return the labels of the attributes stored in the file, i.e. strings
	 * that describe the contents of each column. In ARFF files these would be
	 * the attribute names, in CSV these would be strings heading each column.
	 */
	public String[] getAttributeLabels();

	/**
	 * Sets the values stored in a variable which keeps a record of the names
	 * of the attributes stored in the file represented by this object.
	 * @param a string array containing the name of the attributes found in the file.
	 */
	public void setAttributeLabels(String[] a);

	/**
	 * @return the integer count of attributes in the file, equal to the number of columns
	 * of data in the file, minus 1 (minus 1 since one of the columns contains the class label). 
	 */
	public int getAttributeCount();

	/**
	 * Sets the value of the variable which counts the number of attributes in the
	 * file represented by this class, not including the class label.
	 * @param a the number of attributes found in the file.
	 */
	public void setAttributeCount(int a);

	/**
	 * @return <P>returns an integer array describing the class distribution found in the file.
	 * For example if there are 100 negative examples and 10 positives, then the negatives
	 * are treated as class zero and the positives as class one. So then:</p>
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

	/**
	 * Sets the value stored in a variable which records the class distribution observed in the
	 * file represented by this class.
	 * @param dist the observed distribution to set.
	 */
	public void setClassDistribution(int[] dist);

	/**
	 * Sets the class index of the file.
	 * @param i the new class index.
	 */
	public void setClassIndex(int i);
	
	/**
	 * @return the index of the class attribute in the file, i.e. the column the class attribute
	 * can be found in (zero-indexed).
	 */
	public int getClassIndex();

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

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
	public Object[] sampleToCSV(String trainSetPath, String testSetPath, int negTrainSamples,int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling);
	
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
	public Object[] sampleToARFF(String trainSetPath, String testSetPath, int negTrainSamples,int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling);
	
	/**
	 * <P>Creates meta data this data file. Meta data contain extra information about the
	 * data within the file useful for sampling. Such meta data is actually necessary for
	 * us to correctly sample especially when a specific class distribution is required.
	 * 
	 * This method takes the file represented by this class, parses it, and produces two
	 * new files:</p>
	 * 
	 * <ol>
	 * 	<li>Original File Name.positive.meta</li>
	 *  <li>Original File Name.negative.meta</li>
	 * </ol>
	 * 
	 * <P>The first contains those positive examples in the original file i.e. 
	 * those with class = 1, the second the negative examples. The new 
	 * files are almost identical to the original files in terms of structure,
	 * except that they have an additional feature. This feature is the
	 * row location of each instance in the original file. So for instance if we
	 * have an ARFF file as follows:</p>
	 * 
	 * % Title : Original Data Set.
	 *  RELATION Original
	 *  ATTRIBUTE a	NUMERIC
	 *  ATTRIBUTE b	NUMERIC
	 *  ATTRIBUTE c	NUMERIC
	 *  ATTRIBUTE class		{0,1}
	 *  DATA
	 * 13 , 10 , 2 , 1
	 * 24 , 12 , 2 , 0
	 * 61 , 21 , 3 , 1
	 * 18 , 12 , 2 , 0
	 * 26 , 11 , 2 , 1
	 * 
	 * Then the positive ARFF file would be:
	 * 
	 * % Title : Positives.
	 *  RELATION Original
	 *  ATTRIBUTE index NUMERIC
	 *  ATTRIBUTE a		NUMERIC
	 *  ATTRIBUTE b		NUMERIC
	 *  ATTRIBUTE c		NUMERIC
	 *  ATTRIBUTE class		{0,1}
	 *  DATA
	 * 1 , 13 , 10 , 2 , 1
	 * 3 , 61 , 21 , 3 , 1
	 * 5 , 26 , 11 , 2 , 1
	 * 
	 * And the negative:
	 * % Title : Negatives.
	 *  RELATION Original
	 *  ATTRIBUTE index NUMERIC
	 *  ATTRIBUTE a		NUMERIC
	 *  ATTRIBUTE b		NUMERIC
	 *  ATTRIBUTE c		NUMERIC
	 *  ATTRIBUTE class		{0,1}
	 *  DATA
	 * 2 , 24 , 12 , 2 , 0
	 * 4 , 18 , 12 , 2 , 0
	 * 
	 * <p>By separating the classes in this way, we can create new sampled data sets
	 * from these files. In particular as we have the indexes of the classes, we
	 * can, using random number generation, choose indexes to include in a new sample.</p>
	 * 
	 * @return true if successful, else false.
	 */
	public boolean createMetaData();
	
	
	/**
	 * Shuffles the data in the file so that it obtains a random reordering.
	 * @return true if the file was shuffled successfully, else false.
	 */
	public boolean shuffleDataset();
	
	/**
	 * Shuffles the data in the file so that it obtains a random reordering.
	 * @param pth the path to write the shuffled data to.
	 * @return true if the file was shuffled successfully, else false.
	 */
	public boolean shuffleDataset(String pth);
	
	/**
	 * @return true if the file could be read and understood, else false.
	 */
	public boolean preprocess();
	
	/**
	 * Writes log statements to a log file.
	 * @param msg the string message to write to the log file.
	 * @return true of the message was written successfully, else false.
	 */
	public boolean log(String msg);
}
