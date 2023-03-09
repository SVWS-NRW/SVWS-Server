import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KlausurblockungSchienenOutput, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutput } from '../../core/data/klausurblockung/KlausurblockungSchienenOutput';
import { HashMap, cast_java_util_HashMap } from '../../java/util/HashMap';
import { KlausurblockungSchienenInput, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInput } from '../../core/data/klausurblockung/KlausurblockungSchienenInput';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { KlausurblockungException, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungException } from '../../core/klausurblockung/KlausurblockungException';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { JavaMapEntry, cast_java_util_Map_Entry } from '../../java/util/JavaMapEntry';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KlausurblockungSchienenOutputKlausur, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenOutputKlausur } from '../../core/data/klausurblockung/KlausurblockungSchienenOutputKlausur';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { KlausurblockungSchienenInputSchueler, cast_de_nrw_schule_svws_core_data_klausurblockung_KlausurblockungSchienenInputSchueler } from '../../core/data/klausurblockung/KlausurblockungSchienenInputSchueler';

export class KlausurblockungSchienenDynDaten extends JavaObject {

	private static readonly SCHIENEN_MAX_ANZAHL : number = 1000;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. 
	 */
	private readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler. 
	 */
	private readonly _logger : Logger;

	/**
	 * Die Datenbank-ID der zugehörigen Klausurblockung. Sie muss positiv sein, sonst wird ein Fehler erzeugt. 
	 */
	private readonly _datenbankID : number;

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. 
	 */
	private readonly _mapKlausurZuNummer : HashMap<number, number> = new HashMap();

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. 
	 */
	private readonly _mapSchuelerZuNummer : HashMap<number, number> = new HashMap();

	/**
	 * Die Anzahl der Klausuren. 
	 */
	private readonly _klausurenAnzahl : number;

	/**
	 * Jeder Klausurnummer wird eine Schiene zugeordnet. Der Wert -1 definiert eine temporäre Nicht-Zuordnung. Am Ende
	 *  des Algorithmus hat jede Klausur einen Wert >= 0. Und die Speicher-Zustände. 
	 */
	private readonly _klausurZuSchiene : Array<number>;

	private readonly _klausurZuSchiene1 : Array<number>;

	private readonly _klausurZuSchiene2 : Array<number>;

	/**
	 * Jeder Klausurnummer wird der Knotengrad (Anzahl an Nachbarn) zugeordnet. 
	 */
	private readonly _klausurnummerZuGrad : Array<number>;

	/**
	 * Ein Array aller Klausurnummern, sortiert nach ihrem Knotengrad (Anzahl an Nachbarn). 
	 */
	private readonly _klausurenSortiertGrad : Array<number>;

	/**
	 * Bestimmt, ob ein Klausurnummer-Paar in der selben Schiene verboten ist. 
	 */
	private readonly _verboten : Array<Array<boolean>>;

	/**
	 * Die Anzahl der derzeitigen verwendeten Schienen. Und die Speicher-Zustände. 
	 */
	private _schienenAnzahl : number = 0;

	private _schienenAnzahl1 : number = KlausurblockungSchienenDynDaten.SCHIENEN_MAX_ANZAHL;

	private _schienenAnzahl2 : number = KlausurblockungSchienenDynDaten.SCHIENEN_MAX_ANZAHL;


	/**
	 *Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur als Basis für die
	 * Algorithmen zur schnellen Manipulation.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI). 
	 */
	constructor(pRandom : Random, pLogger : Logger, pInput : KlausurblockungSchienenInput) {
		super();
		this._random = pRandom;
		this._logger = pLogger;
		this._datenbankID = pInput.datenbankID;
		if (this._datenbankID < 0) 
			throw this.fehler("Die Datenbank-ID der Klausurblockung darf nicht negativ (" + this._datenbankID + ") sein!")
		this.initialisiereMapSchueler(pInput);
		this.initialisiereMapKlausuren(pInput);
		this._klausurenAnzahl = this._mapKlausurZuNummer.size();
		this._klausurZuSchiene = Array(this._klausurenAnzahl).fill(0);
		this._klausurnummerZuGrad = Array(this._klausurenAnzahl).fill(0);
		this._klausurenSortiertGrad = this.gibErzeugeKlausurenInReihenfolge();
		this._klausurZuSchiene1 = Array(this._klausurenAnzahl).fill(0);
		this._klausurZuSchiene2 = Array(this._klausurenAnzahl).fill(0);
		this._verboten = [...Array(this._klausurenAnzahl)].map(e => Array(this._klausurenAnzahl).fill(false));
		this.initialisiereMatrixVerboten(pInput);
		this.initialisiereKnotenGrad();
		this.aktionKlausurenAusSchienenEntfernen();
	}

