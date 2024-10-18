package de.svws_nrw.asd.types.jahrgang;

import java.util.List;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Jahrgängen
 * bei Schülern und bei Klassen, sofern diese Klassen nicht Jahrgangsübergreifend
 * unterrichtet werden.
 */
public enum Jahrgaenge implements CoreType<JahrgaengeKatalogEintrag, Jahrgaenge> {

	/** Jahrgang 90: Hausfrüherziehung für Hör- bzw. Sehgeschädigte */
	HAUSFRUEERZIEHUNG,

	/** Jahrgang 00: Frühkindliche Förderung, Förderschulkindergarten */
	JAHRGANG_00,


	/** 1. Jahrgang */
	JAHRGANG_01,

	/** 2. Jahrgang */
	JAHRGANG_02,

	/** 3. Jahrgang */
	JAHRGANG_03,

	/** 4. Jahrgang */
	JAHRGANG_04,

	/** 5. Jahrgang */
	JAHRGANG_05,

	/** 6. Jahrgang */
	JAHRGANG_06,

 	/** 7. Jahrgang */
	JAHRGANG_07,

	/** 8. Jahrgang */
	JAHRGANG_08,

	/** 9. Jahrgang */
	JAHRGANG_09,

	/** 10. Jahrgang */
	JAHRGANG_10,


	/** 11. Jahrgang */
	JAHRGANG_11,

	/** 12. Jahrgang */
	JAHRGANG_12,

	/** 13. Jahrgang */
	JAHRGANG_13,


	/** Jahrgang EF: Gymnasiale Oberstufe - Einführungsphase */
	EF,

	/** Jahrgang Q1: Gymnasiale Oberstufe - Qualifikationsphase 1. Jahr */
	Q1,

	/** Jahrgang Q2: Gymnasiale Oberstufe - Qualifikationsphase 2. Jahr */
	Q2,


	/** Jahrgang Berufspraxisstufe laut AO-SF für den Förderschwerpunkt Geistige Entwicklung (je nach Organisationsform in Teilzeit (86) und Vollzeit (85) möglich) */
	BERUFSPRAXISSTUFE,


	/** Jahrgang 91: Vorkurs/ 1. Semester */
	VORKURS_SEMESTER_1,

	/** Jahrgang 92: Vorkurs/ 2. Semester */
	VORKURS_SEMESTER_2,

	/** 1. Semester */
	SEMESTER_01,

	/** 2. Semester */
	SEMESTER_02,

	/** 3. Semester */
	SEMESTER_03,

	/** 4. Semester */
	SEMESTER_04,

	/** 5. Semester */
	SEMESTER_05,

