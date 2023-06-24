package de.svws_nrw.core.types.gost;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

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

	/**
	 * Liefert TRUE, falls schriftlich, FALLS falls mündlich, andernfalls eine Exception.
	 *
	 * @return TRUE, falls schriftlich, FALLS falls mündlich, andernfalls eine Exception.
	 */
	public boolean getIstSchriftlichOrException() {
		return DeveloperNotificationException.ifNull("Schriftlichkeit sollte nicht NULL sein!", istSchriftlich);
	}

}
