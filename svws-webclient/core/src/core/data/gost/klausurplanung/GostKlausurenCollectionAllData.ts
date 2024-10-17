import { JavaObject } from '../../../../java/lang/JavaObject';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurenCollectionHjData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionHjData';

export class GostKlausurenCollectionAllData extends JavaObject {

	/**
	 * Ein Array mit Paaren der enthaltenen Abiturjahrgänge / GostHalbjahre.
	 */
	public datacontained : List<GostKlausurenCollectionHjData> = new ArrayList<GostKlausurenCollectionHjData>();

	/**
	 * Ein Array mit den Daten der Lehrer.
	 */
	public lehrer : List<LehrerListeEintrag> = new ArrayList<LehrerListeEintrag>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData'].includes(name);
	}

	public static class = new Class<GostKlausurenCollectionAllData>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData');

	public static transpilerFromJSON(json : string): GostKlausurenCollectionAllData {
		const obj = JSON.parse(json) as Partial<GostKlausurenCollectionAllData>;
		const result = new GostKlausurenCollectionAllData();
		if (obj.datacontained !== undefined) {
			for (const elem of obj.datacontained) {
				result.datacontained.add(GostKlausurenCollectionHjData.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LehrerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionAllData) : string {
		let result = '{';
		result += '"datacontained" : [ ';
		for (let i = 0; i < obj.datacontained.size(); i++) {
			const elem = obj.datacontained.get(i);
			result += GostKlausurenCollectionHjData.transpilerToJSON(elem);
			if (i < obj.datacontained.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += LehrerListeEintrag.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionAllData>) : string {
		let result = '{';
		if (obj.datacontained !== undefined) {
			result += '"datacontained" : [ ';
			for (let i = 0; i < obj.datacontained.size(); i++) {
				const elem = obj.datacontained.get(i);
				result += GostKlausurenCollectionHjData.transpilerToJSON(elem);
				if (i < obj.datacontained.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LehrerListeEintrag.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionAllData(obj : unknown) : GostKlausurenCollectionAllData {
	return obj as GostKlausurenCollectionAllData;
}
