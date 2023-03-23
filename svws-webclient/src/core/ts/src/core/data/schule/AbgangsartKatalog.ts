import { JavaObject } from '../../../java/lang/JavaObject';
import { AbgangsartKatalogEintrag } from '../../../core/data/schule/AbgangsartKatalogEintrag';
import { List } from '../../../java/util/List';
import { Vector } from '../../../java/util/Vector';

export class AbgangsartKatalog extends JavaObject {

	/**
	 * Die Version des Katalog. Diese wird bei Änderungen am Katalog um 1 erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public eintraege : List<AbgangsartKatalogEintrag> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.AbgangsartKatalog'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbgangsartKatalog {
		const obj = JSON.parse(json);
		const result = new AbgangsartKatalog();
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if ((obj.eintraege !== undefined) && (obj.eintraege !== null)) {
			for (const elem of obj.eintraege) {
				result.eintraege?.add(AbgangsartKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalog) : string {
		let result = '{';
		result += '"version" : ' + obj.version + ',';
		if (!obj.eintraege) {
			result += '"eintraege" : []';
		} else {
			result += '"eintraege" : [ ';
			for (let i = 0; i < obj.eintraege.size(); i++) {
				const elem = obj.eintraege.get(i);
				result += AbgangsartKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.eintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalog>) : string {
		let result = '{';
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + obj.version + ',';
		}
		if (typeof obj.eintraege !== "undefined") {
			if (!obj.eintraege) {
				result += '"eintraege" : []';
			} else {
				result += '"eintraege" : [ ';
				for (let i = 0; i < obj.eintraege.size(); i++) {
					const elem = obj.eintraege.get(i);
					result += AbgangsartKatalogEintrag.transpilerToJSON(elem);
					if (i < obj.eintraege.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalog(obj : unknown) : AbgangsartKatalog {
	return obj as AbgangsartKatalog;
}
