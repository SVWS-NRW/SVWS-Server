package de.svws_nrw.module.reporting.types.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturFachbelegung und erweitert die Klasse {@link ReportingGostAbiturFachbelegung}.</p>
 */
public class ProxyReportingGostAbiturFachbelegung extends ReportingGostAbiturFachbelegung {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingGostAbiturFachbelegung}.
	 *
	 * @param reportingContext 	Repository für das Reporting.
	 * @param schuljahrAbitur		Das Schuljahr der Abiturprüfung
	 * @param abiturFachbelegung 	Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingGostAbiturFachbelegung(final ReportingContext reportingContext, final int schuljahrAbitur,
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
		this.reportingContext = reportingContext;

		super.block2PruefungNote = Note.fromKuerzel(abiturFachbelegung.block2NotenKuerzelPruefung);
		super.block2MuendlichePruefungNote = Note.fromKuerzel(abiturFachbelegung.block2MuendlichePruefungNotenKuerzel);

		if (abiturFachbelegung.block2Pruefer != null) {
			super.block2Pruefer = this.reportingContext.repositoryLehrer().lehrer(abiturFachbelegung.block2Pruefer);
		}

		final ReportingGostAbiturFachbelegungHalbjahr[] belegungenHJ = new ReportingGostAbiturFachbelegungHalbjahr[6];
		for (int i = 0; i < 6; i++) {
			if (abiturFachbelegung.belegungen[i] != null) {
				belegungenHJ[i] = new ProxyReportingGostAbiturFachbelegungHalbjahr(this.reportingContext, abiturFachbelegung.belegungen[i]);
			} else {
				belegungenHJ[i] = null;
			}
		}
		super.halbjahresbelegungen = belegungenHJ;

		// Für die Daten des Faches wird der Abschnitt Q11 benötigt. Da in der Q-Phase konstante Fachbedingungen gelten müssen, kann hier die Q11
		// verwendet werden. Hat die Schule diesen Abschnitt nicht angelegt, so wird ein virtueller Abschnitt verwendet, der die Fächer des Schuljahres
		// unverändert liefert; das Fach bliebe sonst ohne Not leer.
		final ReportingSchuljahresabschnitt abschnittQ11 = this.reportingContext.repositorySchule().schuljahresabschnittOderVirtuell(schuljahrAbitur - 1, 1);
		super.fach = abschnittQ11.fach(abiturFachbelegung.fachID);
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
