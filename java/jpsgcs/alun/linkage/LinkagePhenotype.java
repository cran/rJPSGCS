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


package jpsgcs.alun.linkage;

import jpsgcs.alun.util.StringFormatter;

/**
 This is a base class from with the linkage phenotype classes
 are derived.
*/
abstract public class LinkagePhenotype
{
/**
 Sets the locus associated with this phenotype.
*/
	public void setLocus(LinkageLocus l)
	{
		this.l = l;
	}

/**
 Gets the locus associated with this phenotype.
*/
	public LinkageLocus getLocus()
	{
		return l;
	}

	abstract public LinkagePhenotype nullCopy();

// Protected data.

	protected static final StringFormatter f = new StringFormatter();

// Private data.

	private LinkageLocus l = null;
}
