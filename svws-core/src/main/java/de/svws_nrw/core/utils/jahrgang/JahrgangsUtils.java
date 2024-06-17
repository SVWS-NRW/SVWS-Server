package de.svws_nrw.core.utils.jahrgang;

import java.util.Comparator;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Jahrgänge zur Verfügung.
 */
public final class JahrgangsUtils {

	private JahrgangsUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/** Ein Default-Comparator für den Vergleich von Jahrgängen in Jahrgangslisten. */
	public static final @NotNull Comparator<@NotNull JahrgangsDaten> comparator = (final @NotNull JahrgangsDaten a, final @NotNull JahrgangsDaten b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		if ((a.kuerzel == null) || (b.kuerzel == null))
			return Long.compare(a.id, b.id);
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};


	/**
	 * Bestimmt für die angegebene Schulform, die übergebene Schulgliederung (auch beim Schüler eingetragenen Schulgliederung)
	 * und den angegebenen Jahrgang die restlichen Jahre an der Schule.
	 * Ist keine Berechnung für diese Kombination implementiert,
	 * so wird null zurückgegeben.
	 *
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 * @param jahrgang     der Jahrgang, für den die restlichen Jahre bestimmt werden sollen
	 *
	 * @return die restlichen Jahre oder null
	 */
	public static Integer getRestlicheJahre(final @NotNull Schulform schulform, final @NotNull Schulgliederung gliederung, final @NotNull String jahrgang) {
		// TODO Benutze einen noch zu definierenden Core-Type für Jahrgänge und verschiebe diese Methode in diesen Core-Type
		if (gliederung == null)
			return null;
		if ((schulform == Schulform.FW) || (schulform == Schulform.WB) || (schulform == Schulform.BK) || (schulform == Schulform.SB))
			return null;
		if (jahrgang == null)
			return null;
		if (schulform == Schulform.GY) {
			// Gymnasium zählt Restjahre immer bis zum Abitur
			switch (jahrgang) {
				// DEFAULT (***) wird hier als G8 interpretiert, Ausnahme Jahrgang 10
				case "05":
					return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 8 : 9;
				case "06":
					return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 7 : 8;
				case "07":
					return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 6 : 7;
				case "08":
					return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 5 : 6;
				case "09":
					return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 4 : 5;
				// Jahrgangsstufe 10 gibt es am GY nur im G9, d. h. DEFAULT (***) kann dort immer als G9 interpretiert werden
				case "10":
					return gliederung.istG8() ? null : 4;
				case "EF":
					return 3;
				case "Q1":
					return 2;
				case "Q2":
					return 1;
				// Alte Oberstufenjahrgänge, keine Verwendung bei aktiven SuS an GY und GE seit 2010
				case "11":
					return 3;
				case "12":
					return 2;
				case "13":
					return 1;
				default:
					return null;
			}
		}
		// Angaben für die allgemein bildenden Schulen ohne Gymnasium
		// TODO nicht darunter fallende Schulformen vorher verarbeiten
		switch (jahrgang) {
			// Primarstufe
			case "E1":
				return 4;
			case "E2":
				return 3;
			case "E3":
				return 3;
			case "03":
				return 2;
			case "04":
				return 1;
			// Sekundarstufe I inklusive Gesamtschulen
			case "05":
				return 6;
			case "06":
				return 5;
			case "07":
				return 4;
			case "08":
				return 3;
			case "09":
				return 2;
			case "10":
				return 1;
			// Sekundarstufe II
			case "EF":
				return 3;
			case "Q1":
				return 2;
			case "Q2":
				return 1;
			// Alte Oberstufenjahrgänge, keine Verwendung bei aktiven SuS an GY und GE seit 2010
			case "11":
				return 3;
			case "12":
				return 2;
			case "13":
				return 1;
			default:
				return null;
		}
	}


	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Sekundarstufe I handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Sek I-Jahrgang handelt, und ansonsten false
	 */
	public static boolean istSekI(final @NotNull String jahrgang) {
		return Jahrgaenge.JG_05.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_06.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_07.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_08.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_09.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_10.daten.kuerzel.equals(jahrgang);
	}


	/**
	 * Gibt zurück, ob es sich bei dem Statistik-Jahrgang um einen Jahrgang der Gymnasialen Oberstufe handelt oder nicht.
	 *
	 * @param jahrgang   der Statistik-Jahrgang
	 *
	 * @return true, falls es sich um einen Jahrgang der Gymnasialen Oberstufe handelt, und ansonsten false
	 */
	public static boolean istGymOb(final @NotNull String jahrgang) {
		return Jahrgaenge.JG_EF.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_Q1.daten.kuerzel.equals(jahrgang)
				|| Jahrgaenge.JG_Q2.daten.kuerzel.equals(jahrgang);
	}

}
