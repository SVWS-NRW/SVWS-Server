import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausuren/GostKursklausur';
import { GostKlausurterminblockungKonfiguration } from '../../../../core/data/gost/klausuren/GostKlausurterminblockungKonfiguration';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKursklausurRich } from '../../../../core/data/gost/klausuren/GostKursklausurRich';

export class GostKlausurterminblockungDaten extends JavaObject {

	/**
	 * Die Konfiguration für den Blockungs-Algorithmus
	 */
	public konfiguration: GostKlausurterminblockungKonfiguration = new GostKlausurterminblockungKonfiguration();

	/**
	 * Die Kurs-Klausuren, für welche die Blockung durchgeführt werden soll.
	 */
	public kursklausuren: List<GostKursklausur> = new ArrayList<GostKursklausur>();

	/**
	 * Die um Informationen für den Blockungsalgorithmus angereicherten Kurs-Klausuren, für welche die Blockung durchgeführt werden soll.
	 */
	public kursklausurenRich: List<GostKursklausurRich> = new ArrayList<GostKursklausurRich>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurterminblockungDaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurterminblockungDaten');

	public static transpilerFromJSON(json: string): GostKlausurterminblockungDaten {
		const obj = JSON.parse(json) as Partial<GostKlausurterminblockungDaten>;
		const result = new GostKlausurterminblockungDaten();
		if (obj.konfiguration === undefined)
			throw new Error('invalid json format, missing attribute konfiguration');
		result.konfiguration = GostKlausurterminblockungKonfiguration.transpilerFromJSON(JSON.stringify(obj.konfiguration));
		if (obj.kursklausuren !== undefined) {
			for (const elem of obj.kursklausuren) {
				result.kursklausuren.add(GostKursklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kursklausurenRich !== undefined) {
			for (const elem of obj.kursklausurenRich) {
				result.kursklausurenRich.add(GostKursklausurRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurterminblockungDaten): string {
		let result = '{';
		result += '"konfiguration" : ' + GostKlausurterminblockungKonfiguration.transpilerToJSON(obj.konfiguration) + ',';
		result += '"kursklausuren" : [ ';
		for (let i = 0; i < obj.kursklausuren.size(); i++) {
			const elem = obj.kursklausuren.get(i);
			result += GostKursklausur.transpilerToJSON(elem);
			if (i < obj.kursklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kursklausurenRich" : [ ';
		for (let i = 0; i < obj.kursklausurenRich.size(); i++) {
			const elem = obj.kursklausurenRich.get(i);
			result += GostKursklausurRich.transpilerToJSON(elem);
			if (i < obj.kursklausurenRich.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostKlausurterminblockungDaten>): string {
		let result = '{';
		if (obj.konfiguration !== undefined) {
			result += '"konfiguration" : ' + GostKlausurterminblockungKonfiguration.transpilerToJSON(obj.konfiguration) + ',';
		}
		if (obj.kursklausuren !== undefined) {
			result += '"kursklausuren" : [ ';
			for (let i = 0; i < obj.kursklausuren.size(); i++) {
				const elem = obj.kursklausuren.get(i);
				result += GostKursklausur.transpilerToJSON(elem);
				if (i < obj.kursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kursklausurenRich !== undefined) {
			result += '"kursklausurenRich" : [ ';
			for (let i = 0; i < obj.kursklausurenRich.size(); i++) {
				const elem = obj.kursklausurenRich.get(i);
				result += GostKursklausurRich.transpilerToJSON(elem);
				if (i < obj.kursklausurenRich.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurterminblockungDaten(obj: unknown): GostKlausurterminblockungDaten {
	return obj as GostKlausurterminblockungDaten;
}
