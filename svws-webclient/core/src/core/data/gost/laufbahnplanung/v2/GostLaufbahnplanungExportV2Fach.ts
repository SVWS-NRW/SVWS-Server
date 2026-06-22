import { JavaObject } from '../../../../../java/lang/JavaObject';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../../../java/lang/Class';

export class GostLaufbahnplanungExportV2Fach extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public id: number = -1;

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzel: string = "";

	/**
	 * Das Fach-Kürzel, welches zur Anzeige verwendet wird.
	 */
	public kuerzelAnzeige: string | null = null;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung: string | null = null;

	/**
	 * Die Nummer, welche die Sortierung der Fächer angibt.
	 */
	public sortierung: number = 32000;

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei der Belegprüfung).
	 */
	public istPruefungsordnungsRelevant: boolean = true;

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 */
	public istFremdsprache: boolean = false;

	/**
	 * Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist.
	 */
	public istFremdSpracheNeuEinsetzend: boolean = false;

	/**
	 * Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an.
	 */
	public biliSprache: string | null = null;

	/**
	 * Gibt an, ob das Fach als Leistungskurs im Abitur gewählt werden kann.
	 */
	public istMoeglichAbiLK: boolean = false;

	/**
	 * Gibt an, ob das Fach als Grundkurs im Abitur gewählt werden kann.
	 */
	public istMoeglichAbiGK: boolean = false;

	/**
	 * Gibt an, ob die Belegung dieses Faches in einem Halbjahr möglich ist oder nicht
	 */
	public readonly istMoeglich: Array<boolean> = Array(GostHalbjahr.maxHalbjahre).fill(false);

	/**
	 * Die Wochenstundenzahl des Faches in der Qualifikationsphase
	 */
	public wochenstundenQualifikationsphase: number = 3;

	/**
	 * Die Fach-ID des Referenzfaches eines Projektkurses oder Vertiefungsfaches
	 */
	public referenzfach1ID: number | null = null;

	/**
	 * Die Fach-ID des zweiten Referenzfaches eines Projektkurses
	 */
	public referenzfach2ID: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Fach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Fach'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV2Fach>('de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Fach');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV2Fach {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV2Fach>;
		const result = new GostLaufbahnplanungExportV2Fach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istPruefungsordnungsRelevant === undefined)
			throw new Error('invalid json format, missing attribute istPruefungsordnungsRelevant');
		result.istPruefungsordnungsRelevant = obj.istPruefungsordnungsRelevant;
		if (obj.istFremdsprache === undefined)
			throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (obj.istFremdSpracheNeuEinsetzend === undefined)
			throw new Error('invalid json format, missing attribute istFremdSpracheNeuEinsetzend');
		result.istFremdSpracheNeuEinsetzend = obj.istFremdSpracheNeuEinsetzend;
		result.biliSprache = (obj.biliSprache === undefined) ? null : obj.biliSprache === null ? null : obj.biliSprache;
		if (obj.istMoeglichAbiLK === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichAbiLK');
		result.istMoeglichAbiLK = obj.istMoeglichAbiLK;
		if (obj.istMoeglichAbiGK === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichAbiGK');
		result.istMoeglichAbiGK = obj.istMoeglichAbiGK;
		if (obj.istMoeglich !== undefined) {
			for (let i = 0; i < obj.istMoeglich.length; i++) {
				result.istMoeglich[i] = obj.istMoeglich[i];
			}
		}
		if (obj.wochenstundenQualifikationsphase === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenQualifikationsphase');
		result.wochenstundenQualifikationsphase = obj.wochenstundenQualifikationsphase;
		result.referenzfach1ID = (obj.referenzfach1ID === undefined) ? null : obj.referenzfach1ID === null ? null : obj.referenzfach1ID;
		result.referenzfach2ID = (obj.referenzfach2ID === undefined) ? null : obj.referenzfach2ID === null ? null : obj.referenzfach2ID;
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV2Fach): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istPruefungsordnungsRelevant" : ' + obj.istPruefungsordnungsRelevant.toString() + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend.toString() + ',';
		result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK.toString() + ',';
		result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK.toString() + ',';
		result += '"istMoeglich" : [ ';
		for (let i = 0; i < obj.istMoeglich.length; i++) {
			const elem = obj.istMoeglich[i];
			result += JSON.stringify(elem);
			if (i < obj.istMoeglich.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase.toString() + ',';
		result += '"referenzfach1ID" : ' + ((obj.referenzfach1ID === null) ? 'null' : obj.referenzfach1ID.toString()) + ',';
		result += '"referenzfach2ID" : ' + ((obj.referenzfach2ID === null) ? 'null' : obj.referenzfach2ID.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV2Fach>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istPruefungsordnungsRelevant !== undefined) {
			result += '"istPruefungsordnungsRelevant" : ' + obj.istPruefungsordnungsRelevant.toString() + ',';
		}
		if (obj.istFremdsprache !== undefined) {
			result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		}
		if (obj.istFremdSpracheNeuEinsetzend !== undefined) {
			result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend.toString() + ',';
		}
		if (obj.biliSprache !== undefined) {
			result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		}
		if (obj.istMoeglichAbiLK !== undefined) {
			result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK.toString() + ',';
		}
		if (obj.istMoeglichAbiGK !== undefined) {
			result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK.toString() + ',';
		}
		if (obj.istMoeglich !== undefined) {
			const a = obj.istMoeglich;
			result += '"istMoeglich" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wochenstundenQualifikationsphase !== undefined) {
			result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase.toString() + ',';
		}
		if (obj.referenzfach1ID !== undefined) {
			result += '"referenzfach1ID" : ' + ((obj.referenzfach1ID === null) ? 'null' : obj.referenzfach1ID.toString()) + ',';
		}
		if (obj.referenzfach2ID !== undefined) {
			result += '"referenzfach2ID" : ' + ((obj.referenzfach2ID === null) ? 'null' : obj.referenzfach2ID.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2Fach(obj: unknown): GostLaufbahnplanungExportV2Fach {
	return obj as GostLaufbahnplanungExportV2Fach;
}
