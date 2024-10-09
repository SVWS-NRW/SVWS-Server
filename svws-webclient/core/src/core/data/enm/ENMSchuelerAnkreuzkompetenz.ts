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
	 * Die zugewiesene Stufe (1-5) bei der Ankreuzkompetenz oder null, falls noch keine Stufe zugewiesen wurde.
	 */
	public stufe : number | null = null;


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
		result.stufe = (obj.stufe === undefined) ? null : obj.stufe === null ? null : obj.stufe;
		return result;
	}

	public static transpilerToJSON(obj : ENMSchuelerAnkreuzkompetenz) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.toString()) + ',';
		result += '"stufe" : ' + ((!obj.stufe) ? 'null' : obj.stufe.toString()) + ',';
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
		if (obj.stufe !== undefined) {
			result += '"stufe" : ' + ((!obj.stufe) ? 'null' : obj.stufe.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMSchuelerAnkreuzkompetenz(obj : unknown) : ENMSchuelerAnkreuzkompetenz {
	return obj as ENMSchuelerAnkreuzkompetenz;
}
