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

public class TransposedPedigreeData extends LinkagePedigreeData
{
	private LinkageParameterData par = null;
	private LinkageFormatter form = null;

	private int first = 0;
	private int got = 0;

	public TransposedPedigreeData(LinkageFormatter b, LinkageParameterData p) throws IOException
	{
		par = p;
		form = b;

		b.newLine();
		int n = b.readInt("Number of individuals",0,true,true);

		Vector<LinkageIndividual> v = new Vector<LinkageIndividual>();
		for (int i=0; i<n; i++)
		{
			if (!b.newLine())
				break;
			v.addElement(new LinkageIndividual(b,par,PEDONLY));
		}
		set(v);

//		readGeneticData(init);
	}

	public int firstLocus()
	{
		return first;
	}

	public int lastLocus()
	{
		return first+got;
	}

	public void readGeneticData(int n) throws IOException
	{
		if (par == null)
			return;

		LinkageLocus[] l = par.getLoci();
			
		first = 0;
		got = n;
		if (got > l.length)
			got = l.length;

		for (int j=0; j<ind.length; j++)
		{
			ind[j].pheno = new LinkagePhenotype[got];
			ind[j].offset = 0;
		}

		for (int i=0; i<got; i++)
		{
			if (!form.newLine())
				break;

			for (int j=0; j<ind.length; j++)
				ind[j].pheno[i] = l[i].readPhenotype(form);
		}

//		System.err.println("got "+got+"  "+first);
	}

	public void readNextData(int n) throws IOException
	{
		if (par == null)
			return;

		LinkageLocus[] l = par.getLoci();

		int get = n;
		if (first + got + get > l.length)
			get = l.length - got - first;

		if (get <= 0)
			return;

		if (get > got)
		{
			for (int i=0; i<get-got; i++)
				form.newLine();

			for (int i=0; i<got; i++)
			{
				if (!form.newLine())
					break;
				for (int j=0; j<ind.length; j++)
					ind[j].pheno[i] = l[i+first+get].readPhenotype(form);
			}
		}
		else
		{
			for (int i=0; i<got-get; i++)
				for (int j=0; j<ind.length; j++)
					ind[j].pheno[i] = ind[j].pheno[i+get];
			
			for (int i=got-get; i<got; i++)
			{
				if (!form.newLine())
					break;
				for (int j=0; j<ind.length; j++)
					ind[j].pheno[i] = l[i+first+get].readPhenotype(form);
			}
		}

		first += get;
		for (int j=0; j<ind.length; j++)
			ind[j].offset = first;

//		System.err.println("got "+got+"  "+first);
	}

	public void writeUntranspose(PrintStream p)
	{
		if (outputpremake)
		{
			for (int i=0; i<ind.length; i++)
				if (i == ind.length-1)
					p.print(ind[i].shortString());
				else
					p.println(ind[i].shortString());
		}
		else 
		{
			for (int i=0; i<ind.length; i++)
			{
				ind[i].writeTo(p);
			//	if (i != ind.length-1)
					p.println();
			}
		}
	
	}

	public void writeTo(PrintStream p)
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
}
