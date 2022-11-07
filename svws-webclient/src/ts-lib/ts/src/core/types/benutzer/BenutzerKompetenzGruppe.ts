import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { BenutzerKompetenzGruppenKatalogEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzGruppenKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzGruppenKatalogEintrag';

export class BenutzerKompetenzGruppe extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BenutzerKompetenzGruppe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, BenutzerKompetenzGruppe> = new Map<String, BenutzerKompetenzGruppe>();

	public static readonly KEINE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("KEINE", 0, new BenutzerKompetenzGruppenKatalogEintrag(-2, "keine", -2, -2));

	public static readonly ADMIN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ADMIN", 1, new BenutzerKompetenzGruppenKatalogEintrag(-1, "admin", -1, -1));

	public static readonly SCHUELER_INDIVIDUALDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHUELER_INDIVIDUALDATEN", 2, new BenutzerKompetenzGruppenKatalogEintrag(100, "Schüler Individualdaten", 1, 1));

	public static readonly SCHUELER_LEISTUNGSDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHUELER_LEISTUNGSDATEN", 3, new BenutzerKompetenzGruppenKatalogEintrag(200, "Schüler Leistungsdaten", 1, 2));

	public static readonly BERICHTE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("BERICHTE", 4, new BenutzerKompetenzGruppenKatalogEintrag(300, "Berichte", 2, 3));

	public static readonly IMPORT_EXPORT : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("IMPORT_EXPORT", 5, new BenutzerKompetenzGruppenKatalogEintrag(400, "Import/Export", 3, 1));

	public static readonly BLOCK_OPERATIONEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("BLOCK_OPERATIONEN", 6, new BenutzerKompetenzGruppenKatalogEintrag(500, "Blockoperationen", 3, 2));

	public static readonly SCHULBEZOGENE_DATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHULBEZOGENE_DATEN", 7, new BenutzerKompetenzGruppenKatalogEintrag(600, "Schulbezogene Daten", 3, 3));

	public static readonly EXTRAS : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("EXTRAS", 8, new BenutzerKompetenzGruppenKatalogEintrag(700, "Extras", 3, 4));

	public static readonly KATALOG_EINTRAEGE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("KATALOG_EINTRAEGE", 9, new BenutzerKompetenzGruppenKatalogEintrag(800, "Katalog-Einträge", 1, 3));

	public static readonly LEHRERDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("LEHRERDATEN", 10, new BenutzerKompetenzGruppenKatalogEintrag(900, "Lehrerdaten", 2, 2));

	public static readonly SCHULPFLICHTVERLETZUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHULPFLICHTVERLETZUNG", 11, new BenutzerKompetenzGruppenKatalogEintrag(1000, "Verfahren Schulpflichtverletzung", 2, 3));

	public static readonly STUNDENPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("STUNDENPLANUNG", 12, new BenutzerKompetenzGruppenKatalogEintrag(1100, "Stundenplanung", 4, 1));

	public static readonly NOTENMODUL : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("NOTENMODUL", 13, new BenutzerKompetenzGruppenKatalogEintrag(1300, "Notenmodul", 4, 2));

	public static readonly DATENBANK : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("DATENBANK", 14, new BenutzerKompetenzGruppenKatalogEintrag(1400, "Datenbank-Management", 4, 3));

	public static readonly OBERSTUFE_LAUFBAHNPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_LAUFBAHNPLANUNG", 15, new BenutzerKompetenzGruppenKatalogEintrag(1600, "Oberstufe - Laufbahnplanung", 5, 1));

	public static readonly OBERSTUFE_KURSPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_KURSPLANUNG", 16, new BenutzerKompetenzGruppenKatalogEintrag(1700, "Oberstufe - Kursplanung", 5, 2));

	public static readonly OBERSTUFE_KLAUSURPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_KLAUSURPLANUNG", 17, new BenutzerKompetenzGruppenKatalogEintrag(1800, "Oberstufe - -Klausurplanung", 5, 3));

	public static readonly ABITUR : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABITUR", 18, new BenutzerKompetenzGruppenKatalogEintrag(1900, "Abitur", 5, 4));

	public static readonly CARDDAV : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("CARDDAV", 19, new BenutzerKompetenzGruppenKatalogEintrag(2000, "Addressbuch (CardDAV)", 1, 4));

	public static readonly CALDAV : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("CALDAV", 20, new BenutzerKompetenzGruppenKatalogEintrag(3000, "Kalender (CaldDAV)", 1, 5));

	public static readonly ABSCHLUSS_SEKI : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABSCHLUSS_SEKI", 21, new BenutzerKompetenzGruppenKatalogEintrag(4000, "Abschlussberechnung Sek I", 2, 1));

	public static readonly ABSCHLUSS_BK : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABSCHLUSS_BK", 22, new BenutzerKompetenzGruppenKatalogEintrag(5000, "Abschlussberechnung berufsbildende Schule", 2, 2));

	public static VERSION : number = 1;

	public readonly daten : BenutzerKompetenzGruppenKatalogEintrag;

	private static readonly _mapID : HashMap<Number, BenutzerKompetenzGruppe> = new HashMap();

	/**
	 * Erzeugt eine neue Benutzerkompetenz-Gruppe für die Aufzählung.
	 *
	 * @param daten    die Daten der Benutzerkompetenz-Gruppe
	 */
	private constructor(name : string, ordinal : number, daten : BenutzerKompetenzGruppenKatalogEintrag) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BenutzerKompetenzGruppe.all_values_by_ordinal.push(this);
		BenutzerKompetenzGruppe.all_values_by_name.set(name, this);
		this.daten = daten;
	}

	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 */
	private static getMapID() : HashMap<Number, BenutzerKompetenzGruppe> {
		if (BenutzerKompetenzGruppe._mapID.size() === 0) 
			for (let p of BenutzerKompetenzGruppe.values()) 
				BenutzerKompetenzGruppe._mapID.put(p.daten.id, p);
		return BenutzerKompetenzGruppe._mapID;
	}

	/**
	 *
	 * Gibt die Benutzerkompetenz-Gruppen anhand der übergebenen ID zurück. 
	 * 
	 * @param id    die ID der Benutzerkompetenz-Gruppen
	 *  
	 * @return die Benutzerkompetenz-Gruppen oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : BenutzerKompetenzGruppe | null {
		return BenutzerKompetenzGruppe.getMapID().get(id);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
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
	public toString() : String {
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
		if (!(other instanceof BenutzerKompetenzGruppe))
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
	public compareTo(other : BenutzerKompetenzGruppe) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BenutzerKompetenzGruppe> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : BenutzerKompetenzGruppe | null {
		let tmp : BenutzerKompetenzGruppe | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenzGruppe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_benutzer_BenutzerKompetenzGruppe(obj : unknown) : BenutzerKompetenzGruppe {
	return obj as BenutzerKompetenzGruppe;
}
