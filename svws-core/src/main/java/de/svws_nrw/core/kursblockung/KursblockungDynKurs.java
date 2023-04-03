package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Ein Kurs-Objekt (während des Blockungsvorganges).
 *
 * @author Benjamin A. Bartsch */
public class KursblockungDynKurs {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Die ID des Kurses, welche von der Datenbank kommt. */
	private final long databaseID;

	/** Die interne ID des Kurses. */
	private final int internalID;

	/** Die Fachart die zu diesem Kurs gehört. */
	private final @NotNull KursblockungDynFachart fachart;

	/** Schienen in denen der Kurs gerade ist, zum Teil fixiert. */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenLage;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenLageSaveS;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenLageSaveK;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenLageSaveG;

	/** Anzahl der Fixierungen. */
	private final int schienenLageFixiert;

	/** Schienen die getauscht werden könnten. */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenFrei;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenFreiSaveS;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenFreiSaveK;

	/** Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei} */
	private final @NotNull KursblockungDynSchiene @NotNull [] schienenFreiSaveG;

	/** Die Anzahl an SuS in diesem Kurs. */
	private int schuelerAnz;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger logger;

	/** Der Kurs wählt eine zufällige Schienenlage und fügt sich selbst den entsprechenden Schienen hinzu.
	 *
	 * @param pRandom              Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pSchienenLage        Ein Array aller Schienen, in denen der Kurs gerade liegt.
	 * @param pSchienenLageFixiert Anzahl an Schienen in {@code pSchienenLage}, die fixiert ist.
	 * @param pSchienenFrei        Ein Array aller Schienen, in die der Kurs wechseln könnte.
	 * @param pKursID              Die ID des Kurses.
	 * @param pFachart             Die zu diesem Kurs zugehörige Fachart.
	 * @param pLogger              Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInternalID          Eine interne ID für schnellen Zugriff. */
	public KursblockungDynKurs(final @NotNull Random pRandom, final @NotNull KursblockungDynSchiene @NotNull [] pSchienenLage,
			final int pSchienenLageFixiert, final @NotNull KursblockungDynSchiene @NotNull [] pSchienenFrei, final long pKursID,
			final @NotNull KursblockungDynFachart pFachart, final @NotNull Logger pLogger, final int pInternalID) {
		_random = pRandom;
		schienenLage = pSchienenLage;
		schienenLageFixiert = pSchienenLageFixiert;
		schienenFrei = pSchienenFrei;
		databaseID = pKursID;
		fachart = pFachart;
		schuelerAnz = 0;
		logger = pLogger;
		internalID = pInternalID;

		schienenLageSaveS = new KursblockungDynSchiene[schienenLage.length];
		schienenLageSaveK = new KursblockungDynSchiene[schienenLage.length];
		schienenLageSaveG = new KursblockungDynSchiene[schienenLage.length];
		for (int i = 0; i < schienenLage.length; i++) {
			schienenLageSaveS[i] = schienenLage[i];
			schienenLageSaveK[i] = schienenLage[i];
			schienenLageSaveG[i] = schienenLage[i];
		}

		schienenFreiSaveS = new KursblockungDynSchiene[schienenFrei.length];
		schienenFreiSaveK = new KursblockungDynSchiene[schienenFrei.length];
		schienenFreiSaveG = new KursblockungDynSchiene[schienenFrei.length];
		for (int i = 0; i < schienenFrei.length; i++) {
			schienenFreiSaveS[i] = schienenFrei[i];
			schienenFreiSaveK[i] = schienenFrei[i];
			schienenFreiSaveG[i] = schienenFrei[i];
		}

		// Der Schiene hinzufügen
		for (int i = 0; i < schienenLage.length; i++) {
			schienenLage[i].aktionKursHinzufuegen(this);
		}
	}

	/**
	 * Gibt die String-Repräsentation des Kurses zurück.
	 *
	 * @return die String-Repräsentation des Kurses
	 */
	@Override
	public @NotNull String toString() {
		return "Kurs (dbID=" + databaseID + ", intiD=" + internalID + ")";
	}

	// ########################################
	// ################ GETTER ################
	// ########################################

	/** Die Kurs-ID der GUI.
	 *
	 * @return Die Kurs-ID der GUI. */
	public long gibDatenbankID() {
		return databaseID;
	}

