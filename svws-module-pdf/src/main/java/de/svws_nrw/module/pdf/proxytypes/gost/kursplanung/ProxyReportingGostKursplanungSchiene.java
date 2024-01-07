package de.svws_nrw.module.pdf.proxytypes.gost.kursplanung;

import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungSchiene;

import java.util.List;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungSchiene und erweitert die Klasse {@link ReportingGostKursplanungSchiene}.</p>
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
 *  	<li>Die Proxy-Klasse können zudem auf das Blockungsergebnis {@link ReportingGostKursplanungSchiene} zugreifen. Drin ist wieder der
 *  		Zugriff auf das Repository {@link ReportingGostKursplanungSchiene} möglich. Im ersteren kann auf die Ergebnis- und Datenmanager der
 *  		Blockung zugegriffen werden, um darüber Daten nachladen zu können.
 *  		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *    		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *    		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKursplanungSchiene extends ReportingGostKursplanungSchiene {

	/** Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört. */
	private final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param reportingGostKursplanungBlockungsergebnis Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört.
	 * @param anzahlDummy				Anzahl der Dummy-Schüler in der Schiene
	 * @param anzahlExterne				Anzahl der externen Schüler in der Schiene
	 * @param anzahlSchueler			Anzahl der Schüler in der Schien
	 * @param bezeichnung				Bezeichnung der Schiene
	 * @param hatKollisionen			Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @param id						ID der Schiene
	 * @param idsKurseMitKollisionen 	Eine Liste mit IDs der Kurse in der Schiene, die eine Kollision enthalten.
	 * @param idsSchuelerMitKollisionen	Eine Liste mit IDs der Schüler in der Schiene, die eine Kollision enthalten.
	 * @param kurse						Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @param nummer					Die Nummer der Schiene.
	 */
	public ProxyReportingGostKursplanungSchiene(final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis, final int anzahlDummy, final int anzahlExterne, final int anzahlSchueler, final String bezeichnung, final boolean hatKollisionen, final Long id, final List<Long> idsKurseMitKollisionen, final List<Long> idsSchuelerMitKollisionen, final List<ReportingGostKursplanungKurs> kurse, final int nummer) {
		super(anzahlDummy, anzahlExterne, anzahlSchueler, bezeichnung, hatKollisionen, id, idsKurseMitKollisionen, idsSchuelerMitKollisionen, kurse, nummer);
		this.reportingGostKursplanungBlockungsergebnis = reportingGostKursplanungBlockungsergebnis;
	}



	/**
	 * Liste vom Typ Kurs, die die Kurse beinhaltet, die in dieser Schiene gemäß Blockungsergebnis liegt.
	 * @return Liste mit Schienen des Kurses
	 */
	@Override
	public List<ReportingGostKursplanungKurs> kurse() {
		if (super.kurse() == null || super.kurse().isEmpty()) {
			super.kurse().addAll(reportingGostKursplanungBlockungsergebnis.kurse()
				.stream()
				.filter(k -> k.schienen().stream().anyMatch(s -> s.id() == this.id()))
				.toList());
		}
		return super.kurse();
	}
}
