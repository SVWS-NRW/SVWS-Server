import { JavaObject } from '../../../java/lang/JavaObject';
import { SchulabschlussBerufsbildendKatalogEintrag } from '../../../core/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class SchulabschlussBerufsbildend extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<SchulabschlussBerufsbildend> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, SchulabschlussBerufsbildend> = new Map<string, SchulabschlussBerufsbildend>();

	/**
	 * Es liegt kein Abschluss vor
	 */
	public static readonly OA : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("OA", 0, [new SchulabschlussBerufsbildendKatalogEintrag(0, "OA", "Ohne Abschluss", "0", null, null)]);

	/**
	 * Abschluss der Ausbildungsvorbereitung
	 */
	public static readonly VORB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VORB", 1, [new SchulabschlussBerufsbildendKatalogEintrag(10, "VORB", "Abschluss der Ausbildungsvorbereitung", "1", null, null)]);

	/**
	 * Versetzungszeugnis
	 */
	public static readonly VERS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VERS", 2, [new SchulabschlussBerufsbildendKatalogEintrag(11, "VERS", "Versetzungszeugnis", "1", null, null)]);

	/**
	 * Abschlusszeugnis in Aufbaubildungsgängen
	 */
	public static readonly AUFB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("AUFB", 3, [new SchulabschlussBerufsbildendKatalogEintrag(12, "AUFB", "Abschlusszeugnis in Aufbaubildungsgängen", "1", null, null)]);

	/**
	 * Abschluss der Berufschulvorbereitung
	 */
	public static readonly BV : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BV", 4, [new SchulabschlussBerufsbildendKatalogEintrag(13, "BV", "Abschluss der Berufschulvorbereitung", "1", null, null)]);

	/**
	 * Vorpraktikum
	 */
	public static readonly VP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VP", 5, [new SchulabschlussBerufsbildendKatalogEintrag(14, "VP", "Vorpraktikum", "1", null, null)]);

	/**
	 * Vorpraktikum
	 */
	public static readonly BP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BP", 6, [new SchulabschlussBerufsbildendKatalogEintrag(15, "BP", "Berufspraktikum", "1", null, null)]);

	/**
	 * Abschluss der Berufschulgrundjahres
	 */
	public static readonly BG : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BG", 7, [new SchulabschlussBerufsbildendKatalogEintrag(20, "BG", "Abschluss der Berufschulgrundjahres", "2", null, null)]);

	/**
	 * Berufschulabschluss
	 */
	public static readonly BS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BS", 8, [new SchulabschlussBerufsbildendKatalogEintrag(30, "BS", "Berufschulabschluss", "3", null, null)]);

	/**
	 * Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly BK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BK", 9, [new SchulabschlussBerufsbildendKatalogEintrag(40, "BK", "Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "4", null, null)]);

	/**
	 * Berufsabschluss
	 */
	public static readonly BAB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BAB", 10, [new SchulabschlussBerufsbildendKatalogEintrag(50, "BAB", "Berufsabschluss", "5", null, null)]);

	/**
	 * Fachschulabschluss (berufliche Weiterbildung)
	 */
	public static readonly BW : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BW", 11, [new SchulabschlussBerufsbildendKatalogEintrag(60, "BW", "Fachschulabschluss (berufliche Weiterbildung)", "6", null, null)]);

	/**
	 * Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly VBK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VBK", 12, [new SchulabschlussBerufsbildendKatalogEintrag(80, "VBK", "Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten", "8", null, null)]);

	/**
	 * Pseudoabschluss: Schulwechsler, die im selben Bildungsgang verbleiben
	 */
	public static readonly WECHSEL : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("WECHSEL", 13, [new SchulabschlussBerufsbildendKatalogEintrag(90, "WECHSEL", "Schulwechsler, die im selben Bildungsgang verbleiben", "9", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

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
		super();
		this.__name = name;
		this.__ordinal = ordinal;
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
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof SchulabschlussBerufsbildend))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : SchulabschlussBerufsbildend) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : SchulabschlussBerufsbildend | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.SchulabschlussBerufsbildend'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_SchulabschlussBerufsbildend(obj : unknown) : SchulabschlussBerufsbildend {
	return obj as SchulabschlussBerufsbildend;
}
