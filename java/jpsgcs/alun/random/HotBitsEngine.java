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

//-
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
//:

/**
 This is link to John Walker's web site that distributes, it is claimed,
 truly random bytes generated from a radioactive decay process.
 Beware running out of your daily allocation without receiving a 
 error message. Bytes repeat after after this happens.
 Not for serious use, but could be used to generate seeds for
 pseudo random generators.
*/
public final class HotBitsEngine extends RandomEngine
{
	static { className = "HotBitsEngine"; }

/**
* This constructs a new wrapper to the HotBits site.
*/
	public HotBitsEngine()
	{
//.
		buffer = new byte[2048];
		nextone = ngot = 0;
		y = 0;
		for (int i=0; i<nb; i++)
		{
			y *= 256.0;
			y += 256.0;
		}
//:
	}

/**
 Returns the next Uniform.
*/
	public final synchronized double next()
	{
//.
		double x = 0;
		for (int i=0; i<nb; i++)
		{
			x *= 256.0;
			x += nextByte();
		}
		return 0.5 + x/y;
//:
	}

/**
 Gets a byte from the buffer.
*/
//-
	public final synchronized byte nextByte()
	{
		if (nextone >= ngot)
			getMore();
		return buffer[nextone++];
	}

	private byte[] buffer = null;
	private int nextone = 0;
	private int ngot = 0;
	private int nb = 4;
	private double y = 0;

/**
 Fills a buffer of bytes by downloading from the Hotbits site.
*/
	private final synchronized void getMore()
	{
		try
		{ 
			URL u = new URL("http://www.fourmilab.ch/cgi-bin/uncgi/Hotbits?nbytes=buffer.length&fmt=bin");
			InputStream s = u.openStream();
			ngot = s.read(buffer);
			s.close();
			nextone = 0;
		}
		catch (IOException e)
		{
			System.out.println("Caught in HotBitsEngine:getMore()");
			e.printStackTrace();
			//throw new Error("Can't get HotBits");
		}
	}
//:
}
