import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragVersetzungsvermerke extends JavaObject {

	/**
	 * Nummer des Versetzungsvermerks
	 */
	public Nr : string | null = null;

	/**
	 * Klartext des Versetzungsvermerks
	 */
	public Klartext : string | null = null;

	/**
	 * Statistikkürzel des Versetzungsvermerks (DEPRECATED)
	 */
	public StatistikKrz : string | null = null;

	/**
	 * Sortierung des Versetzungsvermerks
	 */
	public Sortierung : number | null = null;

	/**
	 * Schulform des Versetzungsvermerks
	 */
	public Schulform : string | null = null;

	/**
	 * Neues Statistikkürzel des Versetzungsvermerks
	 */
	public StatistikKrzNeu : string | null = null;

	/**
	 * Gültig ab Schuljahr
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gültig bis Schuljahr
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragVersetzungsvermerke {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragVersetzungsvermerke();
		result.Nr = typeof obj.Nr === "undefined" ? null : obj.Nr === null ? null : obj.Nr;
		result.Klartext = typeof obj.Klartext === "undefined" ? null : obj.Klartext === null ? null : obj.Klartext;
		result.StatistikKrz = typeof obj.StatistikKrz === "undefined" ? null : obj.StatistikKrz === null ? null : obj.StatistikKrz;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung === null ? null : obj.Sortierung;
		result.Schulform = typeof obj.Schulform === "undefined" ? null : obj.Schulform === null ? null : obj.Schulform;
		result.StatistikKrzNeu = typeof obj.StatistikKrzNeu === "undefined" ? null : obj.StatistikKrzNeu === null ? null : obj.StatistikKrzNeu;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragVersetzungsvermerke) : string {
		let result = '{';
		result += '"Nr" : ' + ((!obj.Nr) ? 'null' : '"' + obj.Nr + '"') + ',';
		result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : '"' + obj.Klartext + '"') + ',';
		result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : '"' + obj.StatistikKrz + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung) + ',';
		result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform + '"') + ',';
		result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : '"' + obj.StatistikKrzNeu + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragVersetzungsvermerke>) : string {
		let result = '{';
		if (typeof obj.Nr !== "undefined") {
			result += '"Nr" : ' + ((!obj.Nr) ? 'null' : '"' + obj.Nr + '"') + ',';
		}
		if (typeof obj.Klartext !== "undefined") {
			result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : '"' + obj.Klartext + '"') + ',';
		}
		if (typeof obj.StatistikKrz !== "undefined") {
			result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : '"' + obj.StatistikKrz + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung) + ',';
		}
		if (typeof obj.Schulform !== "undefined") {
			result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform + '"') + ',';
		}
		if (typeof obj.StatistikKrzNeu !== "undefined") {
			result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : '"' + obj.StatistikKrzNeu + '"') + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragVersetzungsvermerke(obj : unknown) : Schild3KatalogEintragVersetzungsvermerke {
	return obj as Schild3KatalogEintragVersetzungsvermerke;
}
