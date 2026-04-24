import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { KursblockungDynKurs, cast_de_svws_nrw_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { Random } from '../../java/util/Random';
import { KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { Class } from '../../java/lang/Class';
import { Arrays } from '../../java/util/Arrays';
import { HashSet } from '../../java/util/HashSet';

export class KursblockungDynSchueler extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly rnd: Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly log: Logger;

	/**
	 * Die ID (von der GUI) des Schülers, beispielsweise 42.
	 */
	private readonly guiID: number;

	/**
	 * Die interne ID des Schülers.
	 */
	readonly internalSchuelerID: number;

	/**
	 * Ein String-Darstellung des Schüler für Warnungen und Fehlermeldungen, beispielsweise 'Mareike Musterfrau'.
	 */
	private readonly representation: string;

	/**
	 * Alle Facharten (=Fachwahlen) des Schüler, z.B. 'D;LK'.
	 */
	private fachartArr: Array<KursblockungDynFachart>;

	/**
	 * Der aktuell zur Fachart zugeordnete Kurs.
	 */
	private fachartZuKurs: Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveS: Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveK: Array<KursblockungDynKurs | null>;

	private fachartZuKursSaveG: Array<KursblockungDynKurs | null>;

	/**
	 * Referenz zur Statistik, um diese über Nichtwahlen zu informieren.
	 */
	private readonly statistik: KursblockungDynStatistik;

	/**
	 * Die aktuellen Nichtwahlen dieses Schülers.
	 */
	private nichtwahlen: number = 0;

	/**
	 * Die aktuelle Information darüber, ob die Schiene des Schülers belegt ist.
	 */
	private readonly schieneBelegt: Array<boolean>;

	/**
	 * Diese Datenstruktur wird verwendet um bei bestimmten Algorithmus Kurse auf Schienen zu verteilen.
	 */
	private matrix: KursblockungMatrix;

	/**
	 * Verbotene Kurse des Schülers. Diese dürfen nicht belegt werden.
	 */
	readonly kursGesperrt: Array<boolean>;

	/**
	 * Soll der Schüler ignoriert werden beim Verteilen?
	 */
	regel16schuelerIgnorieren: boolean = false;


	/**
	 * Im Konstruktor wird {@code pSchueler} in ein Objekt dieser Klasse umgewandelt.
	 *
	 * @param random           Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger           Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param statistik        Referenz um die Nichtwahlen mitzuteilen.
	 * @param schuelerID       Die ID des Schülers von der GUI/DB.
	 * @param schienenAnzahl   Wir benötigt, um {@link #schieneBelegt} zu initialisieren.
	 * @param kursAnzahl       Die Anzahl aller Kurse. Wird benötigt, damit {@link #kursGesperrt} initialisiert werden kann.
	 * @param internalID       Eine interne ID für schnellen Zugriff.
	 */
	constructor(logger: Logger, random: Random, schuelerID: number, statistik: KursblockungDynStatistik, schienenAnzahl: number, kursAnzahl: number, internalID: number) {
		super();
		this.rnd = random;
		this.log = logger;
		this.guiID = schuelerID;
		this.internalSchuelerID = internalID;
		this.representation = "Schüler " + schuelerID;
		this.statistik = statistik;
		this.fachartArr = Array(0).fill(null);
		this.fachartZuKurs = Array(0).fill(null);
		this.fachartZuKursSaveS = Array(0).fill(null);
		this.fachartZuKursSaveK = Array(0).fill(null);
		this.fachartZuKursSaveG = Array(0).fill(null);
		this.nichtwahlen = 0;
		this.schieneBelegt = Array(schienenAnzahl).fill(false);
		this.kursGesperrt = Array(kursAnzahl).fill(false);
		this.regel16schuelerIgnorieren = false;
		this.matrix = new KursblockungMatrix(this.rnd, 0, 0);
	}

	/**
	 * Liefert die String-Repräsentation der Schiene.
	 *
	 * @return die String-Repräsentation der Schiene.
	 */
	public toString(): string {
		return this.representation;
	}

	/**
	 * Liefert die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 *
	 * @return die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 */
	gibDatenbankID(): number {
		return this.guiID;
	}

	/**
	 * Liefert eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 *
	 * @return eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 */
	gibRepresentation(): string {
		return this.representation;
	}

	/**
	 * Liefert die aktuelle Anzahl an Nichtwahlen.
	 *
	 * @return Die aktuelle Anzahl an Nichtwahlen.
	 */
	gibNichtwahlen(): number {
		return this.nichtwahlen;
	}

	/**
	 * Liefert ein Array aller Facharten (= Fachwahlen) des Schülers.
	 *
	 * @return Ein Array aller Facharten (= Fachwahlen) des Schülers.
	 */
	gibFacharten(): Array<KursblockungDynFachart> {
		return this.fachartArr;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens einen Multikurs hat.
	 * <br>Ein Multikurs ist ein Kurs, der über mehr als eine Schiene geht.
	 *
	 * @return TRUE, falls der Schüler mindestens einen Multikurs hat.
	 */
	gibHatMultikurs(): boolean {
		for (const fachart of this.fachartArr) {
			if (fachart.gibHatMultikurs()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 *
	 * @return ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 */
	gibKurswahlen(): Array<KursblockungDynKurs | null> {
		return this.fachartZuKurs;
	}

	/**
	 * Liefert TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 *
	 * @param kurs  Der Kurs in dem der Schüler potentiell ist.
	 *
	 * @return TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 */
	public gibIstInKurs(kurs: KursblockungDynKurs | null): boolean {
		for (const zugeordneterKurs of this.fachartZuKurs) {
			if (zugeordneterKurs as unknown === kurs as unknown) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs für den Schüler erlaubt ist und zudem die Schienen frei sind.
	 *
	 * @param kurs   Das  {@link KursblockungDynKurs}-Objekt.
	 *
	 * @return TRUE, falls der Kurs für den Schüler erlaubt ist und zudem die Schienen frei sind.
	 */
	gibIstKursFuerSchuelerWaehlbar(kurs: KursblockungDynKurs): boolean {
		if (!kurs.gibIstErlaubtFuerSchueler(this)) {
			return false;
		}
		for (const nrSchiene of kurs.gibSchienenLage()) {
			if (this.schieneBelegt[nrSchiene]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Liefert die zum Fach zugehörige Fachart (= Fachwahl) des Schülers.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die zum Fach zugehörige Fachart (= Fachwahl) des Schülers.
	 */
	private gibFachartZuFachID(idFach: number): KursblockungDynFachart {
		for (const fachart of this.fachartArr) {
			if (fachart.gibFach().id === idFach) {
				return fachart;
			}
		}
		throw new DeveloperNotificationException(this.representation + " hat kein Fach mit ID = " + idFach + "!")
	}

	/**
	 * Setzt alle Facharten (=Fachwahlen) des Schülers.
	 *
	 * @param pFacharten  Die Facharten des Schülers.
	 */
	aktionSetzeFachartenUndIDs(pFacharten: Array<KursblockungDynFachart>): void {
		const nFacharten: number = pFacharten.length;
		this.fachartArr = pFacharten;
		this.fachartZuKurs = Array(nFacharten).fill(null);
		this.fachartZuKursSaveS = Array(nFacharten).fill(null);
		this.fachartZuKursSaveK = Array(nFacharten).fill(null);
		this.fachartZuKursSaveG = Array(nFacharten).fill(null);
		this.statistik.aktionNichtwahlenVeraendern(nFacharten);
		this.nichtwahlen = nFacharten;
		for (let i: number = 1; i < nFacharten; i++) {
			for (let j: number = i; j >= 1; j--) {
				const anzL: number = this.fachartArr[j - 1].gibKurseMax();
				const anzR: number = this.fachartArr[j].gibKurseMax();
				if (anzL > anzR) {
					const fL: KursblockungDynFachart = this.fachartArr[j - 1];
					const fR: KursblockungDynFachart = this.fachartArr[j];
					this.fachartArr[j - 1] = fR;
					this.fachartArr[j] = fL;
				}
			}
		}
		this.matrix = new KursblockungMatrix(this.rnd, nFacharten, this.schieneBelegt.length);
	}

	/**
	 * Sperrt einen bestimmten Kurs für diesen Schüler.
	 *
	 * @param pInterneKursID  Die ID des Kurses, der gesperrt wird.
	 */
	aktionSetzeKursSperrung(pInterneKursID: number): void {
		this.kursGesperrt[pInterneKursID] = true;
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand S.
	 */
	aktionZustandSpeichernS(): void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveS, 0, this.fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand K.
	 */
	aktionZustandSpeichernK(): void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveK, 0, this.fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand G.
	 */
	aktionZustandSpeichernG(): void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveG, 0, this.fachartZuKurs.length);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand S gespeichert wurden.
	 */
	aktionZustandLadenS(): void {
		this.aktionZustandLaden(this.fachartZuKursSaveS);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand K gespeichert wurden.
	 */
	aktionZustandLadenK(): void {
		this.aktionZustandLaden(this.fachartZuKursSaveK);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die im {@link KursblockungDynSchueler}-Objekt gespeichert wurden.
	 *
	 * @param b        Das {@link KursblockungDynSchueler}-Objekt.
	 * @param kursArr  Das Array aller {@link KursblockungDynKurs}-Objekte.
	 */
	aktionZustandLadenVon(b: KursblockungDynSchueler, kursArr: Array<KursblockungDynKurs>): void {
		this.aktionKurseAlleEntfernen();
		for (let i: number = 0; i < b.fachartZuKurs.length; i++) {
			const kursB: KursblockungDynKurs | null = b.fachartZuKurs[i];
			if (kursB !== null) {
				const kurs: KursblockungDynKurs | null = kursArr[kursB.gibInternalID()];
				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					this.aktionKursHinzufuegen(i, kurs);
				} else {
					throw new DeveloperNotificationException("FEHLER: Schüler " + this.guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.")
				}
			}
		}
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand G gespeichert wurden.
	 */
	aktionZustandLadenG(): void {
		this.aktionZustandLaden(this.fachartZuKursSaveG);
	}

	/**
	 * Entfernt den Schüler aus seinen aktuell zugeordneten Kursen.
	 */
	aktionKurseAlleEntfernen(): void {
		for (let i: number = 0; i < this.fachartArr.length; i++) {
			const kurs: KursblockungDynKurs | null = this.fachartZuKurs[i];
			if (kurs !== null) {
				this.aktionKursEntfernen(i, kurs);
			}
		}
	}

	/**
	 * Sucht für die angegebene Fachart einen wählbaren Kurs und ordnet ihn dem Schüler zu.
	 * Dabei wird nur dann ein Kurs hinzugefügt, wenn für die Fachart noch kein Kurs belegt ist,
	 * der Kurs für den Schüler erlaubt ist und alle zugehörigen Schienen noch frei sind.
	 *
	 * Die Kurs-Reihenfolge hängt von {@code kurseZufaellig} ab:
	 * Ist der Wert {@code true}, werden die Kurse der Fachart in zufälliger Reihenfolge geprüft,
	 * andernfalls in der vorgegebenen Reihenfolge.
	 *
	 * Wird ein wählbarer Kurs gefunden, so wird dieser direkt per
	 * {@code aktionKursHinzufuegen(iFachart, kurs)} zugeordnet und die Suche beendet.
	 *
	 * @param iFachart         Der Index der Fachart.
	 * @param kurseZufaellig   {@code true}, wenn die Kurse in zufälliger Reihenfolge geprüft werden sollen.
	 */
	private aktionKursDerFachartSuchenUndVerteilen(iFachart: number, kurseZufaellig: boolean): void {
		const fachart: KursblockungDynFachart = this.fachartArr[iFachart];
		const kurse: Array<KursblockungDynKurs> = fachart.gibKurse();
		const kursReihenfolge: Array<number> = Array(kurse.length).fill(0);
		for (let i: number = 0; i < kursReihenfolge.length; i++) {
			kursReihenfolge[i] = i;
		}
		if (kurseZufaellig) {
			KursblockungStatic.aktionPermutiere(this.rnd, kursReihenfolge);
		}
		for (const iKurs of kursReihenfolge) {
			const kurs: KursblockungDynKurs = kurse[iKurs];
			if (this.gibIstKursFuerSchuelerWaehlbar(kurs)) {
				this.aktionKursHinzufuegen(iFachart, kurs);
				return;
			}
		}
	}

	/**
	 * Geht die Facharten durch (zufällig) und geht dann pro Fachart alle Kurse durch (nicht zufällig).
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	aktionKurseVerteilenZufaellig(): void {
		const permFachart: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.fachartArr.length);
		for (let pFachart: number = 0; pFachart < this.fachartArr.length; pFachart++) {
			const iFachart: number = permFachart[pFachart];
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			this.aktionKursDerFachartSuchenUndVerteilen(iFachart, false);
		}
	}

	/**
	 * Geht die Multikurs-Facharten durch (zufällig) und geht dann pro Fachart alle Kurse durch (zufällig).
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	aktionKurseVerteilenNurMultikurseZufaellig(): void {
		const permFachart: Array<number> = KursblockungStatic.gibPermutation(this.rnd, this.fachartArr.length);
		for (let pFachart: number = 0; pFachart < this.fachartArr.length; pFachart++) {
			const iFachart: number = permFachart[pFachart];
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			const fachart: KursblockungDynFachart = this.fachartArr[iFachart];
			if (fachart.gibHatMultikurs()) {
				this.aktionKursDerFachartSuchenUndVerteilen(iFachart, true);
			}
		}
	}

	/**
	 * Geht die Facharten durch (nicht zufällig) und überprüft, ob der Schüler nur einen erlaubten Kurs hat.
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs(): void {
		for (let iFachart: number = 0; iFachart < this.fachartArr.length; iFachart++) {
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			const fachart: KursblockungDynFachart = this.fachartArr[iFachart];
			const kurse: Array<KursblockungDynKurs> = fachart.gibKurse();
			let kursMoeglichkeiten: number = 0;
			for (const kurs of kurse) {
				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					kursMoeglichkeiten++;
				}
			}
			if (kursMoeglichkeiten === 1) {
				this.aktionKursDerFachartSuchenUndVerteilen(iFachart, false);
			}
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines gewichteten Matching Algorithmus.
	 * Kleinere Kurse werden in der Wahl bevorzugt.
	 */
	aktionKurseVerteilenMitBipartiteMatchingGewichtetem(): void {
		const _INFINITY: number = 1000000;
		const data: Array<Array<number>> = this.matrix.getMatrix();
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				data[r][c] = _INFINITY;
			}
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				if (!this.schieneBelegt[c]) {
					const kurs: KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
					if (kurs !== null) {
						data[r][c] = kurs.gibGewichtetesMatchingBewertung();
					}
				}
			}
		}
		const r2c: Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			const c: number = r2c[r];
			if (c < 0) {
				continue;
			}
			if (data[r][c] === _INFINITY) {
				continue;
			}
			const kursGefunden: KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
			if (kursGefunden !== null) {
				this.aktionKursHinzufuegen(r, kursGefunden);
			} else {
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!")
			}
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines Bipartiten-Matching-Algorithmus.
	 */
	aktionKurseVerteilenMitBipartiteMatching(): void {
		const data: Array<Array<number>> = this.matrix.getMatrix();
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				data[r][c] = 0;
			}
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				if (!this.schieneBelegt[c]) {
					const kurs: KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
					if (kurs !== null) {
						data[r][c] = 1;
					}
				}
			}
		}
		const r2c: Array<number> = this.matrix.gibMaximalesBipartitesMatching(true);
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			const c: number = r2c[r];
			if (c === -1) {
				continue;
			}
			const kursGefunden: KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
			if (kursGefunden !== null) {
				this.aktionKursHinzufuegen(r, kursGefunden);
			} else {
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!")
			}
		}
	}

	/**
	 * Die (nicht Multi) Facharten des S. werden auf eine Schiene gematched. Falls dies nicht klappt, wird der Fachart
	 * gesagt, dass einer ihrer Kurse die Schiene wechseln muss. Um welche Schiene es sich dabei handelt, wird durch den
	 * Matching-Algorithmus berechnet. Der S. wird bei den Berechnungen nicht einem Kurs hinzugefügt.
	 *
	 * @return TRUE, falls sich die Lage der Kurse verändert hat.
	 */
	aktionKurseVerteilenNachDeinemWunsch(): boolean {
		const _VAL_UNGUELTIG: number = 1000000;
		const _VAL_KURS_GEWAEHLT: number = 0;
		const _VAL_KURS_MUSS_WANDERN: number = 1;
		const data: Array<Array<number>> = this.matrix.getMatrix();
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			const fachart: KursblockungDynFachart | null = this.fachartArr[r];
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				data[r][c] = _VAL_UNGUELTIG;
			}
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs()) {
				continue;
			}
			for (let c: number = 0; c < this.schieneBelegt.length; c++) {
				if (!this.schieneBelegt[c]) {
					if (fachart.gibHatSchuelerKursInSchiene(c, this)) {
						data[r][c] = _VAL_KURS_GEWAEHLT;
					} else {
						data[r][c] = fachart.gibHatSchuelerKursMitFreierSchiene(c, this) ? _VAL_KURS_MUSS_WANDERN : _VAL_UNGUELTIG;
					}
				}
			}
		}
		const r2c: Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		let kurslageHatSichVeraendert: boolean = false;
		for (let r: number = 0; r < this.fachartArr.length; r++) {
			const fachart: KursblockungDynFachart | null = this.fachartArr[r];
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs()) {
				continue;
			}
			const c: number = r2c[r];
			if (c < 0) {
				continue;
			}
			if (data[r][c] === _VAL_UNGUELTIG) {
				continue;
			}
			if (data[r][c] === _VAL_KURS_GEWAEHLT) {
				continue;
			}
			fachart.aktionZufaelligerKursWandertNachSchiene(c);
			kurslageHatSichVeraendert = true;
		}
		return kurslageHatSichVeraendert;
	}

	/**
	 * Ausgabe der aktuellen Kurslage zum debuggen.
	 */
	debugKurswahlen(): void {
		this.log.modifyIndent(+4);
		this.log.logLn("");
		this.log.logLn(this.representation);
		const setSchienenLage: HashSet<number> | null = new HashSet<number>();
		for (const kurs of this.fachartZuKurs) {
			if (kurs === null) {
				continue;
			}
			this.log.logLn("    " + kurs.toString() + "    " + Arrays.toString(kurs.gibSchienenLage()));
			for (const schiene of kurs.gibSchienenLage()) {
				if (!setSchienenLage.add(schiene)) {
					this.log.logLn("Kollision");
					return;
				}
			}
		}
		this.log.modifyIndent(-4);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	setzeZusammenMitSchuelerInFach(that: KursblockungDynSchueler, idFach: number): void {
		const fachart1: KursblockungDynFachart = this.gibFachartZuFachID(idFach);
		const fachart2: KursblockungDynFachart = that.gibFachartZuFachID(idFach);
		if (fachart1.gibNr() !== fachart2.gibNr()) {
			throw new DeveloperNotificationException("Regel 11:" + this.representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2 + " haben nicht die selbe Kursart!")
		}
		fachart1.setzeSchuelerZusammenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler nicht zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	setzeVerbietenMitSchuelerInFach(that: KursblockungDynSchueler, idFach: number): void {
		const fachart1: KursblockungDynFachart = this.gibFachartZuFachID(idFach);
		const fachart2: KursblockungDynFachart = that.gibFachartZuFachID(idFach);
		if (fachart1.gibNr() !== fachart2.gibNr()) {
			throw new DeveloperNotificationException("Regel 12:" + this.representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2 + " haben nicht die selbe Kursart!")
		}
		fachart1.setzeSchuelerVerbietenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler bei gemeinsamen Kursen zusammen in einem Kurs landen sollen.
	 *
	 * @param that  Der übergebene Schüler.
	 */
	public setzeZusammenMitSchueler(that: KursblockungDynSchueler): void {
		for (const fachart1 of this.fachartArr) {
			for (const fachart2 of this.fachartArr) {
				if (fachart1.gibNr() === fachart2.gibNr()) {
					fachart1.setzeSchuelerZusammenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
				}
			}
		}
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler bei gemeinsamen Kursen, nicht zusammen in einem Kurs landen sollen.
	 *
	 * @param that  Der übergebene Schüler.
	 */
	setzeVerbietenMitSchueler(that: KursblockungDynSchueler): void {
		for (const fachart1 of this.fachartArr) {
			for (const fachart2 of this.fachartArr) {
				if (fachart1.gibNr() === fachart2.gibNr()) {
					fachart1.setzeSchuelerVerbietenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
				}
			}
		}
	}

	/**
	 * Wendet an, dass der Schüler bei der Blockung nicht auf Kurse verteilt werden soll.
	 */
	setzeSperreBeiKursverteilung(): void {
		for (let i: number = 0; i < this.schieneBelegt.length; i++) {
			this.schieneBelegt[i] = true;
		}
		this.regel16schuelerIgnorieren = true;
	}

	private aktionZustandLaden(wahl: Array<KursblockungDynKurs | null>): void {
		this.aktionKurseAlleEntfernen();
		for (let i: number = 0; i < this.fachartZuKurs.length; i++) {
			const kurs: KursblockungDynKurs | null = wahl[i];
			if (kurs !== null) {
				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					this.aktionKursHinzufuegen(i, kurs);
				} else {
					throw new DeveloperNotificationException("FEHLER: Schüler " + this.guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.")
				}
			}
		}
	}

	private aktionKursHinzufuegen(fachartIndex: number, kurs: KursblockungDynKurs): void {
		kurs.aktionSchuelerHinzufuegen(this.internalSchuelerID);
		this.statistik.aktionNichtwahlenVeraendern(-1);
		this.nichtwahlen--;
		for (const nr of kurs.gibSchienenLage()) {
			DeveloperNotificationException.ifTrue("FEHLER: Schienen-Doppelbelegung! " + this.representation, this.schieneBelegt[nr]);
			this.schieneBelegt[nr] = true;
		}
		this.fachartZuKurs[fachartIndex] = kurs;
	}

	public aktionKursEntfernen(fachartIndex: number, kurs: KursblockungDynKurs) : void;

	/**
	 * Versucht den S. aus dem Kurs zu entfernen.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 */
	public aktionKursEntfernen(idKursDB: number) : void;

	/**
	 * Implementation for method overloads of 'aktionKursEntfernen'
	 */
	public aktionKursEntfernen(__param0: number, __param1?: KursblockungDynKurs): void {
		if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.kursblockung.KursblockungDynKurs'))))) {
			const fachartIndex: number = __param0 as number;
			const kurs: KursblockungDynKurs = cast_de_svws_nrw_core_kursblockung_KursblockungDynKurs(__param1);
			kurs.aktionSchuelerEntfernen(this.internalSchuelerID);
			this.statistik.aktionNichtwahlenVeraendern(+1);
			this.nichtwahlen++;
			for (const nr of kurs.gibSchienenLage()) {
				DeveloperNotificationException.ifTrue("FEHLER: Kurs ist gar nicht in Schiene ! " + this.representation, !this.schieneBelegt[nr]);
				this.schieneBelegt[nr] = false;
			}
			this.fachartZuKurs[fachartIndex] = null;
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && (__param1 === undefined)) {
			const idKursDB: number = __param0 as number;
			for (let fachartIndex: number = 0; fachartIndex < this.fachartArr.length; fachartIndex++) {
				const fachart: KursblockungDynFachart = this.fachartArr[fachartIndex];
				for (const kurs of fachart.gibKurse()) {
					if (kurs.gibDatenbankID() === idKursDB) {
						const kursVorher: KursblockungDynKurs | null = this.fachartZuKurs[fachartIndex];
						if (kursVorher !== null) {
							this.aktionKursEntfernen(fachartIndex, kursVorher);
						}
					}
				}
			}
		} else throw new Error('invalid method overload');
	}

	/**
	 * Versucht den S. in den Kurs zu setzen. Entfernt ggf. einen anderen Kurs der selben Fachart dafür.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 */
	public aktionKursSetzen(idKursDB: number): void {
		for (let fachartIndex: number = 0; fachartIndex < this.fachartArr.length; fachartIndex++) {
			const fachart: KursblockungDynFachart = this.fachartArr[fachartIndex];
			for (const kurs of fachart.gibKurse()) {
				if ((kurs.gibDatenbankID() === idKursDB) && (kurs.gibIstErlaubtFuerSchueler(this))) {
					const kursVorher: KursblockungDynKurs | null = this.fachartZuKurs[fachartIndex];
					if (kursVorher !== null) {
						this.aktionKursEntfernen(fachartIndex, kursVorher);
					}
					this.aktionKursHinzufuegen(fachartIndex, kurs);
				}
			}
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynSchueler'].includes(name);
	}

	public static readonly class = new Class<KursblockungDynSchueler>('de.svws_nrw.core.kursblockung.KursblockungDynSchueler');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynSchueler(obj: unknown): KursblockungDynSchueler {
	return obj as KursblockungDynSchueler;
}
