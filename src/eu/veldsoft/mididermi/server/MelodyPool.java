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

import eu.veldsoft.mididermi.base.Melody;
import eu.veldsoft.mididermi.base.Population;
import eu.veldsoft.mididermi.database.DatabaseMediator;
import eu.veldsoft.mididermi.providers.DatabaseSetMelodiesProvider;
import eu.veldsoft.mididermi.providers.FileSetMelodiesProvider;
import eu.veldsoft.mididermi.providers.FractalSetMelodiesProvider;
import eu.veldsoft.mididermi.providers.RandomSetMelodiesProvider;

/**
 * Melody pool is the common server place where all melodies are presented in
 * the server's RAM.
 */
public class MelodyPool {
	/**
	 * Min pool subset size.
	 */
	private int minSubsetSize = 2;

	/**
	 * Max pool subset size.
	 */
	private int maxSubsetSize = 2;

	/**
	 * Common melody pool.
	 */
	private Vector<Melody> pool;

	/**
	 * How many random melodies to be created.
	 */
	private int randomMelodiesAmount = 0;

	/**
	 * How many random melodies to be created.
	 */
	private int fractalMelodiesAmount = 0;

	/**
	 * Flag for loading melodies from database.
	 */
	private boolean loadMelodiesFromDatabase = false;

	/**
	 * Flag for loading melodies from text files.
	 */
	private boolean loadMelodiesFromFiles = false;

	/**
	 * Flag for storing melodies into database.
	 */
	private boolean storeMelodiesIntoDatabase = false;

	/**
	 * Flag for storing melodies into MIDI files.
	 */
	private boolean storeMelodiesIntoFiles = false;

	/**
	 * Add set of melodies to existing pool.
	 *
	 * @param melodies
	 *            Set of melodies to be added.
	 */
	private void addMelodies(Vector<Melody> melodies) {
		for (int i = 0; i < melodies.size(); i++) {
			pool.add(melodies.elementAt(i));
		}
	}

	/**
	 * Default constructor.
	 */
	public MelodyPool() {
		pool = new Vector<Melody>();
	}

	/**
	 * Initialize melody pool with melodies.
	 */
	public void init() {
		pool.clear();

		if (randomMelodiesAmount > 0) {
			addMelodies(RandomSetMelodiesProvider.provide(randomMelodiesAmount));
		}

		if (fractalMelodiesAmount > 0) {
			addMelodies(FractalSetMelodiesProvider.provide(fractalMelodiesAmount));
		}

		if (loadMelodiesFromDatabase = true) {
			addMelodies(DatabaseSetMelodiesProvider.provide());
		}

		if (loadMelodiesFromFiles = true) {
			addMelodies(FileSetMelodiesProvider.provide());
		}
	}

	/**
	 * Standard finalization method.
	 */
	public void persistentStore() {
		if (this.storeMelodiesIntoDatabase == true) {
			DatabaseMediator.storeMelodies(pool);
		}

		if (this.storeMelodiesIntoFiles == true) {
			MidiFileProducer.produce(pool);
		}
	}

	/**
	 * Select random melodies from the common pool. From the server side not all
	 * general population is sent to the client side. By random selection only
	 * part of the general population is sent to the client side. By this way
	 * different clients have different part of the melodies set and more
	 * options to provide unique melodies.
	 *
	 * @return Population.
	 */
	public Population provideRandomSubset() {
		int num = minSubsetSize + (int) (Math.random() * (maxSubsetSize - minSubsetSize + 1));

		if (num > pool.size())
			num = pool.size();

		boolean take[] = new boolean[pool.size()];
		for (int i = 0; i < take.length; i++)
			take[i] = false;

		while (num > 0) {
			int index = (int) (Math.random() * pool.size());
			if (take[index] == false) {
				take[index] = true;
				num--;
			}
		}

		Population population = new Population();

		for (int i = 0; i < pool.size(); i++)
			if (take[i] == true)
				population.add((Melody) pool.elementAt(i).clone());

		return (population);
	}

	/**
	 * Merge pool with the new set of melodies. Server side merge the general
	 * population with population returned from the client side. By this way
	 * general population is getting more varied.
	 *
	 * @param melodies
	 *            New set of melodies.
	 */
	public void merge(Vector<Melody> melodies) {
		for (int i = 0; i < melodies.size(); i++) {
			boolean original = true;

			for (int j = 0; j < pool.size(); j++)
				if (pool.elementAt(j).equals(melodies.elementAt(i))) {
					pool.elementAt(j).setScore(pool.elementAt(j).getScore() + melodies.elementAt(i).getScore());
					original = false;
					break;
				}

			if (original == true)
				pool.add(melodies.elementAt(i));
		}
	}

