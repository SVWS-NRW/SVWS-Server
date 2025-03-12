import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class NationalitaetenKatalogEintrag extends CoreTypeData {

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
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<NationalitaetenKatalogEintrag>('de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag');

	public static transpilerFromJSON(json : string): NationalitaetenKatalogEintrag {
		const obj = JSON.parse(json) as Partial<NationalitaetenKatalogEintrag>;
		const result = new NationalitaetenKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
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
		return result;
	}

	public static transpilerToJSON(obj : NationalitaetenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"iso3" : ' + JSON.stringify(obj.iso3) + ',';
		result += '"iso2" : ' + JSON.stringify(obj.iso2) + ',';
		result += '"isoNumerisch" : ' + ((obj.isoNumerisch === null) ? 'null' : JSON.stringify(obj.isoNumerisch)) + ',';
		result += '"codeDEStatis" : ' + JSON.stringify(obj.codeDEStatis) + ',';
		result += '"bezeichnungSuche" : ' + JSON.stringify(obj.bezeichnungSuche) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"bezeichnungLang" : ' + JSON.stringify(obj.bezeichnungLang) + ',';
		result += '"staatsangehoerigkeit" : ' + JSON.stringify(obj.staatsangehoerigkeit) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<NationalitaetenKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.iso3 !== undefined) {
			result += '"iso3" : ' + JSON.stringify(obj.iso3) + ',';
		}
		if (obj.iso2 !== undefined) {
			result += '"iso2" : ' + JSON.stringify(obj.iso2) + ',';
		}
		if (obj.isoNumerisch !== undefined) {
			result += '"isoNumerisch" : ' + ((obj.isoNumerisch === null) ? 'null' : JSON.stringify(obj.isoNumerisch)) + ',';
		}
		if (obj.codeDEStatis !== undefined) {
			result += '"codeDEStatis" : ' + JSON.stringify(obj.codeDEStatis) + ',';
		}
		if (obj.bezeichnungSuche !== undefined) {
			result += '"bezeichnungSuche" : ' + JSON.stringify(obj.bezeichnungSuche) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.bezeichnungLang !== undefined) {
			result += '"bezeichnungLang" : ' + JSON.stringify(obj.bezeichnungLang) + ',';
		}
		if (obj.staatsangehoerigkeit !== undefined) {
			result += '"staatsangehoerigkeit" : ' + JSON.stringify(obj.staatsangehoerigkeit) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_NationalitaetenKatalogEintrag(obj : unknown) : NationalitaetenKatalogEintrag {
	return obj as NationalitaetenKatalogEintrag;
}
