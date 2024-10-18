import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurterminblockungErgebnisTermin extends JavaObject {

	/**
	 * Eine Liste der, dem Termin zugeordneten, Kurs-Klausur-IDs
	 */
	public kursklausuren : List<number> = new ArrayList<number>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin'].includes(name);
	}

	public static class = new Class<GostKlausurterminblockungErgebnisTermin>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin');

	public static transpilerFromJSON(json : string): GostKlausurterminblockungErgebnisTermin {
		const obj = JSON.parse(json) as Partial<GostKlausurterminblockungErgebnisTermin>;
		const result = new GostKlausurterminblockungErgebnisTermin();
		if (obj.kursklausuren !== undefined) {
			for (const elem of obj.kursklausuren) {
				result.kursklausuren.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurterminblockungErgebnisTermin) : string {
		let result = '{';
		result += '"kursklausuren" : [ ';
		for (let i = 0; i < obj.kursklausuren.size(); i++) {
			const elem = obj.kursklausuren.get(i);
			result += elem.toString();
			if (i < obj.kursklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurterminblockungErgebnisTermin>) : string {
		let result = '{';
		if (obj.kursklausuren !== undefined) {
			result += '"kursklausuren" : [ ';
			for (let i = 0; i < obj.kursklausuren.size(); i++) {
				const elem = obj.kursklausuren.get(i);
				result += elem.toString();
				if (i < obj.kursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurterminblockungErgebnisTermin(obj : unknown) : GostKlausurterminblockungErgebnisTermin {
	return obj as GostKlausurterminblockungErgebnisTermin;
}
