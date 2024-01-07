package de.svws_nrw.module.pdf.proxytypes.jahrgang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.module.pdf.proxytypes.klasse.ProxyReportingKlasse;
import de.svws_nrw.module.pdf.repositories.ReportingRepository;
import de.svws_nrw.module.pdf.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.pdf.types.klasse.ReportingKlasse;
import de.svws_nrw.module.pdf.types.schueler.ReportingSchueler;

import java.util.Comparator;
import java.util.List;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Jahrgang und erweitert die Klasse {@link ReportingJahrgang}.</p>
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
public class ProxyReportingJahrgang extends ReportingJahrgang {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param jahrgangsDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingJahrgang(final ReportingRepository reportingRepository, final JahrgangsDaten jahrgangsDaten) {
		super(jahrgangsDaten.bezeichnung,
			jahrgangsDaten.gueltigBis,
			jahrgangsDaten.gueltigVon,
			null,
			jahrgangsDaten.id,
			jahrgangsDaten.idFolgejahrgang,
			null,
			jahrgangsDaten.kuerzel,
			jahrgangsDaten.kuerzelSchulgliederung,
			jahrgangsDaten.kuerzelStatistik,
			jahrgangsDaten.istSichtbar,
			null,
			jahrgangsDaten.sortierung);
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
	 * Stellt die Daten des Folgejahrgangs zur Verfügung.
	 * @return Daten des Folgejahrgangs
	 */
	@Override
	public ReportingJahrgang folgejahrgang() {
		if (super.folgejahrgang() == null && super.idFolgejahrgang() != null && (super.idFolgejahrgang() >= 0)) {
			super.setFolgejahrgang(
				new ProxyReportingJahrgang(
					this.reportingRepository,
					this.reportingRepository.mapJahrgaenge()
						.computeIfAbsent(super.idFolgejahrgang(), j -> new DataJahrgangsdaten(this.reportingRepository.conn()).getFromID(super.idFolgejahrgang()))));
		}
		return super.folgejahrgang();
	}

	/**
	 * Stellt eine Liste mit Klassen des Jahrgangs zur Verfügung.
	 * @return	Liste mit Klassen
	 */
	@Override
	public List<ReportingKlasse> klassen() {
		if (super.klassen().isEmpty()) {
			super.setKlassen(
				new DataKlassendaten(this.reportingRepository.conn())
					.getFromSchuljahresabschnittsIDOhneSchueler(this.reportingRepository.aktuellerSchuljahresabschnitt().id).stream()
					.filter(kd -> kd.idJahrgang == super.id())
					.map(kd -> this.reportingRepository.mapKlassen().computeIfAbsent(kd.id, l -> kd))
					.map(k ->
						(ReportingKlasse) new ProxyReportingKlasse(
							this.reportingRepository,
							k))
					.sorted(Comparator
						.comparing(ReportingKlasse::kuerzel)
						.thenComparing(ReportingKlasse::parallelitaet))
					.toList());
		}
		return super.klassen();
	}

	/**
	 * Stellt eine Liste mit Schülern des Jahrgangs zur Verfügung.
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty()) {
			super.setSchueler(klassen().stream()
				.flatMap(k -> k.schueler().stream())
				.sorted(Comparator
					.comparing(ReportingSchueler::nachname)
					.thenComparing(ReportingSchueler::vorname)
					.thenComparing(ReportingSchueler::geburtsdatum))
				.toList());
		}
		return super.schueler();
	}
}
