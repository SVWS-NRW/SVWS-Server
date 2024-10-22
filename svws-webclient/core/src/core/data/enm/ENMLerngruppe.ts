import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

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
	public kursartID : number | null = null;

	/**
	 * Die Bezeichnung der Lerngruppe (z.B. D-GK4)
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Bezeichnung der (allgemeinen) Kursart (z.B. GK)
	 */
	public kursartKuerzel : string | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale Lerngruppe handelt. (z.B. F)
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Die IDs der Lehrer, die der Lerngruppe zugeordnet sind.
	 */
	public lehrerID : List<number> = new ArrayList<number>();

	/**
	 * Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt.
	 */
	public wochenstunden : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLerngruppe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLerngruppe'].includes(name);
	}

	public static class = new Class<ENMLerngruppe>('de.svws_nrw.core.data.enm.ENMLerngruppe');

	public static transpilerFromJSON(json : string): ENMLerngruppe {
		const obj = JSON.parse(json) as Partial<ENMLerngruppe>;
		const result = new ENMLerngruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kID === undefined)
			throw new Error('invalid json format, missing attribute kID');
		result.kID = obj.kID;
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursartID = (obj.kursartID === undefined) ? null : obj.kursartID === null ? null : obj.kursartID;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.kursartKuerzel = (obj.kursartKuerzel === undefined) ? null : obj.kursartKuerzel === null ? null : obj.kursartKuerzel;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.lehrerID !== undefined) {
			for (const elem of obj.lehrerID) {
				result.lehrerID.add(elem);
			}
		}
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : ENMLerngruppe) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kID" : ' + obj.kID.toString() + ',';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"kursartID" : ' + ((obj.kursartID === null) ? 'null' : obj.kursartID.toString()) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"kursartKuerzel" : ' + ((obj.kursartKuerzel === null) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"lehrerID" : [ ';
		for (let i = 0; i < obj.lehrerID.size(); i++) {
			const elem = obj.lehrerID.get(i);
			result += elem.toString();
			if (i < obj.lehrerID.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLerngruppe>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kID !== undefined) {
			result += '"kID" : ' + obj.kID.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.kursartID !== undefined) {
			result += '"kursartID" : ' + ((obj.kursartID === null) ? 'null' : obj.kursartID.toString()) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.kursartKuerzel !== undefined) {
			result += '"kursartKuerzel" : ' + ((obj.kursartKuerzel === null) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : [ ';
			for (let i = 0; i < obj.lehrerID.size(); i++) {
				const elem = obj.lehrerID.get(i);
				result += elem.toString();
				if (i < obj.lehrerID.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLerngruppe(obj : unknown) : ENMLerngruppe {
	return obj as ENMLerngruppe;
}
