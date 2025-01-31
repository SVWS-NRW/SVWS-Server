import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap } from '../../../../java/util/HashMap';
import { LinkedCollection } from '../../../../core/adt/collection/LinkedCollection';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../../../core/logger/Logger';
import { JavaMath } from '../../../../java/lang/JavaMath';
import { System } from '../../../../java/lang/System';
import { Random } from '../../../../java/util/Random';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostKursklausurRich } from '../../../../core/data/gost/klausurplanung/GostKursklausurRich';
import { Arrays } from '../../../../java/util/Arrays';
import { HashSet } from '../../../../java/util/HashSet';

export class KlausurblockungSchienenDynDaten extends JavaObject {

	private static readonly SCHIENEN_MAX_ANZAHL : number = 1000;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Ein {@link Logger}-Objekt für Debug-Zwecke.
	 */
	private readonly _logger : Logger;

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln.
	 */
	private readonly _mapKlausurZuNummer : HashMap<number, number> = new HashMap<number, number>();

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln.
	 */
	private readonly _mapNummerZuKlausur : HashMap<number, GostKursklausurRich> = new HashMap<number, GostKursklausurRich>();

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln.
	 */
	private readonly _mapSchuelerZuNummer : HashMap<number, number> = new HashMap<number, number>();

	/**
	 * Die Anzahl der Klausuren.
	 */
	private readonly _klausurenAnzahl : number;

	/**
	 * Jeder Klausurnummer wird eine Schiene zugeordnet. Der Wert -1 definiert eine temporäre Nicht-Zuordnung.
	 *  Am Ende des Algorithmus hat jede Klausur einen Wert >= 0.
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
	 * Bestimmt, ob ein Klausurnummer-Paar am selben Termin verboten ist.
	 */
	private readonly _verboten : Array<Array<boolean>>;

	/**
	 * Bestimmt, ob ein Klausurnummer-Paar am selben Termin bevorzugt wird.
	 */
	private readonly _bevorzugt : Array<Array<number>>;

	/**
	 * Die Anzahl der derzeitigen verwendeten Schienen.
	 */
	private _schienenAnzahl : number = 0;

	private _schienenAnzahl1 : number = KlausurblockungSchienenDynDaten.SCHIENEN_MAX_ANZAHL;

	private _schienenAnzahl2 : number = KlausurblockungSchienenDynDaten.SCHIENEN_MAX_ANZAHL;


