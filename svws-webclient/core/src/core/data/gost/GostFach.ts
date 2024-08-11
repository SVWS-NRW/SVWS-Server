import { JavaObject } from '../../../java/lang/JavaObject';

export class GostFach extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public id : number = -1;

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzel : string = "";

	/**
	 * Das Fach-Kürzel, welches zur Anzeige verwendet wird.
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Nummer, welche die Sortierung der Fächer angibt.
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei der Belegprüfung).
	 */
	public istPruefungsordnungsRelevant : boolean = true;

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 */
	public istFremdsprache : boolean = false;

	/**
	 * Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist.
	 */
	public istFremdSpracheNeuEinsetzend : boolean = false;

	/**
	 * Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an.
	 */
	public biliSprache : string | null = null;

	/**
	 * Gibt an, ob das Fach als Leistungskurs im Abitur gewählt werden kann.
	 */
	public istMoeglichAbiLK : boolean = false;

	/**
	 * Gibt an, ob das Fach als Grundkurs im Abitur gewählt werden kann.
	 */
	public istMoeglichAbiGK : boolean = false;

	/**
	 * Gibt an, ob das Fach in der EF.1 gewählt werden kann.
	 */
	public istMoeglichEF1 : boolean = false;

	/**
	 * Gibt an, ob das Fach in der EF.2 gewählt werden kann.
	 */
	public istMoeglichEF2 : boolean = false;

	/**
	 * Gibt an, ob das Fach in der Q1.1 gewählt werden kann.
	 */
	public istMoeglichQ11 : boolean = false;

	/**
	 * Gibt an, ob das Fach in der Q1.2 gewählt werden kann.
	 */
	public istMoeglichQ12 : boolean = false;

	/**
	 * Gibt an, ob das Fach in der Q2.1 gewählt werden kann.
	 */
	public istMoeglichQ21 : boolean = false;

	/**
	 * Gibt an, ob das Fach in der Q2.2 gewählt werden kann.
	 */
	public istMoeglichQ22 : boolean = false;

	/**
	 * Die Wochenstundenzahl des Faches in der Qualifikationsphase
	 */
	public wochenstundenQualifikationsphase : number = 3;

	/**
	 * Die Fach-ID des Leitfaches eines Projektkurses oder Vertiefungsfaches
	 */
	public projektKursLeitfach1ID : number | null = null;

	/**
	 * Das Fach-Kürzel des Leitfaches eines Projektkurses oder Vertiefungsfaches
	 */
	public projektKursLeitfach1Kuerzel : string | null = null;

	/**
	 * Die Fach-ID des zweiten Leitfaches eines Projektkurses
	 */
	public projektKursLeitfach2ID : number | null = null;

	/**
	 * Die Fach-Kürzel des zweiten Leitfaches eines Projektkurses
	 */
	public projektKursLeitfach2Kuerzel : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostFach {
		const obj = JSON.parse(json) as Partial<GostFach>;
		const result = new GostFach();
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
		if (obj.istMoeglichEF1 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichEF1');
		result.istMoeglichEF1 = obj.istMoeglichEF1;
		if (obj.istMoeglichEF2 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichEF2');
		result.istMoeglichEF2 = obj.istMoeglichEF2;
		if (obj.istMoeglichQ11 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichQ11');
		result.istMoeglichQ11 = obj.istMoeglichQ11;
		if (obj.istMoeglichQ12 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichQ12');
		result.istMoeglichQ12 = obj.istMoeglichQ12;
		if (obj.istMoeglichQ21 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichQ21');
		result.istMoeglichQ21 = obj.istMoeglichQ21;
		if (obj.istMoeglichQ22 === undefined)
			throw new Error('invalid json format, missing attribute istMoeglichQ22');
		result.istMoeglichQ22 = obj.istMoeglichQ22;
		if (obj.wochenstundenQualifikationsphase === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenQualifikationsphase');
		result.wochenstundenQualifikationsphase = obj.wochenstundenQualifikationsphase;
		result.projektKursLeitfach1ID = (obj.projektKursLeitfach1ID === undefined) ? null : obj.projektKursLeitfach1ID === null ? null : obj.projektKursLeitfach1ID;
		result.projektKursLeitfach1Kuerzel = (obj.projektKursLeitfach1Kuerzel === undefined) ? null : obj.projektKursLeitfach1Kuerzel === null ? null : obj.projektKursLeitfach1Kuerzel;
		result.projektKursLeitfach2ID = (obj.projektKursLeitfach2ID === undefined) ? null : obj.projektKursLeitfach2ID === null ? null : obj.projektKursLeitfach2ID;
		result.projektKursLeitfach2Kuerzel = (obj.projektKursLeitfach2Kuerzel === undefined) ? null : obj.projektKursLeitfach2Kuerzel === null ? null : obj.projektKursLeitfach2Kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : GostFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istPruefungsordnungsRelevant" : ' + obj.istPruefungsordnungsRelevant.toString() + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend.toString() + ',';
		result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK.toString() + ',';
		result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK.toString() + ',';
		result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1.toString() + ',';
		result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2.toString() + ',';
		result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11.toString() + ',';
		result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12.toString() + ',';
		result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21.toString() + ',';
		result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22.toString() + ',';
		result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase.toString() + ',';
		result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID.toString()) + ',';
		result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : JSON.stringify(obj.projektKursLeitfach1Kuerzel)) + ',';
		result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID.toString()) + ',';
		result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : JSON.stringify(obj.projektKursLeitfach2Kuerzel)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostFach>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
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
			result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		}
		if (obj.istMoeglichAbiLK !== undefined) {
			result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK.toString() + ',';
		}
		if (obj.istMoeglichAbiGK !== undefined) {
			result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK.toString() + ',';
		}
		if (obj.istMoeglichEF1 !== undefined) {
			result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1.toString() + ',';
		}
		if (obj.istMoeglichEF2 !== undefined) {
			result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2.toString() + ',';
		}
		if (obj.istMoeglichQ11 !== undefined) {
			result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11.toString() + ',';
		}
		if (obj.istMoeglichQ12 !== undefined) {
			result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12.toString() + ',';
		}
		if (obj.istMoeglichQ21 !== undefined) {
			result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21.toString() + ',';
		}
		if (obj.istMoeglichQ22 !== undefined) {
			result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22.toString() + ',';
		}
		if (obj.wochenstundenQualifikationsphase !== undefined) {
			result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase.toString() + ',';
		}
		if (obj.projektKursLeitfach1ID !== undefined) {
			result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID.toString()) + ',';
		}
		if (obj.projektKursLeitfach1Kuerzel !== undefined) {
			result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : JSON.stringify(obj.projektKursLeitfach1Kuerzel)) + ',';
		}
		if (obj.projektKursLeitfach2ID !== undefined) {
			result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID.toString()) + ',';
		}
		if (obj.projektKursLeitfach2Kuerzel !== undefined) {
			result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : JSON.stringify(obj.projektKursLeitfach2Kuerzel)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostFach(obj : unknown) : GostFach {
	return obj as GostFach;
}
