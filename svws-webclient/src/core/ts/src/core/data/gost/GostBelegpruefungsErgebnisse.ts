import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBelegpruefungErgebnis } from '../../../core/abschluss/gost/GostBelegpruefungErgebnis';
import { List } from '../../../java/util/List';

export class GostBelegpruefungsErgebnisse extends JavaObject {

	/**
	 * Die Art der durchgeführten Belegprüfung.
	 */
	public kuerzel : string = GostBelegpruefungsArt.GESAMT.kuerzel;

	/**
	 * Die Liste der Schüler, für welche die Belegprüfung durchgeführt wurde
	 */
	public schueler : List<Schueler> = new ArrayList();

	/**
	 * Die Liste der Belegprüfungsergebnisse, die Anzahl und Reihenfolge muss der Anzahl und Reihenfolge der Schülerlist entsprechen.
	 */
	public ergebnisse : List<GostBelegpruefungErgebnis> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungsErgebnisse {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungsErgebnisse();
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.ergebnisse !== undefined) && (obj.ergebnisse !== null)) {
			for (const elem of obj.ergebnisse) {
				result.ergebnisse?.add(GostBelegpruefungErgebnis.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungsErgebnisse) : string {
		let result = '{';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.ergebnisse) {
			result += '"ergebnisse" : []';
		} else {
			result += '"ergebnisse" : [ ';
			for (let i = 0; i < obj.ergebnisse.size(); i++) {
				const elem = obj.ergebnisse.get(i);
				result += GostBelegpruefungErgebnis.transpilerToJSON(elem);
				if (i < obj.ergebnisse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungsErgebnisse>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += Schueler.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.ergebnisse !== "undefined") {
			if (!obj.ergebnisse) {
				result += '"ergebnisse" : []';
			} else {
				result += '"ergebnisse" : [ ';
				for (let i = 0; i < obj.ergebnisse.size(); i++) {
					const elem = obj.ergebnisse.get(i);
					result += GostBelegpruefungErgebnis.transpilerToJSON(elem);
					if (i < obj.ergebnisse.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_GostBelegpruefungsErgebnisse(obj : unknown) : GostBelegpruefungsErgebnisse {
	return obj as GostBelegpruefungsErgebnisse;
}
