package de.nrw.schule.svws.core.kursblockung.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code Kurse.txt} eines Kurs42-Textdatei-Exportes. Ein Kurs ist
 * eindeutig über das Attribut {@link #Name} spezifiziert. Die Attribute {@link #Fach} und {@link #Kursart} definieren
 * zusammen die Fachart, z.B. {@code D;LK}.
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "KursId", "Fach", "Name", "Lehrer", "Std", "Raum", "Kursart", "Schulnummer", "Schienenzahl",
		"Gesperrt", "Blocken", "ParallelKursZahl", "ParallelKursNr" })
public class Kurs42DataKurs {

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int KursId;

	/** Das Kürzel des Faches (z.B. 'D') des Kurses. Eine von zwei Spalten, um die Fachart eindeutig zu
	 * identifizieren. */
	@JsonProperty public String Fach;

	/** Der Name des Kurses, z.B. 'D-GK1'. Der Name muss eindeutig sein. */
	@JsonProperty public String Name;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Lehrer;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Std;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Raum;

	/** Das Kürzel der Kursart (z.B. 'LK') des Kurses. Eine von zwei Spalten, um die Fachart eindeutig zu
	 * identifizieren. */
	@JsonProperty public String Kursart;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Schulnummer;

	/** Die Anzahl der Schienen die der Kurs belegt. */
	@JsonProperty public int Schienenzahl;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Gesperrt;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Blocken;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String ParallelKursZahl;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String ParallelKursNr;

}