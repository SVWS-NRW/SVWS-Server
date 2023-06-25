package de.svws_nrw.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.types.gost.GostKursart;

/**
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Kurse.txt} eines Kurs42-Textdatei-Exportes. Ein Kurs ist
 * eindeutig über das Attribut {@link #Name} spezifiziert. Die Attribute {@link #Fach} und {@link #Kursart} definieren
 * zusammen die Fachart, z.B. {@code D;LK}.
 *
 * @author Benjamin A. Bartsch, Thomas Bachran
 */
@JsonPropertyOrder({ "KursId", "Fach", "Name", "Lehrer", "Std", "Raum", "Kursart", "Schulnummer", "Schienenzahl",
	"Gesperrt", "Blocken", "ParallelKursZahl", "ParallelKursNr", "FixiertInSchiene" })
public class Kurs42DataKurse {

	/** Die ID des Kurses innerhalb von Kurs 42. Die Spalte wird ignoriert, da sie für den Import nicht nötig ist. */
	public int KursId;

	/** Das Kürzel des Faches (z.B. 'D') des Kurses. Eine von zwei Spalten in Kurs 42, um die Fachart eindeutig zu identifizieren. */
	public String Fach;

	/** Der Name des Kurses, z.B. 'D-GK1'. Der Name muss eindeutig sein. */
	public String Name;

	/** Das Kürzel des Lehrers, der dem Kurs zugeordnet ist. */
	public String Lehrer;

	/** Die Anzahl der Wochenstunden des Kurses. */
	public int Std;

	/** Der Raum, der dem Kurs zugeordnet ist. Diese Spalte wird ignoriert, da sie für den Import nicht nötig ist. */
	public String Raum;

	/** Das Kürzel der Kursart (z.B. 'LK') des Kurses. Eine von zwei Spalten in Kurs 42, um die
	 * Fachart eindeutig zu identifizieren. (siehe auch {@link GostKursart}) */
	public String Kursart;

	/** ggf. die Schulnummer. Diese Spalte wird ignoriert, da sie für den Import nicht nötig ist. */
	public int Schulnummer;

	/** Die Anzahl der Schienen die der Kurs belegt. */
	public int Schienenzahl;

	/** Blockungsregel: Eine Liste von Schienen-Nummern (0-indiziert), in denen sich der Kurs nicht befinden darf, z.B. "[0,1,2,11]" */
	public String Gesperrt;

	/** Gibt an, ob der Kurs beim Blocken berücksichtigt werden soll. Diese Spalte wird ignoriert, da sie für den Import nicht nötig ist. */
	public int Blocken;

	/** Die Anzahl der parallelen Kurse der gleichen Art */
	public String ParallelKursZahl;

	/** Die Nummer des Kurses in Bezug auf die parallelen Kurse der gleichen Art (1-indiziert) */
	public String ParallelKursNr;

	/** Gibt ggf. an, in welcher Schiene der Kurs fixiert ist */
	public String FixiertInSchiene;

}
