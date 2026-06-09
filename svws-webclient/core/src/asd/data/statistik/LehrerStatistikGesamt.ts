import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class LehrerStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id: number = -1;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel: string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname: string = "";

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname: string = "";

	/**
	 * Das Geburtsdatum des Lehrers.
	 */
	public geburtsdatum: string | null = null;

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht: number = 0;

	/**
	 * Ggf. die ID für die Staatsangehörigkeit des Lehrers.
	 */
	public idStaatsangehoerigkeit: number | null = null;

	/**
	 * Die Lehrämter des Lehrers.
	 */
	public readonly lehraemter: List<LehrerLehramtEintrag> = new ArrayList<LehrerLehramtEintrag>();

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
	 * Das Pflichtstundensoll des Lehrers.
	 */
	public pflichtstundensoll: number | null = null;

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
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<LehrerStatistikGesamt>('de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt');

	public static transpilerFromJSON(json: string): LehrerStatistikGesamt {
		const obj = JSON.parse(json) as Partial<LehrerStatistikGesamt>;
		const result = new LehrerStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.idStaatsangehoerigkeit = (obj.idStaatsangehoerigkeit === undefined) ? null : obj.idStaatsangehoerigkeit === null ? null : obj.idStaatsangehoerigkeit;
		if (obj.lehraemter !== undefined) {
			for (const elem of obj.lehraemter) {
				result.lehraemter.add(LehrerLehramtEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.idRechtsverhaeltnis = (obj.idRechtsverhaeltnis === undefined) ? null : obj.idRechtsverhaeltnis === null ? null : obj.idRechtsverhaeltnis;
		result.idBeschaeftigungsart = (obj.idBeschaeftigungsart === undefined) ? null : obj.idBeschaeftigungsart === null ? null : obj.idBeschaeftigungsart;
		result.idEinsatzstatus = (obj.idEinsatzstatus === undefined) ? null : obj.idEinsatzstatus === null ? null : obj.idEinsatzstatus;
		result.pflichtstundensoll = (obj.pflichtstundensoll === undefined) ? null : obj.pflichtstundensoll === null ? null : obj.pflichtstundensoll;
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
		return result;
	}

	public static transpilerToJSON(obj: LehrerStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"idStaatsangehoerigkeit" : ' + ((obj.idStaatsangehoerigkeit === null) ? 'null' : obj.idStaatsangehoerigkeit.toString()) + ',';
		result += '"lehraemter" : [ ';
		for (let i = 0; i < obj.lehraemter.size(); i++) {
			const elem = obj.lehraemter.get(i);
			result += LehrerLehramtEintrag.transpilerToJSON(elem);
			if (i < obj.lehraemter.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idRechtsverhaeltnis" : ' + ((obj.idRechtsverhaeltnis === null) ? 'null' : obj.idRechtsverhaeltnis.toString()) + ',';
		result += '"idBeschaeftigungsart" : ' + ((obj.idBeschaeftigungsart === null) ? 'null' : obj.idBeschaeftigungsart.toString()) + ',';
		result += '"idEinsatzstatus" : ' + ((obj.idEinsatzstatus === null) ? 'null' : obj.idEinsatzstatus.toString()) + ',';
		result += '"pflichtstundensoll" : ' + ((obj.pflichtstundensoll === null) ? 'null' : obj.pflichtstundensoll.toString()) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.idStaatsangehoerigkeit !== undefined) {
			result += '"idStaatsangehoerigkeit" : ' + ((obj.idStaatsangehoerigkeit === null) ? 'null' : obj.idStaatsangehoerigkeit.toString()) + ',';
		}
		if (obj.lehraemter !== undefined) {
			result += '"lehraemter" : [ ';
			for (let i = 0; i < obj.lehraemter.size(); i++) {
				const elem = obj.lehraemter.get(i);
				result += LehrerLehramtEintrag.transpilerToJSON(elem);
				if (i < obj.lehraemter.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
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
		if (obj.pflichtstundensoll !== undefined) {
			result += '"pflichtstundensoll" : ' + ((obj.pflichtstundensoll === null) ? 'null' : obj.pflichtstundensoll.toString()) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_LehrerStatistikGesamt(obj: unknown): LehrerStatistikGesamt {
	return obj as LehrerStatistikGesamt;
}
