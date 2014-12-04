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
 The class from which all random variables descend.
 By definition a random variable is a real number which
 is the result of some random process.
*/
abstract public class RandomVariable extends Random
{
	static String className = "RandomVariable";

/**
 Return the next replicate of this random variable.
 By default this is done by delegating to a standard 
 generating method.
*/
	public double next()
	{
		return m.apply();
	}

/**
 Set the method used by this random variable to generate
 the next instance.
*/
	public final void setMethod(GenerationMethod g)
	{
		m = g;
	}

//-
/** 
 Get the method used to generate the next instance.
*/
	public final GenerationMethod getMethod()
	{
		return m;
	}
	
	private GenerationMethod m = null;
//:
	
/**
 This main method can be used by any subclasses that overwrite
 the "className" string to be their own class name, and provide
 a default constructor.
 As a quick check on the programs the mean and variance of 1000
 replicates from the distribution is printed.
*/
 
	public static void main(String[] args)
	{
//.
		try
		{
			String s = "jpsgcs.alun.random."+className;
			System.out.println("Default Main Method: "+s);
			RandomVariable X = (RandomVariable)(Class.forName(s).newInstance()); 

			double m = 0;
			double t = 0;
			int n = 1000;
			if (args.length > 0)
				n = (new Integer(args[0])).intValue();
			for (int i=0; i<n; i++)
			{
				double x = X.next();
				m += x;
				t += x*x;
			}

			System.out.println("Number\t\t=\t"+n);
			System.out.println("Mean\t\t= \t"+m/n);
			System.out.println("Variance\t=\t"+(t-m*m/n)/n);
		}
		catch (Exception e)
		{
			System.out.println("Caught in RandomVariable:main()");
			e.printStackTrace();
		}
//:
	}
}
