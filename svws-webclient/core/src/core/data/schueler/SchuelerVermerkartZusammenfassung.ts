import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerVermerkartZusammenfassung extends JavaObject {

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
	 * Die ID der entsprechenden Vermerkart
	 */
	public vermerkart : number = 0;

	/**
	 * Die Anzahl der Vermerke der entsprechenden Vermerkart
	 */
	public anzahlVermerke : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerVermerkartZusammenfassung {
		const obj = JSON.parse(json);
		const result = new SchuelerVermerkartZusammenfassung();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.vermerkart === undefined)
			 throw new Error('invalid json format, missing attribute vermerkart');
		result.vermerkart = obj.vermerkart;
		if (obj.anzahlVermerke === undefined)
			 throw new Error('invalid json format, missing attribute anzahlVermerke');
		result.anzahlVermerke = obj.anzahlVermerke;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerVermerkartZusammenfassung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"vermerkart" : ' + obj.vermerkart + ',';
		result += '"anzahlVermerke" : ' + obj.anzahlVermerke + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerVermerkartZusammenfassung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (obj.vermerkart !== undefined) {
			result += '"vermerkart" : ' + obj.vermerkart + ',';
		}
		if (obj.anzahlVermerke !== undefined) {
			result += '"anzahlVermerke" : ' + obj.anzahlVermerke + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerVermerkartZusammenfassung(obj : unknown) : SchuelerVermerkartZusammenfassung {
	return obj as SchuelerVermerkartZusammenfassung;
}
