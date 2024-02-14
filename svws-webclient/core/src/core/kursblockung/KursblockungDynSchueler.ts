import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { Random } from '../../java/util/Random';
import { KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { Arrays } from '../../java/util/Arrays';
import { HashSet } from '../../java/util/HashSet';

export class KursblockungDynSchueler extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly _logger : Logger;

	/**
	 * Die ID (von der GUI) des Schülers, beispielsweise 42.
	 */
	private readonly guiID : number;

	/**
	 * Ein String-Darstellung des Schüler für Warnungen und Fehlermeldungen, beispielsweise 'Mareike Musterfrau'.
	 */
	private readonly representation : string;

	/**
	 * Alle Facharten (=Fachwahlen) des Schüler, z.B. 'D;LK'.
	 */
	private fachartArr : Array<KursblockungDynFachart>;

	/**
	 * Der aktuell zur Fachart zugeordnete Kurs.
	 */
	private fachartZuKurs : Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveS : Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveK : Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveG : Array<KursblockungDynKurs | null>;

	/**
	 * Referenz zur Statistik, um diese über Nichtwahlen zu informieren.
	 */
	private readonly statistik : KursblockungDynStatistik;

	/**
	 * Die aktuellen Nichtwahlen dieses Schülers.
	 */
	private nichtwahlen : number = 0;

	/**
	 * Die aktuelle Information darüber, ob die Schiene des Schülers belegt ist.
	 */
	private readonly schieneBelegt : Array<boolean>;

	/**
	 * Diese Datenstruktur wird verwendet um bei bestimmten Algorithmus Kurse auf Schienen zu verteilen.
	 */
	private matrix : KursblockungMatrix;

	/**
	 * Verbotene Kurse des Schülers. Diese dürfen nicht belegt werden.
	 */
	private readonly kursGesperrt : Array<boolean>;


	/**
	 * Im Konstruktor wird {@code pSchueler} in ein Objekt dieser Klasse umgewandelt.
	 *
	 * @param pRandom         Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger         Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pStatistik      Referenz um die Nichtwahlen mitzuteilen.
	 * @param pSchuelerID     Die ID des Schülers von der GUI/DB.
	 * @param pSchienenAnzahl Wir benötigt, um {@link #schieneBelegt} zu initialisieren.
	 * @param pKursAnzahl     Die Anzahl aller Kurse. Wird benötigt, damit {@link #kursGesperrt} initialisiert werden
	 *                        kann.
	 */
	constructor(pLogger : Logger, pRandom : Random, pSchuelerID : number, pStatistik : KursblockungDynStatistik, pSchienenAnzahl : number, pKursAnzahl : number) {
		super();
		this._random = pRandom;
		this._logger = pLogger;
		this.guiID = pSchuelerID;
		this.representation = "Schüler " + pSchuelerID;
		this.statistik = pStatistik;
		this.fachartArr = Array(0).fill(null);
		this.fachartZuKurs = Array(0).fill(null);
		this.fachartZuKursSaveS = Array(0).fill(null);
		this.fachartZuKursSaveK = Array(0).fill(null);
		this.fachartZuKursSaveG = Array(0).fill(null);
		this.nichtwahlen = 0;
		this.schieneBelegt = Array(pSchienenAnzahl).fill(false);
		this.kursGesperrt = Array(pKursAnzahl).fill(false);
		this.matrix = new KursblockungMatrix(this._random, 0, 0);
	}

	/**
	 * Liefert die String-Repräsentation der Schiene.
	 *
	 * @return die String-Repräsentation der Schiene.
	 */
	public toString() : string {
		return this.representation;
	}

	/**
	 * Liefert die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 *
	 * @return die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 */
	gibDatenbankID() : number {
		return this.guiID;
	}

	/**
	 * Liefert eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 *
	 * @return eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 */
	gibRepresentation() : string {
		return this.representation;
	}

	/**
	 * Liefert die aktuelle Anzahl an Nichtwahlen.
	 *
	 * @return Die aktuelle Anzahl an Nichtwahlen.
	 */
	gibNichtwahlen() : number {
		return this.nichtwahlen;
	}

	/**
	 * Liefert ein Array aller Facharten (= Fachwahlen) des Schülers.
	 *
	 * @return Ein Array aller Facharten (= Fachwahlen) des Schülers.
	 */
	gibFacharten() : Array<KursblockungDynFachart> {
		return this.fachartArr;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens einen Multikurs hat.
	 * <br>Ein Multikurs ist ein Kurs, der über mehr als eine Schiene geht.
	 *
	 * @return TRUE, falls der Schüler mindestens einen Multikurs hat.
	 */
	gibHatMultikurs() : boolean {
		for (const fachart of this.fachartArr)
			if (fachart.gibHatMultikurs())
				return true;
		return false;
	}

	/**
	 * Liefert ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 *
	 * @return ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 */
	gibKurswahlen() : Array<KursblockungDynKurs | null> {
		return this.fachartZuKurs;
	}

	/**
	 * Liefert TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 *
	 * @param kurs  Der Kurs in dem der Schüler potentiell ist.
	 *
	 * @return TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 */
	gibIstInKurs(kurs : KursblockungDynKurs | null) : boolean {
		for (const zugeordneterKurs of this.fachartZuKurs)
			if (zugeordneterKurs as unknown === kurs as unknown)
				return true;
		return false;
	}

	/**
	 * Setzt alle Facharten (=Fachwahlen) des Schülers.
	 *
	 * @param pFacharten  Die Facharten des Schülers.
	 */
	aktionSetzeFachartenUndIDs(pFacharten : Array<KursblockungDynFachart>) : void {
		const nFacharten : number = pFacharten.length;
		this.fachartArr = pFacharten;
		this.fachartZuKurs = Array(nFacharten).fill(null);
		this.fachartZuKursSaveS = Array(nFacharten).fill(null);
		this.fachartZuKursSaveK = Array(nFacharten).fill(null);
		this.fachartZuKursSaveG = Array(nFacharten).fill(null);
		this.statistik.aktionNichtwahlenVeraendern(nFacharten);
		this.nichtwahlen = nFacharten;
		for (let i : number = 1; i < nFacharten; i++)
			for (let j : number = i; j >= 1; j--) {
				const anzL : number = this.fachartArr[j - 1].gibKurseMax();
				const anzR : number = this.fachartArr[j].gibKurseMax();
				if (anzL > anzR) {
					const fL : KursblockungDynFachart = this.fachartArr[j - 1];
					const fR : KursblockungDynFachart = this.fachartArr[j];
					this.fachartArr[j - 1] = fR;
					this.fachartArr[j] = fL;
				}
			}
		this.matrix = new KursblockungMatrix(this._random, nFacharten, this.schieneBelegt.length);
	}

	/**
	 * Sperrt einen bestimmten Kurs für diesen Schüler.
	 *
	 * @param pInterneKursID  Die ID des Kurses, der gesperrt wird.
	 */
	aktionSetzeKursSperrung(pInterneKursID : number) : void {
		this.kursGesperrt[pInterneKursID] = true;
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand S.
	 */
	aktionZustandSpeichernS() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveS, 0, this.fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand K.
	 */
	aktionZustandSpeichernK() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveK, 0, this.fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand G.
	 */
	aktionZustandSpeichernG() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveG, 0, this.fachartZuKurs.length);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand S gespeichert wurden.
	 */
	aktionZustandLadenS() : void {
		this.aktionZustandLaden(this.fachartZuKursSaveS);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand K gespeichert wurden.
	 */
	aktionZustandLadenK() : void {
		this.aktionZustandLaden(this.fachartZuKursSaveK);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand G gespeichert wurden.
	 */
	aktionZustandLadenG() : void {
		this.aktionZustandLaden(this.fachartZuKursSaveG);
	}

	/**
	 * Entfernt den Schüler aus seinen aktuell zugeordneten Kursen.
	 */
	aktionKurseAlleEntfernen() : void {
		for (let i : number = 0; i < this.fachartArr.length; i++) {
			const kurs : KursblockungDynKurs | null = this.fachartZuKurs[i];
			if (kurs !== null)
				this.aktionKursEntfernen(i, kurs);
		}
	}

	/**
	 * Verteilt alle Kurse des S., die über mehr als eine Schiene gehen.
	 */
	aktionKurseVerteilenNurMultikurseZufaellig() : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.fachartArr.length);
		for (let pFachart : number = 0; pFachart < this.fachartArr.length; pFachart++) {
			const iFachart : number = perm[pFachart];
			if (this.fachartZuKurs[iFachart] !== null)
				continue;
			const fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			if (!fachart.gibHatMultikurs())
				continue;
			const kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			const perm2 : Array<number> = KursblockungStatic.gibPermutation(this._random, kurse.length);
			for (let pKurs : number = 0; pKurs < perm2.length; pKurs++) {
				const kurs : KursblockungDynKurs = kurse[perm2[pKurs]];
				if (!kurs.gibIstErlaubtFuerSchueler(this.kursGesperrt))
					continue;
				let waehlbar : boolean = true;
				for (const nr of kurs.gibSchienenLage())
					if (this.schieneBelegt[nr])
						waehlbar = false;
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Verteilt alle Kurse des S. von denen es (pro Fachart) nur einen gibt.
	 */
	aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs() : void {
		for (let iFachart : number = 0; iFachart < this.fachartArr.length; iFachart++) {
			if (this.fachartZuKurs[iFachart] !== null)
				continue;
			const fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			const kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			let erlaubt : number = 0;
			for (const kurs of kurse)
				if (kurs.gibIstErlaubtFuerSchueler(this.kursGesperrt))
					erlaubt++;
			if (erlaubt !== 1)
				continue;
			for (let iKurse : number = 0; iKurse < kurse.length; iKurse++) {
				const kurs : KursblockungDynKurs = kurse[iKurse];
				if (!kurs.gibIstErlaubtFuerSchueler(this.kursGesperrt))
					continue;
				let waehlbar : boolean = true;
				for (const nr of kurs.gibSchienenLage())
					if (this.schieneBelegt[nr])
						waehlbar = false;
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines gewichteten Matching Algorithmus.
	 * Kleinere Kurse werden in der Wahl bevorzugt.
	 */
	aktionKurseVerteilenMitBipartiteMatchingGewichtetem() : void {
		const _INFINITY : number = 1000000;
		const data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				data[r][c] = _INFINITY;
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs())
				continue;
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				if (!this.schieneBelegt[c]) {
					const kurs : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this.kursGesperrt);
					if (kurs !== null)
						data[r][c] = kurs.gibGewichtetesMatchingBewertung();
				}
		}
		const r2c : Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs())
				continue;
			const c : number = r2c[r];
			if (c < 0)
				continue;
			if (data[r][c] === _INFINITY)
				continue;
			const kursGefunden : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this.kursGesperrt);
			if (kursGefunden !== null)
				this.aktionKursHinzufuegen(r, kursGefunden);
			else
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!")
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines Bipartiten-Matching-Algorithmus.
	 */
	aktionKurseVerteilenMitBipartiteMatching() : void {
		const data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				data[r][c] = 0;
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs())
				continue;
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				if (!this.schieneBelegt[c]) {
					const kurs : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this.kursGesperrt);
					if (kurs !== null)
						data[r][c] = 1;
				}
		}
		const r2c : Array<number> = this.matrix.gibMaximalesBipartitesMatching(true);
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs())
				continue;
			const c : number = r2c[r];
			if (c === -1)
				continue;
			const kursGefunden : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this.kursGesperrt);
			if (kursGefunden !== null)
				this.aktionKursHinzufuegen(r, kursGefunden);
			else
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!")
		}
	}

	/**
	 * Die (nicht Multi) Facharten des S. werden auf eine Schiene gematched. Falls dies nicht klappt, wird der Fachart
	 * gesagt, dass einer ihrer Kurse die Schiene wechseln muss. Um welche Schiene es sich dabei handelt, wird durch den
	 * Matching-Algorithmus berechnet. Der S. wird bei den Berechnungen nicht einem Kurs hinzugefügt.
	 *
	 * @return TRUE, falls sich die Lage der Kurse verändert hat.
	 */
	aktionKurseVerteilenNachDeinemWunsch() : boolean {
		const _VAL_UNGUELTIG : number = 1000000;
		const _VAL_KURS_GEWAEHLT : number = 0;
		const _VAL_KURS_MUSS_WANDERN : number = 1;
		const data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			const fachart : KursblockungDynFachart | null = this.fachartArr[r];
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				data[r][c] = _VAL_UNGUELTIG;
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs())
				continue;
			for (let c : number = 0; c < this.schieneBelegt.length; c++)
				if (!this.schieneBelegt[c]) {
					if (fachart.gibHatKursInSchiene(c, this.kursGesperrt))
						data[r][c] = _VAL_KURS_GEWAEHLT;
					else
						data[r][c] = fachart.gibHatKursMitFreierSchiene(c, this.kursGesperrt) ? _VAL_KURS_MUSS_WANDERN : _VAL_UNGUELTIG;
				}
		}
		const r2c : Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		let kurslage_veraendert : boolean = false;
		for (let r : number = 0; r < this.fachartArr.length; r++) {
			const fachart : KursblockungDynFachart | null = this.fachartArr[r];
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs())
				continue;
			const c : number = r2c[r];
			if (c < 0)
				continue;
			if (data[r][c] === _VAL_UNGUELTIG)
				continue;
			if (data[r][c] === _VAL_KURS_GEWAEHLT)
				continue;
			fachart.aktionZufaelligerKursWandertNachSchiene(c);
			kurslage_veraendert = true;
		}
		return kurslage_veraendert;
	}

	/**
	 * Geht die Facharten durch (Facharten mit einer kleineren Kursanzahl zuerst) und geht dann pro Fachart alle Kurse
	 * durch (Kurse mit kleinerer Schüleranzahl zuerst). Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es
	 * geht weiter mit der nächsten Fachart. Ein Kurs ist wählbar, wenn nicht bereits ein Kurs zugeordnet wurde und die
	 * Schienen in den der Kurs sind frei sind.
	 */
	aktionKurseVerteilenZufaellig() : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.fachartArr.length);
		for (let pFachart : number = 0; pFachart < this.fachartArr.length; pFachart++) {
			const iFachart : number = perm[pFachart];
			if (this.fachartZuKurs[iFachart] !== null)
				continue;
			const fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			const kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			for (let iKurs : number = 0; iKurs < kurse.length; iKurs++) {
				const kurs : KursblockungDynKurs = kurse[iKurs];
				if (!kurs.gibIstErlaubtFuerSchueler(this.kursGesperrt))
					continue;
				let waehlbar : boolean = true;
				for (const nr of kurs.gibSchienenLage())
					if (this.schieneBelegt[nr])
						waehlbar = false;
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Ausgabe der aktuellen Kurslage zum debuggen.
	 */
	debugKurswahlen() : void {
		this._logger.modifyIndent(+4);
		this._logger.logLn("");
		this._logger.logLn(this.representation);
		const setSchienenLage : HashSet<number | null> | null = new HashSet();
		for (let i : number = 0; i < this.fachartZuKurs.length; i++) {
			const kurs : KursblockungDynKurs | null = this.fachartZuKurs[i];
			if (kurs === null)
				continue;
			this._logger.logLn("    " + kurs.toString()! + "    " + Arrays.toString(kurs.gibSchienenLage())!);
			for (const schiene of kurs.gibSchienenLage())
				if (!setSchienenLage.add(schiene)) {
					this._logger.logLn("Kollision");
					return;
				}
		}
		this._logger.modifyIndent(-4);
	}

	private aktionZustandLaden(wahl : Array<KursblockungDynKurs | null>) : void {
		this.aktionKurseAlleEntfernen();
		for (let i : number = 0; i < this.fachartZuKurs.length; i++) {
			const kurs : KursblockungDynKurs | null = wahl[i];
			if (kurs !== null) {
				if (kurs.gibIstErlaubtFuerSchueler(this.kursGesperrt))
					this.aktionKursHinzufuegen(i, kurs);
				else
					throw new DeveloperNotificationException("FEHLER: Schüler " + this.guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.")
			}
		}
	}

	private aktionKursHinzufuegen(fachartIndex : number, kurs : KursblockungDynKurs) : void {
		kurs.aktionSchuelerHinzufuegen();
		this.statistik.aktionNichtwahlenVeraendern(-1);
		this.nichtwahlen--;
		for (const nr of kurs.gibSchienenLage()) {
			DeveloperNotificationException.ifTrue("FEHLER: Schienen-Doppelbelegung! " + this.representation!, this.schieneBelegt[nr]);
			this.schieneBelegt[nr] = true;
		}
		this.fachartZuKurs[fachartIndex] = kurs;
	}

	private aktionKursEntfernen(fachartIndex : number, kurs : KursblockungDynKurs) : void {
		kurs.aktionSchuelerEntfernen();
		this.statistik.aktionNichtwahlenVeraendern(+1);
		this.nichtwahlen++;
		for (const nr of kurs.gibSchienenLage()) {
			DeveloperNotificationException.ifTrue("FEHLER: Kurs ist gar nicht in Schiene ! " + this.representation!, !this.schieneBelegt[nr]);
			this.schieneBelegt[nr] = false;
		}
		this.fachartZuKurs[fachartIndex] = null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynSchueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynSchueler'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynSchueler(obj : unknown) : KursblockungDynSchueler {
	return obj as KursblockungDynSchueler;
}
