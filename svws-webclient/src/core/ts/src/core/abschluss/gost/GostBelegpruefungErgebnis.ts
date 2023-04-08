import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBelegpruefungErgebnisFehler } from '../../../core/abschluss/gost/GostBelegpruefungErgebnisFehler';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';

export class GostBelegpruefungErgebnis extends JavaObject {

	/**
	 * gibt an, ob die Belegprüfung erfolgreich abgeschlossen wurde
	 */
	public erfolgreich : boolean = false;

	/**
	 * eine Liste der Belegungsfehler und Hinweise zur Belegung
	 */
	public fehlercodes : ArrayList<GostBelegpruefungErgebnisFehler> = new ArrayList();

	/**
	 * Ein Log, der den Ablauf der Belegprüfung verdeutlicht
	 */
	public log : List<string> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungErgebnis {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungErgebnis();
		if (typeof obj.erfolgreich === "undefined")
			 throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if ((obj.fehlercodes !== undefined) && (obj.fehlercodes !== null)) {
			for (const elem of obj.fehlercodes) {
				result.fehlercodes?.add(GostBelegpruefungErgebnisFehler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.log !== undefined) && (obj.log !== null)) {
			for (const elem of obj.log) {
				result.log?.add(elem);
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
			for (let i = 0; i < obj.fehlercodes.size(); i++) {
				const elem = obj.fehlercodes.get(i);
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
				for (let i = 0; i < obj.fehlercodes.size(); i++) {
					const elem = obj.fehlercodes.get(i);
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
				for (let i = 0; i < obj.log.size(); i++) {
					const elem = obj.log.get(i);
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

export function cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungErgebnis(obj : unknown) : GostBelegpruefungErgebnis {
	return obj as GostBelegpruefungErgebnis;
}
