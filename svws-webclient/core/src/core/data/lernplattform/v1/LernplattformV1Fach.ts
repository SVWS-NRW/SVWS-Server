import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Fach extends JavaObject {

	/**
	 * Die ID des Faches in der SVWS-DB.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D)
	 */
	public kuerzel : string | null = "";

	/**
	 * Die Kürzelanzeige des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D)
	 */
	public kuerzelAnzeige : string | null = "";

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht.
	 */
	public istFremdsprache : boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Fach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Fach'].includes(name);
	}

	public static class = new Class<LernplattformV1Fach>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Fach');

	public static transpilerFromJSON(json : string): LernplattformV1Fach {
		const obj = JSON.parse(json) as Partial<LernplattformV1Fach>;
		const result = new LernplattformV1Fach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		if (obj.istFremdsprache === undefined)
			throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Fach) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Fach>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		}
		if (obj.istFremdsprache !== undefined) {
			result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Fach(obj : unknown) : LernplattformV1Fach {
	return obj as LernplattformV1Fach;
}
