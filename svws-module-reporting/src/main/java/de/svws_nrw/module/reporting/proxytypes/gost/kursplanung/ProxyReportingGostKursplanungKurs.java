package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungSchiene;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.List;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs und erweitert die Klasse {@link ReportingGostKursplanungKurs}.
 */
public class ProxyReportingGostKursplanungKurs extends ReportingGostKursplanungKurs {

	/** Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört. */
	private final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis;



	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungKurs}.
	 *
	 * @param reportingGostKursplanungBlockungsergebnis Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört.
	 * @param anzahlAB12				Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 * @param anzahlAB3					Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 * @param anzahlAB4					Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 * @param anzahlDummy 				Anzahl der Dummy-Schüler.
	 * @param anzahlExterne				Anzahl der Schülerinnen und Schüler mit Status extern.
	 * @param anzahlSchueler			Anzahl der Schülerinnen und Schüler im Kurs.
	 * @param anzahlSchuelerSchriftlich	Anzahl der Schüler des Kurses, die das Fach schriftlich belegt haben.
	 * @param bezeichnung				Bezeichnung des Kurses.
	 * @param fach 						Das Fach des Kurses.
	 * @param fachwahlstatistik			Die Fachwahl-Statistik zum Fach und zum GOSt-Halbjahr des Kurses
	 * @param gostHalbjahr				Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 * @param gostKursart				Kursart des Kurses.
	 * @param id						ID des Kurses.
	 * @param lehrkraefte				Liste der Lehrkräfte des Kurses.
	 * @param schienen                  Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 * @param schueler					Liste der Schüler des Kurses.
	 */
	public ProxyReportingGostKursplanungKurs(final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis, final int anzahlAB12,
			final int anzahlAB3, final int anzahlAB4, final int anzahlDummy, final int anzahlExterne, final int anzahlSchueler,
			final int anzahlSchuelerSchriftlich, final String bezeichnung, final ReportingFach fach,
			final ReportingGostKursplanungFachwahlstatistik fachwahlstatistik, final GostHalbjahr gostHalbjahr,
			final GostKursart gostKursart, final long id, final List<ReportingLehrer> lehrkraefte, final List<ReportingGostKursplanungSchiene> schienen,
			final List<ReportingSchueler> schueler) {
		super(anzahlAB12,
				anzahlAB3,
				anzahlAB4,
				anzahlDummy,
				anzahlExterne,
				anzahlSchueler,
				anzahlSchuelerSchriftlich,
				ersetzeNullBlankTrim(bezeichnung),
				fach,
				fachwahlstatistik,
				gostHalbjahr,
				gostKursart,
				id,
				lehrkraefte,
				schienen,
				schueler);

		this.reportingGostKursplanungBlockungsergebnis = reportingGostKursplanungBlockungsergebnis;
	}


	/**
	 * Die Fachwahl-Statistik zum Fach und zum GOSt-Halbjahr des Kurses
	 *
	 * @return Fachwahl-Statistik des Faches des Kurses
	 */
	@Override
	public ReportingGostKursplanungFachwahlstatistik fachwahlstatistik() {
		if (super.fachwahlstatistik() == null) {
			super.fachwahlstatistik = this.reportingGostKursplanungBlockungsergebnis.fachwahlstatistik().get(this.fach().id());
		}
		return super.fachwahlstatistik();
	}

	/**
	 * Liste vom Typ Schiene, die die Schienen beinhaltet, in denen der Kurs gemäß Blockungsergebnis liegt.
	 *
	 * @return Liste mit Schienen des Kurses
	 */
	@Override
	public List<ReportingGostKursplanungSchiene> schienen() {
		if ((super.schienen() == null) || super.schienen().isEmpty()) {
			super.schienen().addAll(reportingGostKursplanungBlockungsergebnis.schienen()
					.stream()
					.filter(s -> s.kurse().stream().anyMatch(k -> k.id() == this.id()))
					.toList());
		}
		return super.schienen();
	}

	/**
	 * Liste vom Typ Schüler, die die Schüler beinhaltet, die den Kurs gemäß Blockungsergebnis belegen.
	 *
	 * @return Liste mit Schülern des Kurses
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if ((super.schueler() == null) || super.schueler().isEmpty()) {
			super.schueler().addAll(reportingGostKursplanungBlockungsergebnis.schueler()
					.stream()
					.filter(s -> s.gostKursplanungKursbelegungen()
							.stream()
							.anyMatch(k -> k.kurs().id() == this.id()))
					.toList());
		}
		return super.schueler();
	}
}
