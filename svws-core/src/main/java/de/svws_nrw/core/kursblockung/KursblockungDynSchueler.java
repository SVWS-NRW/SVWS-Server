package de.svws_nrw.core.kursblockung;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Schüler-Objekt (während des Blockungsvorgangs).
 *
 * @author Benjamin A. Bartsch
 */
public class KursblockungDynSchueler {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger _logger;

	/** Die ID (von der GUI) des Schülers, beispielsweise 42. */
	private final long guiID;

	/** Die interne ID des Schülers. */
	final int internalSchuelerID;

	/** Ein String-Darstellung des Schüler für Warnungen und Fehlermeldungen, beispielsweise 'Mareike Musterfrau'. */
	private final @NotNull String representation;

	/** Alle Facharten (=Fachwahlen) des Schüler, z.B. 'D;LK'. */
	private @NotNull KursblockungDynFachart @NotNull [] fachartArr;

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

	/** Diese Datenstruktur wird verwendet um bei bestimmten Algorithmus Kurse auf Schienen zu verteilen. */
	private @NotNull KursblockungMatrix matrix;

	/** Verbotene Kurse des Schülers. Diese dürfen nicht belegt werden. */
	final @NotNull boolean[] kursGesperrt;

	/**
	 * Im Konstruktor wird {@code pSchueler} in ein Objekt dieser Klasse umgewandelt.
	 *
	 * @param pRandom         Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger         Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pStatistik      Referenz um die Nichtwahlen mitzuteilen.
	 * @param pSchuelerID     Die ID des Schülers von der GUI/DB.
	 * @param pSchienenAnzahl Wir benötigt, um {@link #schieneBelegt} zu initialisieren.
	 * @param pKursAnzahl     Die Anzahl aller Kurse. Wird benötigt, damit {@link #kursGesperrt} initialisiert werden kann.
	 * @param pInternalID     Eine interne ID für schnellen Zugriff.
	 */
	KursblockungDynSchueler(final @NotNull Logger pLogger, final @NotNull Random pRandom, final long pSchuelerID,
			final @NotNull KursblockungDynStatistik pStatistik, final int pSchienenAnzahl, final int pKursAnzahl, final int pInternalID) {
		_random = pRandom;
		_logger = pLogger;
		guiID = pSchuelerID;
		internalSchuelerID = pInternalID;
		representation = "Schüler " + pSchuelerID;
		statistik = pStatistik;
		fachartArr = new KursblockungDynFachart[0];
		fachartZuKurs = new KursblockungDynKurs[0];
		fachartZuKursSaveS = new KursblockungDynKurs[0];
		fachartZuKursSaveK = new KursblockungDynKurs[0];
		fachartZuKursSaveG = new KursblockungDynKurs[0];
		nichtwahlen = 0;
		schieneBelegt = new boolean[pSchienenAnzahl];
		kursGesperrt = new boolean[pKursAnzahl];
		matrix = new KursblockungMatrix(_random, 0, 0);
	}

	/**
	 * Liefert die String-Repräsentation der Schiene.
	 *
	 * @return die String-Repräsentation der Schiene.
	 */
	@Override
	public @NotNull String toString() {
		return representation;
	}

	// ########################################
	// ################ GETTER ################
	// ########################################

	/**
	 * Liefert die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 *
	 * @return die ID (von der GUI) dieses Schülers, beispielsweise 42.
	 */
	@NotNull
	long gibDatenbankID() {
		return guiID;
	}

	/**
	 * Liefert eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 *
	 * @return eine String-Darstellung des Schüler (i.d.R. Vorname, Nachname, Geburtsdatum und Geschlecht).
	 */
	@NotNull
	String gibRepresentation() {
		return representation;
	}

	/**
	 * Liefert die aktuelle Anzahl an Nichtwahlen.
	 *
	 * @return Die aktuelle Anzahl an Nichtwahlen.
	 */
	int gibNichtwahlen() {
		return nichtwahlen;
	}