	/**
	 *Teilt dem Logger einen Fehler mit. <br>
	 * TODO BAR Datenstruktur leeren.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. 
	 */
	private fehler(fehlermeldung : string) : KlausurblockungException | null {
		this._logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new KlausurblockungException(fehlermeldung);
	}

	private initialisiereMatrixVerboten(pInput : KlausurblockungSchienenInput) : void {
		for (let schueler of pInput.schueler) {
			for (let klausurID1 of schueler.klausuren) {
				for (let klausurID2 of schueler.klausuren) {
					let klausurNr1 : number | null = this._mapKlausurZuNummer.get(klausurID1);
					let klausurNr2 : number | null = this._mapKlausurZuNummer.get(klausurID2);
					if (klausurNr1 === null) 
						throw this.fehler("NULL-Wert beim Mapping von klausurID1 --> " + klausurID1!)
					if (klausurNr2 === null) 
						throw this.fehler("NULL-Wert beim Mapping von klausurID2 --> " + klausurID2!)
					this._verboten[klausurNr1.valueOf()][klausurNr2.valueOf()] = true;
				}
			}
		}
	}

	private initialisiereKnotenGrad() : void {
		for (let klausurNr1 : number = 0; klausurNr1 < this._klausurenAnzahl; klausurNr1++){
			let kanten : number = 0;
			for (let klausurNr2 : number = 0; klausurNr2 < this._klausurenAnzahl; klausurNr2++)
				if (this._verboten[klausurNr1][klausurNr2]) 
					kanten++;
			this._klausurnummerZuGrad[klausurNr1] = kanten;
		}
		for (let i : number = 1; i < this._klausurenAnzahl; i++)
			for (let j : number = i; j >= 1; j--){
				let nummerR : number = this._klausurenSortiertGrad[j];
				let nummerL : number = this._klausurenSortiertGrad[j - 1];
				if (this._klausurnummerZuGrad[nummerL] >= this._klausurnummerZuGrad[nummerR]) 
					break;
				this._klausurenSortiertGrad[j] = nummerL;
				this._klausurenSortiertGrad[j - 1] = nummerR;
			}
	}

	private initialisiereMapSchueler(pInput : KlausurblockungSchienenInput) : void {
		for (let schueler of pInput.schueler) {
			let schuelerID : number = schueler.id;
			if (schuelerID < 0) 
				throw this.fehler("pInput.schueler.id=" + schuelerID + " ist negativ!")
			if (this._mapSchuelerZuNummer.containsKey(schuelerID)) 
				throw this.fehler("pInput.schueler.id=" + schuelerID + " wurde doppelt definiert!")
			let schuelerNummer : number = this._mapSchuelerZuNummer.size();
			this._mapSchuelerZuNummer.put(schuelerID, schuelerNummer);
		}
	}

	private initialisiereMapKlausuren(pInput : KlausurblockungSchienenInput) : void {
		for (let schueler of pInput.schueler) 
			for (let klausurID of schueler.klausuren) {
				if (klausurID < 0) 
					throw this.fehler("pInput.schueler.klausuren hat eine negative Klausur-ID=" + klausurID! + "!")
				if (this._mapKlausurZuNummer.containsKey(klausurID)) 
					continue;
				let klausurNummer : number = this._mapKlausurZuNummer.size();
				this._mapKlausurZuNummer.put(klausurID, klausurNummer);
			}
	}

	/**
	 *Liefert ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer.
	 * 
	 * @return ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer. 
	 */
	private gibErzeugeKlausurenInReihenfolge() : Array<number> {
		let temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = i;
		return temp;
	}

