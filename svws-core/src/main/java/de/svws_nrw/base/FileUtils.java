package de.svws_nrw.base;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Diese Klasse beinhaltet allgemeine Hilfsmethoden für den Zugriff auf Dateien
 */
public class FileUtils {
	private FileUtils() {
		throw new IllegalStateException("Instantiation of " + FileUtils.class.getName() + " not allowed");
	}

	/**
	 * Diese Methode liest die angegebene UTF8-kodierte Text-Datei aus und speichert die
	 * Daten in einem String.
	 *
	 * @param file   die Datei
	 *
	 * @return der UTF-kodierte Text als String
	 */
	public static String file2UTF8(final File file) {
		try (InputStream istream = new BufferedInputStream(new FileInputStream(file))) {
			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1024];
			int length;
			while ((length = istream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			istream.close();
			return result.toString("UTF-8");
		} catch (@SuppressWarnings("unused") NullPointerException | SecurityException | IOException | IndexOutOfBoundsException e) {
			return null;
		}
	}


	/**
	 * Diese Methode liest die angegebene UTF8-kodierte Text-Datei aus und speichert die
	 * Daten in einem String.
	 *
	 * @param filename   der Dateiname
	 *
	 * @return der UTF-kodierte Text als String
	 */
	public static String file2UTF8(final String filename) {
		try {
			final File file = new File(filename);
			return file2UTF8(file);
		} catch (@SuppressWarnings("unused") final NullPointerException e) {
			return null;
		}
	}



	/**
	 * Diese Methode liest die angegebene Datei aus und speichert die
	 * Daten in einem Byte-Array.
	 *
	 * @param file   die Datei
	 *
	 * @return das Byte-Array
	 */
	public static byte[] file2ByteArray(final File file) {
		try (InputStream istream = new BufferedInputStream(new FileInputStream(file))) {
			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1024];
			int length;
			while ((length = istream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			istream.close();
			return result.toByteArray();
		} catch (@SuppressWarnings("unused") NullPointerException | SecurityException | IOException | IndexOutOfBoundsException e) {
			return null;
		}
	}


	/**
	 * Diese Methode liest die angegebene Datei aus und speichert die
	 * Daten in einem Byte-Array.
	 *
	 * @param filename   der Dateiname
	 *
	 * @return das Byte-Array
	 */
	public static byte[] file2ByteArray(final String filename) {
		try {
			final File file = new File(filename);
			return file2ByteArray(file);
		} catch (@SuppressWarnings("unused") final NullPointerException e) {
			return null;
		}
	}



	/**
	 * Verschiebt die Daten aus der Datei in den {@link OutputStream} und löscht anschließend
	 * die Quell-Datei.
	 *
	 * @param file      die zu lesende Datei und anschließend zu löschende Datei
	 * @param ostream   der {@link OutputStream}, in den die Daten geschrieben werden sollen.
	 *
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei oder
	 *                       beim Schreiben in den {@link OutputStream} ein Fehler auftritt
	 */
	public static void move(final File file, final OutputStream ostream) throws IOException {
		try (InputStream istream = new BufferedInputStream(new FileInputStream(file))) {
			final byte[] buf = new byte[65536];
			int bytes_read = -1;
			while ((bytes_read = istream.read(buf)) > -1) {
				ostream.write(buf, 0, bytes_read);
			}
			istream.close();
		}
		Files.delete(file.toPath());
	}


	/**
	 * Verschiebt die Daten aus der Datei in den {@link OutputStream} und löscht anschließend
	 * die Quell-Datei.
	 *
	 * @param filename   der Name der zu lesenden Datei und anschließend zu löschenden Datei
	 * @param ostream    der {@link OutputStream}, in den die Daten geschrieben werden sollen.
	 *
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei oder
	 *                       beim Schreiben in den {@link OutputStream} ein Fehler auftritt
	 */
	public static void move(final String filename, final OutputStream ostream) throws IOException {
		final File file = new File(filename);
		move(file, ostream);
	}


	/**
	 * Kopiert die Daten aus der Datei direkt in den {@link OutputStream}.
	 *
	 * @param file      die zu lesende Datei
	 * @param ostream   der {@link OutputStream}, in den die Daten geschrieben werden sollen.
	 *
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei oder
	 *                       beim Schreiben in den {@link OutputStream} ein Fehler auftritt
	 */
	public static void copy(final File file, final OutputStream ostream) throws IOException {
		try (InputStream istream = new BufferedInputStream(new FileInputStream(file))) {
			final byte[] buf = new byte[65536];
			int bytes_read = -1;
			while ((bytes_read = istream.read(buf)) > -1) {
				ostream.write(buf, 0, bytes_read);
			}
			istream.close();
		}
	}


	/**
	 * Kopiert die Daten aus der Datei direkt in den {@link OutputStream}.
	 *
	 * @param filename   der name der zu lesenden Datei
	 * @param ostream    der {@link OutputStream}, in den die Daten geschrieben werden sollen.
	 *
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei oder
	 *                       beim Schreiben in den {@link OutputStream} ein Fehler auftritt
	 */
	public static void copy(final String filename, final OutputStream ostream) throws IOException {
		final File file = new File(filename);
		copy(file, ostream);
	}


}
