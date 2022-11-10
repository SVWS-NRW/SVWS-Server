import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class VerkehrsspracheKatalogEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public bezeichnung : String = "";

	public iso2 : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                    die ID
	 * @param kuerzel               der dreistellige ISO 639-2 code 
	 * @param bezeichnung           die ISO-Bezeichnung der Sprache
	 * @param iso2                  der zweistellige ISO 639-1 code, sofern vorhanden
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzel : String, bezeichnung : String, iso2 : String | null, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : String | null, __param4? : Number | null, __param5? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string")) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof Number) || (typeof __param4 === "number")) || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof Number) || (typeof __param5 === "number")) || (__param5 === null))) {
			let id : number = __param0 as number;
			let kuerzel : String = __param1;
			let bezeichnung : String = __param2;
			let iso2 : String | null = __param3;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param4);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param5);
			this.id = id;
			this.kuerzel = kuerzel;
			this.bezeichnung = bezeichnung;
			this.iso2 = iso2;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.VerkehrsspracheKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): VerkehrsspracheKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new VerkehrsspracheKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		result.iso2 = typeof obj.iso2 === "undefined" ? null : obj.iso2 === null ? null : String(obj.iso2);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : VerkehrsspracheKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"iso2" : ' + ((!obj.iso2) ? 'null' : '"' + obj.iso2.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<VerkehrsspracheKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.iso2 !== "undefined") {
			result += '"iso2" : ' + ((!obj.iso2) ? 'null' : '"' + obj.iso2.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_VerkehrsspracheKatalogEintrag(obj : unknown) : VerkehrsspracheKatalogEintrag {
	return obj as VerkehrsspracheKatalogEintrag;
}
