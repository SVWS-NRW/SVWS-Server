import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurterminblockungAlgorithmusConfig } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusConfig';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { HashMap } from '../../../java/util/HashMap';
import { LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Random } from '../../../java/util/Random';
import { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Vector } from '../../../java/util/Vector';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';

export class KlausurterminblockungDynDaten extends JavaObject {

	/**
	 * Ein Maximal-Wert für die maximale Anzahl an Terminen.
	 */
	private static readonly MAX_TERMINE : number = 1000;

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
	private readonly _mapNummerZuKlausur : HashMap<number, GostKursklausur> = new HashMap();

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
	private readonly _klausurGruppen : Vector<Vector<number>> = new Vector();

	/**
	 * Alle Klausurgruppen, sortiert nach ihrem Knotengrad (Anzahl an Nachbarn).
	 */
	private readonly _klausurGruppenGrad : Vector<Vector<number>> = new Vector();


	/**
	 * Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur,
	 * welche als Basis für alle Algorithmen zur schnellen Manipulation dient.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 * @param pConfig Informationen zur Konfiguration des Algorithmus.
	 */
	public constructor(pRandom : Random, pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig) {
		super();
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
		for (const gruppe of this._klausurGruppen) {
			for (const nr1 of gruppe)
				for (const nr2 of gruppe)
					if (this._verboten[nr1][nr2])
						throw new UserNotificationException("Klausurgruppe hat einen Schülerkonflikt!")
		}
	}

