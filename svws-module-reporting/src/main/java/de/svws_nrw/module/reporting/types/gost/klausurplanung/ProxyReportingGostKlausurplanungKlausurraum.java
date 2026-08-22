package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
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

		ermittleRaumdaten(stundenplan, gostKlausurraum);
		ermittleAufsichten(stundenplan, gostKlausurraumstunden);
	}

	/**
	 * Ermittelt die Raumdaten des Klausurraums aus dem Stundenplan, sofern der Klausur bereits ein Raum der Schule zugeordnet ist.
	 * <p>Findet der zum Termin gültige Stundenplan den zugewiesenen Raum nicht, bleibt die Raumangabe leer und der Befund wird gemeldet.</p>
	 *
	 * @param stundenplan     Der zum Klausurtermin gültige Stundenplan.
	 * @param gostKlausurraum Der Klausurraum mit der Zuweisung des Stundenplanraums.
	 */
	private void ermittleRaumdaten(final ReportingStundenplanungStundenplan stundenplan, final GostKlausurraum gostKlausurraum) {
		if (gostKlausurraum.idStundenplanRaum == null) {
			return;
		}
		super.raumdaten = stundenplan.raum(gostKlausurraum.idStundenplanRaum);
		if (super.raumdaten == null) {
			// Die Raumzuweisung verweist auf einen Raum, den der zum Termin gültige Stundenplan nicht führt - etwa eine Raum-ID aus einem anderen
			// Stundenplan, denn die Zuweisungsvalidierung prüft nur die globale Existenz. Die Raumangabe bleibt leer und wird gemeldet.
			this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
					ReportingProblemSchluessel.fuer(ReportingStundenplanungRaum.class, gostKlausurraum.idStundenplanRaum),
					"Der Raum %d fehlt im zum Klausurtermin gültigen Stundenplan; die Raumangabe des Klausurraums %d fehlt in der Ausgabe."
							.formatted(gostKlausurraum.idStundenplanRaum, super.id()), null);
		}
	}

	/**
	 * Ergänzt die Aufsichten des Klausurraums aus dem Zeitraster des Stundenplans.
	 * <p>Eine Raumstunde, deren Zeitrasterstunde der Stundenplan nicht führt, wird ausgelassen und gemeldet: Ungeprüft in der Liste bräche die
	 * Sortierung mit einer NullPointerException ab.</p>
	 *
	 * @param stundenplan             Der zum Klausurtermin gültige Stundenplan.
	 * @param gostKlausurraumstunden  Die Raumstunden des Klausurraums; {@code null} oder leer bedeutet keine Aufsichten.
	 */
	private void ermittleAufsichten(final ReportingStundenplanungStundenplan stundenplan, final List<GostKlausurraumstunde> gostKlausurraumstunden) {
		if ((gostKlausurraumstunden == null) || gostKlausurraumstunden.isEmpty()) {
			return;
		}

		final List<ReportingStundenplanungUnterrichtsrasterstunde> stunden = new ArrayList<>();
		for (final GostKlausurraumstunde stunde : gostKlausurraumstunden) {
			if (stunde == null) {
				continue;
			}
			final ReportingStundenplanungUnterrichtsrasterstunde rasterstunde = stundenplan.unterrichtsrasterstunde(stunde.idZeitraster);
			if (rasterstunde == null) {
				meldeFehlendeZeitrasterstunde(stunde);
			} else {
				stunden.add(rasterstunde);
			}
		}

		if (!stunden.isEmpty()) {
			stunden.sort(Comparator.comparing(ReportingStundenplanungUnterrichtsrasterstunde::stundeImUnterrichtsraster));
			super.aufsichten.addAll(stunden.stream().map(z -> (new ReportingGostKlausurplanungKlausuraufsicht(null, null, null, null, z))).toList());
		}
	}

	/**
	 * Meldet eine Raumstunde, deren Zeitrasterstunde im Stundenplan fehlt - eine fehlende Referenz aus den Fachdaten.
	 *
	 * @param stunde Die Raumstunde mit der nicht auflösbaren Zeitrasterstunde.
	 */
	private void meldeFehlendeZeitrasterstunde(final GostKlausurraumstunde stunde) {
		final long idZeitraster = (stunde.idZeitraster != null) ? stunde.idZeitraster : -1L;
		this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ReportingStundenplanungUnterrichtsrasterstunde.class, idZeitraster),
				"Die Zeitrasterstunde %d fehlt im Stundenplan; eine Aufsichtsstunde des Klausurraums %d fehlt in der Ausgabe."
						.formatted(idZeitraster, super.id()), null);
	}
}
