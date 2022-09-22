package de.nrw.schule.svws.core.kursblockung;

import java.util.HashMap;

import de.nrw.schule.svws.core.adt.set.AVLSet;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Eine Schiene speichert alle aktuellen Kurse, die in dieser Schiene liegen.
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungDynSchiene {

	/** Die Nummer der Schiene. Wenn es 14 Schienen gibt, dann gibt es 14 Objekte dieser Klasse mit den Nummern 0 bis
	 * 13. */
	private final int nr;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger logger;

	/** Die aktuellen Kurse in dieser Schiene. Über die ID (Long-Wert der GUI) kann man schnell darauf zugreifen. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynKurs> kursMap;

	/** Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. */
	private final @NotNull KursblockungDynStatistik statistik;

	/** Im Konstruktor werden die Referenzen übernommen und das HashMap erzeugt.
	 * 
	 * @param pLogger    Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pNr        Die Nummer der Schiene.
	 * @param pStatistik Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. */
	public KursblockungDynSchiene(@NotNull Logger pLogger, int pNr, @NotNull KursblockungDynStatistik pStatistik) {
		logger = pLogger;
		nr = pNr;
		kursMap = new HashMap<>();
		statistik = pStatistik;
	}

	@Override
	public @NotNull String toString() {
		return "" + nr;
	}

	/** Fügt der Schiene einen Kurs hinzu. Das Statistik-Objekt wird über neue Kurs-Paarungen informiert.
	 * 
	 * @param kurs1 Der Kurs, welcher der Schiene hinzugefügt werden soll. */
	public void aktionKursHinzufuegen(@NotNull KursblockungDynKurs kurs1) {
		long kursID = kurs1.gibID();
		if (kursMap.containsKey(kursID)) {
			logger.logLn(LogLevel.ERROR,
					"Kurs '" + kurs1.gibRepresentation() + "' soll in Schiene " + nr + ", ist aber bereits drin. ");
			return;
		}
		// Zuerst Kurs-Paarungen hinzufügen.
		for (@NotNull
		KursblockungDynKurs kurs2 : kursMap.values()) {
			statistik.aktionKurspaarInSchieneHinzufuegen(kurs1, kurs2);
		}
		// Dann der Datenstruktur hinzufügen.
		kursMap.put(kursID, kurs1);
	}

	/** Entfernt aus der Schiene einen Kurs. Das Statistik-Objekt wird über zu entfernende Kurs-Paarungen informiert.
	 * 
	 * @param kurs1 Der Kurs, welcher aus der Schiene entfernt werden soll. */
	public void aktionKursEntfernen(@NotNull KursblockungDynKurs kurs1) {
		long kursID = kurs1.gibID();
		if (!kursMap.containsKey(kursID)) {
			logger.logLn(LogLevel.ERROR, "Kurs '" + kurs1.gibRepresentation() + "' soll aus Schiene " + nr
					+ " enternt werden, ist aber nicht drin.");
			return;
		}
		// Zuerst aus der Datenstruktur entfernen.
		kursMap.remove(kursID);
		// Dann Kurs-Paarungen entfernen.
		for (@NotNull
		KursblockungDynKurs kurs2 : kursMap.values()) {
			statistik.aktionKurspaarInSchieneEntfernen(kurs1, kurs2);
		}
	}

	/** Liefert die aktuelle Nummer der Schiene (0-indiziert).
	 * 
	 * @return Die aktuelle Nummer der Schiene (0-indiziert). */
	public int gibNr() {
		return nr;
	}

	/** Liefert die aktuelle Anzahl an Kursen in dieser Schiene.
	 * 
	 * @return Die aktuelle Anzahl an Kursen in dieser Schiene. */
	public int gibKursAnzahl() {
		return kursMap.size();
	}

	/** Debug-Ausgabe. Nur für Testzwecke.
	 * 
	 * @param nurMultikurse Fallse TRUE, werden nur Multikurse angezeigt. */
	public void debug(boolean nurMultikurse) {
		for (@NotNull
		KursblockungDynKurs k : kursMap.values()) {
			if (nurMultikurse) {
				if (k.gibSchienenAnzahl() < 2) {
					continue;
				}
			}
			System.out.print("    " + k.gibRepresentation() + "\n");
		}

	}

	/** Liefert die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 * für die Blockung verwendet.
	 * 
	 * @return die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 *         für die Blockung verwendet. */
	int gibAnzahlGleicherFacharten() {
		AVLSet<Integer> setFachart = new AVLSet<>(); // Gibt es bereits diese Fachart?

		int summe = 0;
		for (@NotNull KursblockungDynKurs kurs : kursMap.values())
			if (setFachart.add(kurs.gibFachart().gibNr()) == false) // Wenn es die Fachart bereits gibt, ...
				summe++; // ...  dann Malus erhöhen.

		return summe;
	}

}
