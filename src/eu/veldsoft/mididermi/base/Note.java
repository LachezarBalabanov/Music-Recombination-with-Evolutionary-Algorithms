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

package eu.veldsoft.mididermi.base;

import java.io.Serializable;

/**
 * Note represents single music note. Notes are used as objects in melodies.
 */
public class Note implements Cloneable, Serializable {
	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Min note number for random generation.
	 */
	private static final int MIN_RANDOM_NOTE = 0;

	/**
	 * Max note number for random generation.
	 */
	private static final int MAX_RANDOM_NOTE = 127;

	/**
	 * Min note offset for random generation.
	 */
	private static final int MIN_RANDOM_OFFSET = 1;

	/**
	 * Max note offset for random generation.
	 */
	private static final int MAX_RANDOM_OFFSET = 10000;

	/**
	 * Min note duration for random generation.
	 */
	private static final int MIN_RANDOM_DURATION = 1;

	/**
	 * Max note duration for random generation.
	 */
	private static final int MAX_RANDOM_DURATION = 100;

	/**
	 * Min note velocity for random generation.
	 */
	private static final int MIN_RANDOM_VELOCITY = 0;

	/**
	 * Max note velocity for random generation.
	 */
	private static final int MAX_RANDOM_VELOCITY = 127;

	/**
	 * Musical note number according MIDI standard.
	 */
	private int note;

	/**
	 * Musical note offset from the beginning of the melody.
	 */
	private int offset;

	/**
	 * Musical note duration.
	 */
	private int duration;

	/**
	 * Musical note velocity.
	 */
	private int velocity;

	/**
	 * Generate music note with random parameters. It is needed during random
	 * melody construction.
	 *
	 * @return Music note.
	 */
	public static Note provideRandom() {
		int note = MIN_RANDOM_NOTE + (int) (Math.random() * (MAX_RANDOM_NOTE - MIN_RANDOM_NOTE + 1));

		int offset = MIN_RANDOM_OFFSET + (int) (Math.random() * (MAX_RANDOM_OFFSET - MIN_RANDOM_OFFSET + 1));

		int duration = MIN_RANDOM_DURATION + (int) (Math.random() * (MAX_RANDOM_DURATION - MIN_RANDOM_DURATION + 1));

		int velocity = MIN_RANDOM_VELOCITY + (int) (Math.random() * (MAX_RANDOM_VELOCITY - MIN_RANDOM_VELOCITY + 1));

		return (new Note(note, offset, duration, velocity));
	}

	/**
	 * Constructor without parameters.
	 */
	public Note() {
		this(0, 0, 0, 0);
	}

	/**
	 * Constructor with parameters.
	 *
	 * @param note
	 *            Note number.
	 * @param offset
	 *            Start time offset.
	 * @param duration
	 *            Note duration.
	 * @param velocity
	 *            Note velocity.
	 */
	public Note(int note, int offset, int duration, int velocity) {
		super();

		this.setNote(note);
		this.setOffset(offset);
		this.setDuration(duration);
		this.setVelocity(velocity);
	}

	/**
	 * Duration getter.
	 *
	 * @return Note duration.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Duration setter.
	 *
	 * @param duration
	 *            Note duration.
	 */
	public void setDuration(int duration) {
		if (duration < MIN_RANDOM_DURATION)
			duration = MIN_RANDOM_DURATION;

		if (duration > MAX_RANDOM_DURATION)
			duration = MAX_RANDOM_DURATION;

		this.duration = duration;
	}

	/**
	 * Note number getter.
	 *
	 * @return Note number.
	 */
	public int getNote() {
		return note;
	}

	/**
	 * Note number setter.
	 *
	 * @param note
	 *            Note number.
	 */
	public void setNote(int note) {
		if (note < MIN_RANDOM_NOTE)
			note = MIN_RANDOM_NOTE;

		if (note > MAX_RANDOM_NOTE)
			note = MAX_RANDOM_NOTE;

		this.note = note;
	}

	/**
	 * Note start time offset getter.
	 *
	 * @return Note start time offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Note start time offset setter.
	 *
	 * @param offset
	 *            Note start time offset.
	 */
	public void setOffset(int offset) {
		if (offset < MIN_RANDOM_OFFSET)
			offset = MIN_RANDOM_OFFSET;

		if (offset > MAX_RANDOM_OFFSET)
			offset = MAX_RANDOM_OFFSET;

		this.offset = offset;
	}

	/**
	 * Note velocity getter.
	 *
	 * @return Note velocity.
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * Note velocity setter.
	 *
	 * @param velocity
	 *            Note velocity.
	 */
	public void setVelocity(int velocity) {
		if (velocity < MIN_RANDOM_VELOCITY)
			velocity = MIN_RANDOM_VELOCITY;

		if (velocity > MAX_RANDOM_VELOCITY)
			velocity = MAX_RANDOM_VELOCITY;

		this.velocity = velocity;
	}

	/**
	 * Compare two music notes.
	 *
	 * @return True if equals, false otherwise.
	 */
	public boolean equals(Object obj) {
		Note note = null;

		try {
			note = (Note) obj;
		} catch (Exception ex) {
			return (false);
		}

		if (note == null)
			return (false);

		if (this.note != note.note || this.offset != note.offset || this.duration != note.duration || this.velocity != note.velocity)
			return (false);

		return (true);
	}

	/**
	 * Clone the note object. The most clean way to make identical copy of
	 *
	 * @return Copy of the object.
	 */
	public Object clone() {
		return (new Note(note, offset, duration, velocity));
	}
}
