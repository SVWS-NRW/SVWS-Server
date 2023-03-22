import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class FachDaten extends JavaObject {

	/**
	 * Die ID des Faches.
	 */
	public id : number = 0;

	/**
	 * Das eindeutige Kürzel des Faches
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string | null = null;

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzelStatistik : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.fach.FachDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): FachDaten {
		const obj = JSON.parse(json);
		const result = new FachDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik === null ? null : obj.kuerzelStatistik;
		return result;
	}

	public static transpilerToJSON(obj : FachDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_fach_FachDaten(obj : unknown) : FachDaten {
	return obj as FachDaten;
}
