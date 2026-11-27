package de.svws_nrw.core.utils.uvblockung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

	/** Durch einen eigenen Logger werden die absichtlichen ERROR-Meldungen konsumiert. */
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
	 * Weder Lehrer, noch Lerngruppen, noch Fächer, noch Klassen sind vorhanden.
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
	 * Der Lehrer hat die Lehrbefähigung für das Fach, d. h. er kann zugeordnet werden.
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
	 * Erzeugt einen {@link UvAlgorithmusDynDaten}-Zustand wie {@link #gibDatenMinimal1(List)}, aber mit angepassten Wochenstunden
	 * einer Lerngruppe. (Für Tests mit gebrochenen Stunden-Werten.)
	 *
	 * @param regeln          die für den Test zu verwendenden Blockungsregeln.
	 * @param idLerngruppe    die UV-ID der Lerngruppe, deren Wochenstunden angepasst wird.
	 * @param wochenstunden   die neuen Wochenstunden der Lerngruppe.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt mit minimaler Datenmenge.
	 */
	private static UvAlgorithmusDynDaten gibDatenMinimal1MitWochenstunden(
			final List<UvBlockungRegel> regeln,
			final long idLerngruppe,
			final double wochenstunden) {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		man.lerngruppeGetByIdOrException(idLerngruppe).wochenstunden = wochenstunden;
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
	 * 8 SEHR_HOCH-Malus, weil die Leitungen 1 und 2 fehlen.
	 *
	 * Es gibt 12 Lerngruppen mit einem Bedarf von je 2 SOLL-Stunden:
	 * 12 SEHR_HOCH-Malus
	 *
	 * Also insgesamt 20 SEHR_HOCH-Malus.
	 *
	 *
	 * Es gibt 6 Lehrkräfte mit SOLL 6, 6, 4, 4, 2, 2:
	 * 6² + 6² + 4² + 4² + 2² + 2² = 112 MITTEL-Malus.
	 *
	 * Also insgesamt 112 MITTEL-Malus.
	 *
	 *
	 * @param regeln   die für den Test zu verwendenden Blockungsregeln.
	 *
	 * @return ein {@link UvAlgorithmusDynDaten}-Objekt mit vier Klassen, zwölf Lerngruppen und sechs Lehrkräften.
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


	private static UvBlockungRegel gibRegel19LehrkraftVerbotenInJahrgang(final long idLehrer, final long idJahrgang, final int prioritaet) {
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

		// Szenario 4: Fixierte Zuordnung kann nicht entfernt werden (Log) und bleibt erhalten.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertDoesNotThrow(() -> dyn4.setzeLerngruppeLehrkraftDel(1L, 5L));
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Fixierte Zuordnung muss erhalten bleiben.");

		// Szenario 5: Eine nicht vorhandene Beziehung zu löschen wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn5.setzeLerngruppeLehrkraftDel(1L, 5L));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung geben.");

		// Szenario 6: Doppelte Zuordnung wird ignoriert (Log) und lässt Malus und Zustand unverändert.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		assertDoesNotThrow(() -> dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		checkMalus(dyn6, 2, 0, 0, 0, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Es darf nur 1 Zuordnung geben.");
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

		// Szenario 4: Doppelt hinzufügen wird ignoriert (Log) und lässt Malus und Zustand unverändert.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		assertDoesNotThrow(() -> dyn4.setzeKlassenLeitung1Add(4L, 5L, false));
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Es darf nur 1 Zuordnung geben.");

		// Szenario 5: Entfernen ohne Zuordnung wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn5.setzeKlassenLeitung1Del(4L, 5L));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");

		// Szenario 6: Fixierte Klassenleitung kann nicht entfernt werden (Log) und bleibt erhalten.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertDoesNotThrow(() -> dyn6.setzeKlassenLeitung1Del(4L, 5L));
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss erhalten bleiben.");

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

		// Szenario 4: Doppelt hinzufügen wird ignoriert (Log) und lässt Malus und Zustand unverändert.
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(new ArrayList<>());
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		assertDoesNotThrow(() -> dyn4.setzeKlassenLeitung2Add(4L, 5L, false));
		checkMalus(dyn4, 2, 0, 4, 0, 0);
		assertEquals(1, dyn4.gibAktuelleKlassenleitungen().size(), "Es darf nur 1 Zuordnung geben.");

		// Szenario 5: Entfernen ohne Zuordnung wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn5.setzeKlassenLeitung2Del(4L, 5L));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");

		// Szenario 6: Fixierte stellv. Klassenleitung kann nicht entfernt werden (Log) und bleibt erhalten.
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(new ArrayList<>());
		dyn6.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertDoesNotThrow(() -> dyn6.setzeKlassenLeitung2Del(4L, 5L));
		checkMalus(dyn6, 2, 0, 4, 0, 0);
		assertEquals(1, dyn6.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss erhalten bleiben.");

		// Szenario 7: Add -> Del -> Add funktioniert wieder sauber.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(new ArrayList<>());
		dyn7.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn7.setzeKlassenLeitung2Del(4L, 5L);
		dyn7.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn7, 2, 0, 4, 0, 0);
		assertEquals(1, dyn7.gibAktuelleKlassenleitungen().size(), "Zuordnung muss nach erneutem Add wieder vorhanden sein.");

		// Szenario 8: Wie Szenario 2 - erneute Prüfung auf einem frischen Zustand.
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(new ArrayList<>());
		dyn8.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn8, 2, 0, 4, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Es sollte genau eine stellv. Klassenleitung existieren.");
	}


	@Test
	@DisplayName("testRegel00KlasseKlassenleitung2Sonderfaelle")
	void testRegel00KlasseKlassenleitung2Sonderfaelle() {
		// Szenario 9: Lehrkraft ist bereits Leitung 1 - der Leitung-2-Add wird ignoriert und lässt den Malus unverändert (Bugfix).
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(new ArrayList<>());
		dyn9.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn9, 2, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn9.setzeKlassenLeitung2Add(4L, 5L, false));
		checkMalus(dyn9, 2, 0, 4, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Es darf keine zweite Klassenleitung entstehen.");

		// Szenario 10: Unbekannte Klassen-ID bei Add/Del wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn10.setzeKlassenLeitung2Add(999L, 5L, false));
		assertDoesNotThrow(() -> dyn10.setzeKlassenLeitung2Del(999L, 5L));
		checkMalus(dyn10, 3, 0, 4, 0, 0);
		assertEquals(0, dyn10.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");

		// Szenario 11: Unbekannte Lehrkraft-ID bei Add/Del wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn11.setzeKlassenLeitung2Add(4L, 999L, false));
		assertDoesNotThrow(() -> dyn11.setzeKlassenLeitung2Del(4L, 999L));
		checkMalus(dyn11, 3, 0, 4, 0, 0);
		assertEquals(0, dyn11.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");
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
	}


	@Test
	@DisplayName("testRegel01KlasseDefaultBenoetigtKlassenlehrer2")
	void testRegel01KlasseDefaultBenoetigtKlassenlehrer2() {
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

		// Szenario 7: Doppelt hinzufügen wird ignoriert (Log) und lässt Malus und Zustand unverändert.
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(new ArrayList<>());
		dyn7.setzeKlassenLeitung1Add(4L, 5L, false);
		assertDoesNotThrow(() -> dyn7.setzeKlassenLeitung1Add(4L, 5L, false));
		checkMalus(dyn7, 2, 0, 4, 0, 0);
		assertEquals(1, dyn7.gibAktuelleKlassenleitungen().size(), "Es darf nur 1 Zuordnung geben.");

		// Szenario 8: Entfernen ohne Zuordnung wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn8.setzeKlassenLeitung1Del(4L, 5L));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertEquals(0, dyn8.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");

		// Szenario 9: Fixierte Zuordnung kann nicht entfernt werden (Log) und bleibt erhalten.
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(new ArrayList<>());
		dyn9.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn9, 2, 0, 4, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss vorhanden sein.");
		assertDoesNotThrow(() -> dyn9.setzeKlassenLeitung1Del(4L, 5L));
		checkMalus(dyn9, 2, 0, 4, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss erhalten bleiben.");
	}


	@Test
	@DisplayName("testRegel01KlasseDefaultBenoetigtKlassenlehrer3")
	void testRegel01KlasseDefaultBenoetigtKlassenlehrer3() {
		// Szenario 10: Add -> Del -> Add funktioniert wieder sauber.
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(new ArrayList<>());
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn10.setzeKlassenLeitung1Del(4L, 5L);
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn10, 2, 0, 4, 0, 0);
		assertEquals(1, dyn10.gibAktuelleKlassenleitungen().size(), "Zuordnung muss nach erneutem Add wieder vorhanden sein.");

		// Szenario 11: Lehrkraft ist bereits Leitung 2 - der Leitung-1-Add wird ignoriert und lässt den Malus unverändert (Bugfix).
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(new ArrayList<>());
		dyn11.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn11, 2, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn11.setzeKlassenLeitung1Add(4L, 5L, false));
		checkMalus(dyn11, 2, 0, 4, 0, 0);
		assertEquals(1, dyn11.gibAktuelleKlassenleitungen().size(), "Es darf keine zweite Klassenleitung entstehen.");

		// Szenario 12: Unbekannte Klassen-ID bei Add/Del wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn12.setzeKlassenLeitung1Add(999L, 5L, false));
		assertDoesNotThrow(() -> dyn12.setzeKlassenLeitung1Del(999L, 5L));
		checkMalus(dyn12, 3, 0, 4, 0, 0);
		assertEquals(0, dyn12.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");

		// Szenario 13: Unbekannte Lehrkraft-ID bei Add/Del wird ignoriert (Log) und lässt den Malus unverändert.
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1(new ArrayList<>());
		assertDoesNotThrow(() -> dyn13.setzeKlassenLeitung1Add(4L, 999L, false));
		assertDoesNotThrow(() -> dyn13.setzeKlassenLeitung1Del(4L, 999L));
		checkMalus(dyn13, 3, 0, 4, 0, 0);
		assertEquals(0, dyn13.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");
	}


	@Test
	@DisplayName("testRegel01KlasseDefaultBenoetigtKlassenlehrer4")
	void testRegel01KlasseDefaultBenoetigtKlassenlehrer4() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 14: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel14 = new UvBlockungRegel();
		regel14.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr;
		regel14.prioritaet = prioritaetSehrHoch;
		regel14.parameter.add(0L);
		regel14.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal1(List.of(regel14));
		assertTrue(dyn14.gibIstRegelFehlerhaft(regel14));
	}


	@Test
	@DisplayName("testRegel02KlasseBenoetigtKlassenlehrer: Individuelles Klassenlehrer-Soll überschreibt den Default.")
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
	}


	@Test
	@DisplayName("testRegel02KlasseBenoetigtKlassenlehrer2")
	void testRegel02KlasseBenoetigtKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

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

		// Szenario 11: Soll=0 - eine zusätzliche Klassenleitung erzeugt Malus (absolute Abweichung).
		final UvBlockungRegel regel11 = gibRegel02KlasseBenoetigtKlassenlehrer(4L, 0, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal2(List.of(regel11));
		checkMalus(dyn11, 5, 0, 8, 0, 0);
		dyn11.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn11, 6, 0, 8, 0, 0);
		assertEquals(1, dyn11.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn11.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn11, 5, 0, 8, 0, 0);
	}


	@Test
	@DisplayName("testRegel02KlasseBenoetigtKlassenlehrer3")
	void testRegel02KlasseBenoetigtKlassenlehrer3() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 12: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER.nr;
		regel12.prioritaet = prioritaetSehrHoch;
		regel12.parameter.add(4L);
		regel12.parameter.add(1L);
		regel12.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal2(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));
	}


	@Test
	@DisplayName("testRegel02PrioritaetsGuard: Deaktivierte oder ungueltige Prioritaet erzeugt keinen Malusbeitrag.")
	void testRegel02PrioritaetsGuard() {
		// Direkter Test der Regel-Klasse: Prioritaet DEAKTIVIERT (5) traegt keinen Malus bei.
		final int[] malus = new int[UvBlockungRegelPrioritaet.values().length];
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final UvAlgorithmusDynDatenKlasse klasse = new UvAlgorithmusDynDatenKlasse(LOG, RND, 0,
				man.klasseGetByIdOrException(UvDatensatz1.ID_KLASSE), new UvAlgorithmusDynDatenLehrkraft[0]);
		final UvAlgorithmusDynDatenRegel02 regel = new UvAlgorithmusDynDatenRegel02(LOG, malus, klasse, 1, UvBlockungRegelPrioritaet.DEAKTIVIERT.nr);
		assertEquals(0, malus[UvBlockungRegelPrioritaet.DEAKTIVIERT.nr], "Deaktivierte Prioritaet darf keinen Malus erhalten.");
		regel.changeMalus(1);
		assertEquals(0, malus[UvBlockungRegelPrioritaet.DEAKTIVIERT.nr], "Deaktivierte Prioritaet darf keinen Malus erhalten.");

		// Setter mit gueltiger Prioritaet: Der Malus erscheint im richtigen Slot.
		regel.setzeLeitung1AnzahlUndPrioritaet(2, UvBlockungRegelPrioritaet.SEHR_HOCH.nr);
		assertEquals(2, malus[UvBlockungRegelPrioritaet.SEHR_HOCH.nr], "Soll=2 bei 0 Zuordnungen ergibt Malus 2.");
		assertEquals(0, malus[UvBlockungRegelPrioritaet.DEAKTIVIERT.nr], "Slot DEAKTIVIERT muss leer bleiben.");

		// Setter mit ungueltiger Prioritaet (99): Die Aenderung wird ignoriert.
		regel.setzeLeitung1AnzahlUndPrioritaet(3, 99);
		assertEquals(2, malus[UvBlockungRegelPrioritaet.SEHR_HOCH.nr], "Ungueltige Prioritaet darf nichts aendern.");

		// Integration: Regel mit ungueltiger Prioritaet 99 verhaelt sich wie der Default.
		final UvBlockungRegel regel99 = gibRegel02KlasseBenoetigtKlassenlehrer(UvDatensatz2.ID_KLASSE1, 2, 99);
		final UvAlgorithmusDynDaten dyn = gibDatenMinimal2(List.of(regel99));
		checkMalus(dyn, 6, 0, 8, 0, 0);
		assertFalse(dyn.gibIstRegelFehlerhaft(regel99));
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
	@DisplayName("testRegel03KlasseDefaultBenoetigtStellvKlassenlehrer2")
	void testRegel03KlasseDefaultBenoetigtStellvKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 9: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr;
		regel9.prioritaet = prioritaetSehrHoch;
		regel9.parameter.add(0L);
		regel9.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Doppeltes Add und Del ohne Zuordnung werden ignoriert (Log).
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(new ArrayList<>());
		dyn10.setzeKlassenLeitung2Add(4L, 5L, false);
		assertDoesNotThrow(() -> dyn10.setzeKlassenLeitung2Add(4L, 5L, false));
		checkMalus(dyn10, 2, 0, 4, 0, 0);
		assertEquals(1, dyn10.gibAktuelleKlassenleitungen().size(), "Es darf nur 1 Zuordnung geben.");
		dyn10.setzeKlassenLeitung2Del(4L, 5L);
		assertDoesNotThrow(() -> dyn10.setzeKlassenLeitung2Del(4L, 5L));
		checkMalus(dyn10, 3, 0, 4, 0, 0);
		assertEquals(0, dyn10.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung geben.");
	}


	@Test
	@DisplayName("testRegel04KlasseBenoetigtStellvKlassenlehrer: Individuelles stellv. Klassenlehrer-Soll überschreibt den Default.")
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
	}


	@Test
	@DisplayName("testRegel04KlasseBenoetigtStellvKlassenlehrer2")
	void testRegel04KlasseBenoetigtStellvKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

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

		// Szenario 12: Soll=0 - eine zusätzliche stellv. Klassenleitung erzeugt Malus (absolute Abweichung).
		final UvBlockungRegel regel12 = gibRegel04KlasseBenoetigtStellvKlassenlehrer(4L, 0, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal2(List.of(regel12));
		checkMalus(dyn12, 5, 0, 8, 0, 0);
		dyn12.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn12, 6, 0, 8, 0, 0);
		assertEquals(1, dyn12.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		dyn12.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn12, 5, 0, 8, 0, 0);
	}


	@Test
	@DisplayName("testRegel04KlasseBenoetigtStellvKlassenlehrer3")
	void testRegel04KlasseBenoetigtStellvKlassenlehrer3() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 13: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel13 = new UvBlockungRegel();
		regel13.typ = UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER.nr;
		regel13.prioritaet = prioritaetSehrHoch;
		regel13.parameter.add(4L);
		regel13.parameter.add(1L);
		regel13.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal2(List.of(regel13));
		assertTrue(dyn13.gibIstRegelFehlerhaft(regel13));
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

		// Szenario 6: Zwei Lerngruppen derselben Lehrkraft reichen nicht: In der Klasse fehlen 2 der geforderten 4 Stunden.
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
	@DisplayName("testRegel05LehrkraftDefaultWennKlassenlehrerDannMindestensAStunden2")
	void testRegel05LehrkraftDefaultWennKlassenlehrerDannMindestensAStunden2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 9: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel9.prioritaet = prioritaetHoch;
		regel9.parameter.add(2L);
		regel9.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Eine fixierte Klassenleitung zählt für die Stundenregel.
		final UvBlockungRegel regel10 = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10));
		dyn10.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn10, 2, 2, 4, 0, 0);
		dyn10.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn10, 1, 0, 0, 0, 0);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10));
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
	}


	@Test
	@DisplayName("testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse2")
	void testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 6: Die Lehrkraft hat 4 Stunden insgesamt, aber nur 2 in der Klasse 4; es fehlen 2 der geforderten 4 Stunden.
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

		// Szenario 9: Regel 06 überschreibt den Default (Regel 05) für Lehrkraft 5 - unabhängig von der Reihenfolge.
		final UvBlockungRegel regel9a = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvBlockungRegel regel9b = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9a, regel9b));
		dyn9.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn9, 2, 4, 4, 0, 0);
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9a));
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9b));

		final UvBlockungRegel regel10a = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvBlockungRegel regel10b = gibRegel05LehrkraftDefaultWennKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10a, regel10b));
		dyn10.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn10, 2, 4, 4, 0, 0);

		// Szenario 10: Ungültiger Wert (negativ) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel11 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(List.of(regel11));
		assertTrue(dyn11.gibIstRegelFehlerhaft(regel11));

		// Szenario 11: Abweichung von genau 0,5 Stunden (2,5 statt 3) erzeugt noch keinen Malus.
		final UvBlockungRegel regel12 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 3, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal1MitWochenstunden(List.of(regel12), 1L, 2.5);
		dyn12.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn12, 2, 3, 4, 0, 0);
		dyn12.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn12, 1, 0, 0, 0, 0);

		// Szenario 12: Abweichung von mehr als 0,5 Stunden (2,4 statt 3) erzeugt einen Malus.
		final UvBlockungRegel regel13 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 3, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1MitWochenstunden(List.of(regel13), 1L, 2.4);
		dyn13.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn13.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn13, 1, 1, 0, 0, 0);
	}


	@Test
	@DisplayName("testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse3")
	void testRegel06LehrkraftAWennKlassenlehrerDannMindestensBStundenInKlasse3() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 13: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel13 = new UvBlockungRegel();
		regel13.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel13.prioritaet = prioritaetHoch;
		regel13.parameter.add(5L);
		regel13.parameter.add(2L);
		regel13.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1(List.of(regel13));
		assertTrue(dyn13.gibIstRegelFehlerhaft(regel13));

		// Szenario 14: Eine fixierte Klassenleitung zählt für die Stundenregel.
		final UvBlockungRegel regel14 = gibRegel06LehrkraftAWennKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal1(List.of(regel14));
		dyn14.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn14, 2, 2, 4, 0, 0);
		dyn14.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn14, 1, 0, 0, 0, 0);
		assertFalse(dyn14.gibIstRegelFehlerhaft(regel14));
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

		// Szenario 6: Ungültiger Wert (negativ) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(-1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Fehlender Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel7.prioritaet = prioritaetHoch;
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Größerer Datensatz (gibDatenMinimal2) mit beiden Lerngruppen; in der Klasse 4 fehlen weiterhin 2 der geforderten 4 Stunden.
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
	@DisplayName("testRegel07LehrkraftDefaultWennStellvKlassenlehrerDannMindestensAStundenInKlasse2")
	void testRegel07LehrkraftDefaultWennStellvKlassenlehrerDannMindestensAStundenInKlasse2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 9: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr;
		regel9.prioritaet = prioritaetHoch;
		regel9.parameter.add(2L);
		regel9.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Eine fixierte stellv. Klassenleitung zählt für die Stundenregel.
		final UvBlockungRegel regel10 = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10));
		dyn10.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn10, 2, 2, 4, 0, 0);
		dyn10.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn10, 1, 0, 0, 0, 0);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10));
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
	}


	@Test
	@DisplayName("testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse2")
	void testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 6: Ungültiger Wert (negativ) wird als fehlerhaft erkannt.
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

		// Szenario 9: Größerer Datensatz mit zwei Lerngruppen; in der Klasse 4 fehlen dennoch 2 der geforderten 4 Stunden.
		final UvBlockungRegel regel9 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		dyn9.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn9.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		dyn9.setzeLerngruppeLehrkraftAdd(2L, 5L, false);
		checkMalus(dyn9, 3, 2, 8, 0, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size());
		assertEquals(2, dyn9.gibAktuelleZuordnung().size());
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Regel 08 überschreibt den Default (Regel 07) für Lehrkraft 5 - unabhängig von der Reihenfolge.
		final UvBlockungRegel regel10a = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvBlockungRegel regel10b = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10a, regel10b));
		dyn10.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn10, 2, 4, 4, 0, 0);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10a));
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10b));

		final UvBlockungRegel regel11a = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 4, prioritaetHoch);
		final UvBlockungRegel regel11b = gibRegel07LehrkraftDefaultWennStellvKlassenlehrerDann(2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(List.of(regel11a, regel11b));
		dyn11.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn11, 2, 4, 4, 0, 0);

		// Szenario 11: Abweichung von genau 0,5 Stunden (2,5 statt 3) erzeugt noch keinen Malus.
		final UvBlockungRegel regel12 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 3, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal1MitWochenstunden(List.of(regel12), 1L, 2.5);
		dyn12.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn12.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn12, 1, 0, 0, 0, 0);

		// Szenario 12: Abweichung von mehr als 0,5 Stunden (2,4 statt 3) erzeugt einen Malus.
		final UvBlockungRegel regel13 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 3, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1MitWochenstunden(List.of(regel13), 1L, 2.4);
		dyn13.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn13.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn13, 1, 1, 0, 0, 0);
	}


	@Test
	@DisplayName("testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse3")
	void testRegel08LehrkraftAWennStellvKlassenlehrerDannMindestensBStundenInKlasse3() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 13: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel13 = new UvBlockungRegel();
		regel13.typ = UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE.nr;
		regel13.prioritaet = prioritaetHoch;
		regel13.parameter.add(5L);
		regel13.parameter.add(2L);
		regel13.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1(List.of(regel13));
		assertTrue(dyn13.gibIstRegelFehlerhaft(regel13));

		// Szenario 14: Eine fixierte stellv. Klassenleitung zählt für die Stundenregel.
		final UvBlockungRegel regel14 = gibRegel08LehrkraftAWennStellvKlassenlehrerDann(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal1(List.of(regel14));
		dyn14.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn14, 2, 2, 4, 0, 0);
		dyn14.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn14, 1, 0, 0, 0, 0);
		assertFalse(dyn14.gibIstRegelFehlerhaft(regel14));
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

		// Szenario 9: max=MIN - auch eine zweite Klassenleitung bleibt malusneutral (Klassen-Malus -1, Lehrkraft-Malus +1).
		final UvBlockungRegel regel9 = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		dyn9.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn9.setzeKlassenLeitung1Add(7L, 5L, false);
		checkMalus(dyn9, 6, 0, 8, 0, 0);
		assertEquals(2, dyn9.gibAktuelleKlassenleitungen().size(), "Beide Zuweisungen müssen erfolgen, da der Malus nicht schlechter wird.");
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel09LehrerDefaultMaxKlassenlehrer2")
	void testRegel09LehrerDefaultMaxKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 10: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr;
		regel10.prioritaet = prioritaetSehrHoch;
		regel10.parameter.add(0L);
		regel10.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));

		// Szenario 11: Eine fixierte Klassenleitung zählt für das Maximum mit.
		final UvBlockungRegel regel11 = gibRegel09LehrerDefaultMaxKlassenlehrer(UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal2(List.of(regel11));
		dyn11.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn11, 6, 0, 8, 0, 0);
		assertEquals(1, dyn11.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuweisung muss erfolgen.");
		assertFalse(dyn11.gibIstRegelFehlerhaft(regel11));
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

		// Szenario 10: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER.nr;
		regel10.prioritaet = prioritaetSehrHoch;
		regel10.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel10LehrerMaxKlassenlehrer2")
	void testRegel10LehrerMaxKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel10LehrerMaxKlassenlehrer(5L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(List.of(regel11));
		dyn11.setzeKlassenLeitung1Add(4L, 5L, false);
		checkMalus(dyn11, 2, 0, 4, 0, 0);
		dyn11.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn11, 3, 0, 4, 0, 0);
		assertEquals(0, dyn11.gibAktuelleKlassenleitungen().size());

		// Szenario 12: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER.nr;
		regel12.prioritaet = prioritaetSehrHoch;
		regel12.parameter.add(5L);
		regel12.parameter.add(0L);
		regel12.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal1(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));

		// Szenario 13: Eine fixierte Klassenleitung zählt für das Maximum mit.
		final UvBlockungRegel regel13 = gibRegel10LehrerMaxKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG1_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1(List.of(regel13));
		dyn13.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn13, 3, 0, 4, 0, 0);
		assertEquals(1, dyn13.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuweisung muss erfolgen.");
		assertFalse(dyn13.gibIstRegelFehlerhaft(regel13));
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
	}


	@Test
	@DisplayName("testRegel11LehrerDefaultMaxStellvKlassenlehrer2")
	void testRegel11LehrerDefaultMaxStellvKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

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

		// Szenario 11: max=MIN - auch eine zweite stellv. Klassenleitung bleibt malusneutral (Klassen-Malus -1, Lehrkraft-Malus +1).
		final UvBlockungRegel regel11 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal2(List.of(regel11));
		dyn11.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn11.setzeKlassenLeitung2Add(7L, 5L, false);
		checkMalus(dyn11, 6, 0, 8, 0, 0);
		assertEquals(2, dyn11.gibAktuelleKlassenleitungen().size(), "Beide Zuweisungen müssen erfolgen, da der Malus nicht schlechter wird.");
		assertFalse(dyn11.gibIstRegelFehlerhaft(regel11));
	}


	@Test
	@DisplayName("testRegel11LehrerDefaultMaxStellvKlassenlehrer3")
	void testRegel11LehrerDefaultMaxStellvKlassenlehrer3() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 12: Zwei Parameter statt einem werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr;
		regel12.prioritaet = prioritaetSehrHoch;
		regel12.parameter.add(0L);
		regel12.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal2(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));

		// Szenario 13: Eine fixierte stellv. Klassenleitung zählt für das Maximum mit.
		final UvBlockungRegel regel13 = gibRegel11LehrerDefaultMaxStellvKlassenlehrer(UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal2(List.of(regel13));
		dyn13.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn13, 6, 0, 8, 0, 0);
		assertEquals(1, dyn13.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuweisung muss erfolgen.");
		assertFalse(dyn13.gibIstRegelFehlerhaft(regel13));
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
	@DisplayName("testRegel12LehrerMaxStellvKlassenlehrer2")
	void testRegel12LehrerMaxStellvKlassenlehrer2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, 2, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(List.of(regel11));
		dyn11.setzeKlassenLeitung2Add(4L, 5L, false);
		checkMalus(dyn11, 2, 0, 4, 0, 0);
		dyn11.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn11, 3, 0, 4, 0, 0);
		assertEquals(0, dyn11.gibAktuelleKlassenleitungen().size());

		// Szenario 12: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER.nr;
		regel12.prioritaet = prioritaetSehrHoch;
		regel12.parameter.add(5L);
		regel12.parameter.add(0L);
		regel12.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal1(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));

		// Szenario 13: Eine fixierte stellv. Klassenleitung zählt für das Maximum mit.
		final UvBlockungRegel regel13 = gibRegel12LehrerMaxStellvKlassenlehrer(5L, UvRegelManager.LEHRER_LEITUNG2_MIN, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal1(List.of(regel13));
		dyn13.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn13, 3, 0, 4, 0, 0);
		assertEquals(1, dyn13.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuweisung muss erfolgen.");
		assertFalse(dyn13.gibIstRegelFehlerhaft(regel13));
	}


	@Test
	@DisplayName("testRegel13LerngruppeLehrkraftVerbot: Die einzige Lehrkraft ist in der Lerngruppe verboten.")
	void testRegel13LerngruppeLehrkraftVerbot() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Lerngruppe 1 verboten, Add wird ignoriert (Log).
		final UvBlockungRegel regel1 = gibRegel13LehrkraftVerbotenInLerngruppe(5L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");
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
	@DisplayName("testRegel13LerngruppeLehrkraftVerbot2")
	void testRegel13LerngruppeLehrkraftVerbot2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 5: Eine andere Lerngruppe darf der Lehrkraft zugeordnet werden.
		final UvBlockungRegel regel5 = gibRegel13LehrkraftVerbotenInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));
		dyn5.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn5, 19, 0, 92, 0, 0);
		assertEquals(1, dyn5.gibAktuelleZuordnung().size(), "Die Zuordnung zur anderen Lerngruppe muss erlaubt sein.");

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr;
		regel6.prioritaet = prioritaetSehrHoch;
		regel6.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(UvDatensatz3.LEHRER1);
		regel7.parameter.add(UvDatensatz3.LG1);
		regel7.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
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

		// Szenario 3: Fixierung bleibt bestehen, auch wenn man anschließend versucht, die Zuordnung zu löschen (Log).
		final UvBlockungRegel regel3 = gibRegel14LehrkraftFixiertInLerngruppe(5L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertDoesNotThrow(() -> dyn3.setzeLerngruppeLehrkraftDel(1L, 5L));
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Fixierte Zuordnung muss erhalten bleiben.");

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
	@DisplayName("testRegel14LerngruppeLehrkraftFixierung2")
	void testRegel14LerngruppeLehrkraftFixierung2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 6: Eine zweite (nicht fixierte) Lerngruppe darf zusätzlich zugeordnet werden.
		final UvBlockungRegel regel6 = gibRegel14LehrkraftFixiertInLerngruppe(UvDatensatz3.LEHRER1, UvDatensatz3.LG1, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		assertDoesNotThrow(() -> dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false));
		checkMalus(dyn6, 18, 0, 80, 0, 0);
		assertEquals(2, dyn6.gibAktuelleZuordnung().size(), "Beide Zuordnungen müssen vorhanden sein.");

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr;
		regel8.prioritaet = prioritaetSehrHoch;
		regel8.parameter.add(UvDatensatz3.LEHRER1);
		regel8.parameter.add(UvDatensatz3.LG1);
		regel8.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
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

		// Szenario 5: Unbekannte Lehrkraft- bzw. Lerngruppen-ID werden aussortiert.
		final UvBlockungRegel regel5a = gibRegel15LehrkraftGerneInLerngruppe(999L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5a = gibDatenMinimal1(List.of(regel5a));
		assertTrue(dyn5a.gibIstRegelFehlerhaft(regel5a));
		final UvBlockungRegel regel5b = gibRegel15LehrkraftGerneInLerngruppe(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5b = gibDatenMinimal1(List.of(regel5b));
		assertTrue(dyn5b.gibIstRegelFehlerhaft(regel5b));
	}


	@Test
	@DisplayName("testRegel15LerngruppeLehrkraftGerne2")
	void testRegel15LerngruppeLehrkraftGerne2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 6: Eine fixierte Zuordnung erfüllt den Wunsch.
		final UvBlockungRegel regel6 = gibRegel15LehrkraftGerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn6, 2, 0, 0, 0, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B.nr;
		regel8.prioritaet = prioritaetGering;
		regel8.parameter.add(5L);
		regel8.parameter.add(1L);
		regel8.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
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

		// Szenario 5: Unbekannte Lehrkraft- bzw. Lerngruppen-ID werden aussortiert.
		final UvBlockungRegel regel5a = gibRegel16LehrkraftUngerneInLerngruppe(999L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5a = gibDatenMinimal1(List.of(regel5a));
		assertTrue(dyn5a.gibIstRegelFehlerhaft(regel5a));
		final UvBlockungRegel regel5b = gibRegel16LehrkraftUngerneInLerngruppe(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5b = gibDatenMinimal1(List.of(regel5b));
		assertTrue(dyn5b.gibIstRegelFehlerhaft(regel5b));
	}


	@Test
	@DisplayName("testRegel16LerngruppeLehrkraftUngerne2")
	void testRegel16LerngruppeLehrkraftUngerne2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 6: Eine fixierte Zuordnung verletzt den Wunsch.
		final UvBlockungRegel regel6 = gibRegel16LehrkraftUngerneInLerngruppe(5L, 1L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn6, 2, 0, 0, 1, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B.nr;
		regel8.prioritaet = prioritaetGering;
		regel8.parameter.add(5L);
		regel8.parameter.add(1L);
		regel8.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
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
		checkMalus(dyn1, 18, 2, 80, 0, 0);
		assertEquals(2, dyn1.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		// Szenario 2:
		// Drei Lerngruppen liegen im selben Zeitslot.
		// Eine Lehrkraft erhält alle drei Lerngruppen --> 2 HOCH-Malus.
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
		checkMalus(dyn2, 18, 2, 80, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn2, 17, 6, 76, 0, 0);
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
		checkMalus(dyn4, 18, 2, 80, 0, 0);

		// Szenario 5: Wie Szenario 1 + Lerngruppen-Lehrkraft-Zuordnungen werden über Regel14 forciert (DAVOR).
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
		checkMalus(dyn5, 18, 2, 80, 0, 0);

		// Szenario 6: Fehlerhafte Parameter bzw. Referenzen werden als fehlerhaft erkannt.
		final UvBlockungRegel regel6a = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(999L, 1L, 1L, 0L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6a)).gibIstRegelFehlerhaft(regel6a));
		final UvBlockungRegel regel6b = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 8L, 1L, 0L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6b)).gibIstRegelFehlerhaft(regel6b));
		final UvBlockungRegel regel6c = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 33L, 0L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6c)).gibIstRegelFehlerhaft(regel6c));
		final UvBlockungRegel regel6d = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 9L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6d)).gibIstRegelFehlerhaft(regel6d));
		final UvBlockungRegel regel6e = new UvBlockungRegel();
		regel6e.typ = UvBlockungRegelTyp.LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D.nr;
		regel6e.prioritaet = prioritaetHoch;
		regel6e.parameter.add(UvDatensatz3.LG2);
		assertTrue(gibDatenMinimal3(List.of(regel6e)).gibIstRegelFehlerhaft(regel6e));
		final UvBlockungRegel regel6f = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 17L, 0L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6f)).gibIstRegelFehlerhaft(regel6f), "Stunde 17 liegt über dem Maximum.");
		final UvBlockungRegel regel6g = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 4L, prioritaetHoch);
		assertTrue(gibDatenMinimal3(List.of(regel6g)).gibIstRegelFehlerhaft(regel6g), "Wochentyp 4 liegt über dem Modell.");

		// Szenario 6h: Ein doppelter Zeitslot derselben Lerngruppe wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6h1 = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch);
		final UvBlockungRegel regel6h2 = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6h = gibDatenMinimal3(List.of(regel6h1, regel6h2));
		assertFalse(dyn6h.gibIstRegelFehlerhaft(regel6h1));
		assertTrue(dyn6h.gibIstRegelFehlerhaft(regel6h2));

		// Szenario 6i: Derselbe Wochentag und dieselbe Stunde, aber ein anderer Wochentyp derselben Lerngruppe ist erlaubt.
		final UvBlockungRegel regel6i = gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 1L, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6i = gibDatenMinimal3(List.of(regel6h1, regel6i));
		assertFalse(dyn6i.gibIstRegelFehlerhaft(regel6h1));
		assertFalse(dyn6i.gibIstRegelFehlerhaft(regel6i));
	}


	@Test
	@DisplayName("testRegel17LerngruppeLiegtAmWochentagStundeWochentyp3")
	void testRegel17LerngruppeLiegtAmWochentagStundeWochentyp3() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 7:
		// Zwei Lerngruppen liegen im selben Zeitslot mit Wochentyp 2 (B-Woche).
		// Eine Lehrkraft erhält beide Lerngruppen --> 1 HOCH-Malus.
		final @NotNull List<UvBlockungRegel> regeln7 = new ArrayList<>();
		regeln7.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 2L, prioritaetHoch));
		regeln7.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 2L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(regeln7);
		assertFalse(dyn7.gibIstRegelFehlerhaft(regeln7.get(0)));
		assertFalse(dyn7.gibIstRegelFehlerhaft(regeln7.get(1)));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn7, 19, 0, 92, 0, 0);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn7, 18, 2, 80, 0, 0);
		assertEquals(2, dyn7.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		// Szenario 8:
		// Eine Lerngruppe liegt in der A-Woche (1), die andere in der B-Woche (2) im selben Zeitslot.
		// Eine Lehrkraft erhält beide Lerngruppen --> kein Malus, da keine echte Kollision.
		final @NotNull List<UvBlockungRegel> regeln8 = new ArrayList<>();
		regeln8.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 1L, prioritaetHoch));
		regeln8.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 2L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(regeln8);
		assertFalse(dyn8.gibIstRegelFehlerhaft(regeln8.get(0)));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regeln8.get(1)));
		checkMalus(dyn8, 20, 0, 112, 0, 0);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn8, 19, 0, 92, 0, 0);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn8, 18, 0, 80, 0, 0);
		assertEquals(2, dyn8.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

	}


	@Test
	@DisplayName("testRegel17PrioritaetSehrHoch: Der Kollisionsmalus landet bei SEHR_HOCH.")
	void testRegel17PrioritaetSehrHoch() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Zwei Lerngruppen im selben Zeitslot, beide mit derselben Lehrkraft.
		final @NotNull List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetSehrHoch));
		regeln.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetSehrHoch));
		final UvAlgorithmusDynDaten dyn = gibDatenMinimal3(regeln);
		checkMalus(dyn, 20, 0, 112, 0, 0);
		dyn.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn, 20, 0, 80, 0, 0);
	}


	@Test
	@DisplayName("testRegel17KollisionsMalusZurechnung: Der Kollisionsmalus wird deterministisch zugerechnet.")
	void testRegel17KollisionsMalusZurechnung() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 9: Eine Lerngruppe im Wochentyp 0 verbindet alle Wochen.
		// Drei Lerngruppen im selben Zeitslot, dieselbe Lehrkraft --> 2 HOCH-Malus.
		final @NotNull List<UvBlockungRegel> regeln9 = new ArrayList<>();
		regeln9.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 1L, prioritaetHoch));
		regeln9.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG8, 1L, 1L, 2L, prioritaetHoch));
		regeln9.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(regeln9);
		assertFalse(dyn9.gibIstRegelFehlerhaft(regeln9.get(0)));
		assertFalse(dyn9.gibIstRegelFehlerhaft(regeln9.get(1)));
		assertFalse(dyn9.gibIstRegelFehlerhaft(regeln9.get(2)));
		checkMalus(dyn9, 20, 0, 112, 0, 0);
		dyn9.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn9, 19, 0, 92, 0, 0);
		dyn9.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn9, 18, 0, 80, 0, 0);
		dyn9.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn9, 17, 2, 76, 0, 0);
		assertEquals(3, dyn9.gibAktuelleZuordnung().size(), "Es sollten genau 3 Zuordnungen vorhanden sein.");

		// Szenario 10: Drei Lerngruppen in drei verschiedenen Wochen (Wochentyp 1, 2, 3) --> kein Malus.
		final @NotNull List<UvBlockungRegel> regeln10 = new ArrayList<>();
		regeln10.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 1L, prioritaetHoch));
		regeln10.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 2L, prioritaetHoch));
		regeln10.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG8, 1L, 1L, 3L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(regeln10);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regeln10.get(0)));
		assertFalse(dyn10.gibIstRegelFehlerhaft(regeln10.get(1)));
		assertFalse(dyn10.gibIstRegelFehlerhaft(regeln10.get(2)));
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn10, 17, 0, 76, 0, 0);
		assertEquals(3, dyn10.gibAktuelleZuordnung().size(), "Es sollten genau 3 Zuordnungen vorhanden sein.");

		// Szenario 11: Der Kollisionsmalus wird dem Träger (kleinste interne ID) zugerechnet, unabhängig von der Reihenfolge.
		final @NotNull List<UvBlockungRegel> regeln11 = new ArrayList<>();
		regeln11.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regeln11.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetSehrHoch));
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(regeln11);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 19, 1, 80, 0, 0);
		assertEquals(2, dyn11.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");

		final @NotNull List<UvBlockungRegel> regeln12 = new ArrayList<>();
		regeln12.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetSehrHoch));
		regeln12.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(regeln12);
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn12, 19, 1, 80, 0, 0);
		assertEquals(2, dyn12.gibAktuelleZuordnung().size(), "Es sollten genau 2 Zuordnungen vorhanden sein.");
	}


	@Test
	@DisplayName("testRegel17KollisionenWochentypen: Wochentyp 0 kollidiert mit allem, Wochentypen > 0 nur untereinander.")
	void testRegel17KollisionenWochentypen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario A: Beide Lerngruppen haben Wochentyp 0 (jede Woche) --> Kollision.
		final @NotNull List<UvBlockungRegel> regelnA = new ArrayList<>();
		regelnA.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regelnA.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 0L, prioritaetHoch));
		final UvAlgorithmusDynDaten dynA = gibDatenMinimal3(regelnA);
		dynA.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dynA.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dynA, 18, 2, 80, 0, 0);

		// Szenario B: Wochentyp 0 und Wochentyp 1 --> Kollision in der A-Woche.
		final @NotNull List<UvBlockungRegel> regelnB = new ArrayList<>();
		regelnB.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regelnB.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 1L, prioritaetHoch));
		final UvAlgorithmusDynDaten dynB = gibDatenMinimal3(regelnB);
		dynB.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dynB.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dynB, 18, 1, 80, 0, 0);

		// Szenario C: Wochentyp 1 und Wochentyp 2 --> keine Kollision, da verschiedene Wochen.
		final @NotNull List<UvBlockungRegel> regelnC = new ArrayList<>();
		regelnC.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 1L, prioritaetHoch));
		regelnC.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 2L, prioritaetHoch));
		final UvAlgorithmusDynDaten dynC = gibDatenMinimal3(regelnC);
		dynC.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dynC.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		checkMalus(dynC, 18, 0, 80, 0, 0);

		// Szenario D: Wochentyp 0, 1 und 2 --> 2 Kollisionen (Wochentyp 1 mit 0 und Wochentyp 2 mit 0).
		final @NotNull List<UvBlockungRegel> regelnD = new ArrayList<>();
		regelnD.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG2, 1L, 1L, 0L, prioritaetHoch));
		regelnD.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG5, 1L, 1L, 1L, prioritaetHoch));
		regelnD.add(gibRegel17LerngruppeLiegtAmWochentagStundeWochentyp(UvDatensatz3.LG8, 1L, 1L, 2L, prioritaetHoch));
		final UvAlgorithmusDynDaten dynD = gibDatenMinimal3(regelnD);
		dynD.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		dynD.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG5, UvDatensatz3.LEHRER1, false);
		dynD.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG8, UvDatensatz3.LEHRER1, false);
		checkMalus(dynD, 17, 2, 76, 0, 0);
	}



	@Test
	@DisplayName("testRegel18LehrkraftVerbotenInKlasse: Die einzige Lehrkraft ist in der Klasse verboten.")
	void testRegel18LehrkraftVerbotenInKlasse() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Klasse 4 verboten, Add wird ignoriert (Log).
		final UvBlockungRegel regel1 = gibRegel18LehrkraftVerbotenInKlasse(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");

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
	@DisplayName("testRegel18LehrkraftVerbotenInKlasse2")
	void testRegel18LehrkraftVerbotenInKlasse2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 6: Eine Lerngruppe einer anderen Klasse darf zugeordnet werden.
		final UvBlockungRegel regel6 = gibRegel18LehrkraftVerbotenInKlasse(UvDatensatz3.LEHRER1, UvDatensatz3.KLASSE_5A, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 19, 0, 92, 0, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Die Zuordnung in einer anderen Klasse muss erlaubt sein.");

		// Szenario 7: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(UvDatensatz3.LEHRER1);
		regel7.parameter.add(UvDatensatz3.KLASSE_5A);
		regel7.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel19LehrkraftVerbotenInJahrgang: Die einzige Lehrkraft ist im Jahrgang verboten.")
	void testRegel19LehrkraftVerbotenInJahrgang() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 ist in Jahrgang 5 verboten, Add wird ignoriert (Log).
		final UvBlockungRegel regel1 = gibRegel19LehrkraftVerbotenInJahrgang(5L, UvDatensatz1.JAHRGANG, prioritaetGering);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel19LehrkraftVerbotenInJahrgang(5L, UvDatensatz1.JAHRGANG, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));

		// Szenario 3: Unbekannte Lehrkraft-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel3 = gibRegel19LehrkraftVerbotenInJahrgang(999L, UvDatensatz1.JAHRGANG, prioritaetGering);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn3, 2, 0, 0, 0, 0);
		assertEquals(1, dyn3.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot eine unbekannte Lehrkraft betrifft.");
		assertTrue(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Unbekannte Jahrgangs-ID im Verbot, Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel19LehrkraftVerbotenInJahrgang(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn4, 2, 0, 0, 0, 0);
		assertEquals(1, dyn4.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot einen unbekannten Jahrgang betrifft.");
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Regel mit fehlendem Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr;
		regel5.prioritaet = prioritaetGering;
		regel5.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));
	}


	@Test
	@DisplayName("testRegel19LehrkraftVerbotenInJahrgang2")
	void testRegel19LehrkraftVerbotenInJahrgang2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 6: Eine Lerngruppe eines anderen Jahrgangs darf zugeordnet werden.
		final UvBlockungRegel regel6 = gibRegel19LehrkraftVerbotenInJahrgang(UvDatensatz3.LEHRER1, UvDatensatz3.JAHRGANG_5, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 19, 0, 92, 0, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Die Zuordnung in einem anderen Jahrgang muss erlaubt sein.");

		// Szenario 7: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(UvDatensatz3.LEHRER1);
		regel7.parameter.add(UvDatensatz3.JAHRGANG_5);
		regel7.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
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

		// Szenario 7: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel20LehrkraftHatMindestensBMalFachC(999L, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Fach-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 1, 999L, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Negativer Mindestwert wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, -1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Fehlender Parameter (nur Lehrkraft-ID) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C.nr;
		regel10.prioritaet = prioritaetHoch;
		regel10.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel20LehrkraftHatMindestensBMalFachC2")
	void testRegel20LehrkraftHatMindestensBMalFachC2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 19, 0, 92, 0, 0);
		dyn11.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER1);
		checkMalus(dyn11, 20, 1, 112, 0, 0);

		// Szenario 12: Mehrfache Unterschreitung (min=3, nur 1 Deutsch-Zuordnung) ergibt HOCH-Malus 2.
		final UvBlockungRegel regel12 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 3, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn12, 19, 2, 92, 0, 0);

		// Szenario 13: Vier Parameter statt drei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel13 = new UvBlockungRegel();
		regel13.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C.nr;
		regel13.prioritaet = prioritaetHoch;
		regel13.parameter.add(UvDatensatz3.LEHRER1);
		regel13.parameter.add(1L);
		regel13.parameter.add(UvDatensatz3.FACH_DE);
		regel13.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal3(List.of(regel13));
		assertTrue(dyn13.gibIstRegelFehlerhaft(regel13));

		// Szenario 14: Eine fixierte Zuordnung zählt für die Mindestanzahl mit.
		final UvBlockungRegel regel14 = gibRegel20LehrkraftHatMindestensBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal3(List.of(regel14));
		dyn14.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn14, 19, 0, 92, 0, 0);
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

		// Szenario 7: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel21LehrkraftHatMaximalBMalFachC(999L, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Fach-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel21LehrkraftHatMaximalBMalFachC(UvDatensatz3.LEHRER1, 1, 999L, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Negativer Maximalwert wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel21LehrkraftHatMaximalBMalFachC(UvDatensatz3.LEHRER1, -1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Fehlender Parameter (nur Lehrkraft-ID) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C.nr;
		regel10.prioritaet = prioritaetHoch;
		regel10.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel21LehrkraftHatMaximalBMalFachC2")
	void testRegel21LehrkraftHatMaximalBMalFachC2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel21LehrkraftHatMaximalBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 18, 1, 80, 0, 0);
		dyn11.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG4, UvDatensatz3.LEHRER1);
		checkMalus(dyn11, 19, 0, 92, 0, 0);

		// Szenario 12: Mehrfache Überschreitung (max=0, zwei Deutsch-Zuordnungen) ergibt HOCH-Malus 2.
		final UvBlockungRegel regel12 = gibRegel21LehrkraftHatMaximalBMalFachC(UvDatensatz3.LEHRER1, 0, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn12, 18, 2, 80, 0, 0);

		// Szenario 13: Vier Parameter statt drei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel13 = new UvBlockungRegel();
		regel13.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C.nr;
		regel13.prioritaet = prioritaetHoch;
		regel13.parameter.add(UvDatensatz3.LEHRER1);
		regel13.parameter.add(1L);
		regel13.parameter.add(UvDatensatz3.FACH_DE);
		regel13.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal3(List.of(regel13));
		assertTrue(dyn13.gibIstRegelFehlerhaft(regel13));

		// Szenario 14: Fixierte Zuordnungen zählen für das Maximum mit.
		final UvBlockungRegel regel14 = gibRegel21LehrkraftHatMaximalBMalFachC(UvDatensatz3.LEHRER1, 1, UvDatensatz3.FACH_DE, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal3(List.of(regel14));
		dyn14.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn14, 19, 0, 92, 0, 0);
		dyn14.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn14, 18, 1, 80, 0, 0);
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
	@DisplayName("testRegel22LehrkraftHatMindestensNKorrekturen2")
	void testRegel22LehrkraftHatMindestensNKorrekturen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 10: Add und Del gleichen sich aus.
		final UvBlockungRegel regel10 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn10, 19, 0, 92, 0, 0);
		dyn10.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER1);
		checkMalus(dyn10, 20, 1, 112, 0, 0);

		// Szenario 11: Mehrfache Unterschreitung (min=3, nur 1 Korrektur) ergibt HOCH-Malus 2.
		final UvBlockungRegel regel11 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 3, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 19, 2, 92, 0, 0);

		// Szenario 12: Vier Parameter statt drei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN.nr;
		regel12.prioritaet = prioritaetHoch;
		regel12.parameter.add(UvDatensatz3.LEHRER1);
		regel12.parameter.add(1L);
		regel12.parameter.add(0L);
		regel12.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));

		// Szenario 13: Eine fixierte Zuordnung erfüllt die Mindestanzahl.
		final UvBlockungRegel regel13 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal3(List.of(regel13));
		dyn13.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn13, 19, 0, 92, 0, 0);

		// Szenario 14: Eine Lerngruppe ohne Korrekturbelastung (Mathematik) erfüllt die Regel nicht.
		final UvBlockungRegel regel14 = gibRegel22LehrkraftHatMindestensNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn14 = gibDatenMinimal3(List.of(regel14));
		dyn14.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn14, 19, 1, 92, 0, 0);
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
	@DisplayName("testRegel23LehrkraftHatMaximalNKorrekturen2")
	void testRegel23LehrkraftHatMaximalNKorrekturen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 10: Add und Del gleichen sich aus.
		final UvBlockungRegel regel10 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn10, 18, 1, 80, 0, 0);
		dyn10.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG4, UvDatensatz3.LEHRER1);
		checkMalus(dyn10, 19, 0, 92, 0, 0);

		// Szenario 11: Mehrfache Überschreitung (max=0, zwei Korrekturen) ergibt HOCH-Malus 2.
		final UvBlockungRegel regel11 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 0, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 18, 2, 80, 0, 0);

		// Szenario 12: Vier Parameter statt drei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel12 = new UvBlockungRegel();
		regel12.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN.nr;
		regel12.prioritaet = prioritaetHoch;
		regel12.parameter.add(UvDatensatz3.LEHRER1);
		regel12.parameter.add(1L);
		regel12.parameter.add(0L);
		regel12.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		assertTrue(dyn12.gibIstRegelFehlerhaft(regel12));

		// Szenario 13: Fixierte Zuordnungen zählen für das Maximum mit.
		final UvBlockungRegel regel13 = gibRegel23LehrkraftHatMaximalNKorrekturen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn13 = gibDatenMinimal3(List.of(regel13));
		dyn13.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn13, 19, 0, 92, 0, 0);
		dyn13.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG4, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn13, 18, 1, 80, 0, 0);
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
		assertDoesNotThrow(() -> dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false));
		checkMalus(dyn1, 20, 0, 112, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");

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
		assertEquals(UvDatensatz3.LG1, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(UvDatensatz3.LEHRER1, dyn2.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=51 erwartet.");

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
		assertEquals(1, dyn5.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da das Verbot einen unbekannten Jahrgang betrifft.");

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
		assertDoesNotThrow(() -> dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false));
		assertDoesNotThrow(() -> dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertEquals(0, dyn6.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");

		// Szenario 7: Lehrkraft ist in einer Lerngruppe fixiert und darf zugleich das Fach
		// in dem zugehörigen Jahrgang nicht unterrichten. Das ist ein Widerspruch.
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
		final UvAlgorithmusDynDaten dyn7 = assertDoesNotThrow(() -> gibDatenMinimal3(regeln7));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
		assertEquals(0, dyn7.gibAktuelleZuordnung().size(), "Die Fixierung wird ignoriert (Log).");

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
		final UvAlgorithmusDynDaten dyn8 = assertDoesNotThrow(() -> gibDatenMinimal3(regeln8));
		checkMalus(dyn8, 20, 0, 112, 0, 0);
		assertEquals(0, dyn8.gibAktuelleZuordnung().size(), "Die Fixierung wird ignoriert (Log).");

		// Szenario 9: Zu wenige Parameter (Lehrkraft und Fach, aber kein Jahrgang) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN.nr;
		regel9.prioritaet = prioritaetHoch;
		regel9.parameter.add(UvDatensatz3.LEHRER1);
		regel9.parameter.add(UvDatensatz3.FACH_DE);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Doppelte Jahrgangs-ID in der Liste wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5, UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen3")
	void testRegel24LehrkraftDarfNichtFachUnterrichtenInJahrgaengen3() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 11: Eine Deutsch-Lerngruppe in einem nicht verbotenen Jahrgang darf zugeordnet werden.
		final UvBlockungRegel regel11 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		assertFalse(dyn11.gibIstRegelFehlerhaft(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 19, 0, 92, 0, 0);
		assertEquals(1, dyn11.gibAktuelleZuordnung().size(), "Die Zuordnung im Jahrgang 6 muss erlaubt sein.");

		// Szenario 12: Eine Mathematik-Lerngruppe im verbotenen Jahrgang darf zugeordnet werden.
		final UvBlockungRegel regel12 = gibRegel24LehrkraftADarfFachBNichtUnterrichtenInJahrgaengen(
				UvDatensatz3.LEHRER1,
				UvDatensatz3.FACH_DE,
				List.of(UvDatensatz3.JAHRGANG_5),
				prioritaetHoch
		);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		assertFalse(dyn12.gibIstRegelFehlerhaft(regel12));
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn12, 19, 0, 92, 0, 0);
		assertEquals(1, dyn12.gibAktuelleZuordnung().size(), "Die Zuordnung im Fach Mathematik muss erlaubt sein.");
	}


	@Test
	@DisplayName("testRegel25LehrkraftDarfFachNichtUnterrichten")
	void testRegel25LehrkraftDarfFachNichtUnterrichten() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Lehrkraft 5 darf Fach 3 nicht unterrichten, Add wird abgelehnt (DeveloperNotificationException).
		final UvBlockungRegel regel1 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(5L, UvDatensatz1.ID_FACH, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertDoesNotThrow(() -> dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false));
		checkMalus(dyn1, 3, 0, 4, 0, 0);
		assertEquals(0, dyn1.gibAktuelleZuordnung().size(), "Es darf keine Zuordnung entstehen.");

		// Szenario 2: Regel deaktiviert, das Verbot wirkt nicht und Add funktioniert normal.
		final UvBlockungRegel regel2 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(5L, UvDatensatz1.ID_FACH, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 0, 4, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 0, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Zuordnung muss erfolgen, da die Regel deaktiviert ist.");
		assertEquals(1L, dyn2.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn2.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");
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
	@DisplayName("testRegel25LehrkraftDarfFachNichtUnterrichten2")
	void testRegel25LehrkraftDarfFachNichtUnterrichten2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 6: Eine Lerngruppe mit einem anderen Fach darf zugeordnet werden.
		final UvBlockungRegel regel6 = gibRegel25LehrkraftADarfFachBNichtUnterrichten(UvDatensatz3.LEHRER1, UvDatensatz3.FACH_DE, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertFalse(dyn6.gibIstRegelFehlerhaft(regel6));
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 19, 0, 92, 0, 0);
		assertEquals(1, dyn6.gibAktuelleZuordnung().size(), "Die Zuordnung im Fach Mathematik muss erlaubt sein.");

		// Szenario 7: Drei Parameter statt zwei werden als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(UvDatensatz3.LEHRER1);
		regel7.parameter.add(UvDatensatz3.FACH_DE);
		regel7.parameter.add(0L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
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
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-Malus weniger.
		checkMalus(dyn2, 19, 0, 92, 0, 0);
		dyn2.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-Malus weniger.
		checkMalus(dyn2, 18, 1, 80, 0, 0);
		assertEquals(2, dyn2.gibAktuelleZuordnung().size());

		// Szenario 3: Maximal 2 Jahrgänge sind erlaubt. Dieselben beiden Zuordnungen sind dann regelkonform.
		final UvBlockungRegel regel3 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal3(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		checkMalus(dyn3, 20, 0, 112, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-Malus weniger.
		checkMalus(dyn3, 19, 0, 92, 0, 0);
		dyn3.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-Malus weniger.
		checkMalus(dyn3, 18, 0, 80, 0, 0);
		assertEquals(2, dyn3.gibAktuelleZuordnung().size());

		// Szenario 4: Regel deaktiviert. Die gleiche Verletzung erzeugt dann keinen HOCH-Malus.
		final UvBlockungRegel regel4 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(51L, 1, prioritaetDeaktiviert);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal3(List.of(regel4));
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));
		checkMalus(dyn4, 20, 0, 112, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(1L, 51L, false); // Lehrkraft-Soll --> 6² zu 4² --> 20 MITTEL-Malus weniger.
		checkMalus(dyn4, 19, 0, 92, 0, 0);
		dyn4.setzeLerngruppeLehrkraftAdd(7L, 51L, false); // Lehrkraft-Soll --> 4² zu 2² --> 12 MITTEL-Malus weniger.
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

		// Szenario 8: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
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
	@DisplayName("testRegel26LehrkraftAIstInMaximalBJahrgaengen2")
	void testRegel26LehrkraftAIstInMaximalBJahrgaengen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 9: Add und Del gleichen sich aus.
		final UvBlockungRegel regel9 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		dyn9.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn9, 19, 0, 92, 0, 0);
		dyn9.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn9, 18, 1, 80, 0, 0);
		dyn9.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG7, UvDatensatz3.LEHRER1);
		checkMalus(dyn9, 19, 0, 92, 0, 0);

		// Szenario 10: Mehrfache Überschreitung (max=0, zwei Jahrgänge) ergibt HOCH-Malus 2.
		final UvBlockungRegel regel10 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(UvDatensatz3.LEHRER1, 0, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn10, 18, 2, 80, 0, 0);

		// Szenario 11: Fixierte Zuordnungen zählen für das Maximum mit.
		final UvBlockungRegel regel11 = gibRegel26LehrkraftAIstInMaximalBJahrgaengen(UvDatensatz3.LEHRER1, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn11, 19, 0, 92, 0, 0);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG7, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn11, 18, 1, 80, 0, 0);
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

		// Szenario 8: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
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

		// Szenario 3: Soll=0 und es wird zugewiesen. Malus Leitung 1 + Malus Leitung 2 + Malus Lerngruppenanzahl.
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

		// Szenario 7: Unbekannte Lerngruppe-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel28LerngruppeBenoetigtBLehrkraefte(999L, 1L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE.nr;
		regel8.prioritaet = prioritaetSehrHoch;
		regel8.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE.nr;
		regel9.prioritaet = prioritaetSehrHoch;
		regel9.parameter.add(1L);
		regel9.parameter.add(1L);
		regel9.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel28LerngruppeBenoetigtBLehrkraefte2")
	void testRegel28LerngruppeBenoetigtBLehrkraefte2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 10: Add und Del gleichen sich aus.
		final UvBlockungRegel regel10 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 2L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1(List.of(regel10));
		dyn10.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn10, 3, 0, 0, 0, 0);
		dyn10.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn10, 4, 0, 4, 0, 0);

		// Szenario 11: Eine fixierte Zuordnung zählt für das Soll mit.
		final UvBlockungRegel regel11 = gibRegel28LerngruppeBenoetigtBLehrkraefte(1L, 2L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(1L, 5L, true);
		checkMalus(dyn11, 3, 0, 0, 0, 0);
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

		// Szenario 9: Unbekannte Lerngruppe-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, 999L);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Doppelte Lerngruppen-ID (nicht benachbart) wird als fehlerhaft erkannt.
		final UvBlockungRegel regel10 = gibRegel29LerngruppenHabenSelbeLehrkraft(
				prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2, UvDatensatz3.LG1);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel29LerngruppenHabenSelbeLehrkraft2")
	void testRegel29LerngruppenHabenSelbeLehrkraft2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER2, false);
		checkMalus(dyn11, 18, 1, 72, 0, 0);
		dyn11.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG2, UvDatensatz3.LEHRER2);
		checkMalus(dyn11, 19, 0, 92, 0, 0);

		// Szenario 12: Fixierte Zuordnungen erfüllen die Regel.
		final UvBlockungRegel regel12 = gibRegel29LerngruppenHabenSelbeLehrkraft(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn12, 18, 0, 80, 0, 0);
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

		// Szenario 8: Unbekannte Lerngruppe-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, 999L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Doppelte Lerngruppen-ID wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG1);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));

		// Szenario 10: Null-Parameter wird als fehlerhaft erkannt (statt einer NullPointerException).
		final UvBlockungRegel regel10 = new UvBlockungRegel();
		regel10.typ = UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE.nr;
		regel10.prioritaet = prioritaetHoch;
		regel10.parameter.add(UvDatensatz3.LG1);
		regel10.parameter.add(null);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertTrue(dyn10.gibIstRegelFehlerhaft(regel10));
	}


	@Test
	@DisplayName("testRegel30LerngruppenHabenVerschiedeneLehrkraefte2")
	void testRegel30LerngruppenHabenVerschiedeneLehrkraefte2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 11: Add und Del gleichen sich aus.
		final UvBlockungRegel regel11 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 18, 1, 80, 0, 0);
		dyn11.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG2, UvDatensatz3.LEHRER1);
		checkMalus(dyn11, 19, 0, 92, 0, 0);

		// Szenario 12: Fixierte Zuordnungen verletzen die Regel weiterhin.
		final UvBlockungRegel regel12 = gibRegel30LerngruppenVerschiedeneLehrkraefte(prioritaetHoch, UvDatensatz3.LG1, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn12 = gibDatenMinimal3(List.of(regel12));
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		dyn12.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn12, 18, 1, 80, 0, 0);
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

		// Szenario 8: Doppelte Lerngruppe innerhalb einer Regel wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 =
				gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, UvDatensatz3.LG2, UvDatensatz3.LG5, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
		checkMalus(dyn8, 20, 0, 112, 0, 0);

		// Szenario 9: Zwei Regeln 31 verwenden dieselbe Lerngruppe -> die zweite Regel ist fehlerhaft.
		final UvBlockungRegel regel9a = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(1L, prioritaetHoch, UvDatensatz3.LG2);
		final UvBlockungRegel regel9b = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(2L, prioritaetHoch, UvDatensatz3.LG5, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9a, regel9b));
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9a));
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9b));
		checkMalus(dyn9, 20, 0, 112, 0, 0);
	}


	@Test
	@DisplayName("testRegel31LerngruppenGemeinsamKuerzenAufAStunden2")
	void testRegel31LerngruppenGemeinsamKuerzenAufAStunden2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 10: Wert oberhalb der Ist-Stunden erhöht die Wochenstunden der Lerngruppe.
		final UvBlockungRegel regel10 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(3L, prioritaetHoch, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal3(List.of(regel10));
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10));
		checkMalus(dyn10, 20, 0, 112, 0, 0);
		dyn10.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn10, 19, 0, 85, 0, 0);

		// Szenario 11: Wert gleich der Ist-Stunden lässt die Wochenstunden unverändert.
		final UvBlockungRegel regel11 = gibRegel31LerngruppenGemeinsamKuerzenAufAStunden(2L, prioritaetHoch, UvDatensatz3.LG2);
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal3(List.of(regel11));
		assertFalse(dyn11.gibIstRegelFehlerhaft(regel11));
		dyn11.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn11, 19, 0, 92, 0, 0);
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

		// Szenario 3: Fixierung kann nicht entfernt werden (Log), Zuordnung und Malus bleiben erhalten.
		final UvBlockungRegel regel3 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertDoesNotThrow(() -> dyn3.setzeKlassenLeitung1Del(4L, 5L));
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss erhalten bleiben.");

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
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Klassen-ID.");
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetSehrHoch;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel32LehrkraftFixiertAlsLeitung1_2")
	void testRegel32LehrkraftFixiertAlsLeitung1_2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 8: Ist die Lehrkraft als Leitung 1 fixiert, wird ein Leitung-2-Add derselben Klasse ignoriert (Log).
		final UvBlockungRegel regel8 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
		assertDoesNotThrow(() -> dyn8.setzeKlassenLeitung2Add(4L, 5L, false));
		checkMalus(dyn8, 2, 0, 4, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Es darf nur die fixierte Leitung 1 existieren.");

		// Szenario 9: Eine Fixierung in einer anderen Klasse ist unabhängig möglich.
		final UvBlockungRegel regel9 = gibRegel32LehrkraftInKlasseFixiertAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		dyn9.setzeKlassenLeitung1Add(7L, 5L, false);
		checkMalus(dyn9, 4, 0, 8, 0, 0);
		assertEquals(2, dyn9.gibAktuelleKlassenleitungen().size(), "Beide Klassenleitungen müssen vorhanden sein.");
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

		// Szenario 3: Fixierung kann nicht entfernt werden (Log), Zuordnung und Malus bleiben erhalten.
		final UvBlockungRegel regel3 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertDoesNotThrow(() -> dyn3.setzeKlassenLeitung2Del(4L, 5L));
		checkMalus(dyn3, 2, 0, 4, 0, 0);
		assertEquals(1, dyn3.gibAktuelleKlassenleitungen().size(), "Fixierte Zuordnung muss erhalten bleiben.");

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
		assertEquals(0, dyn5.gibAktuelleKlassenleitungen().size(), "Keine Zuordnung bei ungültiger Klassen-ID.");
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetSehrHoch;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel33LehrkraftFixiertAlsLeitung2_2")
	void testRegel33LehrkraftFixiertAlsLeitung2_2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 8: Ist die Lehrkraft als Leitung 2 fixiert, wird ein Leitung-1-Add derselben Klasse ignoriert (Log).
		final UvBlockungRegel regel8 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
		assertDoesNotThrow(() -> dyn8.setzeKlassenLeitung1Add(4L, 5L, false));
		checkMalus(dyn8, 2, 0, 4, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Es darf nur die fixierte Leitung 2 existieren.");

		// Szenario 9: Eine Fixierung in einer anderen Klasse ist unabhängig möglich.
		final UvBlockungRegel regel9 = gibRegel33LehrkraftInKlasseFixiertAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal2(List.of(regel9));
		dyn9.setzeKlassenLeitung2Add(7L, 5L, false);
		checkMalus(dyn9, 4, 0, 8, 0, 0);
		assertEquals(2, dyn9.gibAktuelleKlassenleitungen().size(), "Beide stellv. Klassenleitungen müssen vorhanden sein.");
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

		// Szenario 3: Add wird ignoriert (Log), da es "VERBOTEN" ist - Malus und Zustand bleiben unverändert.
		final UvBlockungRegel regel3 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertDoesNotThrow(() -> dyn3.setzeKlassenLeitung1Add(4L, 5L, false));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		assertEquals(0, dyn3.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung entstehen.");

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel34LehrkraftVerbotenAlsLeitung1(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetSehrHoch;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel34LehrkraftVerbotenAlsLeitung1_2")
	void testRegel34LehrkraftVerbotenAlsLeitung1_2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 8: Eine Klassenleitung in einer anderen Klasse ist weiterhin möglich.
		final UvBlockungRegel regel8 = gibRegel34LehrkraftVerbotenAlsLeitung1(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
		assertDoesNotThrow(() -> dyn8.setzeKlassenLeitung1Add(7L, 5L, false));
		checkMalus(dyn8, 5, 0, 8, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Die Zuordnung in Klasse 7 muss erlaubt sein.");
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

		// Szenario 3: Add wird ignoriert (Log), da die einzige Lehrkraft verboten ist - Malus und Zustand bleiben unverändert.
		final UvBlockungRegel regel3 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn3 = gibDatenMinimal1(List.of(regel3));
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));
		assertDoesNotThrow(() -> dyn3.setzeKlassenLeitung2Add(4L, 5L, false));
		checkMalus(dyn3, 3, 0, 4, 0, 0);
		assertEquals(0, dyn3.gibAktuelleKlassenleitungen().size(), "Es darf keine Zuordnung entstehen.");

		// Szenario 4: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel4 = gibRegel35LehrkraftVerbotenAlsLeitung2(999L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		checkMalus(dyn4, 3, 0, 4, 0, 0);
		assertTrue(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 999L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetSehrHoch;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetSehrHoch;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));
	}


	@Test
	@DisplayName("testRegel35LehrkraftVerbotenAlsLeitung2_2")
	void testRegel35LehrkraftVerbotenAlsLeitung2_2() {
		final int prioritaetSehrHoch = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;

		// Szenario 8: Eine stellv. Klassenleitung in einer anderen Klasse ist weiterhin möglich.
		final UvBlockungRegel regel8 = gibRegel35LehrkraftVerbotenAlsLeitung2(5L, 4L, prioritaetSehrHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal2(List.of(regel8));
		assertFalse(dyn8.gibIstRegelFehlerhaft(regel8));
		assertDoesNotThrow(() -> dyn8.setzeKlassenLeitung2Add(7L, 5L, false));
		checkMalus(dyn8, 5, 0, 8, 0, 0);
		assertEquals(1, dyn8.gibAktuelleKlassenleitungen().size(), "Die Zuordnung in Klasse 7 muss erlaubt sein.");
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
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Hinzufügen und Entfernen heben sich wieder auf.
		final UvBlockungRegel regel4 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung1Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung1Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 1, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Fixierte Zuordnung erfüllt den Wunsch direkt.
		final UvBlockungRegel regel5 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn5, 2, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuordnung soll vorhanden sein.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetGering;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel8 = gibRegel36LehrkraftGerneAlsLeitung1(999L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel9 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		checkMalus(dyn9, 3, 0, 4, 0, 0);
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel36LehrkraftGerneAlsLeitung12")
	void testRegel36LehrkraftGerneAlsLeitung12() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 10: Eine Klassenleitung in einer anderen Klasse lässt den Wunsch unbeeinflusst.
		final UvBlockungRegel regel10 = gibRegel36LehrkraftGerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(List.of(regel10));
		checkMalus(dyn10, 6, 0, 8, 1, 0);
		dyn10.setzeKlassenLeitung1Add(7L, 6L, false);
		checkMalus(dyn10, 5, 0, 8, 1, 0);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10));
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
		assertEquals(4L, dyn3.gibAktuelleKlassenleitungen().get(0).idKlasse, "Klasse ID=4 erwartet.");
		assertEquals(5L, dyn3.gibAktuelleKlassenleitungen().get(0).idLehrer, "Lehrer ID=5 erwartet.");
		assertFalse(dyn3.gibIstRegelFehlerhaft(regel3));

		// Szenario 4: Hinzufügen und Entfernen heben sich wieder auf.
		final UvBlockungRegel regel4 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn4 = gibDatenMinimal1(List.of(regel4));
		dyn4.setzeKlassenLeitung2Add(4L, 5L, false);
		dyn4.setzeKlassenLeitung2Del(4L, 5L);
		checkMalus(dyn4, 3, 0, 4, 1, 0);
		assertEquals(0, dyn4.gibAktuelleKlassenleitungen().size(), "Es sollte keine Zuordnung geben.");
		assertFalse(dyn4.gibIstRegelFehlerhaft(regel4));

		// Szenario 5: Fixierte Zuordnung erfüllt den Wunsch direkt.
		final UvBlockungRegel regel5 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		dyn5.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn5, 2, 0, 4, 0, 0);
		assertEquals(1, dyn5.gibAktuelleKlassenleitungen().size(), "Die fixierte Zuordnung soll vorhanden sein.");
		assertFalse(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetGering;
		regel6.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel7.prioritaet = prioritaetGering;
		regel7.parameter.add(5L);
		regel7.parameter.add(4L);
		regel7.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel8 = gibRegel37LehrkraftGerneAlsLeitung2(999L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel9 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		checkMalus(dyn9, 3, 0, 4, 0, 0);
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel37LehrkraftGerneAlsStellvKlassenleitung2")
	void testRegel37LehrkraftGerneAlsStellvKlassenleitung2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 10: Eine stellv. Klassenleitung in einer anderen Klasse lässt den Wunsch unbeeinflusst.
		final UvBlockungRegel regel10 = gibRegel37LehrkraftGerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal2(List.of(regel10));
		checkMalus(dyn10, 6, 0, 8, 1, 0);
		dyn10.setzeKlassenLeitung2Add(7L, 6L, false);
		checkMalus(dyn10, 5, 0, 8, 1, 0);
		assertFalse(dyn10.gibIstRegelFehlerhaft(regel10));
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

		// Szenario 5: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel5.prioritaet = prioritaetGering;
		regel5.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetGering;
		regel6.parameter.add(5L);
		regel6.parameter.add(4L);
		regel6.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel7 = gibRegel38LehrkraftUngerneAlsLeitung1(999L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		checkMalus(dyn7, 3, 0, 4, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel8 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel38LehrkraftUngerneAlsLeitung1_2")
	void testRegel38LehrkraftUngerneAlsLeitung1_2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 9: Eine fixierte Zuordnung verletzt den Wunsch ebenfalls.
		final UvBlockungRegel regel9 = gibRegel38LehrkraftUngerneAlsLeitung1(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		dyn9.setzeKlassenLeitung1Add(4L, 5L, true);
		checkMalus(dyn9, 2, 0, 4, 1, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel39LehrkraftUngerneAlsLeitung2: Lehrkraft ist ungerne als stellv. Klassenlehrer.")
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

		// Szenario 5: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel5 = new UvBlockungRegel();
		regel5.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel5.prioritaet = prioritaetGering;
		regel5.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel6 = new UvBlockungRegel();
		regel6.typ = UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		regel6.prioritaet = prioritaetGering;
		regel6.parameter.add(5L);
		regel6.parameter.add(4L);
		regel6.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel7 = gibRegel39LehrkraftUngerneAlsLeitung2(999L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		checkMalus(dyn7, 3, 0, 4, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Unbekannte Klassen-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel8 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 999L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		checkMalus(dyn8, 3, 0, 4, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel39LehrkraftUngerneAlsLeitung2_2")
	void testRegel39LehrkraftUngerneAlsLeitung2_2() {
		final int prioritaetGering = UvBlockungRegelPrioritaet.GERING.nr;

		// Szenario 9: Eine fixierte Zuordnung verletzt den Wunsch ebenfalls.
		final UvBlockungRegel regel9 = gibRegel39LehrkraftUngerneAlsLeitung2(5L, 4L, prioritaetGering);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		dyn9.setzeKlassenLeitung2Add(4L, 5L, true);
		checkMalus(dyn9, 2, 0, 4, 1, 0);
		assertEquals(1, dyn9.gibAktuelleKlassenleitungen().size(), "Es sollte genau 1 Zuordnung geben.");
		assertFalse(dyn9.gibIstRegelFehlerhaft(regel9));
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
		assertEquals(1L, dyn1.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");

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

		// Szenario 5: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel40LehrkraftHatMaximalBLerngruppen(999L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Negativer Maximalwert ist ungültig.
		final UvBlockungRegel regel6 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN.nr;
		regel7.prioritaet = prioritaetHoch;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN.nr;
		regel8.prioritaet = prioritaetHoch;
		regel8.parameter.add(5L);
		regel8.parameter.add(1L);
		regel8.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 9: Add und Del gleichen sich aus (Regression: fehlender faktor-Multiplikator in Regel 40).
		final UvBlockungRegel regel9 = gibRegel40LehrkraftHatMaximalBLerngruppen(5L, 0, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		dyn9.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn9, 2, 1, 0, 0, 0);
		dyn9.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn9, 3, 0, 4, 0, 0);
	}


	@Test
	@DisplayName("testRegel41LehrkraftHatMindestensBLerngruppen")
	void testRegel41LehrkraftHatMindestensBLerngruppen() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;
		final int prioritaetDeaktiviert = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;

		// Szenario 1: Min = 1 (Regel wird direkt verletzt)
		final UvBlockungRegel regel1 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn1 = gibDatenMinimal1(List.of(regel1));
		checkMalus(dyn1, 3, 1, 4, 0, 0);
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		dyn1.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn1, 2, 0, 0, 0, 0);
		assertEquals(1, dyn1.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben.");
		assertEquals(1L, dyn1.gibAktuelleZuordnung().get(0).idLerngruppe, "Lerngruppe ID=1 erwartet.");
		assertEquals(5L, dyn1.gibAktuelleZuordnung().get(0).idLehrer, "Lehrer ID=5 erwartet.");

		// Szenario 2: Min = 2 (Regel wird direkt doppelt verletzt)
		final UvBlockungRegel regel2 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 2, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn2 = gibDatenMinimal1(List.of(regel2));
		checkMalus(dyn2, 3, 2, 4, 0, 0);
		assertFalse(dyn2.gibIstRegelFehlerhaft(regel2));
		dyn2.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn2, 2, 1, 0, 0, 0);
		assertEquals(1, dyn2.gibAktuelleZuordnung().size(), "Es sollte genau 1 Zuordnung geben und noch einen HOCH-Malus.");

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

		// Szenario 5: Unbekannte Lehrkraft-ID -> Regel wird aussortiert.
		final UvBlockungRegel regel5 = gibRegel41LehrkraftHatMindestensBLerngruppen(999L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn5 = gibDatenMinimal1(List.of(regel5));
		checkMalus(dyn5, 3, 0, 4, 0, 0);
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// Szenario 6: Negativer Mindestwert ist ungültig.
		final UvBlockungRegel regel6 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, -1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal1(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 7: Nur ein Parameter statt zwei wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = new UvBlockungRegel();
		regel7.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN.nr;
		regel7.prioritaet = prioritaetHoch;
		regel7.parameter.add(5L);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal1(List.of(regel7));
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 8: Drei Parameter statt zwei werden ebenfalls als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = new UvBlockungRegel();
		regel8.typ = UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN.nr;
		regel8.prioritaet = prioritaetHoch;
		regel8.parameter.add(5L);
		regel8.parameter.add(1L);
		regel8.parameter.add(1L);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal1(List.of(regel8));
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));
	}


	@Test
	@DisplayName("testRegel41LehrkraftHatMindestensBLerngruppen2")
	void testRegel41LehrkraftHatMindestensBLerngruppen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Szenario 9: Add und Del gleichen sich aus.
		final UvBlockungRegel regel9 = gibRegel41LehrkraftHatMindestensBLerngruppen(5L, 1, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1(List.of(regel9));
		dyn9.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn9, 2, 0, 0, 0, 0);
		dyn9.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn9, 3, 1, 4, 0, 0);
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

		// Szenario 1: Mindestens 1-mal in 2 Lerngruppen.
		UvBlockungRegel regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		UvAlgorithmusDynDaten dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 92, 0, 0);

		// Szenario 2: Mindestens 1-mal in 3 Lerngruppen.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe123, prioritaetHoch);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 1, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 0, 92, 0, 0);

		// Szenario 3: Mindestens 2-mal in 3 Lerngruppen.
		regel1 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe123, prioritaetHoch);
		dyn1 = gibDatenMinimal3(List.of(regel1));
		assertFalse(dyn1.gibIstRegelFehlerhaft(regel1));
		checkMalus(dyn1, 20, 2, 112, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 19, 1, 92, 0, 0);
		dyn1.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn1, 18, 0, 80, 0, 0);

		// Szenario 4: Mindestens 3-mal in 3 Lerngruppen.
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

		// Szenario F: Doppelte Lerngruppen-ID in der Liste -> ungültig.
		final @NotNull List<Long> gruppeDoppelt = new ArrayList<>();
		gruppeDoppelt.add(UvDatensatz3.LG1);
		gruppeDoppelt.add(UvDatensatz3.LG2);
		gruppeDoppelt.add(UvDatensatz3.LG1);
		final UvBlockungRegel regelF = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppeDoppelt, prioritaetHoch);
		final UvAlgorithmusDynDaten dynF = gibDatenMinimal3(List.of(regelF));
		assertTrue(dynF.gibIstRegelFehlerhaft(regelF), "Regel 42 mit doppelter Lerngruppen-ID muss als fehlerhaft markiert werden.");
		checkMalus(dynF, 20, 0, 112, 0, 0);

		// Szenario G: Unbekannte Lehrkraft-ID -> ungültig.
		final UvBlockungRegel regelG = gibRegel42LehrkraftMindestensBMalInLerngruppen(999L, 1, gruppe12, prioritaetHoch);
		final UvAlgorithmusDynDaten dynG = gibDatenMinimal3(List.of(regelG));
		assertTrue(dynG.gibIstRegelFehlerhaft(regelG), "Regel 42 mit unbekannter Lehrkraft-ID muss als fehlerhaft markiert werden.");
		checkMalus(dynG, 20, 0, 112, 0, 0);

		// Szenario H: Unbekannte Lerngruppen-ID in der Liste -> ungültig.
		final UvBlockungRegel regelH = gibRegel42LehrkraftMindestensBMalInLerngruppen(
				UvDatensatz3.LEHRER1, 1, List.of(UvDatensatz3.LG1, 999L), prioritaetHoch);
		final UvAlgorithmusDynDaten dynH = gibDatenMinimal3(List.of(regelH));
		assertTrue(dynH.gibIstRegelFehlerhaft(regelH), "Regel 42 mit unbekannter Lerngruppen-ID muss als fehlerhaft markiert werden.");
		checkMalus(dynH, 20, 0, 112, 0, 0);
	}


	@Test
	@DisplayName("testRegel42LehrkraftMindestensBMalInLerngruppen2")
	void testRegel42LehrkraftMindestensBMalInLerngruppen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Basis: gültige Lerngruppenmenge für Datensatz 3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		// Szenario 6: Add und Del gleichen sich aus.
		final UvBlockungRegel regel6 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 2, gruppe12, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 2, 112, 0, 0);
		dyn6.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn6, 19, 1, 92, 0, 0);
		dyn6.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG1, UvDatensatz3.LEHRER1);
		checkMalus(dyn6, 20, 2, 112, 0, 0);

		// Szenario 7: Eine fixierte Zuordnung erfüllt die Mindestanzahl.
		final UvBlockungRegel regel7 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		checkMalus(dyn7, 20, 1, 112, 0, 0);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn7, 19, 0, 92, 0, 0);

		// Szenario 8: Mehrfache Unterschreitung (min=3, nur 1 Zuordnung) wird korrekt gezählt.
		final UvBlockungRegel regel8 = gibRegel42LehrkraftMindestensBMalInLerngruppen(UvDatensatz3.LEHRER1, 3, gruppe12, prioritaetHoch);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		checkMalus(dyn8, 20, 3, 112, 0, 0);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn8, 19, 2, 92, 0, 0);
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn8, 18, 1, 80, 0, 0);
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

		// 5) Doppelte Lerngruppen-ID in der Liste.
		final @NotNull List<Long> gruppeDoppelt = new ArrayList<>();
		gruppeDoppelt.add(UvDatensatz3.LG1);
		gruppeDoppelt.add(UvDatensatz3.LG1);
		final @NotNull UvBlockungRegel regel5 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppeDoppelt, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn5 = gibDatenMinimal3(List.of(regel5));
		assertTrue(dyn5.gibIstRegelFehlerhaft(regel5));

		// 6) Unbekannte Lerngruppen-ID in der Liste.
		final @NotNull UvBlockungRegel regel6 = gibRegel43LehrkraftMaximalBMalInLerngruppen(
				UvDatensatz3.LEHRER1, 1, List.of(UvDatensatz3.LG1, 999L), prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));
	}


	@Test
	@DisplayName("testRegel43LehrkraftMaximalBMalInLerngruppen2")
	void testRegel43LehrkraftMaximalBMalInLerngruppen2() {
		final int prioritaetHoch = UvBlockungRegelPrioritaet.HOCH.nr;

		// Hilfslisten für die Lerngruppen-IDs aus UvDatensatz3.
		final @NotNull List<Long> gruppe12 = new ArrayList<>();
		gruppe12.add(UvDatensatz3.LG1);
		gruppe12.add(UvDatensatz3.LG2);

		// Szenario 7: Add und Del gleichen sich aus.
		final @NotNull UvBlockungRegel regel7 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, false);
		dyn7.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG2, UvDatensatz3.LEHRER1, false);
		checkMalus(dyn7, 18, 1, 80, 0, 0);
		dyn7.setzeLerngruppeLehrkraftDel(UvDatensatz3.LG2, UvDatensatz3.LEHRER1);
		checkMalus(dyn7, 19, 0, 92, 0, 0);

		// Szenario 8: Eine fixierte Zuordnung zählt als Vertretung mit.
		final @NotNull UvBlockungRegel regel8 = gibRegel43LehrkraftMaximalBMalInLerngruppen(UvDatensatz3.LEHRER1, 1, gruppe12, prioritaetHoch);
		final @NotNull UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		dyn8.setzeLerngruppeLehrkraftAdd(UvDatensatz3.LG1, UvDatensatz3.LEHRER1, true);
		checkMalus(dyn8, 19, 0, 92, 0, 0);
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

		// Szenario 5: Unbekannte Lehrkraft-ID wird als fehlerhaft erkannt, Standard bleibt erhalten.
		final UvBlockungRegel regel6 = gibRegel45LehrkraftSollIstAktivierung(999L, 1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn6 = gibDatenMinimal3(List.of(regel6));
		checkMalus(dyn6, 20, 0, 112, 0, 0);
		assertTrue(dyn6.gibIstRegelFehlerhaft(regel6));

		// Szenario 6: Ungültige Aktivierung -1 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel7 = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, -1, prioritaetGering);
		final UvAlgorithmusDynDaten dyn7 = gibDatenMinimal3(List.of(regel7));
		checkMalus(dyn7, 20, 0, 112, 0, 0);
		assertTrue(dyn7.gibIstRegelFehlerhaft(regel7));

		// Szenario 7: Ungültige Aktivierung 2 wird als fehlerhaft erkannt.
		final UvBlockungRegel regel8 = gibRegel45LehrkraftSollIstAktivierung(UvDatensatz3.LEHRER1, 2, prioritaetGering);
		final UvAlgorithmusDynDaten dyn8 = gibDatenMinimal3(List.of(regel8));
		checkMalus(dyn8, 20, 0, 112, 0, 0);
		assertTrue(dyn8.gibIstRegelFehlerhaft(regel8));

		// Szenario 8: Fehlender zweiter Parameter wird als fehlerhaft erkannt.
		final UvBlockungRegel regel9 = new UvBlockungRegel();
		regel9.typ = UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B.nr;
		regel9.prioritaet = prioritaetGering;
		regel9.parameter.add(UvDatensatz3.LEHRER1);
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal3(List.of(regel9));
		checkMalus(dyn9, 20, 0, 112, 0, 0);
		assertTrue(dyn9.gibIstRegelFehlerhaft(regel9));
	}


	@Test
	@DisplayName("testRegel45LehrkraftSollIstAktivierung2")
	void testRegel45LehrkraftSollIstAktivierung2() {
		// Szenario 9: Abweichung von genau 0,5 Stunden erzeugt noch keinen Malus (PRE-DEFAULT-Regel 45).
		final UvAlgorithmusDynDaten dyn9 = gibDatenMinimal1MitWochenstunden(new ArrayList<>(), 1L, 1.5);
		checkMalus(dyn9, 3, 0, 4, 0, 0);
		dyn9.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn9, 2, 0, 0, 0, 0);

		// Szenario 10: Abweichung von 0,75 Stunden erzeugt einen Malus von 1 (0,75² = 0,5625).
		final UvAlgorithmusDynDaten dyn10 = gibDatenMinimal1MitWochenstunden(new ArrayList<>(), 1L, 1.25);
		checkMalus(dyn10, 3, 0, 4, 0, 0);
		dyn10.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn10, 2, 0, 1, 0, 0);

		// Szenario 11: Add und Del gleichen sich aus.
		final UvAlgorithmusDynDaten dyn11 = gibDatenMinimal1MitWochenstunden(new ArrayList<>(), 1L, 2.0);
		dyn11.setzeLerngruppeLehrkraftAdd(1L, 5L, false);
		checkMalus(dyn11, 2, 0, 0, 0, 0);
		dyn11.setzeLerngruppeLehrkraftDel(1L, 5L);
		checkMalus(dyn11, 3, 0, 4, 0, 0);
	}


}
