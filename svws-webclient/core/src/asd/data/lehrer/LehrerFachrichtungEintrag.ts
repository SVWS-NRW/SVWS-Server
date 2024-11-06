import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag'].includes(name);
	}

	public static class = new Class<LehrerFachrichtungEintrag>('de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag');

	public static transpilerFromJSON(json : string): LehrerFachrichtungEintrag {
		const obj = JSON.parse(json) as Partial<LehrerFachrichtungEintrag>;
		const result = new LehrerFachrichtungEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idFachrichtung === undefined)
			throw new Error('invalid json format, missing attribute idFachrichtung');
		result.idFachrichtung = obj.idFachrichtung;
		result.idAnerkennungsgrund = (obj.idAnerkennungsgrund === undefined) ? null : obj.idAnerkennungsgrund === null ? null : obj.idAnerkennungsgrund;
		return result;
	}

	public static transpilerToJSON(obj : LehrerFachrichtungEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFachrichtung" : ' + obj.idFachrichtung.toString() + ',';
		result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerFachrichtungEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFachrichtung !== undefined) {
			result += '"idFachrichtung" : ' + obj.idFachrichtung.toString() + ',';
		}
		if (obj.idAnerkennungsgrund !== undefined) {
			result += '"idAnerkennungsgrund" : ' + ((obj.idAnerkennungsgrund === null) ? 'null' : obj.idAnerkennungsgrund.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerFachrichtungEintrag(obj : unknown) : LehrerFachrichtungEintrag {
	return obj as LehrerFachrichtungEintrag;
}