	/**
	 *Liefert ein Ausgabe-Objekt, welches jeder Klausur genau eine Schiene zuordnet.
	 * 
	 * @return ein Ausgabe-Objekt, welches jeder Klausur genau eine Schiene zuordnet. 
	 */
	gibErzeugeOutput() : KlausurblockungSchienenOutput {
		let out : KlausurblockungSchienenOutput | null = new KlausurblockungSchienenOutput();
		out.datenbankID = this._datenbankID;
		out.schienenAnzahl = this._schienenAnzahl;
		for (let e of this._mapKlausurZuNummer.entrySet()) {
			let klausurID : number | null = e.getKey();
			let klausurNr : number | null = e.getValue();
			if (klausurID === null) 
				throw this.fehler("gibErzeugeOutput(): NULL-Wert bei \'klausurID\'!")
			if (klausurNr === null) 
				throw this.fehler("gibErzeugeOutput(): NULL-Wert bei \'klausurNr\'!")
			let schiene : number = this._klausurZuSchiene[klausurNr.valueOf()];
			if (schiene < 0) 
				throw this.fehler("gibErzeugeOutput(): negativer Wert bei \'schiene\'!")
			if (schiene >= this._schienenAnzahl) 
				throw this.fehler("gibErzeugeOutput(): zu großer Wert bei \'schiene\'!")
			let klausur : KlausurblockungSchienenOutputKlausur | null = new KlausurblockungSchienenOutputKlausur();
			klausur.id = klausurID.valueOf();
			klausur.schiene = schiene;
			out.klausuren.add(klausur);
		}
		return out;
	}

