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

package eu.veldsoft.mididermi.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import eu.veldsoft.mididermi.common.MIDIDERMITask;
import eu.veldsoft.mididermi.common.MIDIDERMIInterface;

/**
 * RMI client luncher. All remote side calculations are done here. This class is
 * responsible to request calculating task from the server. In parallel of the
 * calculation process user evaluation is done.
 */
public class MIDIDERMIClient extends Application implements Runnable {
	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instance of the remote server object.
	 */
	MIDIDERMIInterface simpleServerObject = null;

	/**
	 * Handle to task in progress to proceed keyboard events.
	 */
	private MIDIDERMITask task = null;

	/**
	 * Graphic canvas.
	 */
	private Canvas canvas;

	/**
	 * Standard key listener key released method. By pressing up or down arrow
	 * user is able to change the score of the melody.
	 */
	private void keyReleased(KeyEvent event) {
		if (event.getCode() == KeyCode.KP_UP) {
			task.scoreUp();
		}

		if (event.getCode() == KeyCode.KP_DOWN) {
			task.scoreDown();
		}
	}

	/**
	 * Perform music playing and scoring.
	 */
	private void perform() {
		if (task != null) {
			return;
		}

		try {
			task = simpleServerObject.request();
			task.setGraphics(canvas);
			new Thread(this).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Obtain RMI object.
	 *
	 * @param address
	 *            Address of RMI remote server.
	 */
	public void obtainRmiObject(String address) {
		try {
			simpleServerObject = (MIDIDERMIInterface) Naming.lookup("rmi://" + address + "/MIDIDERMIImplementInstance");
		} catch (MalformedURLException | RemoteException | NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Standard application init method.
	 */
	@Override
	public void start(Stage primaryStage) {
		String address = getParameters().getRaw().get(0);
		obtainRmiObject(address);

		canvas = new Canvas(640, 480);
		GraphicsContext context = canvas.getGraphicsContext2D();
		context.setFill(Color.BLACK);
		context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		StackPane root = new StackPane(canvas);
		Scene scene = new Scene(root, 640, 480);

		scene.setOnKeyReleased(this::keyReleased);

		primaryStage.setTitle("MIDIDERMI Client (JavaFX)");
		primaryStage.setScene(scene);
		primaryStage.show();

		canvas.requestFocus();

		perform();
	}

	/**
	 * Standard thread method run. It is needed because class is runnable.
	 *
	 * http://java.sys-con.com/read/46096.htm
	 */
	@Override
	public void run() {
		if (task == null) {
			return;
		}

		task.calculate();

		try {
			simpleServerObject.response(task);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

		Platform.runLater(() -> task = null);
	}

	/**
	 * Main starting point of the program. In this method remote instance is
	 * invoked and calculations are done in a loop.
	 *
	 * @param args
	 *            Command line parameters.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("RMI server address required as argument.");
			System.exit(1);
		}

		launch(args);
	}
}
