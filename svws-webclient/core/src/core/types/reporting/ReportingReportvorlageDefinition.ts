import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { BenutzerKompetenz } from '../../../core/types/benutzer/BenutzerKompetenz';
import { ReportingReportvorlageDatenContext } from '../../../core/types/reporting/ReportingReportvorlageDatenContext';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { ReportingReportvorlageUiNutzung } from '../../../core/types/reporting/ReportingReportvorlageUiNutzung';
import { Class } from '../../../java/lang/Class';
import { ServerMode } from '../../../core/types/ServerMode';
import { ReportingParameter } from '../../../core/data/reporting/ReportingParameter';

export class ReportingReportvorlageDefinition extends JavaObject {

	/**
	 * Die eindeutige Bezeichnung der Report-Vorlage.
	 */
	private readonly bezeichnung: string;

	/**
	 * Der Titel der Vorlage in der UI.
	 */
	private uiTitel: string | null = null;

	/**
	 * Die Beschreibung der Vorlage in der UI.
	 */
	private uiBeschreibung: string | null = null;

	/**
	 * Die Bereiche im Client, in denen die Vorlage angeboten wird.
	 */
	private uiNutzungen: List<ReportingReportvorlageUiNutzung> = ArrayList.of();

	/**
	 * Der Datenaufbau, den die Vorlage verwendet.
	 */
	private datenContext: ReportingReportvorlageDatenContext | null = null;

	/**
	 * Der Pfad zur HTML-Template-Datei, relativ zum Root-Pfad der Vorlagen.
	 */
	private pfadHtmlTemplate: string | null = null;

	/**
	 * Der statische Dateiname ohne Dateiendung.
	 */
	private dateiname: string | null = null;

	/**
	 * Die Benutzerkompetenzen, von denen der Benutzer mindestens eine benötigt.
	 */
	private benutzerKompetenzen: List<BenutzerKompetenz> | null = null;

	/**
	 * Die Schulformen, an denen die Vorlage genutzt werden darf. Eine leere Liste gilt für alle Schulformen.
	 */
	private schulformen: List<Schulform> = ArrayList.of();

	/**
	 * Der mindestens erforderliche ServerMode.
	 */
	private erforderlicherServerMode: ServerMode = ServerMode.STABLE;

	/**
	 * Die Parameter der Vorlage mit ihren Parametergruppen.
	 */
	private reportingParameter: ReportingParameter | null = null;


