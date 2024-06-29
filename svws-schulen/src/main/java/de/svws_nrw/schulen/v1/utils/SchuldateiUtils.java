package de.svws_nrw.schulen.v1.utils;

import de.svws_nrw.schulen.v1.data.SchuldateiEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält allgemeine Hilfsmethoden für die Manager der Schuldatei
 */
public final class SchuldateiUtils {

	private SchuldateiUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Wandelt das übergebene Datum als String mit dem Format 'DD.MM.YYYY' in ein
	 * Integer mit den Angaben für Tag, Monat und Jahr um.
	 *
	 * @param date   das Datum als String
	 *
	 * @return das umgewandelte Datum
	 *
	 * @throws IllegalArgumentException falls der String sich nicht parsen lässt oder
	 *     die Datumsangabe fehlerhaft ist
	 */
	private static @NotNull int[] splitDate(final @NotNull String date) throws IllegalArgumentException {
		final @NotNull String @NotNull [] dmy = date.split("\\.");
		if (dmy.length != 3)
			throw new IllegalArgumentException("Der Datumswert '" + date + "' ist fehlerhaft.");
		try {
			final @NotNull int[] result = new int[3];
			result[0] = Integer.parseInt(dmy[0]);
			if ((result[0] < 1) || (result[0] > 31))
				throw new NumberFormatException("Die Angabe des Tages ist fehlerhaft.");
			result[1] = Integer.parseInt(dmy[1]);
			if ((result[1] < 1) || (result[1] > 12))
				throw new NumberFormatException("Die Angabe des Monats ist fehlerhaft.");
			result[2] = Integer.parseInt(dmy[2]);
			return result;
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			throw new IllegalArgumentException("Der Datumswert '" + date + "' ist fehlerhaft.");
		}
	}


	/**
	 * Prüft, ob das Datum a früher liegt als das Datum b. Es wird eine Datumsangabe der Form
	 * 'DD.MM.YYYY' erwartet,
	 *
	 * @param a   das Datum a
	 * @param b   das Datum b
	 *
	 * @return true, wenn es früher liegt
	 *
	 * @throws IllegalArgumentException wenn die Datumsangaben fehlerhaft sind
	 */
	public static boolean istFrueher(final String a, final String b) throws IllegalArgumentException {
		// Wenn a leer ist, dann wird a als unendlich spät angesehen => nicht früher
		if ((a == null) || (a.isBlank()))
			return false;
		// Wenn b leer ist, dann wird b als unendlich spät angesehen, und außerdem ist a nicht leer => früher
		if ((b == null) || (b.isBlank()))
			return true;
		final @NotNull int[] dmyA = splitDate(a);
		final @NotNull int[] dmyB = splitDate(b);
		int cmp = Integer.compare(dmyA[2], dmyB[2]);
		if (cmp < 0)
			return true;
		if (cmp > 0)
			return false;
		cmp = Integer.compare(dmyA[1], dmyB[1]);
		if (cmp < 0)
			return true;
		if (cmp > 0)
			return false;
		cmp = Integer.compare(dmyA[0], dmyB[0]);
		return (cmp < 0);
	}


	/**
	 * Prüft, ob der Schuldatei-Eintrag in mindestens einem Teil des angebenen Schuljahres gültig ist oder nicht.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param eintrag     der Eintrag
	 *
	 * @return true, wenn der Eintrag in dem Schuljahr gültig ist.
	 *
	 * @throws IllegalArgumentException falls die Formatierung der Datumswerte im Schuldatei-Eintrag fehlerhaft sind
	 */
	public static boolean pruefeSchuljahr(final int schuljahr, final @NotNull SchuldateiEintrag eintrag) throws IllegalArgumentException {
		if ((eintrag.gueltigab != null) && (!eintrag.gueltigab.isBlank())) {
			final @NotNull int[] dmy = splitDate(eintrag.gueltigab);
			if (!((dmy[2] <= schuljahr) || ((dmy[2] == (schuljahr + 1)) && (dmy[1] < 8))))
				return false;
		}
		if ((eintrag.gueltigbis != null) && (!eintrag.gueltigbis.isBlank())) {
			final @NotNull int[] dmy = splitDate(eintrag.gueltigbis);
			if (!((dmy[2] >= (schuljahr + 1)) || ((dmy[2] == schuljahr) && (dmy[1] > 7))))
				return false;
		}
		return true;
	}

}
