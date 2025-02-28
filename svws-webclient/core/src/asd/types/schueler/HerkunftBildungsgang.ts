import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HerkunftBildungsgangKatalogEintrag } from '../../../asd/data/schueler/HerkunftBildungsgangKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class HerkunftBildungsgang extends JavaEnum<HerkunftBildungsgang> implements CoreType<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<HerkunftBildungsgang> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, HerkunftBildungsgang> = new Map<string, HerkunftBildungsgang>();

	/**
	 * Berufsschule, Fachklassen (Teilzeit)
	 */
	public static readonly A01 : HerkunftBildungsgang = new HerkunftBildungsgang("A01", 0, );

	/**
	 * Berufsschule, Fachklassen/Fachhochschulreife (Teilzeit)
	 */
	public static readonly A02 : HerkunftBildungsgang = new HerkunftBildungsgang("A02", 1, );

	/**
	 * Berufsschule, Fachklassen/erweiterte Zusatzqualifikation (Teilzeit)
	 */
	public static readonly A03 : HerkunftBildungsgang = new HerkunftBildungsgang("A03", 2, );

	/**
	 * Berufsschule, Fachklassen mit erweitertem Stützunterricht (Teilzeit)
	 */
	public static readonly A04 : HerkunftBildungsgang = new HerkunftBildungsgang("A04", 3, );

	/**
	 * Berufsfachschule, Berufsabschluss/mittlerer Schulabschluss (nach BKAZVO, BBiG/HwO/ in Vollzeit)
	 */
	public static readonly A10 : HerkunftBildungsgang = new HerkunftBildungsgang("A10", 4, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit)
	 */
	public static readonly A11 : HerkunftBildungsgang = new HerkunftBildungsgang("A11", 5, );

	/**
	 * Berufsschule, Ausbildungsvorbereitung (1-jährig, Vollzeit)
	 */
	public static readonly A12 : HerkunftBildungsgang = new HerkunftBildungsgang("A12", 6, );

	/**
	 * Berufsschule, Ausbildungsvorbereitung (1-jährig, Teilzeit)
	 */
	public static readonly A13 : HerkunftBildungsgang = new HerkunftBildungsgang("A13", 7, );

	/**
	 * Berufsabschluss (nach §50 BBiG/§40 HwO)/Mittlerer Schulabschluss
	 */
	public static readonly A14 : HerkunftBildungsgang = new HerkunftBildungsgang("A14", 8, );

	/**
	 * Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife
	 */
	public static readonly A15 : HerkunftBildungsgang = new HerkunftBildungsgang("A15", 9, );

	/**
	 * Fachklassen (nach §2 BKAZVO)
	 */
	public static readonly A16 : HerkunftBildungsgang = new HerkunftBildungsgang("A16", 10, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachoberschulreife (2-jährig, Vollzeit)
	 */
	public static readonly B01 : HerkunftBildungsgang = new HerkunftBildungsgang("B01", 11, );

	/**
	 * Berufsfachschule, Berufsgrundbildung/Fachoberschulreife (2-jährig, Vollzeit)
	 */
	public static readonly B02 : HerkunftBildungsgang = new HerkunftBildungsgang("B02", 12, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachoberschulreife (nach BKAZVO, BBiG/HwO, in Vollzeit)
	 */
	public static readonly B04 : HerkunftBildungsgang = new HerkunftBildungsgang("B04", 13, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit)
	 */
	public static readonly B05 : HerkunftBildungsgang = new HerkunftBildungsgang("B05", 14, );

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/Erweiterter Erster Schulabschluss (1-jährig, Vollzeit)
	 */
	public static readonly B06 : HerkunftBildungsgang = new HerkunftBildungsgang("B06", 15, );

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/Mittlerer Schulabschluss (1-jährig, Vollzeit)
	 */
	public static readonly B07 : HerkunftBildungsgang = new HerkunftBildungsgang("B07", 16, );

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (2-jährig, Vollzeit)
	 */
	public static readonly B08 : HerkunftBildungsgang = new HerkunftBildungsgang("B08", 17, );

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (3-jährig, Teilzeit)
	 */
	public static readonly B09 : HerkunftBildungsgang = new HerkunftBildungsgang("B09", 18, );

	/**
	 * Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (4-jährig, Teilzeit)
	 */
	public static readonly B10 : HerkunftBildungsgang = new HerkunftBildungsgang("B10", 19, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum, 3-jährig, Vollzeit)
	 */
	public static readonly C01 : HerkunftBildungsgang = new HerkunftBildungsgang("C01", 20, );

	/**
	 * Berufsfachschule, Berufsabschluss (2-jährig, Vollzeit)
	 */
	public static readonly C02 : HerkunftBildungsgang = new HerkunftBildungsgang("C02", 21, );

	/**
	 * Berufsfachschule, Berufliche Kenntnisse/FHR (HBFS) (2-jährig, Vollzeit)
	 */
	public static readonly C03 : HerkunftBildungsgang = new HerkunftBildungsgang("C03", 22, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 11 (1-jährig, Teilzeit)
	 */
	public static readonly C05 : HerkunftBildungsgang = new HerkunftBildungsgang("C05", 23, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 12S (1-jährig, Vollzeit)
	 */
	public static readonly C06 : HerkunftBildungsgang = new HerkunftBildungsgang("C06", 24, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (2-jährig, Teilzeit)
	 */
	public static readonly C07 : HerkunftBildungsgang = new HerkunftBildungsgang("C07", 25, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (1-jährig, Vollzeit)
	 */
	public static readonly C08 : HerkunftBildungsgang = new HerkunftBildungsgang("C08", 26, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 12B (3-jährig, Teilzeit)
	 */
	public static readonly C11 : HerkunftBildungsgang = new HerkunftBildungsgang("C11", 27, );

	/**
	 * Berufsfachschule, Berufsabschluss/Fachhochschulreife (mit Berufspraktikum; 3,5 -jährig, Vollzeit)
	 */
	public static readonly C12 : HerkunftBildungsgang = new HerkunftBildungsgang("C12", 28, );

	/**
	 * Berufsabschluss/FHR (gestuft), (3jährig, Vollzeit)
	 */
	public static readonly C13 : HerkunftBildungsgang = new HerkunftBildungsgang("C13", 29, );

	/**
	 * Berufliches Gymnasium, Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum; 4-jährig, Vollzeit)
	 */
	public static readonly D01 : HerkunftBildungsgang = new HerkunftBildungsgang("D01", 30, );

	/**
	 * Berufliches Gymnasium, Berufliche Kenntnisse/Allg. Hochschulreife
	 */
	public static readonly D02 : HerkunftBildungsgang = new HerkunftBildungsgang("D02", 31, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 13 (1-jährig, Vollzeit)
	 */
	public static readonly D05 : HerkunftBildungsgang = new HerkunftBildungsgang("D05", 32, );

	/**
	 * Fachoberschule, Fachoberschule Kl. 13 (2-jährig, Teilzeit)
	 */
	public static readonly D06 : HerkunftBildungsgang = new HerkunftBildungsgang("D06", 33, );

	/**
	 * Fachschule (2-jährig, Vollzeit)
	 */
	public static readonly E01 : HerkunftBildungsgang = new HerkunftBildungsgang("E01", 34, );

	/**
	 * Fachschule (4-jährig, Teilzeit)
	 */
	public static readonly E02 : HerkunftBildungsgang = new HerkunftBildungsgang("E02", 35, );

	/**
	 * Fachschule (verkürzt/1-jährig, Vollzeit/Teilzeit)
	 */
	public static readonly E03 : HerkunftBildungsgang = new HerkunftBildungsgang("E03", 36, );

	/**
	 * Fachschule (verkürzt/2-jährig, Teilzeit)
	 */
	public static readonly E04 : HerkunftBildungsgang = new HerkunftBildungsgang("E04", 37, );

	/**
	 * Fachschule für Sozialwesen (mit Berufspraktikum/3-jährig, Vollzeit)
	 */
	public static readonly E05 : HerkunftBildungsgang = new HerkunftBildungsgang("E05", 38, );

	/**
	 * Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit)
	 */
	public static readonly E07 : HerkunftBildungsgang = new HerkunftBildungsgang("E07", 39, );

	/**
	 * Fachschule (3-jährig, Teilzeit)
	 */
	public static readonly E13 : HerkunftBildungsgang = new HerkunftBildungsgang("E13", 40, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		HerkunftBildungsgang.all_values_by_ordinal.push(this);
		HerkunftBildungsgang.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang>) : void {
		CoreTypeDataManager.putManager(HerkunftBildungsgang.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> {
		return CoreTypeDataManager.getManager(HerkunftBildungsgang.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftBildungsgang> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftBildungsgang | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : HerkunftBildungsgangKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<HerkunftBildungsgangKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.HerkunftBildungsgang';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schueler.HerkunftBildungsgang', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<HerkunftBildungsgang>('de.svws_nrw.asd.types.schueler.HerkunftBildungsgang');

}

export function cast_de_svws_nrw_asd_types_schueler_HerkunftBildungsgang(obj : unknown) : HerkunftBildungsgang {
	return obj as HerkunftBildungsgang;
}
