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
 * File name: 	IEditor.java
 * Package: cs.man.ac.uk.interfaces
 * Created:	May 30, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

import java.util.TreeMap;

/**
 * The class IEditor defines an interface for sampling and editing data files.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 30/05/14
 */
public interface IEditor
{
	/**
	 * Methods that gathers information about the file to be sampled.
	 * Assumes that the class labels are the last piece of
	 * data in each row.
	 * @return true if successfully pre-processed, else false.
	 */
	public boolean preprocess();

	/**
	 * Shuffles the rows of data within a file, however caution should be be used
	 * with this method. Large files may take a long time to shuffle, and may use
	 * up a great deal of RAM - this method has to load a file into memory to perform
	 * the shuffle.
	 * @return true if shuffled successfully, else false.
	 */
	public boolean shuffle();
	
	/**
	 * Shuffles the rows of data within a file, however caution should be be used
	 * with this method. Large files may take a long time to shuffle, and may use
	 * up a great deal of RAM - this method has to load a file into memory to perform
	 * the shuffle.
	 * @param path the path to save the shuffled data to.
	 * @return true if shuffled successfully, else false.
	 */
	public boolean shuffle(String path);

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
	 * By separating the classes in this way, we can create new sampled data sets
	 * from these files. In particular as we have the indexes of the classes, we
	 * can, using random number generation, choose indexes to include in a new sample.
	 * 
	 * @return true if successful, else false.
	 */
	public boolean createMetaDataBinary();

	/**
	 * Obtains meta information from the ARFF files created by the {@link #createMetaDataBinary()}
	 * method. This method reads the modified ARFF file at the specified path, and extracts the
	 * row index of the instance from the file. For instance, the following is an example of a 
	 * modified ARFF file containing only positive instances:
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
	 * This method will extract the row index (i.e. 1,3,5 above), and store it in a hash map based
	 * data structure. The information above is in the hash map as follows:
	 * 
	 * --------------------
	 * | KEY	|	VALUE |
	 * --------------------
	 * | 1		|	  1	  |
	 * | 2		|	  3	  |
	 * | 3		|	  5	  |
	 * --------------------	
	 * 
	 * The reason we do this is so that we can more easily sample the positive and negative examples.
	 * For instance, to sample a positive example above, all we need to do is choose a random number
	 * between 1-3 (the key value) to obtain the index of a positive instance in the original file.
	 * Thus when we read the original file to sample it, we take only those positive examples that
	 * have been chosen randomly from the hash map structure depicted above.
	 * 
	 * @param labelledDataPath the path to the file containing positives.
	 * @return a hash map containing the patterns.
	 */
	public TreeMap<Integer,Integer> getMetaData(String labelledDataPath);

	//*****************************************
	//*****************************************
	//             Sampling
	//*****************************************
	//*****************************************

	/**
	 * Samples a file producing an ARFF output.
	 * @param trainPath the path to the training set file to create.
	 * @param testPath the path to the test set file to create.
	 * @param negTrainSamples the number of negative samples to include in the training set.
	 * @param posTrainSamples the number of positive samples to include in the training set.
	 * @param trainingSetBalance the class balance desired in the training set.
	 * @param testSetBalance the class balance desired in the test set.
	 * @param labelling the ratio of labelled data to aim for.
	 * @return true if sampled successfully, else false.
	 */
	public boolean sampleToCSV(String trainPath,String testPath,int negTrainSamples,int posTrainSamples,double trainingSetBalance,double testSetBalance,double labelling);
	
	/**
	 * Samples a file producing an ARFF output.
	 * @param trainPath the path to the training set file to create.
	 * @param testPath the path to the test set file to create.
	 * @param negTrainSamples the number of negative samples to include in the training set.
	 * @param posTrainSamples the number of positive samples to include in the training set.
	 * @param trainingSetBalance the class balance desired in the training set.
	 * @param testSetBalance the class balance desired in the test set.
	 * @param labelling the ratio of labelled data to aim for.
	 * @return true if sampled successfully, else false.
	 */
	public boolean sampleToARFF(String trainPath,String testPath,int negTrainSamples,int posTrainSamples,double trainingSetBalance,double testSetBalance,double labelling);

	//*****************************************
	//*****************************************
	//               TO ARFF
	//*****************************************
	//*****************************************

	/**
	 * Writes out the header for an ARFF file, including details of all features.
	 * @param destination the file to write the header information to.
	 * @param title the title of the ARFF file.
	 * @param rel the name for the relation in the file.
	 * @param description a description of the relation.
	 * @return true if completed successfully, else false.
	 */
	public boolean writeARFFHeader(String destination,String title, String rel,String description);

	/**
	 * Writes out the header for an ARFF file, containing the specified features.
	 * Note: you must explicitly include the index of the class labels to have them included
	 * in the output file.
	 * @param destination the file to write the header information to.
	 * @param title the title of the ARFF file.
	 * @param rel the name for the relation in the file.
	 * @param description a description of the relation.
	 * @param featureIndexes the features to include.
	 * @return true if completed successfully, else false.
	 */
	public boolean writeARFFHeader(String destination,String title, String rel,String description,int[] featureIndexes);

	/**
	 * Methods which decides if an instance should be labelled or not.
	 * @param labelled the number of instances currently labelled.
	 * @param unlabelled the number of instances currently unlabeled.
	 * @param INSTANCES_TO_LABEL the total number of instances that must be labelled.
	 * @param INSTANCES_TO_NOT_LABEL the total number of instances that must remain unlabelled.
	 * @param labelling the labeling ratio.
	 * @return true if the current instance should be labelled, else false.
	 */
	public boolean label(int labelled,int unlabelled,int INSTANCES_TO_LABEL,int INSTANCES_TO_NOT_LABEL,double labelling);
}
