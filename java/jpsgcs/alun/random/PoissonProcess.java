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


package jpsgcs.alun.random;

/**
 This class simulates a Poisson point process.
*/
public class PoissonProcess extends DiscreteStochasticProcess
{
	static { className = "PoissonProcess"; }
	
/**
 Creates a new Poisson process with rate 1.
*/
	public PoissonProcess()
	{
		this(1);
	}

/**
 Creates a new Poisson process with the given rate.
*/
	public PoissonProcess(double rate)
	{
		E = new ExponentialRV();
		G = new GammaRV();
		P = new PoissonRV();
		set(rate);
	}

/**
 Sets the rate of the process.
*/
	public void set(double rate)
	{
		E.set(rate);
		r = rate;
	}

/**
 Advances the process to the time of the next event.
*/
	public void next()
	{
		time += E.next();
		count++;
	}

/**
 Advances the process to the time of next nth event.
*/
	public void next(int n)
	{
		G.set(n,r);
		time += G.next();
		count += n;
	}
	
/**
 Advances the process the given time unit.
*/
	public void advance(double t)
	{
		P.set(r*t);
		count += P.next();
		time += t;
	}

/**
 Sets the current state of the process.
 The stochastic state must be a Count.
*/
	public void setState(StochasticState s)
	{
		Count c = (Count)s;
		time = c.time;
		count = c.count;
	}

/**
 Gets the current state of the process.
 The runtime class of the stochastic state is Count.
*/
	public StochasticState getState()
	{
		return new Count(time,count);
	}

	protected double time = 0;
	protected int count = 0;

	private PoissonRV P = null;
	private ExponentialRV E = null;
	private GammaRV G = null;
	private double r = 1;
}
