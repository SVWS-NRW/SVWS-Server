package de.svws_nrw.core.types.reporting;

/**
 * Eine Liste der möglichen Report-Ausgabeformate.
 */
public enum ReportingAusgabeformat {

	/** Report-Ausgabeformat ist HTML */
	HTML(1),

	/** Report-Ausgabeformat ist PDF */
	PDF(2);

	/** Die ID des Report-Ausgabeformats */
	private final int id;

	/**
	 * Erstellt ein neues Report-Ausgabeformat
	 * @param id Die ID des Report-Ausgabeformats
	 */
	ReportingAusgabeformat(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID dieses Report-Ausgabeformats zurück
	 * @return Die ID dieses Report-Ausgabeformats
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt das Report-Ausgabeformat anhand der übergebenen ID.
	 * @param id   	Die ID des gesuchten Report-Ausgabeformats
	 * @return 		Das Report-Ausgabeformat
	 */
	public static ReportingAusgabeformat getByID(final int id) {
		for (final ReportingAusgabeformat af : ReportingAusgabeformat.values())
			if (af.id == id)
				return af;
		return null;
	}

}
