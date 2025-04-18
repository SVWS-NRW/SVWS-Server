import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { BerufskollegFachklassenKatalogDaten } from '../../../core/data/schule/BerufskollegFachklassenKatalogDaten';

export class BerufskollegFachklassenKatalogEintrag extends JavaObject {

	/**
	 * Der Fachklassenschlüssel.
	 */
	public schluessel : string = "";

	/**
	 * Der Fachklassenschlüssel - Teil 2.
	 */
	public schluessel2 : string = "";

	/**
	 * Die Historie des Katalog-Eintrags.
	 */
	public historie : List<BerufskollegFachklassenKatalogDaten> = new ArrayList<BerufskollegFachklassenKatalogDaten>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag'].includes(name);
	}

	public static class = new Class<BerufskollegFachklassenKatalogEintrag>('de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag');

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BerufskollegFachklassenKatalogEintrag>;
		const result = new BerufskollegFachklassenKatalogEintrag();
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.schluessel2 === undefined)
			throw new Error('invalid json format, missing attribute schluessel2');
		result.schluessel2 = obj.schluessel2;
		if (obj.historie !== undefined) {
			for (const elem of obj.historie) {
				result.historie.add(BerufskollegFachklassenKatalogDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogEintrag) : string {
		let result = '{';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"schluessel2" : ' + JSON.stringify(obj.schluessel2) + ',';
		result += '"historie" : [ ';
		for (let i = 0; i < obj.historie.size(); i++) {
			const elem = obj.historie.get(i);
			result += BerufskollegFachklassenKatalogDaten.transpilerToJSON(elem);
			if (i < obj.historie.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogEintrag>) : string {
		let result = '{';
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.schluessel2 !== undefined) {
			result += '"schluessel2" : ' + JSON.stringify(obj.schluessel2) + ',';
		}
		if (obj.historie !== undefined) {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += BerufskollegFachklassenKatalogDaten.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_schule_BerufskollegFachklassenKatalogEintrag(obj : unknown) : BerufskollegFachklassenKatalogEintrag {
	return obj as BerufskollegFachklassenKatalogEintrag;
}
