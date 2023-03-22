import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchemaListeEintrag extends JavaObject {

	/**
	 * Der Name des Schemas.
	 */
	public name : string | null = null;

	/**
	 * Die Revisionsnummer des Schemas.
	 */
	public revision : number = 0;

	/**
	 * Gibt an, ob das Schema als "verdorben" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte.
	 */
	public isTainted : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.db.SchemaListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchemaListeEintrag {
		const obj = JSON.parse(json);
		const result = new SchemaListeEintrag();
		result.name = typeof obj.name === "undefined" ? null : obj.name === null ? null : obj.name;
		if (typeof obj.revision === "undefined")
			 throw new Error('invalid json format, missing attribute revision');
		result.revision = obj.revision;
		if (typeof obj.isTainted === "undefined")
			 throw new Error('invalid json format, missing attribute isTainted');
		result.isTainted = obj.isTainted;
		return result;
	}

	public static transpilerToJSON(obj : SchemaListeEintrag) : string {
		let result = '{';
		result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name + '"') + ',';
		result += '"revision" : ' + obj.revision + ',';
		result += '"isTainted" : ' + obj.isTainted + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchemaListeEintrag>) : string {
		let result = '{';
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name + '"') + ',';
		}
		if (typeof obj.revision !== "undefined") {
			result += '"revision" : ' + obj.revision + ',';
		}
		if (typeof obj.isTainted !== "undefined") {
			result += '"isTainted" : ' + obj.isTainted + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_db_SchemaListeEintrag(obj : unknown) : SchemaListeEintrag {
	return obj as SchemaListeEintrag;
}
