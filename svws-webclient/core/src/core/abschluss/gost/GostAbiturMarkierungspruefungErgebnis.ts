import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostAbiturMarkierungspruefungErgebnis extends JavaObject {

	/**
	 * gibt an, ob die Prüfung erfolgreich war
	 */
	public erfolgreich : boolean = false;

	/**
	 * Ein Log, der den Ablauf der Markierungsprüfung verdeutlicht
	 */
	public log : List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefungErgebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefungErgebnis'].includes(name);
	}

	public static class = new Class<GostAbiturMarkierungspruefungErgebnis>('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefungErgebnis');

	public static transpilerFromJSON(json : string): GostAbiturMarkierungspruefungErgebnis {
		const obj = JSON.parse(json) as Partial<GostAbiturMarkierungspruefungErgebnis>;
		const result = new GostAbiturMarkierungspruefungErgebnis();
		if (obj.erfolgreich === undefined)
			throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if (obj.log !== undefined) {
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostAbiturMarkierungspruefungErgebnis) : string {
		let result = '{';
		result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
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

	public static transpilerToJSONPatch(obj : Partial<GostAbiturMarkierungspruefungErgebnis>) : string {
		let result = '{';
		if (obj.erfolgreich !== undefined) {
			result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
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

export function cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungspruefungErgebnis(obj : unknown) : GostAbiturMarkierungspruefungErgebnis {
	return obj as GostAbiturMarkierungspruefungErgebnis;
}
