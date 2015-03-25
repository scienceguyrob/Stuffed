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
 * File name: 	BaseFile.java
 * Package: cs.man.ac.uk.sample
 * Created:	May 30, 2014
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.sample;

import java.io.File;

import cs.man.ac.uk.io.Writer;

/**
 * The class BaseFile contains the basic methods required by the files being sampled.
 * This class shouldn't be used directly, but should be sub-classed with the methods
 * sample(String,String,int,int,double,double,double), createMetaData() and shuffleDataset()
 * over-ridden.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public class BaseFile implements IFile
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************
	
	/**
	 * The path to the file.
	 */
	protected String path = "";
	
	/**
	 * The file extension.
	 */
	protected String extension = "";
	
	/**
	 * The path to the log file.
	 */
	protected String logPath = "";

	/**
	 * Number of lines in the file.
	 */
	protected int lineCount = 0;

	/**
	 * Number of rows of data in the file.
	 */
	protected int rows = 0;

	/**
	 * Number of columns of data in the file.
	 */
	protected int columns = 0;
	
	/**
	 * The name of this file without the path.
	 */
	protected String fileName  = "";

	/**
	 * The unique class labels present in the file.
	 */
	protected String[] classLabels = null;

	/**
	 * The unique attribute labels present in the file.
	 */
	protected String[] attributeLabels = null;

	/**
	 * The number of attributes.
	 */
	protected int attributeCount = -1;

	/**
	 * The class distribution.
	 */
	protected int[] classDistribution = null;

	/**
	 * The ARFF file relation.
	 */
	protected String relation = "unknown";

	/**
	 * The index of the class labels.
	 */
	protected int classIndex=-1;
	
	/**
	 * The editor class used to perform the sampling.
	 */
	protected IEditor editor = null;
	
	/**
	 * A boolean flag which when true indicates the file represented by this object
	 * has been parsed and pre-processed successfully.
	 */
	protected boolean preprocessed = false;
	
	/**
	 * Logging flag.
	 */
	protected boolean verbose = false;
	
	//*****************************************
	//*****************************************
	//           Getter & Setters
	//*****************************************
	//*****************************************
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getPath()
	 */
	@Override
	public String getPath(){ return this.path; }
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getExtension()
	 */
	@Override
	public String getExtension(){ return this.extension; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getLogPath()
	 */
	@Override
	public String getLogPath(){ return this.logPath; }
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getFileName()
	 */
	@Override
	public String getFileName(){ return this.fileName; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getLineCount()
	 */
	@Override
	public int getLineCount(){ return this.lineCount; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setLineCount(int)
	 */
	@Override
	public void setLineCount(int l){ this.lineCount = l; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getRows()
	 */
	@Override
	public int getRows(){ return this.rows; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setRows(int)
	 */
	@Override
	public void setRows(int r){ this.rows = r; }
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getColumns()
	 */
	@Override
	public int getColumns(){ return this.columns; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setColumns(int)
	 */
	@Override
	public void setColumns(int c){ this.columns = c; } 

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getRelation()
	 */
	@Override
	public String getRelation(){ return this.relation; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setRelation(java.lang.String)
	 */
	@Override
	public void setRelation(String r){ this.relation = r; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getClassLabels()
	 */
	@Override
	public String[] getClassLabels(){ return this.classLabels; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setClassLabels(java.lang.String[])
	 */
	@Override
	public void setClassLabels(String[] l){ this.classLabels = l; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getAttributeLabels()
	 */
	@Override
	public String[] getAttributeLabels(){ return this.attributeLabels; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setAttributeLabels(java.lang.String[])
	 */
	@Override
	public void setAttributeLabels(String[] a){ this.attributeLabels = a; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getAttributeCount()
	 */
	@Override
	public int getAttributeCount(){ return this.attributeCount; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setAttributeCount(int)
	 */
	@Override
	public void setAttributeCount(int a){ this.attributeCount = a; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getClassDistribution()
	 */
	@Override
	public int[] getClassDistribution(){ return this.classDistribution; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setClassDistribution(int[])
	 */
	@Override
	public void setClassDistribution(int[] dist){ this.classDistribution = dist; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#setClassIndex(int)
	 */
	@Override
	public void setClassIndex(int i){ this.classIndex = i; }
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#getClassIndex()
	 */
	@Override
	public int getClassIndex(){ return this.classIndex;}

	//*****************************************
	//*****************************************
	//            Constructor
	//*****************************************
	//*****************************************
	
	/**
	 * Primary constructor.
	 * @param path the path to the ARFF file.
	 * @param classIndex the class index, non-zero indexing.
	 * @param logPath the absolute path to a file to write log statements to.
	 * @param v the verbose logging flag.
	 */
	public BaseFile(String path,int classIndex,String logPath,boolean v)
	{
		this.path = path;
		this.classIndex = classIndex;
		this.logPath = logPath;
		this.verbose = v;
		File f = new File(path);
		this.fileName=f.getName();
		f=null;
		this.extension=this.path.substring(this.path.lastIndexOf("."),this.path.length());
	}
	
	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#sampleToCSV(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToCSV(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		return new Object[]{false,"Base class cannot be used to sample"};
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#sampleToARFF(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public Object[] sampleToARFF(String trainSetPath, String testSetPath, int negTrainSamples,
			int posTrainSamples, double trainSetBalance, double testSetBalance, double labelling)
	{
		return new Object[]{false,"Base class cannot be used to sample"};
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#createMetaData()
	 */
	@Override
	public boolean createMetaData(){ return false; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#shuffleDataset()
	 */
	@Override
	public boolean shuffleDataset(){ return false;}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#log(java.lang.String)
	 */
	@Override
	public boolean log(String msg)
	{
		if(verbose)
		{	System.out.println(msg);
			return Writer.append(this.getLogPath(),msg);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IFile#preprocess()
	 */
	@Override
	public boolean preprocess()
	{
		return false;
	}
	
	/**
	 * Writes out the contents of the supplied array to a log file.
	 * @param array the array whose contents will be output.
	 */
	public void log(String[] array)
	{
		log("\n\n+---------- String[] array DEBUGGING ----------+\n");

		if(array != null)
		{
			if(array.length>0)
			{
				for(int i=0;i<array.length;i++)
					log("Item "+i+": "+array[i]+"\n");
			}
			else
				log("Array is empty.\n");
		}
		else
			log("Array is null.\n");

		log("+----------------------------------------------+\n\n");
	}

	/**
	 * Writes out the contents of the supplied array to a log file.
	 * @param array the array whose contents will be output.
	 */
	public void log(int[] array)
	{
		log("\n\n+------------- int[] array DEBUGGING ----------+\n");
		if(array != null)
		{
			if(array.length>0)
			{
				for(int i=0;i<array.length;i++)
					log("Item "+i+": "+array[i]+"\n");
			}
			else
				log("Array is empty.\n");
		}
		else
			log("Array is null.\n");

		log("+----------------------------------------------+\n\n");
	}
}
