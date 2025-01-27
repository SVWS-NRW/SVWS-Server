import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerEinwilligungsartenZusammenfassung extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id : number = 0;

	/**
	 * Der Nachname des Schülerdatensatzes.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülerdatensatzes.
	 */
	public vorname : string = "";

	/**
	 * Die ID der entsprechenden Einwilligungsart
	 */
	public idEinwilligungsart : number = 0;

	/**
	 * Die Anzahl der Einwilligungen der entsprechenden Einwilligungsart
	 */
	public anzahlEinwilligungen : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung'].includes(name);
	}

	public static class = new Class<SchuelerEinwilligungsartenZusammenfassung>('de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung');

	public static transpilerFromJSON(json : string): SchuelerEinwilligungsartenZusammenfassung {
		const obj = JSON.parse(json) as Partial<SchuelerEinwilligungsartenZusammenfassung>;
		const result = new SchuelerEinwilligungsartenZusammenfassung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.idEinwilligungsart === undefined)
			throw new Error('invalid json format, missing attribute idEinwilligungsart');
		result.idEinwilligungsart = obj.idEinwilligungsart;
		if (obj.anzahlEinwilligungen === undefined)
			throw new Error('invalid json format, missing attribute anzahlEinwilligungen');
		result.anzahlEinwilligungen = obj.anzahlEinwilligungen;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerEinwilligungsartenZusammenfassung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		result += '"anzahlEinwilligungen" : ' + obj.anzahlEinwilligungen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerEinwilligungsartenZusammenfassung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.idEinwilligungsart !== undefined) {
			result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		}
		if (obj.anzahlEinwilligungen !== undefined) {
			result += '"anzahlEinwilligungen" : ' + obj.anzahlEinwilligungen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerEinwilligungsartenZusammenfassung(obj : unknown) : SchuelerEinwilligungsartenZusammenfassung {
	return obj as SchuelerEinwilligungsartenZusammenfassung;
}
