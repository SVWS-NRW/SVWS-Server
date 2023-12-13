import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonalabschnittsdatenLehrerfunktion } from '../../../core/data/lehrer/LehrerPersonalabschnittsdatenLehrerfunktion';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../core/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { List } from '../../../java/util/List';

export class LehrerPersonalabschnittsdaten extends JavaObject {

	/**
	 * Die ID des Abschnitts für den Lehrer in der Datenbank.
	 */
	public id : number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Abschnittdaten gehören.
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Das Pflichtstundensoll des Lehrers.
	 */
	public pflichtstundensoll : number | null = null;

	/**
	 * Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.
	 */
	public rechtsverhaeltnis : string | null = null;

	/**
	 * Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.
	 */
	public beschaeftigungsart : string | null = null;

	/**
	 * [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Core-Type {@link LehrerEinsatzstatus}
	 */
	public einsatzstatus : string | null = null;

	/**
	 * Die Schulnummer der Stammschule, sofern diese abweicht.
	 */
	public stammschulnummer : string | null = null;

	/**
	 * Die allgemeinen Anrechnungsstunden, die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly anrechnungen : List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList();

	/**
	 * Die Stunden, welche Mehrarbeitsgründe haben, dem Pflichtstundensoll hinzuzufügen sind und die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly mehrleistung : List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList();

	/**
	 * Die Stunden, welche Minderarbeitsgründe haben, dem Pflichtstundensoll wegzunehmen sind und die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly minderleistung : List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList();

	/**
	 * Die schulspezifischen-Funktionen, die einem Lehrer in dem Abschnitt der Abschnittsdaten zugeordnet sind.
	 */
	public readonly funktionen : List<LehrerPersonalabschnittsdatenLehrerfunktion> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdaten {
		const obj = JSON.parse(json);
		const result = new LehrerPersonalabschnittsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.pflichtstundensoll = typeof obj.pflichtstundensoll === "undefined" ? null : obj.pflichtstundensoll === null ? null : obj.pflichtstundensoll;
		result.rechtsverhaeltnis = typeof obj.rechtsverhaeltnis === "undefined" ? null : obj.rechtsverhaeltnis === null ? null : obj.rechtsverhaeltnis;
		result.beschaeftigungsart = typeof obj.beschaeftigungsart === "undefined" ? null : obj.beschaeftigungsart === null ? null : obj.beschaeftigungsart;
		result.einsatzstatus = typeof obj.einsatzstatus === "undefined" ? null : obj.einsatzstatus === null ? null : obj.einsatzstatus;
		result.stammschulnummer = typeof obj.stammschulnummer === "undefined" ? null : obj.stammschulnummer === null ? null : obj.stammschulnummer;
		if ((obj.anrechnungen !== undefined) && (obj.anrechnungen !== null)) {
			for (const elem of obj.anrechnungen) {
				result.anrechnungen?.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.mehrleistung !== undefined) && (obj.mehrleistung !== null)) {
			for (const elem of obj.mehrleistung) {
				result.mehrleistung?.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.minderleistung !== undefined) && (obj.minderleistung !== null)) {
			for (const elem of obj.minderleistung) {
				result.minderleistung?.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.funktionen !== undefined) && (obj.funktionen !== null)) {
			for (const elem of obj.funktionen) {
				result.funktionen?.add(LehrerPersonalabschnittsdatenLehrerfunktion.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		if (!obj.anrechnungen) {
			result += '"anrechnungen" : []';
		} else {
			result += '"anrechnungen" : [ ';
			for (let i = 0; i < obj.anrechnungen.size(); i++) {
				const elem = obj.anrechnungen.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.anrechnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.mehrleistung) {
			result += '"mehrleistung" : []';
		} else {
			result += '"mehrleistung" : [ ';
			for (let i = 0; i < obj.mehrleistung.size(); i++) {
				const elem = obj.mehrleistung.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.mehrleistung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.minderleistung) {
			result += '"minderleistung" : []';
		} else {
			result += '"minderleistung" : [ ';
			for (let i = 0; i < obj.minderleistung.size(); i++) {
				const elem = obj.minderleistung.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.minderleistung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.funktionen) {
			result += '"funktionen" : []';
		} else {
			result += '"funktionen" : [ ';
			for (let i = 0; i < obj.funktionen.size(); i++) {
				const elem = obj.funktionen.get(i);
				result += LehrerPersonalabschnittsdatenLehrerfunktion.transpilerToJSON(elem);
				if (i < obj.funktionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.pflichtstundensoll !== "undefined") {
			result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		}
		if (typeof obj.rechtsverhaeltnis !== "undefined") {
			result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		}
		if (typeof obj.beschaeftigungsart !== "undefined") {
			result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		}
		if (typeof obj.einsatzstatus !== "undefined") {
			result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		}
		if (typeof obj.stammschulnummer !== "undefined") {
			result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		}
		if (typeof obj.anrechnungen !== "undefined") {
			if (!obj.anrechnungen) {
				result += '"anrechnungen" : []';
			} else {
				result += '"anrechnungen" : [ ';
				for (let i = 0; i < obj.anrechnungen.size(); i++) {
					const elem = obj.anrechnungen.get(i);
					result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
					if (i < obj.anrechnungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.mehrleistung !== "undefined") {
			if (!obj.mehrleistung) {
				result += '"mehrleistung" : []';
			} else {
				result += '"mehrleistung" : [ ';
				for (let i = 0; i < obj.mehrleistung.size(); i++) {
					const elem = obj.mehrleistung.get(i);
					result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
					if (i < obj.mehrleistung.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.minderleistung !== "undefined") {
			if (!obj.minderleistung) {
				result += '"minderleistung" : []';
			} else {
				result += '"minderleistung" : [ ';
				for (let i = 0; i < obj.minderleistung.size(); i++) {
					const elem = obj.minderleistung.get(i);
					result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
					if (i < obj.minderleistung.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.funktionen !== "undefined") {
			if (!obj.funktionen) {
				result += '"funktionen" : []';
			} else {
				result += '"funktionen" : [ ';
				for (let i = 0; i < obj.funktionen.size(); i++) {
					const elem = obj.funktionen.get(i);
					result += LehrerPersonalabschnittsdatenLehrerfunktion.transpilerToJSON(elem);
					if (i < obj.funktionen.size() - 1)
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

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdaten(obj : unknown) : LehrerPersonalabschnittsdaten {
	return obj as LehrerPersonalabschnittsdaten;
}
