import type { JavaEnum } from '../../../../java/lang/JavaEnum';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap } from '../../../../java/util/HashMap';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';

export class KlausurterminblockungModusQuartale extends JavaObject implements JavaEnum<KlausurterminblockungModusQuartale> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KlausurterminblockungModusQuartale> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KlausurterminblockungModusQuartale> = new Map<string, KlausurterminblockungModusQuartale>();

	/**
	 * Alle Klausuren eines Halbjahres werden gemeinsam geblockt.
	 */
	public static readonly ZUSAMMEN : KlausurterminblockungModusQuartale = new KlausurterminblockungModusQuartale("ZUSAMMEN", 0, 0, "Zusammen");

	/**
	 * Die Klausuren werden pro Quartal im Halbjahr geblockt.
	 */
	public static readonly GETRENNT : KlausurterminblockungModusQuartale = new KlausurterminblockungModusQuartale("GETRENNT", 1, 1, "Getrennt");

	/**
	 * Die ID
	 */
	public readonly id : number;

	/**
	 * Die textuelle Bezeichnung
	 */
	public readonly bezeichnung : string;

	/**
	 * Eine Map mit der Zuordnung zu der ID
	 */
	private static readonly _mapID : HashMap<number, KlausurterminblockungModusQuartale> = new HashMap();

	/**
	 * Erstellt einen neuen Modus.
	 *
	 * @param id            die ID
	 * @param bezeichnung   die Bezeichnung
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KlausurterminblockungModusQuartale.all_values_by_ordinal.push(this);
		KlausurterminblockungModusQuartale.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt die Map mit der Zuordnung zu der ID zurück. Sollte diese noch nicht
	 * initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map mit der Zuordnung zu der ID
	 */
	private static getMapByID() : HashMap<number, KlausurterminblockungModusQuartale> {
		if (KlausurterminblockungModusQuartale._mapID.size() === 0)
			for (const e of KlausurterminblockungModusQuartale.values())
				KlausurterminblockungModusQuartale._mapID.put(e.id, e);
		return KlausurterminblockungModusQuartale._mapID;
	}

	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus oder null, falls die ID ungültig ist
	 */
	public static get(id : number) : KlausurterminblockungModusQuartale | null {
		return KlausurterminblockungModusQuartale.getMapByID().get(id);
	}

	/**
	 * Liefert den Modus anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Modus
	 *
	 * @throws DeveloperNotificationException falls die ID nicht definiert ist
	 */
	public static getOrException(id : number) : KlausurterminblockungModusQuartale {
		return DeveloperNotificationException.ifMapGetIsNull(KlausurterminblockungModusQuartale.getMapByID(), id);
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
		if (!(other instanceof KlausurterminblockungModusQuartale))
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
	public compareTo(other : KlausurterminblockungModusQuartale) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KlausurterminblockungModusQuartale> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KlausurterminblockungModusQuartale | null {
		const tmp : KlausurterminblockungModusQuartale | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_klausurplanung_KlausurterminblockungModusQuartale(obj : unknown) : KlausurterminblockungModusQuartale {
	return obj as KlausurterminblockungModusQuartale;
}
