package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;

import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.</p>
 * <p>Sie enthält die Daten zu einer Klausur eines Kurses der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungSchuelerklausur {

	/** Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden. */
	protected String bemerkung;

	/** Die ID der Schülerklausur. */
	protected long id;

	/** Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur. */
	protected ReportingGostKlausurplanungKursklausur kursklausur;

	/** Der Schüler dieser Schülerklausur. */
	protected ReportingSchueler schueler;

	/** Die Schülerklausurtermine der Schülerklausur. Es können z. B. bei Nachschrieb mehrere Termine sein. */
	protected List<ReportingGostKlausurplanungSchuelerklausurtermin> schuelerklausurtermine;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bemerkung					Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
	 * @param id						Die ID der Schülerklausur.
	 * @param kursklausur				Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur.
	 * @param schueler					Der Schüler dieser Schülerklausur.
	 * @param schuelerklausurtermine 	Die Schülerklausurtermine der Schülerklausur. Es können z. B. bei Nachschrieb mehrere Termine sein.
	 *
	 */
	public ReportingGostKlausurplanungSchuelerklausur(final String bemerkung, final long id, final ReportingGostKlausurplanungKursklausur kursklausur,
			final ReportingSchueler schueler, final List<ReportingGostKlausurplanungSchuelerklausurtermin> schuelerklausurtermine) {
		this.bemerkung = bemerkung;
		this.id = id;
		this.kursklausur = kursklausur;
		this.schueler = schueler;
		this.schuelerklausurtermine = schuelerklausurtermine;
	}


	// ##### Getter #####

	/**
	 * Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die ID der Schülerklausur.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die Kursklausur, die zu dieser Schülerklausur geführt hat. Deren Vorgaben gelten auch für die Schülerklausur.
	 * @return Inhalt des Feldes kursklausur
	 */
	public ReportingGostKlausurplanungKursklausur kursklausur() {
		return kursklausur;
	}

	/**
	 * Der Schüler dieser Schülerklausur.
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}

	/**
	 * Die Schülerklausurtermine der Schülerklausur. Es können z. B. bei Nachschrieb mehrere Termine sein.
	 * @return Inhalt des Feldes schuelerklausurtermine
	 */
	public List<ReportingGostKlausurplanungSchuelerklausurtermin> schuelerklausurtermine() {
		return schuelerklausurtermine;
	}
}