	/**
	 * Liefert ein Array aller Facharten (= Fachwahlen) des Schülers.
	 *
	 * @return Ein Array aller Facharten (= Fachwahlen) des Schülers.
	 */
	@NotNull
	KursblockungDynFachart @NotNull [] gibFacharten() {
		return fachartArr;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens einen Multikurs hat.
	 * <br>Ein Multikurs ist ein Kurs, der über mehr als eine Schiene geht.
	 *
	 * @return TRUE, falls der Schüler mindestens einen Multikurs hat.
	 */
	boolean gibHatMultikurs() {
		for (final @NotNull KursblockungDynFachart fachart : fachartArr)
			if (fachart.gibHatMultikurs())
				return true;

		return false;
	}

	/**
	 * Liefert ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 *
	 * @return ein Array der aktuell zugeordneten Kurse. Das Array kann NULL-Werte enthalten.
	 */
	@NotNull
	KursblockungDynKurs[] gibKurswahlen() {
		return fachartZuKurs;
	}

	/**
	 * Liefert TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 *
	 * @param kurs  Der Kurs in dem der Schüler potentiell ist.
	 *
	 * @return TRUE, falls dieser Schüler dem übergebenen Kurs zugeordnet ist.
	 */
	boolean gibIstInKurs(final KursblockungDynKurs kurs) {
		for (final KursblockungDynKurs zugeordneterKurs : fachartZuKurs)
			if (zugeordneterKurs == kurs)
				return true;

		return false;
	}

	// ########################################
	// ########## AKTIONEN / SETTER ###########
	// ########################################

	/**
	 * Liefert die zum Fach zugehörige Fachart (= Fachwahl) des Schülers.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die zum Fach zugehörige Fachart (= Fachwahl) des Schülers.
	 */
	private @NotNull KursblockungDynFachart gibFachartZuFachID(final long idFach) {
		for (final @NotNull KursblockungDynFachart fachart : fachartArr)
			if (fachart.gibFach().id == idFach)
				return fachart;

		throw new DeveloperNotificationException(representation + " hat kein Fach mit ID = " + idFach + "!");
	}

	/**
	 * Setzt alle Facharten (=Fachwahlen) des Schülers.
	 *
	 * @param pFacharten  Die Facharten des Schülers.
	 */
	void aktionSetzeFachartenUndIDs(final @NotNull KursblockungDynFachart @NotNull [] pFacharten) {
		final int nFacharten = pFacharten.length;
		fachartArr = pFacharten;
		fachartZuKurs = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveS = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveK = new KursblockungDynKurs[nFacharten];
		fachartZuKursSaveG = new KursblockungDynKurs[nFacharten];
		statistik.aktionNichtwahlenVeraendern(nFacharten);
		nichtwahlen = nFacharten;

		// FachartArr sortieren --> geringe Kursanzahl zuerst.
		for (int i = 1; i < nFacharten; i++)
			for (int j = i; j >= 1; j--) {
				final int anzL = fachartArr[j - 1].gibKurseMax();
				final int anzR = fachartArr[j].gibKurseMax();
				if (anzL > anzR) {
					final @NotNull KursblockungDynFachart fL = fachartArr[j - 1];
					final @NotNull KursblockungDynFachart fR = fachartArr[j];
					fachartArr[j - 1] = fR;
					fachartArr[j] = fL;
				}
			}

		matrix = new KursblockungMatrix(_random, nFacharten, schieneBelegt.length);
	}

	/**
	 * Sperrt einen bestimmten Kurs für diesen Schüler.
	 *
	 * @param pInterneKursID  Die ID des Kurses, der gesperrt wird.
	 */
	void aktionSetzeKursSperrung(final int pInterneKursID) {
		kursGesperrt[pInterneKursID] = true;
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand S.
	 */
	void aktionZustandSpeichernS() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveS, 0, fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand K.
	 */
	void aktionZustandSpeichernK() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveK, 0, fachartZuKurs.length);
	}

