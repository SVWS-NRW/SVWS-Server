import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';

export class SchuleStatistikGesamt extends JavaObject {

	/**
	 * Die eindeutige Schulnummer der Schule
	 */
	public schulNr: number = 0;

	/**
	 * Die Schulform der Schule
	 */
	public schulform: string = "";

	/**
	 * Der erste Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung1: string = "";

	/**
	 * Der zweite Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung2: string | null = null;

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung3: string | null = null;

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 */
	public strassenname: string | null = null;

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 */
	public hausnummer: string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 */
	public hausnummerZusatz: string | null = null;

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 */
	public plz: string | null = null;

	/**
	 * Der Ort in dem die Schule liegt.
	 */
	public ort: string | null = null;

	/**
	 * Die Telefonnummer der Schule.
	 */
	public telefon: string | null = null;

	/**
	 * Die Faxnummer der Schule.
	 */
	public fax: string | null = null;

	/**
	 * Die Mailadresse der Schule.
	 */
	public email: string | null = null;

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 */
	public webAdresse: string | null = null;

	/**
	 * Die ID des Schuljahresabschnittes, in welchem sich die Schule befindet.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Die Dauer einer Unterrichsteinheit in Minuten.
	 */
	public dauerUnterrichtseinheit: number = 0;

	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind.
	 */
	public readonly abschnitte: List<Schuljahresabschnitt> = new ArrayList<Schuljahresabschnitt>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchuleStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchuleStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchuleStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchuleStatistikGesamt');

	public static transpilerFromJSON(json: string): SchuleStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchuleStatistikGesamt>;
		const result = new SchuleStatistikGesamt();
		if (obj.schulNr === undefined)
			throw new Error('invalid json format, missing attribute schulNr');
		result.schulNr = obj.schulNr;
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		if (obj.bezeichnung1 === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung1');
		result.bezeichnung1 = obj.bezeichnung1;
		result.bezeichnung2 = (obj.bezeichnung2 === undefined) ? null : obj.bezeichnung2 === null ? null : obj.bezeichnung2;
		result.bezeichnung3 = (obj.bezeichnung3 === undefined) ? null : obj.bezeichnung3 === null ? null : obj.bezeichnung3;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.fax = (obj.fax === undefined) ? null : obj.fax === null ? null : obj.fax;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.webAdresse = (obj.webAdresse === undefined) ? null : obj.webAdresse === null ? null : obj.webAdresse;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.dauerUnterrichtseinheit === undefined)
			throw new Error('invalid json format, missing attribute dauerUnterrichtseinheit');
		result.dauerUnterrichtseinheit = obj.dauerUnterrichtseinheit;
		if (obj.abschnitte !== undefined) {
			for (const elem of obj.abschnitte) {
				result.abschnitte.add(Schuljahresabschnitt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: SchuleStatistikGesamt): string {
		let result = '{';
		result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"bezeichnung1" : ' + JSON.stringify(obj.bezeichnung1) + ',';
		result += '"bezeichnung2" : ' + ((obj.bezeichnung2 === null) ? 'null' : JSON.stringify(obj.bezeichnung2)) + ',';
		result += '"bezeichnung3" : ' + ((obj.bezeichnung3 === null) ? 'null' : JSON.stringify(obj.bezeichnung3)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"webAdresse" : ' + ((obj.webAdresse === null) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"dauerUnterrichtseinheit" : ' + obj.dauerUnterrichtseinheit.toString() + ',';
		result += '"abschnitte" : [ ';
		for (let i = 0; i < obj.abschnitte.size(); i++) {
			const elem = obj.abschnitte.get(i);
			result += Schuljahresabschnitt.transpilerToJSON(elem);
			if (i < obj.abschnitte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuleStatistikGesamt>): string {
		let result = '{';
		if (obj.schulNr !== undefined) {
			result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.bezeichnung1 !== undefined) {
			result += '"bezeichnung1" : ' + JSON.stringify(obj.bezeichnung1) + ',';
		}
		if (obj.bezeichnung2 !== undefined) {
			result += '"bezeichnung2" : ' + ((obj.bezeichnung2 === null) ? 'null' : JSON.stringify(obj.bezeichnung2)) + ',';
		}
		if (obj.bezeichnung3 !== undefined) {
			result += '"bezeichnung3" : ' + ((obj.bezeichnung3 === null) ? 'null' : JSON.stringify(obj.bezeichnung3)) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.hausnummerZusatz !== undefined) {
			result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.fax !== undefined) {
			result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.webAdresse !== undefined) {
			result += '"webAdresse" : ' + ((obj.webAdresse === null) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.dauerUnterrichtseinheit !== undefined) {
			result += '"dauerUnterrichtseinheit" : ' + obj.dauerUnterrichtseinheit.toString() + ',';
		}
		if (obj.abschnitte !== undefined) {
			result += '"abschnitte" : [ ';
			for (let i = 0; i < obj.abschnitte.size(); i++) {
				const elem = obj.abschnitte.get(i);
				result += Schuljahresabschnitt.transpilerToJSON(elem);
				if (i < obj.abschnitte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuleStatistikGesamt(obj: unknown): SchuleStatistikGesamt {
	return obj as SchuleStatistikGesamt;
}
