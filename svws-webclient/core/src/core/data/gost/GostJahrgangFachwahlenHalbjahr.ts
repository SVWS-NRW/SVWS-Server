import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostJahrgangFachwahlenHalbjahr extends JavaObject {

	/**
	 * Die Fachwahlen für den Abiturbereich
	 */
	public fachwahlen : List<GostFachwahl> = new ArrayList<GostFachwahl>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr'].includes(name);
	}

	public static class = new Class<GostJahrgangFachwahlenHalbjahr>('de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr');

	public static transpilerFromJSON(json : string): GostJahrgangFachwahlenHalbjahr {
		const obj = JSON.parse(json) as Partial<GostJahrgangFachwahlenHalbjahr>;
		const result = new GostJahrgangFachwahlenHalbjahr();
		if (obj.fachwahlen !== undefined) {
			for (const elem of obj.fachwahlen) {
				result.fachwahlen.add(GostFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangFachwahlenHalbjahr) : string {
		let result = '{';
		result += '"fachwahlen" : [ ';
		for (let i = 0; i < obj.fachwahlen.size(); i++) {
			const elem = obj.fachwahlen.get(i);
			result += GostFachwahl.transpilerToJSON(elem);
			if (i < obj.fachwahlen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangFachwahlenHalbjahr>) : string {
		let result = '{';
		if (obj.fachwahlen !== undefined) {
			result += '"fachwahlen" : [ ';
			for (let i = 0; i < obj.fachwahlen.size(); i++) {
				const elem = obj.fachwahlen.get(i);
				result += GostFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostJahrgangFachwahlenHalbjahr(obj : unknown) : GostJahrgangFachwahlenHalbjahr {
	return obj as GostJahrgangFachwahlenHalbjahr;
}
