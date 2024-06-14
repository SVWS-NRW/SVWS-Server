import { JavaObject } from '../../../java/lang/JavaObject';

export class SchemaListeEintrag extends JavaObject {

	/**
	 * Der Name des Schemas.
	 */
	public name : string = "";

	/**
	 * Der Name des Datenbank-Benutzers für das Schema.
	 */
	public username : string = "";

	/**
	 * Gibt an, ob das Schema ein SVWS-Schema ist oder nicht.
	 */
	public isSVWS : boolean = false;

	/**
	 * Die Revisionsnummer des Schemas.
	 */
	public revision : number = -1;

	/**
	 * Gibt an, ob das Schema als "verdorben" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte.
	 */
	public isTainted : boolean = false;

	/**
	 * Gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers eingetragen ist.
	 */
	public isInConfig : boolean = false;

	/**
	 * Gibt an, ob das Schema in der Konfiguration des aktuellen SVWS-Servers aufgrund von Fehlern deaktiviert ist.
	 */
	public isDeactivated : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.db.SchemaListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.db.SchemaListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchemaListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchemaListeEintrag();
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.username === "undefined")
			 throw new Error('invalid json format, missing attribute username');
		result.username = obj.username;
		if (typeof obj.isSVWS === "undefined")
			 throw new Error('invalid json format, missing attribute isSVWS');
		result.isSVWS = obj.isSVWS;
		if (typeof obj.revision === "undefined")
			 throw new Error('invalid json format, missing attribute revision');
		result.revision = obj.revision;
		if (typeof obj.isTainted === "undefined")
			 throw new Error('invalid json format, missing attribute isTainted');
		result.isTainted = obj.isTainted;
		if (typeof obj.isInConfig === "undefined")
			 throw new Error('invalid json format, missing attribute isInConfig');
		result.isInConfig = obj.isInConfig;
		if (typeof obj.isDeactivated === "undefined")
			 throw new Error('invalid json format, missing attribute isDeactivated');
		result.isDeactivated = obj.isDeactivated;
		return result;
	}

	public static transpilerToJSON(obj : SchemaListeEintrag) : string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"username" : ' + JSON.stringify(obj.username!) + ',';
		result += '"isSVWS" : ' + obj.isSVWS + ',';
		result += '"revision" : ' + obj.revision + ',';
		result += '"isTainted" : ' + obj.isTainted + ',';
		result += '"isInConfig" : ' + obj.isInConfig + ',';
		result += '"isDeactivated" : ' + obj.isDeactivated + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchemaListeEintrag>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.username !== "undefined") {
			result += '"username" : ' + JSON.stringify(obj.username!) + ',';
		}
		if (typeof obj.isSVWS !== "undefined") {
			result += '"isSVWS" : ' + obj.isSVWS + ',';
		}
		if (typeof obj.revision !== "undefined") {
			result += '"revision" : ' + obj.revision + ',';
		}
		if (typeof obj.isTainted !== "undefined") {
			result += '"isTainted" : ' + obj.isTainted + ',';
		}
		if (typeof obj.isInConfig !== "undefined") {
			result += '"isInConfig" : ' + obj.isInConfig + ',';
		}
		if (typeof obj.isDeactivated !== "undefined") {
			result += '"isDeactivated" : ' + obj.isDeactivated + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_db_SchemaListeEintrag(obj : unknown) : SchemaListeEintrag {
	return obj as SchemaListeEintrag;
}