	/**
	 *Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur als Basis für die
	 * Algorithmen zur schnellen Manipulation.
	 *
	 * @param pLogger Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	constructor(pLogger : Logger, pRandom : Random, pInput : List<GostKursklausurRich>) {
		super();
		this._random = pRandom;
		this._logger = pLogger;
		this.initialisiereMapSchueler(pInput);
		this.initialisiereMapKlausuren(pInput);
		this._klausurenAnzahl = this._mapKlausurZuNummer.size();
		this._klausurZuSchiene = Array(this._klausurenAnzahl).fill(0);
		this._klausurnummerZuGrad = Array(this._klausurenAnzahl).fill(0);
		this._klausurenSortiertGrad = this.gibErzeugeKlausurenInReihenfolge();
		this._klausurZuSchiene1 = Array(this._klausurenAnzahl).fill(0);
		this._klausurZuSchiene2 = Array(this._klausurenAnzahl).fill(0);
		this._verboten = [...Array(this._klausurenAnzahl)].map(e => Array(this._klausurenAnzahl).fill(false));
		this._bevorzugt = [...Array(this._klausurenAnzahl)].map(e => Array(this._klausurenAnzahl).fill(0));
		this.initialisiereMatrixVerboten(pInput);
		this.initialisiereMatrixBevorzugt(pInput);
		this.initialisiereKnotenGrad();
		this.aktionKlausurenAusSchienenEntfernen();
	}

	private initialisiereMapSchueler(pInput : List<GostKursklausurRich>) : void {
		const setSchueler : HashSet<number> = new HashSet<number>();
		for (const gostKursklausur of pInput) {
			for (const schuelerID of gostKursklausur.schuelerIds) {
				if (schuelerID < 0)
					throw new DeveloperNotificationException("Schüler-ID " + schuelerID + " ist negativ!")
				if (setSchueler.add(schuelerID)) {
					const schuelerNummer : number = this._mapSchuelerZuNummer.size();
					this._mapSchuelerZuNummer.put(schuelerID, schuelerNummer);
				}
			}
		}
	}

	private initialisiereMapKlausuren(pInput : List<GostKursklausurRich>) : void {
		for (const gostKursklausur of pInput) {
			if (gostKursklausur.id < 0)
				throw new DeveloperNotificationException("Klausur-ID=" + gostKursklausur.id + " ist negativ!")
			if (this._mapKlausurZuNummer.containsKey(gostKursklausur.id))
				throw new DeveloperNotificationException("Klausur-ID=" + gostKursklausur.id + " ist doppelt!")
			const klausurNummer : number = this._mapKlausurZuNummer.size();
			this._mapKlausurZuNummer.put(gostKursklausur.id, klausurNummer);
			this._mapNummerZuKlausur.put(klausurNummer, gostKursklausur);
		}
	}

	private initialisiereMatrixVerboten(pInput : List<GostKursklausurRich>) : void {
		const mapSchuelerKlausuren : HashMap<number, LinkedCollection<number>> = new HashMap<number, LinkedCollection<number>>();
		for (const gostKursklausur of pInput) {
			for (const schuelerID of gostKursklausur.schuelerIds) {
				let list : LinkedCollection<number> | null = mapSchuelerKlausuren.get(schuelerID);
				if (list === null) {
					list = new LinkedCollection();
					mapSchuelerKlausuren.put(schuelerID, list);
				}
				list.addLast(gostKursklausur.id);
			}
		}
		for (const e of mapSchuelerKlausuren.entrySet()) {
			const list : LinkedCollection<number> = e.getValue();
			for (const klausurID1 of list)
				for (const klausurID2 of list) {
					const klausurNr1 : number = DeveloperNotificationException.ifNull("NULL-Wert beim Mapping von klausurID1(" + klausurID1 + ")", this._mapKlausurZuNummer.get(klausurID1));
					const klausurNr2 : number = DeveloperNotificationException.ifNull("NULL-Wert beim Mapping von klausurID2(" + klausurID2 + ")", this._mapKlausurZuNummer.get(klausurID2));
					this._verboten[klausurNr1.valueOf()][klausurNr2.valueOf()] = true;
				}
		}
	}

	private initialisiereMatrixBevorzugt(pInput : List<GostKursklausurRich>) : void {
		for (const gostKursklausur1 of pInput)
			for (const gostKursklausur2 of pInput)
				if (KlausurblockungSchienenDynDaten.hatGemeinsameSchiene(gostKursklausur1.kursSchiene, gostKursklausur2.kursSchiene)) {
					const klausurNr1 : number | null = this._mapKlausurZuNummer.get(gostKursklausur1.id);
					const klausurNr2 : number | null = this._mapKlausurZuNummer.get(gostKursklausur2.id);
					if (klausurNr1 === null)
						throw new DeveloperNotificationException("NULL-Wert beim Mapping von klausurID1 --> " + gostKursklausur1.id)
					if (klausurNr2 === null)
						throw new DeveloperNotificationException("NULL-Wert beim Mapping von klausurID2 --> " + gostKursklausur2.id)
					this._bevorzugt[klausurNr1.valueOf()][klausurNr2.valueOf()]++;
				}
	}

	private static hatGemeinsameSchiene(kursSchiene1 : Array<number>, kursSchiene2 : Array<number>) : boolean {
		for (const schiene1 of kursSchiene1)
			for (const schiene2 of kursSchiene2)
				if (schiene1 === schiene2)
					return true;
		return false;
	}

	private initialisiereKnotenGrad() : void {
		for (let klausurNr1 : number = 0; klausurNr1 < this._klausurenAnzahl; klausurNr1++) {
			let kanten : number = 0;
			for (let klausurNr2 : number = 0; klausurNr2 < this._klausurenAnzahl; klausurNr2++)
				if (this._verboten[klausurNr1][klausurNr2])
					kanten++;
			this._klausurnummerZuGrad[klausurNr1] = kanten;
		}
		for (let i : number = 1; i < this._klausurenAnzahl; i++)
			for (let j : number = i; j >= 1; j--) {
				const nummerR : number = this._klausurenSortiertGrad[j];
				const nummerL : number = this._klausurenSortiertGrad[j - 1];
				if (this._klausurnummerZuGrad[nummerL] >= this._klausurnummerZuGrad[nummerR])
					break;
				this._klausurenSortiertGrad[j] = nummerL;
				this._klausurenSortiertGrad[j - 1] = nummerR;
			}
	}

	/**
	 * Liefert ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer.
	 *
	 * @return ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer.
	 */
	private gibErzeugeKlausurenInReihenfolge() : Array<number> {
		const temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = i;
		return temp;
	}

