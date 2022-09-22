import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../java/util/Arrays';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungDynStatistik extends JavaObject {

	private matrixFachartPaar : Array<Array<number>> = [...Array(0)].map(e => Array(0).fill(0));

	private bewertungFachartPaar : number = 0;

	private bewertungFachartPaarSaveS : number = 0;

	private bewertungFachartPaarSaveK : number = 0;

	private bewertungFachartPaarSaveG : number = 0;

	private bewertungNichtwahlen : number = 0;

	private bewertungNichtwahlenSaveS : number = 0;

	private bewertungNichtwahlenSaveK : number = 0;

	private bewertungNichtwahlenSaveG : number = 0;

	private bewertungKursdifferenzen : Array<number> = Array(0).fill(0);

	private bewertungKursdifferenzenSaveS : Array<number> = Array(0).fill(0);

	private bewertungKursdifferenzenSaveK : Array<number> = Array(0).fill(0);

	private bewertungKursdifferenzenSaveG : Array<number> = Array(0).fill(0);

	private bewertungKursdifferenzenMaxIndex : number = 0;


	/**
	 *Initialisiert alle Attribute mit Dummy-Werten. 
	 */
	constructor() {
		super();
		this.clear();
	}

	/**
	 *Initialisiert alle Attribute mit Dummy-Werten. Setzt alle Werte auf 0 und initialisiert alle Arrays auf die
	 * Länge 0. 
	 */
	clear() : void {
		this.bewertungFachartPaar = 0;
		this.bewertungFachartPaarSaveS = 0;
		this.bewertungFachartPaarSaveK = 0;
		this.bewertungFachartPaarSaveG = 0;
		this.bewertungNichtwahlen = 0;
		this.bewertungNichtwahlenSaveS = 0;
		this.bewertungNichtwahlenSaveK = 0;
		this.bewertungNichtwahlenSaveG = 0;
		this.matrixFachartPaar = [...Array(0)].map(e => Array(0).fill(0));
		this.bewertungKursdifferenzen = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveS = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveK = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveG = Array(0).fill(0);
		this.bewertungKursdifferenzenMaxIndex = 0;
	}

	/**
	 *Ausgabe von Debug-Informationen. Nur für Testzwecke.
	 * 
	 * @param pPrefix Ein String-Prefix vor der Ausgabe. 
	 */
	debug(pPrefix : String) : void {
		console.log(JSON.stringify(pPrefix.valueOf() + ", NW = " + this.bewertungNichtwahlen + ", FW = " + this.bewertungFachartPaar + ", KDs = " + this.bewertungKursdifferenzenMaxIndex + " = " + Arrays.toString(this.bewertungKursdifferenzen).valueOf()));
	}

	/**
	 *Liefert die aktuelle Fachart-Paar-Bewertung.
	 * 
	 * @return Die aktuelle Fachart-Paar-Bewertung. 
	 */
	gibBewertungFachartPaar() : number {
		return this.bewertungFachartPaar;
	}

	/**
	 *Liefert die aktuelle Anzahl an Nichtwahlen. Das ist die Summe aller Kurs, die bei Schülern nicht zugeordnet
	 * wurden.
	 * 
	 * @return Die aktuelle Anzahl an Nichtwahlen. 
	 */
	gibBewertungNichtwahlen() : number {
		return this.bewertungNichtwahlen;
	}

	/**
	 *Liefert die aktuell größte Kursdifferenz (über alle Facharten).
	 * 
	 * @return Die aktuell größte Kursdifferenz (über alle Facharten). 
	 */
	gibBewertungKursdifferenz() : number {
		return this.bewertungKursdifferenzenMaxIndex;
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibBewertungZustandS_NW_KD() : number {
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveS) {
			return -1;
		}
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveS) {
			return +1;
		}
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--){
			if (this.bewertungKursdifferenzen[i] === this.bewertungKursdifferenzenSaveS[i]) {
				continue;
			}
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveS[i]) {
				return -1;
			}
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveS[i]) {
				return +1;
			}
		}
		return 0;
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibCompareZustandK_NW_KD_FW() : number {
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveK) {
			return -1;
		}
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveK) {
			return +1;
		}
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--){
			if (this.bewertungKursdifferenzen[i] === this.bewertungKursdifferenzenSaveK[i]) {
				continue;
			}
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveK[i]) {
				return -1;
			}
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveK[i]) {
				return +1;
			}
		}
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveK) {
			return -1;
		}
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveK) {
			return +1;
		}
		return 0;
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des
	 * Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des Zustandes-G sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibCompareZustandG_NW_KD_FW() : number {
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveG) {
			return -1;
		}
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveG) {
			return +1;
		}
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--){
			if (this.bewertungKursdifferenzen[i] === this.bewertungKursdifferenzenSaveG[i]) {
				continue;
			}
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveG[i]) {
				return -1;
			}
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveG[i]) {
				return +1;
			}
		}
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveG) {
			return -1;
		}
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveG) {
			return +1;
		}
		return 0;
	}

	/**
	 *Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	gibCompareZustandK_FW_NW_KD() : number {
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveK) {
			return -1;
		}
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveK) {
			return +1;
		}
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveK) {
			return -1;
		}
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveK) {
			return +1;
		}
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--){
			if (this.bewertungKursdifferenzen[i] === this.bewertungKursdifferenzenSaveK[i]) {
				continue;
			}
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveK[i]) {
				return -1;
			}
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveK[i]) {
				return +1;
			}
		}
		return 0;
	}

	/**
	 *Liefert den Wert {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes S sich verschlechtert
	 * hat.
	 * 
	 * @return {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes S sich verschlechtert hat. 
	 */
	gibBewertungFachartPaarSchlechter() : boolean {
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveS) {
			return true;
		}
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveS) {
			return false;
		}
		return false;
	}

	/**
	 *Liefert den Wert {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes K sich verschlechtert
	 * hat.
	 * 
	 * @return {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes K sich verschlechtert hat. 
	 */
	gibBewertungFachartPaarSchlechterK() : boolean {
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveK) {
			return true;
		}
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveK) {
			return false;
		}
		return false;
	}

	/**
	 *Liefert das Array bzw. das Histogramm der Kursdifferenzen.
	 * 
	 * @return das Array bzw. das Histogramm der Kursdifferenzen. 
	 */
	gibArrayDerKursdifferenzen() : Array<number> {
		return this.bewertungKursdifferenzen;
	}

	/**
	 *Initialisiert dieses Objekt mit den Anfangswerten.
	 * 
	 * @param pMatrixFachartPaar Das 2D-Array beinhaltet pro Fachart-Paar eine Bewertung.
	 * @param pMaxSchueler       Die maximale Anzahl an Schülern.
	 * @param pMaxFacharten      Die maximale Anzahl an Facharten. 
	 */
	aktionInitialisiere(pMatrixFachartPaar : Array<Array<number>>, pMaxSchueler : number, pMaxFacharten : number) : void {
		this.matrixFachartPaar = pMatrixFachartPaar;
		this.bewertungKursdifferenzen = Array(pMaxSchueler + 1).fill(0);
		this.bewertungKursdifferenzenMaxIndex = 0;
		this.bewertungKursdifferenzenSaveS = Array(pMaxSchueler + 1).fill(0);
		this.bewertungKursdifferenzenSaveK = Array(pMaxSchueler + 1).fill(0);
		this.bewertungKursdifferenzenSaveG = Array(pMaxSchueler + 1).fill(0);
		this.bewertungKursdifferenzen[0] = pMaxFacharten;
		this.bewertungKursdifferenzenSaveS[0] = pMaxFacharten;
		this.bewertungKursdifferenzenSaveK[0] = pMaxFacharten;
		this.bewertungKursdifferenzenSaveG[0] = pMaxFacharten;
		this.aktionBewertungSpeichernS();
		this.aktionBewertungSpeichernK();
		this.aktionBewertungSpeichernG();
	}

	/**
	 *Informiert die Statistik, dass ein Kurs-Paar hinzuzufügen ist.
	 * 
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares. 
	 */
	aktionKurspaarInSchieneHinzufuegen(pKurs1 : KursblockungDynKurs, pKurs2 : KursblockungDynKurs) : void {
		let nr1 : number = pKurs1.gibFachart().gibNr();
		let nr2 : number = pKurs2.gibFachart().gibNr();
		this.bewertungFachartPaar += this.matrixFachartPaar[nr1][nr2];
	}

	/**
	 *Informiert die Statistik, dass ein Kurs-Paar zu entfernen ist.
	 * 
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares. 
	 */
	aktionKurspaarInSchieneEntfernen(pKurs1 : KursblockungDynKurs, pKurs2 : KursblockungDynKurs) : void {
		let nr1 : number = pKurs1.gibFachart().gibNr();
		let nr2 : number = pKurs2.gibFachart().gibNr();
		this.bewertungFachartPaar -= this.matrixFachartPaar[nr1][nr2];
	}

	/**
	 *Informiert die Statistik über eine Veränderung der Nichtwahlen.
	 * 
	 * @param pVeraenderung Die Veränderungen der Nichtwahlen (negative Werte sind möglich). 
	 */
	aktionNichtwahlenVeraendern(pVeraenderung : number) : void {
		this.bewertungNichtwahlen += pVeraenderung;
	}

	/**
	 *Entfernt eine Kursdifferenz {@code pIndex} aus dem Histogramm
	 * {@link KursblockungDynStatistik#bewertungKursdifferenzen} aller Kursdifferenzen. Der Index des größten
	 * Nicht-Null-Wertes {@link KursblockungDynStatistik#bewertungKursdifferenzenMaxIndex} wird dabei möglicherweise
	 * kleiner. <br>
	 * {@code Beispiel vorher: 5, 0, 6, 0, 0, 1*, 0, 0, 0}<br>
	 * {@code Beispiel danach: 5, 0, 6*, 0, 0, 0, 0, 0, 0}<br>
	 * 
	 * @param pIndex Die Kursdifferenz von der es eine weniger geben soll. 
	 */
	aktionKursdifferenzEntfernen(pIndex : number) : void {
		this.bewertungKursdifferenzen[pIndex]--;
		if (pIndex === this.bewertungKursdifferenzenMaxIndex) 
			while ((this.bewertungKursdifferenzen[this.bewertungKursdifferenzenMaxIndex] === 0) && (this.bewertungKursdifferenzenMaxIndex > 0)) 
				this.bewertungKursdifferenzenMaxIndex--;
	}

	/**
	 *Fügt eine Kursdifferenz {@code pIndex} dem Histogramm {@link KursblockungDynStatistik#bewertungKursdifferenzen}
	 * aller Kursdifferenzen hinzu. Der Index des größten Nicht-Null-Wertes
	 * {@link KursblockungDynStatistik#bewertungKursdifferenzenMaxIndex} wird dabei möglicherweise größer. <br>
	 * {@code Beispiel vorher: 5, 0, 6*, 0, 0, 0, 0, 0, 0}<br>
	 * {@code Beispiel danach: 5, 0, 6, 0, 0, 1*, 0, 0, 0}<br>
	 * 
	 * @param pIndex Die Kursdifferenz von der es eine weniger geben soll. 
	 */
	aktionKursdifferenzHinzufuegen(pIndex : number) : void {
		this.bewertungKursdifferenzen[pIndex]++;
		if (pIndex > this.bewertungKursdifferenzenMaxIndex) 
			this.bewertungKursdifferenzenMaxIndex = pIndex;
	}

	/**
	 *Speichert die aktuellen Werte (im Zustand S). Die Methoden
	 * {@link KursblockungDynStatistik#gibBewertungZustandS_NW_KD()} und
	 * {@link KursblockungDynStatistik#gibBewertungFachartPaarSchlechter} bedienen sich dann der ehemaligen Werte um
	 * festzustellen, ob es eine Verschlechterung gab. 
	 */
	aktionBewertungSpeichernS() : void {
		this.bewertungNichtwahlenSaveS = this.bewertungNichtwahlen;
		this.bewertungFachartPaarSaveS = this.bewertungFachartPaar;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveS, 0, this.bewertungKursdifferenzen.length);
	}

	/**
	 *Speichert die aktuellen Werte (im Zustand K). Die Methoden
	 * {@link KursblockungDynStatistik#gibCompareZustandK_NW_KD_FW()} und
	 * {@link KursblockungDynStatistik#gibBewertungFachartPaarSchlechter} bedienen sich dann der ehemaligen Werte um
	 * festzustellen, ob es eine Verschlechterung gab. 
	 */
	aktionBewertungSpeichernK() : void {
		this.bewertungNichtwahlenSaveK = this.bewertungNichtwahlen;
		this.bewertungFachartPaarSaveK = this.bewertungFachartPaar;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveK, 0, this.bewertungKursdifferenzen.length);
	}

	/**
	 *Speichert den aktuellen Blockungsdaten (im Zustand G). 
	 */
	aktionBewertungSpeichernG() : void {
		this.bewertungNichtwahlenSaveG = this.bewertungNichtwahlen;
		this.bewertungFachartPaarSaveG = this.bewertungFachartPaar;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveG, 0, this.bewertungKursdifferenzen.length);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynStatistik'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik(obj : unknown) : KursblockungDynStatistik {
	return obj as KursblockungDynStatistik;
}
