import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausuren/GostSchuelerklausurterminraumstunde';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKlausurraum } from '../../../../core/data/gost/klausuren/GostKlausurraum';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausuren/GostKlausurraumstunde';

export class GostKlausurenRaumdaten extends JavaObject {

	/**
	 * Ein Array mit den Klausurräumen.
	 */
	public raeume: List<GostKlausurraum> = new ArrayList<GostKlausurraum>();

	/**
	 * Ein Array mit den Klausurraumstunden.
	 */
	public raumstunden: List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	/**
	 * Ein Array mit den Schülerklausurtermin-Raumstunden.
	 */
	public schuelerklausurterminRaumstunden: List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();

	/**
	 * Ein Array mit den IDs der Klausurtermine, zu denen Raumdaten enthalten sind.
	 */
	public idsKlausurtermine: List<number> = new ArrayList<number>();


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
	public addAll(data: GostKlausurenRaumdaten): void {
		this.raeume.addAll(data.raeume);
		this.raumstunden.addAll(data.raumstunden);
		this.schuelerklausurterminRaumstunden.addAll(data.schuelerklausurterminRaumstunden);
		this.idsKlausurtermine.addAll(data.idsKlausurtermine);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenRaumdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenRaumdaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenRaumdaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenRaumdaten');

	public static transpilerFromJSON(json: string): GostKlausurenRaumdaten {
		const obj = JSON.parse(json) as Partial<GostKlausurenRaumdaten>;
		const result = new GostKlausurenRaumdaten();
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
		if (obj.schuelerklausurterminRaumstunden !== undefined) {
			for (const elem of obj.schuelerklausurterminRaumstunden) {
				result.schuelerklausurterminRaumstunden.add(GostSchuelerklausurterminraumstunde.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.idsKlausurtermine !== undefined) {
			for (const elem of obj.idsKlausurtermine) {
				result.idsKlausurtermine.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurenRaumdaten): string {
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
		result += '"schuelerklausurterminRaumstunden" : [ ';
		for (let i = 0; i < obj.schuelerklausurterminRaumstunden.size(); i++) {
			const elem = obj.schuelerklausurterminRaumstunden.get(i);
			result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
			if (i < obj.schuelerklausurterminRaumstunden.size() - 1)
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

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenRaumdaten>): string {
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
		if (obj.schuelerklausurterminRaumstunden !== undefined) {
			result += '"schuelerklausurterminRaumstunden" : [ ';
			for (let i = 0; i < obj.schuelerklausurterminRaumstunden.size(); i++) {
				const elem = obj.schuelerklausurterminRaumstunden.get(i);
				result += GostSchuelerklausurterminraumstunde.transpilerToJSON(elem);
				if (i < obj.schuelerklausurterminRaumstunden.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenRaumdaten(obj: unknown): GostKlausurenRaumdaten {
	return obj as GostKlausurenRaumdaten;
}
