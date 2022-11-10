import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostBelegpruefungErgebnisFehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungErgebnisFehler } from '../../../core/abschluss/gost/GostBelegpruefungErgebnisFehler';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostBelegpruefungErgebnis extends JavaObject {

	public erfolgreich : boolean = false;

	public fehlercodes : Vector<GostBelegpruefungErgebnisFehler> = new Vector();

	public log : List<String> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungErgebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungErgebnis {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungErgebnis();
		if (typeof obj.erfolgreich === "undefined")
			 throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if (!!obj.fehlercodes) {
			for (let elem of obj.fehlercodes) {
				result.fehlercodes?.add(GostBelegpruefungErgebnisFehler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.log) {
			for (let elem of obj.log) {
				result.log?.add(String(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungErgebnis) : string {
		let result = '{';
		result += '"erfolgreich" : ' + obj.erfolgreich + ',';
		if (!obj.fehlercodes) {
			result += '"fehlercodes" : []';
		} else {
			result += '"fehlercodes" : [ ';
			for (let i : number = 0; i < obj.fehlercodes.size(); i++) {
				let elem = obj.fehlercodes.get(i);
				result += GostBelegpruefungErgebnisFehler.transpilerToJSON(elem);
				if (i < obj.fehlercodes.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.log) {
			result += '"log" : []';
		} else {
			result += '"log" : [ ';
			for (let i : number = 0; i < obj.log.size(); i++) {
				let elem = obj.log.get(i);
				result += '"' + elem + '"';
				if (i < obj.log.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungErgebnis>) : string {
		let result = '{';
		if (typeof obj.erfolgreich !== "undefined") {
			result += '"erfolgreich" : ' + obj.erfolgreich + ',';
		}
		if (typeof obj.fehlercodes !== "undefined") {
			if (!obj.fehlercodes) {
				result += '"fehlercodes" : []';
			} else {
				result += '"fehlercodes" : [ ';
				for (let i : number = 0; i < obj.fehlercodes.size(); i++) {
					let elem = obj.fehlercodes.get(i);
					result += GostBelegpruefungErgebnisFehler.transpilerToJSON(elem);
					if (i < obj.fehlercodes.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.log !== "undefined") {
			if (!obj.log) {
				result += '"log" : []';
			} else {
				result += '"log" : [ ';
				for (let i : number = 0; i < obj.log.size(); i++) {
					let elem = obj.log.get(i);
					result += '"' + elem + '"';
					if (i < obj.log.size() - 1)
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

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungErgebnis(obj : unknown) : GostBelegpruefungErgebnis {
	return obj as GostBelegpruefungErgebnis;
}
