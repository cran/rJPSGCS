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
 This class simulates a renewal process.
*/
public class RenewalProcess extends DiscreteStochasticProcess
{
	static { className = "RenewalProcess"; }
	
/**
 Creates a new renewal process with Exponential(1) lifetimes.
 This is equivalent to a Poisson process of rate 1.
*/
	public RenewalProcess()
	{
		this(new ExponentialRV());
	}

/**
 Creates a new renewal process with the specified lifetime distribution.
*/
	public RenewalProcess(RandomVariable lifetime)
	{
		set(lifetime);
		time = 0;
		count = 0;
		last = 0;
		nexttime = X.next();
	}

/**
 Sets the lifetime distribution.
*/
	public void set(RandomVariable life)
	{
		X = life;
	}

/**
 Gets the current state of the process.
 The runtime type of the state is RenewalCount.
*/
	public StochasticState getState()
	{
		return new RenewalCount(time,last,count);
	}

/**
 Sets the current state of the process.
 The state must be a RenewalCount.
 The current lifetime length is set by simple rejection
 This means that if the time between the last event and the current
 time is improbably large this method may take a long time.
 If the conditional density of X/X>c is know for a subclass
 this method should be overwritten to take advantage of this.
*/
	public void setState(StochasticState s)
	{
		RenewalCount c = (RenewalCount)s;
		if (c.time < c.last)
			throw new ParameterException("RenewalProcess time of last event must be before current time");
		time = c.time;
		count = c.count;
		last = c.last;

		do 
		{
			nexttime = last+X.next();
		}
		while (nexttime < time);
	}

/**
 Advances the process to the next event.
*/
	public void next()
	{
		last = time = nexttime;
		nexttime += X.next();
		count++;
	}

/**
 Advances the process n events.
*/
	public void next(int n)
	{
		for (int i=0; i<n; i++)
			next();
	}
	
/**
 Advances the process t time units.
*/
	public void advance(double t)
	{
		time += t;
		while (nexttime < time)
		{
			last = nexttime;
			nexttime += X.next();
			count++;
		} 
	}

	private RandomVariable X = null;
	private double time = 0;
	private double last = 0;
	private int count = 0;
	private double nexttime = 0;

/**
 Test main.
*/
	public static void main(String[] args)
	{
		RenewalProcess R = new RenewalProcess();
		for (int i=0; i<10; i++)
		{
			R.next();
			System.out.println(R.getState());
		}
		System.out.println();

		for (int i=0; i<10; i++)
		{
			R.advance(1);
			System.out.println(R.getState());
		}
		System.out.println();

		R.setState(new RenewalCount(14,0,0));
		for (int i=0; i<10; i++)
		{
			R.next();
			System.out.println(R.getState());
		}
	}
}
