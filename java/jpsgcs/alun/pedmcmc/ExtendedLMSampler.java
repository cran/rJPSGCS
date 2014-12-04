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
import jpsgcs.alun.markov.Variable;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.MultiVariable;
import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.DenseTable;
import jpsgcs.alun.markov.Table;
import jpsgcs.alun.markov.Potential;

import java.util.Set;
import java.util.LinkedHashSet;

public class ExtendedLMSampler extends LocusMeiosisSampler
{
	private static int[] defups = {3};

	public ExtendedLMSampler(GeneticDataSource d, boolean linkfirst)
	{
		this(d,linkfirst,false,3,defups,1.00);
	}

	public ExtendedLMSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials, int opt, int[] ns, double rate)
	{
		LocusProduct[] p = makeLocusSamplers(d,linkfirst,usepotentials);

		gg = makeMeiosisSamplers(p,d,linkfirst,usepotentials,opt);

		dat = d;
		ups = ns;
		link = linkfirst;

		samplerate = rate;
	}

	public ExtendedLMSampler(GeneticDataSource d, boolean linkfirst, double error, int maxerral)
	{
		this(d,linkfirst,false,3,defups,1.00,error,maxerral);
	}

	public ExtendedLMSampler(GeneticDataSource d, boolean linkfirst, boolean usepotentials, int opt, int[] ns, double rate, double error, int maxerral)
	{
		LocusProduct[] p = makeErrorLocusSamplers(d,linkfirst,usepotentials,error,maxerral);

		gg = makeMeiosisSamplers(p,d,linkfirst,usepotentials,opt);

		dat = d;
		ups = ns;
		link = linkfirst;

		samplerate = rate;
	}

	public void sample(boolean report)
	{
		for (int i=0; i<ups.length; i++)
		{
			sampleRandomMeioses(ups[i],dat,link);
			if (report)
				System.err.print("R("+ups[i]+")");
		}

		super.sample(report);
	}
		
// Private data.

	private GraphicalModel[] gg = null;
	private GeneticDataSource dat = null;
	private int[] ups = null;
	private boolean link = false;

	private void sampleRandomMeioses(int nn, GeneticDataSource d, boolean link)
	{
		int n = 0;

		// Find random set of individuals to update.

		int first = link ? 0 : 1;
		for (int j=first; j<h[first].length; j++)
			if (h[first][j][0] != null)
				n++;

		if (n > nn)
			n = nn;

		int[] x = new int[n];
		
		for (int i=0; i<x.length; i++)
		{
			while(true)
			{
				x[i] = (int) (Math.random()*h[first].length);

				if (h[first][x[i]][0] == null)
					continue;

				if (h[first][x[i]][1] == null)
					continue;

				int z = 0;
				for (int j=0; j<i; j++)
					if (x[i] == x[j])
						z++;

				if (z == 0)
					break;
			}
		}

		MarkovRandomField p = new MarkovRandomField();
		Inheritance[] prev = null;
		
		for (int i= first; i<h.length; i++)
		{
			Inheritance[] cur = new Inheritance[2*x.length];

			for (int j=0; j<x.length; j++)
			{
				cur[2*j] = h[i][x[j]][0];
				cur[2*j+1] = h[i][x[j]][1];
			}

			Table t = locusMarginal(cur,gg[i]);
			p.add(t);

			if (prev != null)
			{
				for (int j=0; j<x.length; j++)
				{
					p.add(new Recombination(prev[2*j],cur[2*j],d.getMaleRecomFrac(i-1,i)));
					p.add(new Recombination(prev[2*j+1],cur[2*j+1],d.getMaleRecomFrac(i-1,i)));
				}
			}

			prev = cur;
		}

		GraphicalModel gm = new GraphicalModel(p,false);
		gm.simulate();
	}

	private Table locusMarginal(Inheritance[] n, GraphicalModel m)
	{
		Set<Variable> s = new LinkedHashSet<Variable>();
		for (Inheritance i : n)
			s.add(i);
		Potential[][] p = m.splitByOutvol(s);
		m.preLogPeel(p[0],true);

		MultiVariable v = new MultiVariable(n);
		Table t = new DenseTable(v);
		t.allocate();
		double max = Double.NEGATIVE_INFINITY;

		for (v.init(); v.next(); )
		{
			double x = m.postLogPeel(p[1],true);
			if (max < x)
				max = x;
			t.setValue(x);
		}

		for (v.init(); v.next(); )
		{
			double x = t.getValue();
			t.setValue(Math.exp(x-max));
		}

		return t;
	}
}
