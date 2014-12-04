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
 Implements the rejection method of generation.
*/
public class Rejection extends GenerationMethod
{
/**
 Creates a new rejection scheme for the given target using the given
 envelope and multiplier.
 The density function of the target must be less than the density function
 of the envelope times the multiplier everywhere.
*/
	public Rejection(DensityFunction target, Envelope envelope, double multiplier)
	{
		X = target;
		Y = envelope;
		m = multiplier;
	}

/**
 Applies the method and returns the generated value.
*/
	public double apply()
	{
		Point p = null;
		do 
		{
			p = Y.nextPoint();
		}
		while (U() > X.densityFunction(p.x)/p.y/m);
		return p.x;
	}

/**
 Returns the target density.
*/
	public DensityFunction getTarget()
	{
		return X;
	}

/**
 Returns the envelope density.
*/
	public Envelope getEnvelope()
	{
		return Y;
	}

/**
 Sets the current envelope.
*/
	public void setEnvelope(Envelope d)
	{
		Y = d;
	}

	private DensityFunction X = null;
	private Envelope Y = null;
	private double m = 1;
}
