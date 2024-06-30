import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from '../../../core/data/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag';

export class PrimarstufeSchuleingangsphaseBesuchsjahre extends JavaEnum<PrimarstufeSchuleingangsphaseBesuchsjahre> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<PrimarstufeSchuleingangsphaseBesuchsjahre> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, PrimarstufeSchuleingangsphaseBesuchsjahre> = new Map<string, PrimarstufeSchuleingangsphaseBesuchsjahre>();

	/**
	 * E1: Das erste Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E1 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E1", 0, [new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(1000, "E1", "Schuleingangsphase, 1. Schulbesuchsjahr", null, null)]);

	/**
	 * E2: Das zweite Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E2 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E2", 1, [new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(2000, "E2", "Schuleingangsphase, 2. Schulbesuchsjahr", null, null)]);

	/**
	 * E3: Das dritte Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E3 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E3", 2, [new PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(3000, "E3", "Schuleingangsphase, 3. Schulbesuchsjahr", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Besuchsjahre, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Besuchsjahre
	 */
	public readonly historie : Array<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung der Besuchsjahre zu dem Kürzel der Besuchsjahre
	 */
	private static readonly _mapKuerzel : HashMap<string, PrimarstufeSchuleingangsphaseBesuchsjahre> = new HashMap<string, PrimarstufeSchuleingangsphaseBesuchsjahre>();

	/**
	 * Eine Map mit der Zuordnung der Besuchsjahre zu der ID der Besuchsjahre
	 */
	private static readonly _mapID : HashMap<number, PrimarstufeSchuleingangsphaseBesuchsjahre> = new HashMap<number, PrimarstufeSchuleingangsphaseBesuchsjahre>();

	/**
	 * Erzeugt einen neuen Eintrage für Besuchsjahre in der Aufzählung.
	 *
	 * @param historie   die Historie für die Besuchsjahre, welches ein Array von {@link PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag>) {
		super(name, ordinal);
		PrimarstufeSchuleingangsphaseBesuchsjahre.all_values_by_ordinal.push(this);
		PrimarstufeSchuleingangsphaseBesuchsjahre.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Besuchsjahre auf die zugehörigen Besuchsjahre
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzel der Besuchsjahre auf die zugehörigen Besuchsjahre
	 */
	private static getMapJahrgangByKuerzel() : HashMap<string, PrimarstufeSchuleingangsphaseBesuchsjahre> {
		if (PrimarstufeSchuleingangsphaseBesuchsjahre._mapKuerzel.size() === 0)
			for (const j of PrimarstufeSchuleingangsphaseBesuchsjahre.values())
				PrimarstufeSchuleingangsphaseBesuchsjahre._mapKuerzel.put(j.daten.kuerzel, j);
		return PrimarstufeSchuleingangsphaseBesuchsjahre._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Besuchsjahre auf die zugehörigen Besuchsjahre
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Besuchsjahre auf die zugehörigen Besuchsjahre
	 */
	private static getMapJahrgangByID() : HashMap<number, PrimarstufeSchuleingangsphaseBesuchsjahre> {
		if (PrimarstufeSchuleingangsphaseBesuchsjahre._mapID.size() === 0)
			for (const j of PrimarstufeSchuleingangsphaseBesuchsjahre.values()) {
				for (const k of j.historie)
					PrimarstufeSchuleingangsphaseBesuchsjahre._mapID.put(k.id, j);
			}
		return PrimarstufeSchuleingangsphaseBesuchsjahre._mapID;
	}

	/**
	 * Liefert die Besuchsjahre anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Besuchsjahre
	 *
	 * @return die Besuchsjahre oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		return PrimarstufeSchuleingangsphaseBesuchsjahre.getMapJahrgangByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert die Besuchsjahre anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Besuchsjahre
	 *
	 * @return die Besuchsjahre oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		return PrimarstufeSchuleingangsphaseBesuchsjahre.getMapJahrgangByID().get(id);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<PrimarstufeSchuleingangsphaseBesuchsjahre> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_jahrgang_PrimarstufeSchuleingangsphaseBesuchsjahre(obj : unknown) : PrimarstufeSchuleingangsphaseBesuchsjahre {
	return obj as PrimarstufeSchuleingangsphaseBesuchsjahre;
}
