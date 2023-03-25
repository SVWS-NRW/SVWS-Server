package de.svws_nrw.core.types.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für 
 * die Fremdsprachenarten der Gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostFremdsprachenart {

	/** Ist eine fortgeführte Fremdsprache. */
	FORTGEFUEHRT(0, "fortgeführt"),

	/** Ist eine neueinsetzende Fremdsprache. */
	NEU(1, "neu einsetzend"),

	/** Kann neueinsetzende oder fortgeführte Fremdsprache sein. */
	BELIEBIG(2, "beliebig");


	/** eine eindeutige ID für die Fremdsprachenart */
	public final int id;

	/** Die Bezeichnung der Fremdsprachenart als Text */
	public final @NotNull String bezeichnung; 


	/**
	 * Erzeugt eine neue Fremdsprachenart für die Aufzählung.
	 * 
	 * @param id            die eindeutige ID für die Fremdsprachenart
	 * @param bezeichnung   die Bezeichnung der Fremdsprachenart als Text
	 */
	private GostFremdsprachenart(final int id, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}


}
