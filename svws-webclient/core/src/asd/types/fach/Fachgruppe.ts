import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { FachgruppeKatalogEintrag } from '../../../asd/data/fach/FachgruppeKatalogEintrag';
import { RGBFarbe } from '../../../asd/data/RGBFarbe';

export class Fachgruppe extends JavaEnum<Fachgruppe> implements CoreType<FachgruppeKatalogEintrag, Fachgruppe> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Fachgruppe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Fachgruppe> = new Map<string, Fachgruppe>();

	/**
	 * Fachgruppe Deutsch
	 */
	public static readonly FG_D : Fachgruppe = new Fachgruppe("FG_D", 0, );

	/**
	 * Fachgruppe Arbeitslehre
	 */
	public static readonly FG_AL : Fachgruppe = new Fachgruppe("FG_AL", 1, );

	/**
	 * Fachgruppe Fremdsprachen
	 */
	public static readonly FG_FS : Fachgruppe = new Fachgruppe("FG_FS", 2, );

	/**
	 * Fachgruppe Kunst und Musik
	 */
	public static readonly FG_MS : Fachgruppe = new Fachgruppe("FG_MS", 3, );

	/**
	 * Fachgruppe Literatur, instrumental- oder vokalpraktischer Kurs
	 */
	public static readonly FG_ME : Fachgruppe = new Fachgruppe("FG_ME", 4, );

	/**
	 * Fachgruppe Gesellschaftswissenschaft
	 */
	public static readonly FG_GS : Fachgruppe = new Fachgruppe("FG_GS", 5, );

	/**
	 * Fachgruppe Philosophie
	 */
	public static readonly FG_PL : Fachgruppe = new Fachgruppe("FG_PL", 6, );

	/**
	 * Fachgruppe Religion
	 */
	public static readonly FG_RE : Fachgruppe = new Fachgruppe("FG_RE", 7, );

	/**
	 * Fachgruppe Mathematik
	 */
	public static readonly FG_M : Fachgruppe = new Fachgruppe("FG_M", 8, );

	/**
	 * Fachgruppe Naturwissenschaften
	 */
	public static readonly FG_NW : Fachgruppe = new Fachgruppe("FG_NW", 9, );

	/**
	 * Fachgruppe weiteres naturwissenschaftliches / technisches Fach
	 */
	public static readonly FG_WN : Fachgruppe = new Fachgruppe("FG_WN", 10, );

	/**
	 * Fachgruppe Sport
	 */
	public static readonly FG_SP : Fachgruppe = new Fachgruppe("FG_SP", 11, );

	/**
	 * Fachgruppe Vertiefungskurs
	 */
	public static readonly FG_VX : Fachgruppe = new Fachgruppe("FG_VX", 12, );

	/**
	 * Fachgruppe Projektkurs
	 */
	public static readonly FG_PX : Fachgruppe = new Fachgruppe("FG_PX", 13, );

	/**
	 * Fachgruppe Berufsübergreifender Bereich
	 */
	public static readonly FG_BUE : Fachgruppe = new Fachgruppe("FG_BUE", 14, );

	/**
	 * Fachgruppe Berufsbezogener Bereich
	 */
	public static readonly FG_BBS : Fachgruppe = new Fachgruppe("FG_BBS", 15, );

	/**
	 * Fachgruppe Berufsbezogener Bereich (Schwerpunkt)
	 */
	public static readonly FG_BBS_SCHWERPUNKT : Fachgruppe = new Fachgruppe("FG_BBS_SCHWERPUNKT", 16, );

	/**
	 * Fachgruppe Differenzierungsbereich
	 */
	public static readonly FG_DF : Fachgruppe = new Fachgruppe("FG_DF", 17, );

	/**
	 * Fachgruppe Berufspraktikum
	 */
	public static readonly FG_BP : Fachgruppe = new Fachgruppe("FG_BP", 18, );

	/**
	 * Fachgruppe besondere Lernleistung
	 */
	public static readonly FG_BLL : Fachgruppe = new Fachgruppe("FG_BLL", 19, );

	/**
	 * Fachgruppe Wahlpflichtbereich
	 */
	public static readonly FG_WP : Fachgruppe = new Fachgruppe("FG_WP", 20, );

	/**
	 * Fachgruppe Zusätzliche Unterrichtsveranstaltungen
	 */
	public static readonly FG_ZUV : Fachgruppe = new Fachgruppe("FG_ZUV", 21, );

	/**
	 * Fachgruppe Angleichungskurse
	 */
	public static readonly FG_ANG : Fachgruppe = new Fachgruppe("FG_ANG", 22, );

	/**
	 * Fachgruppe Sprache
	 */
	public static readonly FG_D_SP : Fachgruppe = new Fachgruppe("FG_D_SP", 23, );

	/**
	 * Fachgruppe Sachunterricht
	 */
	public static readonly FG_SU : Fachgruppe = new Fachgruppe("FG_SU", 24, );

	/**
	 * Fachgruppe Förderunterricht
	 */
	public static readonly FG_FOE : Fachgruppe = new Fachgruppe("FG_FOE", 25, );

	/**
	 * Fachgruppe Abschlussarbeit
	 */
	public static readonly FG_ABA : Fachgruppe = new Fachgruppe("FG_ABA", 26, );

	/**
	 * Fachgruppe Projektarbeit
	 */
	public static readonly FG_PA : Fachgruppe = new Fachgruppe("FG_PA", 27, );

	/**
	 * Fachgruppe Informatik (Sek I)
	 */
	public static readonly FG_IF : Fachgruppe = new Fachgruppe("FG_IF", 28, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Fachgruppe.all_values_by_ordinal.push(this);
		Fachgruppe.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe>) : void {
		CoreTypeDataManager.putManager(Fachgruppe.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe> {
		return CoreTypeDataManager.getManager(Fachgruppe.class);
	}

	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		return Fachgruppe.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Gibt die Farbe der Fachgruppe zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die Farbe der Fachgruppe
	 */
	public getFarbe(schuljahr : number) : RGBFarbe {
		const fgke : FachgruppeKatalogEintrag | null = Fachgruppe.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if (fgke === null)
			return new RGBFarbe();
		return fgke.farbe;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Fachgruppe> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Fachgruppe | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : FachgruppeKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<FachgruppeKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.fach.Fachgruppe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.fach.Fachgruppe', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Fachgruppe>('de.svws_nrw.asd.types.fach.Fachgruppe');

}

export function cast_de_svws_nrw_asd_types_fach_Fachgruppe(obj : unknown) : Fachgruppe {
	return obj as Fachgruppe;
}
