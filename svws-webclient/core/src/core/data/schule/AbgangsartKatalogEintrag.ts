import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AbgangsartKatalogDaten } from '../../../core/data/schule/AbgangsartKatalogDaten';

export class AbgangsartKatalogEintrag extends JavaObject {

	/**
	 * Das eindeutige Kürzel des Katalog-Eintrags.
	 */
	public kuerzel : string = "";

	/**
	 * Die Historie des Katalog-Eintrags.
	 */
	public historie : List<AbgangsartKatalogDaten> = new ArrayList<AbgangsartKatalogDaten>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AbgangsartKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.AbgangsartKatalogEintrag'].includes(name);
	}

	public static class = new Class<AbgangsartKatalogEintrag>('de.svws_nrw.core.data.schule.AbgangsartKatalogEintrag');

	public static transpilerFromJSON(json : string): AbgangsartKatalogEintrag {
		const obj = JSON.parse(json) as Partial<AbgangsartKatalogEintrag>;
		const result = new AbgangsartKatalogEintrag();
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.historie !== undefined) {
			for (const elem of obj.historie) {
				result.historie.add(AbgangsartKatalogDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalogEintrag) : string {
		let result = '{';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"historie" : [ ';
		for (let i = 0; i < obj.historie.size(); i++) {
			const elem = obj.historie.get(i);
			result += AbgangsartKatalogDaten.transpilerToJSON(elem);
			if (i < obj.historie.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalogEintrag>) : string {
		let result = '{';
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.historie !== undefined) {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += AbgangsartKatalogDaten.transpilerToJSON(elem);
				if (i < obj.historie.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_AbgangsartKatalogEintrag(obj : unknown) : AbgangsartKatalogEintrag {
	return obj as AbgangsartKatalogEintrag;
}
