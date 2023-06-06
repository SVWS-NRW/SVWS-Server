import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogFachrichtungAnerkennungEintrag } from '../../../core/data/lehrer/LehrerKatalogFachrichtungAnerkennungEintrag';

export class LehrerFachrichtungAnerkennung extends JavaObject implements JavaEnum<LehrerFachrichtungAnerkennung> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerFachrichtungAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerFachrichtungAnerkennung> = new Map<string, LehrerFachrichtungAnerkennung>();

	/**
	 * Fachrichtungsanerkennung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'
	 */
	public static readonly ID4 : LehrerFachrichtungAnerkennung = new LehrerFachrichtungAnerkennung("ID4", 0, [new LehrerKatalogFachrichtungAnerkennungEintrag(4, "1", "erworben durch LABG/OVP bzw. Laufbahnverordnung", null, null)]);

	/**
	 * Fachrichtungsanerkennung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'
	 */
	public static readonly ID5 : LehrerFachrichtungAnerkennung = new LehrerFachrichtungAnerkennung("ID5", 1, [new LehrerKatalogFachrichtungAnerkennungEintrag(5, "2", "Unterrichtserlaubnis (z. B. Zertifikatskurs)", null, null)]);

	/**
	 * Fachrichtungsanerkennung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'
	 */
	public static readonly ID6 : LehrerFachrichtungAnerkennung = new LehrerFachrichtungAnerkennung("ID6", 2, [new LehrerKatalogFachrichtungAnerkennungEintrag(6, "3", "mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis", null, null)]);

	/**
	 * Fachrichtungsanerkennung 'sonstige'
	 */
	public static readonly ID7 : LehrerFachrichtungAnerkennung = new LehrerFachrichtungAnerkennung("ID7", 3, [new LehrerKatalogFachrichtungAnerkennungEintrag(7, "9", "sonstige", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten die Anerkennung der Fachrichtung , wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogFachrichtungAnerkennungEintrag;

	/**
	 * Die Historie mit den Einträgen für die Anerkennung der Fachrichtung
	 */
	public readonly historie : Array<LehrerKatalogFachrichtungAnerkennungEintrag>;

	/**
	 * Eine Hashmap mit allen Fachrichtungsanerkennungen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _anerkennungenByID : HashMap<number, LehrerFachrichtungAnerkennung | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Fachrichtungsanerkennungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _anerkennungenByKuerzel : HashMap<string, LehrerFachrichtungAnerkennung | null> = new HashMap();

	/**
	 * Erzeugt neue Anerkennung für Fachrichtungen in der Aufzählung.
	 *
	 * @param historie   die Historie der Anerkennung für Fachrichtungen, welches ein Array von {@link LehrerKatalogFachrichtungAnerkennungEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogFachrichtungAnerkennungEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerFachrichtungAnerkennung.all_values_by_ordinal.push(this);
		LehrerFachrichtungAnerkennung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 */
	private static getMapAnerkennungenByID() : HashMap<number, LehrerFachrichtungAnerkennung | null> {
		if (LehrerFachrichtungAnerkennung._anerkennungenByID.size() === 0)
			for (const l of LehrerFachrichtungAnerkennung.values())
				LehrerFachrichtungAnerkennung._anerkennungenByID.put(l.daten.id, l);
		return LehrerFachrichtungAnerkennung._anerkennungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 */
	private static getMapAnerkennungenByKuerzel() : HashMap<string, LehrerFachrichtungAnerkennung | null> {
		if (LehrerFachrichtungAnerkennung._anerkennungenByKuerzel.size() === 0)
			for (const l of LehrerFachrichtungAnerkennung.values())
				LehrerFachrichtungAnerkennung._anerkennungenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerFachrichtungAnerkennung._anerkennungenByKuerzel;
	}

	/**
	 * Gibt die Fachrichtungsanerkennung anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Fachrichtungsanerkennung
	 *
	 * @return die Fachrichtungsanerkennung oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerFachrichtungAnerkennung | null {
		return LehrerFachrichtungAnerkennung.getMapAnerkennungenByID().get(id);
	}

	/**
	 * Gibt die Fachrichtungsanerkennung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Fachrichtungsanerkennung
	 *
	 * @return die Fachrichtungsanerkennung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerFachrichtungAnerkennung | null {
		return LehrerFachrichtungAnerkennung.getMapAnerkennungenByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof LehrerFachrichtungAnerkennung))
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
	public compareTo(other : LehrerFachrichtungAnerkennung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerFachrichtungAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerFachrichtungAnerkennung | null {
		const tmp : LehrerFachrichtungAnerkennung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerFachrichtungAnerkennung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerFachrichtungAnerkennung(obj : unknown) : LehrerFachrichtungAnerkennung {
	return obj as LehrerFachrichtungAnerkennung;
}
