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
 This is the base class from which stochastic processes are
 derived.
*/
abstract public class StochasticProcess extends Random
{
	static String className = "StochasticProcess";

/**
 Returns the current state of the process.
*/
	abstract public StochasticState getState();
/**
 Sets the state of the process.
*/
	abstract public void setState(StochasticState s);
/**
 Advances the process the given time.
*/
	abstract public void advance(double time);

/*
 Avances the process to the next event.
*/
	//abstract public void next();

	public static void main(String[] args)
	{
		try
		{
			String s = "jpsgcs.alun.random."+className;
			System.out.println("\nDefault Main Method: "+s);
			StochasticProcess S = (StochasticProcess)(Class.forName(s).newInstance());
			for (int i=0; i<10; i++)
			{
				S.advance(10.0);
				System.out.println(S.getState());
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught in StochasticProcess:main()");
			e.printStackTrace();
		}
	}
}
