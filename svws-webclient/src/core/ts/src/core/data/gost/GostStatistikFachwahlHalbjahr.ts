import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostStatistikFachwahlHalbjahr'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostStatistikFachwahlHalbjahr {
		const obj = JSON.parse(json);
		const result = new GostStatistikFachwahlHalbjahr();
		if (typeof obj.wahlenGKMuendlich === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenGKMuendlich');
		result.wahlenGKMuendlich = obj.wahlenGKMuendlich;
		if (typeof obj.wahlenGKSchriftlich === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenGKSchriftlich');
		result.wahlenGKSchriftlich = obj.wahlenGKSchriftlich;
		if (typeof obj.wahlenGK === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenGK');
		result.wahlenGK = obj.wahlenGK;
		if (typeof obj.wahlenZK === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenZK');
		result.wahlenZK = obj.wahlenZK;
		if (typeof obj.wahlenLK === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenLK');
		result.wahlenLK = obj.wahlenLK;
		return result;
	}

	public static transpilerToJSON(obj : GostStatistikFachwahlHalbjahr) : string {
		let result = '{';
		result += '"wahlenGKMuendlich" : ' + obj.wahlenGKMuendlich + ',';
		result += '"wahlenGKSchriftlich" : ' + obj.wahlenGKSchriftlich + ',';
		result += '"wahlenGK" : ' + obj.wahlenGK + ',';
		result += '"wahlenZK" : ' + obj.wahlenZK + ',';
		result += '"wahlenLK" : ' + obj.wahlenLK + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostStatistikFachwahlHalbjahr>) : string {
		let result = '{';
		if (typeof obj.wahlenGKMuendlich !== "undefined") {
			result += '"wahlenGKMuendlich" : ' + obj.wahlenGKMuendlich + ',';
		}
		if (typeof obj.wahlenGKSchriftlich !== "undefined") {
			result += '"wahlenGKSchriftlich" : ' + obj.wahlenGKSchriftlich + ',';
		}
		if (typeof obj.wahlenGK !== "undefined") {
			result += '"wahlenGK" : ' + obj.wahlenGK + ',';
		}
		if (typeof obj.wahlenZK !== "undefined") {
			result += '"wahlenZK" : ' + obj.wahlenZK + ',';
		}
		if (typeof obj.wahlenLK !== "undefined") {
			result += '"wahlenLK" : ' + obj.wahlenLK + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahlHalbjahr(obj : unknown) : GostStatistikFachwahlHalbjahr {
	return obj as GostStatistikFachwahlHalbjahr;
}
