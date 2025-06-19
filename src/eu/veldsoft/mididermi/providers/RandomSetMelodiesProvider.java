/*============================================================================
 * MIDI-DE-RMI, Version 0.3                                                  *
 * Velbazhd Software LLC                                                     *
 *                                                                           *
 * Copyright (c) 2007-2025 Lachezar Balabanov                                *
 *                         Todor Balabanov                                   *
 *                         Petar Tomov                                       *
 *                                                                           *
 * http://veldsoft.eu/                                                       *
 *                                                                           *
 * This program is free software; you can redistribute it and/or modify      *
 * it under the terms of the GNU General Public License as published by      *
 * the Free Software Foundation; either version 2 of the License, or         *
 * (at your option) any later version.                                       *
 *                                                                           *
 * This program is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             *
 * GNU General Public License for more details.                              *
 *                                                                           *
 * You should have received a copy of the GNU General Public License along   *
 * with this program; if not, write to the Free Software Foundation, Inc.,   *
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.               *
 ============================================================================*/

package eu.veldsoft.mididermi.providers;

import java.util.Vector;

import eu.veldsoft.mididermi.base.Melody;
import eu.veldsoft.mididermi.base.Population;

/**
 * Provide random set of melodies.
 */
public class RandomSetMelodiesProvider {
	/**
	 * Provide random set of melodies.
	 *
	 * @return Random set of melodies.
	 */
	public static Vector<Melody> provide() {
		return( provide(Population.MIN_RANDOM_POPULATION + (int) (Math.random() * (Population.MAX_RANDOM_POPULATION - Population.MIN_RANDOM_POPULATION + 1))) );
	}

	/**
	 * Provide random set of melodies.
	 *
	 * @param size
	 *            Number of chromosomes.
	 *
	 * @return Random set of melodies.
	 */
	public static Vector<Melody> provide(int size) {
		Vector<Melody> melodies = new Vector<Melody>();

		Melody melody = null;
		for (int i = 0; i < size; i++) {
			melody = RandomMelodyProvider.provide();
			melody.setId(Melody.getUniqueId());

			melodies.add(melody);
		}

		return (melodies);
	}
}
