import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { BildungsgangTypKatalogEintrag } from '../../../core/data/schule/BildungsgangTypKatalogEintrag';

export class WeiterbildungskollegBildungsgangTyp extends JavaEnum<WeiterbildungskollegBildungsgangTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<WeiterbildungskollegBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, WeiterbildungskollegBildungsgangTyp> = new Map<string, WeiterbildungskollegBildungsgangTyp>();

	/**
	 * Abendgymnasium
	 */
	public static readonly ABENDGYMNASIUM : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("ABENDGYMNASIUM", 0, [new BildungsgangTypKatalogEintrag(1000, "AG", "Abendgymnasium", null, null)]);

	/**
	 * Abendrealschule
	 */
	public static readonly ABENDREALSCHULE : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("ABENDREALSCHULE", 1, [new BildungsgangTypKatalogEintrag(2000, "AR", "Abendrealschule", null, null)]);

	/**
	 * Kolleg
	 */
	public static readonly KOLLEG : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("KOLLEG", 2, [new BildungsgangTypKatalogEintrag(3000, "KL", "Kolleg", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Typen von Bildungsgängen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BildungsgangTypKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Typen von Bildungsgängen
	 */
	public readonly historie : Array<BildungsgangTypKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Typen von Bildungsgängen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, WeiterbildungskollegBildungsgangTyp> = new HashMap();

	/**
	 * Erzeugt einen neuen Typ von Bildungsgängen in der Aufzählung.
	 *
	 * @param historie   die Historie der Typen von Bildungsgängen, welches ein Array von
	 *                   {@link BildungsgangTypKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BildungsgangTypKatalogEintrag>) {
		super(name, ordinal);
		WeiterbildungskollegBildungsgangTyp.all_values_by_ordinal.push(this);
		WeiterbildungskollegBildungsgangTyp.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Typen von Bildungsgängen auf die
	 * zugehörigen Typen von Bildungsgängen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Typen auf die zugehörigen Typen von Bildungsgängen
	 */
	private static getMapByKuerzel() : HashMap<string, WeiterbildungskollegBildungsgangTyp> {
		if (WeiterbildungskollegBildungsgangTyp._ebenen.size() === 0) {
			for (const s of WeiterbildungskollegBildungsgangTyp.values()) {
				if (s.daten !== null)
					WeiterbildungskollegBildungsgangTyp._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return WeiterbildungskollegBildungsgangTyp._ebenen;
	}

	/**
	 * Gibt den Typ von Bildungsgängen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Typs von Bildungsgängen
	 *
	 * @return der Typ von Bildungsgängen oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : WeiterbildungskollegBildungsgangTyp | null {
		return WeiterbildungskollegBildungsgangTyp.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<WeiterbildungskollegBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : WeiterbildungskollegBildungsgangTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.WeiterbildungskollegBildungsgangTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.WeiterbildungskollegBildungsgangTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_WeiterbildungskollegBildungsgangTyp(obj : unknown) : WeiterbildungskollegBildungsgangTyp {
	return obj as WeiterbildungskollegBildungsgangTyp;
}
