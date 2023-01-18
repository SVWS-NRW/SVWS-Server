import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KatalogEintragOrtsteile extends JavaObject {

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: die ID des Katalog-Eintrags 
	 */
	public ID : number = -1;

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: die Postleitzahl 
	 */
	public PLZ : String = "";

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: der Regionalschlüssel 
	 */
	public RegSchl : String = "";

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: die Ortsbezeichnung 
	 */
	public Ort : String = "";

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: die Bezeichnung des Ortsteils 
	 */
	public Ortsteil : String = "";

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: der Stand des Katalog-Eintrags 
	 */
	public Stand : String = "";

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: Gibt die Gültigkeit ab welchem Schuljahr an 
	 */
	public gueltigVon : Number | null = null;

	/**
	 * Katalog von IT.NRW PLZ Ortsteile: Gibt die Gültigkeit bis zu welchem Schuljahr an 
	 */
	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.KatalogEintragOrtsteile'].includes(name);
	}

	public static transpilerFromJSON(json : string): KatalogEintragOrtsteile {
		const obj = JSON.parse(json);
		const result = new KatalogEintragOrtsteile();
		if (typeof obj.ID === "undefined")
			 throw new Error('invalid json format, missing attribute ID');
		result.ID = obj.ID;
		if (typeof obj.PLZ === "undefined")
			 throw new Error('invalid json format, missing attribute PLZ');
		result.PLZ = String(obj.PLZ);
		if (typeof obj.RegSchl === "undefined")
			 throw new Error('invalid json format, missing attribute RegSchl');
		result.RegSchl = String(obj.RegSchl);
		if (typeof obj.Ort === "undefined")
			 throw new Error('invalid json format, missing attribute Ort');
		result.Ort = String(obj.Ort);
		if (typeof obj.Ortsteil === "undefined")
			 throw new Error('invalid json format, missing attribute Ortsteil');
		result.Ortsteil = String(obj.Ortsteil);
		if (typeof obj.Stand === "undefined")
			 throw new Error('invalid json format, missing attribute Stand');
		result.Stand = String(obj.Stand);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragOrtsteile) : string {
		let result = '{';
		result += '"ID" : ' + obj.ID + ',';
		result += '"PLZ" : ' + '"' + obj.PLZ.valueOf() + '"' + ',';
		result += '"RegSchl" : ' + '"' + obj.RegSchl.valueOf() + '"' + ',';
		result += '"Ort" : ' + '"' + obj.Ort.valueOf() + '"' + ',';
		result += '"Ortsteil" : ' + '"' + obj.Ortsteil.valueOf() + '"' + ',';
		result += '"Stand" : ' + '"' + obj.Stand.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragOrtsteile>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + obj.ID + ',';
		}
		if (typeof obj.PLZ !== "undefined") {
			result += '"PLZ" : ' + '"' + obj.PLZ.valueOf() + '"' + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + '"' + obj.RegSchl.valueOf() + '"' + ',';
		}
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + '"' + obj.Ort.valueOf() + '"' + ',';
		}
		if (typeof obj.Ortsteil !== "undefined") {
			result += '"Ortsteil" : ' + '"' + obj.Ortsteil.valueOf() + '"' + ',';
		}
		if (typeof obj.Stand !== "undefined") {
			result += '"Stand" : ' + '"' + obj.Stand.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintragOrtsteile(obj : unknown) : KatalogEintragOrtsteile {
	return obj as KatalogEintragOrtsteile;
}
