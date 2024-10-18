import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class KAOAEbene4KatalogEintrag extends CoreTypeData {

	/**
	 * Das Zusatzmerkmal, welcher der Eintrag zugeordnet ist.
	 */
	public zusatzmerkmal : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag'].includes(name);
	}

	public static class = new Class<KAOAEbene4KatalogEintrag>('de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag');

	public static transpilerFromJSON(json : string): KAOAEbene4KatalogEintrag {
		const obj = JSON.parse(json) as Partial<KAOAEbene4KatalogEintrag>;
		const result = new KAOAEbene4KatalogEintrag();
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
		if (obj.zusatzmerkmal === undefined)
			throw new Error('invalid json format, missing attribute zusatzmerkmal');
		result.zusatzmerkmal = obj.zusatzmerkmal;
		return result;
	}

	public static transpilerToJSON(obj : KAOAEbene4KatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"zusatzmerkmal" : ' + JSON.stringify(obj.zusatzmerkmal) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAEbene4KatalogEintrag>) : string {
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
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.zusatzmerkmal !== undefined) {
			result += '"zusatzmerkmal" : ' + JSON.stringify(obj.zusatzmerkmal) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kaoa_KAOAEbene4KatalogEintrag(obj : unknown) : KAOAEbene4KatalogEintrag {
	return obj as KAOAEbene4KatalogEintrag;
}
