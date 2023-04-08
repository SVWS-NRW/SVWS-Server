import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { ArrayList } from '../../../java/util/ArrayList';
import { Abiturdaten } from '../../../core/data/gost/Abiturdaten';

export class GostBelegpruefungsdaten extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird.
	 */
	public abiturdaten : Abiturdaten | null = null;

	/**
	 * Die Liste der Fächer der gymnasialen Oberstufe, die für die Belegprüfung genutzt werden sollen
	 */
	public gostFaecher : ArrayList<GostFach> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBelegpruefungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungsdaten {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungsdaten();
		result.abiturdaten = ((typeof obj.abiturdaten === "undefined") || (obj.abiturdaten === null)) ? null : Abiturdaten.transpilerFromJSON(JSON.stringify(obj.abiturdaten));
		if ((obj.gostFaecher !== undefined) && (obj.gostFaecher !== null)) {
			for (const elem of obj.gostFaecher) {
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
			for (let i = 0; i < obj.gostFaecher.size(); i++) {
				const elem = obj.gostFaecher.get(i);
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
				for (let i = 0; i < obj.gostFaecher.size(); i++) {
					const elem = obj.gostFaecher.get(i);
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

export function cast_de_svws_nrw_core_data_gost_GostBelegpruefungsdaten(obj : unknown) : GostBelegpruefungsdaten {
	return obj as GostBelegpruefungsdaten;
}
