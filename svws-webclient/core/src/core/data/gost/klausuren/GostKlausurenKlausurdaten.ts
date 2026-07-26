import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausuren/GostKursklausur';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausuren/GostKlausurvorgabe';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostSchuelerklausurtermin } from '../../../../core/data/gost/klausuren/GostSchuelerklausurtermin';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausuren/GostSchuelerklausur';
import { GostKlausurtermin } from '../../../../core/data/gost/klausuren/GostKlausurtermin';

export class GostKlausurenKlausurdaten extends JavaObject {

	/**
	 * Ein Array mit den Klausurvorgaben.
	 */
	public vorgaben: List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();

	/**
	 * Ein Array mit den Kursklausuren.
	 */
	public kursklausuren: List<GostKursklausur> = new ArrayList<GostKursklausur>();

	/**
	 * Ein Array mit den Schülerklausuren.
	 */
	public schuelerklausuren: List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	/**
	 * Ein Array mit den Schülerklausurterminen.
	 */
	public schuelerklausurtermine: List<GostSchuelerklausurtermin> = new ArrayList<GostSchuelerklausurtermin>();

	/**
	 * Ein Array mit den Klausurterminen.
	 */
	public termine: List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();

	/**
	 * Beschreibung zu den Klausurdaten
	 */
	public description: string | null = null;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenKlausurdaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten');

	public static transpilerFromJSON(json: string): GostKlausurenKlausurdaten {
		const obj = JSON.parse(json) as Partial<GostKlausurenKlausurdaten>;
		const result = new GostKlausurenKlausurdaten();
		if (obj.vorgaben !== undefined) {
			for (const elem of obj.vorgaben) {
				result.vorgaben.add(GostKlausurvorgabe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kursklausuren !== undefined) {
			for (const elem of obj.kursklausuren) {
				result.kursklausuren.add(GostKursklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerklausuren !== undefined) {
			for (const elem of obj.schuelerklausuren) {
				result.schuelerklausuren.add(GostSchuelerklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerklausurtermine !== undefined) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine.add(GostSchuelerklausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.termine !== undefined) {
			for (const elem of obj.termine) {
				result.termine.add(GostKlausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.description === undefined)
			throw new Error('invalid json format, missing attribute description');
		result.description = obj.description;
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurenKlausurdaten): string {
		let result = '{';
		result += '"vorgaben" : [ ';
		for (let i = 0; i < obj.vorgaben.size(); i++) {
			const elem = obj.vorgaben.get(i);
			result += GostKlausurvorgabe.transpilerToJSON(elem);
			if (i < obj.vorgaben.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kursklausuren" : [ ';
		for (let i = 0; i < obj.kursklausuren.size(); i++) {
			const elem = obj.kursklausuren.get(i);
			result += GostKursklausur.transpilerToJSON(elem);
			if (i < obj.kursklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerklausuren" : [ ';
		for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
			const elem = obj.schuelerklausuren.get(i);
			result += GostSchuelerklausur.transpilerToJSON(elem);
			if (i < obj.schuelerklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerklausurtermine" : [ ';
		for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
			const elem = obj.schuelerklausurtermine.get(i);
			result += GostSchuelerklausurtermin.transpilerToJSON(elem);
			if (i < obj.schuelerklausurtermine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"termine" : [ ';
		for (let i = 0; i < obj.termine.size(); i++) {
			const elem = obj.termine.get(i);
			result += GostKlausurtermin.transpilerToJSON(elem);
			if (i < obj.termine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"description" : ' + JSON.stringify(obj.description) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenKlausurdaten>): string {
		let result = '{';
		if (obj.vorgaben !== undefined) {
			result += '"vorgaben" : [ ';
			for (let i = 0; i < obj.vorgaben.size(); i++) {
				const elem = obj.vorgaben.get(i);
				result += GostKlausurvorgabe.transpilerToJSON(elem);
				if (i < obj.vorgaben.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kursklausuren !== undefined) {
			result += '"kursklausuren" : [ ';
			for (let i = 0; i < obj.kursklausuren.size(); i++) {
				const elem = obj.kursklausuren.get(i);
				result += GostKursklausur.transpilerToJSON(elem);
				if (i < obj.kursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerklausuren !== undefined) {
			result += '"schuelerklausuren" : [ ';
			for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
				const elem = obj.schuelerklausuren.get(i);
				result += GostSchuelerklausur.transpilerToJSON(elem);
				if (i < obj.schuelerklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerklausurtermine !== undefined) {
			result += '"schuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
				const elem = obj.schuelerklausurtermine.get(i);
				result += GostSchuelerklausurtermin.transpilerToJSON(elem);
				if (i < obj.schuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.termine !== undefined) {
			result += '"termine" : [ ';
			for (let i = 0; i < obj.termine.size(); i++) {
				const elem = obj.termine.get(i);
				result += GostKlausurtermin.transpilerToJSON(elem);
				if (i < obj.termine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.description !== undefined) {
			result += '"description" : ' + JSON.stringify(obj.description) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenKlausurdaten(obj: unknown): GostKlausurenKlausurdaten {
	return obj as GostKlausurenKlausurdaten;
}
