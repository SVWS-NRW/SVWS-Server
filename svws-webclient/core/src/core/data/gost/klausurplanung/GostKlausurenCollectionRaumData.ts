import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurterminraumstunde';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurraum } from '../../../../core/data/gost/klausurplanung/GostKlausurraum';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostKlausurraumstunde';

export class GostKlausurenCollectionRaumData extends JavaObject {

	/**
	 * Ein Array mit den Klausurräumen.
	 */
	public raeume : List<GostKlausurraum> = new ArrayList<GostKlausurraum>();

	/**
	 * Ein Array mit den Klausurraumstunden.
	 */
	public raumstunden : List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	/**
	 * Ein Array mit den Schülerklausurtermin-Raumstunden.
	 */
	public sktRaumstunden : List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();

	/**
	 * Ein Array mit den IDs der Klausurtermine, zu denen Raumdaten enthalten sind.
	 */
	public idsKlausurtermine : List<number> = new ArrayList<number>();


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
	public addAll(data : GostKlausurenCollectionRaumData) : void {
		this.raeume.addAll(data.raeume);
		this.raumstunden.addAll(data.raumstunden);
		this.sktRaumstunden.addAll(data.sktRaumstunden);
		this.idsKlausurtermine.addAll(data.idsKlausurtermine);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData'].includes(name);
	}

	public static class = new Class<GostKlausurenCollectionRaumData>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData');

	public static transpilerFromJSON(json : string): GostKlausurenCollectionRaumData {
		const obj = JSON.parse(json) as Partial<GostKlausurenCollectionRaumData>;
		const result = new GostKlausurenCollectionRaumData();
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(GostKlausurraum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raumstunden !== undefined) {
			for (const elem of obj.raumstunden) {
				result.raumstunden.add(GostKlausurraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sktRaumstunden !== undefined) {
			for (const elem of obj.sktRaumstunden) {
				result.sktRaumstunden.add(GostSchuelerklausurterminraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.idsKlausurtermine !== undefined) {
			for (const elem of obj.idsKlausurtermine) {
				result.idsKlausurtermine.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionRaumData) : string {
		let result = '{';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += GostKlausurraum.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raumstunden" : [ ';
		for (let i = 0; i < obj.raumstunden.size(); i++) {
			const elem = obj.raumstunden.get(i);
			result += GostKlausurraumstunde.transpilerToJSON(elem);
			if (i < obj.raumstunden.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sktRaumstunden" : [ ';
		for (let i = 0; i < obj.sktRaumstunden.size(); i++) {
			const elem = obj.sktRaumstunden.get(i);
			result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
			if (i < obj.sktRaumstunden.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsKlausurtermine" : [ ';
		for (let i = 0; i < obj.idsKlausurtermine.size(); i++) {
			const elem = obj.idsKlausurtermine.get(i);
			result += elem.toString();
			if (i < obj.idsKlausurtermine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionRaumData>) : string {
		let result = '{';
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += GostKlausurraum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raumstunden !== undefined) {
			result += '"raumstunden" : [ ';
			for (let i = 0; i < obj.raumstunden.size(); i++) {
				const elem = obj.raumstunden.get(i);
				result += GostKlausurraumstunde.transpilerToJSON(elem);
				if (i < obj.raumstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sktRaumstunden !== undefined) {
			result += '"sktRaumstunden" : [ ';
			for (let i = 0; i < obj.sktRaumstunden.size(); i++) {
				const elem = obj.sktRaumstunden.get(i);
				result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
				if (i < obj.sktRaumstunden.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idsKlausurtermine !== undefined) {
			result += '"idsKlausurtermine" : [ ';
			for (let i = 0; i < obj.idsKlausurtermine.size(); i++) {
				const elem = obj.idsKlausurtermine.get(i);
				result += elem.toString();
				if (i < obj.idsKlausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionRaumData(obj : unknown) : GostKlausurenCollectionRaumData {
	return obj as GostKlausurenCollectionRaumData;
}
