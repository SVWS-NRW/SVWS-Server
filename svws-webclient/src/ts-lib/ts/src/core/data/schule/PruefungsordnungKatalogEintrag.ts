import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class PruefungsordnungKatalogEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public kuerzelSchild : String | null = "";

	public bezeichnung : String = "";

	public gvJahr : Number | null = null;

	public gvNr : String = "";

	public gvSeiten : String = "";

	public link : String = "";

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param kuerzelSchild      das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird
	 * @param bezeichnung        die Bezeichnung
	 * @param gvJahr             Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde
	 * @param gvNr               Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung
	 * @param gvSeiten           Gesetz- und Verordnungsblatt: ggf. die Seitenangaben im Jahr der Veröffentlichung
	 * @param link               ggf. ein Link zu einer Version der Verordnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, kuerzelSchild : String | null, bezeichnung : String, gvJahr : Number | null, gvNr : String, gvSeiten : String, link : String, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String | null, __param3? : String, __param4? : Number | null, __param5? : String, __param6? : String, __param7? : String, __param8? : Number | null, __param9? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string")) || (__param2 === null)) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof Number) || (typeof __param4 === "number")) || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof String) || (typeof __param5 === "string"))) && ((typeof __param6 !== "undefined") && ((__param6 instanceof String) || (typeof __param6 === "string"))) && ((typeof __param7 !== "undefined") && ((__param7 instanceof String) || (typeof __param7 === "string"))) && ((typeof __param8 !== "undefined") && ((__param8 instanceof Number) || (typeof __param8 === "number")) || (__param8 === null)) && ((typeof __param9 !== "undefined") && ((__param9 instanceof Number) || (typeof __param9 === "number")) || (__param9 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let kuerzelSchild : String | null = __param2;
			let bezeichnung : String = __param3;
			let gvJahr : Number | null = cast_java_lang_Integer(__param4);
			let gvNr : String = __param5;
			let gvSeiten : String = __param6;
			let link : String = __param7;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param8);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param9);
			this.id = id;
			this.kuerzel = kuerzel;
			this.kuerzelSchild = kuerzelSchild;
			this.bezeichnung = bezeichnung;
			this.gvJahr = gvJahr;
			this.gvNr = gvNr;
			this.gvSeiten = gvSeiten;
			this.link = link;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.PruefungsordnungKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): PruefungsordnungKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new PruefungsordnungKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		result.kuerzelSchild = typeof obj.kuerzelSchild === "undefined" ? null : obj.kuerzelSchild === null ? null : String(obj.kuerzelSchild);
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		result.gvJahr = typeof obj.gvJahr === "undefined" ? null : obj.gvJahr === null ? null : Number(obj.gvJahr);
		if (typeof obj.gvNr === "undefined")
			 throw new Error('invalid json format, missing attribute gvNr');
		result.gvNr = String(obj.gvNr);
		if (typeof obj.gvSeiten === "undefined")
			 throw new Error('invalid json format, missing attribute gvSeiten');
		result.gvSeiten = String(obj.gvSeiten);
		if (typeof obj.link === "undefined")
			 throw new Error('invalid json format, missing attribute link');
		result.link = String(obj.link);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : PruefungsordnungKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : '"' + obj.kuerzelSchild.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr.valueOf()) + ',';
		result += '"gvNr" : ' + '"' + obj.gvNr.valueOf() + '"' + ',';
		result += '"gvSeiten" : ' + '"' + obj.gvSeiten.valueOf() + '"' + ',';
		result += '"link" : ' + '"' + obj.link.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<PruefungsordnungKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.kuerzelSchild !== "undefined") {
			result += '"kuerzelSchild" : ' + ((!obj.kuerzelSchild) ? 'null' : '"' + obj.kuerzelSchild.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.gvJahr !== "undefined") {
			result += '"gvJahr" : ' + ((!obj.gvJahr) ? 'null' : obj.gvJahr.valueOf()) + ',';
		}
		if (typeof obj.gvNr !== "undefined") {
			result += '"gvNr" : ' + '"' + obj.gvNr.valueOf() + '"' + ',';
		}
		if (typeof obj.gvSeiten !== "undefined") {
			result += '"gvSeiten" : ' + '"' + obj.gvSeiten.valueOf() + '"' + ',';
		}
		if (typeof obj.link !== "undefined") {
			result += '"link" : ' + '"' + obj.link.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_PruefungsordnungKatalogEintrag(obj : unknown) : PruefungsordnungKatalogEintrag {
	return obj as PruefungsordnungKatalogEintrag;
}
