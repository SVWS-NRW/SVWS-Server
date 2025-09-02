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
	HAUSFRUEHERZIEHUNG,

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


	/** Jahrgang Berufspraxisstufe in Vollzeit laut AO-SF für den Förderschwerpunkt Geistige Entwicklung (je nach Organisationsform in Teilzeit (86) und Vollzeit (85) möglich) */
	BERUFSPRAXISSTUFE_VOLLZEIT,

	/** Jahrgang Berufspraxisstufe in Teilzeit laut AO-SF für den Förderschwerpunkt Geistige Entwicklung (je nach Organisationsform in Teilzeit (86) und Vollzeit (85) möglich) */
	BERUFSPRAXISSTUFE_TEILZEIT,

	/** 91: Abendrealschule Vorkurs, 1. Semester */
	REALSCHULE_VORKURS_SEMESTER_1,

	/** 92: Abendrealschule Vorkurs, 2. Semester */
	REALSCHULE_VORKURS_SEMESTER_2,

	/** Abendrealschule 1. Semester */
	REALSCHULE_SEMESTER_01,

	/** Abendrealschule 2. Semester */
	REALSCHULE_SEMESTER_02,

	/** Abendrealschule 3. Semester */
	REALSCHULE_SEMESTER_03,

	/** Abendrealschule 4. Semester */
	REALSCHULE_SEMESTER_04,


	/** Abendgymnasium/Kolleg: Vorkurs, 1. Semester */
	VORKURS_SEMESTER_1,

	/** Abendgymnasium/Kolleg: Vorkurs, 2. Semester */
	VORKURS_SEMESTER_2,

	/** Abendgymnasium/Kolleg: 1. Semester (EF.1) */
	SEMESTER_01,

	/** Abendgymnasium/Kolleg: 2. Semester (EF.2) */
	SEMESTER_02,

	/** Abendgymnasium/Kolleg: 3. Semester (Q1.1) */
	SEMESTER_03,

	/** Abendgymnasium/Kolleg: 4. Semester (Q1.2) */
	SEMESTER_04,

	/** Abendgymnasium/Kolleg: 5. Semester (Q2.1) */
	SEMESTER_05,

	/** Abendgymnasium/Kolleg: 6. Semester (Q2.2) */
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
	 * Gibt zurück, ob es sich bei diesem Jahrgang um einen Jahrgang der gymnasialen Oberstufe
	 * handelt oder nicht.
	 *
	 * @return true, wenn dieser ein Jahrgang der gymnasialen Oberstufe ist, und ansonsten false
	 */
	public boolean istGymOb() {
		return switch (this) {
			case EF, Q1, Q2, JAHRGANG_11, JAHRGANG_12, JAHRGANG_13 -> true;
			default -> false;
		};
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
			case HAUSFRUEHERZIEHUNG -> (vergleichsjahrgang == null);
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
			case EF -> (gl == Schulgliederung.GY8) ? (vergleichsjahrgang == JAHRGANG_09) : (vergleichsjahrgang == JAHRGANG_10);
			case Q1 -> (vergleichsjahrgang == EF);
			case Q2 -> (vergleichsjahrgang == Q1);
			case BERUFSPRAXISSTUFE_VOLLZEIT -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_TEILZEIT -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_2 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_1);
			case REALSCHULE_SEMESTER_01 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_2);
			case REALSCHULE_SEMESTER_02 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_01);
			case REALSCHULE_SEMESTER_03 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_02);
			case REALSCHULE_SEMESTER_04 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_03);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == VORKURS_SEMESTER_1);
			case SEMESTER_01 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case SEMESTER_02 -> (vergleichsjahrgang == SEMESTER_01);
			case SEMESTER_03 -> (vergleichsjahrgang == SEMESTER_02);
			case SEMESTER_04 -> (vergleichsjahrgang == SEMESTER_03);
			case SEMESTER_05 -> (vergleichsjahrgang == SEMESTER_04);
			case SEMESTER_06 -> (vergleichsjahrgang == SEMESTER_05);
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
			case HAUSFRUEHERZIEHUNG -> (vergleichsjahrgang == null);
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
			case EF -> (vergleichsjahrgang == Q1);
			case Q1 -> (vergleichsjahrgang == Q2);
			case Q2 -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_VOLLZEIT -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_TEILZEIT -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_1 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_2);
			case REALSCHULE_VORKURS_SEMESTER_2 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_01);
			case REALSCHULE_SEMESTER_01 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_02);
			case REALSCHULE_SEMESTER_02 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_03);
			case REALSCHULE_SEMESTER_03 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_04);
			case REALSCHULE_SEMESTER_04 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == SEMESTER_01);
			case SEMESTER_01 -> (vergleichsjahrgang == SEMESTER_02);
			case SEMESTER_02 -> (vergleichsjahrgang == SEMESTER_03);
			case SEMESTER_03 -> (vergleichsjahrgang == SEMESTER_04);
			case SEMESTER_04 -> (vergleichsjahrgang == SEMESTER_05);
			case SEMESTER_05 -> (vergleichsjahrgang == SEMESTER_06);
			case SEMESTER_06 -> (vergleichsjahrgang == null);
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
			case HAUSFRUEHERZIEHUNG -> (vergleichsjahrgang == null);
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
			case EF -> (vergleichsjahrgang == JAHRGANG_09) || (vergleichsjahrgang == JAHRGANG_10);
			case Q1 -> (vergleichsjahrgang == EF);
			case Q2 -> (vergleichsjahrgang == Q1);
			case BERUFSPRAXISSTUFE_VOLLZEIT -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_TEILZEIT -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_2 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_1);
			case REALSCHULE_SEMESTER_01 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_2);
			case REALSCHULE_SEMESTER_02 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_01);
			case REALSCHULE_SEMESTER_03 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_02);
			case REALSCHULE_SEMESTER_04 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_03);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == VORKURS_SEMESTER_1);
			case SEMESTER_01 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case SEMESTER_02 -> (vergleichsjahrgang == SEMESTER_01);
			case SEMESTER_03 -> (vergleichsjahrgang == SEMESTER_02);
			case SEMESTER_04 -> (vergleichsjahrgang == SEMESTER_03);
			case SEMESTER_05 -> (vergleichsjahrgang == SEMESTER_04);
			case SEMESTER_06 -> (vergleichsjahrgang == SEMESTER_05);
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
			case HAUSFRUEHERZIEHUNG -> (vergleichsjahrgang == null);
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
			case EF -> (vergleichsjahrgang == Q1);
			case Q1 -> (vergleichsjahrgang == Q2);
			case Q2 -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_VOLLZEIT -> (vergleichsjahrgang == null);
			case BERUFSPRAXISSTUFE_TEILZEIT -> (vergleichsjahrgang == null);
			case REALSCHULE_VORKURS_SEMESTER_1 -> (vergleichsjahrgang == REALSCHULE_VORKURS_SEMESTER_2);
			case REALSCHULE_VORKURS_SEMESTER_2 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_01);
			case REALSCHULE_SEMESTER_01 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_02);
			case REALSCHULE_SEMESTER_02 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_03);
			case REALSCHULE_SEMESTER_03 -> (vergleichsjahrgang == REALSCHULE_SEMESTER_04);
			case REALSCHULE_SEMESTER_04 -> (vergleichsjahrgang == null);
			case VORKURS_SEMESTER_1 -> (vergleichsjahrgang == VORKURS_SEMESTER_2);
			case VORKURS_SEMESTER_2 -> (vergleichsjahrgang == SEMESTER_01);
			case SEMESTER_01 -> (vergleichsjahrgang == SEMESTER_02);
			case SEMESTER_02 -> (vergleichsjahrgang == SEMESTER_03);
			case SEMESTER_03 -> (vergleichsjahrgang == SEMESTER_04);
			case SEMESTER_04 -> (vergleichsjahrgang == SEMESTER_05);
			case SEMESTER_05 -> (vergleichsjahrgang == SEMESTER_06);
			case SEMESTER_06 -> (vergleichsjahrgang == null);
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
