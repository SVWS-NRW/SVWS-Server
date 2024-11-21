import { SchulformSchulgliederung } from '../../../asd/data/schule/SchulformSchulgliederung';
import { CoreTypeDataNurSchulformenUndSchulgliederungen } from '../../../asd/data/CoreTypeDataNurSchulformenUndSchulgliederungen';
import { Class } from '../../../java/lang/Class';

export class HerkunftBildungsgangTypKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen', 'de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<HerkunftBildungsgangTypKatalogEintrag>('de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag');

	public static transpilerFromJSON(json : string): HerkunftBildungsgangTypKatalogEintrag {
		const obj = JSON.parse(json) as Partial<HerkunftBildungsgangTypKatalogEintrag>;
		const result = new HerkunftBildungsgangTypKatalogEintrag();
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
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
		return result;
	}

	public static transpilerToJSON(obj : HerkunftBildungsgangTypKatalogEintrag) : string {
		let result = '{';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformSchulgliederung.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<HerkunftBildungsgangTypKatalogEintrag>) : string {
		let result = '{';
		if (obj.zulaessig !== undefined) {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_HerkunftBildungsgangTypKatalogEintrag(obj : unknown) : HerkunftBildungsgangTypKatalogEintrag {
	return obj as HerkunftBildungsgangTypKatalogEintrag;
}
