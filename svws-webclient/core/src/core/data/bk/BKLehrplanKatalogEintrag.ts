import { JavaObject } from '../../../java/lang/JavaObject';
import { BKLehrplan, cast_de_svws_nrw_core_data_bk_BKLehrplan } from '../../../core/data/bk/BKLehrplan';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class BKLehrplanKatalogEintrag extends JavaObject {

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
	public historie : List<BKLehrplan> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKLehrplanKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BKLehrplanKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new BKLehrplanKatalogEintrag();
		if (typeof obj.index === "undefined")
			 throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (typeof obj.schluessel === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if ((obj.historie !== undefined) && (obj.historie !== null)) {
			for (const elem of obj.historie) {
				result.historie?.add(BKLehrplan.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKLehrplanKatalogEintrag) : string {
		let result = '{';
		result += '"index" : ' + obj.index! + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result += '"version" : ' + obj.version + ',';
		if (!obj.historie) {
			result += '"historie" : []';
		} else {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += BKLehrplan.transpilerToJSON(elem);
				if (i < obj.historie.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKLehrplanKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.index !== "undefined") {
			result += '"index" : ' + obj.index + ',';
		}
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + obj.version + ',';
		}
		if (typeof obj.historie !== "undefined") {
			if (!obj.historie) {
				result += '"historie" : []';
			} else {
				result += '"historie" : [ ';
				for (let i = 0; i < obj.historie.size(); i++) {
					const elem = obj.historie.get(i);
					result += BKLehrplan.transpilerToJSON(elem);
					if (i < obj.historie.size() - 1)
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

export function cast_de_svws_nrw_core_data_bk_BKLehrplanKatalogEintrag(obj : unknown) : BKLehrplanKatalogEintrag {
	return obj as BKLehrplanKatalogEintrag;
}
