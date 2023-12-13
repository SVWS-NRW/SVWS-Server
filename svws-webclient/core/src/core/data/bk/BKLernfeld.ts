import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class BKLernfeld extends JavaObject {

	/**
	 * Die Nummer des Lernfelds.
	 */
	public nummer : number = -1;

	/**
	 * Die Bezeichnung des Lernfelds
	 */
	public bezeichnung : string = "";

	/**
	 * Die Bündelfächer, denen das Lernfeld zugeordnet ist.
	 */
	public buendelfaecher : List<string> = new ArrayList();

	/**
	 * Das Ausbildungsjahr, in dem das Lernfeld unterrichtet wird.
	 */
	public ausbildungsjahr : number = -1;

	/**
	 * Der Zeitrichtwert, der den zeitlichen Umfang des Lernfelds in 45Min. Einheiten angibt.
	 */
	public zeitrichtwert : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKLernfeld';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKLernfeld'].includes(name);
	}

	public static transpilerFromJSON(json : string): BKLernfeld {
		const obj = JSON.parse(json);
		const result = new BKLernfeld();
		if (typeof obj.nummer === "undefined")
			 throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if ((obj.buendelfaecher !== undefined) && (obj.buendelfaecher !== null)) {
			for (const elem of obj.buendelfaecher) {
				result.buendelfaecher?.add(elem);
			}
		}
		if (typeof obj.ausbildungsjahr === "undefined")
			 throw new Error('invalid json format, missing attribute ausbildungsjahr');
		result.ausbildungsjahr = obj.ausbildungsjahr;
		if (typeof obj.zeitrichtwert === "undefined")
			 throw new Error('invalid json format, missing attribute zeitrichtwert');
		result.zeitrichtwert = obj.zeitrichtwert;
		return result;
	}

	public static transpilerToJSON(obj : BKLernfeld) : string {
		let result = '{';
		result += '"nummer" : ' + obj.nummer + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		if (!obj.buendelfaecher) {
			result += '"buendelfaecher" : []';
		} else {
			result += '"buendelfaecher" : [ ';
			for (let i = 0; i < obj.buendelfaecher.size(); i++) {
				const elem = obj.buendelfaecher.get(i);
				result += '"' + elem + '"';
				if (i < obj.buendelfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"ausbildungsjahr" : ' + obj.ausbildungsjahr + ',';
		result += '"zeitrichtwert" : ' + obj.zeitrichtwert + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKLernfeld>) : string {
		let result = '{';
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + obj.nummer + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.buendelfaecher !== "undefined") {
			if (!obj.buendelfaecher) {
				result += '"buendelfaecher" : []';
			} else {
				result += '"buendelfaecher" : [ ';
				for (let i = 0; i < obj.buendelfaecher.size(); i++) {
					const elem = obj.buendelfaecher.get(i);
					result += '"' + elem + '"';
					if (i < obj.buendelfaecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.ausbildungsjahr !== "undefined") {
			result += '"ausbildungsjahr" : ' + obj.ausbildungsjahr + ',';
		}
		if (typeof obj.zeitrichtwert !== "undefined") {
			result += '"zeitrichtwert" : ' + obj.zeitrichtwert + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_BKLernfeld(obj : unknown) : BKLernfeld {
	return obj as BKLernfeld;
}
