package de.svws_nrw.core.types.gost;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für Schriftlichkeit von
 * Kursen in der Gymnasialen Oberstufe zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum GostSchriftlichkeit {

	/** Ist mündlich. */
	MUENDLICH(false),

	/** Ist schriftlich. */
	SCHRIFTLICH(true),

	/** Kann mündlich oder schriftlich sein. */
	BELIEBIG(null);


	/** Gibt an, ob eine Schriftlichkeit vorliegt (true), nicht vorliegt (false), oder beliebig sein kann (null) */
	public final Boolean istSchriftlich;


	GostSchriftlichkeit(final Boolean istSchriftlich) {
		this.istSchriftlich = istSchriftlich;
	}

}
