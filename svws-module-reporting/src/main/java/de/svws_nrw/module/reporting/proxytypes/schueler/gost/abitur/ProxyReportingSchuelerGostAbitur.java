package de.svws_nrw.module.reporting.proxytypes.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.module.reporting.proxytypes.gost.abitur.ProxyReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;

import java.util.ArrayList;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostAbitur und erweitert die Klasse {@link ReportingSchuelerGostAbitur}.</p>
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
public class ProxyReportingSchuelerGostAbitur extends ReportingSchuelerGostAbitur {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten aus der Datenbank.
	 * @param reportingRepository Repository für die Reporting.
	 * @param abiturdaten Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingSchuelerGostAbitur(final ReportingRepository reportingRepository, final Abiturdaten abiturdaten) {
		super(abiturdaten.abiturjahr,
			abiturdaten.besondereLernleistung,
			null,
			abiturdaten.besondereLernleistungThema,
			abiturdaten.bewertetesHalbjahr,
			abiturdaten.bilingualeSprache,
			abiturdaten.block1AnzahlKurse,
			abiturdaten.block1DefiziteGesamt,
			abiturdaten.block1DefiziteLK,
			abiturdaten.block1FehlstundenGesamt,
			abiturdaten.block1FehlstundenUnentschuldigt,
			abiturdaten.block1NotenpunkteDurchschnitt,
			abiturdaten.block1PunktSummeGK,
			abiturdaten.block1PunktSummeLK,
			abiturdaten.block1PunktSummeNormiert,
			abiturdaten.block1Zulassung,
			abiturdaten.block2DefiziteGesamt,
			abiturdaten.block2DefiziteLK,
			abiturdaten.block2PunktSumme,
			new ArrayList<>(),
			abiturdaten.freiwilligerRuecktritt,
			abiturdaten.gesamtPunkte,
			abiturdaten.gesamtPunkteVerbesserung,
			abiturdaten.gesamtPunkteVerschlechterung,
			abiturdaten.note,
			abiturdaten.projektKursThema,
			abiturdaten.pruefungBestanden,
			abiturdaten.schuljahrAbitur);
		this.reportingRepository = reportingRepository;

		super.setBesondereLernleistungNote(Note.fromKuerzel(abiturdaten.besondereLernleistungNotenKuerzel));

		super.fachbelegungen().addAll(abiturdaten.fachbelegungen.stream().map(f -> new ProxyReportingGostAbiturFachbelegung(this.reportingRepository, f)).toList());

		this.fachbelegungen().sort(ReportingGostAbiturFachbelegung::compareToGost);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}
}
