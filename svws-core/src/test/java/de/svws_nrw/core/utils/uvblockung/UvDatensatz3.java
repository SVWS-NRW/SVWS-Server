package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Diese Klasse stellt erweiterte Testdaten für die Unterrichtsverteilung bereit.
 *
 * <pre>
 * Erzeugt werden
 * - zwölf Lerngruppen,
 * - sechs Lehrkräfte,
 * - vier Klassen 5A, 5B, 6A und 6B,
 * - drei Fächer (Deutsch, Mathematik, Englisch),
 * - zwei Jahrgänge (5 und 6),
 * - vier Schülergruppen (für jede Klasse eine) mit je einem Schüler pro Schülergruppe.
 * </pre>
 *
 * Alle Daten sind so verknüpft, dass sie einen konsistenten, mittelgroßen Testfall für Regeln
 * der Unterrichtsverteilung bilden.
 *
 * @author Benjamin A. Bartsch
 */
class UvDatensatz3 {

	/** Die ID des Planungsabschnitts. */
	public static final long PLANUNGSABSCHNITT = 199L;

	/** Die ID der Lerngruppe 1 (5A Deutsch). */
	public static final long LG1 = 1L;

	/** Die ID der Lerngruppe 2 (5A Mathematik). */
	public static final long LG2 = 2L;

	/** Die ID der Lerngruppe 3 (5A Englisch). */
	public static final long LG3 = 3L;

	/** Die ID der Lerngruppe 4 (5B Deutsch). */
	public static final long LG4 = 4L;

	/** Die ID der Lerngruppe 5 (5B Mathematik). */
	public static final long LG5 = 5L;

	/** Die ID der Lerngruppe 6 (5B Englisch). */
	public static final long LG6 = 6L;

	/** Die ID der Lerngruppe 7 (6A Deutsch). */
	public static final long LG7 = 7L;

	/** Die ID der Lerngruppe 8 (6A Mathematik). */
	public static final long LG8 = 8L;

	/** Die ID der Lerngruppe 9 (6A Englisch). */
	public static final long LG9 = 9L;

	/** Die ID der Lerngruppe 10 (6B Deutsch). */
	public static final long LG10 = 10L;

	/** Die ID der Lerngruppe 11 (6B Mathematik). */
	public static final long LG11 = 11L;

	/** Die ID der Lerngruppe 12 (6B Englisch). */
	public static final long LG12 = 12L;

	/** Die ID der Klasse 5A. */
	public static final long KLASSE_5A = 21L;

	/** Die ID der Klasse 5B. */
	public static final long KLASSE_5B = 22L;

	/** Die ID der Klasse 6A. */
	public static final long KLASSE_6A = 23L;

	/** Die ID der Klasse 6B. */
	public static final long KLASSE_6B = 24L;

	/** Die ID des UV-Fachs Deutsch. */
	public static final long FACH_DE = 31L;

	/** Die ID des UV-Fachs Mathematik. */
	public static final long FACH_MA = 32L;

	/** Die ID des UV-Fachs Englisch. */
	public static final long FACH_EN = 33L;

	/** Die ID der FachDaten für Deutsch. */
	public static final long FACH_DATEN_DE = 41L;

	/** Die ID der FachDaten für Mathematik. */
	public static final long FACH_DATEN_MA = 42L;

	/** Die ID der FachDaten für Englisch. */
	public static final long FACH_DATEN_EN = 43L;

	/** Die ID der Lehrkraft 1 (Deutsch und Mathematik). */
	public static final long LEHRER1 = 51L;

	/** Die ID der Lehrkraft 2 (Mathematik und Englisch). */
	public static final long LEHRER2 = 52L;

	/** Die ID der Lehrkraft 3 (Deutsch und Englisch). */
	public static final long LEHRER3 = 53L;

	/** Die ID der Lehrkraft 4 (nur Mathematik). */
	public static final long LEHRER4 = 54L;

	/** Die ID der Lehrkraft 5 (nur Deutsch). */
	public static final long LEHRER5 = 55L;

	/** Die ID der Lehrkraft 6 (nur Englisch). */
	public static final long LEHRER6 = 56L;

	/** Die ID des Lehrer-Unterrichtsfachs 1. */
	public static final long LEHRER_UNTERRICHTSFACH1 = 61L;

	/** Die ID des Lehrer-Unterrichtsfachs 2. */
	public static final long LEHRER_UNTERRICHTSFACH2 = 62L;

