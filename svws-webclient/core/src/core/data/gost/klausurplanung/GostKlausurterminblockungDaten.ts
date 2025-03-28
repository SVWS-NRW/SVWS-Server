import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostKlausurterminblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungKonfiguration';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKursklausurRich } from '../../../../core/data/gost/klausurplanung/GostKursklausurRich';

export class GostKlausurterminblockungDaten extends JavaObject {

	/**
	 * Die Konfiguration für den Blockungs-Algorithmus
	 */
	public konfiguration : GostKlausurterminblockungKonfiguration = new GostKlausurterminblockungKonfiguration();

	/**
	 * Die Kurs-Klausuren, für welche die Blockung durchgeführt werden soll.
	 */
	public klausuren : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	/**
	 * Die um Informationen für den Blockungsalgorithmus angereicherten Kurs-Klausuren, für welche die Blockung durchgeführt werden soll.
	 */
	public richKlausuren : List<GostKursklausurRich> = new ArrayList<GostKursklausurRich>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten'].includes(name);
	}

	public static class = new Class<GostKlausurterminblockungDaten>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten');

	public static transpilerFromJSON(json : string): GostKlausurterminblockungDaten {
		const obj = JSON.parse(json) as Partial<GostKlausurterminblockungDaten>;
		const result = new GostKlausurterminblockungDaten();
		if (obj.konfiguration === undefined)
			throw new Error('invalid json format, missing attribute konfiguration');
		result.konfiguration = GostKlausurterminblockungKonfiguration.transpilerFromJSON(JSON.stringify(obj.konfiguration));
		if (obj.klausuren !== undefined) {
			for (const elem of obj.klausuren) {
				result.klausuren.add(GostKursklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.richKlausuren !== undefined) {
			for (const elem of obj.richKlausuren) {
				result.richKlausuren.add(GostKursklausurRich.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurterminblockungDaten) : string {
		let result = '{';
		result += '"konfiguration" : ' + GostKlausurterminblockungKonfiguration.transpilerToJSON(obj.konfiguration) + ',';
		result += '"klausuren" : [ ';
		for (let i = 0; i < obj.klausuren.size(); i++) {
			const elem = obj.klausuren.get(i);
			result += GostKursklausur.transpilerToJSON(elem);
			if (i < obj.klausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"richKlausuren" : [ ';
		for (let i = 0; i < obj.richKlausuren.size(); i++) {
			const elem = obj.richKlausuren.get(i);
			result += GostKursklausurRich.transpilerToJSON(elem);
			if (i < obj.richKlausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurterminblockungDaten>) : string {
		let result = '{';
		if (obj.konfiguration !== undefined) {
			result += '"konfiguration" : ' + GostKlausurterminblockungKonfiguration.transpilerToJSON(obj.konfiguration) + ',';
		}
		if (obj.klausuren !== undefined) {
			result += '"klausuren" : [ ';
			for (let i = 0; i < obj.klausuren.size(); i++) {
				const elem = obj.klausuren.get(i);
				result += GostKursklausur.transpilerToJSON(elem);
				if (i < obj.klausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.richKlausuren !== undefined) {
			result += '"richKlausuren" : [ ';
			for (let i = 0; i < obj.richKlausuren.size(); i++) {
				const elem = obj.richKlausuren.get(i);
				result += GostKursklausurRich.transpilerToJSON(elem);
				if (i < obj.richKlausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurterminblockungDaten(obj : unknown) : GostKlausurterminblockungDaten {
	return obj as GostKlausurterminblockungDaten;
}
