import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulleitung } from '../../../asd/data/schule/Schulleitung';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class LehrerStammdaten extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Personals-Typs des Lehrers.
	 */
	public personalTyp : string = "";

	/**
	 * Ggf. die Anrede des Lehrers.
	 */
	public anrede : string | null = null;

	/**
	 * Ggf. ein akademischer Grad des Lehrers.
	 */
	public titel : string | null = null;

	/**
	 * Ggf. die Amtsbezeichnung des Lehrers.
	 */
	public amtsbezeichnung : string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname : string = "";

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht : number = 0;

	/**
	 * Das Geburtsdatum des Lehrers.
	 */
	public geburtsdatum : string | null = null;

	/**
	 * Ggf. die ID für die Staatsangehörigkeit des Lehrers.
	 */
	public staatsangehoerigkeitID : string | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Lehrers.
	 */
	public strassenname : string | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Ggf. die ID des Wohnortes des Lehrers.
	 */
	public wohnortID : number | null = null;

	/**
	 * Ggf. die ID des Ortsteils im Wohnort des Lehrers.
	 */
	public ortsteilID : number | null = null;

	/**
	 * Ggf. die Telefonnummer des Lehrers.
	 */
	public telefon : string | null = null;

	/**
	 * Ggf. die Mobilnummer des Lehrers.
	 */
	public telefonMobil : string | null = null;

	/**
	 * Ggf. die private Email-Adresse des Lehrers.
	 */
	public emailPrivat : string | null = null;

	/**
	 * Ggf. die dienstliche Email-Adresse des Lehrers.
	 */
	public emailDienstlich : string | null = null;

	/**
	 * Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)
	 */
	public foto : string | null = null;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Gibt an, ob der Eintrag für die Schulstatistik relevant ist oder nicht.
	 */
	public istRelevantFuerStatistik : boolean = false;

	/**
	 * Die Liste der Schulleitungsfunktionen, welche der Schule Lehrer an der Schule hat oder hatte.
	 */
	public readonly leitungsfunktionen : List<Schulleitung> = new ArrayList<Schulleitung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerStammdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerStammdaten'].includes(name);
	}

	public static class = new Class<LehrerStammdaten>('de.svws_nrw.asd.data.lehrer.LehrerStammdaten');

	public static transpilerFromJSON(json : string): LehrerStammdaten {
		const obj = JSON.parse(json) as Partial<LehrerStammdaten>;
		const result = new LehrerStammdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.personalTyp === undefined)
			throw new Error('invalid json format, missing attribute personalTyp');
		result.personalTyp = obj.personalTyp;
		result.anrede = (obj.anrede === undefined) ? null : obj.anrede === null ? null : obj.anrede;
		result.titel = (obj.titel === undefined) ? null : obj.titel === null ? null : obj.titel;
		result.amtsbezeichnung = (obj.amtsbezeichnung === undefined) ? null : obj.amtsbezeichnung === null ? null : obj.amtsbezeichnung;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		result.staatsangehoerigkeitID = (obj.staatsangehoerigkeitID === undefined) ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.wohnortID = (obj.wohnortID === undefined) ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.ortsteilID = (obj.ortsteilID === undefined) ? null : obj.ortsteilID === null ? null : obj.ortsteilID;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.telefonMobil = (obj.telefonMobil === undefined) ? null : obj.telefonMobil === null ? null : obj.telefonMobil;
		result.emailPrivat = (obj.emailPrivat === undefined) ? null : obj.emailPrivat === null ? null : obj.emailPrivat;
		result.emailDienstlich = (obj.emailDienstlich === undefined) ? null : obj.emailDienstlich === null ? null : obj.emailDienstlich;
		result.foto = (obj.foto === undefined) ? null : obj.foto === null ? null : obj.foto;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.istRelevantFuerStatistik === undefined)
			throw new Error('invalid json format, missing attribute istRelevantFuerStatistik');
		result.istRelevantFuerStatistik = obj.istRelevantFuerStatistik;
		if (obj.leitungsfunktionen !== undefined) {
			for (const elem of obj.leitungsfunktionen) {
				result.leitungsfunktionen.add(Schulleitung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LehrerStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"personalTyp" : ' + JSON.stringify(obj.personalTyp) + ',';
		result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"amtsbezeichnung" : ' + ((obj.amtsbezeichnung === null) ? 'null' : JSON.stringify(obj.amtsbezeichnung)) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		result += '"ortsteilID" : ' + ((obj.ortsteilID === null) ? 'null' : obj.ortsteilID.toString()) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"telefonMobil" : ' + ((obj.telefonMobil === null) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		result += '"emailPrivat" : ' + ((obj.emailPrivat === null) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		result += '"emailDienstlich" : ' + ((obj.emailDienstlich === null) ? 'null' : JSON.stringify(obj.emailDienstlich)) + ',';
		result += '"foto" : ' + ((obj.foto === null) ? 'null' : JSON.stringify(obj.foto)) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istRelevantFuerStatistik" : ' + obj.istRelevantFuerStatistik.toString() + ',';
		result += '"leitungsfunktionen" : [ ';
		for (let i = 0; i < obj.leitungsfunktionen.size(); i++) {
			const elem = obj.leitungsfunktionen.get(i);
			result += Schulleitung.transpilerToJSON(elem);
			if (i < obj.leitungsfunktionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerStammdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.personalTyp !== undefined) {
			result += '"personalTyp" : ' + JSON.stringify(obj.personalTyp) + ',';
		}
		if (obj.anrede !== undefined) {
			result += '"anrede" : ' + ((obj.anrede === null) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		}
		if (obj.titel !== undefined) {
			result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (obj.amtsbezeichnung !== undefined) {
			result += '"amtsbezeichnung" : ' + ((obj.amtsbezeichnung === null) ? 'null' : JSON.stringify(obj.amtsbezeichnung)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.staatsangehoerigkeitID !== undefined) {
			result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
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
		if (obj.wohnortID !== undefined) {
			result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		}
		if (obj.ortsteilID !== undefined) {
			result += '"ortsteilID" : ' + ((obj.ortsteilID === null) ? 'null' : obj.ortsteilID.toString()) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.telefonMobil !== undefined) {
			result += '"telefonMobil" : ' + ((obj.telefonMobil === null) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		}
		if (obj.emailPrivat !== undefined) {
			result += '"emailPrivat" : ' + ((obj.emailPrivat === null) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		}
		if (obj.emailDienstlich !== undefined) {
			result += '"emailDienstlich" : ' + ((obj.emailDienstlich === null) ? 'null' : JSON.stringify(obj.emailDienstlich)) + ',';
		}
		if (obj.foto !== undefined) {
			result += '"foto" : ' + ((obj.foto === null) ? 'null' : JSON.stringify(obj.foto)) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.istRelevantFuerStatistik !== undefined) {
			result += '"istRelevantFuerStatistik" : ' + obj.istRelevantFuerStatistik.toString() + ',';
		}
		if (obj.leitungsfunktionen !== undefined) {
			result += '"leitungsfunktionen" : [ ';
			for (let i = 0; i < obj.leitungsfunktionen.size(); i++) {
				const elem = obj.leitungsfunktionen.get(i);
				result += Schulleitung.transpilerToJSON(elem);
				if (i < obj.leitungsfunktionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerStammdaten(obj : unknown) : LehrerStammdaten {
	return obj as LehrerStammdaten;
}
