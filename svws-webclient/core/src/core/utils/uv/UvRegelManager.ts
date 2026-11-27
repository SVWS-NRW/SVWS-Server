import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import type { JavaSet } from '../../../java/util/JavaSet';
import { UvBlockungRegelTyp } from '../../../core/data/uv/regel/UvBlockungRegelTyp';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LogLevel } from '../../../core/logger/LogLevel';
import type { List } from '../../../java/util/List';
import { HashSet } from '../../../java/util/HashSet';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { MapUtils } from '../../../core/utils/MapUtils';
import { JavaDouble } from '../../../java/lang/JavaDouble';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvLerngruppe } from '../../../core/data/uv/UvLerngruppe';
import { JavaLong } from '../../../java/lang/JavaLong';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';
import { UvBlockungRegel } from '../../../core/data/uv/regel/UvBlockungRegel';

export class UvRegelManager extends JavaObject {

	/**
	 * Minimale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Klasse.
	 */
	public static readonly KLASSENLEITUNG1_ANZAHL_MIN: number = 0;

	/**
	 * Maximale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Klasse.
	 */
	public static readonly KLASSENLEITUNG1_ANZAHL_MAX: number = 5;

	/**
	 * Minimale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Klasse.
	 */
	public static readonly KLASSENLEITUNG2_ANZAHL_MIN: number = 0;

	/**
	 * Maximale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Klasse.
	 */
	public static readonly KLASSENLEITUNG2_ANZAHL_MAX: number = 5;

	/**
	 * Minimale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Lehrkraft.
	 */
	public static readonly LEHRER_LEITUNG1_MIN: number = 0;

	/**
	 * Maximale Anzahl an Klassenlehrer-Einsätzen (Leitung 1) pro Lehrkraft.
	 */
	public static readonly LEHRER_LEITUNG1_MAX: number = 3;

	/**
	 * Minimale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Lehrkraft.
	 */
	public static readonly LEHRER_LEITUNG2_MIN: number = 0;

	/**
	 * Maximale Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2) pro Lehrkraft.
	 */
	public static readonly LEHRER_LEITUNG2_MAX: number = 3;

	/**
	 * Die komplette Menge der Regeln.
	 */
	private readonly regelmenge: List<UvBlockungRegel>;

	/**
	 * Die komplette Menge aller fehlerhaften Regeln.
	 */
	private readonly regelmengeAktiviertUndFehlerfrei: List<UvBlockungRegel>;

	/**
	 * Die komplette Menge aller fehlerhaften Regeln.
	 */
	private readonly regelmengeFehlerhaft: List<UvBlockungRegel>;

	/**
	 * Die komplette Menge aller deaktivierten Regeln.
	 */
	private readonly regelmengeDeaktiviert: List<UvBlockungRegel>;

	/**
	 * Die nach Typ-Nummer gruppierte Regel-Menge.
	 */
	private readonly regelmengeByNr: JavaMap<number, List<UvBlockungRegel>> = new HashMap<number, List<UvBlockungRegel>>();

	/**
	 * Referenz zum {@link UvManager}.  Wird benötigt um die Regel-Konsistenz zu gewährleisten.
	 */
	private readonly uvManager: UvManager;

	/**
	 * Sammelt alle Lerngruppen, die bei Regel 31 verwendet werden, da es dort keinen Schnitt geben darf.
	 */
	private benutzteLerngruppeBeiRegel31: HashSet<number> = new HashSet<number>();

	/**
	 * Sammelt alle Zeitslots (Lerngruppe, Wochentag, Stunde, Wochentyp), die bei Regel 17 verwendet werden, da es dort keine Doppelbelegung geben darf.
	 */
	private benutzteZeitslotsBeiRegel17: HashSet<number> = new HashSet<number>();

	/**
	 * (Lehrkraft-ID, Lerngruppen-ID) --> bereits einsortierte Regel der Typen 13 bis 16.
	 */
	private readonly mapLerngruppeLehrkraftRegel: HashMap2D<number, number, UvBlockungRegel> = new HashMap2D<number, number, UvBlockungRegel>();

	/**
	 * (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Leitung-1-Regel der Typen 32, 34, 36, 38.
	 */
	private readonly mapKlasseLeitung1Regel: HashMap2D<number, number, UvBlockungRegel> = new HashMap2D<number, number, UvBlockungRegel>();

