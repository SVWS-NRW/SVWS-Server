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
	 * Ggf. Zusatz zum Nachnamen des Schülerdatensatzes.
	 */
	public zusatzNachname : string = "";

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
	public telefonnummern : List<Telefonnummer> = new ArrayList();

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
	public kategorien : List<string | null> | null = new ArrayList();

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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchKontakt', 'de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchKontakt {
		const obj = JSON.parse(json);
		const result = new AdressbuchKontakt();
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.zusatzNachname === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzNachname');
		result.zusatzNachname = obj.zusatzNachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : obj.plz;
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : obj.ort;
		if ((obj.telefonnummern !== undefined) && (obj.telefonnummern !== null)) {
			for (const elem of obj.telefonnummern) {
				result.telefonnummern?.add(Telefonnummer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : obj.email;
		result.webAdresse = typeof obj.webAdresse === "undefined" ? null : obj.webAdresse === null ? null : obj.webAdresse;
		if ((obj.kategorien !== undefined) && (obj.kategorien !== null)) {
			for (const elem of obj.kategorien) {
				result.kategorien?.add(elem === null ? null : elem);
			}
		}
		result.organisation = typeof obj.organisation === "undefined" ? null : obj.organisation === null ? null : obj.organisation;
		result.rolle = typeof obj.rolle === "undefined" ? null : obj.rolle === null ? null : obj.rolle;
		result.idKind = typeof obj.idKind === "undefined" ? null : obj.idKind === null ? null : obj.idKind;
		result.idEltern = typeof obj.idEltern === "undefined" ? null : obj.idEltern === null ? null : obj.idEltern;
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchKontakt) : string {
		let result = '{';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"zusatzNachname" : ' + JSON.stringify(obj.zusatzNachname!) + ',';
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
				result += (elem == null) ? null : '"' + elem + '"';
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
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (typeof obj.zusatzNachname !== "undefined") {
			result += '"zusatzNachname" : ' + JSON.stringify(obj.zusatzNachname!) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (typeof obj.ort !== "undefined") {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (typeof obj.telefonnummern !== "undefined") {
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
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (typeof obj.webAdresse !== "undefined") {
			result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : JSON.stringify(obj.webAdresse)) + ',';
		}
		if (typeof obj.kategorien !== "undefined") {
			if (!obj.kategorien) {
				result += '"kategorien" : []';
			} else {
				result += '"kategorien" : [ ';
				for (let i = 0; i < obj.kategorien.size(); i++) {
					const elem = obj.kategorien.get(i);
					result += (elem == null) ? null : '"' + elem + '"';
					if (i < obj.kategorien.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.organisation !== "undefined") {
			result += '"organisation" : ' + ((!obj.organisation) ? 'null' : JSON.stringify(obj.organisation)) + ',';
		}
		if (typeof obj.rolle !== "undefined") {
			result += '"rolle" : ' + ((!obj.rolle) ? 'null' : JSON.stringify(obj.rolle)) + ',';
		}
		if (typeof obj.idKind !== "undefined") {
			result += '"idKind" : ' + ((!obj.idKind) ? 'null' : JSON.stringify(obj.idKind)) + ',';
		}
		if (typeof obj.idEltern !== "undefined") {
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
