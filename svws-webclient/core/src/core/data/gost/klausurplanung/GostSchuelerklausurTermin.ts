import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostSchuelerklausurTermin extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public idSchuelerklausur : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public folgeNr : number = -1;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public idTermin : number | null = null;

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number | null = null;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public idKursklausur : number = -1;

	/**
	 * Das Zeitraster des Stundenplans.
	 */
	public idSchueler : number = -1;

	/**
	 * Die textuelle Bemerkung zur Schülerklausur, sofern vorhanden.
	 */
	public bemerkungSchuelerklausur : string | null = null;

	/**
	 * Die textuelle Bemerkung zum Schülerklausurtermin, sofern vorhanden.
	 */
	public bemerkungSchuelerklausurtermin : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostSchuelerklausurTermin {
		const obj = JSON.parse(json);
		const result = new GostSchuelerklausurTermin();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchuelerklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuelerklausur');
		result.idSchuelerklausur = obj.idSchuelerklausur;
		if (typeof obj.folgeNr === "undefined")
			 throw new Error('invalid json format, missing attribute folgeNr');
		result.folgeNr = obj.folgeNr;
		result.idTermin = typeof obj.idTermin === "undefined" ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		if (typeof obj.idKursklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idKursklausur');
		result.idKursklausur = obj.idKursklausur;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.bemerkungSchuelerklausur = typeof obj.bemerkungSchuelerklausur === "undefined" ? null : obj.bemerkungSchuelerklausur === null ? null : obj.bemerkungSchuelerklausur;
		result.bemerkungSchuelerklausurtermin = typeof obj.bemerkungSchuelerklausurtermin === "undefined" ? null : obj.bemerkungSchuelerklausurtermin === null ? null : obj.bemerkungSchuelerklausurtermin;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausurTermin) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		result += '"folgeNr" : ' + obj.folgeNr + ',';
		result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"bemerkungSchuelerklausur" : ' + ((!obj.bemerkungSchuelerklausur) ? 'null' : JSON.stringify(obj.bemerkungSchuelerklausur)) + ',';
		result += '"bemerkungSchuelerklausurtermin" : ' + ((!obj.bemerkungSchuelerklausurtermin) ? 'null' : JSON.stringify(obj.bemerkungSchuelerklausurtermin)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausurTermin>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchuelerklausur !== "undefined") {
			result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		}
		if (typeof obj.folgeNr !== "undefined") {
			result += '"folgeNr" : ' + obj.folgeNr + ',';
		}
		if (typeof obj.idTermin !== "undefined") {
			result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		}
		if (typeof obj.idKursklausur !== "undefined") {
			result += '"idKursklausur" : ' + obj.idKursklausur + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (typeof obj.bemerkungSchuelerklausur !== "undefined") {
			result += '"bemerkungSchuelerklausur" : ' + ((!obj.bemerkungSchuelerklausur) ? 'null' : JSON.stringify(obj.bemerkungSchuelerklausur)) + ',';
		}
		if (typeof obj.bemerkungSchuelerklausurtermin !== "undefined") {
			result += '"bemerkungSchuelerklausurtermin" : ' + ((!obj.bemerkungSchuelerklausurtermin) ? 'null' : JSON.stringify(obj.bemerkungSchuelerklausurtermin)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostSchuelerklausurTermin(obj : unknown) : GostSchuelerklausurTermin {
	return obj as GostSchuelerklausurTermin;
}
