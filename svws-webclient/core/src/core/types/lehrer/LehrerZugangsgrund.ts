import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerKatalogZugangsgrundEintrag } from '../../../core/data/lehrer/LehrerKatalogZugangsgrundEintrag';

export class LehrerZugangsgrund extends JavaEnum<LehrerZugangsgrund> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerZugangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerZugangsgrund> = new Map<string, LehrerZugangsgrund>();

	/**
	 * Grund 'Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung' für das Kommen des Lehrers an die Schule
	 */
	public static readonly NEU : LehrerZugangsgrund = new LehrerZugangsgrund("NEU", 0, [new LehrerKatalogZugangsgrundEintrag(1, "NEU", "Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung", "1", null, null)]);

	/**
	 * Grund 'Übertritt aus dem Schuldienst eines anderen Bundeslandes' für das Kommen des Lehrers an die Schule
	 */
	public static readonly AndBuLand : LehrerZugangsgrund = new LehrerZugangsgrund("AndBuLand", 1, [new LehrerKatalogZugangsgrundEintrag(2, "AndBuLand", "Übertritt aus dem Schuldienst eines anderen Bundeslandes", "2", null, null)]);

	/**
	 * Grund 'Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WECHSEL : LehrerZugangsgrund = new LehrerZugangsgrund("WECHSEL", 2, [new LehrerKatalogZugangsgrundEintrag(3, "WECHSEL", "Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule", "3", null, null)]);

	/**
	 * Grund 'Wiedereintritt in den Schuldienst' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WIEDER : LehrerZugangsgrund = new LehrerZugangsgrund("WIEDER", 3, [new LehrerKatalogZugangsgrundEintrag(4, "WIEDER", "Wiedereintritt in den Schuldienst", "4", null, null)]);

	/**
	 * Grund 'Sonstige Zugänge' für das Kommen des Lehrers an die Schule
	 */
	public static readonly SONSTIG : LehrerZugangsgrund = new LehrerZugangsgrund("SONSTIG", 4, [new LehrerKatalogZugangsgrundEintrag(5, "SONSTIG", "Sonstige Zugänge", "5", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Zugangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogZugangsgrundEintrag;

	/**
	 * Die Historie mit den Einträgen des Zugangsgrundes
	 */
	public readonly historie : Array<LehrerKatalogZugangsgrundEintrag>;

	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 *
	 * @param historie   die Historie des Zugangsgrundes, welches ein Array von {@link LehrerKatalogZugangsgrundEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogZugangsgrundEintrag>) {
		super(name, ordinal);
		LehrerZugangsgrund.all_values_by_ordinal.push(this);
		LehrerZugangsgrund.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Grundes
	 *
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerZugangsgrund | null {
		for (const grund of LehrerZugangsgrund.values())
			if (grund.daten.id === id)
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Grundes
	 *
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerZugangsgrund | null {
		for (const grund of LehrerZugangsgrund.values())
			if (JavaObject.equalsTranspiler(grund.daten.kuerzel, (kuerzel)))
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Schlüssels der
	 * amtlichen Schulstatistik zurück.
	 *
	 * @param schluessel   der Schlüssel der amtlichen Schulstatistik
	 *                     für den Grundes
	 *
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls der Schlüssel ungültig ist
	 */
	public static getByASDSchluessel(schluessel : string | null) : LehrerZugangsgrund | null {
		for (const grund of LehrerZugangsgrund.values())
			if (JavaObject.equalsTranspiler(grund.daten.schluessel, (schluessel)))
				return grund;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerZugangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerZugangsgrund | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.lehrer.LehrerZugangsgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerZugangsgrund', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerZugangsgrund(obj : unknown) : LehrerZugangsgrund {
	return obj as LehrerZugangsgrund;
}
