import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { SchuelerLernabschnittNachpruefung, cast_de_svws_nrw_asd_data_schueler_SchuelerLernabschnittNachpruefung } from '../../../asd/data/schueler/SchuelerLernabschnittNachpruefung';
import { Class } from '../../../java/lang/Class';

export class SchuelerLernabschnittNachpruefungsdaten extends JavaObject {

	/**
	 * Die Kürzel der möglichen Nachprüfungsfächer.
	 */
	public moegliche : List<string> = new ArrayList<string>();

	/**
	 * Die angesetzten bzw. durchgeführten Nachprüfungen
	 */
	public pruefungen : List<SchuelerLernabschnittNachpruefung> = new ArrayList<SchuelerLernabschnittNachpruefung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten'].includes(name);
	}

	public static class = new Class<SchuelerLernabschnittNachpruefungsdaten>('de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten');

	public static transpilerFromJSON(json : string): SchuelerLernabschnittNachpruefungsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittNachpruefungsdaten>;
		const result = new SchuelerLernabschnittNachpruefungsdaten();
		if (obj.moegliche !== undefined) {
			for (const elem of obj.moegliche) {
				result.moegliche.add(elem);
			}
		}
		if (obj.pruefungen !== undefined) {
			for (const elem of obj.pruefungen) {
				result.pruefungen.add(SchuelerLernabschnittNachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittNachpruefungsdaten) : string {
		let result = '{';
		result += '"moegliche" : [ ';
		for (let i = 0; i < obj.moegliche.size(); i++) {
			const elem = obj.moegliche.get(i);
			result += '"' + elem + '"';
			if (i < obj.moegliche.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pruefungen" : [ ';
		for (let i = 0; i < obj.pruefungen.size(); i++) {
			const elem = obj.pruefungen.get(i);
			result += SchuelerLernabschnittNachpruefung.transpilerToJSON(elem);
			if (i < obj.pruefungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittNachpruefungsdaten>) : string {
		let result = '{';
		if (obj.moegliche !== undefined) {
			result += '"moegliche" : [ ';
			for (let i = 0; i < obj.moegliche.size(); i++) {
				const elem = obj.moegliche.get(i);
				result += '"' + elem + '"';
				if (i < obj.moegliche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pruefungen !== undefined) {
			result += '"pruefungen" : [ ';
			for (let i = 0; i < obj.pruefungen.size(); i++) {
				const elem = obj.pruefungen.get(i);
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

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerLernabschnittNachpruefungsdaten(obj : unknown) : SchuelerLernabschnittNachpruefungsdaten {
	return obj as SchuelerLernabschnittNachpruefungsdaten;
}
