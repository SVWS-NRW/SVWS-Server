package de.nrw.schule.svws.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.types.SchuelerStatus;

/** 
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Schueler.txt} eines Kurs42-Textdatei-Exportes. In dieser Datei
 * sind alle SuS und ihre Daten vorhanden. Ein Schüler-Objekt ist eindeutig durch die vier Attribute {@link #Name},
 * {@link #Vorname}, {@link #Geschlecht} und {@link #GebDat} spezifiziert.
 * 
 * @author Benjamin A. Bartsch, Thomas Bachran 
 */
@JsonPropertyOrder({ "IdNr", "Status", "Geschlecht", "Jahrgang", "Klasse", "Name", "Vorname", "CollCount", "GebDat",
		"SchulNr", "DB_IdNr", "Tutor", "PruefOrd", "EMail", "KoopDBIdNr", "ExterneId", "SchulEmail", "Gruppe" })
public class Kurs42DataSchueler {

	/** Eine von Kurs 42 genutzte ID des Schülers */
	public int IdNr;

	/** Der Status des Schülers. Siehe auch {@link SchuelerStatus} */
	public int Status;

	/** Das Geschlecht des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public int Geschlecht;

	/** Der Jahrgang, in dem sich der Schüler befindet. */
	public String Jahrgang;

	/** Die Klasse in der sich der Schüler befindet. */
	public String Klasse;

	/** Der Nachname des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String Name;

	/** Der Vorname des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String Vorname;

	/** ??? Diese Spalte wird ignoriert, da sie für den Import nicht nötig ist. */
	public int CollCount;

	/** Das Geburtsdatum des Schülers. Eine von vier Spalten in Kurs 42, um den Schüler zu identifizieren. */
	public String GebDat;

	/** Die Schulnummer des Schülers. Kann genutzt werden, um externe Schüler beim Import zu ermitteln (siehe Schüler-Status oben). */
	public int SchulNr;

	/** Diese Feld sollte die ID des Schülers in der Schild-Datenbank enthalten */
	public int DB_IdNr;

	/** Der Tutor, der dem Schüler zugewiesen wurde. */
	public String Tutor;

	/** Die Prüfungsordnung, die dem Schüler zugeordnet ist. */
	public String PruefOrd;

	/** Die eMail-Adresse des Schülers. */
	public String EMail;

	/** Ggf. eine Schild-ID einer Kooperations-Schülers. */
	public int KoopDBIdNr;

	/** ??? Diese Spalte wird ignoriert, da sie zum Import nicht nötig ist. */
	public int ExterneId;

	/** Die Schul-eMail-Addresse des Schülers. */
	public String SchulEmail;

	/** ??? Diese Spalte wird ignoriert, da sie zum Import nicht nötig ist. */
	public String Gruppe;

}