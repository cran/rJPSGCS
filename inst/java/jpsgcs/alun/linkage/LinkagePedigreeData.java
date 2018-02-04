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


package jpsgcs.alun.linkage;

import jpsgcs.alun.util.Triplet;
import jpsgcs.alun.genio.Pedigree;
import java.util.Vector;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.*;
import java.io.*;

/**
 A structure representing the data from a linkage .ped file.
*/
public class LinkagePedigreeData implements LinkConstants
{
	public boolean twosexes = true;

	public LinkagePedigreeData()
	{
	}

	public LinkagePedigreeData(LinkageFormatter b, LinkageParameterData par, int format) throws IOException
	{
		Vector<LinkageIndividual> v = new Vector<LinkageIndividual>();
		for (; b.newLine(); )
			v.addElement(new LinkageIndividual(b,par,format));
		set(v);
	}

	public LinkagePedigreeData(LinkageFormatter b, LinkageParameterData par, boolean premake) throws IOException
	{
		this(b,par,(premake ? PREMAKE : STANDARD));
	}

	public LinkagePedigreeData(LinkageFormatter b, LinkageParameterData par) throws IOException
	{
		this(b,par,false);
	}

	public LinkagePedigreeData(LinkagePedigreeData p, int[] x)
	{
		Vector<LinkageIndividual> v = new Vector<LinkageIndividual>();
		
		LinkageIndividual[] ind = p.getIndividuals();
		for (int i=0; i<ind.length; i++)
			v.add(new LinkageIndividual(ind[i],x));

		set(v);
	}

	public LinkagePedigreeData(Collection<LinkageIndividual> v)
	{
		set(v);
	}

	public void quickSet(Collection<LinkageIndividual> v)
	{
		Collection<Triplet> t = new LinkedHashSet<Triplet>();

		for (LinkageIndividual cur : v)
		{
			LinkageIndividual pa = null;
			LinkageIndividual ma = null;
			t.add(new Triplet<LinkageIndividual,LinkageIndividual,LinkageIndividual>(cur,null,null));
		}

		ped = new Pedigree();
		ped.addTriplets(t);

		ind = (LinkageIndividual[])v.toArray(new LinkageIndividual[0]);

		for (int i=0; i<ind.length; i++)
			ind[i].index = i;
	}

	public void set(Collection<LinkageIndividual> v)
	{
		Map<String,LinkageIndividual> h = new LinkedHashMap<String,LinkageIndividual>();

		for (LinkageIndividual cur : v)
		{
			String s = cur.pedid+"::"+cur.id;
			if (h.get(s) != null)
			{
				System.err.println("Warning: Multiple input lines for "+cur.pedid+" "+cur.id);
				System.err.println("Ignoring all but the first.");
			}
			else 
				h.put(s,cur);
		}
		
		Collection<Triplet> t = new LinkedHashSet<Triplet>();
		for (LinkageIndividual cur : v)
		{
			LinkageIndividual pa = null;
			LinkageIndividual ma = null;

			if (cur.paid != 0)
			{
				String s = cur.pedid+"::"+cur.paid;
				pa = h.get(s);
				if (pa == null)
				{
					System.err.println("Warning: Input line for father of "+cur.uniqueName()+" is missing: "+cur.paid);
					System.err.println("Adding a new input line for "+cur.paid);
					pa = new LinkageIndividual(cur,cur.paid);
					pa.sex = MALE;
					h.put(s,pa);
					t.add(new Triplet<LinkageIndividual,LinkageIndividual,LinkageIndividual>(pa,null,null));
				}
			}

			if (cur.maid != 0)
			{
				String s = cur.pedid+"::"+cur.maid;
				ma = h.get(s);
				if (ma == null)
				{
					System.err.println("Warning: Input line for mother of "+cur.uniqueName()+" is missing: "+cur.maid);
					System.err.println("Adding a new input line for "+cur.maid);
					ma = new LinkageIndividual(cur,cur.maid);
					ma.sex = FEMALE;
					h.put(s,ma);
					t.add(new Triplet<LinkageIndividual,LinkageIndividual,LinkageIndividual>(ma,null,null));
				}
			}

			if (twosexes && pa != null && ma != null && pa == ma)
			{
				System.err.println("Warning: Father and mother of "+cur.uniqueName()+" are the same person: "+pa.uniqueName());
				System.err.println("Unless selfing is possible, you should correct this");
			}
			else
			{
				if (pa != null)
				{
					if (pa.sex != MALE)
					{
						if (pa.sex == FEMALE)
						{
							System.err.println("Warning: Father of "+cur.uniqueName()+" previously listed as female: "+pa.uniqueName()+" : "+pa.sex);
							System.err.println("Setting father's sex to male");
						}

						pa.sex = MALE;
					}
				}
	
				if (ma != null)
				{
					if (ma.sex != FEMALE)
					{
						if (ma.sex == MALE)
						{
							System.err.println("Warning: Mother of "+cur.uniqueName()+" previously listed as male: "+ma.uniqueName()+" : "+ma.sex);
							System.err.println("Setting mother's sex to female");
						}
						ma.sex = FEMALE;
					}
				}
			}

			t.add(new Triplet<LinkageIndividual,LinkageIndividual,LinkageIndividual>(cur,pa,ma));
		}

		ped = new Pedigree();
		ped.addTriplets(t);

		Collection<LinkageIndividual> u = h.values();
		ind = (LinkageIndividual[])u.toArray(new LinkageIndividual[u.size()]);
		for (int i=0; i<ind.length; i++)
			ind[i].index = i;
	}

