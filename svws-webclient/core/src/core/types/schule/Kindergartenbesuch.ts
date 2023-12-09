import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { KindergartenbesuchKatalogEintrag } from '../../../core/data/schule/KindergartenbesuchKatalogEintrag';

export class Kindergartenbesuch extends JavaEnum<Kindergartenbesuch> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Kindergartenbesuch> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Kindergartenbesuch> = new Map<string, Kindergartenbesuch>();

	/**
	 * Kein Kindergartenbesuch
	 */
	public static readonly KEINER : Kindergartenbesuch = new Kindergartenbesuch("KEINER", 0, [new KindergartenbesuchKatalogEintrag(1, 0, "kein Kindergarten", null, null)]);

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_1_JAHR : Kindergartenbesuch = new Kindergartenbesuch("MAX_1_JAHR", 1, [new KindergartenbesuchKatalogEintrag(2, 1, "unter 1 Jahr", null, null)]);

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_2_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_2_JAHRE", 2, [new KindergartenbesuchKatalogEintrag(3, 2, "1 bis unter 2 Jahre", null, null)]);

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_3_JAHRE", 3, [new KindergartenbesuchKatalogEintrag(4, 3, "2 bis unter 3 Jahre", null, null)]);

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MIN_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MIN_3_JAHRE", 4, [new KindergartenbesuchKatalogEintrag(5, 4, "3 Jahre und mehr Jahre", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Dauer des Kindergartenbesuchs
	 */
	public readonly daten : KindergartenbesuchKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Dauer des Kindergartenbesuchs
	 */
	public readonly historie : Array<KindergartenbesuchKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Dauern des Kindergartenbesuchs, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _mapKuerzel : HashMap<number, Kindergartenbesuch | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Eintrag in der Aufzählung.
	 *
	 * @param historie   die Historie der Eintrags, welche ein Array von
	 *                   {@link KindergartenbesuchKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KindergartenbesuchKatalogEintrag>) {
		super(name, ordinal);
		Kindergartenbesuch.all_values_by_ordinal.push(this);
		Kindergartenbesuch.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln auf den zugehörigen Core-Type-Wert.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf den zugehörigen Core-Type-Wert
	 */
	private static getMapByKuerzel() : HashMap<number, Kindergartenbesuch | null> {
		if (Kindergartenbesuch._mapKuerzel.size() === 0) {
			for (const s of Kindergartenbesuch.values()) {
				if (s.daten !== null)
					Kindergartenbesuch._mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return Kindergartenbesuch._mapKuerzel;
	}

	/**
	 * Gibt den Core-Type-Wert für das angegebe Kürzel der Dauer des Kindergartenbesuchs zurück.
	 *
	 * @param kuerzel   das Kürzel der Dauer
	 *
	 * @return der Core-Type-Wert oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : number) : Kindergartenbesuch | null {
		return Kindergartenbesuch.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Kindergartenbesuch> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Kindergartenbesuch | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Kindergartenbesuch', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Kindergartenbesuch(obj : unknown) : Kindergartenbesuch {
	return obj as Kindergartenbesuch;
}
