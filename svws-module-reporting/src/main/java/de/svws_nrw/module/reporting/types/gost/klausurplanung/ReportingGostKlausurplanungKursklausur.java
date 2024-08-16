package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.List;

import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur.</p>
 * <p>Sie enthält die Daten zu einer Klausur eines Kurses der Klausurplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKursklausur {

	private ProxyReportingGostKlausurplanungKursklausur proxy;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param proxy		Das Proxy-Objekt
	 */
	public ReportingGostKlausurplanungKursklausur(final ProxyReportingGostKlausurplanungKursklausur proxy) {
		this.proxy = proxy;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Die Anzahl an Schülern, die an diese Kursklausur schreiben müssen.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKlausur() {
		if (schuelerklausuren().isEmpty())
			return "00";
		final int anzahl = schuelerklausuren().size();
		return anzahl < 10 ? ("0" + anzahl) : ("" + anzahl);
	}

	/**
	 * Die Anzahl an Schüler, die im Kurs dieser Klausur sind.
	 * @return Anzahl der Schüler
	 */
	public String anzahlSchuelerKurs() {
		if (this.kurs() == null)
			return "00";
		final int anzahl = this.kurs().schueler().size();
		if (anzahl < 10)
			return "0" + anzahl;
		return "" + anzahl;
	}

	/**
	 * Die Liste aller Namen der regulären Klausurschreiber dieser Kursklausur.
	 * @return	Liste der Klausurschreiber.
	 */
	public List<String> klausurschreiberNamen() {
		return schuelerklausuren().stream().map(s -> s.schueler().vorname() + " " + s.schueler().nachname()).toList();
	}

	/**
	 * Die Liste der Räume, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Liste der Räume der Kursklausur.
	 */
	public List<String> raeume() {
		return proxy.raeume().stream().map(r -> r.kuerzel()).distinct().toList();
	}

	/**
	 * Die Startuhrzeit der Kursklausur, falls schon gesetzt.
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String startuhrzeit() {
		return proxy.getStartuhrzeit();
	}

	/**
	 * Die Unterrichtsstunden, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Unterrichtsstunden der Klausur.
	 */
	public List<Integer> stunden() {
		return proxy.stunden();
	}

	/**
	 * Die kommaseparierte Liste in Textform aller Namen der regulären Klausurschreiber dieser Kursklausur.
	 * @return	Liste der Klausurschreiber als Text.
	 */
	public String textKlausurschreiberNamen() {
		if (this.kurs() == null) {
			return "";
		} else {
			return String.join(", ", klausurschreiberNamen());
		}
	}

	/**
	 * Die kommaseparierte Liste der Räume, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Liste der Räume der Kursklausur als Text.
	 */
	public String textRaeume() {
		if (schuelerklausuren().isEmpty()) {
			return "";
		} else {
			return String.join(", ", raeume());
		}
	}

	/**
	 * Die Unterichtstunden als Zeitbereich in Textform.
	 * @return Die Unterichtstunden der Klausur als Text.
	 */
	public String textStunden() {
		if (!stunden().isEmpty())
			return stunden().getFirst() + "-" + stunden().getLast();
		else
			return "";
	}



	// ##### Getter #####

	/**
	 * Die Auswahlzeit in Minuten, sofern vorhanden.
	 * @return Inhalt des Feldes auswahlzeit
	 */
	public int auswahlzeit() {
		return proxy.getVorgabe().auswahlzeit;
	}

	/**
	 * Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return proxy.bemerkung;
	}

	/**
	 * Die Dauer der Klausur in Minuten.
	 * @return Inhalt des Feldes dauer
	 */
	public int dauer() {
		return proxy.getVorgabe().dauer;
	}

	/**
	 * Die ID der Kursklausur.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return proxy.id;
	}

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen.
	 * @return Inhalt des Feldes istAudioNotwendig
	 */
	public boolean istAudioNotwendig() {
		return proxy.getVorgabe().istAudioNotwendig;
	}

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt.
	 * @return Inhalt des Feldes istMdlPruefung
	 */
	public boolean istMdlPruefung() {
		return proxy.getVorgabe().istMdlPruefung;
	}

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse.
	 * @return Inhalt des Feldes istVideoNotwendig
	 */
	public boolean istVideoNotwendig() {
		return proxy.getVorgabe().istVideoNotwendig;
	}

	/**
	 * Der Termin aus der Klausurplanung, an dem diese Kursklausur stattfindet.
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin klausurtermin() {
		return proxy.getTermin();
	}

	/**
	 * Der Kurs, indem die Klausur geschrieben wird, mit seinen Daten.
	 * @return Inhalt des Feldes kurs
	 */
	public ReportingKurs kurs() {
		return proxy.getKurs();
	}

	/**
	 * Die Liste der Schüler aus dem Kurs, die diese Klausur schreiben.
	 * @return Inhalt des Feldes klausurschreiber
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren() {
		return proxy.getSchuelerklausuren();
	}

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, wenn abweichend vom Klausurtermin, sonst null.
	 * @return Inhalt des Feldes startzeit
	 */
	public Integer startzeit() {
		return proxy.startzeit;
	}
}
