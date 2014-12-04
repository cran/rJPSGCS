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

import jpsgcs.alun.jtree.IntegerMatrix;
import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.pedmcmc.LDModel;
import jpsgcs.alun.pedmcmc.Error;
import jpsgcs.alun.pedmcmc.Genotype;
import jpsgcs.alun.pedmcmc.GenotypeErrorPenetrance;
import jpsgcs.alun.pedmcmc.GenotypePenetrance;
import jpsgcs.alun.pedmcmc.ErrorPrior;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Variable;

public class CompleteHaplotypes implements IntegerMatrix
{
	public CompleteHaplotypes(GeneticDataSource x, double eprob, int hold)
	{
		data = x;
		dummy = new Variable(1);

		p = new GenotypePenetrance[data.nLoci()];
		g = new Genotype[data.nLoci()];
		e = new ErrorPrior[data.nLoci()];

		for (int i=0; i<p.length; i++)
		{
			g[i] = new Genotype(data.nAlleles(i));
			Error err = new Error();
			e[i] = new ErrorPrior(err,eprob);
			p[i] = new CompletingPenetrance(g[i],err,dummy);
		}

		haps = new int[2*data.nIndividuals()][hold];

		offset = 0;
	}

	public CompleteHaplotypes(GeneticDataSource x, double eprob)
	{
		this (x,eprob,x.nLoci());
	}

	public CompleteHaplotypes(GeneticDataSource x, LDModel ld, double eprob)
	{
		this(x,eprob);
		setModel(ld);
	}

	protected CompleteHaplotypes()
	{
	}

	public void simulate()
	{
		update(true);
	}

	public void maximize()
	{
		update(false);
	}

	public void update(boolean random)
	{

		for (int j=0; j<data.nIndividuals(); j++)
		{
			for (int i : lociNeeded)
				p[i].fix(data.penetrance(i,j));


			if (random)
				gm.simulate();
			else
				gm.maximize();

			for (int i : lociNeeded)
			{
				haps[2*j][i-offset] = g[i].pat();
				haps[2*j+1][i-offset] = g[i].mat();
			}
		}


	}

	public int[][] getHaplotypes()
	{
		return haps;
	}

	public int nRows()
	{
		return haps.length;
	}

	public int nColumns()
	{
		throw new RuntimeException("All columns are not held by CompleteHaplotypes. Can't give nCols()");
	}

	public int value(int j, int i)
	{
		return haps[j][i-offset];
	}

	public void shift(int n)
	{
		int m = n;
		if (offset + haps[0].length + m > data.nLoci())
			m = data.nLoci() - haps[0].length - offset;
		
		for (int j=0; j<haps.length; j++)
		{
			for (int i=0; i<haps[j].length-m; i++)
				haps[j][i] = haps[j][i+m];
			for (int i=haps[j].length-m; i<haps[j].length; i++)
				haps[j][i] = 0;
		}

		offset += m;
	}

	public void setModel(LDModel ld)
	{
		lociNeeded = ld.usedLoci();

		MarkovRandomField m = new MarkovRandomField();

		for (int i : lociNeeded)
		{
			m.add(p[i]);
			m.add(e[i]);
		}

		for (Function f : ld.duplicate(g).getFunctions())
			m.add(f);

		m.remove(dummy);

		gm = new GraphicalModel(m,true);
	}

	public GenotypePenetrance[] getPenetrances()
	{
		return p;
	}

	public ErrorPrior[] getPriors()
	{
		return e;
	}

// Privat data.

	protected GeneticDataSource data = null;
	protected int[][] haps = null;
	protected int offset = 0;

	protected GenotypePenetrance[] p = null;
	protected ErrorPrior[] e = null;

	private Genotype[] g = null;
	private GraphicalModel gm = null;
	private int[] lociNeeded = null;
	private Variable dummy = null;
}
