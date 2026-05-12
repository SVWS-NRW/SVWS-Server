package de.svws_nrw.module.reporting.types.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 *  Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegungHalbjahr und erweitert die Klasse {@link ReportingGostAbiturFachbelegungHalbjahr}.
 */
public class ProxyReportingGostAbiturFachbelegungHalbjahr extends ReportingGostAbiturFachbelegungHalbjahr {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostAbiturFachbelegungHalbjahr}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param abiturFachbelegungHalbjahr Daten-Objekt der Halbjahresfachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegungHalbjahr(final ReportingContext reportingContext,
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
		this.reportingContext = reportingContext;

		super.note = Note.fromKuerzel(abiturFachbelegungHalbjahr.notenkuerzel);

		if (abiturFachbelegungHalbjahr.lehrer != null) {
			super.lehrer = this.reportingContext.repositoryLehrer().lehrer(abiturFachbelegungHalbjahr.lehrer);
		}
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}

}
