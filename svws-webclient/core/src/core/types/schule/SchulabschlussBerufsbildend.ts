import { JavaEnum } from '../../../java/lang/JavaEnum';
import { SchulabschlussBerufsbildendKatalogEintrag } from '../../../core/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class SchulabschlussBerufsbildend extends JavaEnum<SchulabschlussBerufsbildend> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchulabschlussBerufsbildend> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchulabschlussBerufsbildend> = new Map<string, SchulabschlussBerufsbildend>();

	/**
	 * Es liegt kein Abschluss vor
	 */
	public static readonly OA : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("OA", 0, [new SchulabschlussBerufsbildendKatalogEintrag(0, "OA", "Ohne Abschluss", "0", null, null)]);

	/**
	 * Abschluss der Ausbildungsvorbereitung
	 */
	public static readonly VORB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VORB", 1, [new SchulabschlussBerufsbildendKatalogEintrag(100000, "VORB", "Abschluss der Ausbildungsvorbereitung", "1", null, null)]);

	/**
	 * Versetzungszeugnis
	 */
	public static readonly VERS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VERS", 2, [new SchulabschlussBerufsbildendKatalogEintrag(110000, "VERS", "Versetzungszeugnis", "1", null, null)]);

	/**
	 * Abschlusszeugnis in Aufbaubildungsgängen
	 */
	public static readonly AUFB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("AUFB", 3, [new SchulabschlussBerufsbildendKatalogEintrag(120000, "AUFB", "Abschlusszeugnis in Aufbaubildungsgängen", "1", null, null)]);

	/**
	 * Abschluss der Berufschulvorbereitung
	 */
	public static readonly BV : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BV", 4, [new SchulabschlussBerufsbildendKatalogEintrag(130000, "BV", "Abschluss der Berufschulvorbereitung", "1", null, null)]);

	/**
	 * Vorpraktikum
	 */
	public static readonly VP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VP", 5, [new SchulabschlussBerufsbildendKatalogEintrag(140000, "VP", "Vorpraktikum", "1", null, null)]);

	/**
	 * Vorpraktikum
	 */
	public static readonly BP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BP", 6, [new SchulabschlussBerufsbildendKatalogEintrag(150000, "BP", "Berufspraktikum", "1", null, null)]);

	/**
	 * Abschluss der Berufschulgrundjahres
	 */
	public static readonly BG : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BG", 7, [new SchulabschlussBerufsbildendKatalogEintrag(200000, "BG", "Abschluss der Berufschulgrundjahres", "2", null, null)]);

	/**
	 * Berufschulabschluss
	 */
	public static readonly BS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BS", 8, [new SchulabschlussBerufsbildendKatalogEintrag(300000, "BS", "Berufschulabschluss", "3", null, null)]);

	/**
	 * Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly BK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BK", 9, [new SchulabschlussBerufsbildendKatalogEintrag(400000, "BK", "Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "4", null, null)]);

	/**
	 * Berufsabschluss
	 */
	public static readonly BAB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BAB", 10, [new SchulabschlussBerufsbildendKatalogEintrag(500000, "BAB", "Berufsabschluss", "5", null, null)]);

	/**
	 * Fachschulabschluss (berufliche Weiterbildung)
	 */
	public static readonly BW : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BW", 11, [new SchulabschlussBerufsbildendKatalogEintrag(600000, "BW", "Fachschulabschluss (berufliche Weiterbildung)", "6", null, null)]);

	/**
	 * Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly VBK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VBK", 12, [new SchulabschlussBerufsbildendKatalogEintrag(800000, "VBK", "Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "8", null, null)]);

	/**
	 * Pseudoabschluss: Schulwechsler, die im selben Bildungsgang verbleiben
	 */
	public static readonly WECHSEL : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("WECHSEL", 13, [new SchulabschlussBerufsbildendKatalogEintrag(900000, "WECHSEL", "Schulwechsler, die im selben Bildungsgang verbleiben", "9", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 2;

	/**
	 * Der aktuellen Daten der Abschlussart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : SchulabschlussBerufsbildendKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Abschlussarten
	 */
	public readonly historie : Array<SchulabschlussBerufsbildendKatalogEintrag>;

	/**
	 * Eine HashMap mit den Abschlussarten, welche ihren Kürzeln zugeordnet werden
	 */
	private static readonly _mapByKuerzel : HashMap<string, SchulabschlussBerufsbildend> = new HashMap();

	/**
	 * Eine HashMap mit den Abschlussarten, welche ihren Statistik-Kürzeln zugeordnet werden
	 */
	private static readonly _mapByKuerzelStatistik : HashMap<string, SchulabschlussBerufsbildend> = new HashMap();

	/**
	 * Erzeugt eine neue Abschlussart in der Aufzählung.
	 *
	 * @param historie   die Historie der Abschlussarten, welches ein Array von {@link SchulabschlussBerufsbildendKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<SchulabschlussBerufsbildendKatalogEintrag>) {
		super(name, ordinal);
		SchulabschlussBerufsbildend.all_values_by_ordinal.push(this);
		SchulabschlussBerufsbildend.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static getMapByKuerzel() : HashMap<string, SchulabschlussBerufsbildend> {
		if (SchulabschlussBerufsbildend._mapByKuerzel.size() === 0) {
			for (const s of SchulabschlussBerufsbildend.values()) {
				if (s.daten !== null)
					SchulabschlussBerufsbildend._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return SchulabschlussBerufsbildend._mapByKuerzel;
	}

	/**
	 * Gibt die Abschlussart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : SchulabschlussBerufsbildend | null {
		return SchulabschlussBerufsbildend.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt eine Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static getMapByKuerzelStatistik() : HashMap<string, SchulabschlussBerufsbildend> {
		if (SchulabschlussBerufsbildend._mapByKuerzelStatistik.size() === 0) {
			for (const s of SchulabschlussBerufsbildend.values()) {
				if (s.daten !== null)
					SchulabschlussBerufsbildend._mapByKuerzelStatistik.put(s.daten.kuerzelStatistik, s);
			}
		}
		return SchulabschlussBerufsbildend._mapByKuerzelStatistik;
	}

	/**
	 * Gibt die Abschlussart für das angegebene Statistik-Kürzel zurück.
	 *
	 * @param kuerzel   das Statistik-Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Statistik-Kürzel ungültig ist
	 */
	public static getByKuerzelStatistik(kuerzel : string | null) : SchulabschlussBerufsbildend | null {
		return SchulabschlussBerufsbildend.getMapByKuerzelStatistik().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchulabschlussBerufsbildend> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchulabschlussBerufsbildend | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_SchulabschlussBerufsbildend(obj : unknown) : SchulabschlussBerufsbildend {
	return obj as SchulabschlussBerufsbildend;
}
