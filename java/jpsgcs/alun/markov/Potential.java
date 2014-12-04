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

import jpsgcs.alun.util.IntDoubleMap;
import jpsgcs.alun.util.IntDoubleItem;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.Collection;

public class Potential implements Function
{
	public void show()
	{
		double xx = 1.0;
		for (Variable v : invol())
			xx *= v.getNStates();
		System.err.print(xx+"  : ");
		
		for (Variable v : invol())
			System.err.print("Var "+v+" "+v.getNStates()+" ");

		System.err.println();

		xx = 1;
		for (Variable v : out.getVariables())
			xx *= v.getNStates();
		System.err.print(xx+"  : ");
		for (Variable v : out.getVariables())
			System.err.print("Var "+v+" "+v.getNStates()+" ");

		System.err.println();
	}

	public Potential(Collection<Variable> invols, Collection<Variable> outs, Collection<Function> pri, Collection<Function> like, Potential next)
	{
		this.next = next;
		//index = ++count;

		inputs = new Table[0];

		out = new MultiVariable(outs);
		output = new DenseTable(out);
		if (next != null)
			next.addInput(output);
		
		Set<Variable> s = new LinkedHashSet<Variable>(invols);
		s.removeAll(outs);
		peel = new MultiVariable(s);
		
		u = cat(peel.getVariables(),out.getVariables());

		cap = (int) (peel.getNStates()/1000.0);
		if (cap > 100000)
			cap = 100000;
		if (cap < 10)
			cap = 10;

		it = null;
		int best = 1000000000;
		for (Function p : pri)
			if (p instanceof Iterable && p.getVariables().length == u.length)
			{
				int n = ((Iterable)p).getNStates();
				if (best > n)
				{
					best = n;
					it = (Iterable)p;
				}
			}

		Set<Function> triv = new LinkedHashSet<Function>();
		for (Function p : pri)
			if (p instanceof Trivial)
				triv.add(p);
		pri.removeAll(triv);
		prior = (Function[]) pri.toArray(new Function[0]);

		triv.clear();
		for (Function p : like)
			if (p instanceof Trivial)
				triv.add(p);
		like.removeAll(triv);
		likely = (Function[]) like.toArray(new Function[0]);
	}

	public void allocate()
	{
		map = new IntDoubleMap[out.getNStates()];
	}

	public void free()
	{
		map = null;
	}

	public void storePotential()
	{
		pot = map;
	}

	public Variable[] getVariables()
	{
		return u;
	}

	public double getValue()
	{
		int iii = out.getState();
		if (map[iii] == null)
			return 0;
		return map[iii].get(peel.getState());
	}

	public Set<Variable> invol()
	{
		Set<Variable> s = new LinkedHashSet<Variable>();
		for (Variable v : out.getVariables())
			s.add(v);
		for (Variable v : peel.getVariables())
			s.add(v);

		return s;
	} 

	public Set<Variable> outvol()
	{
		Set<Variable> s = invol();
		for (Function f : likely)
			for (Variable v: f.getVariables())
				s.add(v);
		return s;
	}

	public void distribute(boolean margins)
	{
		for (Table f : inputs)
			f.clear();

		for (int i=0; i<map.length; i++)
			if (map[i] != null)
			{
				out.setState(i);
				double y = output.getValue();

				if (y > 0)
				{
					scale(map[i],y);

					for (IntDoubleItem x = map[i].head(); x != null; x = x.next)
					{
						peel.setState(x.i);

						for (Table f : inputs)
							f.increase(x.d);
					}

					if (!margins)
						scale(map[i],1.0/y);
				}
				else
				{
					map[i] = null;
				}
			}
	}

	public Map<Variable,Table> margins()
	{
		Map<Variable,Table> m = new LinkedHashMap<Variable,Table>();
		Table[] tabs = new Table[invol().size()];
		int k=0;
		for (Variable v : invol())
		{
			tabs[k] = new DenseTable(v);
			tabs[k].allocate();
			m.put(v,tabs[k]);
			k++;
		}
		
		for (int i=0; i<map.length; i++)
			if (map[i] != null)
			{
				out.setState(i);
				for (IntDoubleItem x = map[i].head(); x != null; x = x.next)
				{
					peel.setState(x.i);
					for (Table f : tabs)
						f.increase(x.d);
				}
			}

		return m;
	}