	/**
	 * Speichert die aktuell belegten Kurse im Zustand G.
	 */
	void aktionZustandSpeichernG() {
		System.arraycopy(fachartZuKurs, 0, fachartZuKursSaveG, 0, fachartZuKurs.length);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand S gespeichert wurden.
	 */
	void aktionZustandLadenS() {
		aktionZustandLaden(fachartZuKursSaveS);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand K gespeichert wurden.
	 */
	void aktionZustandLadenK() {
		aktionZustandLaden(fachartZuKursSaveK);
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die im {@link KursblockungDynSchueler}-Objekt gespeichert wurden.
	 *
	 * @param b        Das {@link KursblockungDynSchueler}-Objekt.
	 * @param kursArr  Das Array aller {@link KursblockungDynKurs}-Objekte.
	 */
	void aktionZustandLadenVon(final @NotNull KursblockungDynSchueler b, final @NotNull KursblockungDynKurs @NotNull [] kursArr) {
		// Vorsicht: Man muss den Schüler in das EIGENE Kurs-Objekt hinzufügen!!!
		aktionKurseAlleEntfernen();

		for (int i = 0; i < b.fachartZuKurs.length; i++) {
			final KursblockungDynKurs kursB = b.fachartZuKurs[i];
			if (kursB != null) {
				final KursblockungDynKurs kurs = kursArr[kursB.gibInternalID()];

				if (kurs.gibIstErlaubtFuerSchueler(this))
					aktionKursHinzufuegen(i, kurs);
				else
					throw new DeveloperNotificationException("FEHLER: Schüler " + guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.");
			}
		}
	}

	/**
	 * Entfernt zunächst den Schüler aus seinen aktuellen Kursen und setzt ihn dann in die Kurse, die zuvor im Zustand G gespeichert wurden.
	 */
	void aktionZustandLadenG() {
		aktionZustandLaden(fachartZuKursSaveG);
	}

	/**
	 * Entfernt den Schüler aus seinen aktuell zugeordneten Kursen.
	 */
	void aktionKurseAlleEntfernen() {
		for (int i = 0; i < fachartArr.length; i++) {
			final KursblockungDynKurs kurs = fachartZuKurs[i];
			// Kurs vorhanden? --> Entferne S. aus dem Kurs.
			if (kurs != null)
				aktionKursEntfernen(i, kurs);
		}
	}

	/**
	 * Verteilt alle Kurse des S., die über mehr als eine Schiene gehen.
	 */
	void aktionKurseVerteilenNurMultikurseZufaellig() {
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			final int iFachart = perm[pFachart];

			// Bereits belegte Facharten überspringen.
			if (fachartZuKurs[iFachart] != null)
				continue;

			// Facharten ohne Multikurse überspringen.
			final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
			if (!fachart.gibHatMultikurs())
				continue;

			// Alle Kurse der Fachart durchgehen und probieren, ob wählbar.
			final @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			final @NotNull int[] perm2 = KursblockungStatic.gibPermutation(_random, kurse.length);
			for (final int i : perm2) {
				final @NotNull KursblockungDynKurs kurs = kurse[i];

				// Überspringt nicht erlaubte Kurse.
				if (!kurs.gibIstErlaubtFuerSchueler(this))
					continue;

				// Der Kurs ist wählbar, wenn jede Schiene des Kurses frei ist.
				boolean waehlbar = true;
				for (final int nr : kurs.gibSchienenLage())
					if (schieneBelegt[nr]) {
						waehlbar = false;
						break;
					}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Verteilt alle Kurse des S. von denen es (pro Fachart) nur einen gibt.
	 */
	void aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs() {
		for (int iFachart = 0; iFachart < fachartArr.length; iFachart++) {
			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null)
				continue;

			// Hole Kurse der Fachart
			final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
			final @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();

			// Facharten mit mehr als einen Kurs ignorieren.
			int erlaubt = 0;
			for (final @NotNull KursblockungDynKurs kurs : kurse)
				if (kurs.gibIstErlaubtFuerSchueler(this))
					erlaubt++;
			if (erlaubt != 1)
				continue;

			// Alle Kurse der Facharten durchgehen und probieren, ob wählbar. Es wird genau ein Kurs sein.
			for (final @NotNull KursblockungDynKurs kurs : kurse) {
				// Überspringt nicht erlaubte Kurse.
				if (!kurs.gibIstErlaubtFuerSchueler(this))
					continue;

				// Der Kurs ist wählbar, wenn jede Schiene des Kurses frei ist.
				boolean waehlbar = true;
				for (final int nr : kurs.gibSchienenLage())
					if (schieneBelegt[nr]) {
						waehlbar = false;
						break;
					}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}
		}
	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines gewichteten Matching Algorithmus.
	 * Kleinere Kurse werden in der Wahl bevorzugt.
	 */
	void aktionKurseVerteilenMitBipartiteMatchingGewichtetem() {
		final long _INFINITY = 1000000;

		// Matrix füllen.
		final @NotNull long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++)
				data[r][c] = _INFINITY;

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs())
				continue;


			// Bewertung der Zeile
			for (int c = 0; c < schieneBelegt.length; c++)
				if (!schieneBelegt[c]) {
					final KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
					if (kurs != null)
						data[r][c] = kurs.gibGewichtetesMatchingBewertung();
				}
		}

		// Berechnen
		final @NotNull int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Zuordnen
		for (int r = 0; r < fachartArr.length; r++) {

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs())
				continue;

			// Kein Matching-Partner gefunden?
			final int c = r2c[r];
			if (c < 0)
				continue;

			// Matching ungültig?
			if (data[r][c] == _INFINITY)
				continue;

			// Zuordnen
			final KursblockungDynKurs kursGefunden = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
			if (kursGefunden != null)
				aktionKursHinzufuegen(r, kursGefunden);
			else
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!");
		}

	}

	/**
	 * Verteilt alle Kurse die über genau 1 Schiene gehen mit Hilfe eines Bipartiten-Matching-Algorithmus.
	 */
	void aktionKurseVerteilenMitBipartiteMatching() {

		// Matrix füllen.
		final @NotNull long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++)
				data[r][c] = 0;

			// Kurs bereits zugeordnet ODER Multikurs? --> Zeile überspringen
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs())
				continue;

			// Bewertung der Zeile
			for (int c = 0; c < schieneBelegt.length; c++)
				if (!schieneBelegt[c]) {
					final KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
					if (kurs != null)
						data[r][c] = 1;
				}
		}

