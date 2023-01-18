import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMJahrgang extends JavaObject {

	/**
	 * Die ID des Jahrgangs aus der SVWS-DB (z.B. 16) 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF) 
	 */
	public kuerzel : String | null = null;

	/**
	 * Das Kürzel des Jahrgangs, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF) 
	 */
	public kuerzelAnzeige : String | null = null;

	/**
	 * Die textuelle Bezeichnung des Jahrgangs. (z.B. Einführungsphase) 
	 */
	public beschreibung : String | null = null;

	/**
	 * Die Stufe des Jahrgangs. (z.B. PR, SI, nur Berufskolleg: SII, Berufskolleg Anlage D und GOSt: SII-1, SII-2, SII-3) 
	 */
	public stufe : String | null = null;

	/**
	 * Die Reihenfolge des Jahrgangs bei der Sortierung der Jahrgänge. (z.B. 8) 
	 */
	public sortierung : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMJahrgang'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMJahrgang {
		const obj = JSON.parse(json);
		const result = new ENMJahrgang();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.kuerzelAnzeige = typeof obj.kuerzelAnzeige === "undefined" ? null : obj.kuerzelAnzeige === null ? null : String(obj.kuerzelAnzeige);
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung === null ? null : String(obj.beschreibung);
		result.stufe = typeof obj.stufe === "undefined" ? null : obj.stufe === null ? null : String(obj.stufe);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj : ENMJahrgang) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige.valueOf() + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		result += '"stufe" : ' + ((!obj.stufe) ? 'null' : '"' + obj.stufe.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMJahrgang>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzelAnzeige !== "undefined") {
			result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige.valueOf() + '"') + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		}
		if (typeof obj.stufe !== "undefined") {
			result += '"stufe" : ' + ((!obj.stufe) ? 'null' : '"' + obj.stufe.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMJahrgang(obj : unknown) : ENMJahrgang {
	return obj as ENMJahrgang;
}
