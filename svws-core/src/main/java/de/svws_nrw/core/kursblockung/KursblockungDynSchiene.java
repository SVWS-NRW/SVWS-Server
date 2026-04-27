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
	private final @NotNull Logger log;

	/** Die aktuellen Kurse in dieser Schiene. Über die ID (Long-Wert der GUI) kann man schnell darauf zugreifen. */
	private final @NotNull HashMap<Long, KursblockungDynKurs> kursMap;

	/** Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. */
	private final @NotNull KursblockungDynStatistik statistik;

	/**
	 * Im Konstruktor werden die Referenzen übernommen und das HashMap erzeugt.
	 *
	 * @param logger      Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param nummer      Die Nummer der Schiene.
	 * @param statistik   Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert.
	 */
	public KursblockungDynSchiene(final @NotNull Logger logger, final int nummer, final @NotNull KursblockungDynStatistik statistik) {
		this.log = logger;
		this.nr = nummer;
		this.kursMap = new HashMap<>();
		this.statistik = statistik;
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
			log.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler);
		}

		// Fachart-Schiene und Kurs-Kurs Beziehungen aktualisieren.
		kurs1.gibFachart().aktionSchieneWurdeHinzugefuegt(this);
		for (final @NotNull KursblockungDynKurs kurs2 : kursMap.values()) {
			statistik.aktionKurspaarInSchieneHinzufuegen(kurs1, kurs2);
		}

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
			log.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler);
		}

		// Zuerst aus der Datenstruktur entfernen.
		kursMap.remove(kursID);

		// Fachart-Schiene und Kurs-Kurs Beziehungen aktualisieren.
		kurs1.gibFachart().aktionSchieneWurdeEntfernt(this);
		for (final @NotNull KursblockungDynKurs kurs2 : kursMap.values()) {
			statistik.aktionKurspaarInSchieneEntfernen(kurs1, kurs2);
		}
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
		for (final @NotNull KursblockungDynKurs kurs : kursMap.values()) {
			if (!setFachart.add(kurs.gibFachart().gibNr())) { // Wenn es die Fachart bereits gibt, ...
				summe++; // ... dann Malus erhöhen.
			}
		}

		return summe;
	}

	/**
	 * Debug-Ausgabe. Nur für Testzwecke.
	 *
	 * @param nurMultikurse Falls TRUE, werden nur Multikurse angezeigt.
	 */
	public void debug(final boolean nurMultikurse) {
		log.modifyIndent(+4);
		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			if ((nurMultikurse) && (k.gibSchienenAnzahl() < 2)) {
				continue;
			}
			log.logLn("    " + k.toString());
		}
		log.modifyIndent(-4);
	}


	/**
	 * Liefert einen StringBuild mit der Darstellung aller Kurse dieser Schiene (4 eingerückt) und optional der zugehörigen Schüler (8 eingerückt).
	 *
	 * @param mitSchuelern    Gibt an, ob die Schüler der Kurse (sowie die Fach-ID des Kurses) mit ausgegeben werden sollen.
	 * @param schuelerMenge   Die Menge aller Schüler (wird nur ausgewertet, wenn mitSchuelern true ist).
	 *
	 * @return einen StringBuild mit der Darstellung aller Kurse dieser Schiene (4 eingerückt) und optional der zugehörigen Schüler (8 eingerückt).
	 */
	public StringBuilder debugAusgabeKurseUndSchueler(final boolean mitSchuelern, final @NotNull KursblockungDynSchueler @NotNull [] schuelerMenge) {
		final StringBuilder sb = new StringBuilder();

		// Ausgabe der Schiene
		sb.append("Schiene ").append(nr + 1).append(System.lineSeparator());

		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			// Ausgabe des Kurses dieser Schiene
			sb.append("    ID=").append(k.gibDatenbankID())
					.append(", Fachart=").append(k.gibFachart())
					.append(", Fach-ID=").append(k.gibFachID())
					.append(System.lineSeparator());

			// Ausgabe Schüler des Kurses dieser Schiene (falls gewünscht)
			if (mitSchuelern) {
				for (final @NotNull KursblockungDynSchueler s : schuelerMenge) {
					if (s.gibIstInKurs(k)) {
						sb.append("        Schüler-ID=").append(s.gibDatenbankID())
								.append(", ").append(s.gibRepresentation())
								.append(System.lineSeparator());
					}
				}
			}
		}

		return sb;
	}


	/**
	 * Liefert true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 *
	 * @return true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 */
	public boolean gibHatNurLK() {
		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			if (k.gibFachart().gibKursart() != GostKursart.LK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Liefert true, falls in der Schiene keine Kurse der Kursart LK sind.
	 *
	 * @return true, falls in der Schiene keine Kurse der Kursart LK sind.
	 */
	public boolean gibHatKeineLK() {
		for (final @NotNull KursblockungDynKurs k : kursMap.values()) {
			if (k.gibFachart().gibKursart() == GostKursart.LK) {
				return false;
			}
		}
		return true;
	}
}
