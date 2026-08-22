package de.svws_nrw.module.reporting.types.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.types.gost.abitur.ProxyReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;

import java.util.ArrayList;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerGostAbitur und erweitert die Klasse {@link ReportingSchuelerGostAbitur}.
 */
public class ProxyReportingSchuelerGostAbitur extends ReportingSchuelerGostAbitur {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerGostAbitur}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param abiturdaten Daten-Objekt der Fachbelegungen aus der Datenbank
	 */
	public ProxyReportingSchuelerGostAbitur(final ReportingContext reportingContext, final Abiturdaten abiturdaten) {
		super(abiturdaten.abiturjahr,
				abiturdaten.schuljahrAbitur,
				null,
				ersetzeNullBlankTrim(abiturdaten.besondereLernleistung),
				null,
				ersetzeNullBlankTrim(abiturdaten.besondereLernleistungThema),
				abiturdaten.bewertetesHalbjahr,
				ersetzeNullBlankTrim(abiturdaten.bilingualeSprache),
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
				ersetzeNullBlankTrim(abiturdaten.note),
				ersetzeNullBlankTrim(abiturdaten.projektKursThema),
				abiturdaten.pruefungBestanden);
		this.reportingContext = reportingContext;

		// Der Abschnitt 2 des Abiturschuljahres kann an der Schule fehlen - etwa bei einem Jahrgang, der sein Abitur erst ablegt. Er bleibt dann null: Ein
		// virtueller Abschnitt hätte hier keine Funktion, denn die Vorlagen nutzen den Abschnitt allein für die Lernabschnitts-Auflösung des Schülers, und
		// die geht mit null um.
		super.abiturSchuljahresabschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.abiturSchuljahr, 2);
		super.besondereLernleistungNote = Note.fromKuerzel(abiturdaten.besondereLernleistungNotenKuerzel);

		super.fachbelegungen()
				.addAll(abiturdaten.fachbelegungen.stream()
						.map(f -> new ProxyReportingGostAbiturFachbelegung(this.reportingContext, abiturdaten.schuljahrAbitur, f)).toList());

		this.fachbelegungen().sort(ReportingGostAbiturFachbelegung.SORTIERUNG.comparatorStandard());
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
