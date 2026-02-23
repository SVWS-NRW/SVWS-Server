import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AbgaengerDetailStatistikExport } from '../../../asd/export/data/AbgaengerDetailStatistikExport';

export class AbgaengerStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Der Jahrgang der Schüler an Allgemeinbildenen Schulen.
	 */
	public jahrgang: string | null = "";

	/**
	 * Satzschlüssel: Das Bildungsgangkennzeichen der Schüler.
	 */
	public bildungsgangkennzeichen: string | null = "";

	/**
	 * Satzschlüssel: Die Schulgliederung der Schüler.
	 */
	public schulgliederung: string | null = "";

	/**
	 * Satzschlüssel: Die Fachklasse der Schüler.
	 */
	public fachklasse: string | null = "";

	/**
	 * Satzschlüssel: Die Klassenart der Schüler.
	 */
	public klassenart: string | null = "";

	/**
	 * Satzschlüssel: Der Jahrgang der Schüler an Berufskollegs.
	 */
	public jahrgangBK: string | null = "";

	/**
	 * Satzschlüssel: Der erste Förderschwerpunkt der Schüler.
	 */
	public foerderschwerpunkt1: string | null = "";

	/**
	 * Satzschlüssel: Der zweite Förderschwerpunkt der Schüler.
	 */
	public foerderschwerpunkt2: string | null = "";

	/**
	 * Satzschlüssel: Der Schüler hat einen Schwerbehinderungsnachweis.
	 */
	public hatSchwerbehinderungsNachweis: boolean = false;

	/**
	 * Die Summe der abgehenden Schüler insgesamt.
	 */
	public abgaengeInsgesamtZusammen: number = 0;

	/**
	 * Die Summe der abgehenden Schüler insgesamt Weiblich.
	 */
	public abgaengeInsgesamtWeiblich: number = 0;

	/**
	 * Die Summe der abgehenden ausländischen Schüler zusammen.
	 */
	public abgaengeAuslaenderZusammen: number = 0;

	/**
	 * Die Summe der abgehenden ausländischen Schüler  Weiblich.
	 */
	public abgaengeAuslaenderWeiblich: number = 0;

	/**
	 * Die Summe der Vorjahresschüler insgesamt.
	 */
	public vorjahresSchuelerInsgesamtZusammen: number = 0;

	/**
	 * Die Summe der Vorjahresschüler insgesamt Weiblich.
	 */
	public vorjahresSchuelerInsgesamtWeiblich: number = 0;

	/**
	 * Die Summe der ausländischen Vorjahresschüler zusammen.
	 */
	public vorjahresSchuelerAuslaenderZusammen: number = 0;

	/**
	 * Die Summe der ausländischen Vorjahresschüler Weiblich.
	 */
	public vorjahresSchuelerAuslaenderWeiblich: number = 0;

	/**
	 * Die Bestätigung, dass für diesen Satz keine Abgänger vorliegen.
	 */
	public bestaetigungKeineAbgaenger: boolean = false;

	/**
	 * Das Datumsstempel der Daten zu den Vorjahresschülern.
	 */
	public datumStempelVorjahresSchueler: string | null = "";

	/**
	 * Kennzeichnung Vorjahresschülerdatensatz.
	 */
	public istVorgabedatensatz: boolean = false;

	/**
	 * Die Summen der Abgänger im Detail (V54).
	 */
	public abgaengerDetailStatistikExport: List<AbgaengerDetailStatistikExport> = new ArrayList<AbgaengerDetailStatistikExport>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.AbgaengerStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.AbgaengerStatistikExport'].includes(name);
	}

	public static readonly class = new Class<AbgaengerStatistikExport>('de.svws_nrw.asd.export.data.AbgaengerStatistikExport');

	public static transpilerFromJSON(json: string): AbgaengerStatistikExport {
		const obj = JSON.parse(json) as Partial<AbgaengerStatistikExport>;
		const result = new AbgaengerStatistikExport();
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.bildungsgangkennzeichen = (obj.bildungsgangkennzeichen === undefined) ? null : obj.bildungsgangkennzeichen === null ? null : obj.bildungsgangkennzeichen;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.fachklasse = (obj.fachklasse === undefined) ? null : obj.fachklasse === null ? null : obj.fachklasse;
		result.klassenart = (obj.klassenart === undefined) ? null : obj.klassenart === null ? null : obj.klassenart;
		result.jahrgangBK = (obj.jahrgangBK === undefined) ? null : obj.jahrgangBK === null ? null : obj.jahrgangBK;
		result.foerderschwerpunkt1 = (obj.foerderschwerpunkt1 === undefined) ? null : obj.foerderschwerpunkt1 === null ? null : obj.foerderschwerpunkt1;
		result.foerderschwerpunkt2 = (obj.foerderschwerpunkt2 === undefined) ? null : obj.foerderschwerpunkt2 === null ? null : obj.foerderschwerpunkt2;
		if (obj.hatSchwerbehinderungsNachweis === undefined)
			throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		if (obj.abgaengeInsgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute abgaengeInsgesamtZusammen');
		result.abgaengeInsgesamtZusammen = obj.abgaengeInsgesamtZusammen;
		if (obj.abgaengeInsgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute abgaengeInsgesamtWeiblich');
		result.abgaengeInsgesamtWeiblich = obj.abgaengeInsgesamtWeiblich;
		if (obj.abgaengeAuslaenderZusammen === undefined)
			throw new Error('invalid json format, missing attribute abgaengeAuslaenderZusammen');
		result.abgaengeAuslaenderZusammen = obj.abgaengeAuslaenderZusammen;
		if (obj.abgaengeAuslaenderWeiblich === undefined)
			throw new Error('invalid json format, missing attribute abgaengeAuslaenderWeiblich');
		result.abgaengeAuslaenderWeiblich = obj.abgaengeAuslaenderWeiblich;
		if (obj.vorjahresSchuelerInsgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute vorjahresSchuelerInsgesamtZusammen');
		result.vorjahresSchuelerInsgesamtZusammen = obj.vorjahresSchuelerInsgesamtZusammen;
		if (obj.vorjahresSchuelerInsgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute vorjahresSchuelerInsgesamtWeiblich');
		result.vorjahresSchuelerInsgesamtWeiblich = obj.vorjahresSchuelerInsgesamtWeiblich;
		if (obj.vorjahresSchuelerAuslaenderZusammen === undefined)
			throw new Error('invalid json format, missing attribute vorjahresSchuelerAuslaenderZusammen');
		result.vorjahresSchuelerAuslaenderZusammen = obj.vorjahresSchuelerAuslaenderZusammen;
		if (obj.vorjahresSchuelerAuslaenderWeiblich === undefined)
			throw new Error('invalid json format, missing attribute vorjahresSchuelerAuslaenderWeiblich');
		result.vorjahresSchuelerAuslaenderWeiblich = obj.vorjahresSchuelerAuslaenderWeiblich;
		if (obj.bestaetigungKeineAbgaenger === undefined)
			throw new Error('invalid json format, missing attribute bestaetigungKeineAbgaenger');
		result.bestaetigungKeineAbgaenger = obj.bestaetigungKeineAbgaenger;
		result.datumStempelVorjahresSchueler = (obj.datumStempelVorjahresSchueler === undefined) ? null : obj.datumStempelVorjahresSchueler === null ? null : obj.datumStempelVorjahresSchueler;
		if (obj.istVorgabedatensatz === undefined)
			throw new Error('invalid json format, missing attribute istVorgabedatensatz');
		result.istVorgabedatensatz = obj.istVorgabedatensatz;
		if (obj.abgaengerDetailStatistikExport !== undefined) {
			for (const elem of obj.abgaengerDetailStatistikExport) {
				result.abgaengerDetailStatistikExport.add(AbgaengerDetailStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: AbgaengerStatistikExport): string {
		let result = '{';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"bildungsgangkennzeichen" : ' + ((obj.bildungsgangkennzeichen === null) ? 'null' : JSON.stringify(obj.bildungsgangkennzeichen)) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"fachklasse" : ' + ((obj.fachklasse === null) ? 'null' : JSON.stringify(obj.fachklasse)) + ',';
		result += '"klassenart" : ' + ((obj.klassenart === null) ? 'null' : JSON.stringify(obj.klassenart)) + ',';
		result += '"jahrgangBK" : ' + ((obj.jahrgangBK === null) ? 'null' : JSON.stringify(obj.jahrgangBK)) + ',';
		result += '"foerderschwerpunkt1" : ' + ((obj.foerderschwerpunkt1 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		result += '"foerderschwerpunkt2" : ' + ((obj.foerderschwerpunkt2 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		result += '"abgaengeInsgesamtZusammen" : ' + obj.abgaengeInsgesamtZusammen.toString() + ',';
		result += '"abgaengeInsgesamtWeiblich" : ' + obj.abgaengeInsgesamtWeiblich.toString() + ',';
		result += '"abgaengeAuslaenderZusammen" : ' + obj.abgaengeAuslaenderZusammen.toString() + ',';
		result += '"abgaengeAuslaenderWeiblich" : ' + obj.abgaengeAuslaenderWeiblich.toString() + ',';
		result += '"vorjahresSchuelerInsgesamtZusammen" : ' + obj.vorjahresSchuelerInsgesamtZusammen.toString() + ',';
		result += '"vorjahresSchuelerInsgesamtWeiblich" : ' + obj.vorjahresSchuelerInsgesamtWeiblich.toString() + ',';
		result += '"vorjahresSchuelerAuslaenderZusammen" : ' + obj.vorjahresSchuelerAuslaenderZusammen.toString() + ',';
		result += '"vorjahresSchuelerAuslaenderWeiblich" : ' + obj.vorjahresSchuelerAuslaenderWeiblich.toString() + ',';
		result += '"bestaetigungKeineAbgaenger" : ' + obj.bestaetigungKeineAbgaenger.toString() + ',';
		result += '"datumStempelVorjahresSchueler" : ' + ((obj.datumStempelVorjahresSchueler === null) ? 'null' : JSON.stringify(obj.datumStempelVorjahresSchueler)) + ',';
		result += '"istVorgabedatensatz" : ' + obj.istVorgabedatensatz.toString() + ',';
		result += '"abgaengerDetailStatistikExport" : [ ';
		for (let i = 0; i < obj.abgaengerDetailStatistikExport.size(); i++) {
			const elem = obj.abgaengerDetailStatistikExport.get(i);
			result += AbgaengerDetailStatistikExport.transpilerToJSON(elem);
			if (i < obj.abgaengerDetailStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AbgaengerStatistikExport>): string {
		let result = '{';
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.bildungsgangkennzeichen !== undefined) {
			result += '"bildungsgangkennzeichen" : ' + ((obj.bildungsgangkennzeichen === null) ? 'null' : JSON.stringify(obj.bildungsgangkennzeichen)) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.fachklasse !== undefined) {
			result += '"fachklasse" : ' + ((obj.fachklasse === null) ? 'null' : JSON.stringify(obj.fachklasse)) + ',';
		}
		if (obj.klassenart !== undefined) {
			result += '"klassenart" : ' + ((obj.klassenart === null) ? 'null' : JSON.stringify(obj.klassenart)) + ',';
		}
		if (obj.jahrgangBK !== undefined) {
			result += '"jahrgangBK" : ' + ((obj.jahrgangBK === null) ? 'null' : JSON.stringify(obj.jahrgangBK)) + ',';
		}
		if (obj.foerderschwerpunkt1 !== undefined) {
			result += '"foerderschwerpunkt1" : ' + ((obj.foerderschwerpunkt1 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		}
		if (obj.foerderschwerpunkt2 !== undefined) {
			result += '"foerderschwerpunkt2" : ' + ((obj.foerderschwerpunkt2 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		}
		if (obj.hatSchwerbehinderungsNachweis !== undefined) {
			result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		}
		if (obj.abgaengeInsgesamtZusammen !== undefined) {
			result += '"abgaengeInsgesamtZusammen" : ' + obj.abgaengeInsgesamtZusammen.toString() + ',';
		}
		if (obj.abgaengeInsgesamtWeiblich !== undefined) {
			result += '"abgaengeInsgesamtWeiblich" : ' + obj.abgaengeInsgesamtWeiblich.toString() + ',';
		}
		if (obj.abgaengeAuslaenderZusammen !== undefined) {
			result += '"abgaengeAuslaenderZusammen" : ' + obj.abgaengeAuslaenderZusammen.toString() + ',';
		}
		if (obj.abgaengeAuslaenderWeiblich !== undefined) {
			result += '"abgaengeAuslaenderWeiblich" : ' + obj.abgaengeAuslaenderWeiblich.toString() + ',';
		}
		if (obj.vorjahresSchuelerInsgesamtZusammen !== undefined) {
			result += '"vorjahresSchuelerInsgesamtZusammen" : ' + obj.vorjahresSchuelerInsgesamtZusammen.toString() + ',';
		}
		if (obj.vorjahresSchuelerInsgesamtWeiblich !== undefined) {
			result += '"vorjahresSchuelerInsgesamtWeiblich" : ' + obj.vorjahresSchuelerInsgesamtWeiblich.toString() + ',';
		}
		if (obj.vorjahresSchuelerAuslaenderZusammen !== undefined) {
			result += '"vorjahresSchuelerAuslaenderZusammen" : ' + obj.vorjahresSchuelerAuslaenderZusammen.toString() + ',';
		}
		if (obj.vorjahresSchuelerAuslaenderWeiblich !== undefined) {
			result += '"vorjahresSchuelerAuslaenderWeiblich" : ' + obj.vorjahresSchuelerAuslaenderWeiblich.toString() + ',';
		}
		if (obj.bestaetigungKeineAbgaenger !== undefined) {
			result += '"bestaetigungKeineAbgaenger" : ' + obj.bestaetigungKeineAbgaenger.toString() + ',';
		}
		if (obj.datumStempelVorjahresSchueler !== undefined) {
			result += '"datumStempelVorjahresSchueler" : ' + ((obj.datumStempelVorjahresSchueler === null) ? 'null' : JSON.stringify(obj.datumStempelVorjahresSchueler)) + ',';
		}
		if (obj.istVorgabedatensatz !== undefined) {
			result += '"istVorgabedatensatz" : ' + obj.istVorgabedatensatz.toString() + ',';
		}
		if (obj.abgaengerDetailStatistikExport !== undefined) {
			result += '"abgaengerDetailStatistikExport" : [ ';
			for (let i = 0; i < obj.abgaengerDetailStatistikExport.size(); i++) {
				const elem = obj.abgaengerDetailStatistikExport.get(i);
				result += AbgaengerDetailStatistikExport.transpilerToJSON(elem);
				if (i < obj.abgaengerDetailStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_AbgaengerStatistikExport(obj: unknown): AbgaengerStatistikExport {
	return obj as AbgaengerStatistikExport;
}
