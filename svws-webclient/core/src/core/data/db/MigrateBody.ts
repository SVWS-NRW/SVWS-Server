import { JavaObject } from '../../../java/lang/JavaObject';

export class MigrateBody extends JavaObject {

	/**
	 * Gibt den Benutzernamen für die Quelldatenbank an.
	 */
	public srcUsername : string | null = null;

	/**
	 * Gibt das Kennwort für die Quelldatenbank an.
	 */
	public srcPassword : string | null = null;

	/**
	 * Gibt den Ort der Quelldatenbank an.
	 */
	public srcLocation : string | null = null;

	/**
	 * Gibt den Schema-Namen der Quelldatenbank.
	 */
	public srcSchema : string | null = null;

	/**
	 * Der Benutzername für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema.
	 */
	public schemaUsername : string | null = null;

	/**
	 * Das Kennwort für den administrativen Zugang auf das neu zu erstellende Datenbank-Schema.
	 */
	public schemaUserPassword : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.db.MigrateBody'].includes(name);
	}

	public static transpilerFromJSON(json : string): MigrateBody {
		const obj = JSON.parse(json);
		const result = new MigrateBody();
		result.srcUsername = typeof obj.srcUsername === "undefined" ? null : obj.srcUsername === null ? null : obj.srcUsername;
		result.srcPassword = typeof obj.srcPassword === "undefined" ? null : obj.srcPassword === null ? null : obj.srcPassword;
		result.srcLocation = typeof obj.srcLocation === "undefined" ? null : obj.srcLocation === null ? null : obj.srcLocation;
		result.srcSchema = typeof obj.srcSchema === "undefined" ? null : obj.srcSchema === null ? null : obj.srcSchema;
		result.schemaUsername = typeof obj.schemaUsername === "undefined" ? null : obj.schemaUsername === null ? null : obj.schemaUsername;
		result.schemaUserPassword = typeof obj.schemaUserPassword === "undefined" ? null : obj.schemaUserPassword === null ? null : obj.schemaUserPassword;
		return result;
	}

	public static transpilerToJSON(obj : MigrateBody) : string {
		let result = '{';
		result += '"srcUsername" : ' + ((!obj.srcUsername) ? 'null' : JSON.stringify(obj.srcUsername)) + ',';
		result += '"srcPassword" : ' + ((!obj.srcPassword) ? 'null' : JSON.stringify(obj.srcPassword)) + ',';
		result += '"srcLocation" : ' + ((!obj.srcLocation) ? 'null' : JSON.stringify(obj.srcLocation)) + ',';
		result += '"srcSchema" : ' + ((!obj.srcSchema) ? 'null' : JSON.stringify(obj.srcSchema)) + ',';
		result += '"schemaUsername" : ' + ((!obj.schemaUsername) ? 'null' : JSON.stringify(obj.schemaUsername)) + ',';
		result += '"schemaUserPassword" : ' + ((!obj.schemaUserPassword) ? 'null' : JSON.stringify(obj.schemaUserPassword)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<MigrateBody>) : string {
		let result = '{';
		if (typeof obj.srcUsername !== "undefined") {
			result += '"srcUsername" : ' + ((!obj.srcUsername) ? 'null' : JSON.stringify(obj.srcUsername)) + ',';
		}
		if (typeof obj.srcPassword !== "undefined") {
			result += '"srcPassword" : ' + ((!obj.srcPassword) ? 'null' : JSON.stringify(obj.srcPassword)) + ',';
		}
		if (typeof obj.srcLocation !== "undefined") {
			result += '"srcLocation" : ' + ((!obj.srcLocation) ? 'null' : JSON.stringify(obj.srcLocation)) + ',';
		}
		if (typeof obj.srcSchema !== "undefined") {
			result += '"srcSchema" : ' + ((!obj.srcSchema) ? 'null' : JSON.stringify(obj.srcSchema)) + ',';
		}
		if (typeof obj.schemaUsername !== "undefined") {
			result += '"schemaUsername" : ' + ((!obj.schemaUsername) ? 'null' : JSON.stringify(obj.schemaUsername)) + ',';
		}
		if (typeof obj.schemaUserPassword !== "undefined") {
			result += '"schemaUserPassword" : ' + ((!obj.schemaUserPassword) ? 'null' : JSON.stringify(obj.schemaUserPassword)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_db_MigrateBody(obj : unknown) : MigrateBody {
	return obj as MigrateBody;
}
