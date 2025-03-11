import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerLeistungsdaten } from '../../../asd/data/schueler/SchuelerLeistungsdaten';
import { SchuelerLernabschnittNachpruefungsdaten } from '../../../asd/data/schueler/SchuelerLernabschnittNachpruefungsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { SchuelerLernabschnittBemerkungen } from '../../../asd/data/schueler/SchuelerLernabschnittBemerkungen';

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
	public wechselNr : number = 0;

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
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 */
	public klassenID : number | null = null;

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
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 */
	public jahrgangID : number | null = null;

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
	public leistungsdaten : List<SchuelerLeistungsdaten> = new ArrayList<SchuelerLeistungsdaten>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten'].includes(name);
	}

	public static class = new Class<SchuelerLernabschnittsdaten>('de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten');

	public static transpilerFromJSON(json : string): SchuelerLernabschnittsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittsdaten>;
		const result = new SchuelerLernabschnittsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.schuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		if (obj.wechselNr === undefined)
			throw new Error('invalid json format, missing attribute wechselNr');
		result.wechselNr = obj.wechselNr;
		result.datumAnfang = (obj.datumAnfang === undefined) ? null : obj.datumAnfang === null ? null : obj.datumAnfang;
		result.datumEnde = (obj.datumEnde === undefined) ? null : obj.datumEnde === null ? null : obj.datumEnde;
		result.datumKonferenz = (obj.datumKonferenz === undefined) ? null : obj.datumKonferenz === null ? null : obj.datumKonferenz;
		result.datumZeugnis = (obj.datumZeugnis === undefined) ? null : obj.datumZeugnis === null ? null : obj.datumZeugnis;
		result.anzahlSchulbesuchsjahre = (obj.anzahlSchulbesuchsjahre === undefined) ? null : obj.anzahlSchulbesuchsjahre === null ? null : obj.anzahlSchulbesuchsjahre;
		if (obj.istGewertet === undefined)
			throw new Error('invalid json format, missing attribute istGewertet');
		result.istGewertet = obj.istGewertet;
		if (obj.istWiederholung === undefined)
			throw new Error('invalid json format, missing attribute istWiederholung');
		result.istWiederholung = obj.istWiederholung;
		if (obj.pruefungsOrdnung === undefined)
			throw new Error('invalid json format, missing attribute pruefungsOrdnung');
		result.pruefungsOrdnung = obj.pruefungsOrdnung;
		result.klassenID = (obj.klassenID === undefined) ? null : obj.klassenID === null ? null : obj.klassenID;
		result.tutorID = (obj.tutorID === undefined) ? null : obj.tutorID === null ? null : obj.tutorID;
		result.folgeklassenID = (obj.folgeklassenID === undefined) ? null : obj.folgeklassenID === null ? null : obj.folgeklassenID;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.jahrgangID = (obj.jahrgangID === undefined) ? null : obj.jahrgangID === null ? null : obj.jahrgangID;
		result.fachklasseID = (obj.fachklasseID === undefined) ? null : obj.fachklasseID === null ? null : obj.fachklasseID;
		result.schwerpunktID = (obj.schwerpunktID === undefined) ? null : obj.schwerpunktID === null ? null : obj.schwerpunktID;
		result.organisationsform = (obj.organisationsform === undefined) ? null : obj.organisationsform === null ? null : obj.organisationsform;
		result.Klassenart = (obj.Klassenart === undefined) ? null : obj.Klassenart === null ? null : obj.Klassenart;
		if (obj.fehlstundenGesamt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenGesamt');
		result.fehlstundenGesamt = obj.fehlstundenGesamt;
		if (obj.fehlstundenUnentschuldigt === undefined)
			throw new Error('invalid json format, missing attribute fehlstundenUnentschuldigt');
		result.fehlstundenUnentschuldigt = obj.fehlstundenUnentschuldigt;
		result.fehlstundenGrenzwert = (obj.fehlstundenGrenzwert === undefined) ? null : obj.fehlstundenGrenzwert === null ? null : obj.fehlstundenGrenzwert;
		if (obj.hatSchwerbehinderungsNachweis === undefined)
			throw new Error('invalid json format, missing attribute hatSchwerbehinderungsNachweis');
		result.hatSchwerbehinderungsNachweis = obj.hatSchwerbehinderungsNachweis;
		if (obj.hatAOSF === undefined)
			throw new Error('invalid json format, missing attribute hatAOSF');
		result.hatAOSF = obj.hatAOSF;
		if (obj.hatAutismus === undefined)
			throw new Error('invalid json format, missing attribute hatAutismus');
		result.hatAutismus = obj.hatAutismus;
		if (obj.hatZieldifferentenUnterricht === undefined)
			throw new Error('invalid json format, missing attribute hatZieldifferentenUnterricht');
		result.hatZieldifferentenUnterricht = obj.hatZieldifferentenUnterricht;
		result.foerderschwerpunkt1ID = (obj.foerderschwerpunkt1ID === undefined) ? null : obj.foerderschwerpunkt1ID === null ? null : obj.foerderschwerpunkt1ID;
		result.foerderschwerpunkt2ID = (obj.foerderschwerpunkt2ID === undefined) ? null : obj.foerderschwerpunkt2ID === null ? null : obj.foerderschwerpunkt2ID;
		result.sonderpaedagogeID = (obj.sonderpaedagogeID === undefined) ? null : obj.sonderpaedagogeID === null ? null : obj.sonderpaedagogeID;
		result.bilingualerZweig = (obj.bilingualerZweig === undefined) ? null : obj.bilingualerZweig === null ? null : obj.bilingualerZweig;
		if (obj.istFachpraktischerAnteilAusreichend === undefined)
			throw new Error('invalid json format, missing attribute istFachpraktischerAnteilAusreichend');
		result.istFachpraktischerAnteilAusreichend = obj.istFachpraktischerAnteilAusreichend;
		result.versetzungsvermerk = (obj.versetzungsvermerk === undefined) ? null : obj.versetzungsvermerk === null ? null : obj.versetzungsvermerk;
		result.noteDurchschnitt = (obj.noteDurchschnitt === undefined) ? null : obj.noteDurchschnitt === null ? null : obj.noteDurchschnitt;
		result.noteLernbereichGSbzwAL = (obj.noteLernbereichGSbzwAL === undefined) ? null : obj.noteLernbereichGSbzwAL === null ? null : obj.noteLernbereichGSbzwAL;
		result.noteLernbereichNW = (obj.noteLernbereichNW === undefined) ? null : obj.noteLernbereichNW === null ? null : obj.noteLernbereichNW;
		result.abschlussart = (obj.abschlussart === undefined) ? null : obj.abschlussart === null ? null : obj.abschlussart;
		if (obj.istAbschlussPrognose === undefined)
			throw new Error('invalid json format, missing attribute istAbschlussPrognose');
		result.istAbschlussPrognose = obj.istAbschlussPrognose;
		result.abschluss = (obj.abschluss === undefined) ? null : obj.abschluss === null ? null : obj.abschluss;
		result.abschlussBerufsbildend = (obj.abschlussBerufsbildend === undefined) ? null : obj.abschlussBerufsbildend === null ? null : obj.abschlussBerufsbildend;
		result.textErgebnisPruefungsalgorithmus = (obj.textErgebnisPruefungsalgorithmus === undefined) ? null : obj.textErgebnisPruefungsalgorithmus === null ? null : obj.textErgebnisPruefungsalgorithmus;
		result.zeugnisart = (obj.zeugnisart === undefined) ? null : obj.zeugnisart === null ? null : obj.zeugnisart;
		result.nachpruefungen = ((obj.nachpruefungen === undefined) || (obj.nachpruefungen === null)) ? null : SchuelerLernabschnittNachpruefungsdaten.transpilerFromJSON(JSON.stringify(obj.nachpruefungen));
		if (obj.bemerkungen === undefined)
			throw new Error('invalid json format, missing attribute bemerkungen');
		result.bemerkungen = SchuelerLernabschnittBemerkungen.transpilerFromJSON(JSON.stringify(obj.bemerkungen));
		if (obj.leistungsdaten !== undefined) {
			for (const elem of obj.leistungsdaten) {
				result.leistungsdaten.add(SchuelerLeistungsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		result += '"datumAnfang" : ' + ((obj.datumAnfang === null) ? 'null' : JSON.stringify(obj.datumAnfang)) + ',';
		result += '"datumEnde" : ' + ((obj.datumEnde === null) ? 'null' : JSON.stringify(obj.datumEnde)) + ',';
		result += '"datumKonferenz" : ' + ((obj.datumKonferenz === null) ? 'null' : JSON.stringify(obj.datumKonferenz)) + ',';
		result += '"datumZeugnis" : ' + ((obj.datumZeugnis === null) ? 'null' : JSON.stringify(obj.datumZeugnis)) + ',';
		result += '"anzahlSchulbesuchsjahre" : ' + ((obj.anzahlSchulbesuchsjahre === null) ? 'null' : obj.anzahlSchulbesuchsjahre.toString()) + ',';
		result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		result += '"tutorID" : ' + ((obj.tutorID === null) ? 'null' : obj.tutorID.toString()) + ',';
		result += '"folgeklassenID" : ' + ((obj.folgeklassenID === null) ? 'null' : obj.folgeklassenID.toString()) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		result += '"fachklasseID" : ' + ((obj.fachklasseID === null) ? 'null' : obj.fachklasseID.toString()) + ',';
		result += '"schwerpunktID" : ' + ((obj.schwerpunktID === null) ? 'null' : obj.schwerpunktID.toString()) + ',';
		result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
		result += '"Klassenart" : ' + ((obj.Klassenart === null) ? 'null' : JSON.stringify(obj.Klassenart)) + ',';
		result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		result += '"fehlstundenGrenzwert" : ' + ((obj.fehlstundenGrenzwert === null) ? 'null' : obj.fehlstundenGrenzwert.toString()) + ',';
		result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		result += '"hatAOSF" : ' + obj.hatAOSF.toString() + ',';
		result += '"hatAutismus" : ' + obj.hatAutismus.toString() + ',';
		result += '"hatZieldifferentenUnterricht" : ' + obj.hatZieldifferentenUnterricht.toString() + ',';
		result += '"foerderschwerpunkt1ID" : ' + ((obj.foerderschwerpunkt1ID === null) ? 'null' : obj.foerderschwerpunkt1ID.toString()) + ',';
		result += '"foerderschwerpunkt2ID" : ' + ((obj.foerderschwerpunkt2ID === null) ? 'null' : obj.foerderschwerpunkt2ID.toString()) + ',';
		result += '"sonderpaedagogeID" : ' + ((obj.sonderpaedagogeID === null) ? 'null' : obj.sonderpaedagogeID.toString()) + ',';
		result += '"bilingualerZweig" : ' + ((obj.bilingualerZweig === null) ? 'null' : JSON.stringify(obj.bilingualerZweig)) + ',';
		result += '"istFachpraktischerAnteilAusreichend" : ' + obj.istFachpraktischerAnteilAusreichend.toString() + ',';
		result += '"versetzungsvermerk" : ' + ((obj.versetzungsvermerk === null) ? 'null' : JSON.stringify(obj.versetzungsvermerk)) + ',';
		result += '"noteDurchschnitt" : ' + ((obj.noteDurchschnitt === null) ? 'null' : JSON.stringify(obj.noteDurchschnitt)) + ',';
		result += '"noteLernbereichGSbzwAL" : ' + ((obj.noteLernbereichGSbzwAL === null) ? 'null' : obj.noteLernbereichGSbzwAL.toString()) + ',';
		result += '"noteLernbereichNW" : ' + ((obj.noteLernbereichNW === null) ? 'null' : obj.noteLernbereichNW.toString()) + ',';
		result += '"abschlussart" : ' + ((obj.abschlussart === null) ? 'null' : obj.abschlussart.toString()) + ',';
		result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose.toString() + ',';
		result += '"abschluss" : ' + ((obj.abschluss === null) ? 'null' : JSON.stringify(obj.abschluss)) + ',';
		result += '"abschlussBerufsbildend" : ' + ((obj.abschlussBerufsbildend === null) ? 'null' : JSON.stringify(obj.abschlussBerufsbildend)) + ',';
		result += '"textErgebnisPruefungsalgorithmus" : ' + ((obj.textErgebnisPruefungsalgorithmus === null) ? 'null' : JSON.stringify(obj.textErgebnisPruefungsalgorithmus)) + ',';
		result += '"zeugnisart" : ' + ((obj.zeugnisart === null) ? 'null' : JSON.stringify(obj.zeugnisart)) + ',';
		result += '"nachpruefungen" : ' + ((obj.nachpruefungen === null) ? 'null' : SchuelerLernabschnittNachpruefungsdaten.transpilerToJSON(obj.nachpruefungen)) + ',';
		result += '"bemerkungen" : ' + SchuelerLernabschnittBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		result += '"leistungsdaten" : [ ';
		for (let i = 0; i < obj.leistungsdaten.size(); i++) {
			const elem = obj.leistungsdaten.get(i);
			result += SchuelerLeistungsdaten.transpilerToJSON(elem);
			if (i < obj.leistungsdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittsdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.schuljahresabschnitt !== undefined) {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		}
		if (obj.wechselNr !== undefined) {
			result += '"wechselNr" : ' + obj.wechselNr.toString() + ',';
		}
		if (obj.datumAnfang !== undefined) {
			result += '"datumAnfang" : ' + ((obj.datumAnfang === null) ? 'null' : JSON.stringify(obj.datumAnfang)) + ',';
		}
		if (obj.datumEnde !== undefined) {
			result += '"datumEnde" : ' + ((obj.datumEnde === null) ? 'null' : JSON.stringify(obj.datumEnde)) + ',';
		}
		if (obj.datumKonferenz !== undefined) {
			result += '"datumKonferenz" : ' + ((obj.datumKonferenz === null) ? 'null' : JSON.stringify(obj.datumKonferenz)) + ',';
		}
		if (obj.datumZeugnis !== undefined) {
			result += '"datumZeugnis" : ' + ((obj.datumZeugnis === null) ? 'null' : JSON.stringify(obj.datumZeugnis)) + ',';
		}
		if (obj.anzahlSchulbesuchsjahre !== undefined) {
			result += '"anzahlSchulbesuchsjahre" : ' + ((obj.anzahlSchulbesuchsjahre === null) ? 'null' : obj.anzahlSchulbesuchsjahre.toString()) + ',';
		}
		if (obj.istGewertet !== undefined) {
			result += '"istGewertet" : ' + obj.istGewertet.toString() + ',';
		}
		if (obj.istWiederholung !== undefined) {
			result += '"istWiederholung" : ' + obj.istWiederholung.toString() + ',';
		}
		if (obj.pruefungsOrdnung !== undefined) {
			result += '"pruefungsOrdnung" : ' + JSON.stringify(obj.pruefungsOrdnung) + ',';
		}
		if (obj.klassenID !== undefined) {
			result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		}
		if (obj.tutorID !== undefined) {
			result += '"tutorID" : ' + ((obj.tutorID === null) ? 'null' : obj.tutorID.toString()) + ',';
		}
		if (obj.folgeklassenID !== undefined) {
			result += '"folgeklassenID" : ' + ((obj.folgeklassenID === null) ? 'null' : obj.folgeklassenID.toString()) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.jahrgangID !== undefined) {
			result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		}
		if (obj.fachklasseID !== undefined) {
			result += '"fachklasseID" : ' + ((obj.fachklasseID === null) ? 'null' : obj.fachklasseID.toString()) + ',';
		}
		if (obj.schwerpunktID !== undefined) {
			result += '"schwerpunktID" : ' + ((obj.schwerpunktID === null) ? 'null' : obj.schwerpunktID.toString()) + ',';
		}
		if (obj.organisationsform !== undefined) {
			result += '"organisationsform" : ' + ((obj.organisationsform === null) ? 'null' : JSON.stringify(obj.organisationsform)) + ',';
		}
		if (obj.Klassenart !== undefined) {
			result += '"Klassenart" : ' + ((obj.Klassenart === null) ? 'null' : JSON.stringify(obj.Klassenart)) + ',';
		}
		if (obj.fehlstundenGesamt !== undefined) {
			result += '"fehlstundenGesamt" : ' + obj.fehlstundenGesamt.toString() + ',';
		}
		if (obj.fehlstundenUnentschuldigt !== undefined) {
			result += '"fehlstundenUnentschuldigt" : ' + obj.fehlstundenUnentschuldigt.toString() + ',';
		}
		if (obj.fehlstundenGrenzwert !== undefined) {
			result += '"fehlstundenGrenzwert" : ' + ((obj.fehlstundenGrenzwert === null) ? 'null' : obj.fehlstundenGrenzwert.toString()) + ',';
		}
		if (obj.hatSchwerbehinderungsNachweis !== undefined) {
			result += '"hatSchwerbehinderungsNachweis" : ' + obj.hatSchwerbehinderungsNachweis.toString() + ',';
		}
		if (obj.hatAOSF !== undefined) {
			result += '"hatAOSF" : ' + obj.hatAOSF.toString() + ',';
		}
		if (obj.hatAutismus !== undefined) {
			result += '"hatAutismus" : ' + obj.hatAutismus.toString() + ',';
		}
		if (obj.hatZieldifferentenUnterricht !== undefined) {
			result += '"hatZieldifferentenUnterricht" : ' + obj.hatZieldifferentenUnterricht.toString() + ',';
		}
		if (obj.foerderschwerpunkt1ID !== undefined) {
			result += '"foerderschwerpunkt1ID" : ' + ((obj.foerderschwerpunkt1ID === null) ? 'null' : obj.foerderschwerpunkt1ID.toString()) + ',';
		}
		if (obj.foerderschwerpunkt2ID !== undefined) {
			result += '"foerderschwerpunkt2ID" : ' + ((obj.foerderschwerpunkt2ID === null) ? 'null' : obj.foerderschwerpunkt2ID.toString()) + ',';
		}
		if (obj.sonderpaedagogeID !== undefined) {
			result += '"sonderpaedagogeID" : ' + ((obj.sonderpaedagogeID === null) ? 'null' : obj.sonderpaedagogeID.toString()) + ',';
		}
		if (obj.bilingualerZweig !== undefined) {
			result += '"bilingualerZweig" : ' + ((obj.bilingualerZweig === null) ? 'null' : JSON.stringify(obj.bilingualerZweig)) + ',';
		}
		if (obj.istFachpraktischerAnteilAusreichend !== undefined) {
			result += '"istFachpraktischerAnteilAusreichend" : ' + obj.istFachpraktischerAnteilAusreichend.toString() + ',';
		}
		if (obj.versetzungsvermerk !== undefined) {
			result += '"versetzungsvermerk" : ' + ((obj.versetzungsvermerk === null) ? 'null' : JSON.stringify(obj.versetzungsvermerk)) + ',';
		}
		if (obj.noteDurchschnitt !== undefined) {
			result += '"noteDurchschnitt" : ' + ((obj.noteDurchschnitt === null) ? 'null' : JSON.stringify(obj.noteDurchschnitt)) + ',';
		}
		if (obj.noteLernbereichGSbzwAL !== undefined) {
			result += '"noteLernbereichGSbzwAL" : ' + ((obj.noteLernbereichGSbzwAL === null) ? 'null' : obj.noteLernbereichGSbzwAL.toString()) + ',';
		}
		if (obj.noteLernbereichNW !== undefined) {
			result += '"noteLernbereichNW" : ' + ((obj.noteLernbereichNW === null) ? 'null' : obj.noteLernbereichNW.toString()) + ',';
		}
		if (obj.abschlussart !== undefined) {
			result += '"abschlussart" : ' + ((obj.abschlussart === null) ? 'null' : obj.abschlussart.toString()) + ',';
		}
		if (obj.istAbschlussPrognose !== undefined) {
			result += '"istAbschlussPrognose" : ' + obj.istAbschlussPrognose.toString() + ',';
		}
		if (obj.abschluss !== undefined) {
			result += '"abschluss" : ' + ((obj.abschluss === null) ? 'null' : JSON.stringify(obj.abschluss)) + ',';
		}
		if (obj.abschlussBerufsbildend !== undefined) {
			result += '"abschlussBerufsbildend" : ' + ((obj.abschlussBerufsbildend === null) ? 'null' : JSON.stringify(obj.abschlussBerufsbildend)) + ',';
		}
		if (obj.textErgebnisPruefungsalgorithmus !== undefined) {
			result += '"textErgebnisPruefungsalgorithmus" : ' + ((obj.textErgebnisPruefungsalgorithmus === null) ? 'null' : JSON.stringify(obj.textErgebnisPruefungsalgorithmus)) + ',';
		}
		if (obj.zeugnisart !== undefined) {
			result += '"zeugnisart" : ' + ((obj.zeugnisart === null) ? 'null' : JSON.stringify(obj.zeugnisart)) + ',';
		}
		if (obj.nachpruefungen !== undefined) {
			result += '"nachpruefungen" : ' + ((obj.nachpruefungen === null) ? 'null' : SchuelerLernabschnittNachpruefungsdaten.transpilerToJSON(obj.nachpruefungen)) + ',';
		}
		if (obj.bemerkungen !== undefined) {
			result += '"bemerkungen" : ' + SchuelerLernabschnittBemerkungen.transpilerToJSON(obj.bemerkungen) + ',';
		}
		if (obj.leistungsdaten !== undefined) {
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

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerLernabschnittsdaten(obj : unknown) : SchuelerLernabschnittsdaten {
	return obj as SchuelerLernabschnittsdaten;
}
