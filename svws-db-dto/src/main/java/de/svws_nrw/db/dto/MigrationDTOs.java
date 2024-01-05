package de.svws_nrw.db.dto;

import java.util.HashMap;

import de.svws_nrw.db.dto.migration.client.MigrationDTOClientKonfigurationBenutzer;
import de.svws_nrw.db.dto.migration.client.MigrationDTOClientKonfigurationGlobal;
import de.svws_nrw.db.dto.migration.coretypes.MigrationDTOKursFortschreibungsart;
import de.svws_nrw.db.dto.migration.coretypes.MigrationDTONote;
import de.svws_nrw.db.dto.migration.coretypes.MigrationDTOPersonalTyp;
import de.svws_nrw.db.dto.migration.schema.MigrationDTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.migration.schema.MigrationDTOSchemaCoreTypeVersion;
import de.svws_nrw.db.dto.migration.schema.MigrationDTOSchemaStatus;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchildAuswahlFilter;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchildVerwaltung;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppe;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppeSchueler;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerReportvorlagen;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerWiedervorlage;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOTextExportVorlagen;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOZuordnungReportvorlagen;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzer;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzerAllgemein;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzerKompetenz;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzerMail;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzergruppe;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzergruppenKompetenz;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOKatalogBenutzerKompetenz;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOKatalogBenutzerKompetenzGruppe;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOProtokollLogin;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOUserGroups;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOUsers;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOBeschaeftigungsart;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOFachgliederungen;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOFachklassen;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOKatalogZertifikate;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOSchuelerBKAbschluss;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOSchuelerBKFach;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOSchuelerZuweisung;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOZertifikate;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOErzieherDatenschutz;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOErzieherLernplattform;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOErzieherart;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOErzieherfunktion;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOSchuelerTelefon;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOTelefonArt;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFach;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFachTeilleistungsarten;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFachgruppen;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFaecherKatalog;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFaecherKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFaecherKatalogSchulformen;
import de.svws_nrw.db.dto.migration.schild.gost.MigrationDTOFaecherNichtMoeglicheKombination;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOAnkreuzdaten;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOKindergarten;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.migration.schild.impexp.MigrationDTOEigeneImporte;
import de.svws_nrw.db.dto.migration.schild.impexp.MigrationDTOEigeneImporteFelder;
import de.svws_nrw.db.dto.migration.schild.impexp.MigrationDTOEigeneImporteTabellen;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOAnsprechpartnerAllgemeineAdresse;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOFahrschuelerart;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOFloskelgruppen;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOFloskeln;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOHaltestellen;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKatalogAdressart;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKatalogDatenschutz;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKonfession;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKursarten;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOOrt;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOOrtsteil;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOSchuleNRW;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOSchulfunktion;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOSchwerpunkt;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOTextDateien;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOVermerkArt;
import de.svws_nrw.db.dto.migration.schild.klassen.MigrationDTOKlassen;
import de.svws_nrw.db.dto.migration.schild.klassen.MigrationDTOKlassenLeitung;
import de.svws_nrw.db.dto.migration.schild.klassen.MigrationDTOKlassenartenKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.klassen.MigrationDTOVersetzung;
import de.svws_nrw.db.dto.migration.schild.kurse.MigrationDTOKurs;
import de.svws_nrw.db.dto.migration.schild.kurse.MigrationDTOKursLehrer;
import de.svws_nrw.db.dto.migration.schild.kurse.MigrationDTOKursartenKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrer;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerDatenschutz;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerFoto;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerFunktion;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramt;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtBefaehigung;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtFachrichtung;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLeitungsfunktionKeys;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLernplattform;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerMehrleistung;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOSchulleitung;
import de.svws_nrw.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppen;
import de.svws_nrw.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppenPersonen;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOEinschulungsart;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOEntlassarten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchueler;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerAbgaenge;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerAllgemeineAdresse;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerFehlstunden;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoerderempfehlung;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoto;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerGrundschuldaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLernplattform;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerMerkmale;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerSprachpruefungen;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerVermerke;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerZP10;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSportbefreiung;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOTeilleistungsarten;
import de.svws_nrw.db.dto.migration.schild.schueler.abitur.MigrationDTOSchuelerAbitur;
import de.svws_nrw.db.dto.migration.schild.schueler.abitur.MigrationDTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.migration.schild.schueler.fhr.MigrationDTOSchuelerFHR;
import de.svws_nrw.db.dto.migration.schild.schueler.fhr.MigrationDTOSchuelerFHRFach;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOAbteilungen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOAbteilungsKlassen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOAlleJahrgaengeKeys;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOAlleSchulformen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOAllgemeineMerkmaleKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOBerufskollegAnlagen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOBerufskollegBerufsebenen1;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOBerufskollegBerufsebenen2;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOBerufskollegBerufsebenen3;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOBerufskollegFachklassenKeys;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOEigeneSchule;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOEigeneSchuleKAoADaten;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOEinschulungsartenKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOHerkunft;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOHerkunftSchulformen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOHerkunftsart;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOHerkunftsartSchulformen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOJahrgang;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOMerkmale;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOOrganisationsformenKatalogKeys;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOSchulformen;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOSchultexte;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOStundentafel;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOStundentafelFaecher;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOTeilstandorte;
import de.svws_nrw.db.dto.migration.schueler.MigrationDTOSchuelerStatus;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOHerkunftKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOHerkunftsartKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoAAnschlussoptionKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoABerufsfeldKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoAKategorieKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoAMerkmalKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoASBOEB4Keys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOKAoAZusatzmerkmalKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTONationalitaetenKeys;
import de.svws_nrw.db.dto.migration.schule.MigrationDTOReligionKeys;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOCredentials;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOCredentialsLernplattformen;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOLernplattformen;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOSchuleCredentials;

