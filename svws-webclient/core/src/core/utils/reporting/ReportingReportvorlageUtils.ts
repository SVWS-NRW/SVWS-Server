import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterVerknuepfung } from '../../../core/types/reporting/ReportingFilterVerknuepfung';
import { ReportingFilterDefinition } from '../../../core/data/reporting/ReportingFilterDefinition';
import { ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import { ReportingSortierungDefinitionGruppe } from '../../../core/data/reporting/ReportingSortierungDefinitionGruppe';
import { ReportingReportvorlageParameterTyp } from '../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingEMailDaten } from '../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
import { ReportingFilterKriterium } from '../../../core/data/reporting/ReportingFilterKriterium';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';
import { ReportingSortierungDefinition } from '../../../core/data/reporting/ReportingSortierungDefinition';
import { ReportingEMailEmpfaengerTyp } from '../../../core/types/reporting/ReportingEMailEmpfaengerTyp';
import { ReportingFilterDefinitionGruppe } from '../../../core/data/reporting/ReportingFilterDefinitionGruppe';
import { ReportingFilterEintrag } from '../../../core/data/reporting/ReportingFilterEintrag';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { ReportingAusgabeformat } from '../../../core/types/reporting/ReportingAusgabeformat';

export class ReportingReportvorlageUtils extends JavaObject {


	private constructor() {
		super();
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
	public static erzeugeReportingvorlageParameterGruppe(name: string, beschreibung: string, uiIstSichtbar: boolean, uiAnzahlSpalten: number, reportingReportvorlageParameter: List<ReportingReportvorlageParameter>): ReportingReportvorlageParameterGruppe {
		const gruppe: ReportingReportvorlageParameterGruppe | null = new ReportingReportvorlageParameterGruppe();
		gruppe.name = name;
		gruppe.beschreibung = beschreibung;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiAnzahlSpalten = uiAnzahlSpalten;
		gruppe.reportvorlageParameter = reportingReportvorlageParameter;
		return gruppe;
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
	public static erzeugeVorlageParameter(name: string, bezeichnung: string, typ: ReportingReportvorlageParameterTyp, wert: string, uiIstSichtbar: boolean, uiKomponentenTyp: ReportingUIKomponentenTyp, uiAnzahlSpalten: number): ReportingReportvorlageParameter {
		const parameter: ReportingReportvorlageParameter | null = new ReportingReportvorlageParameter();
		parameter.name = name;
		parameter.bezeichnung = bezeichnung;
		parameter.typ = typ.getId();
		parameter.wert = wert;
		parameter.uiIstSichtbar = uiIstSichtbar;
		parameter.uiKomponentenTyp = uiKomponentenTyp.getId();
		parameter.uiAnzahlSpalten = uiAnzahlSpalten;
		return parameter;
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
	public static erzeugeSortierungDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, sortierungDefinitionenOptionen: List<ReportingSortierungDefinition>): ReportingSortierungDefinitionGruppe {
		const gruppe: ReportingSortierungDefinitionGruppe | null = new ReportingSortierungDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.sortierungDefinitionenOptionen = new ArrayList(sortierungDefinitionenOptionen);
		return gruppe;
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param bezeichnung                    Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                            Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstMultiselect               Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param multiselectVerknuepfung        Verknüpfung für Multiselect-Filter-Definitionen
	 * @param filterDefinitionenOptionen     Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static erzeugeFilterDefinitionGruppe(bezeichnung: string, typ: string, uiIstSichtbar: boolean, uiIstMultiselect: boolean, multiselectVerknuepfung: ReportingFilterVerknuepfung, filterDefinitionenOptionen: List<ReportingFilterDefinition>): ReportingFilterDefinitionGruppe {
		const gruppe: ReportingFilterDefinitionGruppe | null = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiIstMultiselect = uiIstMultiselect;
		gruppe.multiselectVerknuepfung = multiselectVerknuepfung.getId();
		gruppe.filterDefinitionenOptionen = new ArrayList(filterDefinitionenOptionen);
		return gruppe;
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
			fdgCopy.uiIstMultiselect = fdg.uiIstMultiselect;
			fdgCopy.multiselectVerknuepfung = fdg.multiselectVerknuepfung;
			if (fdg.filterDefinitionenOptionen !== null) {
				fdgCopy.filterDefinitionenOptionen.addAll(ReportingReportvorlageUtils.cloneFilterDefinitionen(fdg.filterDefinitionenOptionen));
			}
			result.add(fdgCopy);
		}
		return result;
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