	/** Liefert die zum Kurs zugehörige Fachart.
	 *
	 * @return Die zum Kurs zugehörige Fachart. */
	public @NotNull KursblockungDynFachart gibFachart() {
		return fachart;
	}

	/** Liefert die aktuelle Anzahl an Schülern in diesem Kurs.
	 *
	 * @return Die aktuelle Anzahl an Schülern in diesem Kurs. */
	public int gibSchuelerAnzahl() {
		return schuelerAnz;
	}

	/** Liefert {@code true} falls die Schienenlage des Kurses noch veränderbar ist. Wenn der Kurs komplett fixiert ist,
	 * oder so viele Schienen gesperrt sind, dass der Kurs keine Wahlmöglichkeit bezüglich seiner Schienen hat, dann ist
	 * das Resultat {@code false}
	 *
	 * @return Liefert {@code true}, falls die Schienenlage des Kurses noch veränderbar ist. */
	public boolean gibHatFreiheitsgrade() {
		return (schienenLageFixiert < schienenLage.length) && (schienenFrei.length > 0);
	}

	/** Liefert die aktuelle Schienenlage dieses Kurses.
	 *
	 * @return Ein Array, das angibt, in welchen Schienen der Kurs ist. Die Werte sind 0-indiziert. */
	public @NotNull int[] gibSchienenLage() {
		final int length = schienenLage.length;
		final @NotNull int[] lage = new int[length];
		for (int i = 0; i < length; i++) {
			lage[i] = schienenLage[i].gibNr();
		}
		return lage;
	}

	/** Liefert die Anzahl an Schienen, die dieser Kurs belegt.
	 *
	 * @return Die Anzahl an Schienen, die dieser Kurs belegt. */
	public int gibSchienenAnzahl() {
		return schienenLage.length;
	}

	/** Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser.
	 *
	 * @return Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser. */
	public long gibGewichtetesMatchingBewertung() {
		return schuelerAnz * schuelerAnz;
		/*
		 * // Alle Kurse in sortierter Reihenfolge (am Anfang kleinere Kurse)
		 *
		 * @NotNull KursblockungDynKurs @NotNull [] kurse = fachart.gibKurse(); // Falls dieser Kurs die gleiche
		 * Schüleranzahl wie der kleinste Kurs hat, ist die Wahl optimal. if (schuelerAnz == kurse[0].schuelerAnz)
		 * return -1; // Falls dieser Kurs nicht die gleiche Schüleranzahl wie der größte Kurs hat, ist die Wahl okay.
		 * if (schuelerAnz != kurse[kurse.length-1].schuelerAnz) return 0; // Andernfalls erhöht sich die Kursdifferenz
		 * --> Schlecht! return 1;
		 */
	}

	/** Liefert TRUE, wenn die Schiene für den Kurs gesperrt wurde.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs gesperrt wurde. */
	boolean gibIstSchieneGesperrt(final int pSchiene) {
		for (final @NotNull KursblockungDynSchiene s : schienenLage) {
			if (s.gibNr() == pSchiene) {
				return false;
			}
		}
		for (final @NotNull KursblockungDynSchiene s : schienenFrei) {
			if (s.gibNr() == pSchiene) {
				return false;
			}
		}
		return true;
	}

	/** Liefert TRUE, wenn die Schiene für den Kurs fixiert wurde.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs fixiert wurde. */
	boolean gibIstSchieneFixiert(final int pSchiene) {
		for (int iLage = 0; iLage < schienenLageFixiert; iLage++) {
			if (schienenLage[iLage].gibNr() == pSchiene) {
				return true;
			}
		}
		return false;
	}

	/** Liefert TRUE, falls der Kurs gerade in Schiene pSchiene ist.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, falls der Kurs gerade in Schiene pSchiene ist. */
	boolean gibIstInSchiene(final int pSchiene) {
		for (int i = 0; i < schienenLage.length; i++)
			if (schienenLage[i].gibNr() == pSchiene)
				return true;
		return false;
	}

	/** Liefert TRUE, falls sich der Kurs in einer Schiene aus [schieneVon, schieneBis] befindet.
	 *
	 * @param  schieneVon Der Anfang des Intervalls (inklusive).
	 * @param  schieneBis Das Ende des Intervalls (inklusive).
	 * @return            TRUE, falls sich der Kurs in einer Schiene aus [schieneVon, schieneBis] befindet. */
	boolean gibIstImSchienenIntervall(final int schieneVon, final int schieneBis) {
		for (int i = 0; i < schienenLage.length; i++)
			if ((schienenLage[i].gibNr() >= schieneVon) && (schienenLage[i].gibNr() <= schieneBis))
				return true;
		return false;

	}

