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


package jpsgcs.alun.pedapps;

import jpsgcs.alun.genio.GeneticDataSource;

public class AlleleSharing
{
	public static int[] hetSharing(GeneticDataSource x)
	{
		int[] probs = probands(x);
		int[] s = new int[x.nLoci()];
			
		for (int i=0; i<s.length; i++)
		{
			int[] c = new int[x.nAlleles(i)];
			
			for (int j=0; j<probs.length; j++)
			{
				int a = x.getAllele(i,probs[j],0);
				int b = x.getAllele(i,probs[j],1);
				
				if (a < 0 || b < 0)
					for (int k=0; k<c.length; k++)
						c[k]++;
				else if (a == b)
					c[a]++;
				else
				{
					c[a]++;
					c[b]++;
				}
			}

			s[i] = 0;
			for (int k=0; k<c.length; k++)
				if (s[i] < c[k])
					s[i] = c[k];
		}

		return s;
	}

	public static int[] homSharing(GeneticDataSource x)
	{
		int[] probs = probands(x);
		int[] s = new int[x.nLoci()];
			
		for (int i=0; i<s.length; i++)
		{
			int[] c = new int[x.nAlleles(i)];
			
			for (int j=0; j<probs.length; j++)
			{
				int a = x.getAllele(i,probs[j],0);
				int b = x.getAllele(i,probs[j],1);
				
				if (a < 0 || b < 0)
					for (int k=0; k<c.length; k++)
						c[k]++;
				else if (a == b)
					c[a]++;
			}

			s[i] = 0;
			for (int k=0; k<c.length; k++)
				if (s[i] < c[k])
					s[i] = c[k];
		}

		return s;
	}

	public static int[] homozygotes(GeneticDataSource x)
	{
		int[] probs = probands(x);

		int[] s = new int[x.nLoci()];
		for (int i=0; i<s.length; i++)
		{
			for (int j=0; j<probs.length; j++)
			{
				int a = x.getAllele(i,probs[j],0);
				int b = x.getAllele(i,probs[j],1);
				if (a < 0 || b < 0 || a == b)
					s[i]++;
			}
		}

		return s;
	}

	private static int[] probands(GeneticDataSource x)
	{
		int[] probs = new int[x.nProbands()];
		int np = 0;
		for (int i=0; i<x.nIndividuals(); i++)
			if (x.proband(i) == 1)
				probs[np++] = i;

		return probs;
	}
			
	public static int[] runs(int[] s, int n)
	{
		int[] u = new int[s.length];
		if (s[0] >= n)
			u[0] = 1;
		for (int i=1; i<s.length; i++)
			if (s[i] >= n)
				u[i] = u[i-1] + 1;

		int[] v = new int[s.length];
		if (s[s.length-1] >= n)
			v[s.length-1] = 1;
		for (int i=s.length-2; i>=0; i--)
			if (s[i] >= n)
				v[i] = v[i+1] +1;

		for (int i=0; i<s.length; i++)
		{
			u[i] = u[i] +v[i]-1;
			if (u[i] < 0)
				u[i] = 0;
		}

		return u;
	}

	public static int max(int[] x)
	{
		int m = 0;
		for (int i=0; i<x.length; i++)
			if (m < x[i])
				m = x[i];
		return m;
	}
}
