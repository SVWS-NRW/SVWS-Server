import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class LernplattformV1Lerngruppe extends JavaObject {

	/**
	 * Die generierte eindeutige ID der Lerngruppe.
	 */
	public id : number = 0;

	/**
	 * Die ID der Lerngruppe in der SVWS-DB (Die ID des Kurses oder die ID der Klasse in der Versetzungstabelle, siehe kursartID).
	 */
	public kId : number = 0;

	/**
	 * Die ID des Faches der Lerngruppe.
	 */
	public fachId : number = 0;

	/**
	 * Gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassenunterricht.
	 */
	public kursartId : number | null = null;

	/**
	 * Die Bezeichnung der Lerngruppe (z.B. D-GK4)
	 */
	public bezeichnung : string | null = null;

	/**
	 * Das Kürzel der (allgemeinen) Kursart (z.B. GK)
	 */
	public kursartKuerzel : string | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale Lerngruppe handelt. (z.B. F)
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Die IDs der Lehrer, die der Lerngruppe zugeordnet sind.
	 */
	public lehrerIds : List<number> = new ArrayList<number>();

	/**
	 * Die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt.
	 */
	public wochenstunden : number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lerngruppe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lerngruppe'].includes(name);
	}

	public static class = new Class<LernplattformV1Lerngruppe>('de.svws_nrw.core.data.lernplattform.v1.LernplattformV1Lerngruppe');

	public static transpilerFromJSON(json : string): LernplattformV1Lerngruppe {
		const obj = JSON.parse(json) as Partial<LernplattformV1Lerngruppe>;
		const result = new LernplattformV1Lerngruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kId === undefined)
			throw new Error('invalid json format, missing attribute kId');
		result.kId = obj.kId;
		if (obj.fachId === undefined)
			throw new Error('invalid json format, missing attribute fachId');
		result.fachId = obj.fachId;
		result.kursartId = (obj.kursartId === undefined) ? null : obj.kursartId === null ? null : obj.kursartId;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.kursartKuerzel = (obj.kursartKuerzel === undefined) ? null : obj.kursartKuerzel === null ? null : obj.kursartKuerzel;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.lehrerIds !== undefined) {
			for (const elem of obj.lehrerIds) {
				result.lehrerIds.add(elem);
			}
		}
		result.wochenstunden = (obj.wochenstunden === undefined) ? null : obj.wochenstunden === null ? null : obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : LernplattformV1Lerngruppe) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kId" : ' + obj.kId.toString() + ',';
		result += '"fachId" : ' + obj.fachId.toString() + ',';
		result += '"kursartId" : ' + ((obj.kursartId === null) ? 'null' : obj.kursartId.toString()) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"kursartKuerzel" : ' + ((obj.kursartKuerzel === null) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"lehrerIds" : [ ';
		for (let i = 0; i < obj.lehrerIds.size(); i++) {
			const elem = obj.lehrerIds.get(i);
			result += elem.toString();
			if (i < obj.lehrerIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wochenstunden" : ' + ((obj.wochenstunden === null) ? 'null' : obj.wochenstunden.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LernplattformV1Lerngruppe>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kId !== undefined) {
			result += '"kId" : ' + obj.kId.toString() + ',';
		}
		if (obj.fachId !== undefined) {
			result += '"fachId" : ' + obj.fachId.toString() + ',';
		}
		if (obj.kursartId !== undefined) {
			result += '"kursartId" : ' + ((obj.kursartId === null) ? 'null' : obj.kursartId.toString()) + ',';
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
		if (obj.lehrerIds !== undefined) {
			result += '"lehrerIds" : [ ';
			for (let i = 0; i < obj.lehrerIds.size(); i++) {
				const elem = obj.lehrerIds.get(i);
				result += elem.toString();
				if (i < obj.lehrerIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + ((obj.wochenstunden === null) ? 'null' : obj.wochenstunden.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lernplattform_v1_LernplattformV1Lerngruppe(obj : unknown) : LernplattformV1Lerngruppe {
	return obj as LernplattformV1Lerngruppe;
}
