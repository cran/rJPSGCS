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
 This class simulates one dimensional BrownianMotion.
*/
public class BrownianMotion extends StochasticProcess
{
	static { className = "BrownianMotion"; }

/**
 Creates a new Brownian motion with default variance parameter 1.
*/
	public BrownianMotion()
	{
		this(1);
	}

/**
 Creates a new Brownian motion simulator with the given variance parameter.
*/
	public BrownianMotion(double var)
	{
		X = new GaussianRV(0,1);
		set(var);
	}

/**
 Sets the variance parameter of the Brownian motion.
*/
	public void set(double var)
	{
		X.set(0,var);
	}

/**
 Returns the current point in space and time of the process.
 The runtime type of the state is Locus.
*/
	public StochasticState getState()
	{
		return new Locus(time,x);
	}

/**
 Sets the point in space and time of the process.
 The state must be a Locus.
*/
	public void setState(StochasticState s)
	{
		Locus l = (Locus)s;
		time = l.time;
		x = l.x;
	}

/**
 Advances the process t time units.
*/
	public void advance(double t)
	{
		time += t;
		x += X.next() * Math.sqrt(t);
	}
	
	private GaussianRV X = null;
	private double x = 0;
	private double time = 0;
}
