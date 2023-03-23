import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerKatalogLehramtAnerkennungEintrag } from '../../../core/data/lehrer/LehrerKatalogLehramtAnerkennungEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class LehrerLehramtAnerkennung extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerLehramtAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerLehramtAnerkennung> = new Map<string, LehrerLehramtAnerkennung>();

	/**
	 * Lehramtsanerkennung 'Zweite Staatsprüfung für ein Lehramt'
	 */
	public static readonly ST : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("ST", 0, [new LehrerKatalogLehramtAnerkennungEintrag(1, "ST", "Zweite Staatsprüfung für ein Lehramt", null, null)]);

	/**
	 * Lehramtsanerkennung 'Anerkennung Lehramt'
	 */
	public static readonly AL : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("AL", 1, [new LehrerKatalogLehramtAnerkennungEintrag(2, "AL", "Anerkennung Lehramt", null, null)]);

	/**
	 * Lehramtsanerkennung 'Anerkennung geeignete Prüfung'
	 */
	public static readonly AP : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("AP", 2, [new LehrerKatalogLehramtAnerkennungEintrag(3, "AP", "Anerkennung geeignete Prüfung", null, null)]);

	/**
	 * Lehramtsanerkennung 'Förderliche Berufstätigkeit'
	 */
	public static readonly BT : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("BT", 3, [new LehrerKatalogLehramtAnerkennungEintrag(4, "BT", "Förderliche Berufstätigkeit", null, null)]);

	/**
	 * Lehramtsanerkennung 'ohne'
	 */
	public static readonly OH : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("OH", 4, [new LehrerKatalogLehramtAnerkennungEintrag(5, "OH", "ohne", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Lehramtsanerkennung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogLehramtAnerkennungEintrag;

	/**
	 * Die Historie mit den Einträgen der Lehramtsanerkennung
	 */
	public readonly historie : Array<LehrerKatalogLehramtAnerkennungEintrag>;

	/**
	 * Eine Hashmap mit allen Lehramtsanerkennungen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _anerkennungenByID : HashMap<number, LehrerLehramtAnerkennung | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Lehramtsanerkennungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _anerkennungenByKuerzel : HashMap<string, LehrerLehramtAnerkennung | null> = new HashMap();

	/**
	 * Erzeugt eine neue Lehramtsanerkennung in der Aufzählung.
	 *
	 * @param historie   die Historie der Lehramtsanerkennung, welches ein Array von {@link LehrerKatalogLehramtAnerkennungEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLehramtAnerkennungEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerLehramtAnerkennung.all_values_by_ordinal.push(this);
		LehrerLehramtAnerkennung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 */
	private static getMapAnerkennungenByID() : HashMap<number, LehrerLehramtAnerkennung | null> {
		if (LehrerLehramtAnerkennung._anerkennungenByID.size() === 0)
			for (let l of LehrerLehramtAnerkennung.values())
				LehrerLehramtAnerkennung._anerkennungenByID.put(l.daten.id, l);
		return LehrerLehramtAnerkennung._anerkennungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 */
	private static getMapAnerkennungenByKuerzel() : HashMap<string, LehrerLehramtAnerkennung | null> {
		if (LehrerLehramtAnerkennung._anerkennungenByKuerzel.size() === 0)
			for (let l of LehrerLehramtAnerkennung.values())
				LehrerLehramtAnerkennung._anerkennungenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerLehramtAnerkennung._anerkennungenByKuerzel;
	}

	/**
	 * Gibt die Lehramtsanerkennung anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Lehramtsanerkennung
	 *
	 * @return die Lehramtsanerkennung oder null, falls die ID ungültig ist.
	 */
	public static getByID(id : number) : LehrerLehramtAnerkennung | null {
		return LehrerLehramtAnerkennung.getMapAnerkennungenByID().get(id);
	}

	/**
	 * Gibt die Lehramtsanerkennung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Lehramtsanerkennung
	 *
	 * @return die Lehramtsanerkennung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerLehramtAnerkennung | null {
		return LehrerLehramtAnerkennung.getMapAnerkennungenByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof LehrerLehramtAnerkennung))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : LehrerLehramtAnerkennung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehramtAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehramtAnerkennung | null {
		const tmp : LehrerLehramtAnerkennung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerLehramtAnerkennung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerLehramtAnerkennung(obj : unknown) : LehrerLehramtAnerkennung {
	return obj as LehrerLehramtAnerkennung;
}
