import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBelegpruefungErgebnis } from '../../../core/abschluss/gost/GostBelegpruefungErgebnis';

export class GostBelegpruefungsErgebnisse extends JavaObject {

	/**
	 * Der Schüler, für welchen die Belegprüfung durchgeführt wurde
	 */
	public schueler : Schueler = new Schueler();

	/**
	 * Die zugehörigen Belegprüfungsergebnisse
	 */
	public ergebnis : GostBelegpruefungErgebnis = new GostBelegpruefungErgebnis();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBelegpruefungsErgebnisse {
		const obj = JSON.parse(json);
		const result = new GostBelegpruefungsErgebnisse();
		if (typeof obj.schueler === "undefined")
			 throw new Error('invalid json format, missing attribute schueler');
		result.schueler = Schueler.transpilerFromJSON(JSON.stringify(obj.schueler));
		if (typeof obj.ergebnis === "undefined")
			 throw new Error('invalid json format, missing attribute ergebnis');
		result.ergebnis = GostBelegpruefungErgebnis.transpilerFromJSON(JSON.stringify(obj.ergebnis));
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungsErgebnisse) : string {
		let result = '{';
		result += '"schueler" : ' + Schueler.transpilerToJSON(obj.schueler) + ',';
		result += '"ergebnis" : ' + GostBelegpruefungErgebnis.transpilerToJSON(obj.ergebnis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungsErgebnisse>) : string {
		let result = '{';
		if (typeof obj.schueler !== "undefined") {
			result += '"schueler" : ' + Schueler.transpilerToJSON(obj.schueler) + ',';
		}
		if (typeof obj.ergebnis !== "undefined") {
			result += '"ergebnis" : ' + GostBelegpruefungErgebnis.transpilerToJSON(obj.ergebnis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBelegpruefungsErgebnisse(obj : unknown) : GostBelegpruefungsErgebnisse {
	return obj as GostBelegpruefungsErgebnisse;
}
