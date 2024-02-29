package de.svws_nrw.module.reporting.proxytypes.gost.laufbahnplanung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.laufbahnplanung.ReportingGostLaufbahnplanungFachwahl;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostLaufbahnplanungFachwahl und erweitert die Klasse {@link ReportingGostLaufbahnplanungFachwahl}.</p>
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
 *  </ul>
 */
public class ProxyReportingGostLaufbahnplanungFachwahl extends ReportingGostLaufbahnplanungFachwahl {

	/**
	 * Erstellt ein neues Reporting-Objekt mittels Standard-Konstruktor der Super-Klasse.
	 * @param abiturfach								Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde.
	 * @param belegungEF1								Fachbelegung in der EF.1
	 * @param belegungEF2								Fachbelegung in der EF.2
	 * @param belegungQ11								Fachbelegung in der Q1.1
	 * @param belegungQ12								Fachbelegung in der Q1.2
	 * @param belegungQ21								Fachbelegung in der Q2.1
	 * @param belegungQ22								Fachbelegung in der Q2.2
	 * @param fach 										Das Fach der Fachwahl.
	 * @param fachIstBelegtInGOSt						Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 * @param fachIstFortfuehrbareFremdspracheInGOSt	Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache.
	 * @param jahrgangFremdsprachenbeginn				Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung.
	 * @param positionFremdsprachenfolge				Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk.
	 */
	public ProxyReportingGostLaufbahnplanungFachwahl(final String abiturfach, final String belegungEF1, final String belegungEF2, final String belegungQ11, final String belegungQ12, final String belegungQ21, final String belegungQ22, final ReportingFach fach, final Boolean fachIstBelegtInGOSt, final Boolean fachIstFortfuehrbareFremdspracheInGOSt, final String jahrgangFremdsprachenbeginn, final String positionFremdsprachenfolge) {
		super(abiturfach, belegungEF1, belegungEF2, belegungQ11, belegungQ12, belegungQ21, belegungQ22, fach, fachIstBelegtInGOSt, fachIstFortfuehrbareFremdspracheInGOSt, jahrgangFremdsprachenbeginn, positionFremdsprachenfolge);
	}
}
