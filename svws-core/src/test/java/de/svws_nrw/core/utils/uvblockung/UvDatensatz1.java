package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Diese Klasse stellt simple Daten zum Testen zur Verfügung.
 * Die IDs dürfen nicht verändert werden, da sonst die Unit-Tests fehlschlagen.
 *
 * @author Benjamin A. Bartsch
 */
class UvDatensatz1 {

	/** Die ID des Planungsabschnitts. */
	public static final long ID_PLANUNGSABSCHNITT = 99L;

	/** Die ID der Lerngruppe. */
	public static final long ID_LERNGRUPPE = 1L;

	/** Die ID der Klasse. */
	public static final long ID_KLASSE = 4L;

	/** Die ID des UV-Fachs. */
	public static final long ID_FACH = 3L;

	/** Die ID des Lehrers. */
	public static final long ID_LEHRER = 5L;

	/** Die ID der FachDaten. */
	public static final long ID_FACH_DATEN = 2L;

	/** Die ID des Lehrer-Unterrichtsfachs. */
	public static final long ID_LEHRER_UNTERRICHTSFACH = 6L;

	/** Die ID des Lehrer-Unterrichtsfachs. */
	public static final long ID_PFLICHTSTUNDENSOLL = 7L;

	/** Die ID der Schülergruppe der Klasse. */
	public static final long ID_SCHUELERGRUPPE = 8L;

	/** Die ID des Schülers. */
	public static final long ID_SCHUELER = 9L;

	/** Die ID des Jahrgangs. */
	public static final long JAHRGANG = 10L;

	/**
	 * Erzeugt einen {@link UvManager} mit dem folgenden Zustand:
	 *
	 * <pre>
	 * - Genau 1 Planungsabschnitt.
	 * - Genau 1 Klasse, welche im Planungsabschnitt ist.
	 * - Genau 1 Fach, welches die Fachdaten referenziert.
	 * - Genau 1 Lehrer, welcher im Planungsabschnitt ist.
	 *     --> und hat ein Pflichtstundensoll von 2 hat.
	 *     --> und darf das Fach unterrichten.
	 * - Genau 1 Lerngruppe
	 *     --> hat das Fach.
	 *     --> hat die Klasse.
	 *     --> hat den Planungsabschnitt.
	 * - Genau 1 Jahrgang (Stufe 5)
	 * - Genau 1 Schülergruppe für die Klasse (mit Erlaubnis für den Jahrgang)
	 * - Genau 1 Schüler (im Planungsabschnitt und der Schülergruppe zugeordnet)
	 * </pre>
	 *
	 * @return einen {@link UvManager} mit dem beschriebenen Zustand.
	 */
	public static UvManager erzeugeUvManager() {
		final UvManager man = UvDatensatz.erzeugeUvManager(
				List.of(UvDatensatz.erzeugeJahrgang(JAHRGANG, "05", "5", "05", "Stufe 5", 5, "", true)),
				List.of(UvDatensatz.erzeugeFachDaten(ID_FACH_DATEN, "MA", "MA", "Mathematik", true, 1)));
		man.planungsabschnittAdd(UvDatensatz.erzeugePlanungsabschnitt(ID_PLANUNGSABSCHNITT, "2026-02-01", null));
		man.fachAdd(UvDatensatz.erzeugeUvFach(ID_FACH, ID_FACH_DATEN, "2026-01-01", null));
		man.klasseAdd(UvDatensatz.erzeugeKlasse(ID_KLASSE, ID_PLANUNGSABSCHNITT, "5A", ID_SCHUELERGRUPPE));
		man.lehrerAdd(UvDatensatz.erzeugeLehrer(ID_LEHRER, "MUS", "Mustermann", "Max", "2020-08-01", null));
		man.lehrerPflichtstundensollAdd(UvDatensatz.erzeugePflichtstundensoll(ID_PFLICHTSTUNDENSOLL, ID_LEHRER, "2026-01-01", null, 2.0));
		man.planungsabschnittLehrerAdd(UvDatensatz.erzeugePlanungsabschnittLehrer(ID_PLANUNGSABSCHNITT, ID_LEHRER));
		man.lehrerUnterrichtsfachAdd(UvDatensatz.erzeugeLehrerUnterrichtsfach(ID_LEHRER_UNTERRICHTSFACH, ID_LEHRER, ID_FACH_DATEN, true, true, false));
		man.lerngruppeAdd(UvDatensatz.erzeugeLerngruppe(ID_LERNGRUPPE, ID_PLANUNGSABSCHNITT, ID_KLASSE, ID_FACH, null, 2));
		man.schuelergruppeAdd(UvDatensatz.erzeugeSchuelergruppe(ID_SCHUELERGRUPPE, ID_PLANUNGSABSCHNITT, "5A GRUPPE", JAHRGANG));
		man.planungsabschnittSchuelerAdd(UvDatensatz.erzeugePlanungsabschnittSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELER, JAHRGANG, ID_KLASSE));
		man.schuelergruppeSchuelerAdd(UvDatensatz.erzeugeSchuelergruppeSchueler(ID_PLANUNGSABSCHNITT, ID_SCHUELERGRUPPE, ID_SCHUELER));
		return man;
	}

}
