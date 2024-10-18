import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class KAOAMerkmalKatalogEintrag extends CoreTypeData {

	/**
	 * Die Kategorie, welcher das Merkmal zugeordnet ist.
	 */
	public kategorie : string = "";

	/**
	 * Die Optionsart des Merkmals.
	 */
	public optionsart : string | null = null;

	/**
	 * Die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf
	 */
	public bkAnlagen : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag'].includes(name);
	}

	public static class = new Class<KAOAMerkmalKatalogEintrag>('de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag');

	public static transpilerFromJSON(json : string): KAOAMerkmalKatalogEintrag {
		const obj = JSON.parse(json) as Partial<KAOAMerkmalKatalogEintrag>;
		const result = new KAOAMerkmalKatalogEintrag();
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
		if (obj.kategorie === undefined)
			throw new Error('invalid json format, missing attribute kategorie');
		result.kategorie = obj.kategorie;
		result.optionsart = (obj.optionsart === undefined) ? null : obj.optionsart === null ? null : obj.optionsart;
		if (obj.bkAnlagen !== undefined) {
			for (const elem of obj.bkAnlagen) {
				result.bkAnlagen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KAOAMerkmalKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"kategorie" : ' + JSON.stringify(obj.kategorie) + ',';
		result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : JSON.stringify(obj.optionsart)) + ',';
		result += '"bkAnlagen" : [ ';
		for (let i = 0; i < obj.bkAnlagen.size(); i++) {
			const elem = obj.bkAnlagen.get(i);
			result += '"' + elem + '"';
			if (i < obj.bkAnlagen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAMerkmalKatalogEintrag>) : string {
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
		if (obj.kategorie !== undefined) {
			result += '"kategorie" : ' + JSON.stringify(obj.kategorie) + ',';
		}
		if (obj.optionsart !== undefined) {
			result += '"optionsart" : ' + ((!obj.optionsart) ? 'null' : JSON.stringify(obj.optionsart)) + ',';
		}
		if (obj.bkAnlagen !== undefined) {
			result += '"bkAnlagen" : [ ';
			for (let i = 0; i < obj.bkAnlagen.size(); i++) {
				const elem = obj.bkAnlagen.get(i);
				result += '"' + elem + '"';
				if (i < obj.bkAnlagen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kaoa_KAOAMerkmalKatalogEintrag(obj : unknown) : KAOAMerkmalKatalogEintrag {
	return obj as KAOAMerkmalKatalogEintrag;
}
