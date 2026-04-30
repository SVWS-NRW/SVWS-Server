package de.svws_nrw.module.reporting.types.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegung und erweitert die Klasse {@link ReportingGostAbiturFachbelegung}.</p>
 */
public class ProxyReportingGostAbiturFachbelegung extends ReportingGostAbiturFachbelegung {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostAbiturFachbelegung}.
	 *
	 * @param reportingRepository 	Repository für das Reporting.
	 * @param schuljahrAbitur		Das Schuljahr der Abiturprüfung
	 * @param abiturFachbelegung 	Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegung(final ReportingRepository reportingRepository, final int schuljahrAbitur,
			final AbiturFachbelegung abiturFachbelegung) {
		super(abiturFachbelegung.abiturFach,
				abiturFachbelegung.block1NotenpunkteDurchschnitt,
				abiturFachbelegung.block1PunktSumme,
				abiturFachbelegung.block2MuendlichePruefungAbweichung,
				abiturFachbelegung.block2MuendlichePruefungBestehen,
				abiturFachbelegung.block2MuendlichePruefungFreiwillig,
				null,
				abiturFachbelegung.block2MuendlichePruefungReihenfolge,
				null,
				null,
				abiturFachbelegung.block2Punkte,
				abiturFachbelegung.block2PunkteZwischenstand,
				null,
				null,
				ersetzeNullBlankTrim(abiturFachbelegung.letzteKursart));
		this.reportingRepository = reportingRepository;

		super.block2PruefungNote = Note.fromKuerzel(abiturFachbelegung.block2NotenKuerzelPruefung);
		super.block2MuendlichePruefungNote = Note.fromKuerzel(abiturFachbelegung.block2MuendlichePruefungNotenKuerzel);

		if (abiturFachbelegung.block2Pruefer != null) {
			super.block2Pruefer = this.reportingRepository.repositoryLehrer().lehrer(abiturFachbelegung.block2Pruefer);
		}

		final ReportingGostAbiturFachbelegungHalbjahr[] belegungenHJ = new ReportingGostAbiturFachbelegungHalbjahr[6];
		for (int i = 0; i < 6; i++) {
			if (abiturFachbelegung.belegungen[i] != null) {
				belegungenHJ[i] = new ProxyReportingGostAbiturFachbelegungHalbjahr(this.reportingRepository, abiturFachbelegung.belegungen[i]);
			} else {
				belegungenHJ[i] = null;
			}
		}
		super.halbjahresbelegungen = belegungenHJ;

		// Für die Daten des Faches wird mindestens der Abschnitt Q11 benötigt. Wenn dieser nicht existiert, dann kann die Belegung nicht existieren.
		// Da in der Q-Phase konstante Fachbedingungen gelten müssen, kann hier die Q11 verwendet werden.
		final ReportingSchuljahresabschnitt abschnittQ11 = this.reportingRepository.repositorySchule().schuljahresabschnitt(schuljahrAbitur - 1, 1);
		if (abschnittQ11 != null) {
			super.fach = abschnittQ11.fach(abiturFachbelegung.fachID);
		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}
}
