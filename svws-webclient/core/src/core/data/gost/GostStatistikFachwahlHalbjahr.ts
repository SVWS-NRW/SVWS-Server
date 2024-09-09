import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostStatistikFachwahlHalbjahr extends JavaObject {

	/**
	 * Die Anzahl der Wahlen als mündlicher Grundkurs, Projektkurs oder Vertiefungskurs.
	 */
	public wahlenGKMuendlich : number = 0;

	/**
	 * Die Anzahl der Wahlen als schriftlicher Grundkurs.
	 */
	public wahlenGKSchriftlich : number = 0;

	/**
	 * Die Gesamtzahl der Wahlen als Grundkurs, Projektkurs oder Vertiefungskurs.
	 */
	public wahlenGK : number = 0;

	/**
	 * Die Anzahl der Wahlen als Zusatzkurs.
	 */
	public wahlenZK : number = 0;

	/**
	 * Die Gesamtzahl der Wahlen als Leistungskurs.
	 */
	public wahlenLK : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr'].includes(name);
	}

	public static class = new Class<GostStatistikFachwahlHalbjahr>('de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr');

	public static transpilerFromJSON(json : string): GostStatistikFachwahlHalbjahr {
		const obj = JSON.parse(json) as Partial<GostStatistikFachwahlHalbjahr>;
		const result = new GostStatistikFachwahlHalbjahr();
		if (obj.wahlenGKMuendlich === undefined)
			throw new Error('invalid json format, missing attribute wahlenGKMuendlich');
		result.wahlenGKMuendlich = obj.wahlenGKMuendlich;
		if (obj.wahlenGKSchriftlich === undefined)
			throw new Error('invalid json format, missing attribute wahlenGKSchriftlich');
		result.wahlenGKSchriftlich = obj.wahlenGKSchriftlich;
		if (obj.wahlenGK === undefined)
			throw new Error('invalid json format, missing attribute wahlenGK');
		result.wahlenGK = obj.wahlenGK;
		if (obj.wahlenZK === undefined)
			throw new Error('invalid json format, missing attribute wahlenZK');
		result.wahlenZK = obj.wahlenZK;
		if (obj.wahlenLK === undefined)
			throw new Error('invalid json format, missing attribute wahlenLK');
		result.wahlenLK = obj.wahlenLK;
		return result;
	}

	public static transpilerToJSON(obj : GostStatistikFachwahlHalbjahr) : string {
		let result = '{';
		result += '"wahlenGKMuendlich" : ' + obj.wahlenGKMuendlich.toString() + ',';
		result += '"wahlenGKSchriftlich" : ' + obj.wahlenGKSchriftlich.toString() + ',';
		result += '"wahlenGK" : ' + obj.wahlenGK.toString() + ',';
		result += '"wahlenZK" : ' + obj.wahlenZK.toString() + ',';
		result += '"wahlenLK" : ' + obj.wahlenLK.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostStatistikFachwahlHalbjahr>) : string {
		let result = '{';
		if (obj.wahlenGKMuendlich !== undefined) {
			result += '"wahlenGKMuendlich" : ' + obj.wahlenGKMuendlich.toString() + ',';
		}
		if (obj.wahlenGKSchriftlich !== undefined) {
			result += '"wahlenGKSchriftlich" : ' + obj.wahlenGKSchriftlich.toString() + ',';
		}
		if (obj.wahlenGK !== undefined) {
			result += '"wahlenGK" : ' + obj.wahlenGK.toString() + ',';
		}
		if (obj.wahlenZK !== undefined) {
			result += '"wahlenZK" : ' + obj.wahlenZK.toString() + ',';
		}
		if (obj.wahlenLK !== undefined) {
			result += '"wahlenLK" : ' + obj.wahlenLK.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostStatistikFachwahlHalbjahr(obj : unknown) : GostStatistikFachwahlHalbjahr {
	return obj as GostStatistikFachwahlHalbjahr;
}
