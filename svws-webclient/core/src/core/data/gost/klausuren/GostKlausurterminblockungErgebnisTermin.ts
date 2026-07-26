import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurterminblockungErgebnisTermin extends JavaObject {

	/**
	 * Eine Liste der, dem Termin zugeordneten, Kurs-Klausur-IDs
	 */
	public idsKursklausuren: List<number> = new ArrayList<number>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungErgebnisTermin';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungErgebnisTermin'].includes(name);
	}

	public static readonly class = new Class<GostKlausurterminblockungErgebnisTermin>('de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungErgebnisTermin');

	public static transpilerFromJSON(json: string): GostKlausurterminblockungErgebnisTermin {
		const obj = JSON.parse(json) as Partial<GostKlausurterminblockungErgebnisTermin>;
		const result = new GostKlausurterminblockungErgebnisTermin();
		if (obj.idsKursklausuren !== undefined) {
			for (const elem of obj.idsKursklausuren) {
				result.idsKursklausuren.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurterminblockungErgebnisTermin): string {
		let result = '{';
		result += '"idsKursklausuren" : [ ';
		for (let i = 0; i < obj.idsKursklausuren.size(); i++) {
			const elem = obj.idsKursklausuren.get(i);
			result += elem.toString();
			if (i < obj.idsKursklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostKlausurterminblockungErgebnisTermin>): string {
		let result = '{';
		if (obj.idsKursklausuren !== undefined) {
			result += '"idsKursklausuren" : [ ';
			for (let i = 0; i < obj.idsKursklausuren.size(); i++) {
				const elem = obj.idsKursklausuren.get(i);
				result += elem.toString();
				if (i < obj.idsKursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurterminblockungErgebnisTermin(obj: unknown): GostKlausurterminblockungErgebnisTermin {
	return obj as GostKlausurterminblockungErgebnisTermin;
}
