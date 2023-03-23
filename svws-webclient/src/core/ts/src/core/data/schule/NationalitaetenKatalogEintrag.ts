import { JavaObject } from '../../../java/lang/JavaObject';

export class NationalitaetenKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Das 3-stellige Kürzel des ISO-Standards 3166-1
	 */
	public iso3 : string = "";

	/**
	 * Das 2-stellige Kürzel des ISO-Standards 3166-1
	 */
	public iso2 : string = "";

	/**
	 * Die 3-stellige Nummer des ISO-Standards 3166-1
	 */
	public isoNumerisch : string | null = null;

	/**
	 * Die 3-stellige Nummer, welche vom statistischen Bundesamt verwendet wird (destatis.de)
	 */
	public codeDEStatis : string = "";

	/**
	 * Die Bezeichnung für eine Suche
	 */
	public bezeichnungSuche : string = "";

	/**
	 * Die kurze Bezeichnung
	 */
	public bezeichnung : string = "";

	/**
	 * Die Bezeichnung für eine Suche
	 */
	public bezeichnungLang : string = "";

	/**
	 * Die Bezeichnung der Staatsangehörigkeit
	 */
	public staatsangehoerigkeit : string = "";

	/**
	 * Gibt an, in welchem Schuljahr die Nationalität ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Nationalität verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


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
	public constructor(id : number, iso3 : string, iso2 : string, isoNumerisch : string | null, codeDEStatis : string, bezeichnungSuche : string, bezeichnung : string, bezeichnungLang : string, staatsangehoerigkeit : string, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : null | string, __param4? : string, __param5? : string, __param6? : string, __param7? : string, __param8? : string, __param9? : null | number, __param10? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string") || (__param3 === null)) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string")) && ((typeof __param5 !== "undefined") && (typeof __param5 === "string")) && ((typeof __param6 !== "undefined") && (typeof __param6 === "string")) && ((typeof __param7 !== "undefined") && (typeof __param7 === "string")) && ((typeof __param8 !== "undefined") && (typeof __param8 === "string")) && ((typeof __param9 !== "undefined") && (typeof __param9 === "number") || (__param9 === null)) && ((typeof __param10 !== "undefined") && (typeof __param10 === "number") || (__param10 === null))) {
			let id : number = __param0 as number;
			let iso3 : string = __param1;
			let iso2 : string = __param2;
			let isoNumerisch : string | null = __param3;
			let codeDEStatis : string = __param4;
			let bezeichnungSuche : string = __param5;
			let bezeichnung : string = __param6;
			let bezeichnungLang : string = __param7;
			let staatsangehoerigkeit : string = __param8;
			let gueltigVon : number | null = __param9;
			let gueltigBis : number | null = __param10;
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
		result.isoNumerisch = typeof obj.isoNumerisch === "undefined" ? null : obj.isoNumerisch === null ? null : obj.isoNumerisch;
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
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : NationalitaetenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"iso3" : ' + '"' + obj.iso3! + '"' + ',';
		result += '"iso2" : ' + '"' + obj.iso2! + '"' + ',';
		result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : '"' + obj.isoNumerisch + '"') + ',';
		result += '"codeDEStatis" : ' + '"' + obj.codeDEStatis! + '"' + ',';
		result += '"bezeichnungSuche" : ' + '"' + obj.bezeichnungSuche! + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"bezeichnungLang" : ' + '"' + obj.bezeichnungLang! + '"' + ',';
		result += '"staatsangehoerigkeit" : ' + '"' + obj.staatsangehoerigkeit! + '"' + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
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
			result += '"iso3" : ' + '"' + obj.iso3 + '"' + ',';
		}
		if (typeof obj.iso2 !== "undefined") {
			result += '"iso2" : ' + '"' + obj.iso2 + '"' + ',';
		}
		if (typeof obj.isoNumerisch !== "undefined") {
			result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : '"' + obj.isoNumerisch + '"') + ',';
		}
		if (typeof obj.codeDEStatis !== "undefined") {
			result += '"codeDEStatis" : ' + '"' + obj.codeDEStatis + '"' + ',';
		}
		if (typeof obj.bezeichnungSuche !== "undefined") {
			result += '"bezeichnungSuche" : ' + '"' + obj.bezeichnungSuche + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.bezeichnungLang !== "undefined") {
			result += '"bezeichnungLang" : ' + '"' + obj.bezeichnungLang + '"' + ',';
		}
		if (typeof obj.staatsangehoerigkeit !== "undefined") {
			result += '"staatsangehoerigkeit" : ' + '"' + obj.staatsangehoerigkeit + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_NationalitaetenKatalogEintrag(obj : unknown) : NationalitaetenKatalogEintrag {
	return obj as NationalitaetenKatalogEintrag;
}
