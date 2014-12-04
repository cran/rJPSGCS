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


package jpsgcs.alun.markov;

import jpsgcs.alun.util.*;
import java.util.Set;

public class DenseTable implements Table
{
	public DenseTable(MultiVariable v)
	{
		m = v;
	}

	public DenseTable(Variable[] vars)
	{
		this (new MultiVariable(vars));
	}

	public DenseTable(Set<Variable> vars)
	{
		this (new MultiVariable(vars));
	}

	public DenseTable(Variable v)
	{
		this (new MultiVariable(v));
	}

	public Variable[] getVariables()
	{
		return m.getVariables();
	}

	public MultiVariable getMultiVariable()
	{
		return m;
	}

	public void allocate()
	{
		try
		{
			tab = new double[m.getNStates()];
		}
		catch (OutOfMemoryError e)
		{
			System.err.println("Cannot allocate table of length "+m.getNStates()+" in DenseTable.allocate()");
			throw(e);
		}
	}

	public void free()
	{
		tab = null;
	}

	public double getValue()
	{
		return tab[m.getState()];
	}

	public void setValue(double d)
	{
		tab[m.getState()] = d;
	}

	public void increase(double d)
	{
		tab[m.getState()] += d;
	}

	public double sum()
	{
		double t = 0;
		for (int i=0; i<tab.length; i++)
			t += tab[i];
		return t;
	}

	public double logLikelihood()
	{
		double ll = 0;
		double nn = 0;

		for (int i=0; i<tab.length; i++)
			if (tab[i] > 0)
			{
				ll += tab[i] * Math.log(tab[i]);
				nn += tab[i];
			}

		return ll - nn * Math.log(nn);
	}

	public double degreesOfFreedom()
	{
		return tab.length - 1;
	}

	public void scale(double d)
	{
		for (int i=0; i<tab.length; i++)
			if (tab[i] > 0)
				tab[i] *= d;
	}

	public void invert()
	{
		for (int i=0; i<tab.length; i++)
			if (tab[i] > 0)
				tab[i] = 1.0/tab[i];
	}

	public void clear()
	{
		for (int i=0; i<tab.length; i++)
			tab[i] = 0;
	}
 
	public String toString()
	{
		return "Df"+m.toString();
	}

	public double[] getTable()
	{
		return tab;
	}

	public void setTable(double[] t)
	{
		tab = t;
	}

// Private data and methods.

	protected double[] tab = null;
	private MultiVariable m = null;
}
