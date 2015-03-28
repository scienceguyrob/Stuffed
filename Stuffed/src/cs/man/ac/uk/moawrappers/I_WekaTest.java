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
 * File name: 	I_WekaTest.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.moawrappers;

/**
 * The class I_WekaTest defines an interface for testing WEKA classifiers.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public interface I_WekaTest
{
	/**
	 * Trains the classifier.
	 * @param path the training set path.
	 * @return true if trained successfully, else false.
	 */
	public boolean train(String path);

	/**
	 * Tests the classifier, but does not learn on the test  examples (static data set equivalent).
	 * @param testSet the path to the test set.
	 * @param outputPath the path to write results to.
	 * @param recrodMissclassifications a flag that when true, tells the classifier
	 *        to record all the mistakes made on data for which there are labels.
	 * @param posMetaData the positive meta data used to evaluate class predictions on unlabelled examples.
	 * @param negMetaData the negative meta data used to evaluate class predictions on unlabelled examples.
	 * @return confusion matrix describing binary classification outcomes.
	 */
	public int[][] testStatic(String testSet,String outputPath,boolean recrodMissclassifications,String posMetaData,String negMetaData);

	/**
	 * Tests the classifier, but does not learn on the test examples (static data set equivalent).
	 * @param testSet the path to the test set.
	 * @param outputPath the path to write results to.
	 * @param posMetaData the positive meta data used to evaluate class predictions on unlabelled examples.
	 * @param negMetaData the negative meta data used to evaluate class predictions on unlabelled examples.
	 * @return the confusion matrix corresponding to classifier outputs.
	 */
	public int[][] testStatic(String testSet,String outputPath,String posMetaData,String negMetaData);
	
	/**
	 * Resets the classifier implementing this interface.
	 * @return true if the learning model was reset successfully, else false.
	 */
	public boolean reset();
	
	/**
	 * Make predictions on never before seen instances.
	 * @param data the data to make predictions on.
	 * @param outputPath the log path to write to.
	 * @param predictionsPath the path to write predictions to.
	 * @return true if predictions were made successfully, else false.
	 */
	public boolean predict(String data,String outputPath,String predictionsPath);
	
	/**
	 * Saves the classifier model to the path specified.
	 * @param path the path to save the classifier model to.
	 * @return true if the learning model was saved correctly, else false.
	 */
	public boolean saveModel(String path);
	
	/**
	 * Loads the classifier model at the path specified.
	 * @param path to the model to load.
	 * @return true if the learning model was loaded from disk successfully, else false.
	 */
	public boolean loadModel(String path);
	
	/**
	 * Processes data items given a positive label.
	 * @param outputPath the path to write positive predictions to.
	 * @param data given a positive class label
	 */
	public void processPositivePrediction(String outputPath,String data);
	
	/**
	 * Processes data items given a positive label.
	 * @param outputPath the path to write positive predictions to.
	 * @param data given a negative class label
	 */
	public void processNegativePrediction(String outputPath,String data);
	
}
