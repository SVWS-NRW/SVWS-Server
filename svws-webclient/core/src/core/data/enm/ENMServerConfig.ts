import { JavaObject } from '../../../java/lang/JavaObject';
import { BenutzerConfigElement } from '../../../core/data/benutzer/BenutzerConfigElement';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ENMServerConfig extends JavaObject {

	/**
	 * Die Konfiguration, die dem Benutzer zugeordnet ist.
	 */
	public server : List<BenutzerConfigElement> = new ArrayList<BenutzerConfigElement>();

	/**
	 * Die globale Konfiguration, die auch für den Benutzer gilt.
	 */
	public global : List<BenutzerConfigElement> = new ArrayList<BenutzerConfigElement>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMServerConfig';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMServerConfig'].includes(name);
	}

	public static class = new Class<ENMServerConfig>('de.svws_nrw.core.data.enm.ENMServerConfig');

	public static transpilerFromJSON(json : string): ENMServerConfig {
		const obj = JSON.parse(json) as Partial<ENMServerConfig>;
		const result = new ENMServerConfig();
		if (obj.server !== undefined) {
			for (const elem of obj.server) {
				result.server.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.global !== undefined) {
			for (const elem of obj.global) {
				result.global.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMServerConfig) : string {
		let result = '{';
		result += '"server" : [ ';
		for (let i = 0; i < obj.server.size(); i++) {
			const elem = obj.server.get(i);
			result += BenutzerConfigElement.transpilerToJSON(elem);
			if (i < obj.server.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"global" : [ ';
		for (let i = 0; i < obj.global.size(); i++) {
			const elem = obj.global.get(i);
			result += BenutzerConfigElement.transpilerToJSON(elem);
			if (i < obj.global.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMServerConfig>) : string {
		let result = '{';
		if (obj.server !== undefined) {
			result += '"server" : [ ';
			for (let i = 0; i < obj.server.size(); i++) {
				const elem = obj.server.get(i);
				result += BenutzerConfigElement.transpilerToJSON(elem);
				if (i < obj.server.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.global !== undefined) {
			result += '"global" : [ ';
			for (let i = 0; i < obj.global.size(); i++) {
				const elem = obj.global.get(i);
				result += BenutzerConfigElement.transpilerToJSON(elem);
				if (i < obj.global.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMServerConfig(obj : unknown) : ENMServerConfig {
	return obj as ENMServerConfig;
}
