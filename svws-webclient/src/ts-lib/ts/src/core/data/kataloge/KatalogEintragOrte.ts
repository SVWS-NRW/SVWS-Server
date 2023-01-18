import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KatalogEintragOrte extends JavaObject {

	/**
	 * Katalog von IT.NRW PLZ Ort: die ID des Katalog-Eintrags 
	 */
	public ID : Number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: die Postleitzahl 
	 */
	public PLZ : String | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: der Regionalschlüssel 
	 */
	public RegSchl : String | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: die Ortsbezeichnung 
	 */
	public Ort : String | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: eine Nummer für die Sortierreihenfolge der Katalog-Einträge 
	 */
	public Sortierung : Number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: Gibt die Gültigkeit ab welchem Schuljahr an 
	 */
	public gueltigVon : Number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ort: Gibt die Gültigkeit bis zu welchem Schuljahr an 
	 */
	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.KatalogEintragOrte'].includes(name);
	}

	public static transpilerFromJSON(json : string): KatalogEintragOrte {
		const obj = JSON.parse(json);
		const result = new KatalogEintragOrte();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : Number(obj.ID);
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ === null ? null : String(obj.PLZ);
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl === null ? null : String(obj.RegSchl);
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort === null ? null : String(obj.Ort);
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung === null ? null : Number(obj.Sortierung);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragOrte) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ.valueOf() + '"') + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragOrte>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		}
		if (typeof obj.PLZ !== "undefined") {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ.valueOf() + '"') + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		}
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintragOrte(obj : unknown) : KatalogEintragOrte {
	return obj as KatalogEintragOrte;
}
