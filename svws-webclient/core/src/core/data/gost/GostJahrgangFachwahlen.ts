import { JavaObject } from '../../../java/lang/JavaObject';
import { GostJahrgangFachwahlenHalbjahr } from '../../../core/data/gost/GostJahrgangFachwahlenHalbjahr';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../java/lang/Class';

export class GostJahrgangFachwahlen extends JavaObject {

	/**
	 * Die Fachwahlen der einzelnen Halbjahre der gymnasialen Oberstufe
	 */
	public halbjahr : Array<GostJahrgangFachwahlenHalbjahr | null> = Array(GostHalbjahr.maxHalbjahre).fill(null);

	/**
	 * Die Fachwahlen für den Abiturbereich
	 */
	public abitur : GostJahrgangFachwahlenHalbjahr = new GostJahrgangFachwahlenHalbjahr();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostJahrgangFachwahlen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostJahrgangFachwahlen'].includes(name);
	}

	public static class = new Class<GostJahrgangFachwahlen>('de.svws_nrw.core.data.gost.GostJahrgangFachwahlen');

	public static transpilerFromJSON(json : string): GostJahrgangFachwahlen {
		const obj = JSON.parse(json) as Partial<GostJahrgangFachwahlen>;
		const result = new GostJahrgangFachwahlen();
		if (obj.halbjahr !== undefined) {
			for (let i = 0; i < obj.halbjahr.length; i++) {
				result.halbjahr[i] = obj.halbjahr[i] == null ? null : (GostJahrgangFachwahlenHalbjahr.transpilerFromJSON(JSON.stringify(obj.halbjahr[i])));
			}
		}
		if (obj.abitur === undefined)
			throw new Error('invalid json format, missing attribute abitur');
		result.abitur = GostJahrgangFachwahlenHalbjahr.transpilerFromJSON(JSON.stringify(obj.abitur));
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangFachwahlen) : string {
		let result = '{';
		result += '"halbjahr" : [ ';
		for (let i = 0; i < obj.halbjahr.length; i++) {
			const elem = obj.halbjahr[i];
			result += (elem === null) ? null : GostJahrgangFachwahlenHalbjahr.transpilerToJSON(elem);
			if (i < obj.halbjahr.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"abitur" : ' + GostJahrgangFachwahlenHalbjahr.transpilerToJSON(obj.abitur) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangFachwahlen>) : string {
		let result = '{';
		if (obj.halbjahr !== undefined) {
			const a = obj.halbjahr;
			result += '"halbjahr" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += (elem === null) ? null : GostJahrgangFachwahlenHalbjahr.transpilerToJSON(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.abitur !== undefined) {
			result += '"abitur" : ' + GostJahrgangFachwahlenHalbjahr.transpilerToJSON(obj.abitur) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostJahrgangFachwahlen(obj : unknown) : GostJahrgangFachwahlen {
	return obj as GostJahrgangFachwahlen;
}
