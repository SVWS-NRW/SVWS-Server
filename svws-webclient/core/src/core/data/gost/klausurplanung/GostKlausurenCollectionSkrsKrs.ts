import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostKlausurraumstunde';
import { GostSchuelerklausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurraumstunde';

export class GostKlausurenCollectionSkrsKrs extends JavaObject {

	/**
	 * Die ID der Schülerklausur.
	 */
	public raumstunden : List<GostKlausurraumstunde> = new ArrayList();

	/**
	 * Die ID der Schülerklausur.
	 */
	public raumstundenGeloescht : List<GostKlausurraumstunde> = new ArrayList();

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public skRaumstunden : List<GostSchuelerklausurraumstunde> = new ArrayList();

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public idKlausurraum : number = -1;

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public idsSchuelerklausuren : List<number> = new ArrayList();

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public kursKlausurPatched : GostKursklausur | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionSkrsKrs {
		const obj = JSON.parse(json);
		const result = new GostKlausurenCollectionSkrsKrs();
		if ((obj.raumstunden !== undefined) && (obj.raumstunden !== null)) {
			for (const elem of obj.raumstunden) {
				result.raumstunden?.add(GostKlausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raumstundenGeloescht !== undefined) && (obj.raumstundenGeloescht !== null)) {
			for (const elem of obj.raumstundenGeloescht) {
				result.raumstundenGeloescht?.add(GostKlausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.skRaumstunden !== undefined) && (obj.skRaumstunden !== null)) {
			for (const elem of obj.skRaumstunden) {
				result.skRaumstunden?.add(GostSchuelerklausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.idKlausurraum === "undefined")
			 throw new Error('invalid json format, missing attribute idKlausurraum');
		result.idKlausurraum = obj.idKlausurraum;
		if ((obj.idsSchuelerklausuren !== undefined) && (obj.idsSchuelerklausuren !== null)) {
			for (const elem of obj.idsSchuelerklausuren) {
				result.idsSchuelerklausuren?.add(elem);
			}
		}
		result.kursKlausurPatched = ((typeof obj.kursKlausurPatched === "undefined") || (obj.kursKlausurPatched === null)) ? null : GostKursklausur.transpilerFromJSON(JSON.stringify(obj.kursKlausurPatched));
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionSkrsKrs) : string {
		let result = '{';
		if (!obj.raumstunden) {
			result += '"raumstunden" : []';
		} else {
			result += '"raumstunden" : [ ';
			for (let i = 0; i < obj.raumstunden.size(); i++) {
				const elem = obj.raumstunden.get(i);
				result += GostKlausurraumstunde.transpilerToJSON(elem);
				if (i < obj.raumstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.raumstundenGeloescht) {
			result += '"raumstundenGeloescht" : []';
		} else {
			result += '"raumstundenGeloescht" : [ ';
			for (let i = 0; i < obj.raumstundenGeloescht.size(); i++) {
				const elem = obj.raumstundenGeloescht.get(i);
				result += GostKlausurraumstunde.transpilerToJSON(elem);
				if (i < obj.raumstundenGeloescht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.skRaumstunden) {
			result += '"skRaumstunden" : []';
		} else {
			result += '"skRaumstunden" : [ ';
			for (let i = 0; i < obj.skRaumstunden.size(); i++) {
				const elem = obj.skRaumstunden.get(i);
				result += GostSchuelerklausurraumstunde.transpilerToJSON(elem);
				if (i < obj.skRaumstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idKlausurraum" : ' + obj.idKlausurraum + ',';
		if (!obj.idsSchuelerklausuren) {
			result += '"idsSchuelerklausuren" : []';
		} else {
			result += '"idsSchuelerklausuren" : [ ';
			for (let i = 0; i < obj.idsSchuelerklausuren.size(); i++) {
				const elem = obj.idsSchuelerklausuren.get(i);
				result += elem;
				if (i < obj.idsSchuelerklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"kursKlausurPatched" : ' + ((!obj.kursKlausurPatched) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursKlausurPatched)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionSkrsKrs>) : string {
		let result = '{';
		if (typeof obj.raumstunden !== "undefined") {
			if (!obj.raumstunden) {
				result += '"raumstunden" : []';
			} else {
				result += '"raumstunden" : [ ';
				for (let i = 0; i < obj.raumstunden.size(); i++) {
					const elem = obj.raumstunden.get(i);
					result += GostKlausurraumstunde.transpilerToJSON(elem);
					if (i < obj.raumstunden.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.raumstundenGeloescht !== "undefined") {
			if (!obj.raumstundenGeloescht) {
				result += '"raumstundenGeloescht" : []';
			} else {
				result += '"raumstundenGeloescht" : [ ';
				for (let i = 0; i < obj.raumstundenGeloescht.size(); i++) {
					const elem = obj.raumstundenGeloescht.get(i);
					result += GostKlausurraumstunde.transpilerToJSON(elem);
					if (i < obj.raumstundenGeloescht.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.skRaumstunden !== "undefined") {
			if (!obj.skRaumstunden) {
				result += '"skRaumstunden" : []';
			} else {
				result += '"skRaumstunden" : [ ';
				for (let i = 0; i < obj.skRaumstunden.size(); i++) {
					const elem = obj.skRaumstunden.get(i);
					result += GostSchuelerklausurraumstunde.transpilerToJSON(elem);
					if (i < obj.skRaumstunden.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.idKlausurraum !== "undefined") {
			result += '"idKlausurraum" : ' + obj.idKlausurraum + ',';
		}
		if (typeof obj.idsSchuelerklausuren !== "undefined") {
			if (!obj.idsSchuelerklausuren) {
				result += '"idsSchuelerklausuren" : []';
			} else {
				result += '"idsSchuelerklausuren" : [ ';
				for (let i = 0; i < obj.idsSchuelerklausuren.size(); i++) {
					const elem = obj.idsSchuelerklausuren.get(i);
					result += elem;
					if (i < obj.idsSchuelerklausuren.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kursKlausurPatched !== "undefined") {
			result += '"kursKlausurPatched" : ' + ((!obj.kursKlausurPatched) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursKlausurPatched)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionSkrsKrs(obj : unknown) : GostKlausurenCollectionSkrsKrs {
	return obj as GostKlausurenCollectionSkrsKrs;
}
