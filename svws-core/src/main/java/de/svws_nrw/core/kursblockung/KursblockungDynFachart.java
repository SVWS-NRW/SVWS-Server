package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Fachart ist eine Kombination aus Fach und Kursart. Kurse sind genau einer Fachart zugeordnet.
 * Ein Schüler ist mehreren Facharten zugeordnet (entsprechend der Anzahl seiner Fachwahlen).
 * Ein Objekt dieser Klasse kennt alle seine Kurse und speichert die Anzahl an SuS die der Fachart zugeordnet wurden, deren Fachwahl also erfüllt wurde.
 *
 * @author Benjamin A. Bartsch
 */
public class KursblockungDynFachart {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Eine laufende Nummer der Fachart. */
	private final int nr;

	/** Referenz zum zugehörigen GOST-Fach. */
	private final @NotNull GostFach gostFach;

	/** Referenz zur zugehörigen GOST-Kursart. */
	private final @NotNull GostKursart gostKursart;

	/** Ein Array aller Kurse dieser Fachart. Das Array bleibt dynamisch sortiert, so dass im Array zunächst der Kurs
	 * mit der geringsten Schüleranzahl ist. */
	private @NotNull KursblockungDynKurs @NotNull [] kursArr;

	/** Die maximale Anzahl an Schülern, die dieser Fachart zugeordnet sein können. Also die Anzahl der Schüler, die diese Fachart gewählt haben. */
	private int schuelerMax;

	/** Die maximale Anzahl an Kursen, die dieser Fachart zugeordnet sein können. */
	private int kurseMax;

	/** Die aktuelle Anzahl an Schülern, die dieser Fachart zugeordnet sind. */
	private int schuelerAnzNow;

	/** Dem Statistik-Objekt wird eine Veränderung der Kursdifferenz mitgeteilt. */
	private final @NotNull KursblockungDynStatistik statistik;


	private final @NotNull int[][] regelverletzungSchuelerpaarBeimHinzufuegen;
	// TODO regelHinzufuegenNichtZusammen(S1, S2) --> matrix += 1, Verletzungen += 0  (man startet mit ohne  Regelverletzung)
	// TODO regelHinzufuegenZusammen(S1, S2)      --> matrix -= 1, Verletzungen += 1  (man startet mit einer Regelverletzung)

	/**
	 * @param pRandom      Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pNr          Eine laufende Nummer (ID) für alle Facharten.
	 * @param pGostFach    Referenz zum zugehörigen GOST-Fach.
	 * @param pGostKursart Referenz zur zugehörigen GOST-Kursart.
	 * @param pStatistik   Dem Statistik-Objekt wird eine Veränderung der Kursdifferenz mitgeteilt.
	 */
	KursblockungDynFachart(
					final @NotNull Random pRandom,
					final int pNr,
					final @NotNull GostFach pGostFach,
					final @NotNull GostKursart pGostKursart,
					final @NotNull KursblockungDynStatistik pStatistik) {
		_random = pRandom;
		nr = pNr;
		gostFach = pGostFach;
		gostKursart = pGostKursart;
		statistik = pStatistik;
		kursArr = new KursblockungDynKurs[0];
		kurseMax = 0;
		schuelerMax = 0;
		schuelerAnzNow = 0;
		regelverletzungSchuelerpaarBeimHinzufuegen = new int[100][100];
	}

	/**
	 * Durch das Überschreiben dieser Methode, liefert dieses Objekt eine automatische String-Darstellung, beispielsweise 'D;LK'.
	 */
	@Override
	public @NotNull String toString() {
		return gostFach.kuerzel + ";" + gostKursart.kuerzel;
	}

	// ########################################
	// ################ GETTER ################
	// ########################################

	/**
	 * Liefert die interne Nummer dieser Fachart.
	 *
	 * @return Die interne Nummer dieser Fachart.
	 */
	int gibNr() {
		return nr;
	}

	/**
	 * Liefert das zugehörige Fach-Objekt.
	 *
	 * @return Das zugehörige Fach-Objekt.
	 */
	@NotNull GostFach gibFach() {
		return gostFach;
	}

	/**
	 * Liefert das zugehörige Kursart-Objekt.
	 *
	 * @return Das zugehörige Kursart-Objekt.
	 */
	@NotNull GostKursart gibKursart() {
		return gostKursart;
	}

