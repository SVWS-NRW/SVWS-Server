import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragPruefungsordnung extends JavaObject {

	/**
	 * Zulässige Schulformen der Prüfungsordnungen
	 */
	public PO_Schulform : string | null = null;

	/**
	 * Erstes Kürzel
	 */
	public PO_Krz : string | null = null;

	/**
	 * Zweites Kürzel
	 */
	public PO_Name : string | null = null;

	/**
	 * Zulässige Gliederungen
	 */
	public PO_SGL : string | null = null;

	/**
	 * Deprecated
	 */
	public PO_MinJahrgang : number | null = null;

	/**
	 * Deprecated
	 */
	public PO_MaxJahrgang : number | null = null;

	/**
	 * Zulässige Jahrgänge
	 */
	public PO_Jahrgaenge : string | null = null;

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
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragPruefungsordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragPruefungsordnung {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragPruefungsordnung();
		result.PO_Schulform = typeof obj.PO_Schulform === "undefined" ? null : obj.PO_Schulform === null ? null : obj.PO_Schulform;
		result.PO_Krz = typeof obj.PO_Krz === "undefined" ? null : obj.PO_Krz === null ? null : obj.PO_Krz;
		result.PO_Name = typeof obj.PO_Name === "undefined" ? null : obj.PO_Name === null ? null : obj.PO_Name;
		result.PO_SGL = typeof obj.PO_SGL === "undefined" ? null : obj.PO_SGL === null ? null : obj.PO_SGL;
		result.PO_MinJahrgang = typeof obj.PO_MinJahrgang === "undefined" ? null : obj.PO_MinJahrgang === null ? null : obj.PO_MinJahrgang;
		result.PO_MaxJahrgang = typeof obj.PO_MaxJahrgang === "undefined" ? null : obj.PO_MaxJahrgang === null ? null : obj.PO_MaxJahrgang;
		result.PO_Jahrgaenge = typeof obj.PO_Jahrgaenge === "undefined" ? null : obj.PO_Jahrgaenge === null ? null : obj.PO_Jahrgaenge;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragPruefungsordnung) : string {
		let result = '{';
		result += '"PO_Schulform" : ' + ((!obj.PO_Schulform) ? 'null' : '"' + obj.PO_Schulform + '"') + ',';
		result += '"PO_Krz" : ' + ((!obj.PO_Krz) ? 'null' : '"' + obj.PO_Krz + '"') + ',';
		result += '"PO_Name" : ' + ((!obj.PO_Name) ? 'null' : '"' + obj.PO_Name + '"') + ',';
		result += '"PO_SGL" : ' + ((!obj.PO_SGL) ? 'null' : '"' + obj.PO_SGL + '"') + ',';
		result += '"PO_MinJahrgang" : ' + ((!obj.PO_MinJahrgang) ? 'null' : obj.PO_MinJahrgang) + ',';
		result += '"PO_MaxJahrgang" : ' + ((!obj.PO_MaxJahrgang) ? 'null' : obj.PO_MaxJahrgang) + ',';
		result += '"PO_Jahrgaenge" : ' + ((!obj.PO_Jahrgaenge) ? 'null' : '"' + obj.PO_Jahrgaenge + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragPruefungsordnung>) : string {
		let result = '{';
		if (typeof obj.PO_Schulform !== "undefined") {
			result += '"PO_Schulform" : ' + ((!obj.PO_Schulform) ? 'null' : '"' + obj.PO_Schulform + '"') + ',';
		}
		if (typeof obj.PO_Krz !== "undefined") {
			result += '"PO_Krz" : ' + ((!obj.PO_Krz) ? 'null' : '"' + obj.PO_Krz + '"') + ',';
		}
		if (typeof obj.PO_Name !== "undefined") {
			result += '"PO_Name" : ' + ((!obj.PO_Name) ? 'null' : '"' + obj.PO_Name + '"') + ',';
		}
		if (typeof obj.PO_SGL !== "undefined") {
			result += '"PO_SGL" : ' + ((!obj.PO_SGL) ? 'null' : '"' + obj.PO_SGL + '"') + ',';
		}
		if (typeof obj.PO_MinJahrgang !== "undefined") {
			result += '"PO_MinJahrgang" : ' + ((!obj.PO_MinJahrgang) ? 'null' : obj.PO_MinJahrgang) + ',';
		}
		if (typeof obj.PO_MaxJahrgang !== "undefined") {
			result += '"PO_MaxJahrgang" : ' + ((!obj.PO_MaxJahrgang) ? 'null' : obj.PO_MaxJahrgang) + ',';
		}
		if (typeof obj.PO_Jahrgaenge !== "undefined") {
			result += '"PO_Jahrgaenge" : ' + ((!obj.PO_Jahrgaenge) ? 'null' : '"' + obj.PO_Jahrgaenge + '"') + ',';
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

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragPruefungsordnung(obj : unknown) : Schild3KatalogEintragPruefungsordnung {
	return obj as Schild3KatalogEintragPruefungsordnung;
}
