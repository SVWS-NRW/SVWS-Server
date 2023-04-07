package de.svws_nrw.core.abschluss;

import de.svws_nrw.core.abschluss.bk.a.BKAnlageAFach;
import de.svws_nrw.core.abschluss.bk.a.BKAnlageAFaecher;
import de.svws_nrw.core.data.abschluss.AbschlussErgebnisBerufsbildend;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Verwaltung von allgemeinen Informationen
 * zur Abschlussberechnung an berufsbildenden Schule und stellt insbesondere
 * auch wiederverwendbare Methoden zur Verfügung.
 */
public final class AbschlussManagerBerufsbildend {
	private AbschlussManagerBerufsbildend() {
		throw new IllegalStateException("Instantiation of " + AbschlussManagerBerufsbildend.class.getName() + " not allowed");
	}

	/**
	 * Erzeugt ein Ergebnis der Abschlussberechnung unter Angabe, ob dieser erworben
	 * wurde. Die Liste der Nachprüfungsfächer ist leer und ein Log ist nicht zugeordnet.
	 *
	 * @param hatBSA                      ist der Berufsschulabschluss erreicht
	 * @param note                        Note des Abschlusses
	 * @param hatBA                       ist der Berufsabschluss erreicht
	 * @param abschlussAllgemeinbildend   der allgemeinbildende Abschluss
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public static @NotNull AbschlussErgebnisBerufsbildend getErgebnis(final boolean hatBSA, final double note, final Boolean hatBA, final SchulabschlussAllgemeinbildend abschlussAllgemeinbildend) {
		// Nachprüfungsmöglichkeiten ???
		final @NotNull AbschlussErgebnisBerufsbildend ergebnis = new AbschlussErgebnisBerufsbildend();
		ergebnis.hatBSA = hatBSA;
		ergebnis.note = note;
		ergebnis.hatBA = hatBA;
		ergebnis.abschlussAllgemeinbildend = (abschlussAllgemeinbildend == null) ? null : abschlussAllgemeinbildend.toString();
		ergebnis.log = null;
		return ergebnis;
	}




	/**
	 * Berechnet den Notendurchschnitt aller Fächer
	 *
	 * @param abschlussFaecher   die Fächer für die Abschlussberechnung
	 *
	 * @return der Notendurchschnitt oder NaN im Fehlerfall
	 */
	public static double getDurchschnitt(final @NotNull BKAnlageAFaecher abschlussFaecher) {
		if ((abschlussFaecher.faecher == null) || (abschlussFaecher.faecher.isEmpty()))
			return Double.NaN;
		int sum = 0;
		for (final BKAnlageAFach fach : abschlussFaecher.faecher) {
			sum += fach.note;
		}
		return ((double) sum) / abschlussFaecher.faecher.size();
	}


	/**
	 * Berechnet die Anzahl der Defizite
	 *
	 * @param abschlussFaecher   die Fächer für die Abschlussberechnung
	 *
	 * @return die Anzahl der Defizite oder -1 im Fehlerfall
	 */
	public static int getAnzahlDefizite(final @NotNull BKAnlageAFaecher abschlussFaecher) {
		if ((abschlussFaecher.faecher == null) || (abschlussFaecher.faecher.isEmpty()))
			return -1;
		int sum = 0;
		for (final BKAnlageAFach fach : abschlussFaecher.faecher) {
			if (fach.note >= 5)
				sum++;
		}
		return sum;
	}


	/**
	 * Berechnet die Anzahl der Note Ungenügend
	 *
	 * @param abschlussFaecher   die Fächer für die Abschlussberechnung
	 *
	 * @return die Anzahl der Note Ungenügend oder -1 im Fehlerfall
	 */
	public static int getAnzahlUngenuegend(final @NotNull BKAnlageAFaecher abschlussFaecher) {
		if ((abschlussFaecher.faecher == null) || (abschlussFaecher.faecher.isEmpty()))
			return -1;
		int sum = 0;
		for (final BKAnlageAFach fach : abschlussFaecher.faecher) {
			if (fach.note >= 6)
				sum++;
		}
		return sum;
	}

}
