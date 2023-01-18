import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMLehrer extends JavaObject {

	/**
	 * Die ID des Lehrers aus der SVWS-DB (z.B. 42) 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers für die Anzeige im Notenmodel (z.B. Mus) 
	 */
	public kuerzel : String | null = null;

	/**
	 * Der Nachname des Lehrers (z.B. Mustermann) 
	 */
	public nachname : String | null = null;

	/**
	 * Der Vorname des Lehrers (z.B. Max) 
	 */
	public vorname : String | null = null;

	/**
	 * Das Geschlecht des Lehrers (m,w,d,x) 
	 */
	public geschlecht : String | null = null;

	/**
	 * Die Dienst-EMail-Adresse des Lehrers 
	 */
	public eMailDienstlich : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMLehrer'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLehrer {
		const obj = JSON.parse(json);
		const result = new ENMLehrer();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname === null ? null : String(obj.nachname);
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname === null ? null : String(obj.vorname);
		result.geschlecht = typeof obj.geschlecht === "undefined" ? null : obj.geschlecht === null ? null : String(obj.geschlecht);
		result.eMailDienstlich = typeof obj.eMailDienstlich === "undefined" ? null : obj.eMailDienstlich === null ? null : String(obj.eMailDienstlich);
		return result;
	}

	public static transpilerToJSON(obj : ENMLehrer) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : '"' + obj.geschlecht.valueOf() + '"') + ',';
		result += '"eMailDienstlich" : ' + ((!obj.eMailDienstlich) ? 'null' : '"' + obj.eMailDienstlich.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLehrer>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + ((!obj.geschlecht) ? 'null' : '"' + obj.geschlecht.valueOf() + '"') + ',';
		}
		if (typeof obj.eMailDienstlich !== "undefined") {
			result += '"eMailDienstlich" : ' + ((!obj.eMailDienstlich) ? 'null' : '"' + obj.eMailDienstlich.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLehrer(obj : unknown) : ENMLehrer {
	return obj as ENMLehrer;
}
