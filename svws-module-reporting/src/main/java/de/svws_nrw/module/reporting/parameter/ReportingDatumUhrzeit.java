package de.svws_nrw.module.reporting.parameter;

/**
 * Ein Termin aus einem Vorlagenparameter vom Typ DATUM_UHRZEIT, getrennt in Datum und Uhrzeit. Das Template setzt beide Teile einzeln in den Text und
 * formatiert das Datum mit den vorhandenen Datumsfunktionen. Ein leerer Parameter ergibt leere Teile statt {@code null}, damit das Template ohne
 * Null-Prüfung auskommt.
 *
 * @param datum   Das Datum als JJJJ-MM-TT oder ein leerer String.
 * @param uhrzeit Die Uhrzeit als HH:mm oder ein leerer String.
 */
public record ReportingDatumUhrzeit(String datum, String uhrzeit) {

	/** Der Termin eines leeren Parameters. */
	public static final ReportingDatumUhrzeit LEER = new ReportingDatumUhrzeit("", "");

	/**
	 * Gibt an, ob der Termin ohne Angaben ist.
	 *
	 * @return true, wenn weder Datum noch Uhrzeit vorliegen
	 */
	public boolean istLeer() {
		return datum.isEmpty() && uhrzeit.isEmpty();
	}

	/**
	 * Liefert den Termin im Format JJJJ-MM-TTTHH:mm, damit eine direkte Ausgabe im Template lesbar bleibt.
	 *
	 * @return Der Termin als Text oder ein leerer String
	 */
	@Override
	public String toString() {
		return istLeer() ? "" : (datum + "T" + uhrzeit);
	}

}