	/**
	 * Liefert ein Ausgabe-Objekt: 1. Ebene = Schienen, 2. Ebene = KlausurIDs
	 *
	 * @return ein Ausgabe-Objekt: 1. Ebene = Schienen, 2. Ebene = KlausurIDs
	 */
	gibErzeugeOutput() : List<List<number>> {
		const out : List<List<number>> = new ArrayList<List<number>>();
		for (let i : number = 0; i < this._schienenAnzahl; i++)
			out.add(new ArrayList<number>());
		for (const e of this._mapKlausurZuNummer.entrySet()) {
			const klausurID : number = e.getKey();
			const klausurNr : number = e.getValue();
			const schiene : number = this._klausurZuSchiene[klausurNr.valueOf()];
			DeveloperNotificationException.ifTrue("schiene(" + schiene + ") < 0", schiene < 0);
			DeveloperNotificationException.ifTrue("schiene(" + schiene + ") >= _schienenAnzahl", schiene >= this._schienenAnzahl);
			out.get(schiene).add(klausurID);
		}
		return out;
	}

	/**
	 * Liefert ein Array aller Klausurnummern in zufälliger Reihenfolge.
	 *
	 * @return ein Array aller Klausurnummern in zufälliger Reihenfolge.
	 */
	gibErzeugeKlausurenInZufaelligerReihenfolge() : Array<number> {
		const temp : Array<number> | null = Array(this._klausurenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			temp[i] = i;
		for (let i1 : number = 0; i1 < this._klausurenAnzahl; i1++) {
			const i2 : number = this._random.nextInt(this._klausurenAnzahl);
			const save1 : number = temp[i1];
			const save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 * Liefert ein Array aller Klausurnummern in zufälliger Reihenfolge nach bevorzugter Lage.
	 *
	 * @return ein Array aller Klausurnummern in zufälliger Reihenfolge nach bevorzugter Lage.
	 */
	gibErzeugeKlausurenInZufaelligerReihenfolgeNachBevorzugterLage() : Array<number> {
		const temp : Array<number> | null = this.gibErzeugeKlausurenInZufaelligerReihenfolge();
		for (let i1 : number = 0; i1 < this._klausurenAnzahl; i1++) {
			const nr1 : number = temp[i1];
			const iTausch : number = i1 + 1;
			for (let i2 : number = iTausch; i2 < this._klausurenAnzahl; i2++) {
				const nr2 : number = temp[i2];
				if (this._bevorzugt[nr1][nr2] > 0) {
					const save1 : number = temp[iTausch];
					const save2 : number = temp[i2];
					temp[iTausch] = save2;
					temp[i2] = save1;
					break;
				}
			}
		}
		return temp;
	}

	/**
	 * Liefert ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 *
	 * @return ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 */
	gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert() : Array<number> {
		const temp : Array<number> | null = Arrays.copyOf(this._klausurenSortiertGrad, this._klausurenAnzahl);
		for (let i1 : number = 0; i1 < this._klausurenAnzahl; i1++) {
			const i2 : number = this._random.nextInt(this._klausurenAnzahl);
			if (((i1 - i2) * (i1 - i2)) > this._klausurenAnzahl)
				continue;
			const save1 : number = temp[i1];
			const save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 * Liefert ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 *
	 * @return ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 */
	gibErzeugeKlausurenMitHoeheremGradZuerst() : Array<number> {
		return Arrays.copyOf(this._klausurenSortiertGrad, this._klausurenAnzahl);
	}

	/**
	 * Liefert ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge.
	 *
	 * @return ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge.
	 */
	gibErzeugeSchienenInZufaelligerReihenfolge() : Array<number> {
		const temp : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		for (let i : number = 0; i < this._schienenAnzahl; i++)
			temp[i] = i;
		for (let i1 : number = 0; i1 < this._schienenAnzahl; i1++) {
			const i2 : number = this._random.nextInt(this._schienenAnzahl);
			const save1 : number = temp[i1];
			const save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 *
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 */
	gibIstBesserAlsZustand1() : boolean {
		return this.gibVergleicheMitAktuellemZustand(this._schienenAnzahl1, this._klausurZuSchiene1);
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
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
		const bevorzugt : number = this.gibSchienenBevorzugt(this._klausurZuSchiene);
		const bevorzugtX : number = this.gibSchienenBevorzugt(klausurZuSchieneX);
		if (bevorzugt > bevorzugtX)
			return true;
		if (bevorzugt < bevorzugtX)
			return false;
		const histogramm : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		const histogrammX : Array<number> | null = Array(this._schienenAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++) {
			histogramm[this._klausurZuSchiene[i]]++;
			histogrammX[klausurZuSchieneX[i]]++;
		}
		let minHisto : number = this._klausurenAnzahl;
		let minHistoX : number = this._klausurenAnzahl;
		for (let i : number = 0; i < this._schienenAnzahl; i++) {
			minHisto = Math.min(minHisto, histogramm[i]);
			minHistoX = Math.min(minHistoX, histogrammX[i]);
		}
		return minHisto < minHistoX;
	}

	private gibSchienenBevorzugt(pKlausurZuSchiene : Array<number>) : number {
		let summeBevorzugt : number = 0;
		for (let nr1 : number = 0; nr1 < this._klausurenAnzahl; nr1++)
			for (let nr2 : number = nr1 + 1; nr2 < this._klausurenAnzahl; nr2++)
				if (pKlausurZuSchiene[nr1] === pKlausurZuSchiene[nr2])
					summeBevorzugt += this._bevorzugt[nr1][nr2];
		return summeBevorzugt;
	}

	/**
	 * Liefert die Anzahl noch nicht verteilter Klausuren.
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
	 * Liefert die Anzahl an Klausuren.
	 *
	 * @return die Anzahl an Klausuren.
	 */
	gibAnzahlKlausuren() : number {
		return this._klausurenAnzahl;
	}

	/**
	 * Liefert TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde.
	 *
	 * @param  klausurNr die Klausurnummer, nach der gefragt wird.
	 * @return           TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde.
	 */
	gibIstKlausurUnverteilt(klausurNr : number) : boolean {
		return this._klausurZuSchiene[klausurNr] < 0;
	}

	/**
	 * Liefert die Nummer einer neu erzeugten Schiene.
	 *
	 * @return die Nummer einer neu erzeugten Schiene.
	 */
	gibErzeugeNeueSchiene() : number {
		this._schienenAnzahl++;
		return this._schienenAnzahl - 1;
	}

	/**
	 * Liefert die Anzahl der derzeit verwendeten Schienen.
	 *
	 * @return die Anzahl der derzeit verwendeten Schienen.
	 */
	gibAnzahlSchienen() : number {
		return this._schienenAnzahl;
	}

	/**
	 * Liefert den freien Knoten (Klausur), der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt.
	 * Falls mehrere Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 *
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt.
	 */
	gibKlausurDieFreiIstMitDenMeistenNachbarsfarben() : number {
		let maxFarben : number = -1;
		let maxNr : number = -1;
		for (const klausurNr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[klausurNr] >= 0)
				continue;
			const farben : number = this.gibNachbarsfarbenDerKlausur(klausurNr);
			if (farben < maxFarben)
				continue;
			maxFarben = farben;
			maxNr = klausurNr;
		}
		return maxNr;
	}

	private gibNachbarsfarbenDerKlausur(klausurNr : number) : number {
		let summe : number = 0;
		const benutzt : Array<boolean> | null = Array(this._schienenAnzahl).fill(false);
		for (let klausurNr2 : number = 0; klausurNr2 < this._klausurenAnzahl; klausurNr2++) {
			const farbe : number = this._klausurZuSchiene[klausurNr2];
			if (((farbe >= 0) && (this._verboten[klausurNr][klausurNr2])) && (!benutzt[farbe])) {
				benutzt[farbe] = true;
				summe++;
			}
		}
		return summe;
	}

	/**
	 * Liefert den freien Knoten, der die meisten freien Nachbarn hat oder -1 falls es keinen gibt.
	 * Falls mehrere Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 *
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt.
	 */
	gibKlausurDieFreiIstMitDenMeistenFreienNachbarn() : number {
		let maxNachbarn : number = -1;
		let maxNr : number = -1;
		for (const nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[nr] >= 0)
				continue;
			const nachbarn : number = this.gibAnzahlFreierNachbarn(nr);
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
	 * Liefert eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat,
	 * die widerum mit "nr1" benachbart sind.
	 *
	 * @param  setS Die Menge an Nachbarn, die denen der gesuchte Knoten nicht benachbart sein darf.
	 * @return      eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat,
	 *              die widerum mit "nr1" benachbart sind.
	 */
	gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS : LinkedCollection<number>) : number {
		let maxNachbarn : number = -1;
		let maxNr : number = -1;
		for (const nr2 of this.gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (this._klausurZuSchiene[nr2] >= 0)
				continue;
			if (this.gibIstBenachbart(nr2, setS))
				continue;
			const nachbarn : number = this.gibAnzahlFreierNachbarnVonNr2DieMitDerMengeBenachbartSind(setS, nr2);
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
			if (((this._verboten[nr2][nr3]) && (this._klausurZuSchiene[nr3] < 0)) && (this.gibIstBenachbart(nr3, setS)))
				summe++;
		return summe;
	}

	private gibIstBenachbart(nr3 : number, setS : LinkedCollection<number>) : boolean {
		for (const nr4 of setS)
			if (this._verboten[nr3][nr4])
				return true;
		return false;
	}

	/**
	 * Speichert die aktuelle Klausur-Schienen-Lage in Zustand 1.
	 */
	aktionZustand1Speichern() : void {
		this._schienenAnzahl1 = this._schienenAnzahl;
		System.arraycopy(this._klausurZuSchiene, 0, this._klausurZuSchiene1, 0, this._klausurenAnzahl);
	}

	/**
	 * Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 1.
	 */
	aktionZustand1Laden() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		this._schienenAnzahl = this._schienenAnzahl1;
		System.arraycopy(this._klausurZuSchiene1, 0, this._klausurZuSchiene, 0, this._klausurenAnzahl);
	}

	/**
	 * Speichert die aktuelle Klausur-Schienen-Lage in Zustand 2.
	 */
	aktionZustand2Speichern() : void {
		this._schienenAnzahl2 = this._schienenAnzahl;
		System.arraycopy(this._klausurZuSchiene, 0, this._klausurZuSchiene2, 0, this._klausurenAnzahl);
		this._logger.logLn("BESSER, bevorzugtSumme = " + this.gibSchienenBevorzugt(this._klausurZuSchiene));
	}

	/**
	 * Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 2.
	 */
	aktionZustand2Laden() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		this._schienenAnzahl = this._schienenAnzahl2;
		System.arraycopy(this._klausurZuSchiene2, 0, this._klausurZuSchiene, 0, this._klausurenAnzahl);
	}

	/**
	 * Entfernt alle Klausur-Schienen-Zuordnungen und passt die Datenstrukturen entsprechend an.
	 */
	aktionKlausurenAusSchienenEntfernen() : void {
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			this._klausurZuSchiene[i] = -1;
		this._schienenAnzahl = 0;
	}

	/**
	 * Liefert TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte.
	 *
	 * @param  nr die Klausur, die in die übergebene Schiene gesetzt werden soll.
	 * @param  s  die Schiene, in der die übergebene Klausur landen soll.
	 * @return    TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte.
	 */
	aktionSetzeKlausurInSchiene(nr : number, s : number) : boolean {
		if (s < 0)
			throw new DeveloperNotificationException("aktionSetzeKlausurInSchiene(" + nr + ", " + s + ") --> Schiene zu klein!")
		if (s >= this._schienenAnzahl)
			throw new DeveloperNotificationException("aktionSetzeKlausurInSchiene(" + nr + ", " + s + ") --> Schiene zu groß!")
		for (let nr2 : number = 0; nr2 < this._klausurenAnzahl; nr2++)
			if ((this._klausurZuSchiene[nr2] === s) && (this._verboten[nr][nr2]))
				return false;
		this._klausurZuSchiene[nr] = s;
		return true;
	}

	/**
	 * Entfernt die übergebene Klausur aus ihrer aktuellen Schiene.
	 * Falls die Klausur keiner Schiene zugeordnet war, wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param klausurNr die Nummer der Klausur, die entfernt werden soll.
	 */
	aktionEntferneKlausurAusSchiene(klausurNr : number) : void {
		if (this._klausurZuSchiene[klausurNr] < 0)
			throw new DeveloperNotificationException("aktionEntferneKlausurAusSchiene(" + klausurNr + ") --> Die Klausur hatte gar keine Schiene!")
		this._klausurZuSchiene[klausurNr] = -1;
	}

	/**
	 * Erhöht die Schienenanzahl um 1, setzt die übergebene Klausur in die neue Schiene und
	 * liefert die neue Schienennummer.
	 *
	 * @param  klausurNr die Klausur, die in eine neue Schiene gesetzt werden soll.
	 * @return           die neue Schiene, in welche die Klausur gesetzt wurde.
	 */
	aktionSetzeKlausurInNeueSchiene(klausurNr : number) : number {
		const schiene : number = this._schienenAnzahl;
		if (this._klausurZuSchiene[klausurNr] >= 0)
			throw new DeveloperNotificationException("aktionSetzeKlausurInNeueSchiene(" + klausurNr + ") --> Die Klausur ist bereits in einer Schiene!")
		this._klausurZuSchiene[klausurNr] = this._schienenAnzahl;
		this._schienenAnzahl++;
		return schiene;
	}

	/**
	 * Addiert pDifferenz zur Schienenanzahl.
	 *
	 * @param pDifferenz Die Differenz der Veränderung.
	 */
	aktionSchienenAnzahlVeraendern(pDifferenz : number) : void {
		this._schienenAnzahl += pDifferenz;
	}

	/**
	 * Setzt die übergebene Klausur in eine zufällige vorhandene Schiene.
	 * Falls dies nicht möglich ist, wird die Klausur in eine neue Schiene gesetzt.
	 *
	 * @param klausurNr Setzt die übergebene Klausur in eine zufällige vorhandene Schiene.
	 *                  Falls dies nicht möglich ist, wird die Klausur in eine neue Schiene gesetzt.
	 */
	aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr : number) : void {
		for (const schienenNr of this.gibErzeugeSchienenInZufaelligerReihenfolge())
			if (this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr))
				return;
		this.aktionSetzeKlausurInNeueSchiene(klausurNr);
	}

	/**
	 * Verteilt nicht verteilte Klausuren in zufällige Schienen.
	 * Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	aktionSetzeNichtverteilteKlausurenZufaellig() : void {
		for (const nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge())
			if (this._klausurZuSchiene[nr] === -1)
				this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/**
	 * Zerstört einige Schienen, d.h. entfernt alle Klausuren aus den zerstörten Schienen
	 * und setzt danach alle entfernen Klausuren neu.
	 */
	aktionZerstoereEinigeSchienenUndVerteileNeu() : void {
		while (this._schienenAnzahl > 0) {
			const s : number = this._random.nextInt(this._schienenAnzahl);
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++) {
				if (this._klausurZuSchiene[nr] === s)
					this._klausurZuSchiene[nr] = -1;
				if (this._klausurZuSchiene[nr] === (this._schienenAnzahl - 1))
					this._klausurZuSchiene[nr] = s;
			}
			this._schienenAnzahl--;
			if (this._random.nextBoolean())
				break;
		}
		this.aktionSetzeNichtverteilteKlausurenZufaellig();
	}

	/**
	 * Ausgabe zum Debuggen der Tests.
	 *
	 * @param header Überschrift der Debug-Ausgabe.
	 */
	debug(header : string) : void {
		this._logger.logLn("");
		this._logger.logLn(header);
		for (let s : number = 0; s < this._schienenAnzahl; s++) {
			this._logger.log("    Schiene " + (s + 1) + ": ");
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
				if (this._klausurZuSchiene[nr] === s) {
					const gostKlausur : GostKursklausurRich | null = this._mapNummerZuKlausur.get(nr);
					if (gostKlausur === null)
						throw new DeveloperNotificationException("Mapping _mapNummerZuKlausur.get(" + nr + ") ist NULL!")
					this._logger.log(" " + (nr + 1) + "/" + Arrays.toString(gostKlausur.kursSchiene));
				}
			this._logger.logLn("");
		}
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			DeveloperNotificationException.ifTrue("Klausur " + (nr + 1) + " --> ohne Schiene!", this._klausurZuSchiene[nr] < 0);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Schienen und setzt sie dann in eine zufällige Schiene.
	 * Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	aktion_EntferneAlles_KlausurenZufaellig_SchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		for (const nr of this.gibErzeugeKlausurenInZufaelligerReihenfolge())
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf.
	 */
	aktion_EntferneAlles_SchienenNacheinander_KlausurenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		while (this.gibAnzahlNichtverteilterKlausuren() > 0) {
			const schienenNr : number = this.gibErzeugeNeueSchiene();
			for (const klausurNr of this.gibErzeugeKlausurenInZufaelligerReihenfolge())
				if (this.gibIstKlausurUnverteilt(klausurNr))
					this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit höherem Grad zuerst auf
	 * eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	aktion_EntferneAlles_KlausurenHoherGradZuerst_SchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		for (const klausurNr of this.gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert())
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf.
	 * Dabei werden Klausuren mit höherem Grad bevorzugt.
	 */
	aktion_EntferneAlles_SchienenNacheinande_KlausurenHoherGradZuerst() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		while (this.gibAnzahlNichtverteilterKlausuren() > 0) {
			const schienenNr : number = this.gibErzeugeNeueSchiene();
			for (const klausurNr of this.gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert())
				if (this.gibIstKlausurUnverteilt(klausurNr))
					this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit den meisten Nachbarsfarben
	 * auf eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	aktion_EntferneAlles_KlausurenMitDenMeistenNachbarsfarben_SchienenZufaellig() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		let klausurNr : number = this.gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();
		while (klausurNr >= 0) {
			this.aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);
			klausurNr = this.gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();
		}
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen,
	 * füllt dann die Schienen nacheinander auf
	 * und wählt die Klausuren so, dass bevorzugte Klausuren-Paare sequentiell durchlaufen werden.
	 */
	aktion_EntferneAlles_TermineNacheinander_KlausurenBevorzugterLage() : void {
		this.aktionKlausurenAusSchienenEntfernen();
		while (this.gibAnzahlNichtverteilterKlausuren() > 0) {
			const schienenNr : number = this.gibErzeugeNeueSchiene();
			for (const klausurNr of this.gibErzeugeKlausurenInZufaelligerReihenfolgeNachBevorzugterLage())
				if (this.gibIstKlausurUnverteilt(klausurNr))
					this.aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenDynDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenDynDaten'].includes(name);
	}

	public static class = new Class<KlausurblockungSchienenDynDaten>('de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenDynDaten');

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungSchienenDynDaten(obj : unknown) : KlausurblockungSchienenDynDaten {
	return obj as KlausurblockungSchienenDynDaten;
}
