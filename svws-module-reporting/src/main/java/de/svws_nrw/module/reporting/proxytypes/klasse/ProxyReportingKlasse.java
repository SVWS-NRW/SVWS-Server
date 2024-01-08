package de.svws_nrw.module.reporting.proxytypes.klasse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Klasse und erweitert die Klasse {@link ReportingKlasse}.</p>
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
public class ProxyReportingKlasse extends ReportingKlasse {

	/** Collator für die deutsche Sortierung von Einträgen */
	@JsonIgnore
	private final Collator colGerman = java.text.Collator.getInstance(Locale.GERMAN);

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 *
	 * @param reportingRepository Repository für die Reporting.
	 * @param klassenDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingKlasse(final ReportingRepository reportingRepository, final KlassenDaten klassenDaten) {
		super(klassenDaten.beginnSommersemester,
			klassenDaten.beschreibung,
			null,
			klassenDaten.id,
			klassenDaten.idAllgemeinbildendOrganisationsform,
			klassenDaten.idBerufsbildendOrganisationsform,
			klassenDaten.idFachklasse,
			klassenDaten.idFolgeklasse,
			klassenDaten.idJahrgang,
			klassenDaten.idKlassenart,
			klassenDaten.klassenLeitungen,
			klassenDaten.schueler.stream().map(s -> s.id).toList(),
			klassenDaten.idSchulgliederung,
			klassenDaten.idSchuljahresabschnitt,
			klassenDaten.idVorgaengerklasse,
			klassenDaten.idWeiterbildungOrganisationsform,
			klassenDaten.istSichtbar,
			null,
			null,
			klassenDaten.kuerzel,
			klassenDaten.kuerzelFolgeklasse,
			klassenDaten.kuerzelVorgaengerklasse,
			klassenDaten.parallelitaet,
			klassenDaten.pruefungsordnung,
			null,
			klassenDaten.sortierung,
			klassenDaten.teilstandort,
			klassenDaten.verwendungAnkreuzkompetenzen,
			null);
		this.reportingRepository = reportingRepository;
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}

	/**
	 * Stellt die Daten der Folgeklasse der Klasse zur Verfügung, wenn diese bereits existiert.
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
	 * Stellt die Jahrgangsdaten der Klasse zur Verfügung.
	 * @return	Die Jahrgangsdaten der Klasse.
	 */
	@Override
	public ReportingJahrgang jahrgang() {
		if (super.jahrgang() == null && super.idJahrgang() != null && super.idJahrgang() >= 0) {
			super.setJahrgang(
				new ProxyReportingJahrgang(
					this.reportingRepository,
					this.reportingRepository.mapJahrgaenge().computeIfAbsent(super.idJahrgang(), j -> new DataJahrgangsdaten(this.reportingRepository.conn()).getFromID(super.idJahrgang()))));
		}
		return super.jahrgang();
	}

	/**
	 * Stellt eine Liste mit Lehrern in der Funktion der Klassenleitung der Klasse zur Verfügung.
	 * @return	Liste von Lehrern als Klassenleitungen
	 */
	@Override
	public List<ReportingLehrer> klassenleitungen() {
		if (super.klassenleitungen().isEmpty() && !super.idsKlassenleitungen().isEmpty()) {
			super.setKlassenleitungen(
				super.idsKlassenleitungen().stream()
					.map(klId -> this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(klId, l -> new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(klId)))
					.map(l ->
						(ReportingLehrer) new ProxyReportingLehrer(
							this.reportingRepository,
							l))
					.sorted(Comparator
						.comparing(ReportingLehrer::nachname, colGerman)
						.thenComparing(ReportingLehrer::vorname, colGerman)
						.thenComparing(ReportingLehrer::kuerzel, colGerman))
					.toList()
			);
		}
		return super.klassenleitungen();
	}

	/**
	 * Stellt eine Liste mit Schülern der Klasse zur Verfügung.
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty() && !super.idsSchueler().isEmpty()) {
			final KlassenDaten tempKlasse = this.reportingRepository.mapKlassen().compute(super.id(), (k, v) -> new DataKlassendaten(this.reportingRepository.conn()).getFromID(super.id()));
			super.setSchueler(
				DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(), tempKlasse.schueler.stream().map(s -> s.id).toList()).stream()
					.map(s -> this.reportingRepository.mapSchuelerStammdaten().computeIfAbsent(s.id, k -> s))
					.map(s ->
						(ReportingSchueler) new ProxyReportingSchueler(
							this.reportingRepository,
							s))
					.sorted(Comparator
						.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::geburtsdatum, colGerman)
						.thenComparing(ReportingSchueler::id, colGerman))
				.toList());
		}
		return super.schueler();
	}

	/**
	 * Stellt die Daten der Vorgängerklasse der Klasse zur Verfügung.
	 * @return Daten der Vorgängerklasse
	 */
	@Override
	public ReportingKlasse vorgaengerklasse() {
		if (super.vorgaengerklasse() == null && super.idVorgaengerklasse() != null && super.idVorgaengerklasse() >= 0) {
			super.setVorgaengerklasse(
				new ProxyReportingKlasse(
					reportingRepository,
					reportingRepository.mapKlassen().computeIfAbsent(super.idVorgaengerklasse(), k -> new DataKlassendaten(reportingRepository.conn()).getFromIDOhneSchueler(super.idVorgaengerklasse()))));
		}
		return super.vorgaengerklasse();
	}

}
