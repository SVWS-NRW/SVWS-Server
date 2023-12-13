import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerKatalogAbgangsgrundEintrag } from '../../../core/data/lehrer/LehrerKatalogAbgangsgrundEintrag';

export class LehrerAbgangsgrund extends JavaEnum<LehrerAbgangsgrund> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerAbgangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerAbgangsgrund> = new Map<string, LehrerAbgangsgrund>();

	/**
	 * Grund 'Eintritt in den Ruhestand' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly RUHEST : LehrerAbgangsgrund = new LehrerAbgangsgrund("RUHEST", 0, [new LehrerKatalogAbgangsgrundEintrag(1, "RUHEST", "Eintritt in den Ruhestand", "11", null, null)]);

	/**
	 * Grund 'Dienst-, Erwerbs-, Berufsunfähigkeit' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly UNFAEHIGK : LehrerAbgangsgrund = new LehrerAbgangsgrund("UNFAEHIGK", 1, [new LehrerKatalogAbgangsgrundEintrag(2, "UNFÄHIGK", "Dienst-, Erwerbs-, Berufsunfähigkeit", "12", null, null)]);

	/**
	 * Grund 'Tod' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly TOD : LehrerAbgangsgrund = new LehrerAbgangsgrund("TOD", 2, [new LehrerKatalogAbgangsgrundEintrag(3, "TOD", "Tod", "13", null, null)]);

	/**
	 * Grund 'Übertritt in den Schuldienst eines anderen Bundeslandes' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly AndBuLand : LehrerAbgangsgrund = new LehrerAbgangsgrund("AndBuLand", 3, [new LehrerKatalogAbgangsgrundEintrag(4, "AndBuLand", "Übertritt in den Schuldienst eines anderen Bundeslandes", "14", null, null)]);

	/**
	 * Grund 'Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly WECHSEL : LehrerAbgangsgrund = new LehrerAbgangsgrund("WECHSEL", 4, [new LehrerKatalogAbgangsgrundEintrag(5, "WECHSEL", "Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule", "15", null, null)]);

	/**
	 * Grund 'Befristete Abgänge' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly BEFRIST : LehrerAbgangsgrund = new LehrerAbgangsgrund("BEFRIST", 5, [new LehrerKatalogAbgangsgrundEintrag(6, "BEFRIST", "Befristete Abgänge", "16", null, null)]);

	/**
	 * Grund 'Sonstige Abgänge' für das Verlassen der Schule durch den Lehrer
	 */
	public static readonly SONSTIG : LehrerAbgangsgrund = new LehrerAbgangsgrund("SONSTIG", 6, [new LehrerKatalogAbgangsgrundEintrag(7, "SONSTIG", "Sonstige Abgänge", "17", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Abgangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogAbgangsgrundEintrag;

	/**
	 * Die Historie mit den Einträgen des Abgangsgrundes
	 */
	public readonly historie : Array<LehrerKatalogAbgangsgrundEintrag>;

	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 *
	 * @param historie   die Historie des Abgangsgrundes, welches ein Array von {@link LehrerKatalogAbgangsgrundEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogAbgangsgrundEintrag>) {
		super(name, ordinal);
		LehrerAbgangsgrund.all_values_by_ordinal.push(this);
		LehrerAbgangsgrund.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Grundes
	 *
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerAbgangsgrund | null {
		for (const grund of LehrerAbgangsgrund.values())
			if (grund.daten.id === id)
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Grundes
	 *
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls das kuerzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerAbgangsgrund | null {
		for (const grund of LehrerAbgangsgrund.values())
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
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls der Schlüssel ungültig ist
	 */
	public static getByASDSchluessel(schluessel : string | null) : LehrerAbgangsgrund | null {
		for (const grund of LehrerAbgangsgrund.values())
			if (JavaObject.equalsTranspiler(grund.daten.schluessel, (schluessel)))
				return grund;
		return null;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerAbgangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerAbgangsgrund | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.lehrer.LehrerAbgangsgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerAbgangsgrund', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerAbgangsgrund(obj : unknown) : LehrerAbgangsgrund {
	return obj as LehrerAbgangsgrund;
}
