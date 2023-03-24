import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { SchulabschlussAllgemeinbildendKatalogEintrag } from '../../../core/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag';

export class SchulabschlussAllgemeinbildend extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<SchulabschlussAllgemeinbildend> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, SchulabschlussAllgemeinbildend> = new Map<string, SchulabschlussAllgemeinbildend>();

	/**
	 * Es liegt kein Abschluss vor
	 */
	public static readonly OA : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("OA", 0, [new SchulabschlussAllgemeinbildendKatalogEintrag(0, "OA", "Ohne Abschluss", "A", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	public static readonly HA9A : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9A", 1, [new SchulabschlussAllgemeinbildendKatalogEintrag(1, "HA9A", "Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B)", "B", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	public static readonly HA9 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9", 2, [new SchulabschlussAllgemeinbildendKatalogEintrag(2, "HA9", "Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)", "C", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen) - siehe BK-Bildungsgang A12
	 */
	public static readonly HA9_FOE : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9_FOE", 3, [new SchulabschlussAllgemeinbildendKatalogEintrag(3, "HA9_FOE", "Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen)", "S", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 9 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe
	 */
	public static readonly HA9_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA9_Q", 4, [new SchulabschlussAllgemeinbildendKatalogEintrag(4, "HA9_Q", "Hauptschulabschluss nach Klasse 9 (mit der Berechtigung zum Besuch der Gymnasialen Oberstufe)", "O", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 10
	 */
	public static readonly HA10 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA10", 5, [new SchulabschlussAllgemeinbildendKatalogEintrag(5, "HA10", "Hauptschulabschluss nach Klasse 10", "D", null, null)]);

	/**
	 * Hauptschulabschluss nach Klasse 10 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe
	 */
	public static readonly HA10_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("HA10_Q", 6, [new SchulabschlussAllgemeinbildendKatalogEintrag(6, "HA10_Q", "Hauptschulabschluss nach Klasse 10 (mit der Berechtigung zum Besuch der Gymnasialen Oberstufe)", "U", null, null)]);

	/**
	 * Der Mittlere Schulabschluss bzw. Fachoberschulreife
	 */
	public static readonly MSA : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA", 7, [new SchulabschlussAllgemeinbildendKatalogEintrag(10, "MSA", "Mittlerer Schulabschluss", "F", null, null)]);

	/**
	 * Der Mittlere Schulabschluss mit der Berechtigung zum Besuch Gymnasialen Oberstufe
	 */
	public static readonly MSA_Q : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA_Q", 8, [new SchulabschlussAllgemeinbildendKatalogEintrag(11, "MSA_Q", "Mittlerer Schulabschluss (mit der Berechtigung zum Besuch Gymnasialen Oberstufe)", "G", null, null)]);

	/**
	 * Der Mittlere Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase der Gymnasialen Oberstufe
	 */
	public static readonly MSA_Q1 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("MSA_Q1", 9, [new SchulabschlussAllgemeinbildendKatalogEintrag(12, "MSA_Q1", "Mittlerer Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase Gymnasialen Oberstufe", "I", null, null)]);

	/**
	 * Versetzung in die Klasse 11 der Fachoberschule (BK)
	 */
	public static readonly VS_11 : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("VS_11", 10, [new SchulabschlussAllgemeinbildendKatalogEintrag(13, "VS_11", "Versetzung in die Klasse 11 der Fachoberschule (BK)", "P", null, null)]);

	/**
	 * Fachhochschulreife (nur schulischer Teil)
	 */
	public static readonly FHR_S : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FHR_S", 11, [new SchulabschlussAllgemeinbildendKatalogEintrag(20, "FHR_S", "Fachhochschulreife (nur schulischer Teil)", "H", null, null)]);

	/**
	 * Fachhochschulreife
	 */
	public static readonly FHR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FHR", 12, [new SchulabschlussAllgemeinbildendKatalogEintrag(21, "FHR", "Fachhochschulreife", "J", null, null)]);

	/**
	 * fachgebundene Hochschulreife (BK)
	 */
	public static readonly FGHR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FGHR", 13, [new SchulabschlussAllgemeinbildendKatalogEintrag(22, "FGHR", "fachgebundene Hochschulreife (BK)", "Q", null, null)]);

	/**
	 * Abitur / Allgemeine Hochschulreife
	 */
	public static readonly ABITUR : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("ABITUR", 14, [new SchulabschlussAllgemeinbildendKatalogEintrag(30, "ABITUR", "Abitur / Allgemeine Hochschulreife", "K", null, null)]);

	/**
	 * Förderschule (Förderschwerpunkt geistige Entwicklung)
	 */
	public static readonly FOEG : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FOEG", 15, [new SchulabschlussAllgemeinbildendKatalogEintrag(40, "FOEG", "Förderschule (Förderschwerpunkt geistige Entwicklung)", "M", null, null)]);

	/**
	 * Förderschule (Förderschwerpunkt Lernen)
	 */
	public static readonly FOEL : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("FOEL", 16, [new SchulabschlussAllgemeinbildendKatalogEintrag(41, "FOEL", "Förderschule (Förderschwerpunkt Lernen)", "V", null, null)]);

	/**
	 * Waldorfschule
	 */
	public static readonly WALD : SchulabschlussAllgemeinbildend = new SchulabschlussAllgemeinbildend("WALD", 17, [new SchulabschlussAllgemeinbildendKatalogEintrag(50, "WALD", "Zeugnis der Waldorfschule", "W", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Abschlussart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : SchulabschlussAllgemeinbildendKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Abschlussarten
	 */
	public readonly historie : Array<SchulabschlussAllgemeinbildendKatalogEintrag>;

	/**
	 * Eine HashMap mit den Abschlussarten, welche ihren Kürzeln zugeordnet werden
	 */
	private static readonly _mapByKuerzel : HashMap<string, SchulabschlussAllgemeinbildend> = new HashMap();

	/**
	 * Eine HashMap mit den Abschlussarten, welche ihren Statistik-Kürzeln zugeordnet werden
	 */
	private static readonly _mapByKuerzelStatistik : HashMap<string, SchulabschlussAllgemeinbildend> = new HashMap();

	/**
	 * Erzeugt eine neue Abschlussart in der Aufzählung.
	 *
	 * @param historie   die Historie der Abschlussarten, welches ein Array von {@link SchulabschlussAllgemeinbildendKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<SchulabschlussAllgemeinbildendKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		SchulabschlussAllgemeinbildend.all_values_by_ordinal.push(this);
		SchulabschlussAllgemeinbildend.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static getMapByKuerzel() : HashMap<string, SchulabschlussAllgemeinbildend> {
		if (SchulabschlussAllgemeinbildend._mapByKuerzel.size() === 0) {
			for (const s of SchulabschlussAllgemeinbildend.values()) {
				if (s.daten !== null)
					SchulabschlussAllgemeinbildend._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return SchulabschlussAllgemeinbildend._mapByKuerzel;
	}

	/**
	 * Gibt die Abschlussart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : SchulabschlussAllgemeinbildend | null {
		return SchulabschlussAllgemeinbildend.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt eine Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Statistik-Kürzeln der Abschlussarten auf die zugehörigen Abschlussarten
	 */
	private static getMapByKuerzelStatistik() : HashMap<string, SchulabschlussAllgemeinbildend> {
		if (SchulabschlussAllgemeinbildend._mapByKuerzelStatistik.size() === 0) {
			for (const s of SchulabschlussAllgemeinbildend.values()) {
				if (s.daten !== null)
					SchulabschlussAllgemeinbildend._mapByKuerzelStatistik.put(s.daten.kuerzelStatistik, s);
			}
		}
		return SchulabschlussAllgemeinbildend._mapByKuerzelStatistik;
	}

	/**
	 * Gibt die Abschlussart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Statistik-Kürzel der Abschlussart
	 *
	 * @return die Abschlussart oder null, falls das Statistik-Kürzel ungültig ist
	 */
	public static getByKuerzelStatistik(kuerzel : string | null) : SchulabschlussAllgemeinbildend | null {
		return SchulabschlussAllgemeinbildend.getMapByKuerzelStatistik().get(kuerzel);
	}

	/**
	 * Prüft, ob dieser Abschluss dem im String-Parameter str übergebenen
	 * Abschluss entspricht.
	 *
	 * @param str   der Name des Abschlusses für den Vergleich als String
	 *
	 * @return true, falls beide Abschlüsse übereinstimmen und ansonsten false
	 */
	public is(str : string | null) : boolean {
		if (str === null)
			return false;
		try {
			const other : SchulabschlussAllgemeinbildend | null = SchulabschlussAllgemeinbildend.valueOf(str);
			return JavaObject.equalsTranspiler(this, (other));
		} catch(e) {
			return false;
		}
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
		if (!(other instanceof SchulabschlussAllgemeinbildend))
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
	public compareTo(other : SchulabschlussAllgemeinbildend) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchulabschlussAllgemeinbildend> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchulabschlussAllgemeinbildend | null {
		const tmp : SchulabschlussAllgemeinbildend | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_SchulabschlussAllgemeinbildend(obj : unknown) : SchulabschlussAllgemeinbildend {
	return obj as SchulabschlussAllgemeinbildend;
}
