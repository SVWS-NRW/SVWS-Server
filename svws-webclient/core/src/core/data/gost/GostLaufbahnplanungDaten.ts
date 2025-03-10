import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBeratungslehrer } from '../../../core/data/gost/GostBeratungslehrer';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostJahrgangFachkombination } from '../../../core/data/gost/GostJahrgangFachkombination';
import { GostLaufbahnplanungDatenSchueler } from '../../../core/data/gost/GostLaufbahnplanungDatenSchueler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostLaufbahnplanungDaten extends JavaObject {

	/**
	 * Die Revision des LP-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1)
	 */
	public lpRevision : number = 2;

	/**
	 * Der Bezeichner der Schulform
	 */
	public schulform : string = "GY";

	/**
	 * Die Schulnummer der Schule, welcher die Laufbahndaten zugeordnet sind.
	 */
	public schulNr : number = 0;

	/**
	 * Der erste Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung1 : string = "";

	/**
	 * Der zweite Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung2 : string = "";

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung3 : string = "";

	/**
	 * Anmerkungen zu diesen Daten
	 */
	public anmerkungen : string = "";

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 */
	public abiturjahr : number = -1;

	/**
	 * Die aktuelle Jahrgangsstufe, welche dem Abiturjahrgang zugeordnet ist.
	 */
	public jahrgang : string | null = null;

	/**
	 * Der derzeitige Beratungstext, welcher auf einem Ausdruck eines Schülerlaufbahnbogens für die
	 *  gymnasiale Oberstufe gedruckt wird.
	 */
	public textBeratungsbogen : string | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird.
	 */
	public hatZusatzkursGE : boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Geschichte beginnt.
	 */
	public beginnZusatzkursGE : string | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird.
	 */
	public hatZusatzkursSW : boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Sozialwissenschaften beginnt.
	 */
	public beginnZusatzkursSW : string | null = null;

	/**
	 * Die Liste der Beratungslehrer für diesen Jahrgang
	 */
	public readonly beratungslehrer : List<GostBeratungslehrer> = new ArrayList<GostBeratungslehrer>();

	/**
	 * Die Liste der Fächer der gymnasialen Oberstufe für diesen Jahrgang
	 */
	public readonly faecher : List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Die Liste der notwendigen und der unzulässigen Kursart-spezifischen Fach-Kombinationen für diesen Jahrgang
	 */
	public readonly fachkombinationen : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();

	/**
	 * Die Liste der Schüler mit ihren Laufbahnplanungsdaten.
	 */
	public readonly schueler : List<GostLaufbahnplanungDatenSchueler> = new ArrayList<GostLaufbahnplanungDatenSchueler>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten'].includes(name);
	}

	public static class = new Class<GostLaufbahnplanungDaten>('de.svws_nrw.core.data.gost.GostLaufbahnplanungDaten');

	public static transpilerFromJSON(json : string): GostLaufbahnplanungDaten {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungDaten>;
		const result = new GostLaufbahnplanungDaten();
		if (obj.lpRevision === undefined)
			throw new Error('invalid json format, missing attribute lpRevision');
		result.lpRevision = obj.lpRevision;
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		if (obj.schulNr === undefined)
			throw new Error('invalid json format, missing attribute schulNr');
		result.schulNr = obj.schulNr;
		if (obj.schulBezeichnung1 === undefined)
			throw new Error('invalid json format, missing attribute schulBezeichnung1');
		result.schulBezeichnung1 = obj.schulBezeichnung1;
		if (obj.schulBezeichnung2 === undefined)
			throw new Error('invalid json format, missing attribute schulBezeichnung2');
		result.schulBezeichnung2 = obj.schulBezeichnung2;
		if (obj.schulBezeichnung3 === undefined)
			throw new Error('invalid json format, missing attribute schulBezeichnung3');
		result.schulBezeichnung3 = obj.schulBezeichnung3;
		if (obj.anmerkungen === undefined)
			throw new Error('invalid json format, missing attribute anmerkungen');
		result.anmerkungen = obj.anmerkungen;
		if (obj.abiturjahr === undefined)
			throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.textBeratungsbogen = (obj.textBeratungsbogen === undefined) ? null : obj.textBeratungsbogen === null ? null : obj.textBeratungsbogen;
		if (obj.hatZusatzkursGE === undefined)
			throw new Error('invalid json format, missing attribute hatZusatzkursGE');
		result.hatZusatzkursGE = obj.hatZusatzkursGE;
		result.beginnZusatzkursGE = (obj.beginnZusatzkursGE === undefined) ? null : obj.beginnZusatzkursGE === null ? null : obj.beginnZusatzkursGE;
		if (obj.hatZusatzkursSW === undefined)
			throw new Error('invalid json format, missing attribute hatZusatzkursSW');
		result.hatZusatzkursSW = obj.hatZusatzkursSW;
		result.beginnZusatzkursSW = (obj.beginnZusatzkursSW === undefined) ? null : obj.beginnZusatzkursSW === null ? null : obj.beginnZusatzkursSW;
		if (obj.beratungslehrer !== undefined) {
			for (const elem of obj.beratungslehrer) {
				result.beratungslehrer.add(GostBeratungslehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.fachkombinationen !== undefined) {
			for (const elem of obj.fachkombinationen) {
				result.fachkombinationen.add(GostJahrgangFachkombination.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(GostLaufbahnplanungDatenSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungDaten) : string {
		let result = '{';
		result += '"lpRevision" : ' + obj.lpRevision.toString() + ',';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		result += '"schulBezeichnung1" : ' + JSON.stringify(obj.schulBezeichnung1) + ',';
		result += '"schulBezeichnung2" : ' + JSON.stringify(obj.schulBezeichnung2) + ',';
		result += '"schulBezeichnung3" : ' + JSON.stringify(obj.schulBezeichnung3) + ',';
		result += '"anmerkungen" : ' + JSON.stringify(obj.anmerkungen) + ',';
		result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"textBeratungsbogen" : ' + ((obj.textBeratungsbogen === null) ? 'null' : JSON.stringify(obj.textBeratungsbogen)) + ',';
		result += '"hatZusatzkursGE" : ' + obj.hatZusatzkursGE.toString() + ',';
		result += '"beginnZusatzkursGE" : ' + ((obj.beginnZusatzkursGE === null) ? 'null' : JSON.stringify(obj.beginnZusatzkursGE)) + ',';
		result += '"hatZusatzkursSW" : ' + obj.hatZusatzkursSW.toString() + ',';
		result += '"beginnZusatzkursSW" : ' + ((obj.beginnZusatzkursSW === null) ? 'null' : JSON.stringify(obj.beginnZusatzkursSW)) + ',';
		result += '"beratungslehrer" : [ ';
		for (let i = 0; i < obj.beratungslehrer.size(); i++) {
			const elem = obj.beratungslehrer.get(i);
			result += GostBeratungslehrer.transpilerToJSON(elem);
			if (i < obj.beratungslehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GostFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachkombinationen" : [ ';
		for (let i = 0; i < obj.fachkombinationen.size(); i++) {
			const elem = obj.fachkombinationen.get(i);
			result += GostJahrgangFachkombination.transpilerToJSON(elem);
			if (i < obj.fachkombinationen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += GostLaufbahnplanungDatenSchueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungDaten>) : string {
		let result = '{';
		if (obj.lpRevision !== undefined) {
			result += '"lpRevision" : ' + obj.lpRevision.toString() + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.schulNr !== undefined) {
			result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		}
		if (obj.schulBezeichnung1 !== undefined) {
			result += '"schulBezeichnung1" : ' + JSON.stringify(obj.schulBezeichnung1) + ',';
		}
		if (obj.schulBezeichnung2 !== undefined) {
			result += '"schulBezeichnung2" : ' + JSON.stringify(obj.schulBezeichnung2) + ',';
		}
		if (obj.schulBezeichnung3 !== undefined) {
			result += '"schulBezeichnung3" : ' + JSON.stringify(obj.schulBezeichnung3) + ',';
		}
		if (obj.anmerkungen !== undefined) {
			result += '"anmerkungen" : ' + JSON.stringify(obj.anmerkungen) + ',';
		}
		if (obj.abiturjahr !== undefined) {
			result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.textBeratungsbogen !== undefined) {
			result += '"textBeratungsbogen" : ' + ((obj.textBeratungsbogen === null) ? 'null' : JSON.stringify(obj.textBeratungsbogen)) + ',';
		}
		if (obj.hatZusatzkursGE !== undefined) {
			result += '"hatZusatzkursGE" : ' + obj.hatZusatzkursGE.toString() + ',';
		}
		if (obj.beginnZusatzkursGE !== undefined) {
			result += '"beginnZusatzkursGE" : ' + ((obj.beginnZusatzkursGE === null) ? 'null' : JSON.stringify(obj.beginnZusatzkursGE)) + ',';
		}
		if (obj.hatZusatzkursSW !== undefined) {
			result += '"hatZusatzkursSW" : ' + obj.hatZusatzkursSW.toString() + ',';
		}
		if (obj.beginnZusatzkursSW !== undefined) {
			result += '"beginnZusatzkursSW" : ' + ((obj.beginnZusatzkursSW === null) ? 'null' : JSON.stringify(obj.beginnZusatzkursSW)) + ',';
		}
		if (obj.beratungslehrer !== undefined) {
			result += '"beratungslehrer" : [ ';
			for (let i = 0; i < obj.beratungslehrer.size(); i++) {
				const elem = obj.beratungslehrer.get(i);
				result += GostBeratungslehrer.transpilerToJSON(elem);
				if (i < obj.beratungslehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachkombinationen !== undefined) {
			result += '"fachkombinationen" : [ ';
			for (let i = 0; i < obj.fachkombinationen.size(); i++) {
				const elem = obj.fachkombinationen.get(i);
				result += GostJahrgangFachkombination.transpilerToJSON(elem);
				if (i < obj.fachkombinationen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += GostLaufbahnplanungDatenSchueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostLaufbahnplanungDaten(obj : unknown) : GostLaufbahnplanungDaten {
	return obj as GostLaufbahnplanungDaten;
}
