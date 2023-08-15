import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurterminblockungModusKursarten } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusKursarten';
import { KlausurterminblockungAlgorithmen } from '../../../core/types/gost/klausurplanung/KlausurterminblockungAlgorithmen';
import { KlausurterminblockungModusQuartale } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusQuartale';

export class KlausurterminblockungAlgorithmusConfig extends JavaObject {

	private max_time_millis : number = 0;

	/**
	 * Der Typ des Algorithmus, welcher verwendet wird.
	 */
	public algorithmus : KlausurterminblockungAlgorithmen = KlausurterminblockungAlgorithmen.NORMAL;

	/**
	 * Der Modus für die Art, ob und wie die beiden Kursarten LK und GK geblockt werden
	 */
	public modusKursarten : KlausurterminblockungModusKursarten = KlausurterminblockungModusKursarten.BEIDE;

	/**
	 * Der Modus dafür, wie die Klausuren in den Quartalen eines Halbjahres geblockt werden.
	 */
	public modusQuartale : KlausurterminblockungModusQuartale = KlausurterminblockungModusQuartale.ZUSAMMEN;

	private regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin : boolean = false;

	private regel_bevorzuge_gleiche_kursschienen_pro_termin : boolean = false;


	/**
	 * Der Konstruktor definiert Standardwerte.
	 */
	public constructor() {
		super();
		this.set_max_time_millis(1000);
		this.set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(false);
		this.set_regel_bevorzuge_gleiche_kursschienen_pro_termin(false);
	}

	/**
	 * Liefert die maximale Blockungszeit.
	 *
	 * @return die maximale Blockungszeit.
	 */
	public get_max_time_millis() : number {
		return this.max_time_millis;
	}

	/**
	 * Setzt die maximale Blockungszeit.
	 *
	 * @param pMaxTimeMillis die maximale Blockungszeit.
	 */
	public set_max_time_millis(pMaxTimeMillis : number) : void {
		this.max_time_millis = pMaxTimeMillis;
	}

	/**
	 * Liefert TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 *
	 * @return TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public get_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin() : boolean {
		return this.regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	}

	/**
	 * TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 *
	 * @param pAktivieren TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(pAktivieren : boolean) : void {
		this.regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin = pAktivieren;
	}

	/**
	 * Liefert TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 *
	 * @return TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 */
	public get_regel_bevorzuge_gleiche_kursschienen_pro_termin() : boolean {
		return this.regel_bevorzuge_gleiche_kursschienen_pro_termin;
	}

	/**
	 * TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
	 * @param pAktivieren TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
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
