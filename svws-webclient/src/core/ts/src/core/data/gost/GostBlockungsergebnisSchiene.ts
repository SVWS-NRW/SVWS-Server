import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBlockungsergebnisSchiene extends JavaObject {

	/**
	 * Die ID der Schiene
	 */
	public id : number = -1;

	/**
	 * Eine Liste der Kurse, welche dieser Schiene zugeordnet sind.
	 */
	public readonly kurse : Vector<GostBlockungsergebnisKurs> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisSchiene {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisSchiene();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(GostBlockungsergebnisKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisSchiene) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i : number = 0; i < obj.kurse.size(); i++) {
				let elem = obj.kurse.get(i);
				result += GostBlockungsergebnisKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisSchiene>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i : number = 0; i < obj.kurse.size(); i++) {
					let elem = obj.kurse.get(i);
					result += GostBlockungsergebnisKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchiene(obj : unknown) : GostBlockungsergebnisSchiene {
	return obj as GostBlockungsergebnisSchiene;
}
