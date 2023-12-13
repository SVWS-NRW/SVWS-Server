import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { BildungsgangTypKatalogEintrag } from '../../../core/data/schule/BildungsgangTypKatalogEintrag';

export class BerufskollegBildungsgangTyp extends JavaEnum<BerufskollegBildungsgangTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BerufskollegBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BerufskollegBildungsgangTyp> = new Map<string, BerufskollegBildungsgangTyp>();

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSFACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSFACHSCHULE", 0, [new BildungsgangTypKatalogEintrag(1000, "BF", "Berufsfachschulen", null, null)]);

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSSCHULE", 1, [new BildungsgangTypKatalogEintrag(2000, "BS", "Berufsschule", null, null)]);

	/**
	 * Berufliches Gymnasium
	 */
	public static readonly BERUFLICHES_GYMNASIUM : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFLICHES_GYMNASIUM", 2, [new BildungsgangTypKatalogEintrag(3000, "BY", "Berufliches Gymnasium", null, null)]);

	/**
	 * Fachoberschule
	 */
	public static readonly FACHOBERSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHOBERSCHULE", 3, [new BildungsgangTypKatalogEintrag(4000, "FO", "Fachoberschule", null, null)]);

	/**
	 * Fachschule
	 */
	public static readonly FACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHSCHULE", 4, [new BildungsgangTypKatalogEintrag(5000, "FS", "Fachschule", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Berufsschultypen von Bildungsgängen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BildungsgangTypKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Berufsschultypen von Bildungsgängen
	 */
	public readonly historie : Array<BildungsgangTypKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Berufsschultypen von Bildungsgängen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, BerufskollegBildungsgangTyp> = new HashMap();

	/**
	 * Erzeugt einen neuen Berufsschultyp von Bildungsgängen in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsschultypen von Bildungsgängen, welches ein Array von
	 *                   {@link BildungsgangTypKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BildungsgangTypKatalogEintrag>) {
		super(name, ordinal);
		BerufskollegBildungsgangTyp.all_values_by_ordinal.push(this);
		BerufskollegBildungsgangTyp.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Berufsschultypen von Bildungsgängen auf die
	 * zugehörigen Berufsschultypen von Bildungsgängen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Typen auf die zugehörigen Berufsschultypen von Bildungsgängen
	 */
	private static getMapByKuerzel() : HashMap<string, BerufskollegBildungsgangTyp> {
		if (BerufskollegBildungsgangTyp._ebenen.size() === 0) {
			for (const s of BerufskollegBildungsgangTyp.values()) {
				if (s.daten !== null)
					BerufskollegBildungsgangTyp._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBildungsgangTyp._ebenen;
	}

	/**
	 * Gibt den Berufsschultyp von Bildungsgängen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Berufsschultyps von Bildungsgängen
	 *
	 * @return der Berufsschultyp von Bildungsgängen oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegBildungsgangTyp | null {
		return BerufskollegBildungsgangTyp.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBildungsgangTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_BerufskollegBildungsgangTyp(obj : unknown) : BerufskollegBildungsgangTyp {
	return obj as BerufskollegBildungsgangTyp;
}
