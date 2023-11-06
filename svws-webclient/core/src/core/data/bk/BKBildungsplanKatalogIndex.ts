import { JavaObject } from '../../../java/lang/JavaObject';
import { BKBildungsplanKatalogEintrag } from '../../../core/data/bk/BKBildungsplanKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class BKBildungsplanKatalogIndex extends JavaObject {

	/**
	 * Der Index (Schulgliederung) für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen
	 */
	public index : number = 0;

	/**
	 * Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public lehrplaene : List<BKBildungsplanKatalogEintrag> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKBildungsplanKatalogIndex'].includes(name);
	}

	public static transpilerFromJSON(json : string): BKBildungsplanKatalogIndex {
		const obj = JSON.parse(json);
		const result = new BKBildungsplanKatalogIndex();
		if (typeof obj.index === "undefined")
			 throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if ((obj.lehrplaene !== undefined) && (obj.lehrplaene !== null)) {
			for (const elem of obj.lehrplaene) {
				result.lehrplaene?.add(BKBildungsplanKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKBildungsplanKatalogIndex) : string {
		let result = '{';
		result += '"index" : ' + obj.index + ',';
		result += '"version" : ' + obj.version + ',';
		if (!obj.lehrplaene) {
			result += '"lehrplaene" : []';
		} else {
			result += '"lehrplaene" : [ ';
			for (let i = 0; i < obj.lehrplaene.size(); i++) {
				const elem = obj.lehrplaene.get(i);
				result += BKBildungsplanKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.lehrplaene.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKBildungsplanKatalogIndex>) : string {
		let result = '{';
		if (typeof obj.index !== "undefined") {
			result += '"index" : ' + obj.index + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + obj.version + ',';
		}
		if (typeof obj.lehrplaene !== "undefined") {
			if (!obj.lehrplaene) {
				result += '"lehrplaene" : []';
			} else {
				result += '"lehrplaene" : [ ';
				for (let i = 0; i < obj.lehrplaene.size(); i++) {
					const elem = obj.lehrplaene.get(i);
					result += BKBildungsplanKatalogEintrag.transpilerToJSON(elem);
					if (i < obj.lehrplaene.size() - 1)
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

export function cast_de_svws_nrw_core_data_bk_BKBildungsplanKatalogIndex(obj : unknown) : BKBildungsplanKatalogIndex {
	return obj as BKBildungsplanKatalogIndex;
}
