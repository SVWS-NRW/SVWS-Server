package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungSchuelerklausur und erweitert die Klasse {@link ReportingGostKlausurplanungSchuelerklausur}.
 */
public class ProxyReportingGostKlausurplanungSchuelerklausur extends ReportingGostKlausurplanungSchuelerklausur {


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungSchuelerklausur}.
	 *
	 * @param gostSchuelerklausur		Die GostKursklausur mit den Daten zur Klausur eines Kurses.
	 * @param gostSchuelerklausurtermin	Der GostSchuelerklausurtermin mit den Daten zum Klausurtermin der Schülerklausur.
	 * @param klausurraum 				Der Klausurraum dieses Schülerklausurtermins, inklusive der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @param klausurtermin 			Der Termin der Schülerklausur aus den Klausurterminen.
	 * @param kursklausur 				Die Kursklausur, die diese Schülerklausur zugeordnet wurde.
	 * @param schueler					Der Schüler zu dieser Schülerklausur.
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

		ersetzeStringNullDurchEmpty(this, false);
	}
}