	public LinkageIndividual pa(LinkageIndividual x)
	{
		for (int i=0; i<ind.length; i++)
			if (ind[i].pedid == x.pedid && ind[i].id == x.paid)
				return ind[i];
		return null;
	}

	public LinkageIndividual ma(LinkageIndividual x)
	{
		for (int i=0; i<ind.length; i++)
			if (ind[i].pedid == x.pedid && ind[i].id == x.maid)
				return ind[i];
		return null;
	}
	
/**
 Returns the array of individual data structures.
*/
	public LinkageIndividual[] getIndividuals()
	{
		return ind;
	}

	public int nIndividuals()
	{
		return ind.length;
	}

	public int firstPedid()
	{
		if (ind == null || ind.length == 0)
			return -1;
		else
			return ind[0].pedid;
	}

/**
 Returns a string representing the data for the given pedigrees in
 the same format as it was read in from the linkage .ped file.
*/
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		if (outputpremake)
			for (int i=0; i<ind.length; i++)
				s.append(ind[i].shortString()+"\n");
		else 
			for (int i=0; i<ind.length; i++)
				s.append(ind[i].longString()+"\n");
	
		if (ind.length > 0)
			s.deleteCharAt(s.length()-1);
		return s.toString();
	}

	public void writeTo(PrintStream p)
	{
		if (outputpremake)
		{
			for (int i=0; i<ind.length; i++)
				if (i == ind.length-1)
					p.print(ind[i].shortStringNoSpace());
				else
					p.println(ind[i].shortStringNoSpace());
		}
		else 
		{
			for (int i=0; i<ind.length; i++)
			{
				ind[i].writeToNoSpace(p);
				p.println();
			}
		}
	}

	public void writeTo(GZIPOutputStream p)
	{
			for (int i=0; i<ind.length; i++)
			{
				ind[i].writeToNoSpace(p);
				try {p.write("\n".getBytes());}
				catch (IOException e) { System.err.println(e);}
			}

	}

	public void writeTranspose(PrintStream p)
	{
		p.println(ind.length);

		for (int i=0; i<ind.length; i++)
			p.println(ind[i].pedigreeDataString());
		
		p.flush();

		int nloc = -1;
		for (int i=0; i<ind.length; i++)
			if (ind[i].pheno != null)
				if (nloc < ind[i].pheno.length)
					nloc = ind[i].pheno.length;

		for (int i=0; i<nloc; i++)
		{
			for (int j=0; j<ind.length; j++)
				p.print(ind[j].pheno[i]+"  ");
			p.println();
		}
		
		p.flush();
	}

/**
 Creates a checked Pedigree structure from the list of individuals.
*/
	public Pedigree getPedigree()
	{
		return ped;
	}

	public LinkagePedigreeData[] splitByPedigree()
	{
		Map<Integer,Vector<LinkageIndividual>> h = new LinkedHashMap<Integer,Vector<LinkageIndividual>>();
		for (int i=0; i<ind.length; i++)
		{
			Integer x = new Integer(ind[i].pedid);
			Vector<LinkageIndividual> v = h.get(x);
			if (v == null)
			{
				v = new Vector<LinkageIndividual>();
				h.put(x,v);
			}
			v.add(ind[i]);
		}

		Vector<LinkageIndividual>[] u = (Vector<LinkageIndividual>[]) h.values().toArray(new Vector[0]);
		LinkagePedigreeData[] lpd = new LinkagePedigreeData[u.length];
		for (int i=0; i<lpd.length; i++)
			lpd[i] = new LinkagePedigreeData(u[i]);

		return lpd;
	}

// Private data.

	protected LinkageIndividual[] ind = null;
	protected Pedigree ped = null;
	protected boolean outputpremake = false;

/** 
 Main reads in a linkage parameter file and a linkage pedigree file which is
 in short form and output the linkage pedigree file in long form.
*/

	public static void main(String[] args)
	{
		try
		{
			boolean premake = false;
			
			switch(args.length)
			{
			case 1: if (args[0].equals("-s"))
					premake = true;
				break;
			}

			LinkageFormatter f = new LinkageFormatter(new BufferedReader(new InputStreamReader(System.in)),"Ped file");
			LinkagePedigreeData ped = new LinkagePedigreeData(f,null,premake);
			System.out.println(ped);
		}
		catch (Exception e)
		{
			System.err.println("Caught in LinkagePedigreeData:main()");
			e.printStackTrace();
		}
	}
}
