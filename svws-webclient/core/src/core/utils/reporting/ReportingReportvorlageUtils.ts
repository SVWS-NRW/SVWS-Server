import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import { ReportingSortierungDefinitionGruppe } from '../../../core/data/reporting/ReportingSortierungDefinitionGruppe';
import { ReportingEMailDaten } from '../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { ReportingFilterKriterium } from '../../../core/data/reporting/ReportingFilterKriterium';
import { SchuelerStatus } from '../../../asd/types/schueler/SchuelerStatus';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';
import { ReportingSortierungDefinition } from '../../../core/data/reporting/ReportingSortierungDefinition';
import { ReportingEMailEmpfaengerTyp } from '../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { ReportingFilterDefinitionGruppe } from '../../../core/data/reporting/ReportingFilterDefinitionGruppe';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { ReportingAusgabeformat } from '../../../core/types/reporting/ReportingAusgabeformat';
import { ServerMode, cast_de_svws_nrw_core_types_ServerMode } from '../../../core/types/ServerMode';
import { ReportingFilterDefinitionFactory } from '../../../core/utils/reporting/ReportingFilterDefinitionFactory';
import { ReportingFilterVerknuepfung, cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ReportingReportvorlageParameterTyp, cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageParameterTyp } from '../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingUIKomponentenTyp, cast_de_svws_nrw_core_types_reporting_ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
import { ReportingFilterEintrag } from '../../../core/data/reporting/ReportingFilterEintrag';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';

export class ReportingReportvorlageUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Wandelt einen {@link ServerMode} in den zu speichernden Text-Wert um. Der Modus {@link ServerMode#STABLE} (und null) wird als leerer String abgebildet,
	 * da er semantisch identisch zu "in allen Modi verfügbar" ist.
	 *
	 * @param serverMode der ServerMode
	 *
	 * @return der Text-Wert des ServerMode oder ein leerer String bei STABLE
	 */
	private static serverModeText(serverMode: ServerMode | null): string {
		return ((serverMode === null) || (serverMode as unknown === ServerMode.STABLE as unknown)) ? "" : serverMode.text;
	}

	/**
	 * Wandelt eine Liste von {@link BenutzerKompetenz} in eine Liste ihrer IDs um. Ein null-Wert wird als leere Liste (= keine Kompetenz erforderlich)
	 * interpretiert.
	 *
	 * @param kompetenzen die Liste der Benutzerkompetenzen oder null
	 *
	 * @return die Liste der Kompetenz-IDs (ggf. leer)
	 */
	private static kompetenzIds(kompetenzen: List<BenutzerKompetenz> | null): List<number> {
		const ids: List<number> | null = new ArrayList<number>();
		if (kompetenzen !== null) {
			for (const kompetenz of kompetenzen) {
				if (kompetenz !== null) {
					ids.add(kompetenz.daten.id);
				}
			}
		}
		return ids;
	}

