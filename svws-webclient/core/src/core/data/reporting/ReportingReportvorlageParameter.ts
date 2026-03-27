import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageParameterTyp } from '../../../core/types/reporting/ReportingReportvorlageParameterTyp';
import { ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
import { Class } from '../../../java/lang/Class';

export class ReportingReportvorlageParameter extends JavaObject {

	/**
	 * Der Name des Vorlage-Parameters, wie er später im HTML-Template verwendet wird.
	 */
	public name: string = "";

	/**
	 * Die Bezeichnung des Vorlage-Parameters, wie er später in der Anzeige der GUI oder an ähnlichen Stellen dargestellt werden soll.
	 */
	public bezeichnung: string = "";

	/**
	 * Der Typ des Wertes des Vorlage-Parameters.
	 */
	public typ: number = ReportingReportvorlageParameterTyp.UNDEFINED.getId();

	/**
	 * Der Wert des Vorlage-Parameters.
	 */
	public wert: string = "";

	/**
	 * Gibt an, ob der Parameter in der UI sichtbar sein soll.
	 */
	public uiIstSichtbar: boolean = true;

	/**
	 * Der Typ der UI-Komponente (z.B. 'checkbox', 'input', 'select', 'textarea', 'numberpicker', 'datepicker').
	 */
	public uiKomponentenTyp: number = ReportingUIKomponentenTyp.UNDEFINED.getId();

	/**
	 * Die Anzahl der Grid-Spalten, die der Parameter in der UI einnehmen soll.
	 */
	public uiAnzahlSpalten: number = 1;


	/**
	 * Konstruktor für die Klasse.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageParameter>('de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter');

	public static transpilerFromJSON(json: string): ReportingReportvorlageParameter {
		const obj = JSON.parse(json) as Partial<ReportingReportvorlageParameter>;
		const result = new ReportingReportvorlageParameter();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.wert === undefined)
			throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		if (obj.uiIstSichtbar === undefined)
			throw new Error('invalid json format, missing attribute uiIstSichtbar');
		result.uiIstSichtbar = obj.uiIstSichtbar;
		if (obj.uiKomponentenTyp === undefined)
			throw new Error('invalid json format, missing attribute uiKomponentenTyp');
		result.uiKomponentenTyp = obj.uiKomponentenTyp;
		if (obj.uiAnzahlSpalten === undefined)
			throw new Error('invalid json format, missing attribute uiAnzahlSpalten');
		result.uiAnzahlSpalten = obj.uiAnzahlSpalten;
		return result;
	}

	public static transpilerToJSON(obj: ReportingReportvorlageParameter): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		result += '"uiKomponentenTyp" : ' + obj.uiKomponentenTyp.toString() + ',';
		result += '"uiAnzahlSpalten" : ' + obj.uiAnzahlSpalten.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingReportvorlageParameter>): string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		}
		if (obj.uiIstSichtbar !== undefined) {
			result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		}
		if (obj.uiKomponentenTyp !== undefined) {
			result += '"uiKomponentenTyp" : ' + obj.uiKomponentenTyp.toString() + ',';
		}
		if (obj.uiAnzahlSpalten !== undefined) {
			result += '"uiAnzahlSpalten" : ' + obj.uiAnzahlSpalten.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingReportvorlageParameter(obj: unknown): ReportingReportvorlageParameter {
	return obj as ReportingReportvorlageParameter;
}
