import { SchulformSchulgliederung } from '../../asd/data/schule/SchulformSchulgliederung';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { CoreTypeData, cast_de_svws_nrw_asd_data_CoreTypeData } from '../../asd/data/CoreTypeData';

export class CoreTypeDataNurSchulformenUndSchulgliederungen extends CoreTypeData {

	/**
	 * Die Informationen zu Schulformen und -gliederungen, wo die Core-Type-Daten zulässig sind.
	 */
	public zulaessig : List<SchulformSchulgliederung> = new ArrayList<SchulformSchulgliederung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<CoreTypeDataNurSchulformenUndSchulgliederungen>('de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen');

	public static transpilerFromJSON(json : string): CoreTypeDataNurSchulformenUndSchulgliederungen {
		const obj = JSON.parse(json) as Partial<CoreTypeDataNurSchulformenUndSchulgliederungen>;
		const result = new CoreTypeDataNurSchulformenUndSchulgliederungen();
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
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : CoreTypeDataNurSchulformenUndSchulgliederungen) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformSchulgliederung.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<CoreTypeDataNurSchulformenUndSchulgliederungen>) : string {
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformenUndSchulgliederungen(obj : unknown) : CoreTypeDataNurSchulformenUndSchulgliederungen {
	return obj as CoreTypeDataNurSchulformenUndSchulgliederungen;
}
