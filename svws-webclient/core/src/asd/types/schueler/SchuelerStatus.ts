import { JavaEnum } from '../../../java/lang/JavaEnum';
import { SchuelerStatusKatalogEintrag } from '../../../asd/data/schueler/SchuelerStatusKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class SchuelerStatus extends JavaEnum<SchuelerStatus> implements CoreType<SchuelerStatusKatalogEintrag, SchuelerStatus> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchuelerStatus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchuelerStatus> = new Map<string, SchuelerStatus>();

	/**
	 * Status Neuaufnahme mit dem Wert 0
	 */
	public static readonly NEUAUFNAHME : SchuelerStatus = new SchuelerStatus("NEUAUFNAHME", 0, );

	/**
	 * Status Warteliste mit dem Wert 1
	 */
	public static readonly WARTELISTE : SchuelerStatus = new SchuelerStatus("WARTELISTE", 1, );

	/**
	 * Status Aktiv mit dem Wert 2
	 */
	public static readonly AKTIV : SchuelerStatus = new SchuelerStatus("AKTIV", 2, );

	/**
	 * Status Beurlaubt mit dem Wert 3
	 */
	public static readonly BEURLAUBT : SchuelerStatus = new SchuelerStatus("BEURLAUBT", 3, );

	/**
	 * Status Extern mit dem Wert 6
	 */
	public static readonly EXTERN : SchuelerStatus = new SchuelerStatus("EXTERN", 4, );

	/**
	 * Status Abschluss mit dem Wert 8
	 */
	public static readonly ABSCHLUSS : SchuelerStatus = new SchuelerStatus("ABSCHLUSS", 5, );

	/**
	 * Status Abgänger mit dem Wert 9
	 */
	public static readonly ABGANG : SchuelerStatus = new SchuelerStatus("ABGANG", 6, );

	/**
	 * Status Abgänger mit dem Wert 10
	 */
	public static readonly EHEMALIGE : SchuelerStatus = new SchuelerStatus("EHEMALIGE", 7, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		SchuelerStatus.all_values_by_ordinal.push(this);
		SchuelerStatus.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus>) : void {
		CoreTypeDataManager.putManager(SchuelerStatus.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus> {
		return CoreTypeDataManager.getManager(SchuelerStatus.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchuelerStatus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchuelerStatus | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SchuelerStatusKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<SchuelerStatusKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.SchuelerStatus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schueler.SchuelerStatus', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<SchuelerStatus>('de.svws_nrw.asd.types.schueler.SchuelerStatus');

}

export function cast_de_svws_nrw_asd_types_schueler_SchuelerStatus(obj : unknown) : SchuelerStatus {
	return obj as SchuelerStatus;
}
