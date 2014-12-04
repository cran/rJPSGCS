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

import jpsgcs.alun.genio.Family;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.util.Triplet;
import jpsgcs.alun.util.IntArray;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.PrintStream;
import java.util.zip.*;

public class LinkageInterface implements GeneticDataSource
{
	public LinkageInterface(LinkageDataSet l)
	{
		L = l;
		naps = new LinkedHashMap<IntArray,double[][]>();
	}

	public void downcodeAlleles()
	{
		L.downCode(false);
	}

	public void writeParameters(PrintStream p)
	{
		L.getParameterData().writeTo(p);
	}

	public void writePedigree(PrintStream p)
	{
		L.getPedigreeData().writeTo(p);
	}

	public void writePedigree(GZIPOutputStream z)
	{
		L.getPedigreeData().writeTo(z);
	}

	public void writeIndividual(int j, PrintStream p)
	{
		L.getPedigreeData().getIndividuals()[j].writeTo(p);
	}

	public int getAllele(int i, int j, int k)
	{
		LinkageLocus loc = L.getParameterData().getLoci()[i];
		if (!(loc instanceof NumberedAlleleLocus))
			return -1;

		LinkageIndividual ind = L.getPedigreeData().getIndividuals()[j];
		NumberedAllelePhenotype y = (NumberedAllelePhenotype)(ind.getPhenotype(i));
	
		return k == 0 ? y.a1-1 : y.a2-1;
	}

	public boolean setAlleles(int i, int j, int b0, int b1)
	{
		LinkageLocus loc = L.getParameterData().getLoci()[i];
		if (!(loc instanceof NumberedAlleleLocus))
			return false;

		LinkageIndividual ind = L.getPedigreeData().getIndividuals()[j];
		NumberedAllelePhenotype y = (NumberedAllelePhenotype)(ind.getPhenotype(i));
		
		y.a1 = b0+1;
		y.a2 = b1+1;

		return true;
	}

	public int nLoci()
	{
		return L.getParameterData().nLoci();
	}

	public int nAlleles(int i)
	{
		return alleleFreqs(i).length;
	}

	public double[] alleleFreqs(int i)
	{
		LinkageParameterData p = L.getParameterData();
		LinkageLocus[] locs = p.getLoci();
		LinkageLocus ll = locs[i];
		return ll.alleleFrequencies();
	}

/** 
	Returns the recombination fraction between the ith and jth loci.
*/
	public double getMaleRecomFrac(int i, int j)
	{
		return L.getParameterData().maleTheta(i,j);
	}

	public double getFemaleRecomFrac(int i, int j)
	{
		return L.getParameterData().femaleTheta(i,j);
	}

	public String name()
	{
		return L.name();
	}

	public int nIndividuals()
	{
		return L.getPedigreeData().getIndividuals().length;
	}

	public String indComment(int j)
	{
		return L.getPedigreeData().getIndividuals()[j].comment;
	}

	public int pa(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		x = (LinkageIndividual)L.getPedigreeData().getPedigree().getTriplet(x).y;
		return x == null ? -1 : x.index;
	}

	public int ma(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		x = (LinkageIndividual)L.getPedigreeData().getPedigree().getTriplet(x).z;
		return x == null ? -1 : x.index;
	}

	public String individualName(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		return ""+x.id;
	}

	public String pedigreeName(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		return x.pedid+" ";
	}

	public int proband(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		return x.proband;
	}

	public int nProbands()
	{
		int n = 0;
		LinkageIndividual[] x = L.getPedigreeData().getIndividuals();
		for (int i=0; i<x.length; i++)
			if (x[i].proband == 1)
				n++;
		return n;
	}

	public String locusName(int i)
	{
		return L.getParameterData().getLoci()[i].locName();
	}

	public int[] canonicalOrder()
	{
		Set<Integer> s = new LinkedHashSet<Integer>();
		Set<Integer> t = new LinkedHashSet<Integer>();
		
		for (int i=0; i<nIndividuals(); i++)
			s.add(new Integer(i));

		Integer ff = new Integer(-1);
		t.add(ff);

		while (!s.isEmpty())
		{
			for (Integer i : s)
			{
				int p = new Integer(pa(i.intValue()));
				int m = new Integer(ma(i.intValue()));
				if (t.contains(p) && t.contains(m))
					t.add(i);
			}

			s.removeAll(t);
		}

		t.remove(ff);

		int[] x = new int[t.size()];
		int n = 0;
		for (Integer i : t)
			x[n++] = i.intValue();
		
		return x;
	}

	public int[][] nuclearFamilies()
	{
		Family[] f = L.getPedigreeData().getPedigree().nuclearFamilies();
		int[][] n = new int[f.length][];
		for (int i=0; i<n.length; i++)
		{
			LinkageIndividual pa = (LinkageIndividual) f[i].getPa();
			LinkageIndividual ma = (LinkageIndividual) f[i].getMa();
			Object[] k = f[i].getKids();
			n[i] = new int[k.length+2];
			n[i][0] = pa == null ? -1 : pa.index;
			n[i][1] = ma == null ? -1 : ma.index;
			for (int j=0; j<k.length; j++)
				n[i][j+2] =  ((LinkageIndividual)k[j]).index;
	}
	
		return n;
	}

