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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.KatalogEintragOrte';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.KatalogEintragOrte'].includes(name);
	}

	public static transpilerFromJSON(json : string): KatalogEintragOrte {
		const obj = JSON.parse(json) as Partial<KatalogEintragOrte>;
		const result = new KatalogEintragOrte();
		result.ID = (obj.ID === undefined) ? null : obj.ID === null ? null : obj.ID;
		result.PLZ = (obj.PLZ === undefined) ? null : obj.PLZ === null ? null : obj.PLZ;
		result.RegSchl = (obj.RegSchl === undefined) ? null : obj.RegSchl === null ? null : obj.RegSchl;
		result.Ort = (obj.Ort === undefined) ? null : obj.Ort === null ? null : obj.Ort;
		result.Sortierung = (obj.Sortierung === undefined) ? null : obj.Sortierung === null ? null : obj.Sortierung;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragOrte) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.toString()) + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragOrte>) : string {
		let result = '{';
		if (obj.ID !== undefined) {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.toString()) + ',';
		}
		if (obj.PLZ !== undefined) {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		}
		if (obj.RegSchl !== undefined) {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		}
		if (obj.Ort !== undefined) {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		}
		if (obj.Sortierung !== undefined) {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.toString()) + ',';
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

export function cast_de_svws_nrw_core_data_kataloge_KatalogEintragOrte(obj : unknown) : KatalogEintragOrte {
	return obj as KatalogEintragOrte;
}
