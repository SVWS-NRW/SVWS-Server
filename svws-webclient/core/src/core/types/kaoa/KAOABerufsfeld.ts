import { JavaEnum } from '../../../java/lang/JavaEnum';
import { KAOABerufsfeldEintrag } from '../../../core/data/kaoa/KAOABerufsfeldEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class KAOABerufsfeld extends JavaEnum<KAOABerufsfeld> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOABerufsfeld> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOABerufsfeld> = new Map<string, KAOABerufsfeld>();

	/**
	 * KAoA-Berufsfeld: Bau, Architektur, Vermessung
	 */
	public static readonly BAV : KAOABerufsfeld = new KAOABerufsfeld("BAV", 0, [new KAOABerufsfeldEintrag(1, "BAV", "Bau, Architektur, Vermessung", null, null)]);

	/**
	 * KAoA-Berufsfeld: Dienstleistung
	 */
	public static readonly D : KAOABerufsfeld = new KAOABerufsfeld("D", 1, [new KAOABerufsfeldEintrag(2, "D", "Dienstleistung", null, null)]);

	/**
	 * KAoA-Berufsfeld: Elektro
	 */
	public static readonly EL : KAOABerufsfeld = new KAOABerufsfeld("EL", 2, [new KAOABerufsfeldEintrag(3, "EL", "Elektro", null, null)]);

	/**
	 * KAoA-Berufsfeld: Gesundheit
	 */
	public static readonly G : KAOABerufsfeld = new KAOABerufsfeld("G", 3, [new KAOABerufsfeldEintrag(4, "G", "Gesundheit", null, null)]);

	/**
	 * KAoA-Berufsfeld: Gesellschafts-,Geisteswissenschaften
	 */
	public static readonly GESGE : KAOABerufsfeld = new KAOABerufsfeld("GESGE", 4, [new KAOABerufsfeldEintrag(5, "GESGE", "Gesellschafts-,Geisteswissenschaften", null, null)]);

	/**
	 * KAoA-Berufsfeld: IT, Computer
	 */
	public static readonly ITC : KAOABerufsfeld = new KAOABerufsfeld("ITC", 5, [new KAOABerufsfeldEintrag(6, "ITC", "IT, Computer", null, null)]);

	/**
	 * KAoA-Berufsfeld: Kunst, Kultur, Gestaltung
	 */
	public static readonly KKG : KAOABerufsfeld = new KAOABerufsfeld("KKG", 6, [new KAOABerufsfeldEintrag(7, "KKG", "Kunst, Kultur, Gestaltung", null, null)]);

	/**
	 * KAoA-Berufsfeld: Landwirtschaft, Natur, Umwelt
	 */
	public static readonly LANAUM : KAOABerufsfeld = new KAOABerufsfeld("LANAUM", 7, [new KAOABerufsfeldEintrag(8, "LANAUM", "Landwirtschaft, Natur, Umwelt", null, null)]);

	/**
	 * KAoA-Berufsfeld: Metall, Maschinenbau
	 */
	public static readonly M : KAOABerufsfeld = new KAOABerufsfeld("M", 8, [new KAOABerufsfeldEintrag(9, "M", "Metall, Maschinenbau", null, null)]);

	/**
	 * KAoA-Berufsfeld: Medien
	 */
	public static readonly ME : KAOABerufsfeld = new KAOABerufsfeld("ME", 9, [new KAOABerufsfeldEintrag(10, "ME", "Medien", null, null)]);

	/**
	 * KAoA-Berufsfeld: Naturwissenschaft
	 */
	public static readonly N : KAOABerufsfeld = new KAOABerufsfeld("N", 10, [new KAOABerufsfeldEintrag(11, "N", "Naturwissenschaft", null, null)]);

	/**
	 * KAoA-Berufsfeld: Produktion, Fertigung
	 */
	public static readonly PRFE : KAOABerufsfeld = new KAOABerufsfeld("PRFE", 11, [new KAOABerufsfeldEintrag(12, "PRFE", "Produktion, Fertigung", null, null)]);

	/**
	 * KAoA-Berufsfeld: Soziales, Pädagogik
	 */
	public static readonly SP : KAOABerufsfeld = new KAOABerufsfeld("SP", 12, [new KAOABerufsfeldEintrag(13, "SP", "Soziales, Pädagogik", null, null)]);

	/**
	 * KAoA-Berufsfeld: Technik, Technologiefelder
	 */
	public static readonly TEC : KAOABerufsfeld = new KAOABerufsfeld("TEC", 13, [new KAOABerufsfeldEintrag(14, "TEC", "Technik, Technologiefelder", null, null)]);

	/**
	 * KAoA-Berufsfeld: Verkehr, Logistik
	 */
	public static readonly VL : KAOABerufsfeld = new KAOABerufsfeld("VL", 14, [new KAOABerufsfeldEintrag(15, "VL", "Verkehr, Logistik", null, null)]);

	/**
	 * KAoA-Berufsfeld: Wirtschaft, Verwaltung
	 */
	public static readonly WIVE : KAOABerufsfeld = new KAOABerufsfeld("WIVE", 15, [new KAOABerufsfeldEintrag(16, "WIVE", "Wirtschaft, Verwaltung", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die aktuellsten Daten des KAoA-Berufsfeldes
	 */
	public readonly daten : KAOABerufsfeldEintrag;

	/**
	 * Die Historie mit den Einträgen des KAoA-Berufsfeldes
	 */
	public readonly historie : Array<KAOABerufsfeldEintrag>;

	/**
	 * Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, KAOABerufsfeld> = new HashMap<number, KAOABerufsfeld>();

	/**
	 * Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, KAOABerufsfeld> = new HashMap<string, KAOABerufsfeld>();

	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 *
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOABerufsfeldEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KAOABerufsfeldEintrag>) {
		super(name, ordinal);
		KAOABerufsfeld.all_values_by_ordinal.push(this);
		KAOABerufsfeld.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID auf das zugehörige Berufsfeld zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von der ID auf das zugehörige Berufsfeld
	 */
	private static getMapStatusByID() : HashMap<number, KAOABerufsfeld> {
		if (KAOABerufsfeld._statusByID.size() === 0)
			for (const g of KAOABerufsfeld.values())
				KAOABerufsfeld._statusByID.put(g.daten.id, g);
		return KAOABerufsfeld._statusByID;
	}

	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Berufsfeld zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von dem Kürzel auf das zugehörige Berufsfeld
	 */
	private static getMapStatusByKuerzel() : HashMap<string, KAOABerufsfeld> {
		if (KAOABerufsfeld._statusByKuerzel.size() === 0)
			for (const g of KAOABerufsfeld.values())
				KAOABerufsfeld._statusByKuerzel.put(g.daten.kuerzel, g);
		return KAOABerufsfeld._statusByKuerzel;
	}

	/**
	 * Gibt das Berufsfeld anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Berufsfeldes
	 *
	 * @return das Berufsfeld oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : KAOABerufsfeld | null {
		return KAOABerufsfeld.getMapStatusByID().get(id);
	}

	/**
	 * Gibt das Berufsfeld anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Berufsfeldes
	 *
	 * @return das Berufsfeld oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : KAOABerufsfeld | null {
		return KAOABerufsfeld.getMapStatusByKuerzel().get(kuerzel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOABerufsfeld> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOABerufsfeld | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.kaoa.KAOABerufsfeld';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.kaoa.KAOABerufsfeld', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_kaoa_KAOABerufsfeld(obj : unknown) : KAOABerufsfeld {
	return obj as KAOABerufsfeld;
}
