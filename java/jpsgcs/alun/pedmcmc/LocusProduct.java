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

import jpsgcs.alun.genio.BasicGeneticData;
import jpsgcs.alun.markov.MarkovRandomField;
import jpsgcs.alun.markov.Function;
import jpsgcs.alun.markov.Functions;
import jpsgcs.alun.markov.Variable;

public class LocusProduct extends MarkovRandomField
{
	protected LocusProduct()
	{
	}

	public LocusProduct(BasicGeneticData d, int i)
	{
		all = new Allele[d.nIndividuals()][2];
		inh = new Inheritance[d.nIndividuals()][2];
		
		for (int j=0; j<all.length; j++)
		{
			all[j][0] = new Allele(d.nAlleles(i));
			all[j][1] = new Allele(d.nAlleles(i));

			if (d.pa(j) >= 0)
				inh[j][0] = new Inheritance();
			if (d.ma(j) >= 0)
				inh[j][1] = new Inheritance();
		}

		for (int j=0; j<all.length; j++)
		{
			if (d.penetrance(i,j) != null)
			{
				Function f = new AllelePenetrance(all[j][0],all[j][1],d.penetrance(i,j));
				Functions.reduceStates(f);
				add(f);
			}
		}

		for (int j=0; j<inh.length; j++)
		{
			Function f = null;

			if (inh[j][0] != null && inh[j][1] != null)
			{
				f = new AlleleTransmission(all[d.pa(j)][0],all[d.pa(j)][1],inh[j][0],all[j][0]);
				Functions.reduceStates(f);
				add(f);
				f = new AlleleTransmission(all[d.ma(j)][0],all[d.ma(j)][1],inh[j][1],all[j][1]);
				Functions.reduceStates(f);
				add(f);
				continue;
			}

			if (inh[j][0] != null)
			{
				f = new AlleleTransmission(all[d.pa(j)][0], all[d.pa(j)][1], inh[j][0], all[j][0]);
				Functions.reduceStates(f);
				add(f);
			}
			else
			{
				f = new AllelePrior(all[j][0],d.alleleFreqs(i));
				Functions.reduceStates(f);
				add(f);
			}
				
			if (inh[j][1] != null)
			{
				f = new AlleleTransmission(all[d.ma(j)][0], all[d.ma(j)][1], inh[j][1], all[j][1]);
				Functions.reduceStates(f);
				add(f);
			}
			else
			{
				f = new AllelePrior(all[j][1],d.alleleFreqs(i));
				Functions.reduceStates(f);
				add(f);
			}
		}
	}
	
	public Inheritance[][] getInheritances()
	{
		return inh;
	}

	public Allele[][] getAlleles()
	{
		return all;
	}

// Private data and methods.

	protected Allele[][] all = null;
	protected Inheritance[][] inh = null;
}
