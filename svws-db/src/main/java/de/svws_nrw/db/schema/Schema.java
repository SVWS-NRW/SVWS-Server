package de.svws_nrw.db.schema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import de.svws_nrw.db.schema.tabellen.Tabelle_AllgAdrAnsprechpartner;
import de.svws_nrw.db.schema.tabellen.Tabelle_AllgemeineMerkmaleKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Benutzer;
import de.svws_nrw.db.schema.tabellen.Tabelle_BenutzerAllgemein;
import de.svws_nrw.db.schema.tabellen.Tabelle_BenutzerEmail;
import de.svws_nrw.db.schema.tabellen.Tabelle_BenutzerKompetenzen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Benutzergruppen;
import de.svws_nrw.db.schema.tabellen.Tabelle_BenutzergruppenKompetenzen;
import de.svws_nrw.db.schema.tabellen.Tabelle_BenutzergruppenMitglieder;
import de.svws_nrw.db.schema.tabellen.Tabelle_Berufskolleg_Anlagen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Berufskolleg_Berufsebenen1;
import de.svws_nrw.db.schema.tabellen.Tabelle_Berufskolleg_Berufsebenen2;
import de.svws_nrw.db.schema.tabellen.Tabelle_Berufskolleg_Berufsebenen3;
import de.svws_nrw.db.schema.tabellen.Tabelle_Berufskolleg_Fachklassen_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Credentials;
import de.svws_nrw.db.schema.tabellen.Tabelle_CredentialsLernplattformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_DavRessourceCollections;
import de.svws_nrw.db.schema.tabellen.Tabelle_DavRessourceCollectionsACL;
import de.svws_nrw.db.schema.tabellen.Tabelle_DavRessources;
import de.svws_nrw.db.schema.tabellen.Tabelle_DavSyncTokenLehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_DavSyncTokenSchueler;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Abt_Kl;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Abteilungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_FachTeilleistungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Fachklassen;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Faecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Jahrgaenge;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_KAoADaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Kursart;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Merkmale;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Schulformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Teilstandorte;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Texte;
import de.svws_nrw.db.schema.tabellen.Tabelle_EigeneSchule_Zertifikate;
import de.svws_nrw.db.schema.tabellen.Tabelle_EinschulungsartKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_EnmLeistungsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_EnmLernabschnittsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_ErzieherDatenschutz;
import de.svws_nrw.db.schema.tabellen.Tabelle_ErzieherLernplattform;
import de.svws_nrw.db.schema.tabellen.Tabelle_FachKatalog;
import de.svws_nrw.db.schema.tabellen.Tabelle_FachKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_FachKatalog_Schulformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Fach_Gliederungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Fachgruppen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Floskelgruppen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Floskeln;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Kurse;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Kurslehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Regeln;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Regelparameter;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Schienen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Zwischenergebnisse;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Jahrgang_Beratungslehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Jahrgang_Fachkombinationen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Jahrgang_Faecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Jahrgangsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Kalenderinformationen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Kursklausuren;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_NtaZeiten;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Raeume;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Raeume_Stunden;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Raeume_Stunden_Aufsichten;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Schuelerklausuren;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Termine;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Klausuren_Vorgaben;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Schueler;
import de.svws_nrw.db.schema.tabellen.Tabelle_Gost_Schueler_Fachwahlen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunft;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunft_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunft_Schulformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunftsart;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunftsart_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Herkunftsart_Schulformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_ImpExp_EigeneImporte;
import de.svws_nrw.db.schema.tabellen.Tabelle_ImpExp_EigeneImporte_Felder;
import de.svws_nrw.db.schema.tabellen.Tabelle_ImpExp_EigeneImporte_Tabellen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Jahrgaenge_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_Anschlussoption_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_Berufsfeld_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_Kategorie_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_Merkmal_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_SBO_Ebene4_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_KAoA_Zusatzmerkmal_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Adressart;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_AllgAdresse;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Ankreuzdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Ankreuzfloskeln;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_BeschaeftigungsArt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Datenschutz;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_EinschulungsArt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Einzelleistungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_EntlassGrund;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_ErzieherArt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_ErzieherFunktion;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_FahrschuelerArt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Foerderschwerpunkt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Haltestelle;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Kindergarten;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Lehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Ort;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Ortsteil;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Religion;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Schule;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Schulfunktionen;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Schwerpunkt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Sportbefreiung;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_TelefonArt;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Textdateien;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Vermerkart;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Zertifikate;
import de.svws_nrw.db.schema.tabellen.Tabelle_Katalog_Aufsichtsbereich;
import de.svws_nrw.db.schema.tabellen.Tabelle_Katalog_Pausenzeiten;
import de.svws_nrw.db.schema.tabellen.Tabelle_Katalog_Raeume;
import de.svws_nrw.db.schema.tabellen.Tabelle_Katalog_Zeitraster;
import de.svws_nrw.db.schema.tabellen.Tabelle_Klassen;
import de.svws_nrw.db.schema.tabellen.Tabelle_KlassenLehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_KlassenartenKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Kompetenzen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Kompetenzgruppen;
import de.svws_nrw.db.schema.tabellen.Tabelle_KursFortschreibungsarten;
import de.svws_nrw.db.schema.tabellen.Tabelle_KursLehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_Kurs_Schueler;
import de.svws_nrw.db.schema.tabellen.Tabelle_KursartenKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_Kurse;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerAbschnittsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerAnrechnung;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerDatenschutz;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerEntlastung;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerFotos;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerFunktionen;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerLehramt;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerLehramtFachr;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerLehramtLehrbef;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerLeitungsfunktion_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerLernplattform;
import de.svws_nrw.db.schema.tabellen.Tabelle_LehrerMehrleistung;
import de.svws_nrw.db.schema.tabellen.Tabelle_Lernplattformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Logins;
import de.svws_nrw.db.schema.tabellen.Tabelle_Nationalitaeten_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_NichtMoeglAbiFachKombi;
import de.svws_nrw.db.schema.tabellen.Tabelle_Noten;
import de.svws_nrw.db.schema.tabellen.Tabelle_OrganisationsformenKatalog_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_PersonalTypen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Personengruppen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Personengruppen_Personen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Religionen_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_SVWS_Client_Konfiguration_Benutzer;
import de.svws_nrw.db.schema.tabellen.Tabelle_SVWS_Client_Konfiguration_Global;
import de.svws_nrw.db.schema.tabellen.Tabelle_SVWS_Core_Type_Versionen;
import de.svws_nrw.db.schema.tabellen.Tabelle_SVWS_DB_AutoInkremente;
import de.svws_nrw.db.schema.tabellen.Tabelle_SVWS_DB_Version;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchildFilter;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schild_Verwaltung;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schueler;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerAbgaenge;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerAbiFaecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerAbitur;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerAnkreuzfloskeln;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerBKAbschluss;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerBKFaecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerDatenschutz;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerEinzelleistungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerErzAdr;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerFHR;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerFHRFaecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerFehlstunden;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerFoerderempfehlungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerFotos;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerGSDaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerKAoADaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerLD_PSFachBem;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerLeistungsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerLernabschnittsdaten;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerLernplattform;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerListe;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerListe_Inhalt;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerMerkmale;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerReportvorlagen;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerSprachenfolge;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerSprachpruefungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerStatus_Keys;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerTelefone;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerVermerke;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerWiedervorlage;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerZP10;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuelerZuweisungen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schueler_AllgAdr;
import de.svws_nrw.db.schema.tabellen.Tabelle_SchuleCredentials;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schulformen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schuljahresabschnitte;
import de.svws_nrw.db.schema.tabellen.Tabelle_Schulleitung;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Aufsichtsbereiche;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Kalenderwochen_Zuordnung;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Pausenaufsichten;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_PausenaufsichtenBereich;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Pausenzeit;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Raeume;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Unterricht;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_UnterrichtKlasse;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_UnterrichtLehrer;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_UnterrichtRaum;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundenplan_Zeitraster;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundentafel;
import de.svws_nrw.db.schema.tabellen.Tabelle_Stundentafel_Faecher;
import de.svws_nrw.db.schema.tabellen.Tabelle_TextExportVorlagen;
import de.svws_nrw.db.schema.tabellen.Tabelle_Usergroups;
import de.svws_nrw.db.schema.tabellen.Tabelle_Users;
import de.svws_nrw.db.schema.tabellen.Tabelle_Versetzung;
import de.svws_nrw.db.schema.tabellen.Tabelle_ZuordnungReportvorlagen;


