import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Merkmal extends JavaObject {

	/**
	 * Die ID des Merkmals
	 */
	public id : number = 0;

	/**
	 * Gibt an, ob das Merkmal einer Schule zugewiesen werden kann
	 */
	public istSchulmerkmal : boolean = false;

	/**
	 * Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann
	 */
	public istSchuelermerkmal : boolean = false;

	/**
	 * Der Kurztext des Merkmals
	 */
	public kurztext : string | null = null;

	/**
	 * Der Langtext des Merkmals
	 */
	public langtext : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Merkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Merkmal'].includes(name);
	}

	public static class = new Class<Merkmal>('de.svws_nrw.core.data.schule.Merkmal');

	public static transpilerFromJSON(json : string): Merkmal {
		const obj = JSON.parse(json) as Partial<Merkmal>;
		const result = new Merkmal();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.istSchulmerkmal === undefined)
			throw new Error('invalid json format, missing attribute istSchulmerkmal');
		result.istSchulmerkmal = obj.istSchulmerkmal;
		if (obj.istSchuelermerkmal === undefined)
			throw new Error('invalid json format, missing attribute istSchuelermerkmal');
		result.istSchuelermerkmal = obj.istSchuelermerkmal;
		result.kurztext = (obj.kurztext === undefined) ? null : obj.kurztext === null ? null : obj.kurztext;
		result.langtext = (obj.langtext === undefined) ? null : obj.langtext === null ? null : obj.langtext;
		return result;
	}

	public static transpilerToJSON(obj : Merkmal) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"istSchulmerkmal" : ' + obj.istSchulmerkmal.toString() + ',';
		result += '"istSchuelermerkmal" : ' + obj.istSchuelermerkmal.toString() + ',';
		result += '"kurztext" : ' + ((obj.kurztext === null) ? 'null' : JSON.stringify(obj.kurztext)) + ',';
		result += '"langtext" : ' + ((obj.langtext === null) ? 'null' : JSON.stringify(obj.langtext)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Merkmal>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.istSchulmerkmal !== undefined) {
			result += '"istSchulmerkmal" : ' + obj.istSchulmerkmal.toString() + ',';
		}
		if (obj.istSchuelermerkmal !== undefined) {
			result += '"istSchuelermerkmal" : ' + obj.istSchuelermerkmal.toString() + ',';
		}
		if (obj.kurztext !== undefined) {
			result += '"kurztext" : ' + ((obj.kurztext === null) ? 'null' : JSON.stringify(obj.kurztext)) + ',';
		}
		if (obj.langtext !== undefined) {
			result += '"langtext" : ' + ((obj.langtext === null) ? 'null' : JSON.stringify(obj.langtext)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Merkmal(obj : unknown) : Merkmal {
	return obj as Merkmal;
}
