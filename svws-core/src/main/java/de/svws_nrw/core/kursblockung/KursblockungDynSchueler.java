package de.svws_nrw.core.kursblockung;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Schüler-Objekt (während des Blockungsvorgangs).
 *
 * @author Benjamin A. Bartsch
 */
public class KursblockungDynSchueler {


	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random rnd;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger log;

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

	/** Soll der Schüler ignoriert werden beim Verteilen?  */
	boolean regel16schuelerIgnorieren;

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
	KursblockungDynSchueler(final @NotNull Logger logger, final @NotNull Random random, final long schuelerID,
			final @NotNull KursblockungDynStatistik statistik, final int schienenAnzahl, final int kursAnzahl, final int internalID) {
		this.rnd = random;
		this.log = logger;
		this.guiID = schuelerID;
		this.internalSchuelerID = internalID;
		this.representation = "Schüler " + schuelerID;
		this.statistik = statistik;
		this.fachartArr = new KursblockungDynFachart[0];
		this.fachartZuKurs = new KursblockungDynKurs[0];
		this.fachartZuKursSaveS = new KursblockungDynKurs[0];
		this.fachartZuKursSaveK = new KursblockungDynKurs[0];
		this.fachartZuKursSaveG = new KursblockungDynKurs[0];
		this.nichtwahlen = 0;
		this.schieneBelegt = new boolean[schienenAnzahl];
		this.kursGesperrt = new boolean[kursAnzahl];
		this.regel16schuelerIgnorieren = false;
		this.matrix = new KursblockungMatrix(rnd, 0, 0);
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
		for (final @NotNull KursblockungDynFachart fachart : fachartArr) {
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
	public boolean gibIstInKurs(final KursblockungDynKurs kurs) {
		for (final KursblockungDynKurs zugeordneterKurs : fachartZuKurs) {
			if (zugeordneterKurs == kurs) {
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
	boolean gibIstKursFuerSchuelerWaehlbar(final @NotNull KursblockungDynKurs kurs) {
		if (!kurs.gibIstErlaubtFuerSchueler(this)) {
			return false;
		}

		for (final int nrSchiene : kurs.gibSchienenLage()) {
			if (schieneBelegt[nrSchiene]) {
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
	private @NotNull KursblockungDynFachart gibFachartZuFachID(final long idFach) {
		for (final @NotNull KursblockungDynFachart fachart : fachartArr) {
			if (fachart.gibFach().id == idFach) {
				return fachart;
			}
		}

		throw new DeveloperNotificationException(representation + " hat kein Fach mit ID = " + idFach + "!");
	}

	// ########################################
	// ########## AKTIONEN / SETTER ###########
	// ########################################

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
		for (int i = 1; i < nFacharten; i++) {
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
		}

		matrix = new KursblockungMatrix(rnd, nFacharten, schieneBelegt.length);
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

				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					aktionKursHinzufuegen(i, kurs);
				} else {
					throw new DeveloperNotificationException("FEHLER: Schüler " + guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.");
				}
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
			if (kurs != null) {
				aktionKursEntfernen(i, kurs);
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
	private void aktionKursDerFachartSuchenUndVerteilen(final int iFachart, final boolean kurseZufaellig) {
		final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
		final @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();

		// Erzeuge die Zuordnung.
		final @NotNull int[] kursReihenfolge = new int[kurse.length];
		for (int i = 0; i < kursReihenfolge.length; i++) {
			kursReihenfolge[i] = i;
		}
		if (kurseZufaellig) {
			KursblockungStatic.aktionPermutiere(rnd, kursReihenfolge);
		}

		for (final int iKurs : kursReihenfolge) {
			final @NotNull KursblockungDynKurs kurs = kurse[iKurs];

			if (gibIstKursFuerSchuelerWaehlbar(kurs)) {
				aktionKursHinzufuegen(iFachart, kurs);
				return;
			}
		}
	}


	/**
	 * Geht die Facharten durch (zufällig) und geht dann pro Fachart alle Kurse durch (nicht zufällig).
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	void aktionKurseVerteilenZufaellig() {
		final @NotNull int[] permFachart = KursblockungStatic.gibPermutation(rnd, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			final int iFachart = permFachart[pFachart];

			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Einen guten Kurs der Fachart suchen und wenn möglich verteilen.
			aktionKursDerFachartSuchenUndVerteilen(iFachart, false);
		}
	}

	/**
	 * Geht die Multikurs-Facharten durch (zufällig) und geht dann pro Fachart alle Kurse durch (zufällig).
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	void aktionKurseVerteilenNurMultikurseZufaellig() {
		final @NotNull int[] permFachart = KursblockungStatic.gibPermutation(rnd, fachartArr.length);

		for (int pFachart = 0; pFachart < fachartArr.length; pFachart++) {
			final int iFachart = permFachart[pFachart];

			// Bereits belegte Facharten überspringen.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Wenn die Fachart Multikurse hat, einen zufälligen Kurs suchen und verteilen.
			final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
			if (fachart.gibHatMultikurs()) {
				aktionKursDerFachartSuchenUndVerteilen(iFachart, true); // Kursreihenfolge zufällig.
			}
		}
	}

	/**
	 * Geht die Facharten durch (nicht zufällig) und überprüft, ob der Schüler nur einen erlaubten Kurs hat.
	 * Falls der Kurs wählbar ist, wird der Schüler hinzugefügt und es geht weiter mit der nächsten Fachart.
	 */
	void aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs() {
		for (int iFachart = 0; iFachart < fachartArr.length; iFachart++) {
			// Bereits belegte Facharten ignorieren.
			if (fachartZuKurs[iFachart] != null) {
				continue;
			}

			// Zähle alle Kurse der Fachart, die der Schüler potentiell wählen kann.
			final @NotNull KursblockungDynFachart fachart = fachartArr[iFachart];
			final @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse();
			int kursMoeglichkeiten = 0;
			for (final @NotNull KursblockungDynKurs kurs : kurse) {
				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					kursMoeglichkeiten++;
				}
			}

			// Versuche den (einen) Kurs zu verteilen.
			if (kursMoeglichkeiten == 1) {
				aktionKursDerFachartSuchenUndVerteilen(iFachart, false);
			}
		}
	}


	private void aktionKurseVerteilenMitBipartiteMatchingGewichtetetInitialisierung(final long wertUngueltig) {
		final @NotNull long @NotNull [][] data = matrix.getMatrix();

		for (int r = 0; r < fachartArr.length; r++) {
			// Die Zeile muss gefüllt werden, bevor sie potentiell übersprungen wird.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = wertUngueltig;
			}

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Bewerte die Fachart r, falls die Schiene c nicht belegt ist.
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (schieneBelegt[c]) {
					continue;
				}
				final KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
				if (kurs != null) {
					data[r][c] = kurs.gibGewichtetesMatchingBewertung();
				}
			}
		}
	}


	private void aktionKurseVerteilenMitBipartiteMatchingInitialisierung(final long wertUngueltig) {
		final @NotNull long @NotNull [][] data = matrix.getMatrix();

		for (int r = 0; r < fachartArr.length; r++) {
			// Die Zeile muss gefüllt werden, bevor sie potentiell übersprungen wird.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = wertUngueltig;
			}

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Bewerte die Fachart r, falls die Schiene c nicht belegt ist.
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (schieneBelegt[c]) {
					continue;
				}
				final KursblockungDynKurs kurs = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
				if (kurs != null) {
					data[r][c] = 1; // Kante existiert.
				}
			}
		}
	}


	private void aktionKurseVerteilenNachDeinemWunschInitialisierung(final long wertUngueltig, final long wertGewaehlt, final long wertWandern) {
		final @NotNull long @NotNull [][] data = matrix.getMatrix();

		for (int r = 0; r < fachartArr.length; r++) {
			// Die Zeile muss gefüllt werden, bevor sie potentiell übersprungen wird.
			for (int c = 0; c < schieneBelegt.length; c++) {
				data[r][c] = wertUngueltig;
			}

			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Bewerte die Fachart r, falls die Schiene c nicht belegt ist.
			for (int c = 0; c < schieneBelegt.length; c++) {
				if (schieneBelegt[c]) {
					continue;
				}
				if (fachartArr[r].gibHatSchuelerPotentiellenKursInSchiene(c, this)) {
					data[r][c] = wertGewaehlt;
				} else if (fachartArr[r].gibHatSchuelerKursMitFreierSchiene(c, this)) {
					data[r][c] = wertWandern;
				}
			}
		}
	}


	private void aktionKurseVerteilenMitMatchingZuordnen(final long wertUngueltig, final @NotNull int[] r2c) {
		final @NotNull long @NotNull [][] data = matrix.getMatrix();

		for (int r = 0; r < fachartArr.length; r++) {
			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Gibt es einen  Matching-Partner und ist die Zuordnung zudem gültig?
			final int c = r2c[r];
			if ((c >= 0) && (data[r][c] != wertUngueltig)) {
				final KursblockungDynKurs kursGefunden = fachartArr[r].gibKleinstenKursInSchieneFuerSchueler(c, this);
				if (kursGefunden != null) {
					aktionKursHinzufuegen(r, kursGefunden);
				} else {
					// Besser keine Exception werfen, damit der Worker-Thread der GUI weiter machen kann.
					log.logLn(LogLevel.ERROR, "Fehler bei der Zuordnung im gewichteten Matching. Kein Kurs für Zelle (r=%d, c=%d) gefunden!"
							.formatted(r, c));
				}
			}
		}
	}


	private boolean aktionKurseVerteilenNachDeinemWunschZuordnen(final long wertWandern, final @NotNull int[] r2c) {
		final @NotNull long @NotNull [][] data = matrix.getMatrix();

		boolean kurslageHatSichVeraendert = false;
		for (int r = 0; r < fachartArr.length; r++) {
			// Überspringe, falls bereits zugeordnet oder die Fachart über mehrere Schienen geht.
			if ((fachartZuKurs[r] != null) || fachartArr[r].gibHatMultikurs()) {
				continue;
			}

			// Gibt es einen Matching-Partner und soll ein Kurs verschoben werden?
			final int c = r2c[r];
			if ((c >= 0) && (data[r][c] == wertWandern)) {
				fachartArr[r].aktionZufaelligerKursWandertNachSchiene(c);
				kurslageHatSichVeraendert = true;
			}

		}

		return kurslageHatSichVeraendert;
	}


	/**
	 * Verteilt alle Kurse mit Hilfe eines gewichteten Matching Algorithmus. Kleinere Kurse werden in der Wahl bevorzugt.
	 * <br>Hinweis: Multikurse werden beim bipartiten Matching nie verteilt, da dies algorithmisch nicht geht.
	 */
	void aktionKurseVerteilenMitBipartiteMatchingGewichtet() {
		// Wegen Berechnungen darf der Wert nicht in der Nähe von INTEGER.MAX_VALUE sein.
		final long _MATRIX_UNGUELTIG = 1000000;

		// Initialisieren
		aktionKurseVerteilenMitBipartiteMatchingGewichtetetInitialisierung(_MATRIX_UNGUELTIG);

		// Matching berechnen.
		final @NotNull int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Zuordnen
		aktionKurseVerteilenMitMatchingZuordnen(_MATRIX_UNGUELTIG, r2c);
	}


	/**
	 * Verteilt alle Kurse mit Hilfe eines bipartiten 0-1-Matching Algorithmus (ungewichtet). Kleinere Kurse werden in der Wahl bevorzugt.
	 * <br>Hinweis: Multikurse werden beim bipartiten Matching nie verteilt, da dies algorithmisch nicht geht.
	 */
	void aktionKurseVerteilenMitBipartiteMatching() {
		// Wert für eine fehlende Kante.
		final long _MATRIX_KEINE_KANTE = 0;

		// Matrix füllen.
		aktionKurseVerteilenMitBipartiteMatchingInitialisierung(_MATRIX_KEINE_KANTE);

		// Matching berechnen.
		final @NotNull int[] r2c = matrix.gibMaximalesBipartitesMatching(true);

		// Zuordnen
		aktionKurseVerteilenMitMatchingZuordnen(_MATRIX_KEINE_KANTE, r2c);
	}


	/**
	 * Alle Facharten (ohne Multikurse) des Schülers werden auf Schienen gematched.
	 * Falls dies nicht klappt, wird der Fachart gesagt, dass einer ihrer Kurse die Schiene wechseln muss.
	 * Um welche Schiene es sich dabei handelt, wird durch den Matching-Algorithmus berechnet.
	 *
	 * <br>Hinweis: Der Schüler wird bei den Berechnungen nicht einem Kurs hinzugefügt!
	 *
	 * @return TRUE, falls sich die Lage der Kurse verändert hat.
	 */
	boolean aktionKurseVerteilenNachDeinemWunsch() {
		// Wert für eine ungültige Wahl.
		final long _WERT_UNGUELTIG = 1000000;

		// Wert für einen Kurs, den der Schüler wählen könnte.
		final long _WERT_KURS_GEWAEHLT = 0;

		// Wert für einen Kurs, den der Schüler wählen könnte, nachdem er gewandert ist.
		final long _WERT_KURS_MUSS_WANDERN = 1;

		// Matrix füllen.
		aktionKurseVerteilenNachDeinemWunschInitialisierung(_WERT_UNGUELTIG, _WERT_KURS_GEWAEHLT, _WERT_KURS_MUSS_WANDERN);

		// Matching berechnen.
		final @NotNull int[] r2c = matrix.gibMinimalesBipartitesMatchingGewichtet(true);

		// Zuordnen
		return aktionKurseVerteilenNachDeinemWunschZuordnen(_WERT_KURS_MUSS_WANDERN, r2c);
	}


	// ########################################
	// ########## PRIVATE METHODEN ############
	// ########################################

	/**
	 * Ausgabe der aktuellen Kurslage zum debuggen.
	 */
	void debugKurswahlen() {
		log.modifyIndent(+4);
		log.logLn("");
		log.logLn(representation);
		final HashSet<Integer> setSchienenLage = new HashSet<>();
		for (final KursblockungDynKurs kurs : fachartZuKurs) {
			if (kurs == null) {
				continue;
			}
			log.logLn("    " + kurs.toString() + "    " + Arrays.toString(kurs.gibSchienenLage()));
			for (final int schiene : kurs.gibSchienenLage()) {
				if (!setSchienenLage.add(schiene)) {
					log.logLn("Kollision");
					return;
				}
			}
		}
		log.modifyIndent(-4);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	void setzeZusammenMitSchuelerInFach(final @NotNull KursblockungDynSchueler that, final long idFach) {
		final @NotNull KursblockungDynFachart fachart1 = this.gibFachartZuFachID(idFach);
		final @NotNull KursblockungDynFachart fachart2 = that.gibFachartZuFachID(idFach);

		if (fachart1.gibNr() != fachart2.gibNr()) {
			throw new DeveloperNotificationException("Regel 11:" + representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2
					+ " haben nicht die selbe Kursart!");
		}

		fachart1.setzeSchuelerZusammenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler nicht zusammen sein sollen im übergebenen Fach.
	 *
	 * @param that    Der übergebene Schüler.
	 * @param idFach  Die Datenbank-ID des Faches.
	 */
	void setzeVerbietenMitSchuelerInFach(final @NotNull KursblockungDynSchueler that, final long idFach) {
		final @NotNull KursblockungDynFachart fachart1 = this.gibFachartZuFachID(idFach);
		final @NotNull KursblockungDynFachart fachart2 = that.gibFachartZuFachID(idFach);

		if (fachart1.gibNr() != fachart2.gibNr()) {
			throw new DeveloperNotificationException("Regel 12:" + representation + " bei " + fachart1 + " und " + that.representation + " bei " + fachart2
					+ " haben nicht die selbe Kursart!");
		}

		fachart1.setzeSchuelerVerbietenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
	}

	/**
	 * Wendet an, dass dieser Schüler und der übergebene Schüler bei gemeinsamen Kursen zusammen in einem Kurs landen sollen.
	 *
	 * @param that  Der übergebene Schüler.
	 */
	public void setzeZusammenMitSchueler(final @NotNull KursblockungDynSchueler that) {
		for (final @NotNull KursblockungDynFachart fachart1 : fachartArr) {
			for (final @NotNull KursblockungDynFachart fachart2 : fachartArr) {
				if (fachart1.gibNr() == fachart2.gibNr()) {
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
	void setzeVerbietenMitSchueler(final @NotNull KursblockungDynSchueler that) {
		for (final @NotNull KursblockungDynFachart fachart1 : fachartArr) {
			for (final @NotNull KursblockungDynFachart fachart2 : fachartArr) {
				if (fachart1.gibNr() == fachart2.gibNr()) {
					fachart1.setzeSchuelerVerbietenMitSchueler(this.internalSchuelerID, that.internalSchuelerID);
				}
			}
		}
	}

	/**
	 * Wendet an, dass der Schüler bei der Blockung nicht auf Kurse verteilt werden soll.
	 */
	void setzeSperreBeiKursverteilung() {
		for (int i = 0; i < schieneBelegt.length; i++) {
			schieneBelegt[i] = true;
		}
		regel16schuelerIgnorieren = true;
	}

	private void aktionZustandLaden(final @NotNull KursblockungDynKurs[] wahl) {
		aktionKurseAlleEntfernen();

		for (int i = 0; i < fachartZuKurs.length; i++) {
			final KursblockungDynKurs kurs = wahl[i];

			if (kurs != null) {
				if (kurs.gibIstErlaubtFuerSchueler(this)) {
					aktionKursHinzufuegen(i, kurs);
				} else {
					throw new DeveloperNotificationException("FEHLER: Schüler " + guiID + " darf den Kurs " + kurs.gibDatenbankID() + " nicht wählen.");
				}
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

	/**
	 * Versucht den S. in den Kurs zu setzen. Entfernt ggf. einen anderen Kurs der selben Fachart dafür.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 */
	public void aktionKursSetzen(final int idKursDB) {
		for (int fachartIndex = 0; fachartIndex < fachartArr.length; fachartIndex++) {
			final @NotNull KursblockungDynFachart fachart = fachartArr[fachartIndex];
			for (final @NotNull KursblockungDynKurs kurs : fachart.gibKurse()) {
				if ((kurs.gibDatenbankID() == idKursDB) && (kurs.gibIstErlaubtFuerSchueler(this))) {
					final KursblockungDynKurs kursVorher = fachartZuKurs[fachartIndex];
					if (kursVorher != null) {
						aktionKursEntfernen(fachartIndex, kursVorher);
					}
					aktionKursHinzufuegen(fachartIndex, kurs);
				}
			}
		}
	}

	/**
	 * Versucht den S. aus dem Kurs zu entfernen.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 */
	public void aktionKursEntfernen(final int idKursDB) {
		for (int fachartIndex = 0; fachartIndex < fachartArr.length; fachartIndex++) {
			final @NotNull KursblockungDynFachart fachart = fachartArr[fachartIndex];
			for (final @NotNull KursblockungDynKurs kurs : fachart.gibKurse()) {
				if (kurs.gibDatenbankID() == idKursDB) {
					final KursblockungDynKurs kursVorher = fachartZuKurs[fachartIndex];
					if (kursVorher != null) {
						aktionKursEntfernen(fachartIndex, kursVorher);
					}
				}
			}
		}
	}
}
