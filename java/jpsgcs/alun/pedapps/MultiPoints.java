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
import jpsgcs.alun.pedmcmc.TwoSidedRecombination;

import java.util.Set;
import java.util.LinkedHashSet;

public class MultiPoints
{
	public MultiPoints(BasicGeneticData d, Inheritance[][][] inh, double[] pos)
	{
		h = inh;
		positions = LocInfo.mapPositions(d,pos,false);

		LocusProduct p = new LocusProduct(d,0);
		Inheritance[][] hh = p.getInheritances();
		right = new Inheritance[hh.length][2];
		left = new Inheritance[hh.length][2];

		Set<TwoSidedRecombination> s = new LinkedHashSet<TwoSidedRecombination>();

		for (int j=0; j<hh.length; j++)
		{
			if (hh[j][0] != null)
			{
				left[j][0] = new Inheritance();
				right[j][0] = new Inheritance();

				TwoSidedRecombination f = new TwoSidedRecombination(left[j][0],hh[j][0],right[j][0],0.5,0.5);
				p.add(f);
				s.add(f);

				p.remove(left[j][0]);
				p.remove(right[j][0]);
			}

			if (hh[j][1] != null)
			{
				left[j][1] = new Inheritance();
				right[j][1] = new Inheritance();
			
				TwoSidedRecombination f = new TwoSidedRecombination(left[j][1],hh[j][1],right[j][1],0.5,0.5);
				p.add(f);
				s.add(f);
				
				p.remove(left[j][1]);
				p.remove(right[j][1]);
			}
		}
		
		rec = (TwoSidedRecombination[]) s.toArray(new TwoSidedRecombination[0]);

		g = new GraphicalModel(p,true);
		g.reduceStates();
		loglhalf = g.logPeel();

		ratios = new double[positions.length];
	}

	public void update()
	{
		for (int i=0; i<ratios.length; i++)
		{
			LocInfo l = positions[i];

			for (int j=0; j<left.length; j++)
			{
				if (left[j][0] != null)
					left[j][0].setState(h[1+l.ileft][j][0].getState());
				if (left[j][1] != null)
					left[j][1].setState(h[1+l.ileft][j][1].getState());
			}

			for (int j=0; j<right.length; j++)
			{
				if (right[j][0] != null)
					right[j][0].setState(h[1+l.iright][j][0].getState());
				if (right[j][1] != null)
					right[j][1].setState(h[1+l.iright][j][1].getState());
			}

			for (int k=0; k<rec.length; k++)
				rec[k].fix(l.tleft,l.tright);

			ratios[i] += Math.exp( g.logPeel() - loglhalf );
		}

		count += 1;
	}

/** 
 Returns a matrix of doubles. The first index indicates the locus, the second indicates
 the value of theta. Each value is the total likelihood ratio
*/
	public double[] results()
	{
		double[] lods = new double[ratios.length];

		for (int i=0; i<lods.length; i++)
			lods[i] = Math.log10 ( ratios[i] /count );

		return lods;
	}

// Private data and methods.

	private LocInfo[] positions = null;
	private double loglhalf = 0;
	private Inheritance[][][] h = null;
	private Inheritance[][] right = null;
	private Inheritance[][] left = null;
	private TwoSidedRecombination[] rec = null;
	private GraphicalModel g = null;
	private double[] ratios = null;
	private double count = 0;
}
