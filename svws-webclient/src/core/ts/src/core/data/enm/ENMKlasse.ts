import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';

export class ENMKlasse extends JavaObject {

	/**
	 * Die ID der Klasse aus der SVWS-DB (z.B. 16)
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF)
	 */
	public kuerzel : string | null = null;

	/**
	 * Das Kürzel ser Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF)
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört (z.B. 11) oder null, falls es sich um eine jahrgangsübergreifende Klasse handelt
	 */
	public idJahrgang : number | null = null;

	/**
	 * Die Reihenfolge der Klasse bei der Sortierung der Klasse. (z.B. 8)
	 */
	public sortierung : number = 0;

	/**
	 * Die IDs der zugeordneten Klassenlehrer.
	 */
	public klassenlehrer : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMKlasse'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMKlasse {
		const obj = JSON.parse(json);
		const result = new ENMKlasse();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = typeof obj.kuerzelAnzeige === "undefined" ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.idJahrgang = typeof obj.idJahrgang === "undefined" ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if ((obj.klassenlehrer !== undefined) && (obj.klassenlehrer !== null)) {
			for (const elem of obj.klassenlehrer) {
				result.klassenlehrer?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMKlasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige + '"') + ',';
		result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		if (!obj.klassenlehrer) {
			result += '"klassenlehrer" : []';
		} else {
			result += '"klassenlehrer" : [ ';
			for (let i = 0; i < obj.klassenlehrer.size(); i++) {
				const elem = obj.klassenlehrer.get(i);
				result += elem;
				if (i < obj.klassenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMKlasse>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.kuerzelAnzeige !== "undefined") {
			result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige + '"') + ',';
		}
		if (typeof obj.idJahrgang !== "undefined") {
			result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.klassenlehrer !== "undefined") {
			if (!obj.klassenlehrer) {
				result += '"klassenlehrer" : []';
			} else {
				result += '"klassenlehrer" : [ ';
				for (let i = 0; i < obj.klassenlehrer.size(); i++) {
					const elem = obj.klassenlehrer.get(i);
					result += elem;
					if (i < obj.klassenlehrer.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMKlasse(obj : unknown) : ENMKlasse {
	return obj as ENMKlasse;
}