	/**
	 * Liefert die Anzahl der SuS, die diese Fachart gewählt haben.
	 *
	 * @return die Anzahl der SuS, die diese Fachart gewählt haben.
	 */
	int gibSchuelerMax() {
		return schuelerMax;
	}

	/**
	 * Liefert die aktuelle Anzahl an SuS, die dieser Fachart zugeordnet sind.
	 *
	 * @return die aktuelle Anzahl an SuS, die dieser Fachart zugeordnet sind.
	 */
	int gibSchuelerZordnungen() {
		return schuelerAnzNow;
	}

	/**
	 * Liefert die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 *
	 * @return die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 */
	int gibKurseMax() {
		return kurseMax;
	}

	/**
	 * Liefert die aktuell größte Kursdifferenz.
	 * <br>Das ist: Größter Kurs - Kleinster Kurs
	 *
	 * @return die aktuell größte Kursdifferenz.
	 */
	int gibKursdifferenz() {
		return kursArr[kursArr.length - 1].gibSchuelerAnzahl() - kursArr[0].gibSchuelerAnzahl();
	}

	/**
	 * Liefert das Array aller Kurse dieser Fachart.
	 * <br>Die Kurse sind aufsteigend nach ihrer Schüleranzahl sortiert.
	 *
	 * @return Das Array aller Kurse dieser Fachart.
	 */
	@NotNull KursblockungDynKurs @NotNull [] gibKurse() {
		return kursArr;
	}