	/**
	 * (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Leitung-2-Regel der Typen 33, 35, 37, 39.
	 */
	private readonly mapKlasseLeitung2Regel: HashMap2D<number, number, UvBlockungRegel> = new HashMap2D<number, number, UvBlockungRegel>();

	/**
	 * (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Fixierung als Leitung 1 (Regel 32).
	 */
	private readonly mapKlasseFixiertLeitung1: HashMap2D<number, number, UvBlockungRegel> = new HashMap2D<number, number, UvBlockungRegel>();

	/**
	 * (Lehrkraft-ID, Klassen-ID) --> bereits einsortierte Fixierung als Leitung 2 (Regel 33).
	 */
	private readonly mapKlasseFixiertLeitung2: HashMap2D<number, number, UvBlockungRegel> = new HashMap2D<number, number, UvBlockungRegel>();

	/**
	 * Lehrkraft-ID --> bereits einsortierte Fixierungen (Regel 14) in Lerngruppen.
	 */
	private readonly mapLehrkraftFixierungenLerngruppe: JavaMap<number, List<UvBlockungRegel>> = new HashMap<number, List<UvBlockungRegel>>();

	/**
	 * Lehrkraft-ID --> bereits einsortierte Verbote (Regel 18) in Klassen.
	 */
	private readonly mapLehrkraftVerboteneKlassen: JavaMap<number, JavaSet<number>> = new HashMap<number, JavaSet<number>>();

	/**
	 * Lehrkraft-ID --> bereits einsortierte Verbote (Regel 19) in Jahrgängen.
	 */
	private readonly mapLehrkraftVerboteneJahrgaenge: JavaMap<number, JavaSet<number>> = new HashMap<number, JavaSet<number>>();


	/**
	 * Initialisiert den Manager mit allen Regeln.
	 *
	 * @param uvManager   Der {@link UvManager}, um die Regeln auf Konsistenz zu prüfen.
	 * @param menge       Die Menge aller {@link UvBlockungRegel}-Objekte.
	 */
	public constructor(uvManager: UvManager, menge: List<UvBlockungRegel>) {
		super();
		this.uvManager = uvManager;
		this.regelmenge = new ArrayList(menge);
		this.regelmengeAktiviertUndFehlerfrei = new ArrayList();
		this.regelmengeFehlerhaft = new ArrayList();
		this.regelmengeDeaktiviert = new ArrayList();
		this.updateAll();
	}

	private updateAll(): void {
		this.benutzteLerngruppeBeiRegel31 = new HashSet();
		this.benutzteZeitslotsBeiRegel17 = new HashSet();
		this.mapLerngruppeLehrkraftRegel.clear();
		this.mapKlasseLeitung1Regel.clear();
		this.mapKlasseLeitung2Regel.clear();
		this.mapKlasseFixiertLeitung1.clear();
		this.mapKlasseFixiertLeitung2.clear();
		this.mapLehrkraftFixierungenLerngruppe.clear();
		this.mapLehrkraftVerboteneKlassen.clear();
		this.mapLehrkraftVerboteneJahrgaenge.clear();
		this.regelmengeAktiviertUndFehlerfrei.clear();
		this.regelmengeDeaktiviert.clear();
		this.regelmengeFehlerhaft.clear();
		this.sortiereRegeln();
		this.updateRegelmengeByTypNr();
	}

	private sortiereRegeln(): void {
		for (const regel of this.regelmenge) {
			this.sortiereRegelEin(regel);
		}
	}

	private sortiereRegelEin(regel: UvBlockungRegel): void {
		if (regel.prioritaet === UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
			this.regelmengeDeaktiviert.add(regel);
			return;
		}
		if (this.istRegelFehlerhaft(regel)) {
			this.regelmengeFehlerhaft.add(regel);
			return;
		}
		this.regelmengeAktiviertUndFehlerfrei.add(regel);
	}