/**
 * Diese Klasse stellt alle Tabellen des Schemas zur Verfügung.
 */
public class Schema {

	/** Das übergeordnete Java-Paket, welches die Klassen für die SVWS-Datenbank beinhaltet */
	public static final String javaPackage = "de.svws_nrw.db";

	/** Das Java-Unterpaket, welches die Klassen für die generierten DTOs für den Dantebankzugriff beinhaltet */
	public static final String javaDTOPackage = "dto";

	/** Eine Map von dem Namen der Tabelle auf die einzelnen Tabellen. */
	public static final LinkedHashMap<String, SchemaTabelle> tabellen = new LinkedHashMap<>();

	/**
	 * Fügt eine Tabelle zu dem Schema hinzu.
	 *
	 * @param <T> der Typ der Tabellen-Definitions-Klasse
	 * @param t   die Instanz der Tabellen-Definitions-Klasse
	 *
	 * @return die Instanz der Tabellen-Definitions-Klasse
	 */
	public static <T extends SchemaTabelle> T add(final T t) {
		tabellen.put(t.name(), t);
		return t;
	}


	/** Tabelle AllgemeineMerkmaleKatalog_Keys */
	public static final Tabelle_AllgemeineMerkmaleKatalog_Keys tab_AllgemeineMerkmaleKatalog_Keys = add(new Tabelle_AllgemeineMerkmaleKatalog_Keys());

	/** Tabelle Benutzergruppen */
	public static final Tabelle_Benutzergruppen tab_Benutzergruppen = add(new Tabelle_Benutzergruppen());

	/** Tabelle Berufskolleg_Anlagen */
	public static final Tabelle_Berufskolleg_Anlagen tab_Berufskolleg_Anlagen = add(new Tabelle_Berufskolleg_Anlagen());

	/** Tabelle Berufskolleg_Berufsebenen1 */
	public static final Tabelle_Berufskolleg_Berufsebenen1 tab_Berufskolleg_Berufsebenen1 = add(new Tabelle_Berufskolleg_Berufsebenen1());

	/** Tabelle Berufskolleg_Berufsebenen2 */
	public static final Tabelle_Berufskolleg_Berufsebenen2 tab_Berufskolleg_Berufsebenen2 = add(new Tabelle_Berufskolleg_Berufsebenen2());

	/** Tabelle Berufskolleg_Berufsebenen3 */
	public static final Tabelle_Berufskolleg_Berufsebenen3 tab_Berufskolleg_Berufsebenen3 = add(new Tabelle_Berufskolleg_Berufsebenen3());

	/** Tabelle Berufskolleg_Fachklassen_Keys */
	public static final Tabelle_Berufskolleg_Fachklassen_Keys tab_Berufskolleg_Fachklassen_Keys = add(new Tabelle_Berufskolleg_Fachklassen_Keys());

	/** Tabelle Credentials */
	public static final Tabelle_Credentials tab_Credentials = add(new Tabelle_Credentials());

	/** Tabelle BenutzerAllgemein */
	public static final Tabelle_BenutzerAllgemein tab_BenutzerAllgemein = add(new Tabelle_BenutzerAllgemein());

	/** Tabelle EigeneSchule_Fachklassen */
	public static final Tabelle_EigeneSchule_Fachklassen tab_EigeneSchule_Fachklassen = add(new Tabelle_EigeneSchule_Fachklassen());

	/** Tabelle EigeneSchule_KAoADaten */
	public static final Tabelle_EigeneSchule_KAoADaten tab_EigeneSchule_KAoADaten = add(new Tabelle_EigeneSchule_KAoADaten());

	/** Tabelle EigeneSchule_Kursart */
	public static final Tabelle_EigeneSchule_Kursart tab_EigeneSchule_Kursart = add(new Tabelle_EigeneSchule_Kursart());

	/** Tabelle EigeneSchule_Merkmale */
	public static final Tabelle_EigeneSchule_Merkmale tab_EigeneSchule_Merkmale = add(new Tabelle_EigeneSchule_Merkmale());

	/** Tabelle EigeneSchule_Schulformen */
	public static final Tabelle_EigeneSchule_Schulformen tab_EigeneSchule_Schulformen = add(new Tabelle_EigeneSchule_Schulformen());

	/** Tabelle EigeneSchule_Teilstandorte */
	public static final Tabelle_EigeneSchule_Teilstandorte tab_EigeneSchule_Teilstandorte = add(new Tabelle_EigeneSchule_Teilstandorte());

	/** Tabelle EigeneSchule_Texte */
	public static final Tabelle_EigeneSchule_Texte tab_EigeneSchule_Texte = add(new Tabelle_EigeneSchule_Texte());

	/** Tabelle EigeneSchule_Zertifikate */
	public static final Tabelle_EigeneSchule_Zertifikate tab_EigeneSchule_Zertifikate = add(new Tabelle_EigeneSchule_Zertifikate());

	/** Tabelle EinschulungsartKatalog_Keys */
	public static final Tabelle_EinschulungsartKatalog_Keys tab_EinschulungsartKatalog_Keys = add(new Tabelle_EinschulungsartKatalog_Keys());

	/** Tabelle FachKatalog */
	public static final Tabelle_FachKatalog tab_FachKatalog = add(new Tabelle_FachKatalog());

	/** Tabelle FachKatalog_Keys */
	public static final Tabelle_FachKatalog_Keys tab_FachKatalog_Keys = add(new Tabelle_FachKatalog_Keys());

	/** Tabelle FachKatalog_Schulformen */
	public static final Tabelle_FachKatalog_Schulformen tab_FachKatalog_Schulformen = add(new Tabelle_FachKatalog_Schulformen());

