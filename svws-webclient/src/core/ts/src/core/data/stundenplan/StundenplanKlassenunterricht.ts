import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class StundenplanKlassenunterricht extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public idKlasse : number = -1;

	/**
	 * Die ID des Faches.
	 */
	public idFach : number = -1;

	/**
	 * Die Bezeichnung des Klassenunterrichtes.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Wochenstunden, welche dem Klassenunterricht zugeordnet sind
	 */
	public wochenstunden : number = 0;

	/**
	 * Die Liste der IDs der Schienen, denen der Klassenunterricht zugeordnet ist.
	 */
	public schienen : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanKlassenunterricht {
		const obj = JSON.parse(json);
		const result = new StundenplanKlassenunterricht();
		if (typeof obj.idKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if ((obj.schienen !== undefined) && (obj.schienen !== null)) {
			for (const elem of obj.schienen) {
				result.schienen?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKlassenunterricht) : string {
		let result = '{';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += elem;
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKlassenunterricht>) : string {
		let result = '{';
		if (typeof obj.idKlasse !== "undefined") {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i = 0; i < obj.schienen.size(); i++) {
					const elem = obj.schienen.get(i);
					result += elem;
					if (i < obj.schienen.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKlassenunterricht(obj : unknown) : StundenplanKlassenunterricht {
	return obj as StundenplanKlassenunterricht;
}
