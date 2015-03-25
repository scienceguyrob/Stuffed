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
 * File name: 	I_MOAUtilities.java
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
 * The class I_MOAUtilities defines an interface for both static
 * and stream classifiers to use the MOA API.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public interface I_MOAUtilities
{
	/**
	 * @return true if the classifier is able to begin classifying the ARFF data streams, else false.
	 */
	public boolean ready();

	/**
	 * Reloads the test and training data stream used by the classifier.
	 * 
	 * @param testSetFilePath the path to the new test data set.
	 * @param trainSetFilePath the path to the new training data set.
	 * @return true if loaded successfully, else false.
	 */
	public boolean reloadStreams(String testSetFilePath, String trainSetFilePath);

	/**
	 * Reloads the existing training and testing data streams used by the classifier.
	 * 
	 * @return true if loaded successfully, else false.
	 */
	public boolean reloadTrainingStreams();
}
