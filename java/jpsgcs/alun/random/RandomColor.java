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

import java.awt.Color;

/**
 This class generates colours at random.
*/
public class RandomColor extends RandomBag
{
/**
 The default set of colours.
*/
	public static final Color[] defaultColors = 
		{ Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.magenta };

/**
 Creates a new random bag containing the default set of
 colours.
*/
	public RandomColor()
	{
		this(defaultColors);
	}	

/**
 Creates a new random bag and puts in the given array of 
 colours.
*/
	public RandomColor(Color[] c)
	{
		super();
		for (int i=0; i<c.length; i++)
			add(c[i]);
	}

/**
 Returns the next colour.
*/
	public Color nextC()
	{
		return (Color) next();
	}
}
