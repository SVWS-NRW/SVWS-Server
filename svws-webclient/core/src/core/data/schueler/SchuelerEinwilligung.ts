import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerEinwilligung extends JavaObject {

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
		return 'de.svws_nrw.core.data.schueler.SchuelerEinwilligung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerEinwilligung'].includes(name);
	}

	public static class = new Class<SchuelerEinwilligung>('de.svws_nrw.core.data.schueler.SchuelerEinwilligung');

	public static transpilerFromJSON(json : string): SchuelerEinwilligung {
		const obj = JSON.parse(json) as Partial<SchuelerEinwilligung>;
		const result = new SchuelerEinwilligung();
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

	public static transpilerToJSON(obj : SchuelerEinwilligung) : string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"abgefragt" : ' + obj.abgefragt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerEinwilligung>) : string {
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

export function cast_de_svws_nrw_core_data_schueler_SchuelerEinwilligung(obj : unknown) : SchuelerEinwilligung {
	return obj as SchuelerEinwilligung;
}
