import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogLeitungsfunktionenEintrag } from '../../../core/data/lehrer/LehrerKatalogLeitungsfunktionenEintrag';

export class LehrerLeitungsfunktion extends JavaEnum<LehrerLeitungsfunktion> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLeitungsfunktion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLeitungsfunktion> = new Map<string, LehrerLeitungsfunktion>();

	/**
	 * Schulleitung
	 */
	public static readonly SL : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL", 0, [new LehrerKatalogLeitungsfunktionenEintrag(1, "SL", "Schulleitung", null, null)]);

	/**
	 * Stellvertretende Schulleitung
	 */
	public static readonly SL_STV : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL_STV", 1, [new LehrerKatalogLeitungsfunktionenEintrag(2, "Stv. SL", "Stellvertretende Schulleitung", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Leitungsfunktionen von Lehrern
	 */
	public readonly daten : LehrerKatalogLeitungsfunktionenEintrag;

	/**
	 * Die Historie mit den Einträgen der Leitungsfunktionen von Lehrern
	 */
	public readonly historie : Array<LehrerKatalogLeitungsfunktionenEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _mapByKuerzel : HashMap<string, LehrerLeitungsfunktion | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren IDs
	 */
	private static readonly _mapByID : HashMap<number, LehrerLeitungsfunktion | null> = new HashMap();

	/**
	 * Erzeugt eine neue Leitungsfunktion in der Aufzählung.
	 *
	 * @param historie   die Historie der Leitungsfunktion, welches ein Array von {@link LehrerKatalogLeitungsfunktionenEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLeitungsfunktionenEintrag>) {
		super(name, ordinal);
		LehrerLeitungsfunktion.all_values_by_ordinal.push(this);
		LehrerLeitungsfunktion.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 */
	private static getMapByKuerzel() : HashMap<string, LehrerLeitungsfunktion | null> {
		if (LehrerLeitungsfunktion._mapByKuerzel.size() === 0) {
			for (const s of LehrerLeitungsfunktion.values()) {
				if (s.daten !== null)
					LehrerLeitungsfunktion._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return LehrerLeitungsfunktion._mapByKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 */
	private static getMapByID() : HashMap<number, LehrerLeitungsfunktion | null> {
		if (LehrerLeitungsfunktion._mapByID.size() === 0) {
			for (const s of LehrerLeitungsfunktion.values()) {
				if (s.daten !== null)
					LehrerLeitungsfunktion._mapByID.put(s.daten.id, s);
			}
		}
		return LehrerLeitungsfunktion._mapByID;
	}

	/**
	 * Gibt die Leitungsfunktion für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Leitungsfunktion
	 *
	 * @return die Leitungsfunktion oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerLeitungsfunktion | null {
		return LehrerLeitungsfunktion.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt die Leitungsfunktion für die angegebene ID zurück.
	 *
	 * @param id   die ID der Leitungsfunktion
	 *
	 * @return die Leitungsfunktion oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerLeitungsfunktion | null {
		return LehrerLeitungsfunktion.getMapByID().get(id);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLeitungsfunktion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLeitungsfunktion | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.lehrer.LehrerLeitungsfunktion';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerLeitungsfunktion', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerLeitungsfunktion(obj : unknown) : LehrerLeitungsfunktion {
	return obj as LehrerLeitungsfunktion;
}
