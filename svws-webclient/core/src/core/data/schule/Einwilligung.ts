import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Einwilligung extends JavaObject {

	/**
	 * Die ID des zugehörigen Schülers.
	 */
	public idSchueler : number = 0;

	/**
	 * Die ID der Einwilligungsart.
	 */
	public idEinwilligungsart : number = 0;

	/**
	 * Der Status der Einwilligung (erteilt/nicht erteilt).
	 */
	public status : boolean = false;

	/**
	 * Der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt).
	 */
	public abgefragt : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Einwilligung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Einwilligung'].includes(name);
	}

	public static class = new Class<Einwilligung>('de.svws_nrw.core.data.schule.Einwilligung');

	public static transpilerFromJSON(json : string): Einwilligung {
		const obj = JSON.parse(json) as Partial<Einwilligung>;
		const result = new Einwilligung();
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idEinwilligungsart === undefined)
			throw new Error('invalid json format, missing attribute idEinwilligungsart');
		result.idEinwilligungsart = obj.idEinwilligungsart;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (obj.abgefragt === undefined)
			throw new Error('invalid json format, missing attribute abgefragt');
		result.abgefragt = obj.abgefragt;
		return result;
	}

	public static transpilerToJSON(obj : Einwilligung) : string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"abgefragt" : ' + obj.abgefragt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Einwilligung>) : string {
		let result = '{';
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idEinwilligungsart !== undefined) {
			result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.abgefragt !== undefined) {
			result += '"abgefragt" : ' + obj.abgefragt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Einwilligung(obj : unknown) : Einwilligung {
	return obj as Einwilligung;
}
