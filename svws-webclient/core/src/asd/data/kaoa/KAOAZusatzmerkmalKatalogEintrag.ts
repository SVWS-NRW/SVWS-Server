import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class KAOAZusatzmerkmalKatalogEintrag extends CoreTypeData {

	/**
	 * Das Merkmal, welcher das Zusatzmerkmal zugeordnet ist.
	 */
	public merkmal : string = "";

	/**
	 * Die Optionsart des Zusatzmerkmals.
	 */
	public optionsart : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<KAOAZusatzmerkmalKatalogEintrag>('de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag');

	public static transpilerFromJSON(json : string): KAOAZusatzmerkmalKatalogEintrag {
		const obj = JSON.parse(json) as Partial<KAOAZusatzmerkmalKatalogEintrag>;
		const result = new KAOAZusatzmerkmalKatalogEintrag();
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
		if (obj.merkmal === undefined)
			throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = obj.merkmal;
		result.optionsart = (obj.optionsart === undefined) ? null : obj.optionsart === null ? null : obj.optionsart;
		return result;
	}

	public static transpilerToJSON(obj : KAOAZusatzmerkmalKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"merkmal" : ' + JSON.stringify(obj.merkmal) + ',';
		result += '"optionsart" : ' + ((obj.optionsart === null) ? 'null' : JSON.stringify(obj.optionsart)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAZusatzmerkmalKatalogEintrag>) : string {
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
		if (obj.merkmal !== undefined) {
			result += '"merkmal" : ' + JSON.stringify(obj.merkmal) + ',';
		}
		if (obj.optionsart !== undefined) {
			result += '"optionsart" : ' + ((obj.optionsart === null) ? 'null' : JSON.stringify(obj.optionsart)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kaoa_KAOAZusatzmerkmalKatalogEintrag(obj : unknown) : KAOAZusatzmerkmalKatalogEintrag {
	return obj as KAOAZusatzmerkmalKatalogEintrag;
}