	/** Tabelle Fach_Gliederungen */
	public static final Tabelle_Fach_Gliederungen tab_Fach_Gliederungen = add(new Tabelle_Fach_Gliederungen());

	/** Tabelle Fachgruppen */
	public static final Tabelle_Fachgruppen tab_Fachgruppen = add(new Tabelle_Fachgruppen());

	/** Tabelle EigeneSchule_Faecher */
	public static final Tabelle_EigeneSchule_Faecher tab_EigeneSchule_Faecher = add(new Tabelle_EigeneSchule_Faecher());

	/** Tabelle EigeneSchule_FachTeilleistungen */
	public static final Tabelle_EigeneSchule_FachTeilleistungen tab_EigeneSchule_FachTeilleistungen = add(new Tabelle_EigeneSchule_FachTeilleistungen());

	/** Tabelle Gost_Jahrgangsdaten */
	public static final Tabelle_Gost_Jahrgangsdaten tab_Gost_Jahrgangsdaten = add(new Tabelle_Gost_Jahrgangsdaten());

	/** Tabelle Gost_Blockung */
	public static final Tabelle_Gost_Blockung tab_Gost_Blockung = add(new Tabelle_Gost_Blockung());

	/** Tabelle Gost_Jahrgang_Fachkombinationen */
	public static final Tabelle_Gost_Jahrgang_Fachkombinationen tab_Gost_Jahrgang_Fachkombinationen = add(new Tabelle_Gost_Jahrgang_Fachkombinationen());

	/** Tabelle Gost_Jahrgang_Faecher */
	public static final Tabelle_Gost_Jahrgang_Faecher tab_Gost_Jahrgang_Faecher = add(new Tabelle_Gost_Jahrgang_Faecher());

	/** Tabelle Gost_Blockung_Kurse */
	public static final Tabelle_Gost_Blockung_Kurse tab_Gost_Blockung_Kurse = add(new Tabelle_Gost_Blockung_Kurse());

	/** Tabelle Gost_Blockung_Regeln */
	public static final Tabelle_Gost_Blockung_Regeln tab_Gost_Blockung_Regeln = add(new Tabelle_Gost_Blockung_Regeln());

	/** Tabelle Gost_Blockung_Schienen */
	public static final Tabelle_Gost_Blockung_Schienen tab_Gost_Blockung_Schienen = add(new Tabelle_Gost_Blockung_Schienen());

	/** Tabelle Gost_Blockung_Zwischenergebnisse */
	public static final Tabelle_Gost_Blockung_Zwischenergebnisse tab_Gost_Blockung_Zwischenergebnisse = add(new Tabelle_Gost_Blockung_Zwischenergebnisse());

	/** Tabelle Gost_Blockung_Regelparameter */
	public static final Tabelle_Gost_Blockung_Regelparameter tab_Gost_Blockung_Regelparameter = add(new Tabelle_Gost_Blockung_Regelparameter());

	/** Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen */
	public static final Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen tab_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen = add(new Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schienen());

	/** Tabelle Herkunft */
	public static final Tabelle_Herkunft tab_Herkunft = add(new Tabelle_Herkunft());

	/** Tabelle Herkunft_Keys */
	public static final Tabelle_Herkunft_Keys tab_Herkunft_Keys = add(new Tabelle_Herkunft_Keys());

	/** Tabelle Herkunft_Schulformen */
	public static final Tabelle_Herkunft_Schulformen tab_Herkunft_Schulformen = add(new Tabelle_Herkunft_Schulformen());

	/** Tabelle Herkunftsart */
	public static final Tabelle_Herkunftsart tab_Herkunftsart = add(new Tabelle_Herkunftsart());

	/** Tabelle Herkunftsart_Keys */
	public static final Tabelle_Herkunftsart_Keys tab_Herkunftsart_Keys = add(new Tabelle_Herkunftsart_Keys());

	/** Tabelle Herkunftsart_Schulformen */
	public static final Tabelle_Herkunftsart_Schulformen tab_Herkunftsart_Schulformen = add(new Tabelle_Herkunftsart_Schulformen());

	/** Tabelle ImpExp_EigeneImporte */
	public static final Tabelle_ImpExp_EigeneImporte tab_ImpExp_EigeneImporte = add(new Tabelle_ImpExp_EigeneImporte());

	/** Tabelle ImpExp_EigeneImporte_Felder */
	public static final Tabelle_ImpExp_EigeneImporte_Felder tab_ImpExp_EigeneImporte_Felder = add(new Tabelle_ImpExp_EigeneImporte_Felder());

	/** Tabelle ImpExp_EigeneImporte_Tabellen */
	public static final Tabelle_ImpExp_EigeneImporte_Tabellen tab_ImpExp_EigeneImporte_Tabellen = add(new Tabelle_ImpExp_EigeneImporte_Tabellen());

	/** Tabelle Jahrgaenge_Keys */
	public static final Tabelle_Jahrgaenge_Keys tab_Jahrgaenge_Keys = add(new Tabelle_Jahrgaenge_Keys());

	/** Tabelle K_Adressart */
	public static final Tabelle_K_Adressart tab_K_Adressart = add(new Tabelle_K_Adressart());

	/** Tabelle K_Ankreuzdaten */
	public static final Tabelle_K_Ankreuzdaten tab_K_Ankreuzdaten = add(new Tabelle_K_Ankreuzdaten());

	/** Tabelle K_Ankreuzfloskeln */
	public static final Tabelle_K_Ankreuzfloskeln tab_K_Ankreuzfloskeln = add(new Tabelle_K_Ankreuzfloskeln());

	/** Tabelle K_BeschaeftigungsArt */
	public static final Tabelle_K_BeschaeftigungsArt tab_K_BeschaeftigungsArt = add(new Tabelle_K_BeschaeftigungsArt());

	/** Tabelle K_Datenschutz */
	public static final Tabelle_K_Datenschutz tab_K_Datenschutz = add(new Tabelle_K_Datenschutz());

	/** Tabelle K_EinschulungsArt */
	public static final Tabelle_K_EinschulungsArt tab_K_EinschulungsArt = add(new Tabelle_K_EinschulungsArt());

	/** Tabelle K_Einzelleistungen */
	public static final Tabelle_K_Einzelleistungen tab_K_Einzelleistungen = add(new Tabelle_K_Einzelleistungen());

	/** Tabelle K_EntlassGrund */
	public static final Tabelle_K_EntlassGrund tab_K_EntlassGrund = add(new Tabelle_K_EntlassGrund());

	/** Tabelle K_ErzieherArt */
	public static final Tabelle_K_ErzieherArt tab_K_ErzieherArt = add(new Tabelle_K_ErzieherArt());

	/** Tabelle K_ErzieherFunktion */
	public static final Tabelle_K_ErzieherFunktion tab_K_ErzieherFunktion = add(new Tabelle_K_ErzieherFunktion());

	/** Tabelle K_FahrschuelerArt */
	public static final Tabelle_K_FahrschuelerArt tab_K_FahrschuelerArt = add(new Tabelle_K_FahrschuelerArt());

