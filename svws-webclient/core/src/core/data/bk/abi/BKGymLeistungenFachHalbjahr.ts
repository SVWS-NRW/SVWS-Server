import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class BKGymLeistungenFachHalbjahr extends JavaObject {

	/**
	 * Die ID des Datensatzes
	 */
	public id : number = 0;

	/**
	 * Das Schuljahr der Fachbelegung
	 */
	public schuljahr : number = 0;

	/**
	 * Das Kürzel des Halbjahres der Fachbelegung
	 */
	public halbjahrKuerzel : string | null = null;

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt.
	 */
	public abschnittGewertet : boolean = false;

	/**
	 * Gibt den Jahrgang, an dem die Belegung zugeordnet ist
	 */
	public jahrgang : string | null = null;

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number | null = null;

	/**
	 * Gibt die ID des Fachlehrers an, bei dem der zur Fachbelegung gehörige Kurs belegt wurde.
	 */
	public idFachlehrer : number | null = null;

	/**
	 * Gibt die Note als Zeichenkette und mit Tendenz an
	 */
	public notenKuerzel : string | null = null;

	/**
	 * Das Kürzel der Kursart der gymnasialen Oberstufe des belegten Kurses
	 */
	public kursartKuerzel : string | null = null;

	/**
	 * Gibt an, ob der Kurs schriftlich belegt wurde.
	 */
	public istSchriftlich : boolean = false;

	/**
	 * Gibt die Sprache als einstelliges Kürzel an, sofern der Kurs bilingual unterrichtet wurde.
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Gibt die Anzahl der Wochenstunden für den Kurs an.
	 */
	public wochenstunden : number = 0;

	/**
	 * Gibt die Anzahl der Fehlstunden in dem Halbjahr an.
	 */
	public fehlstundenGesamt : number = 0;

	/**
	 * Gibt die Anzahl der unentschuldigten Fehlstunden in dem Halbjahr an.
	 */
	public fehlstundenUnentschuldigt : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymLeistungenFachHalbjahr';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymLeistungenFachHalbjahr'].includes(name);
	}

	public static class = new Class<BKGymLeistungenFachHalbjahr>('de.svws_nrw.core.data.bk.abi.BKGymLeistungenFachHalbjahr');

	public static transpilerFromJSON(json : string): BKGymLeistungenFachHalbjahr {
		const obj = JSON.parse(json) as Partial<BKGymLeistungenFachHalbjahr>;
		const result = new BKGymLeistungenFachHalbjahr();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		result.halbjahrKuerzel = (obj.halbjahrKuerzel === undefined) ? null : obj.halbjahrKuerzel === null ? null : obj.halbjahrKuerzel;
		if (obj.abschnittGewertet === undefined)
			throw new Error('invalid json format, missing attribute abschnittGewertet');
		result.abschnittGewertet = obj.abschnittGewertet;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.idKurs = (obj.idKurs === undefined) ? null : obj.idKurs === null ? null : obj.idKurs;
		result.idFachlehrer = (obj.idFachlehrer === undefined) ? null : obj.idFachlehrer === null ? null : obj.idFachlehrer;
		result.notenKuerzel = (obj.notenKuerzel === undefined) ? null : obj.notenKuerzel === null ? null : obj.notenKuerzel;
		result.kursartKuerzel = (obj.kursartKuerzel === undefined) ? null : obj.kursartKuerzel === null ? null : obj.kursartKuerzel;
		if (obj.istSchriftlich === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.fehlstundenGesamt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (obj.fehlstundenUnentschuldigt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		return result;
	}

	public static transpilerToJSON(obj : BKGymLeistungenFachHalbjahr) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"halbjahrKuerzel" : ' + ((obj.halbjahrKuerzel === null) ? 'null' : JSON.stringify(obj.halbjahrKuerzel)) + ',';
		result += '"abschnittGewertet" : ' + obj.abschnittGewertet.toString() + ',';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		result += '"idFachlehrer" : ' + ((obj.idFachlehrer === null) ? 'null' : obj.idFachlehrer.toString()) + ',';
		result += '"notenKuerzel" : ' + ((obj.notenKuerzel === null) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		result += '"kursartKuerzel" : ' + ((obj.kursartKuerzel === null) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymLeistungenFachHalbjahr>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.halbjahrKuerzel !== undefined) {
			result += '"halbjahrKuerzel" : ' + ((obj.halbjahrKuerzel === null) ? 'null' : JSON.stringify(obj.halbjahrKuerzel)) + ',';
		}
		if (obj.abschnittGewertet !== undefined) {
			result += '"abschnittGewertet" : ' + obj.abschnittGewertet.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		}
		if (obj.idFachlehrer !== undefined) {
			result += '"idFachlehrer" : ' + ((obj.idFachlehrer === null) ? 'null' : obj.idFachlehrer.toString()) + ',';
		}
		if (obj.notenKuerzel !== undefined) {
			result += '"notenKuerzel" : ' + ((obj.notenKuerzel === null) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		}
		if (obj.kursartKuerzel !== undefined) {
			result += '"kursartKuerzel" : ' + ((obj.kursartKuerzel === null) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		}
		if (obj.istSchriftlich !== undefined) {
			result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_abi_BKGymLeistungenFachHalbjahr(obj : unknown) : BKGymLeistungenFachHalbjahr {
	return obj as BKGymLeistungenFachHalbjahr;
}
