import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BenutzerConfigElement, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerConfigElement } from './BenutzerConfigElement';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BenutzerConfig extends JavaObject {

	/**
	 * Die Konfiguration, die dem Benutzer zugeordnet ist. 
	 */
	public user : Vector<BenutzerConfigElement> = new Vector();

	/**
	 * Die globale Konfiguration, die auch für den Benutzer gilt. 
	 */
	public global : Vector<BenutzerConfigElement> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerConfig'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerConfig {
		const obj = JSON.parse(json);
		const result = new BenutzerConfig();
		if (!!obj.user) {
			for (let elem of obj.user) {
				result.user?.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.global) {
			for (let elem of obj.global) {
				result.global?.add(BenutzerConfigElement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerConfig) : string {
		let result = '{';
		if (!obj.user) {
			result += '"user" : []';
		} else {
			result += '"user" : [ ';
			for (let i : number = 0; i < obj.user.size(); i++) {
				let elem = obj.user.get(i);
				result += BenutzerConfigElement.transpilerToJSON(elem);
				if (i < obj.user.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.global) {
			result += '"global" : []';
		} else {
			result += '"global" : [ ';
			for (let i : number = 0; i < obj.global.size(); i++) {
				let elem = obj.global.get(i);
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

	public static transpilerToJSONPatch(obj : Partial<BenutzerConfig>) : string {
		let result = '{';
		if (typeof obj.user !== "undefined") {
			if (!obj.user) {
				result += '"user" : []';
			} else {
				result += '"user" : [ ';
				for (let i : number = 0; i < obj.user.size(); i++) {
					let elem = obj.user.get(i);
					result += BenutzerConfigElement.transpilerToJSON(elem);
					if (i < obj.user.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.global !== "undefined") {
			if (!obj.global) {
				result += '"global" : []';
			} else {
				result += '"global" : [ ';
				for (let i : number = 0; i < obj.global.size(); i++) {
					let elem = obj.global.get(i);
					result += BenutzerConfigElement.transpilerToJSON(elem);
					if (i < obj.global.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerConfig(obj : unknown) : BenutzerConfig {
	return obj as BenutzerConfig;
}