	/** Tabelle K_Foerderschwerpunkt */
	public static final Tabelle_K_Foerderschwerpunkt tab_K_Foerderschwerpunkt = add(new Tabelle_K_Foerderschwerpunkt());

	/** Tabelle K_Haltestelle */
	public static final Tabelle_K_Haltestelle tab_K_Haltestelle = add(new Tabelle_K_Haltestelle());

	/** Tabelle K_Kindergarten */
	public static final Tabelle_K_Kindergarten tab_K_Kindergarten = add(new Tabelle_K_Kindergarten());

	/** Tabelle K_Ort */
	public static final Tabelle_K_Ort tab_K_Ort = add(new Tabelle_K_Ort());

	/** Tabelle K_AllgAdresse */
	public static final Tabelle_K_AllgAdresse tab_K_AllgAdresse = add(new Tabelle_K_AllgAdresse());

	/** Tabelle AllgAdrAnsprechpartner */
	public static final Tabelle_AllgAdrAnsprechpartner tab_AllgAdrAnsprechpartner = add(new Tabelle_AllgAdrAnsprechpartner());

	/** Tabelle K_Ortsteil */
	public static final Tabelle_K_Ortsteil tab_K_Ortsteil = add(new Tabelle_K_Ortsteil());

	/** Tabelle K_Religion */
	public static final Tabelle_K_Religion tab_K_Religion = add(new Tabelle_K_Religion());

	/** Tabelle K_Schule */
	public static final Tabelle_K_Schule tab_K_Schule = add(new Tabelle_K_Schule());

	/** Tabelle K_Schulfunktionen */
	public static final Tabelle_K_Schulfunktionen tab_K_Schulfunktionen = add(new Tabelle_K_Schulfunktionen());

	/** Tabelle K_Schwerpunkt */
	public static final Tabelle_K_Schwerpunkt tab_K_Schwerpunkt = add(new Tabelle_K_Schwerpunkt());

	/** Tabelle K_Sportbefreiung */
	public static final Tabelle_K_Sportbefreiung tab_K_Sportbefreiung = add(new Tabelle_K_Sportbefreiung());

	/** Tabelle K_TelefonArt */
	public static final Tabelle_K_TelefonArt tab_K_TelefonArt = add(new Tabelle_K_TelefonArt());

	/** Tabelle K_Textdateien */
	public static final Tabelle_K_Textdateien tab_K_Textdateien = add(new Tabelle_K_Textdateien());

	/** Tabelle K_Vermerkart */
	public static final Tabelle_K_Vermerkart tab_K_Vermerkart = add(new Tabelle_K_Vermerkart());

	/** Tabelle K_Zertifikate */
	public static final Tabelle_K_Zertifikate tab_K_Zertifikate = add(new Tabelle_K_Zertifikate());

	/** Tabelle Katalog_Aufsichtsbereich */
	public static final Tabelle_Katalog_Aufsichtsbereich tab_Katalog_Aufsichtsbereich = add(new Tabelle_Katalog_Aufsichtsbereich());

	/** Tabelle Katalog_Pausenzeiten */
	public static final Tabelle_Katalog_Pausenzeiten tab_Katalog_Pausenzeiten = add(new Tabelle_Katalog_Pausenzeiten());

	/** Tabelle Katalog_Raeume */
	public static final Tabelle_Katalog_Raeume tab_Katalog_Raeume = add(new Tabelle_Katalog_Raeume());

	/** Tabelle Katalog_Zeitraster */
	public static final Tabelle_Katalog_Zeitraster tab_Katalog_Zeitraster = add(new Tabelle_Katalog_Zeitraster());

	/** Tabelle KlassenartenKatalog_Keys */
	public static final Tabelle_KlassenartenKatalog_Keys tab_KlassenartenKatalog_Keys = add(new Tabelle_KlassenartenKatalog_Keys());

	/** Tabelle Kompetenzgruppen */
	public static final Tabelle_Kompetenzgruppen tab_Kompetenzgruppen = add(new Tabelle_Kompetenzgruppen());

	/** Tabelle Kompetenzen */
	public static final Tabelle_Kompetenzen tab_Kompetenzen = add(new Tabelle_Kompetenzen());

	/** Tabelle BenutzergruppenKompetenzen */
	public static final Tabelle_BenutzergruppenKompetenzen tab_BenutzergruppenKompetenzen = add(new Tabelle_BenutzergruppenKompetenzen());

	/** Tabelle KursFortschreibungsarten */
	public static final Tabelle_KursFortschreibungsarten tab_KursFortschreibungsarten = add(new Tabelle_KursFortschreibungsarten());

	/** Tabelle KursartenKatalog_Keys */
	public static final Tabelle_KursartenKatalog_Keys tab_KursartenKatalog_Keys = add(new Tabelle_KursartenKatalog_Keys());

	/** Tabelle Lernplattformen */
	public static final Tabelle_Lernplattformen tab_Lernplattformen = add(new Tabelle_Lernplattformen());

	/** Tabelle CredentialsLernplattformen */
	public static final Tabelle_CredentialsLernplattformen tab_CredentialsLernplattformen = add(new Tabelle_CredentialsLernplattformen());

	/** Tabelle Nationalitaeten_Keys */
	public static final Tabelle_Nationalitaeten_Keys tab_Nationalitaeten_Keys = add(new Tabelle_Nationalitaeten_Keys());

	/** Tabelle NichtMoeglAbiFachKombi */
	public static final Tabelle_NichtMoeglAbiFachKombi tab_NichtMoeglAbiFachKombi = add(new Tabelle_NichtMoeglAbiFachKombi());

	/** Tabelle Noten */
	public static final Tabelle_Noten tab_Noten = add(new Tabelle_Noten());

	/** Tabelle OrganisationsformenKatalog_Keys */
	public static final Tabelle_OrganisationsformenKatalog_Keys tab_OrganisationsformenKatalog_Keys = add(new Tabelle_OrganisationsformenKatalog_Keys());

	/** Tabelle PersonalTypen */
	public static final Tabelle_PersonalTypen tab_PersonalTypen = add(new Tabelle_PersonalTypen());

	/** Tabelle K_Lehrer */
	public static final Tabelle_K_Lehrer tab_K_Lehrer = add(new Tabelle_K_Lehrer());

	/** Tabelle Gost_Blockung_Kurslehrer */
	public static final Tabelle_Gost_Blockung_Kurslehrer tab_Gost_Blockung_Kurslehrer = add(new Tabelle_Gost_Blockung_Kurslehrer());

	/** Tabelle Gost_Jahrgang_Beratungslehrer */
	public static final Tabelle_Gost_Jahrgang_Beratungslehrer tab_Gost_Jahrgang_Beratungslehrer = add(new Tabelle_Gost_Jahrgang_Beratungslehrer());

	/** Tabelle LehrerDatenschutz */
	public static final Tabelle_LehrerDatenschutz tab_LehrerDatenschutz = add(new Tabelle_LehrerDatenschutz());

