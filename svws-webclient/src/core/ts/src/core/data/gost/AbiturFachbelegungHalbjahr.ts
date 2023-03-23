import { JavaObject } from '../../../java/lang/JavaObject';

export class AbiturFachbelegungHalbjahr extends JavaObject {

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung
	 */
	public halbjahrKuerzel : string = "";

	/**
	 * Das Kürzel der Kursart der Gymnasialen Oberstufe dieser Fachbelegung
	 */
	public kursartKuerzel : string = "";

	/**
	 * Gibt an, ob das Fach schriftlich belegt wurde oder nicht.
	 */
	public schriftlich : boolean | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde.
	 */
	public biliSprache : string | null = null;

	/**
	 * Die ID des unterrichtenden Lehrers, welcher die Note erteilt.
	 */
	public lehrer : number | null = null;

	/**
	 * Die Wochenstundenzahl, mir der das Fach belegt wurde
	 */
	public wochenstunden : number = 0;

	/**
	 * Die Anzahl der Fehlstunden.
	 */
	public fehlstundenGesamt : number = 0;

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden.
	 */
	public fehlstundenUnentschuldigt : number = 0;

	/**
	 * Das Notenkürzel der erteilten Note
	 */
	public notenkuerzel : string | null = null;

	/**
	 * Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen
	 */
	public block1gewertet : boolean | null = null;

	/**
	 * Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen.
	 */
	public block1kursAufZeugnis : boolean | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.AbiturFachbelegungHalbjahr'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbiturFachbelegungHalbjahr {
		const obj = JSON.parse(json);
		const result = new AbiturFachbelegungHalbjahr();
		if (typeof obj.halbjahrKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute halbjahrKuerzel');
		result.halbjahrKuerzel = obj.halbjahrKuerzel;
		if (typeof obj.kursartKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kursartKuerzel');
		result.kursartKuerzel = obj.kursartKuerzel;
		result.schriftlich = typeof obj.schriftlich === "undefined" ? null : obj.schriftlich === null ? null : obj.schriftlich;
		result.biliSprache = typeof obj.biliSprache === "undefined" ? null : obj.biliSprache === null ? null : obj.biliSprache;
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : obj.lehrer;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.fehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (typeof obj.fehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		result.notenkuerzel = typeof obj.notenkuerzel === "undefined" ? null : obj.notenkuerzel === null ? null : obj.notenkuerzel;
		result.block1gewertet = typeof obj.block1gewertet === "undefined" ? null : obj.block1gewertet === null ? null : obj.block1gewertet;
		result.block1kursAufZeugnis = typeof obj.block1kursAufZeugnis === "undefined" ? null : obj.block1kursAufZeugnis === null ? null : obj.block1kursAufZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : AbiturFachbelegungHalbjahr) : string {
		let result = '{';
		result += '"halbjahrKuerzel" : ' + '"' + obj.halbjahrKuerzel! + '"' + ',';
		result += '"kursartKuerzel" : ' + '"' + obj.kursartKuerzel! + '"' + ',';
		result += '"schriftlich" : ' + ((!obj.schriftlich) ? 'null' : obj.schriftlich) + ',';
		result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache + '"') + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		result += '"notenkuerzel" : ' + ((!obj.notenkuerzel) ? 'null' : '"' + obj.notenkuerzel + '"') + ',';
		result += '"block1gewertet" : ' + ((!obj.block1gewertet) ? 'null' : obj.block1gewertet) + ',';
		result += '"block1kursAufZeugnis" : ' + ((!obj.block1kursAufZeugnis) ? 'null' : obj.block1kursAufZeugnis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbiturFachbelegungHalbjahr>) : string {
		let result = '{';
		if (typeof obj.halbjahrKuerzel !== "undefined") {
			result += '"halbjahrKuerzel" : ' + '"' + obj.halbjahrKuerzel + '"' + ',';
		}
		if (typeof obj.kursartKuerzel !== "undefined") {
			result += '"kursartKuerzel" : ' + '"' + obj.kursartKuerzel + '"' + ',';
		}
		if (typeof obj.schriftlich !== "undefined") {
			result += '"schriftlich" : ' + ((!obj.schriftlich) ? 'null' : obj.schriftlich) + ',';
		}
		if (typeof obj.biliSprache !== "undefined") {
			result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache + '"') + ',';
		}
		if (typeof obj.lehrer !== "undefined") {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer) + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		}
		if (typeof obj.notenkuerzel !== "undefined") {
			result += '"notenkuerzel" : ' + ((!obj.notenkuerzel) ? 'null' : '"' + obj.notenkuerzel + '"') + ',';
		}
		if (typeof obj.block1gewertet !== "undefined") {
			result += '"block1gewertet" : ' + ((!obj.block1gewertet) ? 'null' : obj.block1gewertet) + ',';
		}
		if (typeof obj.block1kursAufZeugnis !== "undefined") {
			result += '"block1kursAufZeugnis" : ' + ((!obj.block1kursAufZeugnis) ? 'null' : obj.block1kursAufZeugnis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegungHalbjahr(obj : unknown) : AbiturFachbelegungHalbjahr {
	return obj as AbiturFachbelegungHalbjahr;
}
