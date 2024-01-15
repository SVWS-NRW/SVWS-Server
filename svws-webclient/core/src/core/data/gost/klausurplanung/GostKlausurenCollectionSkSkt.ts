import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';

export class GostKlausurenCollectionSkSkt extends JavaObject {

	/**
	 * Die ID der Schülerklausur.
	 */
	public schuelerklausuren : List<GostSchuelerklausur> = new ArrayList();

	/**
	 * Die ID der Schülerklausur.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTermin> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkSkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkSkt'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionSkSkt {
		const obj = JSON.parse(json);
		const result = new GostKlausurenCollectionSkSkt();
		if ((obj.schuelerklausuren !== undefined) && (obj.schuelerklausuren !== null)) {
			for (const elem of obj.schuelerklausuren) {
				result.schuelerklausuren?.add(GostSchuelerklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.schuelerklausurtermine !== undefined) && (obj.schuelerklausurtermine !== null)) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine?.add(GostSchuelerklausurTermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionSkSkt) : string {
		let result = '{';
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
		if (!obj.schuelerklausurtermine) {
			result += '"schuelerklausurtermine" : []';
		} else {
			result += '"schuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
				const elem = obj.schuelerklausurtermine.get(i);
				result += GostSchuelerklausurTermin.transpilerToJSON(elem);
				if (i < obj.schuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionSkSkt>) : string {
		let result = '{';
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
		if (typeof obj.schuelerklausurtermine !== "undefined") {
			if (!obj.schuelerklausurtermine) {
				result += '"schuelerklausurtermine" : []';
			} else {
				result += '"schuelerklausurtermine" : [ ';
				for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
					const elem = obj.schuelerklausurtermine.get(i);
					result += GostSchuelerklausurTermin.transpilerToJSON(elem);
					if (i < obj.schuelerklausurtermine.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionSkSkt(obj : unknown) : GostKlausurenCollectionSkSkt {
	return obj as GostKlausurenCollectionSkSkt;
}
