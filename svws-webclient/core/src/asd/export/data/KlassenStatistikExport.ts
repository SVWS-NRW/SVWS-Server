import { JavaObject } from '../../../java/lang/JavaObject';
import { KlassenBetreuungStatistikExport } from '../../../asd/export/data/KlassenBetreuungStatistikExport';
import { KlassenAltersstrukturStatistikExport } from '../../../asd/export/data/KlassenAltersstrukturStatistikExport';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { KlassenAusbildungsortsartStatistikExport } from '../../../asd/export/data/KlassenAusbildungsortsartStatistikExport';
import { Class } from '../../../java/lang/Class';
import { KlassenAusbildungsorteStatistikExport } from '../../../asd/export/data/KlassenAusbildungsorteStatistikExport';
import { KlassenZuwanderungsgeschichteStatistikExport } from '../../../asd/export/data/KlassenZuwanderungsgeschichteStatistikExport';
import { KlassenWohnorteStatistikExport } from '../../../asd/export/data/KlassenWohnorteStatistikExport';
import { KlassenHerkunftStatistikExport } from '../../../asd/export/data/KlassenHerkunftStatistikExport';
import { KlassenNationalitaetenStatistikExport } from '../../../asd/export/data/KlassenNationalitaetenStatistikExport';

export class KlassenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Der Jahrgang.
	 */
	public jahrgang: string = "";

	/**
	 * Satzschlüssel: 1. Stelle Parallelität / Das Bildungsgangkennzeichen.
	 */
	public bildungsgangkennzeichen: string = "";

	/**
	 * Satzschlüssel: 2. Stelle Parallelität.
	 */
	public parallelitaet2: string = "";

	/**
	 * Satzschlüssel: Die Teilklasse.
	 */
	public teilklasse: string = "";

	/**
	 * Die schulinterne Bezeichnung.
	 */
	public schulinterneBezeichnung: string = "";

	/**
	 * Die Schulgliederung bzw. der Bildungsgang.
	 */
	public schulgliederung: string | null = "";

	/**
	 * Die Fachklasse.
	 */
	public fachklasse: string | null = "";

	/**
	 * Die Klassenart.
	 */
	public klassenart: string | null = "";

	/**
	 * Der Jahrgang der Teilklasse.
	 */
	public jahrgangTeilklasse: string = "";

	/**
	 * Die Organisationsform.
	 */
	public organisationsform: string | null = "";

	/**
	 * Der erste Förderschwerpunkt.
	 */
	public foerderschwerpunkt1: string | null = "";

	/**
	 * Der zweite Förderschwerpunkt.
	 */
	public foerderschwerpunkt2: string | null = "";

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 */
	public hatSchwerbehinderungsNachweis: boolean = false;

	/**
	 * Der Bildungsbereich.
	 */
	public bildungsbereich: string | null = "";

	/**
	 * Ist JVA-Klasse.
	 */
	public jvaKlasse: string | null = "";

	/**
	 * Die Art der Reformpädagogik.
	 */
	public reformpaedagogik: string | null = "";

	/**
	 * Das Kürzel des Klassenlehrers.
	 */
	public kuerzelKlassenlehrer: string = "";

	/**
	 * Die Schüler der Teilklasse insgesamt.
	 */
	public schuelerInsgesamt: number = 0;

	/**
	 * Die Schüler der Teilklasse weiblich.
	 */
	public schuelerWeiblich: number = 0;

	/**
	 * Die ausländischen Schüler der Teilklasse zusammen.
	 */
	public schuelerAuslaendischZusammen: number = 0;

	/**
	 * Die ausländischen Schüler der Teilklasse weiblich.
	 */
	public schuelerAuslaendischWeiblich: number = 0;

	/**
	 * Das Adresskennzeichen.
	 */
	public adresskennzeichen: string | null = "";

	/**
	 * Hat Verkürzung halbjährlich.
	 */
	public verkuerzungHalbjaehrlich: boolean = false;

	/**
	 * Die Daten zur schulischen Herkunft der (Teil-) Klasse (K82).
	 */
	public klassenHerkunftStatistikExport: List<KlassenHerkunftStatistikExport> = new ArrayList<KlassenHerkunftStatistikExport>();

	/**
	 * Die Daten zu den Staatsangehörigkeiten der (Teil-) Klasse (K83).
	 */
	public klassenNationalitaetenStatistikExport: List<KlassenNationalitaetenStatistikExport> = new ArrayList<KlassenNationalitaetenStatistikExport>();

	/**
	 * Die Daten der Ausbildungsorte der (Teil-) Klasse (K85).
	 */
	public klassenAusbildungsortsartStatistikExport: KlassenAusbildungsortsartStatistikExport = new KlassenAusbildungsortsartStatistikExport();

	/**
	 * Die Daten zur Betreuung der (Teil-) Klasse (K87).
	 */
	public klassenBetreuungStatistikExport: List<KlassenBetreuungStatistikExport> = new ArrayList<KlassenBetreuungStatistikExport>();

	/**
	 * Die Daten zur regionalen Herkunft der Schüler nach dem Wohnort in der (Teil-) Klasse (X94).
	 */
	public klassenWohnorteStatistikExport: List<KlassenWohnorteStatistikExport> = new ArrayList<KlassenWohnorteStatistikExport>();

	/**
	 * Die Daten zur Altersstruktur der Schüler in der (Teil-) Klasse (X95).
	 */
	public klassenAltersstrukturStatistikExport: List<KlassenAltersstrukturStatistikExport> = new ArrayList<KlassenAltersstrukturStatistikExport>();

	/**
	 * Die Daten zur regionalen Lage des Ausbildungsortes der Schüler in der (Teil-) Klasse (X96).
	 */
	public klassenAusbildungsorteStatistikExport: List<KlassenAusbildungsorteStatistikExport> = new ArrayList<KlassenAusbildungsorteStatistikExport>();

	/**
	 * Die Daten zur Zuwanderungsgeschichte der Schüler in der (Teil-) Klasse (X98).
	 */
	public klassenZuwanderungsgeschichteStatistikExport: KlassenZuwanderungsgeschichteStatistikExport = new KlassenZuwanderungsgeschichteStatistikExport();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenStatistikExport>('de.svws_nrw.asd.export.data.KlassenStatistikExport');

	public static transpilerFromJSON(json: string): KlassenStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenStatistikExport>;
		const result = new KlassenStatistikExport();
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.bildungsgangkennzeichen === undefined)
			throw new Error('invalid json format, missing attribute bildungsgangkennzeichen');
		result.bildungsgangkennzeichen = obj.bildungsgangkennzeichen;
		if (obj.parallelitaet2 === undefined)
			throw new Error('invalid json format, missing attribute parallelitaet2');
		result.parallelitaet2 = obj.parallelitaet2;
		if (obj.teilklasse === undefined)
			throw new Error('invalid json format, missing attribute teilklasse');
		result.teilklasse = obj.teilklasse;
		if (obj.schulinterneBezeichnung === undefined)
			throw new Error('invalid json format, missing attribute schulinterneBezeichnung');
		result.schulinterneBezeichnung = obj.schulinterneBezeichnung;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.fachklasse = (obj.fachklasse === undefined) ? null : obj.fachklasse === null ? null : obj.fachklasse;
		result.klassenart = (obj.klassenart === undefined) ? null : obj.klassenart === null ? null : obj.klassenart;
		if (obj.jahrgangTeilklasse === undefined)
			throw new Error('invalid json format, missing attribute jahrgangTeilklasse');
		result.jahrgangTeilklasse = obj.jahrgangTeilklasse;
		result.organisationsform = (obj.organisationsform === undefined) ? null : obj.organisationsform === null ? null : obj.organisationsform;
		result.foerderschwerpunkt1 = (obj.foerderschwerpunkt1 === undefined) ? null : obj.foerderschwerpunkt1 === null ? null : obj.foerderschwerpunkt1;
		result.foerderschwerpunkt2 = (obj.foerderschwerpunkt2 === undefined) ? null : obj.foerderschwerpunkt2 === null ? null : obj.foerderschwerpunkt2;
		if (obj.hatSchwerbehinderungsNachweis === undefined)
			throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		result.bildungsbereich = (obj.bildungsbereich === undefined) ? null : obj.bildungsbereich === null ? null : obj.bildungsbereich;
		result.jvaKlasse = (obj.jvaKlasse === undefined) ? null : obj.jvaKlasse === null ? null : obj.jvaKlasse;
		result.reformpaedagogik = (obj.reformpaedagogik === undefined) ? null : obj.reformpaedagogik === null ? null : obj.reformpaedagogik;
		if (obj.kuerzelKlassenlehrer === undefined)
			throw new Error('invalid json format, missing attribute kuerzelKlassenlehrer');
		result.kuerzelKlassenlehrer = obj.kuerzelKlassenlehrer;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		if (obj.schuelerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerWeiblich');
		result.schuelerWeiblich = obj.schuelerWeiblich;
		if (obj.schuelerAuslaendischZusammen === undefined)
			throw new Error('invalid json format, missing attribute schuelerAuslaendischZusammen');
		result.schuelerAuslaendischZusammen = obj.schuelerAuslaendischZusammen;
		if (obj.schuelerAuslaendischWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerAuslaendischWeiblich');
		result.schuelerAuslaendischWeiblich = obj.schuelerAuslaendischWeiblich;
		result.adresskennzeichen = (obj.adresskennzeichen === undefined) ? null : obj.adresskennzeichen === null ? null : obj.adresskennzeichen;
		if (obj.verkuerzungHalbjaehrlich === undefined)
			throw new Error('invalid json format, missing attribute verkuerzungHalbjaehrlich');
		result.verkuerzungHalbjaehrlich = obj.verkuerzungHalbjaehrlich;
		if (obj.klassenHerkunftStatistikExport !== undefined) {
			for (const elem of obj.klassenHerkunftStatistikExport) {
				result.klassenHerkunftStatistikExport.add(KlassenHerkunftStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenNationalitaetenStatistikExport !== undefined) {
			for (const elem of obj.klassenNationalitaetenStatistikExport) {
				result.klassenNationalitaetenStatistikExport.add(KlassenNationalitaetenStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenAusbildungsortsartStatistikExport === undefined)
			throw new Error('invalid json format, missing attribute klassenAusbildungsortsartStatistikExport');
		result.klassenAusbildungsortsartStatistikExport = KlassenAusbildungsortsartStatistikExport.transpilerFromJSON(JSON.stringify(obj.klassenAusbildungsortsartStatistikExport));
		if (obj.klassenBetreuungStatistikExport !== undefined) {
			for (const elem of obj.klassenBetreuungStatistikExport) {
				result.klassenBetreuungStatistikExport.add(KlassenBetreuungStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenWohnorteStatistikExport !== undefined) {
			for (const elem of obj.klassenWohnorteStatistikExport) {
				result.klassenWohnorteStatistikExport.add(KlassenWohnorteStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenAltersstrukturStatistikExport !== undefined) {
			for (const elem of obj.klassenAltersstrukturStatistikExport) {
				result.klassenAltersstrukturStatistikExport.add(KlassenAltersstrukturStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenAusbildungsorteStatistikExport !== undefined) {
			for (const elem of obj.klassenAusbildungsorteStatistikExport) {
				result.klassenAusbildungsorteStatistikExport.add(KlassenAusbildungsorteStatistikExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenZuwanderungsgeschichteStatistikExport === undefined)
			throw new Error('invalid json format, missing attribute klassenZuwanderungsgeschichteStatistikExport');
		result.klassenZuwanderungsgeschichteStatistikExport = KlassenZuwanderungsgeschichteStatistikExport.transpilerFromJSON(JSON.stringify(obj.klassenZuwanderungsgeschichteStatistikExport));
		return result;
	}

	public static transpilerToJSON(obj: KlassenStatistikExport): string {
		let result = '{';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"bildungsgangkennzeichen" : ' + JSON.stringify(obj.bildungsgangkennzeichen) + ',';
		result += '"parallelitaet2" : ' + JSON.stringify(obj.parallelitaet2) + ',';
		result += '"teilklasse" : ' + JSON.stringify(obj.teilklasse) + ',';
		result += '"schulinterneBezeichnung" : ' + JSON.stringify(obj.schulinterneBezeichnung) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"fachklasse" : ' + ((obj.fachklasse === null) ? 'null' : JSON.stringify(obj.fachklasse)) + ',';
		result += '"klassenart" : ' + ((obj.klassenart === null) ? 'null' : JSON.stringify(obj.klassenart)) + ',';
		result += '"jahrgangTeilklasse" : ' + JSON.stringify(obj.jahrgangTeilklasse) + ',';
		result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
		result += '"foerderschwerpunkt1" : ' + ((obj.foerderschwerpunkt1 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		result += '"foerderschwerpunkt2" : ' + ((obj.foerderschwerpunkt2 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		result += '"bildungsbereich" : ' + ((obj.bildungsbereich === null) ? 'null' : JSON.stringify(obj.bildungsbereich)) + ',';
		result += '"jvaKlasse" : ' + ((obj.jvaKlasse === null) ? 'null' : JSON.stringify(obj.jvaKlasse)) + ',';
		result += '"reformpaedagogik" : ' + ((obj.reformpaedagogik === null) ? 'null' : JSON.stringify(obj.reformpaedagogik)) + ',';
		result += '"kuerzelKlassenlehrer" : ' + JSON.stringify(obj.kuerzelKlassenlehrer) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		result += '"schuelerAuslaendischZusammen" : ' + obj.schuelerAuslaendischZusammen.toString() + ',';
		result += '"schuelerAuslaendischWeiblich" : ' + obj.schuelerAuslaendischWeiblich.toString() + ',';
		result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
		result += '"verkuerzungHalbjaehrlich" : ' + obj.verkuerzungHalbjaehrlich.toString() + ',';
		result += '"klassenHerkunftStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenHerkunftStatistikExport.size(); i++) {
			const elem = obj.klassenHerkunftStatistikExport.get(i);
			result += KlassenHerkunftStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenHerkunftStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenNationalitaetenStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenNationalitaetenStatistikExport.size(); i++) {
			const elem = obj.klassenNationalitaetenStatistikExport.get(i);
			result += KlassenNationalitaetenStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenNationalitaetenStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenAusbildungsortsartStatistikExport" : ' + KlassenAusbildungsortsartStatistikExport.transpilerToJSON(obj.klassenAusbildungsortsartStatistikExport) + ',';
		result += '"klassenBetreuungStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenBetreuungStatistikExport.size(); i++) {
			const elem = obj.klassenBetreuungStatistikExport.get(i);
			result += KlassenBetreuungStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenBetreuungStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenWohnorteStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenWohnorteStatistikExport.size(); i++) {
			const elem = obj.klassenWohnorteStatistikExport.get(i);
			result += KlassenWohnorteStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenWohnorteStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenAltersstrukturStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenAltersstrukturStatistikExport.size(); i++) {
			const elem = obj.klassenAltersstrukturStatistikExport.get(i);
			result += KlassenAltersstrukturStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenAltersstrukturStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenAusbildungsorteStatistikExport" : [ ';
		for (let i = 0; i < obj.klassenAusbildungsorteStatistikExport.size(); i++) {
			const elem = obj.klassenAusbildungsorteStatistikExport.get(i);
			result += KlassenAusbildungsorteStatistikExport.transpilerToJSON(elem);
			if (i < obj.klassenAusbildungsorteStatistikExport.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenZuwanderungsgeschichteStatistikExport" : ' + KlassenZuwanderungsgeschichteStatistikExport.transpilerToJSON(obj.klassenZuwanderungsgeschichteStatistikExport) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenStatistikExport>): string {
		let result = '{';
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		if (obj.bildungsgangkennzeichen !== undefined) {
			result += '"bildungsgangkennzeichen" : ' + JSON.stringify(obj.bildungsgangkennzeichen) + ',';
		}
		if (obj.parallelitaet2 !== undefined) {
			result += '"parallelitaet2" : ' + JSON.stringify(obj.parallelitaet2) + ',';
		}
		if (obj.teilklasse !== undefined) {
			result += '"teilklasse" : ' + JSON.stringify(obj.teilklasse) + ',';
		}
		if (obj.schulinterneBezeichnung !== undefined) {
			result += '"schulinterneBezeichnung" : ' + JSON.stringify(obj.schulinterneBezeichnung) + ',';
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
		if (obj.jahrgangTeilklasse !== undefined) {
			result += '"jahrgangTeilklasse" : ' + JSON.stringify(obj.jahrgangTeilklasse) + ',';
		}
		if (obj.organisationsform !== undefined) {
			result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
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
		if (obj.bildungsbereich !== undefined) {
			result += '"bildungsbereich" : ' + ((obj.bildungsbereich === null) ? 'null' : JSON.stringify(obj.bildungsbereich)) + ',';
		}
		if (obj.jvaKlasse !== undefined) {
			result += '"jvaKlasse" : ' + ((obj.jvaKlasse === null) ? 'null' : JSON.stringify(obj.jvaKlasse)) + ',';
		}
		if (obj.reformpaedagogik !== undefined) {
			result += '"reformpaedagogik" : ' + ((obj.reformpaedagogik === null) ? 'null' : JSON.stringify(obj.reformpaedagogik)) + ',';
		}
		if (obj.kuerzelKlassenlehrer !== undefined) {
			result += '"kuerzelKlassenlehrer" : ' + JSON.stringify(obj.kuerzelKlassenlehrer) + ',';
		}
		if (obj.schuelerInsgesamt !== undefined) {
			result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		}
		if (obj.schuelerWeiblich !== undefined) {
			result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		}
		if (obj.schuelerAuslaendischZusammen !== undefined) {
			result += '"schuelerAuslaendischZusammen" : ' + obj.schuelerAuslaendischZusammen.toString() + ',';
		}
		if (obj.schuelerAuslaendischWeiblich !== undefined) {
			result += '"schuelerAuslaendischWeiblich" : ' + obj.schuelerAuslaendischWeiblich.toString() + ',';
		}
		if (obj.adresskennzeichen !== undefined) {
			result += '"adresskennzeichen" : ' + ((obj.adresskennzeichen === null) ? 'null' : JSON.stringify(obj.adresskennzeichen)) + ',';
		}
		if (obj.verkuerzungHalbjaehrlich !== undefined) {
			result += '"verkuerzungHalbjaehrlich" : ' + obj.verkuerzungHalbjaehrlich.toString() + ',';
		}
		if (obj.klassenHerkunftStatistikExport !== undefined) {
			result += '"klassenHerkunftStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenHerkunftStatistikExport.size(); i++) {
				const elem = obj.klassenHerkunftStatistikExport.get(i);
				result += KlassenHerkunftStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenHerkunftStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenNationalitaetenStatistikExport !== undefined) {
			result += '"klassenNationalitaetenStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenNationalitaetenStatistikExport.size(); i++) {
				const elem = obj.klassenNationalitaetenStatistikExport.get(i);
				result += KlassenNationalitaetenStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenNationalitaetenStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenAusbildungsortsartStatistikExport !== undefined) {
			result += '"klassenAusbildungsortsartStatistikExport" : ' + KlassenAusbildungsortsartStatistikExport.transpilerToJSON(obj.klassenAusbildungsortsartStatistikExport) + ',';
		}
		if (obj.klassenBetreuungStatistikExport !== undefined) {
			result += '"klassenBetreuungStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenBetreuungStatistikExport.size(); i++) {
				const elem = obj.klassenBetreuungStatistikExport.get(i);
				result += KlassenBetreuungStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenBetreuungStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenWohnorteStatistikExport !== undefined) {
			result += '"klassenWohnorteStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenWohnorteStatistikExport.size(); i++) {
				const elem = obj.klassenWohnorteStatistikExport.get(i);
				result += KlassenWohnorteStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenWohnorteStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenAltersstrukturStatistikExport !== undefined) {
			result += '"klassenAltersstrukturStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenAltersstrukturStatistikExport.size(); i++) {
				const elem = obj.klassenAltersstrukturStatistikExport.get(i);
				result += KlassenAltersstrukturStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenAltersstrukturStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenAusbildungsorteStatistikExport !== undefined) {
			result += '"klassenAusbildungsorteStatistikExport" : [ ';
			for (let i = 0; i < obj.klassenAusbildungsorteStatistikExport.size(); i++) {
				const elem = obj.klassenAusbildungsorteStatistikExport.get(i);
				result += KlassenAusbildungsorteStatistikExport.transpilerToJSON(elem);
				if (i < obj.klassenAusbildungsorteStatistikExport.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenZuwanderungsgeschichteStatistikExport !== undefined) {
			result += '"klassenZuwanderungsgeschichteStatistikExport" : ' + KlassenZuwanderungsgeschichteStatistikExport.transpilerToJSON(obj.klassenZuwanderungsgeschichteStatistikExport) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenStatistikExport(obj: unknown): KlassenStatistikExport {
	return obj as KlassenStatistikExport;
}