	/** 6. Semester */
	SEMESTER_06;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge> manager) {
		CoreTypeDataManager.putManager(Jahrgaenge.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge> data() {
		return CoreTypeDataManager.getManager(Jahrgaenge.class);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf) {
		return data().hatSchulform(schuljahr, sf, this);
	}


	/**
	 * Gibt den Katalog-Eintrag des Jahrgangs für die übergenene Schulform in dem übergebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param sf          die Schulform
	 *
	 * @return der Katalog-Eintrag oder null, wenn keiner gefunden wird
	 */
	public JahrgaengeKatalogEintrag getBySchulform(final int schuljahr, final @NotNull Schulform sf) {
		return data().getBySchulform(schuljahr, sf, this);
	}


	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Vorgänger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param schuljahr            das Schuljahr, welches für den Zugriff auf die Core-Type-Daten benötigt wird
	 * @param vergleichsjahrgang   der zu prüfende Jahrgang
	 * @param schulform            die Schulform
	 * @param gliederung           die Schulgliederung
	 *
	 * @return true, falls jgVorher ein gültiger Vorgänger-Jahrgang dieses Jahrgangs ist.
	 */
	public boolean isNachfolgerVon(final int schuljahr, final Jahrgaenge vergleichsjahrgang, final Schulform schulform, final Schulgliederung gliederung) {
		if (schulform == null)
			return false;
		if (!this.hatSchulform(schuljahr, schulform) || ((vergleichsjahrgang != null) && (!vergleichsjahrgang.hatSchulform(schuljahr, schulform))))
			return false;
		final Schulgliederung gl = (gliederung == null) ? Schulgliederung.getDefault(schulform) : gliederung;
		return switch (this) {
			case JAHRGANG_00 -> (vergleichsjahrgang == null);
			case JAHRGANG_01 -> (vergleichsjahrgang == null);
			case JAHRGANG_02 -> (vergleichsjahrgang == JAHRGANG_01);
			case JAHRGANG_03 -> (vergleichsjahrgang == JAHRGANG_02);
			case JAHRGANG_04 -> (vergleichsjahrgang == JAHRGANG_03);
			case JAHRGANG_05 -> (vergleichsjahrgang == JAHRGANG_04);
			case JAHRGANG_06 -> (vergleichsjahrgang == JAHRGANG_05);
			case JAHRGANG_07 -> (vergleichsjahrgang == JAHRGANG_06);
			case JAHRGANG_08 -> (vergleichsjahrgang == JAHRGANG_07);
			case JAHRGANG_09 -> (vergleichsjahrgang == JAHRGANG_08);
			case JAHRGANG_10 -> (vergleichsjahrgang == JAHRGANG_09);
			case JAHRGANG_11 -> (vergleichsjahrgang == JAHRGANG_10);
			case JAHRGANG_12 -> (vergleichsjahrgang == JAHRGANG_11);
			case JAHRGANG_13 -> (vergleichsjahrgang == JAHRGANG_12);
			case BERUFSPRAXISSTUFE -> (vergleichsjahrgang == null);
			case HAUSFRUEERZIEHUNG -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == VORKURS_SEMESTER_1);
			case EF -> (gl == Schulgliederung.GY8) ? (vergleichsjahrgang == JAHRGANG_09) : (vergleichsjahrgang == JAHRGANG_10);
			case Q1 -> (vergleichsjahrgang == EF);
			case Q2 -> (vergleichsjahrgang == Q1);
			default -> false;
		};
	}


	/**
	 * Prüft, ob der angegebene Jahrgang bei der angegebenen Schulform und Gliederung ein gültiger Nachfolger-Jahrgang
	 * dieses Jahrgangs ist.
	 *
	 * @param schuljahr            das Schuljahr, welches für den Zugriff auf die Core-Type-Daten benötigt wird
	 * @param vergleichsjahrgang    der zu prüfende Jahrgang
	 * @param schulform    die Schulform
	 * @param gliederung   die Schulgliederung
	 *
	 * @return true, falls jgNachher ein gültiger Nachfolger-Jahrgang dieses Jahrgangs ist.
	 */
	public boolean isVorgaengerVon(final int schuljahr, final Jahrgaenge vergleichsjahrgang, final Schulform schulform, final Schulgliederung gliederung) {
		if (schulform == null)
			return false;
		if (!this.hatSchulform(schuljahr, schulform) || ((vergleichsjahrgang != null) && (!vergleichsjahrgang.hatSchulform(schuljahr, schulform))))
			return false;
		final SchulformKatalogEintrag ske = schulform.daten(schuljahr);
		if (ske == null)
			return false;
		final Schulgliederung gl = (gliederung == null) ? Schulgliederung.getDefault(schulform) : gliederung;
		return switch (this) {
			case JAHRGANG_00 -> (vergleichsjahrgang == JAHRGANG_01);
			case JAHRGANG_01 -> (vergleichsjahrgang == JAHRGANG_02);
			case JAHRGANG_02 -> (vergleichsjahrgang == JAHRGANG_03);
			case JAHRGANG_03 -> (vergleichsjahrgang == JAHRGANG_04);
			case JAHRGANG_04 -> (vergleichsjahrgang == JAHRGANG_05);
			case JAHRGANG_05 -> (vergleichsjahrgang == JAHRGANG_06);
			case JAHRGANG_06 -> (vergleichsjahrgang == JAHRGANG_07);
			case JAHRGANG_07 -> (vergleichsjahrgang == JAHRGANG_08);
			case JAHRGANG_08 -> (vergleichsjahrgang == JAHRGANG_09);
			case JAHRGANG_09 -> (vergleichsjahrgang == JAHRGANG_10)
						|| ((schulform == Schulform.GY) && ((gl == Schulgliederung.GY8) || (gl == Schulgliederung.DEFAULT)) && (vergleichsjahrgang == EF));
			case JAHRGANG_10 -> (vergleichsjahrgang == JAHRGANG_11) || ((ske.hatGymOb) && (vergleichsjahrgang == EF));
			case JAHRGANG_11 -> (vergleichsjahrgang == JAHRGANG_12);
			case JAHRGANG_12 -> (vergleichsjahrgang == JAHRGANG_13);
			case JAHRGANG_13 -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE -> (vergleichsjahrgang == null);
			case HAUSFRUEERZIEHUNG -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == null);
			case EF -> (vergleichsjahrgang == Q1);
			case Q1 -> (vergleichsjahrgang == Q2);
			case Q2 -> (vergleichsjahrgang == null);
			default -> false;
		};
	}


	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Nachfolger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Nachfolger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public boolean isMoeglicherNachfolgerVon(final Jahrgaenge vergleichsjahrgang) {
		return switch (this) {
			case JAHRGANG_00 -> (vergleichsjahrgang == null);
			case JAHRGANG_01 -> (vergleichsjahrgang == null);
			case JAHRGANG_02 -> (vergleichsjahrgang == JAHRGANG_01);
			case JAHRGANG_03 -> (vergleichsjahrgang == JAHRGANG_02);
			case JAHRGANG_04 -> (vergleichsjahrgang == JAHRGANG_03);
			case JAHRGANG_05 -> (vergleichsjahrgang == JAHRGANG_04);
			case JAHRGANG_06 -> (vergleichsjahrgang == JAHRGANG_05);
			case JAHRGANG_07 -> (vergleichsjahrgang == JAHRGANG_06);
			case JAHRGANG_08 -> (vergleichsjahrgang == JAHRGANG_07);
			case JAHRGANG_09 -> (vergleichsjahrgang == JAHRGANG_08);
			case JAHRGANG_10 -> (vergleichsjahrgang == JAHRGANG_09);
			case JAHRGANG_11 -> (vergleichsjahrgang == JAHRGANG_10);
			case JAHRGANG_12 -> (vergleichsjahrgang == JAHRGANG_11);
			case JAHRGANG_13 -> (vergleichsjahrgang == JAHRGANG_12);
			case BERUFSPRAXISSTUFE -> (vergleichsjahrgang == null);
			case HAUSFRUEERZIEHUNG -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == VORKURS_SEMESTER_1);
			case EF -> (vergleichsjahrgang == JAHRGANG_09) || (vergleichsjahrgang == JAHRGANG_10);
			case Q1 -> (vergleichsjahrgang == EF);
			case Q2 -> (vergleichsjahrgang == Q1);
			default -> false;
		};
	}


	/**
	 * Prüft, ob dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 * Da durch den Wechsel von G8 und G9 und den Wechsel von Sek-I (10) in die Sek-II eine Zuordnung unter Umständen
	 * nur Schüler individuell entschieden werden kann, wird hier nur auf mögliche Vorgänger geprüft.
	 *
	 * Diese Methode berücksichtigt keine Schulformen und -gliederungen und liefert daher nur ein grobes Ergebnis
	 *
	 * @param vergleichsjahrgang    der Jahrgang für den Vergleich zum gegebenen Jahrgang.
	 *
	 * @return true, falls dieser Jahrgang ein möglicher Vorgänger des übergebenen Vergleichsjahrgangs ist.
	 */
	public boolean isMoeglicherVorgaengerVon(final Jahrgaenge vergleichsjahrgang) {
		return switch (this) {
			case JAHRGANG_00 -> (vergleichsjahrgang == JAHRGANG_01);
			case JAHRGANG_01 -> (vergleichsjahrgang == JAHRGANG_02);
			case JAHRGANG_02 -> (vergleichsjahrgang == JAHRGANG_03);
			case JAHRGANG_03 -> (vergleichsjahrgang == JAHRGANG_04);
			case JAHRGANG_04 -> (vergleichsjahrgang == JAHRGANG_05);
			case JAHRGANG_05 -> (vergleichsjahrgang == JAHRGANG_06);
			case JAHRGANG_06 -> (vergleichsjahrgang == JAHRGANG_07);
			case JAHRGANG_07 -> (vergleichsjahrgang == JAHRGANG_08);
			case JAHRGANG_08 -> (vergleichsjahrgang == JAHRGANG_09);
			case JAHRGANG_09 -> (vergleichsjahrgang == JAHRGANG_10);
			case JAHRGANG_10 -> (vergleichsjahrgang == JAHRGANG_11);
			case JAHRGANG_11 -> (vergleichsjahrgang == JAHRGANG_12);
			case JAHRGANG_12 -> (vergleichsjahrgang == JAHRGANG_13);
			case JAHRGANG_13 -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE -> (vergleichsjahrgang == null);
			case HAUSFRUEERZIEHUNG -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == null);
			case EF -> (vergleichsjahrgang == Q1);
			case Q1 -> (vergleichsjahrgang == Q2);
			case Q2 -> (vergleichsjahrgang == null);
			default -> false;
		};
	}


	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public boolean hatLernbereichsnote1(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		return switch (schulform) {
			case R, SR, H, S, FW, WF -> (this == Jahrgaenge.JAHRGANG_10);
			case GY, SG -> ((schulgliederung == Schulgliederung.GY8) || (schulgliederung == Schulgliederung.DEFAULT))
					? (this == Jahrgaenge.EF) : (this == Jahrgaenge.JAHRGANG_10);
			case GM, GE, PS, SK -> ((this == Jahrgaenge.JAHRGANG_10) && (schuljahr <= 2024))
					|| ((this == Jahrgaenge.JAHRGANG_09) && (schuljahr <= 2023))
					|| ((this == Jahrgaenge.JAHRGANG_08) && (schuljahr <= 2022));
			case HI -> (this == Jahrgaenge.JAHRGANG_10);
			case KS -> (this == Jahrgaenge.JAHRGANG_10);
			case V -> (this == Jahrgaenge.JAHRGANG_10);
			case BK, SB, WB, G -> false;
		};
	}


	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 1 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 1 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public String getLernbereichsnote1Bezeichnung(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		if (!hatLernbereichsnote1(schulform, schulgliederung, schuljahr))
			return null;
		return switch (schulform) {
			case H, GM, GE, PS, SK -> "Arbeitslehre";
			default -> "Gesellschaftslehre";
		};
	}


	/**
	 * Gibt zurück, ob bei diesem Jahrgang für die angebebene Schulform und Schulgliederung in dem Schuljahr eine
	 * Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return true, falls eine Lernbereichsnote vorhanden ist.
	 */
	public boolean hatLernbereichsnote2(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		return switch (schulform) {
			case R, SR, H, S, FW, WF -> (this == Jahrgaenge.JAHRGANG_10);
			case GY, SG -> ((schulgliederung == Schulgliederung.GY8) || (schulgliederung == Schulgliederung.DEFAULT))
					? (this == Jahrgaenge.EF) : (this == Jahrgaenge.JAHRGANG_10);
			case GM, GE, PS, SK -> ((this == Jahrgaenge.JAHRGANG_10) || (this == Jahrgaenge.JAHRGANG_09) || (this == Jahrgaenge.JAHRGANG_08));
			case HI -> (this == Jahrgaenge.JAHRGANG_10);
			case KS -> (this == Jahrgaenge.JAHRGANG_10);
			case V -> (this == Jahrgaenge.JAHRGANG_10);
			case BK, SB, WB, G -> false;
		};
	}


	/**
	 * Gibt die Bezeichnung der Lernbereichtsnote 2 zurück, sofern bei diesem Jahrgang für die angebebene Schulform
	 * und Schulgliederung in dem Schuljahr eine Lernbereichsnote 2 angebeben werden kann.
	 *
	 * @param schulform        die Schulform
	 * @param schulgliederung  die Schulgliederung
	 * @param schuljahr        das Schuljahr
	 *
	 * @return die Bezeichnung der Lernbereichsnote, falls eine vorhanden ist und ansonsten null.
	 */
	public String getLernbereichsnote2Bezeichnung(final @NotNull Schulform schulform, final Schulgliederung schulgliederung, final int schuljahr) {
		if (!hatLernbereichsnote2(schulform, schulgliederung, schuljahr))
			return null;
		return "Naturwissenschaft";
	}


	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static @NotNull List<Jahrgaenge> getListBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		return data().getListBySchuljahrAndSchulform(schuljahr, schulform);
	}

}
