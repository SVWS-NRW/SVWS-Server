import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { UvBlockungRegelTyp } from '../../../core/data/uv/regel/UvBlockungRegelTyp';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { MapUtils } from '../../../core/utils/MapUtils';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';
import { UvBlockungRegel } from '../../../core/data/uv/regel/UvBlockungRegel';
import { HashSet } from '../../../java/util/HashSet';

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
		this.regelmengeAktiviertUndFehlerfrei.clear();
		this.regelmengeDeaktiviert.clear();
		this.regelmengeFehlerhaft.clear();
		this.sortiereRegeln();
		this.updateRegelmengeByTypNr();
		this.checkAll();
	}

	private sortiereRegeln(): void {
		for (const regel of this.regelmenge) {
			this.sortiereRegelEin(regel);
		}
	}

	private sortiereRegelEin(regel: UvBlockungRegel): void {
		if (this.istRegelFehlerhaft(regel)) {
			this.regelmengeFehlerhaft.add(regel);
			return;
		}
		if (regel.prioritaet === UvBlockungRegelPrioritaet.DEAKTIVIERT.nr) {
			this.regelmengeDeaktiviert.add(regel);
			return;
		}
		this.regelmengeAktiviertUndFehlerfrei.add(regel);
	}

	private istRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		const typ: UvBlockungRegelTyp = UvBlockungRegelTyp.ofNr(regel.typ);
		let _sevar_1204273150 : any;
		const _seexpr_1204273150 = (typ);
		if (_seexpr_1204273150 === UvBlockungRegelTyp.UNDEFINIERT) {
			_sevar_1204273150 = true;
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft01(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER) {
			_sevar_1204273150 = this.istRegelFehlerhaft02(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft03(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER) {
			_sevar_1204273150 = this.istRegelFehlerhaft04(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft05(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE) {
			_sevar_1204273150 = this.istRegelFehlerhaft06(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft07(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE) {
			_sevar_1204273150 = this.istRegelFehlerhaft08(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft09(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER) {
			_sevar_1204273150 = this.istRegelFehlerhaft10(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft11(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER) {
			_sevar_1204273150 = this.istRegelFehlerhaft12(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft13(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft14(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft15(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft16(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D) {
			_sevar_1204273150 = this.istRegelFehlerhaft17(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft18(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft19(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C) {
			_sevar_1204273150 = this.istRegelFehlerhaft20(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C) {
			_sevar_1204273150 = this.istRegelFehlerhaft21(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN) {
			_sevar_1204273150 = this.istRegelFehlerhaft22(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN) {
			_sevar_1204273150 = this.istRegelFehlerhaft23(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft24(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft25(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE) {
			_sevar_1204273150 = this.istRegelFehlerhaft26(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE) {
			_sevar_1204273150 = this.istRegelFehlerhaft27(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE) {
			_sevar_1204273150 = this.istRegelFehlerhaft28(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT) {
			_sevar_1204273150 = this.istRegelFehlerhaft29(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE) {
			_sevar_1204273150 = this.istRegelFehlerhaft30(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft31(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft32(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft33(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft34(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft35(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft36(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft37(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft38(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft39(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft40(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft41(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft42(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN) {
			_sevar_1204273150 = this.istRegelFehlerhaft43(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A) {
			_sevar_1204273150 = UvRegelManager.istRegelFehlerhaft44(regel);
		} else if (_seexpr_1204273150 === UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B) {
			_sevar_1204273150 = this.istRegelFehlerhaft45(regel);
		} else {
			throw new IllegalStateException("Unbekannter Regeltyp: " + regel.typ);;
		}
		return _sevar_1204273150;
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

	private istRegelFehlerhaft13(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft14(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft15(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft16(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istLerngruppeReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft17(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 4) || this.istLerngruppeReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, 7) || UvRegelManager.istWertAusserhalb(regel, 2, 0, 32) || UvRegelManager.istWertAusserhalb(regel, 3, 0, 8);
	}

	private istRegelFehlerhaft18(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft19(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istJahrgangReferenzFalsch(regel, 1);
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
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || this.istFachReferenzFalsch(regel, 1) || this.sindJahrgangReferenzenFalsch(regel, 2);
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
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || this.sindLerngruppeReferenzenFalsch(regel, 0);
	}

	private istRegelFehlerhaft30(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 2) || this.sindLerngruppeReferenzenFalsch(regel, 0);
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
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft33(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft34(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft35(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft36(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft37(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft38(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft39(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || this.istKlassenReferenzFalsch(regel, 1);
	}

	private istRegelFehlerhaft40(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft41(regel: UvBlockungRegel): boolean {
		return UvRegelManager.istParameterAnzahlFalsch(regel, 2) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE);
	}

	private istRegelFehlerhaft42(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 1, JavaLong.MAX_VALUE) || this.sindLerngruppeReferenzenFalsch(regel, 2);
	}

	private istRegelFehlerhaft43(regel: UvBlockungRegel): boolean {
		return UvRegelManager.hatZuWenigeParameter(regel, 3) || this.istLehrerReferenzFalsch(regel, 0) || UvRegelManager.istWertAusserhalb(regel, 1, 0, JavaLong.MAX_VALUE) || this.sindLerngruppeReferenzenFalsch(regel, 2);
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
		} catch(ex : any) {
			return true;
		}
	}

	private istLehrerReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.lehrerGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch(ex : any) {
			return true;
		}
	}

	private istLerngruppeReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.lerngruppeGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch(ex : any) {
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
		} catch(ex : any) {
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

	private istFachReferenzFalsch(regel: UvBlockungRegel, parameterIndex: number): boolean {
		try {
			this.uvManager.fachGetByIdOrException(regel.parameter.get(parameterIndex));
			return false;
		} catch(ex : any) {
			return true;
		}
	}

	private checkAll(): void {
		this.checkLerngruppeLehrkraftKombinationEindeutig();
		this.checkKlasseLeitung1KombinationEindeutig();
		this.checkKlasseLeitung2KombinationEindeutig();
		this.checkKlasseLeitung1UndLeitung2NichtGleichzeitigFixiert();
	}

	private checkLerngruppeLehrkraftKombinationEindeutig(): void {
		const typen: List<UvBlockungRegelTyp> | null = ArrayList.of(UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B, UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B);
		const gesehen: HashMap2D<number, number, UvBlockungRegelTyp> | null = new HashMap2D<number, number, UvBlockungRegelTyp>();
		for (const typ of typen) {
			for (const regel of this.regelnGetMengeByTypAsList(typ)) {
				const idLehrkraft: number = regel.parameter.get(0).valueOf();
				const idLerngruppe: number = regel.parameter.get(1).valueOf();
				const vorhandenerTyp: UvBlockungRegelTyp | null = gesehen.getOrNull(idLehrkraft, idLerngruppe);
				if (vorhandenerTyp !== null) {
					throw new DeveloperNotificationException(JavaString.format("Lehrkraft ID=%d und Lerngruppe ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!", idLehrkraft, idLerngruppe, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}
				gesehen.put(idLehrkraft, idLerngruppe, typ);
			}
		}
	}

	private checkKlasseLeitung1KombinationEindeutig(): void {
		const typen: List<UvBlockungRegelTyp> | null = ArrayList.of(UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B);
		const gesehen: HashMap2D<number, number, UvBlockungRegelTyp> | null = new HashMap2D<number, number, UvBlockungRegelTyp>();
		for (const typ of typen) {
			for (const regel of this.regelnGetMengeByTypAsList(typ)) {
				const idLehrkraft: number = regel.parameter.get(0).valueOf();
				const idKlasse: number = regel.parameter.get(1).valueOf();
				const vorhandenerTyp: UvBlockungRegelTyp | null = gesehen.getOrNull(idLehrkraft, idKlasse);
				if (vorhandenerTyp !== null) {
					throw new DeveloperNotificationException(JavaString.format("Lehrkraft ID=%d und Klasse ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!", idLehrkraft, idKlasse, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}
				gesehen.put(idLehrkraft, idKlasse, typ);
			}
		}
	}

	private checkKlasseLeitung2KombinationEindeutig(): void {
		const typen: List<UvBlockungRegelTyp> | null = ArrayList.of(UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B, UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B);
		const gesehen: HashMap2D<number, number, UvBlockungRegelTyp> | null = new HashMap2D<number, number, UvBlockungRegelTyp>();
		for (const typ of typen) {
			for (const regel of this.regelnGetMengeByTypAsList(typ)) {
				const idLehrkraft: number = regel.parameter.get(0).valueOf();
				const idKlasse: number = regel.parameter.get(1).valueOf();
				const vorhandenerTyp: UvBlockungRegelTyp | null = gesehen.getOrNull(idLehrkraft, idKlasse);
				if (vorhandenerTyp !== null) {
					throw new DeveloperNotificationException(JavaString.format("Lehrkraft ID=%d und Klasse ID=%d sind bereits in Regel '%s' eingetragen, Konflikt mit '%s'!", idLehrkraft, idKlasse, vorhandenerTyp.bezeichnung, typ.bezeichnung));
				}
				gesehen.put(idLehrkraft, idKlasse, typ);
			}
		}
	}

	private checkKlasseLeitung1UndLeitung2NichtGleichzeitigFixiert(): void {
		const gesehenLeitung1: HashMap2D<number, number, UvBlockungRegelTyp> | null = new HashMap2D<number, number, UvBlockungRegelTyp>();
		for (const regel of this.regelnGetMengeByTypAsList(UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B)) {
			gesehenLeitung1.put(regel.parameter.get(0), regel.parameter.get(1), UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B);
		}
		for (const regel of this.regelnGetMengeByTypAsList(UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B)) {
			const idLehrkraft: number = regel.parameter.get(0).valueOf();
			const idKlasse: number = regel.parameter.get(1).valueOf();
			if (gesehenLeitung1.getOrNull(idLehrkraft, idKlasse) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Lehrkraft ID=%d kann nicht gleichzeitig Klassenlehrer (Regel 32) und stellv. Klassenlehrer (Regel 33) in Klasse ID=%d sein!", idLehrkraft, idKlasse));
			}
		}
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
		return new ArrayList<UvBlockungRegel>(this.regelmengeFehlerhaft);
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
