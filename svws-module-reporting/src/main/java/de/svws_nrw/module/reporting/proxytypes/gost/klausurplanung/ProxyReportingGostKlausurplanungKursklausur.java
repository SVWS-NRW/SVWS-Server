package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;


/**
 * <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur und erweitert die Klasse {@link ReportingGostKlausurplanungKursklausur}.</p>
 */
public class ProxyReportingGostKlausurplanungKursklausur extends ReportingGostKlausurplanungKursklausur {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKlausurplanungKursklausur}.
	 * @param gostKursklausur		Die GostKursklausur mit den Daten zur Klausur eines Kurses.
	 * @param gostKlausurVorgabe	Die Vorlage für diese Klausur mit ihren Werten.
	 * @param klausurtermin			Der Klausurtermin, der dieser Kursklausur zugewiesen wurde.
	 * @param kurs 					Der Kurs zur Kursklausur.
	 */
	public ProxyReportingGostKlausurplanungKursklausur(final GostKursklausur gostKursklausur, final GostKlausurvorgabe gostKlausurVorgabe,
			final ReportingGostKlausurplanungKlausurtermin klausurtermin, final ReportingKurs kurs) {
		super(gostKlausurVorgabe.auswahlzeit,
				gostKursklausur.bemerkung,
				gostKlausurVorgabe.dauer,
				gostKursklausur.id,
				gostKlausurVorgabe.istAudioNotwendig,
				gostKlausurVorgabe.istMdlPruefung,
				gostKlausurVorgabe.istVideoNotwendig,
				klausurtermin,
				kurs,
				new ArrayList<>(),
				gostKursklausur.startzeit);

		// Die fertige Kursklausur dem Klausurtermin zuweisen, wenn diese noch nicht dort in der Liste enthalten ist.
		if ((super.klausurtermin != null) && super.klausurtermin.kursklausuren().stream().noneMatch(s -> s.id() == super.id)) {
			super.klausurtermin.kursklausuren().add(this);
		}
	}
}
