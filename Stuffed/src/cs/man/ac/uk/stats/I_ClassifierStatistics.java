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
 * File name: 	I_ClassifierStatisitics.java
 * Package: cs.man.ac.uk.classifiers
 * Created:	May 2, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.stats;

/**
 * <p>I_ClassifierStatistics provides an interface for
 * providing the statistics needed to describe the accuracy
 * of a classifier. </p>
 * 
 * <p>The statistics are calculated using the true positive, true negative,
 * false positive and false negative counts. These are defined as:</p>
 * 
 * <p>True Positives (TP) = those input patterns correctly classified as positive.
 * True Negatives (TN) = those input patterns correctly classified as negative.
 * False Positives (FP) = those input patterns incorrectly classified as positive.
 * False Negatives (FN) = those input patterns incorrectly classified as negative.</p>
 * 
 * @author Rob Lyon
 * 
 * @version 1.0, 05/02/13
 */
public interface I_ClassifierStatistics
{
	/**
	 * @return the accuracy of the classifier. Accuracy is defined 
	 * as: (TP + TN) / (TP + FP + FN + TN)
	 */
	public abstract double getAccuracy();

	/**
	 * Sets the accuracy of the classifier. Accuracy is defined 
	 * as: (TP + TN) / (TP + FP + FN + TN)
	 * @param accuracy how accurate the classifier is.
	 */
	public abstract void setAccuracy(double accuracy);

	/**
	 * @return the precision of the classifier. Precision is defined
	 * as: (TP) / (TP+FP)
	 */
	public abstract double getPrecision();

	/**
	 * Sets the precision of the classifier. Precision is defined
	 * as: (TP) / (TP+FP)
	 * @param precision how precise the classifier is.
	 */
	public abstract void setPrecision(double precision);

	/**
	 * @return the recall (sensitivity) capabilities of the classifier. Recall is defined
	 * as: (TP) / (TP + FN)
	 */
	public abstract double getRecall();

	/**
	 * Sets the recall (sensitivity) of the classifier. Recall is defined
	 * as: (TP) / (TP + FN)
	 * @param recall how well the classifier can recall positive instance.
	 */
	public abstract void setRecall(double recall);

	/**
	 * @return the proportion of negative instances correctly classified. 
	 *         Defined as:  (TN) / (FP+TN)
	 */
	public abstract double getSpecificity();

	/**
	 * Sets the specificity of the classifier. This is the proportion of 
	 * negative instances correctly classified. Defined as:  (TN) / (FP+TN)
	 * @param specificity the proportion of negative instances correctly classified. 
	 */
	public abstract void setSpecificity(double specificity);

	/**
	 * @return the Matthews correlation coefficient.
	 */
	public abstract double getMatthewsCorrelation();

	/**
	 * Returns the Matthews correlation coefficient. This is used in machine learning
	 * as a measure of the quality of binary (two-class) classifications. It takes into
	 * account true and false positives and negatives and is generally regarded as a 
	 * balanced measure which can be used even if the classes are of very different sizes.
	 * The MCC is in essence a correlation coefficient between the observed and predicted
	 * binary classifications; it returns a value between -1 and +1. A coefficient of +1
	 * represents a perfect prediction, 0 no better than random prediction and -1 indicates
	 * total disagreement between prediction and observation. The statistic is also known
	 * as the phi coefficient.
	 * @param matthewsCorrelation the Matthews correlation coefficient.
	 */
	public abstract void setMatthewsCorrelation(double matthewsCorrelation);

	/**
	 * The F1 score (also F-score or F-measure) is a measure of a tests accuracy.
	 * It considers both the precision p and the recall r of the test to compute
	 * the score: p is the number of correct results divided by the number of all
	 * returned results and r is the number of correct results divided by the number
	 * of results that should have been returned.
	 * @return the F1 score
	 */
	public abstract double getfScore();

	/**
	 * Sets the F1 score (also F-score or F-measure) which is a measure of a tests accuracy.
	 * @param fScore the F1 score.
	 */
	public abstract void setFScore(double fScore);

	/**
	 * Gets the negative predictive value (NPV), which is a summary statistic defined
	 * as the proportion of input patterns identified as negative, that are correctly
	 * identified as such. A high NPV means that when the map yields a negative result,
	 * it is most likely correct in its assessment.
	 * @return the negative predictive value
	 */
	public abstract double getNegativePredictiveValue();

	/**
	 * Sets the negative predictive value (NPV), which is a summary statistic defined as
	 * the proportion of input patterns identified as negative, that are correctly
	 * identified as such. A high NPV means that when the map yields a negative result,
	 * it is most likely correct in its assessment.
	 * @param d the negative predictive value
	 */
	public abstract void setNegativePredictiveValue(double d);

	/**
	 * <p>Gets the kappa statistic where,</p>
	 * 
	 * <p>Kappa = (totalAccuracy - randomAccuracy) / (1 - randomAccuracy)</p>
	 * 
	 * where,
	 * 
	 * <p>totalAccuracy = (TP + TN) / (TP + TN + FP + FN)</p>
	 * 
	 * and 
	 * 
	 * <p>randomAccuracy = (TN + FP) * (TN + FN) + (FN + TP) * (FP + TP) / (Total*Total).</p>
	 * 
	 * @return the kappa statistic.
	 */
	public abstract double getKappa();

	/**
	 * Gets the G-mean statistic where,
	 * 
	 * G-Mean = SQRT( ( TP /( TP + FN ) ) * ( TN / ( TN + FP ) ) )
	 * @return the G-Mean.
	 */
	public abstract double getGMean();

	/**
	 * @return the number of true positives
	 */
	public abstract double getTP();

	/**
	 * Sets the number of true positives.
	 * @param v the number of true positives
	 */
	public abstract void setTP(double v);

	/**
	 * @return the number of true negatives.
	 */
	public abstract double getTN();

	/**
	 * Sets the number of true negatives.
	 * @param v the number of true negatives
	 */
	public abstract void setTN(double v);

	/**
	 * @return the number of false positives.
	 */
	public abstract double getFP();

	/**
	 * Sets the number of false positives.
	 * @param v the number of false positives
	 */
	public abstract void setFP(double v);

	/**
	 * @return the number of false negatives.
	 */
	public abstract double getFN();

	/**
	 * Sets the number of false negatives.
	 * @param v the number of false negatives.
	 */
	public abstract void setFN(double v);

	/**
	 * Causes this class to compute its statistics based on
	 * the number of true positives, true negatives, false positives
	 * and false negatives.
	 */
	public abstract void calculate();

	/**
	 * Sets the confusion matrix from which stats are computed.
	 * @param cm a simple confusion matrix output by a classifier.
	 */
	public abstract void setConfusionMatrix(int[][] cm);
	
	/**
	 * Increases the number of true positives by one.
	 */
	public void incrementTP();

	/**
	 * Increases the number of true negatives by one.
	 */
	public void incrementTN();

	/**
	 * Increases the number of false positives by one.
	 */
	public void incrementFP();

	/**
	 * Increases the number of false negatives by one.
	 */
	public void incrementFN();

	/**
	 * Resets the statistics.
	 */
	public void reset();

	/**
	 * @return the statistics held by this class in CSV format.
	 */
	public String toCSV();
	
	/**
	 * @return the statistics held by this class in a confusion matrix.
	 */
	public int[][] toConfusionMatrix();
}
