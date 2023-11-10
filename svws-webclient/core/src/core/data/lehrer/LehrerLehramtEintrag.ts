import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerLehramtEintrag extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Die ID des Lehramtes.
	 */
	public idLehramt : number = 0;

	/**
	 * Die ID des Anerkennungsgrund für das Lehramt.
	 */
	public idAnerkennungsgrund : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerLehramtEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerLehramtEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerLehramtEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idLehramt === "undefined")
			 throw new Error('invalid json format, missing attribute idLehramt');
		result.idLehramt = obj.idLehramt;
		result.idAnerkennungsgrund = typeof obj.idAnerkennungsgrund === "undefined" ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerLehramtEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idLehramt" : ' + obj.idLehramt + ',';
		result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLehramtEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idLehramt !== "undefined") {
			result += '"idLehramt" : ' + obj.idLehramt + ',';
		}
		if (typeof obj.idAnerkennungsgrund !== "undefined") {
			result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerLehramtEintrag(obj : unknown) : LehrerLehramtEintrag {
	return obj as LehrerLehramtEintrag;
}
