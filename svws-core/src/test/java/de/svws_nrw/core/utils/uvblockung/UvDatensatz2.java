package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Diese Klasse stellt einfache Testdaten für die Unterrichtsverteilung bereit.
 *
 * <pre>
 * Erzeugt werden
 * - zwei Lerngruppen,
 * - zwei Lehrkräfte,
 * - zwei Klassen 5A und 5B,
 * - ein Fach,
 * - einen Jahrgang (5),
 * - zwei Schülergruppen (für die 5A und für die 5B) mit je einem Schüler pro Schülergruppe.
 * </pre>
 *
 * Alle Daten sind so verknüpft, dass sie einen konsistenten Minimalfall für Tests bilden.
 *
 * @author Benjamin A. Bartsch
 */
class UvDatensatz2 {

	/** Die ID des Planungsabschnitts. */
	public static final long ID_PLANUNGSABSCHNITT = 99L;

	/** Die ID der Lerngruppe 1. */
	public static final long ID_LERNGRUPPE1 = 1L;

	/** Die ID der Lerngruppe 2. */
	public static final long ID_LERNGRUPPE2 = 2L;

	/** Die ID der Klasse 1. */
	public static final long ID_KLASSE1 = 4L;

	/** Die ID der Klasse 2. */
	public static final long ID_KLASSE2 = 7L;

	/** Die ID des UV-Fachs. */
	public static final long ID_FACH = 11L;

	/** Die ID der FachDaten. */
	public static final long ID_FACH_DATEN = 10L;

	/** Die ID des Lehrers 1. */
	public static final long ID_LEHRER1 = 5L;

	/** Die ID des Lehrers 2. */
	public static final long ID_LEHRER2 = 6L;

	/** Die ID des Lehrer-Unterrichtsfachs 1. */
	public static final long ID_LEHRER_UNTERRICHTSFACH1 = 20L;

	/** Die ID des Lehrer-Unterrichtsfachs 2. */
	public static final long ID_LEHRER_UNTERRICHTSFACH2 = 21L;

	/** Die ID des Lehrer-Pflichtstundensolls 1. */
	public static final long ID_PFLICHTSTUNDENSOLL1 = 1L;

	/** Die ID des Lehrer-Pflichtstundensolls 2. */
	public static final long ID_PFLICHTSTUNDENSOLL2 = 2L;

	/** Die ID der Schülergruppe 1. */
	public static final long ID_SCHUELERGRUPPE1 = 8L;

	/** Die ID der Schülergruppe 2. */
	public static final long ID_SCHUELERGRUPPE2 = 9L;

	/** Die ID des Schülers 1. */
	public static final long ID_SCHUELER1 = 12L;

	/** Die ID des Schülers 2. */
	public static final long ID_SCHUELER2 = 13L;

	/** Die ID des Jahrgangs. */
	public static final long ID_JAHRGANG = 14L;

