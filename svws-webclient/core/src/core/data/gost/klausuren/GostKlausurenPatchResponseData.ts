import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausuren/GostKursklausur';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausuren/GostSchuelerklausurterminraumstunde';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostSchuelerklausurtermin } from '../../../../core/data/gost/klausuren/GostSchuelerklausurtermin';
import { GostKlausurenRaumdaten } from '../../../../core/data/gost/klausuren/GostKlausurenRaumdaten';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausuren/GostKlausurraumstunde';
import { GostKlausurtermin } from '../../../../core/data/gost/klausuren/GostKlausurtermin';

export class GostKlausurenPatchResponseData extends JavaObject {

	/**
	 * Die gepatchte Kursklausur.
	 */
	public kursklausurPatched: GostKursklausur | null = null;

	/**
	 * Der gepatchte Klausurtermin.
	 */
	public terminPatched: GostKlausurtermin | null = null;

	/**
	 * Ein Array mit den gepatchten Schülerklausurterminen.
	 */
	public schuelerklausurterminePatched: List<GostSchuelerklausurtermin> = new ArrayList<GostSchuelerklausurtermin>();

	/**
	 * Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt.
	 */
	public raumdaten: GostKlausurenRaumdaten = new GostKlausurenRaumdaten();

	/**
	 * Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden.
	 */
	public raumstundenGeloescht: List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	/**
	 * Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden.
	 */
	public schuelerklausurterminraumstundenGeloescht: List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();


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
	public addAll(data: GostKlausurenPatchResponseData): void {
		if (data.kursklausurPatched !== null) {
			this.kursklausurPatched = data.kursklausurPatched;
		}
		if (data.terminPatched !== null) {
			this.terminPatched = data.terminPatched;
		}
		this.schuelerklausurterminePatched.addAll(data.schuelerklausurterminePatched);
		this.raumdaten.addAll(data.raumdaten);
		this.raumstundenGeloescht.addAll(data.raumstundenGeloescht);
		this.schuelerklausurterminraumstundenGeloescht.addAll(data.schuelerklausurterminraumstundenGeloescht);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenPatchResponseData>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData');

	public static transpilerFromJSON(json: string): GostKlausurenPatchResponseData {
		const obj = JSON.parse(json) as Partial<GostKlausurenPatchResponseData>;
		const result = new GostKlausurenPatchResponseData();
		result.kursklausurPatched = ((obj.kursklausurPatched === undefined) || (obj.kursklausurPatched === null)) ? null : GostKursklausur.transpilerFromJSON(JSON.stringify(obj.kursklausurPatched));
		result.terminPatched = ((obj.terminPatched === undefined) || (obj.terminPatched === null)) ? null : GostKlausurtermin.transpilerFromJSON(JSON.stringify(obj.terminPatched));
		if (obj.schuelerklausurterminePatched !== undefined) {
			for (const elem of obj.schuelerklausurterminePatched) {
				result.schuelerklausurterminePatched.add(GostSchuelerklausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raumdaten === undefined)
			throw new Error('invalid json format, missing attribute raumdaten');
		result.raumdaten = GostKlausurenRaumdaten.transpilerFromJSON(JSON.stringify(obj.raumdaten));
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

	public static transpilerToJSON(obj: GostKlausurenPatchResponseData): string {
		let result = '{';
		result += '"kursklausurPatched" : ' + ((obj.kursklausurPatched === null) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursklausurPatched)) + ',';
		result += '"terminPatched" : ' + ((obj.terminPatched === null) ? 'null' : GostKlausurtermin.transpilerToJSON(obj.terminPatched)) + ',';
		result += '"schuelerklausurterminePatched" : [ ';
		for (let i = 0; i < obj.schuelerklausurterminePatched.size(); i++) {
			const elem = obj.schuelerklausurterminePatched.get(i);
			result += GostSchuelerklausurtermin.transpilerToJSON(elem);
			if (i < obj.schuelerklausurterminePatched.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raumdaten" : ' + GostKlausurenRaumdaten.transpilerToJSON(obj.raumdaten) + ',';
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

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenPatchResponseData>): string {
		let result = '{';
		if (obj.kursklausurPatched !== undefined) {
			result += '"kursklausurPatched" : ' + ((obj.kursklausurPatched === null) ? 'null' : GostKursklausur.transpilerToJSON(obj.kursklausurPatched)) + ',';
		}
		if (obj.terminPatched !== undefined) {
			result += '"terminPatched" : ' + ((obj.terminPatched === null) ? 'null' : GostKlausurtermin.transpilerToJSON(obj.terminPatched)) + ',';
		}
		if (obj.schuelerklausurterminePatched !== undefined) {
			result += '"schuelerklausurterminePatched" : [ ';
			for (let i = 0; i < obj.schuelerklausurterminePatched.size(); i++) {
				const elem = obj.schuelerklausurterminePatched.get(i);
				result += GostSchuelerklausurtermin.transpilerToJSON(elem);
				if (i < obj.schuelerklausurterminePatched.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raumdaten !== undefined) {
			result += '"raumdaten" : ' + GostKlausurenRaumdaten.transpilerToJSON(obj.raumdaten) + ',';
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

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenPatchResponseData(obj: unknown): GostKlausurenPatchResponseData {
	return obj as GostKlausurenPatchResponseData;
}
