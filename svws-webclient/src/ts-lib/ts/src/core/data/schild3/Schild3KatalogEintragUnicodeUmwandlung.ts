import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragUnicodeUmwandlung extends JavaObject {

	public ID : Number | null = null;

	public Unicodezeichen : String | null = null;

	public Ersatzzeichen : String | null = null;

	public DecimalZeichen : String | null = null;

	public DecimalErsatzzeichen : String | null = null;

	public Hexzeichen : String | null = null;

	public HexErsatzzeichen : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragUnicodeUmwandlung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragUnicodeUmwandlung {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragUnicodeUmwandlung();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID;
		result.Unicodezeichen = typeof obj.Unicodezeichen === "undefined" ? null : obj.Unicodezeichen;
		result.Ersatzzeichen = typeof obj.Ersatzzeichen === "undefined" ? null : obj.Ersatzzeichen;
		result.DecimalZeichen = typeof obj.DecimalZeichen === "undefined" ? null : obj.DecimalZeichen;
		result.DecimalErsatzzeichen = typeof obj.DecimalErsatzzeichen === "undefined" ? null : obj.DecimalErsatzzeichen;
		result.Hexzeichen = typeof obj.Hexzeichen === "undefined" ? null : obj.Hexzeichen;
		result.HexErsatzzeichen = typeof obj.HexErsatzzeichen === "undefined" ? null : obj.HexErsatzzeichen;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragUnicodeUmwandlung) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		result += '"Unicodezeichen" : ' + ((!obj.Unicodezeichen) ? 'null' : '"' + obj.Unicodezeichen.valueOf() + '"') + ',';
		result += '"Ersatzzeichen" : ' + ((!obj.Ersatzzeichen) ? 'null' : '"' + obj.Ersatzzeichen.valueOf() + '"') + ',';
		result += '"DecimalZeichen" : ' + ((!obj.DecimalZeichen) ? 'null' : '"' + obj.DecimalZeichen.valueOf() + '"') + ',';
		result += '"DecimalErsatzzeichen" : ' + ((!obj.DecimalErsatzzeichen) ? 'null' : '"' + obj.DecimalErsatzzeichen.valueOf() + '"') + ',';
		result += '"Hexzeichen" : ' + ((!obj.Hexzeichen) ? 'null' : '"' + obj.Hexzeichen.valueOf() + '"') + ',';
		result += '"HexErsatzzeichen" : ' + ((!obj.HexErsatzzeichen) ? 'null' : '"' + obj.HexErsatzzeichen.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragUnicodeUmwandlung>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		}
		if (typeof obj.Unicodezeichen !== "undefined") {
			result += '"Unicodezeichen" : ' + ((!obj.Unicodezeichen) ? 'null' : '"' + obj.Unicodezeichen.valueOf() + '"') + ',';
		}
		if (typeof obj.Ersatzzeichen !== "undefined") {
			result += '"Ersatzzeichen" : ' + ((!obj.Ersatzzeichen) ? 'null' : '"' + obj.Ersatzzeichen.valueOf() + '"') + ',';
		}
		if (typeof obj.DecimalZeichen !== "undefined") {
			result += '"DecimalZeichen" : ' + ((!obj.DecimalZeichen) ? 'null' : '"' + obj.DecimalZeichen.valueOf() + '"') + ',';
		}
		if (typeof obj.DecimalErsatzzeichen !== "undefined") {
			result += '"DecimalErsatzzeichen" : ' + ((!obj.DecimalErsatzzeichen) ? 'null' : '"' + obj.DecimalErsatzzeichen.valueOf() + '"') + ',';
		}
		if (typeof obj.Hexzeichen !== "undefined") {
			result += '"Hexzeichen" : ' + ((!obj.Hexzeichen) ? 'null' : '"' + obj.Hexzeichen.valueOf() + '"') + ',';
		}
		if (typeof obj.HexErsatzzeichen !== "undefined") {
			result += '"HexErsatzzeichen" : ' + ((!obj.HexErsatzzeichen) ? 'null' : '"' + obj.HexErsatzzeichen.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragUnicodeUmwandlung(obj : unknown) : Schild3KatalogEintragUnicodeUmwandlung {
	return obj as Schild3KatalogEintragUnicodeUmwandlung;
}
