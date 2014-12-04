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
 This class simulates a two dimensional Brownian motion.
*/
public class BrownianMotion2D extends StochasticProcess
{
	static { className = "BrownianMotion2D"; }

/**
 Creates a new 2d Brownian motion with variance parameters 1.
*/
	public BrownianMotion2D()
	{
		this(1,1);
	}

/**
 Creates a new 2d Brownian motion with the given variance parameters.
*/
	public BrownianMotion2D(double xvar, double yvar)
	{
		X = new GaussianRV(0,1);
		Y = new GaussianRV(0,1);
		set(xvar,yvar);
	}

/**
 Sets the variance parameters for the x and y dimensions.
*/
	public void set(double xvar, double yvar)
	{
		X.set(0,xvar);
		Y.set(0,yvar);
	}

/**
 Returns the current state.
 The runtime type of the state is Locus2D.
*/
	public StochasticState getState()
	{
		return new Locus2D(time,x,y);
	}

/**
 Sets the current state of the process.
 The state must be a Locus2D.
*/
	public void setState(StochasticState s)
	{
		Locus2D l = (Locus2D)s;
		time = l.time;
		x = l.x;
		y = l.y;
	}

/**
 Advances the process t time units.
*/
	public void advance(double t)
	{
		time += t;
		x += X.next() * Math.sqrt(t);
		y += Y.next() * Math.sqrt(t);
	}
	
	private GaussianRV X = null;
	private GaussianRV Y = null;
	private double time = 0;
	private double x = 0;
	private double y = 0;
}
