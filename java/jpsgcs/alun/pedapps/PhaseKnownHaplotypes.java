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

public class PhaseKnownHaplotypes extends CompleteHaplotypes
{
	public PhaseKnownHaplotypes(GeneticDataSource x, int hold)
	{
		data = x;
		haps = new int[2*data.nIndividuals()][hold];
		offset = 0;

		for (int j=0; j<data.nIndividuals(); j++)
		{
			for (int i=0; i<haps[0].length; i++)
			{
				int a0 = data.getAllele(i,j,0);
				int a1 = data.getAllele(i,j,1);

				if (a0 < 0)
				{
					System.err.println("Warning: Missing allele data in phase known genotypes.");
					System.err.println("Setting to 0");
					a0 = 0;
				}
			
				if (a1 < 0)
				{
					System.err.println("Warning: Missing allele data in phase known genotypes.");
					System.err.println("Setting to 0");
					a1 = 0;
				}

				haps[2*j][i] = a0;
				haps[2*j+1][i] = a1;
			}
		}

/*
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
*/
	}

/*
	public CompleteHaplotypes( x, double eprob)
	{
		this (x,eprob,x.nLoci());
	}

	public CompleteHaplotypes(BasicGeneticData x, LDModel ld, double eprob)
	{
		this(x,eprob);
		setModel(ld);
	}
*/

	public void simulate() { }

	public void maximize() { }

	public void update(boolean random)
	{

/*
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
*/


	}

	public void shift(int n)
	{
		int m = n;
		if (offset + haps[0].length + m > data.nLoci())
			m = data.nLoci() - haps[0].length - offset;
		
		offset += m;

/*
		for (int j=0; j<haps.length; j++)
		{
			for (int i=0; i<haps[j].length-m; i++)
				haps[j][i] = haps[j][i+m];
			for (int i=haps[j].length-m; i<haps[j].length; i++)
				haps[j][i] = 0;
		}
*/

		for (int j=0; j<data.nIndividuals(); j++)
		{
			for (int i=0; i<haps[0].length-m; i++)
			{
				haps[2*j][i] = haps[2*j][i+m];
				haps[2*j+1][i] = haps[2*j+1][i+m];
			}

			for (int i=haps[0].length-m; i<haps[0].length; i++)
			{
				int a0 = data.getAllele(i+offset,j,0);
				int a1 = data.getAllele(i+offset,j,1);

				if (a0 < 0)
				{
					System.err.println("Warning: Missing allele data in phase known genotypes.");
					System.err.println("Setting to 0");
					a0 = 0;
				}
			
				if (a1 < 0)
				{
					System.err.println("Warning: Missing allele data in phase known genotypes.");
					System.err.println("Setting to 0");
					a1 = 0;
				}

				haps[2*j][i] = a0;
				haps[2*j+1][i] = a1;
			}
		}
	}

	public void setModel(LDModel ld)
	{
	}
}
