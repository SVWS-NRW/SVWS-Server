import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Collection, cast_java_util_Collection } from '../../../java/util/Collection';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { IllegalArgumentException, cast_java_lang_IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostKursart extends JavaObject {

	private static readonly map : HashMap<String, GostKursart> = new HashMap();

	public static readonly LK : GostKursart = new GostKursart(1, "LK", "Leistungskurs");

	public static readonly GK : GostKursart = new GostKursart(2, "GK", "Grundkurs");

	public static readonly ZK : GostKursart = new GostKursart(3, "ZK", "Zusatzkurs");

	public static readonly PJK : GostKursart = new GostKursart(4, "PJK", "Projektkurs");

	public static readonly VTF : GostKursart = new GostKursart(5, "VTF", "Vertiefungskurs");

	public readonly id : number;

	public readonly kuerzel : String;

	public readonly beschreibung : String;


	/**
	 * Erzeugt eine neue Kursart für die Aufzählung.
	 * 
	 * @param id             die eindeutige ID der Kursart der Gymnasialen Oberstufe
	 * @param kuerzel        das Kürzel der Kursart der Gymnasialen Oberstufe
	 * @param beschreibung   die textuelle Beschreibung der allgemeinen Kursart der Gymnasialen Oberstufe
	 */
	private constructor(id : number, kuerzel : String, beschreibung : String) {
		super();
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		GostKursart.map.put(kuerzel, this);
	}

	/**
	 * Prüft die Anzahl der Wochenstunden zu der Kursart.
	 * 
	 * @param anzahl   Anzahl der Wochenstunden
	 * 
	 * @return         Anzahl der Wochenstunden der Kursart korrekt, true oder false
	 */
	public pruefeWochenstunden(anzahl : number) : boolean {
		switch (this.kuerzel) {
			case "GK": 
				return (anzahl === 3) || (anzahl === 4);
			case "LK": 
				return (anzahl === 5);
			case "PJK": 
				return (anzahl === 2) || (anzahl === 3);
			case "VTF": 
				return (anzahl === 2);
			case "ZK": 
				return (anzahl === 3);
		}
		return false;
	}

	/**
	 * Gibt alle Kursarten der gymnasialen Oberstufe zurück.
	 * 
	 * @return eine {@link Collection} mit den Kursarten.
	 */
	public static values() : Collection<GostKursart> {
		return GostKursart.map.values();
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 * 
	 * @param id    die ID der Kursart
	 * 
	 * @return die Kursart
	 * 
	 * @throws IllegalArgumentException falls die ID ungültig ist 
	 */
	public static fromID(id : number) : GostKursart {
		switch (id) {
			case 1: 
				return GostKursart.LK;
			case 2: 
				return GostKursart.GK;
			case 3: 
				return GostKursart.ZK;
			case 4: 
				return GostKursart.PJK;
			case 5: 
				return GostKursart.VTF;
		}
		throw new IllegalArgumentException("Invalid ID value.")
	}

	/**
	 * Gibt die Kursart aus der ID Kursart zurück.
	 * 
	 * @param id    die ID der Kursart
	 * 
	 * @return die Kursart oder null falls die ID ungültig ist 
	 */
	public static fromIDorNull(id : number) : GostKursart | null {
		switch (id) {
			case 1: 
				return GostKursart.LK;
			case 2: 
				return GostKursart.GK;
			case 3: 
				return GostKursart.ZK;
			case 4: 
				return GostKursart.PJK;
			case 5: 
				return GostKursart.VTF;
		}
		return null;
	}

	/**
	 * Gibt die Kursart aus dem Kürzel der Kursart zurück.
	 * 
	 * @param kuerzel    das Kürzel der Kursart
	 * 
	 * @return die Kursart oder null, falls das Kürzel ungültig ist 
	 */
	public static fromKuerzel(kuerzel : String | null) : GostKursart | null {
		return GostKursart.map.get(kuerzel);
	}

	public toString() : String {
		return this.kuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostKursart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostKursart(obj : unknown) : GostKursart {
	return obj as GostKursart;
}
