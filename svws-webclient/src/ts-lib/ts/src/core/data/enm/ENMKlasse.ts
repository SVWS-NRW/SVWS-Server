import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class ENMKlasse extends JavaObject {

	/**
	 * Die ID der Klasse Jahrgangs aus der SVWS-DB (z.B. 16) 
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
	 * Die Reihenfolge der Klasse bei der Sortierung der Klasse. (z.B. 8) 
	 */
	public sortierung : number = 0;

	/**
	 * Die IDs der zugeordneten Klassenlehrer. 
	 */
	public klassenlehrer : Vector<number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMKlasse'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMKlasse {
		const obj = JSON.parse(json);
		const result = new ENMKlasse();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = typeof obj.kuerzelAnzeige === "undefined" ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (!!obj.klassenlehrer) {
			for (let elem of obj.klassenlehrer) {
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
		result += '"sortierung" : ' + obj.sortierung + ',';
		if (!obj.klassenlehrer) {
			result += '"klassenlehrer" : []';
		} else {
			result += '"klassenlehrer" : [ ';
			for (let i : number = 0; i < obj.klassenlehrer.size(); i++) {
				let elem = obj.klassenlehrer.get(i);
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
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.klassenlehrer !== "undefined") {
			if (!obj.klassenlehrer) {
				result += '"klassenlehrer" : []';
			} else {
				result += '"klassenlehrer" : [ ';
				for (let i : number = 0; i < obj.klassenlehrer.size(); i++) {
					let elem = obj.klassenlehrer.get(i);
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

export function cast_de_nrw_schule_svws_core_data_enm_ENMKlasse(obj : unknown) : ENMKlasse {
	return obj as ENMKlasse;
}
