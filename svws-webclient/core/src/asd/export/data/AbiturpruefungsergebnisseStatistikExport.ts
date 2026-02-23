import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class AbiturpruefungsergebnisseStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die laufende Nummer des Schülers.
	 */
	public laufendeNummer: string | null = "";

	/**
	 * Der Jahrgang (leer).
	 */
	public jahrgang: string | null = "";

	/**
	 * Das Bildungsgangkennzeichen.
	 */
	public bildungsgangkennzeichen: string | null = "";

	/**
	 * Die Schulgliederung.
	 */
	public schulgliederung: string | null = "";

	/**
	 * Die Fachklasse (leer).
	 */
	public fachklasse: string | null = "";

	/**
	 * Die Abgangsart (leer).
	 */
	public abgangsart: string | null = "";

	/**
	 * Das Geburtsjahr des Abiturienten.
	 */
	public geburtsjahr: string | null = "";

	/**
	 * Das Geschlecht des Abiturienten.
	 */
	public geschlecht: string | null = "";

	/**
	 * Das erste Abiturfach.
	 */
	public abiturfach1: string | null = "";

	/**
	 * Das erste Abiturfach Aufgabenfeld.
	 */
	public abiturfach1Aufgabenfeld: string | null = "";

	/**
	 * Das erste Abiturfach Fachtyp.
	 */
	public abiturfach1Fachtyp: string | null = "";

	/**
	 * Das zweite Abiturfach.
	 */
	public abiturfach2: string | null = "";

	/**
	 * Das zweite Abiturfach Aufgabenfeld.
	 */
	public abiturfach2Aufgabenfeld: string | null = "";

	/**
	 * Das zweite Abiturfach Fachtyp.
	 */
	public abiturfach2Fachtyp: string | null = "";

	/**
	 * Das dritte Abiturfach.
	 */
	public abiturfach3: string | null = "";

	/**
	 * Das dritte Abiturfach Aufgabenfeld.
	 */
	public abiturfach3Aufgabenfeld: string | null = "";

	/**
	 * Das dritte Abiturfach Fachtyp.
	 */
	public abiturfach3Fachtyp: string | null = "";

	/**
	 * Das vierte Abiturfach.
	 */
	public abiturfach4: string | null = "";

	/**
	 * Das vierte Abiturfach Aufgabenfeld.
	 */
	public abiturfach4Aufgabenfeld: string | null = "";

	/**
	 * Das vierte Abiturfach Fachtyp.
	 */
	public abiturfach4Fachtyp: string | null = "";

	/**
	 * Die Abiturnote.
	 */
	public abiturnote: string | null = "";

	/**
	 * Die Nationalität.
	 */
	public nationalitaet: string | null = "";

	/**
	 * Der Abiturstatus (bestanden/nicht bestanden).
	 */
	public abiturstatus: string | null = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.AbiturpruefungsergebnisseStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.AbiturpruefungsergebnisseStatistikExport'].includes(name);
	}

	public static readonly class = new Class<AbiturpruefungsergebnisseStatistikExport>('de.svws_nrw.asd.export.data.AbiturpruefungsergebnisseStatistikExport');

	public static transpilerFromJSON(json: string): AbiturpruefungsergebnisseStatistikExport {
		const obj = JSON.parse(json) as Partial<AbiturpruefungsergebnisseStatistikExport>;
		const result = new AbiturpruefungsergebnisseStatistikExport();
		result.laufendeNummer = (obj.laufendeNummer === undefined) ? null : obj.laufendeNummer === null ? null : obj.laufendeNummer;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.bildungsgangkennzeichen = (obj.bildungsgangkennzeichen === undefined) ? null : obj.bildungsgangkennzeichen === null ? null : obj.bildungsgangkennzeichen;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.fachklasse = (obj.fachklasse === undefined) ? null : obj.fachklasse === null ? null : obj.fachklasse;
		result.abgangsart = (obj.abgangsart === undefined) ? null : obj.abgangsart === null ? null : obj.abgangsart;
		result.geburtsjahr = (obj.geburtsjahr === undefined) ? null : obj.geburtsjahr === null ? null : obj.geburtsjahr;
		result.geschlecht = (obj.geschlecht === undefined) ? null : obj.geschlecht === null ? null : obj.geschlecht;
		result.abiturfach1 = (obj.abiturfach1 === undefined) ? null : obj.abiturfach1 === null ? null : obj.abiturfach1;
		result.abiturfach1Aufgabenfeld = (obj.abiturfach1Aufgabenfeld === undefined) ? null : obj.abiturfach1Aufgabenfeld === null ? null : obj.abiturfach1Aufgabenfeld;
		result.abiturfach1Fachtyp = (obj.abiturfach1Fachtyp === undefined) ? null : obj.abiturfach1Fachtyp === null ? null : obj.abiturfach1Fachtyp;
		result.abiturfach2 = (obj.abiturfach2 === undefined) ? null : obj.abiturfach2 === null ? null : obj.abiturfach2;
		result.abiturfach2Aufgabenfeld = (obj.abiturfach2Aufgabenfeld === undefined) ? null : obj.abiturfach2Aufgabenfeld === null ? null : obj.abiturfach2Aufgabenfeld;
		result.abiturfach2Fachtyp = (obj.abiturfach2Fachtyp === undefined) ? null : obj.abiturfach2Fachtyp === null ? null : obj.abiturfach2Fachtyp;
		result.abiturfach3 = (obj.abiturfach3 === undefined) ? null : obj.abiturfach3 === null ? null : obj.abiturfach3;
		result.abiturfach3Aufgabenfeld = (obj.abiturfach3Aufgabenfeld === undefined) ? null : obj.abiturfach3Aufgabenfeld === null ? null : obj.abiturfach3Aufgabenfeld;
		result.abiturfach3Fachtyp = (obj.abiturfach3Fachtyp === undefined) ? null : obj.abiturfach3Fachtyp === null ? null : obj.abiturfach3Fachtyp;
		result.abiturfach4 = (obj.abiturfach4 === undefined) ? null : obj.abiturfach4 === null ? null : obj.abiturfach4;
		result.abiturfach4Aufgabenfeld = (obj.abiturfach4Aufgabenfeld === undefined) ? null : obj.abiturfach4Aufgabenfeld === null ? null : obj.abiturfach4Aufgabenfeld;
		result.abiturfach4Fachtyp = (obj.abiturfach4Fachtyp === undefined) ? null : obj.abiturfach4Fachtyp === null ? null : obj.abiturfach4Fachtyp;
		result.abiturnote = (obj.abiturnote === undefined) ? null : obj.abiturnote === null ? null : obj.abiturnote;
		result.nationalitaet = (obj.nationalitaet === undefined) ? null : obj.nationalitaet === null ? null : obj.nationalitaet;
		result.abiturstatus = (obj.abiturstatus === undefined) ? null : obj.abiturstatus === null ? null : obj.abiturstatus;
		return result;
	}

	public static transpilerToJSON(obj: AbiturpruefungsergebnisseStatistikExport): string {
		let result = '{';
		result += '"laufendeNummer" : ' + ((obj.laufendeNummer === null) ? 'null' : JSON.stringify(obj.laufendeNummer)) + ',';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"bildungsgangkennzeichen" : ' + ((obj.bildungsgangkennzeichen === null) ? 'null' : JSON.stringify(obj.bildungsgangkennzeichen)) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"fachklasse" : ' + ((obj.fachklasse === null) ? 'null' : JSON.stringify(obj.fachklasse)) + ',';
		result += '"abgangsart" : ' + ((obj.abgangsart === null) ? 'null' : JSON.stringify(obj.abgangsart)) + ',';
		result += '"geburtsjahr" : ' + ((obj.geburtsjahr === null) ? 'null' : JSON.stringify(obj.geburtsjahr)) + ',';
		result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		result += '"abiturfach1" : ' + ((obj.abiturfach1 === null) ? 'null' : JSON.stringify(obj.abiturfach1)) + ',';
		result += '"abiturfach1Aufgabenfeld" : ' + ((obj.abiturfach1Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach1Aufgabenfeld)) + ',';
		result += '"abiturfach1Fachtyp" : ' + ((obj.abiturfach1Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach1Fachtyp)) + ',';
		result += '"abiturfach2" : ' + ((obj.abiturfach2 === null) ? 'null' : JSON.stringify(obj.abiturfach2)) + ',';
		result += '"abiturfach2Aufgabenfeld" : ' + ((obj.abiturfach2Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach2Aufgabenfeld)) + ',';
		result += '"abiturfach2Fachtyp" : ' + ((obj.abiturfach2Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach2Fachtyp)) + ',';
		result += '"abiturfach3" : ' + ((obj.abiturfach3 === null) ? 'null' : JSON.stringify(obj.abiturfach3)) + ',';
		result += '"abiturfach3Aufgabenfeld" : ' + ((obj.abiturfach3Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach3Aufgabenfeld)) + ',';
		result += '"abiturfach3Fachtyp" : ' + ((obj.abiturfach3Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach3Fachtyp)) + ',';
		result += '"abiturfach4" : ' + ((obj.abiturfach4 === null) ? 'null' : JSON.stringify(obj.abiturfach4)) + ',';
		result += '"abiturfach4Aufgabenfeld" : ' + ((obj.abiturfach4Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach4Aufgabenfeld)) + ',';
		result += '"abiturfach4Fachtyp" : ' + ((obj.abiturfach4Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach4Fachtyp)) + ',';
		result += '"abiturnote" : ' + ((obj.abiturnote === null) ? 'null' : JSON.stringify(obj.abiturnote)) + ',';
		result += '"nationalitaet" : ' + ((obj.nationalitaet === null) ? 'null' : JSON.stringify(obj.nationalitaet)) + ',';
		result += '"abiturstatus" : ' + ((obj.abiturstatus === null) ? 'null' : JSON.stringify(obj.abiturstatus)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AbiturpruefungsergebnisseStatistikExport>): string {
		let result = '{';
		if (obj.laufendeNummer !== undefined) {
			result += '"laufendeNummer" : ' + ((obj.laufendeNummer === null) ? 'null' : JSON.stringify(obj.laufendeNummer)) + ',';
		}
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
		if (obj.abgangsart !== undefined) {
			result += '"abgangsart" : ' + ((obj.abgangsart === null) ? 'null' : JSON.stringify(obj.abgangsart)) + ',';
		}
		if (obj.geburtsjahr !== undefined) {
			result += '"geburtsjahr" : ' + ((obj.geburtsjahr === null) ? 'null' : JSON.stringify(obj.geburtsjahr)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + ((obj.geschlecht === null) ? 'null' : JSON.stringify(obj.geschlecht)) + ',';
		}
		if (obj.abiturfach1 !== undefined) {
			result += '"abiturfach1" : ' + ((obj.abiturfach1 === null) ? 'null' : JSON.stringify(obj.abiturfach1)) + ',';
		}
		if (obj.abiturfach1Aufgabenfeld !== undefined) {
			result += '"abiturfach1Aufgabenfeld" : ' + ((obj.abiturfach1Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach1Aufgabenfeld)) + ',';
		}
		if (obj.abiturfach1Fachtyp !== undefined) {
			result += '"abiturfach1Fachtyp" : ' + ((obj.abiturfach1Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach1Fachtyp)) + ',';
		}
		if (obj.abiturfach2 !== undefined) {
			result += '"abiturfach2" : ' + ((obj.abiturfach2 === null) ? 'null' : JSON.stringify(obj.abiturfach2)) + ',';
		}
		if (obj.abiturfach2Aufgabenfeld !== undefined) {
			result += '"abiturfach2Aufgabenfeld" : ' + ((obj.abiturfach2Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach2Aufgabenfeld)) + ',';
		}
		if (obj.abiturfach2Fachtyp !== undefined) {
			result += '"abiturfach2Fachtyp" : ' + ((obj.abiturfach2Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach2Fachtyp)) + ',';
		}
		if (obj.abiturfach3 !== undefined) {
			result += '"abiturfach3" : ' + ((obj.abiturfach3 === null) ? 'null' : JSON.stringify(obj.abiturfach3)) + ',';
		}
		if (obj.abiturfach3Aufgabenfeld !== undefined) {
			result += '"abiturfach3Aufgabenfeld" : ' + ((obj.abiturfach3Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach3Aufgabenfeld)) + ',';
		}
		if (obj.abiturfach3Fachtyp !== undefined) {
			result += '"abiturfach3Fachtyp" : ' + ((obj.abiturfach3Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach3Fachtyp)) + ',';
		}
		if (obj.abiturfach4 !== undefined) {
			result += '"abiturfach4" : ' + ((obj.abiturfach4 === null) ? 'null' : JSON.stringify(obj.abiturfach4)) + ',';
		}
		if (obj.abiturfach4Aufgabenfeld !== undefined) {
			result += '"abiturfach4Aufgabenfeld" : ' + ((obj.abiturfach4Aufgabenfeld === null) ? 'null' : JSON.stringify(obj.abiturfach4Aufgabenfeld)) + ',';
		}
		if (obj.abiturfach4Fachtyp !== undefined) {
			result += '"abiturfach4Fachtyp" : ' + ((obj.abiturfach4Fachtyp === null) ? 'null' : JSON.stringify(obj.abiturfach4Fachtyp)) + ',';
		}
		if (obj.abiturnote !== undefined) {
			result += '"abiturnote" : ' + ((obj.abiturnote === null) ? 'null' : JSON.stringify(obj.abiturnote)) + ',';
		}
		if (obj.nationalitaet !== undefined) {
			result += '"nationalitaet" : ' + ((obj.nationalitaet === null) ? 'null' : JSON.stringify(obj.nationalitaet)) + ',';
		}
		if (obj.abiturstatus !== undefined) {
			result += '"abiturstatus" : ' + ((obj.abiturstatus === null) ? 'null' : JSON.stringify(obj.abiturstatus)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_AbiturpruefungsergebnisseStatistikExport(obj: unknown): AbiturpruefungsergebnisseStatistikExport {
	return obj as AbiturpruefungsergebnisseStatistikExport;
}