		// Berechnen
		final @NotNull int[] r2c = matrix.gibMaximalesBipartitesMatching(true);
		for (int r = 0; r < fachartArr.length; r++) {

			// Kurs bereits zugeordnet ODER Multikurs? --> Zeile überspringen
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs())
				continue;

			// Keinen Matching-Partner gefunden?
			final int c = r2c[r];
			if (c == -1)
				continue;

			// Zuordnen
			final KursblockungDynKurs kursGefunden = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
			if (kursGefunden != null)
				aktionKursHinzufuegen(r, kursGefunden);
			else
				throw new DeveloperNotificationException("FEHLER: Kein Kurs in [" + r + "/" + c + "] gefunden!");
		}

	}

	/**
	 * Die (nicht Multi) Facharten des S. werden auf eine Schiene gematched. Falls dies nicht klappt, wird der Fachart
	 * gesagt, dass einer ihrer Kurse die Schiene wechseln muss. Um welche Schiene es sich dabei handelt, wird durch den
	 * Matching-Algorithmus berechnet. Der S. wird bei den Berechnungen nicht einem Kurs hinzugefügt.
	 *
	 * @return TRUE, falls sich die Lage der Kurse verändert hat.
	 */
	boolean aktionKurseVerteilenNachDeinemWunsch() {
		final long _VAL_UNGUELTIG = 1000000;
		final long _VAL_KURS_GEWAEHLT = 0;
		final long _VAL_KURS_MUSS_WANDERN = 1;

		// 1) Matrix füllen.
		final @NotNull long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < fachartArr.length; r++) {
			final KursblockungDynFachart fachart = fachartArr[r];

			// Zeile löschen.
			for (int c = 0; c < schieneBelegt.length; c++)
				data[r][c] = _VAL_UNGUELTIG;

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachart.gibHatMultikurs())
				continue;

			// Bewerte die Zeile, falls die Schiene c nicht belegt ist.
			for (int c = 0; c < schieneBelegt.length; c++)
				if (!schieneBelegt[c]) {
					if (fachart.gibHatSchuelerKursInSchiene(c, this))
						data[r][c] = _VAL_KURS_GEWAEHLT;
					else
						data[r][c] = fachart.gibHatSchuelerKursMitFreierSchiene(c, this) ? _VAL_KURS_MUSS_WANDERN : _VAL_UNGUELTIG;
				}
		}

		// 2) Matching Berechnen
		final @NotNull int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// 3) Zuordnen
		boolean kurslage_veraendert = false;
		for (int r = 0; r < fachartArr.length; r++) {
			final KursblockungDynFachart fachart = fachartArr[r];

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachart.gibHatMultikurs())
				continue;

			// Keinen Matching-Partner gefunden?
			final int c = r2c[r];
			if (c < 0)
				continue;

			// Alle Kurse der Fachart nicht wählbar, alle gesperrt?
			if (data[r][c] == _VAL_UNGUELTIG)
				continue;

			// S. wäre in diesem Kurs, aber S. trotzdem nicht hinzufügen, da dies nur eine Simulation ist.
			if (data[r][c] == _VAL_KURS_GEWAEHLT)
				continue;

			// VAL_KURS_MUSS_WANDERN
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
	void aktionKurseVerteilenZufaellig() {
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			final int iFachart = perm[pFachart];

			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null)
				continue;

			// Alle Kurse der Facharten durchgehen und probieren, ob wählbar.
			final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
			final @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			for (final @NotNull KursblockungDynKurs kurs : kurse) {
				// Überspringt nicht erlaubte Kurse.
				if (!kurs.gibIstErlaubtFuerSchueler(this))
					continue;

				// Der Kurs ist wählbar, wenn jede Schiene des Kurses frei ist.
				boolean waehlbar = true;
				for (final int nr : kurs.gibSchienenLage())
					if (schieneBelegt[nr]) {
						waehlbar = false;
						break;
					}

				// Falls wählbar, dann Kurs hinzufügen und zur nächsten Fachart gehen.
				if (waehlbar) {
					aktionKursHinzufuegen(iFachart, kurs);
					break;
				}
			}

		}

	}

	// ########################################
	// ########## PRIVATE METHODEN ############
	// ########################################

	/**
	 * Ausgabe der aktuellen Kurslage zum debuggen.
	 */
	void debugKurswahlen() {
		_logger.modifyIndent(+4);
		_logger.logLn("");
		_logger.logLn(representation);
		final HashSet<Integer> setSchienenLage = new HashSet<>();
		for (final KursblockungDynKurs kurs : fachartZuKurs) {
			if (kurs == null)
				continue;
			_logger.logLn("    " + kurs.toString() + "    " + Arrays.toString(kurs.gibSchienenLage()));
			for (final int schiene : kurs.gibSchienenLage())
				if (!setSchienenLage.add(schiene)) {
					_logger.logLn("Kollision");
					return;
				}
		}
		_logger.modifyIndent(-4);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	void regel_11_zusammen_mit_schueler_in_fach(final @NotNull KursblockungDynSchueler that, final long idFach) {
		final @NotNull KursblockungDynFachart fachart1 = this.gibFachartZuFachID(idFach);
		final @NotNull KursblockungDynFachart fachart2 = that.gibFachartZuFachID(idFach);

		if (fachart1.gibNr() != fachart2.gibNr())
			throw new DeveloperNotificationException("Regel 11:" + representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2
					+ " haben nicht die selbe Kursart!");

		fachart1.regel_schueler_zusammen_mit_schueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler nicht zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	void regel_12_verbieten_mit_schueler_in_fach(final @NotNull KursblockungDynSchueler that, final long idFach) {
		final @NotNull KursblockungDynFachart fachart1 = this.gibFachartZuFachID(idFach);
		final @NotNull KursblockungDynFachart fachart2 = that.gibFachartZuFachID(idFach);

		if (fachart1.gibNr() != fachart2.gibNr())
			throw new DeveloperNotificationException("Regel 12:" + representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2
					+ " haben nicht die selbe Kursart!");

		fachart1.regel_schueler_verbieten_mit_schueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler bei gemeinsamen Kursen zusammen in einem Kurs landen sollen.
	 *
	 * @param that  Der übergebene Schüler.
	 */
	public void regel_13_zusammen_mit_schueler(final @NotNull KursblockungDynSchueler that) {
		for (final @NotNull KursblockungDynFachart fachart1 : fachartArr)
			for (final @NotNull KursblockungDynFachart fachart2 : fachartArr)
				if (fachart1.gibNr() == fachart2.gibNr())
					fachart1.regel_schueler_zusammen_mit_schueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler bei gemeinsamen Kursen, nicht zusammen in einem Kurs landen sollen.
	 *
	 * @param that  Der übergebene Schüler.
	 */
	void regel_14_verbieten_mit_schueler(final @NotNull KursblockungDynSchueler that) {
		for (final @NotNull KursblockungDynFachart fachart1 : fachartArr)
			for (final @NotNull KursblockungDynFachart fachart2 : fachartArr)
				if (fachart1.gibNr() == fachart2.gibNr())
					fachart1.regel_schueler_verbieten_mit_schueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dasss der Schüler bei der Blockung nicht auf Kurse verteilt werden soll.
	 */
	void regel_16_sperre() {
		for (int i = 0; i < schieneBelegt.length; i++)
			schieneBelegt[i] = true;
	}

	private void aktionZustandLaden(final @NotNull KursblockungDynKurs[] wahl) {
		aktionKurseAlleEntfernen();

		for (int i = 0; i < fachartZuKurs.length; i++) {
			final KursblockungDynKurs kurs = wahl[i];

			if (kurs != null) {
				if (kurs.gibIstErlaubtFuerSchueler(this))
					aktionKursHinzufuegen(i, kurs);
				else
					throw new DeveloperNotificationException("FEHLER: Schüler " + guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.");
			}
		}
	}

	private void aktionKursHinzufuegen(final int fachartIndex, final @NotNull KursblockungDynKurs kurs) {
		kurs.aktionSchuelerHinzufuegen(internalSchuelerID);
		statistik.aktionNichtwahlenVeraendern(-1);
		nichtwahlen--;
		for (final int nr : kurs.gibSchienenLage()) {
			DeveloperNotificationException.ifTrue("FEHLER: Schienen-Doppelbelegung! " + representation, schieneBelegt[nr]);
			schieneBelegt[nr] = true;
		}
		fachartZuKurs[fachartIndex] = kurs;
	}

	private void aktionKursEntfernen(final int fachartIndex, final @NotNull KursblockungDynKurs kurs) {
		kurs.aktionSchuelerEntfernen(internalSchuelerID);
		statistik.aktionNichtwahlenVeraendern(+1);
		nichtwahlen++;
		for (final int nr : kurs.gibSchienenLage()) {
			DeveloperNotificationException.ifTrue("FEHLER: Kurs ist gar nicht in Schiene ! " + representation, !schieneBelegt[nr]);
			schieneBelegt[nr] = false;
		}
		fachartZuKurs[fachartIndex] = null;
	}

}
