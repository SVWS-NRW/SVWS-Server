import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { CoreTypeData } from '../../../asd/data/CoreTypeData';

export class KAOAAnschlussoptionenKatalogEintrag extends CoreTypeData {

	/**
	 * Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)
	 */
	public stufen : List<string> = new ArrayList<string>();

	/**
	 * Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden
	 */
	public anzeigeZusatzmerkmal : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<KAOAAnschlussoptionenKatalogEintrag>('de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag');

	public static transpilerFromJSON(json : string): KAOAAnschlussoptionenKatalogEintrag {
		const obj = JSON.parse(json) as Partial<KAOAAnschlussoptionenKatalogEintrag>;
		const result = new KAOAAnschlussoptionenKatalogEintrag();
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
		if (obj.stufen !== undefined) {
			for (const elem of obj.stufen) {
				result.stufen.add(elem);
			}
		}
		if (obj.anzeigeZusatzmerkmal !== undefined) {
			for (const elem of obj.anzeigeZusatzmerkmal) {
				result.anzeigeZusatzmerkmal.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KAOAAnschlussoptionenKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"stufen" : [ ';
		for (let i = 0; i < obj.stufen.size(); i++) {
			const elem = obj.stufen.get(i);
			result += '"' + elem + '"';
			if (i < obj.stufen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anzeigeZusatzmerkmal" : [ ';
		for (let i = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
			const elem = obj.anzeigeZusatzmerkmal.get(i);
			result += '"' + elem + '"';
			if (i < obj.anzeigeZusatzmerkmal.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KAOAAnschlussoptionenKatalogEintrag>) : string {
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
		if (obj.stufen !== undefined) {
			result += '"stufen" : [ ';
			for (let i = 0; i < obj.stufen.size(); i++) {
				const elem = obj.stufen.get(i);
				result += '"' + elem + '"';
				if (i < obj.stufen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anzeigeZusatzmerkmal !== undefined) {
			result += '"anzeigeZusatzmerkmal" : [ ';
			for (let i = 0; i < obj.anzeigeZusatzmerkmal.size(); i++) {
				const elem = obj.anzeigeZusatzmerkmal.get(i);
				result += '"' + elem + '"';
				if (i < obj.anzeigeZusatzmerkmal.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_kaoa_KAOAAnschlussoptionenKatalogEintrag(obj : unknown) : KAOAAnschlussoptionenKatalogEintrag {
	return obj as KAOAAnschlussoptionenKatalogEintrag;
}
