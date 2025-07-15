import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag } from '../../../asd/data/schule/BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class BeruflichesGymnasiumPruefungsordnungAnlage extends JavaEnum<BeruflichesGymnasiumPruefungsordnungAnlage> implements CoreType<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BeruflichesGymnasiumPruefungsordnungAnlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BeruflichesGymnasiumPruefungsordnungAnlage> = new Map<string, BeruflichesGymnasiumPruefungsordnungAnlage>();

	/**
	 * Anlage D1: Bautechnische/-r Assistent/-in/AHR
	 */
	public static readonly D1 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D1", 0, );

	/**
	 * Elektrotechnische/-r Assistent/-in/AHR
	 */
	public static readonly D2 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D2", 1, );

	/**
	 * Erzieher/-in/AHR
	 */
	public static readonly D3 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D3", 2, );

	/**
	 * Informationstechnische/-r Assistent/-in/AHR
	 */
	public static readonly D3a : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D3a", 3, );

	/**
	 * Gestaltungstechnische/-r Assistent/-in/AHR
	 */
	public static readonly D4 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D4", 4, );

	/**
	 * Assistent/-in für Konstruktions- und Fertigungstechnik/AHR
	 */
	public static readonly D6 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D6", 5, );

	/**
	 * Assistent/-in für Konstruktions- und Fertigungstechnik/AHR
	 */
	public static readonly D7 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D7", 6, );

	/**
	 * Chemisch-technische/-r Assistent/-in/AHR
	 */
	public static readonly D8 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D8", 7, );

	/**
	 * Physikalisch-technische/-r Assistent/-in/AHR
	 */
	public static readonly D9 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D9", 8, );

	/**
	 * Umwelttechnische/-r Assistent/-in/AHR
	 */
	public static readonly D10 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D10", 9, );

	/**
	 * Kaufmännische/-r Assistent/-in/AHR
	 */
	public static readonly D12 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D12", 10, );

	/**
	 * Technische/-r Assistent/-in für Betriebsinformatik/AHR
	 */
	public static readonly D13 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D13", 11, );

	/**
	 * Allgemeine Hochschulreife/Bautechnik
	 */
	public static readonly D14 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D14", 12, );

	/**
	 * Allgemeine Hochschulreife/Elektrotechnik
	 */
	public static readonly D15 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D15", 13, );

	/**
	 * Allgemeine Hochschulreife/Ingenieurwissenschaften
	 */
	public static readonly D15a : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D15a", 14, );

	/**
	 * Allgemeine Hochschulreife/Erziehungswissenschaften
	 */
	public static readonly D16 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D16", 15, );

	/**
	 * Allgemeine Hochschulreife/Freizeitsportleiter/-in (Sport, Gesundheitsförderung, Biologie)
	 */
	public static readonly D17 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D17", 16, );

	/**
	 * Allgemeine Hochschulreife/Gesundheit
	 */
	public static readonly D17a : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D17a", 17, );

	/**
	 * Allgemeine Hochschulreife/Kunst, Englisch
	 */
	public static readonly D18 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D18", 18, );

	/**
	 * Allgemeine Hochschulreife/Ernährung
	 */
	public static readonly D19 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D19", 19, );

	/**
	 * Allgemeine Hochschulreife/Maschinenbautechnik
	 */
	public static readonly D20 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D20", 20, );

	/**
	 * Allgemeine Hochschulreife/Mathematik, Informatik
	 */
	public static readonly D21 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D21", 21, );

	/**
	 * Allgemeine Hochschulreife/Biologie, Chemie
	 */
	public static readonly D22 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D22", 22, );

	/**
	 * Allgemeine Hochschulreife/Chemie, Chemietechnik
	 */
	public static readonly D23 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D23", 23, );

	/**
	 * Allgemeine Hochschulreife/Deutsch, Englisch
	 */
	public static readonly D25 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D25", 24, );

	/**
	 * Allgemeine Hochschulreife/Betriebswirtschaftslehre
	 */
	public static readonly D27 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D27", 25, );

	/**
	 * Allgemeine Hochschulreife/Fremdsprachenkorrespondent/-in (Betriebswirtschaftslehre, Sprachen) -> 2024
	 * 	    Allgemeine Hochschulreife/International Business Communication (Betriebswirtschaftslehre Sprachen 2025 ->
	 */
	public static readonly D28 : BeruflichesGymnasiumPruefungsordnungAnlage = new BeruflichesGymnasiumPruefungsordnungAnlage("D28", 26, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		BeruflichesGymnasiumPruefungsordnungAnlage.all_values_by_ordinal.push(this);
		BeruflichesGymnasiumPruefungsordnungAnlage.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage>) : void {
		CoreTypeDataManager.putManager(BeruflichesGymnasiumPruefungsordnungAnlage.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> {
		return CoreTypeDataManager.getManager(BeruflichesGymnasiumPruefungsordnungAnlage.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BeruflichesGymnasiumPruefungsordnungAnlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BeruflichesGymnasiumPruefungsordnungAnlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BeruflichesGymnasiumPruefungsordnungAnlage>('de.svws_nrw.asd.types.schule.BeruflichesGymnasiumPruefungsordnungAnlage');

}

export function cast_de_svws_nrw_asd_types_schule_BeruflichesGymnasiumPruefungsordnungAnlage(obj : unknown) : BeruflichesGymnasiumPruefungsordnungAnlage {
	return obj as BeruflichesGymnasiumPruefungsordnungAnlage;
}
