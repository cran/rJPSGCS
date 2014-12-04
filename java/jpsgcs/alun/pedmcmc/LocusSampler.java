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


package jpsgcs.alun.pedmcmc;

import jpsgcs.alun.genio.GeneticDataSource;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.util.Monitor;

import java.util.Set;
import java.util.LinkedHashSet;

public class LocusSampler
{
	protected LocusSampler()
	{
	}

	public LocusSampler(GeneticDataSource d,boolean linkfirst)
	{
		this(d,linkfirst,false);
	}

	public LocusSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials)
	{
		makeLocusSamplers(d,linkfirst,usepotentials);
	}

	public LocusSampler(GeneticDataSource d,boolean linkfirst,double error, int maxerral)
	{
		this(d,linkfirst,false,error,maxerral);
	}

	public LocusSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials, double error, int maxerral)
	{
		makeErrorLocusSamplers(d,linkfirst,usepotentials,error,maxerral);
	}

	public void initialize()
	{
		for (int i=0; i<g.length; i++)
			if (g[i] != null)
				g[i].simulate(false);
	}

	public void sample()
	{
		sample(false);
	}

	public void sample(boolean report)
	{
		for (int i=0; i<g.length; i++)
			if (g[i] != null)
			{
				g[i].simulate();
				if (report)
					System.err.print("L");
			}
	} 

	public Inheritance[][][] getInheritances()
	{
		return h;
	}

	public Error[][] getErrors()
	{
		return e;
	}

// Private data.
// Protected methods to make locus and meiosis samplers to be used by this and derived classes.

	protected GraphicalModel[] g = null;
	protected Inheritance[][][] h = null;
	protected Error[][] e = null;

	protected LocusProduct[] makeErrorLocusSamplers(GeneticDataSource d, boolean linkfirst, boolean usepotentials, double error, int maxerr)
	{
		LocusProduct[] p = new LocusProduct[d.nLoci()];

		int first = linkfirst ? 0 : 1;

		h = new Inheritance[p.length][][];
		e = new Error[p.length][];

		for (int i=first; i<p.length; i++)
		{
			if (d.nAlleles(i) <= maxerr)
			{
				p[i] = new ErrorLocusProduct(d,i,error);
				h[i] = p[i].inh;
				e[i] = ((ErrorLocusProduct)p[i]).err;
			}
			else
			{
				p[i] = new LocusProduct(d,i);
				h[i] = p[i].inh;
				GraphicalModel gm = new GraphicalModel(p[i],false);
				gm.reduceStates();
			}
		}

		for (int j=0; j<h[1].length; j++)
		{
			if (h[first][j][0] != null)
			{
				for (int i = 1+first; i<p.length; i++)
				{
					Recombination f = new Recombination(h[i-1][j][0],h[i][j][0],d.getMaleRecomFrac(i-1,i));
					p[i-1].add(f);
					p[i-1].remove(h[i][j][0]);
					p[i].add(f);
					p[i].remove(h[i-1][j][0]);
				}
			}

			if (h[first][j][1] != null)
			{
				for (int i = 1+first; i<p.length; i++)
				{
					Recombination f = new Recombination(h[i-1][j][1],h[i][j][1],d.getFemaleRecomFrac(i-1,i));
					p[i-1].add(f);
					p[i-1].remove(h[i][j][1]);
					p[i].add(f);
					p[i].remove(h[i-1][j][1]);
				}
			}
		}

		g = new GraphicalModel[p.length];
		for (int i=first; i<g.length; i++)
			g[i] = new GraphicalModel(p[i],usepotentials);

		return p;
	}

	protected LocusProduct[] makeLocusSamplers(GeneticDataSource d, boolean linkfirst, boolean usepotentials)
	{
		LocusProduct[] p = new LocusProduct[d.nLoci()];

		int first = linkfirst ? 0 : 1;

		h = new Inheritance[p.length][][];

		for (int i=first; i<p.length; i++)
		{
			p[i] = new LocusProduct(d,i);
			h[i] = p[i].inh;
			GraphicalModel gm = new GraphicalModel(p[i],false);
			gm.reduceStates();
		}


		for (int j=0; j<h[1].length; j++)
		{
			if (h[first][j][0] != null)
			{
				for (int i = 1+first; i<p.length; i++)
				{
					Recombination f = new Recombination(h[i-1][j][0],h[i][j][0],d.getMaleRecomFrac(i-1,i));
					p[i-1].add(f);
					p[i-1].remove(h[i][j][0]);
					p[i].add(f);
					p[i].remove(h[i-1][j][0]);
				}
			}

			if (h[first][j][1] != null)
			{
				for (int i = 1+first; i<p.length; i++)
				{
					Recombination f = new Recombination(h[i-1][j][1],h[i][j][1],d.getFemaleRecomFrac(i-1,i));
					p[i-1].add(f);
					p[i-1].remove(h[i][j][1]);
					p[i].add(f);
					p[i].remove(h[i-1][j][1]);
				}
			}
		}

		g = new GraphicalModel[p.length];
		for (int i=first; i<g.length; i++)
			g[i] = new GraphicalModel(p[i],usepotentials);

		return p;
	}
}
