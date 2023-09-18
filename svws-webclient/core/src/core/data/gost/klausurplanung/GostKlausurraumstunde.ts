import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKlausurraumstunde extends JavaObject {

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public id : number = -1;

	/**
	 * Die ID des Klausurraums.
	 */
	public idRaum : number = -1;

	/**
	 * Die ID des Zeitrasters.
	 */
	public idZeitraster : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurraumstunde {
		const obj = JSON.parse(json);
		const result = new GostKlausurraumstunde();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idRaum === "undefined")
			 throw new Error('invalid json format, missing attribute idRaum');
		result.idRaum = obj.idRaum;
		if (typeof obj.idZeitraster === "undefined")
			 throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraumstunde) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idRaum" : ' + obj.idRaum + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraumstunde>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idRaum !== "undefined") {
			result += '"idRaum" : ' + obj.idRaum + ',';
		}
		if (typeof obj.idZeitraster !== "undefined") {
			result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurraumstunde(obj : unknown) : GostKlausurraumstunde {
	return obj as GostKlausurraumstunde;
}
