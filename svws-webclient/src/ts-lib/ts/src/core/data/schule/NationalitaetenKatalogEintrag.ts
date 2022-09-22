import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class NationalitaetenKatalogEintrag extends JavaObject {

	public id : number = 0;

	public iso3 : String = "";

	public iso2 : String = "";

	public isoNumerisch : String | null = null;

	public codeDEStatis : String = "";

	public bezeichnungSuche : String = "";

	public bezeichnung : String = "";

	public bezeichnungLang : String = "";

	public staatsangehoerigkeit : String = "";

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id                     die ID des Katalog-Eintrags
	 * @param iso3                   das 3-stellige Kürzel des ISO-Standards 3166-1
	 * @param iso2                   das 2-stellige Kürzel des ISO-Standards 3166-1
	 * @param isoNumerisch           die 3-stellige Nummer des ISO-Standards 3166-1
	 * @param codeDEStatis           die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de)
	 * @param bezeichnungSuche       die Bezeichnung für eine Suche
	 * @param bezeichnung            die kurze Bezeichnung
	 * @param bezeichnungLang        die Bezeichnung für eine Suche
	 * @param staatsangehoerigkeit   die Bezeichnung der Staatsangehörigkeit
	 * @param gueltigVon             das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis             das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, iso3 : String, iso2 : String, isoNumerisch : String | null, codeDEStatis : String, bezeichnungSuche : String, bezeichnung : String, bezeichnungLang : String, staatsangehoerigkeit : String, gueltigVon : Number | null, gueltigBis : Number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : String, __param2? : String, __param3? : String | null, __param4? : String, __param5? : String, __param6? : String, __param7? : String, __param8? : String, __param9? : Number | null, __param10? : Number | null) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && ((__param1 instanceof String) || (typeof __param1 === "string"))) && ((typeof __param2 !== "undefined") && ((__param2 instanceof String) || (typeof __param2 === "string"))) && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string")) || (__param3 === null)) && ((typeof __param4 !== "undefined") && ((__param4 instanceof String) || (typeof __param4 === "string"))) && ((typeof __param5 !== "undefined") && ((__param5 instanceof String) || (typeof __param5 === "string"))) && ((typeof __param6 !== "undefined") && ((__param6 instanceof String) || (typeof __param6 === "string"))) && ((typeof __param7 !== "undefined") && ((__param7 instanceof String) || (typeof __param7 === "string"))) && ((typeof __param8 !== "undefined") && ((__param8 instanceof String) || (typeof __param8 === "string"))) && ((typeof __param9 !== "undefined") && ((__param9 instanceof Number) || (typeof __param9 === "number")) || (__param9 === null)) && ((typeof __param10 !== "undefined") && ((__param10 instanceof Number) || (typeof __param10 === "number")) || (__param10 === null))) {
			let id : number = __param0 as number;
			let iso3 : String = __param1;
			let iso2 : String = __param2;
			let isoNumerisch : String | null = __param3;
			let codeDEStatis : String = __param4;
			let bezeichnungSuche : String = __param5;
			let bezeichnung : String = __param6;
			let bezeichnungLang : String = __param7;
			let staatsangehoerigkeit : String = __param8;
			let gueltigVon : Number | null = cast_java_lang_Integer(__param9);
			let gueltigBis : Number | null = cast_java_lang_Integer(__param10);
			this.id = id;
			this.iso3 = iso3;
			this.iso2 = iso2;
			this.isoNumerisch = isoNumerisch;
			this.codeDEStatis = codeDEStatis;
			this.bezeichnungSuche = bezeichnungSuche;
			this.bezeichnung = bezeichnung;
			this.bezeichnungLang = bezeichnungLang;
			this.staatsangehoerigkeit = staatsangehoerigkeit;
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.NationalitaetenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): NationalitaetenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new NationalitaetenKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.iso3 === "undefined")
			 throw new Error('invalid json format, missing attribute iso3');
		result.iso3 = obj.iso3;
		if (typeof obj.iso2 === "undefined")
			 throw new Error('invalid json format, missing attribute iso2');
		result.iso2 = obj.iso2;
		result.isoNumerisch = typeof obj.isoNumerisch === "undefined" ? null : obj.isoNumerisch;
		if (typeof obj.codeDEStatis === "undefined")
			 throw new Error('invalid json format, missing attribute codeDEStatis');
		result.codeDEStatis = obj.codeDEStatis;
		if (typeof obj.bezeichnungSuche === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungSuche');
		result.bezeichnungSuche = obj.bezeichnungSuche;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.bezeichnungLang === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungLang');
		result.bezeichnungLang = obj.bezeichnungLang;
		if (typeof obj.staatsangehoerigkeit === "undefined")
			 throw new Error('invalid json format, missing attribute staatsangehoerigkeit');
		result.staatsangehoerigkeit = obj.staatsangehoerigkeit;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : NationalitaetenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"iso3" : ' + '"' + obj.iso3.valueOf() + '"' + ',';
		result += '"iso2" : ' + '"' + obj.iso2.valueOf() + '"' + ',';
		result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : '"' + obj.isoNumerisch.valueOf() + '"') + ',';
		result += '"codeDEStatis" : ' + '"' + obj.codeDEStatis.valueOf() + '"' + ',';
		result += '"bezeichnungSuche" : ' + '"' + obj.bezeichnungSuche.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"bezeichnungLang" : ' + '"' + obj.bezeichnungLang.valueOf() + '"' + ',';
		result += '"staatsangehoerigkeit" : ' + '"' + obj.staatsangehoerigkeit.valueOf() + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NationalitaetenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.iso3 !== "undefined") {
			result += '"iso3" : ' + '"' + obj.iso3.valueOf() + '"' + ',';
		}
		if (typeof obj.iso2 !== "undefined") {
			result += '"iso2" : ' + '"' + obj.iso2.valueOf() + '"' + ',';
		}
		if (typeof obj.isoNumerisch !== "undefined") {
			result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : '"' + obj.isoNumerisch.valueOf() + '"') + ',';
		}
		if (typeof obj.codeDEStatis !== "undefined") {
			result += '"codeDEStatis" : ' + '"' + obj.codeDEStatis.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnungSuche !== "undefined") {
			result += '"bezeichnungSuche" : ' + '"' + obj.bezeichnungSuche.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnungLang !== "undefined") {
			result += '"bezeichnungLang" : ' + '"' + obj.bezeichnungLang.valueOf() + '"' + ',';
		}
		if (typeof obj.staatsangehoerigkeit !== "undefined") {
			result += '"staatsangehoerigkeit" : ' + '"' + obj.staatsangehoerigkeit.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_NationalitaetenKatalogEintrag(obj : unknown) : NationalitaetenKatalogEintrag {
	return obj as NationalitaetenKatalogEintrag;
}
