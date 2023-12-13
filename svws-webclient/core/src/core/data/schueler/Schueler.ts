import { JavaObject } from '../../../java/lang/JavaObject';

export class Schueler extends JavaObject {

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
	 * Der Status des Schülerdatensatzes. Gültige Werte sind im Enum-Typ {@link SchuelerStatus} definiert.
	 */
	public status : number = 0;

	/**
	 * Die ID des Geschlechtes. Gültige Werte sind im Enum-Typ {@link Geschlecht} definiert.
	 */
	public geschlecht : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.Schueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Schueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schueler {
		const obj = JSON.parse(json);
		const result = new Schueler();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.status === "undefined")
			 throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (typeof obj.geschlecht === "undefined")
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		return result;
	}

	public static transpilerToJSON(obj : Schueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"status" : ' + obj.status + ',';
		result += '"geschlecht" : ' + obj.geschlecht + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schueler>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (typeof obj.status !== "undefined") {
			result += '"status" : ' + obj.status + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + obj.geschlecht + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Schueler(obj : unknown) : Schueler {
	return obj as Schueler;
}
