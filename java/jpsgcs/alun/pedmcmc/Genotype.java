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

import jpsgcs.alun.markov.Variable;

/**
 A variable representing the ordered genotype for an
 individual as some genetic locus.
*/
public class Genotype extends Variable
{
	public Genotype(int n)
	{
		super(n*n);
		na = n;
	}

/**
 Returns the state of the paternal allele for this genotype.
*/
	public int pat()
	{
		return getState()/na;
	}

/**
 Return the state of the maternal allele for this genotype.
*/
	public int mat()
	{
		return getState()%na;
	}

	public String toString()
	{
		return "G("+super.toString()+")";
	}

// Private data.

	protected int na = 0;
}