	/**
	 *Liefert ein Array aller Klausurnummern in zufälliger Reihenfolge.
	 * 
	 * @return ein Array aller Klausurnummern in zufälliger Reihenfolge. 
	 */
	gibErzeugeKlausurenInZufaelligerReihenfolge() : Array<number> {
		let temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = i;
		for (let i1 : number = 0; i1 < this._klausurenAnzahl; i1++){
			let i2 : number = this._random.nextInt(this._klausurenAnzahl);
			let save1 : number = temp[i1];
			let save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 *Liefert ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 * 
	 * @return ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst. 
	 */
	gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert() : Array<number> {
		let temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = this._klausurenSortiertGrad[i];
		for (let i1 : number = 0; i1 < this._klausurenAnzahl; i1++){
			let i2 : number = this._random.nextInt(this._klausurenAnzahl);
			if ((i1 - i2) * (i1 - i2) > this._klausurenAnzahl) 
				continue;
			let save1 : number = temp[i1];
			let save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 *Liefert ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 * 
	 * @return ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst. 
	 */
	gibErzeugeKlausurenMitHoeheremGradZuerst() : Array<number> {
		let temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = this._klausurenSortiertGrad[i];
		return temp;
	}

	/**
	 *Liefert ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge.
	 * 
	 * @return ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge. 
	 */
	gibErzeugeSchienenInZufaelligerReihenfolge() : Array<number> {
		let temp : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		for (let i : number = 0; i < this._schienenAnzahl; i++)
			temp[i] = i;
		for (let i1 : number = 0; i1 < this._schienenAnzahl; i1++){
			let i2 : number = this._random.nextInt(this._schienenAnzahl);
			let save1 : number = temp[i1];
			let save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 *Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 * 
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist. 
	 */
	gibIstBesserAlsZustand1() : boolean {
		return this.gibVergleicheMitAktuellemZustand(this._schienenAnzahl1, this._klausurZuSchiene1);
	}

	/**
	 *Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 * 
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist. 
	 */
	gibIstBesserAlsZustand2() : boolean {
		return this.gibVergleicheMitAktuellemZustand(this._schienenAnzahl2, this._klausurZuSchiene2);
	}

	private gibVergleicheMitAktuellemZustand(schienenAnzahlX : number, klausurZuSchieneX : Array<number>) : boolean {
		if (this._schienenAnzahl < schienenAnzahlX) 
			return true;
		if (this._schienenAnzahl > schienenAnzahlX) 
			return false;
		let histogramm : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		let histogrammX : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++){
			histogramm[this._klausurZuSchiene[i]]++;
			histogrammX[klausurZuSchieneX[i]]++;
		}
		let minHisto : number = this._klausurenAnzahl;
		let minHistoX : number = this._klausurenAnzahl;
		for (let i : number = 0; i < this._schienenAnzahl; i++){
			minHisto = Math.min(minHisto, histogramm[i]);
			minHistoX = Math.min(minHistoX, histogrammX[i]);
		}
		return minHisto < minHistoX;
	}

	/**
	 *Liefert die Anzahl noch nicht verteilter Klausuren.
	 * 
	 * @return die Anzahl noch nicht verteilter Klausuren. 
	 */
	gibAnzahlNichtverteilterKlausuren() : number {
		let summe : number = 0;
		for (let klausurNr : number = 0; klausurNr < this._klausurenAnzahl; klausurNr++)
			if (this._klausurZuSchiene[klausurNr] < 0) 
				summe++;
		return summe;
	}

	/**
	 *Liefert die Anzahl an Klausuren.
	 * 
	 * @return die Anzahl an Klausuren. 
	 */
	gibAnzahlKlausuren() : number {
		return this._klausurenAnzahl;
	}

	/**
	 *Liefert TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde.
	 * 
	 * @param  klausurNr die Klausurnummer, nach der gefragt wird.
	 * @return           TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde. 
	 */
	gibIstKlausurUnverteilt(klausurNr : number) : boolean {
		return this._klausurZuSchiene[klausurNr] < 0;
	}

	/**
	 *Liefert die Nummer einer neu erzeugten Schiene.
	 * 
	 * @return die Nummer einer neu erzeugten Schiene. 
	 */
	gibErzeugeNeueSchiene() : number {
		this._schienenAnzahl++;
		return this._schienenAnzahl - 1;
	}

	/**
	 *Liefert die Anzahl der derzeit verwendeten Schienen.
	 * 
	 * @return die Anzahl der derzeit verwendeten Schienen. 
	 */
	gibAnzahlSchienen() : number {
		return this._schienenAnzahl;
	}

	/**
	 *Liefert den freien Knoten (Klausur), der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. Falls
	 * mehrere Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 * 
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. 
	 */
	gibKlausurDieFreiIstMitDenMeistenNachbarsfarben() : number {
		let maxFarben : number = -1;
		let maxNr : number = -1;
		for (let klausurNr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[klausurNr] >= 0) 
				continue;
			let farben : number = this.gibNachbarsfarbenDerKlausur(klausurNr);
			if (farben < maxFarben) 
				continue;
			maxFarben = farben;
			maxNr = klausurNr;
		}
		return maxNr;
	}

	private gibNachbarsfarbenDerKlausur(klausurNr : number) : number {
		let summe : number = 0;
		let benutzt : Array<boolean> | null = Array(this._schienenAnzahl).fill(false);
		for (let klausurNr2 : number = 0; klausurNr2 < this._klausurenAnzahl; klausurNr2++){
			let farbe : number = this._klausurZuSchiene[klausurNr2];
			if ((farbe >= 0) && (this._verboten[klausurNr][klausurNr2])) 
				if (!benutzt[farbe]) {
					benutzt[farbe] = true;
					summe++;
				}
		}
		return summe;
	}

	/**
	 *Liefert den freien Knoten, der die meisten freien Nachbarn hat oder -1 falls es keinen gibt. Falls mehrere
	 * Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 * 
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. 
	 */
	gibKlausurDieFreiIstMitDenMeistenFreienNachbarn() : number {
		let maxNachbarn : number = -1;
		let maxNr : number = -1;
		for (let nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[nr] >= 0) 
				continue;
			let nachbarn : number = this.gibAnzahlFreierNachbarn(nr);
			if (nachbarn < maxNachbarn) 
				continue;
			maxNachbarn = nachbarn;
			maxNr = nr;
		}
		return maxNr;
	}

	private gibAnzahlFreierNachbarn(nr : number) : number {
		let summe : number = 0;
		for (let nr2 : number = 0; nr2 < this._klausurenAnzahl; nr2++)
			if ((this._klausurZuSchiene[nr2] >= 0) && (this._verboten[nr][nr2])) 
				summe++;
		return summe;
	}

	/**
	 *Liefert eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat, die
	 * widerum mit "nr1" benachbart sind.
	 * 
	 * @param  setS Die Menge an Nachbarn, die denen der gesuchte Knoten nicht benachbart sein darf.
	 * @return      eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat,
	 *              die widerum mit "nr1" benachbart sind. 
	 */
	gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS : LinkedCollection<number>) : number {
		let maxNachbarn : number = -1;
		let maxNr : number = -1;
		for (let nr2 of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[nr2] >= 0) 
				continue;
			if (this.gibIstBenachbart(nr2, setS)) 
				continue;
			let nachbarn : number = this.gibAnzahlFreierNachbarnVonNr2DieMitDerMengeBenachbartSind(setS, nr2);
			if (nachbarn < maxNachbarn) 
				continue;
			maxNachbarn = nachbarn;
			maxNr = nr2;
		}
		return maxNr;
	}

	private gibAnzahlFreierNachbarnVonNr2DieMitDerMengeBenachbartSind(setS : LinkedCollection<number>, nr2 : number) : number {
		let summe : number = 0;
		for (let nr3 : number = 0; nr3 < this._klausurenAnzahl; nr3++)
			if ((this._verboten[nr2][nr3]) && (this._klausurZuSchiene[nr3] < 0)) 
				if (this.gibIstBenachbart(nr3, setS)) 
					summe++;
		return summe;
	}

	private gibIstBenachbart(nr3 : number, setS : LinkedCollection<number>) : boolean {
		for (let nr4 of setS) 
			if (this._verboten[nr3][nr4!]) 
				return true;
		return false;
	}

	/**
	 *Speichert die aktuelle Klausur-Schienen-Lage in Zustand 1. 
	 */
	aktionZustand1Speichern() : void {
		this._schienenAnzahl1 = this._schienenAnzahl;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuSchiene1[nr] = this._klausurZuSchiene[nr];
	}

	/**
	 *Speichert die aktuelle Klausur-Schienen-Lage in Zustand 2. 
	 */
	aktionZustand2Speichern() : void {
		this._schienenAnzahl2 = this._schienenAnzahl;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuSchiene2[nr] = this._klausurZuSchiene[nr];
	}

	/**
	 *Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 1. 
	 */
	aktionZustand1Laden() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		this._schienenAnzahl = this._schienenAnzahl1;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuSchiene[nr] = this._klausurZuSchiene1[nr];
	}

