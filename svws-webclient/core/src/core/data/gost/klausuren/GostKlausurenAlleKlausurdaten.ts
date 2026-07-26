import { JavaObject } from '../../../../java/lang/JavaObject';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { GostKlausurenHalbjahresdaten } from '../../../../core/data/gost/klausuren/GostKlausurenHalbjahresdaten';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurenAlleKlausurdaten extends JavaObject {

	/**
	 * Ein Array mit Paaren der enthaltenen Abiturjahrgänge / GostHalbjahre.
	 */
	public halbjahresdaten: List<GostKlausurenHalbjahresdaten> = new ArrayList<GostKlausurenHalbjahresdaten>();

	/**
	 * Ein Array mit den Daten der Lehrer.
	 */
	public lehrer: List<LehrerListeEintrag> = new ArrayList<LehrerListeEintrag>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten'].includes(name);
	}

	public static readonly class = new Class<GostKlausurenAlleKlausurdaten>('de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten');

	public static transpilerFromJSON(json: string): GostKlausurenAlleKlausurdaten {
		const obj = JSON.parse(json) as Partial<GostKlausurenAlleKlausurdaten>;
		const result = new GostKlausurenAlleKlausurdaten();
		if (obj.halbjahresdaten !== undefined) {
			for (const elem of obj.halbjahresdaten) {
				result.halbjahresdaten.add(GostKlausurenHalbjahresdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LehrerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostKlausurenAlleKlausurdaten): string {
		let result = '{';
		result += '"halbjahresdaten" : [ ';
		for (let i = 0; i < obj.halbjahresdaten.size(); i++) {
			const elem = obj.halbjahresdaten.get(i);
			result += GostKlausurenHalbjahresdaten.transpilerToJSON(elem);
			if (i < obj.halbjahresdaten.size() - 1)
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

	public static transpilerToJSONPatch(obj: Partial<GostKlausurenAlleKlausurdaten>): string {
		let result = '{';
		if (obj.halbjahresdaten !== undefined) {
			result += '"halbjahresdaten" : [ ';
			for (let i = 0; i < obj.halbjahresdaten.size(); i++) {
				const elem = obj.halbjahresdaten.get(i);
				result += GostKlausurenHalbjahresdaten.transpilerToJSON(elem);
				if (i < obj.halbjahresdaten.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurenAlleKlausurdaten(obj: unknown): GostKlausurenAlleKlausurdaten {
	return obj as GostKlausurenAlleKlausurdaten;
}
