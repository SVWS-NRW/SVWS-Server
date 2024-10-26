package de.svws_nrw.data.datenaustausch;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * DTO für den den Import aus der Kurs42-CSV-Datei
 */
@JsonPropertyOrder({ "RaumNr", "RaumKuerzel" })
public class Kurs42DataRaum {

	/** Die Raum-Nummer, eine ID aus Kurs 42 */
	public String RaumNr;

	/** Das Raum-Kürzel */
	public String RaumKuerzel;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Kurs42DataRaum() {
		// leer
	}

}
