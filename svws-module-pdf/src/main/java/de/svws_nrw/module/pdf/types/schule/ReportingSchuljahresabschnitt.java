package de.svws_nrw.module.pdf.types.schule;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt.</p>
 * <p>Sie enthälten Daten zu einem Schuljahresabschnitt, also zum Schuljahr und Halbjahr.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuljahresabschnitt {

	/** Die Nummer des Abschnitts im Schuljahr */
	private int abschnitt;

	/** Die ID des Schuljahresabschnittes */
	private long id;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	private int schuljahr;



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



	// ##### Getter und Setter #####

	/**
	 * Die Nummer des Abschnitts im Schuljahr
	 * @return Inhalt des Feldes abschnitt
	 */
	public int abschnitt() {
		return abschnitt;
	}

	/**
	 * Die Nummer des Abschnitts im Schuljahr wird gesetzt.
	 * @param abschnitt Neuer Wert für das Feld abschnitt
	 */
	public void setAbschnitt(final int abschnitt) {
		this.abschnitt = abschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Schuljahresabschnittes wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @return Inhalt des Feldes schuljahr
	 */
	public int schuljahr() {
		return schuljahr;
	}

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt, wird gesetzt.
	 * @param schuljahr Neuer Wert für das Feld schuljahr
	 */
	public void setSchuljahr(final int schuljahr) {
		this.schuljahr = schuljahr;
	}
}
