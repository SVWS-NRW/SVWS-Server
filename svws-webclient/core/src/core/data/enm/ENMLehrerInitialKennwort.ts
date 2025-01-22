import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMLehrerInitialKennwort extends JavaObject {

	/**
	 * Die ID des Lehrers aus der SVWS-DB (z.B. 42)
	 */
	public id : number = -1;

	/**
	 * Das Initialkennwort für die Dienst-EMail-Adresse des Lehrers
	 */
	public initialKennwort : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort'].includes(name);
	}

	public static class = new Class<ENMLehrerInitialKennwort>('de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort');

	public static transpilerFromJSON(json : string): ENMLehrerInitialKennwort {
		const obj = JSON.parse(json) as Partial<ENMLehrerInitialKennwort>;
		const result = new ENMLehrerInitialKennwort();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.initialKennwort = (obj.initialKennwort === undefined) ? null : obj.initialKennwort === null ? null : obj.initialKennwort;
		return result;
	}

	public static transpilerToJSON(obj : ENMLehrerInitialKennwort) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"initialKennwort" : ' + ((obj.initialKennwort === null) ? 'null' : JSON.stringify(obj.initialKennwort)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLehrerInitialKennwort>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.initialKennwort !== undefined) {
			result += '"initialKennwort" : ' + ((obj.initialKennwort === null) ? 'null' : JSON.stringify(obj.initialKennwort)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLehrerInitialKennwort(obj : unknown) : ENMLehrerInitialKennwort {
	return obj as ENMLehrerInitialKennwort;
}
