import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { BenutzerKompetenzKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import type { List } from '../../../java/util/List';
import { BenutzerKompetenzGruppe } from '../../../core/types/benutzer/BenutzerKompetenzGruppe';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';

export class BenutzerKompetenz extends JavaObject implements JavaEnum<BenutzerKompetenz> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BenutzerKompetenz> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BenutzerKompetenz> = new Map<string, BenutzerKompetenz>();

	/**
	 * Es werden keinerlei Kompetenzen benötigt.
	 */
	public static readonly KEINE : BenutzerKompetenz = new BenutzerKompetenz("KEINE", 0, new BenutzerKompetenzKatalogEintrag(-2, BenutzerKompetenzGruppe.KEINE, "keine", null));

	/**
	 * Es werden Admin-Rechte benötigt.
	 */
	public static readonly ADMIN : BenutzerKompetenz = new BenutzerKompetenz("ADMIN", 1, new BenutzerKompetenzKatalogEintrag(-1, BenutzerKompetenzGruppe.ADMIN, "admin", null));

	/**
	 * Es werden Rechte zum Ansehen der Schüler Individualdaten benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_ANSEHEN", 2, new BenutzerKompetenzKatalogEintrag(11, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ansehen", null));

	/**
	 * Es werden Rechte zum Ändern der Schüler Individualdaten benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_AENDERN", 3, new BenutzerKompetenzKatalogEintrag(12, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Ändern", null));

	/**
	 * Es werden Rechte zum Löschen der Schüler Individualdaten benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_LOESCHEN", 4, new BenutzerKompetenzKatalogEintrag(13, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Löschen", null));

	/**
	 * Es werden Rechte zum Ändern der Schüler Vermerke benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN", 5, new BenutzerKompetenzKatalogEintrag(14, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Vermerke ändern", null));

	/**
	 * Es werden Rechte zum Ändern der Schüler KAoA-Daten benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN", 6, new BenutzerKompetenzKatalogEintrag(15, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "KAoA-Daten ändern", null));

	/**
	 * Es werden Rechte zum Ändern der Einwilligungen zu einem Schüler benötigt.
	 */
	public static readonly SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN", 7, new BenutzerKompetenzKatalogEintrag(16, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Einwilligungen ändern (DSGVO, Lernplattformen)", null));

	/**
	 * Es werden Rechte zum Ansehen der Schüler Leistungsdaten benötigt.
	 */
	public static readonly SCHUELER_LEISTUNGSDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_ANSEHEN", 8, new BenutzerKompetenzKatalogEintrag(21, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Ansehen", null));

	/**
	 * Es werden Rechte zum funktionsbezogenen Ändern der Schüler Leistungsdaten benötigt.
	 */
	public static readonly SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN", 9, new BenutzerKompetenzKatalogEintrag(22, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Funktionsbezogen ändern", null));

	/**
	 * Es werden Rechte zum generellen Ändern der Schüler Leistungsdaten benötigt.
	 */
	public static readonly SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN", 10, new BenutzerKompetenzKatalogEintrag(23, BenutzerKompetenzGruppe.SCHUELER_LEISTUNGSDATEN, "Alle ändern", null));

	/**
	 * Es werden Rechte zum Drucken der Berichte benötigt.
	 */
	public static readonly BERICHTE_ALLE_FORMULARE_DRUCKEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_ALLE_FORMULARE_DRUCKEN", 11, new BenutzerKompetenzKatalogEintrag(31, BenutzerKompetenzGruppe.BERICHTE, "Alle Formulare drucken", null));

	/**
	 * Es werden Rechte zum Drucken der Standardberichte benötigt.
	 */
	public static readonly BERICHTE_STANDARDFORMULARE_DRUCKEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_STANDARDFORMULARE_DRUCKEN", 12, new BenutzerKompetenzKatalogEintrag(32, BenutzerKompetenzGruppe.BERICHTE, "Standard-Formulare drucken", null));

	/**
	 * Es werden Rechte zum Ändern der Berichte benötigt.
	 */
	public static readonly BERICHTE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_AENDERN", 13, new BenutzerKompetenzKatalogEintrag(33, BenutzerKompetenzGruppe.BERICHTE, "Ändern", null));

	/**
	 * Es werden Rechte zum Löschen der Berichte benötigt.
	 */
	public static readonly BERICHTE_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("BERICHTE_LOESCHEN", 14, new BenutzerKompetenzKatalogEintrag(34, BenutzerKompetenzGruppe.BERICHTE, "Löschen", null));

	/**
	 * Es werden Rechte zum Importieren von Daten benötigt.
	 */
	public static readonly IMPORT_EXPORT_DATEN_IMPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_DATEN_IMPORTIEREN", 15, new BenutzerKompetenzKatalogEintrag(41, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Daten importieren", null));

	/**
	 * Es werden Rechte zum Exportieren von Schülerdaten benötigt.
	 */
	public static readonly IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN", 16, new BenutzerKompetenzKatalogEintrag(42, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schülerdaten exportieren", null));

	/**
	 * Es werden Rechte zum Exportieren von Lehrerdaten benötigt.
	 */
	public static readonly IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_LEHRERDATEN_EXPORTIEREN", 17, new BenutzerKompetenzKatalogEintrag(43, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Lehrerdaten exportieren", null));

	/**
	 * Es werden Rechte zum Nutzen der SchILD-NRW-Schnittstelle benötigt.
	 */
	public static readonly IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_SCHNITTSTELLE_SCHILD_NRW", 18, new BenutzerKompetenzKatalogEintrag(44, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Schnittstelle SchILD-NRW verwenden", null));

	/**
	 * Es werden Rechte zum Ausführen des Access-DB-Exports benötigt.
	 */
	public static readonly IMPORT_EXPORT_ACCESS_DB : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_ACCESS_DB", 19, new BenutzerKompetenzKatalogEintrag(45, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Access-DB-Export durchführen", null));

	/**
	 * Es werden Rechte zum Export über die XML-Schnittstellen benötigt.
	 */
	public static readonly IMPORT_EXPORT_XML : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_XML", 20, new BenutzerKompetenzKatalogEintrag(46, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Export über XML-Schnittstellen", null));

	/**
	 * Es werden Rechte zum Import und Export über die Schulbewerbung.de-Schnittstelle benötigt.
	 */
	public static readonly IMPORT_EXPORT_SCHULBEWERBUNG_DE : BenutzerKompetenz = new BenutzerKompetenz("IMPORT_EXPORT_SCHULBEWERBUNG_DE", 21, new BenutzerKompetenzKatalogEintrag(47, BenutzerKompetenzGruppe.IMPORT_EXPORT, "Datenaustausch mit Schulbewerbung.de", null));

	/**
	 * Es werden Rechte zum Ansehen der Schulbezogenen Daten benötigt.
	 */
	public static readonly SCHULBEZOGENE_DATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULBEZOGENE_DATEN_ANSEHEN", 22, new BenutzerKompetenzKatalogEintrag(61, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ansehen", null));

	/**
	 * Es werden Rechte zum Ändern der Schulbezogenen Daten benötigt.
	 */
	public static readonly SCHULBEZOGENE_DATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHULBEZOGENE_DATEN_AENDERN", 23, new BenutzerKompetenzKatalogEintrag(62, BenutzerKompetenzGruppe.SCHULBEZOGENE_DATEN, "Ändern", null));

	/**
	 * Es werden Rechte benötigt um ein Backup durchzuführen.
	 */
	public static readonly EXTRAS_BACKUP_DURCHFUEHREN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_BACKUP_DURCHFUEHREN", 24, new BenutzerKompetenzKatalogEintrag(71, BenutzerKompetenzGruppe.EXTRAS, "Backup durchführen", null));

	/**
	 * Es werden Rechte zum Wiederherstellen von gelöschten Schülerdaten benötigt.
	 */
	public static readonly EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_GELOESCHTE_DATEN_ZURUECKHOLEN", 25, new BenutzerKompetenzKatalogEintrag(72, BenutzerKompetenzGruppe.EXTRAS, "Gelöschte Daten zurückholen", null));

	/**
	 * Es werden Rechte zum Ändern der Farben für Fachgruppen benötigt.
	 */
	public static readonly EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_FARBEN_FUER_FACHGRUPPEN_AENDERN", 26, new BenutzerKompetenzKatalogEintrag(73, BenutzerKompetenzGruppe.EXTRAS, "Farben für Fachgruppen ändern", null));

	/**
	 * Es werden Rechte Import von Daten aus Kurs42 benötigt.
	 */
	public static readonly EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN", 27, new BenutzerKompetenzKatalogEintrag(74, BenutzerKompetenzGruppe.EXTRAS, "Daten aus Kurs42 importieren", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zum Bearbeiten von Personengruppen benötigt.
	 */
	public static readonly EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_DATEN_PERSONENGRUPPEN_BEARBEITEN", 28, new BenutzerKompetenzKatalogEintrag(75, BenutzerKompetenzGruppe.EXTRAS, "Personengruppen bearbeiten", null));

	/**
	 * Es werden Rechte zum Ansehen von Katalogen benötigt.
	 */
	public static readonly KATALOG_EINTRAEGE_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_ANSEHEN", 29, new BenutzerKompetenzKatalogEintrag(81, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ansehen", null));

	/**
	 * Es werden Rechte zum Ändern von Katalogen benötigt.
	 */
	public static readonly KATALOG_EINTRAEGE_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_AENDERN", 30, new BenutzerKompetenzKatalogEintrag(82, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Ändern", null));

	/**
	 * Es werden Rechte zum Löschen von Katalogen benötigt.
	 */
	public static readonly KATALOG_EINTRAEGE_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("KATALOG_EINTRAEGE_LOESCHEN", 31, new BenutzerKompetenzKatalogEintrag(83, BenutzerKompetenzGruppe.KATALOG_EINTRAEGE, "Löschen", null));

	/**
	 * Es werden Rechte zum Ansehen von Lehrerdaten benötigt.
	 */
	public static readonly LEHRERDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_ANSEHEN", 32, new BenutzerKompetenzKatalogEintrag(91, BenutzerKompetenzGruppe.LEHRERDATEN, "Ansehen", null));

	/**
	 * Es werden Rechte zum Ändern von Lehrerdaten benötigt.
	 */
	public static readonly LEHRERDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_AENDERN", 33, new BenutzerKompetenzKatalogEintrag(92, BenutzerKompetenzGruppe.LEHRERDATEN, "Ändern", null));

	/**
	 * Es werden Rechte zum Löschen von Lehrerdaten benötigt.
	 */
	public static readonly LEHRERDATEN_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_LOESCHEN", 34, new BenutzerKompetenzKatalogEintrag(93, BenutzerKompetenzGruppe.LEHRERDATEN, "Löschen", null));

	/**
	 * Es werden Rechte zum Ansehen von Lehrerdetaildaten benötigt.
	 */
	public static readonly LEHRERDATEN_DETAILDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_DETAILDATEN_ANSEHEN", 35, new BenutzerKompetenzKatalogEintrag(94, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ansehen", null));

	/**
	 * Es werden Rechte zum Ändern von Lehrerdetaildaten benötigt.
	 */
	public static readonly LEHRERDATEN_DETAILDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("LEHRERDATEN_DETAILDATEN_AENDERN", 36, new BenutzerKompetenzKatalogEintrag(95, BenutzerKompetenzGruppe.LEHRERDATEN, "Detaildaten ändern", null));

	/**
	 * Es werden Rechte zum Ansehen von Daten des Verfahrens Schulpflichtverletzung benötigt.
	 */
	public static readonly SCHULPFLICHTVERLETZUNG_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_ANSEHEN", 37, new BenutzerKompetenzKatalogEintrag(101, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ansehen", null));

	/**
	 * Es werden Rechte zum Ändern von Daten des Verfahrens Schulpflichtverletzung benötigt.
	 */
	public static readonly SCHULPFLICHTVERLETZUNG_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_AENDERN", 38, new BenutzerKompetenzKatalogEintrag(102, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Ändern", null));

	/**
	 * Es werden Rechte zum Löschen von Daten des Verfahrens Schulpflichtverletzung benötigt.
	 */
	public static readonly SCHULPFLICHTVERLETZUNG_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHULPFLICHTVERLETZUNG_LOESCHEN", 39, new BenutzerKompetenzKatalogEintrag(103, BenutzerKompetenzGruppe.SCHULPFLICHTVERLETZUNG, "Löschen", null));

	/**
	 * Es werden Rechte zum Ansehen von Stundenplänen (allgemein) benötigt.
	 */
	public static readonly STUNDENPLAN_ALLGEMEIN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_ALLGEMEIN_ANSEHEN", 40, new BenutzerKompetenzKatalogEintrag(111, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (allgemein)", null));

	/**
	 * Es werden Rechte zum Ansehen von Stundenplänen (funktionsbezogen) benötigt.
	 */
	public static readonly STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN", 41, new BenutzerKompetenzKatalogEintrag(112, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundneplan ansehen (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zum Importieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_IMPORT", 42, new BenutzerKompetenzKatalogEintrag(113, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne importieren", null));

	/**
	 * Es werden Rechte zum Exportieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_EXPORT", 43, new BenutzerKompetenzKatalogEintrag(114, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne exportieren", null));

	/**
	 * Es werden Rechte zum Erstellen von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_ERSTELLEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_ERSTELLEN", 44, new BenutzerKompetenzKatalogEintrag(115, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne erstellen", null));

	/**
	 * Es werden Rechte zum Aktivieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_AKTIVIEREN", 45, new BenutzerKompetenzKatalogEintrag(116, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne aktivieren", null));

	/**
	 * Es werden Rechte zur Administration des Notenmoduls benötigt.
	 */
	public static readonly NOTENMODUL_ADMINISTRATION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_ADMINISTRATION", 46, new BenutzerKompetenzKatalogEintrag(131, BenutzerKompetenzGruppe.NOTENMODUL, "Administration Notenmodul", null));

	/**
	 * Es werden Rechte zur Änderung von Noten im Notenmodul (allgemein) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN", 47, new BenutzerKompetenzKatalogEintrag(132, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (allgemein)", null));

	/**
	 * Es werden Rechte zur Änderung von Noten im Notenmodul (funktionsbezogen) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_AENDERN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_FUNKTION", 48, new BenutzerKompetenzKatalogEintrag(133, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zum Ansehen von Noten im Notenmodul (allgemein) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN", 49, new BenutzerKompetenzKatalogEintrag(134, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (allgemein)", null));

	/**
	 * Es werden Rechte zum Ansehen von Noten im Notenmodul (funktionsbezogen) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_FUNKTION", 50, new BenutzerKompetenzKatalogEintrag(135, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zur Administration der Datenbank (Schema erstellen/migrieren) benötigt.
	 */
	public static readonly DATENBANK_SCHEMA_ERSTELLEN : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SCHEMA_ERSTELLEN", 51, new BenutzerKompetenzKatalogEintrag(141, BenutzerKompetenzGruppe.DATENBANK, "Schema erstellen und migrieren", null));

	/**
	 * Es werden Rechte zum Import von SQLite-Backups benötigt.
	 */
	public static readonly DATENBANK_SQLITE_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_IMPORT", 52, new BenutzerKompetenzKatalogEintrag(142, BenutzerKompetenzGruppe.DATENBANK, "SQLite importieren (Backup einspielen)", null));

	/**
	 * Es werden Rechte zum Export von SQLite-Backups benötigt.
	 */
	public static readonly DATENBANK_SQLITE_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_EXPORT", 53, new BenutzerKompetenzKatalogEintrag(143, BenutzerKompetenzGruppe.DATENBANK, "SQLite exportieren (Backup erstellen)", null));

	/**
	 * Es werden Rechte zur Durchführung der Laufbahnplanung (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN", 54, new BenutzerKompetenzKatalogEintrag(161, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zur Durchführung der Laufbahnplanung (stufenbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN", 55, new BenutzerKompetenzKatalogEintrag(162, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zum Import von Laufbahnpdaten aus LuPO benötigt.
	 */
	public static readonly OBERSTUFE_LUPO_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LUPO_IMPORT", 56, new BenutzerKompetenzKatalogEintrag(163, BenutzerKompetenzGruppe.OBERSTUFE_LAUFBAHNPLANUNG, "Laufbahnplanung aus LuPO importieren", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zur Kursverwaltung - Blocken (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_ALLGEMEIN", 57, new BenutzerKompetenzKatalogEintrag(171, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blocken (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zur Kursverwaltung - Blocken (stufenbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN", 58, new BenutzerKompetenzKatalogEintrag(172, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blocken (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zur Aktivierung einer Blockung benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN", 59, new BenutzerKompetenzKatalogEintrag(173, BenutzerKompetenzGruppe.OBERSTUFE_KURSPLANUNG, "Kursverwaltung - Blockung aktivieren", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zur Bearbeitung einer Klausurplanung benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_AENDERN", 60, new BenutzerKompetenzKatalogEintrag(181, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ändern", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zum Ansehen einer Klausurplanung (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN", 61, new BenutzerKompetenzKatalogEintrag(182, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ansehen (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zum Ansehen einer Klausurplanung (funktionsbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION", 62, new BenutzerKompetenzKatalogEintrag(183, BenutzerKompetenzGruppe.OBERSTUFE_KLAUSURPLANUNG, "Klausurplanung ansehen (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Ansehen von Abiturdaten der Oberstufe (allgemein).
	 */
	public static readonly ABITUR_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_ALLGEMEIN", 63, new BenutzerKompetenzKatalogEintrag(191, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ansehen (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Ansehen von Abiturdaten der Oberstufe (funktionsbezogen).
	 */
	public static readonly ABITUR_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_FUNKTIONSBEZOGEN", 64, new BenutzerKompetenzKatalogEintrag(192, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ansehen (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Ändern aller Daten zum Abitur (allgemein).
	 */
	public static readonly ABITUR_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_ALLGEMEIN", 65, new BenutzerKompetenzKatalogEintrag(193, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ändern (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Ändern aller Daten zum Abitur (funktionsbezogen).
	 */
	public static readonly ABITUR_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_FUNKTIONSBEZOGEN", 66, new BenutzerKompetenzKatalogEintrag(194, BenutzerKompetenzGruppe.ABITUR, "Abiturdaten ändern (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN", 67, new BenutzerKompetenzKatalogEintrag(195, BenutzerKompetenzGruppe.ABITUR, "Prüfungsergebnisse eingeben (allgemein)", Schulform.getMitGymOb()));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 68, new BenutzerKompetenzKatalogEintrag(196, BenutzerKompetenzGruppe.ABITUR, "Prüfungsergebnisse eingeben (funktionsbezogen)", Schulform.getMitGymOb()));

	/**
	 * Es werden Rechte zum Ansehen der Adressdaten eines von Erziehungsberechtigten benötigt
	 */
	public static readonly ADRESSDATEN_ERZIEHER_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("ADRESSDATEN_ERZIEHER_ANSEHEN", 69, new BenutzerKompetenzKatalogEintrag(201, BenutzerKompetenzGruppe.CARDDAV, "Ansehen", null));

	/**
	 * Allgemeine Berechtigung für das Einsehen von Adressdaten über die CardDav API
	 */
	public static readonly ADRESSDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("ADRESSDATEN_ANSEHEN", 70, new BenutzerKompetenzKatalogEintrag(202, BenutzerKompetenzGruppe.CARDDAV, "Ansehen", null));

	/**
	 * Allgemeine Berechtigung für den Zugriff auf die CalDav API
	 */
	public static readonly KALENDER_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KALENDER_ANSEHEN", 71, new BenutzerKompetenzKatalogEintrag(301, BenutzerKompetenzGruppe.CALDAV, "Ansehen", null));

	/**
	 * Berechtigung für den Besitz und das Bearbeiten eines eigenen Kalenders über die CalDav API.
	 */
	public static readonly EIGENEN_KALENDER_BEARBEITEN : BenutzerKompetenz = new BenutzerKompetenz("EIGENEN_KALENDER_BEARBEITEN", 72, new BenutzerKompetenzKatalogEintrag(302, BenutzerKompetenzGruppe.CALDAV, "Bearbeiten", null));

	/**
	 * Allgemeine Berechtigung zum Ansehen generierter Kalender abhängig von der Funktion des Nutzers
	 */
	public static readonly KALENDER_FUNKTIONSBEZOGEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("KALENDER_FUNKTIONSBEZOGEN_ANSEHEN", 73, new BenutzerKompetenzKatalogEintrag(303, BenutzerKompetenzGruppe.CALDAV, "Ansehen", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN", 74, new BenutzerKompetenzKatalogEintrag(401, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (allgemein)", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN", 75, new BenutzerKompetenzKatalogEintrag(402, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN", 76, new BenutzerKompetenzKatalogEintrag(403, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (allgemein)", null));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN", 77, new BenutzerKompetenzKatalogEintrag(404, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN", 78, new BenutzerKompetenzKatalogEintrag(405, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (allgemein)", null));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 79, new BenutzerKompetenzKatalogEintrag(406, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN", 80, new BenutzerKompetenzKatalogEintrag(501, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN", 81, new BenutzerKompetenzKatalogEintrag(502, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_ALLGEMEIN", 82, new BenutzerKompetenzKatalogEintrag(503, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN", 83, new BenutzerKompetenzKatalogEintrag(504, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN", 84, new BenutzerKompetenzKatalogEintrag(505, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 85, new BenutzerKompetenzKatalogEintrag(506, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Die Daten der Benutzerkompetenz
	 */
	public readonly daten : BenutzerKompetenzKatalogEintrag;

	/**
	 * Eine HashMap zum schnellen Zugriff auf ein Aufzählungsobjekt anhand der ID der Benutzerkompetenz
	 */
	private static readonly _mapID : HashMap<number, BenutzerKompetenz> = new HashMap();

	/**
	 * Eine ArrayMap zum schnellen Zugriff auf die Benutzer-Kompetenzen anhand der Benutzer-Kompetenz-Gruppe
	 */
	private static readonly _mapGruppenZuordnung : ArrayMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> = new ArrayMap(BenutzerKompetenzGruppe.values());

	/**
	 * Erzeugt eine neue Benutzerkompetenz für die Aufzählung.
	 *
	 * @param daten   die Daten der Benutzerkompetenz
	 */
	private constructor(name : string, ordinal : number, daten : BenutzerKompetenzKatalogEintrag) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BenutzerKompetenz.all_values_by_ordinal.push(this);
		BenutzerKompetenz.all_values_by_name.set(name, this);
		this.daten = daten;
	}

	/**
	 * Gibt eine Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Benutzerkompetenzen auf die zugehörigen Benutzerkompetenzen
	 */
	private static getMapID() : HashMap<number, BenutzerKompetenz> {
		if (BenutzerKompetenz._mapID.size() === 0)
			for (const p of BenutzerKompetenz.values())
				BenutzerKompetenz._mapID.put(p.daten.id, p);
		return BenutzerKompetenz._mapID;
	}

	/**
	 * Gibt eine Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Benutzerkompetenzen-Gruppen auf die zugehörigen Benutzerkompetenzen
	 */
	private static getMapGruppenZuordnung() : JavaMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> {
		if (BenutzerKompetenz._mapGruppenZuordnung.size() === 0) {
			for (const g of BenutzerKompetenzGruppe.values())
				BenutzerKompetenz._mapGruppenZuordnung.put(g, new ArrayList());
			for (const p of BenutzerKompetenz.values()) {
				let gruppe : BenutzerKompetenzGruppe | null = BenutzerKompetenzGruppe.getByID(p.daten.gruppe_id);
				if (gruppe === null)
					gruppe = BenutzerKompetenzGruppe.KEINE;
				const liste : List<BenutzerKompetenz> | null = BenutzerKompetenz._mapGruppenZuordnung.get(gruppe);
				if (liste !== null)
					liste.add(p);
			}
		}
		return BenutzerKompetenz._mapGruppenZuordnung;
	}

	/**
	 * Gibt die Benutzerkompetenz anhand der übergebenen ID zurück.
	 *
	 * @param id    die ID der Benutzerkompetenz
	 *
	 * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : BenutzerKompetenz | null {
		return BenutzerKompetenz.getMapID().get(id);
	}

	/**
	 *  Überprüft, ob die Kompetenz mit k_id für die Schulform mit s_id zulässig ist.
	 * @param schulform  Die Schulform
	 * @return true, wenn die Kompetenz für die Schulform zulässig ist.
	 */
	public hatSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		if (this.daten.nurSchulformen !== null) {
			return this.daten.nurSchulformen.contains(schulform.daten.id);
		}
		return true;
	}

	/**
	 * Gibt die Liste aller Benutzerkompetenzen zurück, welche der übergebenen Gruppe
	 * zugeordnet sind.
	 *
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 *
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static getKompetenzen(gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> {
		const liste : List<BenutzerKompetenz> | null = BenutzerKompetenz.getMapGruppenZuordnung().get(gruppe);
		if (liste === null)
			return new ArrayList();
		return liste;
	}

	/**
	 * Gibt die Liste aller Benutzerkompetenzen zurück, welche der übergebenen Gruppe
	 * zugeordnet sind und die übergebene Schulform besitzen.
	 *
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 * @param schulform   die Schulform
	 *
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static getKompetenzenMitSchulform(gruppe : BenutzerKompetenzGruppe, schulform : Schulform) : List<BenutzerKompetenz> {
		const l : List<BenutzerKompetenz> | null = new ArrayList();
		const liste : List<BenutzerKompetenz> | null = BenutzerKompetenz.getMapGruppenZuordnung().get(gruppe);
		if (liste === null)
			return l;
		for (const bk of liste) {
			if (bk.hatSchulform(schulform))
				l.add(bk);
		}
		return l;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof BenutzerKompetenz))
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
	public compareTo(other : BenutzerKompetenz) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BenutzerKompetenz> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BenutzerKompetenz | null {
		const tmp : BenutzerKompetenz | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.benutzer.BenutzerKompetenz', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_benutzer_BenutzerKompetenz(obj : unknown) : BenutzerKompetenz {
	return obj as BenutzerKompetenz;
}