	/** Liefert TRUE, falls dieser Kurs in Schiene c wandern darf.
	 *
	 * @param  pSchiene Die Schiene, die angefragt wurde.
	 * @return          TRUE, falls dieser Kurs in Schiene c wandern darf. */
	public boolean gibIstSchieneFrei(final int pSchiene) {
		for (int i = 0; i < schienenFrei.length; i++) {
			if (schienenFrei[i].gibNr() == pSchiene) {
				return true;
			}
		}
		return false;
	}

	// ########################################
	// ########## AKTIONEN / SETTER ###########
	// ########################################

	/** Liefert die interne ID des Kurses.
	 *
	 * @return Die interne ID des Kurses. */
	int gibInternalID() {
		return internalID;
	}

	/** Speichert die aktuelle Lage der Schienen im Zustand S, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenS} zu laden. */
	public void aktionZustandSpeichernS() {
		System.arraycopy(schienenLage, 0, schienenLageSaveS, 0, schienenLage.length);
		System.arraycopy(schienenFrei, 0, schienenFreiSaveS, 0, schienenFrei.length);
	}

	/** Speichert die aktuelle Lage der Schienen im Zustand K, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenK} zu laden. */
	public void aktionZustandSpeichernK() {
		System.arraycopy(schienenLage, 0, schienenLageSaveK, 0, schienenLage.length);
		System.arraycopy(schienenFrei, 0, schienenFreiSaveK, 0, schienenFrei.length);
	}

	/** Speichert die aktuelle Lage der Schienen im Zustand G, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenG} zu laden. */
	public void aktionZustandSpeichernG() {
		System.arraycopy(schienenLage, 0, schienenLageSaveG, 0, schienenLage.length);
		System.arraycopy(schienenFrei, 0, schienenFreiSaveG, 0, schienenFrei.length);
	}

	/** Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernS} gespeicherte Lage der Schienen. */
	public void aktionZustandLadenS() {
		aktionSchienenLageEntfernen();
		System.arraycopy(schienenLageSaveS, 0, schienenLage, 0, schienenLage.length);
		System.arraycopy(schienenFreiSaveS, 0, schienenFrei, 0, schienenFrei.length);
		aktionSchienenLageHinzufuegen();
	}

	/** Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernK} gespeicherte Lage der Schienen. */
	public void aktionZustandLadenK() {
		aktionSchienenLageEntfernen();
		System.arraycopy(schienenLageSaveK, 0, schienenLage, 0, schienenLage.length);
		System.arraycopy(schienenFreiSaveK, 0, schienenFrei, 0, schienenFrei.length);
		aktionSchienenLageHinzufuegen();
	}

	/** Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernG} gespeicherte Lage der Schienen. */
	public void aktionZustandLadenG() {
		aktionSchienenLageEntfernen();
		System.arraycopy(schienenLageSaveG, 0, schienenLage, 0, schienenLage.length);
		System.arraycopy(schienenFreiSaveG, 0, schienenFrei, 0, schienenFrei.length);
		aktionSchienenLageHinzufuegen();
	}

	/** Verteilt den Kurs auf die Schienen zufällig. */
	public void aktionZufaelligVerteilen() {
		// Kurs kann nicht wandern? --> Abbruch
		if (!gibHatFreiheitsgrade()) {
			return;
		}
		// Sind SuS im Kurs? --> Fehler
		if (schuelerAnz > 0) {
			logger.log(LogLevel.ERROR,
					"Kurs.aktionZufaelligVerteilen: schuelerAnz > 0 (Ein Kurs mit SuS darf nicht verteilt werden)");
			return;
		}

		// Es gilt: (schienenLageFixiert < schienenLage.length)
		// ______&& (schienenFrei.length > 0)
		for (int i1 = schienenLageFixiert; i1 < schienenLage.length; i1++) {
			final int i2 = _random.nextInt(schienenFrei.length);
			final @NotNull KursblockungDynSchiene schiene1 = schienenLage[i1];
			final @NotNull KursblockungDynSchiene schiene2 = schienenFrei[i2];
			schiene1.aktionKursEntfernen(this);
			schiene2.aktionKursHinzufuegen(this);
			schienenLage[i1] = schiene2;
			schienenFrei[i2] = schiene1;
		}
	}

