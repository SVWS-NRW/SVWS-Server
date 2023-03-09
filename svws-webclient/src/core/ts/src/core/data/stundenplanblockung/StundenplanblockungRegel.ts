import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungRegel extends JavaObject {

	/**
	 * Die Datenbank-ID der Regel. 
	 */
	public id : number = 0;

	/**
	 * Der Type der Regel - siehe {@link GostKursblockungRegelTyp} 
	 */
	public typ : number = -1;

	/**
	 * Eine Liste der Regel-Parameter 
	 */
	public parameter : Vector<number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungRegel'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungRegel {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungRegel();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (!!obj.parameter) {
			for (let elem of obj.parameter) {
				result.parameter?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungRegel) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"typ" : ' + obj.typ + ',';
		if (!obj.parameter) {
			result += '"parameter" : []';
		} else {
			result += '"parameter" : [ ';
			for (let i : number = 0; i < obj.parameter.size(); i++) {
				let elem = obj.parameter.get(i);
				result += elem;
				if (i < obj.parameter.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungRegel>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		if (typeof obj.parameter !== "undefined") {
			if (!obj.parameter) {
				result += '"parameter" : []';
			} else {
				result += '"parameter" : [ ';
				for (let i : number = 0; i < obj.parameter.size(); i++) {
					let elem = obj.parameter.get(i);
					result += elem;
					if (i < obj.parameter.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungRegel(obj : unknown) : StundenplanblockungRegel {
	return obj as StundenplanblockungRegel;
}
