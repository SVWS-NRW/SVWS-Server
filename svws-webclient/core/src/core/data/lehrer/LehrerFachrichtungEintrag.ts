import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerFachrichtungEintrag extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Die ID der Fachrichtung.
	 */
	public idFachrichtung : number = 0;

	/**
	 * Die ID des Anerkennungsgrund für die Fachrichtung.
	 */
	public idAnerkennungsgrund : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerFachrichtungEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerFachrichtungEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerFachrichtungEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idFachrichtung === "undefined")
			 throw new Error('invalid json format, missing attribute idFachrichtung');
		result.idFachrichtung = obj.idFachrichtung;
		result.idAnerkennungsgrund = typeof obj.idAnerkennungsgrund === "undefined" ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerFachrichtungEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idFachrichtung" : ' + obj.idFachrichtung + ',';
		result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerFachrichtungEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idFachrichtung !== "undefined") {
			result += '"idFachrichtung" : ' + obj.idFachrichtung + ',';
		}
		if (typeof obj.idAnerkennungsgrund !== "undefined") {
			result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerFachrichtungEintrag(obj : unknown) : LehrerFachrichtungEintrag {
	return obj as LehrerFachrichtungEintrag;
}
