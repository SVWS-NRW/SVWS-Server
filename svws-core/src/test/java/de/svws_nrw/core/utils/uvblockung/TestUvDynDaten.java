package de.svws_nrw.core.utils.uvblockung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Testet die Klasse {@link UvAlgorithmusDynDaten} zur dynamischen Manipulation von Unterrichtsverteilungs-Daten.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den UvAlgorithmus.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestUvDynDaten {

	/** Durch einen eigenen Logger, werden die absichtlichen ERROR-Meldungen konsumiert. */
	private static final Logger LOG = new Logger();

	private static final Random RND = new Random(1L);

	/**
	 * Initialisierung der ASD-Core-Types vor allen Tests.
	 * Ohne diese Initialisierung können Enums und Core-Types nicht korrekt aufgelöst werden.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	/**
	 * Erzeugt einen {@link UvAlgorithmusDynDaten}-Zustand mit vollständig leerer Datenmenge.
	 * Weder Lehrer, noch Lerngruppen, noch Fächer oder Klassen sind vorhanden.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt ohne jegliche Daten.
	 */
	private static UvAlgorithmusDynDaten gibDatenLeer() {
		final UvManager man = new UvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, new ArrayList<>());
		final UvPlanungsabschnitt pla = new UvPlanungsabschnitt();
		return new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, pla);
	}


	/**
	 * Erzeugt einen {@link UvAlgorithmusDynDaten}-Zustand:
	 *
	 * <p>
	 * Genau eine Lerngruppe.
	 * Genau ein Lehrer im Planungsabschnitt.
	 * Der Lehrer hat die Lehrbefähigung für das Fach, d.h. er kann zugeordnet werden.
	 * Der Lehrer hat ein Pflichtstunden-Soll von 2 Unterrichtsstunden.
	 * Es gibt keine Regeln.
	 *
	 * Der Malus der Kategorie SEHR_HOCH ist 3:
	 * Es fehlt eine Lehrkraft, eine Klassenleitung und eine stellv. Klassenleitung.
	 *
	 * Der Malus der Kategorie MITTEL ist 4:
	 * Die Lehrkraft-SOLL-IST-Stunden-Abweichung ist (2-0)² = 4.
	 * </p>
	 *
	 * @param regeln   die für den Test zu verwendenden Blockungsregeln.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt mit minimaler Datenmenge.
	 */
	private static UvAlgorithmusDynDaten gibDatenMinimal1(final List<UvBlockungRegel> regeln) {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		final @NotNull UvPlanungsabschnitt planungsabschnitt = man.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		return new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, planungsabschnitt);
	}


	/**
	 * Erzeugt einen {@link UvAlgorithmusDynDaten}-Zustand:
	 *
	 * <p>
	 * Zwei Lerngruppen (ID=1, ID=2), zwei Lehrer (ID=5, ID=6), zwei Klassen (ID=4, ID=7).
	 * Jede Lerngruppe gehört zu genau einer Klasse und einem Fach.
	 * Beide Lehrer haben Lehrbefähigung für das Fach und je 2 Std. Pflichtstunden-Soll.
	 *
	 * Ausgangssituation ohne Regeln:
	 * SEHR_HOCH = 6 (je 1 fehlende Lehrkraft + 1 Klassenleitung + 1 stellv. Klassenleitung pro Klasse)
	 * MITTEL    = 8 (je Lehrer (2-0)² = 4, zusammen 8)
	 * </p>
	 *
	 * @param regeln   die für den Test zu verwendenden Blockungsregeln.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt mit zwei Klassen, zwei Lerngruppen und zwei Lehrern.
	 */
	private static UvAlgorithmusDynDaten gibDatenMinimal2(final List<UvBlockungRegel> regeln) {
		final UvManager man = UvDatensatz2.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		final @NotNull UvPlanungsabschnitt planungsabschnitt = man.planungsabschnittGetByIdOrException(UvDatensatz2.ID_PLANUNGSABSCHNITT);
		return new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, planungsabschnitt);
	}


	/**
	 * Erzeugt einen {@link UvAlgorithmusDynDaten}-Zustand:
	 *
	 * Berechnung des Anfangs-Malus:
	 *
	 *
	 * Es gibt 4 Klassen:
	 * 8 Malus-HOCH, weil Leitung 1 und 2 fehlt.
	 *
	 * Es gibt 12 Lerngruppen je 2 Bedarf an SOLL-Stunden:
	 * 12 Malus-HOCH
	 *
	 * Also insgesamt 20 Malus-HOCH.
	 *
	 *
	 * Es gibt 6 Lehrkräfte mit SOLL 6, 6, 4, 4, 2, 2:
	 * 6² + 6² + 4² + 4² + 2² + 2² = 112 Malus-MITTEL.
	 *
	 * Also insgesamt 112 Malus-MITTEL.
	 *
	 *
	 * @param regeln   die für den Test zu verwendenden Blockungsregeln.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt mit zwei Klassen, zwei Lerngruppen und zwei Lehrern.
	 */
	private static UvAlgorithmusDynDaten gibDatenMinimal3(final List<UvBlockungRegel> regeln) {
		final UvManager man = UvDatensatz3.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		final @NotNull UvPlanungsabschnitt planungsabschnitt = man.planungsabschnittGetByIdOrException(UvDatensatz3.PLANUNGSABSCHNITT);
		return new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, planungsabschnitt);
	}


	private static UvBlockungRegel gibRegel01KlasseDefaultBenoetigtKlassenlehrer(final int anzahl, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) anzahl);
		return regel;
	}


	private static UvBlockungRegel gibRegel02KlasseBenoetigtKlassenlehrer(final long idKlasse, final int anzahl, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idKlasse);
		regel.parameter.add((long) anzahl);
		return regel;
	}


	private static UvBlockungRegel gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(final int anzahl, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) anzahl);
		return regel;
	}


	private static UvBlockungRegel gibRegel04KlasseBenoetigtStellvKlassenlehrer(final long idKlasse, final int anzahl, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idKlasse);
		regel.parameter.add((long) anzahl);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel05LehrkraftDefaultWennKlassenlehrerDann(final int minStunden, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) minStunden);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel06LehrkraftAWennKlassenlehrerDann(final long idLehrer, final int minStunden, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) minStunden);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(final int minStunden, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) minStunden);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel08LehrkraftAWennStellvKlassenlehrerDann(final long idLehrer, final int minStunden, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) minStunden);
		return regel;
	}


	private static UvBlockungRegel gibRegel09LehrerDefaultMaxKlassenlehrer(final int max, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) max);
		return regel;
	}


	private static UvBlockungRegel gibRegel10LehrerMaxKlassenlehrer(final long idLehrer, final int max, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) max);
		return regel;
	}


	private static UvBlockungRegel gibRegel11LehrerDefaultMaxStellvKlassenlehrer(final int max, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) max);
		return regel;
	}


	private static UvBlockungRegel gibRegel12LehrerMaxStellvKlassenlehrer(final long idLehrer, final int max, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) max);
		return regel;
	}


	private static UvBlockungRegel gibRegel13LehrkraftVerbotenInLerngruppe(final long idLehrer, final long idLerngruppe, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idLerngruppe);
		return regel;
	}

	private static UvBlockungRegel gibRegel14LehrkraftFixiertInLerngruppe(final long idLehrer, final long idLerngruppe, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idLerngruppe);
		return regel;
	}


	private static UvBlockungRegel gibRegel15LehrkraftGerneInLerngruppe(final long idLehrer, final long idLerngruppe, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idLerngruppe);
		return regel;
	}


	private static UvBlockungRegel gibRegel16LehrkraftUngerneInLerngruppe(final long idLehrer, final long idLerngruppe, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idLerngruppe);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(
			final long idLerngruppe,
			final long wochentag,
			final long stunde,
			final long wochentyp,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLerngruppe);
		regel.parameter.add(wochentag);
		regel.parameter.add(stunde);
		regel.parameter.add(wochentyp);
		return regel;
	}


	private static UvBlockungRegel gibRegel18LehrkraftVerbotenInKlasse(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel19LehrkraftVerbotenInStufe(final long idLehrer, final long idJahrgang, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idJahrgang);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel20LehrkraftHatMindestensBMalFachC(
			final long idLehrer,
			final int anzahl,
			final long idFach,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		regel.parameter.add(idFach);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel21LehrkraftHatMaximalBMalFachC(
			final long idLehrer,
			final int anzahl,
			final long idFach,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		regel.parameter.add(idFach);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel22LehrkraftHatMindestensNKorrekturen(
			final long idLehrer, final int anzahl, final int prioritaet) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel23LehrkraftHatMaximalNKorrekturen(final long idLehrer, final int anzahl, final int prioritaet) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		return regel;
	}

	private static @NotNull UvBlockungRegel gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
			final long idLehrkraft,
			final long idFach,
			final @NotNull List<Long> idsJahrgaenge,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrkraft);
		regel.parameter.add(idFach);
		regel.parameter.addAll(idsJahrgaenge);

		return regel;
	}


	private static UvBlockungRegel gibRegel25LehrkraftADarfFachBNichtUnterrichten(final long idLehrer, final long idFach, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idFach);
		return regel;
	}


	private static UvBlockungRegel gibRegel26LehrkraftAIstInMaximalBJahrgaengen(final long idLehrkraft, final int maxJahrgaenge, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrkraft);
		regel.parameter.add((long) maxJahrgaenge);
		return regel;
	}


	private static UvBlockungRegel gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(
			final long idLehrerA, final long idLehrerB, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrerA);
		regel.parameter.add(idLehrerB);
		return regel;
	}


	private static UvBlockungRegel gibRegel28LerngruppeBenoetigtBLehrkraefte(final long idLerngruppe, final long anzahl, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLerngruppe);
		regel.parameter.add(anzahl);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel29LerngruppenHabenSelbeLehrkraft(final int prioritaet, final long... idLerngruppen) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = -1;
		regel.typ = UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT.nr;
		regel.prioritaet = prioritaet;
		regel.parameter = new ArrayList<>();

		for (int i = 0; i < idLerngruppen.length; i++) {
			regel.parameter.add(idLerngruppen[i]);
		}

		return regel;
	}

	private static @NotNull UvBlockungRegel gibRegel30LerngruppenVerschiedeneLehrkraefte(final int prioritaet, final long... idLerngruppen) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = -1;
		regel.typ = UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE.nr;
		regel.prioritaet = prioritaet;
		regel.parameter = new ArrayList<>();

		for (int i = 0; i < idLerngruppen.length; i++) {
			regel.parameter.add(idLerngruppen[i]);
		}

		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(
			final long minStunden,
			final int prioritaet,
			final long... idLerngruppen) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = -1;
		regel.typ = UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter = new ArrayList<>();
		regel.parameter.add(minStunden);

		for (final long idLerngruppe : idLerngruppen) {
			regel.parameter.add(idLerngruppe);
		}

		return regel;
	}


	private static UvBlockungRegel gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel34LehrkraftVerbotenAlsLeitung1(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel35LehrkraftVerbotenAlsLeitung2(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel36LehrkraftGerneAlsLeitung1(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel37LehrkraftGerneAlsLeitung2(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel38LehrkraftUngerneAlsLeitung1(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}

	private static UvBlockungRegel gibRegel39LehrkraftUngerneAlsLeitung2(final long idLehrer, final long idKlasse, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add(idKlasse);
		return regel;
	}


	private static UvBlockungRegel gibRegel40LehrkraftHatMaximalBLerngruppen(final long idLehrer, final int max, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) max);
		return regel;
	}


	private static UvBlockungRegel gibRegel41LehrkraftHatMindestensBLerngruppen(final long idLehrer, final int min, final int prioritaet) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) min);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel42LehrkraftMindestensBMalInLerngruppen(
			final long idLehrer,
			final int anzahl,
			final @NotNull List<Long> idsLerngruppen,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		regel.parameter.addAll(idsLerngruppen);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel43LehrkraftMaximalBMalInLerngruppen(
			final long idLehrer,
			final int anzahl,
			final @NotNull List<Long> idsLerngruppen,
			final int prioritaet) {

		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrer);
		regel.parameter.add((long) anzahl);
		regel.parameter.addAll(idsLerngruppen);

		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel44LehrkraftDefaultSollIstAktivierung(final int aktivierung, final int prioritaet) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add((long) aktivierung);
		return regel;
	}


	private static @NotNull UvBlockungRegel gibRegel45LehrkraftSollIstAktivierung(final long idLehrkraft, final int aktivierung, final int prioritaet) {
		final @NotNull UvBlockungRegel regel = new UvBlockungRegel();
		regel.typ = UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B.nr;
		regel.prioritaet = prioritaet;
		regel.parameter.add(idLehrkraft);
		regel.parameter.add((long) aktivierung);
		return regel;
	}


	private static void checkMalus(final UvAlgorithmusDynDaten dyn, final int prio0, final int prio1, final int prio2, final int prio3, final int prio4) {
		final String sError = dyn.gibMalusBeschreibung() + " statt [%d, %d, %d, %d, %d]".formatted(prio0, prio1, prio2, prio3, prio4);

		final int currentPrio0 = dyn.gibMalusDerPrioritaet(UvBlockungRegelPrioritaet.SEHR_HOCH);
		assertEquals(prio0, currentPrio0, sError);

		final int currentPrio1 = dyn.gibMalusDerPrioritaet(UvBlockungRegelPrioritaet.HOCH);
		assertEquals(prio1, currentPrio1, sError);

		final int currentPrio2 = dyn.gibMalusDerPrioritaet(UvBlockungRegelPrioritaet.MITTEL);
		assertEquals(prio2, currentPrio2, sError);

		final int currentPrio3 = dyn.gibMalusDerPrioritaet(UvBlockungRegelPrioritaet.GERING);
		assertEquals(prio3, currentPrio3, sError);

		final int currentPrio4 = dyn.gibMalusDerPrioritaet(UvBlockungRegelPrioritaet.SEHR_GERING);
		assertEquals(prio4, currentPrio4, sError);
	}


	@Test
	@DisplayName("testLeer: Leere Datenmenge darf nicht abstürzen.")
	void testLeer() {
		assertDoesNotThrow(() -> {
			// Malus - Vorher
			final UvAlgorithmusDynDaten dyn = gibDatenLeer();
			checkMalus(dyn, 0, 0, 0, 0, 0);

			// Malus - Nachher 1
			dyn.strategieLerngruppeLehrkraftHinzufuegen();
			checkMalus(dyn, 0, 0, 0, 0, 0);

			// Malus - Nachher 2
			dyn.strategieLerngruppeLehrkraftEntfernen();
			checkMalus(dyn, 0, 0, 0, 0, 0);
			assertEquals(0, dyn.gibAktuelleZuordnung().size());
		});
	}


	@Test
	@DisplayName("testRegel00LerngruppeLehrkraft: Genau eine Lehrkraft und eine Lerngruppe, keine Regeln")
	void testRegel00LerngruppeLehrkraft() {
		// Szenario 1: Ausgangszustand ohne Zuordnung.
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(new ArrayList<>());
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Vor der Optimierung sollte es keine Zuordnung geben.");

		// Szenario 2: Lehrkraft wird gezielt zugeordnet.
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(new ArrayList<>());
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		final List<UvLerngruppenLehrer> zuordnung2 = dyn2.gibAktuelleZuordnung();
		assertEquals(1, zuordnung2.size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, zuordnung2.get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, zuordnung2.get(0).idLehrer, "Lehrer ID=5 erwartet.");

		// Szenario 3: Dieselbe Zuordnung wird wieder entfernt.
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(new ArrayList<>());
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn3.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		final List<UvLerngruppenLehrer> zuordnung3 = dyn3.gibAktuelleZuordnung();
		assertEquals(0, zuordnung3.size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 4: Fixierte Zuordnung kann nicht entfernt werden.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertThrows(DeveloperNotificationException.class, () -> dyn4.setzeLerngruppeLehrkraftDel(1L, 5L));

		// Szenario 5: Eine nicht vorhandene Beziehung zu löschen wirft eine Exception.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertThrows(DeveloperNotificationException.class, () -> dyn5.setzeLerngruppeLehrkraftDel(1L, 5L));

		// Szenario 6: Doppelte Zuordnung wirft eine Exception.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		assertThrows(DeveloperNotificationException.class, () -> dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
	}


	@Test
	@DisplayName("testRegel00KlasseKlassenleitung1: Eine Klasse benötigt standardmäßig eine Klassenleitung.")
	void testRegel00KlasseKlassenleitung1() {
		// Szenario 1: Ausgangszustand ohne Klassenleitungs-Zuordnung.
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(new ArrayList<>());
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es keine Zuordnung geben.");

		// Szenario 2: Klassenleitung wird deterministisch zugeordnet.
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(new ArrayList<>());
		dyn2.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn2, 2, 0, 4, 0, 0);

		final List<UvKlassenLehrer> zuordnung2 = dyn2.gibAktuelleKlassenleitungen();
		assertEquals(1, zuordnung2.size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, zuordnung2.get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, zuordnung2.get(0).idLehrer, "Lehrer ID=5 erwartet.");

		// Szenario 3: Klassenleitung wird wieder entfernt.
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(new ArrayList<>());
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn3.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		assertEquals(0, dyn3.gibAktuelleKlassenleitungen().size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 4: Doppelt hinzufügen wirft eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		assertThrows(DeveloperNotificationException.class, () -> dyn4.setzeKlassenLeitung1Add(4L, 5L, false));

		// Szenario 5: Entfernen ohne Zuordnung wirft eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertThrows(DeveloperNotificationException.class, () -> dyn5.setzeKlassenLeitung1Del(4L, 5L));

		// Szenario 6: Fixierte Klassenleitung kann nicht entfernt werden.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertThrows(DeveloperNotificationException.class, () -> dyn6.setzeKlassenLeitung1Del(4L, 5L));

		// Szenario 7: Add -> Del -> Add funktioniert wieder sauber.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(new ArrayList<>());
		dyn7.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn7.setzeKlassenLeitung1Del(4L, 5L);
		dyn7.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn7, 2, 0, 4, 0, 0);
		assertEquals(1, dyn7.gibAktuelleKlassenleitungen().size(), "Zuordnung muss nach erneutem Add wieder vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel00KlasseKlassenleitung2: Eine Klasse benötigt standardmäßig eine stellv. Leitung.")
	void testRegel00KlasseKlassenleitung2() {
		// Szenario 1: Ausgangszustand ohne stellv. Klassenleitungs-Zuordnung.
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(new ArrayList<>());
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es keine Zuordnung geben.");

		// Szenario 2: Stellv. Klassenleitung wird deterministisch zugeordnet.
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(new ArrayList<>());
		dyn2.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn2, 2, 0, 4, 0, 0);
		final List<UvKlassenLehrer> zuordnung2 = dyn2.gibAktuelleKlassenleitungen();
		assertEquals(1, zuordnung2.size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, zuordnung2.get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, zuordnung2.get(0).idLehrer, "Lehrer ID=5 erwartet.");

		// Szenario 3: Stellv. Klassenleitung wird wieder entfernt.
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(new ArrayList<>());
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn3.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		final List<UvKlassenLehrer> zuordnung3 = dyn3.gibAktuelleKlassenleitungen();
		assertEquals(0, zuordnung3.size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 4: Doppelt hinzufügen wirft eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		assertThrows(DeveloperNotificationException.class, () -> dyn4.setzeKlassenLeitung2Add(4L, 5L, false));

		// Szenario 5: Entfernen ohne Zuordnung wirft eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertThrows(DeveloperNotificationException.class, () -> dyn5.setzeKlassenLeitung2Del(4L, 5L));

		// Szenario 6: Fixierte stellv. Klassenleitung kann nicht entfernt werden.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertThrows(DeveloperNotificationException.class, () -> dyn6.setzeKlassenLeitung2Del(4L, 5L));

		// Szenario 7: Add -> Del -> Add funktioniert wieder sauber.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(new ArrayList<>());
		dyn7.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn7.setzeKlassenLeitung2Del(4L, 5L);
		dyn7.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn7, 2, 0, 4, 0, 0);
		assertEquals(1, dyn7.gibAktuelleKlassenleitungen().size(), "Zuordnung muss nach erneutem Add wieder vorhanden sein.");

		// Szenario 8: Andere Lehrkraft wird als stellv. Klassenleitung gesetzt.
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(new ArrayList<>());
		dyn8.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn8, 2, 0, 4, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Es sollte genau eine stellv. Klassenleitung existieren.");
	}


	@Test
	@DisplayName("testRegel00RandomWalkOhneRegeln")
	void testRegel00RandomWalkOhneRegeln() {
		final UvAlgorithmusDynDaten dyn = gibDatenMinimal2(new ArrayList<>());
		final int[] malusStart = dyn.gibMalusKopie();

		for (int i = 0; i < 1000000; i++) {
			switch (RND.nextInt(6)) {
				case 0 -> dyn.manipulationLerngruppeLehrkraftAdd();
				case 1 -> dyn.manipulationLerngruppeLehrkraftDel();
				case 2 -> dyn.manipulationKlassenleitung1Add();
				case 3 -> dyn.manipulationKlassenleitung1Del();
				case 4 -> dyn.manipulationKlassenleitung2Add();
				case 5 -> dyn.manipulationKlassenleitung2Del();
				default -> throw new IllegalStateException("Unerwarteter Zufallswert.");
			}

			final int[] malusAktuell = dyn.gibMalusKopie();
			for (final int m : malusAktuell) {
				assertTrue(m >= 0, "Malus darf nicht negativ sein. Er ist aber " + m + "!");
			}

			if (RND.nextDouble() < 0.10) {
				dyn.undoAll();
				final int[] malusNachUndo = dyn.gibMalusKopie();
				assertEquals(malusStart.length, malusNachUndo.length);
				for (int j = 0; j < malusStart.length; j++) {
					assertEquals(malusStart[j], malusNachUndo[j], "Nach undoAll muss der Start-Malus wiederhergestellt sein.");
				}
			}
		}
	}


	@Test
	@DisplayName("testRegel01KlasseDefaultBenoetigtKlassenlehrer: Default-Soll für Klassenleitung wird global gesetzt.")
	void testRegel01KlasseDefaultBenoetigtKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, alle gültigen Sollwerte von MIN bis MAX werden geprüft.
		for (int soll = UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN; soll <= UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX; soll++) {
			final UvBlockungRegel regel1 = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(soll, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
			final int erwarteterMalusSehrHoch = 1 + soll + 1;
			checkMalus(dyn1, erwarteterMalusSehrHoch, 0, 4, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, es soll kein zusätzlicher Regelmalus entstehen.
		final UvBlockungRegel regel2 = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(0, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Kein Parameter -> Regel wird aussortiert.
		final UvBlockungRegel regel3 = new UvBlockungRegel();
		regel3.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr;
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Ungültiger Wert = MIN - 1 -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(
				UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Ungültiger Wert = MAX + 1 -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(
				UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Deterministische Klassenleitung 1 wird gesetzt und wieder entfernt.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn6.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn6.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		dyn6.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn6, 3, 0, 4, 0, 0);
		assertEquals(0, dyn6.gibAktuelleKlassenleitungen().size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 7: Doppelt hinzufügen erzeugt eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(new ArrayList<>());
		dyn7.setzeKlassenLeitung1Add(4L, 5L, false);
		assertThrows(DeveloperNotificationException.class, () -> dyn7.setzeKlassenLeitung1Add(4L, 5L, false));

		// Szenario 8: Entfernen ohne Zuordnung erzeugt eine DeveloperNotificationException.
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(new ArrayList<>());
		assertThrows(DeveloperNotificationException.class, () -> dyn8.setzeKlassenLeitung1Del(4L, 5L));

		// Szenario 9: Fixierte Zuordnung kann nicht entfernt werden.
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(new ArrayList<>());
		dyn9.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn9, 2, 0, 4, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertThrows(DeveloperNotificationException.class, () -> dyn9.setzeKlassenLeitung1Del(4L, 5L));

		// Szenario 10: Add -> Del -> Add funktioniert wieder sauber.
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(new ArrayList<>());
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn10.setzeKlassenLeitung1Del(4L, 5L);
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn10, 2, 0, 4, 0, 0);
		assertEquals(1, dyn10.gibAktuelleKlassenleitungen().size(), "Zuordnung muss nach erneutem Add wieder vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel02KlasseBenoetigtKlassenlehrer: Individuelles Klassenleiter-Soll überschreibt den Default.")
	void testRegel02KlasseBenoetigtKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX für Klasse 4 werden akzeptiert.
		for (int soll = UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN; soll <= UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX; soll++) {
			final UvBlockungRegel regel1 = gibRegel02KlasseBenoetigtKlassenlehrer(4L, soll, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal2(List.of(regel1));
			final int erwarteterMalusSehrHoch = 2 + 2 + 1 + soll;
			checkMalus(dyn1, erwarteterMalusSehrHoch, 0, 8, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, es entsteht kein Regelmalus.
		final UvBlockungRegel regel2 = gibRegel02KlasseBenoetigtKlassenlehrer(4L, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal2(List.of(regel2));
		checkMalus(dyn2, 6, 0, 8, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Klassen-ID wird ignoriert, der Malus bleibt unverändert.
		final UvBlockungRegel regel3 = gibRegel02KlasseBenoetigtKlassenlehrer(999L, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal2(List.of(regel3));
		checkMalus(dyn3, 6, 0, 8, 0, 0);
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel4 = gibRegel02KlasseBenoetigtKlassenlehrer(4L, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal2(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = gibRegel02KlasseBenoetigtKlassenlehrer(4L, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal2(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur 1 Parameter statt 2 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER.nr;
		regel6.parameter.add(4L);
		regel6.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Regel 02 überschreibt Regel 01 für Klasse 4, Klasse 7 bleibt beim Default = MIN.
		final UvBlockungRegel regel7a = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(
				UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN, prioritaetSehrHoch);
		final UvBlockungRegel regel7b = gibRegel02KlasseBenoetigtKlassenlehrer(4L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(List.of(regel7a, regel7b));
		checkMalus(dyn7, 6, 0, 8, 0, 0);
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7a));
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7b));

		// Szenario 8: Wie oben, aber unabhängig von der Reihenfolge der Regeln.
		final UvBlockungRegel regel8a = gibRegel02KlasseBenoetigtKlassenlehrer(4L, 2, prioritaetSehrHoch);
		final UvBlockungRegel regel8b = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8a, regel8b));
		checkMalus(dyn8, 6, 0, 8, 0, 0);
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8a));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8b));

		// Szenario 9: Deterministische Klassenleitung 1 für Klasse 4 wird gesetzt/entfernt.
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(new ArrayList<>());
		dyn9.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn9, 5, 0, 8, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn9.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn9.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		dyn9.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn9, 6, 0, 8, 0, 0);
		assertEquals(0, dyn9.gibAktuelleKlassenleitungen().size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 10: Eine zweite Klassenleitung für Klasse 7 kann unabhängig gesetzt werden.
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(new ArrayList<>());
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn10.setzeKlassenLeitung1Add(7L, 6L, false);
		checkMalus(dyn10, 4, 0, 8, 0, 0);
		assertEquals(2, dyn10.gibAktuelleKlassenleitungen().size(), "Es sollten zwei Klassenleitungen vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel03KlasseDefaultBenoetigtStellvKlassenlehrer: Default-Soll für stellv. Klassenleitung wird global gesetzt.")
	void testRegel03KlasseDefaultBenoetigtStellvKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX werden akzeptiert.
		for (int soll = UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN; soll <= UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX; soll++) {
			final UvBlockungRegel regel1 = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(soll, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
			final int erwarteterMalusSehrHoch = 1 + 1 + soll;
			checkMalus(dyn1, erwarteterMalusSehrHoch, 0, 4, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, es entsteht kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel2 = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Kein Parameter -> Regel wird als fehlerhaft erkannt.
		final UvBlockungRegel regel3 = new UvBlockungRegel();
		regel3.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr;
		regel3.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Ungültiger Wert = MIN - 1 -> Regel wird als fehlerhaft erkannt.
		final UvBlockungRegel regel4 = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(
				UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Ungültiger Wert = MAX + 1 -> Regel wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Deterministische stellv. Klassenleitung wird gesetzt und wieder entfernt.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn6.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn6.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		dyn6.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn6, 3, 0, 4, 0, 0);
		assertEquals(0, dyn6.gibAktuelleKlassenleitungen().size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 7: Stellv. Klassenleitung für eine andere Klasse ist unabhängig möglich.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(new ArrayList<>());
		dyn7.setzeKlassenLeitung2Add(7L, 6L, false);
		checkMalus(dyn7, 5, 0, 8, 0, 0);
		assertEquals(1, dyn7.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(7L, dyn7.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=7 erwartet.");
		assertEquals(6L, dyn7.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=6 erwartet.");

		// Szenario 8: Eine feste Zuordnung beeinflusst den Zustand erwartungsgemäß.
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(new ArrayList<>());
		dyn8.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn8, 2, 0, 4, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel04KlasseBenoetigtStellvKlassenlehrer: Individuelles stellv. Klassenleiter-Soll überschreibt den Default.")
	void testRegel04KlasseBenoetigtStellvKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX für Klasse 4 werden akzeptiert.
		for (int soll = UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN; soll <= UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX; soll++) {
			final UvBlockungRegel regel1 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, soll, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal2(List.of(regel1));
			final int erwarteterMalusSehrHoch = 2 + 2 + 1 + soll;
			checkMalus(dyn1, erwarteterMalusSehrHoch, 0, 8, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, der Malus bleibt unverändert.
		final UvBlockungRegel regel2 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal2(List.of(regel2));
		checkMalus(dyn2, 6, 0, 8, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Klassen-ID wird ignoriert, der Malus bleibt unverändert.
		final UvBlockungRegel regel3 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(999L, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal2(List.of(regel3));
		checkMalus(dyn3, 6, 0, 8, 0, 0);
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel4 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal2(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal2(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur 1 Parameter statt 2 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER.nr;
		regel6.parameter.add(4L);
		regel6.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Regel 04 überschreibt Regel 03 für Klasse 4, Klasse 7 bleibt beim Default = MIN.
		final UvBlockungRegel regel7a = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, prioritaetSehrHoch);
		final UvBlockungRegel regel7b = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(List.of(regel7a, regel7b));
		checkMalus(dyn7, 6, 0, 8, 0, 0);
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7a));
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7b));

		// Szenario 8: Wie oben, aber unabhängig von der Reihenfolge der Regeln.
		final UvBlockungRegel regel8a = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, 2, prioritaetSehrHoch);
		final UvBlockungRegel regel8b = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8a, regel8b));
		checkMalus(dyn8, 6, 0, 8, 0, 0);
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8a));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8b));

		// Szenario 9: Deterministische stellv. Klassenleitung wird für Klasse 4 gesetzt.
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(new ArrayList<>());
		dyn9.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn9, 5, 0, 8, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn9.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn9.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");

		// Szenario 10: Stellv. Klassenleitung für Klasse 7 mit anderer Lehrkraft.
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(new ArrayList<>());
		dyn10.setzeKlassenLeitung2Add(7L, 6L, false);
		checkMalus(dyn10, 5, 0, 8, 0, 0);
		assertEquals(1, dyn10.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(7L, dyn10.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=7 erwartet.");
		assertEquals(6L, dyn10.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=6 erwartet.");

		// Szenario 11: Zwei stellv. Klassenleitungen gleichzeitig.
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal2(new ArrayList<>());
		dyn11.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn11.setzeKlassenLeitung2Add(7L, 6L, false);
		checkMalus(dyn11, 4, 0, 8, 0, 0);
		assertEquals(2, dyn11.gibAktuelleKlassenleitungen().size(), "Es sollten zwei stellv. Klassenleitungen vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel05LehrkraftDefaultWennKlassenlehrerDannMindestensAStunden")
	void testRegel05LehrkraftDefaultWennKlassenlehrerDannMindestensAStunden() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Mindestens 2 Stunden gefordert. Lehrkraft ist noch kein Klassenlehrer -> Kein Malus.
		final UvBlockungRegel regel1 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Lehrkraft wird Klassenlehrer, unterrichtet aber 0 Stunden in der Klasse -> Malus entsteht (2 fehlende Stunden).
		final UvBlockungRegel regel2 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		dyn2.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn2, 2, 2, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Lehrkraft wird Klassenlehrer und bekommt eine 2-stündige Lerngruppe in der Klasse -> Kein HOCH-Malus.
		final UvBlockungRegel regel3 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 1, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Regel deaktiviert, Unterschreitung der Mindeststunden hat keine Auswirkung.
		final UvBlockungRegel regel4 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Add und Del (Lerngruppe) gleichen sich aus.
		final UvBlockungRegel regel5 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn5, 2, 2, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size());
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Zwei Lerngruppen derselben Lehrkraft erfüllen die Mindeststunden in gibDatenMinimal2.
		final UvBlockungRegel regel6 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		dyn6.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn6.setzeLerngruppeLehrkraftAdd(2L, 5L, false);
		checkMalus(dyn6, 3, 2, 8, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size());
		assertEquals(2, dyn6.gibAktuelleZuordnung().size());
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Ungültiger Wert (negativ) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(-1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Fehlender Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel8.prioritaet = prioritaetHoch;
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse")
	void testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist noch kein Klassenlehrer -> kein Regelmalus.
		final UvBlockungRegel regel1 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Lehrkraft 5 wird Klassenlehrer, hat aber 0 Stunden in der Klasse -> 2 HOCH-Malus.
		final UvBlockungRegel regel2 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		dyn2.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn2, 2, 2, 4, 0, 0);
		assertEquals(1, dyn2.gibAktuelleKlassenleitungen().size());
		assertEquals(4L, dyn2.gibAktuelleKlassenleitungen().get(0).idKlasse);
		assertEquals(5L, dyn2.gibAktuelleKlassenleitungen().get(0).idLehrer);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Lehrkraft 5 wird Klassenlehrer und bekommt eine 2-stündige Lerngruppe -> Regel erfüllt.
		final UvBlockungRegel regel3 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 1, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Regel deaktiviert -> keine Wirkung.
		final UvBlockungRegel regel4 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Add und Del gleichen sich aus.
		final UvBlockungRegel regel5 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn5, 2, 2, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size());
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Die zweite Lerngruppe aus gibDatenMinimal2 wird genutzt; dadurch hat die Lehrkraft 4 Stunden in der Klasse.
		final UvBlockungRegel regel6 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		dyn6.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn6.setzeLerngruppeLehrkraftAdd(2L, 5L, false);
		checkMalus(dyn6, 3, 2, 8, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size());
		assertEquals(2, dyn6.gibAktuelleZuordnung().size());
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Unbekannte Lehrkraft-ID wird aussortiert.
		final UvBlockungRegel regel7 = gibRegel06LehrkraftAWennKlassenlehrerDann(999L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		checkMalus(dyn7, 3, 0, 4, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Fehlender Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel8.prioritaet = prioritaetHoch;
		regel8.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel07LehrkraftDefaultWennStellvKlassenlehrerDannMindestensAStundenInKlasse")
	void testRegel07LehrkraftDefaultWennStellvKlassenlehrerDannMindestensAStundenInKlasse() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft ist noch nicht stellv. Klassenlehrer -> kein Regelmalus.
		final UvBlockungRegel regel1 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Lehrkraft wird stellv. Klassenlehrer, hat aber 0 Stunden in der Klasse -> Regel verletzt.
		final UvBlockungRegel regel2 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		dyn2.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn2, 2, 2, 4, 0, 0);
		assertEquals(1, dyn2.gibAktuelleKlassenleitungen().size());
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Lehrkraft wird stellv. Klassenlehrer und bekommt eine 2-stündige Lerngruppe -> Regel erfüllt.
		final UvBlockungRegel regel3 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 1, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Regel deaktiviert -> kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel4 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Add und Del heben sich auf.
		final UvBlockungRegel regel5 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn5, 2, 2, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size());
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Ungültiger Wert negativ wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(-1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Fehlender Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel7.prioritaet = prioritaetHoch;
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Zweite Lerngruppe aus gibDatenMinimal2 zeigt denselben Effekt in einem größeren Datensatz.
		final UvBlockungRegel regel8 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		dyn8.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn8.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn8.setzeLerngruppeLehrkraftAdd(2L, 5L, false);
		checkMalus(dyn8, 3, 2, 8, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size());
		assertEquals(2, dyn8.gibAktuelleZuordnung().size());
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse")
	void testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist noch kein stellv. Klassenlehrer -> kein Regelmalus.
		final UvBlockungRegel regel1 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Lehrkraft 5 wird stellv. Klassenlehrer, hat aber 0 Stunden in der Klasse -> 2 HOCH-Malus.
		final UvBlockungRegel regel2 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		dyn2.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn2, 2, 2, 4, 0, 0);
		assertEquals(1, dyn2.gibAktuelleKlassenleitungen().size());
		assertEquals(4L, dyn2.gibAktuelleKlassenleitungen().get(0).idKlasse);
		assertEquals(5L, dyn2.gibAktuelleKlassenleitungen().get(0).idLehrer);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Lehrkraft 5 wird stellv. Klassenlehrer und bekommt eine 2-stündige Lerngruppe in der Klasse -> Regel erfüllt.
		final UvBlockungRegel regel3 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 1, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Regel deaktiviert -> keine Wirkung.
		final UvBlockungRegel regel4 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Add und Del gleichen sich aus.
		final UvBlockungRegel regel5 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn5.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn5, 2, 2, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size());
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Ungültiger Wert negativ wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Fehlender Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel7.prioritaet = prioritaetHoch;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Lehrkraft-ID wird aussortiert.
		final UvBlockungRegel regel8 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(999L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Größerer Datensatz mit zwei Lerngruppen, damit die Mindeststunden exakt erreicht werden.
		final UvBlockungRegel regel9 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		dyn9.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn9.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn9.setzeLerngruppeLehrkraftAdd(2L, 5L, false);
		checkMalus(dyn9, 3, 2, 8, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size());
		assertEquals(2, dyn9.gibAktuelleZuordnung().size());
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel09LehrerDefaultMaxKlassenlehrer: Default-Maximum für Klassenleitung-Einsätze pro Lehrkraft.")
	void testRegel09LehrerDefaultMaxKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX halten den Ausgangs-Malus unverändert.
		for (int max = UvRegelManager.LEHRER_LEITUNG1_MIN; max <= UvRegelManager.LEHRER_LEITUNG1_MAX; max++) {
			final UvBlockungRegel regel1 = gibRegel09LehrerDefaultMaxKlassenlehrer(max, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal2(List.of(regel1));
			checkMalus(dyn1, 6, 0, 8, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, der Malus bleibt unverändert.
		final UvBlockungRegel regel2 = gibRegel09LehrerDefaultMaxKlassenlehrer(2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal2(List.of(regel2));
		checkMalus(dyn2, 6, 0, 8, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: max=2 -> Zuweisung einer Klassenleitung ist möglich, Malus sinkt.
		final UvBlockungRegel regel3 = gibRegel09LehrerDefaultMaxKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal2(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn3, 5, 0, 8, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: max=MIN -> Klassen-Malus (-1) und Lehrkraft-Malus (+1) gleichen sich aus, Zuweisung erfolgt trotzdem.
		final UvBlockungRegel regel4 = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal2(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn4, 6, 0, 8, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Widerspruch Regel01 + Regel09 mit max=MIN -> Malus gleicht sich aus, Zuweisung erfolgt.
		final UvBlockungRegel regel5a = gibRegel01KlasseDefaultBenoetigtKlassenlehrer(1, prioritaetSehrHoch);
		final UvBlockungRegel regel5b = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal2(List.of(regel5a, regel5b));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn5, 6, 0, 8, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5a));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5b));

		// Szenario 6: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Kein Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr;
		regel8.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel10LehrerMaxKlassenlehrer: Individuelles Maximum für Klassenleitung-Einsätze einer Lehrkraft.")
	void testRegel10LehrerMaxKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX halten den Ausgangs-Malus unverändert.
		for (int max = UvRegelManager.LEHRER_LEITUNG1_MIN; max <= UvRegelManager.LEHRER_LEITUNG1_MAX; max++) {
			final UvBlockungRegel regel1 = gibRegel10LehrerMaxKlassenlehrer(5L, max, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
			checkMalus(dyn1, 3, 0, 4, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, der Malus bleibt unverändert.
		final UvBlockungRegel regel2 = gibRegel10LehrerMaxKlassenlehrer(5L, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: max=2 -> Zuweisung ist möglich, Malus sinkt.
		final UvBlockungRegel regel3 = gibRegel10LehrerMaxKlassenlehrer(5L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: max=MIN -> Klassen-Malus (-1) und Lehrkraft-Malus (+1) gleichen sich aus, Zuweisung erfolgt trotzdem.
		final UvBlockungRegel regel4 = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Regel10 überschreibt Regel09 für Lehrer 5.
		final UvBlockungRegel regel5a = gibRegel09LehrerDefaultMaxKlassenlehrer(2, prioritaetSehrHoch);
		final UvBlockungRegel regel5b = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5a, regel5b));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5a));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5b));

		// Szenario 6: Reihenfolge von Regel09 und Regel10 darf keinen Unterschied machen.
		final UvBlockungRegel regel6a = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvBlockungRegel regel6b = gibRegel09LehrerDefaultMaxKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6a, regel6b));
		dyn6.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn6, 3, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6a));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6b));

		// Szenario 7: Unbekannte Lehrkraft-ID wird aussortiert.
		final UvBlockungRegel regel7 = gibRegel10LehrerMaxKlassenlehrer(999L, UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		checkMalus(dyn7, 3, 0, 4, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel11LehrerDefaultMaxStellvKlassenlehrer: Default-Maximum für stellv. Klassenleitung-Einsätze pro Lehrkraft.")
	void testRegel11LehrerDefaultMaxStellvKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX halten den Ausgangs-Malus unverändert.
		for (int max = UvRegelManager.LEHRER_LEITUNG2_MIN; max <= UvRegelManager.LEHRER_LEITUNG2_MAX; max++) {
			final UvBlockungRegel regel1 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(max, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal2(List.of(regel1));
			checkMalus(dyn1, 6, 0, 8, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, der Malus bleibt unverändert.
		final UvBlockungRegel regel2 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal2(List.of(regel2));
		checkMalus(dyn2, 6, 0, 8, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: max=2 -> Zuweisung einer stellv. Klassenleitung ist möglich, Malus sinkt.
		final UvBlockungRegel regel3 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal2(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn3, 5, 0, 8, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: max=MIN -> Klassen-Malus (-1) und Lehrkraft-Malus (+1) gleichen sich aus, Zuweisung erfolgt trotzdem.
		final UvBlockungRegel regel4 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal2(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn4, 6, 0, 8, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Widerspruch Regel03 + Regel11 mit max=MIN -> Malus gleicht sich aus, Zuweisung erfolgt.
		final UvBlockungRegel regel5a = gibRegel03KlasseDefaultBenoetigtStellvKlassenlehrer(1, prioritaetSehrHoch);
		final UvBlockungRegel regel5b = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal2(List.of(regel5a, regel5b));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn5, 6, 0, 8, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5a));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5b));

		// Szenario 6: Alternativer Datensatz: andere Klasse und andere Lehrkraft, damit die deterministische API nicht nur den Default-Fall prüft.
		final UvBlockungRegel regel6 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		dyn6.setzeKlassenLeitung2Add(7L, 6L, false);
		checkMalus(dyn6, 5, 0, 8, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Zuweisung für Klasse 7 durch Lehrkraft 6 muss erfolgen.");
		assertEquals(7L, dyn6.gibAktuelleKlassenleitungen().get(0).idKlasse);
		assertEquals(6L, dyn6.gibAktuelleKlassenleitungen().get(0).idLehrer);
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Add und Del gleichen sich aus.
		final UvBlockungRegel regel7 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(List.of(regel7));
		dyn7.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn7.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn7, 6, 0, 8, 0, 0);
		assertEquals(0, dyn7.gibAktuelleKlassenleitungen().size());
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Kein Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr;
		regel10.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel12LehrerMaxStellvKlassenlehrer: Individuelles Maximum für stellv. Klassenleitung-Einsätze einer Lehrkraft.")
	void testRegel12LehrerMaxStellvKlassenlehrer() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Alle gültigen Werte von MIN bis MAX halten den Ausgangs-Malus unverändert.
		for (int max = UvRegelManager.LEHRER_LEITUNG2_MIN; max <= UvRegelManager.LEHRER_LEITUNG2_MAX; max++) {
			final UvBlockungRegel regel1 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, max, prioritaetSehrHoch);
			final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
			checkMalus(dyn1, 3, 0, 4, 0, 0);
			assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		}

		// Szenario 2: Regel deaktiviert, der Malus bleibt unverändert.
		final UvBlockungRegel regel2 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: max=2 -> Zuweisung ist möglich, Malus sinkt.
		final UvBlockungRegel regel3 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: max=MIN -> Klassen-Malus (-1) und Lehrkraft-Malus (+1) gleichen sich aus, Zuweisung erfolgt trotzdem.
		final UvBlockungRegel regel4 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Regel12 überschreibt Regel11 für Lehrer 5.
		final UvBlockungRegel regel5a = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetSehrHoch);
		final UvBlockungRegel regel5b = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5a, regel5b));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5a));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5b));

		// Szenario 6: Reihenfolge von Regel11 und Regel12 darf keinen Unterschied machen.
		final UvBlockungRegel regel6a = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvBlockungRegel regel6b = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6a, regel6b));
		dyn6.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn6, 3, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Zuweisung muss erfolgen, da Malus nicht schlechter wird.");
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6a));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6b));

		// Szenario 7: Unbekannte Lehrkraft-ID wird aussortiert.
		final UvBlockungRegel regel7 = gibRegel12LehrerMaxStellvKlassenlehrer(999L, UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		checkMalus(dyn7, 3, 0, 4, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MIN - 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Ungültiger Wert = MAX + 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MAX + 1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Nur 1 Parameter statt 2 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER.nr;
		regel10.parameter.add(5L);
		regel10.prioritaet = prioritaetSehrHoch;
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel13LerngruppeLehrkraftVerbot: Die einzige Lehrkraft ist in der Lerngruppe verboten.")
	void testRegel13LerngruppeLehrkraftVerbot() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Lerngruppe 1 verboten, Add wird ignoriert.
		final UvBlockungRegel regel1 = gibRegel13LehrkraftVerbotenInLerngruppe(5L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertThrows(DeveloperNotificationException.class, () -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel13LehrkraftVerbotenInLerngruppe(5L, 1L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Lehrkraft-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel13LehrkraftVerbotenInLerngruppe(999L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Unbekannte Lerngruppen-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel13LehrkraftVerbotenInLerngruppe(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lerngruppe betrifft.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel14LerngruppeLehrkraftFixierung: Die einzige Lehrkraft ist in der Lerngruppe fixiert.")
	void testRegel14LerngruppeLehrkraftFixierung() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Fixierung -> Lehrkraft ist sofort zugeordnet.
		final UvBlockungRegel regel1 = gibRegel14LehrkraftFixiertInLerngruppe(5L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 2, 0, 0, 0, 0);
		assertEquals(1, dyn1.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben, da die Lehrkraft fixiert ist.");
		assertEquals(1L, dyn1.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, Fixierung wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel14LehrkraftFixiertInLerngruppe(5L, 1L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Fixierung bleibt bestehen, auch wenn man anschließend versucht, die Zuordnung zu löschen.
		final UvBlockungRegel regel3 = gibRegel14LehrkraftFixiertInLerngruppe(5L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertThrows(DeveloperNotificationException.class, () -> dyn3.setzeLerngruppeLehrkraftDel(1L, 5L));

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel14LehrkraftFixiertInLerngruppe(999L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Keine Zuordnung bei ungültiger Lehrkraft-ID.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Lerngruppen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel14LehrkraftFixiertInLerngruppe(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleZuordnung().size(), "Keine Zuordnung bei ungültiger Lerngruppen-ID.");
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel15LerngruppeLehrkraftGerne: Die einzige Lehrkraft ist gerne in der Lerngruppe.")
	void testRegel15LerngruppeLehrkraftGerne() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist gerne in Lerngruppe 1.
		final UvBlockungRegel regel1 = gibRegel15LehrkraftGerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 1, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Wunsch zählt nicht.
		final UvBlockungRegel regel2 = gibRegel15LehrkraftGerneInLerngruppe(5L, 1L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die Zuordnung wird hinzugefügt, der Malus verbessert sich.
		final UvBlockungRegel regel3 = gibRegel15LehrkraftGerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn3.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Die Zuordnung wird wieder entfernt, dann hat man wieder den Anfangszustand.
		final UvBlockungRegel regel4 = gibRegel15LehrkraftGerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn4.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn4, 3, 0, 4, 1, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel16LerngruppeLehrkraftUngerne: Die einzige Lehrkraft ist ungerne in der Lerngruppe.")
	void testRegel16LerngruppeLehrkraftUngerne() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist ungerne in Lerngruppe 1.
		final UvBlockungRegel regel1 = gibRegel16LehrkraftUngerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Malus zählt nicht.
		final UvBlockungRegel regel2 = gibRegel16LehrkraftUngerneInLerngruppe(5L, 1L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die Zuordnung wird hinzugefügt, es entsteht ein Malus.
		final UvBlockungRegel regel3 = gibRegel16LehrkraftUngerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 1, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn3.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Die Zuordnung wird wieder entfernt, dann hat man wieder den Anfangszustand.
		final UvBlockungRegel regel4 = gibRegel16LehrkraftUngerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn4.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel17LerngruppeLiegtAmWochentagStundeWochentyp")
	void testRegel17LerngruppeLiegtAmWochentagStundeWochentyp() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1:
		// Zwei Lerngruppen liegen im selben Zeitslot.
		// Eine Lehrkraft erhält beide Lerngruppen --> 1 HOCH-Malus.
		final @NotNull List<UvBlockungRegel> regeln1 = new ArrayList<>();
		regeln1.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regeln1.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(regeln1);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regeln1.get(0)));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regeln1.get(1)));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 92, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 1, 80, 0, 0);
		assertEquals(2, dyn1.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		// Szenario 2:
		// Drei Lerngruppen liegen im selben Zeitslot.
		// Eine Lehrkraft erhält alle drei Lerngruppen --> 2 HOCH-Mali.
		final @NotNull List<UvBlockungRegel> regeln2 = new ArrayList<>();
		regeln2.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 2L, 3L, 0L, prioritaetHoch));
		regeln2.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 2L, 3L, 0L, prioritaetHoch));
		regeln2.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG8, 2L, 3L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(regeln2);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regeln2.get(0)));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regeln2.get(1)));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regeln2.get(2)));
		checkMalus(dyn2, 20, 0, 112, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 18, 1, 80, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 17, 2, 76, 0, 0);
		assertEquals(3, dyn2.gibAktuelleZuordnung().size(), "Es sollten genau 3 Zuordnungen vorhanden sein.");

		// Szenario 3:
		// Regel deaktiviert --> derselbe Konflikt erzeugt keinen zusätzlichen HOCH-Malus.
		final @NotNull List<UvBlockungRegel> regeln3 = new ArrayList<>();
		regeln3.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetDeaktiviert));
		regeln3.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetDeaktiviert));
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(regeln3);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regeln3.get(0)));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regeln3.get(1)));
		checkMalus(dyn3, 20, 0, 112, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 0, 92, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 18, 0, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel17LerngruppeLiegtAmWochentagStundeWochentyp2")
	void testRegel17LerngruppeLiegtAmWochentagStundeWochentyp2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 4: Wie Szenario 1 + Lerngruppen-Lehrkraft-Zuordnungen werden über Regel14 forciert (DANACH).
		final @NotNull List<UvBlockungRegel> regeln4 = new ArrayList<>();
		regeln4.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regeln4.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetHoch));
		regeln4.add(gibRegel14LehrkraftFixiertInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG2, UvBlockungRegelPrioritaet.SEHR_HOCH.nr));
		regeln4.add(gibRegel14LehrkraftFixiertInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG5, UvBlockungRegelPrioritaet.SEHR_HOCH.nr));
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(regeln4);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regeln4.get(0)));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regeln4.get(1)));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regeln4.get(2)));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regeln4.get(3)));
		assertEquals(2, dyn4.gibAktuelleZuordnung().size(), "Beide Lerngruppen müssen durch Regel 14 direkt zugeordnet sein.");
		checkMalus(dyn4, 18, 1, 80, 0, 0);

		// Szenario 4: Wie Szenario 1 + Lerngruppen-Lehrkraft-Zuordnungen werden über Regel14 forciert (DAVOR).
		final @NotNull List<UvBlockungRegel> regeln5 = new ArrayList<>();
		regeln5.add(gibRegel14LehrkraftFixiertInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG2, UvBlockungRegelPrioritaet.SEHR_HOCH.nr));
		regeln5.add(gibRegel14LehrkraftFixiertInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG5, UvBlockungRegelPrioritaet.SEHR_HOCH.nr));
		regeln5.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regeln5.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(regeln5);
		assertFalse(dyn5.gibIstRegelFehlerhaft(regeln5.get(0)));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regeln5.get(1)));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regeln5.get(2)));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regeln5.get(3)));
		assertEquals(2, dyn5.gibAktuelleZuordnung().size(), "Beide Lerngruppen müssen durch Regel 14 direkt zugeordnet sein.");
		checkMalus(dyn5, 18, 1, 80, 0, 0);

	}



	@Test
	@DisplayName("testRegel18LehrkraftVerbotenInKlasse: Die einzige Lehrkraft ist in der Klasse verboten.")
	void testRegel18LehrkraftVerbotenInKlasse() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Klasse 4 verboten, Add wird ignoriert.
		final UvBlockungRegel regel1 = gibRegel18LehrkraftVerbotenInKlasse(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertThrows(DeveloperNotificationException.class, () -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel18LehrkraftVerbotenInKlasse(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Lehrkraft-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel18LehrkraftVerbotenInKlasse(999L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Unbekannte Klassen-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel18LehrkraftVerbotenInKlasse(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Klasse betrifft.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Regel mit fehlendem Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr;
		regel5.prioritaet = prioritaetGering;
		regel5.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel19LehrkraftVerbotenInStufe: Die einzige Lehrkraft ist in der Stufe verboten.")
	void testRegel19LehrkraftVerbotenInStufe() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Jahrgang 5 verboten, Add wird ignoriert.
		final UvBlockungRegel regel1 = gibRegel19LehrkraftVerbotenInStufe(5L, UvDatensatz1.JAHRGANG, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertThrows(DeveloperNotificationException.class, () -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel19LehrkraftVerbotenInStufe(5L, UvDatensatz1.JAHRGANG, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Lehrkraft-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel19LehrkraftVerbotenInStufe(999L, UvDatensatz1.JAHRGANG, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Unbekannte Jahrgangs-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel19LehrkraftVerbotenInStufe(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Stufe betrifft.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel20LehrkraftHatMindestensBMalFachC")
	void testRegel20LehrkraftHatMindestensBMalFachC() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, aber Lehrkraft 1 unterrichtet Fach Deutsch noch nicht mindestens 1-mal.
		final UvBlockungRegel regel1 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Lehrkraft 1 bekommt genau eine Deutsch-Zuordnung.
		final UvBlockungRegel regel2 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Mindestanzahl 2, aber nur 1-mal Fach Deutsch.
		final UvBlockungRegel regel3 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 1, 92, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Mindestanzahl 2, zwei Deutsch-Zuordnungen vorhanden.
		final UvBlockungRegel regel4 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Dasselbe für Fach Mathematik.
		final UvBlockungRegel regel5 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_MA, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 18, 0, 80, 0, 0);
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		assertEquals(2, dyn5.gibAktuelleZuordnung().size());

		// Szenario 6: Regel deaktiviert -> kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel6 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_DE, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
	}


	@Test
	@DisplayName("testRegel21LehrkraftHatMaximalBMalFachC")
	void testRegel21LehrkraftHatMaximalBMalFachC() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, aber Lehrkraft 1 unterrichtet Fach Deutsch noch nicht mehr als maximal 1-mal.
		final UvBlockungRegel regel1 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Lehrkraft 1 bekommt genau eine Deutsch-Zuordnung.
		final UvBlockungRegel regel2 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Maximum 1, aber zwei Deutsch-Zuordnungen vorhanden -> HOCH-Malus entsteht.
		final UvBlockungRegel regel3 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 18, 1, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Maximum 2, zwei Deutsch-Zuordnungen vorhanden -> kein Regelmalus.
		final UvBlockungRegel regel4 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Dasselbe für Fach Mathematik.
		final UvBlockungRegel regel5 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 2, UvDatensatz3.FACH_MA, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 18, 0, 80, 0, 0);
		assertEquals(2, dyn5.gibAktuelleZuordnung().size());

		// Szenario 6: Regel deaktiviert -> kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel6 = gibRegel21LehrkraftHatMaximalBMalFachC(
				UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
	}


	@Test
	@DisplayName("testRegel22LehrkraftHatMindestensNKorrekturen - Lehrkraft A hat mindestens B Korrekturen.")
	void testRegel22LehrkraftHatMindestensNKorrekturen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, Lehrkraft 1 hat noch 0 Korrekturen – Mindestanzahl 1 wird verletzt.
		final UvBlockungRegel regel1 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Lehrkraft 1 bekommt eine Lerngruppe mit Korrekturbelastung (Deutsch, 5A) – Regel erfüllt.
		final UvBlockungRegel regel2 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Mindestanzahl 2, aber nur 1 Korrekturbelastung zugeordnet – Regel verletzt.
		final UvBlockungRegel regel3 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 1, 92, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Mindestanzahl 2 – zwei Korrekturbelastungen zugeordnet – Regel erfüllt.
		final UvBlockungRegel regel4 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Andere Lehrkraft (LEHRER2) mit Mindestanzahl 1 – noch keine Zuordnung, Regel verletzt.
		final UvBlockungRegel regel5 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER2, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		checkMalus(dyn5, 20, 1, 112, 0, 0);
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());

		// Szenario 6: Regel deaktiviert – kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel6 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 2, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel22LehrkraftHatMindestensNKorrekturen(999L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Negativer Mindestwert wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Fehlender Parameter (nur Lehrer-ID, kein Anzahl-Parameter) – Regel fehlerhaft.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN.nr;
		regel9.prioritaet = prioritaetHoch;
		regel9.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel23LehrkraftHatMaximalNKorrekturen - Lehrkraft A hat maximal B Korrekturen.")
	void testRegel23LehrkraftHatMaximalNKorrekturen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, Lehrkraft 1 hat noch 0 Korrekturen – Maximum 1 wird eingehalten.
		final UvBlockungRegel regel1 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Lehrkraft 1 bekommt eine Lerngruppe mit Korrekturbelastung (Deutsch, 5A) – Maximum 1 bleibt eingehalten.
		final UvBlockungRegel regel2 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Maximum 1, aber zwei Korrekturbelastungen zugeordnet – Regel verletzt.
		final UvBlockungRegel regel3 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 18, 1, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Maximum 2 – zwei Korrekturbelastungen zugeordnet – Regel erfüllt.
		final UvBlockungRegel regel4 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Andere Lehrkraft (LEHRER2) mit Maximum 0 – noch keine Zuordnung, Regel erfüllt.
		final UvBlockungRegel regel5 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER2, 0, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);
		assertEquals(0, dyn5.gibAktuelleZuordnung().size());

		// Szenario 6: Regel deaktiviert – kein zusätzlicher Regelmalus.
		final UvBlockungRegel regel6 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel23LehrkraftHatMaximalNKorrekturen(999L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Negativer Maximalwert wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Fehlender Parameter (nur Lehrer-ID, kein Anzahl-Parameter) – Regel fehlerhaft.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN.nr;
		regel9.prioritaet = prioritaetHoch;
		regel9.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen")
	void testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Gültige Regel, Lehrkraft 51 darf Fach 31 in Jahrgang 101 nicht unterrichten.
		final UvBlockungRegel regel1 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		assertThrows(DeveloperNotificationException.class, () -> dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetDeaktiviert
		);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		checkMalus(dyn2, 20, 0, 112, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID1 erwartet.");
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID51 erwartet.");

		// Szenario 3: Unbekannte Lehrkraft-ID wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				999L,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		checkMalus(dyn3, 20, 0, 112, 0, 0);
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 0, 92, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");

		// Szenario 4: Unbekannte Fach-ID wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				999L,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		checkMalus(dyn4, 20, 0, 112, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 19, 0, 92, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot ein unbekanntes Fach betrifft.");

		// Szenario 5: Unbekannte Jahrgangs-ID wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(999L),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 19, 0, 92, 0, 0);
		assertEquals(1, dyn5.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Stufe betrifft.");

	}

	@Test
	@DisplayName("testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen2")
	void testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 6: Mehrere Jahrgänge werden korrekt verarbeitet.
		final UvBlockungRegel regel6 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5, UvDatensatz3.JAHRGANG_6),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		assertThrows(DeveloperNotificationException.class, () -> dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false));
		assertThrows(DeveloperNotificationException.class, () -> dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false));

		// Szenario 7: Lehrkraft ist in einer Lerngruppe fixiert und darf zugleich das Fach
		// in der zugehörigen Stufe nicht unterrichten. Das ist ein Widerspruch.
		final @NotNull List<Long> jahrgaenge7 = new ArrayList<>();
		jahrgaenge7.add(UvDatensatz3.JAHRGANG_5);
		final UvBlockungRegel regel7a = gibRegel14LehrkraftFixiertInLerngruppe(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.LG1,
				prioritaetHoch
		);
		final UvBlockungRegel regel7b = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				jahrgaenge7,
				prioritaetHoch
		);
		final @NotNull List<UvBlockungRegel> regeln7 = new ArrayList<>();
		regeln7.add(regel7a);
		regeln7.add(regel7b);
		assertThrows(DeveloperNotificationException.class, () -> gibDatenMinimal3(regeln7));

		// Szenario 8: Gleicher Widerspruch, aber mit umgekehrter Reihenfolge der Regeln.
		final @NotNull List<Long> jahrgaenge8 = new ArrayList<>();
		jahrgaenge8.add(UvDatensatz3.JAHRGANG_5);
		final UvBlockungRegel regel8a = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				jahrgaenge8,
				prioritaetHoch
		);
		final UvBlockungRegel regel8b = gibRegel14LehrkraftFixiertInLerngruppe(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.LG1,
				prioritaetHoch
		);
		final @NotNull List<UvBlockungRegel> regeln8 = new ArrayList<>();
		regeln8.add(regel8a);
		regeln8.add(regel8b);
		assertThrows(DeveloperNotificationException.class, () -> gibDatenMinimal3(regeln8));
	}


	@Test
	@DisplayName("testRegel25LehrkraftDarfFachNichtUnterrichten")
	void testRegel25LehrkraftDarfFachNichtUnterrichten() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 darf Fach 3 nicht unterrichten, Add wird ignoriert.
		final UvBlockungRegel regel1 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(5L, UvDatensatz1.ID_FACH, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertThrows(DeveloperNotificationException.class, () -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(5L, UvDatensatz1.ID_FACH, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertEquals(1L, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID1 erwartet.");
		assertEquals(5L, dyn2.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID5 erwartet.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Lehrkraft-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(999L, UvDatensatz1.ID_FACH, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Unbekannte Fach-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot ein unbekanntes Fach betrifft.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Regel mit fehlendem Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr;
		regel5.prioritaet = prioritaetSehrHoch;
		regel5.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel26LehrkraftAIstInMaximalBJahrgaengen")
	void testRegel26LehrkraftAIstInMaximalBJahrgaengen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Gültige Regel, aber noch keine Zuordnung. Daher kein zusätzlicher HOCH-Malus.
		final UvBlockungRegel regel1 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel verletzt. Lehrkraft 51 wird in zwei verschiedenen Jahrgängen eingesetzt,
		// erlaubt ist aber maximal 1 Jahrgang. Daher entsteht 1 HOCH-Malus.
		final UvBlockungRegel regel2 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		checkMalus(dyn2, 20, 0, 112, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-malus weniger.
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-malus weniger.
		checkMalus(dyn2, 18, 1, 80, 0, 0);
		assertEquals(2, dyn2.gibAktuelleZuordnung().size());

		// Szenario 3: Maximal 2 Jahrgänge sind erlaubt. Dieselben beiden Zuordnungen sind dann regelkonform.
		final UvBlockungRegel regel3 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		checkMalus(dyn3, 20, 0, 112, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-malus weniger.
		checkMalus(dyn3, 19, 0, 92, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-malus weniger.
		checkMalus(dyn3, 18, 0, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Regel deaktiviert. Die gleiche Verletzung erzeugt dann keinen HOCH-Malus.
		final UvBlockungRegel regel4 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 1, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		checkMalus(dyn4, 20, 0, 112, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-malus weniger.
		checkMalus(dyn4, 19, 0, 92, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-malus weniger.
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(999L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Negativer Maximalwert ist ungültig und wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE.nr;
		regel7.prioritaet = prioritaetHoch;
		regel7.parameter.add(51L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei wird ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE.nr;
		regel8.prioritaet = prioritaetHoch;
		regel8.parameter.add(51L);
		regel8.parameter.add(1L);
		regel8.parameter.add(2L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel27LehrkraefteAUndBNichtInDerSelbenKlasse")
	void testRegel27LehrkraefteAUndBNichtInDerSelbenKlasse() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Regel aktiv, aber beide Lehrkräfte werden in verschiedenen Klassen eingesetzt.
		// In gibDatenMinimal2 liegt Lerngruppe 1 in Klasse 4 und Lerngruppe 2 in Klasse 7.
		// Daher darf kein GERING-Malus durch Regel 27 entstehen.
		final UvBlockungRegel regel1 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(5L, 6L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal2(List.of(regel1));
		checkMalus(dyn1, 6, 0, 8, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn1, 5, 0, 4, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(2L, 6L, false);
		checkMalus(dyn1, 4, 0, 0, 0, 0);
		final List<UvLerngruppenLehrer> zuordnung1 = dyn1.gibAktuelleZuordnung();
		assertEquals(2, zuordnung1.size(), "Es muss zwei Zuordnungen geben!");

		// Szenario 2: Regel deaktiviert. Das Verhalten ist identisch, da im Datensatz ohnehin kein Konfliktfall vorliegt.
		final UvBlockungRegel regel2 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(5L, 6L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal2(List.of(regel2));
		checkMalus(dyn2, 6, 0, 8, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 5, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(2L, 6L, false);
		checkMalus(dyn2, 4, 0, 0, 0, 0);
		final List<UvLerngruppenLehrer> zuordnung2 = dyn2.gibAktuelleZuordnung();
		assertEquals(2, zuordnung2.size(), "Auch bei deaktivierter Regel müssen beide Zuordnungen vorhanden sein.");

		// Szenario 3: Beide Lehrkräfte kommen in dieselbe Klasse.
		// Die Zuordnungen bleiben bestehen, aber Regel 27 erzeugt einen GERING-Malus von 1.
		final UvBlockungRegel regel3 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(5L, 6L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal2(List.of(regel3));
		checkMalus(dyn3, 6, 0, 8, 0, 0);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 5, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 6L, false);
		checkMalus(dyn3, 6, 0, 0, 1, 0); // Lerngruppe hat zu viele Lehrkräfte --> Malus 6
		final List<UvLerngruppenLehrer> zuordnung3 = dyn3.gibAktuelleZuordnung();
		assertEquals(2, zuordnung3.size(), "Regel 27 erzeugt nur einen GERING-Malus.");
		dyn3.setzeLerngruppeLehrkraftDel(1L, 6L);
		checkMalus(dyn3, 5, 0, 4, 0, 0);

		// Szenario 4: Unbekannte erste Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel4 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(999L, 6L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal2(List.of(regel4));
		checkMalus(dyn4, 6, 0, 8, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte zweite Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal2(List.of(regel5));
		checkMalus(dyn5, 6, 0, 8, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Beide Lehrer-IDs identisch. Die Regel ist damit fachlich ungültig.
		final UvBlockungRegel regel6 = gibRegel27LehrkraefteAUndBNichtInDerSelbenKlasse(5L, 5L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal2(List.of(regel6));
		checkMalus(dyn6, 6, 0, 8, 0, 0);
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal2(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei wird ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE.nr;
		regel8.prioritaet = prioritaetGering;
		regel8.parameter.add(5L);
		regel8.parameter.add(6L);
		regel8.parameter.add(7L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel28LerngruppeBenoetigtBLehrkraefte: Lerngruppe benötigt explizit B Lehrkräfte.")
	void testRegel28LerngruppeBenoetigtBLehrkraefte() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Soll=0, Ausgangszustand hat nur Klassen-Malus.
		final UvBlockungRegel regel1 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 0L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 2, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, Soll=0 hat keine Wirkung.
		final UvBlockungRegel regel2 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 0L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Soll=0 und es wird zugewiesen. Malus-Leitung 1 + Malus Leitung 2 + Malus Lerngruppenanzahl.
		final UvBlockungRegel regel3 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 0L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 3, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn3.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Soll=2, Ausgangszustand hat 2 fehlende Lehrkräfte zusätzlich zum Klassen-Malus.
		final UvBlockungRegel regel4 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 2L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 4, 0, 4, 0, 0);
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Soll=2, Zuweisung verbessert den Malus.
		final UvBlockungRegel regel5 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 2L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn5, 3, 0, 0, 0, 0);
		assertEquals(1, dyn5.gibAktuelleZuordnung().size(), "Genau 1 von 2 geforderten Lehrkräften zugeordnet.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Ungültiger Wert = MIN - 1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, -1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));
	}


	@Test
	@DisplayName("testRegel29LerngruppenHabenSelbeLehrkraft")
	void testRegel29LerngruppenHabenSelbeLehrkraft() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Zwei Lerngruppen, keine Zuordnung. Die Regel ist fachlich korrekt, aber noch nicht erfüllt.
		final UvBlockungRegel regel1 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Beide Lerngruppen erhalten unterschiedliche Lehrkräfte. Die Regel ist damit verletzt.
		final UvBlockungRegel regel2 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn2, 18, 1, 72, 0, 0);
		assertEquals(2, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Zwei Lerngruppen mit derselben Lehrkraft. Die Regel ist erfüllt.
		final UvBlockungRegel regel3 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 18, 0, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Regel deaktiviert. Dann wird kein zusätzlicher Regelmalus erzeugt.
		final UvBlockungRegel regel4 = gibRegel29LerngruppenHabenSelbeLehrkraft(
				prioritaetDeaktiviert, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn4, 18, 0, 72, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Regel mit nur einer Lerngruppe ist fachlich ungültig.
		final UvBlockungRegel regel5 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Drei Lerngruppen, eine Lehrkraft ist in allen dreien.
		final UvBlockungRegel regel6 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5,
				UvDatensatz3.LG8);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 18, 0, 80, 0, 0); // Kein Malus, da dritte Lerngruppe noch unbesetzt ist.
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 17, 0, 76, 0, 0);
		assertEquals(3, dyn6.gibAktuelleZuordnung().size());

		// Szenario 7: Drei Lerngruppen, zwei davon mit einer Lehrkraft, die dritte mit einer anderen Lehrkraft.
		final UvBlockungRegel regel7 = gibRegel29LerngruppenHabenSelbeLehrkraft(
				prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5, UvDatensatz3.LG8);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7));
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn7, 17, 1, 60, 0, 0);
		assertEquals(3, dyn7.gibAktuelleZuordnung().size());

		// Szenario 8: Drei Lerngruppen, jeweils eine andere Lehrkraft.
		final UvBlockungRegel regel8 = gibRegel29LerngruppenHabenSelbeLehrkraft(
				prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5, UvDatensatz3.LG8);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER2, false);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER4, false);
		checkMalus(dyn8, 17, 2, 60, 0, 0);
		assertEquals(3, dyn8.gibAktuelleZuordnung().size());
	}


	@Test
	@DisplayName("testRegel30LerngruppenHabenVerschiedeneLehrkraefte")
	void testRegel30LerngruppenHabenVerschiedeneLehrkraefte() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Zwei Lerngruppen, keine Zuordnung. Die Regel ist fachlich korrekt, aber noch nicht erfüllt.
		final UvBlockungRegel regel1 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size());

		// Szenario 2: Beide Lerngruppen erhalten dieselbe Lehrkraft. Die Regel ist damit verletzt.
		final UvBlockungRegel regel2 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 18, 1, 80, 0, 0);
		assertEquals(2, dyn2.gibAktuelleZuordnung().size());
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe);
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer);

		// Szenario 3: Zwei Lerngruppen mit verschiedenen Lehrkräften. Die Regel ist erfüllt.
		final UvBlockungRegel regel3 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn3, 18, 0, 72, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Regel deaktiviert. Dann wird kein zusätzlicher Regelmalus erzeugt.
		final UvBlockungRegel regel4 =
				gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetDeaktiviert, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// Szenario 5: Regel mit nur einer Lerngruppe ist fachlich ungültig.
		final UvBlockungRegel regel5 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Drei Lerngruppen (Mathematik), eine Lehrkraft ist in allen dreien.
		final UvBlockungRegel regel6 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5,
				UvDatensatz3.LG8);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false); // 1. Verletzung
		checkMalus(dyn6, 18, 1, 80, 0, 0);
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false); // 2. Verletzung
		checkMalus(dyn6, 17, 2, 76, 0, 0);
		assertEquals(3, dyn6.gibAktuelleZuordnung().size());

		// Szenario 7: Drei Lerngruppen (Mathematik), eine Lehrkraft hat zwei davon, eine andere Lehrkraft die dritte.
		final UvBlockungRegel regel7 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5,
				UvDatensatz3.LG8);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertFalse(dyn7.gibIstRegelFehlerhaft(regel7));
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false); // 1. Verletzung
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn7, 17, 1, 60, 0, 0);
		assertEquals(3, dyn7.gibAktuelleZuordnung().size());
	}


	@Test
	@DisplayName("testRegel31LerngruppenGemeinsamKuerzenAufAStunden")
	void testRegel31LerngruppenGemeinsamKuerzenAufAStunden() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Einzelne Lerngruppe ist erlaubt. Die Kürzung auf 1 Stunde wirkt konkret auf den MITTEL-Malus.
		final UvBlockungRegel regel1 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 101, 0, 0);
		assertEquals(1, dyn1.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn1.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG2, UvDatensatz3.LEHRER1);
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Die Zuordnung sollte wieder entfernt sein.");

		// Szenario 2: Randfall Kürzung auf 0 Stunden bei genau einer Lerngruppe.
		final UvBlockungRegel regel2 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(0L, prioritaetHoch, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		checkMalus(dyn2, 20, 0, 112, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 19, 0, 112, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Die Lerngruppe bleibt zugeordnet, zählt aber nicht zu den Stunden.");

		// Szenario 3: Zwei Lerngruppen werden gemeinsam auf 1 Stunde gekürzt.
		final UvBlockungRegel regel3 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		checkMalus(dyn3, 20, 0, 112, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 0, 101, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 18, 0, 92, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		// Szenario 4: Zwei Lerngruppen werden gemeinsam auf 0 Stunden gekürzt.
		final UvBlockungRegel regel4 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(0L, prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		checkMalus(dyn4, 20, 0, 112, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 19, 0, 112, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 112, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		// Szenario 5: Deaktivierte Regel hat keine Wirkung, auch wenn A = 0 gesetzt ist.
		final UvBlockungRegel regel5 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(0L, prioritaetDeaktiviert, UvDatensatz3.LG2, UvDatensatz3.LG5);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 19, 0, 92, 0, 0);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 18, 0, 80, 0, 0);
		assertEquals(2, dyn5.gibAktuelleZuordnung().size(), "Ohne aktive Regel zählen beide Lerngruppen normal zu den Stunden.");
	}


	@Test
	@DisplayName("testRegel31LerngruppenGemeinsamKuerzenAufAStunden_UngueltigeParameter")
	void testRegel31LerngruppenGemeinsamKuerzenAufAStunden_UngueltigeParameter() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 1: Kein Parameter.
		final UvBlockungRegel regel1 = new UvBlockungRegel();
		regel1.typ = UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr;
		regel1.prioritaet = prioritaetHoch;
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertTrue(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);

		// Szenario 2: Nur Kürzungswert, aber keine Lerngruppe.
		final UvBlockungRegel regel2 = new UvBlockungRegel();
		regel2.typ = UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr;
		regel2.prioritaet = prioritaetHoch;
		regel2.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertTrue(dyn2.gibIstRegelFehlerhaft(regel2));
		checkMalus(dyn2, 20, 0, 112, 0, 0);

		// Szenario 3: Kürzung < 0 ist ungültig.
		final UvBlockungRegel regel3 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(-1L, prioritaetHoch, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));
		checkMalus(dyn3, 20, 0, 112, 0, 0);

		// Szenario 4: Eine einzelne unbekannte Lerngruppe.
		final UvBlockungRegel regel4 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, 999L);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));
		checkMalus(dyn4, 20, 0, 112, 0, 0);

		// Szenario 5: Mischung aus gültiger und ungültiger Lerngruppe.
		final UvBlockungRegel regel5 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, UvDatensatz3.LG2, 999L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);

		// Szenario 6: Zusätzlicher Parameter ohne gültige Referenz.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr;
		regel6.prioritaet = prioritaetHoch;
		regel6.parameter.add(1L);
		regel6.parameter.add(UvDatensatz3.LG2);
		regel6.parameter.add(UvDatensatz3.LG5);
		regel6.parameter.add(999L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);

		// Szenario 7: Mehrere zusätzliche Parameter ohne Referenz.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr;
		regel7.prioritaet = prioritaetHoch;
		regel7.parameter.add(0L);
		regel7.parameter.add(UvDatensatz3.LG2);
		regel7.parameter.add(999L);
		regel7.parameter.add(1000L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
	}


	@Test
	@DisplayName("testRegel32LehrkraftFixiertAlsLeitung1: Lehrkraft ist fixiert als Klassenlehrer.")
	void testRegel32LehrkraftFixiertAlsLeitung1() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Fixierung -> Lehrkraft ist sofort als Leitung 1 zugeordnet.
		final UvBlockungRegel regel1 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 2, 0, 4, 0, 0);
		assertEquals(1, dyn1.gibAktuelleKlassenleitungen().size(), "Lehrkraft muss direkt als Leitung 1 fixiert zugeordnet sein.");
		assertEquals(4L, dyn1.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, Fixierung wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Fixierung kann nicht entfernt werden.
		final UvBlockungRegel regel3 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertThrows(DeveloperNotificationException.class, () -> dyn3.setzeKlassenLeitung1Del(4L, 5L));

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Lehrkraft-ID.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Klasse-ID.");
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel33LehrkraftFixiertAlsLeitung2: Lehrkraft ist fixiert als stellv. Klassenlehrer.")
	void testRegel33LehrkraftFixiertAlsLeitung2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Fixierung -> Lehrkraft ist sofort als Leitung 2 zugeordnet.
		final UvBlockungRegel regel1 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 2, 0, 4, 0, 0);
		assertEquals(1, dyn1.gibAktuelleKlassenleitungen().size(), "Lehrkraft muss direkt als Leitung 2 fixiert zugeordnet sein.");
		assertEquals(4L, dyn1.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, Fixierung wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Fixierung kann nicht entfernt werden.
		final UvBlockungRegel regel3 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertThrows(DeveloperNotificationException.class, () -> dyn3.setzeKlassenLeitung2Del(4L, 5L));

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Lehrkraft-ID.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Klasse-ID.");
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel34LehrkraftVerbotenAlsLeitung1: Lehrkraft ist verboten als Klassenlehrer.")
	void testRegel34LehrkraftVerbotenAlsLeitung1() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Verbot für Lehrkraft 5 in Klasse 4.
		final UvBlockungRegel regel1 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Add wird deterministisch gesetzt, aber es ist "VERBOTEN".
		final UvBlockungRegel regel3 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertThrows(DeveloperNotificationException.class, () -> dyn3.setzeKlassenLeitung1Add(4L, 5L, false));

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel34LehrkraftVerbotenAlsLeitung1(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klasse-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel35LehrkraftVerbotenAlsLeitung2: Lehrkraft ist verboten als stellv. Klassenlehrer.")
	void testRegel35LehrkraftVerbotenAlsLeitung2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Verbot für Lehrkraft 5 in Klasse 4.
		final UvBlockungRegel regel1 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht.
		final UvBlockungRegel regel2 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Add wird deterministisch gesetzt, obwohl die einzige Lehrkraft verboten ist.
		final UvBlockungRegel regel3 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertThrows(DeveloperNotificationException.class, () -> dyn3.setzeKlassenLeitung2Add(4L, 5L, false));

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel35LehrkraftVerbotenAlsLeitung2(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klasse-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel36LehrkraftGerneAlsLeitung1")
	void testRegel36LehrkraftGerneAlsLeitung1() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist gerne als Klassenleitung in Klasse 4.
		final UvBlockungRegel regel1 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 1, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Wunsch zählt nicht.
		final UvBlockungRegel regel2 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die Klassenleitung wird deterministisch hinzugefügt, der Malus verbessert sich.
		final UvBlockungRegel regel3 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID4 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Hinzufügen und Entfernen heben sich wieder auf.
		final UvBlockungRegel regel4 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 1, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Fixierte Zuordnung erfüllt den Wunsch direkt.
		final UvBlockungRegel regel5 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn5, 2, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuordnung soll vorhanden sein.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel37LehrkraftGerneAlsStellvKlassenleitung")
	void testRegel37LehrkraftGerneAlsStellvKlassenleitung() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist gerne als stellv. Klassenleitung in Klasse 4.
		final UvBlockungRegel regel1 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 1, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Wunsch zählt nicht.
		final UvBlockungRegel regel2 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die stellv. Klassenleitung wird deterministisch hinzugefügt, der Malus verbessert sich.
		final UvBlockungRegel regel3 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID4 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Hinzufügen und Entfernen heben sich wieder auf.
		final UvBlockungRegel regel4 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 1, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Fixierte Zuordnung erfüllt den Wunsch direkt.
		final UvBlockungRegel regel5 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn5, 2, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuordnung soll vorhanden sein.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel38LehrkraftUngerneAlsLeitung1: Lehrkraft ist ungerne als Klassenlehrer.")
	void testRegel38LehrkraftUngerneAlsLeitung1() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist noch nicht als Klassenleitung zugeordnet.
		final UvBlockungRegel regel1 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Wunsch zählt nicht.
		final UvBlockungRegel regel2 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die Klassenleitung wird deterministisch hinzugefügt, der Malus entsteht.
		final UvBlockungRegel regel3 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 1, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse);
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Add und Del gleichen sich aus.
		final UvBlockungRegel regel4 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size());
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel39LehrkraftUngerneAlsLeitung2 Lehrkraft ist ungerne als stellv. Klassenlehrer.")
	void testRegel39LehrkraftUngerneAlsLeitung2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist noch nicht als stellv. Klassenleitung zugeordnet.
		final UvBlockungRegel regel1 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleKlassenleitungen().size(), "Vor der Optimierung sollte es noch keine Zuordnung geben.");
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		// Szenario 2: Regel deaktiviert, der Wunsch zählt nicht.
		final UvBlockungRegel regel2 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 4L, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Die stellv. Klassenleitung wird deterministisch hinzugefügt, der Malus entsteht.
		final UvBlockungRegel regel3 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		dyn3.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn3, 2, 0, 4, 1, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size());
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse);
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Add und Del gleichen sich aus.
		final UvBlockungRegel regel4 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size());
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel40LehrkraftHatMaximalBLerngruppen")
	void testRegel40LehrkraftHatMaximalBLerngruppen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Max = 1
		final UvBlockungRegel regel1 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));

		dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn1, 2, 0, 0, 0, 0);
		assertEquals(1, dyn1.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn1.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID1 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID5 erwartet.");

		// Szenario 2: Max = 0
		final UvBlockungRegel regel2 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, 0, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 1, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");

		// Szenario 3: Deaktivierte Regel hat keine Wirkung.
		final UvBlockungRegel regel3 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, 0, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss ohne Regelmalus erfolgen, da die Regel deaktiviert ist.");

		// Szenario 4: Fixierte Zuordnung zählt ebenfalls für das Maximum.
		final UvBlockungRegel regel4 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel41LehrkraftHatMindestensBLerngruppen")
	void testRegel41LehrkraftHatMindestensBLerngruppen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Min = 1 (Malus wird direkt verletzt)
		final UvBlockungRegel regel1 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 1, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn1, 2, 0, 0, 0, 0);
		assertEquals(1, dyn1.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn1.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID1 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID5 erwartet.");

		// Szenario 2: Min = 2 (Malus wird direkt doppelt verletzt)
		final UvBlockungRegel regel2 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 2, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 1, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben und noch einen HOCH Malus.");

		// Szenario 3: Deaktivierte Regel hat keine Wirkung.
		final UvBlockungRegel regel3 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 3, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss ohne Regelmalus erfolgen, da die Regel deaktiviert ist.");

		// Szenario 4: Fixierte Zuordnung erfüllt die Mindestanzahl ebenfalls.
		final UvBlockungRegel regel4 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel42LehrkraftMindestensBMalInLerngruppen")
	void testRegel42LehrkraftMindestensBMalInLerngruppen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Hilfslisten für die Lerngruppen-IDs aus UvDatensatz3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		final @NotNull List<Long> gruppe123 = new ArrayList<>();
		gruppe123.add(UvDatensatz3.LG1);
		gruppe123.add(UvDatensatz3.LG2);
		gruppe123.add(UvDatensatz3.LG3);

		// Szenario 1: Mindestens 1 mal in 2 Lerngruppen.
		UvBlockungRegel regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 92, 0, 0);

		// Szenario 2: Mindestens 1 mal in 3 Lerngruppen.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe123, prioritaetHoch);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 92, 0, 0);

		// Szenario 3: Mindestens 2 mal in 3 Lerngruppen.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe123, prioritaetHoch);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 2, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 1, 92, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 0, 80, 0, 0);

		// Szenario 4: Mindestens 3 mal in 3 Lerngruppen.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 3, gruppe123, prioritaetHoch);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 3, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 2, 92, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 1, 80, 0, 0);

		// Szenario 5: Regel deaktiviert -> es entsteht kein HOCH-Malus durch Regel 42.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe12, prioritaetDeaktiviert);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 0, 80, 0, 0);
	}

	@Test
	@DisplayName("testRegel42LehrkraftMindestensBMalInLerngruppen_UngueltigeParameter")
	void testRegel42LehrkraftMindestensBMalInLerngruppen_UngueltigeParameter() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Basis: gültige Lerngruppenmenge für Datensatz 3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		// Szenario A: Kein Parameter -> Regel muss als fehlerhaft erkannt werden.
		final UvBlockungRegel regelA = new UvBlockungRegel();
		regelA.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN.nr;
		regelA.prioritaet = prioritaetHoch;
		final UvAlgorithmusDynDaten dynA = gibDatenMinimal3(List.of(regelA));
		assertTrue(dynA.gibIstRegelFehlerhaft(regelA), "Regel 42 ohne Parameter muss als fehlerhaft markiert werden.");
		// Malus bleibt beim Anfangswert.
		checkMalus(dynA, 20, 0, 112, 0, 0);

		// Szenario B: Nur Lehrkraft-ID (1 Parameter) -> Mindest-Anzahl fehlt.
		final UvBlockungRegel regelB = new UvBlockungRegel();
		regelB.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN.nr;
		regelB.prioritaet = prioritaetHoch;
		regelB.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dynB = gibDatenMinimal3(List.of(regelB));
		assertTrue(dynB.gibIstRegelFehlerhaft(regelB), "Regel 42 mit nur einem Parameter muss als fehlerhaft markiert werden.");
		checkMalus(dynB, 20, 0, 112, 0, 0);

		// Szenario C: Lehrkraft-ID + Mindest-Anzahl, aber keine Lerngruppen-IDs.
		final UvBlockungRegel regelC = new UvBlockungRegel();
		regelC.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN.nr;
		regelC.prioritaet = prioritaetHoch;
		regelC.parameter.add(UvDatensatz3.LEHRER1);
		regelC.parameter.add(1L); // Mindest-Anzahl = 1
		final UvAlgorithmusDynDaten dynC = gibDatenMinimal3(List.of(regelC));
		assertTrue(dynC.gibIstRegelFehlerhaft(regelC), "Regel 42 ohne Lerngruppen-IDs muss als fehlerhaft markiert werden.");
		checkMalus(dynC, 20, 0, 112, 0, 0);

		// Szenario D: Mindest-Anzahl = 0 -> ungültig, Regel wird als fehlerhaft markiert.
		final UvBlockungRegel regelD = gibRegel42LehrkraftMindestensBMalInLerngruppen(
				UvDatensatz3.LEHRER1,
				0, // ungültig
				gruppe12,
				prioritaetHoch);
		final UvAlgorithmusDynDaten dynD = gibDatenMinimal3(List.of(regelD));
		assertTrue(dynD.gibIstRegelFehlerhaft(regelD), "Regel 42 mit Mindest-Anzahl 0 muss als fehlerhaft markiert werden.");
		checkMalus(dynD, 20, 0, 112, 0, 0);

		// Szenario E: Mindest-Anzahl kleiner 0 (negativ) -> ungültig.
		final UvBlockungRegel regelE = new UvBlockungRegel();
		regelE.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN.nr;
		regelE.prioritaet = prioritaetHoch;
		regelE.parameter.add(UvDatensatz3.LEHRER1);
		regelE.parameter.add(-1L);
		regelE.parameter.addAll(gruppe12);
		final UvAlgorithmusDynDaten dynE = gibDatenMinimal3(List.of(regelE));
		assertTrue(dynE.gibIstRegelFehlerhaft(regelE), "Regel 42 mit negativer Mindest-Anzahl muss als fehlerhaft markiert werden.");
		checkMalus(dynE, 20, 0, 112, 0, 0);
	}


	@Test
	@DisplayName("testRegel43LehrkraftMaximalBMalInLerngruppen")
	void testRegel43LehrkraftMaximalBMalInLerngruppen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Hilfslisten für die Lerngruppen-IDs aus UvDatensatz3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		final @NotNull List<Long> gruppe123 = new ArrayList<>();
		gruppe123.add(UvDatensatz3.LG1);
		gruppe123.add(UvDatensatz3.LG2);
		gruppe123.add(UvDatensatz3.LG4);

		// 1) MAXIMAL = 2, Lehrkraft 1 in zwei erlaubten Lerngruppen.
		final @NotNull UvBlockungRegel regel1 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 0, 80, 0, 0);
		assertEquals(2, dyn1.gibAktuelleZuordnung().size());

		// 2) MAXIMAL = 1, gleiche Zuordnung verletzt die Regel.
		final @NotNull UvBlockungRegel regel2 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 18, 1, 80, 0, 0);
		assertEquals(2, dyn2.gibAktuelleZuordnung().size());

		// 3) MAXIMAL = 0, Lehrkraft 1 darf in keiner Lerngruppe vorkommen.
		final @NotNull UvBlockungRegel regel3 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 0, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 1, 92, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size());

		// 4) Regel deaktiviert, Verletzung erzeugt keinen Regelmalus.
		final @NotNull UvBlockungRegel regel4 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetDeaktiviert);
		final @NotNull UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 18, 0, 80, 0, 0);
		assertEquals(2, dyn4.gibAktuelleZuordnung().size());

		// 5) Lehrkraft 1 in drei passenden Lerngruppen, MAXIMAL = 2.
		final @NotNull UvBlockungRegel regel5 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe123, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 17, 1, 76, 0, 0);
		assertEquals(3, dyn5.gibAktuelleZuordnung().size());
	}


	@Test
	@DisplayName("testRegel43LehrkraftMaximalBMalInLerngruppen_UngueltigeParameter")
	void testRegel43LehrkraftMaximalBMalInLerngruppen_UngueltigeParameter() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Hilfslisten für die Lerngruppen-IDs aus UvDatensatz3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		// 1) Fehlende Lerngruppen-IDs.
		final @NotNull UvBlockungRegel regel1 = new UvBlockungRegel();
		regel1.typ = UvBlockungRegelTyp.LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN.nr;
		regel1.prioritaet = prioritaetHoch;
		regel1.parameter.add(UvDatensatz3.LEHRER1);
		regel1.parameter.add(1L);
		final @NotNull UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertTrue(dyn1.gibIstRegelFehlerhaft(regel1));

		// 2) Ungültige Lehrkraft-ID.
		final @NotNull UvBlockungRegel regel2 = gibRegel43LehrkraftMaximalBMalInLerngruppen(999L, 1, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		assertTrue(dyn2.gibIstRegelFehlerhaft(regel2));

		// 3) Ungültiger MAXIMAL-Wert.
		final @NotNull UvBlockungRegel regel3 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, -1, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// 4) Leere Lerngruppenliste.
		final @NotNull UvBlockungRegel regel4 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, new ArrayList<>(), prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));
	}


	@Test
	@DisplayName("testRegel44LehrkraftDefaultSollIstAktivierung")
	void testRegel44LehrkraftDefaultSollIstAktivierung() {
		final int prioritaetMittel = UvBlockungRegelPrioritaet.MITTEL.nr;
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 1: Ohne Regel gilt der Standard.
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(new ArrayList<>());
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Ohne weitere Regeln soll es noch keine Zuordnungen geben.");

		// Szenario 2: Aktiviert + Priorität MITTEL entspricht dem Standard.
		final UvBlockungRegel regel2 = gibRegel44LehrkraftDefaultSollIstAktivierung(1, prioritaetMittel);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2));
		checkMalus(dyn2, 20, 0, 112, 0, 0);
		assertEquals(0, dyn2.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Deaktiviert + Priorität MITTEL entfernt den MITTEL-Malus vollständig.
		final UvBlockungRegel regel3 = gibRegel44LehrkraftDefaultSollIstAktivierung(0, prioritaetMittel);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		checkMalus(dyn3, 20, 0, 0, 0, 0);
		assertEquals(0, dyn3.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn3, 19, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Nach dem Add soll genau 1 Zuordnung vorhanden sein.");
		dyn3.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER1);
		checkMalus(dyn3, 20, 0, 0, 0, 0);
		assertEquals(0, dyn3.gibAktuelleZuordnung().size(), "Nach dem Del soll keine Zuordnung mehr vorhanden sein.");

		// Szenario 4: Aktiviert + Priorität GERING verschiebt den Malus von MITTEL nach GERING.
		final UvBlockungRegel regel4 = gibRegel44LehrkraftDefaultSollIstAktivierung(1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		checkMalus(dyn4, 20, 0, 0, 112, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn4, 19, 0, 0, 92, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Nach dem Add soll genau 1 Zuordnung vorhanden sein.");
		dyn4.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER1);
		checkMalus(dyn4, 20, 0, 0, 112, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Nach dem Del soll keine Zuordnung mehr vorhanden sein.");

		// Szenario 5: Kein Parameter -> Regel ist fehlerhaft, Standard bleibt erhalten.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A.nr;
		regel5.prioritaet = prioritaetMittel;
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		checkMalus(dyn5, 20, 0, 112, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Ungültiger Wert -1 -> Regel ist fehlerhaft, Standard bleibt erhalten.
		final UvBlockungRegel regel6 = gibRegel44LehrkraftDefaultSollIstAktivierung(-1, prioritaetMittel);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Ungültiger Wert 2 -> Regel ist fehlerhaft, Standard bleibt erhalten.
		final UvBlockungRegel regel7 = gibRegel44LehrkraftDefaultSollIstAktivierung(2, prioritaetMittel);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel45LehrkraftSollIstAktivierung")
	void testRegel45LehrkraftSollIstAktivierung() {
		final int prioritaetMittel = UvBlockungRegelPrioritaet.MITTEL.nr;
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 1: Ohne Regel gilt der Standard. LEHRER1 hat im Startzustand 36 Malus, LEHRER5 hat 4 Malus.
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(new ArrayList<>());
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Ohne weitere Regeln soll es noch keine Zuordnungen geben.");

		// Szenario 2: Lehrkraft 1 auf GERING aktiviert, Lehrkraft 5 auf GERING aktiviert.
		// 36 + 4 werden von MITTEL nach GERING verschoben.
		final UvBlockungRegel regel2a = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, 1, prioritaetGering);
		final UvBlockungRegel regel2b = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER5, 1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal3(List.of(regel2a, regel2b));
		checkMalus(dyn2, 20, 0, 72, 40, 0);
		assertEquals(0, dyn2.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2a));
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2b));
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER5, false);
		checkMalus(dyn2, 19, 0, 72, 36, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn2.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER5);
		checkMalus(dyn2, 20, 0, 72, 40, 0);

		// Szenario 3: Lehrkraft 1 auf GERING aktiviert, Lehrkraft 5 deaktiviert.
		// 36 werden nach GERING verschoben, 4 entfallen vollständig.
		final UvBlockungRegel regel3a = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, 1, prioritaetGering);
		final UvBlockungRegel regel3b = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER5, 0, prioritaetMittel);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3a, regel3b));
		checkMalus(dyn3, 20, 0, 72, 36, 0);
		assertEquals(0, dyn3.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3a));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3b));
		dyn3.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER5, false);
		checkMalus(dyn3, 19, 0, 72, 36, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn3.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER5);
		checkMalus(dyn3, 20, 0, 72, 36, 0);

		// Szenario 4: Lehrkraft 1 deaktiviert, Lehrkraft 5 auf GERING aktiviert.
		// 36 entfallen vollständig, 4 werden nach GERING verschoben.
		final UvBlockungRegel regel4a = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, 0, prioritaetMittel);
		final UvBlockungRegel regel4b = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER5, 1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4a, regel4b));
		checkMalus(dyn4, 20, 0, 72, 4, 0);
		assertEquals(0, dyn4.gibAktuelleZuordnung().size(), "Es soll noch keine Zuordnungen geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4a));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4b));
		dyn4.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER5, false);
		checkMalus(dyn4, 19, 0, 72, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn4.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER5);
		checkMalus(dyn4, 20, 0, 72, 4, 0);
	}


	@Test
	@DisplayName("testRegel45LehrkraftSollIstAktivierung_UngueltigeParameter")
	void testRegel45LehrkraftSollIstAktivierung_UngueltigeParameter() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 6: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt, Standard bleibt erhalten.
		final UvBlockungRegel regel6 = gibRegel45LehrkraftSollIstAktivierung(999L, 1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Ungültige Aktivierung -1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, -1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Ungültige Aktivierung 2 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, 2, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		checkMalus(dyn8, 20, 0, 112, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Fehlender zweiter Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B.nr;
		regel9.prioritaet = prioritaetGering;
		regel9.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		checkMalus(dyn9, 20, 0, 112, 0, 0);
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


}
