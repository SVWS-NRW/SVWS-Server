import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingEMailEmpfaengerTyp } from '../../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import { ReportingReportvorlageParameterTyp } from '../../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingEMailDaten } from '../../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ReportingReportvorlageUtils } from '../../../../core/utils/reporting/ReportingReportvorlageUtils';
import type { List } from '../../../../java/util/List';
import { ReportingUIKomponentenTyp } from '../../../../core/types/reporting/ReportingUIKomponentenTyp';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { ReportingAusgabeformat } from '../../../../core/types/reporting/ReportingAusgabeformat';
import { ReportingParameter } from '../../../../core/data/reporting/ReportingParameter';

export class ReportingReportvorlageKonfigurationStundenplanung extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVFachStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVFachStundenplan(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, ArrayList.of(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVKlassenStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVKlassenStundenplan(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.KLASSENLEHRER, false, "", ""), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVLehrerStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVLehrerStundenplan(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.LEHRER, false, "", ""), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVLehrerStundenplanKombiniert".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVLehrerStundenplanKombiniert(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVRaumStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVRaumStundenplan(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, ArrayList.of(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVSchuelerStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getStundenplanungVSchuelerStundenplan(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.SCHUELER, false, "", ""), new ArrayList(), new ArrayList(), true, true);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationStundenplanung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationStundenplanung'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageKonfigurationStundenplanung>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationStundenplanung');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageKonfigurationStundenplanung(obj: unknown): ReportingReportvorlageKonfigurationStundenplanung {
	return obj as ReportingReportvorlageKonfigurationStundenplanung;
}
