import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag'].includes(name);
	}

	public static class = new Class<LehrerLehramtEintrag>('de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag');

	public static transpilerFromJSON(json : string): LehrerLehramtEintrag {
		const obj = JSON.parse(json) as Partial<LehrerLehramtEintrag>;
		const result = new LehrerLehramtEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehramt === undefined)
			throw new Error('invalid json format, missing attribute idLehramt');
		result.idLehramt = obj.idLehramt;
		result.idAnerkennungsgrund = (obj.idAnerkennungsgrund === undefined) ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerLehramtEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehramt" : ' + obj.idLehramt.toString() + ',';
		result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerLehramtEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehramt !== undefined) {
			result += '"idLehramt" : ' + obj.idLehramt.toString() + ',';
		}
		if (obj.idAnerkennungsgrund !== undefined) {
			result += '"idAnerkennungsgrund" : ' + ((!obj.idAnerkennungsgrund) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerLehramtEintrag(obj : unknown) : LehrerLehramtEintrag {
	return obj as LehrerLehramtEintrag;
}
