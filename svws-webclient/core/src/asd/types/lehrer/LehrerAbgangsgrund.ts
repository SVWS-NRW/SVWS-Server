import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { LehrerAbgangsgrundKatalogEintrag } from '../../../asd/data/lehrer/LehrerAbgangsgrundKatalogEintrag';

export class LehrerAbgangsgrund extends JavaEnum<LehrerAbgangsgrund> implements CoreType<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerAbgangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerAbgangsgrund> = new Map<string, LehrerAbgangsgrund>();

	/**
	 * Grund 'Eintritt in den Ruhestand' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly RUHEST : LehrerAbgangsgrund = new LehrerAbgangsgrund("RUHEST", 0, );

	/**
	 * Grund 'Dienst-, Erwerbs-, Berufsunfähigkeit' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly UNFAEHIGK : LehrerAbgangsgrund = new LehrerAbgangsgrund("UNFAEHIGK", 1, );

	/**
	 * Grund 'Tod' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly TOD : LehrerAbgangsgrund = new LehrerAbgangsgrund("TOD", 2, );

	/**
	 * Grund 'Übertritt in den Schuldienst eines anderen Bundeslandes' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly AndBuLand : LehrerAbgangsgrund = new LehrerAbgangsgrund("AndBuLand", 3, );

	/**
	 * Grund 'Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly WECHSEL : LehrerAbgangsgrund = new LehrerAbgangsgrund("WECHSEL", 4, );

	/**
	 * Grund 'Befristete Abgänge' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly BEFRIST : LehrerAbgangsgrund = new LehrerAbgangsgrund("BEFRIST", 5, );

	/**
	 * Grund 'Sonstige Abgänge' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly SONSTIG : LehrerAbgangsgrund = new LehrerAbgangsgrund("SONSTIG", 6, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerAbgangsgrund.all_values_by_ordinal.push(this);
		LehrerAbgangsgrund.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund>) : void {
		CoreTypeDataManager.putManager(LehrerAbgangsgrund.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> {
		return CoreTypeDataManager.getManager(LehrerAbgangsgrund.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerAbgangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerAbgangsgrund | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerAbgangsgrundKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<LehrerAbgangsgrundKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerAbgangsgrund>('de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerAbgangsgrund(obj : unknown) : LehrerAbgangsgrund {
	return obj as LehrerAbgangsgrund;
}
