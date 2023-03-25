import { JavaObject } from '../../../../java/lang/JavaObject';

export class Schuelerklausur extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public idSchuelerklausur : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public idKursklausur : number = -1;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public idTermin : number = -1;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public idSchueler : number = -1;

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist.
	 */
	public startzeit : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.Schuelerklausur'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schuelerklausur {
		const obj = JSON.parse(json);
		const result = new Schuelerklausur();
		if (typeof obj.idSchuelerklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuelerklausur');
		result.idSchuelerklausur = obj.idSchuelerklausur;
		if (typeof obj.idKursklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idKursklausur');
		result.idKursklausur = obj.idKursklausur;
		if (typeof obj.idTermin === "undefined")
			 throw new Error('invalid json format, missing attribute idTermin');
		result.idTermin = obj.idTermin;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		return result;
	}

	public static transpilerToJSON(obj : Schuelerklausur) : string {
		let result = '{';
		result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		result += '"idTermin" : ' + obj.idTermin + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schuelerklausur>) : string {
		let result = '{';
		if (typeof obj.idSchuelerklausur !== "undefined") {
			result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		}
		if (typeof obj.idKursklausur !== "undefined") {
			result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		}
		if (typeof obj.idTermin !== "undefined") {
			result += '"idTermin" : ' + obj.idTermin + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_Schuelerklausur(obj : unknown) : Schuelerklausur {
	return obj as Schuelerklausur;
}
