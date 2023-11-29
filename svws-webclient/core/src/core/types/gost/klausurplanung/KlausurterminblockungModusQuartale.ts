import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { HashMap } from '../../../../java/util/HashMap';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';

export class KlausurterminblockungModusQuartale extends JavaEnum<KlausurterminblockungModusQuartale> {

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
		super(name, ordinal);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_klausurplanung_KlausurterminblockungModusQuartale(obj : unknown) : KlausurterminblockungModusQuartale {
	return obj as KlausurterminblockungModusQuartale;
}