	/** Tabelle LehrerFotos */
	public static final Tabelle_LehrerFotos tab_LehrerFotos = add(new Tabelle_LehrerFotos());

	/** Tabelle LehrerLehramt */
	public static final Tabelle_LehrerLehramt tab_LehrerLehramt = add(new Tabelle_LehrerLehramt());

	/** Tabelle LehrerLehramtFachr */
	public static final Tabelle_LehrerLehramtFachr tab_LehrerLehramtFachr = add(new Tabelle_LehrerLehramtFachr());

	/** Tabelle LehrerLehramtLehrbef */
	public static final Tabelle_LehrerLehramtLehrbef tab_LehrerLehramtLehrbef = add(new Tabelle_LehrerLehramtLehrbef());

	/** Tabelle LehrerLernplattform */
	public static final Tabelle_LehrerLernplattform tab_LehrerLernplattform = add(new Tabelle_LehrerLernplattform());

	/** Tabelle Personengruppen */
	public static final Tabelle_Personengruppen tab_Personengruppen = add(new Tabelle_Personengruppen());

	/** Tabelle Personengruppen_Personen */
	public static final Tabelle_Personengruppen_Personen tab_Personengruppen_Personen = add(new Tabelle_Personengruppen_Personen());

	/** Tabelle Religionen_Keys */
	public static final Tabelle_Religionen_Keys tab_Religionen_Keys = add(new Tabelle_Religionen_Keys());

	/** Tabelle SVWS_Client_Konfiguration_Global */
	public static final Tabelle_SVWS_Client_Konfiguration_Global tab_SVWS_Client_Konfiguration_Global = add(new Tabelle_SVWS_Client_Konfiguration_Global());

	/** Tabelle SVWS_Core_Type_Versionen */
	public static final Tabelle_SVWS_Core_Type_Versionen tab_SVWS_Core_Type_Versionen = add(new Tabelle_SVWS_Core_Type_Versionen());

	/** Tabelle SVWS_DB_AutoInkremente */
	public static final Tabelle_SVWS_DB_AutoInkremente tab_SVWS_DB_AutoInkremente = add(new Tabelle_SVWS_DB_AutoInkremente());

	/** Tabelle SVWS_DB_Version */
	public static final Tabelle_SVWS_DB_Version tab_SVWS_DB_Version = add(new Tabelle_SVWS_DB_Version());

	/** Tabelle SchildFilter */
	public static final Tabelle_SchildFilter tab_SchildFilter = add(new Tabelle_SchildFilter());

	/** Tabelle Schildintern_KAoA_Anschlussoption */
	public static final Tabelle_KAoA_Anschlussoption_Keys tab_KAoA_Anschlussoption_Keys = add(new Tabelle_KAoA_Anschlussoption_Keys());

	/** Tabelle Schildintern_KAoA_Berufsfeld */
	public static final Tabelle_KAoA_Berufsfeld_Keys tab_KAoA_Berufsfeld_Keys = add(new Tabelle_KAoA_Berufsfeld_Keys());

	/** Tabelle Schildintern_KAoA_Kategorie */
	public static final Tabelle_KAoA_Kategorie_Keys tab_KAoA_Kategorie_Keys = add(new Tabelle_KAoA_Kategorie_Keys());

	/** Tabelle Schildintern_KAoA_Merkmal */
	public static final Tabelle_KAoA_Merkmal_Keys tab_KAoA_Merkmal_Keys = add(new Tabelle_KAoA_Merkmal_Keys());

	/** Tabelle Schildintern_KAoA_Zusatzmerkmal */
	public static final Tabelle_KAoA_Zusatzmerkmal_Keys tab_KAoA_Zusatzmerkmal_Keys = add(new Tabelle_KAoA_Zusatzmerkmal_Keys());

	/** Tabelle Schildintern_KAoA_SBO_Ebene4 */
	public static final Tabelle_KAoA_SBO_Ebene4_Keys tab_KAoA_SBO_Ebene4_Keys = add(new Tabelle_KAoA_SBO_Ebene4_Keys());

	/** Tabelle SchuelerListe */
	public static final Tabelle_SchuelerListe tab_SchuelerListe = add(new Tabelle_SchuelerListe());

	/** Tabelle SchuelerStatus */
	public static final Tabelle_SchuelerStatus_Keys tab_SchuelerStatus = add(new Tabelle_SchuelerStatus_Keys());

	/** Tabelle SchuleCredentials */
	public static final Tabelle_SchuleCredentials tab_SchuleCredentials = add(new Tabelle_SchuleCredentials());

	/** Tabelle Schulformen */
	public static final Tabelle_Schulformen tab_Schulformen = add(new Tabelle_Schulformen());

	/** Tabelle Schuljahresabschnitte */
	public static final Tabelle_Schuljahresabschnitte tab_Schuljahresabschnitte = add(new Tabelle_Schuljahresabschnitte());

	/** Tabelle EigeneSchule */
	public static final Tabelle_EigeneSchule tab_EigeneSchule = add(new Tabelle_EigeneSchule());

	/** Tabelle EigeneSchule_Abteilungen */
	public static final Tabelle_EigeneSchule_Abteilungen tab_EigeneSchule_Abteilungen = add(new Tabelle_EigeneSchule_Abteilungen());

	/** Tabelle EigeneSchule_Jahrgaenge */
	public static final Tabelle_EigeneSchule_Jahrgaenge tab_EigeneSchule_Jahrgaenge = add(new Tabelle_EigeneSchule_Jahrgaenge());

	/** Tabelle LehrerAbschnittsdaten */
	public static final Tabelle_LehrerAbschnittsdaten tab_LehrerAbschnittsdaten = add(new Tabelle_LehrerAbschnittsdaten());

	/** Tabelle Klassen */
	public static final Tabelle_Klassen tab_Klassen = add(new Tabelle_Klassen());

	/** Tabelle Kurse */
	public static final Tabelle_Kurse tab_Kurse = add(new Tabelle_Kurse());

	/** Tabelle LehrerAnrechnung */
	public static final Tabelle_LehrerAnrechnung tab_LehrerAnrechnung = add(new Tabelle_LehrerAnrechnung());

	/** Tabelle LehrerEntlastung */
	public static final Tabelle_LehrerEntlastung tab_LehrerEntlastung = add(new Tabelle_LehrerEntlastung());

	/** Tabelle LehrerFunktionen */
	public static final Tabelle_LehrerFunktionen tab_LehrerFunktionen = add(new Tabelle_LehrerFunktionen());

	/** Tabelle LehrerMehrleistung */
	public static final Tabelle_LehrerMehrleistung tab_LehrerMehrleistung = add(new Tabelle_LehrerMehrleistung());

	/** Tabelle Schueler */
	public static final Tabelle_Schueler tab_Schueler = add(new Tabelle_Schueler());

	/** Tabelle EigeneSchule_Abt_Kl */
	public static final Tabelle_EigeneSchule_Abt_Kl tab_EigeneSchule_Abt_Kl = add(new Tabelle_EigeneSchule_Abt_Kl());

