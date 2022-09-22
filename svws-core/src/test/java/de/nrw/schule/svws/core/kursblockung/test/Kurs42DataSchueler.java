package de.nrw.schule.svws.core.kursblockung.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Diese Klasse dient als DTO für die CSV-Tabelle {@code Schueler.txt} eines Kurs42-Textdatei-Exportes. In dieser Datei
 * sind alle SuS und ihre Daten vorhanden. Ein Schüler-Objekt ist eindeutig durch die vier Attribute {@link #Name},
 * {@link #Vorname}, {@link #Geschlecht} und {@link #GebDat} spezifiziert.
 * 
 * @author Benjamin A. Bartsch */
@JsonPropertyOrder({ "IdNr", "Status", "Geschlecht", "Jahrgang", "Klasse", "Name", "Vorname", "CollCount", "GebDat",
		"SchulNr", "DB_IdNr", "Tutor", "PruefOrd", "EMail", "KoopDBIdNr", "ExterneId", "SchulEmail", "Gruppe" })
public class Kurs42DataSchueler {

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int IdNr;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int Status;

	/** Das Geschlecht des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public int Geschlecht;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Jahrgang;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Klasse;

	/** Der Nachname des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String Name;

	/** Der Vorname des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String Vorname;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int CollCount;

	/** Das Geburtsdatum des Schülers. Eine von vier Spalten, um den Schüler zu identifizieren. */
	@JsonProperty public String GebDat;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int SchulNr;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int DB_IdNr;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Tutor;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String PruefOrd;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String EMail;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int KoopDBIdNr;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public int ExterneId;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String SchulEmail;

	/** Diese Spalte wird ignoriert, da sie zum Testen der Algorithmen nicht nötig ist. */
	@JsonProperty public String Gruppe;

}