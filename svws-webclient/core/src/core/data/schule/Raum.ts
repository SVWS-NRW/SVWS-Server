import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Raum extends JavaObject {

	/**
	 * Die ID des Raumes.
	 */
	public id : number = -1;

	/**
	 * Das Raumkürzel.
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung des Raumes.
	 */
	public beschreibung : string = "";

	/**
	 * Die Grösse des Raumes an Arbeitsplätzen für Schüler.
	 */
	public groesse : number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Raum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Raum'].includes(name);
	}

	public static class = new Class<Raum>('de.svws_nrw.core.data.schule.Raum');

	public static transpilerFromJSON(json : string): Raum {
		const obj = JSON.parse(json) as Partial<Raum>;
		const result = new Raum();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.groesse === undefined)
			throw new Error('invalid json format, missing attribute groesse');
		result.groesse = obj.groesse;
		return result;
	}

	public static transpilerToJSON(obj : Raum) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"groesse" : ' + obj.groesse.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Raum>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.groesse !== undefined) {
			result += '"groesse" : ' + obj.groesse.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Raum(obj : unknown) : Raum {
	return obj as Raum;
}
