import { JavaObject } from '../../../java/lang/JavaObject';
import { BKBildungsplanKatalogEintrag } from '../../../core/data/bk/BKBildungsplanKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BKBildungsplanKatalog extends JavaObject {

	/**
	 * Die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public lehrplaene : List<BKBildungsplanKatalogEintrag> = new ArrayList<BKBildungsplanKatalogEintrag>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKBildungsplanKatalog';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKBildungsplanKatalog'].includes(name);
	}

	public static class = new Class<BKBildungsplanKatalog>('de.svws_nrw.core.data.bk.BKBildungsplanKatalog');

	public static transpilerFromJSON(json : string): BKBildungsplanKatalog {
		const obj = JSON.parse(json) as Partial<BKBildungsplanKatalog>;
		const result = new BKBildungsplanKatalog();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.lehrplaene !== undefined) {
			for (const elem of obj.lehrplaene) {
				result.lehrplaene.add(BKBildungsplanKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKBildungsplanKatalog) : string {
		let result = '{';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"lehrplaene" : [ ';
		for (let i = 0; i < obj.lehrplaene.size(); i++) {
			const elem = obj.lehrplaene.get(i);
			result += BKBildungsplanKatalogEintrag.transpilerToJSON(elem);
			if (i < obj.lehrplaene.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKBildungsplanKatalog>) : string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.lehrplaene !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_bk_BKBildungsplanKatalog(obj : unknown) : BKBildungsplanKatalog {
	return obj as BKBildungsplanKatalog;
}
