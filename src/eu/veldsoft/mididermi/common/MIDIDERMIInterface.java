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

package eu.veldsoft.mididermi.common;

import java.rmi.Remote;

/**
 * Remote interface functionality. Methods which server provides to the remote
 * clients.
 */
public interface MIDIDERMIInterface extends Remote {
	/**
	 * Request the task for calculation. By this method remote client request
	 * task for calculation.
	 *
	 * @return Task.
	 *
	 * @throws java.rmi.RemoteException
	 *             RMI should be safe.
	 */
	public MIDIDERMITask request() throws java.rmi.RemoteException;

	/**
	 * Return the calculated task. The result of the calculation on the remote
	 * side is returned by the requested task structure.
	 *
	 * @param val
	 *            Task.
	 *
	 * @throws java.rmi.RemoteException
	 *             RMI should be safe.
	 */
	public void response(MIDIDERMITask val) throws java.rmi.RemoteException;
}
