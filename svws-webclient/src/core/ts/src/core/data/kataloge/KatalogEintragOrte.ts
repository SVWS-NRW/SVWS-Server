import { JavaObject } from '../../../java/lang/JavaObject';

export class KatalogEintragOrte extends JavaObject {

	/**
	 * Katalog von IT.NRW PLZ Ort: die ID des Katalog-Eintrags
	 */
	public ID : number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: die Postleitzahl
	 */
	public PLZ : string | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: der Regionalschlüssel
	 */
	public RegSchl : string | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: die Ortsbezeichnung
	 */
	public Ort : string | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: eine Nummer für die Sortierreihenfolge der Katalog-Einträge
	 */
	public Sortierung : number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: Gibt die Gültigkeit ab welchem Schuljahr an
	 */
	public gueltigVon : number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: Gibt die Gültigkeit bis zu welchem Schuljahr an
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.KatalogEintragOrte'].includes(name);
	}

	public static transpilerFromJSON(json : string): KatalogEintragOrte {
		const obj = JSON.parse(json);
		const result = new KatalogEintragOrte();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : obj.ID;
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ === null ? null : obj.PLZ;
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl === null ? null : obj.RegSchl;
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort === null ? null : obj.Ort;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung === null ? null : obj.Sortierung;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragOrte) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ + '"') + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragOrte>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		}
		if (typeof obj.PLZ !== "undefined") {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ + '"') + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		}
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung) + ',';
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

export function cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintragOrte(obj : unknown) : KatalogEintragOrte {
	return obj as KatalogEintragOrte;
}
