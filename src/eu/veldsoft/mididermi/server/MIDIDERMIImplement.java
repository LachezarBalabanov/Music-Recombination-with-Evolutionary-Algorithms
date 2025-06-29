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

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import eu.veldsoft.mididermi.common.MIDIDERMITask;
import eu.veldsoft.mididermi.common.MIDIDERMIInterface;

/**
 * RMI server implementation. All task are given from here and all results are
 * collected here.
 */
public class MIDIDERMIImplement extends UnicastRemoteObject implements MIDIDERMIInterface {
	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Minimum number of epochs to be calculated on the client side.
	 */
	private int minNumEpochs = 0;

	/**
	 * Maximum number of epochs to be calculated on the client side.
	 */
	private int maxNumEpochs = 1;

	/**
	 * Melody pool holding all server side available melodies.
	 */
	private MelodyPool melodyPool;

	/**
	 * RMI server constructor. Binding to the RMI register and initial
	 * population creation.
	 *
	 * @param name
	 *            Name of the service.
	 *
	 * @throws RemoteException
	 *             RMI should be safe.
	 */
	public MIDIDERMIImplement(String name) throws RemoteException {
		super();

		try {
			Naming.rebind(name, this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		melodyPool = new MelodyPool();

		melodyPool.init();
	}

	/**
	 * Request the task for calculation. Client side call the method to obtain
	 * task for calculation.
	 *
	 * @return Task.
	 *
	 * @throws java.rmi.RemoteException
	 *             RMI should be safe.
	 */
	public MIDIDERMITask request() throws RemoteException {
		int numberOfEpochs = minNumEpochs + (int) (Math.random() * (maxNumEpochs - minNumEpochs + 1));

		return (new MIDIDERMITask(melodyPool.provideRandomSubset(), numberOfEpochs));
	}

	/**
	 * Return the calculated task. Client side call the method to provide result
	 * of calculations done.
	 *
	 * @param val
	 *            Task.
	 *
	 * @throws java.rmi.RemoteException
	 *             RMI should be safe.
	 */
	public void response(MIDIDERMITask val) throws RemoteException {
		melodyPool.merge((val.getResult()).getMelodies());

		melodyPool.persistentStore();
	}

	/**
	 * Initialize melody pool parameters.
	 *
	 * @param minPoolSubset
	 *            Minimum pool subset size.
	 *
	 * @param maxPoolSubset
	 *            Maximum pool subset size.
	 *
	 * @param randomMelodiesAmount
	 *            Random melodies amount.
	 *
	 * @param fractalMelodiesAmount
	 *            Fractal melodies amount.
	 *
	 * @param loadMelodiesFromDatabase
	 *            Loading melodies form database flag.
	 *
	 * @param loadMelodiesFromFiles
	 *            Loading melodies form files flag.
	 *
	 * @param storeMelodiesIntoDatabase
	 *            Storing melodies into database flag.
	 *
	 * @param storeMelodiesIntoFiles
	 *            Storing melodies into files flag.
	 */
	public void init(int minPoolSubset, int maxPoolSubset, int randomMelodiesAmount, int fractalMelodiesAmount, boolean loadMelodiesFromDatabase, boolean loadMelodiesFromFiles, boolean storeMelodiesIntoDatabase, boolean storeMelodiesIntoFiles) {
		melodyPool.setMinSubsetSize(minPoolSubset);
		melodyPool.setMaxSubsetSize(maxPoolSubset);
		melodyPool.setRandomMelodiesAmount(randomMelodiesAmount);
		melodyPool.setFractalMelodiesAmount(fractalMelodiesAmount);
		melodyPool.setLoadMelodiesFromDatabase(loadMelodiesFromDatabase);
		melodyPool.setLoadMelodiesFromFiles(loadMelodiesFromFiles);
		melodyPool.setStoreMelodiesIntoDatabase(storeMelodiesIntoDatabase);
		melodyPool.setStoreMelodiesIntoFiles(storeMelodiesIntoFiles);
	}

	/**
	 * Minimum number of epochs getter.
	 *
	 * @return Minimum number of epochs.
	 */
	public int getMinNumEpochs() {
		return (minNumEpochs);
	}

	/**
	 * Minimum number of epochs setter.
	 *
	 * @param minNumEpochs
	 *            Minimum number of epochs.
	 */
	public void setMinNumEpochs(int minNumEpochs) {
		this.minNumEpochs = minNumEpochs;
	}

	/**
	 * Maximum number of epochs setter.
	 *
	 * @return Maximum number of epochs.
	 */
	public int getMaxNumEpochs() {
		return (maxNumEpochs);
	}

	/**
	 * Maximum number of epochs setter.
	 *
	 * @param maxNumEpochs
	 *            Maximum number of epochs.
	 */
	public void setMaxNumEpochs(int maxNumEpochs) {
		this.maxNumEpochs = maxNumEpochs;
	}
}
