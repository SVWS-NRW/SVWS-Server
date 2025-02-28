import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerBeschaeftigungsartKatalogEintrag } from '../../../asd/data/lehrer/LehrerBeschaeftigungsartKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerBeschaeftigungsart extends JavaEnum<LehrerBeschaeftigungsart> implements CoreType<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerBeschaeftigungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerBeschaeftigungsart> = new Map<string, LehrerBeschaeftigungsart>();

	/**
	 * Beschaeftigungsart 'Vollzeit' eines Lehrers
	 */
	public static readonly V : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("V", 0, );

	/**
	 * Beschaeftigungsart 'Teilzeit' eines Lehrers
	 */
	public static readonly T : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("T", 1, );

	/**
	 * Beschaeftigungsart 'Altersteilzeit (Beschäftigungsphase)' eines Lehrers
	 */
	public static readonly AT : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("AT", 2, );

	/**
	 * Beschaeftigungsart 'Altersteilzeit, vorm. teilzeitbeschäftigt (Verzichtsphase Altersermäßigung)' eines Lehrers
	 */
	public static readonly TA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("TA", 3, );

	/**
	 * Beschaeftigungsart 'Altersteilzeit, vorm. vollzeitbeschäftigt (Verzichtsphase Altersermäßigung)' eines Lehrers
	 */
	public static readonly VA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("VA", 4, );

	/**
	 * Beschaeftigungsart 'Sabbatjahr' eines Lehrers
	 */
	public static readonly TS : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("TS", 5, );

	/**
	 * Beschaeftigungsart 'Nebenberufliche Beschäftigung' eines Lehrers
	 */
	public static readonly SB : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("SB", 6, );

	/**
	 * Beschaeftigungsart 'Geringfügige Beschäftigung' eines Lehrers
	 */
	public static readonly GB : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("GB", 7, );

	/**
	 * Beschaeftigungsart 'Studierende' eines Lehrers
	 */
	public static readonly ST : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("ST", 8, );

	/**
	 * Beschaeftigungsart 'Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)' eines Lehrers
	 */
	public static readonly NA : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("NA", 9, );

	/**
	 * Beschaeftigungsart 'Gestellungsvertrag' eines Lehrers
	 */
	public static readonly G : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("G", 10, );

	/**
	 * Beschaeftigungsart 'Unentgeltlich Beschäftigte' eines Lehrers
	 */
	public static readonly X : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("X", 11, );

	/**
	 * Beschaeftigungsart 'Beamte auf Widerruf (LAA) in Teilzeit' eines Lehrers
	 */
	public static readonly WT : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("WT", 12, );

	/**
	 * Beschaeftigungsart 'Beamte auf Widerruf (LAA) in Vollzeit' eines Lehrers
	 */
	public static readonly WV : LehrerBeschaeftigungsart = new LehrerBeschaeftigungsart("WV", 13, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerBeschaeftigungsart.all_values_by_ordinal.push(this);
		LehrerBeschaeftigungsart.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart>) : void {
		CoreTypeDataManager.putManager(LehrerBeschaeftigungsart.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> {
		return CoreTypeDataManager.getManager(LehrerBeschaeftigungsart.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerBeschaeftigungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerBeschaeftigungsart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerBeschaeftigungsartKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerBeschaeftigungsartKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerBeschaeftigungsart>('de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerBeschaeftigungsart(obj : unknown) : LehrerBeschaeftigungsart {
	return obj as LehrerBeschaeftigungsart;
}
