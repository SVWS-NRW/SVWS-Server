import { JavaEnum } from '../../../java/lang/JavaEnum';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { FoerderschwerpunktKatalogEintrag } from '../../../asd/data/schule/FoerderschwerpunktKatalogEintrag';
import type { JavaMap } from '../../../java/util/JavaMap';

export class Foerderschwerpunkt extends JavaEnum<Foerderschwerpunkt> implements CoreType<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Foerderschwerpunkt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Foerderschwerpunkt> = new Map<string, Foerderschwerpunkt>();

	/**
	 * Förderschwerpunkt - kein Förderschwerpunkt
	 */
	public static readonly KEINER : Foerderschwerpunkt = new Foerderschwerpunkt("KEINER", 0, );

	/**
	 * Förderschwerpunkt - Sehen (Blinde)
	 */
	public static readonly BL : Foerderschwerpunkt = new Foerderschwerpunkt("BL", 1, );

	/**
	 * Förderschwerpunkt - Emotionale und soziale Entwicklung
	 */
	public static readonly EZ : Foerderschwerpunkt = new Foerderschwerpunkt("EZ", 2, );

	/**
	 * Förderschwerpunkt - Geistige Entwicklung
	 */
	public static readonly GB : Foerderschwerpunkt = new Foerderschwerpunkt("GB", 3, );

	/**
	 * Förderschwerpunkt - Hören und Kommunikation (Gehörlose)
	 */
	public static readonly GH : Foerderschwerpunkt = new Foerderschwerpunkt("GH", 4, );

	/**
	 * Förderschwerpunkt - Körperliche und motorische Entwicklung
	 */
	public static readonly KB : Foerderschwerpunkt = new Foerderschwerpunkt("KB", 5, );

	/**
	 * Förderschwerpunkt - Schule für Kranke
	 */
	public static readonly KR : Foerderschwerpunkt = new Foerderschwerpunkt("KR", 6, );

	/**
	 * Förderschwerpunkt - Lernen
	 */
	public static readonly LB : Foerderschwerpunkt = new Foerderschwerpunkt("LB", 7, );

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Emotionale und soziale Entwicklung
	 */
	public static readonly PE : Foerderschwerpunkt = new Foerderschwerpunkt("PE", 8, );

	/**
	 * Förderschwerpunkt - Präventive Förderung
	 */
	public static readonly PF : Foerderschwerpunkt = new Foerderschwerpunkt("PF", 9, );

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Lernen
	 */
	public static readonly PL : Foerderschwerpunkt = new Foerderschwerpunkt("PL", 10, );

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Sprache
	 */
	public static readonly PS : Foerderschwerpunkt = new Foerderschwerpunkt("PS", 11, );

	/**
	 * Förderschwerpunkt - Sprache
	 */
	public static readonly SB : Foerderschwerpunkt = new Foerderschwerpunkt("SB", 12, );

	/**
	 * Förderschwerpunkt - Hören und Kommunikation (Schwerhörige)
	 */
	public static readonly SG : Foerderschwerpunkt = new Foerderschwerpunkt("SG", 13, );

	/**
	 * Förderschwerpunkt - Sehen (Sehbehinderte)
	 */
	public static readonly SH : Foerderschwerpunkt = new Foerderschwerpunkt("SH", 14, );

	/**
	 * Förderschwerpunkt - Kein Förderschwerpunkt
	 */
	public static readonly XX : Foerderschwerpunkt = new Foerderschwerpunkt("XX", 15, );

	/**
	 * Eine Map, welche die Menge der erlaubten Schulformen den IDs der Förderschwerpunkt-Einträge zuordnet.
	 */
	private static readonly _mapSchulformenByID : HashMap<number, JavaSet<Schulform>> = new HashMap<number, JavaSet<Schulform>>();

	private static readonly _mapFoerderschwerpunkteBySchuljahrAndSchulform : JavaMap<number, JavaMap<Schulform, List<Foerderschwerpunkt>>> = new HashMap<number, JavaMap<Schulform, List<Foerderschwerpunkt>>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Foerderschwerpunkt.all_values_by_ordinal.push(this);
		Foerderschwerpunkt.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt>) : void {
		CoreTypeDataManager.putManager(Foerderschwerpunkt.class, manager);
		for (const ct of Foerderschwerpunkt.data().getWerte())
			for (const e of ct.historie())
				Foerderschwerpunkt._mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> {
		return CoreTypeDataManager.getManager(Foerderschwerpunkt.class);
	}

	/**
	 * Prüft, ob der Förderschwerpunkt in dem angebenen Schuljahr bei der angegeben Schulform zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return true, wenn er zulässig ist uns ansonsten false
	 */
	public hatSchulform(schuljahr : number, schulform : Schulform | null) : boolean {
		const fske : FoerderschwerpunktKatalogEintrag | null = this.daten(schuljahr);
		if (fske === null)
			return false;
		const schulformen : JavaSet<Schulform> | null = Foerderschwerpunkt._mapSchulformenByID.get(fske.id);
		return (schulformen !== null) && (schulformen.contains(schulform));
	}

	/**
	 * Liefert alle zulässigen Förderschwerpunkte für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Förderschwerpunkte
	 */
	public static getBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<Foerderschwerpunkt> {
		const mapFoerderschwerpunkteBySchulformOfSchuljahr : JavaMap<Schulform, List<Foerderschwerpunkt>> | null = Foerderschwerpunkt._mapFoerderschwerpunkteBySchuljahrAndSchulform.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<Schulform, List<Foerderschwerpunkt>>() });
		if (mapFoerderschwerpunkteBySchulformOfSchuljahr === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<Foerderschwerpunkt> | null = mapFoerderschwerpunkteBySchulformOfSchuljahr.get(schulform);
		if (result === null) {
			result = new ArrayList();
			const schwerpunkte : List<Foerderschwerpunkt> | null = Foerderschwerpunkt.data().getWerteBySchuljahr(schuljahr);
			for (const schwerpunkt of schwerpunkte)
				if (schwerpunkt.hatSchulform(schuljahr, schulform))
					result.add(schwerpunkt);
			mapFoerderschwerpunkteBySchulformOfSchuljahr.put(schulform, result);
		}
		return result;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Foerderschwerpunkt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Foerderschwerpunkt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : FoerderschwerpunktKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<FoerderschwerpunktKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Foerderschwerpunkt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.Foerderschwerpunkt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Foerderschwerpunkt>('de.svws_nrw.asd.types.schule.Foerderschwerpunkt');

}

export function cast_de_svws_nrw_asd_types_schule_Foerderschwerpunkt(obj : unknown) : Foerderschwerpunkt {
	return obj as Foerderschwerpunkt;
}
