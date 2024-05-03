import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';

export class GostKlausurraumblockungKonfiguration extends JavaObject {

	/**
	 * Die maximale Zeit, welche für die Blockung verwendet wird
	 */
	public maxTimeMillis : number = 1000;

	/**
	 * Die Liste der angereicherten Schülerklausurtermine.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTerminRich> = new ArrayList<GostSchuelerklausurTerminRich>();

	/**
	 * Die Liste der angereicherten Klausurräume.
	 */
	public raeume : List<GostKlausurraumRich> = new ArrayList<GostKlausurraumRich>();

	/**
	 * Gewicht, um die Schülerklausuren auf möchlichst wenig Räume zu verteilen.
	 */
	public _regel_blocke_in_moeglichst_wenig_raeume : number = 1;

	/**
	 * Gewicht, um alle Klausuren desselben Kurses in denselben Raum zu blocken.
	 */
	public _regel_selbe_kursklausur_in_selben_raum : number = 1;

	/**
	 * Gewicht, um in einem Klausurraum möglichst alle Klausuren mit ähnlichen Klausurdauern zu blocken.
	 */
	public _regel_aehnliche_klausurdauer_pro_raum : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumblockungKonfiguration'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurraumblockungKonfiguration {
		const obj = JSON.parse(json);
		const result = new GostKlausurraumblockungKonfiguration();
		if (typeof obj.maxTimeMillis === "undefined")
			 throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if ((obj.schuelerklausurtermine !== undefined) && (obj.schuelerklausurtermine !== null)) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine?.add(GostSchuelerklausurTerminRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(GostKlausurraumRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj._regel_blocke_in_moeglichst_wenig_raeume === "undefined")
			 throw new Error('invalid json format, missing attribute _regel_blocke_in_moeglichst_wenig_raeume');
		result._regel_blocke_in_moeglichst_wenig_raeume = obj._regel_blocke_in_moeglichst_wenig_raeume;
		if (typeof obj._regel_selbe_kursklausur_in_selben_raum === "undefined")
			 throw new Error('invalid json format, missing attribute _regel_selbe_kursklausur_in_selben_raum');
		result._regel_selbe_kursklausur_in_selben_raum = obj._regel_selbe_kursklausur_in_selben_raum;
		if (typeof obj._regel_aehnliche_klausurdauer_pro_raum === "undefined")
			 throw new Error('invalid json format, missing attribute _regel_aehnliche_klausurdauer_pro_raum');
		result._regel_aehnliche_klausurdauer_pro_raum = obj._regel_aehnliche_klausurdauer_pro_raum;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumblockungKonfiguration) : string {
		let result = '{';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		if (!obj.schuelerklausurtermine) {
			result += '"schuelerklausurtermine" : []';
		} else {
			result += '"schuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
				const elem = obj.schuelerklausurtermine.get(i);
				result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
				if (i < obj.schuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.raeume) {
			result += '"raeume" : []';
		} else {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += GostKlausurraumRich.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"_regel_blocke_in_moeglichst_wenig_raeume" : ' + obj._regel_blocke_in_moeglichst_wenig_raeume + ',';
		result += '"_regel_selbe_kursklausur_in_selben_raum" : ' + obj._regel_selbe_kursklausur_in_selben_raum + ',';
		result += '"_regel_aehnliche_klausurdauer_pro_raum" : ' + obj._regel_aehnliche_klausurdauer_pro_raum + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumblockungKonfiguration>) : string {
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
					result += GostSchuelerklausurTerminRich.transpilerToJSON(elem);
					if (i < obj.schuelerklausurtermine.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.raeume !== "undefined") {
			if (!obj.raeume) {
				result += '"raeume" : []';
			} else {
				result += '"raeume" : [ ';
				for (let i = 0; i < obj.raeume.size(); i++) {
					const elem = obj.raeume.get(i);
					result += GostKlausurraumRich.transpilerToJSON(elem);
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj._regel_blocke_in_moeglichst_wenig_raeume !== "undefined") {
			result += '"_regel_blocke_in_moeglichst_wenig_raeume" : ' + obj._regel_blocke_in_moeglichst_wenig_raeume + ',';
		}
		if (typeof obj._regel_selbe_kursklausur_in_selben_raum !== "undefined") {
			result += '"_regel_selbe_kursklausur_in_selben_raum" : ' + obj._regel_selbe_kursklausur_in_selben_raum + ',';
		}
		if (typeof obj._regel_aehnliche_klausurdauer_pro_raum !== "undefined") {
			result += '"_regel_aehnliche_klausurdauer_pro_raum" : ' + obj._regel_aehnliche_klausurdauer_pro_raum + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumblockungKonfiguration(obj : unknown) : GostKlausurraumblockungKonfiguration {
	return obj as GostKlausurraumblockungKonfiguration;
}
