import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMFoerderschwerpunkt extends JavaObject {

	/**
	 * Die ID des Förderschwerpunktes.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird
	 */
	public kuerzel : string | null = null;

	/**
	 * Die textuelle Bezeichnung des Förderschwerpunktes.
	 */
	public beschreibung : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMFoerderschwerpunkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMFoerderschwerpunkt'].includes(name);
	}

	public static class = new Class<ENMFoerderschwerpunkt>('de.svws_nrw.core.data.enm.ENMFoerderschwerpunkt');

	public static transpilerFromJSON(json : string): ENMFoerderschwerpunkt {
		const obj = JSON.parse(json) as Partial<ENMFoerderschwerpunkt>;
		const result = new ENMFoerderschwerpunkt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : ENMFoerderschwerpunkt) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFoerderschwerpunkt>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMFoerderschwerpunkt(obj : unknown) : ENMFoerderschwerpunkt {
	return obj as ENMFoerderschwerpunkt;
}
