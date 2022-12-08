package de.nrw.schule.svws.core.kursblockung;

import java.util.Arrays;

import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse beinhaltet Informationen zum Bewerten einer Blockung. Beispielsweise:<br>
 * - Die Anzahl an Nichtwahlen.<br>
 * - Ein Array der Kursdifferenzen.<br>
 * - Ein 2D Array der Facharten Paare (und die zugehörige Bewertung).<br>
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungDynStatistik {

	/** 
	 * In der Matrix ist zu jedem Kursart-Paar eine Bewertung die angibt, wie gut es wäre wenn zwei Kurses dieser
	 * Fachart in der selben Schiene landen. Je kleiner der Wert, desto besser. Erhöht man den Wert der Haupt-Diagonale
	 * in der Matrix, so werden Kurse der gleichen Fachart eher selten in eine Schiene getan. 
	 */
	private @NotNull int @NotNull [][] matrixFachartPaar = new int[0][0];

	/** Regel: Kurs verbiete mit Kurs. */
	private @NotNull int @NotNull [][] regelVerletzungKursMitKurs = new int[0][0];
	
	/** Die aktuelle Bewertung aller Regelverletzungen. */
	private int bewertungRegelverletzungen;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Schüler-Ebene. */
	private int bewertungRegelverletzungenSaveS;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Kurs-Ebene. */
	private int bewertungRegelverletzungenSaveK;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungRegelverletzungen} auf Globaler-Ebene. */
	private int bewertungRegelverletzungenSaveG;
	
	/** Die aktuelle Bewertung der Fachart-Paare aller Schienen. */
	private long bewertungFachartPaar;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungFachartPaar} auf Schüler-Ebene. */
	private long bewertungFachartPaarSaveS;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungFachartPaar} auf Kurs-Ebene. */
	private long bewertungFachartPaarSaveK;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungFachartPaar} auf Globaler-Ebene. */
	private long bewertungFachartPaarSaveG;

	/** Die aktuelle Bewertung der Nichtwahlen aller Schüler. */
	private int bewertungNichtwahlen;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Schüler-Ebene. */
	private int bewertungNichtwahlenSaveS;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Kurs-Ebene. */
	private int bewertungNichtwahlenSaveK;

	/** Zum Speichern des Wertes von {@link KursblockungDynStatistik#bewertungNichtwahlen} auf Globaler-Ebene. */
	private int bewertungNichtwahlenSaveG;

	/** Das Array aller Kursdifferenzen. */
	private @NotNull int[] bewertungKursdifferenzen = new int[0];

	/** Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Schüler-Ebene. */
	private @NotNull int[] bewertungKursdifferenzenSaveS = new int[0];

	/** Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Kurs-Ebene. */
	private @NotNull int[] bewertungKursdifferenzenSaveK = new int[0];

	/** Zum Speichern des Arrays von {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf Globaler-Ebene. */
	private @NotNull int[] bewertungKursdifferenzenSaveG = new int[0];

	/** Verweist im Array {@link KursblockungDynStatistik#bewertungKursdifferenzen} auf den höchsten Index mit einem
	 * Wert größer als 0. Das ist die größte Kursdifferenz. <br>
	 * Ausnahme: Falls das Array nur aus Nullen besteht, dann ist der Wert ebenfalls Null. */
	private int bewertungKursdifferenzenMaxIndex;

	/** 
	 * Initialisiert alle Attribute mit Dummy-Werten. 
	 */
	KursblockungDynStatistik() {
		clear();
	}

	/** 
	 * Initialisiert alle Attribute mit Dummy-Werten. 
	 * Setzt alle Werte auf 0 und initialisiert alle Arrays auf die Länge 0. 
	 */
	void clear() {
		matrixFachartPaar = new int[0][0];
		regelVerletzungKursMitKurs = new int[0][0];

		bewertungRegelverletzungen = 0;
		bewertungRegelverletzungenSaveS = 0;
		bewertungRegelverletzungenSaveK = 0;
		bewertungRegelverletzungenSaveG = 0;
		
		bewertungFachartPaar = 0;
		bewertungFachartPaarSaveS = 0;
		bewertungFachartPaarSaveK = 0;
		bewertungFachartPaarSaveG = 0;
		
		bewertungNichtwahlen = 0;
		bewertungNichtwahlenSaveS = 0;
		bewertungNichtwahlenSaveK = 0;
		bewertungNichtwahlenSaveG = 0;
		
		bewertungKursdifferenzen = new int[0];
		bewertungKursdifferenzenSaveS = new int[0];
		bewertungKursdifferenzenSaveK = new int[0];
		bewertungKursdifferenzenSaveG = new int[0];
		bewertungKursdifferenzenMaxIndex = 0;
	}

	/** 
	 * Initialisiert dieses Objekt mit den Anfangswerten.
	 * 
	 * @param pMatrixFachartPaar Das 2D-Array beinhaltet pro Fachart-Paar eine Bewertung.
	 * @param pMaxSchueler       Die maximale Anzahl an Schülern.
	 * @param pMaxFacharten      Die maximale Anzahl an Facharten. 
	 * @param pMaxKurse          Die maximale Anzahl an Kursen. 
	 */
	void aktionInitialisiere(@NotNull int @NotNull [][] pMatrixFachartPaar, int pMaxSchueler, int pMaxFacharten, int pMaxKurse) {
		matrixFachartPaar = pMatrixFachartPaar;
		regelVerletzungKursMitKurs = new int[pMaxKurse][pMaxKurse];
		
		bewertungRegelverletzungen = 0;
		bewertungRegelverletzungenSaveS = 0;
		bewertungRegelverletzungenSaveK = 0;
		bewertungRegelverletzungenSaveG = 0;
		
		bewertungKursdifferenzenMaxIndex = 0;
		bewertungKursdifferenzen = new int[pMaxSchueler + 1];
		bewertungKursdifferenzenSaveS = new int[pMaxSchueler + 1];
		bewertungKursdifferenzenSaveK = new int[pMaxSchueler + 1];
		bewertungKursdifferenzenSaveG = new int[pMaxSchueler + 1];
		
		bewertungKursdifferenzen[0] = pMaxFacharten;
		bewertungKursdifferenzenSaveS[0] = pMaxFacharten;
		bewertungKursdifferenzenSaveK[0] = pMaxFacharten;
		bewertungKursdifferenzenSaveG[0] = pMaxFacharten;
		
		// Es muss gespeichert werden, da sonst beim Laden vor einem Speichern ein ungültiger Zustand entsteht.
		aktionBewertungSpeichernS();
		aktionBewertungSpeichernK();
		aktionBewertungSpeichernG();
	}

	/** 
	 * Ausgabe von Debug-Informationen. Nur für Testzwecke.
	 * 
	 * @param pPrefix Ein String-Prefix vor der Ausgabe. 
	 */
	void debug(@NotNull String pPrefix) {
		System.out.println(pPrefix + ", NW = " + bewertungRegelverletzungen 
				                   + ", NW = " + bewertungNichtwahlen 
				                   + ", FW = " + bewertungFachartPaar 
				                   + ", KDs = "+ bewertungKursdifferenzenMaxIndex + " = " + Arrays.toString(bewertungKursdifferenzen));
	}

	/** 
	 * Liefert die aktuelle Fachart-Paar-Bewertung.
	 * 
	 * @return Die aktuelle Fachart-Paar-Bewertung. 
	 */
	long gibBewertungFachartPaar() {
		return bewertungFachartPaar;
	}

	/** 
	 * Liefert die aktuelle Anzahl an Nichtwahlen. 
	 * Das ist die Summe aller Kurs, die bei Schülern nicht zugeordnet wurden.
	 * 
	 * @return Die aktuelle Anzahl an Nichtwahlen. 
	 */
	int gibBewertungNichtwahlen() {
		return bewertungNichtwahlen;
	}

	/** 
	 * Liefert die aktuell größte Kursdifferenz (über alle Facharten).
	 * 
	 * @return Die aktuell größte Kursdifferenz (über alle Facharten). 
	 */
	int gibBewertungKursdifferenz() {
		return bewertungKursdifferenzenMaxIndex;
	}

	/** 
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	int gibBewertungZustandS_NW_KD() {
		// Regelverletzungen
		if (bewertungRegelverletzungen > bewertungRegelverletzungenSaveS) return -1;
		if (bewertungRegelverletzungen < bewertungRegelverletzungenSaveS) return +1;
		
		// Nichtwahlen
		if (bewertungNichtwahlen > bewertungNichtwahlenSaveS) return -1;
		if (bewertungNichtwahlen < bewertungNichtwahlenSaveS) return +1;

		// Kursdifferenzen
		for (int i = bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (bewertungKursdifferenzen[i] > bewertungKursdifferenzenSaveS[i]) return -1;
			if (bewertungKursdifferenzen[i] < bewertungKursdifferenzenSaveS[i]) return +1;
		}

		// Bewertungen identisch
		return 0;
	}

	/** 
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	int gibCompareZustandK_NW_KD_FW() {
		// Regelverletzungen
		if (bewertungRegelverletzungen > bewertungRegelverletzungenSaveK) return -1;
		if (bewertungRegelverletzungen < bewertungRegelverletzungenSaveK) return +1;
		
		// Nichtwahlen
		if (bewertungNichtwahlen > bewertungNichtwahlenSaveK) return -1;
		if (bewertungNichtwahlen < bewertungNichtwahlenSaveK) return +1;

		// Kursdifferenzen
		for (int i = bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (bewertungKursdifferenzen[i] > bewertungKursdifferenzenSaveK[i]) return -1;
			if (bewertungKursdifferenzen[i] < bewertungKursdifferenzenSaveK[i]) return +1;
		}

		// FachartPaar
		if (bewertungFachartPaar > bewertungFachartPaarSaveK) return -1;
		if (bewertungFachartPaar < bewertungFachartPaarSaveK) return +1;

		// Bewertungen identisch
		return 0;
	}

	/** 
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	int gibCompareZustandK_FW_NW_KD() {
		// Regelverletzungen
		if (bewertungRegelverletzungen > bewertungRegelverletzungenSaveK) return -1;
		if (bewertungRegelverletzungen < bewertungRegelverletzungenSaveK) return +1;

		// FachartPaar
		if (bewertungFachartPaar > bewertungFachartPaarSaveK) return -1;
		if (bewertungFachartPaar < bewertungFachartPaarSaveK) return +1;
	
		// Nichtwahlen
		if (bewertungNichtwahlen > bewertungNichtwahlenSaveK) return -1;
		if (bewertungNichtwahlen < bewertungNichtwahlenSaveK) return +1;
	
		// Kursdifferenzen
		for (int i = bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (bewertungKursdifferenzen[i] > bewertungKursdifferenzenSaveK[i]) return -1;
			if (bewertungKursdifferenzen[i] < bewertungKursdifferenzenSaveK[i]) return +1;
		}
	
		// Bewertungen identisch
		return 0;
	}

	/** 
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des
	 * Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen, FachartPaar) des Zustandes-G sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. 
	 */
	int gibCompareZustandG_NW_KD_FW() {
		// Regelverletzungen
		if (bewertungRegelverletzungen > bewertungRegelverletzungenSaveG) return -1;
		if (bewertungRegelverletzungen < bewertungRegelverletzungenSaveG) return +1;
		
		// Nichtwahlen
		if (bewertungNichtwahlen > bewertungNichtwahlenSaveG) return -1;
		if (bewertungNichtwahlen < bewertungNichtwahlenSaveG) return +1;

		// Kursdifferenzen
		for (int i = bewertungKursdifferenzen.length - 1; i >= 0; i--) {
			if (bewertungKursdifferenzen[i] > bewertungKursdifferenzenSaveG[i]) return -1;
			if (bewertungKursdifferenzen[i] < bewertungKursdifferenzenSaveG[i]) return +1;
		}

		// FachartPaar
		if (bewertungFachartPaar > bewertungFachartPaarSaveG) return -1;
		if (bewertungFachartPaar < bewertungFachartPaarSaveG) return +1;

		// Bewertungen identisch
		return 0;
	}

	/** 
	 * Liefert den Wert {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes S sich verschlechtert hat.
	 * 
	 * @return {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes S sich verschlechtert hat. 
	 */
	boolean gibBewertungFachartPaarSchlechter() {
		// FachartPaar schlechter?
		if (bewertungFachartPaar > bewertungFachartPaarSaveS) 
			return true;
		// FachartPaar besser?
		if (bewertungFachartPaar < bewertungFachartPaarSaveS) 
			return false;
		// Bewertungen identisch --> nicht schlechter
		return false;
	}

	/** 
	 * Liefert den Wert {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes K sich verschlechtert hat.
	 * 
	 * @return {@code true}, falls die Bewertung (bewertungFachartPaar) des Zustandes K sich verschlechtert hat. 
	 */
	boolean gibBewertungFachartPaarSchlechterK() {
		// FachartPaar schlechter?
		if (bewertungFachartPaar > bewertungFachartPaarSaveK) 
			return true;
		// FachartPaar besser?
		if (bewertungFachartPaar < bewertungFachartPaarSaveK) 
			return false;
		// Bewertungen identisch --> nicht schlechter
		return false;
	}

	/** 
	 * Liefert das Array bzw. das Histogramm der Kursdifferenzen.
	 * 
	 * @return das Array bzw. das Histogramm der Kursdifferenzen. 
	 */
	@NotNull int[] gibArrayDerKursdifferenzen() {
		return bewertungKursdifferenzen;
	}

	/** 
	 * Informiert die Statistik, dass ein Kurs-Paar hinzuzufügen ist.
	 * 
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares.
	 */
	void aktionKurspaarInSchieneHinzufuegen(@NotNull KursblockungDynKurs pKurs1, @NotNull KursblockungDynKurs pKurs2) {
		int nr1 = pKurs1.gibFachart().gibNr();
		int nr2 = pKurs2.gibFachart().gibNr();
		bewertungFachartPaar += matrixFachartPaar[nr1][nr2];
		bewertungRegelverletzungen += regelVerletzungKursMitKurs[nr1][nr2];
	}

	/** 
	 * Informiert die Statistik, dass ein Kurs-Paar zu entfernen ist.
	 * 
	 * @param pKurs1 Der 1. Kurs des Kurs-Paares.
	 * @param pKurs2 Der 2. Kurs des Kurs-Paares. 
	 */
	void aktionKurspaarInSchieneEntfernen(@NotNull KursblockungDynKurs pKurs1, @NotNull KursblockungDynKurs pKurs2) {
		int nr1 = pKurs1.gibFachart().gibNr();
		int nr2 = pKurs2.gibFachart().gibNr();
		bewertungFachartPaar -= matrixFachartPaar[nr1][nr2];
		bewertungRegelverletzungen -= regelVerletzungKursMitKurs[nr1][nr2];
	}

	/** 
	 * Informiert die Statistik über eine Veränderung der Nichtwahlen.
	 * 
	 * @param pVeraenderung Die Veränderungen der Nichtwahlen (negative Werte sind möglich).
	 */
	void aktionNichtwahlenVeraendern(int pVeraenderung) {
		bewertungNichtwahlen += pVeraenderung;
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
	void aktionKursdifferenzHinzufuegen(int pIndex) {
		bewertungKursdifferenzen[pIndex]++;
	
		if (pIndex > bewertungKursdifferenzenMaxIndex)
			bewertungKursdifferenzenMaxIndex = pIndex;
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
	void aktionKursdifferenzEntfernen(int pIndex) {
		bewertungKursdifferenzen[pIndex]--;

		if (pIndex == bewertungKursdifferenzenMaxIndex)
			while ((bewertungKursdifferenzen[bewertungKursdifferenzenMaxIndex] == 0) && (bewertungKursdifferenzenMaxIndex > 0))
				bewertungKursdifferenzenMaxIndex--;

	}

	/** 
	 * Speichert die aktuellen Werte (im Zustand S). <br>
	 * Die Methoden {@link KursblockungDynStatistik#gibBewertungZustandS_NW_KD()} und
	 * {@link KursblockungDynStatistik#gibBewertungFachartPaarSchlechter} bedienen sich dann der ehemaligen Werte um
	 * festzustellen, ob es eine Verschlechterung gab. 
	 */
	void aktionBewertungSpeichernS() {
		bewertungRegelverletzungenSaveS = bewertungRegelverletzungen;
		bewertungNichtwahlenSaveS = bewertungNichtwahlen;
		bewertungFachartPaarSaveS = bewertungFachartPaar;
		System.arraycopy(bewertungKursdifferenzen, 0, bewertungKursdifferenzenSaveS, 0, bewertungKursdifferenzen.length);
	}

	/** 
	 * Speichert die aktuellen Werte (im Zustand K). <br>
	 * Die Methoden {@link KursblockungDynStatistik#gibCompareZustandK_NW_KD_FW()} und
	 * {@link KursblockungDynStatistik#gibBewertungFachartPaarSchlechter} bedienen sich dann der ehemaligen Werte um
	 * festzustellen, ob es eine Verschlechterung gab. 
	 */
	void aktionBewertungSpeichernK() {
		bewertungRegelverletzungenSaveK = bewertungRegelverletzungen;
		bewertungNichtwahlenSaveK = bewertungNichtwahlen;
		bewertungFachartPaarSaveK = bewertungFachartPaar;
		System.arraycopy(bewertungKursdifferenzen, 0, bewertungKursdifferenzenSaveK, 0, bewertungKursdifferenzen.length);
	}

	/** 
	 * Speichert den aktuellen Blockungsdaten (im Zustand G).
	 */
	void aktionBewertungSpeichernG() {
		bewertungRegelverletzungenSaveG = bewertungRegelverletzungen;
		bewertungNichtwahlenSaveG = bewertungNichtwahlen;
		bewertungFachartPaarSaveG = bewertungFachartPaar;
		System.arraycopy(bewertungKursdifferenzen, 0, bewertungKursdifferenzenSaveG, 0, bewertungKursdifferenzen.length);
	}

	/**
	 * Fügt die Regel {@link GostKursblockungRegelTyp#KURS_VERBIETEN_MIT_KURS} zur Bewertung hinzu.
	 * 
	 * @param kurs1  Der 1. Kurs der Regel.
	 * @param kurs2  Der 2. Kurs der Regel.
	 */
	void regelHinzufuegenKursVerbieteMitKurs(@NotNull KursblockungDynKurs kurs1, @NotNull KursblockungDynKurs kurs2) {
		int nr1 = kurs1.gibInternalID();
		int nr2 = kurs2.gibInternalID();
		regelVerletzungKursMitKurs[nr1][nr2] += 1;
		regelVerletzungKursMitKurs[nr2][nr1] += 1;
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
	void regelHinzufuegenKursZusammenMitKurs(@NotNull KursblockungDynKurs kurs1, @NotNull KursblockungDynKurs kurs2) {
		int nr1 = kurs1.gibInternalID();
		int nr2 = kurs2.gibInternalID();
		regelVerletzungKursMitKurs[nr1][nr2] -= 1;
		regelVerletzungKursMitKurs[nr2][nr1] -= 1;
		bewertungRegelverletzungen += Math.max(kurs1.gibSchienenAnzahl(), kurs2.gibSchienenAnzahl());
	}

}