	/** Die ID des Lehrer-Unterrichtsfachs 3. */
	public static final long LEHRER_UNTERRICHTSFACH3 = 63L;

	/** Die ID des Lehrer-Unterrichtsfachs 4. */
	public static final long LEHRER_UNTERRICHTSFACH4 = 64L;

	/** Die ID des Lehrer-Unterrichtsfachs 5. */
	public static final long LEHRER_UNTERRICHTSFACH5 = 65L;

	/** Die ID des Lehrer-Unterrichtsfachs 6. */
	public static final long LEHRER_UNTERRICHTSFACH6 = 66L;

	/** Die ID des Lehrer-Unterrichtsfachs 7. */
	public static final long LEHRER_UNTERRICHTSFACH7 = 67L;

	/** Die ID des Lehrer-Unterrichtsfachs 8. */
	public static final long LEHRER_UNTERRICHTSFACH8 = 68L;

	/** Die ID des Lehrer-Unterrichtsfachs 9. */
	public static final long LEHRER_UNTERRICHTSFACH9 = 69L;

	/** Die ID des Lehrer-Pflichtstundensolls 1. */
	public static final long PFLICHTSTUNDENSOLL1 = 71L;

	/** Die ID des Lehrer-Pflichtstundensolls 2. */
	public static final long PFLICHTSTUNDENSOLL2 = 72L;

	/** Die ID des Lehrer-Pflichtstundensolls 3. */
	public static final long PFLICHTSTUNDENSOLL3 = 73L;

	/** Die ID des Lehrer-Pflichtstundensolls 4. */
	public static final long PFLICHTSTUNDENSOLL4 = 74L;

	/** Die ID des Lehrer-Pflichtstundensolls 5. */
	public static final long PFLICHTSTUNDENSOLL5 = 75L;

	/** Die ID des Lehrer-Pflichtstundensolls 6. */
	public static final long PFLICHTSTUNDENSOLL6 = 76L;

	/** Die ID der Schülergruppe der Klasse 5A. */
	public static final long SCHUELERGRUPPE_5A = 81L;

	/** Die ID der Schülergruppe der Klasse 5B. */
	public static final long SCHUELERGRUPPE_5B = 82L;

	/** Die ID der Schülergruppe der Klasse 6A. */
	public static final long SCHUELERGRUPPE_6A = 83L;

	/** Die ID der Schülergruppe der Klasse 6B. */
	public static final long SCHUELERGRUPPE_6B = 84L;

	/** Die ID des Schülers in Klasse 5A. */
	public static final long SCHUELER_5A = 91L;

	/** Die ID des Schülers in Klasse 5B. */
	public static final long SCHUELER_5B = 92L;

	/** Die ID des Schülers in Klasse 6A. */
	public static final long SCHUELER_6A = 93L;

	/** Die ID des Schülers in Klasse 6B. */
	public static final long SCHUELER_6B = 94L;

	/** Die ID des Jahrgangs 5. */
	public static final long JAHRGANG_5 = 101L;

	/** Die ID des Jahrgangs 6. */
	public static final long JAHRGANG_6 = 102L;



