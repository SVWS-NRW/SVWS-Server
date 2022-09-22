import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { AdressbuchEintrag, cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchEintrag } from '../../../core/data/adressbuch/AdressbuchEintrag';
import { Telefonnummer, cast_de_nrw_schule_svws_core_data_adressbuch_Telefonnummer } from '../../../core/data/adressbuch/Telefonnummer';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class AdressbuchKontakt extends AdressbuchEintrag {

	public nachname : String = "";

	public zusatzNachname : String = "";

	public vorname : String = "";

	public strassenname : String | null = null;

	public hausnummer : String | null = null;

	public hausnummerZusatz : String | null = null;

	public plz : String | null = null;

	public ort : String | null = null;

	public telefonnummern : List<Telefonnummer> = new Vector();

	public email : String | null = null;

	public webAdresse : String | null = "";

	public kategorien : List<String | null> | null = new Vector<String | null>();

	public organisation : String | null = null;

	public rolle : String | null = null;

	public idKind : String | null = null;

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
		result.nachname = obj.nachname;
		if (typeof obj.zusatzNachname === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzNachname');
		result.zusatzNachname = obj.zusatzNachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz;
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort;
		if (!!obj.telefonnummern) {
			for (let elem of obj.telefonnummern) {
				result.telefonnummern?.add(Telefonnummer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.email = typeof obj.email === "undefined" ? null : obj.email;
		result.webAdresse = typeof obj.webAdresse === "undefined" ? null : obj.webAdresse;
		if (!!obj.kategorien) {
			for (let elem of obj.kategorien) {
				result.kategorien?.add(elem);
			}
		}
		result.organisation = typeof obj.organisation === "undefined" ? null : obj.organisation;
		result.rolle = typeof obj.rolle === "undefined" ? null : obj.rolle;
		result.idKind = typeof obj.idKind === "undefined" ? null : obj.idKind;
		result.idEltern = typeof obj.idEltern === "undefined" ? null : obj.idEltern;
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
