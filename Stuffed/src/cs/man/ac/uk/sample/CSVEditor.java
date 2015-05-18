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
 * File name: 	CSVEditor.java
 * Package: cs.man.ac.uk.sample
 * Created:	Jun 2, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import cs.man.ac.uk.common.Strings;
import cs.man.ac.uk.io.Writer;

/**
 * The class CSVEditor is used to sample and modify CSV files only.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 30/05/14
 */
public class CSVEditor extends BaseEditor
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	//*****************************************
	//*****************************************
	//            Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param file the file to be edited/sampled from.
	 */
	public CSVEditor(IFile file){ super(file); }

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseEditor#sampleToCSV(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public boolean sampleToCSV(String trainPath, String testPath, int negTrainSamples,
			int posTrainSamples, double trainingSetBalance, double testSetBalance, double labelling)
	{
		String trueClassPath = testPath.replace(".arff",".trueClass.csv");
		
		int N_tot = this.pfile.getClassDistribution()[0];
		int P_tot = this.pfile.getClassDistribution()[1];
		int MAX = P_tot + N_tot;

		this.pfile.log("Positive samples required in training set: "+posTrainSamples+"\n");
		this.pfile.log("Training set balance: "+trainingSetBalance+"\n");
		this.pfile.log("Test set balance: "+testSetBalance+"\n");
		this.pfile.log("Total positive instances: "+P_tot+"\n");
		this.pfile.log("Total negative instances: "+N_tot+"\n");

		// TRAINING SET

		this.pfile.log("Inferred Parameters -"+"\n");
		// K_train   = the ratio of positive to negative in the training set, i.e. K_train = P_train / N_train .
		double K_train = trainingSetBalance;
		this.pfile.log("K_train = " + K_train + "\t- the ratio of positive to negative in the training set.\n");

		// P_train   = the number of positives we want in the training set.
		int P_train = posTrainSamples;
		this.pfile.log("P_train = " + P_train + "\t- the number of positives we want in the training set.\n");

		// N_train   = the number of negatives required in the training set, i.e. N_train = P_train / K_train .
		int N_train = (int) ( (double) P_train / K_train);
		if(N_train==0)
			N_train=negTrainSamples;
		this.pfile.log("N_train = " + N_train + "\t- the number of negatives required in the training set.\n");

		// MAX_train = the maximum allowable size of the training set, i.e. MAX_train = ( P_train / K_train ) + P_train .
		int MAX_train = N_train + P_train;
		this.pfile.log("MAX_train = " + MAX_train  + "\t- maximum allowable size of the training set.\n");

		// TEST SET

		// K_test    = the ratio of positive to negative in the test set, i.e. K_test = P_test / N_test
		double K_test = testSetBalance;
		this.pfile.log("K_test = " + K_test + "\t- the ratio of positive to negative in the test set.\n");
		// P_test    = the number of positives in the test set, P_test = P_tot - P_train .
		int P_test = P_tot - P_train;
		this.pfile.log("P_test = " + P_test + "\t- the number of positives in the test set.\n");

		// N_test    = the number of negatives required in the test set, i.e. N_test = P_test / K_test .
		int N_test = (int) ( (double) P_test / (double)K_test);
		this.pfile.log("N_test = " + N_test + "\t- the number of negatives required in the test set.\n");

		if(N_test > (N_tot-N_train)) // Not enough negatives to maintain the balance in the test set.
		{
			N_test = N_tot - N_train;
			this.pfile.log("N_test (MOD) = " + N_test + "\n");
			P_test = (int)((double) N_test * (double) K_test);
			this.pfile.log("P_test (MOD) = " + P_test + "\n");
		}

		// MAX_test = the maximum allowable size of the test set, i.e. MAX_test = ( P_test / K_test ) + P_test .
		int MAX_test = P_test + N_test;
		this.pfile.log("MAX_test = " + MAX_test + "\t- the maximum allowable size of the test set.\n");

		int skipablePositive = (P_tot - P_train) - P_test;
		this.pfile.log("skipable positives = " + skipablePositive+ "\t- the number of positives that can be omitted.\n");
		int skipableNegative = (N_tot - N_train) - N_test;
		this.pfile.log("skipable negative = " + skipableNegative+ "\t- the number of negatives that can be omitted.\n");


		// Now prepare training and test data set:
		String trainingSetPath = trainPath;
		String testSetPath =  testPath;

		// Labeling variables
		int INSTANCES_TO_LABEL = (int)((double)(P_test+N_test)*(double)labelling);
		int INSTANCES_TO_NOT_LABEL = (P_test+N_test) - INSTANCES_TO_LABEL;
		int labelled = 1;
		int unlabelled = 1;

		this.pfile.log("Label ratio: " + labelling+"\n");
		this.pfile.log("Instances to not label: " + INSTANCES_TO_NOT_LABEL+"\n");
		this.pfile.log("Instances to label: " + INSTANCES_TO_LABEL+"\n");

		int negInTrainingSet=0;
		int negInTestSet=0;

		int posInTrainingSet=0;
		int posInTestSet=0;

		int testSetCount=0;
		int trainingSetCount=0;

		int instanceIndex=0;
		int writeCounter = 0;
		String testSetString = "";

		/* 
		 * Imagine we have the following ARFF file to sample:
		 * 
		 * % Title : Original Data Set.
		 * @RELATION Original
		 * @ATTRIBUTE a	NUMERIC
		 * @ATTRIBUTE b	NUMERIC
		 * @ATTRIBUTE c	NUMERIC
		 * @ATTRIBUTE class		{0,1}
		 * @DATA
		 * 13 , 10 , 2 , 1
		 * 24 , 12 , 2 , 0
		 * 61 , 21 , 3 , 1
		 * 18 , 12 , 2 , 0
		 * 26 , 11 , 2 , 1
		 * 
		 * Before the sampling can be done correctly, the following must have been done.
		 * 
		 * 1. createMetaDataBinary() must have been run on the original ARFF data file,
		 *    so that we have two new ARFF meta data files:
		 *
		 *    1. <Original File Name>.positive.arff
		 *    2. <Original File Name>.negative.arff
		 *    
		 *    These files contain the labelled positive and negative examples, along with
		 *    row indexes of their positions in the original ARFF file.
		 *
		 * If the two files mentioned have been created, then to build our sample, what we
		 * need to do is find a way to randomly choose the labelled instances from these files. 
		 * 
		 * To do this we must read in the information from these two meta data files (.positive.arff
		 * and .negative.arff). We do this by calling the getMetaData() method. This method returns
		 * a hash map data structure containing an abstraction of this information. Given the original
		 * ARFF shown above, the .positive.arff version of it would be as follows:
		 * 
		 * % Title : Positives.
		 * @RELATION Original
		 * @ATTRIBUTE index NUMERIC
		 * @ATTRIBUTE a		NUMERIC
		 * @ATTRIBUTE b		NUMERIC
		 * @ATTRIBUTE c		NUMERIC
		 * @ATTRIBUTE class		{0,1}
		 * @DATA
		 * 1 , 13 , 10 , 2 , 1
		 * 3 , 61 , 21 , 3 , 1
		 * 5 , 26 , 11 , 2 , 1
		 * 
		 * By calling the getMetaData() method on this file, we obtain the following hash map:
		 * 
		 * --------------------
		 * | KEY	|	VALUE |
		 * --------------------
		 * | 1		|	  1	  |
		 * | 2		|	  3	  |
		 * | 3		|	  5	  |
		 * --------------------	
		 * 
		 * The key is simply a count of the instances in the file, and the value is the row index
		 * of the positive instance in the original ARFF file.
		 * 
		 * So now where know where these positive and negative instances are located in the
		 * original ARFF file.
		 */

		String extension = pfile.getExtension();
		TreeMap<Integer,Integer> positives = getMetaData(this.pfile.getPath().replace(extension, ".positive.meta"));
		TreeMap<Integer,Integer> negs = getMetaData(this.pfile.getPath().replace(extension, ".negative.meta"));

		this.pfile.log("Obtained Meta data\n");
		/*
		 * The next step is to choose which of these instances will end up in our sample.
		 * We create a bit set like implementation to keep track of this. We create a new
		 * hash map data structure where the key is the row index of the instance, and the
		 * value is a boolean flag (i.e. true = chosen to be in the sample, false = omit).
		 * 
		 * To begin with this hash map looks like the following:
		 * 
		 * --------------------
		 * | KEY	|	VALUE |
		 * --------------------
		 * | 1		|	False |
		 * | 3		|	False |
		 * | 5		|	False |
		 * --------------------	
		 */

		TreeMap<Integer,Boolean> negatives = new TreeMap<Integer, Boolean>();
		TreeMap<Integer,Boolean> allpositives = new TreeMap<Integer, Boolean>();

		Iterator<Entry<Integer, Integer>> it = negs.entrySet().iterator();	
		while (it.hasNext()) 
		{
			Map.Entry<Integer,Integer> pairs = (Map.Entry<Integer,Integer>)it.next();
			Integer value = pairs.getValue();
			negatives.put(value, false);
		}
		negs.clear();// No longer needed.

		Iterator<Entry<Integer, Integer>> it2 = positives.entrySet().iterator();	
		while (it2.hasNext()) 
		{
			Map.Entry<Integer,Integer> pairs = (Map.Entry<Integer,Integer>)it2.next();
			Integer value = pairs.getValue();
			allpositives.put(value, true);
		}

		/*
		 * Now we declare further hash maps which will be populated with the chosen 
		 * instances. Some instances are chosen for the training set, some are
		 * chosen for the test set.
		 */

		TreeMap<Integer,Boolean> P_train_indexes = new TreeMap<Integer, Boolean>(); // Positives in training set.
		TreeMap<Integer,Boolean> N_train_indexes = new TreeMap<Integer, Boolean>(); // Negatives in training set.
		TreeMap<Integer,Boolean> P_test_indexes = new TreeMap<Integer, Boolean>();  // Positives in test set.
		TreeMap<Integer,Boolean> N_test_indexes = new TreeMap<Integer, Boolean>();  // Negatives in test set.

		Random r = new Random();

		// Now randomly select positives for training set.
		while (P_train_indexes.size() < P_train )
		{
			// Choose an random index.
			int randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// If this random correctly indexes a positive
			if(positives.get(randomIndex)!= null)
				while(P_train_indexes.containsKey(positives.get(randomIndex)))// If the chosen index has already been chosen
					randomIndex = r.nextInt(positives.size() - 1 + 1) + 1; // Keep choosing a new random index.

			// Add the row index of the positive example (row index in the original ARFF file)
			// to the set of positives to include in the training set sample.
			P_train_indexes.put(positives.get(randomIndex), true);
		}



		// Randomly select positives for test set.
		while (P_test_indexes.size() < P_test )
		{
			int randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// While the positive chosen has already been chosen for the test set,
			// OR while it has already be chosen for the training set.
			while(P_test_indexes.containsKey(positives.get(randomIndex)) 
					| P_train_indexes.containsKey(positives.get(randomIndex)))
				randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// Add the row index of the positive example (row index in the original ARFF file)
			// to the set of positives to include in the test set sample.
			P_test_indexes.put(positives.get(randomIndex), true);
		}

		// Randomly select negatives for training set.
		while (N_train_indexes.size() < N_train )
		{
			int randomIndex = r.nextInt(MAX - 1 + 1) + 1;

			// While the negative chosen has already been chosen for the training set,
			// and other checks.
			while(N_train_indexes.containsKey(randomIndex) |
					P_train_indexes.containsKey(randomIndex) |
					P_test_indexes.containsKey(randomIndex)  |
					allpositives.containsKey(randomIndex)) 
			{
				randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			}	

			// Add the row index of the negative example (row index in the original ARFF file)
			// to the set of negatives to include in the training set sample.
			N_train_indexes.put(randomIndex, false);	
		}

		// Randomly select negatives for test set.
		while (N_test_indexes.size() < N_test )
		{
			int randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			while(N_test_indexes.containsKey(randomIndex) |
					P_train_indexes.containsKey(randomIndex) |
					P_test_indexes.containsKey(randomIndex) |
					N_train_indexes.containsKey(randomIndex)|
					allpositives.containsKey(randomIndex))
			{
				randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			}	

			// Add the row index of the negative example (row index in the original ARFF file)
			// to the set of negatives to include in the test set sample.
			N_test_indexes.put(randomIndex, false);
		}

		/*
		 * Thus we have now chosen which positive and negative instances should be included.
		 */

		this.pfile.log("Completed random sampling... now parsing file.\n");

		// Try to create the file
		File file = new File(this.pfile.getPath());

		// If the file exists
		if(file.exists())
		{
			// Variables used to store the line of the being read
			// using the input stream, and an array list to store the input
			// patterns into.
			String line = "";

			// Read the file and display it line by line. 
			BufferedReader in = null;

			try
			{
				// Open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					// While there are more lines to read and we haven't finished adding training/test examples.
					while ((line = in.readLine()) != null && ( trainingSetCount< MAX_train) | ( testSetCount < MAX_test))
					{
						if(line.startsWith("%") | line.startsWith(" "))// Ignore these		
							continue;
						else if(line.toUpperCase().startsWith("@RELATION"))
							continue;
						else if(line.toUpperCase().startsWith("@ATTRIBUTE"))			
							continue;
						else if(line.toUpperCase().startsWith("@DATA"))
							continue;
						else if(!Strings.isNullOrEmptyString(line)) // The data.
						{
							instanceIndex+=1;
							String[] features = line.split(",");
							String clazz = features[this.pfile.getClassIndex()];

							@SuppressWarnings("unused")
							String classAlpha="-";
							if(clazz.endsWith("1"))
								classAlpha="+";

							String featuresString = features[0];

							for(int j=1;j< features.length-1;j++)
							{
								featuresString+=","+features[j];
							}

							String outputString = featuresString;


							if(P_train_indexes.containsKey(instanceIndex)) // Add positive to training set
							{
								posInTrainingSet +=1;
								trainingSetCount +=1;

								if(clazz.contains("0"))
									this.pfile.log("1. Have negative, expecting positive.");
								Writer.append(trainingSetPath, outputString + ","+clazz+"\n");

							}
							else if(N_train_indexes.containsKey(instanceIndex)) // Add negative to training set
							{
								negInTrainingSet +=1;
								trainingSetCount +=1;
								if(clazz.contains("1"))
									this.pfile.log("2. Have positive, expecting negative.");

								Writer.append(trainingSetPath, outputString+ ","+clazz+"\n");
							}
							else if(P_test_indexes.containsKey(instanceIndex)) // Add positive to test set
							{
								posInTestSet+=1;
								testSetCount+=1;
								writeCounter+=1;

								if(clazz.contains("0"))
									this.pfile.log("3. Have negative, expecting positive.");

								if(label(labelled, unlabelled, INSTANCES_TO_LABEL, INSTANCES_TO_NOT_LABEL, labelling))
								{
									testSetString+= outputString+ ","+clazz+"\n";
									labelled+=1;
								}
								else
								{
									//testSetString+=outputString+ ",?%"+clazz+"\n"; //Uncomment to include true label
									testSetString+=outputString+ ",?\n";
									unlabelled+=1;
								}

								//Finally add this meta data to a file.
								Writer.append(trueClassPath,testSetCount+"\n");
							}
							else if(N_test_indexes.containsKey(instanceIndex)) // Add negative to test set
							{
								negInTestSet+=1;
								testSetCount+=1;
								writeCounter+=1;

								if(clazz.contains("1"))
									this.pfile.log("4. Have positive, expecting negative.");

								if(label(labelled, unlabelled, INSTANCES_TO_LABEL, INSTANCES_TO_NOT_LABEL, labelling))
								{
									testSetString+= outputString+ ","+clazz+"\n";
									labelled+=1;
								}
								else
								{
									//testSetString+=outputString+ ",?%"+clazz+"\n"; //Uncomment to include true label
									testSetString+=outputString+ ",?\n";
									unlabelled+=1;
								}

								//Finally add this meta data to a file if it is a DEFINATE NEGATIVE.
								//if(negatives.containsKey(instanceIndex))
								//Writer.append(trueClassPath,"0\n");
							}
						}

						if(writeCounter == 50)
						{
							Writer.append(testSetPath, testSetString);
							writeCounter=0;
							testSetString="";
						}
					}

					if(writeCounter>0)
						Writer.append(testSetPath, testSetString);

					this.pfile.log("Patterns written: "+ (trainingSetCount+testSetCount) + "\n");
					this.pfile.log("Labelled: "+ (labelled-1)+ "\n");
					this.pfile.log("Unlabelled: "+ (unlabelled-1)+ "\n");
					this.pfile.log("+ in training set: "+ posInTrainingSet+ "\n");
					this.pfile.log("- in training set: "+ negInTrainingSet+ "\n");
					this.pfile.log("+ in test set: "+ posInTestSet+ "\n");
					this.pfile.log("- in test set: "+ negInTestSet+ "\n");
					this.pfile.log("Training set patterns: "+ trainingSetCount+ "\n");
					this.pfile.log("Test set patterns: "+ testSetCount+ "\n");
					this.pfile.log("Completed Sampling\n");
					
					return true;
				}
				catch(IOException e){this.pfile.log(e.toString());return false;}
				finally{in.close();}
			}
			catch (Exception e) {this.pfile.log(e.toString()); return false; }
		}
		else{ return false; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseEditor#sampleToARFF(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public boolean sampleToARFF(String trainPath, String testPath, int negTrainSamples,
			int posTrainSamples, double trainingSetBalance, double testSetBalance, double labelling)
	{
		String trueClassPath = testPath.replace(".arff",".trueClass.csv");
		
		int N_tot = this.pfile.getClassDistribution()[0];
		int P_tot = this.pfile.getClassDistribution()[1];
		int MAX = P_tot + N_tot;

		this.pfile.log("Positive samples required in training set: "+posTrainSamples+"\n");
		this.pfile.log("Training set balance: "+trainingSetBalance+"\n");
		this.pfile.log("Test set balance: "+testSetBalance+"\n");
		this.pfile.log("Total positive instances: "+P_tot+"\n");
		this.pfile.log("Total negative instances: "+N_tot+"\n");

		// TRAINING SET

		this.pfile.log("Inferred Parameters -"+"\n");
		// K_train   = the ratio of positive to negative in the training set, i.e. K_train = P_train / N_train .
		double K_train = trainingSetBalance;
		this.pfile.log("K_train = " + K_train + "\t- the ratio of positive to negative in the training set.\n");

		// P_train   = the number of positives we want in the training set.
		int P_train = posTrainSamples;
		this.pfile.log("P_train = " + P_train + "\t- the number of positives we want in the training set.\n");

		// N_train   = the number of negatives required in the training set, i.e. N_train = P_train / K_train .
		int N_train = (int) ( (double) P_train / K_train);
		if(N_train==0)
			N_train=negTrainSamples;
		this.pfile.log("N_train = " + N_train + "\t- the number of negatives required in the training set.\n");

		// MAX_train = the maximum allowable size of the training set, i.e. MAX_train = ( P_train / K_train ) + P_train .
		int MAX_train = N_train + P_train;
		this.pfile.log("MAX_train = " + MAX_train  + "\t- maximum allowable size of the training set.\n");

		// TEST SET

		// K_test    = the ratio of positive to negative in the test set, i.e. K_test = P_test / N_test
		double K_test = testSetBalance;
		this.pfile.log("K_test = " + K_test + "\t- the ratio of positive to negative in the test set.\n");
		// P_test    = the number of positives in the test set, P_test = P_tot - P_train .
		int P_test = P_tot - P_train;
		this.pfile.log("P_test = " + P_test + "\t- the number of positives in the test set.\n");

		// N_test    = the number of negatives required in the test set, i.e. N_test = P_test / K_test .
		int N_test = (int) ( (double) P_test / (double)K_test);
		this.pfile.log("N_test = " + N_test + "\t- the number of negatives required in the test set.\n");

		if(N_test > (N_tot-N_train)) // Not enough negatives to maintain the balance in the test set.
		{
			N_test = N_tot - N_train;
			this.pfile.log("N_test (MOD) = " + N_test + "\n");
			P_test = (int)((double) N_test * (double) K_test);
			this.pfile.log("P_test (MOD) = " + P_test + "\n");
		}

		// MAX_test = the maximum allowable size of the test set, i.e. MAX_test = ( P_test / K_test ) + P_test .
		int MAX_test = P_test + N_test;
		this.pfile.log("MAX_test = " + MAX_test + "\t- the maximum allowable size of the test set.\n");

		int skipablePositive = (P_tot - P_train) - P_test;
		this.pfile.log("skipable positives = " + skipablePositive+ "\t- the number of positives that can be omitted.\n");
		int skipableNegative = (N_tot - N_train) - N_test;
		this.pfile.log("skipable negative = " + skipableNegative+ "\t- the number of negatives that can be omitted.\n");


		// Now prepare training and test data set:
		String trainingSetPath = trainPath;
		String testSetPath =  testPath;

		writeARFFHeader(trainingSetPath,"","" ,"A training set.");		
		writeARFFHeader(testSetPath,"","","A test set.");
		
		// Labeling variables
		int INSTANCES_TO_LABEL = (int)((double)(P_test+N_test)*(double)labelling);
		int INSTANCES_TO_NOT_LABEL = (P_test+N_test) - INSTANCES_TO_LABEL;
		int labelled = 1;
		int unlabelled = 1;

		this.pfile.log("Label ratio: " + labelling+"\n");
		this.pfile.log("Instances to not label: " + INSTANCES_TO_NOT_LABEL+"\n");
		this.pfile.log("Instances to label: " + INSTANCES_TO_LABEL+"\n");

		int negInTrainingSet=0;
		int negInTestSet=0;

		int posInTrainingSet=0;
		int posInTestSet=0;

		int testSetCount=0;
		int trainingSetCount=0;

		int instanceIndex=0;
		int writeCounter = 0;
		String testSetString = "";

		/* 
		 * Imagine we have the following ARFF file to sample:
		 * 
		 * % Title : Original Data Set.
		 * @RELATION Original
		 * @ATTRIBUTE a	NUMERIC
		 * @ATTRIBUTE b	NUMERIC
		 * @ATTRIBUTE c	NUMERIC
		 * @ATTRIBUTE class		{0,1}
		 * @DATA
		 * 13 , 10 , 2 , 1
		 * 24 , 12 , 2 , 0
		 * 61 , 21 , 3 , 1
		 * 18 , 12 , 2 , 0
		 * 26 , 11 , 2 , 1
		 * 
		 * Before the sampling can be done correctly, the following must have been done.
		 * 
		 * 1. createMetaDataBinary() must have been run on the original ARFF data file,
		 *    so that we have two new ARFF meta data files:
		 *
		 *    1. <Original File Name>.positive.arff
		 *    2. <Original File Name>.negative.arff
		 *    
		 *    These files contain the labelled positive and negative examples, along with
		 *    row indexes of their positions in the original ARFF file.
		 *
		 * If the two files mentioned have been created, then to build our sample, what we
		 * need to do is find a way to randomly choose the labelled instances from these files. 
		 * 
		 * To do this we must read in the information from these two meta data files (.positive.arff
		 * and .negative.arff). We do this by calling the getMetaData() method. This method returns
		 * a hash map data structure containing an abstraction of this information. Given the original
		 * ARFF shown above, the .positive.arff version of it would be as follows:
		 * 
		 * % Title : Positives.
		 * @RELATION Original
		 * @ATTRIBUTE index NUMERIC
		 * @ATTRIBUTE a		NUMERIC
		 * @ATTRIBUTE b		NUMERIC
		 * @ATTRIBUTE c		NUMERIC
		 * @ATTRIBUTE class		{0,1}
		 * @DATA
		 * 1 , 13 , 10 , 2 , 1
		 * 3 , 61 , 21 , 3 , 1
		 * 5 , 26 , 11 , 2 , 1
		 * 
		 * By calling the getMetaData() method on this file, we obtain the following hash map:
		 * 
		 * --------------------
		 * | KEY	|	VALUE |
		 * --------------------
		 * | 1		|	  1	  |
		 * | 2		|	  3	  |
		 * | 3		|	  5	  |
		 * --------------------	
		 * 
		 * The key is simply a count of the instances in the file, and the value is the row index
		 * of the positive instance in the original ARFF file.
		 * 
		 * So now where know where these positive and negative instances are located in the
		 * original ARFF file.
		 */

		String extension = pfile.getExtension();
		TreeMap<Integer,Integer> positives = getMetaData(this.pfile.getPath().replace(extension, ".positive.meta"));
		TreeMap<Integer,Integer> negs = getMetaData(this.pfile.getPath().replace(extension, ".negative.meta"));

		this.pfile.log("Obtained Meta data\n");
		/*
		 * The next step is to choose which of these instances will end up in our sample.
		 * We create a bit set like implementation to keep track of this. We create a new
		 * hash map data structure where the key is the row index of the instance, and the
		 * value is a boolean flag (i.e. true = chosen to be in the sample, false = omit).
		 * 
		 * To begin with this hash map looks like the following:
		 * 
		 * --------------------
		 * | KEY	|	VALUE |
		 * --------------------
		 * | 1		|	False |
		 * | 3		|	False |
		 * | 5		|	False |
		 * --------------------	
		 */

		TreeMap<Integer,Boolean> negatives = new TreeMap<Integer, Boolean>();
		TreeMap<Integer,Boolean> allpositives = new TreeMap<Integer, Boolean>();

		Iterator<Entry<Integer, Integer>> it = negs.entrySet().iterator();	
		while (it.hasNext()) 
		{
			Map.Entry<Integer,Integer> pairs = (Map.Entry<Integer,Integer>)it.next();
			Integer value = pairs.getValue();
			negatives.put(value, false);
		}
		negs.clear();// No longer needed.

		Iterator<Entry<Integer, Integer>> it2 = positives.entrySet().iterator();	
		while (it2.hasNext()) 
		{
			Map.Entry<Integer,Integer> pairs = (Map.Entry<Integer,Integer>)it2.next();
			Integer value = pairs.getValue();
			allpositives.put(value, true);
		}

		/*
		 * Now we declare further hash maps which will be populated with the chosen 
		 * instances. Some instances are chosen for the training set, some are
		 * chosen for the test set.
		 */

		TreeMap<Integer,Boolean> P_train_indexes = new TreeMap<Integer, Boolean>(); // Positives in training set.
		TreeMap<Integer,Boolean> N_train_indexes = new TreeMap<Integer, Boolean>(); // Negatives in training set.
		TreeMap<Integer,Boolean> P_test_indexes = new TreeMap<Integer, Boolean>();  // Positives in test set.
		TreeMap<Integer,Boolean> N_test_indexes = new TreeMap<Integer, Boolean>();  // Negatives in test set.

		Random r = new Random();

		// Now randomly select positives for training set.
		while (P_train_indexes.size() < P_train )
		{
			// Choose an random index.
			int randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// If this random correctly indexes a positive
			if(positives.get(randomIndex)!= null)
				while(P_train_indexes.containsKey(positives.get(randomIndex)))// If the chosen index has already been chosen
					randomIndex = r.nextInt(positives.size() - 1 + 1) + 1; // Keep choosing a new random index.

			// Add the row index of the positive example (row index in the original ARFF file)
			// to the set of positives to include in the training set sample.
			P_train_indexes.put(positives.get(randomIndex), true);
		}



		// Randomly select positives for test set.
		while (P_test_indexes.size() < P_test )
		{
			int randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// While the positive chosen has already been chosen for the test set,
			// OR while it has already be chosen for the training set.
			while(P_test_indexes.containsKey(positives.get(randomIndex)) 
					| P_train_indexes.containsKey(positives.get(randomIndex)))
				randomIndex = r.nextInt(positives.size() - 1 + 1) + 1;

			// Add the row index of the positive example (row index in the original ARFF file)
			// to the set of positives to include in the test set sample.
			P_test_indexes.put(positives.get(randomIndex), true);
		}

		// Randomly select negatives for training set.
		while (N_train_indexes.size() < N_train )
		{
			int randomIndex = r.nextInt(MAX - 1 + 1) + 1;

			// While the negative chosen has already been chosen for the training set,
			// and other checks.
			while(N_train_indexes.containsKey(randomIndex) |
					P_train_indexes.containsKey(randomIndex) |
					P_test_indexes.containsKey(randomIndex)  |
					allpositives.containsKey(randomIndex)) 
			{
				randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			}	

			// Add the row index of the negative example (row index in the original ARFF file)
			// to the set of negatives to include in the training set sample.
			N_train_indexes.put(randomIndex, false);	
		}

		// Randomly select negatives for test set.
		while (N_test_indexes.size() < N_test )
		{
			int randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			while(N_test_indexes.containsKey(randomIndex) |
					P_train_indexes.containsKey(randomIndex) |
					P_test_indexes.containsKey(randomIndex) |
					N_train_indexes.containsKey(randomIndex)|
					allpositives.containsKey(randomIndex))
			{
				randomIndex = r.nextInt(MAX - 1 + 1) + 1;
			}	

			// Add the row index of the negative example (row index in the original ARFF file)
			// to the set of negatives to include in the test set sample.
			N_test_indexes.put(randomIndex, false);
		}

		/*
		 * Thus we have now chosen which positive and negative instances should be included.
		 */

		this.pfile.log("Completed random sampling... now parsing file.\n");

		// Try to create the file
		File file = new File(this.pfile.getPath());

		// If the file exists
		if(file.exists())
		{
			// Variables used to store the line of the being read
			// using the input stream, and an array list to store the input
			// patterns into.
			String line = "";

			// Read the file and display it line by line. 
			BufferedReader in = null;

			try
			{
				// Open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					// While there are more lines to read and we haven't finished adding training/test examples.
					while ((line = in.readLine()) != null && ( trainingSetCount< MAX_train) | ( testSetCount < MAX_test))
					{
						if(line.startsWith("%") | line.startsWith(" "))// Ignore these		
							continue;
						else if(line.toUpperCase().startsWith("@RELATION"))
							continue;
						else if(line.toUpperCase().startsWith("@ATTRIBUTE"))			
							continue;
						else if(line.toUpperCase().startsWith("@DATA"))
							continue;
						else if(!Strings.isNullOrEmptyString(line)) // The data.
						{
							instanceIndex+=1;
							String[] features = line.split(",");
							String clazz = features[this.pfile.getClassIndex()];

							@SuppressWarnings("unused")
							String classAlpha="-";
							if(clazz.endsWith("1"))
								classAlpha="+";

							String featuresString = features[0];

							for(int j=1;j< features.length-1;j++)
							{
								featuresString+=","+features[j];
							}

							String outputString = featuresString;


							if(P_train_indexes.containsKey(instanceIndex)) // Add positive to training set
							{
								posInTrainingSet +=1;
								trainingSetCount +=1;

								if(clazz.contains("0"))
									this.pfile.log("1. Have negative, expecting positive.");
								Writer.append(trainingSetPath, outputString + ","+clazz+"\n");

							}
							else if(N_train_indexes.containsKey(instanceIndex)) // Add negative to training set
							{
								negInTrainingSet +=1;
								trainingSetCount +=1;
								if(clazz.contains("1"))
									this.pfile.log("2. Have positive, expecting negative.");

								Writer.append(trainingSetPath, outputString+ ","+clazz+"\n");
							}
							else if(P_test_indexes.containsKey(instanceIndex)) // Add positive to test set
							{
								posInTestSet+=1;
								testSetCount+=1;
								writeCounter+=1;

								if(clazz.contains("0"))
									this.pfile.log("3. Have negative, expecting positive.");

								if(label(labelled, unlabelled, INSTANCES_TO_LABEL, INSTANCES_TO_NOT_LABEL, labelling))
								{
									testSetString+= outputString+ ","+clazz+"\n";
									labelled+=1;
								}
								else
								{
									//testSetString+=outputString+ ",?%"+clazz+"\n"; //Uncomment to include true label
									testSetString+=outputString+ ",?\n";
									unlabelled+=1;
								}

								//Finally add this meta data to a file.
								Writer.append(trueClassPath,testSetCount+"\n");
							}
							else if(N_test_indexes.containsKey(instanceIndex)) // Add negative to test set
							{
								negInTestSet+=1;
								testSetCount+=1;
								writeCounter+=1;

								if(clazz.contains("1"))
									this.pfile.log("4. Have positive, expecting negative.");

								if(label(labelled, unlabelled, INSTANCES_TO_LABEL, INSTANCES_TO_NOT_LABEL, labelling))
								{
									testSetString+= outputString+ ","+clazz+"\n";
									labelled+=1;
								}
								else
								{
									//testSetString+=outputString+ ",?%"+clazz+"\n"; //Uncomment to include true label
									testSetString+=outputString+ ",?\n";
									unlabelled+=1;
								}

								//Finally add this meta data to a file if it is a DEFINATE NEGATIVE.
								//if(negatives.containsKey(instanceIndex))
								//Writer.append(trueClassPath,"0\n");
							}
						}

						if(writeCounter == 50)
						{
							Writer.append(testSetPath, testSetString);
							writeCounter=0;
							testSetString="";
						}
					}

					if(writeCounter>0)
						Writer.append(testSetPath, testSetString);

					this.pfile.log("Patterns written: "+ (trainingSetCount+testSetCount) + "\n");
					this.pfile.log("Labelled: "+ (labelled-1)+ "\n");
					this.pfile.log("Unlabelled: "+ (unlabelled-1)+ "\n");
					this.pfile.log("+ in training set: "+ posInTrainingSet+ "\n");
					this.pfile.log("- in training set: "+ negInTrainingSet+ "\n");
					this.pfile.log("+ in test set: "+ posInTestSet+ "\n");
					this.pfile.log("- in test set: "+ negInTestSet+ "\n");
					this.pfile.log("Training set patterns: "+ trainingSetCount+ "\n");
					this.pfile.log("Test set patterns: "+ testSetCount+ "\n");
					this.pfile.log("Completed Sampling\n");
					
					return true;
				}
				catch(IOException e){this.pfile.log(e.toString());return false;}
				finally{in.close();}
			}
			catch (Exception e) {this.pfile.log(e.toString()); return false; }
		}
		else{ return false; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseEditor#writeARFFHeader(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean writeARFFHeader(String destination, String title, String rel, String description)
	{
		// The header... it is made up of three parts:
		String header = "% Title : unknown\n"+
				"% Description : N/A\n\n";

		String relation = "@relation unknown\n\n";

		String attributes = "";
		int columnCount=pfile.getColumns();

		for(int i=0;i< columnCount-1;i++)
			attributes += "@attribute attrib"+(i+1)+" numeric\n";

		attributes += "@attribute class {0,1}\n";
		String data = "@data\n";
		// Write out the header
		Writer.append(destination, header);
		Writer.append(destination, relation);
		Writer.append(destination, attributes);
		return Writer.append(destination, data);
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseEditor#writeARFFHeader(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int[])
	 */
	@Override
	public boolean writeARFFHeader(String destination, String title, String rel,String description, int[] featureIndexes)
	{
		return false;//not needed.
	}
}
