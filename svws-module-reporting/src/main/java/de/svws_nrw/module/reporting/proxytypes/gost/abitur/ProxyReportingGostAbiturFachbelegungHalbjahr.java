package de.svws_nrw.module.reporting.proxytypes.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegungHalbjahr;

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
		super(ersetzeNullDurchEmpty(abiturFachbelegungHalbjahr.biliSprache),
				abiturFachbelegungHalbjahr.block1gewertet,
				abiturFachbelegungHalbjahr.block1kursAufZeugnis,
				abiturFachbelegungHalbjahr.fehlstundenGesamt,
				abiturFachbelegungHalbjahr.fehlstundenUnentschuldigt,
				ersetzeNullDurchEmpty(abiturFachbelegungHalbjahr.halbjahrKuerzel),
				abiturFachbelegungHalbjahr.schriftlich,
				ersetzeNullDurchEmpty(abiturFachbelegungHalbjahr.kursartKuerzel),
				null,
				null,
				abiturFachbelegungHalbjahr.wochenstunden);
		this.reportingRepository = reportingRepository;

		super.note = Note.fromKuerzel(abiturFachbelegungHalbjahr.notenkuerzel);

		if (abiturFachbelegungHalbjahr.lehrer != null) {
			super.lehrer = new ProxyReportingLehrer(
					this.reportingRepository,
					this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(abiturFachbelegungHalbjahr.lehrer, l -> {
						try {
							return new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(abiturFachbelegungHalbjahr.lehrer);
						} catch (final ApiOperationException e) {
							ReportingExceptionUtils.putStacktraceInLog(
									"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
									reportingRepository.logger(), LogLevel.INFO, 0);
							return new LehrerStammdaten();
						}
					}));
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
