package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurtermin;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin.</p>
 * <p>Sie enthält die Daten zu einem Termine einer Klausur der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurtermin {

	private ProxyReportingGostKlausurplanungKlausurtermin proxy;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param proxy					Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 */
	public ReportingGostKlausurplanungKlausurtermin(final ProxyReportingGostKlausurplanungKlausurtermin proxy) {
		this.proxy = proxy;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Liste der Schülerklausuren zum Termin ihres Kurses.
	 * @return Die Schülerklausuren zum Kurstermin.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausurenKurstermin() {
		return proxy.getSchuelerklausurenKurstermin();
	}

	/**
	 * Die Liste der Schülerklausuren zum Termin die Nachschreibklausuren darstellen.
	 * @return Die Schülerklausuren als Nachschreibklausuren.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausurenNachschreibtermin() {
		return proxy.getSchuelerklausurenNachschreibtermin();
	}

	/**
	 * Die Startuhrzeit des Klausurtermins, falls schon gesetzt
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		if (proxy.startzeit == null)
			return "";
		return DateUtils.gibZeitStringOfMinuten(proxy.startzeit);
	}

	/**
	 * Erstellt eine Liste der Klausurräume mit Stunden für den KLausurtermin.
	 * @return Die Angaben zu Räumen und Stunden.
	 */
	public String rauemeUndStunden() {
		final List<String> tempList = new ArrayList<>();
		for (final ReportingGostKlausurplanungKlausurraum raum : klausurraeume()) {
			String temp = "";
			if (raum.kuerzel() != null)
				temp = temp + " " + raum.kuerzel();
			if (!temp.isEmpty())
				tempList.add(temp);
		}
		if (tempList.isEmpty())
			return "";
		return String.join(",", tempList);
	}



	// ##### Getter #####

	/**
	 * Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return proxy.bemerkung;
	}

	/**
	 * Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return proxy.bezeichnung;
	}

	/**
	 * Das Datum des Klausurtermins, falls schon gesetzt.
	 * @return Inhalt des Feldes datum
	 */
	public String datum() {
		return proxy.datum;
	}

	/**
	 * Das Gost-Halbjahr, in dem die Klausur geschrieben wird.
	 * @return Inhalt des Feldes gostHalbjahr
	 */
	public GostHalbjahr gostHalbjahr() {
		return GostHalbjahr.fromIDorException(proxy.halbjahr);
	}

	/**
	 * Die ID des Klausurtermins.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return proxy.id;
	}

	/**
	 * Die Information, ob es sich um einen Haupttermin handelt oder nicht.
	 * @return Inhalt des Feldes istHaupttermin
	 */
	public boolean istHaupttermin() {
		return proxy.istHaupttermin;
	}

	/**
	 * Die Klausurräume dieses Termines, inkluse der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @return Inhalt des Feldes klausurraeume
	 */
	public List<ReportingGostKlausurplanungKlausurraum> klausurraeume() {
		return proxy.getKlausurraeume();
	}

	/**
	 * Die Liste von Kursklausuren zu diesem Klausurtermin.
	 * @return Inhalt des Feldes kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> kursklausuren() {
		return proxy.getKursklausuren();
	}

	/**
	 * Die Information, ob es bei einem Haupttermin auch Nachschreibklausuren zugelassen sind oder nicht.
	 * @return Inhalt des Feldes nachschreiberZugelassen
	 */
	public boolean nachschreiberZugelassen() {
		return proxy.nachschreiberZugelassen;
	}

	/**
	 * Das Quartal, in welchem die Klausur geschrieben wird.
	 * @return Inhalt des Feldes quartal
	 */
	public int quartal() {
		return proxy.quartal;
	}

	/**
	 * Die Liste aller Schülerklausuren zu diesem Termin.
	 * @return Inhalt des Feldes schuelerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return proxy.getSchuelerklausuren();
	}
}
