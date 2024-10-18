import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke'].includes(name);
	}

	public static class = new Class<Schild3KatalogEintragVersetzungsvermerke>('de.svws_nrw.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke');

	public static transpilerFromJSON(json : string): Schild3KatalogEintragVersetzungsvermerke {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragVersetzungsvermerke>;
		const result = new Schild3KatalogEintragVersetzungsvermerke();
		result.Nr = (obj.Nr === undefined) ? null : obj.Nr === null ? null : obj.Nr;
		result.Klartext = (obj.Klartext === undefined) ? null : obj.Klartext === null ? null : obj.Klartext;
		result.StatistikKrz = (obj.StatistikKrz === undefined) ? null : obj.StatistikKrz === null ? null : obj.StatistikKrz;
		result.Sortierung = (obj.Sortierung === undefined) ? null : obj.Sortierung === null ? null : obj.Sortierung;
		result.Schulform = (obj.Schulform === undefined) ? null : obj.Schulform === null ? null : obj.Schulform;
		result.StatistikKrzNeu = (obj.StatistikKrzNeu === undefined) ? null : obj.StatistikKrzNeu === null ? null : obj.StatistikKrzNeu;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragVersetzungsvermerke) : string {
		let result = '{';
		result += '"Nr" : ' + ((!obj.Nr) ? 'null' : JSON.stringify(obj.Nr)) + ',';
		result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : JSON.stringify(obj.Klartext)) + ',';
		result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : JSON.stringify(obj.StatistikKrz)) + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : JSON.stringify(obj.Schulform)) + ',';
		result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : JSON.stringify(obj.StatistikKrzNeu)) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragVersetzungsvermerke>) : string {
		let result = '{';
		if (obj.Nr !== undefined) {
			result += '"Nr" : ' + ((!obj.Nr) ? 'null' : JSON.stringify(obj.Nr)) + ',';
		}
		if (obj.Klartext !== undefined) {
			result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : JSON.stringify(obj.Klartext)) + ',';
		}
		if (obj.StatistikKrz !== undefined) {
			result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : JSON.stringify(obj.StatistikKrz)) + ',';
		}
		if (obj.Sortierung !== undefined) {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		}
		if (obj.Schulform !== undefined) {
			result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : JSON.stringify(obj.Schulform)) + ',';
		}
		if (obj.StatistikKrzNeu !== undefined) {
			result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : JSON.stringify(obj.StatistikKrzNeu)) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragVersetzungsvermerke(obj : unknown) : Schild3KatalogEintragVersetzungsvermerke {
	return obj as Schild3KatalogEintragVersetzungsvermerke;
}
