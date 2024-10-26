import { JavaObject } from '../../../java/lang/JavaObject';
import { BKBildungsplan, cast_de_svws_nrw_core_data_bk_BKBildungsplan } from '../../../core/data/bk/BKBildungsplan';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BKBildungsplanKatalogEintrag extends JavaObject {

	/**
	 * Der Index (Schulgliederung) für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen
	 */
	public index : number = -1;

	/**
	 * Der Fachklassenschlüssel.
	 */
	public schluessel : string = "";

	/**
	 * Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public historie : List<BKBildungsplan> = new ArrayList<BKBildungsplan>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKBildungsplanKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKBildungsplanKatalogEintrag'].includes(name);
	}

	public static class = new Class<BKBildungsplanKatalogEintrag>('de.svws_nrw.core.data.bk.BKBildungsplanKatalogEintrag');

	public static transpilerFromJSON(json : string): BKBildungsplanKatalogEintrag {
		const obj = JSON.parse(json) as Partial<BKBildungsplanKatalogEintrag>;
		const result = new BKBildungsplanKatalogEintrag();
		if (obj.index === undefined)
			throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.historie !== undefined) {
			for (const elem of obj.historie) {
				result.historie.add(BKBildungsplan.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKBildungsplanKatalogEintrag) : string {
		let result = '{';
		result += '"index" : ' + obj.index + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"historie" : [ ';
		for (let i = 0; i < obj.historie.size(); i++) {
			const elem = obj.historie.get(i);
			result += BKBildungsplan.transpilerToJSON(elem);
			if (i < obj.historie.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKBildungsplanKatalogEintrag>) : string {
		let result = '{';
		if (obj.index !== undefined) {
			result += '"index" : ' + obj.index + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.historie !== undefined) {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += BKBildungsplan.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_bk_BKBildungsplanKatalogEintrag(obj : unknown) : BKBildungsplanKatalogEintrag {
	return obj as BKBildungsplanKatalogEintrag;
}
