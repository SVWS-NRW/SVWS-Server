import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { SchulformKatalogEintrag } from '../../../core/data/schule/SchulformKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class Schulform extends JavaEnum<Schulform> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Schulform> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Schulform> = new Map<string, Schulform>();

	/**
	 * Schulform Berufskolleg
	 */
	public static readonly BK : Schulform = new Schulform("BK", 0, [new SchulformKatalogEintrag(1000, "BK", "30", "Berufskolleg", false, true, false, false, null, null)]);

	/**
	 * Schulform Freie Waldorfschule
	 */
	public static readonly FW : Schulform = new Schulform("FW", 1, [new SchulformKatalogEintrag(2000, "FW", "17", "Freie Waldorfschule", true, true, false, true, null, null)]);

	/**
	 * Schulform Grundschule
	 */
	public static readonly G : Schulform = new Schulform("G", 2, [new SchulformKatalogEintrag(3000, "G", "02", "Grundschule", true, false, false, false, null, null)]);

	/**
	 * Schulform Gesamtschule
	 */
	public static readonly GE : Schulform = new Schulform("GE", 3, [new SchulformKatalogEintrag(4000, "GE", "15", "Gesamtschule", true, false, false, true, null, null)]);

	/**
	 * Schulform Gemeinschaftsschule
	 */
	public static readonly GM : Schulform = new Schulform("GM", 4, [new SchulformKatalogEintrag(5000, "GM", "16", "Gemeinschaftsschule", true, false, false, false, null, 2022)]);

	/**
	 * Schulform Gymnasium
	 */
	public static readonly GY : Schulform = new Schulform("GY", 5, [new SchulformKatalogEintrag(6000, "GY", "20", "Gymnasium", true, false, false, true, null, null)]);

	/**
	 * Schulform Hauptschule
	 */
	public static readonly H : Schulform = new Schulform("H", 6, [new SchulformKatalogEintrag(7000, "H", "04", "Hauptschule", true, false, false, false, null, null)]);

	/**
	 * Hibernia
	 */
	public static readonly HI : Schulform = new Schulform("HI", 7, [new SchulformKatalogEintrag(8000, "HI", "18", "Hibernia", true, true, false, false, null, null)]);

	/**
	 * Schulform Schulversuch PRIMUS
	 */
	public static readonly PS : Schulform = new Schulform("PS", 8, [new SchulformKatalogEintrag(9000, "PS", "13", "Schulversuch PRIMUS", true, false, false, false, null, null)]);

	/**
	 * Schulform Realschule
	 */
	public static readonly R : Schulform = new Schulform("R", 9, [new SchulformKatalogEintrag(10000, "R", "10", "Realschule", true, false, false, false, null, null)]);

	/**
	 * Schulform Förderschule im Bereich G/H
	 */
	public static readonly S : Schulform = new Schulform("S", 10, [new SchulformKatalogEintrag(11000, "S", "08", "Förderschule im Bereich G/H", true, false, false, false, null, null)]);

	/**
	 * Schulform Klinikschule
	 */
	public static readonly KS : Schulform = new Schulform("KS", 11, [new SchulformKatalogEintrag(12000, "KS", "83", "Klinikschule", true, true, false, false, null, null)]);

	/**
	 * Schulform Förderschule im Bereich Berufskolleg
	 */
	public static readonly SB : Schulform = new Schulform("SB", 12, [new SchulformKatalogEintrag(13000, "SB", "88", "Förderschule im Bereich Berufskolleg", true, false, false, false, null, null)]);

	/**
	 * Schulform Förderschule im Bereich Gymnasium
	 */
	public static readonly SG : Schulform = new Schulform("SG", 13, [new SchulformKatalogEintrag(14000, "SG", "87", "Förderschule im Bereich Gymnasium", true, false, false, true, null, null)]);

	/**
	 * Schulform Sekundarschule
	 */
	public static readonly SK : Schulform = new Schulform("SK", 14, [new SchulformKatalogEintrag(15000, "SK", "14", "Sekundarschule", true, false, false, false, null, null)]);

	/**
	 * Schulform Förderschule im Bereich Realschule
	 */
	public static readonly SR : Schulform = new Schulform("SR", 15, [new SchulformKatalogEintrag(16000, "SR", "85", "Förderschule im Bereich Realschule", true, false, false, false, null, null)]);

	/**
	 * Schulform nicht umorganisierte Volksschule
	 */
	public static readonly V : Schulform = new Schulform("V", 16, [new SchulformKatalogEintrag(17000, "V", "06", "nicht umorganisierte Volksschule", true, false, false, false, null, null)]);

	/**
	 * Schulform Weiterbildungskolleg
	 */
	public static readonly WB : Schulform = new Schulform("WB", 17, [new SchulformKatalogEintrag(18000, "WB", "25", "Weiterbildungskolleg", false, false, true, false, null, null)]);

	/**
	 * Schulform Freie Waldorfschule (Förderschule)
	 */
	public static readonly WF : Schulform = new Schulform("WF", 18, [new SchulformKatalogEintrag(19000, "WF", "19", "Freie Waldorfschule (Förderschule)", true, true, false, true, null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Schulform, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : SchulformKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Schulformen
	 */
	public readonly historie : Array<SchulformKatalogEintrag>;

	/**
	 * Ein ArrayList mit allen definierten Schulformen
	 */
	private static readonly _schulformen : HashMap<string, Schulform | null> = new HashMap<string, Schulform | null>();

	/**
	 * Ein ArrayList mit allen definierten Schulformen, die eine Statistiknummer zugewiesen haben.
	 */
	private static readonly _schulformenNummer : HashMap<string, Schulform | null> = new HashMap<string, Schulform | null>();

	/**
	 * Eine Map mit allen Historien-Einträgen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _mapEintragById : HashMap<number, SchulformKatalogEintrag | null> = new HashMap<number, SchulformKatalogEintrag | null>();

	/**
	 * Erzeugt eine neue Schulform in der Aufzählung.
	 *
	 * @param historie   die Historie der Schulformen, welches ein Array von {@link SchulformKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<SchulformKatalogEintrag>) {
		super(name, ordinal);
		Schulform.all_values_by_ordinal.push(this);
		Schulform.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 */
	private static getMapSchulformenByKuerzel() : HashMap<string, Schulform | null> {
		if (Schulform._schulformen.size() === 0) {
			for (const s of Schulform.values()) {
				if (s.daten !== null)
					Schulform._schulformen.put(s.daten.kuerzel, s);
			}
		}
		return Schulform._schulformen;
	}

	/**
	 * Gibt eine Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 */
	private static getMapSchulformenByNummer() : HashMap<string, Schulform | null> {
		if (Schulform._schulformenNummer.size() === 0)
			for (const s of Schulform.values())
				if ((s.daten !== null) && (s.daten.nummer !== null))
					Schulform._schulformenNummer.put(s.daten.nummer, s);
		return Schulform._schulformenNummer;
	}

	/**
	 * Gibt eine Map von den IDs der Schulform-Katalog-Einträge auf die zugehörigen
	 * Schulform-Katalog-Einträge zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulform-Katalog-Einträge auf die zugehörigen Schulform-Katalog-Einträge
	 */
	private static getMapEintragById() : HashMap<number, SchulformKatalogEintrag | null> {
		if (Schulform._mapEintragById.size() === 0) {
			for (const s of Schulform.values()) {
				for (const e of s.historie)
					Schulform._mapEintragById.put(e.id, e);
			}
		}
		return Schulform._mapEintragById;
	}

	/**
	 * Gibt die Schulform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return die Schulform oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Schulform | null {
		return Schulform.getMapSchulformenByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt die Schulform für die angegebene Nummer zurück.
	 *
	 * @param nummer   die Nummer der Schulform
	 *
	 * @return die Schulform oder null, falls keine Schulform mit dieser Nummer vorhanden ist
	 */
	public static getByNummer(nummer : string | null) : Schulform | null {
		return Schulform.getMapSchulformenByNummer().get(nummer);
	}

	/**
	 * Gibt den Schulform-Katalog-Eintrag anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Schulform-Katalog-Eintrag oder null, falls kein Eintrag mit dieser ID vorhanden ist
	 */
	public static getEintragByID(id : number) : SchulformKatalogEintrag | null {
		return Schulform.getMapEintragById().get(id);
	}

	/**
	 * Gibt alle "echten" Schulformen dieser Aufzählung zurück.
	 * Das bedeutet, dass Pseudoschulformen, die in NRW nicht
	 * existieren, nicht zurückgegeben werden.
	 *
	 * @return eine {@link List} mit alle "echten" Schulformen
	 */
	public static get() : List<Schulform> {
		const result : ArrayList<Schulform> = new ArrayList<Schulform>();
		for (const sf of Schulform.values())
			if ((sf.daten !== null) && (sf.daten.nummer !== null))
				result.add(sf);
		return result;
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück.
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static getMitGymOb() : List<Schulform> {
		const result : ArrayList<Schulform> = new ArrayList<Schulform>();
		for (const sf of Schulform.values())
			if (sf.daten.hatGymOb)
				result.add(sf);
		return result;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Schulform> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Schulform | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.Schulform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Schulform', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Schulform(obj : unknown) : Schulform {
	return obj as Schulform;
}
