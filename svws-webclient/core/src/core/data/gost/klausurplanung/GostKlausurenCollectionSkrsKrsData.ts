import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurenCollectionRaumData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionRaumData';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurterminraumstunde';
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
	 * Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden.
	 */
	public schuelerklausurterminraumstundenGeloescht : List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt die Daten der übergebenen Instanz zu den aktuellen Daten hinzu.
	 * @param data die zu hinzuzufügenden Daten
	 */
	public addAll(data : GostKlausurenCollectionSkrsKrsData) : void {
		this.raumdata.addAll(data.raumdata);
		this.raumstundenGeloescht.addAll(data.raumstundenGeloescht);
		this.schuelerklausurterminraumstundenGeloescht.addAll(data.schuelerklausurterminraumstundenGeloescht);
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
		if (obj.schuelerklausurterminraumstundenGeloescht !== undefined) {
			for (const elem of obj.schuelerklausurterminraumstundenGeloescht) {
				result.schuelerklausurterminraumstundenGeloescht.add(GostSchuelerklausurterminraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
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
		result += '"schuelerklausurterminraumstundenGeloescht" : [ ';
		for (let i = 0; i < obj.schuelerklausurterminraumstundenGeloescht.size(); i++) {
			const elem = obj.schuelerklausurterminraumstundenGeloescht.get(i);
			result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
			if (i < obj.schuelerklausurterminraumstundenGeloescht.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
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
		if (obj.schuelerklausurterminraumstundenGeloescht !== undefined) {
			result += '"schuelerklausurterminraumstundenGeloescht" : [ ';
			for (let i = 0; i < obj.schuelerklausurterminraumstundenGeloescht.size(); i++) {
				const elem = obj.schuelerklausurterminraumstundenGeloescht.get(i);
				result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
				if (i < obj.schuelerklausurterminraumstundenGeloescht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionSkrsKrsData(obj : unknown) : GostKlausurenCollectionSkrsKrsData {
	return obj as GostKlausurenCollectionSkrsKrsData;
}
