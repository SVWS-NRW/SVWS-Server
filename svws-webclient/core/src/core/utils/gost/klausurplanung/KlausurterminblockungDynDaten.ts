import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurterminblockungErgebnis } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungErgebnis';
import { GostKlausurterminblockungErgebnisTermin } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungErgebnisTermin';
import { GostKlausurterminblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurterminblockungKonfiguration';
import { HashMap } from '../../../../java/util/HashMap';
import { LinkedCollection } from '../../../../core/adt/collection/LinkedCollection';
import { ArrayList } from '../../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../../../core/logger/Logger';
import { System } from '../../../../java/lang/System';
import { Random } from '../../../../java/util/Random';
import { KlausurterminblockungAlgorithmen } from '../../../../core/types/gost/klausurplanung/KlausurterminblockungAlgorithmen';
import type { List } from '../../../../java/util/List';
import { GostKursklausurRich } from '../../../../core/data/gost/klausurplanung/GostKursklausurRich';
import { Arrays } from '../../../../java/util/Arrays';
import { UserNotificationException } from '../../../../core/exceptions/UserNotificationException';

export class KlausurterminblockungDynDaten extends JavaObject {

	/**
	 * Ein Maximal-Wert für die maximale Anzahl an Terminen.
	 */
	private static readonly MAX_TERMINE : number = 1000;

	/**
	 * Ein {@link Logger}-Objekt für Debug-Zwecke.
	 */
	private readonly _logger : Logger;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Die Anzahl der derzeitigen verwendeten Termine.
	 */
	private _terminAnzahl : number = 0;

	private _terminAnzahl1 : number = KlausurterminblockungDynDaten.MAX_TERMINE;

	private _terminAnzahl2 : number = KlausurterminblockungDynDaten.MAX_TERMINE;

	/**
	 *  Jeder KlausurNr wird eine TerminNr zugeordnet. Der Wert -1 definiert eine temporäre Nicht-Zuordnung.
	 *  Am Ende des Algorithmus hat jede Klausur eine zugeordnete TerminNr >= 0.
	 */
	private readonly _klausurZuTermin : Array<number>;

	private readonly _klausurZuTermin1 : Array<number>;

	private readonly _klausurZuTermin2 : Array<number>;

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln.
	 */
	private readonly _mapKlausurZuNummer : HashMap<number, number> = new HashMap();

	/**
	 * Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln.
	 */
	private readonly _mapNummerZuKlausur : HashMap<number, GostKursklausurRich> = new HashMap();

	/**
	 * Die Anzahl der Klausuren.
	 */
	private readonly _klausurenAnzahl : number;

	/**
	 * Bestimmt, ob ein Klausurnummer-Paar am selben Termin verboten ist.
	 */
	private readonly _verboten : Array<Array<boolean>>;

	/**
	 * Alle Klausurgruppen.
	 */
	private readonly _klausurGruppen : ArrayList<ArrayList<number>> = new ArrayList();

	/**
	 * Alle Klausurgruppen, sortiert nach ihrem Knotengrad (Anzahl an Nachbarn).
	 */
	private readonly _klausurGruppenGrad : ArrayList<ArrayList<number>> = new ArrayList();


	/**
	 * Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur,
	 * welche als Basis für alle Algorithmen zur schnellen Manipulation dient.
	 *
	 * @param pLogger Ein {@link Logger}-Objekt für Debug-Zwecke.
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 * @param pConfig Informationen zur Konfiguration des Algorithmus.
	 */
	public constructor(pLogger : Logger, pRandom : Random, pInput : List<GostKursklausurRich>, pConfig : GostKlausurterminblockungKonfiguration) {
		super();
		this._logger = pLogger;
		this._random = pRandom;
		this.initialisiereMapKlausuren(pInput);
		this._klausurenAnzahl = this._mapKlausurZuNummer.size();
		this._klausurZuTermin = Array(this._klausurenAnzahl).fill(0);
		this._klausurZuTermin1 = Array(this._klausurenAnzahl).fill(0);
		this._klausurZuTermin2 = Array(this._klausurenAnzahl).fill(0);
		this._verboten = [...Array(this._klausurenAnzahl)].map(e => Array(this._klausurenAnzahl).fill(false));
		this.initialisiereMatrixVerboten(pInput);
		this.initialisiereKlausurgruppen(pInput, pConfig);
		this.checkKlausurgruppenOrException();
		this.initialisiereKlausurgruppenGrad();
		this.aktionClear();
	}

	private checkKlausurgruppenOrException() : void {
		for (const gruppe of this._klausurGruppen)
			for (const nr1 of gruppe)
				for (const nr2 of gruppe)
					if (this._verboten[nr1][nr2])
						throw new UserNotificationException("Die aktuelle Konfiguration führt zu einer Klausurgruppe, in der mindestens ein S. doppelt ist!")
	}

	private gibKlausurNrOrException(pGostKursklausurRich : GostKursklausurRich) : number {
		return DeveloperNotificationException.ifNull("Kein Mapping zu gostKursklausur.id(" + pGostKursklausurRich.id + ")", this._mapKlausurZuNummer.get(pGostKursklausurRich.id));
	}

	private initialisiereKlausurgruppenNormal(pInput : List<GostKursklausurRich>) : void {
		for (const gostKursklausur of pInput) {
			const klausurNr : number = this.gibKlausurNrOrException(gostKursklausur);
			const gruppe : ArrayList<number> = new ArrayList();
			gruppe.add(klausurNr);
			this._klausurGruppen.add(gruppe);
		}
	}

	private initialisiereKlausurgruppenFaecherweise(pInput : List<GostKursklausurRich>) : void {
		const mapFachZuKlausurGruppe : HashMap<number, ArrayList<number>> = new HashMap();
		for (const gostKursklausur of pInput) {
			const klausurNr : number = this.gibKlausurNrOrException(gostKursklausur);
			const fachID : number = gostKursklausur.idFach;
			if (fachID < 0) {
				const gruppe : ArrayList<number> = new ArrayList();
				gruppe.add(klausurNr);
				this._klausurGruppen.add(gruppe);
			} else {
				let gruppe : ArrayList<number> | null = mapFachZuKlausurGruppe.get(fachID);
				if (gruppe === null) {
					gruppe = new ArrayList();
					mapFachZuKlausurGruppe.put(fachID, gruppe);
					this._klausurGruppen.add(gruppe);
				}
				gruppe.add(klausurNr);
			}
		}
	}

	private initialisiereKlausurgruppenSchienenweise(pInput : List<GostKursklausurRich>) : void {
		const mapSchieneZuKlausurGruppe : HashMap<number, ArrayList<number>> = new HashMap();
		for (const gostKursklausur of pInput) {
			const klausurNr : number = this.gibKlausurNrOrException(gostKursklausur);
			const schienenID : number = gostKursklausur.kursSchiene.length < 1 ? -1 : gostKursklausur.kursSchiene[0];
			if (schienenID < 0) {
				const gruppe : ArrayList<number> = new ArrayList();
				gruppe.add(klausurNr);
				this._klausurGruppen.add(gruppe);
			} else {
				let gruppe : ArrayList<number> | null = mapSchieneZuKlausurGruppe.get(schienenID);
				if (gruppe === null) {
					gruppe = new ArrayList();
					mapSchieneZuKlausurGruppe.put(schienenID, gruppe);
					this._klausurGruppen.add(gruppe);
				}
				gruppe.add(klausurNr);
			}
		}
	}

	private initialisiereKlausurgruppen(pInput : List<GostKursklausurRich>, pConfig : GostKlausurterminblockungKonfiguration) : void {
		const algo : KlausurterminblockungAlgorithmen = KlausurterminblockungAlgorithmen.getOrException(pConfig.algorithmus);
		switch (algo) {
			case KlausurterminblockungAlgorithmen.NORMAL: {
				this.initialisiereKlausurgruppenNormal(pInput);
				break;
			}
			case KlausurterminblockungAlgorithmen.FAECHERWEISE: {
				this.initialisiereKlausurgruppenFaecherweise(pInput);
				break;
			}
			case KlausurterminblockungAlgorithmen.SCHIENENWEISE: {
				this.initialisiereKlausurgruppenSchienenweise(pInput);
				break;
			}
		}
	}

	private initialisiereKlausurgruppenGrad() : void {
		this._klausurGruppenGrad.addAll(this._klausurGruppen);
		for (let i : number = 1; i < this._klausurGruppenGrad.size(); i++)
			for (let j : number = i; j >= 1; j--) {
				const gruppeR : ArrayList<number> = this._klausurGruppenGrad.get(j);
				const gruppeL : ArrayList<number> = this._klausurGruppenGrad.get(j - 1);
				const gradR : number = this.gibKnotengrad(gruppeR);
				const gradL : number = this.gibKnotengrad(gruppeL);
				if (gradL >= gradR)
					break;
				this._klausurGruppenGrad.set(j, gruppeL);
				this._klausurGruppenGrad.set(j - 1, gruppeR);
			}
	}

	private gibKnotengrad(pGruppe : ArrayList<number>) : number {
		let grad : number = 0;
		for (const gruppe of this._klausurGruppen)
			if (this.gibIstVerboten(pGruppe, gruppe))
				grad++;
		return grad;
	}

	private gibIstVerboten(pGruppe1 : ArrayList<number>, pGruppe2 : ArrayList<number>) : boolean {
		for (const klausurNr1 of pGruppe1)
			for (const klausurNr2 of pGruppe2)
				if (this._verboten[klausurNr1][klausurNr2])
					return true;
		return false;
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
		const mapSchuelerKlausuren : HashMap<number, LinkedCollection<number>> = new HashMap();
		for (const gostKursklausur of pInput) {
			for (const schuelerID of gostKursklausur.schuelerIds) {
				let list : LinkedCollection<number> | null = mapSchuelerKlausuren.get(schuelerID);
				if (list === null) {
					list = new LinkedCollection();
					mapSchuelerKlausuren.put(schuelerID, list);
				}
				const klausurNr : number | null = this._mapKlausurZuNummer.get(gostKursklausur.id);
				if (klausurNr === null)
					throw new DeveloperNotificationException("Kein Mapping zu gostKursklausur.id = " + gostKursklausur.id)
				list.addLast(klausurNr);
			}
		}
		for (const e of mapSchuelerKlausuren.entrySet()) {
			const list : LinkedCollection<number> = e.getValue();
			for (const klausurNr1 of list)
				for (const klausurNr2 of list)
					if (klausurNr1 !== klausurNr2)
						this._verboten[klausurNr1][klausurNr2] = true;
		}
	}

	/**
	 * Liefert die aktuelle Anzahl an Terminen.
	 *
	 * @return die aktuelle Anzahl an Terminen.
	 */
	gibTerminAnzahl() : number {
		return this._terminAnzahl;
	}

	/**
	 * Liefert die Anzahl an Klausurgruppen. Dies ist ein theoretisches Maximum
	 * für die größte Anzahl an Terminen.
	 *
	 * @return die Anzahl an Klausurgruppen.
	 */
	gibKlausurgruppenAnzahl() : number {
		return this._klausurGruppen.size();
	}

	/**
	 * Liefert die Anzahl noch nicht verteilter Klausuren.
	 *
	 * @return die Anzahl noch nicht verteilter Klausuren.
	 */
	gibExistierenNichtverteilteKlausuren() : boolean {
		for (let klausurNr : number = 0; klausurNr < this._klausurenAnzahl; klausurNr++)
			if (this._klausurZuTermin[klausurNr] < 0)
				return true;
		return false;
	}

	/**
	 * Liefert die Nummer eines neu erzeugten Termins.
	 *
	 * @return die Nummer eines neu erzeugten Termins.
	 */
	gibErzeugeNeuenTermin() : number {
		this._terminAnzahl++;
		return this._terminAnzahl - 1;
	}

	/**
	 * Löscht den letzten Termin.
	 */
	entferneLetztenTermin() : void {
		this._terminAnzahl--;
	}

	/**
	 * Liefert ein Array aller derzeit verwendeten Termine in zufälliger Reihenfolge.
	 *
	 * @return ein Array aller derzeit verwendeten Termine in zufälliger Reihenfolge.
	 */
	private gibTermineInZufaelligerReihenfolge() : Array<number> {
		const temp : Array<number> | null = Array(this._terminAnzahl).fill(0);
		for (let i : number = 0; i < this._terminAnzahl; i++)
			temp[i] = i;
		for (let i1 : number = 0; i1 < this._terminAnzahl; i1++) {
			const i2 : number = this._random.nextInt(this._terminAnzahl);
			const save1 : number = temp[i1];
			const save2 : number = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}
		return temp;
	}

	/**
	 * Liefert die Klausur-Gruppen in zufälliger Reihenfolge.
	 *
	 * @return die Klausur-Gruppen in zufälliger Reihenfolge.
	 */
	private gibKlausurgruppenInZufaelligerReihenfolge() : ArrayList<ArrayList<number>> {
		const temp : ArrayList<ArrayList<number>> = new ArrayList();
		temp.addAll(this._klausurGruppen);
		for (let i1 : number = 0; i1 < temp.size(); i1++) {
			const i2 : number = this._random.nextInt(temp.size());
			const save1 : ArrayList<number> = temp.get(i1);
			const save2 : ArrayList<number> = temp.get(i2);
			temp.set(i1, save2);
			temp.set(i2, save1);
		}
		return temp;
	}

	/**
	 * Liefert ein leicht permutiertes Array aller Klausurgruppen sortiert nach höheren Knotengrad zuerst.
	 *
	 * @return ein leicht permutiertes Array aller Klausurgruppen sortiert nach höheren Knotengrad zuerst.
	 */
	gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert() : ArrayList<ArrayList<number>> {
		const temp : ArrayList<ArrayList<number>> = new ArrayList();
		temp.addAll(this._klausurGruppenGrad);
		const size : number = temp.size();
		for (let i1 : number = 0; i1 < size; i1++) {
			const i2 : number = this._random.nextInt(size);
			if ((i1 - i2) * (i1 - i2) >= size)
				continue;
			const save1 : ArrayList<number> = temp.get(i1);
			const save2 : ArrayList<number> = temp.get(i2);
			temp.set(i1, save2);
			temp.set(i2, save1);
		}
		return temp;
	}

	/**
	 * Liefert die Klausurgruppe mit der geringsten Anzahl an Terminmöglichkeiten.
	 *
	 * @return die Klausurgruppe mit der geringsten Anzahl an Terminmöglichkeiten.
	 */
	gibKlausurgruppeMitMinimalenTerminmoeglichkeiten() : ArrayList<number> {
		let min : number = this._klausurenAnzahl;
		let gruppeMin : ArrayList<number> | null = null;
		for (const gruppe of this.gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
			if (this.gibIstKlausurgruppeUnverteilt(gruppe)) {
				const terminmoeglichekeiten : number = this.gibTerminmoeglichkeiten(gruppe);
				if (terminmoeglichekeiten < min) {
					min = terminmoeglichekeiten;
					gruppeMin = gruppe;
				}
			}
		if (gruppeMin === null)
			throw new DeveloperNotificationException("Das darf nicht passieren!")
		return gruppeMin;
	}

	private gibTerminmoeglichkeiten(gruppe : ArrayList<number>) : number {
		let summe : number = 0;
		for (let terminNr : number = 0; terminNr < this._terminAnzahl; terminNr++)
			if (this.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr)) {
				summe++;
				this.aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
			}
		return summe;
	}

	private gibVergleicheMitAktuellemZustand(terminAnzahlX : number, klausurZuTerminX : Array<number>) : boolean {
		if (this._terminAnzahl < terminAnzahlX)
			return true;
		if (this._terminAnzahl > terminAnzahlX)
			return false;
		const histogramm : Array<number> | null = Array(this._terminAnzahl).fill(0);
		const histogrammX : Array<number> | null = Array(this._terminAnzahl).fill(0);
		for (let i : number = 0; i < this._klausurenAnzahl; i++) {
			histogramm[this._klausurZuTermin[i]]++;
			histogrammX[klausurZuTerminX[i]]++;
		}
		let minHisto : number = this._klausurenAnzahl;
		let minHistoX : number = this._klausurenAnzahl;
		for (let i : number = 0; i < this._terminAnzahl; i++) {
			minHisto = Math.min(minHisto, histogramm[i]);
			minHistoX = Math.min(minHistoX, histogrammX[i]);
		}
		return minHisto < minHistoX;
	}

	/**
	 * Liefert TRUE, falls alle Klausuren der Gruppe noch nicht verteilt wurden.
	 *
	 * @param pGruppe die Gruppe aller Klausuren.
	 * @return TRUE, falls alle Klausuren der Gruppe noch nicht verteilt wurden.
	 */
	private gibIstKlausurgruppeUnverteilt(pGruppe : ArrayList<number>) : boolean {
		for (const klausurNr of pGruppe)
			if (this._klausurZuTermin[klausurNr] >= 0)
				return false;
		return true;
	}

	/**
	 * Liefert TRUE, falls alle Klausuren der Gruppe in den übergebenen Termin gesetzt werden konnten.
	 *
	 * @param  pGruppe die Gruppe aller Klausuren.
	 * @param  pTermin der Termin
	 * @return TRUE, falls alle Klausuren der Gruppe in den übergebenen Termin gesetzt werden konnten.
	 */
	aktionSetzeKlausurgruppeInTermin(pGruppe : ArrayList<number>, pTermin : number) : boolean {
		if (pTermin < 0)
			throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu klein!")
		if (pTermin >= this._terminAnzahl)
			throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu groß!")
		for (let nr2 : number = 0; nr2 < this._klausurenAnzahl; nr2++)
			if (this._klausurZuTermin[nr2] === pTermin)
				for (const nr of pGruppe)
					if (this._verboten[nr][nr2])
						return false;
		for (const nr of pGruppe)
			this._klausurZuTermin[nr] = pTermin;
		return true;
	}

	/**
	 * Entfernt die Klausuren der Gruppe aus ihrem Termin.
	 * @param  pGruppe die Gruppe aller Klausuren.
	 * @param  pTermin der Termin
	 */
	aktionEntferneKlausurgruppeAusTermin(pGruppe : ArrayList<number>, pTermin : number) : void {
		if (pTermin < 0)
			throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu klein!")
		if (pTermin >= this._terminAnzahl)
			throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin(" + pGruppe + ", " + pTermin + ") --> Termin zu groß!")
		for (const nr of pGruppe) {
			if (this._klausurZuTermin[nr] !== pTermin)
				throw new DeveloperNotificationException("aktionEntferneKlausurgruppeAusTermin: Die Gruppe war gar nicht im Termin " + pTermin + "!")
			this._klausurZuTermin[nr] = -1;
		}
	}

	/**
	 * Erhöht die Termin-Anzahl um 1, setzt alle Klausuren der übergebenen Gruppe in den neuen Termin.
	 *
	 * @param pGruppe die Gruppe aller Klausuren
	 */
	aktionSetzeKlausurgruppeInNeuenTermin(pGruppe : ArrayList<number>) : void {
		for (const klausurNr of pGruppe)
			if (this._klausurZuTermin[klausurNr] >= 0)
				throw new DeveloperNotificationException("aktionSetzeKlausurGruppeInNeuenTermin(" + klausurNr + ") --> Die Klausur ist bereits einem Termin zugeordnet!")
		for (const klausurNr of pGruppe)
			this._klausurZuTermin[klausurNr] = this._terminAnzahl;
		this._terminAnzahl++;
	}

	/**
	 * Setzt alle Klausuren in einen zufälligen Termin. <br>
	 * Falls dies nicht möglich ist, wird die Gruppe in einen neuen Termin gesetzt.
	 *
	 * @param pGruppe die Gruppe aller Klausuren, die in einen zufälligen Termin gesetzt werden sollen.
	 */
	private aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(pGruppe : ArrayList<number>) : void {
		for (const terminNr of this.gibTermineInZufaelligerReihenfolge())
			if (this.aktionSetzeKlausurgruppeInTermin(pGruppe, terminNr))
				return;
		this.aktionSetzeKlausurgruppeInNeuenTermin(pGruppe);
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 *
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 */
	gibIstBesserAlsZustand1() : boolean {
		return this.gibVergleicheMitAktuellemZustand(this._terminAnzahl1, this._klausurZuTermin1);
	}

	/**
	 * Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 *
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 */
	gibIstBesserAlsZustand2() : boolean {
		return this.gibVergleicheMitAktuellemZustand(this._terminAnzahl2, this._klausurZuTermin2);
	}

	/**
	 * Erzeugt das Ergebnis mit der berechneten Zuordnung als Liste (Termine)
	 * von Listen (KlausurIDs).
	 *
	 * @return das Ergebnis
	 */
	gibErzeugeOutput() : GostKlausurterminblockungErgebnis {
		const out : GostKlausurterminblockungErgebnis = new GostKlausurterminblockungErgebnis();
		for (let i : number = 0; i < this._terminAnzahl; i++)
			out.termine.add(new GostKlausurterminblockungErgebnisTermin());
		for (const e of this._mapKlausurZuNummer.entrySet()) {
			const klausurID : number = e.getKey();
			const klausurNr : number = e.getValue();
			const terminNr : number = this._klausurZuTermin[klausurNr.valueOf()];
			DeveloperNotificationException.ifTrue("terminNr(" + terminNr + ") < 0", terminNr < 0);
			DeveloperNotificationException.ifTrue("terminNr(" + terminNr + ") >= _terminAnzahl", terminNr >= this._terminAnzahl);
			out.termine.get(terminNr).kursklausuren.add(klausurID);
		}
		return out;
	}

	/**
	 * Entfernt alle Klausur-Termin-Zuordnungen und passt die Datenstrukturen entsprechend an.
	 */
	aktionClear() : void {
		for (let i : number = 0; i < this._klausurenAnzahl; i++)
			this._klausurZuTermin[i] = -1;
		this._terminAnzahl = 0;
	}

	/**
	 * Speichert die aktuelle Klausur-Termin-Lage in Zustand 1.
	 */
	aktionZustand1Speichern() : void {
		this._terminAnzahl1 = this._terminAnzahl;
		System.arraycopy(this._klausurZuTermin, 0, this._klausurZuTermin1, 0, this._klausurenAnzahl);
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 1.
	 */
	aktionZustand1Laden() : void {
		this.aktionClear();
		this._terminAnzahl = this._terminAnzahl1;
		System.arraycopy(this._klausurZuTermin1, 0, this._klausurZuTermin, 0, this._klausurenAnzahl);
	}

	/**
	 * Speichert die aktuelle Klausur-Termin-Lage in Zustand 2.
	 */
	aktionZustand2Speichern() : void {
		this._terminAnzahl2 = this._terminAnzahl;
		System.arraycopy(this._klausurZuTermin, 0, this._klausurZuTermin2, 0, this._klausurenAnzahl);
		this._logger.logLn("BESSER");
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 2.
	 */
	aktionZustand2Laden() : void {
		this.aktionClear();
		this._terminAnzahl = this._terminAnzahl2;
		System.arraycopy(this._klausurZuTermin2, 0, this._klausurZuTermin, 0, this._klausurenAnzahl);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Geht dann alle Klausuren in zufälliger Reihenfolge durch und setzt sie dann in einen zufälligen Termin. <br>
	 * Falls dies nicht klappt, wird ein neuer Termin erzeugt.
	 */
	aktion_Clear_KlausurgruppenZufaellig_TermineZufaellig() : void {
		this.aktionClear();
		for (const gruppe of this.gibKlausurgruppenInZufaelligerReihenfolge())
			this.aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(gruppe);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Füllt dann die Termine nacheinander auf und wählt die Klausurgruppen zufällig.
	 */
	aktion_Clear_TermineNacheinander_GruppeZufaellig() : void {
		this.aktionClear();
		while (this.gibExistierenNichtverteilteKlausuren()) {
			const terminNr : number = this.gibErzeugeNeuenTermin();
			for (const gruppe of this.gibKlausurgruppenInZufaelligerReihenfolge())
				if (this.gibIstKlausurgruppeUnverteilt(gruppe))
					this.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr);
		}
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Verteilt dann die Klausurgruppen mit höherem Knoten-Grad zuerst auf eine zufällige Schiene.
	 * Falls dies nicht klappt, wird eine neue Schiene erzeugt.
	 */
	aktion_Clear_GruppeHoeherGradZuerst_TermineZufaellig() : void {
		this.aktionClear();
		for (const gruppe of this.gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
			this.aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(gruppe);
	}

	/**
	 * Entfernt zunächst alle Klausuren aus ihren Terminen. <br>
	 * Füllt dann die Termine nacheinander auf und wählt die Klausurgruppen nach ihrem Grad.
	 */
	public aktion_Clear_TermineNacheinander_GruppeNachGrad() : void {
		this.aktionClear();
		while (this.gibExistierenNichtverteilteKlausuren()) {
			const terminNr : number = this.gibErzeugeNeuenTermin();
			for (const gruppe of this.gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert())
				if (this.gibIstKlausurgruppeUnverteilt(gruppe))
					this.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr);
		}
	}

	/**
	 * Ausgabe zum Debuggen der Tests.
	 *
	 * @param header Überschrift der Debug-Ausgabe.
	 */
	debug(header : string) : void {
		this._logger.modifyIndent(+4);
		this._logger.logLn(header);
		for (let s : number = 0; s < this._terminAnzahl; s++) {
			this._logger.log("    Schiene " + (s + 1) + ": ");
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
				if (this._klausurZuTermin[nr] === s) {
					const gostKlausur : GostKursklausurRich = DeveloperNotificationException.ifNull("Mapping _mapNummerZuKlausur.get(" + nr + ") ist NULL!", this._mapNummerZuKlausur.get(nr));
					this._logger.log(" " + gostKlausur.kursKurzbezeichnung + "/" + Arrays.toString(gostKlausur.kursSchiene)!);
				}
			this._logger.logLn("");
		}
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			DeveloperNotificationException.ifTrue("Klausur " + (nr + 1) + " --> ohne Schiene!", this._klausurZuTermin[nr] < 0);
		this._logger.modifyIndent(-4);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungDynDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungDynDaten'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurterminblockungDynDaten(obj : unknown) : KlausurterminblockungDynDaten {
	return obj as KlausurterminblockungDynDaten;
}
