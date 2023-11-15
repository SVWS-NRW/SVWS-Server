import { JavaObject } from '../../../java/lang/JavaObject';
import { DruckGostLaufbahnplanungSchuelerFehler } from '../../../core/data/druck/DruckGostLaufbahnplanungSchuelerFehler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { DruckGostLaufbahnplanungSchuelerFachwahlen } from '../../../core/data/druck/DruckGostLaufbahnplanungSchuelerFachwahlen';
import { DruckGostLaufbahnplanungSchuelerHinweise } from '../../../core/data/druck/DruckGostLaufbahnplanungSchuelerHinweise';

export class DruckGostLaufbahnplanungSchueler extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public SchuelerID : number = -1;

	/**
	 * Nachname des Schülers.
	 */
	public Nachname : string = "";

	/**
	 * Vorname des Schülers.
	 */
	public Vorname : string = "";

	/**
	 * Geschlecht des Schülers.
	 */
	public Geschlecht : string | null = "";

	/**
	 * Geburtsdatum des Schülers.
	 */
	public Geburtsdatum : string | null = "";

	/**
	 * Externe Schulnummer des Schülers, wenn er den Status extern hat.
	 */
	public ExterneSchulnummer : string | null = "";

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt
	 */
	public Abiturjahr : number = -1;

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, in dem die Beratung erfolgt
	 */
	public AktuellesGOStHalbjahr : string = "";

	/**
	 * Die aktuelle Klasse zum aktuellen Halbjahr der Oberstufenlaufbahn
	 */
	public AktuelleKlasse : string = "";

	/**
	 * Die Prüfungsordnung des Schülers aus dem aktuellen Lernabschnitt
	 */
	public Pruefungsordnung : string = "";

	/**
	 * Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt
	 */
	public BeratungsGOStHalbjahr : string = "";

	/**
	 * Der Text der Schule für den Beratungsbogen
	 */
	public Beratungsbogentext : string = "";

	/**
	 * Der Text der Schule für den E-Mail-Versand
	 */
	public Emailtext : string = "";

	/**
	 * Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt
	 */
	public Beratungslehrkraefte : string = "";

	/**
	 * Die Lehrkraft der letzten Beratung
	 */
	public Beratungslehrkraft : string = "";

	/**
	 * Das Datum des Rücklaufes der letzten importierten Wahldatei
	 */
	public Ruecklaufdatum : string = "";

	/**
	 * Das Datum der letzten Beratung
	 */
	public Beratungsdatum : string = "";

	/**
	 * Das Datum der letzten Beratung
	 */
	public LetzteBeratungText : string = "";

	/**
	 * Kommentar der Schule zur Laufbahn
	 */
	public Kommentar : string = "";

	/**
	 * Kursanzahl in der EF.1
	 */
	public KursanzahlEF1 : number = 0;

	/**
	 * Kursanzahl in der EF.2
	 */
	public KursanzahlEF2 : number = 0;

	/**
	 * Kursanzahl in der Q1.1
	 */
	public KursanzahlQ11 : number = 0;

	/**
	 * Kursanzahl in der Q1.2
	 */
	public KursanzahlQ12 : number = 0;

	/**
	 * Kursanzahl in der Q2.1
	 */
	public KursanzahlQ21 : number = 0;

	/**
	 * Kursanzahl in der Q2.2
	 */
	public KursanzahlQ22 : number = 0;

	/**
	 * Kursanzahl in der Qualifikationsphase
	 */
	public KursanzahlQPh : number = 0;

	/**
	 * Wochenstundensumme in der EF.1
	 */
	public WochenstundenEF1 : number = 0;

	/**
	 * Wochenstundensumme in der EF.2
	 */
	public WochenstundenEF2 : number = 0;

	/**
	 * Wochenstundensumme in der Q1.1
	 */
	public WochenstundenQ11 : number = 0;

	/**
	 * Wochenstundensumme in der Q1.2
	 */
	public WochenstundenQ12 : number = 0;

	/**
	 * Wochenstundensumme in der Q2.1
	 */
	public WochenstundenQ21 : number = 0;

	/**
	 * Wochenstundensumme in der Q2.2
	 */
	public WochenstundenQ22 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public WochenstundenDurchschnittEF : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public WochenstundenDurchschnittQ1 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public WochenstundenDurchschnittQ2 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der Qualifikationsphase
	 */
	public WochenstundenDurchschnittQPh : number = 0;

	/**
	 * Wochenstundensumme der gesamten Laufbahn
	 */
	public WochenstundenGesamt : number = 0;

	/**
	 * Eine Liste vom Typ Fachwahlen, die alle Fachwahlen und deren Daten enthält.
	 */
	public Fachwahlen : List<DruckGostLaufbahnplanungSchuelerFachwahlen> = new ArrayList();

	/**
	 * Eine Liste vom Typ Fehler, die alle Fehler zur Laufbahn und deren Daten enthält.
	 */
	public Fehler : List<DruckGostLaufbahnplanungSchuelerFehler> = new ArrayList();

	/**
	 * Eine Liste vom Typ Hinweise, die alle Hinweise zur Laufbahn und deren Daten enthält.
	 */
	public Hinweise : List<DruckGostLaufbahnplanungSchuelerHinweise> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): DruckGostLaufbahnplanungSchueler {
		const obj = JSON.parse(json);
		const result = new DruckGostLaufbahnplanungSchueler();
		if (typeof obj.SchuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute SchuelerID');
		result.SchuelerID = obj.SchuelerID;
		if (typeof obj.Nachname === "undefined")
			 throw new Error('invalid json format, missing attribute Nachname');
		result.Nachname = obj.Nachname;
		if (typeof obj.Vorname === "undefined")
			 throw new Error('invalid json format, missing attribute Vorname');
		result.Vorname = obj.Vorname;
		result.Geschlecht = typeof obj.Geschlecht === "undefined" ? null : obj.Geschlecht === null ? null : obj.Geschlecht;
		result.Geburtsdatum = typeof obj.Geburtsdatum === "undefined" ? null : obj.Geburtsdatum === null ? null : obj.Geburtsdatum;
		result.ExterneSchulnummer = typeof obj.ExterneSchulnummer === "undefined" ? null : obj.ExterneSchulnummer === null ? null : obj.ExterneSchulnummer;
		if (typeof obj.Abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute Abiturjahr');
		result.Abiturjahr = obj.Abiturjahr;
		if (typeof obj.AktuellesGOStHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute AktuellesGOStHalbjahr');
		result.AktuellesGOStHalbjahr = obj.AktuellesGOStHalbjahr;
		if (typeof obj.AktuelleKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute AktuelleKlasse');
		result.AktuelleKlasse = obj.AktuelleKlasse;
		if (typeof obj.Pruefungsordnung === "undefined")
			 throw new Error('invalid json format, missing attribute Pruefungsordnung');
		result.Pruefungsordnung = obj.Pruefungsordnung;
		if (typeof obj.BeratungsGOStHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute BeratungsGOStHalbjahr');
		result.BeratungsGOStHalbjahr = obj.BeratungsGOStHalbjahr;
		if (typeof obj.Beratungsbogentext === "undefined")
			 throw new Error('invalid json format, missing attribute Beratungsbogentext');
		result.Beratungsbogentext = obj.Beratungsbogentext;
		if (typeof obj.Emailtext === "undefined")
			 throw new Error('invalid json format, missing attribute Emailtext');
		result.Emailtext = obj.Emailtext;
		if (typeof obj.Beratungslehrkraefte === "undefined")
			 throw new Error('invalid json format, missing attribute Beratungslehrkraefte');
		result.Beratungslehrkraefte = obj.Beratungslehrkraefte;
		if (typeof obj.Beratungslehrkraft === "undefined")
			 throw new Error('invalid json format, missing attribute Beratungslehrkraft');
		result.Beratungslehrkraft = obj.Beratungslehrkraft;
		if (typeof obj.Ruecklaufdatum === "undefined")
			 throw new Error('invalid json format, missing attribute Ruecklaufdatum');
		result.Ruecklaufdatum = obj.Ruecklaufdatum;
		if (typeof obj.Beratungsdatum === "undefined")
			 throw new Error('invalid json format, missing attribute Beratungsdatum');
		result.Beratungsdatum = obj.Beratungsdatum;
		if (typeof obj.LetzteBeratungText === "undefined")
			 throw new Error('invalid json format, missing attribute LetzteBeratungText');
		result.LetzteBeratungText = obj.LetzteBeratungText;
		if (typeof obj.Kommentar === "undefined")
			 throw new Error('invalid json format, missing attribute Kommentar');
		result.Kommentar = obj.Kommentar;
		if (typeof obj.KursanzahlEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlEF1');
		result.KursanzahlEF1 = obj.KursanzahlEF1;
		if (typeof obj.KursanzahlEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlEF2');
		result.KursanzahlEF2 = obj.KursanzahlEF2;
		if (typeof obj.KursanzahlQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlQ11');
		result.KursanzahlQ11 = obj.KursanzahlQ11;
		if (typeof obj.KursanzahlQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlQ12');
		result.KursanzahlQ12 = obj.KursanzahlQ12;
		if (typeof obj.KursanzahlQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlQ21');
		result.KursanzahlQ21 = obj.KursanzahlQ21;
		if (typeof obj.KursanzahlQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlQ22');
		result.KursanzahlQ22 = obj.KursanzahlQ22;
		if (typeof obj.KursanzahlQPh === "undefined")
			 throw new Error('invalid json format, missing attribute KursanzahlQPh');
		result.KursanzahlQPh = obj.KursanzahlQPh;
		if (typeof obj.WochenstundenEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenEF1');
		result.WochenstundenEF1 = obj.WochenstundenEF1;
		if (typeof obj.WochenstundenEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenEF2');
		result.WochenstundenEF2 = obj.WochenstundenEF2;
		if (typeof obj.WochenstundenQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenQ11');
		result.WochenstundenQ11 = obj.WochenstundenQ11;
		if (typeof obj.WochenstundenQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenQ12');
		result.WochenstundenQ12 = obj.WochenstundenQ12;
		if (typeof obj.WochenstundenQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenQ21');
		result.WochenstundenQ21 = obj.WochenstundenQ21;
		if (typeof obj.WochenstundenQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenQ22');
		result.WochenstundenQ22 = obj.WochenstundenQ22;
		if (typeof obj.WochenstundenDurchschnittEF === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenDurchschnittEF');
		result.WochenstundenDurchschnittEF = obj.WochenstundenDurchschnittEF;
		if (typeof obj.WochenstundenDurchschnittQ1 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenDurchschnittQ1');
		result.WochenstundenDurchschnittQ1 = obj.WochenstundenDurchschnittQ1;
		if (typeof obj.WochenstundenDurchschnittQ2 === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenDurchschnittQ2');
		result.WochenstundenDurchschnittQ2 = obj.WochenstundenDurchschnittQ2;
		if (typeof obj.WochenstundenDurchschnittQPh === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenDurchschnittQPh');
		result.WochenstundenDurchschnittQPh = obj.WochenstundenDurchschnittQPh;
		if (typeof obj.WochenstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute WochenstundenGesamt');
		result.WochenstundenGesamt = obj.WochenstundenGesamt;
		if ((obj.Fachwahlen !== undefined) && (obj.Fachwahlen !== null)) {
			for (const elem of obj.Fachwahlen) {
				result.Fachwahlen?.add(DruckGostLaufbahnplanungSchuelerFachwahlen.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.Fehler !== undefined) && (obj.Fehler !== null)) {
			for (const elem of obj.Fehler) {
				result.Fehler?.add(DruckGostLaufbahnplanungSchuelerFehler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.Hinweise !== undefined) && (obj.Hinweise !== null)) {
			for (const elem of obj.Hinweise) {
				result.Hinweise?.add(DruckGostLaufbahnplanungSchuelerHinweise.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : DruckGostLaufbahnplanungSchueler) : string {
		let result = '{';
		result += '"SchuelerID" : ' + obj.SchuelerID + ',';
		result += '"Nachname" : ' + JSON.stringify(obj.Nachname!) + ',';
		result += '"Vorname" : ' + JSON.stringify(obj.Vorname!) + ',';
		result += '"Geschlecht" : ' + ((!obj.Geschlecht) ? 'null' : JSON.stringify(obj.Geschlecht)) + ',';
		result += '"Geburtsdatum" : ' + ((!obj.Geburtsdatum) ? 'null' : JSON.stringify(obj.Geburtsdatum)) + ',';
		result += '"ExterneSchulnummer" : ' + ((!obj.ExterneSchulnummer) ? 'null' : JSON.stringify(obj.ExterneSchulnummer)) + ',';
		result += '"Abiturjahr" : ' + obj.Abiturjahr + ',';
		result += '"AktuellesGOStHalbjahr" : ' + JSON.stringify(obj.AktuellesGOStHalbjahr!) + ',';
		result += '"AktuelleKlasse" : ' + JSON.stringify(obj.AktuelleKlasse!) + ',';
		result += '"Pruefungsordnung" : ' + JSON.stringify(obj.Pruefungsordnung!) + ',';
		result += '"BeratungsGOStHalbjahr" : ' + JSON.stringify(obj.BeratungsGOStHalbjahr!) + ',';
		result += '"Beratungsbogentext" : ' + JSON.stringify(obj.Beratungsbogentext!) + ',';
		result += '"Emailtext" : ' + JSON.stringify(obj.Emailtext!) + ',';
		result += '"Beratungslehrkraefte" : ' + JSON.stringify(obj.Beratungslehrkraefte!) + ',';
		result += '"Beratungslehrkraft" : ' + JSON.stringify(obj.Beratungslehrkraft!) + ',';
		result += '"Ruecklaufdatum" : ' + JSON.stringify(obj.Ruecklaufdatum!) + ',';
		result += '"Beratungsdatum" : ' + JSON.stringify(obj.Beratungsdatum!) + ',';
		result += '"LetzteBeratungText" : ' + JSON.stringify(obj.LetzteBeratungText!) + ',';
		result += '"Kommentar" : ' + JSON.stringify(obj.Kommentar!) + ',';
		result += '"KursanzahlEF1" : ' + obj.KursanzahlEF1 + ',';
		result += '"KursanzahlEF2" : ' + obj.KursanzahlEF2 + ',';
		result += '"KursanzahlQ11" : ' + obj.KursanzahlQ11 + ',';
		result += '"KursanzahlQ12" : ' + obj.KursanzahlQ12 + ',';
		result += '"KursanzahlQ21" : ' + obj.KursanzahlQ21 + ',';
		result += '"KursanzahlQ22" : ' + obj.KursanzahlQ22 + ',';
		result += '"KursanzahlQPh" : ' + obj.KursanzahlQPh + ',';
		result += '"WochenstundenEF1" : ' + obj.WochenstundenEF1 + ',';
		result += '"WochenstundenEF2" : ' + obj.WochenstundenEF2 + ',';
		result += '"WochenstundenQ11" : ' + obj.WochenstundenQ11 + ',';
		result += '"WochenstundenQ12" : ' + obj.WochenstundenQ12 + ',';
		result += '"WochenstundenQ21" : ' + obj.WochenstundenQ21 + ',';
		result += '"WochenstundenQ22" : ' + obj.WochenstundenQ22 + ',';
		result += '"WochenstundenDurchschnittEF" : ' + obj.WochenstundenDurchschnittEF + ',';
		result += '"WochenstundenDurchschnittQ1" : ' + obj.WochenstundenDurchschnittQ1 + ',';
		result += '"WochenstundenDurchschnittQ2" : ' + obj.WochenstundenDurchschnittQ2 + ',';
		result += '"WochenstundenDurchschnittQPh" : ' + obj.WochenstundenDurchschnittQPh + ',';
		result += '"WochenstundenGesamt" : ' + obj.WochenstundenGesamt + ',';
		if (!obj.Fachwahlen) {
			result += '"Fachwahlen" : []';
		} else {
			result += '"Fachwahlen" : [ ';
			for (let i = 0; i < obj.Fachwahlen.size(); i++) {
				const elem = obj.Fachwahlen.get(i);
				result += DruckGostLaufbahnplanungSchuelerFachwahlen.transpilerToJSON(elem);
				if (i < obj.Fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.Fehler) {
			result += '"Fehler" : []';
		} else {
			result += '"Fehler" : [ ';
			for (let i = 0; i < obj.Fehler.size(); i++) {
				const elem = obj.Fehler.get(i);
				result += DruckGostLaufbahnplanungSchuelerFehler.transpilerToJSON(elem);
				if (i < obj.Fehler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.Hinweise) {
			result += '"Hinweise" : []';
		} else {
			result += '"Hinweise" : [ ';
			for (let i = 0; i < obj.Hinweise.size(); i++) {
				const elem = obj.Hinweise.get(i);
				result += DruckGostLaufbahnplanungSchuelerHinweise.transpilerToJSON(elem);
				if (i < obj.Hinweise.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DruckGostLaufbahnplanungSchueler>) : string {
		let result = '{';
		if (typeof obj.SchuelerID !== "undefined") {
			result += '"SchuelerID" : ' + obj.SchuelerID + ',';
		}
		if (typeof obj.Nachname !== "undefined") {
			result += '"Nachname" : ' + JSON.stringify(obj.Nachname!) + ',';
		}
		if (typeof obj.Vorname !== "undefined") {
			result += '"Vorname" : ' + JSON.stringify(obj.Vorname!) + ',';
		}
		if (typeof obj.Geschlecht !== "undefined") {
			result += '"Geschlecht" : ' + ((!obj.Geschlecht) ? 'null' : JSON.stringify(obj.Geschlecht)) + ',';
		}
		if (typeof obj.Geburtsdatum !== "undefined") {
			result += '"Geburtsdatum" : ' + ((!obj.Geburtsdatum) ? 'null' : JSON.stringify(obj.Geburtsdatum)) + ',';
		}
		if (typeof obj.ExterneSchulnummer !== "undefined") {
			result += '"ExterneSchulnummer" : ' + ((!obj.ExterneSchulnummer) ? 'null' : JSON.stringify(obj.ExterneSchulnummer)) + ',';
		}
		if (typeof obj.Abiturjahr !== "undefined") {
			result += '"Abiturjahr" : ' + obj.Abiturjahr + ',';
		}
		if (typeof obj.AktuellesGOStHalbjahr !== "undefined") {
			result += '"AktuellesGOStHalbjahr" : ' + JSON.stringify(obj.AktuellesGOStHalbjahr!) + ',';
		}
		if (typeof obj.AktuelleKlasse !== "undefined") {
			result += '"AktuelleKlasse" : ' + JSON.stringify(obj.AktuelleKlasse!) + ',';
		}
		if (typeof obj.Pruefungsordnung !== "undefined") {
			result += '"Pruefungsordnung" : ' + JSON.stringify(obj.Pruefungsordnung!) + ',';
		}
		if (typeof obj.BeratungsGOStHalbjahr !== "undefined") {
			result += '"BeratungsGOStHalbjahr" : ' + JSON.stringify(obj.BeratungsGOStHalbjahr!) + ',';
		}
		if (typeof obj.Beratungsbogentext !== "undefined") {
			result += '"Beratungsbogentext" : ' + JSON.stringify(obj.Beratungsbogentext!) + ',';
		}
		if (typeof obj.Emailtext !== "undefined") {
			result += '"Emailtext" : ' + JSON.stringify(obj.Emailtext!) + ',';
		}
		if (typeof obj.Beratungslehrkraefte !== "undefined") {
			result += '"Beratungslehrkraefte" : ' + JSON.stringify(obj.Beratungslehrkraefte!) + ',';
		}
		if (typeof obj.Beratungslehrkraft !== "undefined") {
			result += '"Beratungslehrkraft" : ' + JSON.stringify(obj.Beratungslehrkraft!) + ',';
		}
		if (typeof obj.Ruecklaufdatum !== "undefined") {
			result += '"Ruecklaufdatum" : ' + JSON.stringify(obj.Ruecklaufdatum!) + ',';
		}
		if (typeof obj.Beratungsdatum !== "undefined") {
			result += '"Beratungsdatum" : ' + JSON.stringify(obj.Beratungsdatum!) + ',';
		}
		if (typeof obj.LetzteBeratungText !== "undefined") {
			result += '"LetzteBeratungText" : ' + JSON.stringify(obj.LetzteBeratungText!) + ',';
		}
		if (typeof obj.Kommentar !== "undefined") {
			result += '"Kommentar" : ' + JSON.stringify(obj.Kommentar!) + ',';
		}
		if (typeof obj.KursanzahlEF1 !== "undefined") {
			result += '"KursanzahlEF1" : ' + obj.KursanzahlEF1 + ',';
		}
		if (typeof obj.KursanzahlEF2 !== "undefined") {
			result += '"KursanzahlEF2" : ' + obj.KursanzahlEF2 + ',';
		}
		if (typeof obj.KursanzahlQ11 !== "undefined") {
			result += '"KursanzahlQ11" : ' + obj.KursanzahlQ11 + ',';
		}
		if (typeof obj.KursanzahlQ12 !== "undefined") {
			result += '"KursanzahlQ12" : ' + obj.KursanzahlQ12 + ',';
		}
		if (typeof obj.KursanzahlQ21 !== "undefined") {
			result += '"KursanzahlQ21" : ' + obj.KursanzahlQ21 + ',';
		}
		if (typeof obj.KursanzahlQ22 !== "undefined") {
			result += '"KursanzahlQ22" : ' + obj.KursanzahlQ22 + ',';
		}
		if (typeof obj.KursanzahlQPh !== "undefined") {
			result += '"KursanzahlQPh" : ' + obj.KursanzahlQPh + ',';
		}
		if (typeof obj.WochenstundenEF1 !== "undefined") {
			result += '"WochenstundenEF1" : ' + obj.WochenstundenEF1 + ',';
		}
		if (typeof obj.WochenstundenEF2 !== "undefined") {
			result += '"WochenstundenEF2" : ' + obj.WochenstundenEF2 + ',';
		}
		if (typeof obj.WochenstundenQ11 !== "undefined") {
			result += '"WochenstundenQ11" : ' + obj.WochenstundenQ11 + ',';
		}
		if (typeof obj.WochenstundenQ12 !== "undefined") {
			result += '"WochenstundenQ12" : ' + obj.WochenstundenQ12 + ',';
		}
		if (typeof obj.WochenstundenQ21 !== "undefined") {
			result += '"WochenstundenQ21" : ' + obj.WochenstundenQ21 + ',';
		}
		if (typeof obj.WochenstundenQ22 !== "undefined") {
			result += '"WochenstundenQ22" : ' + obj.WochenstundenQ22 + ',';
		}
		if (typeof obj.WochenstundenDurchschnittEF !== "undefined") {
			result += '"WochenstundenDurchschnittEF" : ' + obj.WochenstundenDurchschnittEF + ',';
		}
		if (typeof obj.WochenstundenDurchschnittQ1 !== "undefined") {
			result += '"WochenstundenDurchschnittQ1" : ' + obj.WochenstundenDurchschnittQ1 + ',';
		}
		if (typeof obj.WochenstundenDurchschnittQ2 !== "undefined") {
			result += '"WochenstundenDurchschnittQ2" : ' + obj.WochenstundenDurchschnittQ2 + ',';
		}
		if (typeof obj.WochenstundenDurchschnittQPh !== "undefined") {
			result += '"WochenstundenDurchschnittQPh" : ' + obj.WochenstundenDurchschnittQPh + ',';
		}
		if (typeof obj.WochenstundenGesamt !== "undefined") {
			result += '"WochenstundenGesamt" : ' + obj.WochenstundenGesamt + ',';
		}
		if (typeof obj.Fachwahlen !== "undefined") {
			if (!obj.Fachwahlen) {
				result += '"Fachwahlen" : []';
			} else {
				result += '"Fachwahlen" : [ ';
				for (let i = 0; i < obj.Fachwahlen.size(); i++) {
					const elem = obj.Fachwahlen.get(i);
					result += DruckGostLaufbahnplanungSchuelerFachwahlen.transpilerToJSON(elem);
					if (i < obj.Fachwahlen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.Fehler !== "undefined") {
			if (!obj.Fehler) {
				result += '"Fehler" : []';
			} else {
				result += '"Fehler" : [ ';
				for (let i = 0; i < obj.Fehler.size(); i++) {
					const elem = obj.Fehler.get(i);
					result += DruckGostLaufbahnplanungSchuelerFehler.transpilerToJSON(elem);
					if (i < obj.Fehler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.Hinweise !== "undefined") {
			if (!obj.Hinweise) {
				result += '"Hinweise" : []';
			} else {
				result += '"Hinweise" : [ ';
				for (let i = 0; i < obj.Hinweise.size(); i++) {
					const elem = obj.Hinweise.get(i);
					result += DruckGostLaufbahnplanungSchuelerHinweise.transpilerToJSON(elem);
					if (i < obj.Hinweise.size() - 1)
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

export function cast_de_svws_nrw_core_data_druck_DruckGostLaufbahnplanungSchueler(obj : unknown) : DruckGostLaufbahnplanungSchueler {
	return obj as DruckGostLaufbahnplanungSchueler;
}
