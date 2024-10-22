import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.db.DBSchemaListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.db.DBSchemaListeEintrag'].includes(name);
	}

	public static class = new Class<DBSchemaListeEintrag>('de.svws_nrw.core.data.db.DBSchemaListeEintrag');

	public static transpilerFromJSON(json : string): DBSchemaListeEintrag {
		const obj = JSON.parse(json) as Partial<DBSchemaListeEintrag>;
		const result = new DBSchemaListeEintrag();
		result.name = (obj.name === undefined) ? null : obj.name === null ? null : obj.name;
		if (obj.isDefault === undefined)
			throw new Error('invalid json format, missing attribute isDefault');
		result.isDefault = obj.isDefault;
		return result;
	}

	public static transpilerToJSON(obj : DBSchemaListeEintrag) : string {
		let result = '{';
		result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		result += '"isDefault" : ' + obj.isDefault.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DBSchemaListeEintrag>) : string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + ((obj.name === null) ? 'null' : JSON.stringify(obj.name)) + ',';
		}
		if (obj.isDefault !== undefined) {
			result += '"isDefault" : ' + obj.isDefault.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_db_DBSchemaListeEintrag(obj : unknown) : DBSchemaListeEintrag {
	return obj as DBSchemaListeEintrag;
}
