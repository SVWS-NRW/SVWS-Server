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
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined) && (__param9 === undefined) && (__param10 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && (typeof __param3 === "string") || (__param3 === null)) && ((__param4 !== undefined) && (typeof __param4 === "string")) && ((__param5 !== undefined) && (typeof __param5 === "string")) && ((__param6 !== undefined) && (typeof __param6 === "string")) && ((__param7 !== undefined) && (typeof __param7 === "string")) && ((__param8 !== undefined) && (typeof __param8 === "string")) && ((__param9 !== undefined) && (typeof __param9 === "number") || (__param9 === null)) && ((__param10 !== undefined) && (typeof __param10 === "number") || (__param10 === null))) {
			const id : number = __param0 as number;
			const iso3 : string = __param1;
			const iso2 : string = __param2;
			const isoNumerisch : string | null = __param3;
			const codeDEStatis : string = __param4;
			const bezeichnungSuche : string = __param5;
			const bezeichnung : string = __param6;
			const bezeichnungLang : string = __param7;
			const staatsangehoerigkeit : string = __param8;
			const gueltigVon : number | null = __param9;
			const gueltigBis : number | null = __param10;
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.NationalitaetenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.NationalitaetenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): NationalitaetenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new NationalitaetenKatalogEintrag();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.iso3 === undefined)
			 throw new Error('invalid json format, missing attribute iso3');
		result.iso3 = obj.iso3;
		if (obj.iso2 === undefined)
			 throw new Error('invalid json format, missing attribute iso2');
		result.iso2 = obj.iso2;
		result.isoNumerisch = (obj.isoNumerisch === undefined) ? null : obj.isoNumerisch === null ? null : obj.isoNumerisch;
		if (obj.codeDEStatis === undefined)
			 throw new Error('invalid json format, missing attribute codeDEStatis');
		result.codeDEStatis = obj.codeDEStatis;
		if (obj.bezeichnungSuche === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnungSuche');
		result.bezeichnungSuche = obj.bezeichnungSuche;
		if (obj.bezeichnung === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.bezeichnungLang === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnungLang');
		result.bezeichnungLang = obj.bezeichnungLang;
		if (obj.staatsangehoerigkeit === undefined)
			 throw new Error('invalid json format, missing attribute staatsangehoerigkeit');
		result.staatsangehoerigkeit = obj.staatsangehoerigkeit;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : NationalitaetenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"iso3" : ' + JSON.stringify(obj.iso3!) + ',';
		result += '"iso2" : ' + JSON.stringify(obj.iso2!) + ',';
		result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : JSON.stringify(obj.isoNumerisch)) + ',';
		result += '"codeDEStatis" : ' + JSON.stringify(obj.codeDEStatis!) + ',';
		result += '"bezeichnungSuche" : ' + JSON.stringify(obj.bezeichnungSuche!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"bezeichnungLang" : ' + JSON.stringify(obj.bezeichnungLang!) + ',';
		result += '"staatsangehoerigkeit" : ' + JSON.stringify(obj.staatsangehoerigkeit!) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NationalitaetenKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.iso3 !== undefined) {
			result += '"iso3" : ' + JSON.stringify(obj.iso3!) + ',';
		}
		if (obj.iso2 !== undefined) {
			result += '"iso2" : ' + JSON.stringify(obj.iso2!) + ',';
		}
		if (obj.isoNumerisch !== undefined) {
			result += '"isoNumerisch" : ' + ((!obj.isoNumerisch) ? 'null' : JSON.stringify(obj.isoNumerisch)) + ',';
		}
		if (obj.codeDEStatis !== undefined) {
			result += '"codeDEStatis" : ' + JSON.stringify(obj.codeDEStatis!) + ',';
		}
		if (obj.bezeichnungSuche !== undefined) {
			result += '"bezeichnungSuche" : ' + JSON.stringify(obj.bezeichnungSuche!) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (obj.bezeichnungLang !== undefined) {
			result += '"bezeichnungLang" : ' + JSON.stringify(obj.bezeichnungLang!) + ',';
		}
		if (obj.staatsangehoerigkeit !== undefined) {
			result += '"staatsangehoerigkeit" : ' + JSON.stringify(obj.staatsangehoerigkeit!) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_NationalitaetenKatalogEintrag(obj : unknown) : NationalitaetenKatalogEintrag {
	return obj as NationalitaetenKatalogEintrag;
}
