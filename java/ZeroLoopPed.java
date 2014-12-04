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


import jpsgcs.alun.zeroloop.ZeroLoopPedGenerator;
import jpsgcs.alun.zeroloop.Individual;
import jpsgcs.alun.util.Main;

/**
	This program generates random zero loop pedigrees with specified numbers of 
	marriages and individuals.

<ul>
	Usage: <b> java ZeroLoopPed nmar nind [nsim] [-m/p] [-l] </b>
</ul>
	where
<ul>
	<li> <b> nmar </b> is the required number of marriages or matings. </li>
	<li> <b> nind </b> is the required number of individuals. This must be at least
				2 more than twice the number of matings. </li>
	<li> <b> nsims </b> is the number of pedigrees to simulate. The pedigrees will be 
				separated by a blank line in the output file. The default is 1. </li>
	<li> <b> -m </b> is an optional paramter. If m is specified individals will be monogamous. 
				This is false by default.</li>
	<li> <b> -l </b> is an optional paramter. If -l is specified the individuals will be relabelled
			in sequential order in the output file. The default is not to relabel.  </li>
</ul>
*/
public class ZeroLoopPed
{
	public static void main(String[] args)
	{
		try
		{
			int nmar = 10;
			int nind = 50;
			int sims = 1;
			boolean mono = false;
			boolean relabel = false;

			String[] bargs = Main.strip(args,"-l");
			if (bargs != args)
			{
				relabel = true;
				args = bargs;
			}

			bargs = Main.strip(args,"-m");
			if (bargs != args)
			{
				mono = true;
				args = bargs;
			}

			switch (args.length)
			{
			case 3: sims = new Integer(args[2]).intValue();

			case 2: nind = new Integer(args[1]).intValue();
				nmar = new Integer(args[0]).intValue();
				break;

			default:
				System.err.println("Usage: ZeroLoopPed nmar nind [nsims] [-m] [-l]");
				System.exit(1);
			}

			if (nind < 2*nmar+1)
			{
				System.err.println("Number of individuals must be > 1 + 2 * number of marriages");
				System.exit(1);
			}

			ZeroLoopPedGenerator z = new ZeroLoopPedGenerator(nmar,nind,mono,relabel);
			
			for (int i=0; i<sims; i++)
			{
				for (Individual j : z.next())
					System.out.println(j);
				if (i < sims-1)
					System.out.println();
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught in ZeroLoopPed");
			e.printStackTrace();
		}
	}
}
