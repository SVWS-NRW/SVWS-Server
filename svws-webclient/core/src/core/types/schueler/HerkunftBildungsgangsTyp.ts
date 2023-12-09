import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { BerufskollegBildungsgangTyp } from '../../../core/types/schule/BerufskollegBildungsgangTyp';
import { HerkunftBildungsgangTypKatalogEintrag } from '../../../core/data/schule/HerkunftBildungsgangTypKatalogEintrag';
import { WeiterbildungskollegBildungsgangTyp } from '../../../core/types/schule/WeiterbildungskollegBildungsgangTyp';

export class HerkunftBildungsgangsTyp extends JavaEnum<HerkunftBildungsgangsTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<HerkunftBildungsgangsTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, HerkunftBildungsgangsTyp> = new Map<string, HerkunftBildungsgangsTyp>();

	/**
	 * Weiterbildungskolleg: Abendgymnasium
	 */
	public static readonly AG : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("AG", 0, [new HerkunftBildungsgangTypKatalogEintrag(1000, WeiterbildungskollegBildungsgangTyp.ABENDGYMNASIUM, null, null, null)]);

	/**
	 * Weiterbildungskolleg: Abendrealschule
	 */
	public static readonly AR : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("AR", 1, [new HerkunftBildungsgangTypKatalogEintrag(2000, WeiterbildungskollegBildungsgangTyp.ABENDREALSCHULE, null, null, null)]);

	/**
	 * Weiterbildungskolleg: Abendrealschule
	 */
	public static readonly KL : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("KL", 2, [new HerkunftBildungsgangTypKatalogEintrag(3000, WeiterbildungskollegBildungsgangTyp.KOLLEG, null, null, null)]);

	/**
	 * Berufskolleg: Berufsfachschule
	 */
	public static readonly BF : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BF", 3, [new HerkunftBildungsgangTypKatalogEintrag(10000, null, BerufskollegBildungsgangTyp.BERUFSFACHSCHULE, null, null)]);

	/**
	 * Berufskolleg: Berufschule
	 */
	public static readonly BS : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BS", 4, [new HerkunftBildungsgangTypKatalogEintrag(11000, null, BerufskollegBildungsgangTyp.BERUFSSCHULE, null, null)]);

	/**
	 * Berufskolleg: Berufliches Gymnasium
	 */
	public static readonly BY : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BY", 5, [new HerkunftBildungsgangTypKatalogEintrag(12000, null, BerufskollegBildungsgangTyp.BERUFLICHES_GYMNASIUM, null, null)]);

	/**
	 * Berufskolleg: Fachoberschule
	 */
	public static readonly FO : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("FO", 6, [new HerkunftBildungsgangTypKatalogEintrag(13000, null, BerufskollegBildungsgangTyp.FACHOBERSCHULE, null, null)]);

	/**
	 * Berufskolleg: Fachschule
	 */
	public static readonly FS : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("FS", 7, [new HerkunftBildungsgangTypKatalogEintrag(14000, null, BerufskollegBildungsgangTyp.FACHSCHULE, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Bildungsgangtyps, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftBildungsgangTypKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Bildungsgangtyps
	 */
	public readonly historie : Array<HerkunftBildungsgangTypKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Bildungsgangtypen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, HerkunftBildungsgangsTyp | null> = new HashMap();

	/**
	 * Erzeugt eine neuen Herkunfts-Bildungsgangtyp in der Aufzählung.
	 *
	 * @param historie   die Historie des Herkunfts-Bildungsgangtyps, welche ein Array von
	 *                   {@link HerkunftBildungsgangTypKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftBildungsgangTypKatalogEintrag>) {
		super(name, ordinal);
		HerkunftBildungsgangsTyp.all_values_by_ordinal.push(this);
		HerkunftBildungsgangsTyp.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Bildungsgangtypen auf die
	 * zugehörigen Bildungsgangtypen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Bildungsgangtypen
	 */
	private static getMapByKuerzel() : HashMap<string, HerkunftBildungsgangsTyp | null> {
		if (HerkunftBildungsgangsTyp._ebenen.size() === 0) {
			for (const s of HerkunftBildungsgangsTyp.values()) {
				if (s.daten !== null)
					HerkunftBildungsgangsTyp._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return HerkunftBildungsgangsTyp._ebenen;
	}

	/**
	 * Gibt den Herkunfts-Bildungsgangtypen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Herkunfts-Bildungsgangtyps
	 *
	 * @return der Herkunfts-Bildungsgangtyp oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : HerkunftBildungsgangsTyp | null {
		return HerkunftBildungsgangsTyp.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftBildungsgangsTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftBildungsgangsTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.HerkunftBildungsgangsTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schueler_HerkunftBildungsgangsTyp(obj : unknown) : HerkunftBildungsgangsTyp {
	return obj as HerkunftBildungsgangsTyp;
}
