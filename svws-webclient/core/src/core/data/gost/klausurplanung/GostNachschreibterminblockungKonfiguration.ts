import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';

export class GostNachschreibterminblockungKonfiguration extends JavaObject {

	/**
	 * Die maximale Zeit, welche für die Blockung verwendet wird
	 */
	public maxTimeMillis : number = 1000;

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTermin> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public termine : List<GostKlausurtermin> = new ArrayList();

	/**
	 * True, falls NachschreiberInnen der selben Klausur auf den selben Termin geblockt werden sollen.
	 */
	public _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostNachschreibterminblockungKonfiguration {
		const obj = JSON.parse(json);
		const result = new GostNachschreibterminblockungKonfiguration();
		if (typeof obj.maxTimeMillis === "undefined")
			 throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if ((obj.schuelerklausurtermine !== undefined) && (obj.schuelerklausurtermine !== null)) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine?.add(GostSchuelerklausurTermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.termine !== undefined) && (obj.termine !== null)) {
			for (const elem of obj.termine) {
				result.termine?.add(GostKlausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen === "undefined")
			 throw new Error('invalid json format, missing attribute _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen');
		result._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = obj._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen;
		return result;
	}

	public static transpilerToJSON(obj : GostNachschreibterminblockungKonfiguration) : string {
		let result = '{';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
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
		result += '"_regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen" : ' + obj._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostNachschreibterminblockungKonfiguration>) : string {
		let result = '{';
		if (typeof obj.maxTimeMillis !== "undefined") {
			result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
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
		if (typeof obj._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen !== "undefined") {
			result += '"_regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen" : ' + obj._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostNachschreibterminblockungKonfiguration(obj : unknown) : GostNachschreibterminblockungKonfiguration {
	return obj as GostNachschreibterminblockungKonfiguration;
}
