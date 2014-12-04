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

import jpsgcs.alun.genio.PedigreeData;

import java.util.LinkedHashSet;

public class Pedigrees
{
	public static int[] canonicalOrder(PedigreeData d)
	{
		LinkedHashSet<Integer> x = new LinkedHashSet<Integer>();
		
		for (int i=0; i<d.nIndividuals(); i++)
			add(i,d,x);

		int[] ord = new int[x.size()];
		int j = 0;
		for (Integer i : x)
			ord[j++] = i.intValue();

		return ord;
	}

	public static void add(int i, PedigreeData d, LinkedHashSet<Integer> x)
	{
		if (i < 0 || x.contains(i))
			return;

		add(d.pa(i),d,x);
		add(d.ma(i),d,x);
		
		x.add(i);
	}
		
	public static int[][] parents(PedigreeData d)
	{
		int[][] par = new int[d.nIndividuals()][2];

		for (int i=0; i<par.length; i++)
		{
			par[i][0] = d.pa(i);
			par[i][1] = d.ma(i);
		}

		return par;
	}

	public static int[] fathers(PedigreeData d)
	{
		int[] pa = new int[d.nIndividuals()];
		for (int i=0; i<pa.length; i++)
			pa[i] = d.pa(i);
		return pa;
	}

	public static int[] mothers(PedigreeData d)
	{
		int[] ma = new int[d.nIndividuals()];
		for (int i=0; i<ma.length; i++)
			ma[i] = d.ma(i);
		return ma;
	}
}
