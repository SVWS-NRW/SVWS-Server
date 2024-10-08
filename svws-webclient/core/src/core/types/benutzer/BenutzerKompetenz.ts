import { JavaEnum } from '../../../java/lang/JavaEnum';
import { BenutzerKompetenzKatalogEintrag } from '../../../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { BenutzerKompetenzGruppe } from '../../../core/types/benutzer/BenutzerKompetenzGruppe';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';

export class BenutzerKompetenz extends JavaEnum<BenutzerKompetenz> {

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
	 * Es werden Rechte zum endgültigen Löschen eines Schülers benötigt.
	 */
	public static readonly SCHUELER_LOESCHEN : BenutzerKompetenz = new BenutzerKompetenz("SCHUELER_LOESCHEN", 4, new BenutzerKompetenzKatalogEintrag(13, BenutzerKompetenzGruppe.SCHUELER_INDIVIDUALDATEN, "Löschen", null));

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
	public static readonly EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN : BenutzerKompetenz = new BenutzerKompetenz("EXTRAS_DATEN_AUS_KURS42_IMPORTIEREN", 27, new BenutzerKompetenzKatalogEintrag(74, BenutzerKompetenzGruppe.EXTRAS, "Daten aus Kurs42 importieren", BenutzerKompetenz.getSchulformenGymOb()));

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
	public static readonly LEHRER_PERSONALDATEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("LEHRER_PERSONALDATEN_ANSEHEN", 35, new BenutzerKompetenzKatalogEintrag(94, BenutzerKompetenzGruppe.LEHRERDATEN, "Personaldaten ansehen", null));

	/**
	 * Es werden Rechte zum Ändern von Lehrerdetaildaten benötigt.
	 */
	public static readonly LEHRER_PERSONALDATEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("LEHRER_PERSONALDATEN_AENDERN", 36, new BenutzerKompetenzKatalogEintrag(95, BenutzerKompetenzGruppe.LEHRERDATEN, "Personaldaten ändern", null));

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
	 * Es werden Rechte zum Ansehen der Unterrichtsverteilung bis zum aktuellen Schuljahresabschnitt benötigt.
	 */
	public static readonly UNTERRICHTSVERTEILUNG_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("UNTERRICHTSVERTEILUNG_ANSEHEN", 40, new BenutzerKompetenzKatalogEintrag(105, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ansehen", null));

	/**
	 * Es werden Rechte zum Ansehen der Unterrichtsverteilung auch nach dem aktuellen Schuljahresabschnitt benötigt.
	 */
	public static readonly UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN", 41, new BenutzerKompetenzKatalogEintrag(106, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Planung der Unterrichtsverteilung ansehen", null));

