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
 * File name: 	WekaClassifier.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.moawrappers;

import weka.core.Instance;
import cs.man.ac.uk.classifiers.BaseClassifier;
import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.io.Writer;
import cs.man.ac.uk.stats.ClassifierStatistics;
import cs.man.ac.uk.stats.I_ClassifierStatistics;
/**
 * The class defines the basic type of WEKA classifier.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class WekaClassifier extends BaseClassifier
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The path to the ARFF file containing a training set.
	 */
	protected String trainingSetFilePath = "";

	/**
	 * The path to the ARFF file containing a test set.
	 */
	protected String testSetFilePath = "";
	
	/**
	 * The path to the ARFF file containing data to be classified.
	 */
	protected String classificationFilePath = "";

	/**
	 * Stores the statistics describing the performance of the classifier.
	 */
	protected I_ClassifierStatistics stats = new ClassifierStatistics();

	/**
	 * The index of the class labels in the ARFF training and test files.
	 */
	protected int classIndex = -1;

	/**
	 * The class labels.
	 */
	protected String[] classLabels = null;

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
	public WekaClassifier(String pth,boolean v){ super(pth,v);}
	
	//*****************************************
	//*****************************************
	//           Getter & Setters
	//*****************************************
	//*****************************************

	/**
	 * @return the training set file path.
	 */
	public String getTrainingSetFilePath() { return trainingSetFilePath; }

	/**
	 * @param trainingSetFilePath the training set file path to set.
	 */
	public void setTrainingSetFilePath(String trainingSetFilePath) { this.trainingSetFilePath = trainingSetFilePath; }

	/**
	 * @return the test set file path.
	 */
	public String getTestSetFilePath() { return testSetFilePath; }

	/**
	 * @param testSetFilePath the test set file path to set.
	 */
	public void setTestSetFilePath(String testSetFilePath) { this.testSetFilePath = testSetFilePath; }
	
	/**
	 * @return the classification set file path.
	 */
	public String getClassificationFilePath() { return classificationFilePath; }

	/**
	 * @param classificationFilePath the classification set file path to set.
	 */
	public void setClassificationFilePath(String classificationFilePath) { this.classificationFilePath = classificationFilePath; }
	
	/**
	 * @return the statistics describing the performance of the classifier.
	 */
	public I_ClassifierStatistics getClassifierStatistics() { return this.stats; }

	/**
	 * @return the index of the class labels in the ARFF training and test files.
	 */
	public int getClassIndex(){ return this.classIndex; }

	/**
	 * Sets the index of the class labels in the ARFF training and test files.
	 * 
	 * @param index the index of the class labels in the ARFF files.
	 */
	public void setClassIndex(int index){ this.classIndex = index; }

	/**
	 * Sets the class labels to be expected by the classifier.
	 * @param l the labels to expect.
	 */
	public void setClasslabels(String[] l){ this.classLabels = l;}

	/**
	 * @return Gets the class labels to be expected by the classifier.
	 */
	public String[] getClasslabels(){ return this.classLabels; }

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/**
	 * Obtains the features (attributes) belonging to a instance within the ARFF
	 * file being classified. Returns these in a comma delimited string.
	 * @param instance the instance whose score values should be obtained.
	 * @param attributes number of feature attributes to expect.
	 * @return a comma delimited string containing the scores.
	 */
	public String getFeatures(Instance instance,int attributes)
	{
		String data="";
		for(int i=0;i<attributes;i++)
			data+=instance.value(i)+",";

		return data;
	}
	
	/**
	 * Records miss-classified instances in an ARFF file.
	 * @param path the path to the file to store miss-classified instances.
	 * @param missclassifications a string containing the miss-classified instances (assumed separated by newlines).
	 * @param attributes the number of attributes possessed by the data.
	 */
	public void missclassificationsToARFF(String path,String missclassifications,int attributes)
	{
		String header="@relation Missclassifications_"+Common.getCondensedTime("_")+"_"+Common.getDateWithSeperator("_")+"\n\n";
		
		for(int i=1; i<attributes;i++)
			header+="@attribute Score"+i+" numeric\n";
		
		header+="@attribute class {0,1}\n";
		header+="@attribute reason {FP,FN}\n";
		header+="@data\n";
		
		Writer.append(path, header);
		Writer.append(path, missclassifications);
	}
	
	/**
	 * Records predictions in an ARFF file.
	 * @param path the path to the file to store predictions.
	 * @param attributes the number of attributes possessed by the data.
	 */
	public void ProducePredictionsARFFHeader(String path,int attributes)
	{
		String header="@relation Predictions_"+Common.getCondensedTime("_")+"_"+Common.getDateWithSeperator("_")+"\n\n";
		
		for(int i=1; i<attributes;i++)
			header+="@attribute Score"+i+" numeric\n";
		
		header+="@attribute class {0,1}\n";
		header+="@data\n";
		
		Writer.append(path, header);
	}
}