	/** Tabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler */
	public static final Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler tab_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler = add(new Tabelle_Gost_Blockung_Zwischenergebnisse_Kurs_Schueler());

	/** Tabelle Gost_Schueler */
	public static final Tabelle_Gost_Schueler tab_Gost_Schueler = add(new Tabelle_Gost_Schueler());

	/** Tabelle Gost_Schueler_Fachwahlen */
	public static final Tabelle_Gost_Schueler_Fachwahlen tab_Gost_Schueler_Fachwahlen = add(new Tabelle_Gost_Schueler_Fachwahlen());

	/** Tabelle KlassenLehrer */
	public static final Tabelle_KlassenLehrer tab_KlassenLehrer = add(new Tabelle_KlassenLehrer());

	/** Tabelle KursLehrer */
	public static final Tabelle_KursLehrer tab_KursLehrer = add(new Tabelle_KursLehrer());

	/** Tabelle Kurs_Schueler */
	public static final Tabelle_Kurs_Schueler tab_Kurs_Schueler = add(new Tabelle_Kurs_Schueler());

	/** Tabelle SchuelerAbgaenge */
	public static final Tabelle_SchuelerAbgaenge tab_SchuelerAbgaenge = add(new Tabelle_SchuelerAbgaenge());

	/** Tabelle SchuelerAbiFaecher */
	public static final Tabelle_SchuelerAbiFaecher tab_SchuelerAbiFaecher = add(new Tabelle_SchuelerAbiFaecher());

	/** Tabelle SchuelerAbitur */
	public static final Tabelle_SchuelerAbitur tab_SchuelerAbitur = add(new Tabelle_SchuelerAbitur());

	/** Tabelle SchuelerBKAbschluss */
	public static final Tabelle_SchuelerBKAbschluss tab_SchuelerBKAbschluss = add(new Tabelle_SchuelerBKAbschluss());

	/** Tabelle SchuelerBKFaecher */
	public static final Tabelle_SchuelerBKFaecher tab_SchuelerBKFaecher = add(new Tabelle_SchuelerBKFaecher());

	/** Tabelle SchuelerDatenschutz */
	public static final Tabelle_SchuelerDatenschutz tab_SchuelerDatenschutz = add(new Tabelle_SchuelerDatenschutz());

	/** Tabelle SchuelerErzAdr */
	public static final Tabelle_SchuelerErzAdr tab_SchuelerErzAdr = add(new Tabelle_SchuelerErzAdr());

	/** Tabelle SchuelerFHR */
	public static final Tabelle_SchuelerFHR tab_SchuelerFHR = add(new Tabelle_SchuelerFHR());

	/** Tabelle SchuelerFHRFaecher */
	public static final Tabelle_SchuelerFHRFaecher tab_SchuelerFHRFaecher = add(new Tabelle_SchuelerFHRFaecher());

	/** Tabelle SchuelerFotos */
	public static final Tabelle_SchuelerFotos tab_SchuelerFotos = add(new Tabelle_SchuelerFotos());

	/** Tabelle SchuelerGSDaten */
	public static final Tabelle_SchuelerGSDaten tab_SchuelerGSDaten = add(new Tabelle_SchuelerGSDaten());

	/** Tabelle SchuelerLernabschnittsdaten */
	public static final Tabelle_SchuelerLernabschnittsdaten tab_SchuelerLernabschnittsdaten = add(new Tabelle_SchuelerLernabschnittsdaten());

	/** Tabelle SchuelerLernplattform */
	public static final Tabelle_SchuelerLernplattform tab_SchuelerLernplattform = add(new Tabelle_SchuelerLernplattform());

	/** Tabelle SchuelerListe_Inhalt */
	public static final Tabelle_SchuelerListe_Inhalt tab_SchuelerListe_Inhalt = add(new Tabelle_SchuelerListe_Inhalt());

	/** Tabelle SchuelerMerkmale */
	public static final Tabelle_SchuelerMerkmale tab_SchuelerMerkmale = add(new Tabelle_SchuelerMerkmale());

	/** Tabelle SchuelerSprachenfolge */
	public static final Tabelle_SchuelerSprachenfolge tab_SchuelerSprachenfolge = add(new Tabelle_SchuelerSprachenfolge());

	/** Tabelle SchuelerSprachpruefungen */
	public static final Tabelle_SchuelerSprachpruefungen tab_SchuelerSprachpruefungen = add(new Tabelle_SchuelerSprachpruefungen());

	/** Tabelle SchuelerTelefone */
	public static final Tabelle_SchuelerTelefone tab_SchuelerTelefone = add(new Tabelle_SchuelerTelefone());

	/** Tabelle SchuelerVermerke */
	public static final Tabelle_SchuelerVermerke tab_SchuelerVermerke = add(new Tabelle_SchuelerVermerke());

	/** Tabelle SchuelerZP10 */
	public static final Tabelle_SchuelerZP10 tab_SchuelerZP10 = add(new Tabelle_SchuelerZP10());

	/** Tabelle Schueler_AllgAdr */
	public static final Tabelle_Schueler_AllgAdr tab_Schueler_AllgAdr = add(new Tabelle_Schueler_AllgAdr());

	/** Tabelle Benutzer */
	public static final Tabelle_Benutzer tab_Benutzer = add(new Tabelle_Benutzer());

	/** Tabelle ErzieherDatenschutz */
	public static final Tabelle_ErzieherDatenschutz tab_ErzieherDatenschutz = add(new Tabelle_ErzieherDatenschutz());

	/** Tabelle ErzieherLernplattform */
	public static final Tabelle_ErzieherLernplattform tab_ErzieherLernplattform = add(new Tabelle_ErzieherLernplattform());

	/** Tabelle SchuelerAnkreuzfloskeln */
	public static final Tabelle_SchuelerAnkreuzfloskeln tab_SchuelerAnkreuzfloskeln = add(new Tabelle_SchuelerAnkreuzfloskeln());

	/** Tabelle SchuelerFehlstunden */
	public static final Tabelle_SchuelerFehlstunden tab_SchuelerFehlstunden = add(new Tabelle_SchuelerFehlstunden());

	/** Tabelle SchuelerFoerderempfehlungen */
	public static final Tabelle_SchuelerFoerderempfehlungen tab_SchuelerFoerderempfehlungen = add(new Tabelle_SchuelerFoerderempfehlungen());

	/** Tabelle SchuelerKAoADaten */
	public static final Tabelle_SchuelerKAoADaten tab_SchuelerKAoADaten = add(new Tabelle_SchuelerKAoADaten());

	/** Tabelle SchuelerLD_PSFachBem */
	public static final Tabelle_SchuelerLD_PSFachBem tab_SchuelerLD_PSFachBem = add(new Tabelle_SchuelerLD_PSFachBem());

	/** Tabelle SchuelerLeistungsdaten */
	public static final Tabelle_SchuelerLeistungsdaten tab_SchuelerLeistungsdaten = add(new Tabelle_SchuelerLeistungsdaten());

	/** Tabelle SchuelerZuweisungen */
	public static final Tabelle_SchuelerZuweisungen tab_SchuelerZuweisungen = add(new Tabelle_SchuelerZuweisungen());