	public DenseTable asFunction()
	{
		DenseTable t = new DenseTable((Variable[])invol().toArray(new Variable[0]));
		t.allocate();
		
		for (int i=0; i<pot.length; i++)
			if (pot[i] != null)
			{
				out.setState(i);
				for (IntDoubleItem x = pot[i].head(); x != null; x = x.next)
				{
					peel.setState(x.i);
					t.increase(x.d);
				}
			}

		return t;
	}

	public void drop()
	{
		double x = Math.random();
		double t = 0;
		
//System.err.println("NULL IS "+map+"\t"+map[out.getState()]);
		for (IntDoubleItem y = map[out.getState()].head(); y != null; y = y.next)
		{
			t += y.d;
			if (t >= x)
			{
				peel.setState(y.i);
				break;
			}
		}
	}

	public void collect(boolean posterior)
	{
		if (pot != null)
		{
			potentialCollect(posterior);
		}
		else if (it != null)
		{
			iteratorCollect(posterior);
		}
		else
		{
			genericCollect(posterior);
		}

		for (int i=0; i<map.length; i++)
			if (map[i] != null)
			{
				double y = sum(map[i]);
				if (y > 0)
				{
					output.tab[i] = y;
					scale(map[i],1.0/y);
				}
				else
				{
					output.tab[i] = 0;
					map[i] = null;
				}
			}
	}

	public void max(boolean posterior)
	{
		if (pot != null)
		{
			potentialCollect(posterior);
		}
		else if (it != null)
		{
			iteratorCollect(posterior);
		}
		else
		{
			genericCollect(posterior);
		}

		for (int i=0; i<map.length; i++)
			if (map[i] != null)
			{
				double y = max(map[i]);

				if (y > 0)
				{
					output.tab[i] = y;
					y = removeNonMaxes(map[i],y);
					scale(map[i],1.0/y);
				}
				else
				{
					output.tab[i] = 0;
					map[i] = null;
				}
			}
		
	}
	
// Private data and methods.

	private IntDoubleMap[] map = null;
	private IntDoubleMap[] pot = null;
	private Variable[] u = null;
	private int cap = 0;
	private Iterable it = null;

	private MultiVariable out = null;
	private MultiVariable peel = null;
	private Table[] inputs = null;
	private DenseTable output = null;
	private Function[] prior = null;
	private Function[] likely = null;

	public Potential next = null;
	//public static int count = 0;
	//public int index = 0;

	protected Table output()
	{
		return output;
	}

	protected Table[] inputs()
	{
		return inputs;
	}

	private double sum(IntDoubleMap m)
	{
		double y = 0;
		for (IntDoubleItem x = m.head(); x != null; x = x.next)
			y += x.d;
		return y;
	}

	private double removeNonMaxes(IntDoubleMap m, double y)
	{
		double tot = 0;

		for (IntDoubleItem x = m.head(); x != null; )
		{
			IntDoubleItem z = x;
			x = x.next;
		
			if (z.d < y)
				m.remove(z);
			else
				tot += z.d;
		}

		return tot;
	}

	private double max(IntDoubleMap m)
	{
		double y = 0;
		for (IntDoubleItem x = m.head(); x != null; x = x.next)
			if (y < x.d)
				y = x.d;
		return y;
	}

	private void scale(IntDoubleMap m, double y)
	{
		for (IntDoubleItem x = m.head(); x != null; x = x.next)
			x.d *= y;
	}

	private void addInput(Table f)
        {
                Set<Table>  s = new LinkedHashSet<Table>();
                if (inputs != null)
                        for (Table g : inputs)
                                s.add(g);
                s.add(f);
                inputs = (Table[]) s.toArray(new Table[0]);
        }

