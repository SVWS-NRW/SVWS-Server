import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { Arrays } from '../../java/util/Arrays';
import { System } from '../../java/lang/System';

export class KursblockungDynStatistik extends JavaObject {

	/**
	 *  In der Matrix ist zu jedem Kursart-Paar eine Bewertung die angibt, wie gut es wäre wenn zwei Kurses dieser
	 *  Fachart in der selben Schiene landen. Je kleiner der Wert, desto besser. Erhöht man den Wert der Haupt-Diagonale
	 *  in der Matrix, so werden Kurse der gleichen Fachart eher selten in eine Schiene getan.
	 */
	private matrixFachartPaar : Array<Array<number>> = [...Array(0)].map(e => Array(0).fill(0));

	/**
	 * Regel: Kurs verbiete mit Kurs.
	 */
	private regelVerletzungKursMitKurs : Array<Array<number>> = [...Array(0)].map(e => Array(0).fill(0));

	/**
	 * Die aktuelle Bewertung aller Regelverletzungen.
	 */
	private bewertungRegelverletzungen : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Schüler-Ebene.
	 */
	private bewertungRegelverletzungenSaveS : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Kurs-Ebene.
	 */
	private bewertungRegelverletzungenSaveK : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Globaler-Ebene.
	 */
	private bewertungRegelverletzungenSaveG : number = 0;