	/**
	 * Liefert den Kurs mit der geringsten SuS-Anzahl, welcher in Schiene vorkommt.
	 * <br>Ignoriert Kurse, die bereits voll sind (Regel: max. SuS).
	 * <br>Ignoriert Kurse, die für den aktuellen Schüler gesperrt sind.
	 *
	 * @param  pSchiene      Die Schiene, in der gesucht wird.
	 * @param  kursGesperrt  Definiert, alle Kurse des S. die gesperrt sind und somit ignoriert werden sollen.
	 *
	 * @return den Kurs mit der geringsten SuS-Anzahl, welcher in Schiene vorkommt.
	 */
	KursblockungDynKurs gibKleinstenKursInSchieneFuerSchueler(final int pSchiene, final @NotNull boolean[] kursGesperrt) {
		for (int i = 0; i < kursArr.length; i++) {
			final @NotNull KursblockungDynKurs kurs = kursArr[i];
			if (kurs.gibIstErlaubtFuerSchueler(kursGesperrt))
				for (final int c : kurs.gibSchienenLage()) // Suche passende Schiene.
					if (c == pSchiene)
						return kurs;
		}
		return null;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 */
	boolean gibHatMultikurs() {
		for (final @NotNull KursblockungDynKurs kurs : kursArr)
			if (kurs.gibSchienenAnzahl() > 1)
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 *
	 * @param  pSchiene      Die Schiene, die angefragt wurde.
	 * @param  kursGesperrt  Alle Kurssperrungen des Schülers.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 */
	boolean gibHatKursInSchiene(final int pSchiene, final @NotNull boolean[] kursGesperrt) {
		for (final @NotNull KursblockungDynKurs kurs : kursArr)
			if (kurs.gibIstErlaubtFuerSchueler(kursGesperrt) &&  kurs.gibIstInSchiene(pSchiene))
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 *
	 * @param  pSchiene     D ie Schiene, die angefragt wurde.
	 * @param  kursGesperrt  Alle Kurssperrungen des Schülers.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 */
	boolean gibHatKursMitFreierSchiene(final int pSchiene, final @NotNull boolean[] kursGesperrt) {
		for (final @NotNull KursblockungDynKurs kurs : kursArr)
			if (kurs.gibIstErlaubtFuerSchueler(kursGesperrt) &&  kurs.gibIstSchieneFrei(pSchiene))
					return true;

		return false;
	}

	// ########################################
	// ########## AKTIONEN / SETTER ###########
	// ########################################

	/**
	 * Ordnet alle Kurse der Fachart zu. Die Kurse haben noch keine SuS und sind somit automatisch sortiert.
	 *
	 * @param pKursArr  Alle Kurse der Fachart.
	 */
	void aktionSetKurse(final @NotNull KursblockungDynKurs @NotNull [] pKursArr) {
		kursArr = pKursArr;
	}

	/**
	 * Erhöht die Anzahl ({@link #schuelerMax}) an SuS, die diese Fachart gewählt haben um 1.
	 */
	void aktionMaxSchuelerErhoehen() {
		schuelerMax++;
	}

	/**
	 * Erhöht die Anzahl ({@link #kurseMax}) an Kursen, die zu dieser Fachart gehören.
	 */
	void aktionMaxKurseErhoehen() {
		kurseMax++;
	}

	/**
	 * Muss aufgerufen werden, bevor die Schüleranzahl eines Kurses verändert wird.
	 */
	void aktionKursdifferenzEntfernen() {
		statistik.aktionKursdifferenzEntfernen(gibKursdifferenz());
	}

	/**
	 * Muss aufgerufen werden, nachdem die Schüleranzahl eines Kurses verändert wird.
	 */
	void aktionKursdifferenzHinzufuegen() {
		statistik.aktionKursdifferenzHinzufuegen(gibKursdifferenz());
	}

	/**
	 * Erhöht die Anzahl ({@link #schuelerAnzNow}) an Schülern, die dieser Fachart momentan zugeordnet sind um 1.
	 * Da ein (bestimmter) Kurs nun einen S. mehr hat, muss das Array einmalig von links nach rechts sortiert werden.
	 */
	void aktionSchuelerWurdeHinzugefuegt() {
		schuelerAnzNow++;
		// Ein Kurs hat +1 SuS --> Sortiere 'kursArr' von links nach rechts.
		// Beispiel 11[3]223
		for (int i = 1; i < kursArr.length; i++) {
			final @NotNull KursblockungDynKurs kursL = kursArr[i - 1];
			final @NotNull KursblockungDynKurs kursR = kursArr[i];
			final boolean b1 = kursL.gibSchuelerAnzahl() > kursR.gibSchuelerAnzahl();
			final boolean b2 = (kursL.gibSchuelerAnzahl() == kursR.gibSchuelerAnzahl())
					&& (kursL.gibDatenbankID() > kursR.gibDatenbankID());
			if (b1 || b2) {
				kursArr[i - 1] = kursR;
				kursArr[i] = kursL;
			}
		}
	}

	/**
	 * Verringert die Anzahl ({@link #schuelerAnzNow}) an SuS, die dieser Fachart momentan zugeordnet sind um 1.
	 * Da ein (bestimmter) Kurs nun einen S. weniger hat, muss das Array einmalig von rechts nach links sortiert werden.
	 */
	void aktionSchuelerWurdeEntfernt() {
		schuelerAnzNow--;
		// Ein Kurs hat -1 SuS --> Sortiere 'kursArr' von rechts nach links.
		// Beispiel 1122[1]3
		for (int i = kursArr.length - 1; i >= 1; i--) {
			final @NotNull KursblockungDynKurs kursL = kursArr[i - 1];
			final @NotNull KursblockungDynKurs kursR = kursArr[i];
			final boolean b1 = kursL.gibSchuelerAnzahl() > kursR.gibSchuelerAnzahl();
			final boolean b2 = (kursL.gibSchuelerAnzahl() == kursR.gibSchuelerAnzahl())
					&& (kursL.gibDatenbankID() > kursR.gibDatenbankID());
			if (b1 || b2) {
				kursArr[i - 1] = kursR;
				kursArr[i] = kursL;
			}
		}
	}

	/**
	 * Lässt einen zufälligen Kurs dieser Fachart in die angegebene Schiene wandern.
	 *
	 * @param pSchiene  Die Schiene, in die einer Kurs der Fachart wandern soll.
	 */
	void aktionZufaelligerKursWandertNachSchiene(final int pSchiene) {
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, kursArr.length);

		for (int p = 0; p < perm.length; p++) {
			final KursblockungDynKurs kurs = kursArr[perm[p]];
			if (kurs.gibIstSchieneFrei(pSchiene)) {
				kurs.aktionSetzeInSchiene(pSchiene);
				return;
			}
		}

		throw new DeveloperNotificationException("aktionZufaelligerKursWandertNachSchiene(" + pSchiene + ")");
	}

	/**
	 * Debug Ausgabe. Nur für Testzwecke.
	 *
	 * @param schuelerArr  Das Array mit den Schülerdaten.
	 */
	void debug(final @NotNull KursblockungDynSchueler @NotNull [] schuelerArr) {
		for (int i = 0; i < kursArr.length; i++)
			kursArr[i].debug(schuelerArr);
	}

}
