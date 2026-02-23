import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuleAdressenStatistikGesamt extends JavaObject {

	/**
	 * Die ID der Adressen.
	 */
	public id: number = 0;

	/**
	 * Das Adresskennzeichen einer Adresse.
	 */
	public adresskennzeichen: string | null = null;

	/**
	 * Der Strassenname einer Adresse.
	 */
	public strassenname: string | null = null;

	/**
	 * Die Hausnummer einer Adresse.
	 */
	public hausnummer: string | null = null;

	/**
	 * Der Hausnummernzusatz einer Adresse.
	 */
	public hausnummerZusatz: string | null = null;

	/**
	 * Die Postleitzahl einer Adresse.
	 */
	public plz: string | null = null;

	/**
	 * Der Ort einer Adresse.
	 */
	public ort: string | null = null;

	/**
	 * Ist die Adresse der Hauptsitz der Schule.
	 */
	public istHauptsitz: boolean = false;

	/**
	 * Das Kennzeichen des Standorts.
	 */
	public standortkennzeichen: string | null = null;

	/**
	 * Ist die Adresse aktiv.
	 */
	public istAktiv: boolean = false;

	/**
	 * Die Art der Adresse.
	 */
	public idArt: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchuleAdressenStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchuleAdressenStatistikGesamt');

	public static transpilerFromJSON(json: string): SchuleAdressenStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchuleAdressenStatistikGesamt>;
		const result = new SchuleAdressenStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.adresskennzeichen = (obj.adresskennzeichen === undefined) ? null : obj.adresskennzeichen === null ? null : obj.adresskennzeichen;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
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
		return result;
	}

	public static transpilerToJSON(obj: SchuleAdressenStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"istHauptsitz" : ' + obj.istHauptsitz.toString() + ',';
		result += '"standortkennzeichen" : ' + ((obj.standortkennzeichen === null) ? 'null' : JSON.stringify(obj.standortkennzeichen)) + ',';
		result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		result += '"idArt" : ' + obj.idArt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuleAdressenStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.adresskennzeichen !== undefined) {
			result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuleAdressenStatistikGesamt(obj: unknown): SchuleAdressenStatistikGesamt {
	return obj as SchuleAdressenStatistikGesamt;
}
