import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SchildReportingAttributTyp, cast_de_nrw_schule_svws_core_types_schild3_SchildReportingAttributTyp } from '../../../core/types/schild3/SchildReportingAttributTyp';

export class SchildReportingDatenquelleAttribut extends JavaObject {

	public name : String = "";

	public typ : String = "";

	public beschreibung : String = "";


	/**
	 * Erstellt einen Attribut mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param name           der Name des Attributs
	 * @param typ            der Typ des Attributs
	 * @param beschreibung   die textuelle Beschreibung der Herkunft
	 */
	public constructor(name : String, typ : SchildReportingAttributTyp, beschreibung : String);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : String, __param1? : SchildReportingAttributTyp, __param2? : String) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof String) || (typeof __param0 === "string"))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp')))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")))) {
			let name : String = __param0;
			let typ : SchildReportingAttributTyp = cast_de_nrw_schule_svws_core_types_schild3_SchildReportingAttributTyp(__param1);
			let beschreibung : String = __param2;
			this.name = name;
			this.typ = typ.toString();
			this.beschreibung = beschreibung;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.SchildReportingDatenquelleAttribut'].includes(name);
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
