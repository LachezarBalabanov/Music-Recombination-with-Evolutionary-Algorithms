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

import java.util.Vector;
import java.io.Serializable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiSystem;

import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Differential evolution population class. Implementation of genetic algorithm
 * based optimization approach. All melodies are presented as chromosomes
 * (vectors). During evolution period recombination of chromosomes is applied
 * and human evaluation of the melody quality.
 */
public class Population implements Cloneable, Serializable {
	/**
	 * Painter is checking for playing sequence and do visual effects according
	 * to melody.
	 */
	private class Painter extends Thread {
		/**
		 * Delay for redraw in milliseconds.
		 */
		private static final int REDRAW_DELAY = 60;

		/**
		 * Default run method of the tread.
		 */
		public void run() {
			while (true) {
				try {
					Thread.sleep(REDRAW_DELAY);
				} catch (InterruptedException ex) {
					/*
					 * It does not matter if thread is interrupted during
					 * sleeping mode.
					 */
				}

				if(sequencer == null) {
					continue;
				}

				if(sequencer.isRunning() == false) {
					continue;
				}

				double position = (double) sequencer.getMicrosecondPosition() / (double) sequencer.getMicrosecondLength();

				Note note = melodyPaying.getNoteOn(position);

				GraphicsContext context = canvas.getGraphicsContext2D();

				if (note != null) {
					double red = ((Color)context.getStroke()).getRed()*255;
					double green = ((Color)context.getStroke()).getGreen()*255;
					double blue = ((Color)context.getStroke()).getBlue()*255;
					context.setStroke(Color.rgb((int)(red + note.getNote()) % 256, (int)(green + note.getVelocity()) % 256, (int)(blue + note.getDuration()) % 256));
				}

				int x = (int) (((double) sequencer.getMicrosecondPosition() / (double) sequencer.getMicrosecondLength()) * (int)canvas.getWidth());
				int y = (int) (((double) sequencer.getMicrosecondLength() / (double) sequencer.getMicrosecondPosition()) + Math.random() * canvas.getHeight()) % (int)canvas.getHeight();

				context.strokeLine(x - 2, y, x - 2, y);
				context.strokeLine(x, y - 2, x, y - 2);
				context.strokeLine(x - 1, y, x - 1, y);
				context.strokeLine(x, y - 1, x, y - 1);
				context.strokeLine(x, y, x, y);
				context.strokeLine(x + 1, y, x + 1, y);
				context.strokeLine(x, y + 1, x, y + 1);
				context.strokeLine(x + 2, y, x + 2, y);
				context.strokeLine(x, y + 2, x, y + 2);
			}
		}
	}

	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Time to sleep before next melody in milliseconds.
	 */
	private static long TIME_BEFORE_NEXT_MELODY = 500;

	/**
	 * Min population size for random generation.
	 */
	public static final int MIN_RANDOM_POPULATION = 3;

	/**
	 * Max population size for random generation.
	 */
	public static final int MAX_RANDOM_POPULATION = 5;

	/**
	 * Min population size for fractal generation.
	 */
	public static final int MIN_FRACTAL_POPULATION = 3;

	/**
	 * Max population size for fractal generation.
	 */
	public static final int MAX_FRACTAL_POPULATION = 5;

	/**
	 * Size of the DE population.
	 */
	private int size = 0;

	/**
	 * Chromosomes set of DE.
	 */
	private Vector<Melody> offspring;

	/**
	 * Handle to the melody which is playing.
	 */
	private Melody melodyPaying = null;

	/**
	 * Sequencer object for melody playing.
	 */
	private Sequencer sequencer = null;

	/**
	 * Graphics canvas to visualize music information.
	 */
	private Canvas canvas = null;

	/**
	 * Constructor without parameters. Internal structure is created during
	 * constructor execution.
	 */
	public Population() {
		super();

		offspring = new Vector<Melody>();
	}

	/**
	 * Population size getter.
	 *
	 * @return Population size without new generation.
	 */
	public int getSize() {
		return (size);
	}

	/**
	 * Population size setter.
	 *
	 * @param size
	 *            Population size without new generation.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * All population melodies getter.
	 *
	 * @return All melodies.
	 */
	public Vector<Melody> getMelodies() {
		Vector<Melody> melodies = new Vector<Melody>();

		for (int i = 0; i < offspring.size(); i++)
			melodies.add((Melody) (offspring.elementAt(i)).clone());

		return (melodies);
	}

