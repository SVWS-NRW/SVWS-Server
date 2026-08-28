package de.svws_nrw.module.reporting.types.lehrer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKursunterricht;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingLehrer}.
 */
public class ProxyReportingLehrer extends ReportingLehrer {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

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
	 * @param reportingContext Repository für das Reporting.
	 * @param lehrerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingLehrer(final ReportingContext reportingContext, final LehrerStammdaten lehrerStammdaten) {
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
				Nationalitaeten.data().getWertByIDOrNull(lehrerStammdaten.idStaatsangehoerigkeit),
				null,
				ersetzeNullBlankTrim(lehrerStammdaten.strassenname),
				ersetzeNullBlankTrim(lehrerStammdaten.telefon),
				ersetzeNullBlankTrim(lehrerStammdaten.telefonMobil),
				"",
				"",
				ersetzeNullBlankTrim(lehrerStammdaten.titel),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				reportingContext.repositoryKataloge().ort(lehrerStammdaten.wohnortID),
				reportingContext.repositoryKataloge().ortsteil(lehrerStammdaten.ortsteilID));

		this.reportingContext = reportingContext;
		factoryUnterrichte = new ProxyReportingLehrerFactoryUnterricht(this.reportingContext, this);

		lehrerStammdaten.leitungsfunktionen
				.forEach(leitungsfunktion -> super.leitungsfunktionen.add(new ProxyReportingLehrerLeitungsfunktion(leitungsfunktion)));

		// Registriere die Stammdaten des Lehrers für die weitere Verwendung im Repository.
		reportingContext.repositoryLehrer().registriereStammdaten(super.id(), lehrerStammdaten);
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
	public ReportingContext reportingContext() {
		return reportingContext;
	}


	/**
	 * Liste der Klassenunterrichte, in denen der Lehrer als Fachlehrer in den Leistungsdaten eingetragen ist.
	 *
	 * @return Die Liste der Klassenunterrichte mit dem Lehrer als Fachlehrer.
	 */
	@Override
	public List<ReportingKlassenunterricht> klassenunterrichtAlsFachlehrer() {
		if (!istInitKlassenunterrichtAlsFachlehrer) {
			super.klassenunterrichtAlsFachlehrer = factoryUnterrichte.klassenunterrichtAlsFachlehrer();
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
			super.klassenunterrichtAlsZusatzlehrer = factoryUnterrichte.klassenunterrichtAlsZusatzlehrer();
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
			super.kursunterrichtAlsFachlehrer = factoryUnterrichte.kursunterrichtAlsFachlehrer();
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
			super.kursunterrichtAlsZusatzlehrer = factoryUnterrichte.kursunterrichtAlsZusatzlehrer();
			istInitKursunterrichtAlsZusatzlehrer = true;
		}
		return super.kursunterrichtAlsZusatzlehrer;
	}

	/**
	 * Holt das Foto der Lehrkraft aus dem Repository. Gerufen wird die Methode nur, wenn die Stammdaten es nicht schon mitgebracht haben.
	 *
	 * @return Das Foto im Base64-Format; nie {@code null}, bei fehlendem Foto ein leerer String.
	 */
	@Override
	protected String ladeFoto() {
		return this.reportingContext.repositoryLehrer().lehrerFoto(this.id());
	}

}
