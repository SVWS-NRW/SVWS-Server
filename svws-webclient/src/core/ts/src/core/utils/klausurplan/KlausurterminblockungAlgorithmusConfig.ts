import { JavaObject } from '../../../java/lang/JavaObject';

export class KlausurterminblockungAlgorithmusConfig extends JavaObject {

	/**
	 *  Dieser LK-GK-Modus blockt beide Kursarten gemischt.
	 */
	public static readonly LK_GK_MODUS_BEIDE : number = 0;

	/**
	 *  Dieser LK-GK-Modus blockt nur die Kursart LK.
	 */
	public static readonly LK_GK_MODUS_NUR_LK : number = 1;

	/**
	 *  Dieser LK-GK-Modus blockt nur die Kursart GK.
	 */
	public static readonly LK_GK_MODUS_NUR_GK : number = 2;

	/**
	 *  Dieser LK-GK-Modus blockt zuerst die Kursart LK, danach die Kursart GK.
	 */
	public static readonly LK_GK_MODUS_GETRENNT : number = 3;

	/**
	 *  Der normale Algorithmus minimiert die Anzahl der Termine.
	 */
	public static readonly ALGORITHMUS_NORMAL : number = 1;

	/**
	 *  Dieser Algorithmus forciert, das pro Termin nur die selben Fächer sind (LK+GK).
	 *  Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public static readonly ALGORITHMUS_FAECHERWEISE : number = 2;

	/**
	 *  Dieser Algorithmus forciert, das pro Termin nur die selben Kurs-Schienen sind.
	 *  Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public static readonly ALGORITHMUS_SCHIENENWEISE : number = 3;

	private max_time_millis : number = 0;

	private algorithmus : number = 0;

	private lk_gk_modus : number = 0;

	private regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin : boolean = false;

	private regel_bevorzuge_gleiche_kursschienen_pro_termin : boolean = false;


	/**
	 * Der Konstruktor definiert Standardwerte.
	 */
	public constructor() {
		super();
		this.set_max_time_millis(1000);
		this.set_lk_gk_modus_beide();
		this.set_algorithmus_normal();
		this.set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(false);
		this.set_regel_bevorzuge_gleiche_kursschienen_pro_termin(false);
	}

	/**
	 * Liefert die maximale Blockungszeit.
	 * @return die maximale Blockungszeit.
	 */
	public get_max_time_millis() : number {
		return this.max_time_millis;
	}

	/**
	 * Setzt die maximale Blockungszeit.
	 * @param pMax_time_millis die maximale Blockungszeit.
	 */
	public set_max_time_millis(pMax_time_millis : number) : void {
		this.max_time_millis = pMax_time_millis;
	}

	/**
	 * Liefert den selektierten Algorithmus.
	 * @return den selektierten Algorithmus.
	 */
	public get_algorithmus() : number {
		return this.algorithmus;
	}

	/**
	 * Der normale Algorithmus minimiert die Anzahl der Termine.
	 */
	public set_algorithmus_normal() : void {
		this.algorithmus = KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_NORMAL;
	}

	/**
	 * Dieser Algorithmus forciert, das pro Termin nur die selben Fächer sind (LK+GK).
	 * Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public set_algorithmus_faecherweise() : void {
		this.algorithmus = KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_FAECHERWEISE;
	}

	/**
	 * Dieser Algorithmus forciert, das pro Termin nur die selben Kurs-Schienen sind.
	 * Im zweiten Schritt werden die Termine versucht zu minimieren.
	 */
	public set_algorithmus_schienenweise() : void {
		this.algorithmus = KlausurterminblockungAlgorithmusConfig.ALGORITHMUS_SCHIENENWEISE;
	}

	/**
	 * Setzt den zu verwendenden Algorithmus.
	 * @param pAlgorithmus den zu verwendenden Algorithmus.
	 */
	public set_algorithmus(pAlgorithmus : number) : void {
		this.algorithmus = pAlgorithmus;
	}

	/**
	 * Liefert den LK-GK-Modus.
	 * @return den LK-GK-Modus.
	 */
	public get_lk_gk_modus() : number {
		return this.lk_gk_modus;
	}

	/**
	 * 	Dieser LK-GK-Modus blockt beide Kursarten gemischt.
	 */
	public set_lk_gk_modus_beide() : void {
		this.lk_gk_modus = KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE;
	}

	/**
	 * Dieser LK-GK-Modus blockt nur die Kursart LK.
	 */
	public set_lk_gk_modus_nur_lk() : void {
		this.lk_gk_modus = KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK;
	}

	/**
	 * Dieser LK-GK-Modus blockt nur die Kursart GK.
	 */
	public set_lk_gk_modus_nur_gk() : void {
		this.lk_gk_modus = KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK;
	}

	/**
	 * Dieser LK-GK-Modus blockt zuerst die Kursart LK, danach die Kursart GK.
	 */
	public set_lk_gk_modus_getrennt() : void {
		this.lk_gk_modus = KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT;
	}

	/**
	 * Setzt den LK-GK-Modus.
	 * @param lk_gk_modus ein Wert aus {@link #LK_GK_MODUS_BEIDE}, {@link #LK_GK_MODUS_NUR_GK}, {@link #LK_GK_MODUS_NUR_LK}, {@link #LK_GK_MODUS_GETRENNT}.
	 */
	public set_lk_gk_modus(lk_gk_modus : number) : void {
		this.lk_gk_modus = lk_gk_modus;
	}

	/**
	 * Liefert TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 * @return TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public get_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin() : boolean {
		return this.regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	}

	/**
	 * TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 * @param pAktivieren TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(pAktivieren : boolean) : void {
		this.regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin = pAktivieren;
	}

	/**
	 * Liefert TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 * @return TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 */
	public get_regel_bevorzuge_gleiche_kursschienen_pro_termin() : boolean {
		return this.regel_bevorzuge_gleiche_kursschienen_pro_termin;
	}

	/**
	 * TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
	 * @param pAktivieren TRUETRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
	 */
	public set_regel_bevorzuge_gleiche_kursschienen_pro_termin(pAktivieren : boolean) : void {
		this.regel_bevorzuge_gleiche_kursschienen_pro_termin = pAktivieren;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.KlausurterminblockungAlgorithmusConfig'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_KlausurterminblockungAlgorithmusConfig(obj : unknown) : KlausurterminblockungAlgorithmusConfig {
	return obj as KlausurterminblockungAlgorithmusConfig;
}
