package de.svws_nrw.schulen.v1.utils;

import java.util.Comparator;

import de.svws_nrw.schulen.v1.data.SchuldateiEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält allgemeine Hilfsmethoden für die Manager der Schuldatei
 */
public final class SchuldateiUtils {

	private SchuldateiUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Das Anfangs-Schuljahr in einem Zeitraum, der abgibt, dass der Wert schon immer gültig war */
	public static final int _immerGueltigAb = 1980;

	/** Das End-Schuljahr in einem Zeitraum, der abgibt, dass der Wert in seiner Gültigkeit noch nicht eingeschränkt ist */
	public static final int _immerGueltigBis = 9999;


	/** Der Comparator zur Sortierung der Zeiträume gueltigab - gueltigbis in absteigender Reihenfolge*/
	public static final @NotNull Comparator<SchuldateiEintrag> _comparatorSchuldateieintragZeitraumDescending =
			(final @NotNull SchuldateiEintrag a, final @NotNull SchuldateiEintrag b) -> {
				if (b.gueltigab.equals(a.gueltigab))
					return SchuldateiUtils.compare(b.gueltigbis, a.gueltigbis);
				return SchuldateiUtils.compare(b.gueltigab, a.gueltigab);
			};


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
	 * Ermittlung des Schuljahres in der Zeitpunkt des angegebenen Datumstrings liegt
	 *
	 * @param date		das Datum als String
	 *
	 * @return @NotNull INTEGER  das Schuljahr. 1980 für gilt seit immer (jahr .lte. 1980), 9999 gilt für immer (Jahr .gte. 3000), sonst das Schuljahr
	 *
	 * @throws IllegalArgumentException im Fehlerfall
	 */
	public static @NotNull Integer schuljahrAusDatum(final @NotNull String date) throws IllegalArgumentException {
		final @NotNull int[] dmy = splitDate(date);
		int jahr = dmy[2];
		if (dmy[1] < 8)
			jahr--;
		return jahr;
	}


	/**
	 * Ermittlung des Schuljahres in der Zeitpunkt des angegebenen Datumstrings liegt
	 * Wenn die Gültigkeit von Katalogeinträgen innerhalb eines Schuljahres wechseln, so ist der alte Eintrag
	 * bis zum Ende des Schuljahres gültig und der neue Eintrag erst ab kommenden Schuljahr.
	 * Eintrag1: "gueltigab": "01.01.1970",	"gueltigbis": "07.03.2022"
	 * Eintrag2: "gueltigab": "08.03.2022",	"gueltigbis": "31.12.9999"
	 * Eintrag1 ist dann in den Schuljahren bis 2021 gültig und Eintrag2 ab Schuljahr 2022.
	 *
	 * Ein Eintrag, der nur eine Gültigkeit innerhalb eines Schuljahres hat wird verworfen.
	 *
	 *
	 * @param ab		das gueltigab Datum als String
	 *
	 * @return @NotNull INTEGER  , 9999 gilt für immer (Jahr .gte. 3000), sonst das Schuljahr
	 *
	 * @throws IllegalArgumentException im Fehlerfall
	 */
	public static @NotNull Integer schuljahrGueltigAb(final @NotNull String ab) throws IllegalArgumentException {
		final @NotNull int[] dmyAb = splitDate(ab);
		int jahrAb = dmyAb[2];
		if ((dmyAb[1] > 8) || ((dmyAb[1] == 8) && (dmyAb[0] > 1))) //alle Tage nach dem 01.08. eines Jahres
			jahrAb++;
		return jahrAb;
	}


	/**
	 * Ermittlung des Schuljahres in der Zeitpunkt des angegebenen Datumstrings liegt
	 * Wenn die Gültigkeit von Katalogeinträgen innerhalb eines Schuljahres wechseln, so ist der alte Eintrag
	 * bis zum Ende des Schuljahres gültig und der neue Eintrag erst ab kommenden Schuljahr.
	 * Eintrag1: "gueltigab": "01.01.1970",	"gueltigbis": "07.03.2022"
	 * Eintrag2: "gueltigab": "08.03.2022",	"gueltigbis": "31.12.9999"
	 * Eintrag1 ist dann in den Schuljahren bis 2021 gültig und Eintrag2 ab Schuljahr 2022.
	 *
	 * Ein Eintrag, der nur eine Gültigkeit innerhalb eines Schuljahres hat wird verworfen.
	 *
	 *
	 * @param bis		das gueltigbis Datum als String
	 *
	 * @return @NotNull INTEGER  , 9999 gilt für immer (Jahr .gte. 3000), sonst das Schuljahr
	 *
	 * @throws IllegalArgumentException im Fehlerfall
	 */
	public static @NotNull Integer schuljahrGueltigBis(final @NotNull String bis) throws IllegalArgumentException {
		return schuljahrAusDatum(bis);
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
	 * Gibt den Vergleichswert der beiden Daten wie bei einem Comparator zurück
	 * Die Strings werden in der Form 'DD.MM.YYYY' erwartet
	 *
	 * @param a   das Datum a
	 * @param b   das Datum b
	 *
	 * @return -1,0,1  wie beim Comparator
	 *
	 * @throws IllegalArgumentException wenn die Datumsangaben fehlerhaft sind
	 */
	public static int compare(final String a, final String b) throws IllegalArgumentException {
		// Wenn a leer ist, dann wird a als unendlich spät angesehen => nicht früher
		if ((a == null) || (a.isBlank()))
			return 1;
		// Wenn b leer ist, dann wird b als unendlich spät angesehen, und außerdem ist a nicht leer => früher
		if ((b == null) || (b.isBlank()))
			return -1;
		final @NotNull int[] dmyA = splitDate(a);
		final @NotNull int[] dmyB = splitDate(b);
		int cmp = Integer.compare(dmyA[2], dmyB[2]);
		if (cmp != 0)
			return cmp;
		cmp = Integer.compare(dmyA[1], dmyB[1]);
		if (cmp != 0)
			return cmp;
		return Integer.compare(dmyA[0], dmyB[0]);
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


	/**
	 * Prüft, ob zwei SchuldateiEintrag zeitliche Überlappung haben
	 * Keine zeitliche Überlappung liegt vor
	 * |----1a----|                 |----1b----|
	 *               |----2----|
	 * keine Überlappung bei:  (1.bis kleiner 2.ab || 2.bis kleiner 1.ab)
	 *       Überlappung bei: !(1.bis kleiner 2.ab || 2.bis kleiner 1.ab)
	 *
	 * @param eintrag1		der eine Eintrag
	 * @param eintrag2		der andere Eintrag
	 *
	 * @return				ob die Einträge überlappend sind
	 */
	public static boolean pruefeUeberlappung(final @NotNull SchuldateiEintrag eintrag1, final @NotNull SchuldateiEintrag eintrag2) {
		return !(istFrueher(eintrag1.gueltigbis, eintrag2.gueltigab) || istFrueher(eintrag2.gueltigbis, eintrag1.gueltigab));
	}
}
