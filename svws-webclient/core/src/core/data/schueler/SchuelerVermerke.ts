import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerVermerke extends JavaObject {

	/**
	 * Die ID des Schülervermerks.
	 */
	public id : number = 0;

	/**
	 * Die ID des zugehörigen Schülers.
	 */
	public idSchueler : number = 0;

	/**
	 * Die ID der Vermerkart des Vermerks.
	 */
	public idVermerkart : number | null = null;

	/**
	 * Das Datum der Erstellung oder letzten Bearbeitung.
	 */
	public datum : string | null = null;

	/**
	 * Der Vermerk als Text.
	 */
	public bemerkung : string = "";

	/**
	 * Name des Benutzers welcher den Vermerk angelegt hat.
	 */
	public angelegtVon : string | null = null;

	/**
	 * Name des Benutzers welcher den Vermerk als letzten bearbeitet hat.
	 */
	public geaendertVon : string | null = null;


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
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idVermerkart = (obj.idVermerkart === undefined) ? null : obj.idVermerkart === null ? null : obj.idVermerkart;
		result.datum = (obj.datum === undefined) ? null : obj.datum === null ? null : obj.datum;
		if (obj.bemerkung === undefined)
			 throw new Error('invalid json format, missing attribute bemerkung');
		result.bemerkung = obj.bemerkung;
		result.angelegtVon = (obj.angelegtVon === undefined) ? null : obj.angelegtVon === null ? null : obj.angelegtVon;
		result.geaendertVon = (obj.geaendertVon === undefined) ? null : obj.geaendertVon === null ? null : obj.geaendertVon;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerVermerke) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"idVermerkart" : ' + ((!obj.idVermerkart) ? 'null' : obj.idVermerkart) + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung!) + ',';
		result += '"angelegtVon" : ' + ((!obj.angelegtVon) ? 'null' : JSON.stringify(obj.angelegtVon)) + ',';
		result += '"geaendertVon" : ' + ((!obj.geaendertVon) ? 'null' : JSON.stringify(obj.geaendertVon)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerVermerke>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (obj.idVermerkart !== undefined) {
			result += '"idVermerkart" : ' + ((!obj.idVermerkart) ? 'null' : obj.idVermerkart) + ',';
		}
		if (obj.datum !== undefined) {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + JSON.stringify(obj.bemerkung!) + ',';
		}
		if (obj.angelegtVon !== undefined) {
			result += '"angelegtVon" : ' + ((!obj.angelegtVon) ? 'null' : JSON.stringify(obj.angelegtVon)) + ',';
		}
		if (obj.geaendertVon !== undefined) {
			result += '"geaendertVon" : ' + ((!obj.geaendertVon) ? 'null' : JSON.stringify(obj.geaendertVon)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerVermerke(obj : unknown) : SchuelerVermerke {
	return obj as SchuelerVermerke;
}
