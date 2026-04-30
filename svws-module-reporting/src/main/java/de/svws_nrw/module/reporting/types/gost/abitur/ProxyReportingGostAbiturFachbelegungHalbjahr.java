package de.svws_nrw.module.reporting.types.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;

/**
 *  Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegungHalbjahr und erweitert die Klasse {@link ReportingGostAbiturFachbelegungHalbjahr}.
 */
public class ProxyReportingGostAbiturFachbelegungHalbjahr extends ReportingGostAbiturFachbelegungHalbjahr {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostAbiturFachbelegungHalbjahr}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param abiturFachbelegungHalbjahr Daten-Objekt der Halbjahresfachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegungHalbjahr(final ReportingRepository reportingRepository,
			final AbiturFachbelegungHalbjahr abiturFachbelegungHalbjahr) {
		super(ersetzeNullBlankTrim(abiturFachbelegungHalbjahr.biliSprache),
				abiturFachbelegungHalbjahr.block1gewertet,
				abiturFachbelegungHalbjahr.block1kursAufZeugnis,
				abiturFachbelegungHalbjahr.fehlstundenGesamt,
				abiturFachbelegungHalbjahr.fehlstundenUnentschuldigt,
				ersetzeNullBlankTrim(abiturFachbelegungHalbjahr.halbjahrKuerzel),
				abiturFachbelegungHalbjahr.schriftlich,
				ersetzeNullBlankTrim(abiturFachbelegungHalbjahr.kursartKuerzel),
				null,
				null,
				abiturFachbelegungHalbjahr.wochenstunden);
		this.reportingRepository = reportingRepository;

		super.note = Note.fromKuerzel(abiturFachbelegungHalbjahr.notenkuerzel);

		if (abiturFachbelegungHalbjahr.lehrer != null) {
			super.lehrer = this.reportingRepository.repositoryLehrer().lehrer(abiturFachbelegungHalbjahr.lehrer);
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
