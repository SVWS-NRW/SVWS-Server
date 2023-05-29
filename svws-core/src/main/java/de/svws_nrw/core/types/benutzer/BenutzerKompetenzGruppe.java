package de.svws_nrw.core.types.benutzer;

import java.util.HashMap;

import de.svws_nrw.core.data.benutzer.BenutzerKompetenzGruppenKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Aufzählung für die
 * Benutzerkompetenzgruppen zur Verfügung.
 */
public enum BenutzerKompetenzGruppe {

	/** Es werden keinerlei Kompetenzen benötigt. */
	KEINE(new BenutzerKompetenzGruppenKatalogEintrag(-2, "keine", -2, -2)),

	/** Es werden Admin-Rechte benötigt. */
	ADMIN(new BenutzerKompetenzGruppenKatalogEintrag(-1, "admin", -1, -1)),

	/** Gruppe für Rechte bezüglich der Schüler-Individualdaten. */
	SCHUELER_INDIVIDUALDATEN(new BenutzerKompetenzGruppenKatalogEintrag(100, "Schüler Individualdaten", 1, 1)),

	/** Gruppe für Rechte bezüglich der Schüler-Leistungsdaten. */
	SCHUELER_LEISTUNGSDATEN(new BenutzerKompetenzGruppenKatalogEintrag(200, "Schüler Leistungsdaten", 1, 2)),

	/** Gruppe für Rechte für Berichte. */
	BERICHTE(new BenutzerKompetenzGruppenKatalogEintrag(300, "Berichte", 2, 3)),

	/** Gruppe für Rechte für den Import/Export von Daten. */
	IMPORT_EXPORT(new BenutzerKompetenzGruppenKatalogEintrag(400, "Import/Export", 3, 1)),

	/** Gruppe für Rechte bei Block-Operationen. */
	BLOCK_OPERATIONEN(new BenutzerKompetenzGruppenKatalogEintrag(500, "Blockoperationen", 3, 2)),

	/** Gruppe für Rechte bezüglich Schulbezogenener Daten. */
	SCHULBEZOGENE_DATEN(new BenutzerKompetenzGruppenKatalogEintrag(600, "Schulbezogene Daten", 3, 3)),

	/** Gruppe für Rechte für spezielle Operationen. */
	EXTRAS(new BenutzerKompetenzGruppenKatalogEintrag(700, "Extras", 3, 4)),

	/** Gruppe für Rechte bezüglich Kataloge. */
	KATALOG_EINTRAEGE(new BenutzerKompetenzGruppenKatalogEintrag(800, "Katalog-Einträge", 1, 3)),

	/** Gruppe für Rechte bezüglich Lehrerdaten. */
	LEHRERDATEN(new BenutzerKompetenzGruppenKatalogEintrag(900, "Lehrerdaten", 2, 2)),

	/** Gruppe für Rechte bezüglich des Verfahrens zur Schulpflichverletzung. */
	SCHULPFLICHTVERLETZUNG(new BenutzerKompetenzGruppenKatalogEintrag(1000, "Verfahren Schulpflichtverletzung", 2, 3)),

	/** Gruppe für Rechte bezüglich der Stundenplanung. */
	STUNDENPLANUNG(new BenutzerKompetenzGruppenKatalogEintrag(1100, "Stundenplanung", 4, 1)),

	/** Gruppe für Rechte bezüglich des externen Notenmoduls. */
	NOTENMODUL(new BenutzerKompetenzGruppenKatalogEintrag(1300, "Notenmodul", 4, 2)),

	/** Gruppe für Rechte bezüglich des Datenbank-Managements. */
	DATENBANK(new BenutzerKompetenzGruppenKatalogEintrag(1400, "Datenbank-Management", 4, 3)),

	/** Gruppe für Rechte bezüglich der Laufbahnplanung der Gymnasialen Oberstufe. */
	OBERSTUFE_LAUFBAHNPLANUNG(new BenutzerKompetenzGruppenKatalogEintrag(1600, "Oberstufe - Laufbahnplanung", 5, 1)),

	/** Gruppe für Rechte bezüglich der Kursplanung der Gymnasialen Oberstufe. */
	OBERSTUFE_KURSPLANUNG(new BenutzerKompetenzGruppenKatalogEintrag(1700, "Oberstufe - Kursplanung", 5, 2)),

	/** Gruppe für Rechte bezüglich der Klausurplanung der Gymnasialen Oberstufe. */
	OBERSTUFE_KLAUSURPLANUNG(new BenutzerKompetenzGruppenKatalogEintrag(1800, "Oberstufe - -Klausurplanung", 5, 3)),

	/** Gruppe für Rechte bezüglich des Abiturs. */
	ABITUR(new BenutzerKompetenzGruppenKatalogEintrag(1900, "Abitur", 5, 4)),

	/** Gruppe für Rechte bezüglich des Adressbuchs. */
	CARDDAV(new BenutzerKompetenzGruppenKatalogEintrag(2000, "Addressbuch (CardDAV)", 1, 4)),

	/** Gruppe für Rechte bezüglich der Kalender. */
	CALDAV(new BenutzerKompetenzGruppenKatalogEintrag(3000, "Kalender (CaldDAV)", 1, 5)),
	/** Gruppe für Rechte für die Abschlussberechnung in der Sekundarstufe I. */
	ABSCHLUSS_SEKI(new BenutzerKompetenzGruppenKatalogEintrag(4000, "Abschlussberechnung Sek I", 2, 1)),

	/** Gruppe für Rechte für die Abschlussberechnung an berufsbildenden Schulen. */
	ABSCHLUSS_BK(new BenutzerKompetenzGruppenKatalogEintrag(5000, "Abschlussberechnung berufsbildende Schule", 2, 2));



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Die Daten der Benutzerkompetenz-Gruppe */
	public final @NotNull BenutzerKompetenzGruppenKatalogEintrag daten;


	/** Eine HashMap zum schnellen Zugriff auf ein Aufzählungsobjekt anhand der ID der Benutzerkompetenz-Gruppe */
	private static final @NotNull HashMap<@NotNull Long, @NotNull BenutzerKompetenzGruppe> _mapID = new HashMap<>();


	/**
	 * Erzeugt eine neue Benutzerkompetenz-Gruppe für die Aufzählung.
	 *
	 * @param daten    die Daten der Benutzerkompetenz-Gruppe
	 */
	BenutzerKompetenzGruppe(final @NotNull BenutzerKompetenzGruppenKatalogEintrag daten) {
		this.daten = daten;
	}


	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Benutzerkompetenz-Gruppen auf die zugehörigen Benutzerkompetenz-Gruppen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull BenutzerKompetenzGruppe> getMapID() {
		if (_mapID.size() == 0)
			for (final BenutzerKompetenzGruppe p : BenutzerKompetenzGruppe.values())
				_mapID.put(p.daten.id, p);
		return _mapID;
	}


	/**
	 * Gibt die Benutzerkompetenz-Gruppen anhand der übergebenen ID zurück.
	 *
	 * @param id    die ID der Benutzerkompetenz-Gruppen
	 *
	 * @return die Benutzerkompetenz-Gruppen oder null, falls die ID fehlerhaft ist
	 */
	public static BenutzerKompetenzGruppe getByID(final long id) {
		return getMapID().get(id);
	}

}
