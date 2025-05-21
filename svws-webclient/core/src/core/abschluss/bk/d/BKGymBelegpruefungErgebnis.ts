import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { BKGymBelegpruefungErgebnisFehler } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungErgebnisFehler';

export class BKGymBelegpruefungErgebnis extends JavaObject {

	/**
	 * gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde
	 */
	public erfolgreich : boolean = false;

	/**
	 * eine Liste der Belegungsfehler und Hinweise zur Belegung
	 */
	public fehlercodes : List<BKGymBelegpruefungErgebnisFehler> = new ArrayList<BKGymBelegpruefungErgebnisFehler>();

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
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnis'].includes(name);
	}

	public static class = new Class<BKGymBelegpruefungErgebnis>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefungErgebnis');

	public static transpilerFromJSON(json : string): BKGymBelegpruefungErgebnis {
		const obj = JSON.parse(json) as Partial<BKGymBelegpruefungErgebnis>;
		const result = new BKGymBelegpruefungErgebnis();
		if (obj.erfolgreich === undefined)
			throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if (obj.fehlercodes !== undefined) {
			for (const elem of obj.fehlercodes) {
				result.fehlercodes.add(BKGymBelegpruefungErgebnisFehler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.log !== undefined) {
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKGymBelegpruefungErgebnis) : string {
		let result = '{';
		result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		result += '"fehlercodes" : [ ';
		for (let i = 0; i < obj.fehlercodes.size(); i++) {
			const elem = obj.fehlercodes.get(i);
			result += BKGymBelegpruefungErgebnisFehler.transpilerToJSON(elem);
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

	public static transpilerToJSONPatch(obj : Partial<BKGymBelegpruefungErgebnis>) : string {
		let result = '{';
		if (obj.erfolgreich !== undefined) {
			result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		}
		if (obj.fehlercodes !== undefined) {
			result += '"fehlercodes" : [ ';
			for (let i = 0; i < obj.fehlercodes.size(); i++) {
				const elem = obj.fehlercodes.get(i);
				result += BKGymBelegpruefungErgebnisFehler.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefungErgebnis(obj : unknown) : BKGymBelegpruefungErgebnis {
	return obj as BKGymBelegpruefungErgebnis;
}
