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


package jpsgcs.alun.util;

import java.util.Collection;
import java.util.ArrayList;

/**
 A RandomBag is a Collection of objects from which an object can
 be randomly selected either with replacement, using next(), or 
 without, using draw().
*/

public interface RandomBag<V> extends Collection<V>
{
/** 
 Selects a random element from the bag but does not change the contents 
 of the bag.
 Returns null if the bag is empty.
*/
	public V next();

/**
 Selects and removes a random element from the bag.
 Returns null if the bag is empty.
*/
	public V draw();
}
