import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class DBSchemaListeEintrag extends JavaObject {

	/**
	 * Der Name des Datenbank-Schemas. 
	 */
	public name : string | null = null;

	/**
	 * Gibt an, ob es sich um das Default-Schema in der Konfiguration handelt. 
	 */
	public isDefault : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.db.DBSchemaListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): DBSchemaListeEintrag {
		const obj = JSON.parse(json);
		const result = new DBSchemaListeEintrag();
		result.name = typeof obj.name === "undefined" ? null : obj.name === null ? null : obj.name;
		if (typeof obj.isDefault === "undefined")
			 throw new Error('invalid json format, missing attribute isDefault');
		result.isDefault = obj.isDefault;
		return result;
	}

	public static transpilerToJSON(obj : DBSchemaListeEintrag) : string {
		let result = '{';
		result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name + '"') + ',';
		result += '"isDefault" : ' + obj.isDefault + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DBSchemaListeEintrag>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name + '"') + ',';
		}
		if (typeof obj.isDefault !== "undefined") {
			result += '"isDefault" : ' + obj.isDefault + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_db_DBSchemaListeEintrag(obj : unknown) : DBSchemaListeEintrag {
	return obj as DBSchemaListeEintrag;
}