	private Function[][] orderInputs(boolean posterior)
	{
		Set<Function>[] ff = (Set<Function>[]) new Set[u.length];
		for (int i=0; i<u.length; i++)
			ff[i] = new LinkedHashSet<Function>();
		
		if (posterior)
			for (Function f : likely)
			{
				Set<Variable> s = Functions.asSet(f.getVariables());
				for (int j=u.length-1; j>=0; j--)
					if (s.contains(u[j]))
					{
						ff[j].add(f);
						break;
					}
			}	

		for (Function f : prior)
		{
			Set<Variable> s = Functions.asSet(f.getVariables());
			for (int j=u.length-1; j>=0; j--)
				if (s.contains(u[j]))
				{
					ff[j].add(f);
					break;
				}
		}	
		
		for (Function f : inputs)
		{
			Set<Variable> s = Functions.asSet(f.getVariables());
			for (int j=u.length-1; j>=0; j--)
				if (s.contains(u[j]))
				{
					ff[j].add(f);
					break;
				}
		}	
		
		Function[][] fff = new Function[u.length][];
		for (int i=0; i<fff.length; i++)
			fff[i] = (Function[]) ff[i].toArray(new Function[0]);

		return fff;
	}

	private Variable[] cat(Variable[] p, Variable[] q)
	{
		Variable[] out = new Variable[p.length+q.length];
		int i = 0;
		for (int j=0; j<p.length; j++)
			out[i++] = p[j];
		for (int j=0; j<q.length; j++)
			out[i++] = q[j];
		return out;
/*
		TreeSet<Variable> v = new TreeSet<Variable>();
		for (Variable x : p)
			v.add(x);
		for (Variable x : q)
			v.add(x);
		return (Variable[]) v.toArray(new Variable[0]);
*/
	}

	private void potentialCollect(boolean posterior)
	{
		for (int i=0; i<pot.length; i++)
		{
			map[i] = null;

			if (pot[i] != null)
			{
				out.setState(i);

				for (IntDoubleItem x = pot[i].head(); x != null; x = x.next)
				{
					peel.setState(x.i);
	
					double y = x.d;
	
					if (posterior)
					{
						for (Function f : likely)
							y *= f.getValue();
						if (y <= 0)
							continue;
					}
	
					for (Function f :inputs)
						y *= f.getValue();
					if (y <= 0)
						continue;
		
					if (map[i] == null)
						map[i] = new IntDoubleMap(cap);
					IntDoubleItem t = map[i].force(x.i);
					t.d = y;
				}
			}
		}
	}

	private void iteratorCollect(boolean posterior)
	{
		for (it.init(); it.next(); )
		{
			double x = 1.0;
			
			if (posterior)
			{
				for (Function f : likely)
					x *= f.getValue();
				if (x <= 0)
					continue;
			}

			for (Function f : prior)
				x *= f.getValue();
			if (x <= 0)
				continue;

			for (Function f : inputs)
				x *= f.getValue();

			if (x > 0)
			{
				int iii = out.getState();
				if (map[iii] == null)
					map[iii] = new IntDoubleMap(cap);
				int jjj = peel.getState();
				IntDoubleItem t = map[iii].force(jjj);
				t.d += x;
			}
		}
	}

	private void genericCollect(boolean posterior)
	{
		Function[][] f = orderInputs(posterior);

/*
int cc = 0;
for (int i=0; i<f.length; i++)
	cc += f[i].length;
System.err.println(invol()+"\t"+cc);
*/

		for (int i=0; i<u.length; i++)
			u[i].init();

		double[] x = new double[u.length+1];
		x[0] = 1;
		
		for (int i=0; i>=0; )
		{
			if (!u[i].next())
			{
				u[i].init();
				i--;
			}
			else
			{
				x[i+1] = x[i];
				for (int j=0;  x[i+1] > 0 && j <f[i].length; j++)
					x[i+1] *= f[i][j].getValue();
				
				if (x[i+1] <= 0)
					continue;
				
				if (++i == u.length)
				{
					int iii = out.getState();
					if (map[iii] == null)
						map[iii] = new IntDoubleMap(cap);

					int jjj = peel.getState();
					IntDoubleItem t = map[iii].force(jjj);
					t.d = x[i];
				
					i--;
				}
			}
		}
	}
}
