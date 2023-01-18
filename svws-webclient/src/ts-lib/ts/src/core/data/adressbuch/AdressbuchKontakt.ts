import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { AdressbuchEintrag, cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { Telefonnummer, cast_de_nrw_schule_svws_core_data_adressbuch_Telefonnummer } from '../../../core/data/adressbuch/Telefonnummer';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class AdressbuchKontakt extends AdressbuchEintrag {

	/**
	 * Der Nachname des Kontakts. 
	 */
	public nachname : String = "";

	/**
	 * Ggf. Zusatz zum Nachnamen des Schülerdatensatzes. 
	 */
	public zusatzNachname : String = "";

	/**
	 * Der Vorname des Schülerdatensatzes. 
	 */
	public vorname : String = "";

	/**
	 * Der Straßenname des Kontakts. 
	 */
	public strassenname : String | null = null;

	/**
	 * Die Hausnummer des Kontakts. 
	 */
	public hausnummer : String | null = null;

	/**
	 * Ggf. der Hausnummerzusatz des Kontakts. 
	 */
	public hausnummerZusatz : String | null = null;

	/**
	 * Die Postleitzahl des Kontakts. 
	 */
	public plz : String | null = null;

	/**
	 * Der Ort des Kontakts. 
	 */
	public ort : String | null = null;

	/**
	 * Die Telefonnummern des Kontakts. 
	 */
	public telefonnummern : List<Telefonnummer> = new Vector();

	/**
	 * Die Mailadresse des Kontakts. 
	 */
	public email : String | null = null;

	/**
	 * Die Webadresse des Kontakts 
	 */
	public webAdresse : String | null = "";

	/**
	 * Die Kategorien dieses Kontakts 
	 */
	public kategorien : List<String | null> | null = new Vector();

	/**
	 *  Die Organisation dieses Kontakts 
	 */
	public organisation : String | null = null;

	/**
	 *  Die Rolle innerhalb der Organisation dieses Kontakts 
	 */
	public rolle : String | null = null;

	/**
	 * Die ID des Adressbuchkontakts des Kinds 
	 */
	public idKind : String | null = null;

	/**
	 * Die ID des Adressbuchkontakts der Eltern 
	 */
	public idEltern : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag', 'de.nrw.schule.svws.core.data.adressbuch.AdressbuchKontakt'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchKontakt {
		const obj = JSON.parse(json);
		const result = new AdressbuchKontakt();
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = String(obj.nachname);
		if (typeof obj.zusatzNachname === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzNachname');
		result.zusatzNachname = String(obj.zusatzNachname);
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = String(obj.vorname);
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : String(obj.strassenname);
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : String(obj.hausnummer);
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : String(obj.hausnummerZusatz);
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : String(obj.plz);
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : String(obj.ort);
		if (!!obj.telefonnummern) {
			for (let elem of obj.telefonnummern) {
				result.telefonnummern?.add(Telefonnummer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : String(obj.email);
		result.webAdresse = typeof obj.webAdresse === "undefined" ? null : obj.webAdresse === null ? null : String(obj.webAdresse);
		if (!!obj.kategorien) {
			for (let elem of obj.kategorien) {
				result.kategorien?.add(elem === null ? null : String(elem));
			}
		}
		result.organisation = typeof obj.organisation === "undefined" ? null : obj.organisation === null ? null : String(obj.organisation);
		result.rolle = typeof obj.rolle === "undefined" ? null : obj.rolle === null ? null : String(obj.rolle);
		result.idKind = typeof obj.idKind === "undefined" ? null : obj.idKind === null ? null : String(obj.idKind);
		result.idEltern = typeof obj.idEltern === "undefined" ? null : obj.idEltern === null ? null : String(obj.idEltern);
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchKontakt) : string {
		let result = '{';
		result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		result += '"zusatzNachname" : ' + '"' + obj.zusatzNachname.valueOf() + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : '"' + obj.ort.valueOf() + '"') + ',';
		if (!obj.telefonnummern) {
			result += '"telefonnummern" : []';
		} else {
			result += '"telefonnummern" : [ ';
			for (let i : number = 0; i < obj.telefonnummern.size(); i++) {
				let elem = obj.telefonnummern.get(i);
				result += Telefonnummer.transpilerToJSON(elem);
				if (i < obj.telefonnummern.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse.valueOf() + '"') + ',';
		if (!obj.kategorien) {
			result += '"kategorien" : []';
		} else {
			result += '"kategorien" : [ ';
			for (let i : number = 0; i < obj.kategorien.size(); i++) {
				let elem = obj.kategorien.get(i);
				result += (elem == null) ? null : '"' + elem + '"';
				if (i < obj.kategorien.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"organisation" : ' + ((!obj.organisation) ? 'null' : '"' + obj.organisation.valueOf() + '"') + ',';
		result += '"rolle" : ' + ((!obj.rolle) ? 'null' : '"' + obj.rolle.valueOf() + '"') + ',';
		result += '"idKind" : ' + ((!obj.idKind) ? 'null' : '"' + obj.idKind.valueOf() + '"') + ',';
		result += '"idEltern" : ' + ((!obj.idEltern) ? 'null' : '"' + obj.idEltern.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AdressbuchKontakt>) : string {
		let result = '{';
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		}
		if (typeof obj.zusatzNachname !== "undefined") {
			result += '"zusatzNachname" : ' + '"' + obj.zusatzNachname.valueOf() + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		}
		if (typeof obj.ort !== "undefined") {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : '"' + obj.ort.valueOf() + '"') + ',';
		}
		if (typeof obj.telefonnummern !== "undefined") {
			if (!obj.telefonnummern) {
				result += '"telefonnummern" : []';
			} else {
				result += '"telefonnummern" : [ ';
				for (let i : number = 0; i < obj.telefonnummern.size(); i++) {
					let elem = obj.telefonnummern.get(i);
					result += Telefonnummer.transpilerToJSON(elem);
					if (i < obj.telefonnummern.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		}
		if (typeof obj.webAdresse !== "undefined") {
			result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse.valueOf() + '"') + ',';
		}
		if (typeof obj.kategorien !== "undefined") {
			if (!obj.kategorien) {
				result += '"kategorien" : []';
			} else {
				result += '"kategorien" : [ ';
				for (let i : number = 0; i < obj.kategorien.size(); i++) {
					let elem = obj.kategorien.get(i);
					result += (elem == null) ? null : '"' + elem + '"';
					if (i < obj.kategorien.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.organisation !== "undefined") {
			result += '"organisation" : ' + ((!obj.organisation) ? 'null' : '"' + obj.organisation.valueOf() + '"') + ',';
		}
		if (typeof obj.rolle !== "undefined") {
			result += '"rolle" : ' + ((!obj.rolle) ? 'null' : '"' + obj.rolle.valueOf() + '"') + ',';
		}
		if (typeof obj.idKind !== "undefined") {
			result += '"idKind" : ' + ((!obj.idKind) ? 'null' : '"' + obj.idKind.valueOf() + '"') + ',';
		}
		if (typeof obj.idEltern !== "undefined") {
			result += '"idEltern" : ' + ((!obj.idEltern) ? 'null' : '"' + obj.idEltern.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchKontakt(obj : unknown) : AdressbuchKontakt {
	return obj as AdressbuchKontakt;
}
