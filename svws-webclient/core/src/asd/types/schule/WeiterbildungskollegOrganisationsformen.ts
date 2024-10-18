import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { OrganisationsformKatalogEintrag } from '../../../asd/data/schule/OrganisationsformKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class WeiterbildungskollegOrganisationsformen extends JavaEnum<WeiterbildungskollegOrganisationsformen> implements CoreType<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<WeiterbildungskollegOrganisationsformen> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, WeiterbildungskollegOrganisationsformen> = new Map<string, WeiterbildungskollegOrganisationsformen>();

	/**
	 * Organisationsform: Teilbeleger
	 */
	public static readonly TEILZEIT : WeiterbildungskollegOrganisationsformen = new WeiterbildungskollegOrganisationsformen("TEILZEIT", 0, );

	/**
	 * Organisationsform: Vollbeleger
	 */
	public static readonly VOLLZEIT : WeiterbildungskollegOrganisationsformen = new WeiterbildungskollegOrganisationsformen("VOLLZEIT", 1, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		WeiterbildungskollegOrganisationsformen.all_values_by_ordinal.push(this);
		WeiterbildungskollegOrganisationsformen.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen>) : void {
		CoreTypeDataManager.putManager(WeiterbildungskollegOrganisationsformen.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> {
		return CoreTypeDataManager.getManager(WeiterbildungskollegOrganisationsformen.class);
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
		return WeiterbildungskollegOrganisationsformen.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<WeiterbildungskollegOrganisationsformen> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : WeiterbildungskollegOrganisationsformen | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : OrganisationsformKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<OrganisationsformKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<WeiterbildungskollegOrganisationsformen>('de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen');

}

export function cast_de_svws_nrw_asd_types_schule_WeiterbildungskollegOrganisationsformen(obj : unknown) : WeiterbildungskollegOrganisationsformen {
	return obj as WeiterbildungskollegOrganisationsformen;
}