	/**
	 * Random melodies amount getter.
	 *
	 * @return Random melodies amount.
	 */
	public int getRandomMelodiesAmount() {
		return (randomMelodiesAmount);
	}

	/**
	 * Random melodies amount setter.
	 *
	 * @param randomMelodiesAmount
	 *            Random melodies amount.
	 */
	public void setRandomMelodiesAmount(int randomMelodiesAmount) {
		this.randomMelodiesAmount = randomMelodiesAmount;
	}

	/**
	 * Fractal melodies amount setter.
	 *
	 * @param randomMelodiesAmount
	 *            Random melodies amount.
	 *
	 * @return The amount of melodies.
	 */
	public int getFractalMelodiesAmount() {
		return (fractalMelodiesAmount);
	}

	/**
	 * Fractal melodies amount setter.
	 *
	 * @param fractalMelodiesAmount
	 *            Random melodies amount.
	 */
	public void setFractalMelodiesAmount(int fractalMelodiesAmount) {
		this.fractalMelodiesAmount = fractalMelodiesAmount;
	}

	/**
	 * Loading melodies form database flag getter.
	 *
	 * @return Flag value.
	 */
	public boolean isLoadMelodiesFromDatabase() {
		return (loadMelodiesFromDatabase);
	}

	/**
	 * Loading melodies form database flag setter.
	 *
	 * @param loadMelodiesFromDatabase
	 *            Flag value.
	 */
	public void setLoadMelodiesFromDatabase(boolean loadMelodiesFromDatabase) {
		this.loadMelodiesFromDatabase = loadMelodiesFromDatabase;
	}

	/**
	 * Loading melodies form files flag getter.
	 *
	 * @return Flag value.
	 */
	public boolean isLoadMelodiesFromFiles() {
		return (loadMelodiesFromFiles);
	}

	/**
	 * Loading melodies form files flag setter.
	 *
	 * @param loadMelodiesFromFiles
	 *            Flag value.
	 */
	public void setLoadMelodiesFromFiles(boolean loadMelodiesFromFiles) {
		this.loadMelodiesFromFiles = loadMelodiesFromFiles;
	}

	/**
	 * Storing melodies into database flag getter.
	 *
	 * @return Flag value.
	 */
	public boolean isStoreMelodiesIntoDatabase() {
		return (storeMelodiesIntoDatabase);
	}

	/**
	 * Storing melodies into database flag setter.
	 *
	 * @param storeMelodiesIntoDatabase
	 *            Flag value.
	 */
	public void setStoreMelodiesIntoDatabase(boolean storeMelodiesIntoDatabase) {
		this.storeMelodiesIntoDatabase = storeMelodiesIntoDatabase;
	}

	/**
	 * Storing melodies into files flag getter.
	 *
	 * @return Flag vealue.
	 */
	public boolean isStoreMelodiesIntoFiles() {
		return (storeMelodiesIntoFiles);
	}

	/**
	 * Storing melodies into files flag setter.
	 *
	 * @param storeMelodiesIntoFiles
	 *            Flag value.
	 */
	public void setStoreMelodiesIntoFiles(boolean storeMelodiesIntoFiles) {
		this.storeMelodiesIntoFiles = storeMelodiesIntoFiles;
	}

	/**
	 * Minimum pool subset size getter.
	 *
	 * @return Minimum size.
	 */
	public int getMinSubsetSize() {
		return( minSubsetSize );
	}

	/**
	 * Minimum pool subset size setter.
	 *
	 * @param minSubsetSize
	 *            Minimum size.
	 */
	public void setMinSubsetSize(int minSubsetSize) {
		this.minSubsetSize = minSubsetSize;

		/*
		 * Les than 2 chromosomes are not able to produce generation.
		 */
		if (this.minSubsetSize < 2) {
			this.minSubsetSize = 2;
		}
	}

	/**
	 * Maximum pool subset size getter.
	 *
	 * @return Maximum size.
	 */
	public int getMaxSubsetSize() {
		return maxSubsetSize;
	}

	/**
	 * Maximum pool subset size setter.
	 *
	 * @param maxSubsetSize Maximum size.
	 */
	public void setMaxSubsetSize(int maxSubsetSize) {
		this.maxSubsetSize = maxSubsetSize;

		/*
		 * Les than 2 chromosomes are not able to produce generation.
		 */
		if (this.maxSubsetSize < 2) {
			this.maxSubsetSize = 2;
		}
	}
}
