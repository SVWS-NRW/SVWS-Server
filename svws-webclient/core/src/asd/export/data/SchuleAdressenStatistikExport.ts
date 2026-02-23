import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuleAdressenSchuelerStatistikExport } from '../../../asd/export/data/SchuleAdressenSchuelerStatistikExport';
import { Class } from '../../../java/lang/Class';

export class SchuleAdressenStatistikExport extends JavaObject {

	/**
	 * Die ID der Adressen.
	 */
	public id: number = -1;

	/**
	 * Satzschlüssel: Das Adresskennzeichen einer Adresse.
	 */
	public adresskennzeichen: string | null = "";

	/**
	 * Der Amtliche Gemeindeschlüssel: Länderkuerzel.
	 */
	public regionalschluesselLaenderkuerzel: string | null = "";

	/**
	 * Der Amtliche Gemeindeschlüssel: Gemeindekennzahl.
	 */
	public regionalschluesselGemeindekennzahl: string | null = "";

	/**
	 * Der Strassenname einer Adresse.
	 */
	public strassenname: string | null = "";

	/**
	 * Die Hausnummer und Hausnummerzusatz einer Adresse.
	 */
	public hausnummer: string | null = "";

	/**
	 * Die Postleitzahl einer Adresse.
	 */
	public plz: string | null = "";

	/**
	 * Der Ort einer Adresse.
	 */
	public ort: string | null = "";

	/**
	 * Ist die Adresse der Hauptsitz der Schule.
	 */
	public istHauptsitz: boolean = true;

	/**
	 * Das Kennzeichen des Standorts.
	 */
	public standortkennzeichen: string | null = "";

	/**
	 * Ist die Adresse aktiv.
	 */
	public istAktiv: boolean = true;

	/**
	 * Die Art der Adresse.
	 */
	public idArt: number = 1;

	/**
	 * Die Qualität der Verortung.
	 */
	public verortungQualitaet: string | null = "";

	/**
	 * Koordinaten Rechtswert.
	 */
	public koordinateRechtswert: string | null = "";

	/**
	 * Koordinaten Hochwert.
	 */
	public koordinateHochwert: string | null = "";

	/**
	 * Adressvorgabedatensatz.
	 */
	public istAdressvorgabedatensatz: boolean = false;

	/**
	 * Datumstempel Vorgabedaten.
	 */
	public datumStempelVorgabedaten: string | null = "";

	/**
	 * Die Schülersummen zur Adresse (K88).
	 */
	public schuleAdressenSchuelerStatistikExport: SchuleAdressenSchuelerStatistikExport = new SchuleAdressenSchuelerStatistikExport();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.SchuleAdressenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.SchuleAdressenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<SchuleAdressenStatistikExport>('de.svws_nrw.asd.export.data.SchuleAdressenStatistikExport');

	public static transpilerFromJSON(json: string): SchuleAdressenStatistikExport {
		const obj = JSON.parse(json) as Partial<SchuleAdressenStatistikExport>;
		const result = new SchuleAdressenStatistikExport();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.adresskennzeichen = (obj.adresskennzeichen === undefined) ? null : obj.adresskennzeichen === null ? null : obj.adresskennzeichen;
		result.regionalschluesselLaenderkuerzel = (obj.regionalschluesselLaenderkuerzel === undefined) ? null : obj.regionalschluesselLaenderkuerzel === null ? null : obj.regionalschluesselLaenderkuerzel;
		result.regionalschluesselGemeindekennzahl = (obj.regionalschluesselGemeindekennzahl === undefined) ? null : obj.regionalschluesselGemeindekennzahl === null ? null : obj.regionalschluesselGemeindekennzahl;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		if (obj.istHauptsitz === undefined)
			throw new Error('invalid json format, missing attribute istHauptsitz');
		result.istHauptsitz = obj.istHauptsitz;
		result.standortkennzeichen = (obj.standortkennzeichen === undefined) ? null : obj.standortkennzeichen === null ? null : obj.standortkennzeichen;
		if (obj.istAktiv === undefined)
			throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (obj.idArt === undefined)
			throw new Error('invalid json format, missing attribute idArt');
		result.idArt = obj.idArt;
		result.verortungQualitaet = (obj.verortungQualitaet === undefined) ? null : obj.verortungQualitaet === null ? null : obj.verortungQualitaet;
		result.koordinateRechtswert = (obj.koordinateRechtswert === undefined) ? null : obj.koordinateRechtswert === null ? null : obj.koordinateRechtswert;
		result.koordinateHochwert = (obj.koordinateHochwert === undefined) ? null : obj.koordinateHochwert === null ? null : obj.koordinateHochwert;
		if (obj.istAdressvorgabedatensatz === undefined)
			throw new Error('invalid json format, missing attribute istAdressvorgabedatensatz');
		result.istAdressvorgabedatensatz = obj.istAdressvorgabedatensatz;
		result.datumStempelVorgabedaten = (obj.datumStempelVorgabedaten === undefined) ? null : obj.datumStempelVorgabedaten === null ? null : obj.datumStempelVorgabedaten;
		if (obj.schuleAdressenSchuelerStatistikExport === undefined)
			throw new Error('invalid json format, missing attribute schuleAdressenSchuelerStatistikExport');
		result.schuleAdressenSchuelerStatistikExport = SchuleAdressenSchuelerStatistikExport.transpilerFromJSON(JSON.stringify(obj.schuleAdressenSchuelerStatistikExport));
		return result;
	}

