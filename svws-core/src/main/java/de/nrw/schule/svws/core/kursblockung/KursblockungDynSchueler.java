package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs;
import jakarta.validation.constraints.NotNull;

/** Ein Schüler-Objekt (während des Blockungsvorgangs).
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungDynSchueler {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Die ID (von der GUI) des Schülers, beispielsweise 42. */
	private final long guiID;

	/** Ein String-Darstellung des Schüler für Warnungen und Fehlermeldungen, beispielsweise 'Mareike Musterfrau'. */
	private final @NotNull String representation;

	/** Alle Facharten (=Fachwahlen) des Schüler, z.B. 'D;LK'. */
	private @NotNull KursblockungDynFachart @NotNull [] fachartArr;

	/** Die ID der zur Fachart zugehörigen Fachwahl der GUI bzw. DB. */
	private @NotNull long[] fachartZuGUI;

	/** Der aktuell zur Fachart zugeordnete Kurs. */
	private @NotNull KursblockungDynKurs[] fachartZuKurs;
	private @NotNull KursblockungDynKurs[] fachartZuKursSaveS;
	private @NotNull KursblockungDynKurs[] fachartZuKursSaveK;
	private @NotNull KursblockungDynKurs[] fachartZuKursSaveG;

	/** Referenz zur Statistik, um diese über Nichtwahlen zu informieren. */
	private final @NotNull KursblockungDynStatistik statistik;

	/** Die aktuellen Nichtwahlen dieses Schülers. */
	private int nichtwahlen;

	/** Die aktuelle Information darüber, ob die Schiene des Schülers belegt ist. */
	private final @NotNull boolean[] schieneBelegt;

	// /**
	// * Dies ist ein Dummy, damit das Feld matrix mit einem Nicht-Null-Wert initialisiert wird.
	// */
	// private static @NotNull KursblockungMatrix dummy = new KursblockungMatrix(0, 0);

	/** Diese Datenstruktur wird verwendet um bei bestimmten Algorithmus Kurse auf Schienen zu verteilen. */
	private @NotNull KursblockungMatrix matrix;

	/** Verbotene Kurse des Schülers. Diese dürfen nicht belegt werden. */
	private final @NotNull boolean[] kursGesperrt;

	/** Im Konstruktor wird {@code pSchueler} in ein Objekt dieser Klasse umgewandelt.
	 * 
	 * @param pRandom         Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pStatistik      Referenz um die Nichtwahlen mitzuteilen.
	 * @param pSchueler       Die Schüler-Daten von der GUI/DB.
	 * @param pSchienenAnzahl Wir benötigt, um {@link #schieneBelegt} zu initialisieren.
	 * @param pKursAnzahl     Die Anzahl aller Kurse. Wird benötigt, damit {@link #kursGesperrt} initialisiert werden
	 *                        kann. */
	KursblockungDynSchueler(@NotNull Random pRandom, @NotNull KursblockungInputSchueler pSchueler,
			@NotNull KursblockungDynStatistik pStatistik, int pSchienenAnzahl, int pKursAnzahl) {
		_random = pRandom;
		// dummy = new KursblockungMatrix(0, 0);
		guiID = pSchueler.id;
		representation = pSchueler.representation;
		statistik = pStatistik;
		fachartArr = new KursblockungDynFachart[0];
		fachartZuGUI = new long[0];
		fachartZuKurs = new KursblockungDynKurs[0];
		fachartZuKursSaveS = new KursblockungDynKurs[0];
		fachartZuKursSaveK = new KursblockungDynKurs[0];
		fachartZuKursSaveG = new KursblockungDynKurs[0];
		nichtwahlen = 0;
		schieneBelegt = new boolean[pSchienenAnzahl];
		kursGesperrt = new boolean[pKursAnzahl];
		matrix = new KursblockungMatrix(_random, 0, 0);
	}

	@Override
	public @NotNull String toString() {
		return representation;
	}

	// ########################################
	// ################ GETTER ################
	// ########################################

	/** Liefert die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 * 
	 * @return Die ID (von der GUI) dieses Schülers. */
	@NotNull
	long gibGuiID() {
		return guiID;
	}

	/** Eine String-Darstellung des Schülers. Beinhaltet meistens den Vornamen, den Nachnamen, das Geburtsdatum und das
	 * Geschlecht.
	 * 
	 * @return Eine String-Darstellung des Schülers. */
	@NotNull
	String gibRepresentation() {
		return representation;
	}

	/** Liefert die aktuelle Anzahl an Nichtwahlen.
	 * 
	 * @return Die aktuelle Anzahl an Nichtwahlen. */
	int gibNichtwahlen() {
		return nichtwahlen;
	}

	/** Liefert ein Array aller Facherten (= Fachwahlen) des Schülers.
	 * 
	 * @return Ein Array aller Facherten (= Fachwahlen) des Schülers. */
	@NotNull
	KursblockungDynFachart @NotNull [] gibFacharten() {
		return fachartArr;
	}

	/** Liefert TRUE, falls der Schüler mindestens einen Multikurs hat. Ein Multikurs ist ein Kurs, der über mehr als
	 * eine Schiene geht.
	 * 
	 * @return TRUE, falls der Schüler mindestens einen Multikurs hat. */
	boolean gibHatMultikurs() {
		for (@NotNull
		KursblockungDynFachart fachart : fachartArr) {
			if (fachart.gibHatMultikurs()) {
				return true;
			}
		}
		return false;
	}

	/** Liefert ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 * 
	 * @return Ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten. */
	@NotNull
	KursblockungDynKurs[] gibKurswahlen() {
		return fachartZuKurs;
	}

	// ########################################
	// ########## AKTIONEN / SETTER ###########
	// ########################################

	/** Setzt alle Facharten (=Fachwahlen) des Schülers.
	 * 
	 * @param pFacharten Die Facharten des Schülers.
	 * @param pIDs       Die zur Fachwahl zugehörige ID der GUI bzw. Datenbank. */
	void aktionSetzeFachartenUndIDs(@NotNull KursblockungDynFachart @NotNull [] pFacharten, @NotNull long[] pIDs) {
		int nFacharten = pFacharten.length;
		fachartArr = pFacharten;
		fachartZuGUI = pIDs;
		fachartZuKurs = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveS = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveK = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveG = new KursblockungDynKurs[nFacharten];
		statistik.aktionNichtwahlenVeraendern(nFacharten);
		nichtwahlen = nFacharten;
		// FachartArr und fachartZuGUI sortieren --> geringe Kursanzahl zuerst.
		for (int i = 1; i < nFacharten; i++) {
			for (int j = i; j >= 1; j--) {
				int anzL = fachartArr[j - 1].gibKurseMax();
				int anzR = fachartArr[j].gibKurseMax();
				if (anzL > anzR) {
					@NotNull
					KursblockungDynFachart fL = fachartArr[j - 1];
					@NotNull
					KursblockungDynFachart fR = fachartArr[j];
					long valL = fachartZuGUI[j - 1];
					long valR = fachartZuGUI[j];
					fachartArr[j - 1] = fR;
					fachartArr[j] = fL;
					fachartZuGUI[j - 1] = valR;
					fachartZuGUI[j] = valL;
				}
			}
		}

		matrix = new KursblockungMatrix(_random, nFacharten, schieneBelegt.length);
	}

	/** Sperrt einen bestimmten Kurs für diesen Schüler.
	 * 
	 * @param pInterneKursID Die ID des Kurses, der gesperrt wird. */
	void aktionSetzeKursSperrung(int pInterneKursID) {
		kursGesperrt[pInterneKursID] = true;
	}

	/** Speichert die aktuell belegten Kurse im Zustand S. */
	void aktionZustandSpeichernS() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveS, 0, fachartZuKurs.length);
	}

	/** Speichert die aktuell belegten Kurse im Zustand K. */
	void aktionZustandSpeichernK() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveK, 0, fachartZuKurs.length);
	}

	/** Speichert die aktuell belegten Kurse im Zustand G. */
	void aktionZustandSpeichernG() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveG, 0, fachartZuKurs.length);
	}

	/** Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand
	 * S gespeichert wurden. */
	void aktionZustandLadenS() {
		aktionWaehleKurse(fachartZuKursSaveS);
	}

	/** Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand
	 * K gespeichert wurden. */
	void aktionZustandLadenK() {
		aktionWaehleKurse(fachartZuKursSaveK);
	}

	/** Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand
	 * G gespeichert wurden. */
	void aktionZustandLadenG() {
		aktionWaehleKurse(fachartZuKursSaveG);
	}

	private void aktionWaehleKurse(@NotNull KursblockungDynKurs[] wahl) {
		aktionKurseAlleEntfernen();
		for (int i = 0; i < fachartZuKurs.length; i++) {
			KursblockungDynKurs kurs = wahl[i];

			if (kurs == null) {
				continue;
			}

			if (kursGesperrt[kurs.gibInternalID()]) {
				System.out.println("FEHLER: Schüler " + guiID + " darf den Kurs " + kurs.gibID() + " nicht wählen.");
			}

			aktionKursHinzufuegen(i, kurs);
		}
	}

	/** Entfernt den Schüler aus seinen aktuell zugeordneten Kursen. */
	void aktionKurseAlleEntfernen() {
		for (int i = 0; i < fachartArr.length; i++) {
			KursblockungDynKurs kurs = fachartZuKurs[i];
			// Kurs vorhanden? --> Entferne S. aus dem Kurs.
			if (kurs != null) {
				aktionKursEntfernen(i, kurs);
			}
		}
	}

	// ########################################
	// ########## METHODEN GECHECKT ###########
	// ########################################

	/** Verteilt alle Kurse des S., die über mehr als eine Schiene gehen. */
	void aktionKurseVerteilenNurMultikurseZufaellig() {
		@NotNull
		int[] perm = KursblockungStatic.gibPermutation(_random, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			int iFachart = perm[pFachart];

			// Bereits belegte Facharten überspringen.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Nicht-Multikurse überspringen.
			@NotNull
			KursblockungDynFachart fachart = fachartArr[iFachart];
			if (!fachart.gibHatMultikurs()) {
				continue;
			}

			// Alle Kurse der Fachart durchgehen und probieren, ob wählbar.
			@NotNull
			KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			@NotNull
			int[] perm2 = KursblockungStatic.gibPermutation(_random, kurse.length);
			for (int pKurs = 0; pKurs < perm2.length; pKurs++) {
				@NotNull
				KursblockungDynKurs kurs = kurse[perm2[pKurs]];

				// Überspringt gesperrte Kurse des Schülers.
				if (kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}

				boolean waehlbar = true;
				for (int nr : kurs.gibSchienenLage()) {
					if (schieneBelegt[nr]) {
						waehlbar = false;
					}
				}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/** Verteilt alle Kurse des S. von denen es pro Fachart nur einen gibt. */
	void aktionKurseVerteilenNurFachartenMitEinemKurs() {
		for (int iFachart = 0; iFachart < fachartArr.length; iFachart++) {
			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Facharten mit mehr als einen Kurs ignorieren.
			@NotNull
			KursblockungDynFachart fachart = fachartArr[iFachart];
			if (fachart.gibKurseMax() != 1) {
				continue;
			}

			// Alle Kurse der Facharten durchgehen und probieren, ob wählbar.
			@NotNull
			KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			for (int iKurse = 0; iKurse < kurse.length; iKurse++) {
				@NotNull
				KursblockungDynKurs kurs = kurse[iKurse];

				// Überspringt gesperrte Kurse des Schülers.
				if (kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}

				// Der Kurs ist wählbar, wenn jede Schiene des Kurses frei ist.
				boolean waehlbar = true;
				for (int nr : kurs.gibSchienenLage()) {
					if (schieneBelegt[nr]) {
						waehlbar = false;
					}
				}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/** Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines gewichteten Matching Algorithmus. Kleinere
	 * Kurse werden in der Wahl bevorzugt. */
	void aktionKurseVerteilenMitBipartiteMatchingGewichtetem() {
		long INFINITY = 1000000;

		// Matrix füllen.
		@NotNull
		long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = INFINITY;
			}

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Bewertung der Zeile
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (!schieneBelegt[c]) {
					KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchiene(c, kursGesperrt);
					if (kurs != null) {
						data[r][c] = kurs.gibGewichtetesMatchingBewertung();
					}
				}
			}
		}

		// Berechnen
		@NotNull
		int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Zuordnen
		for (int r = 0; r < fachartArr.length; r++) {

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Kein Matching-Partner gefunden?
			int c = r2c[r];
			if (c < 0) {
				continue;
			}

			// Matching ungültig?
			if (data[r][c] == INFINITY) {
				continue;
			}

			// Zuordnen
			KursblockungDynKurs kursGefunden = fachartArr[r].gibKleinstenKursInSchiene(c, kursGesperrt);
			if (kursGefunden != null) {
				aktionKursHinzufuegen(r, kursGefunden);
			} else {
				System.out.println("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!");
			}

		}

	}

	/** Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines Bipartiten-Matching-Algorithmus. */
	void aktionKurseVerteilenMitBipartiteMatching() {

		// Matrix füllen.
		@NotNull
		long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = 0;
			}

			// Kurs bereits zugeordnet ODER Multikurs? --> Zeile überspringen
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Bewertung der Zeile
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (!schieneBelegt[c]) {
					KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchiene(c, kursGesperrt);
					if (kurs != null) {
						data[r][c] = 1;
					}
				}
			}

		}

		// Berechnen
		@NotNull
		int[] r2c = matrix.gibMaximalesBipartitesMatching(true);
		for (int r = 0; r < fachartArr.length; r++) {

			// Kurs bereits zugeordnet ODER Multikurs? --> Zeile überspringen
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Keinen Matching-Partner gefunden?
			int c = r2c[r];
			if (c == -1) {
				continue;
			}

			// Zuordnen
			KursblockungDynKurs kursGefunden = fachartArr[r].gibKleinstenKursInSchiene(c, kursGesperrt);
			if (kursGefunden != null) {
				aktionKursHinzufuegen(r, kursGefunden);
			} else {
				System.out.println("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!");
			}

		}

	}

	/** Die (nicht Multi) Facharten des S. werden auf eine Schiene gematched. Falls dies nicht klappt, wird der Fachart
	 * gesagt, dass einer ihrer Kurse die Schiene wechseln muss. Um welche Schiene es sich dabei handelt, wird durch den
	 * Matching-Algorithmus berechnet. Der S. wird bei den Berechnungen nicht einem Kurs hinzugefügt.
	 * 
	 * @return TRUE, falls sich die Lage der Kurse verändert hat. */
	boolean aktionKurseVerteilenNachDeinemWunsch() {
		long VAL_UNGUELTIG = 1000000;
		long VAL_KURS_GEWAEHLT = 0;
		long VAL_KURS_MUSS_WANDERN = 1;

		// 1) Matrix füllen.

		@NotNull
		long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {
			KursblockungDynFachart fachart = fachartArr[r];

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = VAL_UNGUELTIG;
			}

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachart.gibHatMultikurs()) {
				continue;
			}

			// Bewerte die Zeile, falls die Schiene c nicht belegt ist.
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (!schieneBelegt[c]) {
					data[r][c] = fachart.gibHatKursInSchiene(c, kursGesperrt) ? VAL_KURS_GEWAEHLT : //
							fachart.gibHatKursMitFreierSchiene(c, kursGesperrt) ? VAL_KURS_MUSS_WANDERN : VAL_UNGUELTIG;
				}
			}

		}

		// Matching Berechnen
		@NotNull
		int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Zuordnen
		boolean kurslage_veraendert = false;
		for (int r = 0; r < fachartArr.length; r++) {
			KursblockungDynFachart fachart = fachartArr[r];

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachart.gibHatMultikurs()) {
				continue;
			}

			// Keinen Matching-Partner gefunden?
			int c = r2c[r];
			if (c < 0) {
				continue;
			}

			// Alle Kurse der Fachart nicht wählbar, alle gesperrt?
			if (data[r][c] == VAL_UNGUELTIG) {
				continue;
			}

			// S. wäre in diesem Kurs, aber S. trotzdem nicht hinzufügen, da dies nur eine Simulation ist.
			if (data[r][c] == VAL_KURS_GEWAEHLT) {
				continue;
			}

			// VAL_KURS_MUSS_WANDERN
			fachart.aktionZufaelligerKursWandertNachSchiene(c);
			kurslage_veraendert = true;
		}

		return kurslage_veraendert;
	}

	/** Geht die Facharten durch (Facharten mit einer kleineren Kursanzahl zuerst) und geht dann pro Fachart alle Kurse
	 * durch (Kurse mit kleinerer Schüleranzahl zuerst). Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es
	 * geht weiter mit der nächsten Fachart. Ein Kurs ist wählbar, wenn nicht bereits ein Kurs zugeordnet wurde und die
	 * Schienen in den der Kurs sind frei sind.<br>
	 */
	void aktionKurseVerteilenZufaellig() {
		@NotNull
		int[] perm = KursblockungStatic.gibPermutation(_random, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			int iFachart = perm[pFachart];

			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Alle Kurse der Facharten durchgehen und probieren, ob wählbar.
			@NotNull
			KursblockungDynFachart fachart = fachartArr[iFachart];
			@NotNull
			KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			for (int iKurs = 0; iKurs < kurse.length; iKurs++) {
				@NotNull
				KursblockungDynKurs kurs = kurse[iKurs];

				// Überspringt gesperrte Kurse des Schülers.
				if (kursGesperrt[kurs.gibInternalID()]) {
					continue;
				}

				// Herausfinden, ob wählbar.
				boolean waehlbar = true;
				for (int nr : kurs.gibSchienenLage()) {
					if (schieneBelegt[nr]) {
						waehlbar = false;
					}
				}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}

		}

	}

	/** Erzeugt pro Fachwahl ein Objekt des Typs {@link KursblockungOutputFachwahlZuKurs} und fügt es dem Vector
	 * {@code vFachwahlZuKurs} hinzu. Die GUI kann daraus die Schüler-Zu-Kurs-Zuordnungen rekonstruiern.
	 * 
	 * @param vFachwahlZuKurs Fügt diesem Vector pro Fachwahl ein Objekt des Typs
	 *                        {@link KursblockungOutputFachwahlZuKurs} hinzu. */
	void aktionOutputsErzeugen(@NotNull Vector<@NotNull KursblockungOutputFachwahlZuKurs> vFachwahlZuKurs) {
		for (int i = 0; i < fachartArr.length; i++) {
			@NotNull
			KursblockungOutputFachwahlZuKurs fachwahlZuKurs = new KursblockungOutputFachwahlZuKurs();
			fachwahlZuKurs.fachwahl = fachartZuGUI[i];
			KursblockungDynKurs tmpKurs = fachartZuKurs[i];
			fachwahlZuKurs.kurs = (tmpKurs == null) ? -1 : tmpKurs.gibID();
			vFachwahlZuKurs.add(fachwahlZuKurs);
		}
	}

	// ########################################
	// ########## PRIVATE METHODEN ############
	// ########################################

	private void aktionKursHinzufuegen(int fachartIndex, @NotNull KursblockungDynKurs kurs) {
		kurs.aktionSchuelerHinzufügen();
		statistik.aktionNichtwahlenVeraendern(-1);
		nichtwahlen--;
		for (int nr : kurs.gibSchienenLage()) {
			if (schieneBelegt[nr]) {
				System.out.println("FEHLER: Schienen-Doppelbelegung! " + representation);
			}
			schieneBelegt[nr] = true;
		}
		fachartZuKurs[fachartIndex] = kurs;
	}

	private void aktionKursEntfernen(int fachartIndex, @NotNull KursblockungDynKurs kurs) {
		kurs.aktionSchuelerEntfernen();
		statistik.aktionNichtwahlenVeraendern(+1);
		nichtwahlen++;
		for (int nr : kurs.gibSchienenLage()) {
			if (!schieneBelegt[nr]) {
				System.out.println("FEHLER: Kurs ist gar nicht in Schiene ! " + representation);
			}
			schieneBelegt[nr] = false;
		}
		fachartZuKurs[fachartIndex] = null;
	}

	/** Liefert TRUE, wenn dieser Schüler dem übergebenen Kurs zugeordnet wurde.
	 * 
	 * @param  kurs Der Kurs in dem der Schüler potentiell ist.
	 * @return      TRUE, wenn dieser Schüler dem übergebenen Kurs zugeordnet wurde. */
	boolean gibIstInKurs(KursblockungDynKurs kurs) {
		for (KursblockungDynKurs zugeordneterKurs : fachartZuKurs)
			if (zugeordneterKurs == kurs)
				return true;
		return false;
	}

}
