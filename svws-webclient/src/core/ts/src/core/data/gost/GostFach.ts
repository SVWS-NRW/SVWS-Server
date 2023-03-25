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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostFach {
		const obj = JSON.parse(json);
		const result = new GostFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.kuerzelAnzeige = typeof obj.kuerzelAnzeige === "undefined" ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istFremdsprache === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (typeof obj.istFremdSpracheNeuEinsetzend === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdSpracheNeuEinsetzend');
		result.istFremdSpracheNeuEinsetzend = obj.istFremdSpracheNeuEinsetzend;
		result.biliSprache = typeof obj.biliSprache === "undefined" ? null : obj.biliSprache === null ? null : obj.biliSprache;
		if (typeof obj.istMoeglichAbiLK === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichAbiLK');
		result.istMoeglichAbiLK = obj.istMoeglichAbiLK;
		if (typeof obj.istMoeglichAbiGK === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichAbiGK');
		result.istMoeglichAbiGK = obj.istMoeglichAbiGK;
		if (typeof obj.istMoeglichEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichEF1');
		result.istMoeglichEF1 = obj.istMoeglichEF1;
		if (typeof obj.istMoeglichEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichEF2');
		result.istMoeglichEF2 = obj.istMoeglichEF2;
		if (typeof obj.istMoeglichQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ11');
		result.istMoeglichQ11 = obj.istMoeglichQ11;
		if (typeof obj.istMoeglichQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ12');
		result.istMoeglichQ12 = obj.istMoeglichQ12;
		if (typeof obj.istMoeglichQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ21');
		result.istMoeglichQ21 = obj.istMoeglichQ21;
		if (typeof obj.istMoeglichQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute istMoeglichQ22');
		result.istMoeglichQ22 = obj.istMoeglichQ22;
		if (typeof obj.wochenstundenQualifikationsphase === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQualifikationsphase');
		result.wochenstundenQualifikationsphase = obj.wochenstundenQualifikationsphase;
		result.projektKursLeitfach1ID = typeof obj.projektKursLeitfach1ID === "undefined" ? null : obj.projektKursLeitfach1ID === null ? null : obj.projektKursLeitfach1ID;
		result.projektKursLeitfach1Kuerzel = typeof obj.projektKursLeitfach1Kuerzel === "undefined" ? null : obj.projektKursLeitfach1Kuerzel === null ? null : obj.projektKursLeitfach1Kuerzel;
		result.projektKursLeitfach2ID = typeof obj.projektKursLeitfach2ID === "undefined" ? null : obj.projektKursLeitfach2ID === null ? null : obj.projektKursLeitfach2ID;
		result.projektKursLeitfach2Kuerzel = typeof obj.projektKursLeitfach2Kuerzel === "undefined" ? null : obj.projektKursLeitfach2Kuerzel === null ? null : obj.projektKursLeitfach2Kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : GostFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend + ',';
		result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache + '"') + ',';
		result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK + ',';
		result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK + ',';
		result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1 + ',';
		result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2 + ',';
		result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11 + ',';
		result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12 + ',';
		result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21 + ',';
		result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22 + ',';
		result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase + ',';
		result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID) + ',';
		result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach1Kuerzel + '"') + ',';
		result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID) + ',';
		result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach2Kuerzel + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.kuerzelAnzeige !== "undefined") {
			result += '"kuerzelAnzeige" : ' + ((!obj.kuerzelAnzeige) ? 'null' : '"' + obj.kuerzelAnzeige + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		}
		if (typeof obj.istFremdSpracheNeuEinsetzend !== "undefined") {
			result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend + ',';
		}
		if (typeof obj.biliSprache !== "undefined") {
			result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache + '"') + ',';
		}
		if (typeof obj.istMoeglichAbiLK !== "undefined") {
			result += '"istMoeglichAbiLK" : ' + obj.istMoeglichAbiLK + ',';
		}
		if (typeof obj.istMoeglichAbiGK !== "undefined") {
			result += '"istMoeglichAbiGK" : ' + obj.istMoeglichAbiGK + ',';
		}
		if (typeof obj.istMoeglichEF1 !== "undefined") {
			result += '"istMoeglichEF1" : ' + obj.istMoeglichEF1 + ',';
		}
		if (typeof obj.istMoeglichEF2 !== "undefined") {
			result += '"istMoeglichEF2" : ' + obj.istMoeglichEF2 + ',';
		}
		if (typeof obj.istMoeglichQ11 !== "undefined") {
			result += '"istMoeglichQ11" : ' + obj.istMoeglichQ11 + ',';
		}
		if (typeof obj.istMoeglichQ12 !== "undefined") {
			result += '"istMoeglichQ12" : ' + obj.istMoeglichQ12 + ',';
		}
		if (typeof obj.istMoeglichQ21 !== "undefined") {
			result += '"istMoeglichQ21" : ' + obj.istMoeglichQ21 + ',';
		}
		if (typeof obj.istMoeglichQ22 !== "undefined") {
			result += '"istMoeglichQ22" : ' + obj.istMoeglichQ22 + ',';
		}
		if (typeof obj.wochenstundenQualifikationsphase !== "undefined") {
			result += '"wochenstundenQualifikationsphase" : ' + obj.wochenstundenQualifikationsphase + ',';
		}
		if (typeof obj.projektKursLeitfach1ID !== "undefined") {
			result += '"projektKursLeitfach1ID" : ' + ((!obj.projektKursLeitfach1ID) ? 'null' : obj.projektKursLeitfach1ID) + ',';
		}
		if (typeof obj.projektKursLeitfach1Kuerzel !== "undefined") {
			result += '"projektKursLeitfach1Kuerzel" : ' + ((!obj.projektKursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach1Kuerzel + '"') + ',';
		}
		if (typeof obj.projektKursLeitfach2ID !== "undefined") {
			result += '"projektKursLeitfach2ID" : ' + ((!obj.projektKursLeitfach2ID) ? 'null' : obj.projektKursLeitfach2ID) + ',';
		}
		if (typeof obj.projektKursLeitfach2Kuerzel !== "undefined") {
			result += '"projektKursLeitfach2Kuerzel" : ' + ((!obj.projektKursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektKursLeitfach2Kuerzel + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostFach(obj : unknown) : GostFach {
	return obj as GostFach;
}
