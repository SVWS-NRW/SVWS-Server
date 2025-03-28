import { JavaObject } from '../../../java/lang/JavaObject';
import { AbgangsartKatalogEintrag } from '../../../core/data/schule/AbgangsartKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class AbgangsartKatalog extends JavaObject {

	/**
	 * Die Version des Katalog. Diese wird bei Änderungen am Katalog um 1 erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public eintraege : List<AbgangsartKatalogEintrag> = new ArrayList<AbgangsartKatalogEintrag>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AbgangsartKatalog';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.AbgangsartKatalog'].includes(name);
	}

	public static class = new Class<AbgangsartKatalog>('de.svws_nrw.core.data.schule.AbgangsartKatalog');

	public static transpilerFromJSON(json : string): AbgangsartKatalog {
		const obj = JSON.parse(json) as Partial<AbgangsartKatalog>;
		const result = new AbgangsartKatalog();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.eintraege !== undefined) {
			for (const elem of obj.eintraege) {
				result.eintraege.add(AbgangsartKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalog) : string {
		let result = '{';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"eintraege" : [ ';
		for (let i = 0; i < obj.eintraege.size(); i++) {
			const elem = obj.eintraege.get(i);
			result += AbgangsartKatalogEintrag.transpilerToJSON(elem);
			if (i < obj.eintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalog>) : string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.eintraege !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_schule_AbgangsartKatalog(obj : unknown) : AbgangsartKatalog {
	return obj as AbgangsartKatalog;
}
