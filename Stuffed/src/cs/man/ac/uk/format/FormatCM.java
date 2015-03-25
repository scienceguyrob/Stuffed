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
 * File name: 	FormatCM.java
 * Package: cs.man.ac.uk.format
 * Created:	March 24th, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.format;

/**
 * Holds methods for formatting classifier confusion matrices for output.
 * @author Rob Lyon
 */
public class FormatCM 
{
	/**
	 *  <p>Prints the contents of a binary confusion matrix in terms of true positives, false positives,
	 *  true negatives and false negatives. Given a matrix of the form,</p>
     *       
     *             Actual Class
     *               -     +
     *        P    -----------
     *        r  - | TN | FN |    TN = Negatives correctly receiving negative label.
     *        e    |---------|    FN = Positives incorrectly receiving negative label.
     *        d  + | FP | TP |    FP = Negatives incorrectly receiving positive label.
     *        .    -----------    TP = Positives correctly receiving positive label.
     *       
     *        ^
     *        |
     *        Predicted.
     *        
     * <p>this method will print out,</p>
     * 
     * <ul>
     * <li>TP = ...</li>
     * <li>FN = ...</li>
     * <li>FP = ...</li>
     * <li>TN = ...</li>
     * </ul>
     * 
     * <p>replacing the dots with actual values.</p>
     * 
	 * @param cm the confusion matrix to print out.
	 */
	public static void formatConfusionMatrix(int[][] cm)
	{
		String formattedText = "";
		            
		String tn = Integer.toString(cm[0][0]);
		String fn = Integer.toString(cm[0][1]);
		String fp = Integer.toString(cm[1][0]);
		String tp = Integer.toString(cm[1][1]);
		
		formattedText = "TN: "+tn+"\nFN: " + fn + "\nFP: " + fp + "\nTP: "+ tp + "\n";
		System.out.print(formattedText);
	}
	
	/**
	 *  <p>Prints the contents of a binary confusion matrix in terms of true positives, false positives,
	 *  true negatives and false negatives. Given a matrix of the form,</p> 
     *       
     *             Actual Class
     *               -     +
     *        P    -----------
     *        r  - | TN | FN |    TN = Negatives correctly receiving negative label.
     *        e    |---------|    FN = Positives incorrectly receiving negative label.
     *        d  + | FP | TP |    FP = Negatives incorrectly receiving positive label.
     *        .    -----------    TP = Positives correctly receiving positive label.
     *       
     *        ^
     *        |
     *        Predicted.
     *        
     * this method will print out a simple matrix with formatting.
     * 
	 * @param cm the confusion matrix to print out.
	 */
	public static void printConfusionMatrix(int[][] cm)
	{
		// Little dynamic code to accommodate multi-class confusion matrices.
		int spaces = 15; // Used to padd values in the matrix, i.e. cell width.
		int hypens = (15 * cm.length) + cm.length + 1; // Used to draw horizonal line of hypens
		
		// Form horizontal line.
		String horizontalLine = "";
		for(int k=0; k<hypens; k++)
			horizontalLine+="-";
		
		System.out.println("\nConfusion matrix\n");
		System.out.println("\nFormat");
		System.out.println("-----------");
		System.out.println("| TN | FN |");
		System.out.println("-----------");
		System.out.println("| FP | TP |");
		System.out.println("-----------\n");
		System.out.println(horizontalLine);
		
		for(int i=0; i<cm.length; i++)
		{
			System.out.print("|");
			
	        for(int j=0; j<cm[i].length; j++)
	        {
	           System.out.format("%"+Integer.toString(spaces)+"d", cm[i][j]);
	           System.out.print("|");
	        }
	        
	        System.out.println("\n"+horizontalLine);  // To move to the next line.
	    }
	}
}
