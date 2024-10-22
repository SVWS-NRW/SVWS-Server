import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BKFachklassenSchluessel extends JavaObject {

	/**
	 * Der Index der Schulgliederung
	 */
	public index : number = -1;

	/**
	 * Der Schlüssel der Fachklasse
	 */
	public schluessel : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.BKFachklassenSchluessel';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.BKFachklassenSchluessel'].includes(name);
	}

	public static class = new Class<BKFachklassenSchluessel>('de.svws_nrw.core.data.bk.BKFachklassenSchluessel');

	public static transpilerFromJSON(json : string): BKFachklassenSchluessel {
		const obj = JSON.parse(json) as Partial<BKFachklassenSchluessel>;
		const result = new BKFachklassenSchluessel();
		if (obj.index === undefined)
			throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		return result;
	}

	public static transpilerToJSON(obj : BKFachklassenSchluessel) : string {
		let result = '{';
		result += '"index" : ' + obj.index + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKFachklassenSchluessel>) : string {
		let result = '{';
		if (obj.index !== undefined) {
			result += '"index" : ' + obj.index + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_BKFachklassenSchluessel(obj : unknown) : BKFachklassenSchluessel {
	return obj as BKFachklassenSchluessel;
}
