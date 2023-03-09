import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchuelerLernabschnittNachpruefung, cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittNachpruefung } from '../../../core/data/schueler/SchuelerLernabschnittNachpruefung';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerLernabschnittNachpruefungsdaten extends JavaObject {

	/**
	 * Die Kürzel der möglichen Nachprüfungsfächer. 
	 */
	public moegliche : Vector<string> = new Vector();

	/**
	 * Die angesetzten bzw. durchgeführten Nachprüfungen 
	 */
	public pruefungen : Vector<SchuelerLernabschnittNachpruefung> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLernabschnittNachpruefungsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerLernabschnittNachpruefungsdaten();
		if (!!obj.moegliche) {
			for (let elem of obj.moegliche) {
				result.moegliche?.add(elem);
			}
		}
		if (!!obj.pruefungen) {
			for (let elem of obj.pruefungen) {
				result.pruefungen?.add(SchuelerLernabschnittNachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittNachpruefungsdaten) : string {
		let result = '{';
		if (!obj.moegliche) {
			result += '"moegliche" : []';
		} else {
			result += '"moegliche" : [ ';
			for (let i : number = 0; i < obj.moegliche.size(); i++) {
				let elem = obj.moegliche.get(i);
				result += '"' + elem + '"';
				if (i < obj.moegliche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.pruefungen) {
			result += '"pruefungen" : []';
		} else {
			result += '"pruefungen" : [ ';
			for (let i : number = 0; i < obj.pruefungen.size(); i++) {
				let elem = obj.pruefungen.get(i);
				result += SchuelerLernabschnittNachpruefung.transpilerToJSON(elem);
				if (i < obj.pruefungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittNachpruefungsdaten>) : string {
		let result = '{';
		if (typeof obj.moegliche !== "undefined") {
			if (!obj.moegliche) {
				result += '"moegliche" : []';
			} else {
				result += '"moegliche" : [ ';
				for (let i : number = 0; i < obj.moegliche.size(); i++) {
					let elem = obj.moegliche.get(i);
					result += '"' + elem + '"';
					if (i < obj.moegliche.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.pruefungen !== "undefined") {
			if (!obj.pruefungen) {
				result += '"pruefungen" : []';
			} else {
				result += '"pruefungen" : [ ';
				for (let i : number = 0; i < obj.pruefungen.size(); i++) {
					let elem = obj.pruefungen.get(i);
					result += SchuelerLernabschnittNachpruefung.transpilerToJSON(elem);
					if (i < obj.pruefungen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittNachpruefungsdaten(obj : unknown) : SchuelerLernabschnittNachpruefungsdaten {
	return obj as SchuelerLernabschnittNachpruefungsdaten;
}
