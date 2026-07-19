import { JavaObject } from '../../../../java/lang/JavaObject';
import { ReportingReportvorlageParameterTyp } from '../../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingEMailDaten } from '../../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../../java/util/ArrayList';
import { ReportingReportvorlageUtils } from '../../../../core/utils/reporting/ReportingReportvorlageUtils';
import { ReportingUIKomponentenTyp } from '../../../../core/types/reporting/ReportingUIKomponentenTyp';
import { ReportingParameter } from '../../../../core/data/reporting/ReportingParameter';
import { ReportingEMailEmpfaengerTyp } from '../../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import { BenutzerKompetenz } from '../../../../core/types/benutzer/BenutzerKompetenz';
import { ReportingSortierungDefinitionFactory } from '../../../../core/utils/reporting/ReportingSortierungDefinitionFactory';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { ReportingAusgabeformat } from '../../../../core/types/reporting/ReportingAusgabeformat';
import { ServerMode } from '../../../../core/types/ServerMode';

export class ReportingReportvorlageKonfigurationSchueler extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostAbiturApoAnlage12A4".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVGostAbiturApoAnlage12A4(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften gemäß Datenbestand", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrkraft statt Schulträger", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostAbiturApoAnlage12A3".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVGostAbiturApoAnlage12A3(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPersoenlichenUnterschriften", "mit persönlichen Unterschriften gemäß Datenbestand", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift", "mit Unterschrift 2. Beratungslehrkraft statt Schulträger", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostLaufbahnplanungErgebnisuebersicht".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVGostLaufbahnplanungErgebnisuebersicht(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostLaufbahnplanungWahlbogen".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVGostLaufbahnplanungWahlbogen(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1, ArrayList.of(ReportingReportvorlageUtils.erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.SCHUELER, false, "Persönlicher Wahlbogen zur Laufbahnplanung in der GOSt", "Im Anhang dieser automatisch generierten E-Mail befindet sich dein persönlicher Wahlbogen zur Laufbahnplanung in der gymnasialen Oberstufe."), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVSchulbescheinigung".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVSchulbescheinigung(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("keinUnterzeichner", "ohne Angabe zum Unterzeichner", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitDigitaleSignatur", "mit digitaler Signatur", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1, ServerMode.DEV, ArrayList.of(BenutzerKompetenz.EXTRAS_DIGITALE_SIGNATUREN_AUSSTELLEN)), ReportingReportvorlageUtils.erzeugeVorlageParameter("qrCodeBreite", "QR-Code-Breite (mm)", ReportingReportvorlageParameterTyp.INTEGER, "" + 40, false, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("qrCodeHoehe", "QR-Code-Höhe (mm)", ReportingReportvorlageParameterTyp.INTEGER, "" + 40, false, ReportingUIKomponentenTyp.INPUT, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("versandinformationen", "Versandinformationen", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 3)))), new ReportingEMailDaten(), new ArrayList(), new ArrayList(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVListeKontaktdatenerzieher".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static getSchuelerVListeKontaktdatenerzieher(): ReportingParameter {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(ArrayList.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), ArrayList.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen", "Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("ueberschrift", "Überschrift (Standard: Schülerliste)", ReportingReportvorlageParameterTyp.STRING, "Schülerliste", true, ReportingUIKomponentenTyp.INPUT, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("bemerkung", "Bemerkung", ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoto", "mit Foto", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAlleVornamen", "mit allen Vornamen", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)))), new ReportingEMailDaten(), ArrayList.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Schülersortierung", "ReportingSchueler", true, ReportingSortierungDefinitionFactory.definitionen(ReportingSortierungDefinitionFactory.standard("Sortierung nach Name und Vorname (Standard)", "ReportingSchueler"), ReportingSortierungDefinitionFactory.definition("Sortierung nach Klasse, Name, Vorname", "ReportingSchueler", false, ArrayList.of("auswahlLernabschnitt.klasse.sortierungEintrag", "auswahlLernabschnitt.klasse.kuerzel", "nachname", "vorname", "vornamen"))))), new ArrayList(), false, true);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationSchueler'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageKonfigurationSchueler>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationSchueler');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageKonfigurationSchueler(obj: unknown): ReportingReportvorlageKonfigurationSchueler {
	return obj as ReportingReportvorlageKonfigurationSchueler;
}
