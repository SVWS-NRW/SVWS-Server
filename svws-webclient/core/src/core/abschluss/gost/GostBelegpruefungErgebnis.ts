import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBelegpruefungErgebnisFehler } from '../../../core/abschluss/gost/GostBelegpruefungErgebnisFehler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostBelegpruefungErgebnis extends JavaObject {

	/**
	 * gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde
	 */
	public erfolgreich : boolean = false;

	/**
	 * eine Liste der Belegungsfehler und Hinweise zur Belegung
	 */
	public fehlercodes : List<GostBelegpruefungErgebnisFehler> = new ArrayList<GostBelegpruefungErgebnisFehler>();

	/**
	 * Ein Log, der den Ablauf der Belegprüfung verdeutlicht
	 */
	public log : List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis'].includes(name);
	}

	public static class = new Class<GostBelegpruefungErgebnis>('de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis');

	public static transpilerFromJSON(json : string): GostBelegpruefungErgebnis {
		const obj = JSON.parse(json) as Partial<GostBelegpruefungErgebnis>;
		const result = new GostBelegpruefungErgebnis();
		if (obj.erfolgreich === undefined)
			throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if (obj.fehlercodes !== undefined) {
			for (const elem of obj.fehlercodes) {
				result.fehlercodes.add(GostBelegpruefungErgebnisFehler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.log !== undefined) {
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungErgebnis) : string {
		let result = '{';
		result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		result += '"fehlercodes" : [ ';
		for (let i = 0; i < obj.fehlercodes.size(); i++) {
			const elem = obj.fehlercodes.get(i);
			result += GostBelegpruefungErgebnisFehler.transpilerToJSON(elem);
			if (i < obj.fehlercodes.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"log" : [ ';
		for (let i = 0; i < obj.log.size(); i++) {
			const elem = obj.log.get(i);
			result += '"' + elem + '"';
			if (i < obj.log.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungErgebnis>) : string {
		let result = '{';
		if (obj.erfolgreich !== undefined) {
			result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		}
		if (obj.fehlercodes !== undefined) {
			result += '"fehlercodes" : [ ';
			for (let i = 0; i < obj.fehlercodes.size(); i++) {
				const elem = obj.fehlercodes.get(i);
				result += GostBelegpruefungErgebnisFehler.transpilerToJSON(elem);
				if (i < obj.fehlercodes.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.log !== undefined) {
			result += '"log" : [ ';
			for (let i = 0; i < obj.log.size(); i++) {
				const elem = obj.log.get(i);
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

}

export function cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungErgebnis(obj : unknown) : GostBelegpruefungErgebnis {
	return obj as GostBelegpruefungErgebnis;
}
