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
 * File name: 	StandardAlgorithmTester.java
 * Package: cs.man.ac.uk.classifiers.standard
 * Created:	March 24th, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifiers.standard;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.io.Writer;
import cs.man.ac.uk.moawrappers.I_WekaTest;
import cs.man.ac.uk.moawrappers.WekaClassifier;
import cs.man.ac.uk.stats.ClassifierStatistics;

/**
 * Wrapper class for WEKA classifiers. Contains methods which automatically
 * evaluate classifier output.
 * 
 * @author Rob Lyon
 */
public class StandardAlgorithmTester  extends WekaClassifier implements I_WekaTest
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The decision tree classifier.
	 */
	private Classifier learner;

	/**
	 * The classifier_name of the classifier.
	 */
	private String name = "";

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
	public StandardAlgorithmTester(String pth,String nme,boolean v, Classifier cls)
	{ 
		super(pth,v);
		this.learner = cls;
		this.name = nme;
	}

	//*****************************************
	//*****************************************
	//           Getter & Setters
	//*****************************************
	//*****************************************

	public Classifier getClassifier(){ return this.learner; }

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#train()
	 */
	@Override
	public boolean train(String trainingSet)
	{	
		// Store training set file path.
		this.trainingSetFilePath=trainingSet;

		log.dualOut("Training "+name,1);
		log.dualOut("Training set: " + this.trainingSetFilePath,1);

		long startTime = System.nanoTime();// Record time prior to training.

		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(this.trainingSetFilePath));
			Instances data = new Instances(reader);

			data.setClassIndex(data.numAttributes() - 1);// Uses last column of data as class index.

			log.dualOut("Training instances: " + data.numInstances(),1);
			learner.buildClassifier(data);

			// Record time at end of training.
			long endTime = System.nanoTime();
			long nanoseconds = endTime - startTime;
			double seconds = (double) nanoseconds / 1000000000.0;


			log.dualOut("Training "+name+" completed in "+nanoseconds+" (ns) or "+seconds+" (s)",1);
			return true;
		}
		catch (IOException e) { log.erroruf("Could not train "+name+" classifier IOException on training data file", e); return false; }
		catch (Exception e) { log.erroruf("Could not train "+name+" classifier Exception building model", e); return false;}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#testStatic(java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	@Override
	public int[][] testStatic(String testSet,String outputPath, boolean recordMissclassifications,String posMetaData,String negMetaData)
	{
		// Store training set file path.
		this.testSetFilePath=testSet;

		/**
		 * The positively labelled data.
		 */
		TreeMap<Integer,String> labelledPositives;

		/**
		 * Negatively labelled data.
		 */
		TreeMap<Integer,String> labelledNegatives;

		if(posMetaData!= null)
			labelledPositives = this.getMetaData(posMetaData);
		else
			labelledPositives = new TreeMap<Integer,String>();
		
		if(negMetaData!= null)
			labelledNegatives = this.getMetaData(negMetaData);
		else
			labelledNegatives = new TreeMap<Integer,String>();

		log.dualOut("Testing "+name,1);

		try
		{
			// Test meta information and important variables.
			int correctPositiveClassifications = 0;
			int correctNegativeClassifications = 0;
			int instanceNumber=0;

			// Used to store classifier outcomes.
			ClassifierStatistics stats = new ClassifierStatistics();

			//Used to store and append output information.
			StringBuilder misclassifiedLabelledInstances = new StringBuilder();

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1); // Last column of data contains the class variable.

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Testing on all instances available.",1);
			log.dualOut("Test set: " + this.testSetFilePath, 1);
			log.dualOut("Test instances: " + data.numInstances(), 1);

			long startTime = System.nanoTime(); // Record time before classification begins.

			// Classify instances and evaluate.
			for (int i = 0; i < data.numInstances(); i++) 
			{
				instanceNumber+=1;

				double classification = learner.classifyInstance(data.instance(i));
				String instanceClass= Double.toString(data.instance(i).classValue());

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
							misclassifiedLabelledInstances.append(getFeatures(data.instance(i),data.numAttributes()-1)+"1,FN\n");
						}
					}
					// If the current instance is on the list of NEGATIVELY
					// labelled examples.
					else if(labelledNegatives.containsKey(instanceNumber))
					{					
						if(classification==1)
						{
							stats.incrementFP();
							misclassifiedLabelledInstances.append(getFeatures(data.instance(i),data.numAttributes()-1)+"0,FP\n");
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
						{
							stats.incrementFP();
							misclassifiedLabelledInstances.append(getFeatures(data.instance(i),data.numAttributes()-1)+"0,FP\n");
						}
						else if(classification==0)
						{
							correctNegativeClassifications+=1;
							stats.incrementTN();
						}
					}
				}
				else
				{

					// LABELLED TEST DATA - WEKA API knows the correct class.				
					if(classification==1 && instanceClass.startsWith("0"))// Predicted positive, actually negative
					{	
						stats.incrementFP();
						misclassifiedLabelledInstances.append(getFeatures(data.instance(i),data.numAttributes()-1)+"0,FP\n");
					}
					else if(classification==1 && instanceClass.startsWith("1"))// Predicted positive, actually positive
					{
						correctPositiveClassifications+=1;
						stats.incrementTP();
					}
					else if(classification==0 && instanceClass.startsWith("1"))// Predicted negative, actually positive
					{	
						stats.incrementFN();
						misclassifiedLabelledInstances.append(getFeatures(data.instance(i),data.numAttributes()-1)+"1,FN\n");
					}
					else if(classification==0 && instanceClass.startsWith("0"))// Predicted negative, actually negative
					{
						correctNegativeClassifications+=1;
						stats.incrementTN();
					}
				}
			}

			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Testing "+name+" completed in "+duration+" (ns) or "+seconds+" (s)",1);

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
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#testStatic(java.lang.String, boolean)
	 */
	@SuppressWarnings("unused")
	@Override
	public int[][] testStatic(String testSet,String outputPath,String posMetaData,String negMetaData)
	{
		// Store training set file path.
		this.testSetFilePath=testSet;

		/**
		 * The positively labelled data.
		 */
		TreeMap<Integer,String> labelledPositives;

		/**
		 * Negatively labelled data.
		 */
		TreeMap<Integer,String> labelledNegatives;

		if(posMetaData!= null)
			labelledPositives = this.getMetaData(posMetaData);
		else
			labelledPositives = new TreeMap<Integer,String>();
		
		if(negMetaData!= null)
			labelledNegatives = this.getMetaData(negMetaData);
		else
			labelledNegatives = new TreeMap<Integer,String>();

		log.dualOut("Testing " + name,1);

		try
		{
			// Test meta information and important variables.
			int correctPositiveClassifications = 0;
			int correctNegativeClassifications = 0;
			int instanceNumber=0;

			ClassifierStatistics stats = new ClassifierStatistics();

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Testing on all instances available.",1);
			log.dualOut("Test set: " + this.testSetFilePath,1);
			log.dualOut("Test instances: " + data.numInstances(),1);

			long startTime = System.nanoTime();

			// label instances
			for (int i = 0; i < data.numInstances(); i++) 
			{
				instanceNumber+=1;

				// Classify the next data item.
				double classification = learner.classifyInstance(data.instance(i));
				String instanceClass= Double.toString(data.instance(i).classValue());

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
							stats.incrementFP();
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
				}
				else
				{

					// LABELLED TEST DATA - WEKA API knows the correct class.				
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
				}
			}

			// Record time classification completed.
			long endTime = System.nanoTime();
			long duration = endTime - startTime;
			double seconds = (double) duration / 1000000000.0;

			log.dualOut("Testing "+name+" completed in "+duration+" (ns) or "+seconds+" (s)",1);

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
		this.testSetFilePath=newData;

		log.dualOut("Predicting using "+name,1);

		try
		{
			// Test meta information and important variables.
			int positiveClassifications = 0;
			int negativeClassifications = 0;
			@SuppressWarnings("unused")
			int instanceNumber=0;

			// Prepare data for testing
			BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);

			log.dualOut(name + " Classifier is ready.",1);
			log.dualOut(name + " Predicting on all instances available.",1);
			log.dualOut("Prediction set instances: " + data.numInstances(),1);

			// Used to store predictions.
			StringBuilder predictions = new StringBuilder();

			// Produce an ARFF file header for output predictions.
			ProducePredictionsARFFHeader(predictionsPath,data.numAttributes());

			long startTime = System.nanoTime();

			// label instances
			for (int i = 0; i < data.numInstances(); i++) 
			{
				instanceNumber+=1;

				if(i%100000==0)
					log.dualOut("Classifying instance: "+i,1);

				double classification = learner.classifyInstance(data.instance(i));

				if(classification==1)// Predicted positive.
				{
					predictions.append(getFeatures(data.instance(i),data.numAttributes()-1)+"1\n");
					positiveClassifications++;
				}
				else// Predicted negative
				{
					predictions.append(getFeatures(data.instance(i),data.numAttributes()-1)+"0\n");
					negativeClassifications++;
				}

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

			log.dualOut("Predicting using "+name+" completed in "+duration+" (ns) or "+seconds+" (s)",1);
			log.dualOut("Predicted positive: "+positiveClassifications+" predicted negative "+negativeClassifications,1);

			return true;
		}
		catch (Exception e)
		{ 
			log.erroruf("Could not make predictions using " +name+ " classifier due to an error",e);
			return false; 
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifiers.I_WekaTest#reset()
	 */
	@Override
	public boolean reset()
	{
		log.dualOut("Resetting " + name,1);
		try
		{
			stats.reset();
			learner = null;
			learner = new J48();
			log.dualOut(name + " reset successfully",1);
			return true;
		}
		catch (Exception e) 
		{ 
			log.erroruf("Caught Exception resetting "+name+" learner",e);
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
			J48 cls = (J48) ois.readObject();
			ois.close();

			learner = cls;

			if(learner!=null)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{ 
			log.erroruf("could not save "+name+" model",e); 
			return false;
		}
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
