package de.svws_nrw.base.kurs42;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Diese Klasse dient als DTO für die INI-Datei {@code Blockung.txt} eines Kurs42-Textdatei-Exportes. In dieser
 * Datei lassen sich grundlegende Informationen zu der Blockung auslesen. Ein Großteil der Informationen wird
 * allerdings ignoriert, da sie für den Import nicht benötigt werden.
 *
 * @author Thomas Bachran
 */
public final class Kurs42DataBlockung {

	/** Der Jahrgang, für welchen die Blockung entworfen wurde. */
	public String Jahrgang;

	/** Das Schuljahr, auf welches sich Blockung bezieht. */
	public int Jahr;

	/** Der Abschnitt, auf welchen sich die Blockung bezieht. Ggf. ist zu prüfen, ob die Schule im Quartalsbetireb ist. */
	public int Abschnitt;

	/** Der Name der Blockung. */
	public String Bezeichnung;

	/** Die Schulnummer zu welcher die Blockung gehört. */
	public String Schulnummer;


	/**
	 * Liest die INI-Datei unter dem angegebenen Pfad ein.
	 *
	 * @param path  der Pfad zu der INI-Datei.
	 *
	 * @throws IOException   für den Fall, dass der
	 */
	public Kurs42DataBlockung(final Path path) throws IOException {
		List<String> data;
		try {
			data = Files.readAllLines(path);
		} catch (@SuppressWarnings("unused") final IOException e) {
			data = Files.readAllLines(path, StandardCharsets.ISO_8859_1);
		}
		for (final String line : data) {
			if (line.startsWith("["))
				continue;
			final String[] parts = line.split("=");
			if (parts.length != 2)
				continue;
			switch (parts[0]) {
				case "Jahrgang" -> this.Jahrgang = parts[1];
				case "Jahr" -> {
					try {
						this.Jahr = Integer.parseInt(parts[1]);
					} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
						continue;
					}
				}
				case "Abschnitt" -> {
					try {
						this.Abschnitt = Integer.parseInt(parts[1]);
					} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
						continue;
					}
				}
				case "Bezeichnung" -> this.Bezeichnung = parts[1];
				case "Schulnummer" -> this.Schulnummer = parts[1];
			}
		}
	}


	@Override
	public String toString() {
		return "Kurs42DataBlockung [Jahrgang=" + Jahrgang + ", Jahr=" + Jahr + ", Abschnitt=" + Abschnitt
				+ ", Bezeichnung=" + Bezeichnung + ", Schulnummer=" + Schulnummer + "]";
	}

}
