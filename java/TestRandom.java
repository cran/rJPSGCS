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



import jpsgcs.alun.random.AdaptiveRejection;
import jpsgcs.alun.random.AhrensBinomial;
import jpsgcs.alun.random.AhrensDieter;
import jpsgcs.alun.random.AliasRejection;
import jpsgcs.alun.random.BernoulliRV;
import jpsgcs.alun.random.BetaRV;
import jpsgcs.alun.random.BinomialRV;
import jpsgcs.alun.random.BirthAndDeathProcess;
import jpsgcs.alun.random.BrownianMotion2D;
import jpsgcs.alun.random.BrownianMotion;
import jpsgcs.alun.random.BuiltInGenerator;
import jpsgcs.alun.random.CauchyRV;
import jpsgcs.alun.random.ChengFeast;
import jpsgcs.alun.random.ChiSquaredRV;
import jpsgcs.alun.random.CongruentialGenerator;
import jpsgcs.alun.random.Count;
import jpsgcs.alun.random.DegenerateIntegerRV;
import jpsgcs.alun.random.DegenerateRV;
import jpsgcs.alun.random.DensityFunction;
import jpsgcs.alun.random.DifferentiableLogConcave;
import jpsgcs.alun.random.DiscreteStochasticProcess;
import jpsgcs.alun.random.Envelope;
import jpsgcs.alun.random.ExponentialMixture;
import jpsgcs.alun.random.ExponentialRV;
import jpsgcs.alun.random.ExponentialSwitching;
import jpsgcs.alun.random.FiniteStateMC;
import jpsgcs.alun.random.FisherRV;
import jpsgcs.alun.random.GammaRV;
import jpsgcs.alun.random.GaussianRV;
import jpsgcs.alun.random.GeneralExponentialRV;
import jpsgcs.alun.random.GenerationMethod;
import jpsgcs.alun.random.GeometricRV;
import jpsgcs.alun.random.HotBitsEngine;
import jpsgcs.alun.random.IntegerMethod;
import jpsgcs.alun.random.IntegerValuedRV;
import jpsgcs.alun.random.IntSampling;
import jpsgcs.alun.random.IntWithoutReplacement;
import jpsgcs.alun.random.IntWithReplacement;
import jpsgcs.alun.random.InverseDistribution;
import jpsgcs.alun.random.Inversion;
import jpsgcs.alun.random.Locus2D;
import jpsgcs.alun.random.Locus;
import jpsgcs.alun.random.LogConcave;
import jpsgcs.alun.random.MarkovChain;
import jpsgcs.alun.random.MarkovState;
import jpsgcs.alun.random.Metropolis;
import jpsgcs.alun.random.Metropolitan;
import jpsgcs.alun.random.MetropolitanMC;
import jpsgcs.alun.random.Mixture;
import jpsgcs.alun.random.NegativeBinomialRV;
import jpsgcs.alun.random.NormalRV;
import jpsgcs.alun.random.ParameterException;
import jpsgcs.alun.random.PoissonProcess;
import jpsgcs.alun.random.PoissonRV;
import jpsgcs.alun.random.PopulationCount;
import jpsgcs.alun.random.PseudoRandomEngine;
import jpsgcs.alun.random.Rand;
import jpsgcs.alun.random.RandomBag;
import jpsgcs.alun.random.RandomColor;
import jpsgcs.alun.random.RandomEngine;
import jpsgcs.alun.random.Random;
import jpsgcs.alun.random.RandomObject;
import jpsgcs.alun.random.RandomPermutation;
import jpsgcs.alun.random.RandomUrn;
import jpsgcs.alun.random.RandomVariable;
import jpsgcs.alun.random.RandomWalk;
import jpsgcs.alun.random.RatioOfUniforms;
import jpsgcs.alun.random.RatioRegion;
import jpsgcs.alun.random.Rejection;
import jpsgcs.alun.random.RenewalCount;
import jpsgcs.alun.random.RenewalProcess;
import jpsgcs.alun.random.Sampler;
import jpsgcs.alun.random.Sampling;
import jpsgcs.alun.random.StochasticProcess;
import jpsgcs.alun.random.StochasticState;
import jpsgcs.alun.random.StudentsRV;
import jpsgcs.alun.random.ThrownString;
import jpsgcs.alun.random.UniformIntegerRV;
import jpsgcs.alun.random.UniformRV;
import jpsgcs.alun.random.VonMisesRV;
import jpsgcs.alun.random.WeibullRV;
import jpsgcs.alun.random.WichmanHill;

/**
 This is a simple program to demonstrate using random variable and
 stochastic process utilities in the jpsgcs.alun.random package.
*/

public class TestRandom
{
	public static void main(String[] args)
	{
		RandomVariable X = new BinomialRV(10,0.25);
		for (int i=0; i<10; i++)
			System.out.println(X.next());
		System.out.println();

		X = new GaussianRV(5,10);
		for (int i=0; i<10; i++)
			System.out.println(X.next());
		System.out.println();

		PoissonProcess P = new PoissonProcess(0.32);
		for (int i=0; i<10; i++)
		{
			P.next();
			StochasticState s = P.getState();
			System.out.println(s.time);
		}
		System.out.println();

		BirthAndDeathProcess B = new BirthAndDeathProcess(10,10);

		for (int i=0; i<10; i++)
		{
			B.next();
			PopulationCount c = (PopulationCount)B.getState();
			System.out.println(c.time+"\t"+c.births+"\t"+c.deaths);
		}
	}
}
