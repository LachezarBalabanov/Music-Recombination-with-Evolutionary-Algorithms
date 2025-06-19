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

import eu.veldsoft.mididermi.base.Note;
import eu.veldsoft.mididermi.base.Melody;

/**
 * Random melody provider class is responsible to generate melody with random
 * length, random timber and random notes.
 */
public class RandomMelodyProvider {
	/**
	 * Min melody sequence size for random generation.
	 */
	private static final int MIN_RANDOM_SEQUENCE = 3;

	/**
	 * Max melody sequence size for random generation.
	 */
	private static final int MAX_RANDOM_SEQUENCE = 15;

	/**
	 * Create random melody as sequence of random music notes. By this way
	 * system can be easy populated with a lot of melodies, but melodies with no
	 * sense.
	 *
	 * @return Melody constructed.
	 */
	public static Melody provide() {
		return (provide(MIN_RANDOM_SEQUENCE + (int) (Math.random() * (MAX_RANDOM_SEQUENCE - MIN_RANDOM_SEQUENCE + 1))));
	}

	/**
	 * Create random melody as sequence of random music notes. By this way
	 * system can be easy populated with a lot of melodies, but melodies with no
	 * sense. The difference with the previous method is only that length is
	 * specified by input parameter.
	 *
	 * @param length
	 *            The length of the melody.
	 *
	 * @return Melody constructed.
	 */
	public static Melody provide(int length) {
		Melody melody = new Melody();

		for (int i = 0; i < length; i++)
			melody.addNote(Note.provideRandom());

		melody.setTimber(1 + (int) (Math.random() * 128));
		melody.setId(Melody.getUniqueId());
		melody.setScore(0);

		melody.sort();

		return (melody);
	}
}
