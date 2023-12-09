import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogEinsatzstatusEintrag } from '../../../core/data/lehrer/LehrerKatalogEinsatzstatusEintrag';

export class LehrerEinsatzstatus extends JavaEnum<LehrerEinsatzstatus> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerEinsatzstatus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerEinsatzstatus> = new Map<string, LehrerEinsatzstatus>();

	/**
	 * Einsatzstatus: 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig'
	 */
	public static readonly A : LehrerEinsatzstatus = new LehrerEinsatzstatus("A", 0, [new LehrerKatalogEinsatzstatusEintrag(1, "A", "Stammschule, ganz oder teilweise auch an anderen Schulen tätig", null, null)]);

	/**
	 * Einsatzstatus: 'nicht Stammschule, aber auch hier tätig'
	 */
	public static readonly B : LehrerEinsatzstatus = new LehrerEinsatzstatus("B", 1, [new LehrerKatalogEinsatzstatusEintrag(2, "B", "nicht Stammschule, aber auch hier tätig", null, null)]);

	/**
	 * Einsatzstatus: 'Stammschule, nur hier tätig'
	 */
	public static readonly DEFAULT : LehrerEinsatzstatus = new LehrerEinsatzstatus("DEFAULT", 2, [new LehrerKatalogEinsatzstatusEintrag(3, "*", "Stammschule, nur hier tätig", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Einsatzstatus, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogEinsatzstatusEintrag;

	/**
	 * Die Historie mit den Einträgen des Einsatzstatus
	 */
	public readonly historie : Array<LehrerKatalogEinsatzstatusEintrag>;

	/**
	 * Eine Hashmap mit allen Einsatzstatus, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, LehrerEinsatzstatus | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einsatzstatus, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, LehrerEinsatzstatus | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Einsatzstatus in der Aufzählung.
	 *
	 * @param historie   die Historie des Einsatzstatus, welches ein Array von {@link LehrerKatalogEinsatzstatusEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogEinsatzstatusEintrag>) {
		super(name, ordinal);
		LehrerEinsatzstatus.all_values_by_ordinal.push(this);
		LehrerEinsatzstatus.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static getMapStatusByID() : HashMap<number, LehrerEinsatzstatus | null> {
		if (LehrerEinsatzstatus._statusByID.size() === 0)
			for (const g of LehrerEinsatzstatus.values())
				LehrerEinsatzstatus._statusByID.put(g.daten.id, g);
		return LehrerEinsatzstatus._statusByID;
	}

	/**
	 * Gibt eine Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static getMapStatusByKuerzel() : HashMap<string, LehrerEinsatzstatus | null> {
		if (LehrerEinsatzstatus._statusByKuerzel.size() === 0)
			for (const g of LehrerEinsatzstatus.values())
				LehrerEinsatzstatus._statusByKuerzel.put(g.daten.kuerzel, g);
		return LehrerEinsatzstatus._statusByKuerzel;
	}

	/**
	 * Gibt den Einsatzstatus anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Einsatzstatus
	 *
	 * @return der Einsatzstatus oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerEinsatzstatus | null {
		return LehrerEinsatzstatus.getMapStatusByID().get(id);
	}

	/**
	 * Gibt den Einsatzstatus anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Einsatzstatus
	 *
	 * @return der Einsatzstatus oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerEinsatzstatus | null {
		return LehrerEinsatzstatus.getMapStatusByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerEinsatzstatus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerEinsatzstatus | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerEinsatzstatus', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerEinsatzstatus(obj : unknown) : LehrerEinsatzstatus {
	return obj as LehrerEinsatzstatus;
}
