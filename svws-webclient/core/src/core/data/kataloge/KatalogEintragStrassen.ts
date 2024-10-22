import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KatalogEintragStrassen extends JavaObject {

	/**
	 * Katalog von ITNRW Straßen: zugehörige Ort
	 */
	public Ort : string | null = null;

	/**
	 * Katalog von ITNRW Straßen: zugehöriger Regionalschlüssel
	 */
	public RegSchl : string | null = null;

	/**
	 * Katalog von ITNRW Straßen: Straßenname
	 */
	public Strasse : string | null = null;

	/**
	 * Katalog von ITNRW Straßen: aktueller Stand
	 */
	public Stand : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.KatalogEintragStrassen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.KatalogEintragStrassen'].includes(name);
	}

	public static class = new Class<KatalogEintragStrassen>('de.svws_nrw.core.data.kataloge.KatalogEintragStrassen');

	public static transpilerFromJSON(json : string): KatalogEintragStrassen {
		const obj = JSON.parse(json) as Partial<KatalogEintragStrassen>;
		const result = new KatalogEintragStrassen();
		result.Ort = (obj.Ort === undefined) ? null : obj.Ort === null ? null : obj.Ort;
		result.RegSchl = (obj.RegSchl === undefined) ? null : obj.RegSchl === null ? null : obj.RegSchl;
		result.Strasse = (obj.Strasse === undefined) ? null : obj.Strasse === null ? null : obj.Strasse;
		result.Stand = (obj.Stand === undefined) ? null : obj.Stand === null ? null : obj.Stand;
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragStrassen) : string {
		let result = '{';
		result += '"Ort" : ' + ((obj.Ort === null) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		result += '"RegSchl" : ' + ((obj.RegSchl === null) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		result += '"Strasse" : ' + ((obj.Strasse === null) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		result += '"Stand" : ' + ((obj.Stand === null) ? 'null' : JSON.stringify(obj.Stand)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragStrassen>) : string {
		let result = '{';
		if (obj.Ort !== undefined) {
			result += '"Ort" : ' + ((obj.Ort === null) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		}
		if (obj.RegSchl !== undefined) {
			result += '"RegSchl" : ' + ((obj.RegSchl === null) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		}
		if (obj.Strasse !== undefined) {
			result += '"Strasse" : ' + ((obj.Strasse === null) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		}
		if (obj.Stand !== undefined) {
			result += '"Stand" : ' + ((obj.Stand === null) ? 'null' : JSON.stringify(obj.Stand)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_KatalogEintragStrassen(obj : unknown) : KatalogEintragStrassen {
	return obj as KatalogEintragStrassen;
}
