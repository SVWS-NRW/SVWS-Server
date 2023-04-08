import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';

export class GostBlockungsergebnisSchiene extends JavaObject {

	/**
	 * Die ID der Schiene
	 */
	public id : number = -1;

	/**
	 * Eine Liste der Kurse, welche dieser Schiene zugeordnet sind.
	 */
	public readonly kurse : ArrayList<GostBlockungsergebnisKurs> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisSchiene {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisSchiene();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
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
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
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
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
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

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisSchiene(obj : unknown) : GostBlockungsergebnisSchiene {
	return obj as GostBlockungsergebnisSchiene;
}
