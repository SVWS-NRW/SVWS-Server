import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { EinschulungsartKatalogEintrag } from '../../../core/data/schule/EinschulungsartKatalogEintrag';
import { Class } from '../../../java/lang/Class';

export class Einschulungsart extends JavaEnum<Einschulungsart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Einschulungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Einschulungsart> = new Map<string, Einschulungsart>();

	/**
	 * Einschulungsart: Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben
	 */
	public static readonly E51 : Einschulungsart = new Einschulungsart("E51", 0, [new EinschulungsartKatalogEintrag(1000, "51", "älter als 6. Jahre", "Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben", null, null)]);

	/**
	 * Einschulungsart: Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden
	 */
	public static readonly E52 : Einschulungsart = new Einschulungsart("E52", 1, [new EinschulungsartKatalogEintrag(2000, "52", "jünger als 6 Jahre", "Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden", null, null)]);

	/**
	 * Einschulungsart: Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen
	 */
	public static readonly E53 : Einschulungsart = new Einschulungsart("E53", 2, [new EinschulungsartKatalogEintrag(3000, "53", "zurückgestellt (§35 Abs. 3 SchulG)", "Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen", null, null)]);

	/**
	 * Einschulungsart: Kinder, die erstmals eine Früherziehung besuchen
	 */
	public static readonly E54 : Einschulungsart = new Einschulungsart("E54", 3, [new EinschulungsartKatalogEintrag(4000, "54", "Früherziehung", "Kinder, die erstmals eine Früherziehung besuchen", null, null)]);

	/**
	 * Einschulungsart: Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung
	 */
	public static readonly E18 : Einschulungsart = new Einschulungsart("E18", 4, [new EinschulungsartKatalogEintrag(7000, "18", "vorher: Früherziehung", "Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung", null, null)]);

	/**
	 * Einschulungsart: Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten
	 */
	public static readonly E19 : Einschulungsart = new Einschulungsart("E19", 5, [new EinschulungsartKatalogEintrag(8000, "19", "vorher: Förderschulkindergarten", "Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Einschulungsart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : EinschulungsartKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Einschulungsart
	 */
	public readonly historie : Array<EinschulungsartKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _mapBySchluessel : HashMap<string, Einschulungsart> = new HashMap<string, Einschulungsart>();

	/**
	 * Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren IDs
	 */
	private static readonly _mapByID : HashMap<number, Einschulungsart> = new HashMap<number, Einschulungsart>();

	/**
	 * Erzeugt eine neuen Einschulungsart in der Aufzählung.
	 *
	 * @param historie   die Historie der Einschulungsart, welche ein Array von
	 *                   {@link EinschulungsartKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<EinschulungsartKatalogEintrag>) {
		super(name, ordinal);
		Einschulungsart.all_values_by_ordinal.push(this);
		Einschulungsart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Einschulungsarten auf die
	 * zugehörigen Einschulungsarten zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Einschulungsarten
	 */
	private static getMapBySchluessel() : HashMap<string, Einschulungsart> {
		if (Einschulungsart._mapBySchluessel.size() === 0) {
			for (const s of Einschulungsart.values()) {
				if (s.daten !== null)
					Einschulungsart._mapBySchluessel.put(s.daten.kuerzel, s);
			}
		}
		return Einschulungsart._mapBySchluessel;
	}

	/**
	 * Gibt die Einschulungsart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Einschulungsart
	 *
	 * @return die Einschulungsart oder null, falls das Kürzel ungültig ist
	 */
	public static getBySchluessel(kuerzel : string | null) : Einschulungsart | null {
		return Einschulungsart.getMapBySchluessel().get(kuerzel);
	}

	/**
	 * Gibt eine Map von den IDs der Einschulungsarten auf die
	 * zugehörigen Einschulungsarten zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs auf die zugehörigen Einschulungsarten
	 */
	private static getMapByID() : HashMap<number, Einschulungsart> {
		if (Einschulungsart._mapByID.size() === 0) {
			for (const s of Einschulungsart.values()) {
				if (s.daten !== null)
					Einschulungsart._mapByID.put(s.daten.id, s);
			}
		}
		return Einschulungsart._mapByID;
	}

	/**
	 * Gibt die Einschulungsart für die angegebene ID zurück.
	 *
	 * @param id   die ID der Einschulungsart
	 *
	 * @return die Einschulungsart oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : Einschulungsart | null {
		return Einschulungsart.getMapByID().get(id);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Einschulungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Einschulungsart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schueler.Einschulungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.Einschulungsart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Einschulungsart>('de.svws_nrw.core.types.schueler.Einschulungsart');

}

export function cast_de_svws_nrw_core_types_schueler_Einschulungsart(obj : unknown) : Einschulungsart {
	return obj as Einschulungsart;
}
