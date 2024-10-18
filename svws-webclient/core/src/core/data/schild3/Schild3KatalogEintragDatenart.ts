import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragDatenart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragDatenart'].includes(name);
	}

	public static class = new Class<Schild3KatalogEintragDatenart>('de.svws_nrw.core.data.schild3.Schild3KatalogEintragDatenart');

	public static transpilerFromJSON(json : string): Schild3KatalogEintragDatenart {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragDatenart>;
		const result = new Schild3KatalogEintragDatenart();
		result.DatenartKrz = (obj.DatenartKrz === undefined) ? null : obj.DatenartKrz === null ? null : obj.DatenartKrz;
		result.Datenart = (obj.Datenart === undefined) ? null : obj.Datenart === null ? null : obj.Datenart;
		result.Tabellenname = (obj.Tabellenname === undefined) ? null : obj.Tabellenname === null ? null : obj.Tabellenname;
		result.Reihenfolge = (obj.Reihenfolge === undefined) ? null : obj.Reihenfolge === null ? null : obj.Reihenfolge;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragDatenart) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : JSON.stringify(obj.Datenart)) + ',';
		result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : JSON.stringify(obj.Tabellenname)) + ',';
		result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.toString()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragDatenart>) : string {
		let result = '{';
		if (obj.DatenartKrz !== undefined) {
			result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		}
		if (obj.Datenart !== undefined) {
			result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : JSON.stringify(obj.Datenart)) + ',';
		}
		if (obj.Tabellenname !== undefined) {
			result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : JSON.stringify(obj.Tabellenname)) + ',';
		}
		if (obj.Reihenfolge !== undefined) {
			result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.toString()) + ',';
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

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragDatenart(obj : unknown) : Schild3KatalogEintragDatenart {
	return obj as Schild3KatalogEintragDatenart;
}
