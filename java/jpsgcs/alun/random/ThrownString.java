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
 This class implements Kingman's model of the thrown string.
 See the RSS series B paper.
 This is a finite element approximation to the model.
*/
public class ThrownString extends StochasticProcess
{
	static { className = "ThrownString"; }

/**
 Creates a new thrown string with wiggliness parameter 1.
*/
	public ThrownString()
	{
		this(1);
	}

/**
 Creates a new thrown string with wiggliness parameter set
 to the given value.
*/
	public ThrownString(double wiggly)
	{
		T = new BrownianMotion();
		set(wiggly);
	}

/**
 Sets the value of the wiggliness parameter.
*/
	public void set(double wiggly)
	{
		if (wiggly <= 0)
			throw new ParameterException("ThrownString must have positive wiggliness");
		T.set(wiggly);
	}

/**
 Advances the process t time units.
 The smaller the advances the closer the finite element approximation.
*/
	public void advance(double t)
	{
		T.advance(t);
		double theta = ((Locus)T.getState()).x;
		x += Math.cos(theta);
		y += Math.sin(theta);
		time += t;
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
 Gets the current state of the process.
 The runtime class of the state is Locus2D.
*/
	public StochasticState getState()
	{
		return new Locus2D(time,x,y);
	}
	
	private BrownianMotion T = null;
	private double x = 0;
	private double y = 0;
	private double time = 0;
}
