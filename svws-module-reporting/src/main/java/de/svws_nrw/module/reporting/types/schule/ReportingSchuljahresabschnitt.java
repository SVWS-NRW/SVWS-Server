package de.svws_nrw.module.reporting.types.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;

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

	/** Die Fächer des Schuljahresabschnitts */
	protected List<ReportingFach> faecher = new ArrayList<>();



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param abschnitt Die Nummer des Abschnitts im Schuljahr
	 * @param id 		Die ID des Schuljahresabschnittes
	 * @param schuljahr Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 * @param faecher	Die Fächer des Schuljahresabschnitts
	 */
	public ReportingSchuljahresabschnitt(final int abschnitt, final long id, final int schuljahr, final List<ReportingFach> faecher) {
		this.abschnitt = abschnitt;
		this.id = id;
		this.schuljahr = schuljahr;
		this.faecher = faecher;
	}


	// ##### Berechnete Methoden #####
	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY.A
	 * @return Kurzer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittKurz() {
		return "%s/%s.%s".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Kurzer Text zum Schuljahresabschnitt im Format 20XX/YY.A
	 * @return Kurzer Text zum Schuljahresabschnitt
	 */
	public String textSchuljahresabschnittLang() {
		return "%s/%s %s. Halbjahr".formatted(schuljahr, (schuljahr % 100) + 1, abschnitt);
	}

	/**
	 * Gibt das Fach zur ID aus der Liste der Fächer des Schuljahresabschnitts zurück
	 * @param id	Die ID des Faches
	 * @return 		Das Fach zur ID oder null, wenn das Fach nicht vorhanden ist.
	 */
	public ReportingFach fach(final long id) {
		return faecher().stream().filter(f -> f.id() == id).findFirst().orElse(null);
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

	/**
	 * Die Fächer des Schuljahresabschnitts
	 * @return Inhalt des Feldes faecher
	 */
	public List<ReportingFach> faecher() {
		return faecher;
	}
}
