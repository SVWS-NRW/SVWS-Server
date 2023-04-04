package de.svws_nrw.davapi.util.icalendar;

/**
 * Diese Aufzählung benennt die bekannten Property Keys für in
 * VCalendar-Objekten benutzte Properties
 *
 */
public enum PropertyKeys {
	/** Begin-Property für Kalender, Timezone, Events */
	BEGIN,
	/** End-Property für Kalender, Timezone, Events */
	END,
	/** Versions-Property */
	VERSION,
	/** ID Definition für Zeitzonen */
	TZID,
	/** Property für den Start des Ereignisses */
	DTSTART,
	/** Property für das Ende eines Ereignisses */
	DTEND,
	/** UID Property */
	UID,
	/** Recurrence Rule Property */
	RRULE,
	/** Recurrent Date Property */
	RDATE,
	/** Property Key für ausgenommene Daten */
	EXDATE,
	/** Ortsdaten */
	LOCATION,
	/** Erstelldatum */
	CREATED,
	/** LastModified eines VEvents */
	LASTMODIFIED,
	/** Summary/Titel */
	SUMMARY,
	/** Beschreibung des Events */
	DESC;

	/**
	 * Formatiert dieses Property zusammen mit Argumenten. Dabei werden die
	 * Argumente mit Semikolon getrennt und als kommagetrennte Liste angegebene<br>
	 * <code>DTSTART;TZID=EUROPE/BERLIN</code>
	 *
	 * @param arguments eine Liste von zuzufügenden Argumenten
	 * @return den Formattierten PropertyKey
	 *
	 */
	public String toStringWithArguments(final String... arguments) {
		return name() + ";" + String.join(";", arguments);
	}
}
