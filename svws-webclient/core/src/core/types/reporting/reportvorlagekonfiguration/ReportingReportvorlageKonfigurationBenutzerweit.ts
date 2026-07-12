import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingReportvorlageParameterTyp } from '../../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ReportingReportvorlageParameterGruppe } from '../../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import type { List } from '../../../../java/util/List';
import { ReportingReportvorlageUtils } from '../../../../core/utils/reporting/ReportingReportvorlageUtils';
import { ReportingUIKomponentenTyp } from '../../../../core/types/reporting/ReportingUIKomponentenTyp';
import { Class } from '../../../../java/lang/Class';

export class ReportingReportvorlageKonfigurationBenutzerweit extends JavaObject {

	/**
	 * Der Name der Client-Anwendung, unter dem die benutzerweiten Reporting-Einstellungen in der Client-Konfiguration gespeichert werden.
	 */
	public static readonly CONFIG_APP_NAME: string = "SVWS-Client";

	/**
	 * Der Schlüssel, unter dem die benutzerweiten Reporting-Einstellungen in der Client-Konfiguration des Benutzers gespeichert werden.
	 */
	public static readonly CONFIG_KEY_BENUTZER_VORLAGEN: string = "reporting.einstellungen.benutzer.vorlagen";

	/**
	 * Der Name des benutzerweiten Parameters für die Anzeige des Schulkürzels bei externen Schülern.
	 */
	public static readonly PARAMETER_MIT_EXTERNER_SCHULE_KUERZEL: string = "mitExternerSchuleKuerzel";

	/**
	 * Der Name des benutzerweiten Parameters für das Anhängen von Datum und Uhrzeit an die Dateinamen der erzeugten Dateien.
	 */
	public static readonly PARAMETER_DATEINAME_MIT_ZEITSTEMPEL: string = "dateinameMitZeitstempel";


	private constructor() {
		super();
	}

	/**
	 * Erstellt den Katalog der benutzerweiten Parameter mit ihren Parametergruppen, wie er in der Einstellungs-UI angezeigt wird.
	 * Die Werte der Parameter sind die Katalog-Defaults.
	 *
	 * @return Die Liste der Parametergruppen mit den benutzerweiten Parametern und ihren Default-Werten
	 */
	public static getBenutzerweiteParameterGruppen(): List<ReportingReportvorlageParameterGruppe> {
		return ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in den Ausgabedateien aller Reportvorlagen.", true, 1, ArrayList.of(ReportingReportvorlageUtils.erzeugeVorlageParameter(ReportingReportvorlageKonfigurationBenutzerweit.PARAMETER_MIT_EXTERNER_SCHULE_KUERZEL, "mit Schulkürzel bei externen Schülern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1))), ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen", "Die folgenden Optionen beeinflussen die Ausgabe der erzeugten Dateien aller Reportvorlagen.", true, 1, ArrayList.of(ReportingReportvorlageUtils.erzeugeVorlageParameter(ReportingReportvorlageKonfigurationBenutzerweit.PARAMETER_DATEINAME_MIT_ZEITSTEMPEL, "Datum und Uhrzeit an Dateinamen anhängen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1))));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageKonfigurationBenutzerweit>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageKonfigurationBenutzerweit(obj: unknown): ReportingReportvorlageKonfigurationBenutzerweit {
	return obj as ReportingReportvorlageKonfigurationBenutzerweit;
}
