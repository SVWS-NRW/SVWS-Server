package de.svws_nrw.module.reporting.proxytypes.gost.kursplanung;

import java.util.List;

import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungSchiene;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungSchiene und erweitert die Klasse {@link ReportingGostKursplanungSchiene}.
 */
public class ProxyReportingGostKursplanungSchiene extends ReportingGostKursplanungSchiene {

	/** Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört. */
	private final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostKursplanungSchiene}.
	 * @param reportingGostKursplanungBlockungsergebnis Das Blockungsergebnis zur Kursplanung der gymnasialen Oberstufe, zu dem dieses Objekt gehört.
	 * @param anzahlDummy				Anzahl der Dummy-Schüler in der Schiene
	 * @param anzahlExterne				Anzahl der externen Schüler in der Schiene
	 * @param anzahlSchueler			Anzahl der Schüler in der Schien
	 * @param bezeichnung				Bezeichnung der Schiene
	 * @param hatKollisionen			Gibt an, ob in der Schiene Schüler mit Kurskollisionen vorhanden sind.
	 * @param id						ID der Schiene
	 * @param idsKurseMitKollisionen 	Eine Liste mit IDs der Kurse in der Schiene, die eine Kollision enthalten.
	 * @param idsSchuelerMitKollisionen	Eine Liste mit IDs der Schüler in der Schiene, die eine Kollision enthalten.
	 * @param kurse						Eine Liste vom Typ Kurse, die alle Kurse der Schiene und deren Daten enthält.
	 * @param nummer					Die Nummer der Schiene.
	 */
	public ProxyReportingGostKursplanungSchiene(final ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis,
			final int anzahlDummy, final int anzahlExterne, final int anzahlSchueler, final String bezeichnung, final boolean hatKollisionen, final Long id,
			final List<Long> idsKurseMitKollisionen, final List<Long> idsSchuelerMitKollisionen, final List<ReportingGostKursplanungKurs> kurse,
			final int nummer) {
		super(anzahlDummy, anzahlExterne, anzahlSchueler, bezeichnung, hatKollisionen, id, idsKurseMitKollisionen, idsSchuelerMitKollisionen, kurse, nummer);
		this.reportingGostKursplanungBlockungsergebnis = reportingGostKursplanungBlockungsergebnis;
	}


	/**
	 * Liste vom Typ Kurs, die die Kurse beinhaltet, die in dieser Schiene gemäß Blockungsergebnis liegt.
	 * @return Liste mit Schienen des Kurses
	 */
	@Override
	public List<ReportingGostKursplanungKurs> kurse() {
		if ((super.kurse() == null) || super.kurse().isEmpty()) {
			super.kurse().addAll(reportingGostKursplanungBlockungsergebnis.kurse()
					.stream()
					.filter(k -> k.schienen().stream().anyMatch(s -> s.id() == this.id()))
					.toList());
		}
		return super.kurse();
	}
}
