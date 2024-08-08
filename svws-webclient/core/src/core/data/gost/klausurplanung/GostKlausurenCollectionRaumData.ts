import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurterminraumstunde';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurraum } from '../../../../core/data/gost/klausurplanung/GostKlausurraum';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostKlausurraumstunde';

export class GostKlausurenCollectionRaumData extends JavaObject {

	/**
	 * Die ID der Schülerklausur.
	 */
	public raeume : List<GostKlausurraum> = new ArrayList<GostKlausurraum>();

	/**
	 * Die ID der Schülerklausur.
	 */
	public raumstunden : List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public sktRaumstunden : List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public idsKlausurtermine : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionRaumData {
		const obj = JSON.parse(json);
		const result = new GostKlausurenCollectionRaumData();
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(GostKlausurraum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raumstunden !== undefined) && (obj.raumstunden !== null)) {
			for (const elem of obj.raumstunden) {
				result.raumstunden?.add(GostKlausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.sktRaumstunden !== undefined) && (obj.sktRaumstunden !== null)) {
			for (const elem of obj.sktRaumstunden) {
				result.sktRaumstunden?.add(GostSchuelerklausurterminraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.idsKlausurtermine !== undefined) && (obj.idsKlausurtermine !== null)) {
			for (const elem of obj.idsKlausurtermine) {
				result.idsKlausurtermine?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionRaumData) : string {
		let result = '{';
		if (!obj.raeume) {
			result += '"raeume" : []';
		} else {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += GostKlausurraum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
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
		if (!obj.sktRaumstunden) {
			result += '"sktRaumstunden" : []';
		} else {
			result += '"sktRaumstunden" : [ ';
			for (let i = 0; i < obj.sktRaumstunden.size(); i++) {
				const elem = obj.sktRaumstunden.get(i);
				result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
				if (i < obj.sktRaumstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.idsKlausurtermine) {
			result += '"idsKlausurtermine" : []';
		} else {
			result += '"idsKlausurtermine" : [ ';
			for (let i = 0; i < obj.idsKlausurtermine.size(); i++) {
				const elem = obj.idsKlausurtermine.get(i);
				result += elem;
				if (i < obj.idsKlausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionRaumData>) : string {
		let result = '{';
		if (obj.raeume !== undefined) {
			if (!obj.raeume) {
				result += '"raeume" : []';
			} else {
				result += '"raeume" : [ ';
				for (let i = 0; i < obj.raeume.size(); i++) {
					const elem = obj.raeume.get(i);
					result += GostKlausurraum.transpilerToJSON(elem);
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.raumstunden !== undefined) {
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
		if (obj.sktRaumstunden !== undefined) {
			if (!obj.sktRaumstunden) {
				result += '"sktRaumstunden" : []';
			} else {
				result += '"sktRaumstunden" : [ ';
				for (let i = 0; i < obj.sktRaumstunden.size(); i++) {
					const elem = obj.sktRaumstunden.get(i);
					result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
					if (i < obj.sktRaumstunden.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.idsKlausurtermine !== undefined) {
			if (!obj.idsKlausurtermine) {
				result += '"idsKlausurtermine" : []';
			} else {
				result += '"idsKlausurtermine" : [ ';
				for (let i = 0; i < obj.idsKlausurtermine.size(); i++) {
					const elem = obj.idsKlausurtermine.get(i);
					result += elem;
					if (i < obj.idsKlausurtermine.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionRaumData(obj : unknown) : GostKlausurenCollectionRaumData {
	return obj as GostKlausurenCollectionRaumData;
}
