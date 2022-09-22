package de.nrw.schule.svws.core.kursblockung.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code Fachwahlen.txt} eines Kurs42-Textdatei-Exportes. In dieser
 * Datei findet man die Fachwahlen der SuS. Pro Schüler und Fachwahl existiert eine Zeile. Ein Schüler-Objekt ist in der
 * Kurs42-Datenstruktur eindeutig durch die vier Attribute {@link #Name}, {@link #Vorname}, {@link #Geschlecht} und
 * {@link #GebDat} spezifiziert. Das gewählte Fach ist eindeutig durch die zwei Attribute {@link #Fachkrz} und
 * {@link #Kursart} spezifiziert.
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "Name", "Vorname", "GebDat", "Geschlecht", "Fachkrz", "Kursart", "Note", "Kl1", "Kl2", "KLG",
		"SoMi1", "SoMi2", "SomiG", "Fehl1", "Fehl2", "uFehl1", "uFehl2", "FehlStd", "UFehlStd", "Facharbeit",
		"Facharbeitsthema", "Kurs" })
public class Kurs42DataFachwahl {

	/** Der Nachname des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String Name;

	/** Der Vorname des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String Vorname;

	/** Das Geburtsdatum des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String GebDat;

	/** Das Geschlecht des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public int Geschlecht;

	/** Das Kürzel des Faches (z.B. 'D') der Fachwahl des Schülers. Eine von zwei Spalten, um die Fachart eindeutig zu
	 * identifizieren. */
	@JsonProperty public String Fachkrz;

	/** Das Kürzel der Kursart (z.B. 'LK') der Fachwahl des Schülers. Eine von zwei Spalten, um die Fachart eindeutig zu
	 * identifizieren. */
	@JsonProperty public String Kursart;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Note;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Kl1;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Kl2;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int KLG;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int SoMi1;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int SoMi2;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int SomiG;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Fehl1;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Fehl2;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int uFehl1;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int uFehl2;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int FehlStd;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int UFehlStd;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Facharbeit;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Facharbeitsthema;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Kurs;

}