	private initialisiereKlausurgruppen(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig) : void {
		switch (pConfig.get_algorithmus()) {
			case KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_NORMAL:{
				for (const gostKursklausur of pInput) {
					const klausurNr : number | null = this._mapKlausurZuNummer.get(gostKursklausur.id);
					if (klausurNr === null)
						throw new DeveloperNotificationException("Kein Mapping zu gostKursklausur.id = " + gostKursklausur.id)
					const gruppe : Vector<number> = new Vector();
					gruppe.add(klausurNr);
					this._klausurGruppen.add(gruppe);
				}
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_FAECHERWEISE:{
				const mapFachZuKlausurGruppe : HashMap<number, Vector<number>> = new HashMap();
				for (const gostKursklausur of pInput) {
					const klausurNr : number | null = this._mapKlausurZuNummer.get(gostKursklausur.id);
					if (klausurNr === null)
						throw new DeveloperNotificationException("Kein Mapping zu gostKursklausur.id = " + gostKursklausur.id)
					const fachID : number = gostKursklausur.idFach;
					if (fachID < 0) {
						const gruppe : Vector<number> = new Vector();
						gruppe.add(klausurNr);
						this._klausurGruppen.add(gruppe);
					} else {
						let gruppe : Vector<number> | null = mapFachZuKlausurGruppe.get(fachID);
						if (gruppe === null) {
							gruppe = new Vector();
							mapFachZuKlausurGruppe.put(fachID, gruppe);
							this._klausurGruppen.add(gruppe);
						}
						gruppe.add(klausurNr);
					}
				}
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_SCHIENENWEISE: {
				const mapSchieneZuKlausurGruppe : HashMap<number, Vector<number>> = new HashMap()
				for (const gostKursklausur of pInput) {
					const klausurNr : number | null = this._mapKlausurZuNummer.get(gostKursklausur.id);
					if (klausurNr === null)
						throw new DeveloperNotificationException("Kein Mapping zu gostKursklausur.id = " + gostKursklausur.id)
					const schienenID : number = gostKursklausur.kursSchiene.length < 1 ? -1 : gostKursklausur.kursSchiene[0];
					if (schienenID < 0) {
						const gruppe : Vector<number> = new Vector();
						gruppe.add(klausurNr);
						this._klausurGruppen.add(gruppe);
					} else {
						let gruppe : Vector<number> | null = mapSchieneZuKlausurGruppe.get(schienenID);
						if (gruppe === null) {
							gruppe = new Vector();
							mapSchieneZuKlausurGruppe.put(schienenID, gruppe);
							this._klausurGruppen.add(gruppe);
						}
						gruppe.add(klausurNr);
					}
				}
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der Algorithmus ist unbekannt!")
			}
		}
	}

	private initialisiereKlausurgruppenGrad() : void {
		this._klausurGruppenGrad.addAll(this._klausurGruppen);
		for (let i : number = 1; i < this._klausurGruppenGrad.size(); i++)
			for (let j : number = i; j >= 1; j--) {
				const gruppeR : Vector<number> = this._klausurGruppenGrad.get(j);
				const gruppeL : Vector<number> = this._klausurGruppenGrad.get(j - 1);
				const gradR : number = this.gibKnotengrad(gruppeR);
				const gradL : number = this.gibKnotengrad(gruppeL);
				if (gradL >= gradR)
					break;
				this._klausurGruppenGrad.set(j, gruppeL);
				this._klausurGruppenGrad.set(j - 1, gruppeR);
			}
	}

	private gibKnotengrad(pGruppe : Vector<number>) : number {
		let grad : number = 0;
		for (const gruppe of this._klausurGruppen)
			if (this.gibIstVerboten(pGruppe, gruppe))
				grad++;
		return grad;
	}

	private gibIstVerboten(pGruppe1 : Vector<number>, pGruppe2 : Vector<number>) : boolean {
		for (const klausurNr1 of pGruppe1)
			for (const klausurNr2 of pGruppe2)
				if (this._verboten[klausurNr1][klausurNr2])
					return true;
		return false;
	}

	private initialisiereMapKlausuren(pInput : List<GostKursklausur>) : void {
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

	private initialisiereMatrixVerboten(pInput : List<GostKursklausur>) : void {
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
		for (const schuelerID of mapSchuelerKlausuren.keySet()) {
			const list : LinkedCollection<number> | null = mapSchuelerKlausuren.get(schuelerID);
			if (list === null)
				throw new DeveloperNotificationException("Die Liste darf nicht NULL sein.")
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
	private gibKlausurgruppenInZufaelligerReihenfolge() : Vector<Vector<number>> {
		const temp : Vector<Vector<number>> = new Vector();
		temp.addAll(this._klausurGruppen);
		for (let i1 : number = 0; i1 < temp.size(); i1++) {
			const i2 : number = this._random.nextInt(temp.size());
			const save1 : Vector<number> = temp.get(i1);
			const save2 : Vector<number> = temp.get(i2);
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
	gibKlausurgruppenMitHoeheremGradZuerstEtwasPermutiert() : Vector<Vector<number>> {
		const temp : Vector<Vector<number>> = new Vector();
		temp.addAll(this._klausurGruppenGrad);
		const size : number = temp.size();
		for (let i1 : number = 0; i1 < size; i1++) {
			const i2 : number = this._random.nextInt(size);
			if ((i1 - i2) * (i1 - i2) >= size)
				continue;
			const save1 : Vector<number> = temp.get(i1);
			const save2 : Vector<number> = temp.get(i2);
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
	gibKlausurgruppeMitMinimalenTerminmoeglichkeiten() : Vector<number> {
		let min : number = this._klausurenAnzahl;
		let gruppeMin : Vector<number> | null = null;
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

	private gibTerminmoeglichkeiten(gruppe : Vector<number>) : number {
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
	private gibIstKlausurgruppeUnverteilt(pGruppe : Vector<number>) : boolean {
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
	aktionSetzeKlausurgruppeInTermin(pGruppe : Vector<number>, pTermin : number) : boolean {
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
	aktionEntferneKlausurgruppeAusTermin(pGruppe : Vector<number>, pTermin : number) : void {
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
	aktionSetzeKlausurgruppeInNeuenTermin(pGruppe : Vector<number>) : void {
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
	private aktionSetzeKlausurgruppeInZufallsterminOderErzeugeNeuenTermin(pGruppe : Vector<number>) : void {
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
	 * Liefert die berechnete Zuordnung als Liste (Termine) von Listen (KlausurIDs).
	 *
	 * @return die berechnete Zuordnung als Liste (Termine) von Listen (KlausurIDs).
	 */
	gibErzeugeOutput() : List<List<number>> {
		const out : List<List<number>> = new Vector();
		for (let i : number = 0; i < this._terminAnzahl; i++)
			out.add(new Vector());
		for (const klausurID of this._mapKlausurZuNummer.keySet()) {
			const klausurNr : number | null = this._mapKlausurZuNummer.get(klausurID);
			if (klausurID === null)
				throw new DeveloperNotificationException("gibErzeugeOutput(): NULL-Wert bei 'klausurID'!")
			if (klausurNr === null)
				throw new DeveloperNotificationException("gibErzeugeOutput(): NULL-Wert bei 'klausurNr'!")
			const terminNr : number = this._klausurZuTermin[klausurNr.valueOf()];
			if (terminNr < 0)
				throw new DeveloperNotificationException("gibErzeugeOutput(): negativer Wert bei 'terminNr'!")
			if (terminNr >= this._terminAnzahl)
				throw new DeveloperNotificationException("gibErzeugeOutput(): zu großer Wert bei 'terminNr'!")
			out.get(terminNr).add(klausurID);
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
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuTermin1[nr] = this._klausurZuTermin[nr];
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 1.
	 */
	aktionZustand1Laden() : void {
		this.aktionClear();
		this._terminAnzahl = this._terminAnzahl1;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuTermin[nr] = this._klausurZuTermin1[nr];
	}

	/**
	 * Speichert die aktuelle Klausur-Termin-Lage in Zustand 2.
	 */
	aktionZustand2Speichern() : void {
		this._terminAnzahl2 = this._terminAnzahl;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuTermin2[nr] = this._klausurZuTermin[nr];
	}

	/**
	 * Lädt die aktuelle Klausur-Termin-Lage aus Zustand 2.
	 */
	aktionZustand2Laden() : void {
		this.aktionClear();
		this._terminAnzahl = this._terminAnzahl2;
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			this._klausurZuTermin[nr] = this._klausurZuTermin2[nr];
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
	debug(header : string | null) : void {
		console.log();
		console.log(JSON.stringify(header));
		for (let s : number = 0; s < this._terminAnzahl; s++) {
			let line : string | null = "";
			line += "    Schiene " + (s + 1) + ": ";
			for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
				if (this._klausurZuTermin[nr] === s) {
					const gostKlausur : GostKursklausur | null = this._mapNummerZuKlausur.get(nr);
					if (gostKlausur === null)
						throw new DeveloperNotificationException("Mapping _mapNummerZuKlausur.get(" + nr + ") ist NULL!")
					line += " " + gostKlausur.kursKurzbezeichnung + "/" + Arrays.toString(gostKlausur.kursSchiene)!;
				}
			console.log(JSON.stringify(line));
		}
		for (let nr : number = 0; nr < this._klausurenAnzahl; nr++)
			if (this._klausurZuTermin[nr] < 0)
				throw new DeveloperNotificationException("Klausur " + (nr + 1) + " --> ohne Schiene!")
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.KlausurterminblockungDynDaten'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_KlausurterminblockungDynDaten(obj : unknown) : KlausurterminblockungDynDaten {
	return obj as KlausurterminblockungDynDaten;
}
