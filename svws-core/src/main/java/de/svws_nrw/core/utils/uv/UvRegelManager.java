package de.svws_nrw.core.utils.uv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten einer Regel-Menge der Unterrichtsverteilung.
 *
 * <p>Besonderheiten:</p>
 * <ul>
 *   <li>Regel 31 (LERNGRUPPEN_GEMEINSAM_KUERZBAR_BIS_MINDESTWERT_A):
 *       Die referenzierten Lerngruppen-Mengen müssen disjunkt sein.
 *   </li>
 * </ul>
 *
 *
 * @author Benjamin A. Bartsch
 */
public class UvRegelManager {

	/** Minimale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Klasse. */
	public static final int KLASSENLEITUNG1_ANZAHL_MIN = 0;

	/** Maximale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Klasse. */
	public static final int KLASSENLEITUNG1_ANZAHL_MAX = 5;

	/** Minimale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Klasse. */
	public static final int KLASSENLEITUNG2_ANZAHL_MIN = 0;

	/** Maximale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Klasse. */
	public static final int KLASSENLEITUNG2_ANZAHL_MAX = 5;

	/** Minimale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Lehrkraft. */
	public static final int LEHRER_LEITUNG1_MIN = 0;

	/** Maximale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Lehrkraft. */
	public static final int LEHRER_LEITUNG1_MAX = 3;

	/** Minimale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Lehrkraft. */
	public static final int LEHRER_LEITUNG2_MIN = 0;

	/** Maximale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Lehrkraft. */
	public static final int LEHRER_LEITUNG2_MAX = 3;

	/** Die komplette Menge der Regeln. */
	private final @NotNull List<UvBlockungRegel> regelmenge;

	/** Die komplette Menge aller fehlerhaften Regeln. */
	private final @NotNull List<UvBlockungRegel> regelmengeAktiviertUndFehlerfrei;

	/** Die komplette Menge aller fehlerhaften Regeln. */
	private final @NotNull List<UvBlockungRegel> regelmengeFehlerhaft;

	/** Die komplette Menge aller deaktivierten Regeln. */
	private final @NotNull List<UvBlockungRegel> regelmengeDeaktiviert;

	/** Die nach Typ-Nummer gruppierte Regel-Menge. */
	private final @NotNull Map<Integer, List<UvBlockungRegel>> regelmengeByNr = new HashMap<>();

	/** Referenz zum {@link UvManager}.  Wird benötigt um die Regel-Konsistenz zu gewährleisten. */
	private final @NotNull UvManager uvManager;

	/** Sammelt alle Lerngruppen, die bei Regel 31 verwendet werden, da es dort keinen Schnitt geben darf. */
	private @NotNull HashSet<Long> benutzteLerngruppeBeiRegel31 = new HashSet<>();

	/** Sammelt alle Zeitslots (Lerngruppe, Wochentag, Stunde, Wochentyp), die bei Regel 17 verwendet werden, da es dort keine Doppelbelegung geben darf. */
	private @NotNull HashSet<Long> benutzteZeitslotsBeiRegel17 = new HashSet<>();

	/** (Lehrkraft-ID, Lerngruppen-ID) --> bereits einsortierte Regel der Typen 13 bis 16. */
	private final @NotNull HashMap2D<Long, Long, UvBlockungRegel> mapLerngruppeLehrkraftRegel = new HashMap2D<>();

	/** (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Leitung-1-Regel der Typen 32, 34, 36, 38. */
	private final @NotNull HashMap2D<Long, Long, UvBlockungRegel> mapKlasseLeitung1Regel = new HashMap2D<>();

	/** (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Leitung-2-Regel der Typen 33, 35, 37, 39. */
	private final @NotNull HashMap2D<Long, Long, UvBlockungRegel> mapKlasseLeitung2Regel = new HashMap2D<>();

	/** (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Fixierung als Leitung 1 (Regel 32). */
	private final @NotNull HashMap2D<Long, Long, UvBlockungRegel> mapKlasseFixiertLeitung1 = new HashMap2D<>();

	/** (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Fixierung als Leitung 2 (Regel 33). */
	private final @NotNull HashMap2D<Long, Long, UvBlockungRegel> mapKlasseFixiertLeitung2 = new HashMap2D<>();

	/** Lehrkraft-ID --> bereits einsortierte Fixierungen (Regel 14) in Lerngruppen. */
	private final @NotNull Map<Long, List<UvBlockungRegel>> mapLehrkraftFixierungenLerngruppe = new HashMap<>();

	/** Lehrkraft-ID --> bereits einsortierte Verbote (Regel 18) in Klassen. */
	private final @NotNull Map<Long, Set<Long>> mapLehrkraftVerboteneKlassen = new HashMap<>();

