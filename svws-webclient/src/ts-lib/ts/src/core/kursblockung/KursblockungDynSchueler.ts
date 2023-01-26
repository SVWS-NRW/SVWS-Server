import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { KursblockungDynFachart, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { KursblockungDynStatistik, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungStatic, cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungMatrix, cast_de_nrw_schule_svws_core_kursblockung_KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../java/util/Arrays';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { HashSet, cast_java_util_HashSet } from '../../java/util/HashSet';

export class KursblockungDynSchueler extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. 
	 */
	private readonly _random : Random;

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
	 * @param pStatistik      Referenz um die Nichtwahlen mitzuteilen.
	 * @param pSchuelerID     Die ID des Schülers von der GUI/DB.
	 * @param pSchienenAnzahl Wir benötigt, um {@link #schieneBelegt} zu initialisieren.
	 * @param pKursAnzahl     Die Anzahl aller Kurse. Wird benötigt, damit {@link #kursGesperrt} initialisiert werden
	 *                        kann.
	 */
	constructor(pRandom : Random, pSchuelerID : number, pStatistik : KursblockungDynStatistik, pSchienenAnzahl : number, pKursAnzahl : number) {
		super();
		this._random = pRandom;
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

	public toString() : string {
		return this.representation;
	}

	/**
	 * Liefert die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 * 
	 * @return Die ID (von der GUI) dieses Schülers.
	 */
	gibDatenbankID() : number {
		return this.guiID;
	}

	/**
	 * Eine String-Darstellung des Schülers. Beinhaltet meistens den Vornamen, den Nachnamen, das Geburtsdatum und das
	 * Geschlecht.
	 * 
	 * @return Eine String-Darstellung des Schülers.
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
	 * Liefert TRUE, falls der Schüler mindestens einen Multikurs hat. Ein Multikurs ist ein Kurs, der über mehr als
	 * eine Schiene geht.
	 * 
	 * @return TRUE, falls der Schüler mindestens einen Multikurs hat.
	 */
	gibHatMultikurs() : boolean {
		for (let fachart of this.fachartArr) {
			if (fachart.gibHatMultikurs()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 * 
	 * @return Ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 */
	gibKurswahlen() : Array<KursblockungDynKurs | null> {
		return this.fachartZuKurs;
	}

	/**
	 * Setzt alle Facharten (=Fachwahlen) des Schülers.
	 * 
	 * @param pFacharten Die Facharten des Schülers.
	 */
	aktionSetzeFachartenUndIDs(pFacharten : Array<KursblockungDynFachart>) : void {
		let nFacharten : number = pFacharten.length;
		this.fachartArr = pFacharten;
		this.fachartZuKurs = Array(nFacharten).fill(null);
		this.fachartZuKursSaveS = Array(nFacharten).fill(null);
		this.fachartZuKursSaveK = Array(nFacharten).fill(null);
		this.fachartZuKursSaveG = Array(nFacharten).fill(null);
		this.statistik.aktionNichtwahlenVeraendern(nFacharten);
		this.nichtwahlen = nFacharten;
		for (let i : number = 1; i < nFacharten; i++)
			for (let j : number = i; j >= 1; j--){
				let anzL : number = this.fachartArr[j - 1].gibKurseMax();
				let anzR : number = this.fachartArr[j].gibKurseMax();
				if (anzL > anzR) {
					let fL : KursblockungDynFachart = this.fachartArr[j - 1];
					let fR : KursblockungDynFachart = this.fachartArr[j];
					this.fachartArr[j - 1] = fR;
					this.fachartArr[j] = fL;
				}
			}
		this.matrix = new KursblockungMatrix(this._random, nFacharten, this.schieneBelegt.length);
	}

	/**
	 * Sperrt einen bestimmten Kurs für diesen Schüler.
	 * 
	 * @param pInterneKursID Die ID des Kurses, der gesperrt wird.
	 */
	aktionSetzeKursSperrung(pInterneKursID : number) : void {
		this.kursGesperrt[pInterneKursID] = true;
	}

	/**
	 *Speichert die aktuell belegten Kurse im Zustand S. 
	 */
	aktionZustandSpeichernS() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveS, 0, this.fachartZuKurs.length);
	}

	/**
	 *Speichert die aktuell belegten Kurse im Zustand K. 
	 */
	aktionZustandSpeichernK() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveK, 0, this.fachartZuKurs.length);
	}

	/**
	 *Speichert die aktuell belegten Kurse im Zustand G. 
	 */
	aktionZustandSpeichernG() : void {
		System.arraycopy(this.fachartZuKurs, 0, this.fachartZuKursSaveG, 0, this.fachartZuKurs.length);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand S
	 * gespeichert wurden.
	 */
	aktionZustandLadenS() : void {
		this.aktionWaehleKurse(this.fachartZuKursSaveS);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand K
	 * gespeichert wurden.
	 */
	aktionZustandLadenK() : void {
		this.aktionWaehleKurse(this.fachartZuKursSaveK);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand G
	 * gespeichert wurden.
	 */
	aktionZustandLadenG() : void {
		this.aktionWaehleKurse(this.fachartZuKursSaveG);
	}

	private aktionWaehleKurse(wahl : Array<KursblockungDynKurs | null>) : void {
		this.aktionKurseAlleEntfernen();
		for (let i : number = 0; i < this.fachartZuKurs.length; i++){
			let kurs : KursblockungDynKurs | null = wahl[i];
			if (kurs === null) {
				continue;
			}
			if (this.kursGesperrt[kurs.gibInternalID()]) {
				console.log(JSON.stringify("FEHLER: Schüler " + this.guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen."));
			}
			this.aktionKursHinzufuegen(i, kurs);
		}
	}

	/**
	 *Entfernt den Schüler aus seinen aktuell zugeordneten Kursen. 
	 */
	aktionKurseAlleEntfernen() : void {
		for (let i : number = 0; i < this.fachartArr.length; i++){
			let kurs : KursblockungDynKurs | null = this.fachartZuKurs[i];
			if (kurs !== null) {
				this.aktionKursEntfernen(i, kurs);
			}
		}
	}

	/**
	 *Verteilt alle Kurse des S., die über mehr als eine Schiene gehen. 
	 */
	aktionKurseVerteilenNurMultikurseZufaellig() : void {
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.fachartArr.length);
		for (let pFachart : number = 0; pFachart < this.fachartArr.length; pFachart++){
			let iFachart : number = perm[pFachart];
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			let fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			if (!fachart.gibHatMultikurs()) {
				continue;
			}
			let kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			let perm2 : Array<number> = KursblockungStatic.gibPermutation(this._random, kurse.length);
			for (let pKurs : number = 0; pKurs < perm2.length; pKurs++){
				let kurs : KursblockungDynKurs = kurse[perm2[pKurs]];
				if (this.kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}
				let waehlbar : boolean = true;
				for (let nr of kurs.gibSchienenLage()) {
					if (this.schieneBelegt[nr]) {
						waehlbar = false;
					}
				}
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 *Verteilt alle Kurse des S. von denen es pro Fachart nur einen gibt. 
	 */
	aktionKurseVerteilenNurFachartenMitEinemKurs() : void {
		for (let iFachart : number = 0; iFachart < this.fachartArr.length; iFachart++){
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			let fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			if (fachart.gibKurseMax() !== 1) {
				continue;
			}
			let kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			for (let iKurse : number = 0; iKurse < kurse.length; iKurse++){
				let kurs : KursblockungDynKurs = kurse[iKurse];
				if (this.kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}
				let waehlbar : boolean = true;
				for (let nr of kurs.gibSchienenLage()) {
					if (this.schieneBelegt[nr]) {
						waehlbar = false;
					}
				}
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines gewichteten Matching Algorithmus. Kleinere
	 * Kurse werden in der Wahl bevorzugt.
	 */
	aktionKurseVerteilenMitBipartiteMatchingGewichtetem() : void {
		let INFINITY : number = 1000000;
		let data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++){
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				data[r][c] = INFINITY;
			}
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				if (!this.schieneBelegt[c]) {
					let kurs : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchiene(c, this.kursGesperrt);
					if (kurs !== null) {
						data[r][c] = kurs.gibGewichtetesMatchingBewertung();
					}
				}
			}
		}
		let r2c : Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let r : number = 0; r < this.fachartArr.length; r++){
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			let c : number = r2c[r];
			if (c < 0) {
				continue;
			}
			if (data[r][c] === INFINITY) {
				continue;
			}
			let kursGefunden : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchiene(c, this.kursGesperrt);
			if (kursGefunden !== null) {
				this.aktionKursHinzufuegen(r, kursGefunden);
			} else {
				console.log(JSON.stringify("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!"));
			}
		}
	}

	/**
	 *Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines Bipartiten-Matching-Algorithmus. 
	 */
	aktionKurseVerteilenMitBipartiteMatching() : void {
		let data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++){
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				data[r][c] = 0;
			}
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				if (!this.schieneBelegt[c]) {
					let kurs : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchiene(c, this.kursGesperrt);
					if (kurs !== null) {
						data[r][c] = 1;
					}
				}
			}
		}
		let r2c : Array<number> = this.matrix.gibMaximalesBipartitesMatching(true);
		for (let r : number = 0; r < this.fachartArr.length; r++){
			if ((this.fachartZuKurs[r] !== null) || this.fachartArr[r].gibHatMultikurs()) {
				continue;
			}
			let c : number = r2c[r];
			if (c === -1) {
				continue;
			}
			let kursGefunden : KursblockungDynKurs | null = this.fachartArr[r].gibKleinstenKursInSchiene(c, this.kursGesperrt);
			if (kursGefunden !== null) {
				this.aktionKursHinzufuegen(r, kursGefunden);
			} else {
				console.log(JSON.stringify("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!"));
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
	aktionKurseVerteilenNachDeinemWunsch() : boolean {
		let VAL_UNGUELTIG : number = 1000000;
		let VAL_KURS_GEWAEHLT : number = 0;
		let VAL_KURS_MUSS_WANDERN : number = 1;
		let data : Array<Array<number>> = this.matrix.getMatrix();
		for (let r : number = 0; r < this.fachartArr.length; r++){
			let fachart : KursblockungDynFachart | null = this.fachartArr[r];
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				data[r][c] = VAL_UNGUELTIG;
			}
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs()) {
				continue;
			}
			for (let c : number = 0; c < this.schieneBelegt.length; c++){
				if (!this.schieneBelegt[c]) {
					data[r][c] = fachart.gibHatKursInSchiene(c, this.kursGesperrt) ? VAL_KURS_GEWAEHLT : fachart.gibHatKursMitFreierSchiene(c, this.kursGesperrt) ? VAL_KURS_MUSS_WANDERN : VAL_UNGUELTIG;
				}
			}
		}
		let r2c : Array<number> = this.matrix.gibMinimalesBipartitesMatchingGewichtet(true);
		let kurslage_veraendert : boolean = false;
		for (let r : number = 0; r < this.fachartArr.length; r++){
			let fachart : KursblockungDynFachart | null = this.fachartArr[r];
			if ((this.fachartZuKurs[r] !== null) || fachart.gibHatMultikurs()) {
				continue;
			}
			let c : number = r2c[r];
			if (c < 0) {
				continue;
			}
			if (data[r][c] === VAL_UNGUELTIG) {
				continue;
			}
			if (data[r][c] === VAL_KURS_GEWAEHLT) {
				continue;
			}
			fachart.aktionZufaelligerKursWandertNachSchiene(c);
			kurslage_veraendert = true;
		}
		return kurslage_veraendert;
	}

	/**
	 * Geht die Facharten durch (Facharten mit einer kleineren Kursanzahl zuerst) und geht dann pro Fachart alle Kurse
	 * durch (Kurse mit kleinerer Schüleranzahl zuerst). Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es
	 * geht weiter mit der nächsten Fachart. Ein Kurs ist wählbar, wenn nicht bereits ein Kurs zugeordnet wurde und die
	 * Schienen in den der Kurs sind frei sind.<br>
	 */
	aktionKurseVerteilenZufaellig() : void {
		let perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.fachartArr.length);
		for (let pFachart : number = 0; pFachart < this.fachartArr.length; pFachart++){
			let iFachart : number = perm[pFachart];
			if (this.fachartZuKurs[iFachart] !== null) {
				continue;
			}
			let fachart : KursblockungDynFachart = this.fachartArr[iFachart];
			let kurse : Array<KursblockungDynKurs> = fachart.gibKurse();
			for (let iKurs : number = 0; iKurs < kurse.length; iKurs++){
				let kurs : KursblockungDynKurs = kurse[iKurs];
				if (this.kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}
				let waehlbar : boolean = true;
				for (let nr of kurs.gibSchienenLage()) {
					if (this.schieneBelegt[nr]) {
						waehlbar = false;
					}
				}
				if (waehlbar) {
					this.aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	private aktionKursHinzufuegen(fachartIndex : number, kurs : KursblockungDynKurs) : void {
		kurs.aktionSchuelerHinzufügen();
		this.statistik.aktionNichtwahlenVeraendern(-1);
		this.nichtwahlen--;
		for (let nr of kurs.gibSchienenLage()) {
			if (this.schieneBelegt[nr]) {
				console.log(JSON.stringify("FEHLER: Schienen-Doppelbelegung! " + this.representation!));
			}
			this.schieneBelegt[nr] = true;
		}
		this.fachartZuKurs[fachartIndex] = kurs;
	}

	private aktionKursEntfernen(fachartIndex : number, kurs : KursblockungDynKurs) : void {
		kurs.aktionSchuelerEntfernen();
		this.statistik.aktionNichtwahlenVeraendern(+1);
		this.nichtwahlen++;
		for (let nr of kurs.gibSchienenLage()) {
			if (!this.schieneBelegt[nr]) {
				console.log(JSON.stringify("FEHLER: Kurs ist gar nicht in Schiene ! " + this.representation!));
			}
			this.schieneBelegt[nr] = false;
		}
		this.fachartZuKurs[fachartIndex] = null;
	}

	/**
	 * Liefert TRUE, wenn dieser Schüler dem übergebenen Kurs zugeordnet wurde.
	 * 
	 * @param  kurs Der Kurs in dem der Schüler potentiell ist.
	 * 
	 * @return      TRUE, wenn dieser Schüler dem übergebenen Kurs zugeordnet wurde.
	 */
	gibIstInKurs(kurs : KursblockungDynKurs | null) : boolean {
		for (let zugeordneterKurs of this.fachartZuKurs) 
			if (zugeordneterKurs as unknown === kurs as unknown) 
				return true;
		return false;
	}

	/**
	 * Ausgabe der aktuellen Kurslage zum debuggen.
	 */
	public debugKurswahlen() : void {
		console.log();
		console.log(JSON.stringify(this.representation));
		let setSchienenLage : HashSet<number | null> | null = new HashSet();
		for (let i : number = 0; i < this.fachartZuKurs.length; i++){
			let kurs : KursblockungDynKurs | null = this.fachartZuKurs[i];
			if (kurs === null) 
				continue;
			console.log(JSON.stringify("    " + kurs.toString()! + "    " + Arrays.toString(kurs.gibSchienenLage())!));
			for (let schiene of kurs.gibSchienenLage()) {
				if (setSchienenLage.add(schiene) === false) {
					console.log(JSON.stringify("Kollision"));
					return;
				}
			}
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynSchueler'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler(obj : unknown) : KursblockungDynSchueler {
	return obj as KursblockungDynSchueler;
}
