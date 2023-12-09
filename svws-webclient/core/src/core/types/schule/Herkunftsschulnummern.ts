import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { HerkunftsschulnummerKatalogEintrag } from '../../../core/data/schule/HerkunftsschulnummerKatalogEintrag';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { Arrays } from '../../../java/util/Arrays';
import { Pair } from '../../../core/adt/Pair';

export class Herkunftsschulnummern extends JavaEnum<Herkunftsschulnummern> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Herkunftsschulnummern> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Herkunftsschulnummern> = new Map<string, Herkunftsschulnummern>();

	/**
	 * Herkunft Schulnummer : Schule aus dem sonstigen Ausland
	 */
	public static readonly SONSTIGES_AUSLAND : Herkunftsschulnummern = new Herkunftsschulnummern("SONSTIGES_AUSLAND", 0, [new HerkunftsschulnummerKatalogEintrag(999000000, 999000, "Schule aus dem sonstigen Ausland", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Schleswig-Holstein
	 */
	public static readonly SCHLESWIG_HOLSTEIN : Herkunftsschulnummern = new Herkunftsschulnummern("SCHLESWIG_HOLSTEIN", 1, [new HerkunftsschulnummerKatalogEintrag(999001000, 999001, "Schule aus Schleswig-Holstein", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Hamburg
	 */
	public static readonly HAMBURG : Herkunftsschulnummern = new Herkunftsschulnummern("HAMBURG", 2, [new HerkunftsschulnummerKatalogEintrag(999002000, 999002, "Schule aus Hamburg", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Niedersachsen
	 */
	public static readonly NIEDERSACHSEN : Herkunftsschulnummern = new Herkunftsschulnummern("NIEDERSACHSEN", 3, [new HerkunftsschulnummerKatalogEintrag(999003000, 999003, "Schule aus Niedersachsen", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Bremen
	 */
	public static readonly BREMEN : Herkunftsschulnummern = new Herkunftsschulnummern("BREMEN", 4, [new HerkunftsschulnummerKatalogEintrag(999004000, 999004, "Schule aus Bremen", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Hessen
	 */
	public static readonly HESSEN : Herkunftsschulnummern = new Herkunftsschulnummern("HESSEN", 5, [new HerkunftsschulnummerKatalogEintrag(999006000, 999006, "Schule aus Hessen", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Rheinland-Pfalz
	 */
	public static readonly RHEINLANDPFALZ : Herkunftsschulnummern = new Herkunftsschulnummern("RHEINLANDPFALZ", 6, [new HerkunftsschulnummerKatalogEintrag(999007000, 999007, "Schule aus Rheinland-Pfalz", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Baden-Württemberg
	 */
	public static readonly BADEN_WUERTTEMBERG : Herkunftsschulnummern = new Herkunftsschulnummern("BADEN_WUERTTEMBERG", 7, [new HerkunftsschulnummerKatalogEintrag(999008000, 999008, "Schule aus Baden-Württemberg", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Bayern
	 */
	public static readonly BAYERN : Herkunftsschulnummern = new Herkunftsschulnummern("BAYERN", 8, [new HerkunftsschulnummerKatalogEintrag(999009000, 999009, "Schule aus Bayern", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus dem Saarland
	 */
	public static readonly SAARLAND : Herkunftsschulnummern = new Herkunftsschulnummern("SAARLAND", 9, [new HerkunftsschulnummerKatalogEintrag(999010000, 999010, "Schule aus dem Saarland", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Berlin
	 */
	public static readonly BERLIN : Herkunftsschulnummern = new Herkunftsschulnummern("BERLIN", 10, [new HerkunftsschulnummerKatalogEintrag(999011000, 999011, "Schule aus Berlin", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Brandenburg
	 */
	public static readonly BRANDENBURG : Herkunftsschulnummern = new Herkunftsschulnummern("BRANDENBURG", 11, [new HerkunftsschulnummerKatalogEintrag(999012000, 999012, "Schule aus Brandenburg", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Mecklenburg-Vorpommern
	 */
	public static readonly MECKLENBURG_VORPOMMERN : Herkunftsschulnummern = new Herkunftsschulnummern("MECKLENBURG_VORPOMMERN", 12, [new HerkunftsschulnummerKatalogEintrag(999013000, 999013, "Schule aus Mecklenburg-Vorpommern", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Sachsen
	 */
	public static readonly SACHSEN : Herkunftsschulnummern = new Herkunftsschulnummern("SACHSEN", 13, [new HerkunftsschulnummerKatalogEintrag(999014000, 999014, "Schule aus Sachsen", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Sachsen-Anhalt
	 */
	public static readonly SACHSEN_ANHALT : Herkunftsschulnummern = new Herkunftsschulnummern("SACHSEN_ANHALT", 14, [new HerkunftsschulnummerKatalogEintrag(999015000, 999015, "Schule aus Sachsen-Anhalt", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Thüringen
	 */
	public static readonly THUERINGEN : Herkunftsschulnummern = new Herkunftsschulnummern("THUERINGEN", 15, [new HerkunftsschulnummerKatalogEintrag(999016000, 999016, "Schule aus Thüringen", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Belgien
	 */
	public static readonly BELGIEN : Herkunftsschulnummern = new Herkunftsschulnummern("BELGIEN", 16, [new HerkunftsschulnummerKatalogEintrag(991000000, 991000, "Schule aus Belgien", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus Luxemburg
	 */
	public static readonly LUXEMBURG : Herkunftsschulnummern = new Herkunftsschulnummern("LUXEMBURG", 17, [new HerkunftsschulnummerKatalogEintrag(992000000, 992000, "Schule aus Luxemburg", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Schule aus den Niederlanden
	 */
	public static readonly NIEDERLANDE : Herkunftsschulnummern = new Herkunftsschulnummern("NIEDERLANDE", 18, [new HerkunftsschulnummerKatalogEintrag(993000000, 993000, "Schule aus den Niederlanden", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Sonstige/keine Schule
	 */
	public static readonly SONSTIGE : Herkunftsschulnummern = new Herkunftsschulnummern("SONSTIGE", 19, [new HerkunftsschulnummerKatalogEintrag(980500000, 980500, "Sonstige/keine Schule", null, null, null)]);

	/**
	 * Herkunft Schulnummer : Herkunft noch unbekannt (nur A12, A13)
	 */
	public static readonly UNBEKANNT : Herkunftsschulnummern = new Herkunftsschulnummern("UNBEKANNT", 20, [new HerkunftsschulnummerKatalogEintrag(999500000, 999500, "Herkunft noch unbekannt (nur A12, A13)", Arrays.asList(new Pair(Schulform.BK, Schulgliederung.A12), new Pair(Schulform.BK, Schulgliederung.A13), new Pair(Schulform.SB, Schulgliederung.A12), new Pair(Schulform.SB, Schulgliederung.A13)), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Herkunftsschulnummer, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : HerkunftsschulnummerKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Herkunftsschulnummer
	 */
	public readonly historie : Array<HerkunftsschulnummerKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Herkunftsschulnummern, zugeordnet zu ihren Schulnummern
	 */
	private static readonly _mapBySchulnummer : HashMap<number, Herkunftsschulnummern | null> = new HashMap();

	/**
	 * Erzeugt eine neue Herkunftsschulnummer in der Aufzählung.
	 *
	 * @param historie   die Historie der Herkunftsschulnummer, welches ein Array von {@link HerkunftsschulnummerKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftsschulnummerKatalogEintrag>) {
		super(name, ordinal);
		Herkunftsschulnummern.all_values_by_ordinal.push(this);
		Herkunftsschulnummern.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Herkunftsschulnummern auf die zugehörigen Herkunftsschulnummern
	 */
	private static getMapBySchulnummer() : HashMap<number, Herkunftsschulnummern | null> {
		if (Herkunftsschulnummern._mapBySchulnummer.size() === 0) {
			for (const s of Herkunftsschulnummern.values()) {
				if (s.daten !== null)
					Herkunftsschulnummern._mapBySchulnummer.put(s.daten.schulnummer, s);
			}
		}
		return Herkunftsschulnummern._mapBySchulnummer;
	}

	/**
	 * Gibt die Herkunftsschulnummer für die angegebene Schulnummer zurück.
	 *
	 * @param nummer   die Schulnummer
	 *
	 * @return die Herkunftsschulnummer oder null, falls die Schulnummer hier nicht vohanden ist
	 */
	public static getByKuerzel(nummer : number | null) : Herkunftsschulnummern | null {
		return Herkunftsschulnummern.getMapBySchulnummer().get(nummer);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Herkunftsschulnummern> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Herkunftsschulnummern | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Herkunftsschulnummern', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Herkunftsschulnummern(obj : unknown) : Herkunftsschulnummern {
	return obj as Herkunftsschulnummern;
}
