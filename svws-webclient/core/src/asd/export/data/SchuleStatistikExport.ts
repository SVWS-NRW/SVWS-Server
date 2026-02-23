import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuleAdressenStatistikExport } from '../../../asd/export/data/SchuleAdressenStatistikExport';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchuleStatistikExport extends JavaObject {

	/**
	 * Die eindeutige Schulnummer der Schule
	 */
	public schulNr: number = -1;

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
	public bezeichnung2: string | null = "";

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung3: string | null = "";

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 */
	public strassenname: string | null = "";

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 */
	public plz: string | null = "";

	/**
	 * Der Ort in dem die Schule liegt.
	 */
	public ort: string | null = "";

	/**
	 * Die Telefonnummer der Schule.
	 */
	public telefon: string | null = "";

	/**
	 * Die Faxnummer der Schule.
	 */
	public fax: string | null = "";

	/**
	 * Die Mailadresse der Schule.
	 */
	public email: string | null = "";

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 */
	public webAdresse: string | null = "";

	/**
	 * Das Zeitmodel (Unterrichtsstunden- (1) oder Unterrichtsminutenmodell (45)).
	 */
	public zeitmodel: number = 45;

	/**
	 * Gebundener Ganztag
	 */
	public gebundenerGanztag: number = 0;

	/**
	 * Offener Ganztag
	 */
	public istOffenerGanztag: boolean = false;

	/**
	 * Die Form des offenen Ganztag
	 */
	public formOffenerGanztag: string | null = "";

	/**
	 * Ist JVA
	 */
	public istJva: boolean = false;

	/**
	 * Bilingualer Unterricht
	 */
	public bilingualerUnterricht: number = 0;

	/**
	 * Hat Realschule Hauptbildungsgang
	 */
	public hatRealschuleHauptschulbildungsgang: boolean = false;

	/**
	 * Hat die Schule internationale Kontakte
	 */
	public hatInternationaleKontakte: boolean = false;

	/**
	 * Hat die Schule eine konfessionelle Kooperation
	 */
	public hatKonfessionelleKooperation: boolean = false;

	/**
	 * Talentschule Form
	 */
	public talentschule: number = 0;

	/**
	 * Reformpedagogik Schulebene
	 */
	public reformpaedagogik: string | null = "";

	/**
	 * Die Adressen einer Schule (B02).
	 */
	public adressenStatistikExport: List<SchuleAdressenStatistikExport> = new ArrayList<SchuleAdressenStatistikExport>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.SchuleStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.SchuleStatistikExport'].includes(name);
	}

	public static readonly class = new Class<SchuleStatistikExport>('de.svws_nrw.asd.export.data.SchuleStatistikExport');

	public static transpilerFromJSON(json: string): SchuleStatistikExport {
		const obj = JSON.parse(json) as Partial<SchuleStatistikExport>;
		const result = new SchuleStatistikExport();
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
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.fax = (obj.fax === undefined) ? null : obj.fax === null ? null : obj.fax;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.webAdresse = (obj.webAdresse === undefined) ? null : obj.webAdresse === null ? null : obj.webAdresse;
		if (obj.zeitmodel === undefined)
			throw new Error('invalid json format, missing attribute zeitmodel');
		result.zeitmodel = obj.zeitmodel;
		if (obj.gebundenerGanztag === undefined)
			throw new Error('invalid json format, missing attribute gebundenerGanztag');
		result.gebundenerGanztag = obj.gebundenerGanztag;
		if (obj.istOffenerGanztag === undefined)
			throw new Error('invalid json format, missing attribute istOffenerGanztag');
		result.istOffenerGanztag = obj.istOffenerGanztag;
		result.formOffenerGanztag = (obj.formOffenerGanztag === undefined) ? null : obj.formOffenerGanztag === null ? null : obj.formOffenerGanztag;
		if (obj.istJva === undefined)
			throw new Error('invalid json format, missing attribute istJva');
		result.istJva = obj.istJva;
		if (obj.bilingualerUnterricht === undefined)
			throw new Error('invalid json format, missing attribute bilingualerUnterricht');
		result.bilingualerUnterricht = obj.bilingualerUnterricht;
		if (obj.hatRealschuleHauptschulbildungsgang === undefined)
			throw new Error('invalid json format, missing attribute hatRealschuleHauptschulbildungsgang');
		result.hatRealschuleHauptschulbildungsgang = obj.hatRealschuleHauptschulbildungsgang;
		if (obj.hatInternationaleKontakte === undefined)
			throw new Error('invalid json format, missing attribute hatInternationaleKontakte');
		result.hatInternationaleKontakte = obj.hatInternationaleKontakte;
		if (obj.hatKonfessionelleKooperation === undefined)
			throw new Error('invalid json format, missing attribute hatKonfessionelleKooperation');
		result.hatKonfessionelleKooperation = obj.hatKonfessionelleKooperation;
		if (obj.talentschule === undefined)
			throw new Error('invalid json format, missing attribute talentschule');
		result.talentschule = obj.talentschule;
		result.reformpaedagogik = (obj.reformpaedagogik === undefined) ? null : obj.reformpaedagogik === null ? null : obj.reformpaedagogik;
		if (obj.adressenStatistikExport !== undefined) {
			for (const elem of obj.adressenStatistikExport) {
				result.adressenStatistikExport.add(SchuleAdressenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: SchuleStatistikExport): string {
		let result = '{';
		result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"bezeichnung1" : ' + JSON.stringify(obj.bezeichnung1) + ',';
		result += '"bezeichnung2" : ' + ((obj.bezeichnung2 === null) ? 'null' : JSON.stringify(obj.bezeichnung2)) + ',';
		result += '"bezeichnung3" : ' + ((obj.bezeichnung3 === null) ? 'null' : JSON.stringify(obj.bezeichnung3)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"webAdresse" : ' + ((obj.webAdresse === null) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		result += '"zeitmodel" : ' + obj.zeitmodel.toString() + ',';
		result += '"gebundenerGanztag" : ' + obj.gebundenerGanztag.toString() + ',';
		result += '"istOffenerGanztag" : ' + obj.istOffenerGanztag.toString() + ',';
		result += '"formOffenerGanztag" : ' + ((obj.formOffenerGanztag === null) ? 'null' : JSON.stringify(obj.formOffenerGanztag)) + ',';
		result += '"istJva" : ' + obj.istJva.toString() + ',';
		result += '"bilingualerUnterricht" : ' + obj.bilingualerUnterricht.toString() + ',';
		result += '"hatRealschuleHauptschulbildungsgang" : ' + obj.hatRealschuleHauptschulbildungsgang.toString() + ',';
		result += '"hatInternationaleKontakte" : ' + obj.hatInternationaleKontakte.toString() + ',';
		result += '"hatKonfessionelleKooperation" : ' + obj.hatKonfessionelleKooperation.toString() + ',';
		result += '"talentschule" : ' + obj.talentschule.toString() + ',';
		result += '"reformpaedagogik" : ' + ((obj.reformpaedagogik === null) ? 'null' : JSON.stringify(obj.reformpaedagogik)) + ',';
		result += '"adressenStatistikExport" : [ ';
		for (let i = 0; i < obj.adressenStatistikExport.size(); i++) {
			const elem = obj.adressenStatistikExport.get(i);
			result += SchuleAdressenStatistikExport.transpilerToJSON(elem);
			if (i < obj.adressenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuleStatistikExport>): string {
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
		if (obj.zeitmodel !== undefined) {
			result += '"zeitmodel" : ' + obj.zeitmodel.toString() + ',';
		}
		if (obj.gebundenerGanztag !== undefined) {
			result += '"gebundenerGanztag" : ' + obj.gebundenerGanztag.toString() + ',';
		}
		if (obj.istOffenerGanztag !== undefined) {
			result += '"istOffenerGanztag" : ' + obj.istOffenerGanztag.toString() + ',';
		}
		if (obj.formOffenerGanztag !== undefined) {
			result += '"formOffenerGanztag" : ' + ((obj.formOffenerGanztag === null) ? 'null' : JSON.stringify(obj.formOffenerGanztag)) + ',';
		}
		if (obj.istJva !== undefined) {
			result += '"istJva" : ' + obj.istJva.toString() + ',';
		}
		if (obj.bilingualerUnterricht !== undefined) {
			result += '"bilingualerUnterricht" : ' + obj.bilingualerUnterricht.toString() + ',';
		}
		if (obj.hatRealschuleHauptschulbildungsgang !== undefined) {
			result += '"hatRealschuleHauptschulbildungsgang" : ' + obj.hatRealschuleHauptschulbildungsgang.toString() + ',';
		}
		if (obj.hatInternationaleKontakte !== undefined) {
			result += '"hatInternationaleKontakte" : ' + obj.hatInternationaleKontakte.toString() + ',';
		}
		if (obj.hatKonfessionelleKooperation !== undefined) {
			result += '"hatKonfessionelleKooperation" : ' + obj.hatKonfessionelleKooperation.toString() + ',';
		}
		if (obj.talentschule !== undefined) {
			result += '"talentschule" : ' + obj.talentschule.toString() + ',';
		}
		if (obj.reformpaedagogik !== undefined) {
			result += '"reformpaedagogik" : ' + ((obj.reformpaedagogik === null) ? 'null' : JSON.stringify(obj.reformpaedagogik)) + ',';
		}
		if (obj.adressenStatistikExport !== undefined) {
			result += '"adressenStatistikExport" : [ ';
			for (let i = 0; i < obj.adressenStatistikExport.size(); i++) {
				const elem = obj.adressenStatistikExport.get(i);
				result += SchuleAdressenStatistikExport.transpilerToJSON(elem);
				if (i < obj.adressenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_SchuleStatistikExport(obj: unknown): SchuleStatistikExport {
	return obj as SchuleStatistikExport;
}
