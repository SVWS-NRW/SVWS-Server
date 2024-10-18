import { JavaObject } from '../../../java/lang/JavaObject';
import { BenutzerConfigElement } from '../../../core/data/benutzer/BenutzerConfigElement';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BenutzerConfig extends JavaObject {

	/**
	 * Die Konfiguration, die dem Benutzer zugeordnet ist.
	 */
	public user : List<BenutzerConfigElement> = new ArrayList<BenutzerConfigElement>();

	/**
	 * Die globale Konfiguration, die auch für den Benutzer gilt.
	 */
	public global : List<BenutzerConfigElement> = new ArrayList<BenutzerConfigElement>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerConfig';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerConfig'].includes(name);
	}

	public static class = new Class<BenutzerConfig>('de.svws_nrw.core.data.benutzer.BenutzerConfig');

	public static transpilerFromJSON(json : string): BenutzerConfig {
		const obj = JSON.parse(json) as Partial<BenutzerConfig>;
		const result = new BenutzerConfig();
		if (obj.user !== undefined) {
			for (const elem of obj.user) {
				result.user.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.global !== undefined) {
			for (const elem of obj.global) {
				result.global.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerConfig) : string {
		let result = '{';
		result += '"user" : [ ';
		for (let i = 0; i < obj.user.size(); i++) {
			const elem = obj.user.get(i);
			result += BenutzerConfigElement.transpilerToJSON(elem);
			if (i < obj.user.size() - 1)
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

	public static transpilerToJSONPatch(obj : Partial<BenutzerConfig>) : string {
		let result = '{';
		if (obj.user !== undefined) {
			result += '"user" : [ ';
			for (let i = 0; i < obj.user.size(); i++) {
				const elem = obj.user.get(i);
				result += BenutzerConfigElement.transpilerToJSON(elem);
				if (i < obj.user.size() - 1)
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

export function cast_de_svws_nrw_core_data_benutzer_BenutzerConfig(obj : unknown) : BenutzerConfig {
	return obj as BenutzerConfig;
}
