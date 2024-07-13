package de.svws_nrw.module.reporting.types.schule;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt.</p>
 * <p>Sie enthälten Daten zu einem Schuljahresabschnitt, also zum Schuljahr und Halbjahr.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuljahresabschnitt {

	/** Die Nummer des Abschnitts im Schuljahr */
	protected int abschnitt;

	/** Die ID des Schuljahresabschnittes */
	protected long id;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	protected int schuljahr;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abschnitt Die Nummer des Abschnitts im Schuljahr
	 * @param id Die ID des Schuljahresabschnittes
	 * @param schuljahr Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 */
	public ReportingSchuljahresabschnitt(final int abschnitt, final long id, final int schuljahr) {
		this.abschnitt = abschnitt;
		this.id = id;
		this.schuljahr = schuljahr;
	}



	// ##### Getter #####

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 * @return Inhalt des Feldes abschnitt
	 */
	public int abschnitt() {
		return abschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @return Inhalt des Feldes schuljahr
	 */
	public int schuljahr() {
		return schuljahr;
	}

}
