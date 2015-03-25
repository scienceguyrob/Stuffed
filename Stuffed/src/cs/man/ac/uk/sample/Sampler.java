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
 * File name: 	Sampler.java
 * Package: cs.man.ac.uk.sample
 * Created:	May 30, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

import cs.man.ac.uk.common.Common;

/**
 * The class Sampler is the external interface for this file sampling code.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public class Sampler implements ISampler
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************
	
	/**
	 * The path to the log file.
	 */
	private String logPath = "";
	
	/**
	 * The path to the file to be sampled.
	 */
	private String path = "";
	
	/**
	 * The file extension of the file to be sampled.
	 */
	private String extension = "";
	
	/**
	 * The path to the log file.
	 */
	private int classIndex = -1;
	
	/**
	 * The file to be sampled.
	 */
	private IFile file = null;
	
	/**
	 * Verbose logging flag.
	 */
	private boolean verbose = false;
	
	/**
	 * The constant string extension used for positive meta data.
	 */
	private final String POSITIVE_META_DATA_EXT = ".positive.meta";
	
	/**
	 * The constant string extension used for negative meta data.
	 */
	private final String NEGATIVE_META_DATA_EXT = ".negative.meta";
	
	//*****************************************
	//*****************************************
	//            Constructor
	//*****************************************
	//*****************************************
	
	/**
	 * Default constructor.
	 * @param logPath the absolute path to the file to write log statements to.
	 * @param v the verbose logging flag.
	 */
	public Sampler(String logPath,boolean v)
	{
		this.logPath=logPath;
		this.verbose = v;
	}
	
	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#setLogPath(java.lang.String)
	 */
	@Override
	public void setLogPath(String path){ this.logPath = path;}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getRows()
	 */
	@Override
	public int getRows()
	{
		if(file!=null)
			return file.getRows();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getColumns()
	 */
	@Override
	public int getColumns()
	{
		if(file!=null)
			return file.getColumns();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#doesMetaDataExist()
	 */
	@Override
	public boolean doesMetaDataExist()
	{
		if(this.path != null && !this.path.isEmpty())
		{	
			// Now construct paths to positive and negative meta data.
			String positiveMetaDataPath=path.replace(extension, POSITIVE_META_DATA_EXT);
			String negativeMetaDataPath=path.replace(extension, NEGATIVE_META_DATA_EXT);
			
			if(Common.fileExist(positiveMetaDataPath) & Common.fileExist(negativeMetaDataPath))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getClassLabels()
	 */
	@Override
	public String[] getClassLabels()
	{
		if(file!=null)
			return file.getClassLabels();
		else
			return new String[]{};
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getAttributeLabels()
	 */
	@Override
	public String[] getAttributeLabels()
	{
		if(file!=null)
			return file.getAttributeLabels();
		else
			return new String[]{};
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getAttributeCount()
	 */
	@Override
	public int getAttributeCount()
	{
		if(file!=null)
			return file.getAttributeCount();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#getClassDistribution()
	 */
	@Override
	public int[] getClassDistribution()
	{
		if(file!=null)
			return file.getClassDistribution();
		else
			return new int[]{};
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#load(java.lang.String, int)
	 */
	@Override
	public boolean load(String path, int classIndex)
	{
		this.path = path;
		this.classIndex = classIndex;
		
		// First get the file extension
		extension = path.substring(path.lastIndexOf("."),path.length());
					
		if(doesMetaDataExist())
		{
			if(extension.toLowerCase().contains(".csv"))
			{
				file = new CSV(this.path, this.classIndex, this.logPath,this.verbose);
				return file.preprocess();
			}
			else if(extension.toLowerCase().contains(".arff"))
			{
				file = new ARFF(this.path, this.classIndex, this.logPath,this.verbose);
				return file.preprocess();
			}
			else
				return false;
		}
		else // Create meta data too.
		{
			if(extension.toLowerCase().contains(".csv"))
			{
				file = new CSV(this.path, this.classIndex, this.logPath,this.verbose);
				
				boolean result1 = file.preprocess();
				boolean result2 = file.createMetaData();
				return result1 & result2;
			}
			else if(extension.toLowerCase().contains(".arff"))
			{
				file = new ARFF(this.path, this.classIndex, this.logPath,this.verbose);
				boolean result1 = file.preprocess();
				boolean result2 = file.createMetaData();
				return result1 & result2;
			}
			else
				return false;
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#sampleToCSV(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToCSV(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		if(file!=null)
			return file.sampleToCSV(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);
		else
			return new Object[]{false,"File not initialised"};
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.ISampler#sampleToARFF(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToARFF(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		if(file!=null)
			return file.sampleToARFF(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);
		else
			return new Object[]{false,"File not initialised"};
	}
}