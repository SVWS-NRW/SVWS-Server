import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchuleAbschnitte, cast_de_nrw_schule_svws_core_data_schule_SchuleAbschnitte } from '../../../core/data/schule/SchuleAbschnitte';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Schuljahresabschnitt, cast_de_nrw_schule_svws_core_data_schule_Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuleStammdaten extends JavaObject {

	/**
	 * Die eindeutige Schulnummer der Schule 
	 */
	public schulNr : number = 0;

	/**
	 * Die Schulform der Schule 
	 */
	public schulform : String = "";

	/**
	 * Der erste Teil (von dreien) der Bezeichnung der Schule 
	 */
	public bezeichnung1 : String = "";

	/**
	 * Der zweite Teil (von dreien) der Bezeichnung der Schule 
	 */
	public bezeichnung2 : String | null = null;

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule 
	 */
	public bezeichnung3 : String | null = null;

	/**
	 * Der Straßenname der Straße in der die Schule liegt. 
	 */
	public strassenname : String | null = null;

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt. 
	 */
	public hausnummer : String | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt. 
	 */
	public hausnummerZusatz : String | null = null;

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt. 
	 */
	public plz : String | null = null;

	/**
	 * Der Ort in dem die Schule liegt. 
	 */
	public ort : String | null = null;

	/**
	 * Die Telefonnummer der Schule. 
	 */
	public telefon : String | null = null;

	/**
	 * Die Faxnummer der Schule. 
	 */
	public fax : String | null = null;

	/**
	 * Die Mailadresse der Schule. 
	 */
	public email : String | null = null;

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name) 
	 */
	public webAdresse : String | null = null;

	/**
	 * Die ID des Schuljahresabschnittes, in welchem sich die Schule befindet. 
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Die Anzahl der Abschnitte pro Jahrgangsstufe. 
	 */
	public anzJGS_Jahr : number = 0;

	/**
	 * Die Informationen zu den Abschnitten pro Jahr. (meist Haljahre (2) oder Quartale (4) 
	 */
	public schuleAbschnitte : SchuleAbschnitte = new SchuleAbschnitte();

	/**
	 * Die Dauer einer Unterrichsteinheit in Minuten. 
	 */
	public dauerUnterrichtseinheit : number = 0;

	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind. 
	 */
	public readonly abschnitte : Vector<Schuljahresabschnitt> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchuleStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuleStammdaten {
		const obj = JSON.parse(json);
		const result = new SchuleStammdaten();
		if (typeof obj.schulNr === "undefined")
			 throw new Error('invalid json format, missing attribute schulNr');
		result.schulNr = obj.schulNr;
		if (typeof obj.schulform === "undefined")
			 throw new Error('invalid json format, missing attribute schulform');
		result.schulform = String(obj.schulform);
		if (typeof obj.bezeichnung1 === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung1');
		result.bezeichnung1 = String(obj.bezeichnung1);
		result.bezeichnung2 = typeof obj.bezeichnung2 === "undefined" ? null : obj.bezeichnung2 === null ? null : String(obj.bezeichnung2);
		result.bezeichnung3 = typeof obj.bezeichnung3 === "undefined" ? null : obj.bezeichnung3 === null ? null : String(obj.bezeichnung3);
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : String(obj.strassenname);
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : String(obj.hausnummer);
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : String(obj.hausnummerZusatz);
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : String(obj.plz);
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : String(obj.ort);
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : String(obj.telefon);
		result.fax = typeof obj.fax === "undefined" ? null : obj.fax === null ? null : String(obj.fax);
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : String(obj.email);
		result.webAdresse = typeof obj.webAdresse === "undefined" ? null : obj.webAdresse === null ? null : String(obj.webAdresse);
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.anzJGS_Jahr === "undefined")
			 throw new Error('invalid json format, missing attribute anzJGS_Jahr');
		result.anzJGS_Jahr = obj.anzJGS_Jahr;
		if (typeof obj.schuleAbschnitte === "undefined")
			 throw new Error('invalid json format, missing attribute schuleAbschnitte');
		result.schuleAbschnitte = SchuleAbschnitte.transpilerFromJSON(JSON.stringify(obj.schuleAbschnitte));
		if (typeof obj.dauerUnterrichtseinheit === "undefined")
			 throw new Error('invalid json format, missing attribute dauerUnterrichtseinheit');
		result.dauerUnterrichtseinheit = obj.dauerUnterrichtseinheit;
		if (!!obj.abschnitte) {
			for (let elem of obj.abschnitte) {
				result.abschnitte?.add(Schuljahresabschnitt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuleStammdaten) : string {
		let result = '{';
		result += '"schulNr" : ' + obj.schulNr + ',';
		result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		result += '"bezeichnung1" : ' + '"' + obj.bezeichnung1.valueOf() + '"' + ',';
		result += '"bezeichnung2" : ' + ((!obj.bezeichnung2) ? 'null' : '"' + obj.bezeichnung2.valueOf() + '"') + ',';
		result += '"bezeichnung3" : ' + ((!obj.bezeichnung3) ? 'null' : '"' + obj.bezeichnung3.valueOf() + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : '"' + obj.ort.valueOf() + '"') + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax.valueOf() + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse.valueOf() + '"') + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"anzJGS_Jahr" : ' + obj.anzJGS_Jahr + ',';
		result += '"schuleAbschnitte" : ' + SchuleAbschnitte.transpilerToJSON(obj.schuleAbschnitte) + ',';
		result += '"dauerUnterrichtseinheit" : ' + obj.dauerUnterrichtseinheit + ',';
		if (!obj.abschnitte) {
			result += '"abschnitte" : []';
		} else {
			result += '"abschnitte" : [ ';
			for (let i : number = 0; i < obj.abschnitte.size(); i++) {
				let elem = obj.abschnitte.get(i);
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

	public static transpilerToJSONPatch(obj : Partial<SchuleStammdaten>) : string {
		let result = '{';
		if (typeof obj.schulNr !== "undefined") {
			result += '"schulNr" : ' + obj.schulNr + ',';
		}
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung1 !== "undefined") {
			result += '"bezeichnung1" : ' + '"' + obj.bezeichnung1.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung2 !== "undefined") {
			result += '"bezeichnung2" : ' + ((!obj.bezeichnung2) ? 'null' : '"' + obj.bezeichnung2.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung3 !== "undefined") {
			result += '"bezeichnung3" : ' + ((!obj.bezeichnung3) ? 'null' : '"' + obj.bezeichnung3.valueOf() + '"') + ',';
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
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax.valueOf() + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		}
		if (typeof obj.webAdresse !== "undefined") {
			result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse.valueOf() + '"') + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.anzJGS_Jahr !== "undefined") {
			result += '"anzJGS_Jahr" : ' + obj.anzJGS_Jahr + ',';
		}
		if (typeof obj.schuleAbschnitte !== "undefined") {
			result += '"schuleAbschnitte" : ' + SchuleAbschnitte.transpilerToJSON(obj.schuleAbschnitte) + ',';
		}
		if (typeof obj.dauerUnterrichtseinheit !== "undefined") {
			result += '"dauerUnterrichtseinheit" : ' + obj.dauerUnterrichtseinheit + ',';
		}
		if (typeof obj.abschnitte !== "undefined") {
			if (!obj.abschnitte) {
				result += '"abschnitte" : []';
			} else {
				result += '"abschnitte" : [ ';
				for (let i : number = 0; i < obj.abschnitte.size(); i++) {
					let elem = obj.abschnitte.get(i);
					result += Schuljahresabschnitt.transpilerToJSON(elem);
					if (i < obj.abschnitte.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_SchuleStammdaten(obj : unknown) : SchuleStammdaten {
	return obj as SchuleStammdaten;
}
