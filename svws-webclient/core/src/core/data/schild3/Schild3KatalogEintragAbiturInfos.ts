import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragAbiturInfos extends JavaObject {

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public PrfOrdnung : string | null = null;

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public AbiFach : string | null = null;

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public Bedingung : string | null = null;

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public AbiInfoKrz : string | null = null;

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public AbiInfoBeschreibung : string | null = null;

	/**
	 * Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin
	 */
	public AbiInfoText : string | null = null;

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
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragAbiturInfos';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragAbiturInfos'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragAbiturInfos {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragAbiturInfos>;
		const result = new Schild3KatalogEintragAbiturInfos();
		result.PrfOrdnung = (obj.PrfOrdnung === undefined) ? null : obj.PrfOrdnung === null ? null : obj.PrfOrdnung;
		result.AbiFach = (obj.AbiFach === undefined) ? null : obj.AbiFach === null ? null : obj.AbiFach;
		result.Bedingung = (obj.Bedingung === undefined) ? null : obj.Bedingung === null ? null : obj.Bedingung;
		result.AbiInfoKrz = (obj.AbiInfoKrz === undefined) ? null : obj.AbiInfoKrz === null ? null : obj.AbiInfoKrz;
		result.AbiInfoBeschreibung = (obj.AbiInfoBeschreibung === undefined) ? null : obj.AbiInfoBeschreibung === null ? null : obj.AbiInfoBeschreibung;
		result.AbiInfoText = (obj.AbiInfoText === undefined) ? null : obj.AbiInfoText === null ? null : obj.AbiInfoText;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragAbiturInfos) : string {
		let result = '{';
		result += '"PrfOrdnung" : ' + ((!obj.PrfOrdnung) ? 'null' : JSON.stringify(obj.PrfOrdnung)) + ',';
		result += '"AbiFach" : ' + ((!obj.AbiFach) ? 'null' : JSON.stringify(obj.AbiFach)) + ',';
		result += '"Bedingung" : ' + ((!obj.Bedingung) ? 'null' : JSON.stringify(obj.Bedingung)) + ',';
		result += '"AbiInfoKrz" : ' + ((!obj.AbiInfoKrz) ? 'null' : JSON.stringify(obj.AbiInfoKrz)) + ',';
		result += '"AbiInfoBeschreibung" : ' + ((!obj.AbiInfoBeschreibung) ? 'null' : JSON.stringify(obj.AbiInfoBeschreibung)) + ',';
		result += '"AbiInfoText" : ' + ((!obj.AbiInfoText) ? 'null' : JSON.stringify(obj.AbiInfoText)) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragAbiturInfos>) : string {
		let result = '{';
		if (obj.PrfOrdnung !== undefined) {
			result += '"PrfOrdnung" : ' + ((!obj.PrfOrdnung) ? 'null' : JSON.stringify(obj.PrfOrdnung)) + ',';
		}
		if (obj.AbiFach !== undefined) {
			result += '"AbiFach" : ' + ((!obj.AbiFach) ? 'null' : JSON.stringify(obj.AbiFach)) + ',';
		}
		if (obj.Bedingung !== undefined) {
			result += '"Bedingung" : ' + ((!obj.Bedingung) ? 'null' : JSON.stringify(obj.Bedingung)) + ',';
		}
		if (obj.AbiInfoKrz !== undefined) {
			result += '"AbiInfoKrz" : ' + ((!obj.AbiInfoKrz) ? 'null' : JSON.stringify(obj.AbiInfoKrz)) + ',';
		}
		if (obj.AbiInfoBeschreibung !== undefined) {
			result += '"AbiInfoBeschreibung" : ' + ((!obj.AbiInfoBeschreibung) ? 'null' : JSON.stringify(obj.AbiInfoBeschreibung)) + ',';
		}
		if (obj.AbiInfoText !== undefined) {
			result += '"AbiInfoText" : ' + ((!obj.AbiInfoText) ? 'null' : JSON.stringify(obj.AbiInfoText)) + ',';
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

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragAbiturInfos(obj : unknown) : Schild3KatalogEintragAbiturInfos {
	return obj as Schild3KatalogEintragAbiturInfos;
}
