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
 * File name: 	BaseEditor.java
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
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import cs.man.ac.uk.common.Strings;
import cs.man.ac.uk.common.VariableCast;
import cs.man.ac.uk.io.Writer;

/**
 * The class BaseEditor specifies the basic object, for a class hierarchy,
 * used to edit and sample data files.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/30/14
 */
public class BaseEditor implements IEditor
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The file being sampled or edited.
	 */
	protected IFile pfile = null;

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param file the file being edited or sampled.
	 */
	public BaseEditor(IFile file)
	{
		this.pfile = file;
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#preprocess()
	 */
	@Override
	public boolean preprocess()
	{		
		int LINES = 0;
		int rows = 0;
		int columns = 0;
		int classIndex = -1;
		String relation = "";

		Vector<String> attributes = new Vector<String>();
		Vector<String> classLabels = new Vector<String>();
		TreeMap<String, Integer> classDistribution = new TreeMap<String, Integer>();


		//Firstly try to read the file
		File file = new File(this.pfile.getPath());

		//if the file exists
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
				//open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					while ((line = in.readLine()) != null)
					{	
						if(line.startsWith("%") | line.startsWith(" "))// Ignore these		
							LINES+=1;
						else if(line.toUpperCase().startsWith("@RELATION"))
						{
							relation = line.replace("@RELATION", "").trim();
							LINES+=1;
						}
						else if(line.toUpperCase().startsWith("@ATTRIBUTE"))
						{					
							attributes.add(line.trim());
							LINES+=1;
						}
						else if(line.toUpperCase().startsWith("@DATA"))
						{					
							LINES+=1;
						}
						else if(!Strings.isNullOrEmptyString(line)) // The data.
						{
							LINES+=1;
							rows+=1;

							String[] components = line.split(",");

							if(columns==0)
								columns=components.length;// Assumes one column contains the class label.

							// Check class index is valid.
							if(classIndex<0)
								if(this.pfile.getClassIndex()<0)
								{
									classIndex = components.length-1;
									this.pfile.setClassIndex(classIndex);
								}
								else
									classIndex = this.pfile.getClassIndex();

							String clazz = components[classIndex];

							if(!classLabels.contains(clazz))
							{
								classLabels.add(components[classIndex]);
								classDistribution.put(clazz, 1);
							}
							else 
							{
								classDistribution.put(clazz, classDistribution.get(clazz) + 1);
							}

						}
						else { this.pfile.log("Ignored line:\n"+line); }
					}

					int[] classDist=new int[classDistribution.size()];

					Iterator<Entry<String, Integer>> it = classDistribution.entrySet().iterator();

					int i = 0;
					while (it.hasNext()) 
					{
						Map.Entry<String,Integer> pairs = (Map.Entry<String,Integer>)it.next();
						Integer value = pairs.getValue();
						classDist[i]=value;
						i++;
					}

					this.pfile.setLineCount(LINES);
					this.pfile.setRows(rows);
					this.pfile.setRelation(relation);
					this.pfile.setAttributeCount(attributes.size()-1);
					this.pfile.setColumns(columns);
					this.pfile.setAttributeLabels(VariableCast.convertStringListToArray(attributes));
					this.pfile.setClassLabels(VariableCast.convertStringListToArray(classLabels));
					this.pfile.setClassDistribution(classDist);

					return true;
				}
				catch(IOException e){ this.pfile.log("IO Exception:\n"+e.toString());return false;}
				finally{in.close();}
			}
			catch (Exception e) { this.pfile.log("Exception:\n"+e.toString());e.printStackTrace(); return false; }
		}
		else{ return false; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#shuffle()
	 */
	@Override
	public boolean shuffle()
	{
		String extension = this.pfile.getExtension();
		String outputPath = this.pfile.getPath().replace(extension, ".shuffled."+extension);
		Vector<String> lines = new Vector<String>();

		//Firstly try to read the file
		File file = new File(this.pfile.getPath());

		//if the file exists
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
				//open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					while ((line = in.readLine()) != null)
					{
						if(line.startsWith("%") | line.startsWith(" "))// Ignore these		
							Writer.append(outputPath,line+"\n");
						else if(line.startsWith("@RELATION")| line.startsWith("@relation"))
							Writer.append(outputPath,line+"\n\n");
						else if(line.startsWith("@ATTRIBUTE") | line.startsWith("@attribute"))			
							Writer.append(outputPath,line+"\n");
						else if(line.startsWith("@DATA")| line.startsWith("@data"))
							Writer.append(outputPath,"\n"+line+"\n");
						else if(!Strings.isNullOrEmptyString(line)) // The data.
							lines.add(line);

						else continue;
					}

					Collections.shuffle(lines);

					StringBuffer buffer = new StringBuffer();
					int writeCounter = 0;

					for(int i = 0; i< lines.size();i++)
					{
						buffer.append(lines.elementAt(i)+"\n");
						writeCounter++;

						if(writeCounter% 50 == 0)
						{
							Writer.append(outputPath, buffer.toString());
							buffer.setLength(0);
							writeCounter=0;
						}
					}

					if(writeCounter > 0)
						Writer.append(outputPath, buffer.toString());

					return true;
				}
				catch(IOException e){return false;}
				finally{in.close();}
			}
			catch (Exception e) { return false; }
		}
		else{ return false; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#createMetaDataBinary()
	 */
	@Override
	public boolean createMetaDataBinary()
	{
		String extension = this.pfile.getExtension();
		String path = this.pfile.getPath();
		int classIndex = this.pfile.getClassIndex();

		String positivePath = path.replace(extension, ".positive.meta");
		String negativePath = path.replace(extension, ".negative.meta");

		//Firstly try to read the file
		File file = new File(path);

		//if the file exists
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
				//open stream to file
				in = new BufferedReader(new FileReader(file));

				try
				{   
					int instance = 0;

					while ((line = in.readLine()) != null)
					{
						if(line.startsWith("%") | line.startsWith(" "))// Ignore these		
						{
							Writer.append(positivePath,line+"\n");
							Writer.append(negativePath,line+"\n");
						}
						else if(line.toUpperCase().startsWith("@RELATION"))
						{
							Writer.append(positivePath,line+"\n");
							Writer.append(negativePath,line+"\n");

							Writer.append(positivePath,"@ATTRIBUTE index	NUMERIC\n");
							Writer.append(negativePath,"@ATTRIBUTE index	NUMERIC\n");
						}
						else if(line.toUpperCase().startsWith("@ATTRIBUTE"))			
						{
							Writer.append(positivePath,line+"\n");
							Writer.append(negativePath,line+"\n");
						}
						else if(line.toUpperCase().startsWith("@DATA"))
						{
							Writer.append(positivePath,line+"\n");
							Writer.append(negativePath,line+"\n");
						}
						else if(!Strings.isNullOrEmptyString(line)) // The data.
						{
							String[] components = line.split(",");
							String clazz = components[classIndex];

							instance+=1;

							if(clazz.endsWith("1"))
								Writer.append(positivePath,instance+","+line+"\n");
							else
								Writer.append(negativePath,instance+","+line+"\n");

						}
						else continue;
					}

					return true;
				}
				catch(IOException e){return false;}
				finally{in.close();}
			}
			catch (Exception e) { return false; }
		}
		else{ return false; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#getMetaData(java.lang.String)
	 */
	@Override
	public TreeMap<Integer, Integer> getMetaData(String labelledDataPath)
	{
		// Stores the loaded meta data.
		TreeMap<Integer,Integer> patterns = new TreeMap<Integer,Integer>();

		//Firstly try to create the file
		File file = new File(labelledDataPath);

		//if the file exists
		if(file.exists())
		{
			String line = ""; 
			BufferedReader in = null;

			try
			{
				in = new BufferedReader(new FileReader(file));

				try
				{   
					int key = 0;
					while ((line = in.readLine()) != null)
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
							String[] contents = line.split(",");

							try
							{
								int value = Integer.parseInt(contents[0]);
								key+=1;
								patterns.put(key, value);
							}
							catch(NumberFormatException nfe){this.pfile.log("Error:"+nfe.toString());}
						}
					}

					return patterns;
				}
				catch(IOException e){return null;}
				finally{in.close();}
			}
			catch (Exception e) { return null; }
		}
		else{this.pfile.log("Does not exist:"+labelledDataPath); return null; }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#writeARFFHeader(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean writeARFFHeader(String destination, String title, String rel, String description)
	{
		// The header... it is made up of three parts:
		String header = "% Title : "+ title +"\n"+
				"% Description : "+description+"\n\n";

		String relation = "@RELATION "+ rel + "\n\n";

		String[] attributeLabels = this.pfile.getAttributeLabels();
		String attributes = "";

		for(int i=0;i< attributeLabels.length;i++)
			attributes += attributeLabels[i]+"\n";

		String data = "@DATA\n";
		// Write out the header
		Writer.append(destination, header);
		Writer.append(destination, relation);
		Writer.append(destination, attributes);
		return Writer.append(destination, data);
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#writeARFFHeader(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int[])
	 */
	@Override
	public boolean writeARFFHeader(String destination, String title, String rel,String description, int[] featureIndexes)
	{
		// The header... it is made up of three parts:
		String header = "% Title : "+ title +"\n"+
				"% Description : "+description+"\n\n";

		String relation = "@RELATION "+ rel + "\n\n";

		String attributes = "";

		for(int i=0;i<featureIndexes.length;i++)
			attributes += this.pfile.getAttributeLabels()[featureIndexes[i]-1]+"\n";

		String data = "@DATA\n";
		// Write out the header
		Writer.append(destination, header);
		Writer.append(destination, relation);
		Writer.append(destination, attributes);
		return Writer.append(destination, data);
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#label(int, int, int, int, double)
	 */
	@Override
	public boolean label(int labelled, int unlabelled, int INSTANCES_TO_LABEL,int INSTANCES_TO_NOT_LABEL, double labelling)
	{
		Random r = new Random();
		int result = 0;

		// keep looping until a decision is made.
		while(result == 0)
		{
			double rand = r.nextDouble();
			result = Double.compare(rand, labelling);
		}

		if(Double.compare(labelling, 1) == 0)//Everything should be labelled in this case.
			return true;
		else if(Double.compare(labelling, 0.0) == 0)// Nothing should be labelled in this case.
			return false;
		else 
		{
			// Random > labelling ratio and there are still instances we shouldn't label.
			if(result > 0 && unlabelled < INSTANCES_TO_NOT_LABEL)
				return false;
			// Random < labelling ratio and there are still instances to label.
			else if(result < 0 && labelled < INSTANCES_TO_LABEL)
				return true;
			// Random > labelling ratio and there are too many unlabelled instances.
			else if(result > 0 && unlabelled >= INSTANCES_TO_NOT_LABEL)
				return true;
			else
				return false;
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#sampleToCSV(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public boolean sampleToCSV(String trainPath, String testPath, int negTrainSamples,
			int posTrainSamples, double trainingSetBalance, double testSetBalance, double labelling)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.sample.IEditor#sampleToARFF(java.lang.String, java.lang.String, int, int, double, double, double)
	 */
	@Override
	public boolean sampleToARFF(String trainPath, String testPath, int negTrainSamples,
			int posTrainSamples, double trainingSetBalance, double testSetBalance, double labelling)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
