import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerSchulbesuchMerkmal extends JavaObject {

	public id : number = 0;

	public datumVon : String | null = null;

	public datumBis : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchMerkmal'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchMerkmal {
		const obj = JSON.parse(json);
		const result = new SchuelerSchulbesuchMerkmal();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.datumVon = typeof obj.datumVon === "undefined" ? null : obj.datumVon === null ? null : String(obj.datumVon);
		result.datumBis = typeof obj.datumBis === "undefined" ? null : obj.datumBis === null ? null : String(obj.datumBis);
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchMerkmal) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : '"' + obj.datumVon.valueOf() + '"') + ',';
		result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : '"' + obj.datumBis.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchMerkmal>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.datumVon !== "undefined") {
			result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : '"' + obj.datumVon.valueOf() + '"') + ',';
		}
		if (typeof obj.datumBis !== "undefined") {
			result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : '"' + obj.datumBis.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchMerkmal(obj : unknown) : SchuelerSchulbesuchMerkmal {
	return obj as SchuelerSchulbesuchMerkmal;
}
