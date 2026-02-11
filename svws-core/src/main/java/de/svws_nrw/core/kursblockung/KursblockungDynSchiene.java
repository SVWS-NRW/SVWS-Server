package de.svws_nrw.core.kursblockung;

import java.util.HashMap;
import java.util.HashSet;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Schiene speichert alle aktuellen Kurse, die in dieser Schiene liegen.
 *
 * @author Benjamin A. Bartsch
 */
public class KursblockungDynSchiene {

	/** Die Nummer der Schiene. Wenn es 14 Schienen gibt, dann gibt es 14 Objekte dieser Klasse mit den Nummern 0 bis 13. */
	private final int nr;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger logger;

	/** Die aktuellen Kurse in dieser Schiene. Über die ID (Long-Wert der GUI) kann man schnell darauf zugreifen. */
	private final @NotNull HashMap<Long, KursblockungDynKurs> kursMap;

	/** Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. */
	private final @NotNull KursblockungDynStatistik statistik;

	/**
	 * Im Konstruktor werden die Referenzen übernommen und das HashMap erzeugt.
	 *
	 * @param pLogger     Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pNr         Die Nummer der Schiene.
	 * @param pStatistik  Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert.
	 */
	public KursblockungDynSchiene(final @NotNull Logger pLogger, final int pNr, final @NotNull KursblockungDynStatistik pStatistik) {
		logger = pLogger;
		nr = pNr;
		kursMap = new HashMap<>();
		statistik = pStatistik;
	}

	/**
	 * Gibt die String-Repräsentation der Schiene zurück.
	 *
	 * @return die String-Repräsentation der Schiene
	 */
	@Override
	public @NotNull String toString() {
		return "" + nr;
	}

	/**
	 * Fügt der Schiene einen Kurs hinzu. Das Statistik-Objekt wird über neue Kurs-Paarungen informiert.
	 *
	 * @param kurs1 Der Kurs, welcher der Schiene hinzugefügt werden soll.
	 */
	public void aktionKursHinzufuegen(final @NotNull KursblockungDynKurs kurs1) {
		// Fehler?
		final long kursID = kurs1.gibDatenbankID();
		if (kursMap.containsKey(kursID)) {
			final String fehler = "Kurs '" + kurs1.toString() + "' soll in Schiene " + nr + ", ist aber bereits drin.";
			logger.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler);
		}

		// Fachart-Schiene und Kurs-Kurs Beziehungen aktualisieren.
		kurs1.gibFachart().aktionSchieneWurdeHinzugefuegt(this);
		for (final @NotNull KursblockungDynKurs kurs2 : kursMap.values())
			statistik.aktionKurspaarInSchieneHinzufuegen(kurs1, kurs2);

		// Dann der Datenstruktur hinzufügen.
		kursMap.put(kursID, kurs1);
	}

	/**
	 * Entfernt aus der Schiene einen Kurs. Das Statistik-Objekt wird über zu entfernende Kurs-Paarungen informiert.
	 *
	 * @param kurs1 Der Kurs, welcher aus der Schiene entfernt werden soll.
	 */
	public void aktionKursEntfernen(final @NotNull KursblockungDynKurs kurs1) {
		// Fehler?
		final long kursID = kurs1.gibDatenbankID();
		if (!kursMap.containsKey(kursID)) {
			final String fehler = "Kurs '" + kurs1.toString() + "' soll aus Schiene " + nr + " entfernt werden, ist aber nicht drin.";
			logger.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler);
		}

		// Zuerst aus der Datenstruktur entfernen.
		kursMap.remove(kursID);

		// Fachart-Schiene und Kurs-Kurs Beziehungen aktualisieren.
		kurs1.gibFachart().aktionSchieneWurdeEntfernt(this);
		for (final @NotNull KursblockungDynKurs kurs2 : kursMap.values())
			statistik.aktionKurspaarInSchieneEntfernen(kurs1, kurs2);
	}

	/**
	 * Liefert die aktuelle Nummer der Schiene (0-indiziert).
	 *
	 * @return Die aktuelle Nummer der Schiene (0-indiziert).
	 */
	public int gibNr() {
		return nr;
	}

	/**
	 * Liefert die aktuelle Anzahl an Kursen in dieser Schiene.
	 *
	 * @return Die aktuelle Anzahl an Kursen in dieser Schiene.
	 */
	public int gibKursAnzahl() {
		return kursMap.size();
	}

	/**
	 * Liefert die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 * für die Blockung verwendet.
	 *
	 * @return die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 *         für die Blockung verwendet.
	 */
	int gibAnzahlGleicherFacharten() {
		final HashSet<Integer> setFachart = new HashSet<>(); // Gibt es bereits diese Fachart?

		int summe = 0;
		for (final @NotNull KursblockungDynKurs kurs : kursMap.values())
			if (!setFachart.add(kurs.gibFachart().gibNr())) // Wenn es die Fachart bereits gibt, ...
				summe++; // ... dann Malus erhöhen.

		return summe;
	}

	/**
	 * Debug-Ausgabe. Nur für Testzwecke.
	 *
	 * @param nurMultikurse Falls TRUE, werden nur Multikurse angezeigt.
	 */
	public void debug(final boolean nurMultikurse) {
		logger.modifyIndent(+4);
		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			if ((nurMultikurse) && (k.gibSchienenAnzahl() < 2))
				continue;
			logger.logLn("    " + k.toString());
		}
		logger.modifyIndent(-4);
	}

	/**
	 * Ausgabe der Kurse dieser Schiene (4 eingerückt).
	 */
	public void printlnKurse() {
		System.out.println("Schiene " + (nr + 1));
		for (final @NotNull KursblockungDynKurs k : kursMap.values())
			System.out.println("    ID " + k.gibDatenbankID() + ", " + k.gibFachart());
	}

	/**
	 * Ausgabe der Kurse (4 eingerückt) dieser Schiene zusammen mit den SuS (8 eingerückt) der Kurse.
	 *
	 * @param _schuelerArr  Die Menge alle SuS.
	 */
	public void printlnKurseUndSchueler(final @NotNull KursblockungDynSchueler @NotNull [] _schuelerArr) {
		System.out.println("Schiene " + (nr + 1));
		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			System.out.println("    ID " + k.gibDatenbankID() + ", " + k.gibFachart() + ", Fach-ID=" + k.gibFachID());
			for (final @NotNull KursblockungDynSchueler s : _schuelerArr)
				if (s.gibIstInKurs(k))
					System.out.println("        ID " + s.gibDatenbankID() + ", " + s.gibRepresentation());
		}
	}

	/**
	 * Liefert true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 *
	 * @return true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 */
	public boolean gibHatNurLK() {
		for (final @NotNull KursblockungDynKurs k : kursMap.values())
			if (k.gibFachart().gibKursart() != GostKursart.LK)
				return false;
		return true;
	}

	/**
	 * Liefert true, falls in der Schiene keine Kurse der Kursart LK sind.
	 *
	 * @return true, falls in der Schiene keine Kurse der Kursart LK sind.
	 */
	public boolean gibHatKeineLK() {
		for (final @NotNull KursblockungDynKurs k : kursMap.values())
			if (k.gibFachart().gibKursart() == GostKursart.LK)
				return false;
		return true;
	}
}
