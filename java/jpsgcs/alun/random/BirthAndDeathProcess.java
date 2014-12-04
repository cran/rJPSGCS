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
 This class simulates a birth and death process.
*/
public class BirthAndDeathProcess extends PoissonProcess
{
	static { className = "BirthAndDeathProcess"; }
	
/**
 Creates a new process with birth rate equal to death rate equal to 1.
*/
	public BirthAndDeathProcess()
	{
		this(1,1);
	}

/**
 Creates a new birth and death process with the given rates.
*/
	public BirthAndDeathProcess(double birthrate, double deathrate)
	{
		super();
		B = new BernoulliRV();
		set(birthrate,deathrate);
	}

/**
 Set the birth and death rates of the process.
*/
	public void set(double b, double d)
	{
		super.set(b+d);
		B.set(b/(b+d));
	}

/**
 Get the current state.
 The runtime type of the state is PopulationCount.
*/
	public StochasticState getState()
	{
		return new PopulationCount(time,births,deaths);
	}

/**
 Set the current state.
 The state must be a PopulationCount.
*/
	public void setState(StochasticState s)
	{
		super.setState(s);
		PopulationCount c = (PopulationCount)s;
		births = c.births;
		deaths = c.deaths;
	}

/**
 Advances the process to the next event.
*/
	public void next()
	{
		super.next();
		addBND(1);
	}

/*
 Advances the process to the n events.
*/
	public void next(int n)
	{
		super.next(n);
		addBND(n);
	}

/**
 Advances the process t time units.
*/
	public void advance(double t)
	{
		int c = count;
		super.advance(t);
		addBND(count - c);
	}

/**
 Advances the process to the next birth.
*/
	public void nextBirth()
	{
		int b = births;
		do
		{
			next();
		}
		while (births == b);
	}

/**
 Advances the process to the next death.
*/
	public void nextDeath()
	{
		int d = deaths;
		do
		{
			next();
		}
		while (deaths == d);
	}

/**
 Adds to the current counts of births and deaths.
*/
	private void addBND(int n)
	{
		for (int i=0; i<n; i++)
			if (B.nextB())
				births++;
			else
				deaths++;
	}

	private BernoulliRV B = null;
	private int births = 0;
	private int deaths = 0;
}
