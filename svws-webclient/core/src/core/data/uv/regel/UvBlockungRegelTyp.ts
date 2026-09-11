import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { Class } from '../../../../java/lang/Class';

export class UvBlockungRegelTyp extends JavaEnum<UvBlockungRegelTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<UvBlockungRegelTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, UvBlockungRegelTyp> = new Map<string, UvBlockungRegelTyp>();

	/**
	 *  Der Regel-Typ(0) - Undefiniert.
	 */
	public static readonly UNDEFINIERT: UvBlockungRegelTyp = new UvBlockungRegelTyp("UNDEFINIERT", 0, 0, "Undefiniert");

	/**
	 *  Default: Definiert, wie viele Klassenlehrer JEDE Klasse standardmäßig benötigt. Ohne diese Regel ist der Wert 1.
	 *  <br>- Parameter A: Anzahl (long).
	 */
	public static readonly KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER", 1, 1, "Default: Jede Klasse benötigt <A> Klassenlehrer.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel und definiert, wie viele Klassenlehrer eine bestimmte Klasse benötigt.
	 *  <br>- Parameter A: Datenbank-ID der Klasse (long).
	 *  <br>- Parameter B: Anzahl (long).
	 */
	public static readonly KLASSE_A_BENOETIGT_B_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("KLASSE_A_BENOETIGT_B_KLASSENLEHRER", 2, 2, "Klasse <A> benötigt <B> Klassenlehrer (Individuell).");

	/**
	 *  Default: Definiert, wie viele stellv. Klassenlehrer JEDE Klasse standardmäßig benötigt.
	 *  <br>- Parameter A: Anzahl (long).
	 */
	public static readonly KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER", 3, 3, "Default: Jede Klasse benötigt <A> stellv. Klassenlehrer.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel und definiert, wie viele stellv. Klassenlehrer eine bestimmte Klasse benötigt.
	 *  <br>- Parameter A: Datenbank-ID der Klasse (long).
	 *  <br>- Parameter B: Anzahl (long).
	 */
	public static readonly KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER", 4, 4, "Klasse <A> benötigt <B> stellv. Klassenlehrer (Individuell).");

	/**
	 *  Default: Definiert die Mindeststundenanzahl, die ein Klassenlehrer in der eigenen Klasse unterrichten muss.
	 *  <br>- Parameter A: Mindeststundenanzahl (long).
	 */
	public static readonly LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE", 5, 5, "Default: Wenn Lehrkraft Klassenlehrer ist, dann mindestens <A> Stunden in der Klasse.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel für die Mindeststundenanzahl eines Klassenlehrers in einer bestimmten Klasse.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Mindeststundenanzahl (long).
	 */
	public static readonly LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE", 6, 6, "Wenn Lehrkraft <A> Klassenlehrer ist, dann mindestens <B> Stunden in der Klasse (Individuell).");

	/**
	 *  Default: Definiert die Mindeststundenanzahl, die ein stellv. Klassenlehrer in der eigenen Klasse unterrichten muss.
	 *  <br>- Parameter A: Mindeststundenanzahl (long).
	 */
	public static readonly LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE", 7, 7, "Default: Wenn Lehrkraft stellv. Klassenlehrer ist, dann mindestens <A> Stunden in der Klasse.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel für die Mindeststundenanzahl eines stellv. Klassenlehrers in einer bestimmten Klasse.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Mindeststundenanzahl (long).
	 */
	public static readonly LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE", 8, 8, "Wenn Lehrkraft <A> stellv. Klassenlehrer ist, dann mindestens <B> Stunden in der Klasse (Individuell).");

	/**
	 *  Default: Definiert, wie oft JEDE Lehrkraft maximal als Klassenlehrer eingesetzt werden darf.
	 *  <br>- Parameter A: Maximal-Anzahl (long).
	 */
	public static readonly LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER", 9, 9, "Default: Jede Lehrkraft ist maximal <A> mal Klassenlehrer.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel für eine bestimmte Lehrkraft, wie oft sie maximal Klassenlehrer sein darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Maximal-Anzahl (long).
	 */
	public static readonly LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER", 10, 10, "Lehrkraft <A> ist maximal <B> mal Klassenlehrer (Individuell).");

	/**
	 *  Default: Definiert, wie oft JEDE Lehrkraft maximal als stellv. Klassenlehrer eingesetzt werden darf.
	 *  <br>- Parameter A: Maximal-Anzahl (long).
	 */
	public static readonly LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER", 11, 11, "Default: Jede Lehrkraft ist maximal <A> mal stellv. Klassenlehrer.");

	/**
	 *  Spezifisch: Überschreibt die Default-Regel für eine bestimmte Lehrkraft, wie oft sie maximal stellv. Klassenlehrer sein darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Maximal-Anzahl (long).
	 */
	public static readonly LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER", 12, 12, "Lehrkraft <A> ist maximal <B> mal stellv. Klassenlehrer (Individuell).");

	/**
	 *  Verbietet eine Lehrkraft in einer Lerngruppe.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	public static readonly LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B", 13, 13, "Lehrkraft <A> ist verboten in Lerngruppe <B>.");

	/**
	 *  Fixiert eine Lehrkraft in einer Lerngruppe.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	public static readonly LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B", 14, 14, "Lehrkraft <A> ist fixiert in Lerngruppe <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft gerne einer Lerngruppe zugeordnet wird.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	public static readonly LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B", 15, 15, "Lehrkraft <A> gerne in Lerngruppe <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft ungerne einer Lerngruppe zugeordnet wird.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Lerngruppe (long).
	 */
	public static readonly LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B", 16, 16, "Lehrkraft <A> ungerne in Lerngruppe <B>.");

	/**
	 *  Legt fest, dass eine Lerngruppe zu einem bestimmten Zeitpunkt stattfinden soll. Nötig um KOOP-Konflikte früh zu erkennen.
	 *  <br>- Parameter A: Datenbank-ID der Lerngruppe (long).
	 *  <br>- Parameter B: Wochentag (long).
	 *  <br>- Parameter C: Stunde (long).
	 *  <br>- Parameter D: Wochentyp (long). 0 = Jede Woche, 1 = Nur A-Woche, 2 = Nur B-Woche, 3 = Nur C-Woche, ...
	 */
	public static readonly LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D: UvBlockungRegelTyp = new UvBlockungRegelTyp("LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D", 17, 17, "Lerngruppe <A> liegt am Wochentag <B> in Stunde <C> für Wochentyp <D>.");

	/**
	 *  Definiert, dass eine Lehrkraft in einer kompletten Klasse nicht unterrichten darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B", 18, 18, "Lehrkraft <A> ist verboten in Klasse <B>.");

	/**
	 *  Definiert, dass eine Lehrkraft in einer kompletten Jahrgangsstufe nicht unterrichten darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Jahrgang (long).
	 */
	public static readonly LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B", 19, 19, "Lehrkraft <A> ist verboten in Jahrgangsstufe <B>.");

	/**
	 *  Definiert, dass eine Lehrkraft mindestens X Lerngruppen eines bestimmten Faches unterrichten soll.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 *  <br>- Parameter C: Datenbank-ID des Faches (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C", 20, 20, "Lehrkraft <A> hat mindestens <B> mal Fach <C>.");

	/**
	 *  Definiert, dass eine Lehrkraft maximal X Lerngruppen eines bestimmten Faches unterrichten darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 *  <br>- Parameter C: Datenbank-ID des Faches (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C", 21, 21, "Lehrkraft <A> hat maximal <B> mal Fach <C>.");

	/**
	 *  Definiert, dass eine Lehrkraft mindestens X Korrekturen übernehmen soll.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN", 22, 22, "Lehrkraft <A> hat mindestens <B> Korrekturen.");

	/**
	 *  Definiert, dass eine Lehrkraft maximal X Korrekturen übernehmen darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN", 23, 23, "Lehrkraft <A> hat maximal <B> Korrekturen.");

	/**
	 *  Verbietet einer Lehrkraft, ein bestimmtes Fach in bestimmten Jahrgangsstufen zu unterrichten,
	 *  auch wenn eine Lehrbefähigung vorhanden ist.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID des Faches (long).
	 *  <br>- Parameter ...: Datenbank-IDs der Jahrgangsstufen (long).
	 */
	public static readonly LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN", 24, 24, "Lehrkraft <A> darf Fach <B> nicht unterrichten in den Stufen <...>.");

	/**
	 *  Verbietet einer Lehrkraft, ein bestimmtes Fach generell zu unterrichten,
	 *  auch wenn eine Lehrbefähigung vorhanden ist.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID des Faches (long).
	 */
	public static readonly LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN", 25, 25, "Lehrkraft <A> darf Fach <B> generell nicht unterrichten.");

	/**
	 *  Begrenzt die Anzahl verschiedener Jahrgangsstufen, in denen eine Lehrkraft eingesetzt wird.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Maximal-Anzahl verschiedener Stufen (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE", 26, 26, "Lehrkraft <A> hat maximal <B> verschiedene Jahrgangsstufen.");

	/**
	 *  Verhindert, dass zwei bestimmte Lehrkräfte gleichzeitig in derselben Klasse unterrichten.
	 *  <br>- Parameter A: Datenbank-ID der ersten Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der zweiten Lehrkraft (long).
	 */
	public static readonly LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE", 27, 27, "Lehrkräfte <A> und <B> nicht in der selben Klasse.");

	/**
	 *  Definiert, dass eine Lerngruppe abweichend von 1 genau X Lehrkräfte benötigt.
	 *  <br>- Parameter A: Datenbank-ID der Lerngruppe (long).
	 *  <br>- Parameter B: Anzahl (long).
	 */
	public static readonly LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE", 28, 28, "Lerngruppe <A> benötigt <B> Lehrkräfte.");

	/**
	 *  Forciert, dass eine Menge von Lerngruppen zwingend dieselbe Lehrkraft erhalten muss.
	 *  <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	public static readonly LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT: UvBlockungRegelTyp = new UvBlockungRegelTyp("LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT", 29, 29, "Lerngruppen <...> erhalten dieselbe Lehrkraft.");

	/**
	 *  Erzwingt, dass eine Menge von Lerngruppen paarweise verschiedene Lehrkräfte erhalten.
	 *  <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	public static readonly LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE: UvBlockungRegelTyp = new UvBlockungRegelTyp("LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE", 30, 30, "Lerngruppen <...> erhalten verschiedene Lehrkräfte.");

	/**
	 *  Definiert, dass bestimmte Lerngruppen dieselbe Stundenkürzung erhalten dürfen, bis zu einem Mindestwert.
	 *  <br>- Parameter A: Mindeststundenwert (long).
	 *  <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	public static readonly LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN", 31, 31, "Gemeinsame Stundenkürzung auf <A> Stunden bei den Lerngruppen <...>.");

	/**
	 *  Fixiert eine Lehrkraft als Klassenlehrer einer bestimmten Klasse.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B", 32, 32, "Lehrkraft <A> ist fixiert als Klassenlehrer in Klasse <B>.");

	/**
	 *  Fixiert eine Lehrkraft als stellv. Klassenlehrer einer bestimmten Klasse.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B", 33, 33, "Lehrkraft <A> ist fixiert als stellv. Klassenlehrer in Klasse <B>.");

	/**
	 *  Verbietet einer Lehrkraft, Klassenlehrer einer bestimmten Klasse zu sein.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B", 34, 34, "Lehrkraft <A> ist verboten als Klassenlehrer in Klasse <B>.");

	/**
	 *  Verbietet einer Lehrkraft, stellv. Klassenlehrer einer bestimmten Klasse zu sein.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B", 35, 35, "Lehrkraft <A> ist verboten als stellv. Klassenlehrer in Klasse <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft gerne Klassenlehrer einer bestimmten Klasse sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B", 36, 36, "Lehrkraft <A> gerne als Klassenlehrer in Klasse <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft gerne stellv. Klassenlehrer einer bestimmten Klasse sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B", 37, 37, "Lehrkraft <A> gerne als stellv. Klassenlehrer in Klasse <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft ungerne Klassenlehrer einer bestimmten Klasse sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B", 38, 38, "Lehrkraft <A> ungerne als Klassenlehrer in Klasse <B>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft ungerne stellv. Klassenlehrer einer bestimmten Klasse sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Datenbank-ID der Klasse (long).
	 */
	public static readonly LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B", 39, 39, "Lehrkraft <A> ungerne als stellv. Klassenlehrer in Klasse <B>.");

	/**
	 *  Begrenzt die Gesamtanzahl an Lerngruppen, die eine Lehrkraft haben darf.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Maximal-Anzahl der Lerngruppen (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN", 40, 40, "Lehrkraft <A> hat maximal <B> Lerngruppen.");

	/**
	 *  Legt die Mindestanzahl an Lerngruppen fest, die eine Lehrkraft haben soll.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Mindest-Anzahl der Lerngruppen (long).
	 */
	public static readonly LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN", 41, 41, "Lehrkraft <A> hat mindestens <B> Lerngruppen.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft mindestens eine bestimmte Anzahl mal in einer Menge von Lerngruppen sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 *  <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	public static readonly LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN", 42, 42, "Lehrkraft <A> mindestens <B> mal in Lerngruppen <...>.");

	/**
	 *  Definiert den Wunsch, dass eine Lehrkraft maximal eine bestimmte Anzahl mal in einer Menge von Lerngruppen sein möchte.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Anzahl (long).
	 *  <br>- Parameter ...: Datenbank-IDs der Lerngruppen (long).
	 */
	public static readonly LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN", 43, 43, "Lehrkraft <A> maximal <B> mal in Lerngruppen <...>.");

	/**
	 *  Definiert, ob die SOLL-IST-Abweichung aller Lehrkräfte aktiviert ist.
	 *  <br>- Parameter A: Aktivierung (0=Nein, 1=Ja).
	 */
	public static readonly LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A", 44, 44, "Default: Jede Lehrkraft hat <A> (0=Nein, 1=Ja)  Soll-Ist-Abweichung.");

	/**
	 *  Definiert, ob die SOLL-IST-Abweichung der Lehrkraft aktiviert ist.
	 *  <br>- Parameter A: Datenbank-ID der Lehrkraft (long).
	 *  <br>- Parameter B: Aktivierung (0=Nein, 1=Ja).
	 */
	public static readonly LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B: UvBlockungRegelTyp = new UvBlockungRegelTyp("LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B", 45, 45, "Lehrkraft <A> hat <B> (0=Nein, 1=Ja) Soll-Ist-Abweichung.");

	/**
	 * Die NR der Regel.
	 */
	public readonly nr: number;

	/**
	 * Die Bezeichnung der Regel.
	 */
	public readonly bezeichnung: string | null;

	/**
	 * Erstellt einen neuen Regel-Typ.
	 *
	 * @param nr            die Nummer des Typs.
	 * @param bezeichnung   die textuelle Bezeichnung des Typs.
	 */
	private constructor(name: string, ordinal: number, nr: number, bezeichnung: string) {
		super(name, ordinal);
		UvBlockungRegelTyp.all_values_by_ordinal.push(this);
		UvBlockungRegelTyp.all_values_by_name.set(name, this);
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
	public static ofNr(nr: number): UvBlockungRegelTyp {
		for (const t of this.values()) {
			if (t.nr === nr) {
				return t;
			}
		}
		return UvBlockungRegelTyp.UNDEFINIERT;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<UvBlockungRegelTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): UvBlockungRegelTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<UvBlockungRegelTyp>('de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp');

}

export function cast_de_svws_nrw_core_data_uv_regel_UvBlockungRegelTyp(obj: unknown): UvBlockungRegelTyp {
	return obj as UvBlockungRegelTyp;
}
