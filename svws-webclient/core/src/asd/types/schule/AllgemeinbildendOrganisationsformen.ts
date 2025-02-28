import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { OrganisationsformKatalogEintrag } from '../../../asd/data/schule/OrganisationsformKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class AllgemeinbildendOrganisationsformen extends JavaEnum<AllgemeinbildendOrganisationsformen> implements CoreType<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<AllgemeinbildendOrganisationsformen> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, AllgemeinbildendOrganisationsformen> = new Map<string, AllgemeinbildendOrganisationsformen>();

	/**
	 * Organisationsform: Nicht zuordenbar (Früherziehung für Hör- und Sehgeschädigte, Ambulante Maßnahmen)
	 */
	public static readonly NICHT_ZUGEORDNET : AllgemeinbildendOrganisationsformen = new AllgemeinbildendOrganisationsformen("NICHT_ZUGEORDNET", 0, );

	/**
	 * Organisationsform: Halbtagsunterricht
	 */
	public static readonly HALBTAG : AllgemeinbildendOrganisationsformen = new AllgemeinbildendOrganisationsformen("HALBTAG", 1, );

	/**
	 * Organisationsform: Teilnahme am gebundenen Ganztag
	 */
	public static readonly GANZTAG : AllgemeinbildendOrganisationsformen = new AllgemeinbildendOrganisationsformen("GANZTAG", 2, );

	/**
	 * Organisationsform: Teilnahme am erweiterten Ganztag
	 */
	public static readonly GANZTAG_ERWEITERT : AllgemeinbildendOrganisationsformen = new AllgemeinbildendOrganisationsformen("GANZTAG_ERWEITERT", 3, );

	/**
	 * Organisationsform: Teilnahme am offenen Ganztag
	 */
	public static readonly GANZTAG_OFFEN : AllgemeinbildendOrganisationsformen = new AllgemeinbildendOrganisationsformen("GANZTAG_OFFEN", 4, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		AllgemeinbildendOrganisationsformen.all_values_by_ordinal.push(this);
		AllgemeinbildendOrganisationsformen.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen>) : void {
		CoreTypeDataManager.putManager(AllgemeinbildendOrganisationsformen.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> {
		return CoreTypeDataManager.getManager(AllgemeinbildendOrganisationsformen.class);
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
		return AllgemeinbildendOrganisationsformen.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<AllgemeinbildendOrganisationsformen> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : AllgemeinbildendOrganisationsformen | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : OrganisationsformKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<OrganisationsformKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<AllgemeinbildendOrganisationsformen>('de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen');

}

export function cast_de_svws_nrw_asd_types_schule_AllgemeinbildendOrganisationsformen(obj : unknown) : AllgemeinbildendOrganisationsformen {
	return obj as AllgemeinbildendOrganisationsformen;
}