	/**
	 * Erstellt ein ReportingParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param ausgabeformatOptionen                 Liste von Ausgabeformat-Optionen. Wenn null oder leer, wird standardmäßig PDF verwendet.
	 * @param reportvorlageParameterGruppen         Liste von ReportingReportvorlageParameterGruppen. Wenn null, wird eine leere Liste erstellt.
	 * @param eMailDaten                            Die Einstellungen und Informationen zum E-Mail-Versand. Wenn null, werden Standardwerte initialisiert,
	 *                                              die aber keinen E-Mail-Versand ermöglichen.
	 * @param sortierungDefinitionenGruppen         Liste von ReportingSortierungDefinitionGruppen, die die Sortierungsdefinitionen enthalten. Wenn null, wird
	 *                                              eine leere Liste erstellt.
	 * @param filterDefinitionenGruppen             Liste von ReportingFilterDefinitionGruppen, die die Filterdefinitionen enthalten. Wenn null, wird eine
	 *                                              leere Liste erstellt.
	 * @param uiIstSichtbarEinzelausgabeDaten       Gibt an, ob die Option "Einzelausgabe" in der UI sichtbar sein soll.
	 * @param uiIstSichtbarDuplexdruck              Gibt an, ob die Option "Duplexdruck" in der UI sichtbar sein soll.
	 *
	 * @return Ein konfiguriertes ReportingParameter-Objekt mit den angegebenen Eigenschaften.
	 */
	public static erzeugeReportingParameter(ausgabeformatOptionen: List<number> | null, reportvorlageParameterGruppen: List<ReportingReportvorlageParameterGruppe> | null, eMailDaten: ReportingEMailDaten | null, sortierungDefinitionenGruppen: List<ReportingSortierungDefinitionGruppe> | null, filterDefinitionenGruppen: List<ReportingFilterDefinitionGruppe> | null, uiIstSichtbarEinzelausgabeDaten: boolean, uiIstSichtbarDuplexdruck: boolean): ReportingParameter {
		const reportingParameter: ReportingParameter | null = new ReportingParameter();
		reportingParameter.ausgabeformatOptionen = new ArrayList(((ausgabeformatOptionen === null) || ausgabeformatOptionen.isEmpty()) ? ArrayList.of(ReportingAusgabeformat.PDF.getId()) : ausgabeformatOptionen);
		let ausgabeoptionenBeschreibung: string | null = (uiIstSichtbarEinzelausgabeDaten) ? "Die Option 'Daten in einzelne Dateien ausgeben' ermöglicht es, bei der Ausgabe pro Datensatz in eine einzelne Datei zu erzeugen. " : "";
		ausgabeoptionenBeschreibung += (uiIstSichtbarDuplexdruck) ? ("Wird Duplexdruck aktiviert, so kann die erzeuget Datei später auf einem Drucker mit der Option 'beidseitiger Druck (Duplexdruck)' gedruckt werden.") : "";
		const standardausgabeoptionenGruppe: ReportingReportvorlageParameterGruppe | null = ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen", ausgabeoptionenBeschreibung, true, 3, Arrays.asList(ReportingReportvorlageUtils.erzeugeVorlageParameter("einzelausgabeDaten", "Daten in einzelne Dateien ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, uiIstSichtbarEinzelausgabeDaten, ReportingUIKomponentenTyp.CHECKBOX, 1), ReportingReportvorlageUtils.erzeugeVorlageParameter("duplexdruck", "Duplexdruck", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, uiIstSichtbarDuplexdruck, ReportingUIKomponentenTyp.CHECKBOX, 1)));
		reportingParameter.reportvorlageParameterGruppen = new ArrayList((reportvorlageParameterGruppen === null) ? new ArrayList() : reportvorlageParameterGruppen);
		reportingParameter.reportvorlageParameterGruppen.add(standardausgabeoptionenGruppe);
		reportingParameter.eMailDaten = (eMailDaten === null) ? new ReportingEMailDaten() : eMailDaten;
		reportingParameter.sortierungDefinitionenGruppen = new ArrayList((sortierungDefinitionenGruppen === null) ? new ArrayList() : sortierungDefinitionenGruppen);
		reportingParameter.filterDefinitionenGruppen = new ArrayList((filterDefinitionenGruppen === null) ? new ArrayList() : filterDefinitionenGruppen);
		return reportingParameter;
	}

	/**
	 * Erstellt ein ReportingParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param name                            Name der Parametergruppe
	 * @param beschreibung                    Beschreibung der Parametergruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Parametergruppe in der UI sichtbar sein soll
	 * @param uiAnzahlSpalten                 Anzahl der Spalten für die Parametergruppe in der UI
	 * @param reportingReportvorlageParameter Liste der ReportingReportvorlageParameter, die in der Parametergruppe enthalten sind
	 *
	 * @return Ein ReportingReportvorlageParameterGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeReportingvorlageParameterGruppe(name: string, beschreibung: string, uiIstSichtbar: boolean, uiAnzahlSpalten: number, reportingReportvorlageParameter: List<ReportingReportvorlageParameter>) : ReportingReportvorlageParameterGruppe;

	/**
	 * Erstellt ein ReportingReportvorlageParameterGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param name                            Name der Parametergruppe
	 * @param beschreibung                    Beschreibung der Parametergruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Parametergruppe in der UI sichtbar sein soll
	 * @param uiAnzahlSpalten                 Anzahl der Spalten für die Parametergruppe in der UI
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param reportingReportvorlageParameter Liste der ReportingReportvorlageParameter, die in der Parametergruppe enthalten sind
	 *
	 * @return Ein ReportingReportvorlageParameterGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeReportingvorlageParameterGruppe(name: string, beschreibung: string, uiIstSichtbar: boolean, uiAnzahlSpalten: number, uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>, reportingReportvorlageParameter: List<ReportingReportvorlageParameter>) : ReportingReportvorlageParameterGruppe;

	/**
	 * Implementation for method overloads of 'erzeugeReportingvorlageParameterGruppe'
	 */
	public static erzeugeReportingvorlageParameterGruppe(__param0: string, __param1: string, __param2: boolean, __param3: number, __param4: List<ReportingReportvorlageParameter> | ServerMode, __param5?: List<BenutzerKompetenz>, __param6?: List<ReportingReportvorlageParameter>): ReportingReportvorlageParameterGruppe {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "number") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('java.util.List'))) || (__param4 === null)) && (__param5 === undefined) && (__param6 === undefined)) {
			const name: string = __param0;
			const beschreibung: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiAnzahlSpalten: number = __param3 as number;
			const reportingReportvorlageParameter: List<ReportingReportvorlageParameter> = cast_java_util_List(__param4);
			return ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe(name, beschreibung, uiIstSichtbar, uiAnzahlSpalten, ServerMode.STABLE, ArrayList.of(), reportingReportvorlageParameter);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "number") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null)) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null))) {
			const name: string = __param0;
			const beschreibung: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiAnzahlSpalten: number = __param3 as number;
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param4);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param5);
			const reportingReportvorlageParameter: List<ReportingReportvorlageParameter> = cast_java_util_List(__param6);
			const gruppe: ReportingReportvorlageParameterGruppe | null = new ReportingReportvorlageParameterGruppe();
			gruppe.name = name;
			gruppe.beschreibung = beschreibung;
			gruppe.uiIstSichtbar = uiIstSichtbar;
			gruppe.uiAnzahlSpalten = uiAnzahlSpalten;
			gruppe.reportvorlageParameter = reportingReportvorlageParameter;
			gruppe.uiErforderlicherServerMode = ReportingReportvorlageUtils.serverModeText(uiErforderlicherServerMode);
			gruppe.uiErforderlicheKompetenzen = ReportingReportvorlageUtils.kompetenzIds(uiErforderlicheKompetenzen);
			return gruppe;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erstellt ein ReportingReportvorlageParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param name              Name des Parameters
	 * @param bezeichnung       Bezeichnung des Parameters
	 * @param typ               Typ des Parameters
	 * @param wert              Wert des Parameters
	 * @param uiIstSichtbar     Gibt an, ob der Parameter in der UI sichtbar sein soll
	 * @param uiKomponentenTyp  Typ der UI-Komponente für den Parameter
	 * @param uiAnzahlSpalten   Anzahl der Spalten für die UI-Komponente
	 *
	 * @return Ein ReportingReportvorlageParameter-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingReportvorlageParameterTyp, wert: string, uiIstSichtbar: boolean, uiKomponentenTyp: ReportingUIKomponentenTyp, uiAnzahlSpalten: number) : ReportingReportvorlageParameter;

	/**
	 * Erstellt ein ReportingReportvorlageParameter-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param name                        Name des Parameters
	 * @param bezeichnung                 Bezeichnung des Parameters
	 * @param typ                         Typ des Parameters
	 * @param wert                        Wert des Parameters
	 * @param uiIstSichtbar               Gibt an, ob der Parameter in der UI sichtbar sein soll
	 * @param uiKomponentenTyp            Typ der UI-Komponente für den Parameter
	 * @param uiAnzahlSpalten             Anzahl der Spalten für die UI-Komponente
	 * @param uiErforderlicherServerMode  Der mindestens erforderliche ServerMode, damit der Parameter verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen  Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return Ein ReportingReportvorlageParameter-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingReportvorlageParameterTyp, wert: string, uiIstSichtbar: boolean, uiKomponentenTyp: ReportingUIKomponentenTyp, uiAnzahlSpalten: number, uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>) : ReportingReportvorlageParameter;

	/**
	 * Implementation for method overloads of 'erzeugeVorlageParameter'
	 */
	public static erzeugeVorlageParameter(__param0: string, __param1: string, __param2: ReportingReportvorlageParameterTyp, __param3: string, __param4: boolean, __param5: ReportingUIKomponentenTyp, __param6: number, __param7?: ServerMode, __param8?: List<BenutzerKompetenz>): ReportingReportvorlageParameter {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp')))) && ((__param3 !== undefined) && (typeof __param3 === "string")) && ((__param4 !== undefined) && typeof __param4 === "boolean") && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp')))) && ((__param6 !== undefined) && typeof __param6 === "number") && (__param7 === undefined) && (__param8 === undefined)) {
			const name: string = __param0;
			const bezeichnung: string = __param1;
			const typ: ReportingReportvorlageParameterTyp = cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageParameterTyp(__param2);
			const wert: string = __param3;
			const uiIstSichtbar: boolean = __param4 as boolean;
			const uiKomponentenTyp: ReportingUIKomponentenTyp = cast_de_svws_nrw_core_types_reporting_ReportingUIKomponentenTyp(__param5);
			const uiAnzahlSpalten: number = __param6 as number;
			return ReportingReportvorlageUtils.erzeugeVorlageParameter(name, bezeichnung, typ, wert, uiIstSichtbar, uiKomponentenTyp, uiAnzahlSpalten, ServerMode.STABLE, ArrayList.of());
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp')))) && ((__param3 !== undefined) && (typeof __param3 === "string")) && ((__param4 !== undefined) && typeof __param4 === "boolean") && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp')))) && ((__param6 !== undefined) && typeof __param6 === "number") && ((__param7 !== undefined) && ((__param7 instanceof JavaObject) && (__param7.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param8 !== undefined) && ((__param8 instanceof JavaObject) && (__param8.isTranspiledInstanceOf('java.util.List'))) || (__param8 === null))) {
			const name: string = __param0;
			const bezeichnung: string = __param1;
			const typ: ReportingReportvorlageParameterTyp = cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageParameterTyp(__param2);
			const wert: string = __param3;
			const uiIstSichtbar: boolean = __param4 as boolean;
			const uiKomponentenTyp: ReportingUIKomponentenTyp = cast_de_svws_nrw_core_types_reporting_ReportingUIKomponentenTyp(__param5);
			const uiAnzahlSpalten: number = __param6 as number;
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param7);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param8);
			const parameter: ReportingReportvorlageParameter | null = new ReportingReportvorlageParameter();
			parameter.name = name;
			parameter.bezeichnung = bezeichnung;
			parameter.typ = typ.getId();
			parameter.wert = wert;
			parameter.uiIstSichtbar = uiIstSichtbar;
			parameter.uiKomponentenTyp = uiKomponentenTyp.getId();
			parameter.uiAnzahlSpalten = uiAnzahlSpalten;
			parameter.uiErforderlicherServerMode = ReportingReportvorlageUtils.serverModeText(uiErforderlicherServerMode);
			parameter.uiErforderlicheKompetenzen = ReportingReportvorlageUtils.kompetenzIds(uiErforderlicheKompetenzen);
			return parameter;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erstellt ein ReportingEMailDaten-Objekt basierend auf den angegebenen Eigenschaften.
	 *
	 * @param eMailEmpfaengerTyp             Typ des Empfängers für die E-Mail
	 * @param istPrivateEmailAlternative     Gibt an, ob es sich um eine private E-Mail-Alternative handelt
	 * @param betreff                        Betreff der E-Mail
	 * @param text                           Textinhalt der E-Mail
	 *
	 * @return Ein ReportingEMailDaten-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeEmailParameter(eMailEmpfaengerTyp: ReportingEMailEmpfaengerTyp, istPrivateEmailAlternative: boolean, betreff: string, text: string): ReportingEMailDaten {
		const daten: ReportingEMailDaten | null = new ReportingEMailDaten();
		daten.empfaengerTyp = eMailEmpfaengerTyp.getId();
		daten.istPrivateEmailAlternative = istPrivateEmailAlternative;
		daten.betreff = betreff;
		daten.text = text;
		return daten;
	}

	/**
	 * Erstellt ein ReportingSortierungDefinitionGruppe-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param bezeichnung                    Bezeichnung der Sortierung-Definition-Gruppe
	 * @param typ                            Typ der Sortierung-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Sortierung-Definition-Gruppe in der UI sichtbar sein soll
	 * @param sortierungDefinitionenOptionen Liste der ReportingSortierungDefinition-Objekte, die in der Sortierung-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingSortierungDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeSortierungDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, sortierungDefinitionenOptionen: List<ReportingSortierungDefinition>) : ReportingSortierungDefinitionGruppe;

	/**
	 * Erstellt ein ReportingSortierungDefinitionGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param bezeichnung                    Bezeichnung der Sortierung-Definition-Gruppe
	 * @param typ                            Typ der Sortierung-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Sortierung-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiErforderlicherServerMode     Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen     Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param sortierungDefinitionenOptionen Liste der ReportingSortierungDefinition-Objekte, die in der Sortierung-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingSortierungDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeSortierungDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>, sortierungDefinitionenOptionen: List<ReportingSortierungDefinition>) : ReportingSortierungDefinitionGruppe;

	/**
	 * Implementation for method overloads of 'erzeugeSortierungDefinitionGruppe'
	 */
	public static erzeugeSortierungDefinitionGruppe(__param0: string, __param1: string, __param2: boolean, __param3: List<ReportingSortierungDefinition> | ServerMode, __param4?: List<BenutzerKompetenz>, __param5?: List<ReportingSortierungDefinition>): ReportingSortierungDefinitionGruppe {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.List'))) || (__param3 === null)) && (__param4 === undefined) && (__param5 === undefined)) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const sortierungDefinitionenOptionen: List<ReportingSortierungDefinition> = cast_java_util_List(__param3);
			return ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, ServerMode.STABLE, ArrayList.of(), sortierungDefinitionenOptionen);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('java.util.List'))) || (__param4 === null)) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null))) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param3);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param4);
			const sortierungDefinitionenOptionen: List<ReportingSortierungDefinition> = cast_java_util_List(__param5);
			const gruppe: ReportingSortierungDefinitionGruppe | null = new ReportingSortierungDefinitionGruppe();
			gruppe.bezeichnung = bezeichnung;
			gruppe.typ = typ;
			gruppe.uiIstSichtbar = uiIstSichtbar;
			gruppe.sortierungDefinitionenOptionen = new ArrayList(sortierungDefinitionenOptionen);
			gruppe.uiErforderlicherServerMode = ReportingReportvorlageUtils.serverModeText(uiErforderlicherServerMode);
			gruppe.uiErforderlicheKompetenzen = ReportingReportvorlageUtils.kompetenzIds(uiErforderlicheKompetenzen);
			return gruppe;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstFilterMultiselect: boolean, uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung, filterDefinitionenOptionen: List<ReportingFilterDefinition>) : ReportingFilterDefinitionGruppe;

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstFilterMultiselect: boolean, uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung, uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>, filterDefinitionenOptionen: List<ReportingFilterDefinition>) : ReportingFilterDefinitionGruppe;

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt mit einer Vorauswahl an Filterdefinitionen.
	 *
	 * <p>Wichtig: Die Einträge in {@code filterDefinitionenVorauswahl} müssen dieselben Objektinstanzen sein wie die entsprechenden Einträge in
	 * {@code filterDefinitionenOptionen}, da die UI die Vorauswahl über die Objektidentität ermittelt.</p>
	 *
	 * @param bezeichnung                    Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                            Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe zur Verfügung stehen
	 * @param filterDefinitionenVorauswahl    Liste der ReportingFilterDefinition-Objekte, die in der Gruppe vorausgewählt sein sollen
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstFilterMultiselect: boolean, uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung, filterDefinitionenOptionen: List<ReportingFilterDefinition>, filterDefinitionenVorauswahl: List<ReportingFilterDefinition>) : ReportingFilterDefinitionGruppe;

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt mit einer Vorauswahl an Filterdefinitionen, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * <p>Wichtig: Die Einträge in {@code filterDefinitionenVorauswahl} müssen dieselben Objektinstanzen sein wie die entsprechenden Einträge in
	 * {@code filterDefinitionenOptionen}, da die UI die Vorauswahl über die Objektidentität ermittelt.</p>
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe zur Verfügung stehen
	 * @param filterDefinitionenVorauswahl    Liste der ReportingFilterDefinition-Objekte, die in der Gruppe vorausgewählt sein sollen
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstFilterMultiselect: boolean, uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung, uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>, filterDefinitionenOptionen: List<ReportingFilterDefinition>, filterDefinitionenVorauswahl: List<ReportingFilterDefinition>) : ReportingFilterDefinitionGruppe;

	/**
	 * Implementation for method overloads of 'erzeugeFilterDefinitionGruppe'
	 */
	public static erzeugeFilterDefinitionGruppe(__param0: string, __param1: string, __param2: boolean, __param3: boolean, __param4: ReportingFilterVerknuepfung, __param5: List<ReportingFilterDefinition> | ServerMode, __param6?: List<BenutzerKompetenz> | List<ReportingFilterDefinition>, __param7?: List<ReportingFilterDefinition>, __param8?: List<ReportingFilterDefinition>): ReportingFilterDefinitionGruppe {
		if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "boolean") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null)) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined)) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiIstFilterMultiselect: boolean = __param3 as boolean;
			const uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung = cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung(__param4);
			const filterDefinitionenOptionen: List<ReportingFilterDefinition> = cast_java_util_List(__param5);
			return ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect, uiFilterMultiselectVerknuepfung, ServerMode.STABLE, ArrayList.of(), filterDefinitionenOptionen);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "boolean") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && ((__param7 !== undefined) && ((__param7 instanceof JavaObject) && (__param7.isTranspiledInstanceOf('java.util.List'))) || (__param7 === null)) && (__param8 === undefined)) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiIstFilterMultiselect: boolean = __param3 as boolean;
			const uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung = cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung(__param4);
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param5);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param6);
			const filterDefinitionenOptionen: List<ReportingFilterDefinition> = cast_java_util_List(__param7);
			const gruppe: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
			gruppe.bezeichnung = bezeichnung;
			gruppe.typ = typ;
			gruppe.uiIstSichtbar = uiIstSichtbar;
			gruppe.uiIstFilterMultiselect = uiIstFilterMultiselect;
			gruppe.uiFilterMultiselectVerknuepfung = uiFilterMultiselectVerknuepfung.getId();
			gruppe.filterDefinitionenOptionen = new ArrayList(filterDefinitionenOptionen);
			gruppe.uiErforderlicherServerMode = ReportingReportvorlageUtils.serverModeText(uiErforderlicherServerMode);
			gruppe.uiErforderlicheKompetenzen = ReportingReportvorlageUtils.kompetenzIds(uiErforderlicheKompetenzen);
			return gruppe;
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "boolean") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('java.util.List'))) || (__param5 === null)) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && (__param7 === undefined) && (__param8 === undefined)) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiIstFilterMultiselect: boolean = __param3 as boolean;
			const uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung = cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung(__param4);
			const filterDefinitionenOptionen: List<ReportingFilterDefinition> = cast_java_util_List(__param5);
			const filterDefinitionenVorauswahl: List<ReportingFilterDefinition> = cast_java_util_List(__param6);
			return ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect, uiFilterMultiselectVerknuepfung, ServerMode.STABLE, ArrayList.of(), filterDefinitionenOptionen, filterDefinitionenVorauswahl);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && typeof __param2 === "boolean") && ((__param3 !== undefined) && typeof __param3 === "boolean") && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('java.util.List'))) || (__param6 === null)) && ((__param7 !== undefined) && ((__param7 instanceof JavaObject) && (__param7.isTranspiledInstanceOf('java.util.List'))) || (__param7 === null)) && ((__param8 !== undefined) && ((__param8 instanceof JavaObject) && (__param8.isTranspiledInstanceOf('java.util.List'))) || (__param8 === null))) {
			const bezeichnung: string = __param0;
			const typ: string = __param1;
			const uiIstSichtbar: boolean = __param2 as boolean;
			const uiIstFilterMultiselect: boolean = __param3 as boolean;
			const uiFilterMultiselectVerknuepfung: ReportingFilterVerknuepfung = cast_de_svws_nrw_core_types_reporting_ReportingFilterVerknuepfung(__param4);
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param5);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param6);
			const filterDefinitionenOptionen: List<ReportingFilterDefinition> = cast_java_util_List(__param7);
			const filterDefinitionenVorauswahl: List<ReportingFilterDefinition> = cast_java_util_List(__param8);
			const gruppe: ReportingFilterDefinitionGruppe | null = ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect, uiFilterMultiselectVerknuepfung, uiErforderlicherServerMode, uiErforderlicheKompetenzen, filterDefinitionenOptionen);
			gruppe.filterDefinitionen = new ArrayList(filterDefinitionenVorauswahl);
			return gruppe;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Erstellt die Filter-Definition-Gruppe "Statusfilter" für den Reporting-Typ "ReportingSchueler". Als Optionen werden alle Werte des
	 * {@link SchuelerStatus} angeboten (Multiselect, OR-Verknüpfung); vorausgewählt sind die Status AKTIV und EXTERN.
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt für die Filterung nach dem Schülerstatus
	 */
	public static erzeugeSchuelerStatusfilterGruppe() : ReportingFilterDefinitionGruppe;

	/**
	 * Erstellt die Filter-Definition-Gruppe "Statusfilter" für den Reporting-Typ "ReportingSchueler", inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen. Als Optionen werden alle Werte des {@link SchuelerStatus} angeboten (Multiselect, OR-Verknüpfung); vorausgewählt sind
	 * die Status AKTIV und EXTERN.
	 *
	 * @param uiErforderlicherServerMode Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt für die Filterung nach dem Schülerstatus
	 */
	public static erzeugeSchuelerStatusfilterGruppe(uiErforderlicherServerMode: ServerMode, uiErforderlicheKompetenzen: List<BenutzerKompetenz>) : ReportingFilterDefinitionGruppe;

	/**
	 * Implementation for method overloads of 'erzeugeSchuelerStatusfilterGruppe'
	 */
	public static erzeugeSchuelerStatusfilterGruppe(__param0?: ServerMode, __param1?: List<BenutzerKompetenz>): ReportingFilterDefinitionGruppe {
		if ((__param0 === undefined) && (__param1 === undefined)) {
			return ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe(ServerMode.STABLE, ArrayList.of());
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.types.ServerMode')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null))) {
			const uiErforderlicherServerMode: ServerMode = cast_de_svws_nrw_core_types_ServerMode(__param0);
			const uiErforderlicheKompetenzen: List<BenutzerKompetenz> = cast_java_util_List(__param1);
			const optionen: List<ReportingFilterDefinition> | null = new ArrayList<ReportingFilterDefinition>();
			const vorauswahl: List<ReportingFilterDefinition> | null = new ArrayList<ReportingFilterDefinition>();
			for (const status of SchuelerStatus.values()) {
				const definition: ReportingFilterDefinition | null = ReportingFilterDefinitionFactory.definition(ReportingReportvorlageUtils.normalisiereSchuelerStatusBezeichnung(status.name()), "ReportingSchueler", ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("status", status.name())));
				optionen.add(definition);
				if ((status as unknown === SchuelerStatus.AKTIV as unknown) || (status as unknown === SchuelerStatus.EXTERN as unknown)) {
					vorauswahl.add(definition);
				}
			}
			return ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe("Schülerstatus", "ReportingSchueler", true, true, ReportingFilterVerknuepfung.OR, uiErforderlicherServerMode, uiErforderlicheKompetenzen, optionen, vorauswahl);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Normalisiert den technischen Namen eines {@link SchuelerStatus} für die Anzeige, indem der erste Buchstabe groß und die übrigen Buchstaben klein
	 * geschrieben werden (z. B. "AKTIV" wird zu "Aktiv").
	 *
	 * @param name Der technische Name (Enum-Name) des Status
	 *
	 * @return Die normalisierte Bezeichnung für die Anzeige
	 */
	private static normalisiereSchuelerStatusBezeichnung(name: string): string {
		if (JavaString.isEmpty(name)) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	/**
	 * Wählt aus den Katalog-Optionen einer Sortier- oder Filtergruppe genau die Einträge aus, deren Bezeichnung in der Liste der gespeicherten Bezeichnungen
	 * enthalten ist. Für jede gespeicherte Bezeichnung wird die passende Options-Instanz herausgesucht (nicht kopiert), damit die UI die Auswahl über die
	 * Objektidentität erkennt. Unbekannte Bezeichnungen (z. B. weil sich der Katalog geändert hat) werden übersprungen; die Reihenfolge der gespeicherten
	 * Bezeichnungen bleibt erhalten. Die Methode wird sowohl serverseitig (Anwenden der gespeicherten Einstellungen im ReportingParameterBuilder) als auch
	 * clientseitig (Vorbefüllen der Auswahl-Komponenten aus den gespeicherten Einstellungen) verwendet.
	 *
	 * @param <T>                       der Options-Typ, z. B. {@link ReportingSortierungDefinition} oder {@link ReportingFilterDefinition}
	 * @param optionen                  die Katalog-Optionen der Gruppe
	 * @param bezeichnungExtractor      Funktion, die die Bezeichnung eines Options-Objekts liefert
	 * @param gespeicherteBezeichnungen die gespeicherten Bezeichnungen in Auswahlreihenfolge
	 *
	 * @return die ausgewählten Options-Instanzen (dieselben Instanzen wie in {@code optionen}), ggf. leer
	 */
	public static waehleGespeicherteAuswahl<T>(optionen: List<T>, bezeichnungExtractor: JavaFunction<T, string>, gespeicherteBezeichnungen: List<string>): List<T> {
		const ergebnis: List<T> | null = new ArrayList<T>();
		for (const bezeichnung of gespeicherteBezeichnungen) {
			if (bezeichnung === null) {
				continue;
			}
			for (const option of optionen) {
				if ((option !== null) && JavaObject.equalsTranspiler(bezeichnung, (bezeichnungExtractor.apply(option)))) {
					ergebnis.add(option);
					break;
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Normalisiert den Eingabestring für Schlüsselangaben, indem er Leerzeichen entfernt und in Kleinbuchstaben umgewandelt wird.
	 *
	 * @param input Der Eingabestring, der normalisiert werden soll
	 *
	 * @return Der normalisierte Eingabestring
	 */
	public static normalizeKeyInput(input: string | null): string {
		return (input === null) ? "" : input.trim().toLowerCase();
	}

	/**
	 * Erstellt eine Kopie eines ReportingParameter-Objekts.
	 *
	 * @param source Das zu kopierende ReportingParameter-Objekt
	 *
	 * @return Eine Kopie des ReportingParameter-Objekts
	 */
	public static cloneReportingParameter(source: ReportingParameter): ReportingParameter {
		const copy: ReportingParameter | null = new ReportingParameter();
		copy.idSchuljahresabschnitt = source.idSchuljahresabschnitt;
		copy.ausgabeformat = source.ausgabeformat;
		copy.reportvorlage = source.reportvorlage;
		copy.idHauptdatenObjekt = source.idHauptdatenObjekt;
		copy.ausgabeformatOptionen.addAll(source.ausgabeformatOptionen);
		copy.idsHauptdaten.addAll(source.idsHauptdaten);
		copy.idsDetaildaten.addAll(source.idsDetaildaten);
		if (source.eMailDaten !== null) {
			copy.eMailDaten = new ReportingEMailDaten();
			copy.eMailDaten.empfaengerTyp = source.eMailDaten.empfaengerTyp;
			copy.eMailDaten.istPrivateEmailAlternative = source.eMailDaten.istPrivateEmailAlternative;
			copy.eMailDaten.betreff = source.eMailDaten.betreff;
			copy.eMailDaten.text = source.eMailDaten.text;
		} else {
			copy.eMailDaten = null;
		}
		copy.reportvorlageParameterGruppen.addAll(ReportingReportvorlageUtils.cloneVorlageParameterGruppen(source.reportvorlageParameterGruppen));
		copy.sortierungDefinitionenGruppen.addAll(ReportingReportvorlageUtils.cloneSortierungDefinitionGruppen(source.sortierungDefinitionenGruppen));
		copy.filterDefinitionenGruppen.addAll(ReportingReportvorlageUtils.cloneFilterDefinitionGruppen(source.filterDefinitionenGruppen));
		return copy;
	}

	private static cloneVorlageParameterGruppen(source: List<ReportingReportvorlageParameterGruppe> | null): List<ReportingReportvorlageParameterGruppe> {
		const result: List<ReportingReportvorlageParameterGruppe> | null = new ArrayList<ReportingReportvorlageParameterGruppe>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const vpg of source) {
			if (vpg === null) {
				continue;
			}
			const vpgCopy: ReportingReportvorlageParameterGruppe | null = new ReportingReportvorlageParameterGruppe();
			vpgCopy.name = vpg.name;
			vpgCopy.beschreibung = vpg.beschreibung;
			vpgCopy.uiIstSichtbar = vpg.uiIstSichtbar;
			vpgCopy.uiAnzahlSpalten = vpg.uiAnzahlSpalten;
			vpgCopy.uiErforderlicherServerMode = vpg.uiErforderlicherServerMode;
			if (vpg.uiErforderlicheKompetenzen !== null) {
				vpgCopy.uiErforderlicheKompetenzen = new ArrayList(vpg.uiErforderlicheKompetenzen);
			}
			if (vpg.reportvorlageParameter !== null) {
				vpgCopy.reportvorlageParameter.addAll(ReportingReportvorlageUtils.cloneVorlageParameter(vpg.reportvorlageParameter));
			}
			result.add(vpgCopy);
		}
		return result;
	}

	private static cloneVorlageParameter(source: List<ReportingReportvorlageParameter> | null): List<ReportingReportvorlageParameter> {
		const result: List<ReportingReportvorlageParameter> | null = new ArrayList<ReportingReportvorlageParameter>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const vp of source) {
			if (vp === null) {
				continue;
			}
			const vpCopy: ReportingReportvorlageParameter | null = new ReportingReportvorlageParameter();
			vpCopy.name = vp.name;
			vpCopy.bezeichnung = vp.bezeichnung;
			vpCopy.typ = vp.typ;
			vpCopy.wert = vp.wert;
			vpCopy.uiIstSichtbar = vp.uiIstSichtbar;
			vpCopy.uiKomponentenTyp = vp.uiKomponentenTyp;
			vpCopy.uiAnzahlSpalten = vp.uiAnzahlSpalten;
			vpCopy.uiErforderlicherServerMode = vp.uiErforderlicherServerMode;
			if (vp.uiErforderlicheKompetenzen !== null) {
				vpCopy.uiErforderlicheKompetenzen = new ArrayList(vp.uiErforderlicheKompetenzen);
			}
			result.add(vpCopy);
		}
		return result;
	}

	private static cloneSortierungDefinitionGruppen(source: List<ReportingSortierungDefinitionGruppe> | null): List<ReportingSortierungDefinitionGruppe> {
		const result: List<ReportingSortierungDefinitionGruppe> | null = new ArrayList<ReportingSortierungDefinitionGruppe>();
		if (source === null) {
			return result;
		}
		for (const sdg of source) {
			if (sdg.sortierungDefinitionenOptionen === null) {
				continue;
			}
			const sdgCopy: ReportingSortierungDefinitionGruppe | null = new ReportingSortierungDefinitionGruppe();
			sdgCopy.bezeichnung = sdg.bezeichnung;
			sdgCopy.typ = sdg.typ;
			sdgCopy.uiIstSichtbar = sdg.uiIstSichtbar;
			sdgCopy.uiErforderlicherServerMode = sdg.uiErforderlicherServerMode;
			if (sdg.uiErforderlicheKompetenzen !== null) {
				sdgCopy.uiErforderlicheKompetenzen = new ArrayList(sdg.uiErforderlicheKompetenzen);
			}
			if (sdg.sortierungDefinitionenOptionen !== null) {
				sdgCopy.sortierungDefinitionenOptionen.addAll(ReportingReportvorlageUtils.cloneSortierungDefinitionen(sdg.sortierungDefinitionenOptionen));
			}
			result.add(sdgCopy);
		}
		return result;
	}

	private static cloneSortierungDefinitionen(source: List<ReportingSortierungDefinition> | null): List<ReportingSortierungDefinition> {
		const result: List<ReportingSortierungDefinition> | null = new ArrayList<ReportingSortierungDefinition>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const sd of source) {
			if (sd === null) {
				continue;
			}
			const sdCopy: ReportingSortierungDefinition | null = new ReportingSortierungDefinition();
			sdCopy.bezeichnung = sd.bezeichnung;
			sdCopy.typ = sd.typ;
			sdCopy.verwendeStandardsortierung = sd.verwendeStandardsortierung;
			sdCopy.attribute.addAll(sd.attribute);
			result.add(sdCopy);
		}
		return result;
	}

	private static cloneFilterDefinitionGruppen(source: List<ReportingFilterDefinitionGruppe> | null): List<ReportingFilterDefinitionGruppe> {
		const result: List<ReportingFilterDefinitionGruppe> | null = new ArrayList<ReportingFilterDefinitionGruppe>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const fdg of source) {
			if (fdg === null) {
				continue;
			}
			const fdgCopy: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
			fdgCopy.bezeichnung = fdg.bezeichnung;
			fdgCopy.typ = fdg.typ;
			fdgCopy.uiIstSichtbar = fdg.uiIstSichtbar;
			fdgCopy.uiIstFilterMultiselect = fdg.uiIstFilterMultiselect;
			fdgCopy.uiFilterMultiselectVerknuepfung = fdg.uiFilterMultiselectVerknuepfung;
			fdgCopy.uiErforderlicherServerMode = fdg.uiErforderlicherServerMode;
			if (fdg.uiErforderlicheKompetenzen !== null) {
				fdgCopy.uiErforderlicheKompetenzen = new ArrayList(fdg.uiErforderlicheKompetenzen);
			}
			if (fdg.filterDefinitionenOptionen !== null) {
				const optionenKopie: List<ReportingFilterDefinition> | null = ReportingReportvorlageUtils.cloneFilterDefinitionen(fdg.filterDefinitionenOptionen);
				fdgCopy.filterDefinitionenOptionen.addAll(optionenKopie);
				ReportingReportvorlageUtils.uebertrageFilterVorauswahl(fdg, optionenKopie, fdgCopy);
			}
			result.add(fdgCopy);
		}
		return result;
	}

	private static uebertrageFilterVorauswahl(fdg: ReportingFilterDefinitionGruppe, optionenKopie: List<ReportingFilterDefinition>, fdgCopy: ReportingFilterDefinitionGruppe): void {
		if (fdg.filterDefinitionen !== null) {
			for (const vorauswahl of fdg.filterDefinitionen) {
				const index: number = fdg.filterDefinitionenOptionen.indexOf(vorauswahl);
				if ((index >= 0) && (index < optionenKopie.size())) {
					fdgCopy.filterDefinitionen.add(optionenKopie.get(index));
				}
			}
		}
	}

	private static cloneFilterDefinitionen(source: List<ReportingFilterDefinition> | null): List<ReportingFilterDefinition> {
		const result: List<ReportingFilterDefinition> | null = new ArrayList<ReportingFilterDefinition>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const fd of source) {
			if (fd === null) {
				continue;
			}
			const fdCopy: ReportingFilterDefinition | null = new ReportingFilterDefinition();
			fdCopy.bezeichnung = fd.bezeichnung;
			fdCopy.typ = fd.typ;
			fdCopy.kriterien.addAll(ReportingReportvorlageUtils.cloneFilterKriterien(fd.kriterien));
			result.add(fdCopy);
		}
		return result;
	}

	private static cloneFilterKriterien(source: List<ReportingFilterKriterium> | null): List<ReportingFilterKriterium> {
		const result: List<ReportingFilterKriterium> | null = new ArrayList<ReportingFilterKriterium>();
		if ((source === null) || source.isEmpty()) {
			return result;
		}
		for (const k of source) {
			if (k === null) {
				continue;
			}
			const kCopy: ReportingFilterKriterium | null = new ReportingFilterKriterium();
			kCopy.verknuepfung = k.verknuepfung;
			kCopy.nicht = k.nicht;
			if (k.eintraege !== null) {
				for (const e of k.eintraege) {
					const eCopy: ReportingFilterEintrag | null = new ReportingFilterEintrag();
					eCopy.attribut = e.attribut;
					eCopy.operation = e.operation;
					eCopy.werte.addAll(e.werte);
					kCopy.eintraege.add(eCopy);
				}
			}
			if (k.unterkriterien !== null) {
				kCopy.unterkriterien.addAll(ReportingReportvorlageUtils.cloneFilterKriterien(k.unterkriterien));
			}
			result.add(kCopy);
		}
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageUtils>('de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils');

}

export function cast_de_svws_nrw_core_utils_reporting_ReportingReportvorlageUtils(obj: unknown): ReportingReportvorlageUtils {
	return obj as ReportingReportvorlageUtils;
}
