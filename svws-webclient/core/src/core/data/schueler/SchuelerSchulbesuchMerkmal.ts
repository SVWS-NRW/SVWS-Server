import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerSchulbesuchMerkmal extends JavaObject {

	/**
	 * Die ID des besonderen Merkmals für die Statistik.
	 */
	public id : number = 0;

	/**
	 * Das Datum, ab dem das Merkmal vorliegt.
	 */
	public datumVon : string | null = null;

	/**
	 * Das Datum, bis wann das Merkmal vorliegt.
	 */
	public datumBis : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchMerkmal {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchMerkmal>;
		const result = new SchuelerSchulbesuchMerkmal();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.datumVon = (obj.datumVon === undefined) ? null : obj.datumVon === null ? null : obj.datumVon;
		result.datumBis = (obj.datumBis === undefined) ? null : obj.datumBis === null ? null : obj.datumBis;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchMerkmal) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchMerkmal>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.datumVon !== undefined) {
			result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		}
		if (obj.datumBis !== undefined) {
			result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerSchulbesuchMerkmal(obj : unknown) : SchuelerSchulbesuchMerkmal {
	return obj as SchuelerSchulbesuchMerkmal;
}
