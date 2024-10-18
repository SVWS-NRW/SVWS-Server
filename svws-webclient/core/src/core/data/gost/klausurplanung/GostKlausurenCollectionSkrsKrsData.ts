import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurenCollectionRaumData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionRaumData';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostKlausurraumstunde';

export class GostKlausurenCollectionSkrsKrsData extends JavaObject {

	/**
	 * Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt.
	 */
	public raumdata : GostKlausurenCollectionRaumData = new GostKlausurenCollectionRaumData();

	/**
	 * Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden.
	 */
	public raumstundenGeloescht : List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	/**
	 * Ein Array mit den IDs der Schülerklausurtermine, bei denen Daten geändert wurden.
	 */
	public idsSchuelerklausurtermine : List<number> = new ArrayList<number>();

	/**
	 * Die gepatchte Kursklausur.
	 */
	public kursKlausurPatched : GostKursklausur | null = null;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData'].includes(name);
	}

	public static class = new Class<GostKlausurenCollectionSkrsKrsData>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData');

	public static transpilerFromJSON(json : string): GostKlausurenCollectionSkrsKrsData {
		const obj = JSON.parse(json) as Partial<GostKlausurenCollectionSkrsKrsData>;
		const result = new GostKlausurenCollectionSkrsKrsData();
		if (obj.raumdata === undefined)
			throw new Error('invalid json format, missing attribute raumdata');
		result.raumdata = GostKlausurenCollectionRaumData.transpilerFromJSON(JSON.stringify(obj.raumdata));
		if (obj.raumstundenGeloescht !== undefined) {
			for (const elem of obj.raumstundenGeloescht) {
				result.raumstundenGeloescht.add(GostKlausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.idsSchuelerklausurtermine !== undefined) {
			for (const elem of obj.idsSchuelerklausurtermine) {
				result.idsSchuelerklausurtermine.add(elem);
			}
		}
		result.kursKlausurPatched = ((obj.kursKlausurPatched === undefined) || (obj.kursKlausurPatched === null)) ? null : GostKursklausur.transpilerFromJSON(JSON.stringify(obj.kursKlausurPatched));
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionSkrsKrsData) : string {
		let result = '{';
		result += '"raumdata" : ' + GostKlausurenCollectionRaumData.transpilerToJSON(obj.raumdata) + ',';
		result += '"raumstundenGeloescht" : [ ';
		for (let i = 0; i < obj.raumstundenGeloescht.size(); i++) {
			const elem = obj.raumstundenGeloescht.get(i);
			result += GostKlausurraumstunde.transpilerToJSON(elem);
			if (i < obj.raumstundenGeloescht.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsSchuelerklausurtermine" : [ ';
		for (let i = 0; i < obj.idsSchuelerklausurtermine.size(); i++) {
			const elem = obj.idsSchuelerklausurtermine.get(i);
			result += elem.toString();
			if (i < obj.idsSchuelerklausurtermine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kursKlausurPatched" : ' + ((!obj.kursKlausurPatched) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursKlausurPatched)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionSkrsKrsData>) : string {
		let result = '{';
		if (obj.raumdata !== undefined) {
			result += '"raumdata" : ' + GostKlausurenCollectionRaumData.transpilerToJSON(obj.raumdata) + ',';
		}
		if (obj.raumstundenGeloescht !== undefined) {
			result += '"raumstundenGeloescht" : [ ';
			for (let i = 0; i < obj.raumstundenGeloescht.size(); i++) {
				const elem = obj.raumstundenGeloescht.get(i);
				result += GostKlausurraumstunde.transpilerToJSON(elem);
				if (i < obj.raumstundenGeloescht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idsSchuelerklausurtermine !== undefined) {
			result += '"idsSchuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.idsSchuelerklausurtermine.size(); i++) {
				const elem = obj.idsSchuelerklausurtermine.get(i);
				result += elem.toString();
				if (i < obj.idsSchuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kursKlausurPatched !== undefined) {
			result += '"kursKlausurPatched" : ' + ((!obj.kursKlausurPatched) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursKlausurPatched)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionSkrsKrsData(obj : unknown) : GostKlausurenCollectionSkrsKrsData {
	return obj as GostKlausurenCollectionSkrsKrsData;
}
