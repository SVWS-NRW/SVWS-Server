import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag'].includes(name);
	}

	public static class = new Class<LehrerLehrbefaehigungEintrag>('de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag');

	public static transpilerFromJSON(json : string): LehrerLehrbefaehigungEintrag {
		const obj = JSON.parse(json) as Partial<LehrerLehrbefaehigungEintrag>;
		const result = new LehrerLehrbefaehigungEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehrbefaehigung === undefined)
			throw new Error('invalid json format, missing attribute idLehrbefaehigung');
		result.idLehrbefaehigung = obj.idLehrbefaehigung;
		result.idAnerkennungsgrund = (obj.idAnerkennungsgrund === undefined) ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerLehrbefaehigungEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehrbefaehigung" : ' + obj.idLehrbefaehigung.toString() + ',';
		result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLehrbefaehigungEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehrbefaehigung !== undefined) {
			result += '"idLehrbefaehigung" : ' + obj.idLehrbefaehigung.toString() + ',';
		}
		if (obj.idAnerkennungsgrund !== undefined) {
			result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerLehrbefaehigungEintrag(obj : unknown) : LehrerLehrbefaehigungEintrag {
	return obj as LehrerLehrbefaehigungEintrag;
}
