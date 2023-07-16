import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostSchuelerklausurraumstunde extends JavaObject {

	/**
	 * Die ID der Schülerklausur.
	 */
	public idSchuelerklausur : number = -1;

	/**
	 * Die ID der Klausurraumstunde.
	 */
	public idRaumstunde : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurraumstunde'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostSchuelerklausurraumstunde {
		const obj = JSON.parse(json);
		const result = new GostSchuelerklausurraumstunde();
		if (typeof obj.idSchuelerklausur === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuelerklausur');
		result.idSchuelerklausur = obj.idSchuelerklausur;
		if (typeof obj.idRaumstunde === "undefined")
			 throw new Error('invalid json format, missing attribute idRaumstunde');
		result.idRaumstunde = obj.idRaumstunde;
		return result;
	}

	public static transpilerToJSON(obj : GostSchuelerklausurraumstunde) : string {
		let result = '{';
		result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		result += '"idRaumstunde" : ' + obj.idRaumstunde + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostSchuelerklausurraumstunde>) : string {
		let result = '{';
		if (typeof obj.idSchuelerklausur !== "undefined") {
			result += '"idSchuelerklausur" : ' + obj.idSchuelerklausur + ',';
		}
		if (typeof obj.idRaumstunde !== "undefined") {
			result += '"idRaumstunde" : ' + obj.idRaumstunde + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostSchuelerklausurraumstunde(obj : unknown) : GostSchuelerklausurraumstunde {
	return obj as GostSchuelerklausurraumstunde;
}
