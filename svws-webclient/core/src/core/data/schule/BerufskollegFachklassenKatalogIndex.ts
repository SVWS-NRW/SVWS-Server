import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegFachklassenKatalogEintrag } from '../../../core/data/schule/BerufskollegFachklassenKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BerufskollegFachklassenKatalogIndex extends JavaObject {

	/**
	 * Der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen
	 */
	public index : number = 0;

	/**
	 * Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht.
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs.
	 */
	public fachklassen : List<BerufskollegFachklassenKatalogEintrag> = new ArrayList<BerufskollegFachklassenKatalogEintrag>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex'].includes(name);
	}

	public static class = new Class<BerufskollegFachklassenKatalogIndex>('de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex');

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogIndex {
		const obj = JSON.parse(json) as Partial<BerufskollegFachklassenKatalogIndex>;
		const result = new BerufskollegFachklassenKatalogIndex();
		if (obj.index === undefined)
			throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.fachklassen !== undefined) {
			for (const elem of obj.fachklassen) {
				result.fachklassen.add(BerufskollegFachklassenKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogIndex) : string {
		let result = '{';
		result += '"index" : ' + obj.index.toString() + ',';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"fachklassen" : [ ';
		for (let i = 0; i < obj.fachklassen.size(); i++) {
			const elem = obj.fachklassen.get(i);
			result += BerufskollegFachklassenKatalogEintrag.transpilerToJSON(elem);
			if (i < obj.fachklassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogIndex>) : string {
		let result = '{';
		if (obj.index !== undefined) {
			result += '"index" : ' + obj.index.toString() + ',';
		}
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.fachklassen !== undefined) {
			result += '"fachklassen" : [ ';
			for (let i = 0; i < obj.fachklassen.size(); i++) {
				const elem = obj.fachklassen.get(i);
				result += BerufskollegFachklassenKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.fachklassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_BerufskollegFachklassenKatalogIndex(obj : unknown) : BerufskollegFachklassenKatalogIndex {
	return obj as BerufskollegFachklassenKatalogIndex;
}
