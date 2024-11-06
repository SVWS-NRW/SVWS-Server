import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { GostBlockungKurs, cast_de_svws_nrw_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostBlockungKursAufteilung extends JavaObject {

	/**
	 * Informationen zum ersten Kurs.
	 */
	public kurs1 : GostBlockungKurs = new GostBlockungKurs();

	/**
	 * Informationen zum zweiten Kurs.
	 */
	public kurs2 : GostBlockungKurs = new GostBlockungKurs();

	/**
	 * Die IDs der Schüler, die dem ersten Kurs zugeordnet bleiben.
	 */
	public schueler1 : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der Schüler, die dem zweiten Kurs zugeordnet werden.
	 */
	public schueler2 : List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungKursAufteilung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungKursAufteilung'].includes(name);
	}

	public static class = new Class<GostBlockungKursAufteilung>('de.svws_nrw.core.data.gost.GostBlockungKursAufteilung');

	public static transpilerFromJSON(json : string): GostBlockungKursAufteilung {
		const obj = JSON.parse(json) as Partial<GostBlockungKursAufteilung>;
		const result = new GostBlockungKursAufteilung();
		if (obj.kurs1 === undefined)
			throw new Error('invalid json format, missing attribute kurs1');
		result.kurs1 = GostBlockungKurs.transpilerFromJSON(JSON.stringify(obj.kurs1));
		if (obj.kurs2 === undefined)
			throw new Error('invalid json format, missing attribute kurs2');
		result.kurs2 = GostBlockungKurs.transpilerFromJSON(JSON.stringify(obj.kurs2));
		if (obj.schueler1 !== undefined) {
			for (const elem of obj.schueler1) {
				result.schueler1.add(elem);
			}
		}
		if (obj.schueler2 !== undefined) {
			for (const elem of obj.schueler2) {
				result.schueler2.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungKursAufteilung) : string {
		let result = '{';
		result += '"kurs1" : ' + GostBlockungKurs.transpilerToJSON(obj.kurs1) + ',';
		result += '"kurs2" : ' + GostBlockungKurs.transpilerToJSON(obj.kurs2) + ',';
		result += '"schueler1" : [ ';
		for (let i = 0; i < obj.schueler1.size(); i++) {
			const elem = obj.schueler1.get(i);
			result += elem.toString();
			if (i < obj.schueler1.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler2" : [ ';
		for (let i = 0; i < obj.schueler2.size(); i++) {
			const elem = obj.schueler2.get(i);
			result += elem.toString();
			if (i < obj.schueler2.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungKursAufteilung>) : string {
		let result = '{';
		if (obj.kurs1 !== undefined) {
			result += '"kurs1" : ' + GostBlockungKurs.transpilerToJSON(obj.kurs1) + ',';
		}
		if (obj.kurs2 !== undefined) {
			result += '"kurs2" : ' + GostBlockungKurs.transpilerToJSON(obj.kurs2) + ',';
		}
		if (obj.schueler1 !== undefined) {
			result += '"schueler1" : [ ';
			for (let i = 0; i < obj.schueler1.size(); i++) {
				const elem = obj.schueler1.get(i);
				result += elem.toString();
				if (i < obj.schueler1.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler2 !== undefined) {
			result += '"schueler2" : [ ';
			for (let i = 0; i < obj.schueler2.size(); i++) {
				const elem = obj.schueler2.get(i);
				result += elem.toString();
				if (i < obj.schueler2.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungKursAufteilung(obj : unknown) : GostBlockungKursAufteilung {
	return obj as GostBlockungKursAufteilung;
}
