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
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedmcmc.Recombination;
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.MarkovRandomField;

import java.util.Map;
import java.util.LinkedHashMap;

public class GeneDropper
{
	public GeneDropper(GeneticDataSource d, LDModel ld, boolean linkfirst)
	{
		data = d;

		first = linkfirst ? 0 : 1;
		
		all = new Variable[d.nLoci()-first];
		for (int i=first; i<d.nLoci(); i++)
			all[i-first] = new Variable(d.nAlleles(i));

		Map<Variable,Variable> map = new LinkedHashMap<Variable,Variable>();
		Variable[] ldv = ld.getLocusVariables();
		for (int i=0; i<all.length; i++)
			map.put(ldv[i],all[i]);

		gall = new GraphicalModel(ld.replicate(map),true);
		
		pat = new Variable[d.nLoci()-first];
		for (int i=0; i<pat.length; i++)
			pat[i] = new Variable(2);

		MarkovRandomField p = new MarkovRandomField();
		for (int i=1+first; i<d.nLoci(); i++)
			p.add(new Recombination(pat[i-first],pat[i-first-1], d.getMaleRecomFrac(i,i-1)));

		gpat = new GraphicalModel(p,true);
		

		mat = new Variable[d.nLoci()-first];
		for (int i=0; i<mat.length; i++)
			mat[i] = new Variable(2);

		p = new MarkovRandomField();
		for (int i=1+first; i<d.nLoci(); i++)
			p.add(new Recombination(mat[i-first],mat[i-first-1], d.getFemaleRecomFrac(i,i-1)));

		gmat = new GraphicalModel(p,true);

		gall.collect();
		gpat.collect();
		gmat.collect();

		alleles = new int[d.nLoci()-first][d.nIndividuals()][2];

		pa = new int[d.nIndividuals()];
		ma = new int[d.nIndividuals()];
		
		for (int i=0; i<pa.length; i++)
		{
			pa[i] = d.pa(i);
			ma[i] = d.ma(i);
		}

		ord = Pedigrees.canonicalOrder(d);
	}

	public void geneDrop()
	{
		geneDrop(false);
	}

	public void geneDrop(boolean allout)
	{
		sampleAlleles();

		for (int i=first; i<data.nLoci(); i++)
		{
			for (int j=0; j<data.nIndividuals(); j++)
			{
				int a0 = data.getAllele(i,j,0);
				if (allout || a0 >= 0)
					a0 = alleles[i-first][j][0];

				int a1 = data.getAllele(i,j,1);
				if (allout || a1 >= 0)
					a1 = alleles[i-first][j][1];

				data.setAlleles(i,j,a0,a1);
			}
		}
	}

// Private data.

	private GeneticDataSource data = null;
	private int first = 0;

	private int[][][] alleles = null;
	private int[] pa = null;
	private int[] ma = null;
	private int[] ord = null;
	
	private Variable[] all = null;
	private Variable[] pat = null;
	private Variable[] mat = null;
	private GraphicalModel gall = null;
	private GraphicalModel gpat = null;
	private GraphicalModel gmat = null;

	private void sampleAlleles()
	{
		for (int jj=0; jj<ord.length; jj++)
		{
			int j = ord[jj];

			if (pa[j] < 0)
			{
				gall.drop();
				for (int i=0; i<alleles.length; i++)
					alleles[i][j][0] = all[i].getState();
			}
			else
			{
				gpat.drop();
				for (int i=0; i<alleles.length; i++)
					alleles[i][j][0] = alleles[i][pa[j]][pat[i].getState()];
			}

			if (ma[j] < 0)
			{
				gall.drop();
				for (int i=0; i<alleles.length; i++)
					alleles[i][j][1] = all[i].getState();
			}
			else
			{
				gmat.drop();
				for (int i=0; i<alleles.length; i++)
					alleles[i][j][1] = alleles[i][ma[j]][mat[i].getState()];
			}
		}
	}
}