	/**
	 *Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 2. 
	 */
	aktionZustand2Laden() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		this._schienenAnzahl = this._schienenAnzahl2;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuSchiene[nr] = this._klausurZuSchiene2[nr];
	}

	/**
	 *Entfernt alle Klausur-Schienen-Zuordnungen und passt die Datenstrukturen entsprechend an. 
	 */
	aktionKlausurenAusSchienenEntfernen() : void {
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			this._klausurZuSchiene[i] = -1;
		this._schienenAnzahl = 0;
	}

	/**
	 *Liefert TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte.
	 * 
	 * @param  nr die Klausur, die in die übergebene Schiene gesetzt werden soll.
	 * @param  s  die Schiene, in der die übergebene Klausur landen soll.
	 * @return    TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte. 
	 */
	aktionSetzeKlausurInSchiene(nr : number, s : number) : boolean {
		if (s < 0) 
			throw this.fehler("aktionSetzeKlausurInSchiene(" + nr + ", " + s + ") --> Schiene zu klein!")
		if (s >= this._schienenAnzahl) 
			throw this.fehler("aktionSetzeKlausurInSchiene(" + nr + ", " + s + ") --> Schiene zu groß!")
		for (let nr2 : number = 0; nr2 < this._klausurenAnzahl; nr2++)
			if (this._klausurZuSchiene[nr2] === s) 
				if (this._verboten[nr][nr2]) 
					return false;
		this._klausurZuSchiene[nr] = s;
		return true;
	}

	/**
	 *Entfernt die übergebene Klausur aus ihrer aktuellen Schiene. Falls die Klausur keiner Schiene zugeordnet war,
	 * wird eine {@link KlausurblockungException} geworfen.
	 * 
	 * @param klausurNr die Nummer der Klausur, die entfernt werden soll. 
	 */
	aktionEntferneKlausurAusSchiene(klausurNr : number) : void {
		if (this._klausurZuSchiene[klausurNr] < 0) 
			throw this.fehler("aktionEntferneKlausurAusSchiene(" + klausurNr + ") --> Die Klausur hatte gar keine Schiene!")
		this._klausurZuSchiene[klausurNr] = -1;
	}

	/**
	 *Erhöht die Schienenanzahl um 1, setzt die übergebene Klausur in die neue Schiene und liefert die neue
	 * Schienennummer.
	 * 
	 * @param  klausurNr die Klausur, die in eine neue Schiene gesetzt werden soll.
	 * @return           die neue Schiene, in welche die Klausur gesetzt wurde. 
	 */
	aktionSetzeKlausurInNeueSchiene(klausurNr : number) : number {
		let schiene : number = this._schienenAnzahl;
		if (this._klausurZuSchiene[klausurNr] >= 0) 
			throw this.fehler("aktionSetzeKlausurInNeueSchiene(" + klausurNr + ") --> Die Klausur ist bereits in einer Schiene!")
		this._klausurZuSchiene[klausurNr] = this._schienenAnzahl;
		this._schienenAnzahl++;
		return schiene;
	}

	/**
	 *Addiert pDifferenz zur Schienenanzahl.
	 * 
	 * @param pDifferenz Die Differenz der Veränderung. 
	 */
	aktionSchienenAnzahlVeraendern(pDifferenz : number) : void {
		this._schienenAnzahl += pDifferenz;
	}

	/**
	 *Setzt die übergebene Klausur in eine zufällige vorhandene Schiene. Falls dies nicht möglich ist, wird die
	 * Klausur in eine neue Schiene gesetzt.
	 * 
	 * @param klausurNr Setzt die übergebene Klausur in eine zufällige vorhandene Schiene. Falls dies nicht möglich ist,
	 *                  wird die Klausur in eine neue Schiene gesetzt. 
	 */
	aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr : number) : void {
		for (let schienenNr of this.gibErzeugeSchienenInZufaelligerReihenfolge()) 
			if (this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr) === true) 
				return;
		this.aktionSetzeKlausurInNeueSchiene(klausurNr);
	}

	/**
	 *Verteilt nicht verteilte Klausuren in zufällige Schienen. Falls dies nicht klappt, wird eine neue Schiene
	 * erzeugt. 
	 */
	aktionSetzeNichtverteilteKlausurenZufaellig() : void {
		for (let nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) 
			if (this._klausurZuSchiene[nr] === -1) 
				this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/**
	 *Zerstört einige Schienen, d.h. entfernt alle Klausuren aus den zerstörten Schienen und setzt danach alle
	 * entfernen Klausuren neu. 
	 */
	aktionZerstoereEinigeSchienenUndVerteileNeu() : void {
		while (this._schienenAnzahl > 0) {
			let s : number = this._random.nextInt(this._schienenAnzahl);
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++){
				if (this._klausurZuSchiene[nr] === s) 
					this._klausurZuSchiene[nr] = -1;
				if (this._klausurZuSchiene[nr] === this._schienenAnzahl - 1) 
					this._klausurZuSchiene[nr] = s;
			}
			this._schienenAnzahl--;
			if (this._random.nextBoolean()) 
				break;
		}
		this.aktionSetzeNichtverteilteKlausurenZufaellig();
	}

	/**
	 *Ausgabe zum Debuggen der Tests.
	 * 
	 * @param header Überschrift der Debug-Ausgabe. 
	 */
	debug(header : string | null) : void {
		console.log();
		console.log(JSON.stringify(header));
		for (let s : number = 0; s < this._schienenAnzahl; s++){
			console.log(JSON.stringify("    Schiene " + (s + 1)));
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
				if (this._klausurZuSchiene[nr] === s) 
					console.log(JSON.stringify("        Klausur " + (nr + 1)));
		}
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			if (this._klausurZuSchiene[nr] < 0) 
				console.log(JSON.stringify("        Klausur " + (nr + 1) + " --> ohne Schiene!"));
	}

	/**
	 *Entfernt zunächst alle Klausuren aus ihren Schienen und setzt sie dann in eine zufällige Schiene. Falls dies
	 * nicht klappt, wird eine neue Schiene erzeugt. 
	 */
	aktionEntferneAllesSetzeKlausurenZufaelligAufSchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		for (let nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) 
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/**
	 *Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit höherem Grad zuerst auf
	 * eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt. 
	 */
	aktionEntferneAllesSetzeKlausurenHoherGradAufSchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		for (let klausurNr of this.gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert()) 
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);
	}

	/**
	 *Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit den meisten Nachbarsfarben
	 * auf eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt. 
	 */
	aktionEntferneAllesSetzeKlausurenMitDenMeistenNachbarsfarbenAufSchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		let klausurNr : number = this.gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();
		while (klausurNr >= 0) {
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);
			klausurNr = this.gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();
		}
	}

	/**
	 *Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf. 
	 */
	aktionEntferneAllesFuelleSchienenNacheinandeAuf() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		while (this.gibAnzahlNichtverteilterKlausuren() > 0) {
			let schienenNr : number = this.gibErzeugeNeueSchiene();
			for (let klausurNr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) 
				if (this.gibIstKlausurUnverteilt(klausurNr)) 
					this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	/**
	 *Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf. Dabei werden
	 * Klausuren mit höherem Grad bevorzugt. 
	 */
	aktionEntferneAllesFuelleSchienenNacheinandeAufHoherGradZuerst() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		while (this.gibAnzahlNichtverteilterKlausuren() > 0) {
			let schienenNr : number = this.gibErzeugeNeueSchiene();
			for (let klausurNr of this.gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert()) 
				if (this.gibIstKlausurUnverteilt(klausurNr)) 
					this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.klausurblockung.KlausurblockungSchienenDynDaten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenDynDaten(obj : unknown) : KlausurblockungSchienenDynDaten {
	return obj as KlausurblockungSchienenDynDaten;
}
