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

package eu.veldsoft.mididermi.server;

import java.util.Vector;
import java.io.FileWriter;
import java.io.BufferedWriter;

import eu.veldsoft.mididermi.base.Melody;

/**
 * Produce MIDI files in specified folder.
 */
public class MidiFileProducer {
	/**
	 * Project subfolder for saving files.
	 */
	private static String MIDI_SAVE_FOLDER = "../midis/";

	/**
	 * Produce MIDI files in the specified internal application folder.
	 *
	 * @param melodies
	 *            Vector of melodies.
	 */
	public static void produce(Vector<Melody> melodies) {
		for (int i = 0; i < melodies.size(); i++) {
			String fileName = MIDI_SAVE_FOLDER;
			Melody melody = melodies.elementAt(i);

			fileName += melody.getId();
			fileName += "_";
			fileName += melody.getGenre();
			fileName += "_";
			fileName += melody.getTimber();
			fileName += "_";
			fileName += System.currentTimeMillis();
			fileName += ".mid";

			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
				// TODO Something is wrong with saving binary information.
				out.write(melody.toMidiBytes());
				out.close();
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
}
