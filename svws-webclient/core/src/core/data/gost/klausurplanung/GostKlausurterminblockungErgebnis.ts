import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurterminblockungErgebnisTermin } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungErgebnisTermin';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurterminblockungErgebnis extends JavaObject {

	/**
	 * Ein Array mit den GostKlausurterminblockungErgebnisTerminen.
	 */
	public termine : List<GostKlausurterminblockungErgebnisTermin> = new ArrayList<GostKlausurterminblockungErgebnisTermin>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis'].includes(name);
	}

	public static class = new Class<GostKlausurterminblockungErgebnis>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis');

	public static transpilerFromJSON(json : string): GostKlausurterminblockungErgebnis {
		const obj = JSON.parse(json) as Partial<GostKlausurterminblockungErgebnis>;
		const result = new GostKlausurterminblockungErgebnis();
		if (obj.termine !== undefined) {
			for (const elem of obj.termine) {
				result.termine.add(GostKlausurterminblockungErgebnisTermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurterminblockungErgebnis) : string {
		let result = '{';
		result += '"termine" : [ ';
		for (let i = 0; i < obj.termine.size(); i++) {
			const elem = obj.termine.get(i);
			result += GostKlausurterminblockungErgebnisTermin.transpilerToJSON(elem);
			if (i < obj.termine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurterminblockungErgebnis>) : string {
		let result = '{';
		if (obj.termine !== undefined) {
			result += '"termine" : [ ';
			for (let i = 0; i < obj.termine.size(); i++) {
				const elem = obj.termine.get(i);
				result += GostKlausurterminblockungErgebnisTermin.transpilerToJSON(elem);
				if (i < obj.termine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurterminblockungErgebnis(obj : unknown) : GostKlausurterminblockungErgebnis {
	return obj as GostKlausurterminblockungErgebnis;
}