	private istRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		if (regel.parameter.contains(null)) {
			return true;
		}
		const typ: UvBlockungRegelTyp = UvBlockungRegelTyp.ofNr(regel.typ);
		let _sevar_321001983;
		const _seexpr_321001983 = (typ);
		if (_seexpr_321001983 === UvBlockungRegelTyp.UNDEFINIERT) {
			_sevar_321001983 = true;
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft01(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER) {
			_sevar_321001983 = this.istRegelFehlerhaft02(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft03(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER) {
			_sevar_321001983 = this.istRegelFehlerhaft04(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft05(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE) {
			_sevar_321001983 = this.istRegelFehlerhaft06(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft07(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE) {
			_sevar_321001983 = this.istRegelFehlerhaft08(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft09(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER) {
			_sevar_321001983 = this.istRegelFehlerhaft10(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft11(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER) {
			_sevar_321001983 = this.istRegelFehlerhaft12(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B) {
			_sevar_321001983 = this.istLerngruppenLehrkraftRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B) {
			_sevar_321001983 = this.istRegelFehlerhaft14(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B) {
			_sevar_321001983 = this.istLerngruppenLehrkraftRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B) {
			_sevar_321001983 = this.istLerngruppenLehrkraftRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D) {
			_sevar_321001983 = this.istRegelFehlerhaft17(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B) {
			_sevar_321001983 = this.istRegelFehlerhaft18(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B) {
			_sevar_321001983 = this.istRegelFehlerhaft19(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C) {
			_sevar_321001983 = this.istRegelFehlerhaft20(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C) {
			_sevar_321001983 = this.istRegelFehlerhaft21(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN) {
			_sevar_321001983 = this.istRegelFehlerhaft22(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN) {
			_sevar_321001983 = this.istRegelFehlerhaft23(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN) {
			_sevar_321001983 = this.istRegelFehlerhaft24(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN) {
			_sevar_321001983 = this.istRegelFehlerhaft25(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE) {
			_sevar_321001983 = this.istRegelFehlerhaft26(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE) {
			_sevar_321001983 = this.istRegelFehlerhaft27(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE) {
			_sevar_321001983 = this.istRegelFehlerhaft28(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT) {
			_sevar_321001983 = this.istRegelFehlerhaft29(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE) {
			_sevar_321001983 = this.istRegelFehlerhaft30(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN) {
			_sevar_321001983 = this.istRegelFehlerhaft31(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istRegelFehlerhaft32(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istRegelFehlerhaft33(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istKlassenleitungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istStellvertretungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istKlassenleitungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istStellvertretungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istKlassenleitungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_321001983 = this.istStellvertretungsRegelFehlerhaft(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN) {
			_sevar_321001983 = this.istRegelFehlerhaft40(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN) {
			_sevar_321001983 = this.istRegelFehlerhaft41(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN) {
			_sevar_321001983 = this.istRegelFehlerhaft42(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN) {
			_sevar_321001983 = this.istRegelFehlerhaft43(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A) {
			_sevar_321001983 = UvRegelManager.istRegelFehlerhaft44(regel);
		} else if (_seexpr_321001983 === UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B) {
			_sevar_321001983 = this.istRegelFehlerhaft45(regel);
		} else {
			Logger.global().logLn(LogLevel.ERROR, JavaString.format("Unbekannter Regeltyp: %d - Regel wird als fehlerhaft markiert.", regel.typ));
			_sevar_321001983 = true;
		}
		return _sevar_321001983;
	}

	private static istRegelFehlerhaft01(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX);
	}

	private istRegelFehlerhaft02(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istKlassenReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MIN, UvRegelManager.KLASSENLEITUNG1_ANZAHL_MAX);
	}

	private static istRegelFehlerhaft03(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX);
	}

	private istRegelFehlerhaft04(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istKlassenReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MIN, UvRegelManager.KLASSENLEITUNG2_ANZAHL_MAX);
	}

	private static istRegelFehlerhaft05(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, 0, 100);
	}

	private istRegelFehlerhaft06(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, 100);
	}

	private static istRegelFehlerhaft07(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, 0, 100);
	}

	private istRegelFehlerhaft08(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, 100);
	}

	private static istRegelFehlerhaft09(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, UvRegelManager.LEHRER_LEITUNG1_MIN, UvRegelManager.LEHRER_LEITUNG1_MAX);
	}

	private istRegelFehlerhaft10(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, UvRegelManager.LEHRER_LEITUNG1_MIN, UvRegelManager.LEHRER_LEITUNG1_MAX);
	}

	private static istRegelFehlerhaft11(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, UvRegelManager.LEHRER_LEITUNG2_MIN, UvRegelManager.LEHRER_LEITUNG2_MAX);
	}

	private istRegelFehlerhaft12(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, UvRegelManager.LEHRER_LEITUNG2_MIN, UvRegelManager.LEHRER_LEITUNG2_MAX);
	}

	private istLerngruppenLehrkraftRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1) || (this.mapLerngruppeLehrkraftRegel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null);
		if (!istFehlerhaft) {
			this.mapLerngruppeLehrkraftRegel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft14(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1) || (this.mapLerngruppeLehrkraftRegel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null) || this.istLehrkraftVerbotenInLerngruppe(regel.parameter.get(0), regel.parameter.get(1));
		if (!istFehlerhaft) {
			this.mapLerngruppeLehrkraftRegel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			MapUtils.getOrCreateArrayList(this.mapLehrkraftFixierungenLerngruppe, regel.parameter.get(0)).add(regel);
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft17(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 4) || this.istLerngruppeReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, this.uvManager.zeitrasterGetMaxWochentag()) || UvRegelManager.istWertAusserhalb(regel, 2, 0, this.uvManager.zeitrasterGetMaxStunde()) || UvRegelManager.istWertAusserhalb(regel, 3, 0, this.uvManager.zeitrasterGetWochentypmodell()) || !this.benutzteZeitslotsBeiRegel17.add(UvRegelManager.gibZeitslotKey(regel));
	}

	private static gibZeitslotKey(regel: UvBlockungRegel): number {
		return ((regel.parameter.get(0) * 100 + regel.parameter.get(1)) * 100 + regel.parameter.get(2)) * 100 + regel.parameter.get(3);
	}

	private istRegelFehlerhaft18(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1) || this.istFixierungInKlasseVorhanden(regel.parameter.get(0), regel.parameter.get(1));
		if (!istFehlerhaft) {
			MapUtils.getOrCreateHashSet(this.mapLehrkraftVerboteneKlassen, regel.parameter.get(0)).add(regel.parameter.get(1));
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft19(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istJahrgangReferenzFalsch(regel, 1) || this.istFixierungInJahrgangVorhanden(regel.parameter.get(0), regel.parameter.get(1));
		if (!istFehlerhaft) {
			MapUtils.getOrCreateHashSet(this.mapLehrkraftVerboteneJahrgaenge, regel.parameter.get(0)).add(regel.parameter.get(1));
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft20(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE) || this.istFachReferenzFalsch(regel, 2);
	}

	private istRegelFehlerhaft21(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE) || this.istFachReferenzFalsch(regel, 2);
	}

	private istRegelFehlerhaft22(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft23(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft24(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || this.istFachReferenzFalsch(regel, 1) || this.sindJahrgangReferenzenFalsch(regel, 2) || UvRegelManager.hatDoppelteReferenzen(regel, 2);
	}

	private istRegelFehlerhaft25(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istFachReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft26(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft27(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLehrerReferenzFalsch(regel, 1) || JavaObject.equalsTranspiler(regel.parameter.get(0), (regel.parameter.get(1)));
	}

	private istRegelFehlerhaft28(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLerngruppeReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft29(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || this.sindLerngruppeReferenzenFalsch(regel, 0) || UvRegelManager.hatDoppelteReferenzen(regel, 0);
	}

	private istRegelFehlerhaft30(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || this.sindLerngruppeReferenzenFalsch(regel, 0) || UvRegelManager.hatDoppelteReferenzen(regel, 0);
	}

	private istRegelFehlerhaft31(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || UvRegelManager.istWertAusserhalb(regel, 0, 0, JavaLong.MAX_VALUE) || this.sindLerngruppeReferenzenFalsch(regel, 1) || this.istRegelFehlerhaft31b(regel);
	}

	private istRegelFehlerhaft31b(regel: UvBlockungRegel): boolean {
		for (let i: number = 1; i < regel.parameter.size(); i++) {
			const idLerngruppe: number = regel.parameter.get(i).valueOf();
			if (!this.benutzteLerngruppeBeiRegel31.add(idLerngruppe)) {
				return true;
			}
		}
		return false;
	}

	private istRegelFehlerhaft32(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1) || (this.mapKlasseLeitung1Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null) || (this.mapKlasseFixiertLeitung2.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null);
		if (!istFehlerhaft) {
			this.mapKlasseLeitung1Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			this.mapKlasseFixiertLeitung1.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft33(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1) || (this.mapKlasseLeitung2Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null) || (this.mapKlasseFixiertLeitung1.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null);
		if (!istFehlerhaft) {
			this.mapKlasseLeitung2Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
			this.mapKlasseFixiertLeitung2.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}
		return istFehlerhaft;
	}

	private istKlassenleitungsRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1) || (this.mapKlasseLeitung1Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null);
		if (!istFehlerhaft) {
			this.mapKlasseLeitung1Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}
		return istFehlerhaft;
	}

	private istStellvertretungsRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		const istFehlerhaft: boolean = UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1) || (this.mapKlasseLeitung2Regel.getOrNull(regel.parameter.get(0), regel.parameter.get(1)) !== null);
		if (!istFehlerhaft) {
			this.mapKlasseLeitung2Regel.put(regel.parameter.get(0), regel.parameter.get(1), regel);
		}
		return istFehlerhaft;
	}

	private istRegelFehlerhaft40(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft41(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft42(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 1, JavaLong.MAX_VALUE) || this.sindLerngruppeReferenzenFalsch(regel, 2) || UvRegelManager.hatDoppelteReferenzen(regel, 2);
	}

	private istRegelFehlerhaft43(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE) || this.sindLerngruppeReferenzenFalsch(regel, 2) || UvRegelManager.hatDoppelteReferenzen(regel, 2);
	}

	private static istRegelFehlerhaft44(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 1) || UvRegelManager.istWertAusserhalb(regel, 0, 0, 1);
	}

	private istRegelFehlerhaft45(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, 1);
	}

	private static istParameterAnzahlFalsch(regel: UvBlockungRegel, erwarteteAnzahl: number): boolean {
		return regel.parameter.size() !== erwarteteAnzahl;
	}

	private static hatZuWenigeParameter(regel: UvBlockungRegel, minAnzahl: number): boolean {
		return regel.parameter.size() < minAnzahl;
	}

	private static istWertAusserhalb(regel: UvBlockungRegel, parameterIndex: number, min: number, max: number): boolean {
		const wert: number = regel.parameter.get(parameterIndex).valueOf();
		return (wert < min) || (wert > max);
	}

	private istKlassenReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.klasseGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (ex: any) {
			return true;
		}
	}

	private istLehrerReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.lehrerGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (ex: any) {
			return true;
		}
	}

	private istLerngruppeReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.lerngruppeGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (ex: any) {
			return true;
		}
	}

	private sindLerngruppeReferenzenFalsch(regel: UvBlockungRegel, startIndex: number): boolean {
		for (let i: number = startIndex; i < regel.parameter.size(); i++) {
			if (this.istLerngruppeReferenzFalsch(regel, i)) {
				return true;
			}
		}
		return false;
	}

	private istJahrgangReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.jahrgangsdatenGetById(regel.parameter.get(parameterIndex));
			return false;
		} catch (ex: any) {
			return true;
		}
	}

	private sindJahrgangReferenzenFalsch(regel: UvBlockungRegel, startIndex: number): boolean {
		for (let i: number = startIndex; i < regel.parameter.size(); i++) {
			if (this.istJahrgangReferenzFalsch(regel, i)) {
				return true;
			}
		}
		return false;
	}

	private static hatDoppelteReferenzen(regel: UvBlockungRegel, startIndex: number): boolean {
		const bekannteReferenzen: JavaSet<number> = new HashSet<number>();
		for (let i: number = startIndex; i < regel.parameter.size(); i++) {
			if (!bekannteReferenzen.add(regel.parameter.get(i))) {
				return true;
			}
		}
		return false;
	}

	private istFachReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.fachGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch (ex: any) {
			return true;
		}
	}

	private gibKlassenDerLerngruppeOderLeer(lerngruppe: UvLerngruppe): List<UvKlasse> {
		try {
			return this.uvManager.klasseGetMengeByLerngruppe(lerngruppe);
		} catch (ex: any) {
			Logger.global().logLn(LogLevel.ERROR, JavaString.format("Die Klassen der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.", lerngruppe.id));
			return new ArrayList();
		}
	}

	private gibJahrgaengeDerLerngruppeOderLeer(lerngruppe: UvLerngruppe): List<JahrgangsDaten> {
		try {
			return this.uvManager.jahrgangsdatenGetMengeByLerngruppe(lerngruppe);
		} catch (ex: any) {
			Logger.global().logLn(LogLevel.ERROR, JavaString.format("Die Jahrgänge der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.", lerngruppe.id));
			return new ArrayList();
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
	private istLehrkraftVerbotenInLerngruppe(idLehrkraft: number, idLerngruppe: number): boolean {
		const lerngruppe: UvLerngruppe = this.uvManager.lerngruppeGetByIdOrException(idLerngruppe);
		const klassen: JavaSet<number> | null = this.mapLehrkraftVerboteneKlassen.get(idLehrkraft);
		if (klassen !== null) {
			for (const klasse of this.gibKlassenDerLerngruppeOderLeer(lerngruppe)) {
				if (klassen.contains(klasse.id)) {
					return true;
				}
			}
		}
		const jahrgaenge: JavaSet<number> | null = this.mapLehrkraftVerboteneJahrgaenge.get(idLehrkraft);
		if (jahrgaenge !== null) {
			for (const jahrgang of this.gibJahrgaengeDerLerngruppeOderLeer(lerngruppe)) {
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
	private istFixierungInKlasseVorhanden(idLehrkraft: number, idKlasse: number): boolean {
		const fixierungen: List<UvBlockungRegel> | null = this.mapLehrkraftFixierungenLerngruppe.get(idLehrkraft);
		if (fixierungen === null) {
			return false;
		}
		for (const fixierung of fixierungen) {
			const lerngruppe: UvLerngruppe = this.uvManager.lerngruppeGetByIdOrException(fixierung.parameter.get(1));
			for (const klasse of this.gibKlassenDerLerngruppeOderLeer(lerngruppe)) {
				if (klasse.id === idKlasse) {
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
	private istFixierungInJahrgangVorhanden(idLehrkraft: number, idJahrgang: number): boolean {
		const fixierungen: List<UvBlockungRegel> | null = this.mapLehrkraftFixierungenLerngruppe.get(idLehrkraft);
		if (fixierungen === null) {
			return false;
		}
		for (const fixierung of fixierungen) {
			const lerngruppe: UvLerngruppe = this.uvManager.lerngruppeGetByIdOrException(fixierung.parameter.get(1));
			for (const jahrgang of this.gibJahrgaengeDerLerngruppeOderLeer(lerngruppe)) {
				if (jahrgang.id === idJahrgang) {
					return true;
				}
			}
		}
		return false;
	}

	private updateRegelmengeByTypNr(): void {
		this.regelmengeByNr.clear();
		for (const regel of this.regelmengeAktiviertUndFehlerfrei) {
			MapUtils.getOrCreateArrayList(this.regelmengeByNr, regel.typ).add(regel);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvBlockungRegel}-Objekte.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller {@link UvBlockungRegel}-Objekte.
	 */
	public regelnGetMengeAlleAsList(): List<UvBlockungRegel> {
		return new ArrayList<UvBlockungRegel>(this.regelmenge);
	}

	/**
	 * Liefert eine Liste aller fehlerhaften {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller fehlerhaften {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 */
	public regelnGetMengeFehlerhaftAsList(): List<UvBlockungRegel> {
		return new ArrayList<UvBlockungRegel>(this.regelmengeFehlerhaft);
	}

	/**
	 * Liefert eine Liste aller deaktivierten {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aller deaktivierten {@link UvBlockungRegel}-Objekte, die automatisch entfernt wurden.
	 */
	public regelnGetMengeDeaktiviertAsList(): List<UvBlockungRegel> {
		return new ArrayList<UvBlockungRegel>(this.regelmengeDeaktiviert);
	}

	/**
	 * Liefert eine Liste aktivierten und fehlerfreien {@link UvBlockungRegel}-Objekte, also alle Regeln, die nicht automatisch entfernt wurden.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @return eine Liste aktivierten und fehlerfreien {@link UvBlockungRegel}-Objekte, also alle Regeln, die nicht automatisch entfernt wurden.
	 */
	public regelnGetMengeAktiviertUndFehlerfreiAsList(): List<UvBlockungRegel> {
		return new ArrayList<UvBlockungRegel>(this.regelmengeAktiviertUndFehlerfrei);
	}

	/**
	 * Liefert eine Liste aller {@link UvBlockungRegel}-Objekte eines bestimmten Typs.
	 * <br>Die Menge wird in eine neue Liste kopiert.
	 *
	 * @param typ   Das {@link UvBlockungRegelTyp}-Objekt.
	 *
	 * @return eine Liste aller {@link UvBlockungRegel}-Objekte eines bestimmten Typs.
	 */
	public regelnGetMengeByTypAsList(typ: UvBlockungRegelTyp): List<UvBlockungRegel> {
		const menge: List<UvBlockungRegel> = MapUtils.getOrCreateArrayList(this.regelmengeByNr, typ.nr);
		return new ArrayList<UvBlockungRegel>(menge);
	}

	/**
	 * Liefert aktive und deaktivierte Fixierungsregeln einer Lehrkraft in einer Lerngruppe.
	 *
	 * @param regeln der zu untersuchende Regelbestand
	 * @param idLerngruppe die Lerngruppe
	 * @param idLehrer die Lehrkraft
	 * @return die passenden Fixierungsregeln
	 */
	public static lehrerFixierungsRegeln(regeln: List<UvBlockungRegel>, idLerngruppe: number, idLehrer: number): List<UvBlockungRegel> {
		const result: List<UvBlockungRegel> = new ArrayList<UvBlockungRegel>();
		for (const regel of regeln) {
			if (regel.typ === UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr && regel.parameter.size() === 2 && regel.parameter.get(0) === idLehrer && regel.parameter.get(1) === idLerngruppe) {
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
	public static klassenlehrerFixierungsRegeln(regeln: List<UvBlockungRegel>, idKlasse: number, idLehrer: number): List<UvBlockungRegel> {
		const result: List<UvBlockungRegel> = new ArrayList<UvBlockungRegel>();
		for (const regel of regeln) {
			if ((regel.typ === UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr || regel.typ === UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr) && regel.parameter.size() === 2 && regel.parameter.get(0) === idLehrer && regel.parameter.get(1) === idKlasse) {
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
	public static lehrerIstFixiert(regeln: List<UvBlockungRegel>, idLerngruppe: number, idLehrer: number): boolean {
		for (const regel of UvRegelManager.lehrerFixierungsRegeln(regeln, idLerngruppe, idLehrer)) {
			if (regel.prioritaet !== UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
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
	public static klassenlehrerIstFixiert(regeln: List<UvBlockungRegel>, idKlasse: number, idLehrer: number): boolean {
		for (const regel of UvRegelManager.klassenlehrerFixierungsRegeln(regeln, idKlasse, idLehrer)) {
			if (regel.prioritaet !== UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
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
	public regelIstGueltig(entwurf: UvBlockungRegel): boolean {
		const kandidaten: List<UvBlockungRegel> = new ArrayList<UvBlockungRegel>();
		for (const regel of this.regelmenge) {
			if (regel.id !== entwurf.id) {
				kandidaten.add(regel);
			}
		}
		kandidaten.add(entwurf);
		const pruefung: UvRegelManager = new UvRegelManager(this.uvManager, kandidaten);
		for (const regel of pruefung.regelnGetMengeFehlerhaftAsList()) {
			if (regel.id === entwurf.id) {
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
	public lehrerFixierungsEntwurf(plan: UvPlanungsabschnitt, idLerngruppe: number, idLehrer: number): UvBlockungRegel {
		const gruppe: UvLerngruppe | null = this.uvManager.lerngruppeGetByIdOrNull(idLerngruppe);
		if (gruppe === null || gruppe.idPlanungsabschnitt !== plan.id || !this.uvManager.lerngruppeLehrerIstZugeordnet(gruppe, idLehrer)) {
			throw new DeveloperNotificationException("Die Lehrkraft ist dieser Lerngruppe nicht zugeordnet.");
		}
		return this.fixierungsEntwurf(UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr, idLehrer, idLerngruppe, UvRegelManager.lehrerFixierungsRegeln(this.regelmenge, idLerngruppe, idLehrer));
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
	public klassenlehrerFixierungsEntwurf(plan: UvPlanungsabschnitt, idKlasse: number, idLehrer: number, reihenfolge: number): UvBlockungRegel {
		const klasse: UvKlasse | null = this.uvManager.klasseGetByIdOrNull(idKlasse);
		let vorhanden: boolean = false;
		if (klasse !== null && klasse.idPlanungsabschnitt === plan.id && reihenfolge >= 1 && reihenfolge <= JavaDouble.MAX_VALUE && reihenfolge === Math.floor(reihenfolge)) {
			for (const zuordnung of this.uvManager.klassenLehrerGetMengeByKlasse(klasse)) {
				if (zuordnung.idLehrer === idLehrer && zuordnung.reihenfolge === reihenfolge) {
					vorhanden = true;
				}
			}
		}
		if (!vorhanden) {
			throw new DeveloperNotificationException("Die Lehrkraft ist dieser Klasse in dieser Reihenfolge nicht zugeordnet.");
		}
		const typ: number = reihenfolge === 1 ? UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr : UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr;
		return this.fixierungsEntwurf(typ, idLehrer, idKlasse, UvRegelManager.klassenlehrerFixierungsRegeln(this.regelmenge, idKlasse, idLehrer));
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
	public static lehrerFixierungUmschaltenRegeln(uvManager: UvManager, regelbestand: List<UvBlockungRegel>, plan: UvPlanungsabschnitt, klasse: boolean, idZiel: number, idLehrer: number, reihenfolge: number): List<UvBlockungRegel> {
		const regeln: List<UvBlockungRegel> = klasse ? UvRegelManager.klassenlehrerFixierungsRegeln(regelbestand, idZiel, idLehrer) : UvRegelManager.lehrerFixierungsRegeln(regelbestand, idZiel, idLehrer);
		const result: List<UvBlockungRegel> = new ArrayList<UvBlockungRegel>();
		for (const regel of regeln) {
			if (regel.prioritaet !== UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
				const aenderung: UvBlockungRegel = UvRegelManager.kopiereRegel(regel);
				aenderung.prioritaet = UvBlockungRegelPrioritaet.DEAKTIVIERT.nr;
				result.add(aenderung);
			}
		}
		if (result.isEmpty()) {
			const pruefung: UvRegelManager = new UvRegelManager(uvManager, regelbestand);
			result.add(klasse ? pruefung.klassenlehrerFixierungsEntwurf(plan, idZiel, idLehrer, reihenfolge) : pruefung.lehrerFixierungsEntwurf(plan, idZiel, idLehrer));
		}
		return result;
	}

	private fixierungsEntwurf(typ: number, idLehrer: number, idZiel: number, regeln: List<UvBlockungRegel>): UvBlockungRegel {
		let vorlage: UvBlockungRegel | null = null;
		for (const regel of regeln) {
			if (regel.typ === typ && regel.prioritaet === UvBlockungRegelPrioritaet.DEAKTIVIERT.nr && vorlage === null) {
				vorlage = regel;
			}
		}
		const entwurf: UvBlockungRegel = vorlage === null ? new UvBlockungRegel() : UvRegelManager.kopiereRegel(vorlage);
		entwurf.typ = typ;
		entwurf.prioritaet = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
		entwurf.parameter = new ArrayList();
		entwurf.parameter.add(idLehrer);
		entwurf.parameter.add(idZiel);
		if (!this.regelIstGueltig(entwurf)) {
			throw new DeveloperNotificationException("Die Parameter dieser Regel sind fachlich ungültig.");
		}
		return entwurf;
	}

	private static kopiereRegel(regel: UvBlockungRegel): UvBlockungRegel {
		const kopie: UvBlockungRegel = new UvBlockungRegel();
		kopie.id = regel.id;
		kopie.typ = regel.typ;
		kopie.prioritaet = regel.prioritaet;
		kopie.parameter = new ArrayList(regel.parameter);
		return kopie;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uv.UvRegelManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uv.UvRegelManager'].includes(name);
	}

	public static readonly class = new Class<UvRegelManager>('de.svws_nrw.core.utils.uv.UvRegelManager');

}

export function cast_de_svws_nrw_core_utils_uv_UvRegelManager(obj: unknown): UvRegelManager {
	return obj as UvRegelManager;
}