/**
 * Diese Klasse dient als Verzeichnis aller Datenbank-DTO-Klassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class MigrationDTOs {

    /** Enthält das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den entsprechenden Java-DTO-Klassen. */
    private static HashMap<String, Class<? extends Object>> mapDTOName2DTOClass = null;

    /** Enthält das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB. */
    private static HashMap<String, Class<? extends Object>> mapTablename2DTOClass = null;


    /**
     * Gibt das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB zurück.
     *
     * @return eine Hashmap mit dem Mapping
     */
     private static HashMap<String, Class<? extends Object>> getMapDTOName2DTOClass() {
         if (mapDTOName2DTOClass == null) {
             mapDTOName2DTOClass = new HashMap<>();
             mapDTOName2DTOClass.put(MigrationDTOClientKonfigurationBenutzer.class.getSimpleName(), MigrationDTOClientKonfigurationBenutzer.class);
             mapDTOName2DTOClass.put(MigrationDTOClientKonfigurationGlobal.class.getSimpleName(), MigrationDTOClientKonfigurationGlobal.class);
             mapDTOName2DTOClass.put(MigrationDTOKursFortschreibungsart.class.getSimpleName(), MigrationDTOKursFortschreibungsart.class);
             mapDTOName2DTOClass.put(MigrationDTONote.class.getSimpleName(), MigrationDTONote.class);
             mapDTOName2DTOClass.put(MigrationDTOPersonalTyp.class.getSimpleName(), MigrationDTOPersonalTyp.class);
             mapDTOName2DTOClass.put(MigrationDTOSchemaAutoInkremente.class.getSimpleName(), MigrationDTOSchemaAutoInkremente.class);
             mapDTOName2DTOClass.put(MigrationDTOSchemaCoreTypeVersion.class.getSimpleName(), MigrationDTOSchemaCoreTypeVersion.class);
             mapDTOName2DTOClass.put(MigrationDTOSchemaStatus.class.getSimpleName(), MigrationDTOSchemaStatus.class);
             mapDTOName2DTOClass.put(MigrationDTOSchildAuswahlFilter.class.getSimpleName(), MigrationDTOSchildAuswahlFilter.class);
             mapDTOName2DTOClass.put(MigrationDTOSchildVerwaltung.class.getSimpleName(), MigrationDTOSchildVerwaltung.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerIndividuelleGruppe.class.getSimpleName(), MigrationDTOSchuelerIndividuelleGruppe.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerIndividuelleGruppeSchueler.class.getSimpleName(), MigrationDTOSchuelerIndividuelleGruppeSchueler.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerReportvorlagen.class.getSimpleName(), MigrationDTOSchuelerReportvorlagen.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerWiedervorlage.class.getSimpleName(), MigrationDTOSchuelerWiedervorlage.class);
             mapDTOName2DTOClass.put(MigrationDTOTextExportVorlagen.class.getSimpleName(), MigrationDTOTextExportVorlagen.class);
             mapDTOName2DTOClass.put(MigrationDTOZuordnungReportvorlagen.class.getSimpleName(), MigrationDTOZuordnungReportvorlagen.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzer.class.getSimpleName(), MigrationDTOBenutzer.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzerAllgemein.class.getSimpleName(), MigrationDTOBenutzerAllgemein.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzerKompetenz.class.getSimpleName(), MigrationDTOBenutzerKompetenz.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzerMail.class.getSimpleName(), MigrationDTOBenutzerMail.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzergruppe.class.getSimpleName(), MigrationDTOBenutzergruppe.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzergruppenKompetenz.class.getSimpleName(), MigrationDTOBenutzergruppenKompetenz.class);
             mapDTOName2DTOClass.put(MigrationDTOBenutzergruppenMitglied.class.getSimpleName(), MigrationDTOBenutzergruppenMitglied.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogBenutzerKompetenz.class.getSimpleName(), MigrationDTOKatalogBenutzerKompetenz.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogBenutzerKompetenzGruppe.class.getSimpleName(), MigrationDTOKatalogBenutzerKompetenzGruppe.class);
             mapDTOName2DTOClass.put(MigrationDTOProtokollLogin.class.getSimpleName(), MigrationDTOProtokollLogin.class);
             mapDTOName2DTOClass.put(MigrationDTOUserGroups.class.getSimpleName(), MigrationDTOUserGroups.class);
             mapDTOName2DTOClass.put(MigrationDTOUsers.class.getSimpleName(), MigrationDTOUsers.class);
             mapDTOName2DTOClass.put(MigrationDTOBeschaeftigungsart.class.getSimpleName(), MigrationDTOBeschaeftigungsart.class);
             mapDTOName2DTOClass.put(MigrationDTOFachgliederungen.class.getSimpleName(), MigrationDTOFachgliederungen.class);
             mapDTOName2DTOClass.put(MigrationDTOFachklassen.class.getSimpleName(), MigrationDTOFachklassen.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogZertifikate.class.getSimpleName(), MigrationDTOKatalogZertifikate.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerBKAbschluss.class.getSimpleName(), MigrationDTOSchuelerBKAbschluss.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerBKFach.class.getSimpleName(), MigrationDTOSchuelerBKFach.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerZuweisung.class.getSimpleName(), MigrationDTOSchuelerZuweisung.class);
             mapDTOName2DTOClass.put(MigrationDTOZertifikate.class.getSimpleName(), MigrationDTOZertifikate.class);
             mapDTOName2DTOClass.put(MigrationDTOErzieherDatenschutz.class.getSimpleName(), MigrationDTOErzieherDatenschutz.class);
             mapDTOName2DTOClass.put(MigrationDTOErzieherLernplattform.class.getSimpleName(), MigrationDTOErzieherLernplattform.class);
             mapDTOName2DTOClass.put(MigrationDTOErzieherart.class.getSimpleName(), MigrationDTOErzieherart.class);
             mapDTOName2DTOClass.put(MigrationDTOErzieherfunktion.class.getSimpleName(), MigrationDTOErzieherfunktion.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerErzieherAdresse.class.getSimpleName(), MigrationDTOSchuelerErzieherAdresse.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerTelefon.class.getSimpleName(), MigrationDTOSchuelerTelefon.class);
             mapDTOName2DTOClass.put(MigrationDTOTelefonArt.class.getSimpleName(), MigrationDTOTelefonArt.class);
             mapDTOName2DTOClass.put(MigrationDTOFach.class.getSimpleName(), MigrationDTOFach.class);
             mapDTOName2DTOClass.put(MigrationDTOFachTeilleistungsarten.class.getSimpleName(), MigrationDTOFachTeilleistungsarten.class);
             mapDTOName2DTOClass.put(MigrationDTOFachgruppen.class.getSimpleName(), MigrationDTOFachgruppen.class);
             mapDTOName2DTOClass.put(MigrationDTOFaecherKatalog.class.getSimpleName(), MigrationDTOFaecherKatalog.class);
             mapDTOName2DTOClass.put(MigrationDTOFaecherKatalogKeys.class.getSimpleName(), MigrationDTOFaecherKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOFaecherKatalogSchulformen.class.getSimpleName(), MigrationDTOFaecherKatalogSchulformen.class);
             mapDTOName2DTOClass.put(MigrationDTOFaecherNichtMoeglicheKombination.class.getSimpleName(), MigrationDTOFaecherNichtMoeglicheKombination.class);
             mapDTOName2DTOClass.put(MigrationDTOAnkreuzdaten.class.getSimpleName(), MigrationDTOAnkreuzdaten.class);
             mapDTOName2DTOClass.put(MigrationDTOAnkreuzfloskeln.class.getSimpleName(), MigrationDTOAnkreuzfloskeln.class);
             mapDTOName2DTOClass.put(MigrationDTOKindergarten.class.getSimpleName(), MigrationDTOKindergarten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerAnkreuzfloskeln.class.getSimpleName(), MigrationDTOSchuelerAnkreuzfloskeln.class);
             mapDTOName2DTOClass.put(MigrationDTOEigeneImporte.class.getSimpleName(), MigrationDTOEigeneImporte.class);
             mapDTOName2DTOClass.put(MigrationDTOEigeneImporteFelder.class.getSimpleName(), MigrationDTOEigeneImporteFelder.class);
             mapDTOName2DTOClass.put(MigrationDTOEigeneImporteTabellen.class.getSimpleName(), MigrationDTOEigeneImporteTabellen.class);
             mapDTOName2DTOClass.put(MigrationDTOAnsprechpartnerAllgemeineAdresse.class.getSimpleName(), MigrationDTOAnsprechpartnerAllgemeineAdresse.class);
             mapDTOName2DTOClass.put(MigrationDTOFahrschuelerart.class.getSimpleName(), MigrationDTOFahrschuelerart.class);
             mapDTOName2DTOClass.put(MigrationDTOFloskelgruppen.class.getSimpleName(), MigrationDTOFloskelgruppen.class);
             mapDTOName2DTOClass.put(MigrationDTOFloskeln.class.getSimpleName(), MigrationDTOFloskeln.class);
             mapDTOName2DTOClass.put(MigrationDTOHaltestellen.class.getSimpleName(), MigrationDTOHaltestellen.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogAdressart.class.getSimpleName(), MigrationDTOKatalogAdressart.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogAllgemeineAdresse.class.getSimpleName(), MigrationDTOKatalogAllgemeineAdresse.class);
             mapDTOName2DTOClass.put(MigrationDTOKatalogDatenschutz.class.getSimpleName(), MigrationDTOKatalogDatenschutz.class);
             mapDTOName2DTOClass.put(MigrationDTOKonfession.class.getSimpleName(), MigrationDTOKonfession.class);
             mapDTOName2DTOClass.put(MigrationDTOKursarten.class.getSimpleName(), MigrationDTOKursarten.class);
             mapDTOName2DTOClass.put(MigrationDTOOrt.class.getSimpleName(), MigrationDTOOrt.class);
             mapDTOName2DTOClass.put(MigrationDTOOrtsteil.class.getSimpleName(), MigrationDTOOrtsteil.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuleNRW.class.getSimpleName(), MigrationDTOSchuleNRW.class);
             mapDTOName2DTOClass.put(MigrationDTOSchulfunktion.class.getSimpleName(), MigrationDTOSchulfunktion.class);
             mapDTOName2DTOClass.put(MigrationDTOSchwerpunkt.class.getSimpleName(), MigrationDTOSchwerpunkt.class);
             mapDTOName2DTOClass.put(MigrationDTOTextDateien.class.getSimpleName(), MigrationDTOTextDateien.class);
             mapDTOName2DTOClass.put(MigrationDTOVermerkArt.class.getSimpleName(), MigrationDTOVermerkArt.class);
             mapDTOName2DTOClass.put(MigrationDTOKlassen.class.getSimpleName(), MigrationDTOKlassen.class);
             mapDTOName2DTOClass.put(MigrationDTOKlassenLeitung.class.getSimpleName(), MigrationDTOKlassenLeitung.class);
             mapDTOName2DTOClass.put(MigrationDTOKlassenartenKatalogKeys.class.getSimpleName(), MigrationDTOKlassenartenKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOVersetzung.class.getSimpleName(), MigrationDTOVersetzung.class);
             mapDTOName2DTOClass.put(MigrationDTOKurs.class.getSimpleName(), MigrationDTOKurs.class);
             mapDTOName2DTOClass.put(MigrationDTOKursLehrer.class.getSimpleName(), MigrationDTOKursLehrer.class);
             mapDTOName2DTOClass.put(MigrationDTOKursartenKatalogKeys.class.getSimpleName(), MigrationDTOKursartenKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrer.class.getSimpleName(), MigrationDTOLehrer.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerAbschnittsdaten.class.getSimpleName(), MigrationDTOLehrerAbschnittsdaten.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerAnrechnungsstunde.class.getSimpleName(), MigrationDTOLehrerAnrechnungsstunde.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerDatenschutz.class.getSimpleName(), MigrationDTOLehrerDatenschutz.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerEntlastungsstunde.class.getSimpleName(), MigrationDTOLehrerEntlastungsstunde.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerFoto.class.getSimpleName(), MigrationDTOLehrerFoto.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerFunktion.class.getSimpleName(), MigrationDTOLehrerFunktion.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerLehramt.class.getSimpleName(), MigrationDTOLehrerLehramt.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerLehramtBefaehigung.class.getSimpleName(), MigrationDTOLehrerLehramtBefaehigung.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerLehramtFachrichtung.class.getSimpleName(), MigrationDTOLehrerLehramtFachrichtung.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerLeitungsfunktionKeys.class.getSimpleName(), MigrationDTOLehrerLeitungsfunktionKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerLernplattform.class.getSimpleName(), MigrationDTOLehrerLernplattform.class);
             mapDTOName2DTOClass.put(MigrationDTOLehrerMehrleistung.class.getSimpleName(), MigrationDTOLehrerMehrleistung.class);
             mapDTOName2DTOClass.put(MigrationDTOSchulleitung.class.getSimpleName(), MigrationDTOSchulleitung.class);
             mapDTOName2DTOClass.put(MigrationDTOPersonengruppen.class.getSimpleName(), MigrationDTOPersonengruppen.class);
             mapDTOName2DTOClass.put(MigrationDTOPersonengruppenPersonen.class.getSimpleName(), MigrationDTOPersonengruppenPersonen.class);
             mapDTOName2DTOClass.put(MigrationDTOEinschulungsart.class.getSimpleName(), MigrationDTOEinschulungsart.class);
             mapDTOName2DTOClass.put(MigrationDTOEntlassarten.class.getSimpleName(), MigrationDTOEntlassarten.class);
             mapDTOName2DTOClass.put(MigrationDTOFoerderschwerpunkt.class.getSimpleName(), MigrationDTOFoerderschwerpunkt.class);
             mapDTOName2DTOClass.put(MigrationDTOSchueler.class.getSimpleName(), MigrationDTOSchueler.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerAbgaenge.class.getSimpleName(), MigrationDTOSchuelerAbgaenge.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerAllgemeineAdresse.class.getSimpleName(), MigrationDTOSchuelerAllgemeineAdresse.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerDatenschutz.class.getSimpleName(), MigrationDTOSchuelerDatenschutz.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerFehlstunden.class.getSimpleName(), MigrationDTOSchuelerFehlstunden.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerFoerderempfehlung.class.getSimpleName(), MigrationDTOSchuelerFoerderempfehlung.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerFoto.class.getSimpleName(), MigrationDTOSchuelerFoto.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerGrundschuldaten.class.getSimpleName(), MigrationDTOSchuelerGrundschuldaten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerKAoADaten.class.getSimpleName(), MigrationDTOSchuelerKAoADaten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerLeistungsdaten.class.getSimpleName(), MigrationDTOSchuelerLeistungsdaten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerLernabschnittsdaten.class.getSimpleName(), MigrationDTOSchuelerLernabschnittsdaten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerLernplattform.class.getSimpleName(), MigrationDTOSchuelerLernplattform.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerMerkmale.class.getSimpleName(), MigrationDTOSchuelerMerkmale.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerPSFachBemerkungen.class.getSimpleName(), MigrationDTOSchuelerPSFachBemerkungen.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerSprachenfolge.class.getSimpleName(), MigrationDTOSchuelerSprachenfolge.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerSprachpruefungen.class.getSimpleName(), MigrationDTOSchuelerSprachpruefungen.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerTeilleistung.class.getSimpleName(), MigrationDTOSchuelerTeilleistung.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerVermerke.class.getSimpleName(), MigrationDTOSchuelerVermerke.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerZP10.class.getSimpleName(), MigrationDTOSchuelerZP10.class);
             mapDTOName2DTOClass.put(MigrationDTOSportbefreiung.class.getSimpleName(), MigrationDTOSportbefreiung.class);
             mapDTOName2DTOClass.put(MigrationDTOTeilleistungsarten.class.getSimpleName(), MigrationDTOTeilleistungsarten.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerAbitur.class.getSimpleName(), MigrationDTOSchuelerAbitur.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerAbiturFach.class.getSimpleName(), MigrationDTOSchuelerAbiturFach.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerFHR.class.getSimpleName(), MigrationDTOSchuelerFHR.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerFHRFach.class.getSimpleName(), MigrationDTOSchuelerFHRFach.class);
             mapDTOName2DTOClass.put(MigrationDTOAbteilungen.class.getSimpleName(), MigrationDTOAbteilungen.class);
             mapDTOName2DTOClass.put(MigrationDTOAbteilungsKlassen.class.getSimpleName(), MigrationDTOAbteilungsKlassen.class);
             mapDTOName2DTOClass.put(MigrationDTOAlleJahrgaengeKeys.class.getSimpleName(), MigrationDTOAlleJahrgaengeKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOAlleSchulformen.class.getSimpleName(), MigrationDTOAlleSchulformen.class);
             mapDTOName2DTOClass.put(MigrationDTOAllgemeineMerkmaleKatalogKeys.class.getSimpleName(), MigrationDTOAllgemeineMerkmaleKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOBerufskollegAnlagen.class.getSimpleName(), MigrationDTOBerufskollegAnlagen.class);
             mapDTOName2DTOClass.put(MigrationDTOBerufskollegBerufsebenen1.class.getSimpleName(), MigrationDTOBerufskollegBerufsebenen1.class);
             mapDTOName2DTOClass.put(MigrationDTOBerufskollegBerufsebenen2.class.getSimpleName(), MigrationDTOBerufskollegBerufsebenen2.class);
             mapDTOName2DTOClass.put(MigrationDTOBerufskollegBerufsebenen3.class.getSimpleName(), MigrationDTOBerufskollegBerufsebenen3.class);
             mapDTOName2DTOClass.put(MigrationDTOBerufskollegFachklassenKeys.class.getSimpleName(), MigrationDTOBerufskollegFachklassenKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOEigeneSchule.class.getSimpleName(), MigrationDTOEigeneSchule.class);
             mapDTOName2DTOClass.put(MigrationDTOEigeneSchuleKAoADaten.class.getSimpleName(), MigrationDTOEigeneSchuleKAoADaten.class);
             mapDTOName2DTOClass.put(MigrationDTOEinschulungsartenKatalogKeys.class.getSimpleName(), MigrationDTOEinschulungsartenKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunft.class.getSimpleName(), MigrationDTOHerkunft.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunftSchulformen.class.getSimpleName(), MigrationDTOHerkunftSchulformen.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunftsart.class.getSimpleName(), MigrationDTOHerkunftsart.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunftsartSchulformen.class.getSimpleName(), MigrationDTOHerkunftsartSchulformen.class);
             mapDTOName2DTOClass.put(MigrationDTOJahrgang.class.getSimpleName(), MigrationDTOJahrgang.class);
             mapDTOName2DTOClass.put(MigrationDTOMerkmale.class.getSimpleName(), MigrationDTOMerkmale.class);
             mapDTOName2DTOClass.put(MigrationDTOOrganisationsformenKatalogKeys.class.getSimpleName(), MigrationDTOOrganisationsformenKatalogKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOSchulformen.class.getSimpleName(), MigrationDTOSchulformen.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuljahresabschnitte.class.getSimpleName(), MigrationDTOSchuljahresabschnitte.class);
             mapDTOName2DTOClass.put(MigrationDTOSchultexte.class.getSimpleName(), MigrationDTOSchultexte.class);
             mapDTOName2DTOClass.put(MigrationDTOStundentafel.class.getSimpleName(), MigrationDTOStundentafel.class);
             mapDTOName2DTOClass.put(MigrationDTOStundentafelFaecher.class.getSimpleName(), MigrationDTOStundentafelFaecher.class);
             mapDTOName2DTOClass.put(MigrationDTOTeilstandorte.class.getSimpleName(), MigrationDTOTeilstandorte.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuelerStatus.class.getSimpleName(), MigrationDTOSchuelerStatus.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunftKeys.class.getSimpleName(), MigrationDTOHerkunftKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOHerkunftsartKeys.class.getSimpleName(), MigrationDTOHerkunftsartKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoAAnschlussoptionKeys.class.getSimpleName(), MigrationDTOKAoAAnschlussoptionKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoABerufsfeldKeys.class.getSimpleName(), MigrationDTOKAoABerufsfeldKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoAKategorieKeys.class.getSimpleName(), MigrationDTOKAoAKategorieKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoAMerkmalKeys.class.getSimpleName(), MigrationDTOKAoAMerkmalKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoASBOEB4Keys.class.getSimpleName(), MigrationDTOKAoASBOEB4Keys.class);
             mapDTOName2DTOClass.put(MigrationDTOKAoAZusatzmerkmalKeys.class.getSimpleName(), MigrationDTOKAoAZusatzmerkmalKeys.class);
             mapDTOName2DTOClass.put(MigrationDTONationalitaetenKeys.class.getSimpleName(), MigrationDTONationalitaetenKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOReligionKeys.class.getSimpleName(), MigrationDTOReligionKeys.class);
             mapDTOName2DTOClass.put(MigrationDTOCredentials.class.getSimpleName(), MigrationDTOCredentials.class);
             mapDTOName2DTOClass.put(MigrationDTOCredentialsLernplattformen.class.getSimpleName(), MigrationDTOCredentialsLernplattformen.class);
             mapDTOName2DTOClass.put(MigrationDTOLernplattformen.class.getSimpleName(), MigrationDTOLernplattformen.class);
             mapDTOName2DTOClass.put(MigrationDTOSchuleCredentials.class.getSimpleName(), MigrationDTOSchuleCredentials.class);
         }
         return mapDTOName2DTOClass;
     }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen DTO-Namen zurück.
     *
     * @param name   der DTO-Name
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromDTOName(final String name) {
    	return getMapDTOName2DTOClass().get(name);
    }


    /**
     * Gibt das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den zugehörigen
     * Java-DTO-Klassen zurück.
     *
     * @return eine Hashmap mit dem Mapping
     */
     private static HashMap<String, Class<? extends Object>> getMapTablename2DTOClass() {
         if (mapTablename2DTOClass == null) {
             mapTablename2DTOClass = new HashMap<>();
             mapTablename2DTOClass.put("Client_Konfiguration_Benutzer", MigrationDTOClientKonfigurationBenutzer.class);
             mapTablename2DTOClass.put("Client_Konfiguration_Global", MigrationDTOClientKonfigurationGlobal.class);
             mapTablename2DTOClass.put("KursFortschreibungsarten", MigrationDTOKursFortschreibungsart.class);
             mapTablename2DTOClass.put("Noten", MigrationDTONote.class);
             mapTablename2DTOClass.put("PersonalTypen", MigrationDTOPersonalTyp.class);
             mapTablename2DTOClass.put("Schema_AutoInkremente", MigrationDTOSchemaAutoInkremente.class);
             mapTablename2DTOClass.put("Schema_Core_Type_Versionen", MigrationDTOSchemaCoreTypeVersion.class);
             mapTablename2DTOClass.put("Schema_Status", MigrationDTOSchemaStatus.class);
             mapTablename2DTOClass.put("SchildFilter", MigrationDTOSchildAuswahlFilter.class);
             mapTablename2DTOClass.put("Schild_Verwaltung", MigrationDTOSchildVerwaltung.class);
             mapTablename2DTOClass.put("SchuelerListe", MigrationDTOSchuelerIndividuelleGruppe.class);
             mapTablename2DTOClass.put("SchuelerListe_Inhalt", MigrationDTOSchuelerIndividuelleGruppeSchueler.class);
             mapTablename2DTOClass.put("SchuelerReportvorlagen", MigrationDTOSchuelerReportvorlagen.class);
             mapTablename2DTOClass.put("SchuelerWiedervorlage", MigrationDTOSchuelerWiedervorlage.class);
             mapTablename2DTOClass.put("TextExportVorlagen", MigrationDTOTextExportVorlagen.class);
             mapTablename2DTOClass.put("ZuordnungReportvorlagen", MigrationDTOZuordnungReportvorlagen.class);
             mapTablename2DTOClass.put("Benutzer", MigrationDTOBenutzer.class);
             mapTablename2DTOClass.put("BenutzerAllgemein", MigrationDTOBenutzerAllgemein.class);
             mapTablename2DTOClass.put("BenutzerKompetenzen", MigrationDTOBenutzerKompetenz.class);
             mapTablename2DTOClass.put("BenutzerEmail", MigrationDTOBenutzerMail.class);
             mapTablename2DTOClass.put("Benutzergruppen", MigrationDTOBenutzergruppe.class);
             mapTablename2DTOClass.put("BenutzergruppenKompetenzen", MigrationDTOBenutzergruppenKompetenz.class);
             mapTablename2DTOClass.put("BenutzergruppenMitglieder", MigrationDTOBenutzergruppenMitglied.class);
             mapTablename2DTOClass.put("Kompetenzen", MigrationDTOKatalogBenutzerKompetenz.class);
             mapTablename2DTOClass.put("Kompetenzgruppen", MigrationDTOKatalogBenutzerKompetenzGruppe.class);
             mapTablename2DTOClass.put("Logins", MigrationDTOProtokollLogin.class);
             mapTablename2DTOClass.put("Usergroups", MigrationDTOUserGroups.class);
             mapTablename2DTOClass.put("Users", MigrationDTOUsers.class);
             mapTablename2DTOClass.put("K_BeschaeftigungsArt", MigrationDTOBeschaeftigungsart.class);
             mapTablename2DTOClass.put("Fach_Gliederungen", MigrationDTOFachgliederungen.class);
             mapTablename2DTOClass.put("EigeneSchule_Fachklassen", MigrationDTOFachklassen.class);
             mapTablename2DTOClass.put("K_Zertifikate", MigrationDTOKatalogZertifikate.class);
             mapTablename2DTOClass.put("SchuelerBKAbschluss", MigrationDTOSchuelerBKAbschluss.class);
             mapTablename2DTOClass.put("SchuelerBKFaecher", MigrationDTOSchuelerBKFach.class);
             mapTablename2DTOClass.put("SchuelerZuweisungen", MigrationDTOSchuelerZuweisung.class);
             mapTablename2DTOClass.put("EigeneSchule_Zertifikate", MigrationDTOZertifikate.class);
             mapTablename2DTOClass.put("ErzieherDatenschutz", MigrationDTOErzieherDatenschutz.class);
             mapTablename2DTOClass.put("ErzieherLernplattform", MigrationDTOErzieherLernplattform.class);
             mapTablename2DTOClass.put("K_ErzieherArt", MigrationDTOErzieherart.class);
             mapTablename2DTOClass.put("K_ErzieherFunktion", MigrationDTOErzieherfunktion.class);
             mapTablename2DTOClass.put("SchuelerErzAdr", MigrationDTOSchuelerErzieherAdresse.class);
             mapTablename2DTOClass.put("SchuelerTelefone", MigrationDTOSchuelerTelefon.class);
             mapTablename2DTOClass.put("K_TelefonArt", MigrationDTOTelefonArt.class);
             mapTablename2DTOClass.put("EigeneSchule_Faecher", MigrationDTOFach.class);
             mapTablename2DTOClass.put("EigeneSchule_FachTeilleistungen", MigrationDTOFachTeilleistungsarten.class);
             mapTablename2DTOClass.put("Fachgruppen", MigrationDTOFachgruppen.class);
             mapTablename2DTOClass.put("FachKatalog", MigrationDTOFaecherKatalog.class);
             mapTablename2DTOClass.put("FachKatalog_Keys", MigrationDTOFaecherKatalogKeys.class);
             mapTablename2DTOClass.put("FachKatalog_Schulformen", MigrationDTOFaecherKatalogSchulformen.class);
             mapTablename2DTOClass.put("NichtMoeglAbiFachKombi", MigrationDTOFaecherNichtMoeglicheKombination.class);
             mapTablename2DTOClass.put("K_Ankreuzdaten", MigrationDTOAnkreuzdaten.class);
             mapTablename2DTOClass.put("K_Ankreuzfloskeln", MigrationDTOAnkreuzfloskeln.class);
             mapTablename2DTOClass.put("K_Kindergarten", MigrationDTOKindergarten.class);
             mapTablename2DTOClass.put("SchuelerAnkreuzfloskeln", MigrationDTOSchuelerAnkreuzfloskeln.class);
             mapTablename2DTOClass.put("ImpExp_EigeneImporte", MigrationDTOEigeneImporte.class);
             mapTablename2DTOClass.put("ImpExp_EigeneImporte_Felder", MigrationDTOEigeneImporteFelder.class);
             mapTablename2DTOClass.put("ImpExp_EigeneImporte_Tabellen", MigrationDTOEigeneImporteTabellen.class);
             mapTablename2DTOClass.put("AllgAdrAnsprechpartner", MigrationDTOAnsprechpartnerAllgemeineAdresse.class);
             mapTablename2DTOClass.put("K_FahrschuelerArt", MigrationDTOFahrschuelerart.class);
             mapTablename2DTOClass.put("Floskelgruppen", MigrationDTOFloskelgruppen.class);
             mapTablename2DTOClass.put("Floskeln", MigrationDTOFloskeln.class);
             mapTablename2DTOClass.put("K_Haltestelle", MigrationDTOHaltestellen.class);
             mapTablename2DTOClass.put("K_Adressart", MigrationDTOKatalogAdressart.class);
             mapTablename2DTOClass.put("K_AllgAdresse", MigrationDTOKatalogAllgemeineAdresse.class);
             mapTablename2DTOClass.put("K_Datenschutz", MigrationDTOKatalogDatenschutz.class);
             mapTablename2DTOClass.put("K_Religion", MigrationDTOKonfession.class);
             mapTablename2DTOClass.put("EigeneSchule_Kursart", MigrationDTOKursarten.class);
             mapTablename2DTOClass.put("K_Ort", MigrationDTOOrt.class);
             mapTablename2DTOClass.put("K_Ortsteil", MigrationDTOOrtsteil.class);
             mapTablename2DTOClass.put("K_Schule", MigrationDTOSchuleNRW.class);
             mapTablename2DTOClass.put("K_Schulfunktionen", MigrationDTOSchulfunktion.class);
             mapTablename2DTOClass.put("K_Schwerpunkt", MigrationDTOSchwerpunkt.class);
             mapTablename2DTOClass.put("K_Textdateien", MigrationDTOTextDateien.class);
             mapTablename2DTOClass.put("K_Vermerkart", MigrationDTOVermerkArt.class);
             mapTablename2DTOClass.put("Klassen", MigrationDTOKlassen.class);
             mapTablename2DTOClass.put("KlassenLehrer", MigrationDTOKlassenLeitung.class);
             mapTablename2DTOClass.put("KlassenartenKatalog_Keys", MigrationDTOKlassenartenKatalogKeys.class);
             mapTablename2DTOClass.put("Versetzung", MigrationDTOVersetzung.class);
             mapTablename2DTOClass.put("Kurse", MigrationDTOKurs.class);
             mapTablename2DTOClass.put("KursLehrer", MigrationDTOKursLehrer.class);
             mapTablename2DTOClass.put("KursartenKatalog_Keys", MigrationDTOKursartenKatalogKeys.class);
             mapTablename2DTOClass.put("K_Lehrer", MigrationDTOLehrer.class);
             mapTablename2DTOClass.put("LehrerAbschnittsdaten", MigrationDTOLehrerAbschnittsdaten.class);
             mapTablename2DTOClass.put("LehrerAnrechnung", MigrationDTOLehrerAnrechnungsstunde.class);
             mapTablename2DTOClass.put("LehrerDatenschutz", MigrationDTOLehrerDatenschutz.class);
             mapTablename2DTOClass.put("LehrerEntlastung", MigrationDTOLehrerEntlastungsstunde.class);
             mapTablename2DTOClass.put("LehrerFotos", MigrationDTOLehrerFoto.class);
             mapTablename2DTOClass.put("LehrerFunktionen", MigrationDTOLehrerFunktion.class);
             mapTablename2DTOClass.put("LehrerLehramt", MigrationDTOLehrerLehramt.class);
             mapTablename2DTOClass.put("LehrerLehramtLehrbef", MigrationDTOLehrerLehramtBefaehigung.class);
             mapTablename2DTOClass.put("LehrerLehramtFachr", MigrationDTOLehrerLehramtFachrichtung.class);
             mapTablename2DTOClass.put("LehrerLeitungsfunktion_Keys", MigrationDTOLehrerLeitungsfunktionKeys.class);
             mapTablename2DTOClass.put("LehrerLernplattform", MigrationDTOLehrerLernplattform.class);
             mapTablename2DTOClass.put("LehrerMehrleistung", MigrationDTOLehrerMehrleistung.class);
             mapTablename2DTOClass.put("Schulleitung", MigrationDTOSchulleitung.class);
             mapTablename2DTOClass.put("Personengruppen", MigrationDTOPersonengruppen.class);
             mapTablename2DTOClass.put("Personengruppen_Personen", MigrationDTOPersonengruppenPersonen.class);
             mapTablename2DTOClass.put("K_EinschulungsArt", MigrationDTOEinschulungsart.class);
             mapTablename2DTOClass.put("K_EntlassGrund", MigrationDTOEntlassarten.class);
             mapTablename2DTOClass.put("K_Foerderschwerpunkt", MigrationDTOFoerderschwerpunkt.class);
             mapTablename2DTOClass.put("Schueler", MigrationDTOSchueler.class);
             mapTablename2DTOClass.put("SchuelerAbgaenge", MigrationDTOSchuelerAbgaenge.class);
             mapTablename2DTOClass.put("Schueler_AllgAdr", MigrationDTOSchuelerAllgemeineAdresse.class);
             mapTablename2DTOClass.put("SchuelerDatenschutz", MigrationDTOSchuelerDatenschutz.class);
             mapTablename2DTOClass.put("SchuelerFehlstunden", MigrationDTOSchuelerFehlstunden.class);
             mapTablename2DTOClass.put("SchuelerFoerderempfehlungen", MigrationDTOSchuelerFoerderempfehlung.class);
             mapTablename2DTOClass.put("SchuelerFotos", MigrationDTOSchuelerFoto.class);
             mapTablename2DTOClass.put("SchuelerGSDaten", MigrationDTOSchuelerGrundschuldaten.class);
             mapTablename2DTOClass.put("SchuelerKAoADaten", MigrationDTOSchuelerKAoADaten.class);
             mapTablename2DTOClass.put("SchuelerLeistungsdaten", MigrationDTOSchuelerLeistungsdaten.class);
             mapTablename2DTOClass.put("SchuelerLernabschnittsdaten", MigrationDTOSchuelerLernabschnittsdaten.class);
             mapTablename2DTOClass.put("SchuelerLernplattform", MigrationDTOSchuelerLernplattform.class);
             mapTablename2DTOClass.put("SchuelerMerkmale", MigrationDTOSchuelerMerkmale.class);
             mapTablename2DTOClass.put("SchuelerLD_PSFachBem", MigrationDTOSchuelerPSFachBemerkungen.class);
             mapTablename2DTOClass.put("SchuelerSprachenfolge", MigrationDTOSchuelerSprachenfolge.class);
             mapTablename2DTOClass.put("SchuelerSprachpruefungen", MigrationDTOSchuelerSprachpruefungen.class);
             mapTablename2DTOClass.put("SchuelerEinzelleistungen", MigrationDTOSchuelerTeilleistung.class);
             mapTablename2DTOClass.put("SchuelerVermerke", MigrationDTOSchuelerVermerke.class);
             mapTablename2DTOClass.put("SchuelerZP10", MigrationDTOSchuelerZP10.class);
             mapTablename2DTOClass.put("K_Sportbefreiung", MigrationDTOSportbefreiung.class);
             mapTablename2DTOClass.put("K_Einzelleistungen", MigrationDTOTeilleistungsarten.class);
             mapTablename2DTOClass.put("SchuelerAbitur", MigrationDTOSchuelerAbitur.class);
             mapTablename2DTOClass.put("SchuelerAbiFaecher", MigrationDTOSchuelerAbiturFach.class);
             mapTablename2DTOClass.put("SchuelerFHR", MigrationDTOSchuelerFHR.class);
             mapTablename2DTOClass.put("SchuelerFHRFaecher", MigrationDTOSchuelerFHRFach.class);
             mapTablename2DTOClass.put("EigeneSchule_Abteilungen", MigrationDTOAbteilungen.class);
             mapTablename2DTOClass.put("EigeneSchule_Abt_Kl", MigrationDTOAbteilungsKlassen.class);
             mapTablename2DTOClass.put("Jahrgaenge_Keys", MigrationDTOAlleJahrgaengeKeys.class);
             mapTablename2DTOClass.put("Schulformen", MigrationDTOAlleSchulformen.class);
             mapTablename2DTOClass.put("AllgemeineMerkmaleKatalog_Keys", MigrationDTOAllgemeineMerkmaleKatalogKeys.class);
             mapTablename2DTOClass.put("Berufskolleg_Anlagen", MigrationDTOBerufskollegAnlagen.class);
             mapTablename2DTOClass.put("Berufskolleg_Berufsebenen1", MigrationDTOBerufskollegBerufsebenen1.class);
             mapTablename2DTOClass.put("Berufskolleg_Berufsebenen2", MigrationDTOBerufskollegBerufsebenen2.class);
             mapTablename2DTOClass.put("Berufskolleg_Berufsebenen3", MigrationDTOBerufskollegBerufsebenen3.class);
             mapTablename2DTOClass.put("Berufskolleg_Fachklassen_Keys", MigrationDTOBerufskollegFachklassenKeys.class);
             mapTablename2DTOClass.put("EigeneSchule", MigrationDTOEigeneSchule.class);
             mapTablename2DTOClass.put("EigeneSchule_KAoADaten", MigrationDTOEigeneSchuleKAoADaten.class);
             mapTablename2DTOClass.put("EinschulungsartKatalog_Keys", MigrationDTOEinschulungsartenKatalogKeys.class);
             mapTablename2DTOClass.put("Herkunft", MigrationDTOHerkunft.class);
             mapTablename2DTOClass.put("Herkunft_Schulformen", MigrationDTOHerkunftSchulformen.class);
             mapTablename2DTOClass.put("Herkunftsart", MigrationDTOHerkunftsart.class);
             mapTablename2DTOClass.put("Herkunftsart_Schulformen", MigrationDTOHerkunftsartSchulformen.class);
             mapTablename2DTOClass.put("EigeneSchule_Jahrgaenge", MigrationDTOJahrgang.class);
             mapTablename2DTOClass.put("EigeneSchule_Merkmale", MigrationDTOMerkmale.class);
             mapTablename2DTOClass.put("OrganisationsformenKatalog_Keys", MigrationDTOOrganisationsformenKatalogKeys.class);
             mapTablename2DTOClass.put("EigeneSchule_Schulformen", MigrationDTOSchulformen.class);
             mapTablename2DTOClass.put("Schuljahresabschnitte", MigrationDTOSchuljahresabschnitte.class);
             mapTablename2DTOClass.put("EigeneSchule_Texte", MigrationDTOSchultexte.class);
             mapTablename2DTOClass.put("Stundentafel", MigrationDTOStundentafel.class);
             mapTablename2DTOClass.put("Stundentafel_Faecher", MigrationDTOStundentafelFaecher.class);
             mapTablename2DTOClass.put("EigeneSchule_Teilstandorte", MigrationDTOTeilstandorte.class);
             mapTablename2DTOClass.put("SchuelerStatus_Keys", MigrationDTOSchuelerStatus.class);
             mapTablename2DTOClass.put("Herkunft_Keys", MigrationDTOHerkunftKeys.class);
             mapTablename2DTOClass.put("Herkunftsart_Keys", MigrationDTOHerkunftsartKeys.class);
             mapTablename2DTOClass.put("KAoA_Anschlussoption_Keys", MigrationDTOKAoAAnschlussoptionKeys.class);
             mapTablename2DTOClass.put("KAoA_Berufsfeld_Keys", MigrationDTOKAoABerufsfeldKeys.class);
             mapTablename2DTOClass.put("KAoA_Kategorie_Keys", MigrationDTOKAoAKategorieKeys.class);
             mapTablename2DTOClass.put("KAoA_Merkmal_Keys", MigrationDTOKAoAMerkmalKeys.class);
             mapTablename2DTOClass.put("KAoA_SBO_Ebene4_Keys", MigrationDTOKAoASBOEB4Keys.class);
             mapTablename2DTOClass.put("KAoA_Zusatzmerkmal_Keys", MigrationDTOKAoAZusatzmerkmalKeys.class);
             mapTablename2DTOClass.put("Nationalitaeten_Keys", MigrationDTONationalitaetenKeys.class);
             mapTablename2DTOClass.put("Religionen_Keys", MigrationDTOReligionKeys.class);
             mapTablename2DTOClass.put("Credentials", MigrationDTOCredentials.class);
             mapTablename2DTOClass.put("CredentialsLernplattformen", MigrationDTOCredentialsLernplattformen.class);
             mapTablename2DTOClass.put("Lernplattformen", MigrationDTOLernplattformen.class);
             mapTablename2DTOClass.put("SchuleCredentials", MigrationDTOSchuleCredentials.class);
         }
         return mapTablename2DTOClass;
     }


    /**
     * Gibt die DTO-Klasse mit dem angegebenen Tabellennamen zurück.
     *
     * @param name   der Tabellenname
     *
     * @return die DTO-Klasse
     */
    public static Class<? extends Object> getFromTableName(final String name) {
    	return getMapTablename2DTOClass().get(name);
    }

}
