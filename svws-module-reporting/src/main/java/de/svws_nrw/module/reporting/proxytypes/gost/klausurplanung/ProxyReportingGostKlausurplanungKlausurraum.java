package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.module.reporting.proxytypes.stundenplanung.ProxyReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.proxytypes.stundenplanung.ProxyReportingStundenplanungZeitrasterstunde;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausuraufsicht;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurraum}.</p>
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
public class ProxyReportingGostKlausurplanungKlausurraum extends ReportingGostKlausurplanungKlausurraum {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Reporting-Objekt.
	 * @param reportingRepository		Repository für die Reporting.
	 * @param klausurtermin 			Der Klausurtermin, dem dieser Klausurraum zugeordnet ist.
	 * @param gostKlausurraum 			Der Klausurraum mit Informationen zum Termin und dem Stundeplanraum
	 * @param gostKlausurraumstunden	Die Raumstunde mit Informationen zum Klausurraum und der Unterrichtsstunde aus dem Zeitraster.
	 */
	public ProxyReportingGostKlausurplanungKlausurraum(final ReportingRepository reportingRepository,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final GostKlausurraum gostKlausurraum,
			final List<GostKlausurraumstunde> gostKlausurraumstunden) {
		super(new ArrayList<>(),
				gostKlausurraum.bemerkung,
				gostKlausurraum.id,
				klausurtermin,
				null);

		this.reportingRepository = reportingRepository;

		// Stundenplan zum Klausurtermin ermitteln. Ohne Stundenplan gibt es keine Raumdaten und kein Zeitraster für die Aufsichten.
		final Stundenplan stundenplan = this.reportingRepository.stundenplan(super.klausurtermin.datum());

		if (stundenplan != null) {
			// Wenn bereits ein Raum der Schule (aus dem Stundenplan) der Klausur zugeordnet wurde, dann die Daten ermitteln und ergänzen.
			if (gostKlausurraum.idStundenplanRaum != null) {
				stundenplan.raeume.stream().filter(r -> r.id == gostKlausurraum.idStundenplanRaum).findFirst()
						.ifPresent(stundenplanRaum -> super.raumdaten = new ProxyReportingStundenplanungRaum(stundenplanRaum, stundenplan.id));
			}

			// Stunden der Klausur für die Aufsichten aus dem Zeitraster des Stundenplans ergänzen.
			if ((gostKlausurraumstunden != null) && !gostKlausurraumstunden.isEmpty()) {
				final List<ReportingStundenplanungZeitrasterstunde> stunden = new ArrayList<>();
				for (final GostKlausurraumstunde stunde : gostKlausurraumstunden) {
					stundenplan.zeitraster.stream().filter(z -> z.id == stunde.idZeitraster).findFirst()
							.ifPresent(stundenplanstunde -> stunden.add(new ProxyReportingStundenplanungZeitrasterstunde(stundenplanstunde, stundenplan.id)));
				}
				if (!stunden.isEmpty()) {
					stunden.sort(Comparator.comparing(ReportingStundenplanungZeitrasterstunde::unterrichtstunde));
					// TODO: Wenn die Aufsichten im Client vorhanden sind und die Datensttruikturen stehen, dann ProxyReportingGostKlausurplanungKlausuraufsicht
					//  anlegen. Zudem müssen dann auch die fehlenden Daten (hier null) ergänzt werden.
					super.aufsichten.addAll(stunden.stream().map(z -> (new ReportingGostKlausurplanungKlausuraufsicht(null, null, null, null, z))).toList());
				}
			}
		}

	}
}
