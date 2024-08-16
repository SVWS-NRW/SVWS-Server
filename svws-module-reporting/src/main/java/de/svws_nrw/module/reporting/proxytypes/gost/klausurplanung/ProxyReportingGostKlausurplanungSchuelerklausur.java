package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur und erweitert die Klasse {@link ReportingGostKlausurplanungKursklausur}.</p>
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
 *  	<li>Die Proxy-Klasse können zudem auf den Klausurplan {@link ReportingGostKlausurplanungKlausurplan} zugreifen. Drin ist wieder der
 *  		Zugriff auf das Repository {@link ReportingRepository} möglich.
 *  		Im ersteren kann auf Klausurmanager zugegriffen werden, um darüber Daten nachladen zu können.
 *  		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *    		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *    		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKlausurplanungSchuelerklausur extends ReportingGostKlausurplanungSchuelerklausur {


	/**
	 * Erstellt ein neues Reporting-Objekt.
	 * @param gostSchuelerklausur		Die GostKursklausur mit den Daten zur Klausur eines Kurses.
	 * @param gostSchuelerklausurtermin	Der GostSchuelerklausurtermin mit den Daten zum Klausurtermin der Schülerklausur.
	 * @param klausurraum 				Der Klausurraum dieses Schülerklausurtermines, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @param klausurtermin 			Der Termin der Schülerklausur aus den Klausurterminen.
	 * @param kursklausur 				Die Kursklausur, der diese Schülerklausur zugeordnet wurde.
	 * @param schueler					Der Shcüler zu dieser Schülerklausur.
	 */
	public ProxyReportingGostKlausurplanungSchuelerklausur(final GostSchuelerklausur gostSchuelerklausur,
			final GostSchuelerklausurTermin gostSchuelerklausurtermin, final ReportingGostKlausurplanungKlausurraum klausurraum,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final ReportingGostKlausurplanungKursklausur kursklausur,
			final ReportingSchueler schueler) {
		super(gostSchuelerklausur.bemerkung,
				gostSchuelerklausur.id,
				gostSchuelerklausurtermin.id,
				klausurraum,
				klausurtermin,
				kursklausur,
				gostSchuelerklausurtermin.folgeNr,
				schueler,
				gostSchuelerklausurtermin.startzeit);

		// Die fertige Schülerklausur der Kursklausur zuweisen, wenn diese noch nicht dort in der Liste enthalten ist.
		if ((super.kursklausur != null) && super.kursklausur.schuelerklausuren().stream().noneMatch(s -> s.id() == super.id)) {
			super.kursklausur.schuelerklausuren().add(this);
		}

		// Die fertige Schülerklausur dem Klausurtermin zuweisen, wenn diese noch nicht dort in der Liste enthalten ist.
		if ((super.klausurtermin != null) && super.klausurtermin.schuelerklausuren().stream().noneMatch(s -> s.id() == super.id)) {
			super.klausurtermin.schuelerklausuren().add(this);
		}
	}
}