	/**
	 * Erzeugt eine Definition mit der Bezeichnung der Report-Vorlage. Alle weiteren Angaben folgen über die Setter.
	 *
	 * @param bezeichnung   die eindeutige Bezeichnung der Report-Vorlage
	 */
	public constructor(bezeichnung: string) {
		super();
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Setzt den Titel, den die UI zur Vorlage anzeigt, z. B. als Card-Titel.
	 *
	 * @param uiTitel   der Titel
	 *
	 * @return diese Definition
	 */
	public setUiTitel(uiTitel: string): ReportingReportvorlageDefinition {
		this.uiTitel = uiTitel;
		return this;
	}

	/**
	 * Setzt die Beschreibung, die die UI zur Vorlage anzeigt, z. B. als Card-Untertitel.
	 *
	 * @param uiBeschreibung   die Beschreibung
	 *
	 * @return diese Definition
	 */
	public setUiBeschreibung(uiBeschreibung: string): ReportingReportvorlageDefinition {
		this.uiBeschreibung = uiBeschreibung;
		return this;
	}

	/**
	 * Setzt die Bereiche im Client, in denen die Vorlage angeboten wird. Ohne Aufruf bindet der Client die Vorlage nur dort ein, wo er sie ausdrücklich nennt.
	 *
	 * @param uiNutzungen   die Bereiche im Client
	 *
	 * @return diese Definition
	 */
	public setUiNutzungen(uiNutzungen: List<ReportingReportvorlageUiNutzung>): ReportingReportvorlageDefinition {
		this.uiNutzungen = uiNutzungen;
		return this;
	}

	/**
	 * Setzt den Datenaufbau. Er bestimmt, welche Daten der Server für die Vorlage lädt.
	 *
	 * @param datenContext   der Datenaufbau
	 *
	 * @return diese Definition
	 */
	public setDatenContext(datenContext: ReportingReportvorlageDatenContext): ReportingReportvorlageDefinition {
		this.datenContext = datenContext;
		return this;
	}

	/**
	 * Setzt den Pfad zur HTML-Template-Datei, relativ zum Root-Pfad der Vorlagen.
	 *
	 * @param pfadHtmlTemplate   der Pfad
	 *
	 * @return diese Definition
	 */
	public setPfadHtmlTemplate(pfadHtmlTemplate: string): ReportingReportvorlageDefinition {
		this.pfadHtmlTemplate = pfadHtmlTemplate;
		return this;
	}

	/**
	 * Setzt den statischen Dateinamen ohne Dateiendung.
	 *
	 * @param dateiname   der Dateiname
	 *
	 * @return diese Definition
	 */
	public setDateiname(dateiname: string): ReportingReportvorlageDefinition {
		this.dateiname = dateiname;
		return this;
	}

	/**
	 * Setzt die Benutzerkompetenzen. Der Benutzer benötigt mindestens eine davon.
	 *
	 * @param benutzerKompetenzen   die Benutzerkompetenzen
	 *
	 * @return diese Definition
	 */
	public setBenutzerKompetenzen(benutzerKompetenzen: List<BenutzerKompetenz>): ReportingReportvorlageDefinition {
		this.benutzerKompetenzen = benutzerKompetenzen;
		return this;
	}

	/**
	 * Beschränkt die Vorlage auf die übergebenen Schulformen. Ohne Aufruf gilt die Vorlage für alle Schulformen.
	 *
	 * @param schulformen   die Schulformen
	 *
	 * @return diese Definition
	 */
	public setSchulformen(schulformen: List<Schulform>): ReportingReportvorlageDefinition {
		this.schulformen = schulformen;
		return this;
	}

	/**
	 * Setzt den mindestens erforderlichen ServerMode. Ohne Aufruf gilt STABLE und die Vorlage ist in allen Modi nutzbar.
	 *
	 * @param erforderlicherServerMode   der ServerMode
	 *
	 * @return diese Definition
	 */
	public setErforderlicherServerMode(erforderlicherServerMode: ServerMode): ReportingReportvorlageDefinition {
		this.erforderlicherServerMode = erforderlicherServerMode;
		return this;
	}

	/**
	 * Setzt die Parameter der Vorlage mit ihren Parametergruppen.
	 *
	 * @param reportingParameter   die Parameter
	 *
	 * @return diese Definition
	 */
	public setReportingParameter(reportingParameter: ReportingParameter): ReportingReportvorlageDefinition {
		this.reportingParameter = reportingParameter;
		return this;
	}

	getBezeichnung(): string {
		return this.bezeichnung;
	}

	getUiTitel(): string {
		if (this.uiTitel === null) {
			throw this.fehlendeAngabe("uiTitel");
		}
		return this.uiTitel;
	}

	getUiBeschreibung(): string {
		if (this.uiBeschreibung === null) {
			throw this.fehlendeAngabe("uiBeschreibung");
		}
		return this.uiBeschreibung;
	}

	getUiNutzungen(): List<ReportingReportvorlageUiNutzung> {
		return this.uiNutzungen;
	}

	getDatenContext(): ReportingReportvorlageDatenContext {
		if (this.datenContext === null) {
			throw this.fehlendeAngabe("datenContext");
		}
		return this.datenContext;
	}

	getPfadHtmlTemplate(): string {
		if (this.pfadHtmlTemplate === null) {
			throw this.fehlendeAngabe("pfadHtmlTemplate");
		}
		return this.pfadHtmlTemplate;
	}

	getDateiname(): string {
		if (this.dateiname === null) {
			throw this.fehlendeAngabe("dateiname");
		}
		return this.dateiname;
	}

	getBenutzerKompetenzen(): List<BenutzerKompetenz> {
		if (this.benutzerKompetenzen === null) {
			throw this.fehlendeAngabe("benutzerKompetenzen");
		}
		return this.benutzerKompetenzen;
	}

	getSchulformen(): List<Schulform> {
		return this.schulformen;
	}

	getErforderlicherServerMode(): ServerMode {
		return this.erforderlicherServerMode;
	}

	getReportingParameter(): ReportingParameter {
		if (this.reportingParameter === null) {
			throw this.fehlendeAngabe("reportingParameter");
		}
		return this.reportingParameter;
	}

	/**
	 * Erzeugt den Fehler für eine fehlende Pflichtangabe. Er tritt schon beim Laden des Enums auf und fällt damit in jedem Test auf.
	 *
	 * @param angabe   der Name der fehlenden Angabe
	 *
	 * @return der Fehler mit Vorlage und fehlender Angabe
	 */
	private fehlendeAngabe(angabe: string): IllegalStateException {
		return new IllegalStateException("Der Report-Vorlage '" + this.bezeichnung + "' fehlt die Pflichtangabe '" + angabe + "'.");
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.ReportingReportvorlageDefinition';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.ReportingReportvorlageDefinition'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageDefinition>('de.svws_nrw.core.types.reporting.ReportingReportvorlageDefinition');

}

export function cast_de_svws_nrw_core_types_reporting_ReportingReportvorlageDefinition(obj: unknown): ReportingReportvorlageDefinition {
	return obj as ReportingReportvorlageDefinition;
}
