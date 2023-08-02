import { JavaObject } from '../../../java/lang/JavaObject';

export class SchildReportingDatenquelleAttribut extends JavaObject {

	/**
	 * Der Name des Attributs
	 */
	public name : string = "";

	/**
	 * Der Typ des Attributs
	 */
	public typ : string = "";

	/**
	 * Die Beschreibung des Attributs
	 */
	public beschreibung : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.SchildReportingDatenquelleAttribut'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingDatenquelleAttribut {
		const obj = JSON.parse(json);
		const result = new SchildReportingDatenquelleAttribut();
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingDatenquelleAttribut) : string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"typ" : ' + JSON.stringify(obj.typ!) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingDatenquelleAttribut>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + JSON.stringify(obj.typ!) + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_SchildReportingDatenquelleAttribut(obj : unknown) : SchildReportingDatenquelleAttribut {
	return obj as SchildReportingDatenquelleAttribut;
}