	/**
	 * Erzeugt einen {@link UvManager} mit dem folgenden Zustand:
	 *
	 * <p>
	 * Vier Klassen (5A, 5B, 6A, 6B) in zwei Jahrgängen (5 und 6).
	 * Jede Klasse besitzt genau drei Lerngruppen in den Fächern Deutsch, Mathematik und Englisch.
	 * Sechs Lehrkräfte mit bewusst unterschiedlichen Lehrbefähigungen und Pflichtstunden-Sollwerten.
	 * Jede Klasse besitzt genau eine Schülergruppe mit genau einem Schüler.
	 * </p>
	 *
	 * @return ein {@link UvManager}-Objekt mit dem beschriebenen Zustand.
	 */
	public static UvManager erzeugeUvManager() {
		final UvPlanungsabschnitt pl1 = UvDatensatz.erzeugePlanungsabschnitt(PLANUNGSABSCHNITT, "2026-02-01", null);

		final FachDaten fdDe = UvDatensatz.erzeugeFachDaten(FACH_DATEN_DE, "D", "D", "Deutsch", true, 1);
		final FachDaten fdMa = UvDatensatz.erzeugeFachDaten(FACH_DATEN_MA, "MA", "MA", "Mathematik", true, 2);
		final FachDaten fdEn = UvDatensatz.erzeugeFachDaten(FACH_DATEN_EN, "E", "E", "Englisch", true, 3);

		final UvFach faDe = UvDatensatz.erzeugeUvFach(FACH_DE, FACH_DATEN_DE, "2026-01-01", null);
		final UvFach faMa = UvDatensatz.erzeugeUvFach(FACH_MA, FACH_DATEN_MA, "2026-01-01", null);
		final UvFach faEn = UvDatensatz.erzeugeUvFach(FACH_EN, FACH_DATEN_EN, "2026-01-01", null);

		final JahrgangsDaten jg5 = UvDatensatz.erzeugeJahrgang(JAHRGANG_5, "05", "5", "05", "Jahrgang 5", 5, "", true);
		final JahrgangsDaten jg6 = UvDatensatz.erzeugeJahrgang(JAHRGANG_6, "06", "6", "06", "Jahrgang 6", 6, "", true);

		final UvSchuelergruppe sg5A = UvDatensatz.erzeugeSchuelergruppe(SCHUELERGRUPPE_5A, PLANUNGSABSCHNITT, "5A GRUPPE", JAHRGANG_5);
		final UvSchuelergruppe sg5B = UvDatensatz.erzeugeSchuelergruppe(SCHUELERGRUPPE_5B, PLANUNGSABSCHNITT, "5B GRUPPE", JAHRGANG_5);
		final UvSchuelergruppe sg6A = UvDatensatz.erzeugeSchuelergruppe(SCHUELERGRUPPE_6A, PLANUNGSABSCHNITT, "6A GRUPPE", JAHRGANG_6);
		final UvSchuelergruppe sg6B = UvDatensatz.erzeugeSchuelergruppe(SCHUELERGRUPPE_6B, PLANUNGSABSCHNITT, "6B GRUPPE", JAHRGANG_6);

		final UvKlasse kl5A = UvDatensatz.erzeugeKlasse(KLASSE_5A, PLANUNGSABSCHNITT, "5A", SCHUELERGRUPPE_5A);
		final UvKlasse kl5B = UvDatensatz.erzeugeKlasse(KLASSE_5B, PLANUNGSABSCHNITT, "5B", SCHUELERGRUPPE_5B);
		final UvKlasse kl6A = UvDatensatz.erzeugeKlasse(KLASSE_6A, PLANUNGSABSCHNITT, "6A", SCHUELERGRUPPE_6A);
		final UvKlasse kl6B = UvDatensatz.erzeugeKlasse(KLASSE_6B, PLANUNGSABSCHNITT, "6B", SCHUELERGRUPPE_6B);

		final UvPlanungsabschnittSchueler psch5A = UvDatensatz.erzeugePlanungsabschnittSchueler(PLANUNGSABSCHNITT, SCHUELER_5A, JAHRGANG_5, KLASSE_5A);
		final UvPlanungsabschnittSchueler psch5B = UvDatensatz.erzeugePlanungsabschnittSchueler(PLANUNGSABSCHNITT, SCHUELER_5B, JAHRGANG_5, KLASSE_5B);
		final UvPlanungsabschnittSchueler psch6A = UvDatensatz.erzeugePlanungsabschnittSchueler(PLANUNGSABSCHNITT, SCHUELER_6A, JAHRGANG_6, KLASSE_6A);
		final UvPlanungsabschnittSchueler psch6B = UvDatensatz.erzeugePlanungsabschnittSchueler(PLANUNGSABSCHNITT, SCHUELER_6B, JAHRGANG_6, KLASSE_6B);

		final UvSchuelergruppeSchueler sgs5A = UvDatensatz.erzeugeSchuelergruppeSchueler(PLANUNGSABSCHNITT, SCHUELERGRUPPE_5A, SCHUELER_5A);
		final UvSchuelergruppeSchueler sgs5B = UvDatensatz.erzeugeSchuelergruppeSchueler(PLANUNGSABSCHNITT, SCHUELERGRUPPE_5B, SCHUELER_5B);
		final UvSchuelergruppeSchueler sgs6A = UvDatensatz.erzeugeSchuelergruppeSchueler(PLANUNGSABSCHNITT, SCHUELERGRUPPE_6A, SCHUELER_6A);
		final UvSchuelergruppeSchueler sgs6B = UvDatensatz.erzeugeSchuelergruppeSchueler(PLANUNGSABSCHNITT, SCHUELERGRUPPE_6B, SCHUELER_6B);

		final UvLehrer le1 = UvDatensatz.erzeugeLehrer(LEHRER1, "ADA", "Adams", "Anna", "2020-08-01", null);
		final UvLehrer le2 = UvDatensatz.erzeugeLehrer(LEHRER2, "BEN", "Bender", "Bernd", "2020-08-01", null);
		final UvLehrer le3 = UvDatensatz.erzeugeLehrer(LEHRER3, "CLA", "Clasen", "Clara", "2021-08-01", null);
		final UvLehrer le4 = UvDatensatz.erzeugeLehrer(LEHRER4, "DIR", "Dirks", "David", "2022-08-01", null);
		final UvLehrer le5 = UvDatensatz.erzeugeLehrer(LEHRER5, "EVA", "Evans", "Eva", "2023-08-01", null);
		final UvLehrer le6 = UvDatensatz.erzeugeLehrer(LEHRER6, "FIN", "Finke", "Finn", "2024-08-01", null);

		final UvLehrerPflichtstundensoll ps1 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL1, LEHRER1, "2026-01-01", null, 6.0);
		final UvLehrerPflichtstundensoll ps2 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL2, LEHRER2, "2026-01-01", null, 6.0);
		final UvLehrerPflichtstundensoll ps3 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL3, LEHRER3, "2026-01-01", null, 4.0);
		final UvLehrerPflichtstundensoll ps4 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL4, LEHRER4, "2026-01-01", null, 4.0);
		final UvLehrerPflichtstundensoll ps5 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL5, LEHRER5, "2026-01-01", null, 2.0);
		final UvLehrerPflichtstundensoll ps6 = UvDatensatz.erzeugePflichtstundensoll(PFLICHTSTUNDENSOLL6, LEHRER6, "2026-01-01", null, 2.0);

		final UvPlanungsabschnittLehrer pal1 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER1);
		final UvPlanungsabschnittLehrer pal2 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER2);
		final UvPlanungsabschnittLehrer pal3 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER3);
		final UvPlanungsabschnittLehrer pal4 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER4);
		final UvPlanungsabschnittLehrer pal5 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER5);
		final UvPlanungsabschnittLehrer pal6 = UvDatensatz.erzeugePlanungsabschnittLehrer(PLANUNGSABSCHNITT, LEHRER6);

		final LehrerUnterrichtsfach uf1 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH1, LEHRER1, FACH_DATEN_DE, true, true, false);
		final LehrerUnterrichtsfach uf2 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH2, LEHRER1, FACH_DATEN_MA, true, true, false);
		final LehrerUnterrichtsfach uf3 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH3, LEHRER2, FACH_DATEN_MA, true, true, false);
		final LehrerUnterrichtsfach uf4 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH4, LEHRER2, FACH_DATEN_EN, true, true, false);
		final LehrerUnterrichtsfach uf5 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH5, LEHRER3, FACH_DATEN_DE, true, true, false);
		final LehrerUnterrichtsfach uf6 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH6, LEHRER3, FACH_DATEN_EN, true, true, false);
		final LehrerUnterrichtsfach uf7 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH7, LEHRER4, FACH_DATEN_MA, true, true, false);
		final LehrerUnterrichtsfach uf8 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH8, LEHRER5, FACH_DATEN_DE, true, true, false);
		final LehrerUnterrichtsfach uf9 = UvDatensatz.erzeugeLehrerUnterrichtsfach(LEHRER_UNTERRICHTSFACH9, LEHRER6, FACH_DATEN_EN, true, true, false);

		final UvLerngruppe lg1 = UvDatensatz.erzeugeLerngruppe(LG1, PLANUNGSABSCHNITT, KLASSE_5A, FACH_DE, null, 2);
		final UvLerngruppe lg2 = UvDatensatz.erzeugeLerngruppe(LG2, PLANUNGSABSCHNITT, KLASSE_5A, FACH_MA, null, 2);
		final UvLerngruppe lg3 = UvDatensatz.erzeugeLerngruppe(LG3, PLANUNGSABSCHNITT, KLASSE_5A, FACH_EN, null, 2);
		final UvLerngruppe lg4 = UvDatensatz.erzeugeLerngruppe(LG4, PLANUNGSABSCHNITT, KLASSE_5B, FACH_DE, null, 2);
		final UvLerngruppe lg5 = UvDatensatz.erzeugeLerngruppe(LG5, PLANUNGSABSCHNITT, KLASSE_5B, FACH_MA, null, 2);
		final UvLerngruppe lg6 = UvDatensatz.erzeugeLerngruppe(LG6, PLANUNGSABSCHNITT, KLASSE_5B, FACH_EN, null, 2);
		final UvLerngruppe lg7 = UvDatensatz.erzeugeLerngruppe(LG7, PLANUNGSABSCHNITT, KLASSE_6A, FACH_DE, null, 2);
		final UvLerngruppe lg8 = UvDatensatz.erzeugeLerngruppe(LG8, PLANUNGSABSCHNITT, KLASSE_6A, FACH_MA, null, 2);
		final UvLerngruppe lg9 = UvDatensatz.erzeugeLerngruppe(LG9, PLANUNGSABSCHNITT, KLASSE_6A, FACH_EN, null, 2);
		final UvLerngruppe lg10 = UvDatensatz.erzeugeLerngruppe(LG10, PLANUNGSABSCHNITT, KLASSE_6B, FACH_DE, null, 2);
		final UvLerngruppe lg11 = UvDatensatz.erzeugeLerngruppe(LG11, PLANUNGSABSCHNITT, KLASSE_6B, FACH_MA, null, 2);
		final UvLerngruppe lg12 = UvDatensatz.erzeugeLerngruppe(LG12, PLANUNGSABSCHNITT, KLASSE_6B, FACH_EN, null, 2);

		final UvManager man = UvDatensatz.erzeugeUvManager(List.of(jg5, jg6), List.of(fdDe, fdMa, fdEn));
		man.planungsabschnittAdd(pl1);
		man.fachAdd(faDe);
		man.fachAdd(faMa);
		man.fachAdd(faEn);
		man.klasseAdd(kl5A);
		man.klasseAdd(kl5B);
		man.klasseAdd(kl6A);
		man.klasseAdd(kl6B);
		man.lehrerAdd(le1);
		man.lehrerAdd(le2);
		man.lehrerAdd(le3);
		man.lehrerAdd(le4);
		man.lehrerAdd(le5);
		man.lehrerAdd(le6);
		man.lehrerPflichtstundensollAdd(ps1);
		man.lehrerPflichtstundensollAdd(ps2);
		man.lehrerPflichtstundensollAdd(ps3);
		man.lehrerPflichtstundensollAdd(ps4);
		man.lehrerPflichtstundensollAdd(ps5);
		man.lehrerPflichtstundensollAdd(ps6);
		man.planungsabschnittLehrerAdd(pal1);
		man.planungsabschnittLehrerAdd(pal2);
		man.planungsabschnittLehrerAdd(pal3);
		man.planungsabschnittLehrerAdd(pal4);
		man.planungsabschnittLehrerAdd(pal5);
		man.planungsabschnittLehrerAdd(pal6);
		man.lehrerUnterrichtsfachAdd(uf1);
		man.lehrerUnterrichtsfachAdd(uf2);
		man.lehrerUnterrichtsfachAdd(uf3);
		man.lehrerUnterrichtsfachAdd(uf4);
		man.lehrerUnterrichtsfachAdd(uf5);
		man.lehrerUnterrichtsfachAdd(uf6);
		man.lehrerUnterrichtsfachAdd(uf7);
		man.lehrerUnterrichtsfachAdd(uf8);
		man.lehrerUnterrichtsfachAdd(uf9);
		man.lerngruppeAdd(lg1);
		man.lerngruppeAdd(lg2);
		man.lerngruppeAdd(lg3);
		man.lerngruppeAdd(lg4);
		man.lerngruppeAdd(lg5);
		man.lerngruppeAdd(lg6);
		man.lerngruppeAdd(lg7);
		man.lerngruppeAdd(lg8);
		man.lerngruppeAdd(lg9);
		man.lerngruppeAdd(lg10);
		man.lerngruppeAdd(lg11);
		man.lerngruppeAdd(lg12);
		man.schuelergruppeAdd(sg5A);
		man.schuelergruppeAdd(sg5B);
		man.schuelergruppeAdd(sg6A);
		man.schuelergruppeAdd(sg6B);
		man.planungsabschnittSchuelerAdd(psch5A);
		man.planungsabschnittSchuelerAdd(psch5B);
		man.planungsabschnittSchuelerAdd(psch6A);
		man.planungsabschnittSchuelerAdd(psch6B);
		man.schuelergruppeSchuelerAdd(sgs5A);
		man.schuelergruppeSchuelerAdd(sgs5B);
		man.schuelergruppeSchuelerAdd(sgs6A);
		man.schuelergruppeSchuelerAdd(sgs6B);
		return man;
	}
}
