package de.svws_nrw.davapi.util.icalendar.recurrence;

/**
 * Diese Aufzählung repräsentiert die Möglichen Wochentage, welche im
 * {@link RRule#getByDays()} auftreten können.<br>
 * Vgl.
 * <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.3.10">RFC
 * 5545</a> <br>
 * <code>
The BYDAY rule part specifies a COMMA-separated list of days of
the week; SU indicates Sunday; MO indicates Monday; TU indicates
Tuesday; WE indicates Wednesday; TH indicates Thursday; FR
indicates Friday; and SA indicates Saturday.
 *</code>
 */
public enum WeekDay {
	/** Sonntag */
	SUNDAY("SU"),
	/** Montag */
	MONDAY("MO"),
	/** Dienstag */
	TUESDAY("TU"),
	/** Mittwoch */
	WEDNESDAY("WE"),
	/** Donnerstag */
	THURSDAY("TH"),
	/** Freitag */
	FRIDAY("FR"),
	/** Samstag */
	SATURDAY("SA");

	private final String stringRep;

	WeekDay(final String stringRep) {
		this.stringRep = stringRep;
	}

	/**
	 * Gibt die kurze Stringrepräsentation des Wochentags wieder. <code>
	 * SU indicates Sunday; MO indicates Monday; TU indicates
	Tuesday; WE indicates Wednesday; TH indicates Thursday; FR
	indicates Friday; and SA indicates Saturday.</code>
	 *
	 * @return das Kürzel des Wochentags
	 */
	public String getStringRep() {
		return stringRep;
	}

	/**
	 * Gibt den Wochentag anhand der gegebenen Stringrepräsentation wieder. Zunächst
	 * wird versucht über das Kürzel (SU,MO,TU,WE,TH,FR,SA) den Wochentag zu finden,
	 * ansonsten wird auf {@link #valueOf(String)} zurückgegriffen.
	 *
	 * @param string die Zeichenkette für die der Wochentag gesucht werden soll.
	 * @return den Wochentag anhand der Zeichenkette
	 *
	 */
	public static WeekDay fromStringRep(final String string) {
		switch (string) {
		case "SU":
			return SUNDAY;
		case "MO":
			return MONDAY;
		case "TU":
			return TUESDAY;
		case "WE":
			return WEDNESDAY;
		case "TH":
			return THURSDAY;
		case "FR":
			return FRIDAY;
		case "SA":
			return SATURDAY;
		default:
			return valueOf(string);
		}
	}
}
