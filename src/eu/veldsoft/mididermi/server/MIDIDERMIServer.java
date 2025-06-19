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

import java.rmi.RMISecurityManager;

/**
 * RMI server luncher. Create instance of the RMI server object.
 */
public class MIDIDERMIServer {
	/**
	 * Main starting point of the program. RMI server application instance is
	 * created.
	 *
	 * @param args
	 *            Command line parameters.
	 */
	public static void main(String[] args) {
		MIDIDERMIImplement implementation = null;

		try {
			implementation = new MIDIDERMIImplement("MIDIDERMIImplementInstance");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int minPoolSubset = 2;
		int maxPoolSubset = 2;
		int randomMelodiesAmount = 0;
		int fractalMelodiesAmount = 0;
		boolean loadMelodiesFromDatabase = false;
		boolean loadMelodiesFromFiles = false;
		boolean storeMelodiesIntoDatabase = false;
		boolean storeMelodiesIntoFiles = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-MINPOOL")) {
				/*
				 * Minimum pool subset.
				 */
				try {
					minPoolSubset = Integer.parseInt(args[i + 1]);
				} catch (Exception ex) {
					minPoolSubset = 2;
				}
			} else if (args[i].equals("-MAXPOOL")) {
				/*
				 * Maximum pool subset.
				 */
				try {
					maxPoolSubset = Integer.parseInt(args[i + 1]);
				} catch (Exception ex) {
					maxPoolSubset = 2;
				}
			} else if (args[i].equals("-MINEPOCHS")) {
				/*
				 * Minimum epochs.
				 */
				try {
					implementation.setMinNumEpochs(Integer.parseInt(args[i + 1]));
				} catch (Exception ex) {
					implementation.setMinNumEpochs(0);
				}
			} else if (args[i].equals("-MAXEPOCHS")) {
				/*
				 * Maximum epochs.
				 */
				try {
					implementation.setMaxNumEpochs(Integer.parseInt(args[i + 1]));
				} catch (Exception ex) {
					implementation.setMaxNumEpochs(0);
				}
			} else if (args[i].equals("-LR")) {
				/*
				 * Load random melodies.
				 */
				try {
					randomMelodiesAmount = Integer.parseInt(args[i + 1]);
				} catch (Exception ex) {
					randomMelodiesAmount = 0;
				}
			} else if (args[i].equals("-LT")) {
				/*
				 * Load fractal melodies.
				 */
				try {
					fractalMelodiesAmount = Integer.parseInt(args[i + 1]);
				} catch (Exception ex) {
					fractalMelodiesAmount = 0;
				}
			} else if (args[i].equals("-LD")) {
				/*
				 * Load all melodies from database.
				 */
				loadMelodiesFromDatabase = true;
			} else if (args[i].equals("-LF")) {
				/*
				 * Load all melodies written as text files.
				 */
				loadMelodiesFromFiles = true;
			} else if (args[i].equals("-SD")) {
				/*
				 * Store all melodies into database.
				 */
				storeMelodiesIntoDatabase = true;
			} else if (args[i].equals("-SF")) {
				/*
				 * Store all melodies as MIDI binary files.
				 */
				storeMelodiesIntoFiles = true;
			}
		}

		implementation.init(minPoolSubset, maxPoolSubset, randomMelodiesAmount, fractalMelodiesAmount, loadMelodiesFromDatabase, loadMelodiesFromFiles, storeMelodiesIntoDatabase, storeMelodiesIntoFiles);

		System.out.println("MIDI DE RMI Server bound ...");
	}
}
