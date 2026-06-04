import { JavaObject } from '../../../../../java/lang/JavaObject';
import { GostLaufbahnplanungExportV1Schueler } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Schueler';
import { GostLaufbahnplanungExportV1Fach } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fach';
import { ArrayList } from '../../../../../java/util/ArrayList';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostLaufbahnplanungExportV1Beratungslehrer } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Beratungslehrer';
import { GostLaufbahnplanungExportV1Fachkombination } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fachkombination';

export class GostLaufbahnplanungExportV1 extends JavaObject {

	/**
	 * Die Revision des LP-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1)
	 */
	public lpRevision: number = 1;

	/**
	 * Die Schulnummer der Schule, welcher die Laufbahndaten zugeordnet sind.
	 */
	public schulNr: number = 0;

	/**
	 * Der erste Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung1: string = "";

	/**
	 * Der zweite Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung2: string = "";

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule
	 */
	public schulBezeichnung3: string = "";

	/**
	 * Anmerkungen zu diesen Daten
	 */
	public anmerkungen: string = "";

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 */
	public abiturjahr: number = -1;

	/**
	 * Die aktuelle Jahrgangsstufe, welche dem Abiturjahrgang zugeordnet ist.
	 */
	public jahrgang: string | null = null;

	/**
	 * Der derzeitige Beratungstext, welcher auf einem Ausdruck eines Schülerlaufbahnbogens für die
	 *  gymnasiale Oberstufe gedruckt wird.
	 */
	public textBeratungsbogen: string | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird.
	 */
	public hatZusatzkursGE: boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Geschichte beginnt.
	 */
	public beginnZusatzkursGE: string | null = null;

	/**
	 * Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird.
	 */
	public hatZusatzkursSW: boolean = true;

	/**
	 * Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Sozialwissenschaften beginnt.
	 */
	public beginnZusatzkursSW: string | null = null;

	/**
	 * Die Liste der Beratungslehrer für diesen Jahrgang
	 */
	public readonly beratungslehrer: List<GostLaufbahnplanungExportV1Beratungslehrer> = new ArrayList<GostLaufbahnplanungExportV1Beratungslehrer>();

	/**
	 * Die Liste der Fächer der gymnasialen Oberstufe für diesen Jahrgang
	 */
	public readonly faecher: List<GostLaufbahnplanungExportV1Fach> = new ArrayList<GostLaufbahnplanungExportV1Fach>();

	/**
	 * Die Liste der notwendigen und der unzulässigen Kursart-spezifischen Fach-Kombinationen für diesen Jahrgang
	 */
	public readonly fachkombinationen: List<GostLaufbahnplanungExportV1Fachkombination> = new ArrayList<GostLaufbahnplanungExportV1Fachkombination>();

	/**
	 * Die Liste der Schüler mit ihren Laufbahnplanungsdaten.
	 */
	public readonly schueler: List<GostLaufbahnplanungExportV1Schueler> = new ArrayList<GostLaufbahnplanungExportV1Schueler>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV1>('de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV1 {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV1>;
		const result = new GostLaufbahnplanungExportV1();
		if (obj.lpRevision === undefined)
			throw new Error('invalid json format, missing attribute lpRevision');
		result.lpRevision = obj.lpRevision;
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
				result.beratungslehrer.add(GostLaufbahnplanungExportV1Beratungslehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GostLaufbahnplanungExportV1Fach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.fachkombinationen !== undefined) {
			for (const elem of obj.fachkombinationen) {
				result.fachkombinationen.add(GostLaufbahnplanungExportV1Fachkombination.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(GostLaufbahnplanungExportV1Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV1): string {
		let result = '{';
		result += '"lpRevision" : ' + obj.lpRevision.toString() + ',';
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
			result += GostLaufbahnplanungExportV1Beratungslehrer.transpilerToJSON(elem);
			if (i < obj.beratungslehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GostLaufbahnplanungExportV1Fach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachkombinationen" : [ ';
		for (let i = 0; i < obj.fachkombinationen.size(); i++) {
			const elem = obj.fachkombinationen.get(i);
			result += GostLaufbahnplanungExportV1Fachkombination.transpilerToJSON(elem);
			if (i < obj.fachkombinationen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += GostLaufbahnplanungExportV1Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV1>): string {
		let result = '{';
		if (obj.lpRevision !== undefined) {
			result += '"lpRevision" : ' + obj.lpRevision.toString() + ',';
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
				result += GostLaufbahnplanungExportV1Beratungslehrer.transpilerToJSON(elem);
				if (i < obj.beratungslehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostLaufbahnplanungExportV1Fach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachkombinationen !== undefined) {
			result += '"fachkombinationen" : [ ';
			for (let i = 0; i < obj.fachkombinationen.size(); i++) {
				const elem = obj.fachkombinationen.get(i);
				result += GostLaufbahnplanungExportV1Fachkombination.transpilerToJSON(elem);
				if (i < obj.fachkombinationen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += GostLaufbahnplanungExportV1Schueler.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v1_GostLaufbahnplanungExportV1(obj: unknown): GostLaufbahnplanungExportV1 {
	return obj as GostLaufbahnplanungExportV1;
}
