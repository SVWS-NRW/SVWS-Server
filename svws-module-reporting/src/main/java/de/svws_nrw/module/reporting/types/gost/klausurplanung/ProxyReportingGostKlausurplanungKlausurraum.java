package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterrichtsrasterstunde;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurraum und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurraum}.
 */
public class ProxyReportingGostKlausurplanungKlausurraum extends ReportingGostKlausurplanungKlausurraum {

	/** Repository für das Reporting. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKlausurraum}.
	 *
	 * @param reportingContext		Repository für das Reporting.
	 * @param klausurtermin 			Der Klausurtermin, dem dieser Klausurraum zugeordnet ist.
	 * @param gostKlausurraum 			Der Klausurraum mit Informationen zum Termin und dem Stundeplanraum
	 * @param gostKlausurraumstunden	Die Raumstunde mit Informationen zum Klausurraum und der Unterrichtsstunde aus dem Zeitraster.
	 */
	public ProxyReportingGostKlausurplanungKlausurraum(final ReportingContext reportingContext,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final GostKlausurraum gostKlausurraum,
			final List<GostKlausurraumstunde> gostKlausurraumstunden) {
		super(new ArrayList<>(),
				ersetzeNullBlankTrim(gostKlausurraum.bemerkung),
				gostKlausurraum.id,
				klausurtermin,
				null);

		this.reportingContext = reportingContext;

		// Ohne Klausurtermin gibt es kein Datum und damit keinen Stundenplan, aus dem Raumdaten und Aufsichten ermittelt werden könnten.
		if (super.klausurtermin == null) {
			return;
		}

		// Stundenplan zum Klausurtermin ermitteln. Ohne Stundenplan gibt es keine Raumdaten und kein Zeitraster für die Aufsichten.
		final ReportingStundenplanungStundenplan stundenplan = this.reportingContext.repositoryStundenplan().stundenplan(super.klausurtermin.datum());

		if (stundenplan == null) {
			return;
		}

		// Wenn bereits ein Raum der Schule (aus dem Stundenplan) der Klausur zugeordnet wurde, dann die Daten ermitteln und ergänzen.
		if (gostKlausurraum.idStundenplanRaum != null) {
			super.raumdaten = stundenplan.raum(gostKlausurraum.idStundenplanRaum);
		}

		// Stunden der Klausur für die Aufsichten aus dem Zeitraster des Stundenplans ergänzen.
		if ((gostKlausurraumstunden == null) || gostKlausurraumstunden.isEmpty()) {
			return;
		}

		final List<ReportingStundenplanungUnterrichtsrasterstunde> stunden = new ArrayList<>();
		for (final GostKlausurraumstunde stunde : gostKlausurraumstunden) {
			if (stunde != null) {
				stunden.add(stundenplan.unterrichtsrasterstunde(stunde.idZeitraster));
			}
		}

		if (!stunden.isEmpty()) {
			stunden.sort(Comparator.comparing(ReportingStundenplanungUnterrichtsrasterstunde::stundeImUnterrichtsraster));
			super.aufsichten.addAll(stunden.stream().map(z -> (new ReportingGostKlausurplanungKlausuraufsicht(null, null, null, null, z))).toList());
		}
	}
}
