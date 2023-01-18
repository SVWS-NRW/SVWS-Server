import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class AbiturFachbelegungHalbjahr extends JavaObject {

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung 
	 */
	public halbjahrKuerzel : String = "";

	/**
	 * Das Kürzel der Kursart der Gymnasialen Oberstufe dieser Fachbelegung  
	 */
	public kursartKuerzel : String = "";

	/**
	 * Gibt an, ob das Fach schriftlich belegt wurde oder nicht. 
	 */
	public schriftlich : Boolean | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde. 
	 */
	public biliSprache : String | null = null;

	/**
	 * Die ID des unterrichtenden Lehrers, welcher die Note erteilt. 
	 */
	public lehrer : Number | null = null;

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
	public notenkuerzel : String | null = null;

	/**
	 * Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen 
	 */
	public block1gewertet : Boolean | null = null;

	/**
	 * Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen. 
	 */
	public block1kursAufZeugnis : Boolean | null = null;


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
		result.halbjahrKuerzel = String(obj.halbjahrKuerzel);
		if (typeof obj.kursartKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kursartKuerzel');
		result.kursartKuerzel = String(obj.kursartKuerzel);
		result.schriftlich = typeof obj.schriftlich === "undefined" ? null : obj.schriftlich === null ? null : Boolean(obj.schriftlich);
		result.biliSprache = typeof obj.biliSprache === "undefined" ? null : obj.biliSprache === null ? null : String(obj.biliSprache);
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : Number(obj.lehrer);
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.fehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (typeof obj.fehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		result.notenkuerzel = typeof obj.notenkuerzel === "undefined" ? null : obj.notenkuerzel === null ? null : String(obj.notenkuerzel);
		result.block1gewertet = typeof obj.block1gewertet === "undefined" ? null : obj.block1gewertet === null ? null : Boolean(obj.block1gewertet);
		result.block1kursAufZeugnis = typeof obj.block1kursAufZeugnis === "undefined" ? null : obj.block1kursAufZeugnis === null ? null : Boolean(obj.block1kursAufZeugnis);
		return result;
	}

	public static transpilerToJSON(obj : AbiturFachbelegungHalbjahr) : string {
		let result = '{';
		result += '"halbjahrKuerzel" : ' + '"' + obj.halbjahrKuerzel.valueOf() + '"' + ',';
		result += '"kursartKuerzel" : ' + '"' + obj.kursartKuerzel.valueOf() + '"' + ',';
		result += '"schriftlich" : ' + ((!obj.schriftlich) ? 'null' : obj.schriftlich.valueOf()) + ',';
		result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache.valueOf() + '"') + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		result += '"notenkuerzel" : ' + ((!obj.notenkuerzel) ? 'null' : '"' + obj.notenkuerzel.valueOf() + '"') + ',';
		result += '"block1gewertet" : ' + ((!obj.block1gewertet) ? 'null' : obj.block1gewertet.valueOf()) + ',';
		result += '"block1kursAufZeugnis" : ' + ((!obj.block1kursAufZeugnis) ? 'null' : obj.block1kursAufZeugnis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbiturFachbelegungHalbjahr>) : string {
		let result = '{';
		if (typeof obj.halbjahrKuerzel !== "undefined") {
			result += '"halbjahrKuerzel" : ' + '"' + obj.halbjahrKuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.kursartKuerzel !== "undefined") {
			result += '"kursartKuerzel" : ' + '"' + obj.kursartKuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.schriftlich !== "undefined") {
			result += '"schriftlich" : ' + ((!obj.schriftlich) ? 'null' : obj.schriftlich.valueOf()) + ',';
		}
		if (typeof obj.biliSprache !== "undefined") {
			result += '"biliSprache" : ' + ((!obj.biliSprache) ? 'null' : '"' + obj.biliSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.lehrer !== "undefined") {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
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
			result += '"notenkuerzel" : ' + ((!obj.notenkuerzel) ? 'null' : '"' + obj.notenkuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.block1gewertet !== "undefined") {
			result += '"block1gewertet" : ' + ((!obj.block1gewertet) ? 'null' : obj.block1gewertet.valueOf()) + ',';
		}
		if (typeof obj.block1kursAufZeugnis !== "undefined") {
			result += '"block1kursAufZeugnis" : ' + ((!obj.block1kursAufZeugnis) ? 'null' : obj.block1kursAufZeugnis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegungHalbjahr(obj : unknown) : AbiturFachbelegungHalbjahr {
	return obj as AbiturFachbelegungHalbjahr;
}
