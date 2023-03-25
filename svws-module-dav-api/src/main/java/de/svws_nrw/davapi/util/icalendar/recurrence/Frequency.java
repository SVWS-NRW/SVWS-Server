package de.svws_nrw.davapi.util.icalendar.recurrence;

/**
 * Dieses Enum repräsentiert die Frequenz wiederkehrender Ereignisse. vgl.
 * <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.3.10">RFC
 * 5545 </a>
 *
 */
public enum Frequency {
	/** Sekündlich */
	SECONDLY,
	/** Minütlich */
	MINUTELY,
	/** Stündlich */
	HOURLY,
	/** Täglich */
	DAILY,
	/** Wöchentlich */
	WEEKLY,
	/** Monatlich */
	MONTHLY,
	/** Jährlich */
	YEARLY
}
