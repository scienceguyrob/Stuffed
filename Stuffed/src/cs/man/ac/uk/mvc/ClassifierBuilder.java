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
 * File name: 	ClassifierBuilder.java
 * Package: cs.man.ac.uk.mvc
 * Created:	April 9, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@cs.man.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.mvc;

import moa.classifiers.AbstractClassifier;
import moa.classifiers.bayes.PNB;
import moa.classifiers.trees.GHVFDT;
import moa.classifiers.trees.HoeffdingTree;
import moa.classifiers.trees.OCVFDT;
import weka.classifiers.trees.J48;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import cs.man.ac.uk.classifiers.Classifiers;
import cs.man.ac.uk.classifiers.standard.StandardAlgorithmTester;
import cs.man.ac.uk.classifiers.stream.StreamAlgorithmTester;

/**
 * This class is used to control the building of new classifiers.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 04/09/14
 */
public class ClassifierBuilder
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The path to the ARFF training set file used to train a classifier.
	 */
	private String trainingSet ="";

	/**
	 * The path to the ARFF training set file used to test a classifier.
	 */
	private String testSet ="";

	/**
	 * The path to write training and test results to.
	 */
	private String outputFile = "";

	/**
	 * The verbose logging flag.
	 */
	private boolean verbose = false;

	/**
	 * The algorithm to use.
	 */
	private int algorithm = -1;
	
	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param pth the log file path.
	 * @param v the boolean logging flag.
	 */
	public ClassifierBuilder(String pth, boolean v)
	{ 
		this.outputFile = pth;
		this.verbose = v;
	}

	//*****************************************
	//*****************************************
	//           Getter & Setters
	//*****************************************
	//*****************************************
	
	/**
	 * @param trainingSet the trainingSet to set
	 */
	public void setTrainingSet(String trainingSet) { this.trainingSet = trainingSet; }

	/**
	 * @param testSet the testSet to set
	 */
	public void setTestSet(String testSet) { this.testSet = testSet; }

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(int algorithm) { this.algorithm = algorithm; }

	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(String outputFile) { this.outputFile = outputFile; }
	
	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************
	
	/**
	 * Builds and tests the classifier specified by the algorithm variable.
	 * Note if no unlabelled data is in the test set, then meta data can be set to null.
	 * @return confusion matrix describing binary classification outcomes.
	 */
	public  int[][] test()
	{
		switch (algorithm)
		{
			case Classifiers.J48:
				return stdloadAndTest(new StandardAlgorithmTester(this.outputFile,"J48",this.verbose,new J48())); 
			case Classifiers.MLP:
				return stdloadAndTest(new StandardAlgorithmTester(this.outputFile,"MLP",this.verbose,new MultilayerPerceptron())); 
			case Classifiers.NB:
				return stdloadAndTest(new StandardAlgorithmTester(this.outputFile,"NB",this.verbose,new NaiveBayes())); 
			case Classifiers.SVM:
				return stdloadAndTest(new StandardAlgorithmTester(this.outputFile,"SVM",this.verbose,new SMO())); 
			case Classifiers.HTREE:
				return streamloadAndTest(new StreamAlgorithmTester(this.outputFile,"HTREE",this.verbose,new HoeffdingTree()));
			case Classifiers.GHVFDT:
				return streamloadAndTest(new StreamAlgorithmTester(this.outputFile,"GHVFDT",this.verbose,new GHVFDT()));
			case Classifiers.PNB:
				return streamloadAndTest(new StreamAlgorithmTester(this.outputFile,"PNB",this.verbose,new PNB()));  
			case Classifiers.OCVFDT:
				return streamloadAndTest(new StreamAlgorithmTester(this.outputFile,"OCVFDT",this.verbose,new OCVFDT()));  
			default:
				int[][] confusion_matrix = {{0,0},{0,0}};
				return confusion_matrix;
		}
	}
	
	/**
	 * Builds and tests the classifier specified by the algorithm variable.
	 * Note if no unlabelled data is in the test set, then meta data can be set to null.
	 * Only for stream classifiers, no pre-training undertaken.
	 * @return confusion matrix describing binary classification outcomes.
	 */
	public  int[][] testNoTrain()
	{
		switch (algorithm)
		{
			case Classifiers.HTREE:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"HTree",this.verbose,new HoeffdingTree()));
			case Classifiers.GHVFDT:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"GHVFDT",this.verbose,new GHVFDT()));
			case Classifiers.PNB:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"PNB",this.verbose,new PNB()));  
			case Classifiers.OCVFDT:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"OCVFDT",this.verbose,new OCVFDT()));  
			default:
				int[][] confusion_matrix = {{0,0},{0,0}};
				return confusion_matrix;
		}
	}
	
	/**
	 * Tests the supplied classifier specified by the algorithm variable.
	 * Note if no unlabelled data is in the test set, then meta data can be set to null.
	 * Only for stream classifiers, no pre-training undertaken.
	 * @param c the classifier to use for testing.
	 * @return confusion matrix describing binary classification outcomes.
	 */
	public  int[][] testNoTrain(AbstractClassifier c)
	{
		switch (algorithm)
		{
			case Classifiers.HTREE:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"HTREE",this.verbose,c));
			case Classifiers.GHVFDT:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"GHVFDT",this.verbose,c));
			case Classifiers.PNB:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"PNB",this.verbose,c));  
			case Classifiers.OCVFDT:
				return streamTest(new StreamAlgorithmTester(this.outputFile,"OCVFDT",this.verbose,c));  
			default:
				int[][] confusion_matrix = {{0,0},{0,0}};
				return confusion_matrix;
		}
	}
	
	/**
	 * Trains and tests the supplied classifier, standard static learning scenario.
	 * @param classifier the classifier to train and test.
	 * @return true if trained and tested successfully, else false.
	 */
	private int[][] stdloadAndTest(StandardAlgorithmTester classifier)
	{
		classifier.train(trainingSet);
		
		int[][] confusionMatrix = classifier.testStatic(testSet,this.outputFile,testSet.replace(".arff",".trueClass.csv"));
		
		return confusionMatrix;
	}
	
	/**
	 * Trains and tests the supplied classifier, in a streaming data scenario.
	 * @param classifier the classifier to train and test.
	 * @return true if trained and tested successfully, else false.
	 */
	private int[][] streamloadAndTest(StreamAlgorithmTester classifier)
	{
		classifier.train(trainingSet);
		
		int[][] confusionMatrix = classifier.testStream(testSet,this.outputFile,testSet.replace(".arff",".trueClass.csv"));
		
		return confusionMatrix;
	}
	
	/**
	 * Tests the supplied classifier (no pre-training), in a streaming data scenario.
	 * @param classifier the classifier to train and test.
	 * @return true if trained and tested successfully, else false.
	 */
	private int[][] streamTest(StreamAlgorithmTester classifier)
	{
		int[][] confusionMatrix = classifier.testStream(testSet,this.outputFile,testSet.replace(".arff",".trueClass.csv"));
		
		return confusionMatrix;
	}
}