	/**
	 * Es werden Rechte zum Ändern der Unterrichtsverteilung (allgemein) benötigt.
	 */
	public static readonly UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN", 42, new BenutzerKompetenzKatalogEintrag(107, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ändern (allgemein)", null));

	/**
	 * Es werden Rechte zum Ändern der Unterrichtsverteilung (funktionsbezogen) benötigt.
	 */
	public static readonly UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN", 43, new BenutzerKompetenzKatalogEintrag(108, BenutzerKompetenzGruppe.UNTERRICHTSVERTEILUNG, "Unterrichtsverteilung ändern (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zum Ansehen von Stundenplänen (allgemein) benötigt.
	 */
	public static readonly STUNDENPLAN_ALLGEMEIN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_ALLGEMEIN_ANSEHEN", 44, new BenutzerKompetenzKatalogEintrag(111, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (allgemein)", null));

	/**
	 * Es werden Rechte zum Ansehen von Stundenplänen (funktionsbezogen) benötigt.
	 */
	public static readonly STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN", 45, new BenutzerKompetenzKatalogEintrag(112, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenplan ansehen (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zum Importieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_IMPORT", 46, new BenutzerKompetenzKatalogEintrag(113, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne importieren", null));

	/**
	 * Es werden Rechte zum Exportieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_EXPORT", 47, new BenutzerKompetenzKatalogEintrag(114, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne exportieren", null));

	/**
	 * Es werden Rechte zum Erstellen von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_AENDERN", 48, new BenutzerKompetenzKatalogEintrag(115, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne ändern", null));

	/**
	 * Es werden Rechte zum Aktivieren von Stundenplänen benötigt.
	 */
	public static readonly STUNDENPLAN_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("STUNDENPLAN_AKTIVIEREN", 49, new BenutzerKompetenzKatalogEintrag(116, BenutzerKompetenzGruppe.STUNDENPLANUNG, "Stundenpläne aktivieren", null));

	/**
	 * Es werden Rechte zur Administration des Notenmoduls benötigt.
	 */
	public static readonly NOTENMODUL_ADMINISTRATION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_ADMINISTRATION", 50, new BenutzerKompetenzKatalogEintrag(131, BenutzerKompetenzGruppe.NOTENMODUL, "Administration Notenmodul", null));

	/**
	 * Es werden Rechte zur Änderung von Noten im Notenmodul (allgemein) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN", 51, new BenutzerKompetenzKatalogEintrag(132, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (allgemein)", null));

	/**
	 * Es werden Rechte zur Änderung von Noten im Notenmodul (funktionsbezogen) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_AENDERN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_AENDERN_FUNKTION", 52, new BenutzerKompetenzKatalogEintrag(133, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ändern (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zum Ansehen von Noten im Notenmodul (allgemein) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN", 53, new BenutzerKompetenzKatalogEintrag(134, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (allgemein)", null));

	/**
	 * Es werden Rechte zum Ansehen von Noten im Notenmodul (funktionsbezogen) benötigt.
	 */
	public static readonly NOTENMODUL_NOTEN_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("NOTENMODUL_NOTEN_ANSEHEN_FUNKTION", 54, new BenutzerKompetenzKatalogEintrag(135, BenutzerKompetenzGruppe.NOTENMODUL, "Noten ansehen (funktionsbezogen)", null));

	/**
	 * Es werden Rechte zur Administration der Datenbank (Schema erstellen/migrieren) benötigt.
	 */
	public static readonly DATENBANK_SCHEMA_ERSTELLEN : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SCHEMA_ERSTELLEN", 55, new BenutzerKompetenzKatalogEintrag(141, BenutzerKompetenzGruppe.DATENBANK, "Schema erstellen und migrieren", null));

	/**
	 * Es werden Rechte zum Import von SQLite-Backups benötigt.
	 */
	public static readonly DATENBANK_SQLITE_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_IMPORT", 56, new BenutzerKompetenzKatalogEintrag(142, BenutzerKompetenzGruppe.DATENBANK, "SQLite importieren (Backup einspielen)", null));

	/**
	 * Es werden Rechte zum Export von SQLite-Backups benötigt.
	 */
	public static readonly DATENBANK_SQLITE_EXPORT : BenutzerKompetenz = new BenutzerKompetenz("DATENBANK_SQLITE_EXPORT", 57, new BenutzerKompetenzKatalogEintrag(143, BenutzerKompetenzGruppe.DATENBANK, "SQLite exportieren (Backup erstellen)", null));

	/**
	 * Es werden Rechte zur Durchführung der Laufbahnplanung (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN", 58, new BenutzerKompetenzKatalogEintrag(160, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturjahrgänge anlegen und löschen", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Durchführung der Laufbahnplanung (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN", 59, new BenutzerKompetenzKatalogEintrag(161, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Durchführung der Laufbahnplanung (stufenbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN", 60, new BenutzerKompetenzKatalogEintrag(162, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zum Import von Laufbahnpdaten aus LuPO benötigt.
	 */
	public static readonly OBERSTUFE_LUPO_IMPORT : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_LUPO_IMPORT", 61, new BenutzerKompetenzKatalogEintrag(163, BenutzerKompetenzGruppe.OBERSTUFE, "Laufbahnplanung aus LuPO importieren", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Kursverwaltung - Blocken (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_ALLGEMEIN", 62, new BenutzerKompetenzKatalogEintrag(171, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blocken (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Kursverwaltung - Blocken (stufenbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN", 63, new BenutzerKompetenzKatalogEintrag(172, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blocken (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Aktivierung einer Blockung benötigt.
	 */
	public static readonly OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KURSPLANUNG_BLOCKUNG_AKTIVIEREN", 64, new BenutzerKompetenzKatalogEintrag(173, BenutzerKompetenzGruppe.OBERSTUFE, "Kursplanung - Blockung aktivieren", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zur Bearbeitung einer Klausurplanung benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_AENDERN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_AENDERN", 65, new BenutzerKompetenzKatalogEintrag(181, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ändern", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zum Ansehen einer Klausurplanung (allgemein) benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN", 66, new BenutzerKompetenzKatalogEintrag(182, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ansehen (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Es werden Rechte zum Ansehen einer Klausurplanung (funktionsbezogen) benötigt.
	 */
	public static readonly OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION : BenutzerKompetenz = new BenutzerKompetenz("OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION", 67, new BenutzerKompetenzKatalogEintrag(183, BenutzerKompetenzGruppe.OBERSTUFE, "Klausurplanung ansehen (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Ansehen von Abiturdaten der Oberstufe (allgemein).
	 */
	public static readonly ABITUR_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_ALLGEMEIN", 68, new BenutzerKompetenzKatalogEintrag(191, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ansehen (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Ansehen von Abiturdaten der Oberstufe (funktionsbezogen).
	 */
	public static readonly ABITUR_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_ANSEHEN_FUNKTIONSBEZOGEN", 69, new BenutzerKompetenzKatalogEintrag(192, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ansehen (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Ändern aller Daten zum Abitur (allgemein).
	 */
	public static readonly ABITUR_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_ALLGEMEIN", 70, new BenutzerKompetenzKatalogEintrag(193, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ändern (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Ändern aller Daten zum Abitur (funktionsbezogen).
	 */
	public static readonly ABITUR_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_AENDERN_FUNKTIONSBEZOGEN", 71, new BenutzerKompetenzKatalogEintrag(194, BenutzerKompetenzGruppe.OBERSTUFE, "Abiturdaten ändern (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_ALLGEMEIN", 72, new BenutzerKompetenzKatalogEintrag(195, BenutzerKompetenzGruppe.OBERSTUFE, "Prüfungsergebnisse eingeben (allgemein)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABITUR_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 73, new BenutzerKompetenzKatalogEintrag(196, BenutzerKompetenzGruppe.OBERSTUFE, "Prüfungsergebnisse eingeben (funktionsbezogen)", BenutzerKompetenz.getSchulformenGymOb()));

	/**
	 * Allgemeine Berechtigung zum Verwenden der CardDav API für Addressdaten
	 */
	public static readonly CARDDAV_NUTZEN : BenutzerKompetenz = new BenutzerKompetenz("CARDDAV_NUTZEN", 74, new BenutzerKompetenzKatalogEintrag(201, BenutzerKompetenzGruppe.CARDDAV, "Addressbuch (CardDAV) nutzen", null));

	/**
	 * Es werden Rechte zum Ansehen der Adressdaten eines von Erziehungsberechtigten benötigt
	 */
	public static readonly CARDDAV_ERZIEHER_ANSEHEN : BenutzerKompetenz = new BenutzerKompetenz("CARDDAV_ERZIEHER_ANSEHEN", 75, new BenutzerKompetenzKatalogEintrag(202, BenutzerKompetenzGruppe.CARDDAV, "Erzieherdaten im Addressbuch anzeigen", null));

	/**
	 * Allgemeine Berechtigung zum Verwenden der CalDav API für Kalenderdaten
	 */
	public static readonly CALDAV_NUTZEN : BenutzerKompetenz = new BenutzerKompetenz("CALDAV_NUTZEN", 76, new BenutzerKompetenzKatalogEintrag(301, BenutzerKompetenzGruppe.CALDAV, "Kalender (CalDAV) nutzen", null));

	/**
	 * Berechtigung für den Besitz und das Bearbeiten eines eigenen Kalenders über die CalDav API.
	 */
	public static readonly CALDAV_EIGENER_KALENDER : BenutzerKompetenz = new BenutzerKompetenz("CALDAV_EIGENER_KALENDER", 77, new BenutzerKompetenzKatalogEintrag(302, BenutzerKompetenzGruppe.CALDAV, "Eigener Kalender", null));

	/**
	 * Allgemeine Berechtigung zum Ansehen der generierten Kalenderdaten
	 */
	public static readonly CALDAV_KALENDER_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("CALDAV_KALENDER_ANSEHEN_ALLGEMEIN", 78, new BenutzerKompetenzKatalogEintrag(303, BenutzerKompetenzGruppe.CALDAV, "Kalender Ansehen (Allgemein)", null));

	/**
	 * Funktionsbezogene Berechtigung zum Ansehen generierter Kalender
	 */
	public static readonly CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("CALDAV_KALENDER_ANSEHEN_FUNKTIONSBEZOGEN", 79, new BenutzerKompetenzKatalogEintrag(304, BenutzerKompetenzGruppe.CALDAV, "Kalender Ansehen (Funktionsbezogen)", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_ALLGEMEIN", 80, new BenutzerKompetenzKatalogEintrag(401, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (allgemein)", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten der Sekundarstufe I (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_ANSEHEN_FUNKTIONSBEZOGEN", 81, new BenutzerKompetenzKatalogEintrag(402, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ansehen (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_ALLGEMEIN", 82, new BenutzerKompetenzKatalogEintrag(403, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (allgemein)", null));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten der Sekundarstufe I (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_AENDERN_FUNKTIONSBEZOGEN", 83, new BenutzerKompetenzKatalogEintrag(404, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Abschlussdaten ändern (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_ALLGEMEIN", 84, new BenutzerKompetenzKatalogEintrag(405, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (allgemein)", null));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_SEKI_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 85, new BenutzerKompetenzKatalogEintrag(406, BenutzerKompetenzGruppe.ABSCHLUSS_SEKI, "Prüfungsergebnisse eingeben (funktionsbezogen)", null));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_ALLGEMEIN", 86, new BenutzerKompetenzKatalogEintrag(501, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ansehen von Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_ANSEHEN_FUNKTIONSBEZOGEN", 87, new BenutzerKompetenzKatalogEintrag(502, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ansehen (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_AENDERN_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_ALLGEMEIN", 88, new BenutzerKompetenzKatalogEintrag(503, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Ändern aller Abschlussdaten an einer berufsbildenden Schule (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_AENDERN_FUNKTIONSBEZOGEN", 89, new BenutzerKompetenzKatalogEintrag(504, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Abschlussdaten ändern (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (allgemein).
	 */
	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_ALLGEMEIN", 90, new BenutzerKompetenzKatalogEintrag(505, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (allgemein)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Berechtigung zum Eingeben von Prüfungsergebnissen (funktionsbezogen).
	 */
	public static readonly ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN : BenutzerKompetenz = new BenutzerKompetenz("ABSCHLUSS_BK_EINGABE_ERGEBNISSE_FUNKTIONSBEZOGEN", 91, new BenutzerKompetenzKatalogEintrag(506, BenutzerKompetenzGruppe.ABSCHLUSS_BK, "Prüfungsergebnisse eingeben (funktionsbezogen)", Arrays.asList(Schulform.BK, Schulform.SB)));

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 2;

	/**
	 * Die Daten der Benutzerkompetenz
	 */
	public readonly daten : BenutzerKompetenzKatalogEintrag;

	/**
	 * Eine HashMap zum schnellen Zugriff auf ein Aufzählungsobjekt anhand der ID der Benutzerkompetenz
	 */
	private static readonly _mapID : HashMap<number, BenutzerKompetenz> = new HashMap<number, BenutzerKompetenz>();

	/**
	 * Eine ArrayMap zum schnellen Zugriff auf die Benutzer-Kompetenzen anhand der Benutzer-Kompetenz-Gruppe
	 */
	private static readonly _mapGruppenZuordnung : ArrayMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>> = new ArrayMap<BenutzerKompetenzGruppe, List<BenutzerKompetenz>>(BenutzerKompetenzGruppe.values());

	/**
	 * Erzeugt eine neue Benutzerkompetenz für die Aufzählung.
	 *
	 * @param daten   die Daten der Benutzerkompetenz
	 */
	private constructor(name : string, ordinal : number, daten : BenutzerKompetenzKatalogEintrag) {
		super(name, ordinal);
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
				BenutzerKompetenz._mapGruppenZuordnung.put(g, new ArrayList<BenutzerKompetenz>());
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
	 * @param id   die ID der Benutzerkompetenz
	 *
	 * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : BenutzerKompetenz | null {
		return BenutzerKompetenz.getMapID().get(id);
	}

	/**
	 * Überprüft, ob die Kompetenz mit k_id für die Schulform mit s_id zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Abfrage bezieht
	 * @param schulform  Die Schulform
	 *
	 * @return true, wenn die Kompetenz für die Schulform zulässig ist.
	 */
	public hatSchulform(schuljahr : number, schulform : Schulform | null) : boolean {
		if (schulform === null)
			return false;
		if (this.daten.nurSchulformen !== null)
			return this.daten.nurSchulformen.contains(schulform.name());
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
	 * @param schuljahr   das Schuljahr, auf welches sich die Abfrage bezieht
	 * @param gruppe   die Benutzerkompetenz-Gruppe
	 * @param schulform   die Schulform
	 *
	 * @return die Liste der Benutzerkompetenzen
	 */
	public static getKompetenzenMitSchulform(schuljahr : number, gruppe : BenutzerKompetenzGruppe, schulform : Schulform) : List<BenutzerKompetenz> {
		const l : List<BenutzerKompetenz> | null = new ArrayList<BenutzerKompetenz>();
		const liste : List<BenutzerKompetenz> | null = BenutzerKompetenz.getMapGruppenZuordnung().get(gruppe);
		if (liste === null)
			return l;
		for (const bk of liste) {
			if (bk.hatSchulform(schuljahr, schulform))
				l.add(bk);
		}
		return l;
	}

	private static getSchulformenGymOb() : List<Schulform> {
		const result : List<Schulform> = new ArrayList<Schulform>();
		result.add(Schulform.FW);
		result.add(Schulform.GE);
		result.add(Schulform.GY);
		result.add(Schulform.SG);
		result.add(Schulform.WF);
		return result;
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.benutzer.BenutzerKompetenz';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.benutzer.BenutzerKompetenz', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BenutzerKompetenz>('de.svws_nrw.core.types.benutzer.BenutzerKompetenz');

}

export function cast_de_svws_nrw_core_types_benutzer_BenutzerKompetenz(obj : unknown) : BenutzerKompetenz {
	return obj as BenutzerKompetenz;
}
