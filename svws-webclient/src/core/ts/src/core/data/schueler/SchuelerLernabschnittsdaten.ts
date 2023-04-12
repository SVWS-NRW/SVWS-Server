import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerLeistungsdaten } from '../../../core/data/schueler/SchuelerLeistungsdaten';
import { SchuelerLernabschnittNachpruefungsdaten } from '../../../core/data/schueler/SchuelerLernabschnittNachpruefungsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';
import { SchuelerLernabschnittBemerkungen } from '../../../core/data/schueler/SchuelerLernabschnittBemerkungen';

export class SchuelerLernabschnittsdaten extends JavaObject {

	/**
	 * Die ID des Lernabschnitts in der Datenbank.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers, zu dem diese Lernabschnittdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören.
	 */
	public schuljahresabschnitt : number = 0;

	/**
	 * Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.).
	 */
	public wechselNr : number | null = null;

	/**
	 * Das Datum, wann der Lernabschnitt beginnt
	 */
	public datumAnfang : string | null = null;

	/**
	 * Das Datum, wann der Lernabschnitt endet
	 */
	public datumEnde : string | null = null;

	/**
	 * Das Datum der Konferenz
	 */
	public datumKonferenz : string | null = null;

	/**
	 * Das Datum des Zeugnisses bzw. der Laufbahnbescheinigung
	 */
	public datumZeugnis : string | null = null;

	/**
	 * Die Anzahl der Schulbesuchsjahre
	 */
	public anzahlSchulbesuchsjahre : number | null = null;

	/**
	 * Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht
	 */
	public istGewertet : boolean = true;

	/**
	 * Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht
	 */
	public istWiederholung : boolean = false;

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.
	 */
	public pruefungsOrdnung : string = "";

	/**
	 * Die ID der Klasse des Schülers.
	 */
	public klassenID : number = -1;

	/**
	 * Die ID eines Tutors, der den Schüler betreut, oder null, falls keiner zugewiesen ist
	 */
	public tutorID : number | null = null;

	/**
	 * Die ID der Folge-Klasse des Schülers, sofern dieser vom Standard der Klassentabelle abweicht.
	 */
	public folgeklassenID : number | null = null;

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers.
	 */
	public schulgliederung : string | null = null;

	/**
	 * Die ID des Jahrgangs des Schülers
	 */
	public jahrgangID : number = -1;

	/**
	 * Die ID der Fachklasse des Schülers an einem Berufskolleg
	 */
	public fachklasseID : number | null = null;

	/**
	 * Der Schwerpunkt eines Schülers laut dem Schwerpunkt-Katalog
	 */
	public schwerpunktID : number | null = null;

	/**
	 * Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type)
	 */
	public organisationsform : string | null = null;

	/**
	 * Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type)
	 */
	public Klassenart : string | null = "RK";

	/**
	 * Die Summe der Gesamtfehlstunden für den gesamten Lernabschnitt
	 */
	public fehlstundenGesamt : number = 0;

	/**
	 * Die Summe der unentschuldigten Fehlstunden für den gesamten Lernabschnitt
	 */
	public fehlstundenUnentschuldigt : number = 0;

	/**
	 * Der Grenzwert für die Fehlstunden, ab dem am Berufskolleg Warnbriefe zur Entlassung verschickt werden
	 */
	public fehlstundenGrenzwert : number | null = null;

	/**
	 * Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht
	 */
	public hatSchwerbehinderungsNachweis : boolean = false;

	/**
	 * Gibt an, ob eine Förderung nach der Ausbildungsordnung Sonderpädagogischer Förderung (AOSF) vorliegt
	 */
	public hatAOSF : boolean = false;

	/**
	 * Gibt an, ob eine Diagnose zu Autismus vorliegt oder nicht
	 */
	public hatAutismus : boolean = false;

	/**
	 * Gibt an, ob zieldifferent unterrichet wird oder nicht
	 */
	public hatZieldifferentenUnterricht : boolean = false;

	/**
	 * Die ID des Haupförderschwerpunktes des Schülers
	 */
	public foerderschwerpunkt1ID : number | null = null;