	/** Tabelle BenutzerEmail */
	public static final Tabelle_BenutzerEmail tab_BenutzerEmail = add(new Tabelle_BenutzerEmail());

	/** Tabelle BenutzerKompetenzen */
	public static final Tabelle_BenutzerKompetenzen tab_BenutzerKompetenzen = add(new Tabelle_BenutzerKompetenzen());

	/** Tabelle BenutzergruppenMitglieder */
	public static final Tabelle_BenutzergruppenMitglieder tab_BenutzergruppenMitglieder = add(new Tabelle_BenutzergruppenMitglieder());

	/** Tabelle DavRessourceCollections */
	public static final Tabelle_DavRessourceCollections tab_DavRessourceCollections = add(new Tabelle_DavRessourceCollections());

	/** Tabelle DavRessourceCollectionsACL */
	public static final Tabelle_DavRessourceCollectionsACL tab_DavRessourceCollectionACL = add(new Tabelle_DavRessourceCollectionsACL());

	/** Tabelle Logins */
	public static final Tabelle_Logins tab_Logins = add(new Tabelle_Logins());

	/** Tabelle SVWS_Client_Konfiguration_Benutzer */
	public static final Tabelle_SVWS_Client_Konfiguration_Benutzer tab_SVWS_Client_Konfiguration_Benutzer = add(new Tabelle_SVWS_Client_Konfiguration_Benutzer());

	/** Tabelle Schild_Verwaltung */
	public static final Tabelle_Schild_Verwaltung tab_Schild_Verwaltung = add(new Tabelle_Schild_Verwaltung());

	/** Tabelle SchuelerEinzelleistungen */
	public static final Tabelle_SchuelerEinzelleistungen tab_SchuelerEinzelleistungen = add(new Tabelle_SchuelerEinzelleistungen());

	/** Tabelle SchuelerReportvorlagen */
	public static final Tabelle_SchuelerReportvorlagen tab_SchuelerReportvorlagen = add(new Tabelle_SchuelerReportvorlagen());

	/** Tabelle SchuelerWiedervorlage */
	public static final Tabelle_SchuelerWiedervorlage tab_SchuelerWiedervorlage = add(new Tabelle_SchuelerWiedervorlage());

	/** Tabelle LehrerLeitungsfunktion_Keys */
	public static final Tabelle_LehrerLeitungsfunktion_Keys tab_LehrerLeitungsfunktion_Keys = add(new Tabelle_LehrerLeitungsfunktion_Keys());

	/** Tabelle Schulleitung */
	public static final Tabelle_Schulleitung tab_Schulleitung = add(new Tabelle_Schulleitung());

	/** Tabelle Stundenplan */
	public static final Tabelle_Stundenplan tab_Stundenplan = add(new Tabelle_Stundenplan());

	/** Tabelle Stundenplan_Aufsichtsbereiche */
	public static final Tabelle_Stundenplan_Aufsichtsbereiche tab_Stundenplan_Aufsichtsbereiche = add(new Tabelle_Stundenplan_Aufsichtsbereiche());

	/** Tabelle Stundenplan_Pausenzeit */
	public static final Tabelle_Stundenplan_Pausenzeit tab_Stundenplan_Pausenzeit = add(new Tabelle_Stundenplan_Pausenzeit());

	/** Tabelle Stundenplan_Pausenaufsichten */
	public static final Tabelle_Stundenplan_Pausenaufsichten tab_Stundenplan_Pausenaufsichten = add(new Tabelle_Stundenplan_Pausenaufsichten());

	/** Tabelle Stundenplan_PausenaufsichtenBereich */
	public static final Tabelle_Stundenplan_PausenaufsichtenBereich tab_Stundenplan_PausenaufsichtenBereich = add(new Tabelle_Stundenplan_PausenaufsichtenBereich());

	/** Tabelle Stundenplan_Raeume */
	public static final Tabelle_Stundenplan_Raeume tab_Stundenplan_Raeume = add(new Tabelle_Stundenplan_Raeume());

	/** Tabelle Stundenplan_Zeitraster */
	public static final Tabelle_Stundenplan_Zeitraster tab_Stundenplan_Zeitraster = add(new Tabelle_Stundenplan_Zeitraster());

	/** Tabelle Stundenplan_Unterricht */
	public static final Tabelle_Stundenplan_Unterricht tab_Stundenplan_Unterricht = add(new Tabelle_Stundenplan_Unterricht());

	/** Tabelle Stundenplan_UnterrichtKlasse */
	public static final Tabelle_Stundenplan_UnterrichtKlasse tab_Stundenplan_UnterrichtKlasse = add(new Tabelle_Stundenplan_UnterrichtKlasse());

	/** Tabelle Stundenplan_UnterrichtLehrer */
	public static final Tabelle_Stundenplan_UnterrichtLehrer tab_Stundenplan_UnterrichtLehrer = add(new Tabelle_Stundenplan_UnterrichtLehrer());

	/** Tabelle Stundenplan_UnterrichtRaum */
	public static final Tabelle_Stundenplan_UnterrichtRaum tab_Stundenplan_UnterrichtRaum = add(new Tabelle_Stundenplan_UnterrichtRaum());

	/** Tabelle_Stundenplan_Kalenderwochen_Zuordnung */
	public static final Tabelle_Stundenplan_Kalenderwochen_Zuordnung tab_Stundenplan_Kalenderwochen_Zuordnung = add(new Tabelle_Stundenplan_Kalenderwochen_Zuordnung());

	/** Tabelle Stundentafel */
	public static final Tabelle_Stundentafel tab_Stundentafel = add(new Tabelle_Stundentafel());

	/** Tabelle Stundentafel_Faecher */
	public static final Tabelle_Stundentafel_Faecher tab_Stundentafel_Faecher = add(new Tabelle_Stundentafel_Faecher());

	/** Tabelle Floskelgruppen */
	public static final Tabelle_Floskelgruppen tab_Floskelgruppen = add(new Tabelle_Floskelgruppen());

	/** Tabelle Floskeln */
	public static final Tabelle_Floskeln tab_Floskeln = add(new Tabelle_Floskeln());

	/** Tabelle TextExportVorlagen */
	public static final Tabelle_TextExportVorlagen tab_TextExportVorlagen = add(new Tabelle_TextExportVorlagen());

	/** Tabelle Usergroups */
	public static final Tabelle_Usergroups tab_Usergroups = add(new Tabelle_Usergroups());

	/** Tabelle Users */
	public static final Tabelle_Users tab_Users = add(new Tabelle_Users());

	/** Tabelle Versetzung */
	public static final Tabelle_Versetzung tab_Versetzung = add(new Tabelle_Versetzung());

	/** Tabelle DavRessources */
	public static final Tabelle_DavRessources tab_DavRessources = add(new Tabelle_DavRessources());

	/** Tabelle ZuordnungReportvorlagen */
	public static final Tabelle_ZuordnungReportvorlagen tab_ZuordnungReportvorlagen = add(new Tabelle_ZuordnungReportvorlagen());