	/** Lehrkraft-ID --> bereits einsortierte Verbote (Regel 19) in Jahrgängen. */
	private final @NotNull Map<Long, Set<Long>> mapLehrkraftVerboteneJahrgaenge = new HashMap<>();

	/**
	 * Initialisiert den Manager mit allen Regeln.
	 *
	 * @param uvManager   Der {@link UvManager}, um die Regeln auf Konsistenz zu prüfen.
	 * @param menge       Die Menge aller {@link UvBlockungRegel}-Objekte.
	 */
	public UvRegelManager(final @NotNull UvManager uvManager, final @NotNull List<UvBlockungRegel> menge) {
		this.uvManager = uvManager;
		this.regelmenge = new ArrayList<>(menge);
		this.regelmengeAktiviertUndFehlerfrei = new ArrayList<>();
		this.regelmengeFehlerhaft = new ArrayList<>();
		this.regelmengeDeaktiviert = new ArrayList<>();

		updateAll();
	}

	/* Setzt die Konfliktindizes zurück und ordnet die Regeln für die erneute Prüfung ein. */
	private void updateAll() {
		benutzteLerngruppeBeiRegel31 = new HashSet<>();
		benutzteZeitslotsBeiRegel17 = new HashSet<>();

		// Globale Konflikt-Mappings zurücksetzen.
		mapLerngruppeLehrkraftRegel.clear();
		mapKlasseLeitung1Regel.clear();
		mapKlasseLeitung2Regel.clear();
		mapKlasseFixiertLeitung1.clear();
		mapKlasseFixiertLeitung2.clear();
		mapLehrkraftFixierungenLerngruppe.clear();
		mapLehrkraftVerboteneKlassen.clear();
		mapLehrkraftVerboteneJahrgaenge.clear();

		regelmengeAktiviertUndFehlerfrei.clear();
		regelmengeDeaktiviert.clear();
		regelmengeFehlerhaft.clear();

		sortiereRegeln();

		updateRegelmengeByTypNr();
	}

	private void sortiereRegeln() {
		for (final @NotNull UvBlockungRegel regel : regelmenge) {
			sortiereRegelEin(regel);
		}
	}

	private void sortiereRegelEin(final @NotNull UvBlockungRegel regel) {

		// Deaktivierte Regeln werden nicht geprüft und nehmen nicht am Konflikt-Check teil.
		if (regel.prioritaet == UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
			regelmengeDeaktiviert.add(regel);
			return;
		}

		// Fehlerhafte Regeln (inkl. Konflikte mit bereits einsortierten Regeln) werden automatisch entfernt.
		if (istRegelFehlerhaft(regel)) {
			regelmengeFehlerhaft.add(regel);
			return;
		}

		regelmengeAktiviertUndFehlerfrei.add(regel);
	}

	private boolean istRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		// Regeln mit null-Parametern sind immer fehlerhaft. (Schutz vor einer NullPointerException in den nachfolgenden Prüfungen.)
		if (regel.parameter.contains(null)) {
			return true;
		}

		final @NotNull UvBlockungRegelTyp typ = UvBlockungRegelTyp.ofNr(regel.typ);

