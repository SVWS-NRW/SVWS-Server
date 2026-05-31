import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingFilterDefinitionFactory } from '../../../../core/utils/reporting/ReportingFilterDefinitionFactory';
import { ReportingFilterVerknuepfung } from '../../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingReportvorlageParameterTyp } from '../../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingEMailDaten } from '../../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ReportingReportvorlageUtils } from '../../../../core/utils/reporting/ReportingReportvorlageUtils';
import { ReportingUIKomponentenTyp } from '../../../../core/types/reporting/ReportingUIKomponentenTyp';
import { ReportingParameter } from '../../../../core/data/reporting/ReportingParameter';
import { ReportingEMailEmpfaengerTyp } from '../../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import { ReportingSortierungDefinitionFactory } from '../../../../core/utils/reporting/ReportingSortierungDefinitionFactory';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { ReportingAusgabeformat } from '../../../../core/types/reporting/ReportingAusgabeformat';

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
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), ArrayList.of(ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe("Quartalsfilter", "ReportingGostKlausurplanungKlausurtermin", true, true, ReportingFilterVerknuepfung.OR, ReportingFilterDefinitionFactory.definitionen(ReportingFilterDefinitionFactory.definition("1. Quartal", "ReportingGostKlausurplanungKlausurtermin", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("quartal", "1"))), ReportingFilterDefinitionFactory.definition("2. Quartal", "ReportingGostKlausurplanungKlausurtermin", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("quartal", "2")))))), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKlausurplanungVSchuelerMitKlausuren.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKlausurplanungVSchuelerMitKlausuren(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), ArrayList.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Schülersortierung", "ReportingSchueler", true, ReportingSortierungDefinitionFactory.definitionen(ReportingSortierungDefinitionFactory.standard("Sortierung nach Name und Vorname (Standard)", "ReportingSchueler"), ReportingSortierungDefinitionFactory.definition("Sortierung nach Klasse, Name, Vorname", "ReportingSchueler", false, ArrayList.of("auswahlLernabschnitt.klasse.sortierungEintrag", "auswahlLernabschnitt.klasse.kuerzel", "nachname", "vorname", "vornamen"))))), ArrayList.of(ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe("Quartalsfilter", "ReportingGostKlausurplanungSchuelerklausur", true, true, ReportingFilterVerknuepfung.OR, ReportingFilterDefinitionFactory.definitionen(ReportingFilterDefinitionFactory.definition("1. Quartal", "ReportingGostKlausurplanungSchuelerklausur", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("klausurtermin.quartal", "1"))), ReportingFilterDefinitionFactory.definition("2. Quartal", "ReportingGostKlausurplanungSchuelerklausur", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("klausurtermin.quartal", "2")))))), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKursMitKursschuelern.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVKursMitKursschuelern(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), new ArrayList(), ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.GOSTKURSPLANUNG_KURSLEHRER, false, "Kurslisten zur Kursplanung", "Im Anhang dieser automatisch generierten E-Mail befinden sich Kurslisten aus der Kursplanung."), new ArrayList(), new ArrayList(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKurseMitStatistikwerten.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVKurseMitStatistikwerten(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVSchuelerMitKursen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitSchienenKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostKursplanungVSchuelerMitSchienenKursen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), false, true);
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
