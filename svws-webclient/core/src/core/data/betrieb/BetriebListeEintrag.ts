import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BetriebListeEintrag extends JavaObject {

	/**
	 * ID der weiteren Adresse (Betriebe)
	 */
	public id : number = 0;

	/**
	 * Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart
	 */
	public adressArt : number | null = null;

	/**
	 * Name1 des Betriebs
	 */
	public name1 : string | null = null;

	/**
	 * Straßenname des Betriebsdatensatz
	 */
	public strassenname : string | null = null;

	/**
	 * Hausnummer wenn getrennt gespeichert
	 */
	public hausnr : string | null = null;

	/**
	 * Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden
	 */
	public hausnrzusatz : string | null = null;

	/**
	 * OrtID des Betriebs
	 */
	public ort_id : number | null = null;

	/**
	 * Ortsteil-ID des Betriebs
	 */
	public ortsteil_id : number | null = null;

	/**
	 * Brache des Betriebs
	 */
	public branche : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.betrieb.BetriebListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.betrieb.BetriebListeEintrag'].includes(name);
	}

	public static class = new Class<BetriebListeEintrag>('de.svws_nrw.core.data.betrieb.BetriebListeEintrag');

	public static transpilerFromJSON(json : string): BetriebListeEintrag {
		const obj = JSON.parse(json) as Partial<BetriebListeEintrag>;
		const result = new BetriebListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.adressArt = (obj.adressArt === undefined) ? null : obj.adressArt === null ? null : obj.adressArt;
		result.name1 = (obj.name1 === undefined) ? null : obj.name1 === null ? null : obj.name1;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnr = (obj.hausnr === undefined) ? null : obj.hausnr === null ? null : obj.hausnr;
		result.hausnrzusatz = (obj.hausnrzusatz === undefined) ? null : obj.hausnrzusatz === null ? null : obj.hausnrzusatz;
		result.ort_id = (obj.ort_id === undefined) ? null : obj.ort_id === null ? null : obj.ort_id;
		result.ortsteil_id = (obj.ortsteil_id === undefined) ? null : obj.ortsteil_id === null ? null : obj.ortsteil_id;
		result.branche = (obj.branche === undefined) ? null : obj.branche === null ? null : obj.branche;
		return result;
	}

	public static transpilerToJSON(obj : BetriebListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"adressArt" : ' + ((obj.adressArt === null) ? 'null' : obj.adressArt.toString()) + ',';
		result += '"name1" : ' + ((obj.name1 === null) ? 'null' : JSON.stringify(obj.name1)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnr" : ' + ((obj.hausnr === null) ? 'null' : JSON.stringify(obj.hausnr)) + ',';
		result += '"hausnrzusatz" : ' + ((obj.hausnrzusatz === null) ? 'null' : JSON.stringify(obj.hausnrzusatz)) + ',';
		result += '"ort_id" : ' + ((obj.ort_id === null) ? 'null' : obj.ort_id.toString()) + ',';
		result += '"ortsteil_id" : ' + ((obj.ortsteil_id === null) ? 'null' : obj.ortsteil_id.toString()) + ',';
		result += '"branche" : ' + ((obj.branche === null) ? 'null' : JSON.stringify(obj.branche)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.adressArt !== undefined) {
			result += '"adressArt" : ' + ((obj.adressArt === null) ? 'null' : obj.adressArt.toString()) + ',';
		}
		if (obj.name1 !== undefined) {
			result += '"name1" : ' + ((obj.name1 === null) ? 'null' : JSON.stringify(obj.name1)) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnr !== undefined) {
			result += '"hausnr" : ' + ((obj.hausnr === null) ? 'null' : JSON.stringify(obj.hausnr)) + ',';
		}
		if (obj.hausnrzusatz !== undefined) {
			result += '"hausnrzusatz" : ' + ((obj.hausnrzusatz === null) ? 'null' : JSON.stringify(obj.hausnrzusatz)) + ',';
		}
		if (obj.ort_id !== undefined) {
			result += '"ort_id" : ' + ((obj.ort_id === null) ? 'null' : obj.ort_id.toString()) + ',';
		}
		if (obj.ortsteil_id !== undefined) {
			result += '"ortsteil_id" : ' + ((obj.ortsteil_id === null) ? 'null' : obj.ortsteil_id.toString()) + ',';
		}
		if (obj.branche !== undefined) {
			result += '"branche" : ' + ((obj.branche === null) ? 'null' : JSON.stringify(obj.branche)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_betrieb_BetriebListeEintrag(obj : unknown) : BetriebListeEintrag {
	return obj as BetriebListeEintrag;
}
