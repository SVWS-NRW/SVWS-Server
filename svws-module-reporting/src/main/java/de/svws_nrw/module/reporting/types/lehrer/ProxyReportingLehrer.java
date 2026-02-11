package de.svws_nrw.module.reporting.types.lehrer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKursunterricht;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingLehrer}.
 */
public class ProxyReportingLehrer extends ReportingLehrer {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	// Die Factory des Lehrers, die bei Bedarf seine Klassen und Kursunterrichte erzeugen kann.
	@JsonIgnore
	private final ProxyReportingLehrerFactoryUnterricht factoryUnterrichte;

	/** Flag für den Initialisierungsstatus der Unterrichtslisten, um mehrfache DB-Anfragen zu vermeiden. */
	private boolean istInitKlassenunterrichtAlsFachlehrer = false;

	/** Flag für den Initialisierungsstatus der Unterrichtslisten, um mehrfache DB-Anfragen zu vermeiden. */
	private boolean istInitKlassenunterrichtAlsZusatzlehrer = false;

	/** Flag für den Initialisierungsstatus der Unterrichtslisten, um mehrfache DB-Anfragen zu vermeiden. */
	private boolean istInitKursunterrichtAlsFachlehrer = false;

	/** Flag für den Initialisierungsstatus der Unterrichtslisten, um mehrfache DB-Anfragen zu vermeiden. */
	private boolean istInitKursunterrichtAlsZusatzlehrer = false;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingLehrer}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param lehrerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingLehrer(final ReportingRepository reportingRepository, final LehrerStammdaten lehrerStammdaten) {
		super(ersetzeNullBlankTrim(lehrerStammdaten.amtsbezeichnung),
				ersetzeNullBlankTrim(lehrerStammdaten.anrede),
				ersetzeNullBlankTrim(lehrerStammdaten.emailPrivat),
				ersetzeNullBlankTrim(lehrerStammdaten.emailDienstlich),
				"",
				lehrerStammdaten.foto,
				ersetzeNullBlankTrim(lehrerStammdaten.geburtsdatum),
				"",
				"",
				"",
				Geschlecht.fromValue(lehrerStammdaten.geschlecht),
				ersetzeNullBlankTrim(lehrerStammdaten.hausnummer),
				ersetzeNullBlankTrim(lehrerStammdaten.hausnummerZusatz),
				lehrerStammdaten.id,
				ersetzeNullBlankTrim(lehrerStammdaten.kuerzel),
				new ArrayList<>(),
				ersetzeNullBlankTrim(lehrerStammdaten.nachname),
				PersonalTyp.fromKuerzel(lehrerStammdaten.personalTyp),
				Nationalitaeten.getByDESTATIS(lehrerStammdaten.staatsangehoerigkeitID),
				null,
				ersetzeNullBlankTrim(lehrerStammdaten.strassenname),
				ersetzeNullBlankTrim(lehrerStammdaten.telefon),
				ersetzeNullBlankTrim(lehrerStammdaten.telefonMobil),
				"",
				"",
				ersetzeNullBlankTrim(lehrerStammdaten.titel),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				(lehrerStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(lehrerStammdaten.wohnortID) : null,
				(lehrerStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(lehrerStammdaten.ortsteilID) : null);

		this.reportingRepository = reportingRepository;
		this.factoryUnterrichte = new ProxyReportingLehrerFactoryUnterricht(this.reportingRepository, this);

		lehrerStammdaten.leitungsfunktionen
				.forEach(leitungsfunktion -> super.leitungsfunktionen.add(new ProxyReportingLehrerLeitungsfunktion(reportingRepository, leitungsfunktion)));

		// Füge Stammdaten des Lehrers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapLehrerStammdaten().putIfAbsent(super.id(), lehrerStammdaten);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    Ergibt true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Liste der Klassenunterrichte, in denen der Lehrer als Fachlehrer in den Leistungsdaten eingetragen ist.
	 *
	 * @return Die Liste der Klassenunterrichte mit dem Lehrer als Fachlehrer.
	 */
	@Override
	public List<ReportingKlassenunterricht> klassenunterrichtAlsFachlehrer() {
		if (!istInitKlassenunterrichtAlsFachlehrer) {
			super.klassenunterrichtAlsFachlehrer = this.factoryUnterrichte.erstelleKlassenunterrichtAlsFachlehrer();
			istInitKlassenunterrichtAlsFachlehrer = true;
		}
		return super.klassenunterrichtAlsFachlehrer;
	}

	/**
	 * Liste der Klassenunterrichte, in denen der Lehrer als zusätzliche Lehrkraft eingetragen ist.
	 *
	 * @return Die Liste der Klassenunterrichte mit dem Lehrer als Zusatzkraft.
	 */
	@Override
	public List<ReportingKlassenunterricht> klassenunterrichtAlsZusatzlehrer() {
		if (!istInitKlassenunterrichtAlsZusatzlehrer) {
			super.klassenunterrichtAlsZusatzlehrer = factoryUnterrichte.erstelleKlassenunterrichtAlsZusatzlehrer();
			istInitKlassenunterrichtAlsZusatzlehrer = true;
		}
		return super.klassenunterrichtAlsZusatzlehrer;
	}

	/**
	 * Liste der Kursunterrichte, in denen der Lehrer der Fachlehrer laut Leistungsdaten ist, in der Regel der Kursleiter.
	 *
	 * @return Die Kursunterrichte mit dem Lehrer als Fachlehrer.
	 */
	@Override
	public List<ReportingKursunterricht> kursunterrichtAlsFachlehrer() {
		if (!istInitKursunterrichtAlsFachlehrer) {
			super.kursunterrichtAlsFachlehrer = factoryUnterrichte.erstelleKursunterrichtAlsFachlehrer();
			istInitKursunterrichtAlsFachlehrer = true;
		}
		return super.kursunterrichtAlsFachlehrer;
	}

	/**
	 * Liste der Kursunterrichte, in denen der Lehrer als zusätzliche Lehrkraft eingetragen ist.
	 *
	 * @return Die Kursunterrichte mit dem Lehrer als Zusatzkraft.
	 */
	@Override
	public List<ReportingKursunterricht> kursunterrichtAlsZusatzlehrer() {
		if (!istInitKursunterrichtAlsZusatzlehrer) {
			super.kursunterrichtAlsZusatzlehrer = factoryUnterrichte.erstellekursunterrichtAlsZusatzlehrer();
			istInitKursunterrichtAlsZusatzlehrer = true;
		}
		return super.kursunterrichtAlsZusatzlehrer;
	}

}
