package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import java.util.List;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert den Katalog der benutzerweiten Report-Parameter, d. h. der Parameter, die ein Benutzer einmalig in seinen
 * Nutzereinstellungen festlegt und die für alle Reportvorlagen gelten. Benutzerweite Parameter und Vorlagen-Parameter sind disjunkte
 * Mengen: Ein katalog-geführter Parameter darf in keiner Vorlagen-Konfiguration vorkommen.
 *
 * <p>Der Katalog ist die Single Source of Truth für die Einstellungs-UI im Client, die serverseitige Kombination der Parameter in der
 * ReportingFactory und die zugehörigen Tests.</p>
 */
public final class ReportingReportvorlageKonfigurationBenutzerweit {

	/** Der Name der Client-Anwendung, unter dem die benutzerweiten Reporting-Einstellungen in der Client-Konfiguration gespeichert werden. */
	public static final @NotNull String CONFIG_APP_NAME = "SVWS-Client";

	/** Der Schlüssel, unter dem die benutzerweiten Reporting-Einstellungen in der Client-Konfiguration des Benutzers gespeichert werden. */
	public static final @NotNull String CONFIG_KEY_BENUTZER_VORLAGEN = "reporting.einstellungen.benutzer.vorlagen";

	/** Der Name des benutzerweiten Parameters für die Anzeige des Schulkürzels bei externen Schülern. */
	public static final @NotNull String PARAMETER_MIT_EXTERNER_SCHULE_KUERZEL = "mitExternerSchuleKuerzel";

	/** Der Name des benutzerweiten Parameters für das Anhängen von Datum und Uhrzeit an die Dateinamen der erzeugten Dateien. */
	public static final @NotNull String PARAMETER_DATEINAME_MIT_ZEITSTEMPEL = "dateinameMitZeitstempel";


	private ReportingReportvorlageKonfigurationBenutzerweit() {
	}


	/**
	 * Erstellt den Katalog der benutzerweiten Parameter mit ihren Parametergruppen, wie er in der Einstellungs-UI angezeigt wird.
	 * Die Werte der Parameter sind die Katalog-Defaults.
	 *
	 * @return Die Liste der Parametergruppen mit den benutzerweiten Parametern und ihren Default-Werten
	 */
	public static @NotNull List<ReportingReportvorlageParameterGruppe> getBenutzerweiteParameterGruppen() {
		return List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in den Ausgabedateien aller Reportvorlagen.",
						true, 1, List.of(
								ReportingReportvorlageUtils.erzeugeVorlageParameter(PARAMETER_MIT_EXTERNER_SCHULE_KUERZEL,
										"mit Schulkürzel bei externen Schülern",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1))),
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen",
						"Die folgenden Optionen beeinflussen die Ausgabe der erzeugten Dateien aller Reportvorlagen.",
						true, 1, List.of(
								ReportingReportvorlageUtils.erzeugeVorlageParameter(PARAMETER_DATEINAME_MIT_ZEITSTEMPEL,
										"Datum und Uhrzeit an Dateinamen anhängen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1))));
	}
}
