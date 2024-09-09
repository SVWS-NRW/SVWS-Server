import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { UebergangsempfehlungKatalogEintrag } from '../../../asd/data/schueler/UebergangsempfehlungKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Uebergangsempfehlung extends JavaEnum<Uebergangsempfehlung> implements CoreType<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Uebergangsempfehlung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Uebergangsempfehlung> = new Map<string, Uebergangsempfehlung>();

	/**
	 *  Übergangsempfehlung Hauptschule
	 */
	public static readonly HAUPTSCHULE : Uebergangsempfehlung = new Uebergangsempfehlung("HAUPTSCHULE", 0, );

	/**
	 *  Übergangsempfehlung Hauptschule / Realschule (eingeschränkt)
	 */
	public static readonly HAUPTSCHULE_REALSCHULE : Uebergangsempfehlung = new Uebergangsempfehlung("HAUPTSCHULE_REALSCHULE", 1, );

	/**
	 *  Übergangsempfehlung Realschule
	 */
	public static readonly REALSCHULE : Uebergangsempfehlung = new Uebergangsempfehlung("REALSCHULE", 2, );

	/**
	 *  Übergangsempfehlung Realschule / Gymnasium (eingeschränkt)
	 */
	public static readonly REALSCHULE_GYMNASIUM : Uebergangsempfehlung = new Uebergangsempfehlung("REALSCHULE_GYMNASIUM", 3, );

	/**
	 *  Übergangsempfehlung Gymnasium
	 */
	public static readonly GYMNASIUM : Uebergangsempfehlung = new Uebergangsempfehlung("GYMNASIUM", 4, );

	/**
	 *  Keine Übergangsempfehlung
	 */
	public static readonly KEINE : Uebergangsempfehlung = new Uebergangsempfehlung("KEINE", 5, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Uebergangsempfehlung.all_values_by_ordinal.push(this);
		Uebergangsempfehlung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung>) : void {
		CoreTypeDataManager.putManager(Uebergangsempfehlung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> {
		return CoreTypeDataManager.getManager(Uebergangsempfehlung.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Uebergangsempfehlung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Uebergangsempfehlung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : UebergangsempfehlungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<UebergangsempfehlungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.Uebergangsempfehlung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schueler.Uebergangsempfehlung', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Uebergangsempfehlung>('de.svws_nrw.asd.types.schueler.Uebergangsempfehlung');

}

export function cast_de_svws_nrw_asd_types_schueler_Uebergangsempfehlung(obj : unknown) : Uebergangsempfehlung {
	return obj as Uebergangsempfehlung;
}
