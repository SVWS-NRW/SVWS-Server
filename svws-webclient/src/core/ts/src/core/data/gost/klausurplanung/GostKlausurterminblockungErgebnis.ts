import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurterminblockungErgebnisTermin } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungErgebnisTermin';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';

export class GostKlausurterminblockungErgebnis extends JavaObject {

	/**
	 * Eine Liste der Termine-Ergebnisse
	 */
	public termine : List<GostKlausurterminblockungErgebnisTermin> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurterminblockungErgebnis {
		const obj = JSON.parse(json);
		const result = new GostKlausurterminblockungErgebnis();
		if ((obj.termine !== undefined) && (obj.termine !== null)) {
			for (const elem of obj.termine) {
				result.termine?.add(GostKlausurterminblockungErgebnisTermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurterminblockungErgebnis) : string {
		let result = '{';
		if (!obj.termine) {
			result += '"termine" : []';
		} else {
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

	public static transpilerToJSONPatch(obj : Partial<GostKlausurterminblockungErgebnis>) : string {
		let result = '{';
		if (typeof obj.termine !== "undefined") {
			if (!obj.termine) {
				result += '"termine" : []';
			} else {
				result += '"termine" : [ ';
				for (let i = 0; i < obj.termine.size(); i++) {
					const elem = obj.termine.get(i);
					result += GostKlausurterminblockungErgebnisTermin.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurterminblockungErgebnis(obj : unknown) : GostKlausurterminblockungErgebnis {
	return obj as GostKlausurterminblockungErgebnis;
}
