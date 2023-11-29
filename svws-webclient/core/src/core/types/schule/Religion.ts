import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { ReligionKatalogEintrag } from '../../../core/data/schule/ReligionKatalogEintrag';

export class Religion extends JavaEnum<Religion> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Religion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Religion> = new Map<string, Religion>();

	/**
	 * Religion: alevitisch
	 */
	public static readonly AR : Religion = new Religion("AR", 0, [new ReligionKatalogEintrag(1000, "AR", "alevitisch", null, null)]);

	/**
	 * Religion: evangelisch
	 */
	public static readonly ER : Religion = new Religion("ER", 1, [new ReligionKatalogEintrag(2000, "ER", "evangelisch", null, null)]);

	/**
	 * Religion: jüdisch
	 */
	public static readonly HR : Religion = new Religion("HR", 2, [new ReligionKatalogEintrag(3000, "HR", "jüdisch", null, null)]);

	/**
	 * Religion: islamisch
	 */
	public static readonly IR : Religion = new Religion("IR", 3, [new ReligionKatalogEintrag(4000, "IR", "islamisch", null, null)]);

	/**
	 * Religion: katholisch
	 */
	public static readonly KR : Religion = new Religion("KR", 4, [new ReligionKatalogEintrag(5000, "KR", "katholisch", null, null)]);

	/**
	 * Religion: mennonitische BG NRW
	 */
	public static readonly ME : Religion = new Religion("ME", 5, [new ReligionKatalogEintrag(6000, "ME", "mennonitische BG NRW", null, null)]);

	/**
	 * Religion: ohne Bekenntnis
	 */
	public static readonly OH : Religion = new Religion("OH", 6, [new ReligionKatalogEintrag(7000, "OH", "ohne Bekenntnis", null, null)]);

	/**
	 * Religion: griechisch-orthodox
	 */
	public static readonly OR : Religion = new Religion("OR", 7, [new ReligionKatalogEintrag(8000, "OR", "griechisch-orthodox", null, null)]);

	/**
	 * Religion: syrisch-orthodox
	 */
	public static readonly SO : Religion = new Religion("SO", 8, [new ReligionKatalogEintrag(9000, "SO", "syrisch-orthodox", null, null)]);

	/**
	 * Religion: sonstige orthodoxe
	 */
	public static readonly XO : Religion = new Religion("XO", 9, [new ReligionKatalogEintrag(10000, "XO", "sonstige orthodoxe", null, null)]);

	/**
	 * Religion: andere Religionen
	 */
	public static readonly XR : Religion = new Religion("XR", 10, [new ReligionKatalogEintrag(11000, "XR", "andere Religionen", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Religionen
	 */
	public readonly daten : ReligionKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Religionen
	 */
	public readonly historie : Array<ReligionKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _mapKuerzel : HashMap<string, Religion | null> = new HashMap();

	/**
	 * Erzeugt eine neue Religion in der Aufzählung.
	 *
	 * @param historie   die Historie der Religionen, welche ein Array von
	 *                   {@link ReligionKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<ReligionKatalogEintrag>) {
		super(name, ordinal);
		Religion.all_values_by_ordinal.push(this);
		Religion.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Religionen auf die
	 * zugehörigen Religionen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Religionen
	 */
	private static getMapByKuerzel() : HashMap<string, Religion | null> {
		if (Religion._mapKuerzel.size() === 0) {
			for (const s of Religion.values()) {
				if (s.daten !== null)
					Religion._mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return Religion._mapKuerzel;
	}

	/**
	 * Gibt die Religion für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Religion
	 *
	 * @return die Religion oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Religion | null {
		return Religion.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Religion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Religion | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Religion', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Religion(obj : unknown) : Religion {
	return obj as Religion;
}
