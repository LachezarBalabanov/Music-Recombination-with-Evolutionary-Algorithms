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
 * Fractal melody provider class is responsible to generate melody with length,
 * timber and notes according proper fractal formulas.
 */
public class FractalMelodyProvider {
	/**
	 * Min melody sequence size for random generation.
	 */
	private static final int MIN_FRACTAL_SEQUENCE = 3;

	/**
	 * Max melody sequence size for random generation.
	 */
	private static final int MAX_FRACTAL_SEQUENCE = 15;

	/**
	 * Create fractal melody as sequence of music notes. By this way system can
	 * be easy populated with a lot of melodies.
	 *
	 * @return Melody constructed.
	 */
	public static Melody provide() {
		return (provide(MIN_FRACTAL_SEQUENCE + (int) (Math.random() * (MAX_FRACTAL_SEQUENCE - MIN_FRACTAL_SEQUENCE + 1))));
	}

	/**
	 * Create fractal melody as sequence of music notes. By this way system can
	 * be easy populated with a lot of melodies. The difference with the
	 * previous method is only that length is specified by input parameter.
	 *
	 * @param length
	 *            The length of the melody.
	 *
	 * @return Melody constructed.
	 */
	public static Melody provide(int length) {
		Melody melody = new Melody();

		// TODO Better fractal formula can be implemented.
		Note next = null;
		Note previous = null;
		for (int i = 0; i < length; i++) {
			next = Note.provideRandom();

			if (i > 0 && previous.getNote() > next.getNote()) {
				next.setNote(next.getNote() + 1);
			} else if (i > 0 && previous.getNote() < next.getNote()) {
				next.setNote(next.getNote() - 1);
			}

			melody.addNote(next);
			previous = next;
		}

		melody.setTimber(1 + (int) (Math.random() * 128));
		melody.setId(Melody.getUniqueId());
		melody.setScore(0);

		melody.sort();

		return (melody);
	}
}
