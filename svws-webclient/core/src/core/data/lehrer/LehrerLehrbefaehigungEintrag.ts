import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerLehrbefaehigungEintrag extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Die ID der Lehrbefähigung.
	 */
	public idLehrbefaehigung : number = 0;

	/**
	 * Die ID des Anerkennungsgrund für die Lehrbefähigung.
	 */
	public idAnerkennungsgrund : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerLehrbefaehigungEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerLehrbefaehigungEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerLehrbefaehigungEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerLehrbefaehigungEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idLehrbefaehigung === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrbefaehigung');
		result.idLehrbefaehigung = obj.idLehrbefaehigung;
		result.idAnerkennungsgrund = typeof obj.idAnerkennungsgrund === "undefined" ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerLehrbefaehigungEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idLehrbefaehigung" : ' + obj.idLehrbefaehigung + ',';
		result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLehrbefaehigungEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idLehrbefaehigung !== "undefined") {
			result += '"idLehrbefaehigung" : ' + obj.idLehrbefaehigung + ',';
		}
		if (typeof obj.idAnerkennungsgrund !== "undefined") {
			result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerLehrbefaehigungEintrag(obj : unknown) : LehrerLehrbefaehigungEintrag {
	return obj as LehrerLehrbefaehigungEintrag;
}
