import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBeratungslehrer, cast_de_nrw_schule_svws_core_data_gost_GostBeratungslehrer } from './GostBeratungslehrer';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from './GostFach';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Abiturdaten, cast_de_nrw_schule_svws_core_data_gost_Abiturdaten } from './Abiturdaten';

export class GostBelegpruefungsdaten extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird. 
	 */
	public abiturdaten : Abiturdaten | null = null;

	/**
	 * Die Liste der Fächer der gymnasialen Oberstufe, die für die Belegprüfung genutzt werden sollen 
	 */
	public gostFaecher : Vector<GostFach> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBelegpruefungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungsdaten {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungsdaten();
		result.abiturdaten = ((typeof obj.abiturdaten === "undefined") || (obj.abiturdaten === null)) ? null : Abiturdaten.transpilerFromJSON(JSON.stringify(obj.abiturdaten));
		if (!!obj.gostFaecher) {
			for (let elem of obj.gostFaecher) {
				result.gostFaecher?.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungsdaten) : string {
		let result = '{';
		result += '"abiturdaten" : ' + ((!obj.abiturdaten) ? 'null' : Abiturdaten.transpilerToJSON(obj.abiturdaten)) + ',';
		if (!obj.gostFaecher) {
			result += '"gostFaecher" : []';
		} else {
			result += '"gostFaecher" : [ ';
			for (let i : number = 0; i < obj.gostFaecher.size(); i++) {
				let elem = obj.gostFaecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.gostFaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungsdaten>) : string {
		let result = '{';
		if (typeof obj.abiturdaten !== "undefined") {
			result += '"abiturdaten" : ' + ((!obj.abiturdaten) ? 'null' : Abiturdaten.transpilerToJSON(obj.abiturdaten)) + ',';
		}
		if (typeof obj.gostFaecher !== "undefined") {
			if (!obj.gostFaecher) {
				result += '"gostFaecher" : []';
			} else {
				result += '"gostFaecher" : [ ';
				for (let i : number = 0; i < obj.gostFaecher.size(); i++) {
					let elem = obj.gostFaecher.get(i);
					result += GostFach.transpilerToJSON(elem);
					if (i < obj.gostFaecher.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBelegpruefungsdaten(obj : unknown) : GostBelegpruefungsdaten {
	return obj as GostBelegpruefungsdaten;
}
