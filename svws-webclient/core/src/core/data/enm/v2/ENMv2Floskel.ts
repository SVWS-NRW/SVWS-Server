import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Floskel extends JavaObject {

	/**
	 * Das Kürzel der Floskel.
	 */
	public kuerzel: string | null = null;

	/**
	 * Der Text der Floskel.
	 */
	public text: string | null = null;

	/**
	 * Die ID des Faches, dem die Floskel zugeordnet ist, sofern die Floskel einem Fach
	 *  zugeordnet wurde, ansonsten null.
	 */
	public fachID: number | null = null;

	/**
	 * Eine den Notenstufen ähnliche Kategorisierung
	 */
	public niveau: number | null = null;

	/**
	 * Die ID des Jahrganges, dem die Floskel zugeordnet ist, falls die Floskel einem Fach
	 *  zugeordnet wurde, ansonsten null, falls sie für alle Jahrgänge gilt.
	 */
	public jahrgangID: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Floskel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Floskel'].includes(name);
	}

	public static readonly class = new Class<ENMv2Floskel>('de.svws_nrw.core.data.enm.v2.ENMv2Floskel');

	public static transpilerFromJSON(json: string): ENMv2Floskel {
		const obj = JSON.parse(json) as Partial<ENMv2Floskel>;
		const result = new ENMv2Floskel();
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.text = (obj.text === undefined) ? null : obj.text === null ? null : obj.text;
		result.fachID = (obj.fachID === undefined) ? null : obj.fachID === null ? null : obj.fachID;
		result.niveau = (obj.niveau === undefined) ? null : obj.niveau === null ? null : obj.niveau;
		result.jahrgangID = (obj.jahrgangID === undefined) ? null : obj.jahrgangID === null ? null : obj.jahrgangID;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Floskel): string {
		let result = '{';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		result += '"fachID" : ' + ((obj.fachID === null) ? 'null' : obj.fachID.toString()) + ',';
		result += '"niveau" : ' + ((obj.niveau === null) ? 'null' : obj.niveau.toString()) + ',';
		result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Floskel>): string {
		let result = '{';
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + ((obj.fachID === null) ? 'null' : obj.fachID.toString()) + ',';
		}
		if (obj.niveau !== undefined) {
			result += '"niveau" : ' + ((obj.niveau === null) ? 'null' : obj.niveau.toString()) + ',';
		}
		if (obj.jahrgangID !== undefined) {
			result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Floskel(obj: unknown): ENMv2Floskel {
	return obj as ENMv2Floskel;
}
