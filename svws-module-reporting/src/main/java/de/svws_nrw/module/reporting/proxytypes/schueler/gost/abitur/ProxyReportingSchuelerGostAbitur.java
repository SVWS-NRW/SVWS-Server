package de.svws_nrw.module.reporting.proxytypes.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.proxytypes.gost.abitur.ProxyReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;

import java.util.ArrayList;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostAbitur und erweitert die Klasse {@link ReportingSchuelerGostAbitur}.
 */
public class ProxyReportingSchuelerGostAbitur extends ReportingSchuelerGostAbitur {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerGostAbitur}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param abiturdaten Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingSchuelerGostAbitur(final ReportingRepository reportingRepository, final Abiturdaten abiturdaten) {
		super(abiturdaten.abiturjahr,
				abiturdaten.besondereLernleistung,
				null,
				abiturdaten.besondereLernleistungThema,
				abiturdaten.bewertetesHalbjahr,
				abiturdaten.bilingualeSprache,
				abiturdaten.block1AnzahlKurse,
				abiturdaten.block1DefiziteGesamt,
				abiturdaten.block1DefiziteLK,
				abiturdaten.block1FehlstundenGesamt,
				abiturdaten.block1FehlstundenUnentschuldigt,
				abiturdaten.block1NotenpunkteDurchschnitt,
				abiturdaten.block1PunktSummeGK,
				abiturdaten.block1PunktSummeLK,
				abiturdaten.block1PunktSummeNormiert,
				abiturdaten.block1Zulassung,
				abiturdaten.block2DefiziteGesamt,
				abiturdaten.block2DefiziteLK,
				abiturdaten.block2PunktSumme,
				new ArrayList<>(),
				abiturdaten.freiwilligerRuecktritt,
				abiturdaten.gesamtPunkte,
				abiturdaten.gesamtPunkteVerbesserung,
				abiturdaten.gesamtPunkteVerschlechterung,
				abiturdaten.note,
				abiturdaten.projektKursThema,
				abiturdaten.pruefungBestanden,
				abiturdaten.schuljahrAbitur);
		this.reportingRepository = reportingRepository;

		super.besondereLernleistungNote = Note.fromKuerzel(abiturdaten.besondereLernleistungNotenKuerzel);

		super.fachbelegungen()
				.addAll(abiturdaten.fachbelegungen.stream()
						.map(f -> new ProxyReportingGostAbiturFachbelegung(this.reportingRepository, abiturdaten.schuljahrAbitur, f)).toList());

		this.fachbelegungen().sort(ReportingGostAbiturFachbelegung::compareToGost);

		ersetzeStringNullDurchEmpty(this, false);
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