	public int[] kids(int j)
	{
		LinkageIndividual x = L.getPedigreeData().getIndividuals()[j];
		Object[] kk = L.getPedigreeData().getPedigree().kids(x);
		int[] kkk = new int[kk.length];
		for (int i=0; i<kkk.length; i++)
			kkk[i] = ((LinkageIndividual)kk[i]).index;
		return kkk;
	}

	public double[][] penetrance(int i, int j)
	{
		LinkageLocus l = L.getParameterData().getLoci()[i];

		if (l instanceof NumberedAlleleLocus)
		{
			NumberedAllelePhenotype nap = (NumberedAllelePhenotype) L.getPedigreeData().getIndividuals()[j].getPhenotype(i);
			int p = nap.a1-1;
			int q = nap.a2-1;
			return napTable(nAlleles(i),p,q);
		}
		else if (l instanceof AffectionStatusLocus)
		{
			AffectionStatusLocus lal = (AffectionStatusLocus) l;
			AffectionStatusPhenotype asp = (AffectionStatusPhenotype) L.getPedigreeData().getIndividuals()[j].getPhenotype(i);

			double[] f = null;
			if (asp.status > 0)
				f = lal.liab[asp.liability-1];

			return affTable(nAlleles(i),asp.status,f);
		}
		else if (l instanceof QuantitativeLocus)
		{
			QuantitativeLocus q = (QuantitativeLocus) l;
			QuantitativePhenotype p = (QuantitativePhenotype) L.getPedigreeData().getIndividuals()[j].getPhenotype(i);
			return q.densityOf(p.v);
		}

		return null;
	}

	private double[][] affTable(int na, int stat, double[] f)
	{
		if (stat == 0)
			return null;

		double[][] x = new double[na][];
		for (int i=0; i<x.length; i++)
			x[i] = new double[na];

		switch (stat)
		{
		case 0:
			for (int i=0; i<x.length; i++)
				for (int j=0; j<x[i].length; j++)
					x[i][j] = 1;
			break;
		case 1:
			for (int i=0; i<x.length; i++)
				for (int j=0; j<x[i].length; j++)
					x[i][j] = 1-f[i+j];
			break;
		case 2:
			for (int i=0; i<x.length; i++)
				for (int j=0; j<x[i].length; j++)
					x[i][j] = f[i+j];
			break;
		}

		return x;
	}

	private double[][] napTable(int na, int p, int q)
	{
		if (p < 0  && q < 0)
			return null;

		IntArray a = new IntArray(na,p,q);
		double[][] x = (double[][])naps.get(a);

		if (x == null)
		{
			x = new double[na][];
			for (int i=0; i<x.length; i++)
				x[i] = new double[na];

			if (p < 0)
			{
				if (q < 0)
					for (int i=0; i<x.length; i++)
						for (int j=0; j<x[i].length; j++)
							x[i][j] = 1;
				else
					for (int i=0; i<x.length; i++)
						x[i][q] = x[q][i] = 1;
			}
			else
			{
				if (q < 0)
					for (int i=0; i<x.length; i++)
						x[i][p] = x[p][i] = 1;
				else
					x[p][q] = x[q][p] = 1;
			}

			naps.put(a,x);
		}

		return x;
	}

/*
 From old Genetic data interface.
*/
	public String toPhase()
	{
		StringBuffer s = new StringBuffer();

		s.append(nIndividuals());
		s.append("\n");
		s.append(nLoci());
		s.append("\n");
		for (int i=0; i<nLoci(); i++)
			s.append(nAlleles(i) == 2 ? "S" : "M");
		s.append("\n");

		for (int i=0; i<nIndividuals(); i++)
		{
			s.append("#"+i);
			s.append("\n");
			for (int k=0; k<2; k++)
			{
				for (int j=0; j<nLoci(); j++)
				{
					s.append(" ");
					if (getAllele(j,i,k) == -1)
						s.append( nAlleles(j) == 2 ? "?" : "-1" );
					else
						s.append(getAllele(j,i,k));
				}
				s.append("\n");
			}
		}

		s.deleteCharAt(s.length()-1);
		return s.toString();
	}

	public String toFastPhase()
	{
		String space = " ";
		StringBuffer s = new StringBuffer();

		s.append(nIndividuals());
		s.append("\n");
		s.append(nLoci());
		s.append("\n");
		for (int i=0; i<nLoci(); i++)
		{
			if (nAlleles(i) != 2)
				System.err.println("Warning: Locus "+i+" is not diallelic. Allele numbers > 1 will be set to 1");
			s.append("S");
		}
		s.append("\n");

		for (int i=0; i<nIndividuals(); i++)
		{
			s.append("# id "+i);
			s.append("\n");
			for (int k=0; k<2; k++)
			{
				for (int j=0; j<nLoci(); j++)
				{
					int allele = getAllele(j,i,k);
					s.append(space);
					s.append( allele == -1 ? "?" : ( allele > 1 ? 1 : allele ) );
				}
				s.append("\n");
			}
		}

		s.deleteCharAt(s.length()-1);
		return s.toString();
	}

// Private data.

	private LinkageDataSet L = null;
	private Map<IntArray,double[][]> naps = null;
	private boolean usepedid = false;
}
