import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Klasse extends JavaObject {

	/**
	 * Die ID der Klasse aus der SVWS-DB (z.B. 12)
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z. B. EFA)
	 */
	public kuerzel : string | null = null;

	/**
	 * Das Kürzel der Klasse, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z. B. EF)
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört. Bei einer jahrgangsübergreifenden Klasse ist der Wert null.
	 */
	public jahrgangId : number | null = null;

	/**
	 * Die IDs der zugeordneten Klassenlehrer.
	 */
	public klassenlehrer : List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Klasse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Klasse'].includes(name);
	}

	public static class = new Class<LernplattformV1Klasse>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Klasse');

	public static transpilerFromJSON(json : string): LernplattformV1Klasse {
		const obj = JSON.parse(json) as Partial<LernplattformV1Klasse>;
		const result = new LernplattformV1Klasse();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.jahrgangId = (obj.jahrgangId === undefined) ? null : obj.jahrgangId === null ? null : obj.jahrgangId;
		if (obj.klassenlehrer !== undefined) {
			for (const elem of obj.klassenlehrer) {
				result.klassenlehrer.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Klasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"jahrgangId" : ' + ((obj.jahrgangId === null) ? 'null' : obj.jahrgangId.toString()) + ',';
		result += '"klassenlehrer" : [ ';
		for (let i = 0; i < obj.klassenlehrer.size(); i++) {
			const elem = obj.klassenlehrer.get(i);
			result += elem.toString();
			if (i < obj.klassenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Klasse>) : string {
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
		if (obj.jahrgangId !== undefined) {
			result += '"jahrgangId" : ' + ((obj.jahrgangId === null) ? 'null' : obj.jahrgangId.toString()) + ',';
		}
		if (obj.klassenlehrer !== undefined) {
			result += '"klassenlehrer" : [ ';
			for (let i = 0; i < obj.klassenlehrer.size(); i++) {
				const elem = obj.klassenlehrer.get(i);
				result += elem.toString();
				if (i < obj.klassenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Klasse(obj : unknown) : LernplattformV1Klasse {
	return obj as LernplattformV1Klasse;
}
