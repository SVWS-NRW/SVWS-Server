package de.svws_nrw.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** 
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Blockplan.txt} eines Kurs42-Textdatei-Exportes. In dieser
 * Datei lässt sich die Lage der Kurse in den Schienen auslesen. Schienen sind 0-indiziert. Die Kursbezeichnung ist der
 * eindeutige Schlüssel um den Kurs zu identifizieren. Ein Wert von 1 in der Spalte {@code Fixiert} bedeutet, dass der
 * Kurs fixiert ist. Ist ein Kurs in mehreren Schienen vertreten, so gibt es mehrere Einträge in der Datei.
 * 
 * @author Benjamin A. Bartsch, Thomas Bachran 
 */
@JsonPropertyOrder({ "Schiene", "Kursbezeichnung", "Fixiert" })
public class Kurs42DataBlockplan {

	/** Die Schiene des Kurses (0-indiziert). */
	public int Schiene;

	/** Der Name des Kurses, z.B. D-GK3. */
	public String Kursbezeichnung;

	/** Gibt an, ob der Kurs in dieser Schiene fixiert ist (1), oder nicht (0). */
	public int Fixiert;

}