package de.svws_nrw.core.utils.uv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
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


	private void updateAll() {
		benutzteLerngruppeBeiRegel31 = new HashSet<>();

		regelmengeAktiviertUndFehlerfrei.clear();
		regelmengeDeaktiviert.clear();
		regelmengeFehlerhaft.clear();

		sortiereRegeln();

		updateRegelmengeByTypNr();

		checkAll();  // TODO keine Exception, stattdessen Regeln entfernen
	}


	private void sortiereRegeln() {
		for (final @NotNull UvBlockungRegel regel : regelmenge) {
			sortiereRegelEin(regel);
		}
	}


	private void sortiereRegelEin(final @NotNull UvBlockungRegel regel) {

		if (istRegelFehlerhaft(regel)) {
			regelmengeFehlerhaft.add(regel);
			return;
		}

		if (regel.prioritaet == UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
			regelmengeDeaktiviert.add(regel);
			return;
		}

		regelmengeAktiviertUndFehlerfrei.add(regel);
	}


	private boolean istRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
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

			case LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B -> istRegelFehlerhaft13(regel);
			case LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B -> istRegelFehlerhaft14(regel);
			case LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B -> istRegelFehlerhaft15(regel);
			case LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B -> istRegelFehlerhaft16(regel);
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
			case LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft34(regel);
			case LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft35(regel);
			case LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft36(regel);
			case LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft37(regel);
			case LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft38(regel);
			case LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> istRegelFehlerhaft39(regel);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN -> istRegelFehlerhaft40(regel);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN -> istRegelFehlerhaft41(regel);

			case LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN -> istRegelFehlerhaft42(regel);
			case LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN -> istRegelFehlerhaft43(regel);

			case LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A -> istRegelFehlerhaft44(regel);
			case LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B -> istRegelFehlerhaft45(regel);

			default -> throw new IllegalStateException("Unbekannter Regeltyp: " + regel.typ);
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

	private boolean istRegelFehlerhaft13(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft14(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft15(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft16(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istLerngruppeReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft17(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 4)
				|| istLerngruppeReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, 7)  // Wochentag
				|| istWertAusserhalb(regel, 2, 0, 32) // Stunde
				|| istWertAusserhalb(regel, 3, 0, 8); // Wochentyp
	}

	private boolean istRegelFehlerhaft18(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft19(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istJahrgangReferenzFalsch(regel, 1);
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
				|| sindJahrgangReferenzenFalsch(regel, 2);
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
				|| sindLerngruppeReferenzenFalsch(regel, 0);
	}

	private boolean istRegelFehlerhaft30(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 2)
				|| sindLerngruppeReferenzenFalsch(regel, 0);
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
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft33(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft34(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft35(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft36(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft37(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft38(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
	}

	private boolean istRegelFehlerhaft39(final @NotNull UvBlockungRegel regel) {
		return istParameterAnzahlFalsch(regel, 2)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istKlassenReferenzFalsch(regel, 1);
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
				|| sindLerngruppeReferenzenFalsch(regel, 2);
	}

	private boolean istRegelFehlerhaft43(final @NotNull UvBlockungRegel regel) {
		return hatZuWenigeParameter(regel, 3)
				|| istLehrerReferenzFalsch(regel, 0)
				|| istWertAusserhalb(regel, 1, 0, Long.MAX_VALUE)
				|| sindLerngruppeReferenzenFalsch(regel, 2);
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

	private boolean istFachReferenzFalsch(final @NotNull UvBlockungRegel regel, final int parameterIndex) {
		try {
			uvManager.fachGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			return true;
		}
	}


	private void checkAll() {
		checkLerngruppeLehrkraftKombinationEindeutig();
		checkKlasseLeitung1KombinationEindeutig();
		checkKlasseLeitung2KombinationEindeutig();
		checkKlasseLeitung1UndLeitung2NichtGleichzeitigFixiert();
	}

	private void checkLerngruppeLehrkraftKombinationEindeutig() {
		final List<UvBlockungRegelTyp> typen = List.of(
				UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B,
				UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B,
				UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B,
				UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B
		);

		// (idLehrkraft, idLerngruppe) --> erster gefundener Typ
		final HashMap2D<Long, Long, UvBlockungRegelTyp> gesehen = new HashMap2D<>();

		for (final @NotNull UvBlockungRegelTyp typ : typen) {
			for (final @NotNull UvBlockungRegel regel : regelnGetMengeByTypAsList(typ)) {
				final long idLehrkraft = regel.parameter.get(0);
				final long idLerngruppe = regel.parameter.get(1);
				final UvBlockungRegelTyp vorhandenerTyp = gesehen.getOrNull(idLehrkraft, idLerngruppe);

				if (vorhandenerTyp != null) {
					throw new DeveloperNotificationException(
							"Lehrkraft ID=%d und Lerngruppe ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!"
									.formatted(idLehrkraft, idLerngruppe, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}

				gesehen.put(idLehrkraft, idLerngruppe, typ);
			}
		}
	}


	private void checkKlasseLeitung1KombinationEindeutig() {
		final List<UvBlockungRegelTyp> typen = List.of(
				UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B,           // 32 - Fixiert
				UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B,  // 34 - Verboten
				UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B,     // 36 - Gerne
				UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B    // 38 - Ungerne
		);

		// (idLehrkraft, idKlasse) --> erster gefundener Typ
		final HashMap2D<Long, Long, UvBlockungRegelTyp> gesehen = new HashMap2D<>();

		for (final @NotNull UvBlockungRegelTyp typ : typen) {
			for (final @NotNull UvBlockungRegel regel : regelnGetMengeByTypAsList(typ)) {
				final long idLehrkraft = regel.parameter.get(0);
				final long idKlasse = regel.parameter.get(1);
				final UvBlockungRegelTyp vorhandenerTyp = gesehen.getOrNull(idLehrkraft, idKlasse);

				if (vorhandenerTyp != null) {
					throw new DeveloperNotificationException(
							"Lehrkraft ID=%d und Klasse ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!"
									.formatted(idLehrkraft, idKlasse, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}

				gesehen.put(idLehrkraft, idKlasse, typ);
			}
		}
	}


	private void checkKlasseLeitung2KombinationEindeutig() {
		final List<UvBlockungRegelTyp> typen = List.of(
				UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B,           // 33 - Fixiert
				UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B,  // 35 - Verboten
				UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B,     // 37 - Gerne
				UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B    // 39 - Ungerne
		);

		// (idLehrkraft, idKlasse) --> erster gefundener Typ
		final HashMap2D<Long, Long, UvBlockungRegelTyp> gesehen = new HashMap2D<>();

		for (final @NotNull UvBlockungRegelTyp typ : typen) {
			for (final @NotNull UvBlockungRegel regel : regelnGetMengeByTypAsList(typ)) {
				final long idLehrkraft = regel.parameter.get(0);
				final long idKlasse = regel.parameter.get(1);
				final UvBlockungRegelTyp vorhandenerTyp = gesehen.getOrNull(idLehrkraft, idKlasse);

				if (vorhandenerTyp != null) {
					throw new DeveloperNotificationException(
							"Lehrkraft ID=%d und Klasse ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!"
									.formatted(idLehrkraft, idKlasse, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}

				gesehen.put(idLehrkraft, idKlasse, typ);
			}
		}
	}


	private void checkKlasseLeitung1UndLeitung2NichtGleichzeitigFixiert() {
		// Sammle alle (idLehrkraft, idKlasse)-Paare, die als Leitung 1 fixiert sind.
		final HashMap2D<Long, Long, UvBlockungRegelTyp> gesehenLeitung1 = new HashMap2D<>();
		for (final @NotNull UvBlockungRegel regel : regelnGetMengeByTypAsList(UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B)) {
			gesehenLeitung1.put(regel.parameter.get(0), regel.parameter.get(1), UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B);
		}

		// Prüfe: Kein als Leitung 2 fixiertes Paar darf auch als Leitung 1 fixiert sein.
		for (final @NotNull UvBlockungRegel regel : regelnGetMengeByTypAsList(UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B)) {
			final long idLehrkraft = regel.parameter.get(0);
			final long idKlasse = regel.parameter.get(1);

			if (gesehenLeitung1.getOrNull(idLehrkraft, idKlasse) != null) {
				throw new DeveloperNotificationException(
						"Lehrkraft ID=%d kann nicht gleichzeitig Klassenlehrer (Regel 32) und stellv. Klassenlehrer (Regel 33) in Klasse ID=%d sein!"
								.formatted(idLehrkraft, idKlasse));
			}
		}
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
		return new ArrayList<>(regelmengeFehlerhaft);
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


}
