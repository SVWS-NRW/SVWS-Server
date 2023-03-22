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
	public schulform : string = "";

	/**
	 * Der erste Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung1 : string = "";

	/**
	 * Der zweite Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung2 : string | null = null;

	/**
	 * Der dritte Teil (von dreien) der Bezeichnung der Schule
	 */
	public bezeichnung3 : string | null = null;

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 */
	public strassenname : string | null = null;

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 */
	public plz : string | null = null;

	/**
	 * Der Ort in dem die Schule liegt.
	 */
	public ort : string | null = null;

	/**
	 * Die Telefonnummer der Schule.
	 */
	public telefon : string | null = null;

	/**
	 * Die Faxnummer der Schule.
	 */
	public fax : string | null = null;

	/**
	 * Die Mailadresse der Schule.
	 */
	public email : string | null = null;

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 */
	public webAdresse : string | null = null;

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
		result.schulform = obj.schulform;
		if (typeof obj.bezeichnung1 === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung1');
		result.bezeichnung1 = obj.bezeichnung1;
		result.bezeichnung2 = typeof obj.bezeichnung2 === "undefined" ? null : obj.bezeichnung2 === null ? null : obj.bezeichnung2;
		result.bezeichnung3 = typeof obj.bezeichnung3 === "undefined" ? null : obj.bezeichnung3 === null ? null : obj.bezeichnung3;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : obj.plz;
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : obj.ort;
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : obj.telefon;
		result.fax = typeof obj.fax === "undefined" ? null : obj.fax === null ? null : obj.fax;
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : obj.email;
		result.webAdresse = typeof obj.webAdresse === "undefined" ? null : obj.webAdresse === null ? null : obj.webAdresse;
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
		if (!(obj.abschnitte === undefined)) {
			for (const elem of obj.abschnitte) {
				result.abschnitte?.add(Schuljahresabschnitt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuleStammdaten) : string {
		let result = '{';
		result += '"schulNr" : ' + obj.schulNr + ',';
		result += '"schulform" : ' + '"' + obj.schulform! + '"' + ',';
		result += '"bezeichnung1" : ' + '"' + obj.bezeichnung1! + '"' + ',';
		result += '"bezeichnung2" : ' + ((!obj.bezeichnung2) ? 'null' : '"' + obj.bezeichnung2 + '"') + ',';
		result += '"bezeichnung3" : ' + ((!obj.bezeichnung3) ? 'null' : '"' + obj.bezeichnung3 + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz + '"') + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz + '"') + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : '"' + obj.ort + '"') + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon + '"') + ',';
		result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email + '"') + ',';
		result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse + '"') + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"anzJGS_Jahr" : ' + obj.anzJGS_Jahr + ',';
		result += '"schuleAbschnitte" : ' + SchuleAbschnitte.transpilerToJSON(obj.schuleAbschnitte) + ',';
		result += '"dauerUnterrichtseinheit" : ' + obj.dauerUnterrichtseinheit + ',';
		if (!obj.abschnitte) {
			result += '"abschnitte" : []';
		} else {
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

	public static transpilerToJSONPatch(obj : Partial<SchuleStammdaten>) : string {
		let result = '{';
		if (typeof obj.schulNr !== "undefined") {
			result += '"schulNr" : ' + obj.schulNr + ',';
		}
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + '"' + obj.schulform + '"' + ',';
		}
		if (typeof obj.bezeichnung1 !== "undefined") {
			result += '"bezeichnung1" : ' + '"' + obj.bezeichnung1 + '"' + ',';
		}
		if (typeof obj.bezeichnung2 !== "undefined") {
			result += '"bezeichnung2" : ' + ((!obj.bezeichnung2) ? 'null' : '"' + obj.bezeichnung2 + '"') + ',';
		}
		if (typeof obj.bezeichnung3 !== "undefined") {
			result += '"bezeichnung3" : ' + ((!obj.bezeichnung3) ? 'null' : '"' + obj.bezeichnung3 + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer + '"') + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz + '"') + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz + '"') + ',';
		}
		if (typeof obj.ort !== "undefined") {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : '"' + obj.ort + '"') + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon + '"') + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email + '"') + ',';
		}
		if (typeof obj.webAdresse !== "undefined") {
			result += '"webAdresse" : ' + ((!obj.webAdresse) ? 'null' : '"' + obj.webAdresse + '"') + ',';
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
				for (let i = 0; i < obj.abschnitte.size(); i++) {
					const elem = obj.abschnitte.get(i);
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
