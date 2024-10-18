import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostLeistungenFachbelegung extends JavaObject {

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
	 * Der Abschnitt, dem die Fachbelegung zugeordnet ist - unterscheidet sich z.B. im Quartalsbetrieb vom Halbjahr
	 */
	public abschnitt : number = 0;

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt.
	 */
	public abschnittGewertet : boolean = false;

	/**
	 * Gibt den Jahrgang, an dem die Belegung zugeordnet ist
	 */
	public jahrgang : string | null = null;

	/**
	 * Gibt die ID des Fachlehrers an, bei dem der zur Fachbelegung gehörige Kurs belegt wurde.
	 */
	public lehrer : number | null = null;

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


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLeistungenFachbelegung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLeistungenFachbelegung'].includes(name);
	}

	public static class = new Class<GostLeistungenFachbelegung>('de.svws_nrw.core.data.gost.GostLeistungenFachbelegung');

	public static transpilerFromJSON(json : string): GostLeistungenFachbelegung {
		const obj = JSON.parse(json) as Partial<GostLeistungenFachbelegung>;
		const result = new GostLeistungenFachbelegung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		result.halbjahrKuerzel = (obj.halbjahrKuerzel === undefined) ? null : obj.halbjahrKuerzel === null ? null : obj.halbjahrKuerzel;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.abschnittGewertet === undefined)
			throw new Error('invalid json format, missing attribute abschnittGewertet');
		result.abschnittGewertet = obj.abschnittGewertet;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.lehrer = (obj.lehrer === undefined) ? null : obj.lehrer === null ? null : obj.lehrer;
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

	public static transpilerToJSON(obj : GostLeistungenFachbelegung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"halbjahrKuerzel" : ' + ((!obj.halbjahrKuerzel) ? 'null' : JSON.stringify(obj.halbjahrKuerzel)) + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"abschnittGewertet" : ' + obj.abschnittGewertet.toString() + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.toString()) + ',';
		result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLeistungenFachbelegung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.halbjahrKuerzel !== undefined) {
			result += '"halbjahrKuerzel" : ' + ((!obj.halbjahrKuerzel) ? 'null' : JSON.stringify(obj.halbjahrKuerzel)) + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.abschnittGewertet !== undefined) {
			result += '"abschnittGewertet" : ' + obj.abschnittGewertet.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.toString()) + ',';
		}
		if (obj.notenKuerzel !== undefined) {
			result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		}
		if (obj.kursartKuerzel !== undefined) {
			result += '"kursartKuerzel" : ' + ((!obj.kursartKuerzel) ? 'null' : JSON.stringify(obj.kursartKuerzel)) + ',';
		}
		if (obj.istSchriftlich !== undefined) {
			result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
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

export function cast_de_svws_nrw_core_data_gost_GostLeistungenFachbelegung(obj : unknown) : GostLeistungenFachbelegung {
	return obj as GostLeistungenFachbelegung;
}
