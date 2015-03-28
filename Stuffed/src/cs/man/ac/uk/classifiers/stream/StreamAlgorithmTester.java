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
 * File name: 	StreamAlgorithmTester.java
 * Package: cs.man.ac.uk.classifiers.stream
 * Created:	March 24th, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifiers.stream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.TreeMap;

import moa.classifiers.AbstractClassifier;
import moa.streams.ArffFileStream;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.io.Writer;
import cs.man.ac.uk.moawrappers.I_WekaTest;
import cs.man.ac.uk.moawrappers.MOAClassifier;
import cs.man.ac.uk.stats.ClassifierStatistics;

/**
 * Wrapper class for MOA stream classifiers. Contains methods which automatically
 * evaluate classifier output.
 * 
 * @author Rob Lyon
 */
public class StreamAlgorithmTester  extends MOAClassifier implements I_WekaTest
{

	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * For the classifier seed.
	 */
	Random rand = null;

	/**
	 * The name of the classifier.
	 */
	private String name = "";

	/**
	 * The classifier.
	 */
	protected AbstractClassifier learner;

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param pth the log file path.
	 * @param nme the name of the classifier, used for file logging.
	 * @param v boolean logging flag.
	 * @param cls the classifier to test.
	 */
	public StreamAlgorithmTester(String pth,String nme,boolean v, AbstractClassifier cls)
	{ 
		super(pth,v);
		this.learner = cls;
		this.name = nme;
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#train(java.lang.String)
	 */
	@Override
	public boolean train(String trainingSet)
	{
		try
		{
			// Store training set file path.
			this.trainingSetFilePath = trainingSet;

			// Since the file is expected to be large, open using a stream reader,
			// which reads in each instance in the file incrementally.
			this.trainingStream = new ArffFileStream (this.trainingSetFilePath, -1);
			this.trainingStream.prepareForUse();

			log.dualOut("Training " + name,1);
			long startTime = System.nanoTime();

			double accuracy = 0;
			double numberSamples = 0;
			double numberSamplesCorrect = 0;

			learner.setModelContext (trainingStream.getHeader());
			learner.prepareForUse();

			while ( trainingStream.hasMoreInstances() )
			{
				Instance trainInst = trainingStream.nextInstance(); 

				if(learner.correctlyClassifies(trainInst))
					numberSamplesCorrect++;

				learner.trainOnInstance(trainInst);

				numberSamples++;
			}

			log.dualOut("Training data instances: " + numberSamples,1);

			accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberSamples; 
			log.dualOut(name+ " trained on " + numberSamples + " instances & has "+accuracy+"% accuracy.",1);

			long endTime = System.nanoTime();
			long nanoseconds = endTime - startTime;
			double seconds = (double) nanoseconds / 1000000000.0;
			log.dualOut("Training "+name+" completed in "+nanoseconds+" (ns) or "+seconds+" (s)",1);
			return true;
		}
		catch (Exception e) 
		{ 
			log.erroruf("Could not train "+name+" classifier Exception building model", e);
			return false;
		}
	}

	//*****************************************
	//*****************************************
	//            TEST METHODS
	//*****************************************
	//*****************************************


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#testStatic(java.lang.String, java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	@Override
	public int[][] testStatic(String testSet,String outputPath, boolean recordMissclassifications)
	{
		try
		{
			this.testSetFilePath=testSet;
			this.testStream = new ArffFileStream (this.testSetFilePath, -1);
			this.testStream.prepareForUse();

			log.dualOut("Testing " + name,1);

			// Test meta information and important variables.
			int correctPositiveClassifications = 0;
			int correctNegativeClassifications = 0;
			int instanceNumber=0;

			ClassifierStatistics stats = new ClassifierStatistics();

			//Used to store and append output information.
			StringBuilder misclassifiedLabelledInstances = new StringBuilder();

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Testing on all instances available.",1);
			log.dualOut("Test set: " + this.testSetFilePath,1);
			log.dualOut("Test instances: " + data.numInstances(),1);

			learner.setModelContext (testStream.getHeader());
			learner.prepareForUse();

			long startTime = System.nanoTime();

			while ( testStream.hasMoreInstances() )
			{
				Instance testInst = testStream.nextInstance(); 
				instanceNumber+=1;

				String instanceClass= Double.toString(testInst.classValue());

				double[] votes = learner.getVotesForInstance(testInst);
				int classification = Utils.maxIndex(votes);

				// LABELLED TEST DATA - MOA knows the correct class.				
				if(classification==1 && instanceClass.startsWith("0"))// Predicted positive, actually negative
				{	
					stats.incrementFP();
					misclassifiedLabelledInstances.append(getFeatures(testInst,data.numAttributes()-1)+"0,FP\n");
				}
				else if(classification==1 && instanceClass.startsWith("1"))// Predicted positive, actually positive
				{
					correctPositiveClassifications+=1;
					stats.incrementTP();
				}
				else if(classification==0 && instanceClass.startsWith("1"))// Predicted negative, actually positive
				{	
					stats.incrementFN();
					misclassifiedLabelledInstances.append(getFeatures(testInst,data.numAttributes()-1)+"1,FN\n");
				}
				else if(classification==0 && instanceClass.startsWith("0"))// Predicted negative, actually negative
				{
					correctNegativeClassifications+=1;
					stats.incrementTN();
				}	

				learner.trainOnInstance(testInst);
			}

			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Testing " + name + " completed in "+duration+" (ns) or "+seconds+" (s)",1);
			log.dualOut(name + " Performance",1);

			if(recordMissclassifications)
			{
				Writer.append(outputPath, "Missclassified instances:\n");
				Writer.append(outputPath, misclassifiedLabelledInstances.toString());

				// Build path to write miss-classified data in ARFF format to.
				String extension = "";

				int i = outputPath.lastIndexOf('.');
				if (i > 0)
				{
					// Get file extension of output file.
					extension = outputPath.substring(i,outputPath.length());

					// Create the path to a new file to write miss-classified instances to,
					// in ARFF format.
					String timestamp = Common.getCondensedTime("_")+"_"+Common.getDateWithSeperator("_");
					String missclassifiedARRFPath = outputPath.replace(extension, "_Missclassified_" + timestamp + ".arff");
					missclassificationsToARFF(missclassifiedARRFPath, misclassifiedLabelledInstances.toString(), data.numAttributes());
				}
			}

			return stats.toConfusionMatrix();
		}
		catch (Exception e) 
		{ 
			log.erroruf("Could not test " + name + " classifier due to an error",e);
			return new int[][] {{0,0},{0,0}}; // Return empty matrix.
		}
	}


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#testStatic(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	@Override
	public int[][] testStatic(String testSet,String outputPath)
	{
		try
		{
			this.testSetFilePath = testSet;
			this.testStream = new ArffFileStream (this.testSetFilePath, -1);
			this.testStream.prepareForUse();

			log.dualOut("Testing " + name,1);

			// Test meta information and important variables.
			int correctPositiveClassifications = 0;
			int correctNegativeClassifications = 0;
			int instanceNumber=0;

			ClassifierStatistics stats = new ClassifierStatistics();

			//Used to store and append output information.
			StringBuilder misclassifiedLabelledInstances = new StringBuilder();

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Testing on all instances available.",1);
			log.dualOut("Test set: " + this.testSetFilePath,1);
			log.dualOut("Test instances: " + data.numInstances(),1);

			learner.setModelContext (testStream.getHeader());
			learner.prepareForUse();

			long startTime = System.nanoTime();

			while ( testStream.hasMoreInstances() )
			{
				Instance testInst = testStream.nextInstance(); 
				instanceNumber+=1;

				String instanceClass= Double.toString(testInst.classValue());

				double[] votes = learner.getVotesForInstance(testInst);
				int classification = Utils.maxIndex(votes);

				// LABELLED TEST DATA - MOA knows the correct class.				
				if(classification==1 && instanceClass.startsWith("0"))// Predicted positive, actually negative
				{	
					stats.incrementFP();
					misclassifiedLabelledInstances.append(getFeatures(testInst,data.numAttributes()-1)+"0,FP\n");
				}
				else if(classification==1 && instanceClass.startsWith("1"))// Predicted positive, actually positive
				{
					correctPositiveClassifications+=1;
					stats.incrementTP();
				}
				else if(classification==0 && instanceClass.startsWith("1"))// Predicted negative, actually positive
				{	
					stats.incrementFN();
					misclassifiedLabelledInstances.append(getFeatures(testInst,data.numAttributes()-1)+"1,FN\n");
				}
				else if(classification==0 && instanceClass.startsWith("0"))// Predicted negative, actually negative
				{
					correctNegativeClassifications+=1;
					stats.incrementTN();
				}	

				learner.trainOnInstance(testInst);
			}

			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Testing " + name + " completed in "+duration+" (ns) or "+seconds+" (s)",1);

			return stats.toConfusionMatrix();
		}
		catch (Exception e) 
		{ 
			log.erroruf("Could not test " +name+ " classifier due to an error",e); 
			return new int[][] {{0,0},{0,0}}; // Return empty matrix.
		}
	}


	/**
	 * This method performs a stream test (binary classification) , using data
	 * created using the sampling classes in cs.man.au.sample. As such it expects
	 * that each sampled data set be accompanied by a .positives and a .negatives 
	 * file, which contain true class labels for the instances unlabelled in the
	 * test set. This allows an evaluation on unlabelled data to be performed.
	 * 
	 * @param testSet the test set file to be used as a stream.
	 * @param outputPath the file to write logging statements to.
	 * @return a confusion matrix describing classifier performance.
	 */
	@SuppressWarnings("unused")
	public int[][] testStream(String testSet,String outputPath)
	{
		try
		{
			this.testSetFilePath = testSet;
			
			/**
			 * The positively labelled data.
			 */
			TreeMap<Integer,String> labelledPositives;

			/**
			 * Negatively labelled data.
			 */
			TreeMap<Integer,String> labelledNegatives;
			
			labelledPositives = this.getMetaData(this.testSetFilePath.replace(".arff", ".positives"));
			labelledNegatives = this.getMetaData(this.testSetFilePath.replace(".arff", ".negatives"));
			
			this.testStream = new ArffFileStream (this.testSetFilePath, -1);
			this.testStream.prepareForUse();

			log.dualOut("Testing " + name,1);

			// Test meta information and important variables.
			int correctPositiveClassifications = 0;
			int correctNegativeClassifications = 0;
			int instanceNumber=0;

			ClassifierStatistics stats = new ClassifierStatistics();

			//Used to store and append output information.
			StringBuilder misclassifiedLabelledInstances = new StringBuilder();

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Testing on all instances available.",1);
			log.dualOut("Test set: " + this.testSetFilePath,1);
			log.dualOut("Test instances: " + data.numInstances(),1);

			learner.setModelContext (testStream.getHeader());
			learner.prepareForUse();

			long startTime = System.nanoTime();

			while ( testStream.hasMoreInstances() )
			{
				Instance testInst = testStream.nextInstance(); 
				instanceNumber+=1;

				String instanceClass= Double.toString(testInst.classValue());

				double[] votes = learner.getVotesForInstance(testInst);
				int classification = Utils.maxIndex(votes);

				if(instanceClass.contains("NaN"))// UNLABELLED TEST DATA - MOA does not know what the correct class is!
				{
					// If the current instance is on the list of POSITIVELY
					// labelled examples.
					if(labelledPositives.containsKey(instanceNumber))
					{
						if(classification==1)
						{
							correctPositiveClassifications+=1;
							stats.incrementTP();
						}
						else if(classification==0)
						{
							stats.incrementFN();
						}
					}
					// If the current instance is on the list of NEGATIVELY
					// labelled examples.
					else if(labelledNegatives.containsKey(instanceNumber))
					{					
						if(classification==1)
						{
							stats.incrementFP();
						}
						else if(classification==0)
						{
							correctNegativeClassifications+=1;
							stats.incrementTN();
						}
					}
					else // THEN THIS DATA IS ALMOST CERTAINLY NEGATIVE.
					{
						if(classification==1)
							stats.incrementFP();
						else if(classification==0)
						{
							correctNegativeClassifications+=1;
							stats.incrementTN();
						}
					}
					
					// Train on unlabelled data if possible.
					learner.trainOnInstance(testInst);
				}
				else
				{
					// LABELLED TEST DATA - MOA knows the correct class.				
					if(classification==1 && instanceClass.startsWith("0"))// Predicted positive, actually negative
					{	
						stats.incrementFP();
					}
					else if(classification==1 && instanceClass.startsWith("1"))// Predicted positive, actually positive
					{
						correctPositiveClassifications+=1;
						stats.incrementTP();
					}
					else if(classification==0 && instanceClass.startsWith("1"))// Predicted negative, actually positive
					{	
						stats.incrementFN();
					}
					else if(classification==0 && instanceClass.startsWith("0"))// Predicted negative, actually negative
					{
						correctNegativeClassifications+=1;
						stats.incrementTN();
					}	

					learner.trainOnInstance(testInst);
				}
			}

			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Testing " + name + " completed in "+duration+" (ns) or "+seconds+" (s)",1);

			return stats.toConfusionMatrix();
		}
		catch (Exception e) 
		{ 
			log.erroruf("Could not test " +name+ " classifier due to an error",e); 
			return new int[][] {{0,0},{0,0}}; // Return empty matrix.
		}
	}

	/**
	 * Make predictions on never before seen instances.
	 * @param newData the data to make predictions on.
	 * @param outputPath the log path
	 * @param predictionsPath the path to write predictions to.
	 */
	public boolean predict(String newData,String outputPath,String predictionsPath)
	{
		// Store training set file path.
		this.testSetFilePath = newData;

		this.testStream = new ArffFileStream (this.testSetFilePath, -1);
		this.testStream.prepareForUse();

		log.dualOut("Predicting using " + name,1);

		try
		{
			// Test meta information and important variables.
			int positiveClassifications = 0;
			int negativeClassifications = 0;
			@SuppressWarnings("unused")
			int instanceNumber=0;

			//Used to store and append output information.
			StringBuilder predictions = new StringBuilder();

			// Produce an ARFF file header for output predictions.
			ProducePredictionsARFFHeader(predictionsPath,testStream.getHeader().numAttributes());

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Predicting on all instances available.",1);

			long startTime = System.nanoTime();

			learner.setModelContext (testStream.getHeader());
			learner.prepareForUse();

			// Predict over instances.
			while ( testStream.hasMoreInstances() )
			{
				Instance testInst = testStream.nextInstance(); 
				instanceNumber+=1;

				double[] votes = learner.getVotesForInstance(testInst);
				int classification = Utils.maxIndex(votes);

				String instanceClass = Double.toString(testInst.classValue());

				if(classification==1)// Predicted positive.
				{
					predictions.append(getFeatures(testInst,testInst.numAttributes()-1)+"1\n");
					positiveClassifications++;
				}
				else// Predicted negative
				{
					predictions.append(getFeatures(testInst,testInst.numAttributes()-1)+"0\n");
					negativeClassifications++;
				}

				// If label is available, train.
				if(!instanceClass.contains("NaN"))
					learner.trainOnInstance(testInst);

				// Write buffer to file.
				if(predictions.length()>10000)
				{
					Writer.append(predictionsPath, predictions.toString());
					predictions.setLength(0);
				}
			}

			// Empty buffer.
			if(predictions.length()>0)
				Writer.append(outputPath, predictions.toString());

			// Report details.
			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Predicting using " + name + " completed in " + duration + " (ns) or " + seconds + " (s)",1);
			log.dualOut("Predicted positive: " + positiveClassifications + " predicted negative " + negativeClassifications,1);

			return true;
		}
		catch (Exception e)
		{ 
			log.erroruf("Could not make predictions using " +name+ " classifier due to an error",e);
			return false;
		}
	}

	/**
	 * Saves the classifier model to the path specified.
	 * @param path the path to save the classifier model to.
	 */
	public boolean saveModel(String path)
	{
		try
		{
			Common.fileDelete(path);// Delete existing model if it exists.
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(learner);
			oos.flush();
			oos.close();

			return Common.fileExist(path);
		}
		catch(Exception e)
		{ 
			log.erroruf("could not save "+name+" model",e);
			return false;
		}
	}

	/**
	 * Loads the classifier model at the path specified.
	 * @param path to the model to load.
	 */
	public boolean loadModel(String path)
	{
		try
		{
			// Deserialize model
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			AbstractClassifier cls = (AbstractClassifier) ois.readObject();
			ois.close();

			learner = cls;

			if(learner!=null)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{ 
			log.erroruf("could not save " + name + " model",e);
			return false;
		}
	}

	/**
	 * Resets this classifier
	 */
	@Override
	public boolean reset()
	{
		log.dualOut("Resetting " + name,1);
		try
		{
			stats.reset();
			this.trainingStream = null;
			this.testStream = null;		
			learner.resetLearning();
			log.dualOut(name + " reset successfully",1);
			return true;
		}
		catch (Exception e)
		{ 
			log.erroruf("Caught Exception resetting " + name + " learner",e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifiers.I_MOATest#setOptions()
	 */
	public void setOptions()
	{
		//Parameters:

		// -m : Maximum memory consumed by the tree
		//this.learner.getOptions().setViaCLIString("-m 33554432");// 32 MB (default).
		// -n : Numeric estimator to use :
		//this.learner.getOptions().setViaCLIString("-n (GaussianNumericAttributeClassObserver -n 10)"); // Gaussian estimator, 10 bins (default).
		// Gaussian approximation evaluating 10 splitpoints
		// Gaussian approximation evaluating 100 splitpoints
		// Greenwald-Khanna quantile summary with 10 tuples
		// Greenwald-Khanna quantile summary with 100 tuples
		// Greenwald-Khanna quantile summary with 1000 tuples
		// VFML method with 10 bins
		// VFML method with 100 bins
		// VFML method with 1000 bins
		// Exhaustive binary tree

		// -e : How many instances between memory consumption checks
		//this.learner.getOptions().setViaCLIString("-e 1000000");// check every one million instances (default).

		// -g : The number of instances a leaf should observe between split attempts
		//this.learner.getOptions().setViaCLIString("-g 200");// 200 (default).

		// -s : Split criterion to use. Example : InfoGainSplitCriterion
		//this.learner.getOptions().setViaCLIString("-s InfoGainSplitCriterion");// Default.

		// -c : The allowable error in split decision, values closer to 0 will take longer to decide
		//this.learner.getOptions().setViaCLIString("-c 0");// Default

		// -t : Threshold below which a split will be forced to break ties
		//this.learner.getOptions().setViaCLIString("-t 0.05");// Default.

		// -b : Only allow binary splits
		//this.learner.getOptions().setViaCLIString("-b"); // Disabled by default

		// -z : Stop growing as soon as memory limit is hit
		//this.learner.getOptions().setViaCLIString("-z");// Disabled by default

		// -r : Disable poor attributes
		//this.learner.getOptions().setViaCLIString("-r");// Disabled by default

		// -p : Disable pre-pruning
		//this.learner.getOptions().setViaCLIString("-p");// Disabled by default

		// -l : Leaf classifier to use at the leaves: Majority class, Naive Bayes, Naive Bayes Adaptive. By default: Naive Bayes Adaptive.
		//this.learner.getOptions().setViaCLIString("-l NBAdaptive");// Default
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#processPositivePrediction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processPositivePrediction(String outputPath, String data) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#processNegativePrediction(java.lang.String, java.lang.String)
	 */
	@Override
	public void processNegativePrediction(String outputPath, String data) {
		// TODO Auto-generated method stub

	}
}