	/**
	 * Add melody. Population can be extended during evolution process.
	 */
	public void add(Melody melody) {
		melody.setId(offspring.size());
		offspring.add(melody);

		size++;
	}

	/**
	 * Recombination of the chromosomes according to DE rules. Choosing
	 * chromosomes and do differential vector addition.
	 */
	public void recombine() {
		Melody melody = null;
		int size = offspring.size();

		for (int i = 0; i < size; i++) {
			melody = (Melody) (offspring.elementAt(i)).clone();
			melody.update((offspring.elementAt(i)).getDiffertial(offspring.elementAt((int) (Math.random() * size))));
			melody.setId(offspring.size());

			offspring.add(melody);
		}
	}

	/**
	 * Determine the fitness function for each chromosome by creating a MIDI
	 * stream and evaluating from the user.
	 */
	public void evaluate() {
		for (int j = 0; j < offspring.size(); j++) {
			melodyPaying = offspring.elementAt(j);

			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(bao);
			byte[] bytes = null;
			try {
				char[] chars = melodyPaying.toMidiBytes();
				for (int i = 0; i < chars.length; i++) {
					out.write(chars[i]);
				}
				out.flush();
				bytes = bao.toByteArray();
				bao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out.println(melodyPaying);

			ByteArrayInputStream bai = new ByteArrayInputStream(bytes);

			try {
				sequencer = MidiSystem.getSequencer();
				Sequence sequence = MidiSystem.getSequence(new DataInputStream(bai));

				sequencer.open();
				sequencer.setSequence(sequence);

				Painter painter = new Painter();
				painter.start();
				sequencer.start();

				Thread.sleep(sequencer.getMicrosecondLength() / 1000);
				Thread.sleep(TIME_BEFORE_NEXT_MELODY);

				sequencer.stop();
				sequencer.close();
				painter.interrupt();

				sequencer = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Sort the population according to the fitness value. One of the possible
	 * ways to select parents for the next generation.
	 */
	public void sort() {
		Melody a, b;
		boolean done = false;

		while (done == false) {
			done = true;

			for (int i = 0; i < offspring.size() - 1; i++) {
				a = offspring.elementAt(i);
				b = offspring.elementAt(i + 1);

				if (a.getScore() < b.getScore()) {
					offspring.removeElementAt(i);
					offspring.insertElementAt(b, i);
					offspring.removeElementAt(i + 1);
					offspring.insertElementAt(a, i + 1);
					done = false;
				}
			}
		}
	}

	/**
	 * Remove extra population chromosomes. New generation is added as part of
	 * the old generation, after selection process only limited amount of
	 * chormosomes should survive.
	 */
	public void shrink() {
		for (int i = offspring.size() - 1; i >= size; i--) {
			offspring.removeElementAt(i);
		}
	}

	/**
	 * Population epochs. Calculates given number of epochs as part of
	 * population evolution.
	 *
	 * @param number
	 *            How many epochs.
	 */
	public void epoches(int number) {
		for (int i = 0; i < number; i++) {
			recombine();
			evaluate();
			sort();
			shrink();
		}
	}

	/**
	 * Score up of the currently playing melody. Method is provided as
	 * connection between graphic user interface and real melody object.
	 */
	public void scoreUp() {
		melodyPaying.scoreUp();

		System.out.println(melodyPaying);
	}

	/**
	 * Score down of the currently playing melody. Method is provided as
	 * connection between graphic user interface and real melody object.
	 */
	public void scoreDown() {
		melodyPaying.scoreDown();

		System.out.println(melodyPaying);
	}

	/**
	 * Clone the population object. The most clean way to make identical copy of
	 * complex data structure.
	 *
	 * @return Copy of the object.
	 */
	public Object clone() {
		Population population = new Population();

		population.offspring = new Vector<Melody>();

		for (int i = 0; i < offspring.size(); i++) {
			population.offspring.add((Melody) (offspring.elementAt(i)).clone());
		}

		population.size = size;

		return (population);
	}

	/**
	 * Set active graphics canvas.
	 *
	 * @param canvas
	 *            Graphics canvas.
	 */
	public void setGraphics(Canvas canvas) {
		this.canvas = canvas;
	}
}
