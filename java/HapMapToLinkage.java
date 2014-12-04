/*
 Copyright 2010 Alun Thomas.

This file is part of JPSGCS.

JPSGCS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JPSGCS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JPSGCS.  If not, see <http://www.gnu.org/licenses/>.
*/


import jpsgcs.alun.pedapps.GeneticMap;

import java.io.PrintStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;

/**
	This converts a file of maker information in the format used by the HapMap
	project into standard LINKAGE parameter and pedigree files. 

<ul>
	<li> Usage : <b> java HapMapToLinakge output.par output.ped [lo hi] <hapmap.input </b> </li>
	<li> Usage : <b> java HapMapToLinakge hapmap.input genetic.map output.par output.ped [lo hi] </b> </li>
</ul>
	where 
<ul>
<li> <b> hapmap.input </b> is a HapMap format file of genotypes. </li>
<li> <b> genetic.map </b> is a file containing physical (base pair) to genetic (centiMorgan)
	distance information. </li>
<li> <b> output.par </b> is the output LINKAGE parameter file.  </li>
<li> <b> output.ped </b> is the output LINKAGE pedigree file.  </li>
<li> <b> lo hi </b> these are optional parameters specifiying the first and last markers
	to include data for. The default to to use all the markers. </li>
</ul>

<p>	As the relationships
	between the HapMap genotyped individuals are not specified in this file, they
	are treated as unrelated in the LINKAGE files. 

*/

public class HapMapToLinkage
{
    	public static void main(String[] args) throws IOException
    	{
            	try
		{
			int lo = -1;
			int hi = -1;

			BufferedReader br = null;
			PrintStream parout = null;
			PrintStream pedout = null;
			GeneticMap map = null;

			switch(args.length)
			{
			case 6: lo = new Integer(args[2]).intValue();
				hi = new Integer(args[3]).intValue();

			case 4: br = new BufferedReader(new FileReader(args[0]));
				map = GeneticMap.readMap(args[1]);
				parout = new PrintStream(args[2]);
				pedout = new PrintStream(args[3]);
				break;

			default:
				System.err.println("Usage: java HapMapToLinkage hapmap.input genetic.map out.par out.ped [lo hi]");
				System.exit(1);
			}
			 
			Vector v = readTheData(br);
                        int[] index = processTheData(v);

			if (lo > 0 && hi > lo)
				for (int i = v.size()-1; i>0; i--)
					if (i > hi || i < lo)
						v.remove(i); 

			outputParameters(v,parout,index,map);
			parout.flush();
			parout.close();
		
                    	outputPedigree(v,pedout,index);
			pedout.flush();
			pedout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

 	public static Vector readTheData(BufferedReader b) throws IOException
	{
		Vector vec = new Vector();
	        for (String line = b.readLine(); line != null; line = b.readLine())
		{
                	Vector w = new Vector();
			for (StringTokenizer t = new StringTokenizer(line); t.hasMoreTokens(); )
			{ 			
	                       w.add(t.nextToken());
                        }
			vec.add(w);
		}
                return vec;
	}

	public static void outputParameters(Vector v, PrintStream b, int[] index, GeneticMap map)
        {        
                b.print(" "+(v.size()-1)+" 0 0 5 "+"<< n loci, risk locus, sexlinked, program code");
                b.println();
                b.print(" 0 0.0 0.0 0  << mutation locus, mutation rates male, female, haplotype something");
		b.println();
		for (int x=1;x<v.size(); x++)
		{
		        b.print(" "+x+" ");
		}
		b.print(" << Marker Order");		
                b.println();
		int locname = index[0];
		for (int i=1; i<v.size(); i++)
		{
			Vector w = (Vector)v.get(i);
			b.print(" 3 4 ");
			b.println(w.get(locname)+":"+w.get(locname+2)+":"+w.get(locname+3));
			b.println(" 0.25 0.25 0.25 0.25");
		}

	        int pos = index[1];     
        	b.println(" 0 0 << sexdifference, interference ");
	        int previousdistance=0;               

                for (int j=1; j<v.size(); j++)
		{
			Vector w = (Vector)v.get(j);
			String str = w.get(pos).toString();		      
                        int presentdistance = Integer.parseInt(str);
                        if(j>1)
			{
				double cur = map.centiMorgan(presentdistance);
				double prev = map.centiMorgan(previousdistance);
				b.print(" "+map.cMToThetaKosambi(cur-prev));
                        	//b.print(" "+map.recombination(presentdistance,previousdistance));
			}
			previousdistance = presentdistance;
		}
                b.print(" <<spurious genetic distances");
		b.println(); 
                b.println(" 1 5 0.2 0.1 << who knows what this is ??? ");
	}

	public static void outputPedigree(Vector v, PrintStream b, int[] index)
	{
		int first = index[2];
		Vector w = (Vector)v.get(0);
                for (int i=first; i<w.size(); i++)
		{
			b.print("1 ");
			String name = (String) w.get(i);
			int id = new Integer(name.substring(2)).intValue();
			b.print(id+"\t");
			b.print("0 0 0 0 0 1 1\t");
			for (int j=1; j<v.size(); j++)
		        {    		            
                             b.print(((Vector)v.get(j)).get(i)+"  ");
			}			
                        b.print(" >>> "+name);
			b.println();
		}
	}

	public static int[] processTheData(Vector v)
	{
		Vector v0 = (Vector)v.get(0);
		int nameat = 0;
		int posat = 0;
		int firstperson = 0;
		for (int j=0; j<v0.size(); j++)
		{
			if (v0.get(j).equals("rs#"))
				nameat = j;
			if (v0.get(j).equals("pos"))
				posat = j;
			if (v0.get(j).equals("QCcode") || v0.get(j).equals("QC_code"))
				firstperson = j+1;
		}
                for (int i=1; i<v.size(); i++)
		{
			Vector w = (Vector) v.get(i);
			for (int j=0; j<w.size(); j++)
			{
				String s = (String) w.get(j);
				w.set(j,conversion(s));
			}
		}
                int[] output = {nameat, posat, firstperson};
		return output;
	}

	public static String conversion(String s)
	{
		if (s.equals("AA")) return "1 1";
		if (s.equals("CC")) return "2 2";
                if (s.equals("GG")) return "3 3";
		if (s.equals("TT")) return "4 4";
		if (s.equals("NN")) return "0 0";
                if (s.equals("AC") || s.equals("CA")) return "1 2";
	        if (s.equals("AG") || s.equals("GA")) return "1 3";
                if (s.equals("AT") || s.equals("TA")) return "1 4";
                if (s.equals("AN") || s.equals("NA")) return "1 0";
                if (s.equals("CG") || s.equals("GC")) return "2 3";
                if (s.equals("CT") || s.equals("TC")) return "2 4";
	        if (s.equals("CN") || s.equals("NC")) return "2 0";
                if (s.equals("GT") || s.equals("TG")) return "3 4";
                if (s.equals("GN") || s.equals("NG")) return "3 0";
                if (s.equals("TN") || s.equals("NT")) return "4 0";
		return s;
	}
}