	/**
	 * Die aktuelle Bewertung der Fachart-Paare aller Schienen.
	 */
	private bewertungFachartPaar : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungFachartPaar} auf Kurs-Ebene.
	 */
	private bewertungFachartPaarSaveK : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungFachartPaar} auf Globaler-Ebene.
	 */
	private bewertungFachartPaarSaveG : number = 0;

	/**
	 * Die aktuelle Bewertung der Nichtwahlen aller Schüler.
	 */
	private bewertungNichtwahlen : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Schüler-Ebene.
	 */
	private bewertungNichtwahlenSaveS : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Kurs-Ebene.
	 */
	private bewertungNichtwahlenSaveK : number = 0;

	/**
	 * Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Globaler-Ebene.
	 */
	private bewertungNichtwahlenSaveG : number = 0;

	/**
	 * Das Array aller Kursdifferenzen.
	 */
	private bewertungKursdifferenzen : Array<number> = Array(0).fill(0);

	/**
	 * Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Schüler-Ebene.
	 */
	private bewertungKursdifferenzenSaveS : Array<number> = Array(0).fill(0);

	/**
	 * Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Kurs-Ebene.
	 */
	private bewertungKursdifferenzenSaveK : Array<number> = Array(0).fill(0);

	/**
	 * Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Globaler-Ebene.
	 */
	private bewertungKursdifferenzenSaveG : Array<number> = Array(0).fill(0);

	/**
	 * Verweist im Array {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf den höchsten Index mit einem
	 *  Wert größer als 0. Das ist die größte Kursdifferenz. <br>
	 *  Ausnahme: Falls das Array nur aus Nullen besteht, dann ist der Wert ebenfalls Null.
	 */
	private bewertungKursdifferenzenMaxIndex : number = 0;


	/**
	 * Initialisiert alle Attribute mit Dummy-Werten.
	 */
	constructor() {
		super();
		this.clear();
	}

	/**
	 * Initialisiert alle Attribute mit Dummy-Werten.
	 * Setzt alle Werte auf 0 und initialisiert alle Arrays auf die Länge 0.
	 */
	clear() : void {
		this.matrixFachartPaar = [...Array(0)].map(e => Array(0).fill(0));
		this.regelVerletzungKursMitKurs = [...Array(0)].map(e => Array(0).fill(0));
		this.bewertungRegelverletzungen = 0;
		this.bewertungRegelverletzungenSaveS = 0;
		this.bewertungRegelverletzungenSaveK = 0;
		this.bewertungRegelverletzungenSaveG = 0;
		this.bewertungFachartPaar = 0;
		this.bewertungFachartPaarSaveK = 0;
		this.bewertungFachartPaarSaveG = 0;
		this.bewertungNichtwahlen = 0;
		this.bewertungNichtwahlenSaveS = 0;
		this.bewertungNichtwahlenSaveK = 0;
		this.bewertungNichtwahlenSaveG = 0;
		this.bewertungKursdifferenzen = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveS = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveK = Array(0).fill(0);
		this.bewertungKursdifferenzenSaveG = Array(0).fill(0);
		this.bewertungKursdifferenzenMaxIndex = 0;
	}

	/**
	 * Initialisiert dieses Objekt mit den Anfangswerten.
	 *
	 * @param pMatrixFachartPaar Das 2D-Array beinhaltet pro Fachart-Paar eine Bewertung.
	 * @param pMaxSchueler       Die maximale Anzahl an Schülern.
	 * @param pMaxFacharten      Die maximale Anzahl an Facharten.
	 * @param pMaxKurse          Die maximale Anzahl an Kursen.
	 */
	aktionInitialisiere(pMatrixFachartPaar : Array<Array<number>>, pMaxSchueler : number, pMaxFacharten : number, pMaxKurse : number) : void {
		this.matrixFachartPaar = pMatrixFachartPaar;
		this.regelVerletzungKursMitKurs = [...Array(pMaxKurse)].map(e => Array(pMaxKurse).fill(0));
		this.bewertungRegelverletzungen = 0;
		this.bewertungRegelverletzungenSaveS = 0;
		this.bewertungRegelverletzungenSaveK = 0;
		this.bewertungRegelverletzungenSaveG = 0;
		this.bewertungKursdifferenzenMaxIndex = 0;
		this.bewertungKursdifferenzen = Array(pMaxSchueler + 1).fill(0);
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
	 * Ausgabe von Debug-Informationen. Nur für Testzwecke.
	 *
	 * @param pPrefix Ein String-Prefix vor der Ausgabe.
	 */
	debug(pPrefix : string) : void {
		console.log(JSON.stringify(pPrefix! + ", RV = " + this.bewertungRegelverletzungen + ", NW = " + this.bewertungNichtwahlen + ", FW = " + this.bewertungFachartPaar + ", KDs = " + this.bewertungKursdifferenzenMaxIndex + " = " + Arrays.toString(this.bewertungKursdifferenzen)!));
	}

	/**
	 * Liefert die aktuelle Fachart-Paar-Bewertung.
	 *
	 * @return Die aktuelle Fachart-Paar-Bewertung.
	 */
	gibBewertungFachartPaar() : number {
		return this.bewertungFachartPaar;
	}

	/**
	 * Liefert die aktuelle Anzahl an Nichtwahlen.
	 * Das ist die Summe aller Kurs, die bei Schülern nicht zugeordnet wurden.
	 *
	 * @return Die aktuelle Anzahl an Nichtwahlen.
	 */
	gibBewertungNichtwahlen() : number {
		return this.bewertungNichtwahlen;
	}

	/**
	 * Liefert die aktuell größte Kursdifferenz (über alle Facharten).
	 *
	 * @return Die aktuell größte Kursdifferenz (über alle Facharten).
	 */
	gibBewertungKursdifferenz() : number {
		return this.bewertungKursdifferenzenMaxIndex;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibBewertungZustandS_NW_KD() : number {
		if (this.bewertungRegelverletzungen > this.bewertungRegelverletzungenSaveS)
			return -1;
		if (this.bewertungRegelverletzungen < this.bewertungRegelverletzungenSaveS)
			return +1;
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveS)
			return -1;
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveS)
			return +1;
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveS[i])
				return -1;
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveS[i])
				return +1;
		}
		return 0;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandK_NW_KD_FW() : number {
		if (this.bewertungRegelverletzungen > this.bewertungRegelverletzungenSaveK)
			return -1;
		if (this.bewertungRegelverletzungen < this.bewertungRegelverletzungenSaveK)
			return +1;
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveK)
			return -1;
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveK)
			return +1;
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveK[i])
				return -1;
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveK[i])
				return +1;
		}
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveK)
			return -1;
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveK)
			return +1;
		return 0;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandK_FW_NW_KD() : number {
		if (this.bewertungRegelverletzungen > this.bewertungRegelverletzungenSaveK)
			return -1;
		if (this.bewertungRegelverletzungen < this.bewertungRegelverletzungenSaveK)
			return +1;
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveK)
			return -1;
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveK)
			return +1;
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveK)
			return -1;
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveK)
			return +1;
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveK[i])
				return -1;
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveK[i])
				return +1;
		}
		return 0;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des
	 * Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des Zustandes-G sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	gibCompareZustandG_NW_KD_FW() : number {
		if (this.bewertungRegelverletzungen > this.bewertungRegelverletzungenSaveG)
			return -1;
		if (this.bewertungRegelverletzungen < this.bewertungRegelverletzungenSaveG)
			return +1;
		if (this.bewertungNichtwahlen > this.bewertungNichtwahlenSaveG)
			return -1;
		if (this.bewertungNichtwahlen < this.bewertungNichtwahlenSaveG)
			return +1;
		for (let i : number = this.bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (this.bewertungKursdifferenzen[i] > this.bewertungKursdifferenzenSaveG[i])
				return -1;
			if (this.bewertungKursdifferenzen[i] < this.bewertungKursdifferenzenSaveG[i])
				return +1;
		}
		if (this.bewertungFachartPaar > this.bewertungFachartPaarSaveG)
			return -1;
		if (this.bewertungFachartPaar < this.bewertungFachartPaarSaveG)
			return +1;
		return 0;
	}

	/**
	 * Liefert das Array bzw. das Histogramm der Kursdifferenzen.
	 *
	 * @return das Array bzw. das Histogramm der Kursdifferenzen.
	 */
	gibArrayDerKursdifferenzen() : Array<number> {
		return this.bewertungKursdifferenzen;
	}

	/**
	 * Informiert die Statistik, dass ein Kurs-Paar hinzuzufügen ist.
	 *
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares.
	 */
	aktionKurspaarInSchieneHinzufuegen(pKurs1 : KursblockungDynKurs, pKurs2 : KursblockungDynKurs) : void {
		const faNr1 : number = pKurs1.gibFachart().gibNr();
		const faNr2 : number = pKurs2.gibFachart().gibNr();
		const kuNr1 : number = pKurs1.gibInternalID();
		const kuNr2 : number = pKurs2.gibInternalID();
		this.bewertungFachartPaar += this.matrixFachartPaar[faNr1][faNr2];
		this.bewertungRegelverletzungen += this.regelVerletzungKursMitKurs[kuNr1][kuNr2];
	}

	/**
	 * Informiert die Statistik, dass ein Kurs-Paar zu entfernen ist.
	 *
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares.
	 */
	aktionKurspaarInSchieneEntfernen(pKurs1 : KursblockungDynKurs, pKurs2 : KursblockungDynKurs) : void {
		const faNr1 : number = pKurs1.gibFachart().gibNr();
		const faNr2 : number = pKurs2.gibFachart().gibNr();
		const kuNr1 : number = pKurs1.gibInternalID();
		const kuNr2 : number = pKurs2.gibInternalID();
		this.bewertungFachartPaar -= this.matrixFachartPaar[faNr1][faNr2];
		this.bewertungRegelverletzungen -= this.regelVerletzungKursMitKurs[kuNr1][kuNr2];
	}

	/**
	 * Informiert die Statistik über eine Veränderung der Nichtwahlen.
	 *
	 * @param pVeraenderung Die Veränderungen der Nichtwahlen (negative Werte sind möglich).
	 */
	aktionNichtwahlenVeraendern(pVeraenderung : number) : void {
		this.bewertungNichtwahlen += pVeraenderung;
	}

	/**
	 * Fügt eine Kursdifferenz {@code pIndex} dem Histogramm {@link KursblockungDynStatistik#bewertungKursdifferenzen}
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
	 * Entfernt eine Kursdifferenz {@code pIndex} aus dem Histogramm
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
	 * Speichert die aktuellen Werte (im Zustand S).
	 */
	aktionBewertungSpeichernS() : void {
		this.bewertungRegelverletzungenSaveS = this.bewertungRegelverletzungen;
		this.bewertungNichtwahlenSaveS = this.bewertungNichtwahlen;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveS, 0, this.bewertungKursdifferenzen.length);
	}

	/**
	 * Speichert die aktuellen Werte (im Zustand K).
	 */
	aktionBewertungSpeichernK() : void {
		this.bewertungRegelverletzungenSaveK = this.bewertungRegelverletzungen;
		this.bewertungNichtwahlenSaveK = this.bewertungNichtwahlen;
		this.bewertungFachartPaarSaveK = this.bewertungFachartPaar;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveK, 0, this.bewertungKursdifferenzen.length);
	}

	/**
	 * Speichert den aktuellen Blockungsdaten (im Zustand G).
	 */
	aktionBewertungSpeichernG() : void {
		this.bewertungRegelverletzungenSaveG = this.bewertungRegelverletzungen;
		this.bewertungNichtwahlenSaveG = this.bewertungNichtwahlen;
		this.bewertungFachartPaarSaveG = this.bewertungFachartPaar;
		System.arraycopy(this.bewertungKursdifferenzen, 0, this.bewertungKursdifferenzenSaveG, 0, this.bewertungKursdifferenzen.length);
	}

	/**
	 * Fügt die Regel {@link GostKursblockungRegelTyp#KURS_VERBIETEN_MIT_KURS} zur Bewertung hinzu.
	 *
	 * @param kurs1  Der 1. Kurs der Regel.
	 * @param kurs2  Der 2. Kurs der Regel.
	 */
	regelHinzufuegenKursVerbieteMitKurs(kurs1 : KursblockungDynKurs, kurs2 : KursblockungDynKurs) : void {
		const nr1 : number = kurs1.gibInternalID();
		const nr2 : number = kurs2.gibInternalID();
		this.regelVerletzungKursMitKurs[nr1][nr2] += 1;
		this.regelVerletzungKursMitKurs[nr2][nr1] += 1;
	}

	/**
	 * Fügt die Regel {@link GostKursblockungRegelTyp#KURS_ZUSAMMEN_MIT_KURS} zur Bewertung hinzu.
	 *
	 * Erhöht direkt den Malus 'bewertungRegelverletzungen', da die Kurse anfangs noch nicht zusammen sind.
	 * Geht ein Kurs über 2 Schienen, der andere über 3, dann können diese maximal 2 Mal zusammen sein.
	 *
	 * @param kurs1  Der 1. Kurs der Regel.
	 * @param kurs2  Der 2. Kurs der Regel.
	 */
	regelHinzufuegenKursZusammenMitKurs(kurs1 : KursblockungDynKurs, kurs2 : KursblockungDynKurs) : void {
		const nr1 : number = kurs1.gibInternalID();
		const nr2 : number = kurs2.gibInternalID();
		this.regelVerletzungKursMitKurs[nr1][nr2] -= 1;
		this.regelVerletzungKursMitKurs[nr2][nr1] -= 1;
		this.bewertungRegelverletzungen += Math.max(kurs1.gibSchienenAnzahl(), kurs2.gibSchienenAnzahl());
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynStatistik'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik(obj : unknown) : KursblockungDynStatistik {
	return obj as KursblockungDynStatistik;
}
