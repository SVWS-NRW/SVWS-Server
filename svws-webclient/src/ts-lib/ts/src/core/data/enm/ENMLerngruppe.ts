import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class ENMLerngruppe extends JavaObject {

	/**
	 * Die eindeutige ID der Lerngruppe - generiert, nicht (!) aus der SVWS-DB 
	 */
	public id : number = 0;

	/**
	 * Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der Versetzungstabelle, siehe kursartID). 
	 */
	public kID : number = 0;

	/**
	 * Die ID des Faches der Lerngruppe. 
	 */
	public fachID : number = 0;

	/**
	 * Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassen-Unterricht. 
	 */
	public kursartID : Number | null = null;

	/**
	 * Die Bezeichnung der Lerngruppe (z.B. D-GK4) 
	 */
	public bezeichnung : String | null = null;

	/**
	 * Die Bezeichnung der (allgemeinen) Kursart (z.B. GK) 
	 */
	public kursartKuerzel : String | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale Lerngruppe handelt. (z.B. F) 
	 */
	public bilingualeSprache : String | null = null;

	/**
	 * Die IDs der Lehrer, die der Lerngruppe zugeordnet sind. 
	 */
	public lehrerID : Vector<Number> = new Vector();

	/**
	 * Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt. 
	 */
	public wochenstunden : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMLerngruppe'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLerngruppe {
		const obj = JSON.parse(json);
		const result = new ENMLerngruppe();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kID === "undefined")
			 throw new Error('invalid json format, missing attribute kID');
		result.kID = obj.kID;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursartID = typeof obj.kursartID === "undefined" ? null : obj.kursartID === null ? null : Number(obj.kursartID);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		result.kursartKuerzel = typeof obj.kursartKuerzel === "undefined" ? null : obj.kursartKuerzel === null ? null : String(obj.kursartKuerzel);
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : String(obj.bilingualeSprache);
		if (!!obj.lehrerID) {
			for (let elem of obj.lehrerID) {
				result.lehrerID?.add(Number(elem));
			}
		}
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : ENMLerngruppe) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kID" : ' + obj.kID + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"kursartID" : ' + ((!obj.kursartID) ? 'null' : obj.kursartID.valueOf()) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : '"' + obj.kursartKuerzel.valueOf() + '"') + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		if (!obj.lehrerID) {
			result += '"lehrerID" : []';
		} else {
			result += '"lehrerID" : [ ';
			for (let i : number = 0; i < obj.lehrerID.size(); i++) {
				let elem = obj.lehrerID.get(i);
				result += elem;
				if (i < obj.lehrerID.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLerngruppe>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kID !== "undefined") {
			result += '"kID" : ' + obj.kID + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.kursartID !== "undefined") {
			result += '"kursartID" : ' + ((!obj.kursartID) ? 'null' : obj.kursartID.valueOf()) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.kursartKuerzel !== "undefined") {
			result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : '"' + obj.kursartKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.lehrerID !== "undefined") {
			if (!obj.lehrerID) {
				result += '"lehrerID" : []';
			} else {
				result += '"lehrerID" : [ ';
				for (let i : number = 0; i < obj.lehrerID.size(); i++) {
					let elem = obj.lehrerID.get(i);
					result += elem;
					if (i < obj.lehrerID.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLerngruppe(obj : unknown) : ENMLerngruppe {
	return obj as ENMLerngruppe;
}
