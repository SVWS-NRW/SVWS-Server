package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausuraufsicht;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterrichtsrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurraum und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurraum}.
 */
public class ProxyReportingGostKlausurplanungKlausurraum extends ReportingGostKlausurplanungKlausurraum {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurraum}.
	 *
	 * @param reportingRepository		Repository für das Reporting.
	 * @param klausurtermin 			Der Klausurtermin, dem dieser Klausurraum zugeordnet ist.
	 * @param gostKlausurraum 			Der Klausurraum mit Informationen zum Termin und dem Stundeplanraum
	 * @param gostKlausurraumstunden	Die Raumstunde mit Informationen zum Klausurraum und der Unterrichtsstunde aus dem Zeitraster.
	 */
	public ProxyReportingGostKlausurplanungKlausurraum(final ReportingRepository reportingRepository,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final GostKlausurraum gostKlausurraum,
			final List<GostKlausurraumstunde> gostKlausurraumstunden) {
		super(new ArrayList<>(),
				ersetzeNullBlankTrim(gostKlausurraum.bemerkung),
				gostKlausurraum.id,
				klausurtermin,
				null);

		this.reportingRepository = reportingRepository;

		// Stundenplan zum Klausurtermin ermitteln. Ohne Stundenplan gibt es keine Raumdaten und kein Zeitraster für die Aufsichten.
		final ReportingStundenplanungStundenplan stundenplan = this.reportingRepository.stundenplan(super.klausurtermin.datum());

		if (stundenplan != null) {
			// Wenn bereits ein Raum der Schule (aus dem Stundenplan) der Klausur zugeordnet wurde, dann die Daten ermitteln und ergänzen.
			if (gostKlausurraum.idStundenplanRaum != null)
				super.raumdaten = stundenplan.raum(gostKlausurraum.idStundenplanRaum);

			// Stunden der Klausur für die Aufsichten aus dem Zeitraster des Stundenplans ergänzen.
			if ((gostKlausurraumstunden != null) && !gostKlausurraumstunden.isEmpty()) {
				final List<ReportingStundenplanungUnterrichtsrasterstunde> stunden = new ArrayList<>();
				for (final GostKlausurraumstunde stunde : gostKlausurraumstunden) {
					if (stunde != null)
						stunden.add(stundenplan.unterrichtsrasterstunde(stunde.idZeitraster));
				}
				if (!stunden.isEmpty()) {
					stunden.sort(Comparator.comparing(ReportingStundenplanungUnterrichtsrasterstunde::stundeImUnterrichtsraster));
					// TODO: Wenn die Aufsichten im Client vorhanden sind und die Datenstrukturen stehen, dann ProxyReportingGostKlausurplanungKlausuraufsicht
					//  anlegen. Zudem müssen dann auch die fehlenden Daten (hier null) ergänzt werden.
					super.aufsichten.addAll(stunden.stream().map(z -> (new ReportingGostKlausurplanungKlausuraufsicht(null, null, null, null, z))).toList());
				}
			}
		}
	}
}
