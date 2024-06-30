import { AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import { Telefonnummer } from '../../../core/data/adressbuch/Telefonnummer';
import type { List } from '../../../java/util/List';

export class AdressbuchKontakt extends AdressbuchEintrag {

	/**
	 * Der Nachname des Kontakts.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülerdatensatzes.
	 */
	public vorname : string = "";

	/**
	 * Der Straßenname des Kontakts.
	 */
	public strassenname : string | null = null;

	/**
	 * Die Hausnummer des Kontakts.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz des Kontakts.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Die Postleitzahl des Kontakts.
	 */
	public plz : string | null = null;

	/**
	 * Der Ort des Kontakts.
	 */
	public ort : string | null = null;

	/**
	 * Die Telefonnummern des Kontakts.
	 */
	public telefonnummern : List<Telefonnummer> = new ArrayList<Telefonnummer>();

	/**
	 * Die Mailadresse des Kontakts.
	 */
	public email : string | null = null;

	/**
	 * Die Webadresse des Kontakts
	 */
	public webAdresse : string | null = "";

	/**
	 * Die Kategorien dieses Kontakts
	 */
	public kategorien : List<string> | null = new ArrayList<string>();

	/**
	 *  Die Organisation dieses Kontakts
	 */
	public organisation : string | null = null;

	/**
	 *  Die Rolle innerhalb der Organisation dieses Kontakts
	 */
	public rolle : string | null = null;

	/**
	 * Die ID des Adressbuchkontakts des Kinds
	 */
	public idKind : string | null = null;

	/**
	 * Die ID des Adressbuchkontakts der Eltern
	 */
	public idEltern : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.AdressbuchKontakt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchKontakt', 'de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchKontakt {
		const obj = JSON.parse(json);
		const result = new AdressbuchKontakt();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.adressbuchId === undefined)
			 throw new Error('invalid json format, missing attribute adressbuchId');
		result.adressbuchId = obj.adressbuchId;
		if (obj.uri === undefined)
			 throw new Error('invalid json format, missing attribute uri');
		result.uri = obj.uri;
		if (obj.version === undefined)
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.nachname === undefined)
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		if ((obj.telefonnummern !== undefined) && (obj.telefonnummern !== null)) {
			for (const elem of obj.telefonnummern) {
				result.telefonnummern?.add(Telefonnummer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.webAdresse = (obj.webAdresse === undefined) ? null : obj.webAdresse === null ? null : obj.webAdresse;
		if ((obj.kategorien !== undefined) && (obj.kategorien !== null)) {
			for (const elem of obj.kategorien) {
				result.kategorien?.add(elem);
			}
		}
		result.organisation = (obj.organisation === undefined) ? null : obj.organisation === null ? null : obj.organisation;
		result.rolle = (obj.rolle === undefined) ? null : obj.rolle === null ? null : obj.rolle;
		result.idKind = (obj.idKind === undefined) ? null : obj.idKind === null ? null : obj.idKind;
		result.idEltern = (obj.idEltern === undefined) ? null : obj.idEltern === null ? null : obj.idEltern;
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchKontakt) : string {
		let result = '{';
		result += '"id" : ' + JSON.stringify(obj.id!) + ',';
		result += '"adressbuchId" : ' + JSON.stringify(obj.adressbuchId!) + ',';
		result += '"uri" : ' + JSON.stringify(obj.uri!) + ',';
		result += '"version" : ' + JSON.stringify(obj.version!) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		if (!obj.telefonnummern) {
			result += '"telefonnummern" : []';
		} else {
			result += '"telefonnummern" : [ ';
			for (let i = 0; i < obj.telefonnummern.size(); i++) {
				const elem = obj.telefonnummern.get(i);
				result += Telefonnummer.transpilerToJSON(elem);
				if (i < obj.telefonnummern.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		if (!obj.kategorien) {
			result += '"kategorien" : []';
		} else {
			result += '"kategorien" : [ ';
			for (let i = 0; i < obj.kategorien.size(); i++) {
				const elem = obj.kategorien.get(i);
				result += '"' + elem + '"';
				if (i < obj.kategorien.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"organisation" : ' + ((!obj.organisation) ? 'null' : JSON.stringify(obj.organisation)) + ',';
		result += '"rolle" : ' + ((!obj.rolle) ? 'null' : JSON.stringify(obj.rolle)) + ',';
		result += '"idKind" : ' + ((!obj.idKind) ? 'null' : JSON.stringify(obj.idKind)) + ',';
		result += '"idEltern" : ' + ((!obj.idEltern) ? 'null' : JSON.stringify(obj.idEltern)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AdressbuchKontakt>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + JSON.stringify(obj.id!) + ',';
		}
		if (obj.adressbuchId !== undefined) {
			result += '"adressbuchId" : ' + JSON.stringify(obj.adressbuchId!) + ',';
		}
		if (obj.uri !== undefined) {
			result += '"uri" : ' + JSON.stringify(obj.uri!) + ',';
		}
		if (obj.version !== undefined) {
			result += '"version" : ' + JSON.stringify(obj.version!) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.hausnummerZusatz !== undefined) {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.telefonnummern !== undefined) {
			if (!obj.telefonnummern) {
				result += '"telefonnummern" : []';
			} else {
				result += '"telefonnummern" : [ ';
				for (let i = 0; i < obj.telefonnummern.size(); i++) {
					const elem = obj.telefonnummern.get(i);
					result += Telefonnummer.transpilerToJSON(elem);
					if (i < obj.telefonnummern.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.webAdresse !== undefined) {
			result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		}
		if (obj.kategorien !== undefined) {
			if (!obj.kategorien) {
				result += '"kategorien" : []';
			} else {
				result += '"kategorien" : [ ';
				for (let i = 0; i < obj.kategorien.size(); i++) {
					const elem = obj.kategorien.get(i);
					result += '"' + elem + '"';
					if (i < obj.kategorien.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.organisation !== undefined) {
			result += '"organisation" : ' + ((!obj.organisation) ? 'null' : JSON.stringify(obj.organisation)) + ',';
		}
		if (obj.rolle !== undefined) {
			result += '"rolle" : ' + ((!obj.rolle) ? 'null' : JSON.stringify(obj.rolle)) + ',';
		}
		if (obj.idKind !== undefined) {
			result += '"idKind" : ' + ((!obj.idKind) ? 'null' : JSON.stringify(obj.idKind)) + ',';
		}
		if (obj.idEltern !== undefined) {
			result += '"idEltern" : ' + ((!obj.idEltern) ? 'null' : JSON.stringify(obj.idEltern)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_AdressbuchKontakt(obj : unknown) : AdressbuchKontakt {
	return obj as AdressbuchKontakt;
}
