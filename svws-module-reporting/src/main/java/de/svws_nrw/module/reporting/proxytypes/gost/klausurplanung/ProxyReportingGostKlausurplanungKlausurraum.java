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
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungZeitrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurraum und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurraum}.
 */
public class ProxyReportingGostKlausurplanungKlausurraum extends ReportingGostKlausurplanungKlausurraum {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurraum}.
	 *
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
					if (stunde != null) {
						stundenplan.zeitraster.stream().filter(z -> z.id == stunde.idZeitraster).findFirst()
								.ifPresent(
										stundenplanstunde -> stunden.add(new ProxyReportingStundenplanungZeitrasterstunde(stundenplanstunde, stundenplan.id)));
					}
				}
				if (!stunden.isEmpty()) {
					stunden.sort(Comparator.comparing(ReportingStundenplanungZeitrasterstunde::unterrichtstunde));
					// TODO: Wenn die Aufsichten im Client vorhanden sind und die Datenstrukturen stehen, dann ProxyReportingGostKlausurplanungKlausuraufsicht
					//  anlegen. Zudem müssen dann auch die fehlenden Daten (hier null) ergänzt werden.
					super.aufsichten.addAll(stunden.stream().map(z -> (new ReportingGostKlausurplanungKlausuraufsicht(null, null, null, null, z))).toList());
				}
			}
		}

	}
}
