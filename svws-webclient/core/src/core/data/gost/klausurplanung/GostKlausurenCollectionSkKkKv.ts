import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';

export class GostKlausurenCollectionSkKkKv extends JavaObject {

	/**
	 * Die Liste der Klausurvorgaben.
	 */
	public vorgaben : List<GostKlausurvorgabe> = new ArrayList();

	/**
	 * Die Liste der Kursklausuren.
	 */
	public kursklausuren : List<GostKursklausur> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public schuelerklausuren : List<GostSchuelerklausur> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public termine : List<GostKlausurtermin> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkKkKv';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkKkKv'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionSkKkKv {
		const obj = JSON.parse(json);
		const result = new GostKlausurenCollectionSkKkKv();
		if ((obj.vorgaben !== undefined) && (obj.vorgaben !== null)) {
			for (const elem of obj.vorgaben) {
				result.vorgaben?.add(GostKlausurvorgabe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kursklausuren !== undefined) && (obj.kursklausuren !== null)) {
			for (const elem of obj.kursklausuren) {
				result.kursklausuren?.add(GostKursklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.schuelerklausuren !== undefined) && (obj.schuelerklausuren !== null)) {
			for (const elem of obj.schuelerklausuren) {
				result.schuelerklausuren?.add(GostSchuelerklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.termine !== undefined) && (obj.termine !== null)) {
			for (const elem of obj.termine) {
				result.termine?.add(GostKlausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionSkKkKv) : string {
		let result = '{';
		if (!obj.vorgaben) {
			result += '"vorgaben" : []';
		} else {
			result += '"vorgaben" : [ ';
			for (let i = 0; i < obj.vorgaben.size(); i++) {
				const elem = obj.vorgaben.get(i);
				result += GostKlausurvorgabe.transpilerToJSON(elem);
				if (i < obj.vorgaben.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kursklausuren) {
			result += '"kursklausuren" : []';
		} else {
			result += '"kursklausuren" : [ ';
			for (let i = 0; i < obj.kursklausuren.size(); i++) {
				const elem = obj.kursklausuren.get(i);
				result += GostKursklausur.transpilerToJSON(elem);
				if (i < obj.kursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schuelerklausuren) {
			result += '"schuelerklausuren" : []';
		} else {
			result += '"schuelerklausuren" : [ ';
			for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
				const elem = obj.schuelerklausuren.get(i);
				result += GostSchuelerklausur.transpilerToJSON(elem);
				if (i < obj.schuelerklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.termine) {
			result += '"termine" : []';
		} else {
			result += '"termine" : [ ';
			for (let i = 0; i < obj.termine.size(); i++) {
				const elem = obj.termine.get(i);
				result += GostKlausurtermin.transpilerToJSON(elem);
				if (i < obj.termine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionSkKkKv>) : string {
		let result = '{';
		if (typeof obj.vorgaben !== "undefined") {
			if (!obj.vorgaben) {
				result += '"vorgaben" : []';
			} else {
				result += '"vorgaben" : [ ';
				for (let i = 0; i < obj.vorgaben.size(); i++) {
					const elem = obj.vorgaben.get(i);
					result += GostKlausurvorgabe.transpilerToJSON(elem);
					if (i < obj.vorgaben.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kursklausuren !== "undefined") {
			if (!obj.kursklausuren) {
				result += '"kursklausuren" : []';
			} else {
				result += '"kursklausuren" : [ ';
				for (let i = 0; i < obj.kursklausuren.size(); i++) {
					const elem = obj.kursklausuren.get(i);
					result += GostKursklausur.transpilerToJSON(elem);
					if (i < obj.kursklausuren.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schuelerklausuren !== "undefined") {
			if (!obj.schuelerklausuren) {
				result += '"schuelerklausuren" : []';
			} else {
				result += '"schuelerklausuren" : [ ';
				for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
					const elem = obj.schuelerklausuren.get(i);
					result += GostSchuelerklausur.transpilerToJSON(elem);
					if (i < obj.schuelerklausuren.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.termine !== "undefined") {
			if (!obj.termine) {
				result += '"termine" : []';
			} else {
				result += '"termine" : [ ';
				for (let i = 0; i < obj.termine.size(); i++) {
					const elem = obj.termine.get(i);
					result += GostKlausurtermin.transpilerToJSON(elem);
					if (i < obj.termine.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionSkKkKv(obj : unknown) : GostKlausurenCollectionSkKkKv {
	return obj as GostKlausurenCollectionSkKkKv;
}
