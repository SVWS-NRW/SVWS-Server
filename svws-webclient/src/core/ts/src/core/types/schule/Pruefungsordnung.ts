import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { PruefungsordnungKatalogEintrag } from '../../../core/data/schule/PruefungsordnungKatalogEintrag';

export class Pruefungsordnung extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Pruefungsordnung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Pruefungsordnung> = new Map<string, Pruefungsordnung>();

	/**
	 * APO-BK: Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs
	 */
	public static readonly APO_BK : Pruefungsordnung = new Pruefungsordnung("APO_BK", 0, [new PruefungsordnungKatalogEintrag(1000, "APO-BK", "APO-BK-99", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 1999, "26", "239-384", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1999&p_nr=26", 1999, 2002), new PruefungsordnungKatalogEintrag(1001, "APO-BK", "APO-BK-03", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2003, "32", "357-368", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=10290&ver=8&val=10290&sg=0&menu=0&vd_back=N", 2003, 2010), new PruefungsordnungKatalogEintrag(1002, "APO-BK", "APO-BK-11", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2011, "17", "361-376", "https://recht.nrw.de/lmi/owa/br_show_historie?p_id=20405", 2011, 2011), new PruefungsordnungKatalogEintrag(1003, "APO-BK", "APO-BK-13", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2012, "23", "421-438", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=13493&ver=8&val=13493&sg=0&menu=0&vd_back=N", 2012, 2014), new PruefungsordnungKatalogEintrag(1004, "APO-BK", "APO-BK-15", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2015, "2", "13-38", "https://bass.schul-welt.de/3129.htm", 2015, null)]);

	/**
	 * APO-GOSt: Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe
	 */
	public static readonly APO_GOST : Pruefungsordnung = new Pruefungsordnung("APO_GOST", 1, [new PruefungsordnungKatalogEintrag(2000, "APO-GOSt", null, "Verordnung über den Bildungsgang und die Abiturprüfung in der Oberstufe des Gymnasiums (Ausbildungs- und Prüfungsordnung gemäß §26b SchVG – APO – OStG)", 1979, "20", "248-259", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1979&p_nr=20", 1979, 1998), new PruefungsordnungKatalogEintrag(2001, "APO-GOSt", "APO-GOSt01", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 1998, "43", "593-608", "https://bass.schul-welt.de/9607.htm", 1999, 2011), new PruefungsordnungKatalogEintrag(2002, "APO-GOSt", "APO-GOSt02", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 1998, "43", "593-608", "https://bass.schul-welt.de/9607.htm", 1999, 2011), new PruefungsordnungKatalogEintrag(2003, "APO-GOSt", "APO-GOSt(C)10", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 2009, "8", "177-184", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=11319&ver=8&val=11319&sg=0&menu=0&vd_back=N", 2010, 2012), new PruefungsordnungKatalogEintrag(2004, "APO-GOSt", "APO-GOSt(B)10", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 2009, "26", "529-538", "https://bass.schul-welt.de/9607.htm", 2010, null)]);

	/**
	 * APO-WbK: Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs
	 */
	public static readonly APO_WBK : Pruefungsordnung = new Pruefungsordnung("APO_WBK", 2, [new PruefungsordnungKatalogEintrag(3000, "APO-WbK", "APO-WbK-00", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs (Ausbildungs- und Prüfungsordnung Weiterbildungskolleg - APO-WbK)", 2000, "19", "289-308", "https://recht.nrw.de/lmi/owa/br_show_historie?p_id=2661", 2000, 2009), new PruefungsordnungKatalogEintrag(3001, "APO-WbK", "APO-WBK10", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs (Ausbildungs- und Prüfungsordnung Weiterbildungskolleg - APO-WbK)", 2010, "8", "143-154", "https://bass.schul-welt.de/3693.htm", 2010, null)]);

	/**
	 * AO-GS: Verordnung über den Bildungsgang in der Grundschule
	 */
	public static readonly AO_GS : Pruefungsordnung = new Pruefungsordnung("AO_GS", 3, [new PruefungsordnungKatalogEintrag(4000, "AO-GS", "AO-GS00", "Verordnung über den Bildungsgang in der Grundschule (Ausbildungsordnung gemäß §26b SchVG - AO-GS)", 1979, "34", "465-467", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1979&p_nr=34", 1979, null), new PruefungsordnungKatalogEintrag(4001, "AO-GS", "AO-GS05", "Verordnung über den Bildungsgang in der Grundschule (Ausbildungsordnung Grundschule - AO-GS)", 2005, "16", "251-272", "https://bass.schul-welt.de/6181.htm", 2005, null)]);

	/**
	 * AO-SI: Verordnung über die Ausbildung in der Sekundarstufe I
	 */
	public static readonly AO_SI : Pruefungsordnung = new Pruefungsordnung("AO_SI", 4, [new PruefungsordnungKatalogEintrag(5000, "AO-SI", "AO-SI99", "Verordnung über die Ausbildung in der Sekundarstufe I (Ausbildungsordnung Sekundarstufe I - AO-S I)", 1998, "45", "631-648", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=2148&ver=8&val=2148&sg=0&menu=1&vd_back=N", 1999, 2004)]);

	/**
	 * APO-SI: Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I
	 */
	public static readonly APO_SI : Pruefungsordnung = new Pruefungsordnung("APO_SI", 5, [new PruefungsordnungKatalogEintrag(6000, "APO-SI", "APO-SI05", "Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I (Ausbildungs- und Prüfungsordnung Sekundarstufe I – APO-S I)", 2005, "24", "535-566", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=3779&vd_back=N546&sg=0&menu=1", 2005, 2012), new PruefungsordnungKatalogEintrag(6001, "APO-SI", "APO-SI05", "Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I (Ausbildungs- und Prüfungsordnung Sekundarstufe I – APO-S I)", 2012, "27", "487-506", "https://bass.schul-welt.de/12691.htm", 2013, null)]);

	/**
	 * AOSF: Verordnung über die sonderpädagogische Förderung, den Hausunterricht und die Schule für Kranke
	 */
	public static readonly AOSF : Pruefungsordnung = new Pruefungsordnung("AOSF", 6, [new PruefungsordnungKatalogEintrag(8000, "AOSF", "AOSF-SI05", "Verordnung über die sonderpädagogische Förderung, den Hausunterricht und die Schule für Kranke (Ausbildungsordnung gemäß § 52 SchulG - AO-SF)", 2005, "24", "535-566", "https://bass.schul-welt.de/6225.htm", 2005, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Verordnung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : PruefungsordnungKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Verordnung
	 */
	public readonly historie : Array<PruefungsordnungKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Verordnungen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _verordnungen : HashMap<string, Pruefungsordnung> = new HashMap();

	/**
	 * Erzeugt eine neue Verordnung in der Aufzählung.
	 *
	 * @param historie   die Historie der Verordnung, welches ein Array von {@link PruefungsordnungKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<PruefungsordnungKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Pruefungsordnung.all_values_by_ordinal.push(this);
		Pruefungsordnung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Prüfungsordnungen auf die zugehörigen Prüfungsordnungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Prüfungsordnungen auf die zugehörigen Prüfungsordnungen
	 */
	private static getMapPruefungsordnungByKuerzel() : HashMap<string, Pruefungsordnung> {
		if (Pruefungsordnung._verordnungen.size() === 0) {
			for (const s of Pruefungsordnung.values()) {
				if (s.daten !== null)
					Pruefungsordnung._verordnungen.put(s.daten.kuerzel, s);
			}
		}
		return Pruefungsordnung._verordnungen;
	}

	/**
	 * Gibt die Prüfungsordnung für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Prüfungsordnung
	 *
	 * @return die Prüfungsordnung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Pruefungsordnung | null {
		return Pruefungsordnung.getMapPruefungsordnungByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
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
		if (!(other instanceof Pruefungsordnung))
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
	public compareTo(other : Pruefungsordnung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Pruefungsordnung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Pruefungsordnung | null {
		const tmp : Pruefungsordnung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Pruefungsordnung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Pruefungsordnung(obj : unknown) : Pruefungsordnung {
	return obj as Pruefungsordnung;
}
