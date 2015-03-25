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
 * File name: 	Example.java
 * Package: (default)
 * Created:	March 24th, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */

import weka.classifiers.trees.J48;
import moa.classifiers.trees.HoeffdingTree;
import cs.man.ac.uk.classifiers.standard.StandardAlgorithmTester;
import cs.man.ac.uk.classifiers.stream.StreamAlgorithmTester;
import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.format.FormatCM;
import cs.man.ac.uk.sample.Sampler;
import cs.man.ac.uk.stats.ClassifierStatistics;
/**
 * Demonstrates how to use the code.
 * 
 * @author Rob Lyon
 */
public class Example 
{
	/**
	 * Simple main method that executes two tests: one using a stream classifier,
	 * another using a standard static classifier.
	 * 
	 * @param args unused arguments.
	 */
	public static void main(String[] args) 
	{
		testOne();
		testTwo();
		testThree();
	}

	/**
	 * Builds and tests a single stream classifier, in a static learning
	 * scenario
	 */
	private static void testOne()
	{
		// Input variables.
		String outputPath  = "/home/rob/workspace/Stuffed/Test_One.txt";
		String predictPath = "/home/rob/workspace/Stuffed/TestOne_Predict.arff";
		String trainingSet = "/home/rob/workspace/Stuffed/data/MiniBoone.arff";
		String testSet     = "/home/rob/workspace/Stuffed/data/MiniBoone.arff"; // Use same data just for these examples!
		boolean verbose    = true;
		boolean recordMissclassifications = true;
		
		// Clean up earlier files
		Common.fileDelete(outputPath);
		
		System.out.println("\n+----------------------------------------+");
        System.out.println("+                TEST ONE                +");
        System.out.println("+----------------------------------------+\n");
        
		StreamAlgorithmTester sat = new StreamAlgorithmTester(outputPath,"Hoeffding Tree",verbose,new HoeffdingTree());

		sat.train(trainingSet);
		
		int[][] confusionMatrix = sat.testStatic(testSet, outputPath);
		
		FormatCM.printConfusionMatrix(confusionMatrix);
		
		// Collect stats.
		ClassifierStatistics stats_1 = new ClassifierStatistics();
		stats_1.setConfusionMatrix(confusionMatrix);
		stats_1.calculate();
		
		System.out.println(stats_1.toString());
		
		// Standard static test, miss-classifications recorded in an ARFF file.
		confusionMatrix = sat.testStatic(testSet, outputPath, recordMissclassifications);
		FormatCM.printConfusionMatrix(confusionMatrix);
		
		// Make predictions over a set of examples, produces an output file.
		sat.predict(testSet, outputPath, predictPath);
		
		System.out.println("\n\n+---------- END TEST ONE ----------+\n\n");
		
	}
	
	/**
	 * Builds and tests a single standard classifier.
	 */
	private static void testTwo()
	{
		// Input variables.
		String outputPath  = "/home/rob/workspace/Stuffed/Test_Two.txt";
		String predictPath = "/home/rob/workspace/Stuffed/TestTwo_Predict.arff";
		String trainingSet = "/home/rob/workspace/Stuffed/data/MiniBoone.arff";
		String testSet     = "/home/rob/workspace/Stuffed/data/MiniBoone.arff";
		boolean verbose    = true;
		boolean recordMissclassifications = true;
		
		// Clean up earlier files
		Common.fileDelete(outputPath);
		
		System.out.println("\n+----------------------------------------+");
        System.out.println("+                 TEST TWO               +");
        System.out.println("+----------------------------------------+\n");
        
		StandardAlgorithmTester sat = new StandardAlgorithmTester(outputPath,"J48",verbose,new J48());
		
		sat.train(trainingSet);
		
		int[][] confusionMatrix = sat.testStatic(testSet, outputPath);
		
		// Shows how to print this out.
		FormatCM.printConfusionMatrix(confusionMatrix);
		
		// Collect stats.
		ClassifierStatistics stats_2 = new ClassifierStatistics();
		stats_2.setConfusionMatrix(confusionMatrix);
		stats_2.calculate();
		System.out.println(stats_2.toString());
		
		// Standard static test, miss-classifications recorded in an ARFF file
		confusionMatrix = sat.testStatic(testSet, outputPath, recordMissclassifications);
		FormatCM.printConfusionMatrix(confusionMatrix);
		
		// Make predictions over a set of examples, produces an output file.
		sat.predict(testSet, outputPath, predictPath);
		
		System.out.println("\n\n+---------- END TEST TWO ----------+\n\n");
	}
	
	/**
	 * Builds and tests a single stream classifier, in a stream learning
	 * scenario. This involves sampling a data set to produce a stream
	 * analogue with varying proportions of labelled data.
	 */
	private static void testThree()
	{
		// Input variables.
		String outputPath   = "/home/rob/workspace/Stuffed/Test_Three.txt";
		String dataPath     = "/home/rob/workspace/Stuffed/data/MiniBoone.arff";
		
		// Destination training and test files.
		String testSetPath  = "/home/rob/workspace/Stuffed/data/test.arff";
		String trainSetPath = "/home/rob/workspace/Stuffed/data/train.arff";
		
		boolean verbose     = true;
		
		// Clean up earlier files
		Common.fileDelete(outputPath);
		Common.fileDelete(testSetPath);
		Common.fileDelete(trainSetPath);
		
		
		// Sampling variables:
		int pos = 36499;  // Positives in the data file.
        int neg = 93565; // Negatives in the data file.
        
        // The desired training set distribution - minimum of one example per class.
        // This can be changed in the code if you prefer.
        int negTrainSamples = 1;
        int posTrainSamples = 1;
        
        // Desired training set balance.
        double trainSetBalance = 1.0;  // Equal training set balance.
        
        // The proportion of the test set we would like labelled.
        double labelling       = 0.1; // 10% of test data labelled.
        double testSetBalance  = ((double)pos - (double)posTrainSamples) / ((double)neg - (double)negTrainSamples);
        
        // Build sampler.
        Sampler s = new Sampler(outputPath,false);
        s.load(dataPath, -1); // Read in the file to be sampled.
        
        // Perform sampling, collect result information.
        Object[] output = s.sampleToARFF(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);
        
        // Process outcome of sampling operation.
        boolean result = (Boolean) output[0];
        String outcome = (String) output[1];
        
        // If sampling fails...
        if(!result)
        {
        	System.out.println("Error in sampling:");
        	System.out.println(outcome);
        	System.exit(0);
        }
        
        System.out.println("\n+----------------------------------------+");
        System.out.println("+               TEST THREE               +");
        System.out.println("+----------------------------------------+\n");
        
        // Else sampling succeeded, continue to run test.
		StreamAlgorithmTester sat = new StreamAlgorithmTester(outputPath,"Hoeffding Tree",verbose,new HoeffdingTree());

		sat.train(trainSetPath);
		
		int[][] confusionMatrix = sat.testStream(testSetPath, outputPath);
		
		FormatCM.printConfusionMatrix(confusionMatrix);
		
		// Collect stats
		ClassifierStatistics stats_3 = new ClassifierStatistics();
		stats_3.setConfusionMatrix(confusionMatrix);
		stats_3.calculate();
		
		System.out.println(stats_3.toString());
		
		System.out.println("\n\n+---------- END TEST THREE ----------+\n\n");
		
	}
}
