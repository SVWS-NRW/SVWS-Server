import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../asd/data/schueler/Schueler';
import { GostBelegpruefungErgebnis } from '../../../core/abschluss/gost/GostBelegpruefungErgebnis';
import { Class } from '../../../java/lang/Class';

export class GostBelegpruefungsErgebnisse extends JavaObject {

	/**
	 * Der Schüler, für welchen die Belegprüfung durchgeführt wurde
	 */
	public schueler : Schueler = new Schueler();

	/**
	 * Gibt an, ob der Schüler aktuell Fachwahlen hat der nicht.
	 */
	public hatFachwahlen : boolean = false;

	/**
	 * Gibt an, ob und wann der Schüler zuletzt beraten wurde.
	 */
	public beratungsDatum : string | null = null;

	/**
	 * Gibt an, ob und wann für den Schüler zuletzt ein Import der Laufbahnplanungsdaten stattgefunden hat.
	 */
	public ruecklaufDatum : string | null = null;

	/**
	 * Die zugehörigen Belegprüfungsergebnisse
	 */
	public ergebnis : GostBelegpruefungErgebnis = new GostBelegpruefungErgebnis();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse'].includes(name);
	}

	public static class = new Class<GostBelegpruefungsErgebnisse>('de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse');

	public static transpilerFromJSON(json : string): GostBelegpruefungsErgebnisse {
		const obj = JSON.parse(json) as Partial<GostBelegpruefungsErgebnisse>;
		const result = new GostBelegpruefungsErgebnisse();
		if (obj.schueler === undefined)
			throw new Error('invalid json format, missing attribute schueler');
		result.schueler = Schueler.transpilerFromJSON(JSON.stringify(obj.schueler));
		if (obj.hatFachwahlen === undefined)
			throw new Error('invalid json format, missing attribute hatFachwahlen');
		result.hatFachwahlen = obj.hatFachwahlen;
		result.beratungsDatum = (obj.beratungsDatum === undefined) ? null : obj.beratungsDatum === null ? null : obj.beratungsDatum;
		result.ruecklaufDatum = (obj.ruecklaufDatum === undefined) ? null : obj.ruecklaufDatum === null ? null : obj.ruecklaufDatum;
		if (obj.ergebnis === undefined)
			throw new Error('invalid json format, missing attribute ergebnis');
		result.ergebnis = GostBelegpruefungErgebnis.transpilerFromJSON(JSON.stringify(obj.ergebnis));
		return result;
	}

	public static transpilerToJSON(obj : GostBelegpruefungsErgebnisse) : string {
		let result = '{';
		result += '"schueler" : ' + Schueler.transpilerToJSON(obj.schueler) + ',';
		result += '"hatFachwahlen" : ' + obj.hatFachwahlen.toString() + ',';
		result += '"beratungsDatum" : ' + ((obj.beratungsDatum === null) ? 'null' : JSON.stringify(obj.beratungsDatum)) + ',';
		result += '"ruecklaufDatum" : ' + ((obj.ruecklaufDatum === null) ? 'null' : JSON.stringify(obj.ruecklaufDatum)) + ',';
		result += '"ergebnis" : ' + GostBelegpruefungErgebnis.transpilerToJSON(obj.ergebnis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBelegpruefungsErgebnisse>) : string {
		let result = '{';
		if (obj.schueler !== undefined) {
			result += '"schueler" : ' + Schueler.transpilerToJSON(obj.schueler) + ',';
		}
		if (obj.hatFachwahlen !== undefined) {
			result += '"hatFachwahlen" : ' + obj.hatFachwahlen.toString() + ',';
		}
		if (obj.beratungsDatum !== undefined) {
			result += '"beratungsDatum" : ' + ((obj.beratungsDatum === null) ? 'null' : JSON.stringify(obj.beratungsDatum)) + ',';
		}
		if (obj.ruecklaufDatum !== undefined) {
			result += '"ruecklaufDatum" : ' + ((obj.ruecklaufDatum === null) ? 'null' : JSON.stringify(obj.ruecklaufDatum)) + ',';
		}
		if (obj.ergebnis !== undefined) {
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