	/**
	 * Die ID des weiteren Förderschwerpunktes des Schülers
	 */
	public foerderschwerpunkt2ID : number | null = null;

	/**
	 * Die ID eines Sonderpädagogen, der den Schüler betreut und auch im Notenmodul hat
	 */
	public sonderpaedagogeID : number | null = null;

	/**
	 * Die Sprache des bilngualen Zweigs, falls der Schüler im bilingualen Zweig unterrichtet wird
	 */
	public bilingualerZweig : string | null = null;

	/**
	 * Gibt für das Berufskolleg an, ob der fachpraktische Anteil in den Anlagen B08, B09 und B10 ausreichend sind für Versetzung
	 */
	public istFachpraktischerAnteilAusreichend : boolean = true;

	/**
	 * Das Kürzel des Versetzungsvermerks
	 */
	public versetzungsvermerk : string | null = null;

	/**
	 * Die Durchschnittsnote in diesem Lernabschnitt - wird ggf. von einem Prüfungsalgorithmus gesetzt und kann dann ausgelesen werden
	 */
	public noteDurchschnitt : string | null = null;

	/**
	 * Die Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre für den Hauptschulabschluss nach Klassen 10
	 */
	public noteLernbereichGSbzwAL : number | null = null;

	/**
	 * Die Lernbereichnote Naturwissenschaft für den Hauptschulabschluss nach Klassen 10
	 */
	public noteLernbereichNW : number | null = null;

	/**
	 * Die Art des Abschlusses (siehe Katalog)
	 */
	public abschlussart : number | null = null;

	/**
	 * Gibt an, ob der berechnete Abschluss eine Prognose ist oder nicht (siehe Katalog)
	 */
	public istAbschlussPrognose : boolean = false;

	/**
	 * Der erreichte allgemeinbildende Abschluss
	 */
	public abschluss : string | null = null;

	/**
	 * Der erreichte berufsbezogene Abschluss am Berufskolleg
	 */
	public abschlussBerufsbildend : string | null = null;

	/**
	 * Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung
	 */
	public textErgebnisPruefungsalgorithmus : string | null = null;

	/**
	 * Die Art des Zeugnisses
	 */
	public zeugnisart : string | null = null;

	/**
	 * Die Informationen den Nachprüfungen in diesem Lernabschnitt oder null, falls keine vorhanden sind.
	 */
	public nachpruefungen : SchuelerLernabschnittNachpruefungsdaten | null = null;

	/**
	 * Die Bemerkungen in diesem Lernabschnitt.
	 */
	public bemerkungen : SchuelerLernabschnittBemerkungen = new SchuelerLernabschnittBemerkungen();

