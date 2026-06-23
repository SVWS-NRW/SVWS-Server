import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { LehrerFunktion } from '../../../asd/data/lehrer/LehrerFunktion';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class LehrerPersonalabschnittsdaten extends JavaObject {

	/**
	 * Die ID des Abschnitts für den Lehrer in der Datenbank.
	 */
	public id: number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer: number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Abschnittdaten gehören.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Das Pflichtstundensoll des Lehrers.
	 */
	public pflichtstundensoll: number | null = null;

	/**
	 * Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.
	 */
	public idRechtsverhaeltnis: number | null = null;

	/**
	 * Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.
	 */
	public idBeschaeftigungsart: number | null = null;

	/**
	 * [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig)
	 */
	public idEinsatzstatus: number | null = null;

	/**
	 * Die Schulnummer der Stammschule, sofern diese abweicht.
	 */
	public stammschulnummer: string | null = null;

	/**
	 * Die allgemeinen Anrechnungsstunden, die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly anrechnungen: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList<LehrerPersonalabschnittsdatenAnrechnungsstunden>();

	/**
	 * Die Stunden, welche Mehrarbeitsgründe haben, dem Pflichtstundensoll hinzuzufügen sind und die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly mehrleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList<LehrerPersonalabschnittsdatenAnrechnungsstunden>();

	/**
	 * Die Stunden, welche Minderarbeitsgründe haben, dem Pflichtstundensoll wegzunehmen sind und die den Abschnittsdaten des Lehrers zugeordnet sind.
	 */
	public readonly minderleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> = new ArrayList<LehrerPersonalabschnittsdatenAnrechnungsstunden>();

	/**
	 * Die schulspezifischen-Funktionen, die einem Lehrer in dem Abschnitt der Abschnittsdaten zugeordnet sind.
	 */
	public readonly funktionen: List<LehrerFunktion> = new ArrayList<LehrerFunktion>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten'].includes(name);
	}

	public static readonly class = new Class<LehrerPersonalabschnittsdaten>('de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten');

	public static transpilerFromJSON(json: string): LehrerPersonalabschnittsdaten {
		const obj = JSON.parse(json) as Partial<LehrerPersonalabschnittsdaten>;
		const result = new LehrerPersonalabschnittsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.pflichtstundensoll = (obj.pflichtstundensoll === undefined) ? null : obj.pflichtstundensoll === null ? null : obj.pflichtstundensoll;
		result.idRechtsverhaeltnis = (obj.idRechtsverhaeltnis === undefined) ? null : obj.idRechtsverhaeltnis === null ? null : obj.idRechtsverhaeltnis;
		result.idBeschaeftigungsart = (obj.idBeschaeftigungsart === undefined) ? null : obj.idBeschaeftigungsart === null ? null : obj.idBeschaeftigungsart;
		result.idEinsatzstatus = (obj.idEinsatzstatus === undefined) ? null : obj.idEinsatzstatus === null ? null : obj.idEinsatzstatus;
		result.stammschulnummer = (obj.stammschulnummer === undefined) ? null : obj.stammschulnummer === null ? null : obj.stammschulnummer;
		if (obj.anrechnungen !== undefined) {
			for (const elem of obj.anrechnungen) {
				result.anrechnungen.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.mehrleistung !== undefined) {
			for (const elem of obj.mehrleistung) {
				result.mehrleistung.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.minderleistung !== undefined) {
			for (const elem of obj.minderleistung) {
				result.minderleistung.add(LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.funktionen !== undefined) {
			for (const elem of obj.funktionen) {
				result.funktionen.add(LehrerFunktion.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: LehrerPersonalabschnittsdaten): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"pflichtstundensoll" : ' + ((obj.pflichtstundensoll === null) ? 'null' : obj.pflichtstundensoll.toString()) + ',';
		result += '"idRechtsverhaeltnis" : ' + ((obj.idRechtsverhaeltnis === null) ? 'null' : obj.idRechtsverhaeltnis.toString()) + ',';
		result += '"idBeschaeftigungsart" : ' + ((obj.idBeschaeftigungsart === null) ? 'null' : obj.idBeschaeftigungsart.toString()) + ',';
		result += '"idEinsatzstatus" : ' + ((obj.idEinsatzstatus === null) ? 'null' : obj.idEinsatzstatus.toString()) + ',';
		result += '"stammschulnummer" : ' + ((obj.stammschulnummer === null) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		result += '"anrechnungen" : [ ';
		for (let i = 0; i < obj.anrechnungen.size(); i++) {
			const elem = obj.anrechnungen.get(i);
			result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
			if (i < obj.anrechnungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"mehrleistung" : [ ';
		for (let i = 0; i < obj.mehrleistung.size(); i++) {
			const elem = obj.mehrleistung.get(i);
			result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
			if (i < obj.mehrleistung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"minderleistung" : [ ';
		for (let i = 0; i < obj.minderleistung.size(); i++) {
			const elem = obj.minderleistung.get(i);
			result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
			if (i < obj.minderleistung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"funktionen" : [ ';
		for (let i = 0; i < obj.funktionen.size(); i++) {
			const elem = obj.funktionen.get(i);
			result += LehrerFunktion.transpilerToJSON(elem);
			if (i < obj.funktionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerPersonalabschnittsdaten>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.pflichtstundensoll !== undefined) {
			result += '"pflichtstundensoll" : ' + ((obj.pflichtstundensoll === null) ? 'null' : obj.pflichtstundensoll.toString()) + ',';
		}
		if (obj.idRechtsverhaeltnis !== undefined) {
			result += '"idRechtsverhaeltnis" : ' + ((obj.idRechtsverhaeltnis === null) ? 'null' : obj.idRechtsverhaeltnis.toString()) + ',';
		}
		if (obj.idBeschaeftigungsart !== undefined) {
			result += '"idBeschaeftigungsart" : ' + ((obj.idBeschaeftigungsart === null) ? 'null' : obj.idBeschaeftigungsart.toString()) + ',';
		}
		if (obj.idEinsatzstatus !== undefined) {
			result += '"idEinsatzstatus" : ' + ((obj.idEinsatzstatus === null) ? 'null' : obj.idEinsatzstatus.toString()) + ',';
		}
		if (obj.stammschulnummer !== undefined) {
			result += '"stammschulnummer" : ' + ((obj.stammschulnummer === null) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		}
		if (obj.anrechnungen !== undefined) {
			result += '"anrechnungen" : [ ';
			for (let i = 0; i < obj.anrechnungen.size(); i++) {
				const elem = obj.anrechnungen.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.anrechnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.mehrleistung !== undefined) {
			result += '"mehrleistung" : [ ';
			for (let i = 0; i < obj.mehrleistung.size(); i++) {
				const elem = obj.mehrleistung.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.mehrleistung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.minderleistung !== undefined) {
			result += '"minderleistung" : [ ';
			for (let i = 0; i < obj.minderleistung.size(); i++) {
				const elem = obj.minderleistung.get(i);
				result += LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSON(elem);
				if (i < obj.minderleistung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.funktionen !== undefined) {
			result += '"funktionen" : [ ';
			for (let i = 0; i < obj.funktionen.size(); i++) {
				const elem = obj.funktionen.get(i);
				result += LehrerFunktion.transpilerToJSON(elem);
				if (i < obj.funktionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_lehrer_LehrerPersonalabschnittsdaten(obj: unknown): LehrerPersonalabschnittsdaten {
	return obj as LehrerPersonalabschnittsdaten;
}
