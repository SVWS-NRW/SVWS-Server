import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMFloskel extends JavaObject {

	/**
	 * Das Kürzel der Floskel.
	 */
	public kuerzel : string | null = null;

	/**
	 * Der Text der Floskel.
	 */
	public text : string | null = null;

	/**
	 * Die ID des Faches, dem die Floskel zugeordnet ist, sofern die Floskel einem Fach
	 *  zugeordnet wurde, ansonsten null.
	 */
	public fachID : number | null = null;

	/**
	 * Eine den Notenstufen ähnliche Kategorisierung
	 */
	public niveau : number | null = null;

	/**
	 * Die ID des Jahrganges, dem die Floskel zugeordnet ist, falls die Floskel einem Fach
	 *  zugeordnet wurde, ansonsten null, falls sie für alle Jahrgänge gilt.
	 */
	public jahrgangID : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMFloskel';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMFloskel'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFloskel {
		const obj = JSON.parse(json);
		const result = new ENMFloskel();
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.text = typeof obj.text === "undefined" ? null : obj.text === null ? null : obj.text;
		result.fachID = typeof obj.fachID === "undefined" ? null : obj.fachID === null ? null : obj.fachID;
		result.niveau = typeof obj.niveau === "undefined" ? null : obj.niveau === null ? null : obj.niveau;
		result.jahrgangID = typeof obj.jahrgangID === "undefined" ? null : obj.jahrgangID === null ? null : obj.jahrgangID;
		return result;
	}

	public static transpilerToJSON(obj : ENMFloskel) : string {
		let result = '{';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : JSON.stringify(obj.text)) + ',';
		result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID) + ',';
		result += '"niveau" : ' + ((!obj.niveau) ? 'null' : obj.niveau) + ',';
		result += '"jahrgangID" : ' + ((!obj.jahrgangID) ? 'null' : obj.jahrgangID) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFloskel>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + ((!obj.text) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID) + ',';
		}
		if (typeof obj.niveau !== "undefined") {
			result += '"niveau" : ' + ((!obj.niveau) ? 'null' : obj.niveau) + ',';
		}
		if (typeof obj.jahrgangID !== "undefined") {
			result += '"jahrgangID" : ' + ((!obj.jahrgangID) ? 'null' : obj.jahrgangID) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMFloskel(obj : unknown) : ENMFloskel {
	return obj as ENMFloskel;
}
