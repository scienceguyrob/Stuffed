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
 * File name: 	ClassifierStatisitics.java
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
 * This class is used to store the statistics gathered on a generated classifier.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class ClassifierStatistics implements I_ClassifierStatistics
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The accuracy of the map.
	 */
	private double accuracy = 0;

	/**
	 * The precision of the map. Precision is the fraction
	 * of retrieved instances that are relevant.
	 */
	private double precision = 0;

	/**
	 * The recall of the map. Recall is the fraction of relevant
	 * instances that are retrieved
	 */
	private double recall = 0;

	/**
	 * The precision of the map. Specificity relates to the ability
	 * of the map to identify negative results.
	 */
	private double specificity = 0;

	/**
	 *  The negative predictive value (NPV) is a summary statistic
	 *  defined as the proportion of input patterns identified as not pulsars,
	 *  that are correctly identified as such. A high NPV means that when the
	 *  map yields a negative result, it is most likely correct in its assessment.
	 */
	private double negativePredictiveValue = 0;

	/**
	 * The Matthews correlation coefficient is used in machine learning
	 * as a measure of the quality of binary (two-class) classifications.
	 * It takes into account true and false positives and negatives and
	 * is generally regarded as a balanced measure which can be used even
	 * if the classes are of very different sizes. The MCC is in essence a
	 * correlation coefficient between the observed and predicted binary
	 * classifications; it returns a value between -1 and +1. A coefficient
	 * of +1 represents a perfect prediction, 0 no better than random prediction
	 * and -1 indicates total disagreement between prediction and observation.
	 * The statistic is also known as the phi coefficient.
	 */
	private double matthewsCorrelation = 0;

	/**
	 * The F1 score (also F-score or F-measure) is a measure of a test's accuracy.
	 * It considers both the precision p and the recall r of the test to compute
	 * the score: p is the number of correct results divided by the number of all
	 * returned results and r is the number of correct results divided by the
	 * number of results that should have been returned.
	 */
	private double fScore = 0;

	/**
	 * The true positives.
	 */
	private double TP = 0;

	/**
	 * The true negatives.
	 */
	private double TN = 0;

	/**
	 * The false positives.
	 */
	private double FP = 0;

	/**
	 * The false negatives.
	 */
	private double FN = 0;

	/**
	 * The kappa statistic.
	 */
	private double kappa = 0.0;

	/**
	 * The G-Mean.
	 */
	private double gmean= 0.0;

	//*****************************************
	//*****************************************
	//         Getters & Setters
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getAccuracy()
	 */   
	public double getAccuracy() { return accuracy; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setAccuracy(double)
	 */   
	public void setAccuracy(double accuracy) { this.accuracy = accuracy; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getPrecision()
	 */   
	public double getPrecision() { return precision; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setPrecision(double)
	 */
	public void setPrecision(double precision) { this.precision = precision; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getRecall()
	 */  
	public double getRecall() { return recall; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setRecall(double)
	 */ 
	public void setRecall(double recall) { this.recall = recall; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getSpecificity()
	 */
	public double getSpecificity() { return specificity; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setSpecificity(double)
	 */
	public void setSpecificity(double specificity) { this.specificity = specificity; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getMatthewsCorrelation()
	 */
	public double getMatthewsCorrelation() { return matthewsCorrelation; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setMatthewsCorrelation(double)
	 */
	public void setMatthewsCorrelation(double matthewsCorrelation) { this.matthewsCorrelation = matthewsCorrelation; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getfScore()
	 */  
	public double getfScore() { return fScore; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setFScore(double)
	 */
	public void setFScore(double fScore) { this.fScore = fScore; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getNegativePredictiveValue()
	 */  
	public double getNegativePredictiveValue() { return negativePredictiveValue; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setNegativePredictiveValue(double)
	 */    
	public void setNegativePredictiveValue(double d){ negativePredictiveValue = d; }

	/* (non-Javadoc)
	 * @see com.scienceguyrob.data.interfaces.I_ClassifierStatistics#getKappa()
	 */
	public double getKappa(){ return this.kappa;}

	/* (non-Javadoc)
	 * @see com.scienceguyrob.data.interfaces.I_ClassifierStatistics#getGMean()
	 */
	public double getGMean(){ return this.gmean; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getTP()
	 */  
	public double getTP() { return TP; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setTP(double)
	 */
	public void setTP(double v) { this.TP = v; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getTN()
	 */
	public double getTN() { return TN; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setTN(double)
	 */   
	public void setTN(double v) { this.TN = v; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getFP()
	 */    
	public double getFP() { return FP; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setFP(double)
	 */   
	public void setFP(double v) { this.FP = v; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#getFN()
	 */  
	public double getFN() { return FN; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setFN(double)
	 */  
	public void setFN(double v) { this.FN = v; }

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 */
	public ClassifierStatistics(){}

	/**
	 * Primary constructor.
	 * @param tp the true positives.
	 * @param tn the true negatives.
	 * @param fp the false positives.
	 * @param fn the false negatives.
	 */
	public ClassifierStatistics(double tp,double tn,double fp,double fn)
	{
		this.TP = tp;
		this.TN = tn;
		this.FP = fp;
		this.FN = fn;
	}

	//*****************************************
	//*****************************************
	//               Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#calculate()
	 */  
	public void calculate()
	{
		accuracy = (TP + TN) / (TP + FP + FN + TN);
		//accuracy = accuracy * 100; // present as %

		precision = (TP) / (TP+FP);

		recall = (TP) / (TP + FN);

		specificity = (TN) / (FP+TN);

		negativePredictiveValue = (TN) / (FN + TN);

		matthewsCorrelation = ((TP * TN) - (FP * FN)) / Math.sqrt((TP+FP) * (TP+FN) * (TN+FP) * (TN+FN));

		fScore = 2 * ((precision * recall) / (precision + recall));

		/**
		 * Kappa = (totalAccuracy - randomAccuracy) / (1 - randomAccuracy)
		 * 
		 * where,
		 * 
		 * totalAccuracy = (TP + TN) / (TP + TN + FP + FN)
		 * 
		 * and 
		 * 
		 * randomAccuracy = (TN + FP) * (TN + FN) + (FN + TP) * (FP + TP) / (Total*Total).
		 */
		double total     = TP+TN+FP+FN;
		double totalAcc  = (TP + TN) / (TP + TN + FP + FN);
		double randomAcc =  (((TN + FP) * (TN + FN)) + ((FN + TP) * (FP + TP))) / (total*total);
		kappa = (totalAcc - randomAcc) / (1 - randomAcc);

		gmean = Math.sqrt( ( TP /( TP + FN ) ) * ( TN / ( TN + FP ) ) );
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#setConfusionMatrix()
	 */
	public void setConfusionMatrix(int[][] cm) 
	{
		this.TN = cm[0][0];
		this.FN = cm[0][1];
		this.FP = cm[1][0];
		this.TP = cm[1][1];
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#incrementTP()
	 */
	public void incrementTP() { this.TP++; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#incrementTN()
	 */
	public void incrementTN() { this.TN++; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#incrementFP()
	 */
	public void incrementFP() { this.FP++; }

	/* (non-Javadoc)
	 * @see uk.ac.man.jb.pct.data.I_ClassifierStatistics#incrementFN()
	 */
	public void incrementFN() { this.FN++; }

	/* (non-Javadoc)
	 * @see com.scienceguyrob.data.interfaces.I_ClassifierStatistics#reset()
	 */
	public void reset()
	{
		this.setTP(0);
		this.setTN(0);
		this.setFP(0);
		this.setFN(0);

		this.setAccuracy(0);
		this.setPrecision(0);
		this.setRecall(0);
		this.setSpecificity(0);
		this.setNegativePredictiveValue(0);
		this.setMatthewsCorrelation(0);
		this.setFScore(0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String output = String.format("%-14s", "\n\t+----- PERFORMANCE -----+") +"\n" +
				String.format("%-16s", "\tTN:") + TN + "\n" +
				String.format("%-16s", "\tFN:") + FN + "\n" +
				String.format("%-16s", "\tFP:") + FP + "\n" +
				String.format("%-16s", "\tTP:") + TP + "\n" +
				String.format("%-16s", "\tAccuracy:") + (accuracy * 100) + "\n" +
				String.format("%-16s", "\tPrecision:") + (precision * 100) + "\n" +
				String.format("%-16s", "\tRecall:") + (recall * 100) + "\n" +
				String.format("%-16s", "\tSpecificity:") + (specificity * 100) + "\n" +
				String.format("%-16s", "\tNPV:") + (negativePredictiveValue * 100) + "\n" +
				String.format("%-16s", "\tMCC:") + matthewsCorrelation + "\n" +
				String.format("%-16s", "\tF-Score:") + fScore + "\n" +
				String.format("%-16s", "\tKappa:") + kappa + "\n" +
				String.format("%-16s", "\tG-Mean:") + gmean + "\n";
		
		return output;
	}

	/* (non-Javadoc)
	 * @see com.scienceguyrob.data.interfaces.I_ClassifierStatistics#toCSV()
	 */
	public String toCSV()
	{
		return this.TN + "," + this.FN + "," + this.FP + "," + this.TP + "," + this.accuracy + "," + this.precision + "," + this.recall +","+
				this.specificity + "," + this.negativePredictiveValue + "," + this.matthewsCorrelation + "," + this.fScore + "," + this.kappa + "," + this.gmean;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.stats.I_ClassifierStatistics#toConfusionMatrix()
	 */
	@Override
	public int[][] toConfusionMatrix() 
	{
		int[][] cm = {{(int)this.TN,(int)this.FN},{(int)this.FP,(int)this.TP}};
		
		return cm;
	}
	
}
