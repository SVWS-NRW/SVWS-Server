import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingSortierungDefinitionFactory } from '../../../../core/utils/reporting/ReportingSortierungDefinitionFactory';
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

export class ReportingReportvorlageKonfigurationLehrer extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVListeKontaktdaten".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getLehrerVListeKontaktdaten(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("ueberschrift", "Überschrift (Standard: Lehrerliste)", ReportingReportvorlageParameterTyp.STRING, "Lehrerliste", true, ReportingUIKomponentenTyp.INPUT, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("bemerkung", "Bemerkung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerAmtsbezeichnung", "mit Amtsbezeichnung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoto", "mit Foto", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerTelefon", "mit Telefon", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerTelefonMobil", "mit Mobiltelefon", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerEmailDienstlich", "mit E-Mail (dienstlich)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteLehrerLehraemter", "mit Lehrämtern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, false, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteLehrerBeschaeftigung", "mit Beschäftigung", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, false, ReportingUIKomponentenTyp.CHECKBOX, 3)))), new ReportingEMailDaten(), ArrayList.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Lehrersortierung", "ReportingLehrer", true, ReportingSortierungDefinitionFactory.definitionen(ReportingSortierungDefinitionFactory.standard("Sortierung nach Name und Vorname (Standard)", "ReportingLehrer"), ReportingSortierungDefinitionFactory.definition("Sortierung nach Kürzel", "ReportingLehrer", false, ArrayList.of("kuerzel", "nachname", "vorname"))))), ArrayList.of(ReportingReportvorlageUtils.erzeugeLehrerPersonaltypfilterGruppe()), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVListeSchuelerLeistungsdaten".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getLehrerVListeSchuelerLeistungsdaten(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVStammdatenliste".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getLehrerVStammdatenliste(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), new ArrayList(), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationLehrer'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageKonfigurationLehrer>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationLehrer');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageKonfigurationLehrer(obj: unknown): ReportingReportvorlageKonfigurationLehrer {
	return obj as ReportingReportvorlageKonfigurationLehrer;
}
