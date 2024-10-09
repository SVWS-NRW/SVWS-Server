import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMSchuelerAnkreuzkompetenz extends JavaObject {

	/**
	 * Die ID des Eintrages aus der SVWS-DB
	 */
	public id : number = -1;

	/**
	 * Die ID der der Ankreuzkompetenz, auf welches sich der Eintrag bezieht
	 */
	public fachID : number | null = null;

	/**
	 * Gibt an, ob Stufe 1 bei der Ankreuzkompetenz zugewiesen ist oder nicht.
	 */
	public stufe1 : boolean = false;

	/**
	 * Gibt an, ob Stufe 2 bei der Ankreuzkompetenz zugewiesen ist oder nicht.
	 */
	public stufe2 : boolean = false;

	/**
	 * Gibt an, ob Stufe 3 bei der Ankreuzkompetenz zugewiesen ist oder nicht.
	 */
	public stufe3 : boolean = false;

	/**
	 * Gibt an, ob Stufe 4 bei der Ankreuzkompetenz zugewiesen ist oder nicht.
	 */
	public stufe4 : boolean = false;

	/**
	 * Gibt an, ob Stufe 5 bei der Ankreuzkompetenz zugewiesen ist oder nicht.
	 */
	public stufe5 : boolean = false;

	/**
	 * Der Zeitstempel der letzten Änderung an den zugewiesenen Stufen
	 */
	public tsStufe : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz'].includes(name);
	}

	public static class = new Class<ENMSchuelerAnkreuzkompetenz>('de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz');

	public static transpilerFromJSON(json : string): ENMSchuelerAnkreuzkompetenz {
		const obj = JSON.parse(json) as Partial<ENMSchuelerAnkreuzkompetenz>;
		const result = new ENMSchuelerAnkreuzkompetenz();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.fachID = (obj.fachID === undefined) ? null : obj.fachID === null ? null : obj.fachID;
		if (obj.stufe1 === undefined)
			throw new Error('invalid json format, missing attribute stufe1');
		result.stufe1 = obj.stufe1;
		if (obj.stufe2 === undefined)
			throw new Error('invalid json format, missing attribute stufe2');
		result.stufe2 = obj.stufe2;
		if (obj.stufe3 === undefined)
			throw new Error('invalid json format, missing attribute stufe3');
		result.stufe3 = obj.stufe3;
		if (obj.stufe4 === undefined)
			throw new Error('invalid json format, missing attribute stufe4');
		result.stufe4 = obj.stufe4;
		if (obj.stufe5 === undefined)
			throw new Error('invalid json format, missing attribute stufe5');
		result.stufe5 = obj.stufe5;
		result.tsStufe = (obj.tsStufe === undefined) ? null : obj.tsStufe === null ? null : obj.tsStufe;
		return result;
	}

	public static transpilerToJSON(obj : ENMSchuelerAnkreuzkompetenz) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.toString()) + ',';
		result += '"stufe1" : ' + obj.stufe1.toString() + ',';
		result += '"stufe2" : ' + obj.stufe2.toString() + ',';
		result += '"stufe3" : ' + obj.stufe3.toString() + ',';
		result += '"stufe4" : ' + obj.stufe4.toString() + ',';
		result += '"stufe5" : ' + obj.stufe5.toString() + ',';
		result += '"tsStufe" : ' + ((!obj.tsStufe) ? 'null' : JSON.stringify(obj.tsStufe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSchuelerAnkreuzkompetenz>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.toString()) + ',';
		}
		if (obj.stufe1 !== undefined) {
			result += '"stufe1" : ' + obj.stufe1.toString() + ',';
		}
		if (obj.stufe2 !== undefined) {
			result += '"stufe2" : ' + obj.stufe2.toString() + ',';
		}
		if (obj.stufe3 !== undefined) {
			result += '"stufe3" : ' + obj.stufe3.toString() + ',';
		}
		if (obj.stufe4 !== undefined) {
			result += '"stufe4" : ' + obj.stufe4.toString() + ',';
		}
		if (obj.stufe5 !== undefined) {
			result += '"stufe5" : ' + obj.stufe5.toString() + ',';
		}
		if (obj.tsStufe !== undefined) {
			result += '"tsStufe" : ' + ((!obj.tsStufe) ? 'null' : JSON.stringify(obj.tsStufe)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMSchuelerAnkreuzkompetenz(obj : unknown) : ENMSchuelerAnkreuzkompetenz {
	return obj as ENMSchuelerAnkreuzkompetenz;
}
