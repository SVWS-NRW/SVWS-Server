import { JavaEnum } from '../../../java/lang/JavaEnum';
import { LaenderKatalogEintrag } from '../../../asd/data/schule/LaenderKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Laender extends JavaEnum<Laender> implements CoreType<LaenderKatalogEintrag, Laender> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<Laender> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, Laender> = new Map<string, Laender>();

	/**
	 * Land: Übriges Ausland
	 */
	public static readonly AL: Laender = new Laender("AL", 0, );

	/**
	 * Land: Belgien
	 */
	public static readonly B: Laender = new Laender("B", 1, );

	/**
	 * Land: Brandenburg
	 */
	public static readonly BB: Laender = new Laender("BB", 2, );

	/**
	 * Land: Berlin
	 */
	public static readonly BE: Laender = new Laender("BE", 3, );

	/**
	 * Land: Baden-Württemberg
	 */
	public static readonly BW: Laender = new Laender("BW", 4, );

	/**
	 * Land: Bayern
	 */
	public static readonly BY: Laender = new Laender("BY", 5, );

	/**
	 * Land: Bremen
	 */
	public static readonly HB: Laender = new Laender("HB", 6, );

	/**
	 * Land: Hessen
	 */
	public static readonly HE: Laender = new Laender("HE", 7, );

	/**
	 * Land: Hamburg
	 */
	public static readonly HH: Laender = new Laender("HH", 8, );

	/**
	 * Land: Luxemburg
	 */
	public static readonly L: Laender = new Laender("L", 9, );

	/**
	 * Land: Mecklenburg-Vorpommern
	 */
	public static readonly MV: Laender = new Laender("MV", 10, );

	/**
	 * Land: Niedersachsen
	 */
	public static readonly NI: Laender = new Laender("NI", 11, );

	/**
	 * Land: Niederlande
	 */
	public static readonly NL: Laender = new Laender("NL", 12, );

	/**
	 * Land: Nordrhein-Westfalen
	 */
	public static readonly NW: Laender = new Laender("NW", 13, );

	/**
	 * Land: Rheinland-Pfalz
	 */
	public static readonly RP: Laender = new Laender("RP", 14, );

	/**
	 * Land: Saarland
	 */
	public static readonly SL: Laender = new Laender("SL", 15, );

	/**
	 * Land: Sachsen
	 */
	public static readonly SN: Laender = new Laender("SN", 16, );

	/**
	 * Land: Sachsen-Anhalt
	 */
	public static readonly ST: Laender = new Laender("ST", 17, );

	/**
	 * Land: Schleswig-Holstein
	 */
	public static readonly SH: Laender = new Laender("SH", 18, );

	/**
	 * Land: Thüringen
	 */
	public static readonly TH: Laender = new Laender("TH", 19, );

	private constructor(name: string, ordinal: number) {
		super(name, ordinal);
		Laender.all_values_by_ordinal.push(this);
		Laender.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static init(manager: CoreTypeDataManager<LaenderKatalogEintrag, Laender>): void {
		CoreTypeDataManager.putManager(Laender.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static data(): CoreTypeDataManager<LaenderKatalogEintrag, Laender> {
		return CoreTypeDataManager.getManager(Laender.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<Laender> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): Laender | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager(): CoreTypeDataManager<LaenderKatalogEintrag, Laender> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr: number): LaenderKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId(): string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie(): List<LaenderKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Laender';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.types.schule.Laender', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<Laender>('de.svws_nrw.asd.types.schule.Laender');

}

export function cast_de_svws_nrw_asd_types_schule_Laender(obj: unknown): Laender {
	return obj as Laender;
}
