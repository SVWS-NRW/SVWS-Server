import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingVorlageParameterTyp } from '../../../core/types/reporting/ReportingVorlageParameterTyp';
import { ReportingUIKomponentenTyp } from '../../../core/types/reporting/ReportingUIKomponentenTyp';
import { Class } from '../../../java/lang/Class';

export class ReportingVorlageParameter extends JavaObject {

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
	public typ: number = ReportingVorlageParameterTyp.UNDEFINED.getId();

	/**
	 * Der Wert des Vorlage-Parameters.
	 */
	public wert: string = "";

	/**
	 * Gibt an, ob der Parameter in der UI sichtbar sein soll.
	 */
	public istSichtbar: string = "true";

	/**
	 * Der Typ der UI-Komponente (z.B. 'checkbox', 'input', 'select', 'textarea', 'numberpicker', 'datepicker').
	 */
	public komponentenTyp: number = ReportingUIKomponentenTyp.UNDEFINED.getId();

	/**
	 * Die Anzahl der Grid-Spalten, die der Parameter in der UI einnehmen soll.
	 */
	public spaltenAnzahl: number = 1;


	/**
	 * Konstruktor für die Klasse.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingVorlageParameter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingVorlageParameter'].includes(name);
	}

	public static readonly class = new Class<ReportingVorlageParameter>('de.svws_nrw.core.data.reporting.ReportingVorlageParameter');

	public static transpilerFromJSON(json: string): ReportingVorlageParameter {
		const obj = JSON.parse(json) as Partial<ReportingVorlageParameter>;
		const result = new ReportingVorlageParameter();
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
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.komponentenTyp === undefined)
			throw new Error('invalid json format, missing attribute komponentenTyp');
		result.komponentenTyp = obj.komponentenTyp;
		if (obj.spaltenAnzahl === undefined)
			throw new Error('invalid json format, missing attribute spaltenAnzahl');
		result.spaltenAnzahl = obj.spaltenAnzahl;
		return result;
	}

	public static transpilerToJSON(obj: ReportingVorlageParameter): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		result += '"istSichtbar" : ' + JSON.stringify(obj.istSichtbar) + ',';
		result += '"komponentenTyp" : ' + obj.komponentenTyp.toString() + ',';
		result += '"spaltenAnzahl" : ' + obj.spaltenAnzahl.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingVorlageParameter>): string {
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
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + JSON.stringify(obj.istSichtbar) + ',';
		}
		if (obj.komponentenTyp !== undefined) {
			result += '"komponentenTyp" : ' + obj.komponentenTyp.toString() + ',';
		}
		if (obj.spaltenAnzahl !== undefined) {
			result += '"spaltenAnzahl" : ' + obj.spaltenAnzahl.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingVorlageParameter(obj: unknown): ReportingVorlageParameter {
	return obj as ReportingVorlageParameter;
}
