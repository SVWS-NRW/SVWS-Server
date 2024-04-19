import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { BerufskollegAnlageKatalogEintrag } from '../../../core/data/schule/BerufskollegAnlageKatalogEintrag';

export class BerufskollegAnlage extends JavaEnum<BerufskollegAnlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BerufskollegAnlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BerufskollegAnlage> = new Map<string, BerufskollegAnlage>();

	/**
	 * Anlage A: Fachklassen duales System und Ausbildungsvorbereitung
	 */
	public static readonly A : BerufskollegAnlage = new BerufskollegAnlage("A", 0, [new BerufskollegAnlageKatalogEintrag(1000, "A", "Fachklassen duales System und Ausbildungsvorbereitung", null, null)]);

	/**
	 * Anlage B: Berufsfachschule
	 */
	public static readonly B : BerufskollegAnlage = new BerufskollegAnlage("B", 1, [new BerufskollegAnlageKatalogEintrag(2000, "B", "Berufsfachschule", null, null)]);

	/**
	 * Anlage C: Berufsfachschule und Fachoberschule
	 */
	public static readonly C : BerufskollegAnlage = new BerufskollegAnlage("C", 2, [new BerufskollegAnlageKatalogEintrag(3000, "C", "Berufsfachschule und Fachoberschule", null, null)]);

	/**
	 * Anlage D: Berufliches Gymnasium und Fachoberschule
	 */
	public static readonly D : BerufskollegAnlage = new BerufskollegAnlage("D", 3, [new BerufskollegAnlageKatalogEintrag(4000, "D", "Berufliches Gymnasium und Fachoberschule", null, null)]);

	/**
	 * Anlage E: Fachschule
	 */
	public static readonly E : BerufskollegAnlage = new BerufskollegAnlage("E", 4, [new BerufskollegAnlageKatalogEintrag(5000, "E", "Fachschule", null, null)]);

	/**
	 * Anlage H: Berufsgrundbildung und Berufsausbildung an einer freien Waldorfschule / Hiberniakolleg
	 */
	public static readonly H : BerufskollegAnlage = new BerufskollegAnlage("H", 5, [new BerufskollegAnlageKatalogEintrag(6000, "H", "Bildungsgänge an freien Waldorfschulen / Hiberniakolleg", null, null)]);

	/**
	 * Anlage X: Ehemalige Kollegschule
	 */
	public static readonly X : BerufskollegAnlage = new BerufskollegAnlage("X", 6, [new BerufskollegAnlageKatalogEintrag(24000, "X", "Ehemalige Kollegschule", null, null)]);

	/**
	 * Anlage Z: TODO
	 */
	public static readonly Z : BerufskollegAnlage = new BerufskollegAnlage("Z", 7, [new BerufskollegAnlageKatalogEintrag(26000, "Z", "Kooperationsklasse Hauptschule", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Anlage, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BerufskollegAnlageKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Anlage
	 */
	public readonly historie : Array<BerufskollegAnlageKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Anlagen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _anlagen : HashMap<string, BerufskollegAnlage> = new HashMap<string, BerufskollegAnlage>();

	/**
	 * Erzeugt eine neue Anlage in der Aufzählung.
	 *
	 * @param historie   die Historie der Anlage, welches ein Array von {@link BerufskollegAnlageKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BerufskollegAnlageKatalogEintrag>) {
		super(name, ordinal);
		BerufskollegAnlage.all_values_by_ordinal.push(this);
		BerufskollegAnlage.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Anlagen auf die zugehörigen Anlagenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Anlagen auf die zugehörigen Anlagen
	 */
	private static getMapAnlageByKuerzel() : HashMap<string, BerufskollegAnlage> {
		if (BerufskollegAnlage._anlagen.size() === 0) {
			for (const s of BerufskollegAnlage.values()) {
				if (s.daten !== null)
					BerufskollegAnlage._anlagen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegAnlage._anlagen;
	}

	/**
	 * Gibt die Anlage für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Anlage
	 *
	 * @return die Anlage oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegAnlage | null {
		return BerufskollegAnlage.getMapAnlageByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegAnlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegAnlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.BerufskollegAnlage';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.BerufskollegAnlage', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_BerufskollegAnlage(obj : unknown) : BerufskollegAnlage {
	return obj as BerufskollegAnlage;
}
