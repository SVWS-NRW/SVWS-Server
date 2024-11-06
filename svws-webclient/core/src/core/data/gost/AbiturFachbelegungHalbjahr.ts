import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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
	public schriftlich : boolean = false;

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
	 * Das Notenkürzel der erteilten Note. Das Kürzel ist ein leerer String, falls keine Note in den Leistungsdaten gesetzt ist. Der Wert null ist nur zulässig, wenn Fachwahlen vorliegen, für die keine Leistungsdaten hinterlegt sind.
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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr'].includes(name);
	}

	public static class = new Class<AbiturFachbelegungHalbjahr>('de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr');

	public static transpilerFromJSON(json : string): AbiturFachbelegungHalbjahr {
		const obj = JSON.parse(json) as Partial<AbiturFachbelegungHalbjahr>;
		const result = new AbiturFachbelegungHalbjahr();
		if (obj.halbjahrKuerzel === undefined)
			throw new Error('invalid json format, missing attribute halbjahrKuerzel');
		result.halbjahrKuerzel = obj.halbjahrKuerzel;
		if (obj.kursartKuerzel === undefined)
			throw new Error('invalid json format, missing attribute kursartKuerzel');
		result.kursartKuerzel = obj.kursartKuerzel;
		if (obj.schriftlich === undefined)
			throw new Error('invalid json format, missing attribute schriftlich');
		result.schriftlich = obj.schriftlich;
		result.biliSprache = (obj.biliSprache === undefined) ? null : obj.biliSprache === null ? null : obj.biliSprache;
		result.lehrer = (obj.lehrer === undefined) ? null : obj.lehrer === null ? null : obj.lehrer;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.fehlstundenGesamt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (obj.fehlstundenUnentschuldigt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		result.notenkuerzel = (obj.notenkuerzel === undefined) ? null : obj.notenkuerzel === null ? null : obj.notenkuerzel;
		result.block1gewertet = (obj.block1gewertet === undefined) ? null : obj.block1gewertet === null ? null : obj.block1gewertet;
		result.block1kursAufZeugnis = (obj.block1kursAufZeugnis === undefined) ? null : obj.block1kursAufZeugnis === null ? null : obj.block1kursAufZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : AbiturFachbelegungHalbjahr) : string {
		let result = '{';
		result += '"halbjahrKuerzel" : ' + JSON.stringify(obj.halbjahrKuerzel) + ',';
		result += '"kursartKuerzel" : ' + JSON.stringify(obj.kursartKuerzel) + ',';
		result += '"schriftlich" : ' + obj.schriftlich.toString() + ',';
		result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		result += '"notenkuerzel" : ' + ((obj.notenkuerzel === null) ? 'null' : JSON.stringify(obj.notenkuerzel)) + ',';
		result += '"block1gewertet" : ' + ((obj.block1gewertet === null) ? 'null' : obj.block1gewertet.toString()) + ',';
		result += '"block1kursAufZeugnis" : ' + ((obj.block1kursAufZeugnis === null) ? 'null' : obj.block1kursAufZeugnis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbiturFachbelegungHalbjahr>) : string {
		let result = '{';
		if (obj.halbjahrKuerzel !== undefined) {
			result += '"halbjahrKuerzel" : ' + JSON.stringify(obj.halbjahrKuerzel) + ',';
		}
		if (obj.kursartKuerzel !== undefined) {
			result += '"kursartKuerzel" : ' + JSON.stringify(obj.kursartKuerzel) + ',';
		}
		if (obj.schriftlich !== undefined) {
			result += '"schriftlich" : ' + obj.schriftlich.toString() + ',';
		}
		if (obj.biliSprache !== undefined) {
			result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.fehlstundenGesamt !== undefined) {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		}
		if (obj.fehlstundenUnentschuldigt !== undefined) {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		}
		if (obj.notenkuerzel !== undefined) {
			result += '"notenkuerzel" : ' + ((obj.notenkuerzel === null) ? 'null' : JSON.stringify(obj.notenkuerzel)) + ',';
		}
		if (obj.block1gewertet !== undefined) {
			result += '"block1gewertet" : ' + ((obj.block1gewertet === null) ? 'null' : obj.block1gewertet.toString()) + ',';
		}
		if (obj.block1kursAufZeugnis !== undefined) {
			result += '"block1kursAufZeugnis" : ' + ((obj.block1kursAufZeugnis === null) ? 'null' : obj.block1kursAufZeugnis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_AbiturFachbelegungHalbjahr(obj : unknown) : AbiturFachbelegungHalbjahr {
	return obj as AbiturFachbelegungHalbjahr;
}