	/**
	 * Erzeugt einen {@link UvManager} mit dem folgenden Zustand:
	 *
	 * <p>
	 * Zwei Lerngruppen (ID=1, ID=2), zwei Lehrer (ID=5, ID=6), zwei Klassen (ID=4, ID=7).
	 * Jede Lerngruppe gehört zu genau einer Klasse und einem Fach.
	 * Beide Lehrer haben Lehrbefähigung für das Fach und je 2 Std. Pflichtstunden-Soll.
	 * Beide Klassen haben jeweils eine Schülergruppe.
	 * Beide Schülergruppen haben jeweils genau einen Schüler.
	 * Beide Klassen liegen im selben Jahrgang 5.
	 * </p>
	 *
	 * @return ein {@link UvManager}-Objekt mit dem beschriebenen Zustand.
	 */
	public static UvManager erzeugeUvManager() {
		final UvManager man = UvDatensatz.erzeugeUvManager(
				List.of(UvDatensatz.erzeugeJahrgang(ID_JAHRGANG, "05", "5", "05", "Jahrgang 5", 5, "", true)),
				List.of(UvDatensatz.erzeugeFachDaten(ID_FACH_DATEN, "MA", "MA", "Mathematik", true, 1)));

		man.planungsabschnittAdd(UvDatensatz.erzeugePlanungsabschnitt(ID_PLANUNGSABSCHNITT, "2026-02-01", null));
		man.fachAdd(UvDatensatz.erzeugeUvFach(ID_FACH, ID_FACH_DATEN, "2026-01-01", null));
		man.klasseAdd(UvDatensatz.erzeugeKlasse(ID_KLASSE1, ID_PLANUNGSABSCHNITT, "5A", ID_SCHUELERGRUPPE1));
		man.klasseAdd(UvDatensatz.erzeugeKlasse(ID_KLASSE2, ID_PLANUNGSABSCHNITT, "5B", ID_SCHUELERGRUPPE2));
		man.lehrerAdd(UvDatensatz.erzeugeLehrer(ID_LEHRER1, "MUS", "Mustermann", "Max", "2020-08-01", null));
		man.lehrerAdd(UvDatensatz.erzeugeLehrer(ID_LEHRER2, "SCH", "Schmidt", "Anna", "2021-08-01", null));
		man.lehrerPflichtstundensollAdd(UvDatensatz.erzeugePflichtstundensoll(ID_PFLICHTSTUNDENSOLL1, ID_LEHRER1, "2026-01-01", null, 2.0));
		man.lehrerPflichtstundensollAdd(UvDatensatz.erzeugePflichtstundensoll(ID_PFLICHTSTUNDENSOLL2, ID_LEHRER2, "2026-01-01", null, 2.0));
		man.planungsabschnittLehrerAdd(UvDatensatz.erzeugePlanungsabschnittLehrer(ID_PLANUNGSABSCHNITT, ID_LEHRER1));
		man.planungsabschnittLehrerAdd(UvDatensatz.erzeugePlanungsabschnittLehrer(ID_PLANUNGSABSCHNITT, ID_LEHRER2));
		man.lehrerUnterrichtsfachAdd(UvDatensatz.erzeugeLehrerUnterrichtsfach(ID_LEHRER_UNTERRICHTSFACH1, ID_LEHRER1, ID_FACH_DATEN, true, true, false));
		man.lehrerUnterrichtsfachAdd(UvDatensatz.erzeugeLehrerUnterrichtsfach(ID_LEHRER_UNTERRICHTSFACH2, ID_LEHRER2, ID_FACH_DATEN, true, true, false));
		man.lerngruppeAdd(UvDatensatz.erzeugeLerngruppe(ID_LERNGRUPPE1, ID_PLANUNGSABSCHNITT, ID_KLASSE1, ID_FACH, null, 2));
		man.lerngruppeAdd(UvDatensatz.erzeugeLerngruppe(ID_LERNGRUPPE2, ID_PLANUNGSABSCHNITT, ID_KLASSE2, ID_FACH, null, 2));
		man.schuelergruppeAdd(UvDatensatz.erzeugeSchuelergruppe(ID_SCHUELERGRUPPE1, ID_PLANUNGSABSCHNITT, "5A GRUPPE", ID_JAHRGANG));
		man.schuelergruppeAdd(UvDatensatz.erzeugeSchuelergruppe(ID_SCHUELERGRUPPE2, ID_PLANUNGSABSCHNITT, "5B GRUPPE", ID_JAHRGANG));
		man.planungsabschnittSchuelerAdd(UvDatensatz.erzeugePlanungsabschnittSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELER1, ID_JAHRGANG, ID_KLASSE1));
		man.planungsabschnittSchuelerAdd(UvDatensatz.erzeugePlanungsabschnittSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELER2, ID_JAHRGANG, ID_KLASSE2));
		man.schuelergruppeSchuelerAdd(UvDatensatz.erzeugeSchuelergruppeSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELERGRUPPE1, ID_SCHUELER1));
		man.schuelergruppeSchuelerAdd(UvDatensatz.erzeugeSchuelergruppeSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELERGRUPPE2, ID_SCHUELER2));
		return man;
	}
}
