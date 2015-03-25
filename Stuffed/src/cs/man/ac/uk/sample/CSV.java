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
 * File name: 	CSV.java
 * Package: cs.man.ac.uk.sample
 * Created:	May 30, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

/**
 * The class CSV represents a simple CSV file that can be sampled.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public class CSV extends BaseFile
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Primary constructor.
	 * @param path the path to the ARFF file.
	 * @param classIndex the class index, non-zero indexing.
	 * @param logPath the absolute path to a file to write log statements to.
	 * @param v verbose logging flag.
	 */
	public CSV(String path,int classIndex,String logPath,boolean v)
	{
		super(path, classIndex, logPath,v);
		editor = new CSVEditor(this);
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseFile#sampleToCSV(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToCSV(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		if(preprocessed)
		{
			boolean result = editor.sampleToCSV(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);

			if(result)
				return new Object[]{true,"SUCCESS"};
			else
				return new Object[]{false,"FAIL"};
		}
		else
			return new Object[]{false,"Preprocessing not complete"};
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseFile#sampleToARFF(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToARFF(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		if(preprocessed)
		{
			boolean result = editor.sampleToARFF(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);

			if(result)
				return new Object[]{true,"SUCCESS"};
			else
				return new Object[]{false,"FAIL"};
		}
		else
			return new Object[]{false,"Preprocessing not complete"};
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseFile#preprocess()
	 */
	@Override
	public boolean preprocess()
	{
		if(this.editor.preprocess())
		{
			log("ARFF Editor setup correctly\n");
			log("Line count:\t"+this.lineCount +"\n");
			log("Patterns  :\t"+this.rows+"\n");
			log("Relation  :\t"+this.relation+"\n");
			log("Attributes:\t"+this.attributeCount+"\n");
			log("Attribute labels:\n");
			log(this.attributeLabels);
			log("Class labels:\n");
			log(this.classLabels);
			log("Class distribution:\n");
			log(this.classDistribution);

			this.preprocessed=true;
			return this.preprocessed;
		}
		else
		{
			log("CSV editor did not complete preprocessing.");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseFile#createMetaData()
	 */
	@Override
	public boolean createMetaData()
	{ 
		if(preprocessed)
		{
			if(this.classLabels.length==2)// Binary case
				return editor.createMetaDataBinary();
			else 
			{
				// Multi class;
				return false;
			}
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.BaseFile#shuffleDataset()
	 */
	@Override
	public boolean shuffleDataset()
	{ 
		if(preprocessed)
			return editor.shuffle();
		else
			return false;
	}
}
