import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Jahrgang extends JavaObject {

	/**
	 * Die ID des Jahrgangs aus der SVWS-DB (z. B. 12)
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z. B. EFA)
	 */
	public kuerzel : string | null = null;

	/**
	 * Das Kürzel der Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z. B. EF)
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die Bezeichnung des Jahrgangs, welche bei der amtlichen Statistik verwendet wird.
	 */
	public bezeichnung : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Jahrgang';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Jahrgang'].includes(name);
	}

	public static class = new Class<LernplattformV1Jahrgang>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Jahrgang');

	public static transpilerFromJSON(json : string): LernplattformV1Jahrgang {
		const obj = JSON.parse(json) as Partial<LernplattformV1Jahrgang>;
		const result = new LernplattformV1Jahrgang();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Jahrgang) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Jahrgang>) : string {
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
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Jahrgang(obj : unknown) : LernplattformV1Jahrgang {
	return obj as LernplattformV1Jahrgang;
}
