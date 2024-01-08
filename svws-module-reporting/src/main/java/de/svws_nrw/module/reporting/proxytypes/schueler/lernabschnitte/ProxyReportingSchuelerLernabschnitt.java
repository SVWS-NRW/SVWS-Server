package de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.klasse.ProxyReportingKlasse;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt und erweitert die Klasse {@link ReportingSchuelerLernabschnitt}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingSchuelerLernabschnitt extends ReportingSchuelerLernabschnitt {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param schuelerLernabschnittsdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuelerLernabschnitt(final ReportingRepository reportingRepository, final SchuelerLernabschnittsdaten schuelerLernabschnittsdaten) {
        super(schuelerLernabschnittsdaten.abschluss,
            schuelerLernabschnittsdaten.abschlussart,
            schuelerLernabschnittsdaten.abschlussBerufsbildend,
            schuelerLernabschnittsdaten.anzahlSchulbesuchsjahre,
            schuelerLernabschnittsdaten.bilingualerZweig,
            schuelerLernabschnittsdaten.datumAnfang,
            schuelerLernabschnittsdaten.datumEnde,
            schuelerLernabschnittsdaten.datumKonferenz,
            schuelerLernabschnittsdaten.datumZeugnis,
            schuelerLernabschnittsdaten.fehlstundenGesamt,
            schuelerLernabschnittsdaten.fehlstundenGrenzwert,
            schuelerLernabschnittsdaten.fehlstundenUnentschuldigt,
			null,
			null,
            schuelerLernabschnittsdaten.bemerkungen.foerderschwerpunkt,
			null,
            schuelerLernabschnittsdaten.hatAOSF,
            schuelerLernabschnittsdaten.hatAutismus,
            schuelerLernabschnittsdaten.hatSchwerbehinderungsNachweis,
            schuelerLernabschnittsdaten.hatZieldifferentenUnterricht,
			null,
            schuelerLernabschnittsdaten.id,
            schuelerLernabschnittsdaten.fachklasseID,
            schuelerLernabschnittsdaten.foerderschwerpunkt1ID,
            schuelerLernabschnittsdaten.foerderschwerpunkt2ID,
            schuelerLernabschnittsdaten.folgeklassenID,
            schuelerLernabschnittsdaten.jahrgangID,
            schuelerLernabschnittsdaten.klassenID,
            schuelerLernabschnittsdaten.schuelerID,
            schuelerLernabschnittsdaten.schuljahresabschnitt,
            schuelerLernabschnittsdaten.schwerpunktID,
            schuelerLernabschnittsdaten.sonderpaedagogeID,
            schuelerLernabschnittsdaten.tutorID,
            schuelerLernabschnittsdaten.istAbschlussPrognose,
            schuelerLernabschnittsdaten.istFachpraktischerAnteilAusreichend,
            schuelerLernabschnittsdaten.istGewertet,
            schuelerLernabschnittsdaten.istWiederholung,
			null,
            schuelerLernabschnittsdaten.Klassenart,
			null,
            null,
            schuelerLernabschnittsdaten.noteDurchschnitt,
            schuelerLernabschnittsdaten.noteLernbereichGSbzwAL,
            schuelerLernabschnittsdaten.noteLernbereichNW,
            schuelerLernabschnittsdaten.organisationsform,
            schuelerLernabschnittsdaten.pruefungsOrdnung,
            null,
            schuelerLernabschnittsdaten.schulgliederung,
            null,
            null,
			schuelerLernabschnittsdaten.textErgebnisPruefungsalgorithmus,
            null,
            schuelerLernabschnittsdaten.bemerkungen.uebergangESF,
            schuelerLernabschnittsdaten.bemerkungen.versetzungsentscheidung,
            schuelerLernabschnittsdaten.versetzungsvermerk,
            schuelerLernabschnittsdaten.wechselNr,
            schuelerLernabschnittsdaten.zeugnisart,
            schuelerLernabschnittsdaten.bemerkungen.zeugnisASV,
            schuelerLernabschnittsdaten.bemerkungen.zeugnisAUE,
            schuelerLernabschnittsdaten.bemerkungen.zeugnisAllgemein,
            schuelerLernabschnittsdaten.bemerkungen.zeugnisLELS);

		this.reportingRepository = reportingRepository;
		super.setFoerderschwerpunkt1(this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt1ID));
		super.setFoerderschwerpunkt2(this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt2ID));
    }


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}


	/**
	 * Stellt die Daten der Folgeklasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 * @return Daten der Folgeklasse
	 */
	@Override
	public ReportingKlasse folgeklasse() {
		if (super.folgeklasse() == null && super.idFolgeklasse() != null && super.idFolgeklasse() >= 0) {
			super.setFolgeklasse(
				new ProxyReportingKlasse(
					reportingRepository,
					reportingRepository.mapKlassen().computeIfAbsent(super.idFolgeklasse(), k -> new DataKlassendaten(reportingRepository.conn()).getFromIDOhneSchueler(super.idFolgeklasse()))));
		}
		return super.folgeklasse();
	}

	/**
	 * Stellt die Daten des Jahrgangs des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 * @return Daten des Jahrgangs
	 */
	@Override
	public ReportingJahrgang jahrgang() {
		if (super.jahrgang() == null && super.idJahrgang() != null && super.idJahrgang() >= 0) {
			super.setJahrgang(
				new ProxyReportingJahrgang(
					reportingRepository,
					reportingRepository.mapJahrgaenge().computeIfAbsent(super.idJahrgang(), k -> new DataJahrgangsdaten(reportingRepository.conn()).getFromID(super.idJahrgang()))));
		}
		return super.jahrgang();
	}

	/**
	 * Stellt die Daten der Klasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 * @return Daten der Klasse
	 */
	@Override
	public ReportingKlasse klasse() {
		if (super.klasse() == null && super.idKlasse() != null && super.idKlasse() >= 0) {
			super.setKlasse(
				new ProxyReportingKlasse(
					reportingRepository,
					reportingRepository.mapKlassen().computeIfAbsent(super.idKlasse(), k -> new DataKlassendaten(reportingRepository.conn()).getFromIDOhneSchueler(super.idKlasse()))));
		}
		return super.klasse();
	}

	// TODO Klasse für die Leistungsdaten für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

	// TODO Klasse für die Nachprüfungen für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

	/**
	 * Stellt die Daten des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 * @return Daten des Schülers
	 */
	@Override
	public ReportingSchueler schueler() {
		if (super.schueler() == null && super.idSchueler() >= 0) {
			super.setSchueler(
				new ProxyReportingSchueler(
					reportingRepository,
					reportingRepository.mapSchuelerStammdaten().computeIfAbsent(super.idSchueler(), l -> new DataSchuelerStammdaten(reportingRepository.conn()).getStammdaten(reportingRepository.conn(), super.idSchueler()))));
		}
		return super.schueler();
	}

	/**
	 * Stellt die Daten des Schuljahresabschnitts zur Verfügung, in dem dieser Lernabschnitt liegt.
	 * @return Daten des Schuljahresabschnitts
	 */
	@Override
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		if (super.schuljahresabschnitt() == null && super.idSchuljahresabschnitt()  >= 0) {
			super.setSchuljahresabschnitt(
				new ProxyReportingSchuljahresabschnitt(reportingRepositorySchule().mapSchuljahresabschnitte().get(super.idSchuljahresabschnitt())));
		}
		return super.schuljahresabschnitt();
	}


	/**
	 * Stellt die Daten des Sonderpädagogen zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 * @return Daten des Sonderpädagogen
	 */
	@Override
	public ReportingLehrer sonderpaedagoge() {
		if (super.sonderpaedagoge() == null && super.idSonderpaedagoge() != null  &&  super.idSonderpaedagoge() >= 0) {
			super.setSonderpaedagoge(
				new ProxyReportingLehrer(
					reportingRepository,
					reportingRepository.mapLehrerStammdaten().computeIfAbsent(super.idSonderpaedagoge(), l -> new DataLehrerStammdaten(reportingRepository.conn()).getFromID(super.idSonderpaedagoge()))));
		}
		return super.sonderpaedagoge();
	}

	/**
	 * Stellt die Daten des Tutors zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 * @return Daten des Tutors
	 */
	@Override
	public ReportingLehrer tutor() {
		if (super.tutor() == null && super.idTutor() >= 0) {
			super.setTutor(
				new ProxyReportingLehrer(
					reportingRepository,
					reportingRepository.mapLehrerStammdaten().computeIfAbsent(super.idTutor(), l -> new DataLehrerStammdaten(reportingRepository.conn()).getFromID(super.idTutor()))));
		}
		return super.tutor();
	}
}
