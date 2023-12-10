import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { BKFachklassenSchluessel } from '../../../core/data/bk/BKFachklassenSchluessel';

export class BKFBFach extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das im Alltag verwendete Kuerzel des Fachs.
	 */
	public kuerzel : string = "";

	/**
	 * Die Zeugnisbezeichnung des Fachs
	 */
	public bezeichnung : string = "";

	/**
	 * Die Fachklassen, in denen das Fach im Bildungsplan steht
	 */
	public fachklassen : List<BKFachklassenSchluessel> = new ArrayList();

	/**
	 * Gibt an, in welchem Schuljahr der Historien-Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Historien-Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKFBFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKFBFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): BKFBFach {
		const obj = JSON.parse(json);
		const result = new BKFBFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if ((obj.fachklassen !== undefined) && (obj.fachklassen !== null)) {
			for (const elem of obj.fachklassen) {
				result.fachklassen?.add(BKFachklassenSchluessel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : BKFBFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		if (!obj.fachklassen) {
			result += '"fachklassen" : []';
		} else {
			result += '"fachklassen" : [ ';
			for (let i = 0; i < obj.fachklassen.size(); i++) {
				const elem = obj.fachklassen.get(i);
				result += BKFachklassenSchluessel.transpilerToJSON(elem);
				if (i < obj.fachklassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKFBFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.fachklassen !== "undefined") {
			if (!obj.fachklassen) {
				result += '"fachklassen" : []';
			} else {
				result += '"fachklassen" : [ ';
				for (let i = 0; i < obj.fachklassen.size(); i++) {
					const elem = obj.fachklassen.get(i);
					result += BKFachklassenSchluessel.transpilerToJSON(elem);
					if (i < obj.fachklassen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_BKFBFach(obj : unknown) : BKFBFach {
	return obj as BKFBFach;
}
