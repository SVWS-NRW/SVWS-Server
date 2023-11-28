import { JavaEnum } from '../../../java/lang/JavaEnum';
import { LehrerKatalogLehrbefaehigungAnerkennungEintrag } from '../../../core/data/lehrer/LehrerKatalogLehrbefaehigungAnerkennungEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class LehrerLehrbefaehigungAnerkennung extends JavaEnum<LehrerLehrbefaehigungAnerkennung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigungAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehrbefaehigungAnerkennung> = new Map<string, LehrerLehrbefaehigungAnerkennung>();

	/**
	 * Anerkennung der Lehrbefähigung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'
	 */
	public static readonly ID_1 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_1", 0, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(1, "1", "erworben durch LABG/OVP bzw. Laufbahnverordnung", null, null)]);

	/**
	 * Anerkennung der Lehrbefähigung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'
	 */
	public static readonly ID_2 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_2", 1, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(2, "2", "Unterrichtserlaubnis (z. B. Zertifikatskurs)", null, null)]);

	/**
	 * Anerkennung der Lehrbefähigung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'
	 */
	public static readonly ID_3 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_3", 2, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(3, "3", "mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis", null, null)]);

	/**
	 * Anerkennung der Lehrbefähigung 'sonstige'
	 */
	public static readonly ID_9 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_9", 3, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(4, "9", "sonstige", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Lehrbefähigung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogLehrbefaehigungAnerkennungEintrag;

	/**
	 * Die Historie mit den Einträgen der Lehrbefähigung
	 */
	public readonly historie : Array<LehrerKatalogLehrbefaehigungAnerkennungEintrag>;

	/**
	 * Eine Hashmap mit allen Anerkennungsgründen für Lehrbefähigungen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _anerkennungenByID : HashMap<number, LehrerLehrbefaehigungAnerkennung | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Anerkennungsgründen für Lehrbefähigungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _anerkennungenByKuerzel : HashMap<string, LehrerLehrbefaehigungAnerkennung | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Anerkennungsgrund für Lehrbefähigungen in der Aufzählung.
	 *
	 * @param historie   die Historie der Lehrbefähigung, welches ein Array von {@link LehrerKatalogLehrbefaehigungAnerkennungEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLehrbefaehigungAnerkennungEintrag>) {
		super(name, ordinal);
		LehrerLehrbefaehigungAnerkennung.all_values_by_ordinal.push(this);
		LehrerLehrbefaehigungAnerkennung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static getMapAnerkennungenByID() : HashMap<number, LehrerLehrbefaehigungAnerkennung | null> {
		if (LehrerLehrbefaehigungAnerkennung._anerkennungenByID.size() === 0)
			for (const l of LehrerLehrbefaehigungAnerkennung.values())
				LehrerLehrbefaehigungAnerkennung._anerkennungenByID.put(l.daten.id, l);
		return LehrerLehrbefaehigungAnerkennung._anerkennungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static getMapAnerkennungenByKuerzel() : HashMap<string, LehrerLehrbefaehigungAnerkennung | null> {
		if (LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel.size() === 0)
			for (const l of LehrerLehrbefaehigungAnerkennung.values())
				LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel;
	}

	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Anerkennungsgrunded für Lehrbefähigungen
	 *
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerLehrbefaehigungAnerkennung | null {
		return LehrerLehrbefaehigungAnerkennung.getMapAnerkennungenByID().get(id);
	}

	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Anerkennungsgrunded für Lehrbefähigungen
	 *
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerLehrbefaehigungAnerkennung | null {
		return LehrerLehrbefaehigungAnerkennung.getMapAnerkennungenByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehrbefaehigungAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehrbefaehigungAnerkennung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerLehrbefaehigungAnerkennung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerLehrbefaehigungAnerkennung(obj : unknown) : LehrerLehrbefaehigungAnerkennung {
	return obj as LehrerLehrbefaehigungAnerkennung;
}
