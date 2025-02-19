import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerSchulbesuchMerkmal extends JavaObject {

	/**
	 * Die ID des besonderen Merkmals für die Statistik.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler : number = 0;

	/**
	 * Der Kurztext des Merkmals
	 */
	public kurztext : string | null = null;

	/**
	 * Das Datum, ab dem das Merkmal vorliegt.
	 */
	public datumVon : string | null = null;

	/**
	 * Das Datum, bis wann das Merkmal vorliegt.
	 */
	public datumBis : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal'].includes(name);
	}

	public static class = new Class<SchuelerSchulbesuchMerkmal>('de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal');

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchMerkmal {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchMerkmal>;
		const result = new SchuelerSchulbesuchMerkmal();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.kurztext = (obj.kurztext === undefined) ? null : obj.kurztext === null ? null : obj.kurztext;
		result.datumVon = (obj.datumVon === undefined) ? null : obj.datumVon === null ? null : obj.datumVon;
		result.datumBis = (obj.datumBis === undefined) ? null : obj.datumBis === null ? null : obj.datumBis;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchMerkmal) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"kurztext" : ' + ((obj.kurztext === null) ? 'null' : JSON.stringify(obj.kurztext)) + ',';
		result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchMerkmal>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.kurztext !== undefined) {
			result += '"kurztext" : ' + ((obj.kurztext === null) ? 'null' : JSON.stringify(obj.kurztext)) + ',';
		}
		if (obj.datumVon !== undefined) {
			result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		}
		if (obj.datumBis !== undefined) {
			result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerSchulbesuchMerkmal(obj : unknown) : SchuelerSchulbesuchMerkmal {
	return obj as SchuelerSchulbesuchMerkmal;
}
