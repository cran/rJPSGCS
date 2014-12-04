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
 Abstract class from which sampling with and without replacement
 are derived.
*/
abstract public class IntSampling extends IntegerValuedRV
{
/**
 Returns an array of the next integers sampled.
*/
	abstract public int[] nextI(int n);

/**
 Restarts the sampling process.
*/
	abstract public void restart();

/**
 Test main.
*/
	public static void main(String[] args)
	{
		try
		{
			String s = "jpsgcs.alun.random."+className;
			System.out.println("Default Main Method: "+s);
			IntSampling X = (IntSampling)(Class.forName(s).newInstance()); 

			for (int i=0; i<10; i++)
			{
				X.restart();
				int[] y = X.nextI(12);
				for (int j=0; j<y.length; j++)
					System.out.print(" "+y[j]);
				System.out.println();
			}
			for (int i=0; i<10; i++)
			{
				X.restart();
				int[] y = X.nextI(5);
				for (int j=0; j<y.length; j++)
					System.out.print(" "+y[j]);
				System.out.println();
			}
		}
		catch (Exception e)
		{
			System.out.println("Caught in IntSampling:main()");
			e.printStackTrace();
		}
	}
}
