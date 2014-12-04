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

import jpsgcs.alun.markov.GraphicalModel;
import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.pedmcmc.Inheritance;
import jpsgcs.alun.pedmcmc.LocusProduct;
import jpsgcs.alun.pedmcmc.Recombination;

import java.util.Set;
import java.util.LinkedHashSet;

public class TLods
{
	public TLods(BasicGeneticData d, Inheritance[][][] inh, double[] thetas)
	{
		h = inh;
		theta = thetas;

		LocusProduct p = new LocusProduct(d,0);
		Inheritance[][] hh = p.getInheritances();
		t = new Inheritance[hh.length][2];
		Set<Recombination> s = new LinkedHashSet<Recombination>();

		for (int j=0; j<hh.length; j++)
		{
			if (hh[j][0] != null)
			{
				t[j][0] = new Inheritance();
				Recombination f = new Recombination(hh[j][0],t[j][0],0.5);
				p.add(f);
				p.remove(t[j][0]);
				s.add(f);
			}

			if (hh[j][1] != null)
			{
				t[j][1] = new Inheritance();
				Recombination f = new Recombination(hh[j][1],t[j][1],0.5);
				p.add(f);
				p.remove(t[j][1]);
				s.add(f);
			}
		}
		
		rec = (Recombination[]) s.toArray(new Recombination[0]);

		g = new GraphicalModel(p,true);
		g.reduceStates();
		loglhalf = g.logPeel();

		ratios = new double[h.length][theta.length];
	}

	public void update()
	{
		for (int i=1; i<h.length; i++)
		{
			for (int j=0; j<t.length; j++)
			{
				if (t[j][0] != null)
					t[j][0].setState(h[i][j][0].getState());
				if (t[j][1] != null)
					t[j][1].setState(h[i][j][1].getState());
			}

			for (int j=0; j<theta.length; j++)
			{
				for (int k=0; k<rec.length; k++)
					rec[k].fix(theta[j]);
				ratios[i][j] += Math.exp( g.logPeel() - loglhalf );
			}
		}

		count++;
	}

/** 
 Returns a matrix of doubles. The first index indicates the locus, the second indicates
 the value of theta. Each value is the total likelihood ratio
*/
	public double[][] results()
	{
		double[][] lods = new double[h.length][theta.length];

		for (int i=1; i<lods.length; i++)
			for (int k=0; k<lods[i].length; k++)
				lods[i][k] = Math.log10( ratios[i][k] / count );

		return lods;
	}

// Private data and methods.

	private double loglhalf = 0;
	private Inheritance[][][] h = null;
	private Inheritance[][] t = null;
	private Recombination[] rec = null;
	private double[][] ratios = null;
	private GraphicalModel g = null;
	private double[] theta = null;
	private int count = 0;
}
