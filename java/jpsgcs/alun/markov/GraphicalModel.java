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


package jpsgcs.alun.markov;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;


public class GraphicalModel
{
	public GraphicalModel(MarkovRandomField p, boolean compit)
	{
		ModelInfo[] m = p.compile();
		Map<Set<Variable>,Potential> h = new LinkedHashMap<Set<Variable>,Potential>();
		c = new Potential[m.length];

		for (int i=c.length-1; i>=0; i--)
		{
			Set<Variable> out = new LinkedHashSet<Variable>(m[i].clique);
			if (m[i].next != null)
				out.retainAll(m[i].next);
			else
				out.clear();

/*
System.out.println("Potential "+i+"\t"+m[i].clique+"\t"+m[i].func.size()+"\t"+m[i].evid.size());
if (m[i].func.size() > 100)
	for (Function f : m[i].func)
		System.out.println(f);
*/

			c[i] = new Potential(m[i].clique,out,m[i].func,m[i].evid,h.get(m[i].next));

		//	c[i].index = i;
			c[i].next = h.get(m[i].next);
			h.put(m[i].clique,c[i]);
		}

		if (compit)
			compile();
	}

	public Potential[][] splitByOutvol(Variable[] vs)
	{
		Set<Variable> s = new LinkedHashSet<Variable>();
		for (Variable v : vs)
			s.add(v);
		return splitByOutvol(s);
	}

	public Set<DenseTable> potentialFunctions()
	{
		Set<DenseTable> s = new LinkedHashSet<DenseTable>();
		for (int i=0; i<c.length; i++)
			s.add(c[i].asFunction());
		return s;
	}

	public Potential[][] splitByOutvol(Set<Variable> s)
	{
		Set<Potential> a = new LinkedHashSet<Potential>();
		Set<Potential> b = new LinkedHashSet<Potential>();

		for (int i=0; i<c.length; i++)
		{
			if (b.contains(c[i]))
				continue;

			Set<Variable> out = c[i].outvol();
			out.retainAll(s);

			if (out.isEmpty())
			{
				a.add(c[i]);
			}
			else
			{
				for (Potential p = c[i]; p != null && !b.contains(p); p = p.next)
					b.add(p);
			}
		}

		Potential[][] res = new Potential[2][];
		res[0] = new Potential[a.size()];
		res[1] = new Potential[b.size()];
		for (int i=0, j=0, k=0; i<c.length; i++)
		{
			if (a.contains(c[i]))
				res[0][j++] = c[i];
			else if (b.contains(c[i]))
				res[1][k++] = c[i];
		}
		
		return res;
	}

	public double preLogPeel(Potential[] p, boolean usepost)
	{
		double result = 0;
		
		for (int i=0; i<p.length; i++)
		{
			p[i].output().allocate();
			p[i].allocate();
			p[i].collect(usepost);
			p[i].free();
			for (Table t : p[i].inputs())
				t.free();

			double x = p[i].output().sum();
			if (x > 0)
			{
				p[i].output().scale(1.0/x);
				result += Math.log(x);
			}
			else
			{
				p[i].output().free();
				return Double.NEGATIVE_INFINITY;
			}
		}

		return result;
	}

	public double postLogPeel(Potential[] p, boolean usepost)
	{
		double result = 0;
		
		for (int i=0; i<p.length; i++)
		{
			p[i].output().allocate();
			p[i].allocate();
			p[i].collect(usepost);
			p[i].free();
			
			double x = p[i].output().sum();
			if (x > 0)
			{
				p[i].output().scale(1.0/x);
				result += Math.log(x);
			}
			else
			{
				p[i].output().free();
				return Double.NEGATIVE_INFINITY;
			}
		}

		return result;
	}

	public void cleanUpOutputs()
	{
		for (int i=0; i<c.length; i++)
			c[i].output().free();
	}

	public double logPeel()
	{
		return logPeel(true);
	}

	public double logPeel(boolean usepost)
	{
		double result = 0;

		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(usepost);
			c[i].free();
			for (Table t : c[i].inputs())
				t.free();

			double x = c[i].output().sum();
			if (x > 0)
			{
				c[i].output().scale(1.0/x);
				result += Math.log(x);
			}
			else
			{
				c[i].output().free();
				return Double.NEGATIVE_INFINITY;
			}
		}

		c[c.length-1].output().free();
		return result;
	}

	public double peel()
	{	
		return peel(true);
	}

	public double peel(boolean usepost)
	{
		return Math.exp(logPeel(usepost));
	}

	public void simulate()
	{
		simulate(true);
	}

	public void simulate(boolean usepost)
	{
		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(usepost);
/*
System.err.println("\n"+i);
c[i].show();
System.err.println("Sum "+c[i].output().sum());
if (c[i].output().sum() <= 0)
System.exit(1);
*/
			c[i].output().scale(1.0/c[i].output().sum());
		}

		for (int i=c.length-1; i>=0; i--)
		{
//System.err.println("Dropping "+i);
			c[i].drop();
			c[i].free();
			c[i].output().free();
		}
	}

	public void maximize()
	{
		maximize(true);
	}

	public void maximize(boolean usepost)
	{
		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].max(usepost);
			c[i].output().scale(1.0/c[i].output().sum());
		}

		for (int i=c.length-1; i>=0; i--)
		{
			c[i].drop();
			c[i].free();
			c[i].output().free();
		}
	}

	public void collect()
	{
		collect(true);
	}

	public void collect(boolean usepost)
	{
		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(usepost);
			c[i].output().scale(1.0/c[i].output().sum());
		}
	}

	public void drop()
	{
		for (int i=c.length-1; i>=0; i--)
			c[i].drop();
	}

	public double compile()
	{
		double x = 0;

		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(false);

			x = c[i].output().sum();

/*
System.err.println("\n"+i);
c[i].show();
System.err.println("Sum "+c[i].output().sum());
if (c[i].output().sum() <= 0)
System.exit(1);
*/
			
			c[i].output().scale(1.0/x);
		}

		for (int i=c.length-1; i>=0; i--)
		{
			c[i].distribute(false);
			c[i].output().free();
			c[i].storePotential();
			c[i].free();
		}

		return x;
	}

	public void marginalize()
	{
		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(false);
			c[i].output().scale(1.0/c[i].output().sum());
		}
	
		marg = new LinkedHashMap<Variable,Table>();

		for (int i=c.length-1; i>=0; i--)
		{
			c[i].distribute(true);
			c[i].output().free();
			marg.putAll(c[i].margins());
			c[i].free();
		}
	}

	public Table margin(Variable v)
	{
		return marg.get(v);
	}

	public void reduceStates()
	{
		for (int i=0; i<c.length; i++)
		{
			c[i].output().allocate();
			c[i].allocate();
			c[i].collect(false);
			c[i].output().scale(1.0/c[i].output().sum());
		}

		for (int i=c.length-1; i>=0; i--)
		{
			c[i].distribute(false);
			c[i].output().free();
		}

		for (int i=0; i<c.length; i++)
		{
			Functions.reduceStates(c[i]);
			c[i].free();
		}
	}

// Private data.

	private Potential[] c = null;
	private Map<Variable,Table> marg = null;
}
