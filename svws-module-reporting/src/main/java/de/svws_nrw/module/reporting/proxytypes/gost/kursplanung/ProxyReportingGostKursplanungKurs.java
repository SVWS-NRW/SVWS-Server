package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import java.util.List;

import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungSchiene;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs und erweitert die Klasse {@link ReportingGostKursplanungKurs}.</p>
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
 *    	<li>Die Proxy-Klasse können zudem auf das Blockungsergebnis {@link ReportingGostKursplanungSchiene} zugreifen. Drin ist wieder der
 *    		Zugriff auf das Repository {@link ReportingGostKursplanungSchiene} möglich. Im ersteren kann auf die Ergebnis- und Datenmanager der
 *    		Blockung zugegriffen werden, um darüber Daten nachladen zu können.
 *    		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *     		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *     		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKursplanungKurs extends ReportingGostKursplanungKurs {

	/** Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört. */
	private final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param reportingGostKursplanungBlockungsergebnis Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört.
	 * @param anzahlAB12				Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3					Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4					Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlExterne				Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlKlausurschreiber	Anzahl der Klausurschreiber.
	 * @param anzahlSchueler			Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param bezeichnung				Bezeichnung des Kurses.
	 * @param farbeClientRGB 			Farbe des Faches des Kurses im Web-Client.
	 * @param gostHalbjahr				Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param id						ID des Kurses.
	 * @param kursart					Kursart des Kurses.
	 * @param lehrkraefte				Liste der Lehrkräfte des Kurses.
	 * @param schienen                  Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @param schueler					Liste der Schüler des Kurses.
	 */
	public ProxyReportingGostKursplanungKurs(final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis, final int anzahlAB12, final int anzahlAB3, final int anzahlAB4, final int anzahlDummy, final int anzahlExterne, final int anzahlKlausurschreiber, final int anzahlSchueler, final String bezeichnung, final String farbeClientRGB, final String gostHalbjahr, final long id, final String kursart, final List<ReportingLehrer> lehrkraefte, final List<ReportingGostKursplanungSchiene> schienen, final List<ReportingSchueler> schueler) {
		super(anzahlAB12, anzahlAB3, anzahlAB4, anzahlDummy, anzahlExterne, anzahlKlausurschreiber, anzahlSchueler, bezeichnung, farbeClientRGB, gostHalbjahr, id, kursart, lehrkraefte, schienen, schueler);
		this.reportingGostKursplanungBlockungsergebnis = reportingGostKursplanungBlockungsergebnis;
	}



	/**
	 * Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @return Liste mit Schienen des Kurses
	 */
	@Override
	public List<ReportingGostKursplanungSchiene> schienen() {
		if (super.schienen() == null || super.schienen().isEmpty()) {
			super.schienen().addAll(reportingGostKursplanungBlockungsergebnis.schienen()
				.stream()
				.filter(s -> s.kurse().stream().anyMatch(k -> k.id() == this.id()))
				.toList());
		}
		return super.schienen();
	}

	/**
	 * Liste vom Typ Schüler, die die Schüler beinhaltet, die den Kurs gemäß Blockungsergebnis belegen.
	 * @return Liste mit Schülern des Kurses
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler() == null || super.schueler().isEmpty()) {
			super.schueler().addAll(reportingGostKursplanungBlockungsergebnis.schueler()
				.stream()
					.filter(s -> s.gostKursplanungKursbelegungen()
						.stream()
						.anyMatch(k -> k.kurs().id() == this.id()))
				.toList());
		}
		return super.schueler();
	}
}
