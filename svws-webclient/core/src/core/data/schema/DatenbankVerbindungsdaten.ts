import { JavaObject } from '../../../java/lang/JavaObject';

export class DatenbankVerbindungsdaten extends JavaObject {

	/**
	 * Gibt den Benutzernamen für die Datenbank an.
	 */
	public username : string | null = null;

	/**
	 * Gibt das Kennwort für die Datenbank an.
	 */
	public password : string | null = null;

	/**
	 * Gibt den Ort der Datenbank an.
	 */
	public location : string | null = null;

	/**
	 * Gibt den Schema-Namen der Datenbank an.
	 */
	public schema : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schema.DatenbankVerbindungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schema.DatenbankVerbindungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): DatenbankVerbindungsdaten {
		const obj = JSON.parse(json);
		const result = new DatenbankVerbindungsdaten();
		result.username = (obj.username === undefined) ? null : obj.username === null ? null : obj.username;
		result.password = (obj.password === undefined) ? null : obj.password === null ? null : obj.password;
		result.location = (obj.location === undefined) ? null : obj.location === null ? null : obj.location;
		result.schema = (obj.schema === undefined) ? null : obj.schema === null ? null : obj.schema;
		return result;
	}

	public static transpilerToJSON(obj : DatenbankVerbindungsdaten) : string {
		let result = '{';
		result += '"username" : ' + ((!obj.username) ? 'null' : JSON.stringify(obj.username)) + ',';
		result += '"password" : ' + ((!obj.password) ? 'null' : JSON.stringify(obj.password)) + ',';
		result += '"location" : ' + ((!obj.location) ? 'null' : JSON.stringify(obj.location)) + ',';
		result += '"schema" : ' + ((!obj.schema) ? 'null' : JSON.stringify(obj.schema)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DatenbankVerbindungsdaten>) : string {
		let result = '{';
		if (obj.username !== undefined) {
			result += '"username" : ' + ((!obj.username) ? 'null' : JSON.stringify(obj.username)) + ',';
		}
		if (obj.password !== undefined) {
			result += '"password" : ' + ((!obj.password) ? 'null' : JSON.stringify(obj.password)) + ',';
		}
		if (obj.location !== undefined) {
			result += '"location" : ' + ((!obj.location) ? 'null' : JSON.stringify(obj.location)) + ',';
		}
		if (obj.schema !== undefined) {
			result += '"schema" : ' + ((!obj.schema) ? 'null' : JSON.stringify(obj.schema)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schema_DatenbankVerbindungsdaten(obj : unknown) : DatenbankVerbindungsdaten {
	return obj as DatenbankVerbindungsdaten;
}