	public static transpilerToJSON(obj: SchuleAdressenStatistikExport): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
		result += '"regionalschluesselLaenderkuerzel" : ' + ((obj.regionalschluesselLaenderkuerzel === null) ? 'null' : JSON.stringify(obj.regionalschluesselLaenderkuerzel)) + ',';
		result += '"regionalschluesselGemeindekennzahl" : ' + ((obj.regionalschluesselGemeindekennzahl === null) ? 'null' : JSON.stringify(obj.regionalschluesselGemeindekennzahl)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"istHauptsitz" : ' + obj.istHauptsitz.toString() + ',';
		result += '"standortkennzeichen" : ' + ((obj.standortkennzeichen === null) ? 'null' : JSON.stringify(obj.standortkennzeichen)) + ',';
		result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		result += '"idArt" : ' + obj.idArt.toString() + ',';
		result += '"verortungQualitaet" : ' + ((obj.verortungQualitaet === null) ? 'null' : JSON.stringify(obj.verortungQualitaet)) + ',';
		result += '"koordinateRechtswert" : ' + ((obj.koordinateRechtswert === null) ? 'null' : JSON.stringify(obj.koordinateRechtswert)) + ',';
		result += '"koordinateHochwert" : ' + ((obj.koordinateHochwert === null) ? 'null' : JSON.stringify(obj.koordinateHochwert)) + ',';
		result += '"istAdressvorgabedatensatz" : ' + obj.istAdressvorgabedatensatz.toString() + ',';
		result += '"datumStempelVorgabedaten" : ' + ((obj.datumStempelVorgabedaten === null) ? 'null' : JSON.stringify(obj.datumStempelVorgabedaten)) + ',';
		result += '"schuleAdressenSchuelerStatistikExport" : ' + SchuleAdressenSchuelerStatistikExport.transpilerToJSON(obj.schuleAdressenSchuelerStatistikExport) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuleAdressenStatistikExport>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.adresskennzeichen !== undefined) {
			result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
		}
		if (obj.regionalschluesselLaenderkuerzel !== undefined) {
			result += '"regionalschluesselLaenderkuerzel" : ' + ((obj.regionalschluesselLaenderkuerzel === null) ? 'null' : JSON.stringify(obj.regionalschluesselLaenderkuerzel)) + ',';
		}
		if (obj.regionalschluesselGemeindekennzahl !== undefined) {
			result += '"regionalschluesselGemeindekennzahl" : ' + ((obj.regionalschluesselGemeindekennzahl === null) ? 'null' : JSON.stringify(obj.regionalschluesselGemeindekennzahl)) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.istHauptsitz !== undefined) {
			result += '"istHauptsitz" : ' + obj.istHauptsitz.toString() + ',';
		}
		if (obj.standortkennzeichen !== undefined) {
			result += '"standortkennzeichen" : ' + ((obj.standortkennzeichen === null) ? 'null' : JSON.stringify(obj.standortkennzeichen)) + ',';
		}
		if (obj.istAktiv !== undefined) {
			result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		}
		if (obj.idArt !== undefined) {
			result += '"idArt" : ' + obj.idArt.toString() + ',';
		}
		if (obj.verortungQualitaet !== undefined) {
			result += '"verortungQualitaet" : ' + ((obj.verortungQualitaet === null) ? 'null' : JSON.stringify(obj.verortungQualitaet)) + ',';
		}
		if (obj.koordinateRechtswert !== undefined) {
			result += '"koordinateRechtswert" : ' + ((obj.koordinateRechtswert === null) ? 'null' : JSON.stringify(obj.koordinateRechtswert)) + ',';
		}
		if (obj.koordinateHochwert !== undefined) {
			result += '"koordinateHochwert" : ' + ((obj.koordinateHochwert === null) ? 'null' : JSON.stringify(obj.koordinateHochwert)) + ',';
		}
		if (obj.istAdressvorgabedatensatz !== undefined) {
			result += '"istAdressvorgabedatensatz" : ' + obj.istAdressvorgabedatensatz.toString() + ',';
		}
		if (obj.datumStempelVorgabedaten !== undefined) {
			result += '"datumStempelVorgabedaten" : ' + ((obj.datumStempelVorgabedaten === null) ? 'null' : JSON.stringify(obj.datumStempelVorgabedaten)) + ',';
		}
		if (obj.schuleAdressenSchuelerStatistikExport !== undefined) {
			result += '"schuleAdressenSchuelerStatistikExport" : ' + SchuleAdressenSchuelerStatistikExport.transpilerToJSON(obj.schuleAdressenSchuelerStatistikExport) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_SchuleAdressenStatistikExport(obj: unknown): SchuleAdressenStatistikExport {
	return obj as SchuleAdressenStatistikExport;
}
