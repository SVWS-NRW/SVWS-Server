import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchildReportingDatenquelleAttribut extends JavaObject {

	public name : String = "";

	public typ : String = "";

	public beschreibung : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelleAttribut'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingDatenquelleAttribut {
		const obj = JSON.parse(json);
		const result = new SchildReportingDatenquelleAttribut();
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = String(obj.name);
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = String(obj.typ);
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = String(obj.beschreibung);
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingDatenquelleAttribut) : string {
		let result = '{';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"typ" : ' + '"' + obj.typ.valueOf() + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingDatenquelleAttribut>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + '"' + obj.typ.valueOf() + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_SchildReportingDatenquelleAttribut(obj : unknown) : SchildReportingDatenquelleAttribut {
	return obj as SchildReportingDatenquelleAttribut;
}
