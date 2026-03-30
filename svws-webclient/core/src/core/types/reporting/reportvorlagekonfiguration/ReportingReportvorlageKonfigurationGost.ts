import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingReportvorlageParameterTyp } from '../../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ReportingReportvorlageUtils } from '../../../../core/utils/reporting/ReportingReportvorlageUtils';
import type { List } from '../../../../java/util/List';
import { ReportingUIKomponentenTyp } from '../../../../core/types/reporting/ReportingUIKomponentenTyp';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { ReportingAusgabeformat } from '../../../../core/types/reporting/ReportingAusgabeformat';
import { ReportingParameter } from '../../../../core/data/reporting/ReportingParameter';

export class ReportingReportvorlageKonfigurationGost extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 *Erstellt die Reportparamater für die Vorlage GostKlausurplanungVKlausurtermineMitKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKlausurplanungVKlausurtermineMitKursen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ArrayList(), new ArrayList(), false, false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKlausurplanungVSchuelerMitKlausuren.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKlausurplanungVSchuelerMitKlausuren(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKursMitKursschuelern.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVKursMitKursschuelern(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKurseMitStatistikwerten.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVKurseMitStatistikwerten(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVSchuelerMitKursen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitSchienenKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVSchuelerMitSchienenKursen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ArrayList(), new ArrayList(), false, false, true);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationGost';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationGost'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageKonfigurationGost>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationGost');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageKonfigurationGost(obj: unknown): ReportingReportvorlageKonfigurationGost {
	return obj as ReportingReportvorlageKonfigurationGost;
}
