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
 This is the base class for all random number generators in
 the package.
*/
abstract public class RandomEngine
{
	static protected String className = "RandomEngine";

/**
 Repeated calls to this method are assumed to return independent
 realisation from a Uniform(0,1) distribution. 
 No checks are made on the distribution or of independence. It is
 up the to subclass to implement a sound method of generation.
*/
	abstract public double next();

/**
 The main method simply outputs some realisations from the random
 engine that can be input to statistical software to check the 
 properties. By default 1000 values are output, but a different number
 can be stated on the command line.
 To use this main method the subclass only has to reset the "className"
 string to its name, and provide a default, no argument, constructor.
 Using the main of RandomEngine itself will just throw an exception.
*/
	public static void main(String[] args)
	{
		try
		{
			String s = "jpsgcs.alun.random."+className;
			RandomEngine r = (RandomEngine)(Class.forName(s).newInstance());
			int n = 1000;
			if (args.length > 0)
				n = (new Integer(args[0])).intValue();
			for (int i=0; i<n; i++)
				System.out.println(r.next());
		}
		catch (Exception e)
		{
			System.out.println("Caught in RandomEngine:main()");
			e.printStackTrace();
		}
	}
}
