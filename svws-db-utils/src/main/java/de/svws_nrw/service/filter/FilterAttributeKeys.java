package de.svws_nrw.service.filter;

/**
 * Diese Klasse speichert alle aktuell aktiven Attribut Schlüssel.
 */
public final class FilterAttributeKeys {

	private FilterAttributeKeys() {
	}

	/** Schüler Status */
	public static final FilterAttributeKey SCHUELER_STATUS = new FilterAttributeKey("schueler.status");

	/** Schüler Geschlecht */
	public static final FilterAttributeKey SCHUELER_GESCHLECHT = new FilterAttributeKey("schueler.geschlecht");

	/** Schüler Wohnort */
	public static final FilterAttributeKey SCHUELER_WOHNORT = new FilterAttributeKey("schueler.wohnort");
}
