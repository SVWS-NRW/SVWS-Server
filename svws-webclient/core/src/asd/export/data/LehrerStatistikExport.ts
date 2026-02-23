import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerFachrichtungenStatistikExport } from '../../../asd/export/data/LehrerFachrichtungenStatistikExport';
import { LehrerMehrleistungenStatistikExport } from '../../../asd/export/data/LehrerMehrleistungenStatistikExport';
import { LehrerAnrechungenStatistikExport } from '../../../asd/export/data/LehrerAnrechungenStatistikExport';
import { LehrerMinderleistungenStatistikExport } from '../../../asd/export/data/LehrerMinderleistungenStatistikExport';
import { LehrerLehrbefaehigungenStatistikExport } from '../../../asd/export/data/LehrerLehrbefaehigungenStatistikExport';
import { LehrerErteilteStundenStatistikExport } from '../../../asd/export/data/LehrerErteilteStundenStatistikExport';
import { LehrerLehraemterStatistikExport } from '../../../asd/export/data/LehrerLehraemterStatistikExport';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class LehrerStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Das Kürzel des Lehrers.
	 */
	public kuerzel: string = "";

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname: string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname: string = "";

	/**
	 * Der Tag des Geburtsdatums des Lehrers.
	 */
	public geburtsdatumTag: string | null = "";

	/**
	 * Der Monat des Geburtsdatums des Lehrers.
	 */
	public geburtsdatumMonat: string | null = "";

	/**
	 * Das Jahr des Geburtsdatums des Lehrers.
	 */
	public geburtsdatumJahr: string | null = "";

	/**
	 * Das Geschlecht das Lehrers.
	 */
	public geschlecht: number = 7;

	/**
	 * Die Staatsangehörigkeit des Lehrers.
	 */
	public staatsangehoerigkeit: string | null = "";

	/**
	 * Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit).
	 */
	public rechtsverhaeltnis: string | null = "";

	/**
	 * Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.).
	 */
	public beschaeftigungsart: string | null = "";

	/**
	 * [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig)
	 */
	public einsatzstatus: string | null = "";

	/**
	 * Das Pflichtstundensoll des Lehrers.
	 */
	public pflichtstundensoll: number = 0.0;

	/**
	 * Der zu erteilende Unterricht des Lehrers.
	 */
	public zuErteilenderUnterricht: number = 0.0;

	/**
	 * Erteilter Unterricht des Lehrers.
	 */
	public erteilerUnterricht: number = 0.0;

	/**
	 * Die Daten zu den Lehrämtern (L62).
	 */
	public lehraemterStatistikExport: List<LehrerLehraemterStatistikExport> = new ArrayList<LehrerLehraemterStatistikExport>();

	/**
	 * Die Daten zu den Fachrichtungen (L63).
	 */
	public fachrichtungenStatistikExport: List<LehrerFachrichtungenStatistikExport> = new ArrayList<LehrerFachrichtungenStatistikExport>();

	/**
	 * Die Daten zu der Lehrbefähigungen (L64).
	 */
	public lehrbefaehigungenStatistikExport: List<LehrerLehrbefaehigungenStatistikExport> = new ArrayList<LehrerLehrbefaehigungenStatistikExport>();

	/**
	 * Die nicht unterrichtlichen Tätigkeiten / Anrechungen (L65).
	 */
	public anrechungenStatistikExport: List<LehrerAnrechungenStatistikExport> = new ArrayList<LehrerAnrechungenStatistikExport>();

	/**
	 * Die Mehrleistungen (L66).
	 */
	public mehrleistungenStatistikExport: List<LehrerMehrleistungenStatistikExport> = new ArrayList<LehrerMehrleistungenStatistikExport>();

	/**
	 * Die Minderleistungen (L67).
	 */
	public minderleistungenStatistikExport: List<LehrerMinderleistungenStatistikExport> = new ArrayList<LehrerMinderleistungenStatistikExport>();

	/**
	 * Die erteilten Stunden nach Bildungsbereich (nur FW) (L68).
	 */
	public erteilteStundenStatistikExport: List<LehrerErteilteStundenStatistikExport> = new ArrayList<LehrerErteilteStundenStatistikExport>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerStatistikExport>('de.svws_nrw.asd.export.data.LehrerStatistikExport');

	public static transpilerFromJSON(json: string): LehrerStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerStatistikExport>;
		const result = new LehrerStatistikExport();
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		result.geburtsdatumTag = (obj.geburtsdatumTag === undefined) ? null : obj.geburtsdatumTag === null ? null : obj.geburtsdatumTag;
		result.geburtsdatumMonat = (obj.geburtsdatumMonat === undefined) ? null : obj.geburtsdatumMonat === null ? null : obj.geburtsdatumMonat;
		result.geburtsdatumJahr = (obj.geburtsdatumJahr === undefined) ? null : obj.geburtsdatumJahr === null ? null : obj.geburtsdatumJahr;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.staatsangehoerigkeit = (obj.staatsangehoerigkeit === undefined) ? null : obj.staatsangehoerigkeit === null ? null : obj.staatsangehoerigkeit;
		result.rechtsverhaeltnis = (obj.rechtsverhaeltnis === undefined) ? null : obj.rechtsverhaeltnis === null ? null : obj.rechtsverhaeltnis;
		result.beschaeftigungsart = (obj.beschaeftigungsart === undefined) ? null : obj.beschaeftigungsart === null ? null : obj.beschaeftigungsart;
		result.einsatzstatus = (obj.einsatzstatus === undefined) ? null : obj.einsatzstatus === null ? null : obj.einsatzstatus;
		if (obj.pflichtstundensoll === undefined)
			throw new Error('invalid json format, missing attribute pflichtstundensoll');
		result.pflichtstundensoll = obj.pflichtstundensoll;
		if (obj.zuErteilenderUnterricht === undefined)
			throw new Error('invalid json format, missing attribute zuErteilenderUnterricht');
		result.zuErteilenderUnterricht = obj.zuErteilenderUnterricht;
		if (obj.erteilerUnterricht === undefined)
			throw new Error('invalid json format, missing attribute erteilerUnterricht');
		result.erteilerUnterricht = obj.erteilerUnterricht;
		if (obj.lehraemterStatistikExport !== undefined) {
			for (const elem of obj.lehraemterStatistikExport) {
				result.lehraemterStatistikExport.add(LehrerLehraemterStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.fachrichtungenStatistikExport !== undefined) {
			for (const elem of obj.fachrichtungenStatistikExport) {
				result.fachrichtungenStatistikExport.add(LehrerFachrichtungenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrbefaehigungenStatistikExport !== undefined) {
			for (const elem of obj.lehrbefaehigungenStatistikExport) {
				result.lehrbefaehigungenStatistikExport.add(LehrerLehrbefaehigungenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.anrechungenStatistikExport !== undefined) {
			for (const elem of obj.anrechungenStatistikExport) {
				result.anrechungenStatistikExport.add(LehrerAnrechungenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.mehrleistungenStatistikExport !== undefined) {
			for (const elem of obj.mehrleistungenStatistikExport) {
				result.mehrleistungenStatistikExport.add(LehrerMehrleistungenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.minderleistungenStatistikExport !== undefined) {
			for (const elem of obj.minderleistungenStatistikExport) {
				result.minderleistungenStatistikExport.add(LehrerMinderleistungenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.erteilteStundenStatistikExport !== undefined) {
			for (const elem of obj.erteilteStundenStatistikExport) {
				result.erteilteStundenStatistikExport.add(LehrerErteilteStundenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: LehrerStatistikExport): string {
		let result = '{';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"geburtsdatumTag" : ' + ((obj.geburtsdatumTag === null) ? 'null' : JSON.stringify(obj.geburtsdatumTag)) + ',';
		result += '"geburtsdatumMonat" : ' + ((obj.geburtsdatumMonat === null) ? 'null' : JSON.stringify(obj.geburtsdatumMonat)) + ',';
		result += '"geburtsdatumJahr" : ' + ((obj.geburtsdatumJahr === null) ? 'null' : JSON.stringify(obj.geburtsdatumJahr)) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"staatsangehoerigkeit" : ' + ((obj.staatsangehoerigkeit === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit)) + ',';
		result += '"rechtsverhaeltnis" : ' + ((obj.rechtsverhaeltnis === null) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		result += '"beschaeftigungsart" : ' + ((obj.beschaeftigungsart === null) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		result += '"einsatzstatus" : ' + ((obj.einsatzstatus === null) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		result += '"pflichtstundensoll" : ' + obj.pflichtstundensoll.toString() + ',';
		result += '"zuErteilenderUnterricht" : ' + obj.zuErteilenderUnterricht.toString() + ',';
		result += '"erteilerUnterricht" : ' + obj.erteilerUnterricht.toString() + ',';
		result += '"lehraemterStatistikExport" : [ ';
		for (let i = 0; i < obj.lehraemterStatistikExport.size(); i++) {
			const elem = obj.lehraemterStatistikExport.get(i);
			result += LehrerLehraemterStatistikExport.transpilerToJSON(elem);
			if (i < obj.lehraemterStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachrichtungenStatistikExport" : [ ';
		for (let i = 0; i < obj.fachrichtungenStatistikExport.size(); i++) {
			const elem = obj.fachrichtungenStatistikExport.get(i);
			result += LehrerFachrichtungenStatistikExport.transpilerToJSON(elem);
			if (i < obj.fachrichtungenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrbefaehigungenStatistikExport" : [ ';
		for (let i = 0; i < obj.lehrbefaehigungenStatistikExport.size(); i++) {
			const elem = obj.lehrbefaehigungenStatistikExport.get(i);
			result += LehrerLehrbefaehigungenStatistikExport.transpilerToJSON(elem);
			if (i < obj.lehrbefaehigungenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"anrechungenStatistikExport" : [ ';
		for (let i = 0; i < obj.anrechungenStatistikExport.size(); i++) {
			const elem = obj.anrechungenStatistikExport.get(i);
			result += LehrerAnrechungenStatistikExport.transpilerToJSON(elem);
			if (i < obj.anrechungenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"mehrleistungenStatistikExport" : [ ';
		for (let i = 0; i < obj.mehrleistungenStatistikExport.size(); i++) {
			const elem = obj.mehrleistungenStatistikExport.get(i);
			result += LehrerMehrleistungenStatistikExport.transpilerToJSON(elem);
			if (i < obj.mehrleistungenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"minderleistungenStatistikExport" : [ ';
		for (let i = 0; i < obj.minderleistungenStatistikExport.size(); i++) {
			const elem = obj.minderleistungenStatistikExport.get(i);
			result += LehrerMinderleistungenStatistikExport.transpilerToJSON(elem);
			if (i < obj.minderleistungenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"erteilteStundenStatistikExport" : [ ';
		for (let i = 0; i < obj.erteilteStundenStatistikExport.size(); i++) {
			const elem = obj.erteilteStundenStatistikExport.get(i);
			result += LehrerErteilteStundenStatistikExport.transpilerToJSON(elem);
			if (i < obj.erteilteStundenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerStatistikExport>): string {
		let result = '{';
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.geburtsdatumTag !== undefined) {
			result += '"geburtsdatumTag" : ' + ((obj.geburtsdatumTag === null) ? 'null' : JSON.stringify(obj.geburtsdatumTag)) + ',';
		}
		if (obj.geburtsdatumMonat !== undefined) {
			result += '"geburtsdatumMonat" : ' + ((obj.geburtsdatumMonat === null) ? 'null' : JSON.stringify(obj.geburtsdatumMonat)) + ',';
		}
		if (obj.geburtsdatumJahr !== undefined) {
			result += '"geburtsdatumJahr" : ' + ((obj.geburtsdatumJahr === null) ? 'null' : JSON.stringify(obj.geburtsdatumJahr)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.staatsangehoerigkeit !== undefined) {
			result += '"staatsangehoerigkeit" : ' + ((obj.staatsangehoerigkeit === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit)) + ',';
		}
		if (obj.rechtsverhaeltnis !== undefined) {
			result += '"rechtsverhaeltnis" : ' + ((obj.rechtsverhaeltnis === null) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		}
		if (obj.beschaeftigungsart !== undefined) {
			result += '"beschaeftigungsart" : ' + ((obj.beschaeftigungsart === null) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		}
		if (obj.einsatzstatus !== undefined) {
			result += '"einsatzstatus" : ' + ((obj.einsatzstatus === null) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		}
		if (obj.pflichtstundensoll !== undefined) {
			result += '"pflichtstundensoll" : ' + obj.pflichtstundensoll.toString() + ',';
		}
		if (obj.zuErteilenderUnterricht !== undefined) {
			result += '"zuErteilenderUnterricht" : ' + obj.zuErteilenderUnterricht.toString() + ',';
		}
		if (obj.erteilerUnterricht !== undefined) {
			result += '"erteilerUnterricht" : ' + obj.erteilerUnterricht.toString() + ',';
		}
		if (obj.lehraemterStatistikExport !== undefined) {
			result += '"lehraemterStatistikExport" : [ ';
			for (let i = 0; i < obj.lehraemterStatistikExport.size(); i++) {
				const elem = obj.lehraemterStatistikExport.get(i);
				result += LehrerLehraemterStatistikExport.transpilerToJSON(elem);
				if (i < obj.lehraemterStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachrichtungenStatistikExport !== undefined) {
			result += '"fachrichtungenStatistikExport" : [ ';
			for (let i = 0; i < obj.fachrichtungenStatistikExport.size(); i++) {
				const elem = obj.fachrichtungenStatistikExport.get(i);
				result += LehrerFachrichtungenStatistikExport.transpilerToJSON(elem);
				if (i < obj.fachrichtungenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrbefaehigungenStatistikExport !== undefined) {
			result += '"lehrbefaehigungenStatistikExport" : [ ';
			for (let i = 0; i < obj.lehrbefaehigungenStatistikExport.size(); i++) {
				const elem = obj.lehrbefaehigungenStatistikExport.get(i);
				result += LehrerLehrbefaehigungenStatistikExport.transpilerToJSON(elem);
				if (i < obj.lehrbefaehigungenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.anrechungenStatistikExport !== undefined) {
			result += '"anrechungenStatistikExport" : [ ';
			for (let i = 0; i < obj.anrechungenStatistikExport.size(); i++) {
				const elem = obj.anrechungenStatistikExport.get(i);
				result += LehrerAnrechungenStatistikExport.transpilerToJSON(elem);
				if (i < obj.anrechungenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.mehrleistungenStatistikExport !== undefined) {
			result += '"mehrleistungenStatistikExport" : [ ';
			for (let i = 0; i < obj.mehrleistungenStatistikExport.size(); i++) {
				const elem = obj.mehrleistungenStatistikExport.get(i);
				result += LehrerMehrleistungenStatistikExport.transpilerToJSON(elem);
				if (i < obj.mehrleistungenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.minderleistungenStatistikExport !== undefined) {
			result += '"minderleistungenStatistikExport" : [ ';
			for (let i = 0; i < obj.minderleistungenStatistikExport.size(); i++) {
				const elem = obj.minderleistungenStatistikExport.get(i);
				result += LehrerMinderleistungenStatistikExport.transpilerToJSON(elem);
				if (i < obj.minderleistungenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.erteilteStundenStatistikExport !== undefined) {
			result += '"erteilteStundenStatistikExport" : [ ';
			for (let i = 0; i < obj.erteilteStundenStatistikExport.size(); i++) {
				const elem = obj.erteilteStundenStatistikExport.get(i);
				result += LehrerErteilteStundenStatistikExport.transpilerToJSON(elem);
				if (i < obj.erteilteStundenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerStatistikExport(obj: unknown): LehrerStatistikExport {
	return obj as LehrerStatistikExport;
}
