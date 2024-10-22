import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { CoreTypeData, cast_de_svws_nrw_asd_data_CoreTypeData } from '../../asd/data/CoreTypeData';

export class CoreTypeDataNurSchulformen extends CoreTypeData {

	/**
	 * Die Kürzel der Schulformen, bei welchen der Jahrgang vorkommt.
	 */
	public schulformen : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.CoreTypeDataNurSchulformen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData', 'de.svws_nrw.asd.data.CoreTypeDataNurSchulformen'].includes(name);
	}

	public static class = new Class<CoreTypeDataNurSchulformen>('de.svws_nrw.asd.data.CoreTypeDataNurSchulformen');

	public static transpilerFromJSON(json : string): CoreTypeDataNurSchulformen {
		const obj = JSON.parse(json) as Partial<CoreTypeDataNurSchulformen>;
		const result = new CoreTypeDataNurSchulformen();
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
		if (obj.schulformen !== undefined) {
			for (const elem of obj.schulformen) {
				result.schulformen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : CoreTypeDataNurSchulformen) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"schulformen" : [ ';
		for (let i = 0; i < obj.schulformen.size(); i++) {
			const elem = obj.schulformen.get(i);
			result += '"' + elem + '"';
			if (i < obj.schulformen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<CoreTypeDataNurSchulformen>) : string {
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
		if (obj.schulformen !== undefined) {
			result += '"schulformen" : [ ';
			for (let i = 0; i < obj.schulformen.size(); i++) {
				const elem = obj.schulformen.get(i);
				result += '"' + elem + '"';
				if (i < obj.schulformen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_CoreTypeDataNurSchulformen(obj : unknown) : CoreTypeDataNurSchulformen {
	return obj as CoreTypeDataNurSchulformen;
}