	/** Tabelle DavSyncTokenSchueler */
	public static final Tabelle_DavSyncTokenSchueler tab_DavSyncTokenSchueler = add(new Tabelle_DavSyncTokenSchueler());

    /** Tabelle DavSyncTokenLehrer */
    public static final Tabelle_DavSyncTokenLehrer tab_DavSyncTokenLehrer = add(new Tabelle_DavSyncTokenLehrer());

    /** Tabelle EnmLeistungsdaten */
    public static final Tabelle_EnmLeistungsdaten tab_EnmLeistungsdaten = add(new Tabelle_EnmLeistungsdaten());

    /** Tabelle EnmLernabschnittsdaten */
    public static final Tabelle_EnmLernabschnittsdaten tab_EnmLernabschnittsdaten = add(new Tabelle_EnmLernabschnittsdaten());

    /** Tabelle Gost_Klausuren_Vorgaben */
    public static final Tabelle_Gost_Klausuren_Vorgaben tab_Gost_Klausuren_Vorgaben = add(new Tabelle_Gost_Klausuren_Vorgaben());

    /** Tabelle Gost_Klausuren_Termine */
    public static final Tabelle_Gost_Klausuren_Termine tab_Gost_Klausuren_Termine = add(new Tabelle_Gost_Klausuren_Termine());

    /** Tabelle Gost_Klausuren_Kursklausuren */
    public static final Tabelle_Gost_Klausuren_Kursklausuren tab_Gost_Klausuren_Kursklausuren = add(new Tabelle_Gost_Klausuren_Kursklausuren());

    /** Tabelle Gost_Klausuren_Schuelerklausuren */
    public static final Tabelle_Gost_Klausuren_Schuelerklausuren tab_Gost_Klausuren_Schuelerklausuren = add(new Tabelle_Gost_Klausuren_Schuelerklausuren());

    /** Tabelle Gost_Klausuren_NtaZeiten */
    public static final Tabelle_Gost_Klausuren_NtaZeiten tab_Tabelle_Gost_Klausuren_NtaZeiten = add(new Tabelle_Gost_Klausuren_NtaZeiten());

    /** Tabelle Gost_Klausuren_Raeume */
    public static final Tabelle_Gost_Klausuren_Raeume tab_Gost_Klausuren_Raeume = add(new Tabelle_Gost_Klausuren_Raeume());

    /** Tabelle Gost_Klausuren_Raeume_Stunden */
    public static final Tabelle_Gost_Klausuren_Raeume_Stunden tab_Gost_Klausuren_Raeume_Stunden = add(new Tabelle_Gost_Klausuren_Raeume_Stunden());

    /** Tabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden */
    public static final Tabelle_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden tab_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden = add(new Tabelle_Gost_Klausuren_Schuelerklausuren_Raeume_Stunden());

    /** Tabelle Gost_Klausuren_Aufsichten */
    public static final Tabelle_Gost_Klausuren_Raeume_Stunden_Aufsichten tab_Gost_Klausuren_Raeume_Stunden_Aufsichten = add(new Tabelle_Gost_Klausuren_Raeume_Stunden_Aufsichten());

//    /** Tabelle Gost_Klausuren_Entfall */
//    public static final Tabelle_Gost_Klausuren_Entfall tab_Gost_Klausuren_Entfall = add(new Tabelle_Gost_Klausuren_Entfall());

    /** Tabelle Gost_Klausuren_Kalenderinformationen */
    public static final Tabelle_Gost_Klausuren_Kalenderinformationen tab_Gost_Klausuren_Kalenderinformationen = add(new Tabelle_Gost_Klausuren_Kalenderinformationen());


    /**
     * Liefert die SQL-Befehle zum Anlegen von Default-SVWS-Benutzern
     * bei einem leeren Schema in Abhängigkeit von der übergebenen Revision.
     *
     * @param rev    die Revision
     *
     * @return eine Liste mit den SQL-Befehlen
     */
    public static final List<String> getCreateBenutzerSQL(final long rev) {
    	final Vector<String> result = new Vector<>();
    	if (rev == 0) {
    		result.add("INSERT INTO Users(ID,US_Name,US_LoginName,US_UserGroups,US_Privileges) VALUES "
    			 	 + "(1,'Administrator','Admin','1;2;3','$');");
    		result.add("INSERT INTO Usergroups(UG_ID, UG_Bezeichnung, UG_Kompetenzen, UG_Nr) VALUES "
    				 + "(1, 'Administrator', '$', 1),"
    				 + "(2, 'Lehrer', '11;21;22;81;31;61', 3),"
    				 + "(3, 'Sekretariat', '11;12;13;14;21;81;82;83;31;32;33;34;91;92;93;94;95;61;62;71', 4);");
    		return result;
    	}
    	result.add("INSERT INTO Credentials(ID, Benutzername, BenutzernamePseudonym, Initialkennwort, PasswordHash, RSAPublicKey, RSAPrivateKey, AES) VALUES "
    			 + "(1, 'Admin', NULL, NULL, NULL, NULL, NULL, NULL);");
		result.add("INSERT INTO BenutzerAllgemein(ID,AnzeigeName,CredentialID) VALUES "
				 + "(1,'Administrator',1);");
		result.add("INSERT INTO Benutzer(ID, Typ, Allgemein_ID, Lehrer_ID, Schueler_ID, Erzieher_ID, IstAdmin) VALUES "
				 + "(1, 0, 1, NULL, NULL, NULL, 1);");
		result.add("INSERT INTO Benutzergruppen(ID, Bezeichnung, IstAdmin) VALUES "
				 + "(1, 'Administrator', 1),"
				 + "(2, 'Lehrer', 0),"
				 + "(3, 'Sekretariat', 0);");
		result.add("INSERT INTO BenutzergruppenMitglieder(Gruppe_ID, Benutzer_ID) VALUES (1, 1);");
		result.add("INSERT INTO BenutzergruppenKompetenzen(Gruppe_ID, Kompetenz_ID) VALUES "
				 + "(2, 11), (2, 21), (2, 22), (2, 31), (2, 61), (2, 81),"
				 + "(3, 11),(3, 12),(3, 13),(3, 14),(3, 21),(3, 31),(3, 32),(3, 33),(3, 34),(3, 61),(3, 62),(3, 71),(3, 81),(3, 82),(3, 83),(3, 91),(3, 92),(3, 93),(3, 94),(3, 95);");
    	return result;
    }


    /**
     * Liefert alle Tabellen, welche in der angegebenen Revision definiert sind.
     *
     * @param rev   die SVWS-DB-Revision
     *
     * @return eine Liste mit den definierten Tabellen
     */
    public static final List<SchemaTabelle> getTabellen(final long rev) {
    	return tabellen.values().stream()
    			.filter(t -> ((rev == -1) && (t.veraltet().revision == -1))
    					|| ((rev != -1) && (rev >= t.revision().revision) && ((t.veraltet().revision == -1) || (rev < t.veraltet().revision))))
    			.collect(Collectors.toList());
    }

}
