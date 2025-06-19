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

package eu.veldsoft.mididermi.database;

import java.util.StringTokenizer;
import java.util.Vector;
import java.io.IOException;
import java.util.Properties;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import eu.veldsoft.mididermi.base.Melody;
import eu.veldsoft.mididermi.base.Note;

/**
 * Database mediator handles database functionality for loading and storing
 * melody data.
 */
public class DatabaseMediator {
	/**
	 * Database connection host.
	 */
	private static String host = "";

	/**
	 * Database connection port.
	 */
	private static String port = "";

	/**
	 * Database username to be used for connection.
	 */
	private static String username = "";

	/**
	 * Password of user to be connected.
	 */
	private static String password = "";

	/**
	 * Working database name.
	 */
	private static String database = "";

	/**
	 * Database connection URL string.
	 */
	private static String url;

	/**
	 * Connection object.
	 */
	private static Connection connection = null;

	/**
	 * Obtain database properties.
	 */
	static {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream("database.properties"));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		host = properties.getProperty("host");
		port = properties.getProperty("port");
		username = properties.getProperty("username");
		password = properties.getProperty("password");
		database = properties.getProperty("database");

		url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Extract all available melodies stored into database.
	 *
	 * @return All melodies stored into database.
	 */
	public static Vector<Melody> loadMelodies() {
		CallableStatement statement = null;
		ResultSet result = null;
		Vector<Melody> melodies = new Vector<Melody>();

		if (connection == null) {
			return (melodies);
		}

		try {
			statement = connection.prepareCall("{ call get_all_melodies() }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result = statement.executeQuery();

			while (result.next() != false) {
				Melody melody = new Melody();
				melody.setId(result.getLong("system_id"));
				String sequence = result.getString("sequence");
				StringTokenizer tokenizer = new StringTokenizer(sequence, " ", false);
				while (tokenizer.hasMoreElements()) {
					Note note = new Note();
					try {
						note.setNote(Integer.parseInt((String) tokenizer.nextElement()));
						note.setOffset(Integer.parseInt((String) tokenizer.nextElement()));
						note.setDuration(Integer.parseInt((String) tokenizer.nextElement()));
						note.setVelocity(Integer.parseInt((String) tokenizer.nextElement()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					melody.addNote(note);
				}
				melody.setTimber(result.getInt("timber"));
				melody.setScore(result.getInt("score"));
				melody.setGenre(result.getInt("genre"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return (melodies);
	}

	/**
	 * Store list of melodies into database.
	 *
	 * @param melodies
	 *            Vector of melodies.
	 */
	public static void storeMelodies(Vector<Melody> melodies) {
		CallableStatement statement = null;
		Melody melody = null;

		if (connection == null) {
			return;
		}

		for (int i = 0; i < melodies.size(); i++) {
			melody = melodies.elementAt(i);

			try {
				statement = connection.prepareCall("{ call add_melody(?, ?, ?, ?, ?) }");

				statement.setLong(1, melody.getId());
				statement.setString(2, melody.getNotesInNumbers());
				statement.setInt(3, melody.getTimber());
				statement.setInt(4, melody.getScore());
				statement.setInt(5, melody.getGenre());

				statement.execute();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