	/** Setzt die Lage des Kurses auf die in der Liste übergebenen Schienen.
	 *
	 * @param pSchienenWahl Die Schienen (0-indiziert), in denen der Kurs liegen soll. */
	public void aktionVerteileAufSchienen(final @NotNull LinkedCollection<@NotNull Integer> pSchienenWahl) {

		for (int iLage = schienenLageFixiert; iLage < schienenLage.length; iLage++) {
			final @NotNull KursblockungDynSchiene schieneL = schienenLage[iLage];
			if (pSchienenWahl.contains(schieneL.gibNr())) {
				continue;
			}
			// SchieneL muss raus.
			for (int iFrei = 0; iFrei < schienenFrei.length; iFrei++) {
				final @NotNull KursblockungDynSchiene schieneF = schienenFrei[iFrei];
				if (pSchienenWahl.contains(schieneF.gibNr())) {
					// Tauschpartner schieneF gefunden.
					schieneL.aktionKursEntfernen(this);
					schieneF.aktionKursHinzufuegen(this);
					schienenFrei[iFrei] = schieneL;
					schienenLage[iLage] = schieneF;
					break;
				}
			}

		}

	}

	/** Setzt die Lage des Kurses auf die übergebene Schiene.
	 *
	 * @param pSchiene Die Schiene (0-indiziert), in der der Kurs liegen soll. */
	void aktionSetzeInSchiene(final int pSchiene) {

		for (int iLage = schienenLageFixiert; iLage < schienenLage.length; iLage++) {
			// Bereits in Schiene? --> Abbruch
			final @NotNull KursblockungDynSchiene schieneL = schienenLage[iLage];
			if (schieneL.gibNr() == pSchiene) {
				return;
			}

			// Suche Tauschpartner-Schiene.
			for (int iFrei = 0; iFrei < schienenFrei.length; iFrei++) {
				final @NotNull KursblockungDynSchiene schieneF = schienenFrei[iFrei];
				if (pSchiene == schieneF.gibNr()) {
					// Tauschpartner schieneF gefunden.
					schieneL.aktionKursEntfernen(this);
					schieneF.aktionKursHinzufuegen(this);
					schienenFrei[iFrei] = schieneL;
					schienenLage[iLage] = schieneF;
					return;
				}
			}

		}

	}

	/** Entfernt einen Schüler aus diesem Kurs. */
	public void aktionSchuelerEntfernen() {
		fachart.aktionKursdifferenzEntfernen();
		schuelerAnz--; // Darf erst hier passieren.
		fachart.aktionSchuelerWurdeEntfernt(); // Sortiert das Kurs-Array der Fachart
		fachart.aktionKursdifferenzHinzufuegen();
	}

	/** Fügt einen Schüler diesem Kurs hinzu. */
	public void aktionSchuelerHinzufuegen() {
		fachart.aktionKursdifferenzEntfernen();
		schuelerAnz++; // Darf erst hier passieren.
		fachart.aktionSchuelerWurdeHinzugefuegt(); // Sortiert das Kurs-Array der Fachart
		fachart.aktionKursdifferenzHinzufuegen();
	}

	// ########################################
	// ########### PRIVATE METHODEN ###########
	// ########################################

	private void aktionSchienenLageHinzufuegen() {
		for (int i = 0; i < schienenLage.length; i++) {
			schienenLage[i].aktionKursHinzufuegen(this);
		}
	}

	private void aktionSchienenLageEntfernen() {
		for (int i = 0; i < schienenLage.length; i++) {
			schienenLage[i].aktionKursEntfernen(this);
		}
	}

	/** Debug Ausgabe. Nur für Testzwecke.
	 *
	 * @param schuelerArr Nötig, um den Kursen SuS zuzuordnen. */
	void debug(final @NotNull KursblockungDynSchueler @NotNull [] schuelerArr) {
		System.out.println(toString() + " --> " + schuelerAnz + " SuS.");
		for (final KursblockungDynSchueler s : schuelerArr) {
			final @NotNull KursblockungDynKurs[] kurse = s.gibKurswahlen();
			for (final KursblockungDynKurs kurs : kurse) {
				if (kurs == this) {
					System.out.println("        " + s.gibDatenbankID());
				}
			}
		}
	}

}
