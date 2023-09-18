import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragDatenart extends JavaObject {

	/**
	 * Kürzel der Datenart
	 */
	public DatenartKrz : string | null = null;

	/**
	 * Datenart
	 */
	public Datenart : string | null = null;

	/**
	 * Name der Tabelle
	 */
	public Tabellenname : string | null = null;

	/**
	 * Reihenfolge
	 */
	public Reihenfolge : number | null = null;

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
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragDatenart'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragDatenart {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragDatenart();
		result.DatenartKrz = typeof obj.DatenartKrz === "undefined" ? null : obj.DatenartKrz === null ? null : obj.DatenartKrz;
		result.Datenart = typeof obj.Datenart === "undefined" ? null : obj.Datenart === null ? null : obj.Datenart;
		result.Tabellenname = typeof obj.Tabellenname === "undefined" ? null : obj.Tabellenname === null ? null : obj.Tabellenname;
		result.Reihenfolge = typeof obj.Reihenfolge === "undefined" ? null : obj.Reihenfolge === null ? null : obj.Reihenfolge;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragDatenart) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : JSON.stringify(obj.Datenart)) + ',';
		result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : JSON.stringify(obj.Tabellenname)) + ',';
		result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragDatenart>) : string {
		let result = '{';
		if (typeof obj.DatenartKrz !== "undefined") {
			result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		}
		if (typeof obj.Datenart !== "undefined") {
			result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : JSON.stringify(obj.Datenart)) + ',';
		}
		if (typeof obj.Tabellenname !== "undefined") {
			result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : JSON.stringify(obj.Tabellenname)) + ',';
		}
		if (typeof obj.Reihenfolge !== "undefined") {
			result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge) + ',';
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

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragDatenart(obj : unknown) : Schild3KatalogEintragDatenart {
	return obj as Schild3KatalogEintragDatenart;
}
