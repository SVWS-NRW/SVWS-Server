package de.nrw.schule.svws.core.types.benutzer;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.benutzer.BenutzerKompetenzKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die Benutzerkompetenzen 
 * zur Verfügung.
 */
public enum BenutzerKompetenz {
	
	/** Es werden keinerlei Kompetenzen benötigt. */
    KEINE(new BenutzerKompetenzKatalogEintrag(-2, BenutzerKompetenzGruppe.KEINE, "keine")),
    
    /** Es werden Admin-Rechte benötigt. */
    ADMIN(new BenutzerKompetenzKatalogEintrag(-1, BenutzerKompetenzGruppe.ADMIN, "admin")),
    
    /** Es werden Rechte zum Ansehen der Schüler Individualdaten benötigt. */
    SCHUELER_INDIVIDUALDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	11, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ansehen"
    )),
    
    /** Es werden Rechte zum Ändern der Schüler Individualdaten benötigt. */
    SCHUELER_INDIVIDUALDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	12, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ändern"
    )),
    
    /** Es werden Rechte zum Löschen der Schüler Individualdaten benötigt. */
    SCHUELER_INDIVIDUALDATEN_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
    	13, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Löschen"
    )),
    
    /** Es werden Rechte zum Ändern der Schüler Vermerke benötigt. */
    SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	14, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Vermerke ändern"
    )),

    /** Es werden Rechte zum Ändern der Schüler KAoA-Daten benötigt. */
    SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	15, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "KAoA-Daten ändern"
    )),
    
    /** Es werden Rechte zum Ändern der Einwilligungen zu einem Schüler benötigt. */
    SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	16, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Einwilligungen ändern (DSGVO, Lernplattformen)"
    )),
    
    /** Es werden Rechte zum Ansehen der Schüler Leistungsdaten benötigt. */
    SCHUELER_LEISTUNGSDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	21, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Ansehen"
    )),
    
    /** Es werden Rechte zum funktionsbezogenen Ändern der Schüler Leistungsdaten benötigt. */
    SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	22, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Funktionsbezogen ändern"
    )),
    
    /** Es werden Rechte zum generellen Ändern der Schüler Leistungsdaten benötigt. */
    SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	23, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Alle ändern"
    )),
    
    /** Es werden Rechte zum Drucken der Berichte benötigt. */
    BERICHTE_ALLE_FORMULARE_DRUCKEN(new BenutzerKompetenzKatalogEintrag(
    	31, BenutzerKompetenzGruppe.BERICHTE, "Alle Formulare drucken"
    )),
    
    /** Es werden Rechte zum Drucken der Standardberichte benötigt. */
    BERICHTE_STANDARDFORMULARE_DRUCKEN(new BenutzerKompetenzKatalogEintrag(
    	32, BenutzerKompetenzGruppe.BERICHTE, "Standard-Formulare drucken"
    )),
    
    /** Es werden Rechte zum Ändern der Berichte benötigt. */
    BERICHTE_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	33, BenutzerKompetenzGruppe.BERICHTE, "Ändern"
    )),
    
    /** Es werden Rechte zum Löschen der Berichte benötigt. */
    BERICHTE_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
    	34, BenutzerKompetenzGruppe.BERICHTE, "Löschen"
    )),
    
    /** Es werden Rechte zum Importieren von Daten benötigt. */
    IMPORT_EXPORT_DATEN_IMPORTIEREN(new BenutzerKompetenzKatalogEintrag(
    	41, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Daten importieren"
    )),
    
    /** Es werden Rechte zum Exportieren von Schülerdaten benötigt. */
    IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN(new BenutzerKompetenzKatalogEintrag(
    	42, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schülerdaten exportieren"
    )),
    
    /** Es werden Rechte zum Exportieren von Lehrerdaten benötigt. */
    IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN(new BenutzerKompetenzKatalogEintrag(
    	43, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Lehrerdaten exportieren"
    )),
    
    /** Es werden Rechte zum Nutzen der SchILD-NRW-Schnittstelle benötigt. */
    IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW(new BenutzerKompetenzKatalogEintrag(
    	44, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schnittstelle SchILD-NRW verwenden"
    )),

    /** Es werden Rechte zum Ausführen des Access-DB-Exports benötigt. */
    IMPORT_EXPORT_ACCESS_DB(new BenutzerKompetenzKatalogEintrag(
    	45, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Access-DB-Export durchführen"
    )),

    /** Es werden Rechte zum Export über die XML-Schnittstellen benötigt. */
    IMPORT_EXPORT_XML(new BenutzerKompetenzKatalogEintrag(
    	46, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Export über XML-Schnittstellen"
    )),
    
    /** Es werden Rechte zum Ansehen der Schulbezogenen Daten benötigt. */
    SCHULBEZOGENE_DATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	61, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ansehen"
    )),
    
    /** Es werden Rechte zum Ändern der Schulbezogenen Daten benötigt. */
    SCHULBEZOGENE_DATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	62, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ändern"
    )),
    
    /** Es werden Rechte benötigt um ein Backup durchzuführen. */
    EXTRAS_BACKUP_DURCHFUEHREN(new BenutzerKompetenzKatalogEintrag(
    	71, BenutzerKompetenzGruppe.EXTRAS, "Backup durchführen"
    )),
    
    /** Es werden Rechte zum Wiederherstellen von gelöschten Schülerdaten benötigt. */
    EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN(new BenutzerKompetenzKatalogEintrag(
    	72, BenutzerKompetenzGruppe.EXTRAS, "Gelöschte Daten zurückholen"
    )),
    
    /** Es werden Rechte zum Ändern der Farben für Fachgruppen benötigt. */
    EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	73, BenutzerKompetenzGruppe.EXTRAS, "Farben für Fachgruppen ändern"
    )),
    
    /** Es werden Rechte Import von Daten aus Kurs42 benötigt. */
    EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN(new BenutzerKompetenzKatalogEintrag(
    	74, BenutzerKompetenzGruppe.EXTRAS, "Daten aus Kurs42 importieren"
    )),

    /** Es werden Rechte zum Bearbeiten von Personengruppen benötigt. */
    EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN(new BenutzerKompetenzKatalogEintrag(
    	75, BenutzerKompetenzGruppe.EXTRAS, "Personengruppen bearbeiten"
    )),
    
    /** Es werden Rechte zum Ansehen von Katalogen benötigt. */
    KATALOG_EINTRAEGE_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	81, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ansehen"
    )),
    
    /** Es werden Rechte zum Ändern von Katalogen benötigt. */
    KATALOG_EINTRAEGE_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	82, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ändern"
    )),
    
    /** Es werden Rechte zum Löschen von Katalogen benötigt. */
    KATALOG_EINTRAEGE_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
    	83, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Löschen"
    )),
    
    /** Es werden Rechte zum Ansehen von Lehrerdaten benötigt. */
    LEHRERDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	91, BenutzerKompetenzGruppe.LEHRERDATEN, "Ansehen"
    )),
    
    /** Es werden Rechte zum Ändern von Lehrerdaten benötigt. */
    LEHRERDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	92, BenutzerKompetenzGruppe.LEHRERDATEN, "Ändern"
    )),
    
    /** Es werden Rechte zum Löschen von Lehrerdaten benötigt. */
    LEHRERDATEN_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
    	93, BenutzerKompetenzGruppe.LEHRERDATEN, "Löschen"
    )),
    
    /** Es werden Rechte zum Ansehen von Lehrerdetaildaten benötigt. */
    LEHRERDATEN_DETAILDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	94, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ansehen"
    )),
    
    /** Es werden Rechte zum Ändern von Lehrerdetaildaten benötigt. */
    LEHRERDATEN_DETAILDATEN_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	95, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ändern"
    )),

    /** Es werden Rechte zum Ansehen von Daten des Verfahrens Schulpflichtverletzung benötigt. */
    SCHULPFLICHTVERLETZUNG_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	101, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ansehen"
    )),
    
    /** Es werden Rechte zum Ändern von Daten des Verfahrens Schulpflichtverletzung benötigt. */
    SCHULPFLICHTVERLETZUNG_AENDERN(new BenutzerKompetenzKatalogEintrag(
    	102, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ändern"
    )),

    /** Es werden Rechte zum Löschen von Daten des Verfahrens Schulpflichtverletzung benötigt. */
    SCHULPFLICHTVERLETZUNG_LOESCHEN(new BenutzerKompetenzKatalogEintrag(
    	103, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Löschen"
    )), 
    
    /** Es werden Rechte zum Ansehen der Adressdaten eines von Erziehungsberechtigten benötigt*/
    ADRESSDATEN_ERZIEHER_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	201, BenutzerKompetenzGruppe.CARDDAV, "Ansehen"
    )), 
    
    /** Allgemeine Berechtigung für das Einsehen von Adressdaten über die CardDav API */
    ADRESSDATEN_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
    	202, BenutzerKompetenzGruppe.CARDDAV, "Ansehen"
    )),
    
    /** Allgemeine Berechtigung für den Zugriff auf die CalDav API */
    KALENDER_ANSEHEN(new BenutzerKompetenzKatalogEintrag(
		301, BenutzerKompetenzGruppe.CALDAV, "Ansehen"
	)), 
    
    /** Berechtigung für den Besitz und das Bearbeiten eines eigenen Kalenders über die CalDav API. */
    EIGENEN_KALENDER_BEARBEITEN(
		new BenutzerKompetenzKatalogEintrag(302, BenutzerKompetenzGruppe.CALDAV, "Bearbeiten"
	)), 
    
    /** Allgemeine Berechtigung zum Ansehen generierter Kalender abhängig von der Funktion des Nutzers */
    KALENDER_FUNKTIONSBEZOGEN_ANSEHEN(
		new BenutzerKompetenzKatalogEintrag(302, BenutzerKompetenzGruppe.CALDAV, "Ansehen"
	));


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Die Daten der Benutzerkompetenz */
    public final @NotNull BenutzerKompetenzKatalogEintrag daten;


	/** Eine HashMap zum schnellen Zugriff auf ein Aufzählungobjekt anhand der ID der Benutzerkompetenz */
	private static final @NotNull HashMap<@NotNull Long, @NotNull BenutzerKompetenz> _mapID = new HashMap<>();


    /**
     * Erzeugt eine neue Benutzerkompetenz für die Aufzählung.
     *
     * @param id                  die ID der Benutzerkompetenz
     * @param bezeichnungGruppe   die Bezeichnung der Gruppe von Kompetenzen zu dieser diese Benutzerkompetenz gehört
     * @param bezeichnung         die Bezeichnung der Benutzerkompetenz
     */
    private BenutzerKompetenz(@NotNull BenutzerKompetenzKatalogEintrag daten) {
        this.daten = daten;
    }


	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull BenutzerKompetenz> getMapID() {
		if (_mapID.size() == 0)
			for (BenutzerKompetenz p : BenutzerKompetenz.values())
				_mapID.put(p.daten.id, p);				
		return _mapID;
	}
	

    /** 
     * Gibt die Benutzerkompetenz anhand der übergebenen ID zurück. 
     * 
     * @param id    die ID der Benutzerkompetenz
     *  
     * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
     */
    public static BenutzerKompetenz getByID(long id) {
    	return getMapID().get(id);
    }

}
