import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { BenutzerKompetenzGruppenKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzGruppenKatalogEintrag';

export class BenutzerKompetenzGruppe extends JavaEnum<BenutzerKompetenzGruppe> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BenutzerKompetenzGruppe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BenutzerKompetenzGruppe> = new Map<string, BenutzerKompetenzGruppe>();

	/**
	 * Es werden keinerlei Kompetenzen benötigt.
	 */
	public static readonly KEINE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("KEINE", 0, new BenutzerKompetenzGruppenKatalogEintrag(-2, "keine", -2, -2));

	/**
	 * Es werden Admin-Rechte benötigt.
	 */
	public static readonly ADMIN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ADMIN", 1, new BenutzerKompetenzGruppenKatalogEintrag(-1, "admin", -1, -1));

	/**
	 * Gruppe für Rechte bezüglich der Schüler-Individualdaten.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHUELER_INDIVIDUALDATEN", 2, new BenutzerKompetenzGruppenKatalogEintrag(100, "Schüler Individualdaten", 1, 1));

	/**
	 * Gruppe für Rechte bezüglich der Schüler-Leistungsdaten.
	 */
	public static readonly SCHUELER_LEISTUNGSDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHUELER_LEISTUNGSDATEN", 3, new BenutzerKompetenzGruppenKatalogEintrag(200, "Schüler Leistungsdaten", 1, 2));

	/**
	 * Gruppe für Rechte für Berichte.
	 */
	public static readonly BERICHTE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("BERICHTE", 4, new BenutzerKompetenzGruppenKatalogEintrag(300, "Berichte", 2, 3));

	/**
	 * Gruppe für Rechte für den Import/Export von Daten.
	 */
	public static readonly IMPORT_EXPORT : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("IMPORT_EXPORT", 5, new BenutzerKompetenzGruppenKatalogEintrag(400, "Import/Export", 3, 1));

	/**
	 * Gruppe für Rechte bei Block-Operationen.
	 */
	public static readonly BLOCK_OPERATIONEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("BLOCK_OPERATIONEN", 6, new BenutzerKompetenzGruppenKatalogEintrag(500, "Blockoperationen", 3, 2));

	/**
	 * Gruppe für Rechte bezüglich Schulbezogenener Daten.
	 */
	public static readonly SCHULBEZOGENE_DATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHULBEZOGENE_DATEN", 7, new BenutzerKompetenzGruppenKatalogEintrag(600, "Schulbezogene Daten", 3, 3));

	/**
	 * Gruppe für Rechte für spezielle Operationen.
	 */
	public static readonly EXTRAS : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("EXTRAS", 8, new BenutzerKompetenzGruppenKatalogEintrag(700, "Extras", 3, 4));

	/**
	 * Gruppe für Rechte bezüglich Kataloge.
	 */
	public static readonly KATALOG_EINTRAEGE : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("KATALOG_EINTRAEGE", 9, new BenutzerKompetenzGruppenKatalogEintrag(800, "Katalog-Einträge", 1, 3));

	/**
	 * Gruppe für Rechte bezüglich Lehrerdaten.
	 */
	public static readonly LEHRERDATEN : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("LEHRERDATEN", 10, new BenutzerKompetenzGruppenKatalogEintrag(900, "Lehrerdaten", 2, 2));

	/**
	 * Gruppe für Rechte bezüglich des Verfahrens zur Schulpflichverletzung.
	 */
	public static readonly SCHULPFLICHTVERLETZUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("SCHULPFLICHTVERLETZUNG", 11, new BenutzerKompetenzGruppenKatalogEintrag(1000, "Verfahren Schulpflichtverletzung", 2, 3));

	/**
	 * Gruppe für Rechte bezüglich der Stundenplanung.
	 */
	public static readonly STUNDENPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("STUNDENPLANUNG", 12, new BenutzerKompetenzGruppenKatalogEintrag(1100, "Stundenplanung", 4, 1));

	/**
	 * Gruppe für Rechte bezüglich des externen Notenmoduls.
	 */
	public static readonly NOTENMODUL : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("NOTENMODUL", 13, new BenutzerKompetenzGruppenKatalogEintrag(1300, "Notenmodul", 4, 2));

	/**
	 * Gruppe für Rechte bezüglich des Datenbank-Managements.
	 */
	public static readonly DATENBANK : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("DATENBANK", 14, new BenutzerKompetenzGruppenKatalogEintrag(1400, "Datenbank-Management", 4, 3));

	/**
	 * Gruppe für Rechte bezüglich der Laufbahnplanung der Gymnasialen Oberstufe.
	 */
	public static readonly OBERSTUFE_LAUFBAHNPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_LAUFBAHNPLANUNG", 15, new BenutzerKompetenzGruppenKatalogEintrag(1600, "Oberstufe - Laufbahnplanung", 5, 1));

	/**
	 * Gruppe für Rechte bezüglich der Kursplanung der Gymnasialen Oberstufe.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_KURSPLANUNG", 16, new BenutzerKompetenzGruppenKatalogEintrag(1700, "Oberstufe - Kursplanung", 5, 2));

	/**
	 * Gruppe für Rechte bezüglich der Klausurplanung der Gymnasialen Oberstufe.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("OBERSTUFE_KLAUSURPLANUNG", 17, new BenutzerKompetenzGruppenKatalogEintrag(1800, "Oberstufe - Klausurplanung", 5, 3));

	/**
	 * Gruppe für Rechte bezüglich des Abiturs.
	 */
	public static readonly ABITUR : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABITUR", 18, new BenutzerKompetenzGruppenKatalogEintrag(1900, "Abitur", 5, 4));

	/**
	 * Gruppe für Rechte bezüglich des Adressbuchs.
	 */
	public static readonly CARDDAV : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("CARDDAV", 19, new BenutzerKompetenzGruppenKatalogEintrag(2000, "Addressbuch (CardDAV)", 1, 4));

	/**
	 * Gruppe für Rechte bezüglich der Kalender.
	 */
	public static readonly CALDAV : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("CALDAV", 20, new BenutzerKompetenzGruppenKatalogEintrag(3000, "Kalender (CalDAV)", 1, 5));

	/**
	 * Gruppe für Rechte für die Abschlussberechnung in der Sekundarstufe I.
	 */
	public static readonly ABSCHLUSS_SEKI : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABSCHLUSS_SEKI", 21, new BenutzerKompetenzGruppenKatalogEintrag(4000, "Abschlussberechnung Sek I", 2, 1));

	/**
	 * Gruppe für Rechte für die Abschlussberechnung an berufsbildenden Schulen.
	 */
	public static readonly ABSCHLUSS_BK : BenutzerKompetenzGruppe = new BenutzerKompetenzGruppe("ABSCHLUSS_BK", 22, new BenutzerKompetenzGruppenKatalogEintrag(5000, "Abschlussberechnung berufsbildende Schule", 2, 2));

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die Daten der Benutzerkompetenz-Gruppe
	 */
	public readonly daten : BenutzerKompetenzGruppenKatalogEintrag;

	/**
	 * Eine HashMap zum schnellen Zugriff auf ein Aufzählungsobjekt anhand der ID der Benutzerkompetenz-Gruppe
	 */
	private static readonly _mapID : HashMap<number, BenutzerKompetenzGruppe> = new HashMap();

	/**
	 * Erzeugt eine neue Benutzerkompetenz-Gruppe für die Aufzählung.
	 *
	 * @param daten    die Daten der Benutzerkompetenz-Gruppe
	 */
	private constructor(name : string, ordinal : number, daten : BenutzerKompetenzGruppenKatalogEintrag) {
		super(name, ordinal);
		BenutzerKompetenzGruppe.all_values_by_ordinal.push(this);
		BenutzerKompetenzGruppe.all_values_by_name.set(name, this);
		this.daten = daten;
	}

	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 */
	private static getMapID() : HashMap<number, BenutzerKompetenzGruppe> {
		if (BenutzerKompetenzGruppe._mapID.size() === 0)
			for (const p of BenutzerKompetenzGruppe.values())
				BenutzerKompetenzGruppe._mapID.put(p.daten.id, p);
		return BenutzerKompetenzGruppe._mapID;
	}

	/**
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
	public static valueOf(name : string) : BenutzerKompetenzGruppe | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_benutzer_BenutzerKompetenzGruppe(obj : unknown) : BenutzerKompetenzGruppe {
	return obj as BenutzerKompetenzGruppe;
}