		return switch (typ) {
			case UNDEFINIERT -> true;

			case KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER -> istRegelFehlerhaft01(regel);
			case KLASSE_A_BENOETIGT_B_KLASSENLEHRER -> istRegelFehlerhaft02(regel);
			case KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER -> istRegelFehlerhaft03(regel);
			case KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER -> istRegelFehlerhaft04(regel);

			case LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> istRegelFehlerhaft05(regel);
			case LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> istRegelFehlerhaft06(regel);
			case LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> istRegelFehlerhaft07(regel);
			case LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> istRegelFehlerhaft08(regel);

			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER -> istRegelFehlerhaft09(regel);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER -> istRegelFehlerhaft10(regel);
			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER -> istRegelFehlerhaft11(regel);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER -> istRegelFehlerhaft12(regel);

			case LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B -> istLerngruppenLehrkraftRegelFehlerhaft(regel);
			case LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B -> istRegelFehlerhaft14(regel);
			case LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B -> istLerngruppenLehrkraftRegelFehlerhaft(regel);
			case LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B -> istLerngruppenLehrkraftRegelFehlerhaft(regel);
			case LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D -> istRegelFehlerhaft17(regel);

			case LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B -> istRegelFehlerhaft18(regel);
			case LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B -> istRegelFehlerhaft19(regel);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C -> istRegelFehlerhaft20(regel);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C -> istRegelFehlerhaft21(regel);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN -> istRegelFehlerhaft22(regel);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN -> istRegelFehlerhaft23(regel);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN -> istRegelFehlerhaft24(regel);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN -> istRegelFehlerhaft25(regel);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE -> istRegelFehlerhaft26(regel);

			case LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE -> istRegelFehlerhaft27(regel);
			case LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE -> istRegelFehlerhaft28(regel);
			case LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT -> istRegelFehlerhaft29(regel);
			case LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE -> istRegelFehlerhaft30(regel);
			case LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN -> istRegelFehlerhaft31(regel);

			case LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft32(regel);
			case LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft33(regel);
			case LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B -> istKlassenleitungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istStellvertretungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> istKlassenleitungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istStellvertretungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> istKlassenleitungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istStellvertretungsRegelFehlerhaft(regel);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN -> istRegelFehlerhaft40(regel);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN -> istRegelFehlerhaft41(regel);

			case LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN -> istRegelFehlerhaft42(regel);
			case LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN -> istRegelFehlerhaft43(regel);

			case LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A -> istRegelFehlerhaft44(regel);
			case LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B -> istRegelFehlerhaft45(regel);

			default -> {
				Logger.global().logLn(LogLevel.ERROR, "Unbekannter Regeltyp: %d - Regel wird als fehlerhaft markiert.".formatted(regel.typ));
				yield true;
			}
		};
	}

	private static boolean istRegelFehlerhaft01(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, KLASSENLEITUNG1_ANZAHL_MIN, KLASSENLEITUNG1_ANZAHL_MAX);
	}

	private boolean istRegelFehlerhaft02(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istKlassenReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, KLASSENLEITUNG1_ANZAHL_MIN, KLASSENLEITUNG1_ANZAHL_MAX);
	}

	private static boolean istRegelFehlerhaft03(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, KLASSENLEITUNG2_ANZAHL_MIN, KLASSENLEITUNG2_ANZAHL_MAX);
	}

	private boolean istRegelFehlerhaft04(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istKlassenReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, KLASSENLEITUNG2_ANZAHL_MIN, KLASSENLEITUNG2_ANZAHL_MAX);
	}

	private static boolean istRegelFehlerhaft05(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, 0, 100);
	}

	private boolean istRegelFehlerhaft06(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, 100);
	}

	private static boolean istRegelFehlerhaft07(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, 0, 100);
	}

	private boolean istRegelFehlerhaft08(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, 100);
	}

	private static boolean istRegelFehlerhaft09(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, LEHRER_LEITUNG1_MIN, LEHRER_LEITUNG1_MAX);
	}

	private boolean istRegelFehlerhaft10(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, LEHRER_LEITUNG1_MIN, LEHRER_LEITUNG1_MAX);
	}

	private static boolean istRegelFehlerhaft11(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 1)
				|| istWertAusserhalb(regel, 0, LEHRER_LEITUNG2_MIN, LEHRER_LEITUNG2_MAX);
	}

	private boolean istRegelFehlerhaft12(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, LEHRER_LEITUNG2_MIN, LEHRER_LEITUNG2_MAX);
	}

	private boolean istLerngruppenLehrkraftRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Lerngruppe) wurde bereits eine Regel (13-16) einsortiert.
				|| (mapLerngruppeLehrkraftRegel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null);

		if (!istFehlerhaft) {
			// Mapping für bereits einsortierte Regeln aktualisieren.
			mapLerngruppeLehrkraftRegel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft14(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Lerngruppe) wurde bereits eine Regel (13-16) einsortiert.
				|| (mapLerngruppeLehrkraftRegel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null)
				// Konflikt: Die Lehrkraft ist in einer Klasse oder einem Jahrgang der Lerngruppe verboten.
				|| istLehrkraftVerbotenInLerngruppe(regel.parameter.get(0), regel.parameter.get(1));

		if (!istFehlerhaft) {
			// Mappings für bereits einsortierte Regeln aktualisieren.
			mapLerngruppeLehrkraftRegel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			MapUtils.getOrCreateArrayList(mapLehrkraftFixierungenLerngruppe, regel.parameter.get(0)).add(regel);
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft17(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 4)
				|| istLerngruppeReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, uvManager.zeitrasterGetMaxWochentag())     // Wochentag
				|| istWertAusserhalb(regel, 2, 0, uvManager.zeitrasterGetMaxStunde())        // Stunde
				|| istWertAusserhalb(regel, 3, 0, uvManager.zeitrasterGetWochentypmodell()) // Wochentyp
				// Konflikt: Der Zeitslot (Lerngruppe, Wochentag, Stunde, Wochentyp) wurde bereits von einer Regel 17 belegt.
				|| !benutzteZeitslotsBeiRegel17.add(gibZeitslotKey(regel));
	}

	private static long gibZeitslotKey(final @NotNull UvBlockungRegel regel) {
		return ((regel.parameter.get(0) * 100L + regel.parameter.get(1)) * 100L + regel.parameter.get(2)) * 100L + regel.parameter.get(3);
	}

	private boolean istRegelFehlerhaft18(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1)
				// Konflikt: Die Lehrkraft ist in einer Lerngruppe dieser Klasse bereits fixiert (Regel 14).
				|| istFixierungInKlasseVorhanden(regel.parameter.get(0), regel.parameter.get(1));

		if (!istFehlerhaft) {
			// Mapping für bereits einsortierte Regeln aktualisieren.
			MapUtils.getOrCreateHashSet(mapLehrkraftVerboteneKlassen, regel.parameter.get(0)).add(regel.parameter.get(1));
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft19(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istJahrgangReferenzFalsch(regel, 1)
				// Konflikt: Die Lehrkraft ist in einer Lerngruppe dieses Jahrgangs bereits fixiert (Regel 14).
				|| istFixierungInJahrgangVorhanden(regel.parameter.get(0), regel.parameter.get(1));

		if (!istFehlerhaft) {
			// Mapping für bereits einsortierte Regeln aktualisieren.
			MapUtils.getOrCreateHashSet(mapLehrkraftVerboteneJahrgaenge, regel.parameter.get(0)).add(regel.parameter.get(1));
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft20(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE)
				|| istFachReferenzFalsch(regel, 2);
	}

	private boolean istRegelFehlerhaft21(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE)
				|| istFachReferenzFalsch(regel, 2);
	}

	private boolean istRegelFehlerhaft22(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft23(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft24(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istFachReferenzFalsch(regel, 1)
				|| sindJahrgangReferenzenFalsch(regel, 2)
				|| hatDoppelteReferenzen(regel, 2);
	}

	private boolean istRegelFehlerhaft25(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istFachReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft26(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft27(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLehrerReferenzFalsch(regel, 1)
				|| regel.parameter.get(0).equals(regel.parameter.get(1)); // LehrerA == LehrerB
	}

	private boolean istRegelFehlerhaft28(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLerngruppeReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft29(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 2)
				|| sindLerngruppeReferenzenFalsch(regel, 0)
				|| hatDoppelteReferenzen(regel, 0);
	}

	private boolean istRegelFehlerhaft30(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 2)
				|| sindLerngruppeReferenzenFalsch(regel, 0)
				|| hatDoppelteReferenzen(regel, 0);
	}

	private boolean istRegelFehlerhaft31(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 2)
				|| istWertAusserhalb(regel, 0, 0, Long.MAX_VALUE)
				|| sindLerngruppeReferenzenFalsch(regel, 1)
				|| istRegelFehlerhaft31b(regel);
	}

	private boolean istRegelFehlerhaft31b(final @NotNull UvBlockungRegel regel) {
		for (int i = 1; i < regel.parameter.size(); i++) {
			final long idLerngruppe = regel.parameter.get(i);
			if (!benutzteLerngruppeBeiRegel31.add(idLerngruppe)) {
				return true;
			}
		}
		return false;
	}

	private boolean istRegelFehlerhaft32(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Klasse) wurde bereits eine Leitung-1-Regel (32, 34, 36, 38) einsortiert.
				|| (mapKlasseLeitung1Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null)
				// Konflikt: Die Lehrkraft ist in dieser Klasse bereits als Leitung 2 fixiert (Regel 33).
				|| (mapKlasseFixiertLeitung2.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null);

		if (!istFehlerhaft) {
			// Mappings für bereits einsortierte Regeln aktualisieren.
			mapKlasseLeitung1Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			mapKlasseFixiertLeitung1.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft33(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Klasse) wurde bereits eine Leitung-2-Regel (33, 35, 37, 39) einsortiert.
				|| (mapKlasseLeitung2Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null)
				// Konflikt: Die Lehrkraft ist in dieser Klasse bereits als Leitung 1 fixiert (Regel 32).
				|| (mapKlasseFixiertLeitung1.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null);

		if (!istFehlerhaft) {
			// Mappings für bereits einsortierte Regeln aktualisieren.
			mapKlasseLeitung2Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			mapKlasseFixiertLeitung2.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}

		return istFehlerhaft;
	}

	private boolean istKlassenleitungsRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Klasse) wurde bereits eine Leitung-1-Regel (32, 34, 36, 38) einsortiert.
				|| (mapKlasseLeitung1Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null);

		if (!istFehlerhaft) {
			// Mapping für bereits einsortierte Regeln aktualisieren.
			mapKlasseLeitung1Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}

		return istFehlerhaft;
	}

	private boolean istStellvertretungsRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		final boolean istFehlerhaft = istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1)
				// Konflikt: Zu dieser (Lehrkraft, Klasse) wurde bereits eine Leitung-2-Regel (33, 35, 37, 39) einsortiert.
				|| (mapKlasseLeitung2Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) != null);

		if (!istFehlerhaft) {
			// Mapping für bereits einsortierte Regeln aktualisieren.
			mapKlasseLeitung2Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}

		return istFehlerhaft;
	}

	private boolean istRegelFehlerhaft40(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft41(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE);
	}

	private boolean istRegelFehlerhaft42(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 1, Long.MAX_VALUE)
				|| sindLerngruppeReferenzenFalsch(regel, 2)
				|| hatDoppelteReferenzen(regel, 2);
	}

	private boolean istRegelFehlerhaft43(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE)
				|| sindLerngruppeReferenzenFalsch(regel, 2)
				|| hatDoppelteReferenzen(regel, 2);
	}

	private static boolean istRegelFehlerhaft44(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 1)
				|| istWertAusserhalb(regel, 0, 0, 1); // 0...1
	}

	private boolean istRegelFehlerhaft45(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, 1); // 0...1
	}

	private static boolean istParameterAnzahlFalsch(final @NotNull UvBlockungRegel regel, final int erwarteteAnzahl) {
		return regel.parameter.size() != erwarteteAnzahl;
	}

	private static boolean hatZuWenigeParameter(final @NotNull UvBlockungRegel regel, final int minAnzahl) {
		return regel.parameter.size() < minAnzahl;
	}

	private static boolean istWertAusserhalb(final @NotNull UvBlockungRegel regel, final int parameterIndex, final long min, final long max) {
		final long wert = regel.parameter.get(parameterIndex);
		return (wert < min) || (wert > max);
	}

	private boolean istKlassenReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.klasseGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}

	private boolean istLehrerReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.lehrerGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}

	private boolean istLerngruppeReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.lerngruppeGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}

	private boolean sindLerngruppeReferenzenFalsch(final @NotNull UvBlockungRegel regel, final int startIndex) {
		for (int i = startIndex; i < regel.parameter.size(); i++) {
			if (istLerngruppeReferenzFalsch(regel, i)) {
				return true;
			}
		}
		return false;
	}

	private boolean istJahrgangReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.jahrgangsdatenGetById(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}

	private boolean sindJahrgangReferenzenFalsch(final @NotNull UvBlockungRegel regel, final int startIndex) {
		for (int i = startIndex; i < regel.parameter.size(); i++) {
			if (istJahrgangReferenzFalsch(regel, i)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hatDoppelteReferenzen(final @NotNull UvBlockungRegel regel, final int startIndex) {
		final @NotNull Set<Long> bekannteReferenzen = new HashSet<>();
		for (int i = startIndex; i < regel.parameter.size(); i++) {
			if (!bekannteReferenzen.add(regel.parameter.get(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean istFachReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.fachGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}

	private @NotNull List<UvKlasse> gibKlassenDerLerngruppeOderLeer(final @NotNull UvLerngruppe lerngruppe) {
		try {
			return uvManager.klasseGetMengeByLerngruppe(lerngruppe);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			Logger.global().logLn(LogLevel.ERROR,
					"Die Klassen der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.".formatted(lerngruppe.id));
			return new ArrayList<>();
		}
	}

	private @NotNull List<JahrgangsDaten> gibJahrgaengeDerLerngruppeOderLeer(final @NotNull UvLerngruppe lerngruppe) {
		try {
			return uvManager.jahrgangsdatenGetMengeByLerngruppe(lerngruppe);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			Logger.global().logLn(LogLevel.ERROR,
					"Die Jahrgänge der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.".formatted(lerngruppe.id));
			return new ArrayList<>();
		}
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft in einer Klasse oder einem Jahrgang der Lerngruppe verboten ist.
	 * <br>Hinweis: Es werden nur die bereits einsortierten Regeln (Regel 18 und 19) berücksichtigt.
	 *
	 * @param idLehrkraft    die ID der Lehrkraft.
	 * @param idLerngruppe   die ID der Lerngruppe.
	 *
	 * @return TRUE, falls die Lehrkraft in einer Klasse oder einem Jahrgang der Lerngruppe verboten ist.
	 */
	private boolean istLehrkraftVerbotenInLerngruppe(final long idLehrkraft, final long idLerngruppe) {
		final @NotNull UvLerngruppe lerngruppe = uvManager.lerngruppeGetByIdOrException(idLerngruppe);

		final Set<Long> klassen = mapLehrkraftVerboteneKlassen.get(idLehrkraft);
		if (klassen != null) {
			for (final @NotNull UvKlasse klasse : gibKlassenDerLerngruppeOderLeer(lerngruppe)) {
				if (klassen.contains(klasse.id)) {
					return true;
				}
			}
		}

		final Set<Long> jahrgaenge = mapLehrkraftVerboteneJahrgaenge.get(idLehrkraft);
		if (jahrgaenge != null) {
			for (final @NotNull JahrgangsDaten jahrgang : gibJahrgaengeDerLerngruppeOderLeer(lerngruppe)) {
				if (jahrgaenge.contains(jahrgang.id)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft in einer Lerngruppe der Klasse bereits fixiert wurde (Regel 14).
	 *
	 * @param idLehrkraft   die ID der Lehrkraft.
	 * @param idKlasse      die ID der Klasse.
	 *
	 * @return TRUE, falls die Lehrkraft in einer Lerngruppe der Klasse bereits fixiert wurde.
	 */
	private boolean istFixierungInKlasseVorhanden(final long idLehrkraft, final long idKlasse) {
		final List<UvBlockungRegel> fixierungen = mapLehrkraftFixierungenLerngruppe.get(idLehrkraft);
		if (fixierungen == null) {
			return false;
		}
		for (final @NotNull UvBlockungRegel fixierung : fixierungen) {
			final @NotNull UvLerngruppe lerngruppe = uvManager.lerngruppeGetByIdOrException(fixierung.parameter.get(1));
			for (final @NotNull UvKlasse klasse : gibKlassenDerLerngruppeOderLeer(lerngruppe)) {
				if (klasse.id == idKlasse) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft in einer Lerngruppe des Jahrgangs bereits fixiert wurde (Regel 14).
	 *
	 * @param idLehrkraft   die ID der Lehrkraft.
	 * @param idJahrgang    die ID des Jahrgangs.
	 *
	 * @return TRUE, falls die Lehrkraft in einer Lerngruppe des Jahrgangs bereits fixiert wurde.
	 */
	private boolean istFixierungInJahrgangVorhanden(final long idLehrkraft, final long idJahrgang) {
		final List<UvBlockungRegel> fixierungen = mapLehrkraftFixierungenLerngruppe.get(idLehrkraft);
		if (fixierungen == null) {
			return false;
		}
		for (final @NotNull UvBlockungRegel fixierung : fixierungen) {
			final @NotNull UvLerngruppe lerngruppe = uvManager.lerngruppeGetByIdOrException(fixierung.parameter.get(1));
			for (final @NotNull JahrgangsDaten jahrgang : gibJahrgaengeDerLerngruppeOderLeer(lerngruppe)) {
				if (jahrgang.id == idJahrgang) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateRegelmengeByTypNr() {
		regelmengeByNr.clear();

		for (final @NotNull UvBlockungRegel regel : regelmengeAktiviertUndFehlerfrei) {
			MapUtils.getOrCreateArrayList(regelmengeByNr, regel.typ).add(regel);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvBlockungRegel}-Objekte.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller {@link UvBlockungRegel}-Objekte.
	 */
	public @NotNull List<UvBlockungRegel> regelnGetMengeAlleAsList() {
		return new ArrayList<>(regelmenge);
	}

	/**
	 * Liefert eine Liste aller fehlerhaften {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller fehlerhaften {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 */
	public @NotNull List<UvBlockungRegel> regelnGetMengeFehlerhaftAsList() {
		return new ArrayList<>(regelmengeFehlerhaft);
	}

	/**
	 * Liefert eine Liste aller deaktivierten {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller deaktivierten {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 */
	public @NotNull List<UvBlockungRegel> regelnGetMengeDeaktiviertAsList() {
		return new ArrayList<>(regelmengeDeaktiviert);
	}

	/**
	 * Liefert eine Liste aktivierten und fehlerfreien {@link UvBlockungRegel}-Objekte, also alle Regeln, die nicht automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aktivierten und fehlerfreien {@link UvBlockungRegel}-Objekte, also alle Regeln, die nicht automatisch entfernt wurden.
	 */
	public @NotNull List<UvBlockungRegel> regelnGetMengeAktiviertUndFehlerfreiAsList() {
		return new ArrayList<>(regelmengeAktiviertUndFehlerfrei);
	}

	/**
	 * Liefert eine Liste aller {@link UvBlockungRegel}-Objekte eines bestimmten Typs.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @param typ   Das {@link UvBlockungRegelTyp}-Objekt.
	 *
	 * @return eine Liste aller {@link UvBlockungRegel}-Objekte eines bestimmten Typs.
	 */
	public @NotNull List<UvBlockungRegel> regelnGetMengeByTypAsList(final @NotNull UvBlockungRegelTyp typ) {
		final @NotNull List<UvBlockungRegel> menge = MapUtils.getOrCreateArrayList(regelmengeByNr, typ.nr);
		return new ArrayList<>(menge);
	}

	/**
	 * Liefert aktive und deaktivierte Fixierungsregeln einer Lehrkraft in einer Lerngruppe.
	 *
	 * @param regeln der zu untersuchende Regelbestand
	 * @param idLerngruppe die Lerngruppe
	 * @param idLehrer die Lehrkraft
	 * @return die passenden Fixierungsregeln
	 */
	public static @NotNull List<UvBlockungRegel> lehrerFixierungsRegeln(
			final @NotNull List<UvBlockungRegel> regeln,
			final long idLerngruppe,
			final long idLehrer) {
		final @NotNull List<UvBlockungRegel> result = new ArrayList<>();
		for (final @NotNull UvBlockungRegel regel : regeln) {
			if (regel.typ == UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr && regel.parameter.size() == 2
					&& regel.parameter.get(0) == idLehrer && regel.parameter.get(1) == idLerngruppe) {
				result.add(regel);
			}
		}
		return result;
	}

	/**
	 * Liefert aktive und deaktivierte Fixierungsregeln einer Klassenleitung oder Stellvertretung.
	 *
	 * @param regeln der zu untersuchende Regelbestand
	 * @param idKlasse die Klasse
	 * @param idLehrer die Lehrkraft
	 * @return die passenden Fixierungsregeln
	 */
	public static @NotNull List<UvBlockungRegel> klassenlehrerFixierungsRegeln(
			final @NotNull List<UvBlockungRegel> regeln,
			final long idKlasse,
			final long idLehrer) {
		final @NotNull List<UvBlockungRegel> result = new ArrayList<>();
		for (final @NotNull UvBlockungRegel regel : regeln) {
			if ((regel.typ == UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr
					|| regel.typ == UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr)
					&& regel.parameter.size() == 2 && regel.parameter.get(0) == idLehrer && regel.parameter.get(1) == idKlasse) {
				result.add(regel);
			}
		}
		return result;
	}

	/**
	 * Prüft, ob für die Zuordnung eine aktive Fixierung vorliegt.
	 *
	 * @param regeln der zu untersuchende Regelbestand
	 * @param idLerngruppe die Ziel-ID
	 * @param idLehrer die Lehrkraft
	 * @return ob mindestens eine Fixierung aktiv ist
	 */
	public static boolean lehrerIstFixiert(final @NotNull List<UvBlockungRegel> regeln, final long idLerngruppe, final long idLehrer) {
		for (final @NotNull UvBlockungRegel regel : lehrerFixierungsRegeln(regeln, idLerngruppe, idLehrer)) {
			if (regel.prioritaet != UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob für die Zuordnung eine aktive Fixierung vorliegt.
	 *
	 * @param regeln der zu untersuchende Regelbestand
	 * @param idKlasse die Ziel-ID
	 * @param idLehrer die Lehrkraft
	 * @return ob mindestens eine Fixierung aktiv ist
	 */
	public static boolean klassenlehrerIstFixiert(final @NotNull List<UvBlockungRegel> regeln, final long idKlasse, final long idLehrer) {
		for (final @NotNull UvBlockungRegel regel : klassenlehrerFixierungsRegeln(regeln, idKlasse, idLehrer)) {
			if (regel.prioritaet != UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft einen Regelentwurf zusammen mit den übrigen Regeln, ohne den Regelbestand zu ändern.
	 *
	 * @param entwurf der zu prüfende Regelentwurf
	 * @return ob der Entwurf nicht als fehlerhaft einsortiert wird
	 */
	public boolean regelIstGueltig(final @NotNull UvBlockungRegel entwurf) {
		final @NotNull List<UvBlockungRegel> kandidaten = new ArrayList<>();
		for (final @NotNull UvBlockungRegel regel : regelmenge) {
			if (regel.id != entwurf.id) {
				kandidaten.add(regel);
			}
		}
		kandidaten.add(entwurf);
		final @NotNull UvRegelManager pruefung = new UvRegelManager(uvManager, kandidaten);
		for (final @NotNull UvBlockungRegel regel : pruefung.regelnGetMengeFehlerhaftAsList()) {
			if (regel.id == entwurf.id) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Erstellt einen geprüften Fixierungsentwurf für eine bestehende Lehrkraftzuordnung.
	 *
	 * @param plan der aktuelle Planungsabschnitt
	 * @param idLerngruppe die Lerngruppe
	 * @param idLehrer die Lehrkraft
	 * @return die neue oder reaktivierte Fixierungsregel
	 */
	public @NotNull UvBlockungRegel lehrerFixierungsEntwurf(final @NotNull UvPlanungsabschnitt plan, final long idLerngruppe, final long idLehrer) {
		final UvLerngruppe gruppe = uvManager.lerngruppeGetByIdOrNull(idLerngruppe);
		if (gruppe == null || gruppe.idPlanungsabschnitt != plan.id || !uvManager.lerngruppeLehrerIstZugeordnet(gruppe, idLehrer)) {
			throw new DeveloperNotificationException("Die Lehrkraft ist dieser Lerngruppe nicht zugeordnet.");
		}
		return fixierungsEntwurf(UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr, idLehrer, idLerngruppe,
				lehrerFixierungsRegeln(regelmenge, idLerngruppe, idLehrer));
	}

	/**
	 * Erstellt eine Fixierungsregel passend zur Reihenfolge der bestehenden Klassenleitung.
	 *
	 * @param plan der aktuelle Planungsabschnitt
	 * @param idKlasse die Klasse
	 * @param idLehrer die Lehrkraft
	 * @param reihenfolge die bestehende Reihenfolge der Klassenleitung
	 * @return die neue oder reaktivierte Fixierungsregel
	 */
	public @NotNull UvBlockungRegel klassenlehrerFixierungsEntwurf(
			final @NotNull UvPlanungsabschnitt plan,
			final long idKlasse,
			final long idLehrer,
			final double reihenfolge) {
		final UvKlasse klasse = uvManager.klasseGetByIdOrNull(idKlasse);
		boolean vorhanden = false;
		if (klasse != null && klasse.idPlanungsabschnitt == plan.id && reihenfolge >= 1 && reihenfolge <= Double.MAX_VALUE
				&& reihenfolge == Math.floor(reihenfolge)) {
			for (final @NotNull UvKlassenLehrer zuordnung : uvManager.klassenLehrerGetMengeByKlasse(klasse)) {
				if (zuordnung.idLehrer == idLehrer && zuordnung.reihenfolge == reihenfolge) {
					vorhanden = true;
				}
			}
		}
		if (!vorhanden) {
			throw new DeveloperNotificationException("Die Lehrkraft ist dieser Klasse in dieser Reihenfolge nicht zugeordnet.");
		}
		final int typ = reihenfolge == 1 ? UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr
				: UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		return fixierungsEntwurf(typ, idLehrer, idKlasse, klassenlehrerFixierungsRegeln(regelmenge, idKlasse, idLehrer));
	}

	/**
	 * Ermittelt sämtliche Regeländerungen beim Umschalten einer Fixierung, ohne bestehende Regeln zu verändern.
	 *
	 * @param uvManager die UV-Daten
	 * @param regelbestand die bestehenden Regeln
	 * @param plan der aktuelle Planungsabschnitt
	 * @param klasse ob eine Klassenleitung statt einer Lerngruppenlehrkraft fixiert wird
	 * @param idZiel die Ziel-ID
	 * @param idLehrer die Lehrkraft
	 * @param reihenfolge die bestehende Reihenfolge bei Klassenleitungen
	 * @return die anzulegende oder zu ändernden Regeln
	 */
	public static @NotNull List<UvBlockungRegel> lehrerFixierungUmschaltenRegeln(
			final @NotNull UvManager uvManager,
			final @NotNull List<UvBlockungRegel> regelbestand,
			final @NotNull UvPlanungsabschnitt plan,
			final boolean klasse,
			final long idZiel,
			final long idLehrer,
			final double reihenfolge) {
		final @NotNull List<UvBlockungRegel> regeln = klasse ? klassenlehrerFixierungsRegeln(regelbestand, idZiel, idLehrer) : lehrerFixierungsRegeln(regelbestand, idZiel, idLehrer);
		final @NotNull List<UvBlockungRegel> result = new ArrayList<>();
		for (final @NotNull UvBlockungRegel regel : regeln) {
			if (regel.prioritaet != UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
				final @NotNull UvBlockungRegel aenderung = kopiereRegel(regel);
				aenderung.prioritaet = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;
				result.add(aenderung);
			}
		}
		if (result.isEmpty()) {
			final @NotNull UvRegelManager pruefung = new UvRegelManager(uvManager, regelbestand);
			result.add(klasse ? pruefung.klassenlehrerFixierungsEntwurf(plan, idZiel, idLehrer, reihenfolge) : pruefung.lehrerFixierungsEntwurf(plan, idZiel, idLehrer));
		}
		return result;
	}

	private @NotNull UvBlockungRegel fixierungsEntwurf(final int typ, final long idLehrer, final long idZiel,
			final @NotNull List<UvBlockungRegel> regeln) {
		UvBlockungRegel vorlage = null;
		for (final @NotNull UvBlockungRegel regel : regeln) {
			if (regel.typ == typ && regel.prioritaet == UvBlockungRegelPrioritaet.DEAKTIVIERT.nr && vorlage == null) {
				vorlage = regel;
			}
		}
		final @NotNull UvBlockungRegel entwurf = vorlage == null ? new UvBlockungRegel() : kopiereRegel(vorlage);
		entwurf.typ = typ;
		entwurf.prioritaet = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		entwurf.parameter = new ArrayList<>();
		entwurf.parameter.add(idLehrer);
		entwurf.parameter.add(idZiel);
		if (!regelIstGueltig(entwurf)) {
			throw new DeveloperNotificationException("Die Parameter dieser Regel sind fachlich ungültig.");
		}
		return entwurf;
	}

	private static @NotNull UvBlockungRegel kopiereRegel(final @NotNull UvBlockungRegel regel) {
		final @NotNull UvBlockungRegel kopie = new UvBlockungRegel();
		kopie.id = regel.id;
		kopie.typ = regel.typ;
		kopie.prioritaet = regel.prioritaet;
		kopie.parameter = new ArrayList<>(regel.parameter);
		return kopie;
	}

}
