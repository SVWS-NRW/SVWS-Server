import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UnterrichtsverteilungStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die Unterrichtseinheitennummer.
	 */
	public unterrichtseinheitennummer: string | null = "";

	/**
	 * Satzschlüssel: Die Kopplungsnummer.
	 */
	public kopplungsnummer: string | null = "";

	/**
	 * Das Folgezeilenmerkmal.
	 */
	public folgezeilenmerkmal: string = "";

	/**
	 * Der Jahrgang.
	 */
	public jahrgang: string = "";

	/**
	 * 1. Stelle Parallelität / Das Bildungsgangkennzeichen.
	 */
	public bildungsgangkennzeichen: string = "";

	/**
	 * 2. Stelle Parallelität.
	 */
	public parallelitaet2: string = "";

	/**
	 * Die Teilklasse.
	 */
	public teilklasse: string = "";

	/**
	 * Die schulinterne Bezeichnung.
	 */
	public schulinterneBezeichnung: string = "";

	/**
	 * Die Schulgliederung der Klasse / Gruppe.
	 */
	public schulgliederung: string = "";

	/**
	 * Die Art der Gruppe.
	 */
	public artDerGruppe: string = "";

	/**
	 * Die Wochenstunden.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Das Fach.
	 */
	public fach: string = "";

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel: string = "";

	/**
	 * Die teilnehmenden Schüler insgesamt.
	 */
	public schuelerInsgesamt: number = 0;

	/**
	 * Die teilnehmenden Schüler weiblich.
	 */
	public schuelerWeiblich: number = 0;

	/**
	 * Die Unterrichtssprache bei bilingualem Unterricht.
	 */
	public bilingualSprache: string = "";

	/**
	 * Schüler von anderer Schule.
	 */
	public fremdschueler: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.UnterrichtsverteilungStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.UnterrichtsverteilungStatistikExport'].includes(name);
	}

	public static readonly class = new Class<UnterrichtsverteilungStatistikExport>('de.svws_nrw.asd.export.data.UnterrichtsverteilungStatistikExport');

	public static transpilerFromJSON(json: string): UnterrichtsverteilungStatistikExport {
		const obj = JSON.parse(json) as Partial<UnterrichtsverteilungStatistikExport>;
		const result = new UnterrichtsverteilungStatistikExport();
		result.unterrichtseinheitennummer = (obj.unterrichtseinheitennummer === undefined) ? null : obj.unterrichtseinheitennummer === null ? null : obj.unterrichtseinheitennummer;
		result.kopplungsnummer = (obj.kopplungsnummer === undefined) ? null : obj.kopplungsnummer === null ? null : obj.kopplungsnummer;
		if (obj.folgezeilenmerkmal === undefined)
			throw new Error('invalid json format, missing attribute folgezeilenmerkmal');
		result.folgezeilenmerkmal = obj.folgezeilenmerkmal;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.bildungsgangkennzeichen === undefined)
			throw new Error('invalid json format, missing attribute bildungsgangkennzeichen');
		result.bildungsgangkennzeichen = obj.bildungsgangkennzeichen;
		if (obj.parallelitaet2 === undefined)
			throw new Error('invalid json format, missing attribute parallelitaet2');
		result.parallelitaet2 = obj.parallelitaet2;
		if (obj.teilklasse === undefined)
			throw new Error('invalid json format, missing attribute teilklasse');
		result.teilklasse = obj.teilklasse;
		if (obj.schulinterneBezeichnung === undefined)
			throw new Error('invalid json format, missing attribute schulinterneBezeichnung');
		result.schulinterneBezeichnung = obj.schulinterneBezeichnung;
		if (obj.schulgliederung === undefined)
			throw new Error('invalid json format, missing attribute schulgliederung');
		result.schulgliederung = obj.schulgliederung;
		if (obj.artDerGruppe === undefined)
			throw new Error('invalid json format, missing attribute artDerGruppe');
		result.artDerGruppe = obj.artDerGruppe;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.fach === undefined)
			throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		if (obj.schuelerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerWeiblich');
		result.schuelerWeiblich = obj.schuelerWeiblich;
		if (obj.bilingualSprache === undefined)
			throw new Error('invalid json format, missing attribute bilingualSprache');
		result.bilingualSprache = obj.bilingualSprache;
		if (obj.fremdschueler === undefined)
			throw new Error('invalid json format, missing attribute fremdschueler');
		result.fremdschueler = obj.fremdschueler;
		return result;
	}

	public static transpilerToJSON(obj: UnterrichtsverteilungStatistikExport): string {
		let result = '{';
		result += '"unterrichtseinheitennummer" : ' + ((obj.unterrichtseinheitennummer === null) ? 'null' : JSON.stringify(obj.unterrichtseinheitennummer)) + ',';
		result += '"kopplungsnummer" : ' + ((obj.kopplungsnummer === null) ? 'null' : JSON.stringify(obj.kopplungsnummer)) + ',';
		result += '"folgezeilenmerkmal" : ' + JSON.stringify(obj.folgezeilenmerkmal) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"bildungsgangkennzeichen" : ' + JSON.stringify(obj.bildungsgangkennzeichen) + ',';
		result += '"parallelitaet2" : ' + JSON.stringify(obj.parallelitaet2) + ',';
		result += '"teilklasse" : ' + JSON.stringify(obj.teilklasse) + ',';
		result += '"schulinterneBezeichnung" : ' + JSON.stringify(obj.schulinterneBezeichnung) + ',';
		result += '"schulgliederung" : ' + JSON.stringify(obj.schulgliederung) + ',';
		result += '"artDerGruppe" : ' + JSON.stringify(obj.artDerGruppe) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"fach" : ' + JSON.stringify(obj.fach) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		result += '"bilingualSprache" : ' + JSON.stringify(obj.bilingualSprache) + ',';
		result += '"fremdschueler" : ' + obj.fremdschueler.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UnterrichtsverteilungStatistikExport>): string {
		let result = '{';
		if (obj.unterrichtseinheitennummer !== undefined) {
			result += '"unterrichtseinheitennummer" : ' + ((obj.unterrichtseinheitennummer === null) ? 'null' : JSON.stringify(obj.unterrichtseinheitennummer)) + ',';
		}
		if (obj.kopplungsnummer !== undefined) {
			result += '"kopplungsnummer" : ' + ((obj.kopplungsnummer === null) ? 'null' : JSON.stringify(obj.kopplungsnummer)) + ',';
		}
		if (obj.folgezeilenmerkmal !== undefined) {
			result += '"folgezeilenmerkmal" : ' + JSON.stringify(obj.folgezeilenmerkmal) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		if (obj.bildungsgangkennzeichen !== undefined) {
			result += '"bildungsgangkennzeichen" : ' + JSON.stringify(obj.bildungsgangkennzeichen) + ',';
		}
		if (obj.parallelitaet2 !== undefined) {
			result += '"parallelitaet2" : ' + JSON.stringify(obj.parallelitaet2) + ',';
		}
		if (obj.teilklasse !== undefined) {
			result += '"teilklasse" : ' + JSON.stringify(obj.teilklasse) + ',';
		}
		if (obj.schulinterneBezeichnung !== undefined) {
			result += '"schulinterneBezeichnung" : ' + JSON.stringify(obj.schulinterneBezeichnung) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + JSON.stringify(obj.schulgliederung) + ',';
		}
		if (obj.artDerGruppe !== undefined) {
			result += '"artDerGruppe" : ' + JSON.stringify(obj.artDerGruppe) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.fach !== undefined) {
			result += '"fach" : ' + JSON.stringify(obj.fach) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.schuelerInsgesamt !== undefined) {
			result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		}
		if (obj.schuelerWeiblich !== undefined) {
			result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		}
		if (obj.bilingualSprache !== undefined) {
			result += '"bilingualSprache" : ' + JSON.stringify(obj.bilingualSprache) + ',';
		}
		if (obj.fremdschueler !== undefined) {
			result += '"fremdschueler" : ' + obj.fremdschueler.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_UnterrichtsverteilungStatistikExport(obj: unknown): UnterrichtsverteilungStatistikExport {
	return obj as UnterrichtsverteilungStatistikExport;
}
