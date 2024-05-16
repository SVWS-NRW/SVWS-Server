import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerVermerke extends JavaObject {

	/**
	 * ID des Datensatzes
	 */
	public id : number = 0;

	/**
	 * ID des Schülers
	 */
	public schueler_id : number = 0;

	/**
	 * AdressID des Betriebeeintrags beim Schüler
	 */
	public VermerkArt_ID : number = 0;

	/**
	 * ID der Beschäftigungsart des Schülers
	 */
	public Datum : string | null = null;

	/**
	 * TODO
	 */
	public Bemerkung : string | null = null;

	/**
	 * TODO
	 */
	public AngelegtVon : string | null = null;

	/**
	 * TODO
	 */
	public GeaendertVon : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerVermerke';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerVermerke'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerVermerke {
		const obj = JSON.parse(json);
		const result = new SchuelerVermerke();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schueler_id === "undefined")
			 throw new Error('invalid json format, missing attribute schueler_id');
		result.schueler_id = obj.schueler_id;
		if (typeof obj.VermerkArt_ID === "undefined")
			 throw new Error('invalid json format, missing attribute VermerkArt_ID');
		result.VermerkArt_ID = obj.VermerkArt_ID;
		result.Datum = typeof obj.Datum === "undefined" ? null : obj.Datum === null ? null : obj.Datum;
		result.Bemerkung = typeof obj.Bemerkung === "undefined" ? null : obj.Bemerkung === null ? null : obj.Bemerkung;
		result.AngelegtVon = typeof obj.AngelegtVon === "undefined" ? null : obj.AngelegtVon === null ? null : obj.AngelegtVon;
		result.GeaendertVon = typeof obj.GeaendertVon === "undefined" ? null : obj.GeaendertVon === null ? null : obj.GeaendertVon;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerVermerke) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schueler_id" : ' + obj.schueler_id + ',';
		result += '"VermerkArt_ID" : ' + obj.VermerkArt_ID + ',';
		result += '"Datum" : ' + ((!obj.Datum) ? 'null' : JSON.stringify(obj.Datum)) + ',';
		result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : JSON.stringify(obj.Bemerkung)) + ',';
		result += '"AngelegtVon" : ' + ((!obj.AngelegtVon) ? 'null' : JSON.stringify(obj.AngelegtVon)) + ',';
		result += '"GeaendertVon" : ' + ((!obj.GeaendertVon) ? 'null' : JSON.stringify(obj.GeaendertVon)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerVermerke>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schueler_id !== "undefined") {
			result += '"schueler_id" : ' + obj.schueler_id + ',';
		}
		if (typeof obj.VermerkArt_ID !== "undefined") {
			result += '"VermerkArt_ID" : ' + obj.VermerkArt_ID + ',';
		}
		if (typeof obj.Datum !== "undefined") {
			result += '"Datum" : ' + ((!obj.Datum) ? 'null' : JSON.stringify(obj.Datum)) + ',';
		}
		if (typeof obj.Bemerkung !== "undefined") {
			result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : JSON.stringify(obj.Bemerkung)) + ',';
		}
		if (typeof obj.AngelegtVon !== "undefined") {
			result += '"AngelegtVon" : ' + ((!obj.AngelegtVon) ? 'null' : JSON.stringify(obj.AngelegtVon)) + ',';
		}
		if (typeof obj.GeaendertVon !== "undefined") {
			result += '"GeaendertVon" : ' + ((!obj.GeaendertVon) ? 'null' : JSON.stringify(obj.GeaendertVon)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerVermerke(obj : unknown) : SchuelerVermerke {
	return obj as SchuelerVermerke;
}
