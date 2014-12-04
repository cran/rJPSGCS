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


package jpsgcs.alun.gchap;

/**
 A base class from which all observation, single or multi-locus are
 derived.
 An observation is a collection of phenotypes.
*/
abstract public class Observation
{
/**
 Returns the trait for which this is an observation.
*/
	public GeneticTrait getTrait()
	{
		return m;
	}

/**
 Returns the log likelihood for the given observation.
*/
	public double logLikelihood()
	{
		double ll = 0;
		for (int i=0; i<d.length; i++)
			ll += d[i].logLikelihood();
	
		return ll;
	}

/**
 Performs gene counting on this observation until 
 we get some sort of convergence.
*/
	public int geneCountToConvergence()
	{
		int jump = 10;
		int its = 0;
		int count = 0;
		double ll = logLikelihood();
		double t = 0;
		do
		{
			its += jump;
			getTrait().geneCount(jump);
			getTrait().downCode(0);
			t = ll;
			ll = logLikelihood();
			if (Math.abs(ll-t) < Double.MIN_VALUE)
				count++;
			else
				count = 0;
//System.err.println("Log likelihood = "+logLikelihood());
		}
		while (count < 10);
		return its;
	}

	public Phenotype[] getData()
	{
		return d;
	}

	protected GeneticTrait m = null;
	protected Phenotype[] d = null;
}
