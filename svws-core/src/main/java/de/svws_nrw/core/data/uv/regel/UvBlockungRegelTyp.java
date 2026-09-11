package de.svws_nrw.core.data.uv.regel;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die möglichen Regel-Typen einer {@link UvBlockungRegel}.
 */
public enum UvBlockungRegelTyp {

	/**
	 * Der Regel-Typ(0) - Undefiniert.
	 */
	UNDEFINIERT(0, "Undefiniert"),

	/**
	 * Default: Definiert, wie viele Klassenlehrer JEDE Klasse standardmäßig benötigt. Ohne diese Regel ist der Wert 1.
	 * <br>- Parameter A: Anzahl (long).
	 */
	KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER(1, "Default: Jede Klasse benötigt <A> Klassenlehrer."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel und definiert, wie viele Klassenlehrer eine bestimmte Klasse benötigt.
	 * <br>- Parameter A: Datenbank-ID der Klasse (long).
	 * <br>- Parameter B: Anzahl (long).
	 */
	KLASSE_A_BENOETIGT_B_KLASSENLEHRER(2, "Klasse <A> benötigt <B> Klassenlehrer (Individuell)."),

	/**
	 * Default: Definiert, wie viele stellv. Klassenlehrer JEDE Klasse standardmäßig benötigt.
	 * <br>- Parameter A: Anzahl (long).
	 */
	KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER(3, "Default: Jede Klasse benötigt <A> stellv. Klassenlehrer."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel und definiert, wie viele stellv. Klassenlehrer eine bestimmte Klasse benötigt.
	 * <br>- Parameter A: Datenbank-ID der Klasse (long).
	 * <br>- Parameter B: Anzahl (long).
	 */
	KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER(4, "Klasse <A> benötigt <B> stellv. Klassenlehrer (Individuell)."),

	/**
	 * Default: Definiert die Mindeststundenanzahl, die ein Klassenlehrer in der eigenen Klasse unterrichten muss.
	 * <br>- Parameter A: Mindeststundenanzahl (long).
	 */
	LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE(5,
			"Default: Wenn Lehrkraft Klassenlehrer ist, dann mindestens <A> Stunden in der Klasse."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel für die Mindeststundenanzahl eines Klassenlehrers in einer bestimmten Klasse.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Mindeststundenanzahl (long).
	 */
	LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE(6,
			"Wenn Lehrkraft <A> Klassenlehrer ist, dann mindestens <B> Stunden in der Klasse (Individuell)."),

	/**
	 * Default: Definiert die Mindeststundenanzahl, die ein stellv. Klassenlehrer in der eigenen Klasse unterrichten muss.
	 * <br>- Parameter A: Mindeststundenanzahl (long).
	 */
	LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE(7,
			"Default: Wenn Lehrkraft stellv. Klassenlehrer ist, dann mindestens <A> Stunden in der Klasse."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel für die Mindeststundenanzahl eines stellv. Klassenlehrers in einer bestimmten Klasse.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Mindeststundenanzahl (long).
	 */
	LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE(8,
			"Wenn Lehrkraft <A> stellv. Klassenlehrer ist, dann mindestens <B> Stunden in der Klasse (Individuell)."),

	/**
	 * Default: Definiert, wie oft JEDE Lehrkraft maximal als Klassenlehrer eingesetzt werden darf.
	 * <br>- Parameter A: Maximal-Anzahl (long).
	 */
	LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER(9, "Default: Jede Lehrkraft ist maximal <A> mal Klassenlehrer."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel für eine bestimmte Lehrkraft, wie oft sie maximal Klassenlehrer sein darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Maximal-Anzahl (long).
	 */
	LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER(10, "Lehrkraft <A> ist maximal <B> mal Klassenlehrer (Individuell)."),

	/**
	 * Default: Definiert, wie oft JEDE Lehrkraft maximal als stellv. Klassenlehrer eingesetzt werden darf.
	 * <br>- Parameter A: Maximal-Anzahl (long).
	 */
	LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER(11, "Default: Jede Lehrkraft ist maximal <A> mal stellv. Klassenlehrer."),

	/**
	 * Spezifisch: Überschreibt die Default-Regel für eine bestimmte Lehrkraft, wie oft sie maximal stellv. Klassenlehrer sein darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Maximal-Anzahl (long).
	 */
	LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER(12, "Lehrkraft <A> ist maximal <B> mal stellv. Klassenlehrer (Individuell)."),

	/**
	 * Verbietet eine Lehrkraft in einer Lerngruppe.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B(13, "Lehrkraft <A> ist verboten in Lerngruppe <B>."),

	/**
	 * Fixiert eine Lehrkraft in einer Lerngruppe.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B(14, "Lehrkraft <A> ist fixiert in Lerngruppe <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft gerne einer Lerngruppe zugeordnet wird.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B(15, "Lehrkraft <A> gerne in Lerngruppe <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft ungerne einer Lerngruppe zugeordnet wird.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B(16, "Lehrkraft <A> ungerne in Lerngruppe <B>."),

	/**
	 * Legt fest, dass eine Lerngruppe zu einem bestimmten Zeitpunkt stattfinden soll. Nötig um KOOP-Konflikte früh zu erkennen.
	 * <br>- Parameter A: Datenbank-ID der Lerngruppe (long).
	 * <br>- Parameter B: Wochentag (long).
	 * <br>- Parameter C: Stunde (long).
	 * <br>- Parameter D: Wochentyp (long). 0 = Jede Woche, 1 = Nur A-Woche, 2 = Nur B-Woche, 3 = Nur C-Woche, ...
	 */
	LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D(17, "Lerngruppe <A> liegt am Wochentag <B> in Stunde <C> für Wochentyp <D>."),

	/**
	 * Definiert, dass eine Lehrkraft in einer kompletten Klasse nicht unterrichten darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B(18, "Lehrkraft <A> ist verboten in Klasse <B>."),

	/**
	 * Definiert, dass eine Lehrkraft in einer kompletten Jahrgangsstufe nicht unterrichten darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Jahrgang (long).
	 */
	LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B(19, "Lehrkraft <A> ist verboten in Jahrgangsstufe <B>."),

	/**
	 * Definiert, dass eine Lehrkraft mindestens X Lerngruppen eines bestimmten Faches unterrichten soll.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 * <br>- Parameter C: Datenbank-ID des Faches (long).
	 */
	LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C(20, "Lehrkraft <A> hat mindestens <B> mal Fach <C>."),

	/**
	 * Definiert, dass eine Lehrkraft maximal X Lerngruppen eines bestimmten Faches unterrichten darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 * <br>- Parameter C: Datenbank-ID des Faches (long).
	 */
	LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C(21, "Lehrkraft <A> hat maximal <B> mal Fach <C>."),

	/**
	 * Definiert, dass eine Lehrkraft mindestens X Korrekturen übernehmen soll.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 */
	LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN(22, "Lehrkraft <A> hat mindestens <B> Korrekturen."),

	/**
	 * Definiert, dass eine Lehrkraft maximal X Korrekturen übernehmen darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 */
	LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN(23, "Lehrkraft <A> hat maximal <B> Korrekturen."),

	/**
	 * Verbietet einer Lehrkraft, ein bestimmtes Fach in bestimmten Jahrgangsstufen zu unterrichten,
	 * auch wenn eine Lehrbefähigung vorhanden ist.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID des Faches (long).
	 * <br>- Parameter ...: Datenbank-IDs der Jahrgangsstufen (long).
	 */
	LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN(24, "Lehrkraft <A> darf Fach <B> nicht unterrichten in den Stufen <...>."),

	/**
	 * Verbietet einer Lehrkraft, ein bestimmtes Fach generell zu unterrichten,
	 * auch wenn eine Lehrbefähigung vorhanden ist.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID des Faches (long).
	 */
	LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN(25, "Lehrkraft <A> darf Fach <B> generell nicht unterrichten."),

	/**
	 * Begrenzt die Anzahl verschiedener Jahrgangsstufen, in denen eine Lehrkraft eingesetzt wird.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Maximal-Anzahl verschiedener Stufen (long).
	 */
	LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE(26, "Lehrkraft <A> hat maximal <B> verschiedene Jahrgangsstufen."),

	/**
	 * Verhindert, dass zwei bestimmte Lehrkräfte gleichzeitig in derselben Klasse unterrichten.
	 * <br>- Parameter A: Datenbank-ID der ersten Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der zweiten Lehrkraft (long).
	 */
	LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE(27, "Lehrkräfte <A> und <B> nicht in der selben Klasse."),

	/**
	 * Definiert, dass eine Lerngruppe abweichend von 1 genau X Lehrkräfte benötigt.
	 * <br>- Parameter A: Datenbank-ID der Lerngruppe (long).
	 * <br>- Parameter B: Anzahl (long).
	 */
	LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE(28, "Lerngruppe <A> benötigt <B> Lehrkräfte."),

	/**
	 * Forciert, dass eine Menge von Lerngruppen zwingend dieselbe Lehrkraft erhalten muss.
	 * <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT(29, "Lerngruppen <...> erhalten dieselbe Lehrkraft."),

	/**
	 * Erzwingt, dass eine Menge von Lerngruppen paarweise verschiedene Lehrkräfte erhalten.
	 * <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE(30, "Lerngruppen <...> erhalten verschiedene Lehrkräfte."),

	/**
	 * Definiert, dass bestimmte Lerngruppen dieselbe Stundenkürzung erhalten dürfen, bis zu einem Mindestwert.
	 * <br>- Parameter A: Mindeststundenwert (long).
	 * <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN(31, "Gemeinsame Stundenkürzung auf <A> Stunden bei den Lerngruppen <...>."),

	/**
	 * Fixiert eine Lehrkraft als Klassenlehrer einer bestimmten Klasse.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B(32, "Lehrkraft <A> ist fixiert als Klassenlehrer in Klasse <B>."),

	/**
	 * Fixiert eine Lehrkraft als stellv. Klassenlehrer einer bestimmten Klasse.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B(33, "Lehrkraft <A> ist fixiert als stellv. Klassenlehrer in Klasse <B>."),

	/**
	 * Verbietet einer Lehrkraft, Klassenlehrer einer bestimmten Klasse zu sein.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B(34, "Lehrkraft <A> ist verboten als Klassenlehrer in Klasse <B>."),

	/**
	 * Verbietet einer Lehrkraft, stellv. Klassenlehrer einer bestimmten Klasse zu sein.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B(35, "Lehrkraft <A> ist verboten als stellv. Klassenlehrer in Klasse <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft gerne Klassenlehrer einer bestimmten Klasse sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B(36, "Lehrkraft <A> gerne als Klassenlehrer in Klasse <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft gerne stellv. Klassenlehrer einer bestimmten Klasse sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B(37, "Lehrkraft <A> gerne als stellv. Klassenlehrer in Klasse <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft ungerne Klassenlehrer einer bestimmten Klasse sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B(38, "Lehrkraft <A> ungerne als Klassenlehrer in Klasse <B>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft ungerne stellv. Klassenlehrer einer bestimmten Klasse sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B(39, "Lehrkraft <A> ungerne als stellv. Klassenlehrer in Klasse <B>."),

	/**
	 * Begrenzt die Gesamtanzahl an Lerngruppen, die eine Lehrkraft haben darf.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Maximal-Anzahl der Lerngruppen (long).
	 */
	LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN(40, "Lehrkraft <A> hat maximal <B> Lerngruppen."),

	/**
	 * Legt die Mindestanzahl an Lerngruppen fest, die eine Lehrkraft haben soll.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Mindest-Anzahl der Lerngruppen (long).
	 */
	LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN(41, "Lehrkraft <A> hat mindestens <B> Lerngruppen."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft mindestens eine bestimmte Anzahl mal in einer Menge von Lerngruppen sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 * <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN(42, "Lehrkraft <A> mindestens <B> mal in Lerngruppen <...>."),

	/**
	 * Definiert den Wunsch, dass eine Lehrkraft maximal eine bestimmte Anzahl mal in einer Menge von Lerngruppen sein möchte.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Anzahl (long).
	 * <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN(43, "Lehrkraft <A> maximal <B> mal in Lerngruppen <...>."),

	/**
	 * Definiert, ob die SOLL-IST-Abweichung aller Lehrkräfte aktiviert ist.
	 * <br>- Parameter A: Aktivierung (0=Nein, 1=Ja).
	 */
	LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A(44, "Default: Jede Lehrkraft hat <A> (0=Nein, 1=Ja)  Soll-Ist-Abweichung."),

	/**
	 * Definiert, ob die SOLL-IST-Abweichung der Lehrkraft aktiviert ist.
	 * <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 * <br>- Parameter B: Aktivierung (0=Nein, 1=Ja).
	 */
	LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B(45, "Lehrkraft <A> hat <B> (0=Nein, 1=Ja) Soll-Ist-Abweichung.");


	/** Die NR der Regel. */
	public final int nr;

	/** Die Bezeichnung der Regel. */
	public final String bezeichnung;

	/**
	 * Erstellt einen neuen Regel-Typ.
	 *
	 * @param nr            die Nummer des Typs.
	 * @param bezeichnung   die textuelle Bezeichnung des Typs.
	 */
	UvBlockungRegelTyp(final int nr, final @NotNull String bezeichnung) {
		this.nr = nr;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Liefert das ENUM zur übergebenen Nummer.
	 *
	 * @param nr   Die Nummer des Enums.
	 *
	 * @return das ENUM zur übergebenen Nummer.
	 */
	public static @NotNull UvBlockungRegelTyp ofNr(final int nr) {
		for (final UvBlockungRegelTyp t : values()) {
			if (t.nr == nr) {
				return t;
			}
		}
		return UNDEFINIERT;
	}

}
