import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { OrganisationsformKatalogEintrag } from '../../../asd/data/schule/OrganisationsformKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class BerufskollegOrganisationsformen extends JavaEnum<BerufskollegOrganisationsformen> implements CoreType<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BerufskollegOrganisationsformen> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BerufskollegOrganisationsformen> = new Map<string, BerufskollegOrganisationsformen>();

	/**
	 * Organisationsform: Teilzeitunterricht (außerhalb der TZ-Berufsschule)
	 */
	public static readonly TEILZEIT : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("TEILZEIT", 0, );

	/**
	 * Organisationsform: Teilzeitunterricht ohne Blockunterricht (Normalklasse) TZ-Berufsschule
	 */
	public static readonly TEILZEIT_NORMALKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("TEILZEIT_NORMALKLASSE", 1, );

	/**
	 * Organisationsform: Teilzeitunterricht ohne Blockunterricht (Bezirksfachklasse) TZ-Berufsschule
	 */
	public static readonly TEILZEIT_BEZIRKSFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("TEILZEIT_BEZIRKSFACHKLASSE", 2, );

	/**
	 * Organisationsform: Teilzeitunterricht ohne Blockunterricht (Landesfachklasse) TZ-Berufsschule
	 */
	public static readonly TEILZEIT_LANDESFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("TEILZEIT_LANDESFACHKLASSE", 3, );

	/**
	 * Organisationsform: Vollzeitunterricht
	 */
	public static readonly VOLLZEIT : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("VOLLZEIT", 4, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. im Unterricht (Normalklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_IM_UNTERRICHT : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_IM_UNTERRICHT", 5, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Normalklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_NICHT_IM_UNTERRICHT : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_NICHT_IM_UNTERRICHT", 6, );

	/**
	 * Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Normalklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT", 7, );

	/**
	 * Organisationsform: Ganztagsunterricht (Normalklasse) im dualen System
	 */
	public static readonly DUAL_GANZTAG : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_GANZTAG", 8, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. im Unterricht (Bezirksfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_IM_UNTERRICHT_BEZIRKSFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_IM_UNTERRICHT_BEZIRKSFACHKLASSE", 9, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Bezirksfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_NICHT_IM_UNTERRICHT_BEZIRKSFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_NICHT_IM_UNTERRICHT_BEZIRKSFACHKLASSE", 10, );

	/**
	 * Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Bezirksfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_BEZIRKSFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_BEZIRKSFACHKLASSE", 11, );

	/**
	 * Organisationsform: Ganztagsunterricht (Bezirksfachklasse) im dualen System
	 */
	public static readonly DUAL_GANZTAG_BEZIRKSFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_GANZTAG_BEZIRKSFACHKLASSE", 12, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. im Unterricht (Landesfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_IM_UNTERRICHT_LANDESFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_IM_UNTERRICHT_LANDESFACHKLASSE", 13, );

	/**
	 * Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Landesfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_NICHT_IM_UNTERRICHT_LANDESFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_NICHT_IM_UNTERRICHT_LANDESFACHKLASSE", 14, );

	/**
	 * Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Landesfachklasse) im dualen System
	 */
	public static readonly DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_LANDESFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_LANDESFACHKLASSE", 15, );

	/**
	 * Organisationsform: Ganztagsunterricht (Landesfachklasse) im dualen System
	 */
	public static readonly DUAL_GANZTAG_LANDESFACHKLASSE : BerufskollegOrganisationsformen = new BerufskollegOrganisationsformen("DUAL_GANZTAG_LANDESFACHKLASSE", 16, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		BerufskollegOrganisationsformen.all_values_by_ordinal.push(this);
		BerufskollegOrganisationsformen.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen>) : void {
		CoreTypeDataManager.putManager(BerufskollegOrganisationsformen.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> {
		return CoreTypeDataManager.getManager(BerufskollegOrganisationsformen.class);
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
		return BerufskollegOrganisationsformen.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegOrganisationsformen> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegOrganisationsformen | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> {
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
		return 'de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BerufskollegOrganisationsformen>('de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegOrganisationsformen(obj : unknown) : BerufskollegOrganisationsformen {
	return obj as BerufskollegOrganisationsformen;
}
