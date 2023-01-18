import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragDQRNiveaus extends JavaObject {

	/**
	 * DQR-Niveau für Gliederung 
	 */
	public Gliederung : String | null = null;

	/**
	 * DQR-Niveau für die Fachklasse 
	 */
	public FKS : String | null = null;

	/**
	 * DQR-Niveau als Nummer 
	 */
	public DQR_Niveau : Number | null = null;

	/**
	 * Gültig ab Schuljahr 
	 */
	public gueltigVon : Number | null = null;

	/**
	 * Gültig bis Schuljahr 
	 */
	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragDQRNiveaus'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragDQRNiveaus {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragDQRNiveaus();
		result.Gliederung = typeof obj.Gliederung === "undefined" ? null : obj.Gliederung === null ? null : String(obj.Gliederung);
		result.FKS = typeof obj.FKS === "undefined" ? null : obj.FKS === null ? null : String(obj.FKS);
		result.DQR_Niveau = typeof obj.DQR_Niveau === "undefined" ? null : obj.DQR_Niveau === null ? null : Number(obj.DQR_Niveau);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragDQRNiveaus) : string {
		let result = '{';
		result += '"Gliederung" : ' + ((!obj.Gliederung) ? 'null' : '"' + obj.Gliederung.valueOf() + '"') + ',';
		result += '"FKS" : ' + ((!obj.FKS) ? 'null' : '"' + obj.FKS.valueOf() + '"') + ',';
		result += '"DQR_Niveau" : ' + ((!obj.DQR_Niveau) ? 'null' : obj.DQR_Niveau.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragDQRNiveaus>) : string {
		let result = '{';
		if (typeof obj.Gliederung !== "undefined") {
			result += '"Gliederung" : ' + ((!obj.Gliederung) ? 'null' : '"' + obj.Gliederung.valueOf() + '"') + ',';
		}
		if (typeof obj.FKS !== "undefined") {
			result += '"FKS" : ' + ((!obj.FKS) ? 'null' : '"' + obj.FKS.valueOf() + '"') + ',';
		}
		if (typeof obj.DQR_Niveau !== "undefined") {
			result += '"DQR_Niveau" : ' + ((!obj.DQR_Niveau) ? 'null' : obj.DQR_Niveau.valueOf()) + ',';
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

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragDQRNiveaus(obj : unknown) : Schild3KatalogEintragDQRNiveaus {
	return obj as Schild3KatalogEintragDQRNiveaus;
}