	/**
	 * Die Leistungsdaten des Schülers in diesem Lernabschnitt.
	 */
	public leistungsdaten : List<SchuelerLeistungsdaten> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLernabschnittsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerLernabschnittsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.schuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		result.wechselNr = typeof obj.wechselNr === "undefined" ? null : obj.wechselNr === null ? null : obj.wechselNr;
		result.datumAnfang = typeof obj.datumAnfang === "undefined" ? null : obj.datumAnfang === null ? null : obj.datumAnfang;
		result.datumEnde = typeof obj.datumEnde === "undefined" ? null : obj.datumEnde === null ? null : obj.datumEnde;
		result.datumKonferenz = typeof obj.datumKonferenz === "undefined" ? null : obj.datumKonferenz === null ? null : obj.datumKonferenz;
		result.datumZeugnis = typeof obj.datumZeugnis === "undefined" ? null : obj.datumZeugnis === null ? null : obj.datumZeugnis;
		result.anzahlSchulbesuchsjahre = typeof obj.anzahlSchulbesuchsjahre === "undefined" ? null : obj.anzahlSchulbesuchsjahre === null ? null : obj.anzahlSchulbesuchsjahre;
		if (typeof obj.istGewertet === "undefined")
			 throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (typeof obj.istWiederholung === "undefined")
			 throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (typeof obj.pruefungsOrdnung === "undefined")
			 throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		if (typeof obj.klassenID === "undefined")
			 throw new Error('invalid json format, missing attribute klassenID');
		result.klassenID = obj.klassenID;
		result.tutorID = typeof obj.tutorID === "undefined" ? null : obj.tutorID === null ? null : obj.tutorID;
		result.folgeklassenID = typeof obj.folgeklassenID === "undefined" ? null : obj.folgeklassenID === null ? null : obj.folgeklassenID;
		result.schulgliederung = typeof obj.schulgliederung === "undefined" ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		if (typeof obj.jahrgangID === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgangID');
		result.jahrgangID = obj.jahrgangID;
		result.fachklasseID = typeof obj.fachklasseID === "undefined" ? null : obj.fachklasseID === null ? null : obj.fachklasseID;
		result.schwerpunktID = typeof obj.schwerpunktID === "undefined" ? null : obj.schwerpunktID === null ? null : obj.schwerpunktID;
		result.organisationsform = typeof obj.organisationsform === "undefined" ? null : obj.organisationsform === null ? null : obj.organisationsform;
		result.Klassenart = typeof obj.Klassenart === "undefined" ? null : obj.Klassenart === null ? null : obj.Klassenart;
		if (typeof obj.fehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (typeof obj.fehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		result.fehlstundenGrenzwert = typeof obj.fehlstundenGrenzwert === "undefined" ? null : obj.fehlstundenGrenzwert === null ? null : obj.fehlstundenGrenzwert;
		if (typeof obj.hatSchwerbehinderungsNachweis === "undefined")
			 throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		if (typeof obj.hatAOSF === "undefined")
			 throw new Error('invalid json format, missing attribute hatAOSF');
		result.hatAOSF = obj.hatAOSF;
		if (typeof obj.hatAutismus === "undefined")
			 throw new Error('invalid json format, missing attribute hatAutismus');
		result.hatAutismus = obj.hatAutismus;
		if (typeof obj.hatZieldifferentenUnterricht === "undefined")
			 throw new Error('invalid json format, missing attribute hatZieldifferentenUnterricht');
		result.hatZieldifferentenUnterricht = obj.hatZieldifferentenUnterricht;
		result.foerderschwerpunkt1ID = typeof obj.foerderschwerpunkt1ID === "undefined" ? null : obj.foerderschwerpunkt1ID === null ? null : obj.foerderschwerpunkt1ID;
		result.foerderschwerpunkt2ID = typeof obj.foerderschwerpunkt2ID === "undefined" ? null : obj.foerderschwerpunkt2ID === null ? null : obj.foerderschwerpunkt2ID;
		result.sonderpaedagogeID = typeof obj.sonderpaedagogeID === "undefined" ? null : obj.sonderpaedagogeID === null ? null : obj.sonderpaedagogeID;
		result.bilingualerZweig = typeof obj.bilingualerZweig === "undefined" ? null : obj.bilingualerZweig === null ? null : obj.bilingualerZweig;
		if (typeof obj.istFachpraktischerAnteilAusreichend === "undefined")
			 throw new Error('invalid json format, missing attribute istFachpraktischerAnteilAusreichend');
		result.istFachpraktischerAnteilAusreichend = obj.istFachpraktischerAnteilAusreichend;
		result.versetzungsvermerk = typeof obj.versetzungsvermerk === "undefined" ? null : obj.versetzungsvermerk === null ? null : obj.versetzungsvermerk;
		result.noteDurchschnitt = typeof obj.noteDurchschnitt === "undefined" ? null : obj.noteDurchschnitt === null ? null : obj.noteDurchschnitt;
		result.noteLernbereichGSbzwAL = typeof obj.noteLernbereichGSbzwAL === "undefined" ? null : obj.noteLernbereichGSbzwAL === null ? null : obj.noteLernbereichGSbzwAL;
		result.noteLernbereichNW = typeof obj.noteLernbereichNW === "undefined" ? null : obj.noteLernbereichNW === null ? null : obj.noteLernbereichNW;
		result.abschlussart = typeof obj.abschlussart === "undefined" ? null : obj.abschlussart === null ? null : obj.abschlussart;
		if (typeof obj.istAbschlussPrognose === "undefined")
			 throw new Error('invalid json format, missing attribute istAbschlussPrognose');
		result.istAbschlussPrognose = obj.istAbschlussPrognose;
		result.abschluss = typeof obj.abschluss === "undefined" ? null : obj.abschluss === null ? null : obj.abschluss;
		result.abschlussBerufsbildend = typeof obj.abschlussBerufsbildend === "undefined" ? null : obj.abschlussBerufsbildend === null ? null : obj.abschlussBerufsbildend;
		result.textErgebnisPruefungsalgorithmus = typeof obj.textErgebnisPruefungsalgorithmus === "undefined" ? null : obj.textErgebnisPruefungsalgorithmus === null ? null : obj.textErgebnisPruefungsalgorithmus;
		result.zeugnisart = typeof obj.zeugnisart === "undefined" ? null : obj.zeugnisart === null ? null : obj.zeugnisart;
		result.nachpruefungen = ((typeof obj.nachpruefungen === "undefined") || (obj.nachpruefungen === null)) ? null : SchuelerLernabschnittNachpruefungsdaten.transpilerFromJSON(JSON.stringify(obj.nachpruefungen));
		if (typeof obj.bemerkungen === "undefined")
			 throw new Error('invalid json format, missing attribute bemerkungen');
		result.bemerkungen = SchuelerLernabschnittBemerkungen.transpilerFromJSON(JSON.stringify(obj.bemerkungen));
		if ((obj.leistungsdaten !== undefined) && (obj.leistungsdaten !== null)) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten?.add(SchuelerLeistungsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt + ',';
		result += '"wechselNr" : ' + ((!obj.wechselNr) ? 'null' : obj.wechselNr) + ',';
		result += '"datumAnfang" : ' + ((!obj.datumAnfang) ? 'null' : '"' + obj.datumAnfang + '"') + ',';
		result += '"datumEnde" : ' + ((!obj.datumEnde) ? 'null' : '"' + obj.datumEnde + '"') + ',';
		result += '"datumKonferenz" : ' + ((!obj.datumKonferenz) ? 'null' : '"' + obj.datumKonferenz + '"') + ',';
		result += '"datumZeugnis" : ' + ((!obj.datumZeugnis) ? 'null' : '"' + obj.datumZeugnis + '"') + ',';
		result += '"anzahlSchulbesuchsjahre" : ' + ((!obj.anzahlSchulbesuchsjahre) ? 'null' : obj.anzahlSchulbesuchsjahre) + ',';
		result += '"istGewertet" : ' + obj.istGewertet + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		result += '"pruefungsOrdnung" : ' + '"' + obj.pruefungsOrdnung! + '"' + ',';
		result += '"klassenID" : ' + obj.klassenID + ',';
		result += '"tutorID" : ' + ((!obj.tutorID) ? 'null' : obj.tutorID) + ',';
		result += '"folgeklassenID" : ' + ((!obj.folgeklassenID) ? 'null' : obj.folgeklassenID) + ',';
		result += '"schulgliederung" : ' + ((!obj.schulgliederung) ? 'null' : '"' + obj.schulgliederung + '"') + ',';
		result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		result += '"fachklasseID" : ' + ((!obj.fachklasseID) ? 'null' : obj.fachklasseID) + ',';
		result += '"schwerpunktID" : ' + ((!obj.schwerpunktID) ? 'null' : obj.schwerpunktID) + ',';
		result += '"organisationsform" : ' + ((!obj.organisationsform) ? 'null' : '"' + obj.organisationsform + '"') + ',';
		result += '"Klassenart" : ' + ((!obj.Klassenart) ? 'null' : '"' + obj.Klassenart + '"') + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		result += '"fehlstundenGrenzwert" : ' + ((!obj.fehlstundenGrenzwert) ? 'null' : obj.fehlstundenGrenzwert) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis + ',';
		result += '"hatAOSF" : ' + obj.hatAOSF + ',';
		result += '"hatAutismus" : ' + obj.hatAutismus + ',';
		result += '"hatZieldifferentenUnterricht" : ' + obj.hatZieldifferentenUnterricht + ',';
		result += '"foerderschwerpunkt1ID" : ' + ((!obj.foerderschwerpunkt1ID) ? 'null' : obj.foerderschwerpunkt1ID) + ',';
		result += '"foerderschwerpunkt2ID" : ' + ((!obj.foerderschwerpunkt2ID) ? 'null' : obj.foerderschwerpunkt2ID) + ',';
		result += '"sonderpaedagogeID" : ' + ((!obj.sonderpaedagogeID) ? 'null' : obj.sonderpaedagogeID) + ',';
		result += '"bilingualerZweig" : ' + ((!obj.bilingualerZweig) ? 'null' : '"' + obj.bilingualerZweig + '"') + ',';
		result += '"istFachpraktischerAnteilAusreichend" : ' + obj.istFachpraktischerAnteilAusreichend + ',';
		result += '"versetzungsvermerk" : ' + ((!obj.versetzungsvermerk) ? 'null' : '"' + obj.versetzungsvermerk + '"') + ',';
		result += '"noteDurchschnitt" : ' + ((!obj.noteDurchschnitt) ? 'null' : '"' + obj.noteDurchschnitt + '"') + ',';
		result += '"noteLernbereichGSbzwAL" : ' + ((!obj.noteLernbereichGSbzwAL) ? 'null' : obj.noteLernbereichGSbzwAL) + ',';
		result += '"noteLernbereichNW" : ' + ((!obj.noteLernbereichNW) ? 'null' : obj.noteLernbereichNW) + ',';
		result += '"abschlussart" : ' + ((!obj.abschlussart) ? 'null' : obj.abschlussart) + ',';
		result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose + ',';
		result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss + '"') + ',';
		result += '"abschlussBerufsbildend" : ' + ((!obj.abschlussBerufsbildend) ? 'null' : '"' + obj.abschlussBerufsbildend + '"') + ',';
		result += '"textErgebnisPruefungsalgorithmus" : ' + ((!obj.textErgebnisPruefungsalgorithmus) ? 'null' : '"' + obj.textErgebnisPruefungsalgorithmus + '"') + ',';
		result += '"zeugnisart" : ' + ((!obj.zeugnisart) ? 'null' : '"' + obj.zeugnisart + '"') + ',';
		result += '"nachpruefungen" : ' + ((!obj.nachpruefungen) ? 'null' : SchuelerLernabschnittNachpruefungsdaten.transpilerToJSON(obj.nachpruefungen)) + ',';
		result += '"bemerkungen" : ' + SchuelerLernabschnittBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		if (!obj.leistungsdaten) {
			result += '"leistungsdaten" : []';
		} else {
			result += '"leistungsdaten" : [ ';
			for (let i = 0; i < obj.leistungsdaten.size(); i++) {
				const elem = obj.leistungsdaten.get(i);
				result += SchuelerLeistungsdaten.transpilerToJSON(elem);
				if (i < obj.leistungsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.schuljahresabschnitt !== "undefined") {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt + ',';
		}
		if (typeof obj.wechselNr !== "undefined") {
			result += '"wechselNr" : ' + ((!obj.wechselNr) ? 'null' : obj.wechselNr) + ',';
		}
		if (typeof obj.datumAnfang !== "undefined") {
			result += '"datumAnfang" : ' + ((!obj.datumAnfang) ? 'null' : '"' + obj.datumAnfang + '"') + ',';
		}
		if (typeof obj.datumEnde !== "undefined") {
			result += '"datumEnde" : ' + ((!obj.datumEnde) ? 'null' : '"' + obj.datumEnde + '"') + ',';
		}
		if (typeof obj.datumKonferenz !== "undefined") {
			result += '"datumKonferenz" : ' + ((!obj.datumKonferenz) ? 'null' : '"' + obj.datumKonferenz + '"') + ',';
		}
		if (typeof obj.datumZeugnis !== "undefined") {
			result += '"datumZeugnis" : ' + ((!obj.datumZeugnis) ? 'null' : '"' + obj.datumZeugnis + '"') + ',';
		}
		if (typeof obj.anzahlSchulbesuchsjahre !== "undefined") {
			result += '"anzahlSchulbesuchsjahre" : ' + ((!obj.anzahlSchulbesuchsjahre) ? 'null' : obj.anzahlSchulbesuchsjahre) + ',';
		}
		if (typeof obj.istGewertet !== "undefined") {
			result += '"istGewertet" : ' + obj.istGewertet + ',';
		}
		if (typeof obj.istWiederholung !== "undefined") {
			result += '"istWiederholung" : ' + obj.istWiederholung + ',';
		}
		if (typeof obj.pruefungsOrdnung !== "undefined") {
			result += '"pruefungsOrdnung" : ' + '"' + obj.pruefungsOrdnung + '"' + ',';
		}
		if (typeof obj.klassenID !== "undefined") {
			result += '"klassenID" : ' + obj.klassenID + ',';
		}
		if (typeof obj.tutorID !== "undefined") {
			result += '"tutorID" : ' + ((!obj.tutorID) ? 'null' : obj.tutorID) + ',';
		}
		if (typeof obj.folgeklassenID !== "undefined") {
			result += '"folgeklassenID" : ' + ((!obj.folgeklassenID) ? 'null' : obj.folgeklassenID) + ',';
		}
		if (typeof obj.schulgliederung !== "undefined") {
			result += '"schulgliederung" : ' + ((!obj.schulgliederung) ? 'null' : '"' + obj.schulgliederung + '"') + ',';
		}
		if (typeof obj.jahrgangID !== "undefined") {
			result += '"jahrgangID" : ' + obj.jahrgangID + ',';
		}
		if (typeof obj.fachklasseID !== "undefined") {
			result += '"fachklasseID" : ' + ((!obj.fachklasseID) ? 'null' : obj.fachklasseID) + ',';
		}
		if (typeof obj.schwerpunktID !== "undefined") {
			result += '"schwerpunktID" : ' + ((!obj.schwerpunktID) ? 'null' : obj.schwerpunktID) + ',';
		}
		if (typeof obj.organisationsform !== "undefined") {
			result += '"organisationsform" : ' + ((!obj.organisationsform) ? 'null' : '"' + obj.organisationsform + '"') + ',';
		}
		if (typeof obj.Klassenart !== "undefined") {
			result += '"Klassenart" : ' + ((!obj.Klassenart) ? 'null' : '"' + obj.Klassenart + '"') + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt + ',';
		}
		if (typeof obj.fehlstundenGrenzwert !== "undefined") {
			result += '"fehlstundenGrenzwert" : ' + ((!obj.fehlstundenGrenzwert) ? 'null' : obj.fehlstundenGrenzwert) + ',';
		}
		if (typeof obj.hatSchwerbehinderungsNachweis !== "undefined") {
			result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis + ',';
		}
		if (typeof obj.hatAOSF !== "undefined") {
			result += '"hatAOSF" : ' + obj.hatAOSF + ',';
		}
		if (typeof obj.hatAutismus !== "undefined") {
			result += '"hatAutismus" : ' + obj.hatAutismus + ',';
		}
		if (typeof obj.hatZieldifferentenUnterricht !== "undefined") {
			result += '"hatZieldifferentenUnterricht" : ' + obj.hatZieldifferentenUnterricht + ',';
		}
		if (typeof obj.foerderschwerpunkt1ID !== "undefined") {
			result += '"foerderschwerpunkt1ID" : ' + ((!obj.foerderschwerpunkt1ID) ? 'null' : obj.foerderschwerpunkt1ID) + ',';
		}
		if (typeof obj.foerderschwerpunkt2ID !== "undefined") {
			result += '"foerderschwerpunkt2ID" : ' + ((!obj.foerderschwerpunkt2ID) ? 'null' : obj.foerderschwerpunkt2ID) + ',';
		}
		if (typeof obj.sonderpaedagogeID !== "undefined") {
			result += '"sonderpaedagogeID" : ' + ((!obj.sonderpaedagogeID) ? 'null' : obj.sonderpaedagogeID) + ',';
		}
		if (typeof obj.bilingualerZweig !== "undefined") {
			result += '"bilingualerZweig" : ' + ((!obj.bilingualerZweig) ? 'null' : '"' + obj.bilingualerZweig + '"') + ',';
		}
		if (typeof obj.istFachpraktischerAnteilAusreichend !== "undefined") {
			result += '"istFachpraktischerAnteilAusreichend" : ' + obj.istFachpraktischerAnteilAusreichend + ',';
		}
		if (typeof obj.versetzungsvermerk !== "undefined") {
			result += '"versetzungsvermerk" : ' + ((!obj.versetzungsvermerk) ? 'null' : '"' + obj.versetzungsvermerk + '"') + ',';
		}
		if (typeof obj.noteDurchschnitt !== "undefined") {
			result += '"noteDurchschnitt" : ' + ((!obj.noteDurchschnitt) ? 'null' : '"' + obj.noteDurchschnitt + '"') + ',';
		}
		if (typeof obj.noteLernbereichGSbzwAL !== "undefined") {
			result += '"noteLernbereichGSbzwAL" : ' + ((!obj.noteLernbereichGSbzwAL) ? 'null' : obj.noteLernbereichGSbzwAL) + ',';
		}
		if (typeof obj.noteLernbereichNW !== "undefined") {
			result += '"noteLernbereichNW" : ' + ((!obj.noteLernbereichNW) ? 'null' : obj.noteLernbereichNW) + ',';
		}
		if (typeof obj.abschlussart !== "undefined") {
			result += '"abschlussart" : ' + ((!obj.abschlussart) ? 'null' : obj.abschlussart) + ',';
		}
		if (typeof obj.istAbschlussPrognose !== "undefined") {
			result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose + ',';
		}
		if (typeof obj.abschluss !== "undefined") {
			result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss + '"') + ',';
		}
		if (typeof obj.abschlussBerufsbildend !== "undefined") {
			result += '"abschlussBerufsbildend" : ' + ((!obj.abschlussBerufsbildend) ? 'null' : '"' + obj.abschlussBerufsbildend + '"') + ',';
		}
		if (typeof obj.textErgebnisPruefungsalgorithmus !== "undefined") {
			result += '"textErgebnisPruefungsalgorithmus" : ' + ((!obj.textErgebnisPruefungsalgorithmus) ? 'null' : '"' + obj.textErgebnisPruefungsalgorithmus + '"') + ',';
		}
		if (typeof obj.zeugnisart !== "undefined") {
			result += '"zeugnisart" : ' + ((!obj.zeugnisart) ? 'null' : '"' + obj.zeugnisart + '"') + ',';
		}
		if (typeof obj.nachpruefungen !== "undefined") {
			result += '"nachpruefungen" : ' + ((!obj.nachpruefungen) ? 'null' : SchuelerLernabschnittNachpruefungsdaten.transpilerToJSON(obj.nachpruefungen)) + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + SchuelerLernabschnittBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		}
		if (typeof obj.leistungsdaten !== "undefined") {
			if (!obj.leistungsdaten) {
				result += '"leistungsdaten" : []';
			} else {
				result += '"leistungsdaten" : [ ';
				for (let i = 0; i < obj.leistungsdaten.size(); i++) {
					const elem = obj.leistungsdaten.get(i);
					result += SchuelerLeistungsdaten.transpilerToJSON(elem);
					if (i < obj.leistungsdaten.size() - 1)
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

export function cast_de_svws_nrw_core_data_schueler_SchuelerLernabschnittsdaten(obj : unknown) : SchuelerLernabschnittsdaten {
	return obj as SchuelerLernabschnittsdaten;
}
